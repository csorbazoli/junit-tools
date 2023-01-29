package org.junit.tools.preferences;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.tools.preferences.DefaultValueMapper.DefaultValueMapperBuilder;

public class DefaultValueMapperTest {

    @Test
    public void testBuilder() {
	// given
	// when
	DefaultValueMapperBuilder actual = DefaultValueMapper.builder();
	// then
	assertThat(actual).isNotNull();
    }

    @Test
    public void testBuilderAppendRules() {
	// given
	DefaultValueMapperBuilder builder = DefaultValueMapper.builder();
	Map<String, String> typeToValueMappings = new HashMap<>();
	typeToValueMappings.put("String", "Test${Name}");

	// when
	DefaultValueMapperBuilder actual = builder.appendRules(typeToValueMappings);
	// then
	assertThat(actual).isEqualTo(builder);
    }

    @Test
    public void testBuilderAppendRule() {
	// given
	DefaultValueMapperBuilder builder = DefaultValueMapper.builder();
	String defaultValue = "null";
	// when
	DefaultValueMapperBuilder actual = builder.appendRule(defaultValue);
	// then
	assertThat(actual).isEqualTo(builder);
    }

    @Test
    public void testBuilderBuild() {
	// given
	DefaultValueMapperBuilder builder = DefaultValueMapper.builder();
	// when
	DefaultValueMapper actual = builder.build();
	// then
	assertThat(actual).isNotNull();
    }

    @Test
    public void testGetDefaultValueForParameter() {
	// given
	DefaultValueMapper underTest = DefaultValueMapper.builder()
		.appendRule("Integer", "123")
		.appendRule("String", "\"Test${Name}\"")
		.build();
	// when
	String actual = underTest.getDefaultValueForParameter("String", "userName");
	// then
	assertThat(actual).isEqualTo("\"TestUserName\"");
    }

    @Test
    public void testGetDefaultValueForParameter_shouldReturnNullIfNoneMatch() {
	// given
	DefaultValueMapper underTest = DefaultValueMapper.builder()
		.appendRule("Integer", "123")
		.appendRule("String", "\"Test${Name}\"")
		.build();
	// when
	String actual = underTest.getDefaultValueForParameter("Object", "userName");
	// then
	assertThat(actual).isEqualTo("null");
    }

}
