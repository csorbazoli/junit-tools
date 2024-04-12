package org.junit.tools.preferences;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FieldDeclarationTest {

    @Parameter(0)
    public String declaration;
    @Parameter(1)
    public String expectedAnnotation;
    @Parameter(2)
    public String expectedModifier;
    @Parameter(3)
    public String expectedType;
    @Parameter(4)
    public String expectedName;
    @Parameter(5)
    public String expectedInitialValue;

    @Parameters(name = "TestCase: {0}")
    public static List<Object[]> parameters() {
	return Arrays.asList(new Object[][] {
		{ "testField", null, null, null, null, null },
		{ "String testField", null, null, "String", "testField", null },
		{ "int testField = 1", null, null, "int", "testField", "1" },
		{ "@Rule public ExpectedException expected = ExpectedException.none()", "Rule", "public", "ExpectedException", "expected",
			"ExpectedException.none()" },
		{ "@SomeAnnotation private String testField", "SomeAnnotation", "private", "String", "testField", null },
		{ "@SomeAnnotation String testField", "SomeAnnotation", null, "String", "testField", null },
		{ "@SomeAnnotation String testField = \"X\"", "SomeAnnotation", null, "String", "testField", "\"X\"" },
		{ "@SomeAnnotation(param=1) String testField = \"X\"", "SomeAnnotation(param=1)", null, "String", "testField", "\"X\"" },
		{ "@SomeAnnotation(param=1) TestObject testField = new TestObject(\"name\", 2)", "SomeAnnotation(param=1)", null, "TestObject", "testField",
			"new TestObject(\"name\", 2)" },
	});
    }

    @Test
    public void testFromConfigString() throws Exception {
	// given
	String item = declaration;
	// when
	Optional<FieldDeclaration> actual = FieldDeclaration.fromConfigString(item);
	// then
	if (expectedName == null) {
	    assertThat(actual).isEmpty();
	} else {
	    assertThat(actual).isNotEmpty();
	    assertThat(actual.get().getAnnotation()).isEqualTo(expectedAnnotation);
	    assertThat(actual.get().getInitialValue()).isEqualTo(expectedInitialValue);
	    assertThat(actual.get().getModifier()).isEqualTo(expectedModifier);
	    assertThat(actual.get().getName()).isEqualTo(expectedName);
	    assertThat(actual.get().getType()).isEqualTo(expectedType);
	}
    }

    @Test
    public void testToConfigString() throws Exception {
	// given
	String item = declaration;
	Optional<FieldDeclaration> underTest = FieldDeclaration.fromConfigString(item);
	if (underTest.isPresent()) {
	    // when
	    String actual = underTest.get().toConfigString();
	    // then
	    assertThat(actual).isEqualTo(item);
	}
    }

    @Test
    public void testToJavaString_shouldBeSameAsConfigString() throws Exception {
	// given
	String item = declaration;
	Optional<FieldDeclaration> underTest = FieldDeclaration.fromConfigString(item);
	if (underTest.isPresent()) {
	    // when
	    String actual = underTest.get().toJavaString();
	    // then
	    assertThat(actual).isEqualTo(item);
	}
    }

}