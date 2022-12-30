package org.junit.tools.generator;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.junit.Before;
import org.junit.Test;
import org.junit.tools.TestHelper;
import org.junit.tools.generator.model.tml.Assertion;
import org.junit.tools.generator.model.tml.AssertionType;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.ParamAssignment;
import org.junit.tools.generator.model.tml.Precondition;
import org.junit.tools.generator.model.tml.Result;
import org.junit.tools.generator.model.tml.Settings;
import org.junit.tools.generator.model.tml.TestCase;
import org.junit.tools.preferences.JUTPreferences;
import org.mockito.Mockito;

public class TestClassGeneratorTest {

    TestClassGenerator underTest = new TestClassGenerator();

    @Before
    public void setupTest() {
	underTest.gherkinStyle = true;
    }

    @Test
    public void testCreateParamAssignments() {
	// given
	ParamAssignment stringParam = new ParamAssignment();
	stringParam.setParamType("String");
	stringParam.setParamName("testString");
	stringParam.setAssignment("\"testValue\"");
	ParamAssignment intParam = new ParamAssignment();
	intParam.setParamType("int");
	intParam.setParamName("testInt");
	intParam.setAssignment("123");
	List<ParamAssignment> paramAssignments = Arrays.asList(stringParam, intParam);
	StringBuilder methodBody = new StringBuilder();
	// when
	underTest.createParamAssignments(paramAssignments, methodBody);
	// then
	assertEquals("String testString = \"testValue\";\n"
		+ "int testInt = 123;\n"
		+ "", methodBody.toString());
    }

    @Test
    public void testCreateParamNameList() {
	// given
	Param stringParam = new Param();
	stringParam.setName("testString");
	stringParam.setPrimitive(true);
	stringParam.setType("String");
	Param intParam = new Param();
	intParam.setName("testInt");
	intParam.setPrimitive(true);
	intParam.setType("int");
	List<Param> params = Arrays.asList(stringParam, intParam);
	TestHelper.initDefaultValueMapping();
	// when
	String actual = underTest.createParamNameList(params, true);
	// then
	assertEquals("testString, testInt", actual);
    }

    @Test
    public void testCreateAssertionType() {
	// given
	// when
	String actual = underTest.createAssertionType(AssertionType.EQUALS_J5, "test");
	// then
	assertEquals("isEqualTo", actual);
    }

    @Test
    public void testCreateAssertionsMethodBody_simpleExpectedValue() {
	// given
	StringBuilder sbTestMethodBody = new StringBuilder();
	TestCase testCase = new TestCase();
	testCase.setName("testedMethod");
	testCase.setTestBase("testBaseProperty");
	Assertion assertion = new Assertion();
	assertion.setBase("{result}");
	assertion.setBaseType("String");
	assertion.setMessage("test message");
	assertion.setType(AssertionType.EQUALS_J5);
	assertion.setValue("\"test value\"");
	testCase.getAssertion().add(assertion);
	// when
	underTest.createAssertionsMethodBody(sbTestMethodBody, "actual", "String", "actual", testCase);
	// then
	assertEquals("\n"
		+ "// then\n" +
		"assertThat(actual).withFailMessage(\"testedMethod: test message\").isEqualTo(\"test value\");",
		sbTestMethodBody.toString());
	// + "Assert.assertThat(\"testedMethod: test message\", baseVar.testBase());"
    }

    @Test
    public void testCreateAssertionsMethodBody_complexExpectedValue() {
	// given
	StringBuilder sbTestMethodBody = new StringBuilder();
	TestCase testCase = new TestCase();
	testCase.setName("testedMethod");
	testCase.setTestBase("testBaseProperty");
	Assertion assertion = new Assertion();
	assertion.setBase("TestUtils.objectToJson({result})");
	assertion.setBaseType("TestBean");
	assertion.setMessage("test message");
	assertion.setType(AssertionType.EQUALS_J5);
	assertion.setValue("TestUtils.readTestFile(\"testedMethod/TestBean.json\")");
	testCase.getAssertion().add(assertion);
	// when
	underTest.createAssertionsMethodBody(sbTestMethodBody, "actual", "TestBean", "actual", testCase);
	// then
	assertEquals("\n"
		+ "// then\n" +
		"assertThat(TestUtils.objectToJson(actual)).withFailMessage(\"testedMethod: test message\").isEqualTo(TestUtils.readTestFile(\"testedMethod/TestBean.json\"));",
		sbTestMethodBody.toString());
    }

    @Test
    public void testCreateTestCaseBody_simpleExpectedValue() {
	// given
	StringBuilder sbTestMethodBody = new StringBuilder();

	Param stringParam = new Param();
	stringParam.setName("testString");
	stringParam.setPrimitive(true);
	stringParam.setType("String");
	Param intParam = new Param();
	intParam.setName("testInt");
	intParam.setPrimitive(true);
	intParam.setType("int");
	List<Param> params = Arrays.asList(stringParam, intParam);

	ParamAssignment stringParamAssignment = new ParamAssignment();
	stringParamAssignment.setParamType("String");
	stringParamAssignment.setParamName("testString");
	stringParamAssignment.setAssignment("\"testValue\"");
	ParamAssignment intParamAssignment = new ParamAssignment();
	intParamAssignment.setParamType("int");
	intParamAssignment.setParamName("testInt");
	intParamAssignment.setAssignment("123");
	List<ParamAssignment> paramAssignments = Arrays.asList(stringParamAssignment, intParamAssignment);

	// when
	underTest.createTestCaseBody(sbTestMethodBody, "someMethod", "SomeClass",
		"underTest", "initUnderTest", "actual", "String",
		params, paramAssignments, false);
	// then
	assertEquals("// given\n"
		+ "underTest=initUnderTest();\n"
		+ "String testString = \"testValue\";\n"
		+ "int testInt = 123;\n"
		+ "// when\n"
		+ "String actual=underTest.someMethod(testString, testInt);",
		sbTestMethodBody.toString());
    }

    @Test
    public void testCreateTestCaseBody_voidMethod() {
	// given
	StringBuilder sbTestMethodBody = new StringBuilder();

	Param stringParam = new Param();
	stringParam.setName("testString");
	stringParam.setPrimitive(true);
	stringParam.setType("String");
	Param intParam = new Param();
	intParam.setName("testInt");
	intParam.setPrimitive(true);
	intParam.setType("int");
	List<Param> params = Arrays.asList(stringParam, intParam);

	ParamAssignment stringParamAssignment = new ParamAssignment();
	stringParamAssignment.setParamType("String");
	stringParamAssignment.setParamName("testString");
	stringParamAssignment.setAssignment("\"testValue\"");
	ParamAssignment intParamAssignment = new ParamAssignment();
	intParamAssignment.setParamType("int");
	intParamAssignment.setParamName("testInt");
	intParamAssignment.setAssignment("123");
	List<ParamAssignment> paramAssignments = Arrays.asList(stringParamAssignment, intParamAssignment);

	// when
	underTest.createTestCaseBody(sbTestMethodBody, "someMethod", "SomeClass",
		"underTest", "initUnderTest", "", "void",
		params, paramAssignments, false);
	// then
	assertEquals("// given\n"
		+ "underTest=initUnderTest();\n"
		+ "String testString = \"testValue\";\n"
		+ "int testInt = 123;\n"
		+ "// when\n"
		+ "underTest.someMethod(testString, testInt);",
		sbTestMethodBody.toString());
    }

    @Test
    public void testCreateTestCaseBody_noInitMethod() {
	// given
	StringBuilder sbTestMethodBody = new StringBuilder();

	Param stringParam = new Param();
	stringParam.setName("testString");
	stringParam.setPrimitive(true);
	stringParam.setType("String");
	Param intParam = new Param();
	intParam.setName("testInt");
	intParam.setPrimitive(true);
	intParam.setType("int");
	List<Param> params = Arrays.asList(stringParam, intParam);

	ParamAssignment stringParamAssignment = new ParamAssignment();
	stringParamAssignment.setParamType("String");
	stringParamAssignment.setParamName("testString");
	stringParamAssignment.setAssignment("\"testValue\"");
	ParamAssignment intParamAssignment = new ParamAssignment();
	intParamAssignment.setParamType("int");
	intParamAssignment.setParamName("testInt");
	intParamAssignment.setAssignment("123");
	List<ParamAssignment> paramAssignments = Arrays.asList(stringParamAssignment, intParamAssignment);

	// when
	underTest.createTestCaseBody(sbTestMethodBody, "someMethod", "SomeClass",
		"underTest", null, "actual", "String",
		params, paramAssignments, false);
	// then
	assertEquals("// given\n"
		+ "String testString = \"testValue\";\n"
		+ "int testInt = 123;\n"
		+ "// when\n"
		+ "String actual=underTest.someMethod(testString, testInt);",
		sbTestMethodBody.toString());
    }

    @Test
    public void testCreateTestCaseBody_staticMethod() {
	// given
	StringBuilder sbTestMethodBody = new StringBuilder();

	Param stringParam = new Param();
	stringParam.setName("testString");
	stringParam.setPrimitive(true);
	stringParam.setType("String");
	Param intParam = new Param();
	intParam.setName("testInt");
	intParam.setPrimitive(true);
	intParam.setType("int");
	List<Param> params = Arrays.asList(stringParam, intParam);

	ParamAssignment stringParamAssignment = new ParamAssignment();
	stringParamAssignment.setParamType("String");
	stringParamAssignment.setParamName("testString");
	stringParamAssignment.setAssignment("\"testValue\"");
	ParamAssignment intParamAssignment = new ParamAssignment();
	intParamAssignment.setParamType("int");
	intParamAssignment.setParamName("testInt");
	intParamAssignment.setAssignment("123");
	List<ParamAssignment> paramAssignments = Arrays.asList(stringParamAssignment, intParamAssignment);

	// when
	underTest.createTestCaseBody(sbTestMethodBody, "someMethod", "SomeClass",
		"underTest", "initUnderTest", "actual", "String",
		params, paramAssignments, true);
	// then
	assertEquals("// given\n"
		+ "String testString = \"testValue\";\n"
		+ "int testInt = 123;\n"
		+ "// when\n"
		+ "String actual=SomeClass.someMethod(testString, testInt);",
		sbTestMethodBody.toString());
    }

    @Test
    public void testCreateTestClassFrameAnnotations_withLogger() {
	// given
	JUTPreferences.setTestClassAnnotations(new String[] { "ActiveProfiles(\"test\")" });
	JUTPreferences.setJUnitVersion(5);
	org.junit.tools.generator.model.tml.Test tmlTest = new org.junit.tools.generator.model.tml.Test();
	Settings settings = new Settings();
	settings.setLogger(true);
	tmlTest.setSettings(settings);
	// when
	String actual = underTest.createTestClassFrameAnnotations(tmlTest, "SomeClass");
	// then
	assertEquals("@Slf4j\n"
		+ "@ExtendWith(MockitoExtension.class)\n"
		+ "@ActiveProfiles(\"test\")\n",
		actual);
    }

    @Test
    public void testCreateTestClassFrameAnnotations_Junit4() {
	// given
	JUTPreferences.setTestClassAnnotations(new String[0]);
	JUTPreferences.setJUnitVersion(4);
	org.junit.tools.generator.model.tml.Test tmlTest = new org.junit.tools.generator.model.tml.Test();
	// when
	String actual = underTest.createTestClassFrameAnnotations(tmlTest, "SomeClass");
	// then
	assertEquals("@RunWith(MockitoJUnitRunner.class)\n",
		actual);
    }

    @Test
    public void testCreateTestClassFrameAnnotations_SpringTest4() {
	// given
	JUTPreferences.setTestClassAnnotations(new String[0]);
	JUTPreferences.setJUnitVersion(4);
	org.junit.tools.generator.model.tml.Test tmlTest = new org.junit.tools.generator.model.tml.Test();
	tmlTest.setTestBase("SomeClass");
	tmlTest.setSpring(true);
	// when
	String actual = underTest.createTestClassFrameAnnotations(tmlTest, "SomeClass");
	// then
	assertEquals("@RunWith(SpringRunner.class)\n"
		+ "@SpringBootTest\n"
		+ "@ActiveProfiles(\"test\")\n"
		+ "@ContextConfiguration(classes = { SomeClass.class })\n",
		actual);
    }

    @Test
    public void testCreateTestClassFrameAnnotations_SpringTest5() {
	// given
	JUTPreferences.setTestClassAnnotations(new String[0]);
	JUTPreferences.setJUnitVersion(5);
	org.junit.tools.generator.model.tml.Test tmlTest = new org.junit.tools.generator.model.tml.Test();
	tmlTest.setTestBase("SomeClass");
	tmlTest.setSpring(true);
	// when
	String actual = underTest.createTestClassFrameAnnotations(tmlTest, "SomeClass");
	// then
	assertEquals("@ExtendWith(SpringExtension.class)\n"
		+ "@SpringBootTest\n"
		+ "@ActiveProfiles(\"test\")\n"
		+ "@ContextConfiguration(classes = { SomeClass.class })\n",
		actual);
    }

    @Test
    public void testCreateTestMethodBody() throws Exception {
	// given
	IType type = Mockito.mock(IType.class);
	Method tmlMethod = new Method();
	tmlMethod.setName("someMethod");
	tmlMethod.setModifier("public");
	Result methodResult = new Result();
	methodResult.setName("actual");
	methodResult.setType("String");
	methodResult.setValue("testValue");
	tmlMethod.setResult(methodResult);
	tmlMethod.setStatic(false);
	Param stringParam = new Param();
	stringParam.setName("testString");
	stringParam.setPrimitive(true);
	stringParam.setType("String");
	Param intParam = new Param();
	intParam.setName("testInt");
	intParam.setPrimitive(true);
	intParam.setType("int");
	tmlMethod.getParam().add(stringParam);
	tmlMethod.getParam().add(intParam);
	TestCase testCase = new TestCase();
	testCase.setName("TestCase1");
	testCase.setTestBase("TestBase");
	Assertion assertion = new Assertion();
	assertion.setBase("{result}");
	assertion.setType(AssertionType.EQUALS_J5);
	assertion.setValue("\"testValueForAssertion\"");
	testCase.getAssertion().add(assertion);
	Precondition precondition = new Precondition();
	precondition.setComment("TestComment");
	testCase.getPreconditions().add(precondition);
	ParamAssignment stringParamAssignment = new ParamAssignment();
	stringParamAssignment.setParamType("String");
	stringParamAssignment.setParamName("testString");
	stringParamAssignment.setAssignment("\"testValue\"");
	ParamAssignment intParamAssignment = new ParamAssignment();
	intParamAssignment.setParamType("int");
	intParamAssignment.setParamName("testInt");
	intParamAssignment.setAssignment("123");
	testCase.getParamAssignments().add(stringParamAssignment);
	testCase.getParamAssignments().add(intParamAssignment);
	tmlMethod.getTestCase().add(testCase);
	// when
	String actual = underTest.createTestMethodBody(type, tmlMethod, "someMethod", "SomeClass");
	// then
	assertEquals("// given\n"
		+ "String testString = \"testValue\";\n"
		+ "int testInt = 123;\n"
		+ "// when\n"
		+ "String actual=underTest.someMethod(testString, testInt);\n"
		+ "// then\n"
		+ "assertThat(actual).isEqualTo(\"testValueForAssertion\");",
		actual);
    }

    @Test
    public void testCreateStandardClassFields() throws Exception {
	// given
	IType type = Mockito.mock(IType.class);
	org.junit.tools.generator.model.tml.Test tmlTest = new org.junit.tools.generator.model.tml.Test();
	// when
	underTest.createStandardClassFields(type, "SomeClass", false);
	// then
	verify(type).createField("@InjectMocks\n"
		+ "SomeClass underTest;", null, false, null);
    }

    @Test
    public void testCreateStandardClassFields_shouldNotCreateIfAlreadyExists() throws Exception {
	// given
	IType type = Mockito.mock(IType.class);
	org.junit.tools.generator.model.tml.Test tmlTest = new org.junit.tools.generator.model.tml.Test();
	IField existingField = Mockito.mock(IField.class);
	when(existingField.getElementName()).thenReturn("underTest");
	when(type.getFields()).thenReturn(new IField[] { existingField });
	// when
	underTest.createStandardClassFields(type, "SomeClass", false);
	// then
	verify(type, never()).createField(anyString(), any(), anyBoolean(), any());
    }

}
