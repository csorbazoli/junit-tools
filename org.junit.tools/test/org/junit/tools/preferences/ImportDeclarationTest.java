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
	Optional<ImportDeclaration> actual = ImportDeclaration.fromConfigString(item);
	// then
	if (expectedPackageName == null) {
	    assertThat(actual).isEmpty();
	} else {
	    assertThat(actual).isPresent();
	    assertThat(actual.get().isStatic()).isEqualTo(expectedIsStatic);
	    assertThat(actual.get().getPackageName()).isEqualTo(expectedPackageName);
	}
    }

    @Test
    public void testToConfigString() throws Exception {
	// given
	String item = declaration;
	Optional<ImportDeclaration> underTest = ImportDeclaration.fromConfigString(item);
	if (underTest.isPresent()) {
	    // when
	    String actual = underTest.get().toConfigString();
	    // then
	    assertThat(actual).isEqualTo(item);
	}
    }

}