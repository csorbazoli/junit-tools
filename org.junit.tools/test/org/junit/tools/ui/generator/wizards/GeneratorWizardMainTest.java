package org.junit.tools.ui.generator.wizards;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.tools.base.JUTWarning;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.JUTElements;
import org.junit.tools.generator.model.JUTElements.JUTProjects;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.utils.TestUtils;
import org.junit.tools.generator.utils.TestValueFactory;
import org.junit.tools.ui.generator.wizards.pages.GeneratorWizardMainPage;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GeneratorWizardMainTest {

    private GeneratorWizardMain underTest;

    @Mock
    private GeneratorModel model;
    @Mock
    private GeneratorWizardMainPage generatorWizardMainPage;
    @Mock
    private ICompilationUnit testBase;
    @Mock
    private ICompilationUnit testClass;
    @Mock
    private IJavaProject baseProject;
    @Mock
    private IJavaProject testProject;

    @Before
    public void setupTest() throws Exception {
	try {
	    JUTElements jutElements = new JUTElements();
	    jutElements.setClassesAndPackages(jutElements.createClassesAndPackages());
	    JUTProjects projects = new JUTProjects();
	    projects.setBaseProject(baseProject);
	    projects.setTestProject(testProject);
	    jutElements.setProjects(projects);
	    when(model.getJUTElements()).thenReturn(jutElements);
	    underTest = new GeneratorWizardMain(model, generatorWizardMainPage);
	} catch (JUTWarning e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testUpdateModelMethod() throws Exception {
	// given
	IMethod method = mock(IMethod.class);
	when(method.getParameters()).thenReturn(new ILocalVariable[] {});
	Method tmlMethod = TestValueFactory.fillFields(new Method());
	// when
	underTest.updateModelMethod(method, tmlMethod);
	// then
	assertThat(TestUtils.objectToJson(tmlMethod)).isEqualTo(TestUtils.readTestFile("testmodel/Method_updated.json"));
    }
}