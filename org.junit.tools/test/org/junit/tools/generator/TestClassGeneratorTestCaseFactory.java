package org.junit.tools.generator;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.tools.base.JUTWarning;
import org.junit.tools.generator.model.JUTElements;
import org.junit.tools.generator.model.JUTElements.JUTClassesAndPackages;
import org.junit.tools.generator.model.JUTElements.JUTConstructorsAndMethods;
import org.junit.tools.generator.model.mocks.MockCompilationUnit;
import org.junit.tools.generator.model.mocks.MockJavaProject;
import org.junit.tools.generator.model.mocks.MockMethod;
import org.junit.tools.generator.model.mocks.MockPackageFragment;
import org.junit.tools.generator.model.mocks.MockPackageFragmentRoot;
import org.junit.tools.generator.model.mocks.MockPath;
import org.junit.tools.generator.model.mocks.MockProject;
import org.junit.tools.generator.model.mocks.MockType;
import org.junit.tools.generator.model.tml.Annotation;
import org.junit.tools.generator.model.tml.Attribute;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.Settings;
import org.junit.tools.generator.model.tml.Test;
import org.junit.tools.generator.utils.TestUtils;

public class TestClassGeneratorTestCaseFactory {

    private TestClassGeneratorTestCaseFactory() {
	// private constructor
    }

    public static void saveTestCaseModel(String testCase, TestClassGeneratorTestCase testCaseModel) throws IOException {
	TestUtils.setOverwriteTestResources(true);
	TestUtils.assertTestFileEquals("integrated/" + testCase + "_testCase.json", TestUtils.objectToJson(testCaseModel));
	TestUtils.setOverwriteTestResources(false);
    }

    public static TestClassGeneratorTestCase loadTestCaseModel(String testCase) {
	TestClassGeneratorTestCase testCaseModel = null;
	try {
	    testCaseModel = TestUtils.readObjectFromJsonFile("integrated/" + testCase + "_testCase.json",
		    TestClassGeneratorTestCase.class);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (testCaseModel == null) {
	    testCaseModel = new TestClassGeneratorTestCase();
	}
	return testCaseModel;
    }

    public static Method initTmlMethod(TestClassGeneratorTestCase testCaseModel) {
	Method ret = testCaseModel.getTmlMethod();
	if (ret == null) {
	    ret = new Method();
	    ret.setName("testSomeMethod");
	    testCaseModel.setTmlMethod(ret);
	}
	return ret;
    }

    public static MockMethod initMockMethod(TestClassGeneratorTestCase testCaseModel) {
	MockMethod ret = testCaseModel.getTestMethod();
	if (ret == null) {
	    ret = MockMethod.builder()
		    .elementName("testSomeMethod")
		    .build();
	    testCaseModel.setTestMethod(ret);
	}
	return ret;
    }

    public static JUTElements initJUTElements(TestClassGeneratorTestCase testCaseModel) throws JUTWarning {
	JUTElements ret = new JUTElements();
	ret.setClassesAndPackages(initClassesAndPackages(ret.createClassesAndPackages(), testCaseModel));
	ret.setConstructorsAndMethods(new JUTConstructorsAndMethods());
	ret.initProjects(initJavaProject(testCaseModel), initCompilationUnit(testCaseModel), false);
	return ret;
    }

    private static MockCompilationUnit initCompilationUnit(TestClassGeneratorTestCase testCaseModel) {
	MockCompilationUnit ret = testCaseModel.getTestClassCompilationUnit();
	if (ret == null) {
	    ret = MockCompilationUnit.builder()
		    .elementName("TestClassTest")
		    .build();
	    ret.createType("TestClassTest", null, false, null);
	    testCaseModel.setTestClassCompilationUnit(ret);
	}
	return ret;
    }

    private static MockType initMockType() {
	return MockType.builder()
		.elementName("TestClass")
		.build();
    }

    private static MockJavaProject initJavaProject(TestClassGeneratorTestCase testCaseModel) {
	MockJavaProject ret = testCaseModel.getJavaProject();
	if (ret == null) {
	    ret = MockJavaProject.builder()
		    .elementName("TestProject")
		    .project(MockProject.builder()
			    .build())
		    .build();
	    ret.setPackageFragmentRoot(MockPackageFragmentRoot.builder()
		    .javaProject(ret)
		    .path(MockPath.builder()
			    .elementName("testProject")
			    .build())
		    .build());
	    testCaseModel.setJavaProject(ret);
	} else {
	    ret.getPackageFragmentRoot().setJavaProject(testCaseModel.getJavaProject());
	}
	return ret;
    }

    private static JUTClassesAndPackages initClassesAndPackages(JUTClassesAndPackages classesPackages, TestClassGeneratorTestCase testCaseModel) {

	MockPackageFragment basePackage = testCaseModel.getBasePackage();
	if (basePackage == null) {
	    basePackage = MockPackageFragment.builder()
		    .parent(MockCompilationUnit.builder()
			    .build())
		    .build();
	    testCaseModel.setBasePackage(basePackage);
	}
	basePackage.setElementName("com.example.junittoolsdemo");
	classesPackages.setBasePackages(Arrays.asList(basePackage));
	MockCompilationUnit testClass = initCompilationUnit(testCaseModel);
	classesPackages.setBaseTest(testClass);
	classesPackages.setTestClassName(testClass.getElementName());
	classesPackages.setBaseClassName(testClass.getElementName().replaceFirst("Test$", ""));
	classesPackages.setTestPackageName("com.example.junittoolsdemo");
	classesPackages.setTestPackage(basePackage);
	return classesPackages;
    }

    public static Test initTestModel(TestClassGeneratorTestCase testCaseModel) {
	Test ret = testCaseModel.getTmlTest();
	if (ret == null) {
	    ret = new Test();
	    ret.setSettings(initSettings());
	    testCaseModel.setTmlTest(ret);
	}
	return ret;
    }

    private static Settings initSettings() {
	Settings ret = new Settings();
	ret.setLogger(false);
	ret.setSetUp(false);
	ret.setSetUpBeforeClass(false);
	ret.setTearDown(false);
	ret.setTearDownAfterClass(false);
	ret.setTestUtils(false);
	ret.setThrowsDeclaration(true);
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
