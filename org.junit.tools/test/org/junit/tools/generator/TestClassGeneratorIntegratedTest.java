package org.junit.tools.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.text.PreferencesAdapter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.tools.Activator;
import org.junit.tools.base.JUTWarning;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.JUTElements;
import org.junit.tools.generator.model.mocks.MockCompilationUnit;
import org.junit.tools.generator.model.mocks.MockMethod;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.utils.TestUtils;
import org.junit.tools.preferences.JUTPreferenceInitializer;
import org.junit.tools.preferences.JUTPreferences;

@RunWith(Parameterized.class)
public class TestClassGeneratorIntegratedTest {

    TestClassGenerator underTest = new TestClassGenerator();

    @Parameters(name = "TestCase: {0}")
    public static List<Object[]> getParameters() {
	return Arrays.asList(new Object[][] {
		{ "Baseline" }
		// only static methods
		// junit4 vs junit5
		// easymock vs mockito
		// repeating test method
		// gherkin switched off
		// Spring MVC
	});
    }

    @Parameter(0)
    public String testCase;

    @Before
    public void setupTest() {
	initPluginActivator();
	JUTPreferenceInitializer.initDefaultPreferences();
    }

    @Test
    public void testGenerate() throws Exception {
	// given
	try {
	    GeneratorModel model = initGeneratorModel();
	    // when
	    IMethod actual = underTest.generate(model, Arrays.asList(), new NullProgressMonitor());
	    // then
	    assertThat(actual).isInstanceOf(MockMethod.class);
	    assertThat(actual.getCompilationUnit()).isInstanceOf(MockCompilationUnit.class);
	    TestUtils.assertTestFileEquals("integrated/" + testCase + "_source.txt", actual.getCompilationUnit().getSource().trim());
	} catch (Exception e) {
	    e.printStackTrace();
	    fail(e.getMessage());
	}
    }

    // helper methods
    private void initPluginActivator() {
	Activator activator = new Activator() {
	    @Override
	    public IPreferenceStore getPreferenceStore() {
		return initPreferenceStore();
	    }
	};
	activator.init();
    }

    protected IPreferenceStore initPreferenceStore() {
	PreferencesAdapter ret = new PreferencesAdapter();
	return ret;
    }

    private GeneratorModel initGeneratorModel() throws JUTWarning, IOException, JavaModelException {
	TestClassGeneratorTestCase testCaseModel = TestClassGeneratorTestCaseFactory.loadTestCaseModel(testCase);

	TestJUTPreferences preferences = testCaseModel.getPreferences();
	if (preferences == null) {
	    preferences = new TestJUTPreferences();
	    preferences.setAdditionalFields(new String[] { "@Rule ExpectedException expected = ExpectedExcepton.none" });
	    preferences.setAdditionalImports(new String[] { "java.util.List", "static java.util.Arrays.asList" });
	    preferences.setAssertJEnabled(true);
	    preferences.setGherkinStyleEnabled(true);
	    preferences.setJUnitVersion(5);
	    preferences.setMockFramework(JUTPreferences.MOCKFW_MOCKITO);
	    preferences.setRepeatingTestMethodsEnabled(true);
	    preferences.setReplayAllVerifyAllEnabled(true);
	}
	JUTPreferences.setAdditionalFields(preferences.getAdditionalFields());
	JUTPreferences.setAdditionalImports(preferences.getAdditionalImports());
	JUTPreferences.setAssertJEnabled(preferences.isAssertJEnabled());
	JUTPreferences.setGherkinStyleEnabled(preferences.isGherkinStyleEnabled());
	JUTPreferences.setJUnitVersion(preferences.getJUnitVersion());
	JUTPreferences.setMockFramework(preferences.getMockFramework());
	JUTPreferences.setRepeatingTestMethodsEnabled(preferences.isRepeatingTestMethodsEnabled());
	JUTPreferences.setReplayAllVerifyAllEnabled(preferences.isReplayAllVerifyAllEnabled());
	JUTPreferences.setShowSettingsBeforeGenerate(false);

	org.junit.tools.generator.model.tml.Test tmlTest = TestClassGeneratorTestCaseFactory.initTestModel(testCaseModel);
	JUTElements jutElements = TestClassGeneratorTestCaseFactory.initJUTElements(testCaseModel);
	GeneratorModel ret = new GeneratorModel(jutElements, tmlTest);

	MockMethod methodToCreate = TestClassGeneratorTestCaseFactory.initMockMethod(testCaseModel);
	ret.setMethodsToCreate(Arrays.asList(methodToCreate));
	Map<IMethod, Method> methodMap = new HashMap<>();
	Method tmlMethod = TestClassGeneratorTestCaseFactory.initTmlMethod(testCaseModel);
	methodMap.put(methodToCreate, tmlMethod);
	ret.setMethodMap(methodMap);
	if (tmlMethod.getTestCase().isEmpty()) {
	    TestCasesGenerator tcg = new TestCasesGenerator();
	    tcg.generateTestCases(ret);
	}

	TestClassGeneratorTestCaseFactory.saveTestCaseModel(testCase, testCaseModel);
	return ret;
    }

}
