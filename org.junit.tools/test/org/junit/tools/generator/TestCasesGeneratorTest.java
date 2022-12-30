package org.junit.tools.generator;

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
	// given
	JUTElements elements = new JUTElements();
	org.junit.tools.generator.model.tml.Test test = new org.junit.tools.generator.model.tml.Test();
	GeneratorModel model = new GeneratorModel(elements, test);
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
	assertEquals(1, method.getTestCase().size());
	TestCase firstTestCase = method.getTestCase().get(0);
	assertEquals("String {result}#EQUALS_J5#\"Test${Name}\"", firstTestCase.getAssertion().stream()
		.map(ass -> ass.getBaseType() + " " + ass.getBase() + "#" + ass.getType() + "#" + ass.getValue())
		.collect(Collectors.joining()));
	assertEquals("String someParam = \"TestSomeParam\"", firstTestCase.getParamAssignments().stream()
		.map(ass -> ass.getParamType() + " " + ass.getParamName() + " = " + ass.getAssignment())
		.collect(Collectors.joining()));
    }

    @Test
    public void testGenerateTestCases_shouldCompareJsonForComplexReturnTypes() throws Exception {
	// given
	JUTElements elements = new JUTElements();
	org.junit.tools.generator.model.tml.Test test = new org.junit.tools.generator.model.tml.Test();
	GeneratorModel model = new GeneratorModel(elements, test);
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
	assertEquals(1, method.getTestCase().size());
	TestCase firstTestCase = method.getTestCase().get(0);
	assertEquals("TestBean TestUtils.objectToJson({result})#EQUALS_J5#TestUtils.readTestFile(\"someMethod/TestBean.json\")",
		firstTestCase.getAssertion().stream()
			.map(ass -> ass.getBaseType() + " " + ass.getBase() + "#" + ass.getType() + "#" + ass.getValue())
			.collect(Collectors.joining()));
	assertEquals("TestBean someParam = TestValueFactory.fillFields(new TestBean())", firstTestCase.getParamAssignments().stream()
		.map(ass -> ass.getParamType() + " " + ass.getParamName() + " = " + ass.getAssignment())
		.collect(Collectors.joining()));
    }

}
