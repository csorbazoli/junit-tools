package org.junit.tools.generator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMethod;
import org.junit.Test;
import org.junit.tools.TestHelper;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.JUTElements;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.Result;
import org.junit.tools.generator.model.tml.TestCase;
import org.mockito.Mockito;

public class TestCasesGeneratorTest {

    private TestCasesGenerator underTest = new TestCasesGenerator();

    @Test
    public void testGenerateTestCases() throws Exception {
	GeneratorModel model = createModel();
	IMethod methodKey = Mockito.mock(IMethod.class);
	model.setMethodsToCreate(Arrays.asList(methodKey));
	HashMap<IMethod, Method> methodMap = new HashMap<>();
	Method method = new Method();
	method.setModifier("public");
	method.setStatic(false);
	Result result = new Result();
	result.setType("String");
	method.setResult(result);
	method.setName("someMethod");
	Param param = new Param();
	param.setName("someParam");
	method.getParam().add(param);
	param.setType("String");
	methodMap.put(methodKey, method);
	model.setMethodMap(methodMap);
	TestHelper.initDefaultValueMapping();
	// when
	underTest.generateTestCases(model);
	// then
	assertThat(method.getTestCase()).hasSize(1);
	TestCase firstTestCase = method.getTestCase().get(0);
	assertThat(firstTestCase.getAssertion().stream()
		.map(ass -> ass.getBaseType() + " " + ass.getBase() + "#" + ass.getType() + "#" + ass.getValue())
		.collect(Collectors.joining("\n")))
		.isEqualTo("String {result}#EQUALS#\"TestExpected\"");
	assertThat(firstTestCase.getParamAssignments().stream()
		.map(ass -> ass.getParamType() + " " + ass.getParamName() + " = " + ass.getAssignment())
		.collect(Collectors.joining("\n")))
		.isEqualTo("String someParam = \"TestSomeParam\"");
    }

    @Test
    public void testGenerateTestCases_shouldCompareJsonForComplexReturnTypes() throws Exception {
	GeneratorModel model = createModel();
	IMethod methodKey = Mockito.mock(IMethod.class);
	model.setMethodsToCreate(Arrays.asList(methodKey));
	HashMap<IMethod, Method> methodMap = new HashMap<>();
	Method method = new Method();
	method.setModifier("public");
	method.setStatic(false);
	Result result = new Result();
	result.setType("TestBean");
	method.setResult(result);
	method.setName("someMethod");
	Param param = new Param();
	param.setName("someParam");
	method.getParam().add(param);
	param.setType("TestBean");
	methodMap.put(methodKey, method);
	model.setMethodMap(methodMap);
	TestHelper.initDefaultValueMapping();
	// when
	underTest.generateTestCases(model);
	// then
	assertThat(method.getTestCase()).hasSize(1);
	TestCase firstTestCase = method.getTestCase().get(0);
	assertEquals("TestBean TestUtils.objectToJson({result})#EQUALS#TestUtils.readTestFile(\"SomeClass/someMethod.json\")",
		firstTestCase.getAssertion().stream()
			.map(ass -> ass.getBaseType() + " " + ass.getBase() + "#" + ass.getType() + "#" + ass.getValue())
			.collect(Collectors.joining("\n")));
	assertEquals("TestBean someParam = TestValueFactory.fillFields(new TestBean())", firstTestCase.getParamAssignments().stream()
		.map(ass -> ass.getParamType() + " " + ass.getParamName() + " = " + ass.getAssignment())
		.collect(Collectors.joining("\n")));
    }

    @Test
    public void testGenerateTestCases_noAssertionForVoidMethods() throws Exception {
	GeneratorModel model = createModel();
	IMethod methodKey = Mockito.mock(IMethod.class);
	model.setMethodsToCreate(Arrays.asList(methodKey));
	HashMap<IMethod, Method> methodMap = new HashMap<>();
	Method method = new Method();
	method.setModifier("public");
	method.setStatic(false);
	method.setResult(null);
	method.setName("someMethod");
	Param param = new Param();
	param.setName("someParam");
	method.getParam().add(param);
	param.setType("String");
	methodMap.put(methodKey, method);
	model.setMethodMap(methodMap);
	TestHelper.initDefaultValueMapping();
	// when
	underTest.generateTestCases(model);
	// then
	assertThat(method.getTestCase()).hasSize(1);
	TestCase firstTestCase = method.getTestCase().get(0);
	assertThat(firstTestCase.getAssertion()).isEmpty();
	assertThat(firstTestCase.getParamAssignments().stream()
		.map(ass -> ass.getParamType() + " " + ass.getParamName() + " = " + ass.getAssignment())
		.collect(Collectors.joining("\n"))).isEqualTo("String someParam = \"TestSomeParam\"");
    }

    @Test
    public void testGenerateTestCases_shouldAssertIsTrueForBooleanResultType() throws Exception {
	GeneratorModel model = createModel();
	IMethod methodKey = Mockito.mock(IMethod.class);
	model.setMethodsToCreate(Arrays.asList(methodKey));
	HashMap<IMethod, Method> methodMap = new HashMap<>();
	Method method = new Method();
	method.setModifier("public");
	method.setStatic(false);
	Result result = new Result();
	result.setType("Boolean");
	method.setResult(result);
	method.setName("someMethod");
	Param param = new Param();
	param.setName("someParam");
	method.getParam().add(param);
	param.setType("String");
	methodMap.put(methodKey, method);
	model.setMethodMap(methodMap);
	TestHelper.initDefaultValueMapping();
	// when
	underTest.generateTestCases(model);
	// then
	assertThat(method.getTestCase()).hasSize(1);
	TestCase firstTestCase = method.getTestCase().get(0);
	assertEquals("Boolean {result}#IS_TRUE#", firstTestCase.getAssertion().stream()
		.map(ass -> ass.getBaseType() + " " + ass.getBase() + "#" + ass.getType() + "#" + ass.getValue())
		.collect(Collectors.joining("\n")));
    }

    @Test
    public void testGenerateTestCases_shouldAssertIsNotEmptyForOptionalResultType() throws Exception {
	GeneratorModel model = createModel();
	IMethod methodKey = Mockito.mock(IMethod.class);
	model.setMethodsToCreate(Arrays.asList(methodKey));
	HashMap<IMethod, Method> methodMap = new HashMap<>();
	Method method = new Method();
	method.setModifier("public");
	method.setStatic(false);
	Result result = new Result();
	result.setType("Optional<TestObject>");
	method.setResult(result);
	method.setName("someMethod");
	Param param = new Param();
	param.setName("someParam");
	method.getParam().add(param);
	param.setType("String");
	methodMap.put(methodKey, method);
	model.setMethodMap(methodMap);
	TestHelper.initDefaultValueMapping();
	// when
	underTest.generateTestCases(model);
	// then
	assertThat(method.getTestCase()).hasSize(1);
	TestCase firstTestCase = method.getTestCase().get(0);
	assertThat(firstTestCase.getAssertion().stream()
		.map(ass -> ass.getBaseType() + " " + ass.getBase() + "#" + ass.getType() + "#" + ass.getValue())
		.collect(Collectors.joining("\n")))
		.isEqualTo("Optional<TestObject> {result}#IS_NOT_EMPTY#\n"
			+ "TestObject TestUtils.objectToJson({result}.get())#EQUALS#TestUtils.readTestFile(\"SomeClass/someMethod.json\")");
    }

    @Test
    public void testGenerateTestCases_shouldAssertIsNotEmptyForCollectionResultType() throws Exception {
	// given
	GeneratorModel model = createModel();
	IMethod methodKey = Mockito.mock(IMethod.class);
	model.setMethodsToCreate(Arrays.asList(methodKey));
	HashMap<IMethod, Method> methodMap = new HashMap<>();
	Method method = new Method();
	method.setModifier("public");
	method.setStatic(false);
	Result result = new Result();
	result.setType("Collection<TestObject>");
	method.setResult(result);
	method.setName("someMethod");
	Param param = new Param();
	param.setName("someParam");
	method.getParam().add(param);
	param.setType("String");
	methodMap.put(methodKey, method);
	model.setMethodMap(methodMap);
	TestHelper.initDefaultValueMapping();
	// when
	underTest.generateTestCases(model);
	// then
	assertThat(method.getTestCase()).hasSize(1);
	TestCase firstTestCase = method.getTestCase().get(0);
	assertThat(firstTestCase.getAssertion().stream()
		.map(ass -> ass.getBaseType() + " " + ass.getBase() + "#" + ass.getType() + "#" + ass.getValue())
		.collect(Collectors.joining("\n")))
		.isEqualTo("Collection<TestObject> {result}#IS_NOT_EMPTY#\n"
			+ "Collection<TestObject> TestUtils.objectToJson({result})#EQUALS#TestUtils.readTestFile(\"SomeClass/someMethod.json\")");
    }

    // helper methods
    private GeneratorModel createModel() {
	JUTElements elements = new JUTElements();
	elements.initClassesAndPackages().setBaseClassName("SomeClass");
	org.junit.tools.generator.model.tml.Test test = new org.junit.tools.generator.model.tml.Test();
	test.setTestBase("SomeClass");
	test.setTestClass("SomeClassTest");
	GeneratorModel model = new GeneratorModel(elements, test);
	return model;
    }

}
