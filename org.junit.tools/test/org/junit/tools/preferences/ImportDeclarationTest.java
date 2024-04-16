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
public class ImportDeclarationTest {

    @Parameter(0)
    public String declaration;
    @Parameter(1)
    public boolean expectedIsStatic;
    @Parameter(2)
    public String expectedPackageName;

    @Parameters(name = "TestCase: {0}")
    public static List<Object[]> parameters() {
	return Arrays.asList(new Object[][] {
		{ "static org.assertj.core.api.Assertions.assertThat", true, "org.assertj.core.api.Assertions.assertThat" },
		{ "java.util.List", false, "java.util.List" },
		{ "java.util.*", false, "java.util.*" },
		{ "", false, null },
	});
    }

    @Test
    public void testFromConfigString() throws Exception {
	// given
	String item = declaration;
	// when
	ImportDeclaration actual = ImportDeclaration.fromConfigString(item);
	// then
	if (expectedPackageName == null) {
	    assertThat(actual).isNull();
	} else {
	    assertThat(actual).isNotNull();
	    assertThat(actual.isStatic()).isEqualTo(expectedIsStatic);
	    assertThat(actual.getPackageName()).isEqualTo(expectedPackageName);
	}
    }

    @Test
    public void testToConfigString() throws Exception {
	// given
	String item = declaration;
	ImportDeclaration underTest = ImportDeclaration.fromConfigString(item);
	if (underTest != null) {
	    // when
	    String actual = underTest.toConfigString();
	    // then
	    assertThat(actual).isEqualTo(item);
	}
    }

}