package org.junit.tools.preferences;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class ValueMappingTest {

    private DefaultValueMapper mapper = DefaultValueMapper.builder().build();

    @Test
    public void testNewValueMapping() {
	// given
	// when
	ValueMapping actual = new ValueMapping("String", "\"Test${Name}\"", mapper);
	// then
	assertThat(actual).isNotNull();
    }

    @Test
    public void testIsMatching_simpleType() {
	// given
	ValueMapping underTest = new ValueMapping("String", "\"Test${Name}\"", mapper);
	// when
	boolean actual = underTest.isMatchingType("String");
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testIsMatching_array() {
	// given
	ValueMapping underTest = new ValueMapping("String", "\"Test${Name}\"", mapper);
	// when
	boolean actual = underTest.isMatchingType("String[]");
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testIsMatching_mistmatch() {
	// given
	ValueMapping underTest = new ValueMapping("String", "\"Test${Name}\"", mapper);
	// when
	boolean actual = underTest.isMatchingType("Integer");
	// then
	assertThat(actual).isFalse();
    }

    @Test
    public void testIsMatching_genericType() {
	// given
	ValueMapping underTest = new ValueMapping("Optional<T>", "\"Optional.of(${T})", mapper);
	// when
	boolean actual = underTest.isMatchingType("Optional<String>");
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testGetValueForNamedVariable_withNameReference() {
	// given
	ValueMapping underTest = new ValueMapping("String", "\"Test${Name}\"", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("String", "userName");
	// then
	assertThat(actual).isEqualTo("\"TestUserName\"");
    }

    @Test
    public void testGetValueForNamedVariable_withLowerCaseNameReference() {
	// given
	ValueMapping underTest = new ValueMapping("String", "\"${name}\"", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("String", "userName");
	// then
	assertThat(actual).isEqualTo("\"userName\"");
    }

    @Test
    public void testGetValueForNamedVariable_withTypeReference() {
	// given
	ValueMapping underTest = new ValueMapping("String", "\"Test${Class}\"", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("String", "userName");
	// then
	assertThat(actual).isEqualTo("\"TestString\"");
    }

    @Test
    public void testGetValueForNamedVariable_withlowerCaseTypeReference() {
	// given
	ValueMapping underTest = new ValueMapping("String", "\"${class}Value\"", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("String", "userName");
	// then
	assertThat(actual).isEqualTo("\"stringValue\"");
    }

    @Test
    public void testGetValueForNamedVariable_genericType1() {
	// given
	mapper = DefaultValueMapper.builder()
		.appendRule("String", "\"Test${Name}\"")
		.build();
	ValueMapping underTest = new ValueMapping("Optional<T>", "Optional.of(${T})", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("Optional<String>", "userName");
	// then
	assertThat(actual).isEqualTo("Optional.of(\"TestUserName\")");
    }

    @Test
    public void testGetValueForNamedVariable_genericType2() {
	// given
	mapper = DefaultValueMapper.builder()
		.appendRule("Integer", "123")
		.appendRule("String", "\"Test${Name}\"")
		.build();
	ValueMapping underTest = new ValueMapping("Map<T, U>", "Collections.singletonMap(${T}, ${U})", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("Map<String, Integer>", "usersAge");
	// then
	assertThat(actual).isEqualTo("Collections.singletonMap(\"TestUsersAge\", 123)");
    }

    @Test
    public void testGetValueForNamedVariable_genericTypeNested() {
	// given
	mapper = DefaultValueMapper.builder()
		.appendRule("Integer", "123")
		.appendRule("String", "\"Test${Name}\"")
		.appendRule("List<T>", "Arrays.asList(${T})")
		.build();
	ValueMapping underTest = new ValueMapping("Map<T, U>", "Collections.singletonMap(${T}, ${U})", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("Map<String, List<Integer>>", "userLists");
	// then
	assertThat(actual).isEqualTo("Collections.singletonMap(\"TestUserLists\", Arrays.asList(123))");
    }

    @Test
    public void testGetValueForNamedVariable_array() {
	// given
	mapper = DefaultValueMapper.builder()
		.appendRule("Integer", "123")
		.build();
	ValueMapping underTest = new ValueMapping("String", "\"Test${Name}\"", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("String[]", "users");
	// then
	assertThat(actual).isEqualTo("new String[]{\"TestUsers\"}");
    }

    @Test
    @Ignore // this wouldn't work, only Collections.singletonList(new String[]{"test"})
    public void testGetValueForNamedVariable_genericTypeArray() {
	// given
	mapper = DefaultValueMapper.builder()
		.appendRule("Integer", "123")
		.appendRule("String", "\"Test${Name}\"")
		.build();
	ValueMapping underTest = new ValueMapping("List<T>", "Arrays.asList(${T})", mapper);
	// when
	String actual = underTest.getValueForNamedVariable("List<String[]>", "userLists");
	// then
	assertThat(actual).isEqualTo("Arrays.asList(new String[]{\"TestUserLists\"})");
    }

}
