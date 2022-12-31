package org.junit.tools.generator.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.junit.Test;
import org.junit.tools.preferences.JUTPreferences;

public class GeneratorUtilsTest {

    @Test
    public void testFindField() throws Exception {
	// given
	IType type = mock(IType.class);
	String name = "underTest";
	IField exitingField = mock(IField.class);
	when(exitingField.getElementName()).thenReturn(name);
	when(type.getFields()).thenReturn(new IField[] { exitingField });
	// when
	IField actual = GeneratorUtils.findField(type, name);
	// then
	assertThat(actual).isEqualTo(exitingField);
    }

    @Test
    public void testFindInjectedFields_shouldFindAutowiredFields() throws Exception {
	// given
	ICompilationUnit type = mock(ICompilationUnit.class);
	IType primaryType = mock(IType.class);
	when(type.findPrimaryType()).thenReturn(primaryType);

	when(primaryType.getMethods()).thenReturn(new IMethod[] {});

	IField exitingField = mock(IField.class);
	when(exitingField.getElementName()).thenReturn("someService");
	when(exitingField.getTypeSignature()).thenReturn("QSomeSpringService;");
	when(primaryType.getFields()).thenReturn(new IField[] { exitingField });
	IAnnotation springAnnotation = mock(IAnnotation.class);
	when(exitingField.getAnnotations()).thenReturn(new IAnnotation[] { springAnnotation });
	when(springAnnotation.getElementName()).thenReturn("Autowired");
	// when
	Map<String, String> actual = GeneratorUtils.findInjectedFields(type);
	// then
	assertThat(actual).containsEntry("someService", "SomeSpringService");
    }

    @Test
    public void testFindInjectedFields_shouldFindConstructorInjections() throws Exception {
	// given
	ICompilationUnit type = mock(ICompilationUnit.class);
	IType primaryType = mock(IType.class);
	when(primaryType.getElementName()).thenReturn("SomeClass");

	when(primaryType.getFields()).thenReturn(new IField[] {});

	IMethod constructor = mock(IMethod.class);
	when(primaryType.getMethods()).thenReturn(new IMethod[] { constructor });
	when(constructor.getElementName()).thenReturn("SomeClass");

	ILocalVariable param = mock(ILocalVariable.class);
	when(param.getElementName()).thenReturn("someService");
	when(param.getTypeSignature()).thenReturn("QSomeSpringService;");
	when(constructor.getParameters()).thenReturn(new ILocalVariable[] { param });
	when(type.findPrimaryType()).thenReturn(primaryType);
	// when
	Map<String, String> actual = GeneratorUtils.findInjectedFields(type);
	// then
	assertThat(actual).containsEntry("someService", "SomeSpringService");
    }

    @Test
    public void testHasSpringAnnotation_shouldReturnTrueOnAnySpringAnnotatedClass() throws Exception {
	// given
	ICompilationUnit baseClass = mock(ICompilationUnit.class);
	IType testType = mock(IType.class);
	when(baseClass.getTypes()).thenReturn(new IType[] { testType });
	IAnnotation springAnnotation = mock(IAnnotation.class);
	when(testType.getAnnotations()).thenReturn(new IAnnotation[] { springAnnotation });
	when(springAnnotation.getElementName()).thenReturn("RestController");

	JUTPreferences.setRelevantSpringAnnotations(new String[] { "Controller", "RestController", "Service", "Component" });
	// when
	boolean actual = GeneratorUtils.hasSpringAnnotation(baseClass);
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testIsSpringController_shouldReturnTrueOnAnyControllerAnnotatedClass() throws Exception {
	// given
	ICompilationUnit baseClass = mock(ICompilationUnit.class);
	IType testType = mock(IType.class);
	when(baseClass.getTypes()).thenReturn(new IType[] { testType });
	IAnnotation springAnnotation = mock(IAnnotation.class);
	when(testType.getAnnotations()).thenReturn(new IAnnotation[] { springAnnotation });
	when(springAnnotation.getElementName()).thenReturn("RestController");
	// when
	boolean actual = GeneratorUtils.isSpringController(baseClass);
	// then
	assertThat(actual).isTrue();
    }

}
