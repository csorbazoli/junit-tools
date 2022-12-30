package org.junit.tools.preferences;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class JUTPreferencesTest {

    @Test
    public void testConvertToArray() {
	// given
	// when
	String[] actual = JUTPreferences.convertToArray("A;B;C;");
	// then
	assertArrayEquals(new String[] { "A", "B", "C" }, actual);
    }

    @Test
    public void testConvertFromArray() {
	// given
	// when
	String actual = JUTPreferences.convertFromArray(new String[] { "A", "B", "C" });
	// then
	assertEquals("A;B;C;", actual);
    }

    @Test
    public void testConvertToMap() {
	// given
	// when
	Map<String, String> actual = JUTPreferences.convertToMap("A=1;B=TestValueFactory.fillFields(new ${Class}());");
	// then
	assertEquals("1", actual.get("A"));
	assertEquals("TestValueFactory.fillFields(new ${Class}())", actual.get("B"));
    }

    @Test
    public void testConvertFromMap() {
	// given
	Map<String, String> map = new TreeMap<>();
	map.put("A", "1");
	map.put("B", "TestValueFactory.fillFields(new ${Class}())");

	// when
	String actual = JUTPreferences.convertFromMap(map);
	// then
	assertEquals("A=1;B=TestValueFactory.fillFields(new ${Class}())", actual);
    }

}
