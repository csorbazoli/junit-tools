package org.junit.tools.generator.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.junit.Test;
import org.junit.tools.preferences.JUTPreferences;

public class JDTUtilsTest {

    @Test
    public void testCreateInitValue_valueExpression() {
	// given
	Map<String, String> valueMap = new HashMap<>();
	valueMap.put("String", "\"Test${Name}\"");
	JUTPreferences.setDefaultValuesByType(valueMap);
	// when
	String actual = JDTUtils.createInitValue("String", "param", true);
	// then
	assertEquals("\"TestParam\"", actual);
    }

    @Test
    public void testCreateInitValue_simpleValue() {
	// given
	Map<String, String> valueMap = new HashMap<>();
	valueMap.put("char", "'c'");
	JUTPreferences.setDefaultValuesByType(valueMap);
	// when
	String actual = JDTUtils.createInitValue("char", "param", true);
	// then
	assertEquals("'c'", actual);
    }

    @Test
    public void testCreateInitValue_JavaBeans() {
	// given
	Map<String, String> valueMap = new HashMap<>();
	JUTPreferences.setDefaultValuesByType(valueMap);
	JUTPreferences.setDefaultValueForJavaBeans("TestValueFactory.fillFields(new ${Class}())");
	// when
	String actual = JDTUtils.createInitValue("TestObject", "param", true);
	// then
	assertEquals("TestValueFactory.fillFields(new TestObject())", actual);
    }

    @Test
    public void testCreateInitValue_fallback() {
	// given
	Map<String, String> valueMap = new HashMap<>();
	JUTPreferences.setDefaultValuesByType(valueMap);
	JUTPreferences.setDefaultValueForJavaBeans("");
	JUTPreferences.setDefaultValueFallback("Mockito.mock(${Class}.class)");
	// when
	String actual = JDTUtils.createInitValue("TestObject", "param", true);
	// then
	assertEquals("Mockito.mock(TestObject.class)", actual);
    }

    @Test
    public void testCreateInitValue_noDefaultValues() {
	// given
	Map<String, String> valueMap = new HashMap<>();
	JUTPreferences.setDefaultValuesByType(valueMap);
	JUTPreferences.setDefaultValueForJavaBeans("");
	JUTPreferences.setDefaultValueFallback("");
	// when
	String actual = JDTUtils.createInitValue("TestObject", "param", true);
	// then
	assertEquals("null", actual);
    }

    @Test
    public void testHasDefaultContructor() {
	// given
	IType type = mock(IType.class);
	when(type.getElementName()).thenReturn("TestClass");
	IMethod constructorMethod = mock(IMethod.class);
	when(type.getMethod("TestClass", null)).thenReturn(constructorMethod);
	// when
	boolean actual = JDTUtils.hasDefaultConstructor(type);
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testIsStaticMethodsh_shouldReturnTrueIfAllMethodsHaveStaticModifier() throws Exception {
	// given
	IMethod method1 = mock(IMethod.class);
	when(method1.getFlags()).thenReturn(Flags.AccStatic | Flags.AccPublic);
	IMethod method2 = mock(IMethod.class);
	when(method2.getFlags()).thenReturn(Flags.AccStatic | Flags.AccPublic);
	List<IMethod> methods = Arrays.asList(method1, method2);
	// when
	boolean actual = JDTUtils.isStaticMethods(methods);
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testIsStaticMethodsh_shouldReturnFalseIfAnyMethodHasNoStaticModifier() throws Exception {
	// given
	IMethod method1 = mock(IMethod.class);
	when(method1.getFlags()).thenReturn(Flags.AccStatic | Flags.AccPublic);
	IMethod method2 = mock(IMethod.class);
	when(method2.getFlags()).thenReturn(Flags.AccPublic);
	List<IMethod> methods = Arrays.asList(method1, method2);
	// when
	boolean actual = JDTUtils.isStaticMethods(methods);
	// then
	assertThat(actual).isFalse();
    }

}
