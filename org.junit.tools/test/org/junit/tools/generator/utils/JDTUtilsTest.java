package org.junit.tools.generator.utils;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

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
	String actual = JDTUtils.createInitValue("String", "param");
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
	String actual = JDTUtils.createInitValue("char", "param");
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
	String actual = JDTUtils.createInitValue("TestObject", "param");
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
	String actual = JDTUtils.createInitValue("TestObject", "param");
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
	String actual = JDTUtils.createInitValue("TestObject", "param");
	// then
	assertEquals("null", actual);
    }

}
