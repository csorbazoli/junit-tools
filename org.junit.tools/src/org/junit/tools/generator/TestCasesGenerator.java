package org.junit.tools.generator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.tml.Assertion;
import org.junit.tools.generator.model.tml.AssertionType;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.ObjectFactory;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.ParamAssignment;
import org.junit.tools.generator.model.tml.TestCase;
import org.junit.tools.generator.utils.JDTUtils;
import org.junit.tools.preferences.JUTPreferences;

/**
 * The test-cases-generator. It's only creating a single test case with an
 * isEqualTo assertion.
 * 
 */
public class TestCasesGenerator {

    private static final Set<String> COLLECTION_TYPES = new HashSet<>(Arrays.asList("Collection<",
	    "List<",
	    "Set<",
	    "Map<"));
    private final ObjectFactory of = new ObjectFactory();

    public void generateTestCases(GeneratorModel utmModel)
	    throws JavaModelException {
	for (IMethod method : utmModel.getMethodsToCreate()) {

	    Method tmlMethod = utmModel.getMethodMap().get(method);

	    // add default test-case
	    TestCase testCase = of.createTestCase();
	    testCase.setTestBase("");
	    testCase.setName("default test");

	    Assertion assertion = createAssertionForResult(tmlMethod, utmModel.getTmlTest().getTestBase());
	    if (assertion != null) {
		testCase.getAssertion().add(assertion);
	    }

	    testCase.getParamAssignments().addAll(createParamAssignments(tmlMethod));

	    tmlMethod.getTestCase().add(testCase);

	}
    }

    private List<ParamAssignment> createParamAssignments(Method tmlMethod) {
	List<ParamAssignment> ret = new LinkedList<>();
	for (Param param : tmlMethod.getParam()) {
	    ParamAssignment assignment = new ParamAssignment();
	    assignment.setParamType(param.getType());
	    assignment.setParamName(param.getName());
	    assignment.setAssignment(JDTUtils.createInitValue(param.getType(), param.getName(), true));
	    ret.add(assignment);
	}
	return ret;
    }

    private Assertion createAssertionForResult(Method tmlMethod, String testClass) {
	if (tmlMethod.getResult() == null) {
	    return null;
	}
	Assertion defaultAssertion = new Assertion();
	String resultType = tmlMethod.getResult().getType();
	defaultAssertion.setBaseType(resultType);
	if ("Boolean".equalsIgnoreCase(resultType)) {
	    defaultAssertion.setType(AssertionType.IS_TRUE);
	    defaultAssertion.setBase("{result}");
	    defaultAssertion.setValue("");
	} else if (resultType.startsWith("Optional") || isCollection(resultType)) {
	    defaultAssertion.setType(AssertionType.IS_NOT_EMPTY);
	    defaultAssertion.setBase("{result}");
	    defaultAssertion.setValue("");
	} else {
	    defaultAssertion.setType(AssertionType.EQUALS);
	    String expected = JUTPreferences.getDefaultValuesByType().get(resultType);
	    if (expected == null) {
		defaultAssertion.setBase("TestUtils.objectToJson({result})");
		expected = "TestUtils.readTestFile(\"" + testClass + "/" + resultType + ".json\")";
	    } else {
		defaultAssertion.setBase("{result}");
		expected = JDTUtils.replaceValuePlaceholders(expected, "Expected", resultType);
	    }
	    defaultAssertion.setValue(expected);
	}
	return defaultAssertion;
    }

    private boolean isCollection(String resultType) {
	return COLLECTION_TYPES.stream().anyMatch(typeString -> resultType.contains(typeString));
    }

}
