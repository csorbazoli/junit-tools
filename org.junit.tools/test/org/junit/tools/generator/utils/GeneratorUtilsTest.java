package org.junit.tools.generator.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Test;
import org.junit.tools.base.MethodRef;
import org.junit.tools.preferences.JUTPreferences;

public class GeneratorUtilsTest {

    @Test
    public void testFindMethod_shouldFindExactMatch() throws Exception {
	// given
	IMethod method1 = createMockedMethod("myMethod", "");
	IMethod method2 = createMockedMethod("otherMethod", "");
	Collection<IMethod> methods = Arrays.asList(method1, method2);
	MethodRef methodRef = new MethodRef("myMethod", "(QString;QDemoObject;)QString;");
	// when
	IMethod actual = GeneratorUtils.findMethod(methods, methodRef);
	// then
	assertThat(actual).isEqualTo(method1);
    }

    @Test
    public void testFindMethod_shouldFindPrefixMatch() throws Exception {
	// given
	IMethod method1 = createMockedMethod("testMyMethod_shouldDoSomething", "");
	IMethod method2 = createMockedMethod("testOtherMethod", "");
	Collection<IMethod> methods = Arrays.asList(method1, method2);
	MethodRef methodRef = new MethodRef("testMyMethod", "(QString;QDemoObject;)QString;");
	// when
	IMethod actual = GeneratorUtils.findMethod(methods, methodRef);
	// then
	assertThat(actual).isEqualTo(method1);
    }

    @Test
    public void testFindMethod_shouldFindPrefixMatchReverse() throws Exception {
	// given
	IMethod method1 = createMockedMethod("myMethod", "(QString;QDemoObject;)QString;");
	IMethod method2 = createMockedMethod("otherMethod", "(QString;QDemoObject;)QString;");
	Collection<IMethod> methods = Arrays.asList(method1, method2);
	MethodRef methodRef = new MethodRef("myMethod_shouldDoSomething", "");
	// when
	IMethod actual = GeneratorUtils.findMethod(methods, methodRef);
	// then
	assertThat(actual).isEqualTo(method1);
    }

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

	IField existingField = mock(IField.class);
	when(existingField.getElementName()).thenReturn("someService");
	when(existingField.getTypeSignature()).thenReturn("QSomeSpringService;");
	when(primaryType.getFields()).thenReturn(new IField[] { existingField });
	IAnnotation springAnnotation = mock(IAnnotation.class);
	when(existingField.getAnnotations()).thenReturn(new IAnnotation[] { springAnnotation });
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

	IMethod constructor = createMockedMethod("SomeClass", "");
	when(primaryType.getMethods()).thenReturn(new IMethod[] { constructor });

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
    public void testFindNonPrimitiveFields_shouldFindFieldsWithComplexTypes() throws Exception {
	// given
	ICompilationUnit type = mock(ICompilationUnit.class);
	IType primaryType = mock(IType.class);
	when(type.findPrimaryType()).thenReturn(primaryType);

	when(primaryType.getMethods()).thenReturn(new IMethod[] {});

	IField existingField = mock(IField.class);
	when(existingField.getElementName()).thenReturn("someService");
	when(existingField.getTypeSignature()).thenReturn("QSomeSpringService;");
	when(primaryType.getFields()).thenReturn(new IField[] { existingField });

	JUTPreferences.setInjectionTypeFilter(new String[] { "String" });
	// when
	Map<String, String> actual = GeneratorUtils.findPotentialInjectedFields(type);
	// then
	assertThat(actual).containsEntry("someService", "SomeSpringService");
    }

    @Test
    public void testFindNonPrimitiveFields_shouldIgnoreFieldsWithPrimitiveTypes() throws Exception {
	// given
	ICompilationUnit type = mock(ICompilationUnit.class);
	IType primaryType = mock(IType.class);
	when(type.findPrimaryType()).thenReturn(primaryType);

	when(primaryType.getMethods()).thenReturn(new IMethod[] {});

	IField existingField = mock(IField.class);
	when(existingField.getElementName()).thenReturn("someValue");
	when(existingField.getTypeSignature()).thenReturn("QString;");
	when(primaryType.getFields()).thenReturn(new IField[] { existingField });

	JUTPreferences.setInjectionTypeFilter(new String[] { "String" });
	// when
	Map<String, String> actual = GeneratorUtils.findPotentialInjectedFields(type);
	// then
	assertThat(actual).isEmpty();
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

    @Test
    public void testDetermineHttpMethod_shouldReturnHttpMethodAccordingToXMappingAnnotation() throws Exception {
	// given
	IMethod method = createMockedMethod("getSomething", "");
	IAnnotation mappingAnnotation = mock(IAnnotation.class);
	when(method.getAnnotations()).thenReturn(new IAnnotation[] { mappingAnnotation });
	when(mappingAnnotation.getElementName()).thenReturn("GetMapping");
	// when
	String actual = GeneratorUtils.determineHttpMethod(method);
	// then
	assertThat(actual).isEqualTo("get");
    }

    @Test
    public void testDetermineHttpMethod_shouldReturnHttpMethodAccordingToMappingAnnotationsMethodAttribute() throws Exception {
	// given
	IMethod method = createMockedMethod("postSomething", "QString;");
	IAnnotation mappingAnnotation = mock(IAnnotation.class);
	when(method.getAnnotations()).thenReturn(new IAnnotation[] { mappingAnnotation });
	when(mappingAnnotation.getElementName()).thenReturn("RequestMapping");
	IMemberValuePair attribute = mock(IMemberValuePair.class);
	when(mappingAnnotation.getMemberValuePairs()).thenReturn(new IMemberValuePair[] { attribute });
	when(attribute.getMemberName()).thenReturn("method");
	when(attribute.getValue()).thenReturn("RequestMethod.POST");
	// when
	String actual = GeneratorUtils.determineHttpMethod(method);
	// then
	assertThat(actual).isEqualTo("post");
    }

    @Test
    public void testDetermineRequestPath_shouldReturnPathAttributeIfGiven() throws Exception {
	// given
	IMethod method = createMockedMethod("getSomething", "QString;");
	IAnnotation mappingAnnotation = mock(IAnnotation.class);
	when(method.getAnnotations()).thenReturn(new IAnnotation[] { mappingAnnotation });
	when(mappingAnnotation.getElementName()).thenReturn("GetMapping");
	IMemberValuePair attribute = mock(IMemberValuePair.class);
	when(mappingAnnotation.getMemberValuePairs()).thenReturn(new IMemberValuePair[] { attribute });
	when(attribute.getMemberName()).thenReturn("path");
	when(attribute.getValue()).thenReturn("/test");
	// when
	String actual = GeneratorUtils.determineRequestPath(method);
	// then
	assertThat(actual).isEqualTo("/test");
    }

    @Test
    public void testDetermineRequestPath_shouldReturnValueAttributeIfPathNotGiven() throws Exception {
	// given
	IMethod method = createMockedMethod("getSomething", "QString;");
	IAnnotation mappingAnnotation = mock(IAnnotation.class);
	when(method.getAnnotations()).thenReturn(new IAnnotation[] { mappingAnnotation });
	when(mappingAnnotation.getElementName()).thenReturn("GetMapping");
	IMemberValuePair attribute = mock(IMemberValuePair.class);
	when(mappingAnnotation.getMemberValuePairs()).thenReturn(new IMemberValuePair[] { attribute });
	when(attribute.getMemberName()).thenReturn("value");
	when(attribute.getValue()).thenReturn("/test");
	// when
	String actual = GeneratorUtils.determineRequestPath(method);
	// then
	assertThat(actual).isEqualTo("/test");
    }

    @Test
    public void testDetermineRequestPath_shouldReturnEmptyStringIfPathAttributeIsNotSpecified() throws Exception {
	// given
	IMethod method = createMockedMethod("getSomething", "");
	IAnnotation mappingAnnotation = mock(IAnnotation.class);
	when(method.getAnnotations()).thenReturn(new IAnnotation[] { mappingAnnotation });
	when(mappingAnnotation.getElementName()).thenReturn("GetMapping");
	when(mappingAnnotation.getMemberValuePairs()).thenReturn(new IMemberValuePair[] {});
	// when
	String actual = GeneratorUtils.determineRequestPath(method);
	// then
	assertThat(actual).isEmpty();
    }

    // helper methods
    private IMethod createMockedMethod(String name, String signature) throws JavaModelException {
	IMethod ret = mock(IMethod.class);
	when(ret.getElementName()).thenReturn(name);
	when(ret.getSignature()).thenReturn(signature);
	return ret;
    }

}
