package org.junit.tools.generator;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
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
import org.junit.tools.generator.model.JUTElements.JUTClassesAndPackages;
import org.junit.tools.generator.model.JUTElements.JUTConstructorsAndMethods;
import org.junit.tools.generator.model.mocks.MockCompilationUnit;
import org.junit.tools.generator.model.mocks.MockJavaProject;
import org.junit.tools.generator.model.tml.Annotation;
import org.junit.tools.generator.model.tml.Attribute;
import org.junit.tools.generator.utils.TestUtils;
import org.junit.tools.preferences.JUTPreferences;

@RunWith(Parameterized.class)
public class TestClassGeneratorIntegratedTest {

    TestClassGenerator underTest = new TestClassGenerator();

    @Parameters(name = "TestCase: {0}")
    public static List<Object[]> getParameters() {
	return Arrays.asList(new Object[][] {
		{ "Baseline", initTestModel() }
	});
    }

    @Parameter(0)
    public String testCase;

    @Parameter(1)
    public org.junit.tools.generator.model.tml.Test tmlTest;

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
	    GeneratorModel model = initGeneratorModel(tmlTest);
	    // when
	    ICompilationUnit actual = underTest.generate(model, Arrays.asList(), new NullProgressMonitor());
	    // then
	    TestUtils.assertTestFileEquals("integrated/" + testCase + ".java", actual.getSource());
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

    private GeneratorModel initGeneratorModel(org.junit.tools.generator.model.tml.Test tmlTest) throws JUTWarning {
	JUTElements jutElements = initJUTElements();
	return new GeneratorModel(jutElements, tmlTest);
    }

    private JUTElements initJUTElements() throws JUTWarning {
	JUTElements ret = new JUTElements();
	ret.setClassesAndPackages(initClassesAndPackages(ret.createClassesAndPackages()));
	ret.setConstructorsAndMethods(new JUTConstructorsAndMethods());
	ret.initProjects(initJavaProject(), initCompilationUnit(), false);
	return ret;
    }

    private ICompilationUnit initCompilationUnit() {
	return MockCompilationUnit.builder()
		.build();
    }

    private IJavaProject initJavaProject() {
	return MockJavaProject.builder()
		.elementName("TestProject")
		.build();
    }

    private JUTClassesAndPackages initClassesAndPackages(JUTClassesAndPackages classesPackages) {
	return classesPackages;
    }

    private static org.junit.tools.generator.model.tml.Test initTestModel() {
	org.junit.tools.generator.model.tml.Test ret = new org.junit.tools.generator.model.tml.Test();
	return ret;
    }

    private Annotation createAnnotation(String name, Attribute... attributes) {
	Annotation ret = new Annotation();
	ret.setName(name);
	Stream.of(attributes)
		.forEach(ret.getAttributes()::add);
	return ret;
    }

    private Attribute createAttribute(String name, String value) {
	Attribute ret = new Attribute();
	ret.setName(name);
	ret.setType("String");
	ret.setValue(value);
	return ret;
    }

}
