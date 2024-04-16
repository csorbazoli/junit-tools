package org.junit.tools.preferences;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

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
		{ "List<String> testList = Arrays.asList(\"test\")", null, null, "List<String>", "testList", "Arrays.asList(\"test\")" },
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
	FieldDeclaration actual = FieldDeclaration.fromConfigString(item);
	// then
	if (expectedName == null) {
	    assertThat(actual).isNull();
	} else {
	    assertThat(actual).isNotNull();
	    assertThat(actual.getAnnotation()).isEqualTo(expectedAnnotation);
	    assertThat(actual.getInitialValue()).isEqualTo(expectedInitialValue);
	    assertThat(actual.getModifier()).isEqualTo(expectedModifier);
	    assertThat(actual.getName()).isEqualTo(expectedName);
	    assertThat(actual.getType()).isEqualTo(expectedType);
	}
    }

    @Test
    public void testToConfigString() throws Exception {
	// given
	String item = declaration;
	FieldDeclaration underTest = FieldDeclaration.fromConfigString(item);
	if (underTest != null) {
	    // when
	    String actual = underTest.toConfigString();
	    // then
	    assertThat(actual).isEqualTo(item);
	}
    }

    @Test
    public void testToJavaString_shouldBeSameAsConfigString() throws Exception {
	// given
	String item = declaration;
	FieldDeclaration underTest = FieldDeclaration.fromConfigString(item);
	if (underTest != null) {
	    // when
	    String actual = underTest.toJavaString();
	    // then
	    assertThat(actual).isEqualTo(item);
	}
    }

}