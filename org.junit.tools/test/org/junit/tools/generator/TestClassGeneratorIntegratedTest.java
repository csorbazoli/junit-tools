package org.junit.tools.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
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
import org.junit.tools.preferences.JUTPreferences;

@RunWith(Parameterized.class)
public class TestClassGeneratorIntegratedTest {

    TestClassGenerator underTest = new TestClassGenerator();

    @Parameters(name = "TestCase: {0}")
    public static List<Object[]> getParameters() {
	return Arrays.asList(new Object[][] {
		{ "Baseline" }
	});
    }

    @Parameter(0)
    public String testCase;

    @Before
    public void setupTest() {
	JUTPreferences.setJUnitVersion(5);
	JUTPreferences.setGherkinStyleEnabled(true);
	initPluginActivator();
    }

    @Test
    public void testGenerate() throws Exception {
	// given
	try {
	    GeneratorModel model = initGeneratorModel();
	    // when
	    ICompilationUnit actual = underTest.generate(model, Arrays.asList(), new NullProgressMonitor());
	    // then
	    assertThat(actual).isInstanceOf(MockCompilationUnit.class);
	    TestUtils.assertTestFileEquals("integrated/" + testCase + "_source.txt", actual.getSource().trim());
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

    private GeneratorModel initGeneratorModel() throws JUTWarning, IOException {
	TestClassGeneratorTestCase testCaseModel = TestClassGeneratorTestCaseFactory.loadTestCaseModel(testCase);

	org.junit.tools.generator.model.tml.Test tmlTest = TestClassGeneratorTestCaseFactory.initTestModel(testCaseModel);
	JUTElements jutElements = TestClassGeneratorTestCaseFactory.initJUTElements(testCaseModel);
	GeneratorModel ret = new GeneratorModel(jutElements, tmlTest);
	MockMethod methodToCreate = TestClassGeneratorTestCaseFactory.initMockMethod(testCaseModel);
	ret.setMethodsToCreate(Arrays.asList(methodToCreate));
	Map<IMethod, Method> methodMap = new HashMap<>();
	Method tmlMethod = TestClassGeneratorTestCaseFactory.initTmlMethod(testCaseModel);
	methodMap.put(methodToCreate, tmlMethod);
	ret.setMethodMap(methodMap);

	TestClassGeneratorTestCaseFactory.saveTestCaseModel(testCase, testCaseModel);
	return ret;
    }

}
