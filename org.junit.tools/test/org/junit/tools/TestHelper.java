package org.junit.tools;

import java.util.HashMap;
import java.util.Map;

import org.junit.tools.preferences.JUTPreferences;

public class TestHelper {

    public static void initDefaultValueMapping() {
	Map<String, String> valueMap = new HashMap<>();
	valueMap.put("String", "\"Test${Name}\"");
	valueMap.put("int", "123");
	JUTPreferences.setDefaultValuesByType(valueMap);
	JUTPreferences.setDefaultValueForJavaBeans("TestValueFactory.fillFields(new ${Class}())");
	JUTPreferences.setDefaultValueFallback("Mockito.mock(${Class}.class)");
    }
}
