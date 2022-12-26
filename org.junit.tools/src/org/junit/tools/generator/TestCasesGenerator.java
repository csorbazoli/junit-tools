package org.junit.tools.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.tml.Assertion;
import org.junit.tools.generator.model.tml.AssertionType;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.ObjectFactory;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.ParamAssignment;
import org.junit.tools.generator.model.tml.TestCase;
import org.junit.tools.generator.utils.JDTUtils;

/**
 * The test-cases-generator. It's only a beta version.
 * 
 * @author JUnit-Tools-Team
 * 
 */
public class TestCasesGenerator {

    public class ExpressionAnalyzer {

	private final Map<Expression, Integer> expressionOrder = new HashMap<Expression, Integer>();
	private final Map<Expression, org.eclipse.jdt.core.dom.PostfixExpression.Operator> expressionPostfixes = new HashMap<Expression, org.eclipse.jdt.core.dom.PostfixExpression.Operator>();
	private final Map<Expression, org.eclipse.jdt.core.dom.PrefixExpression.Operator> expressionPrefixes = new HashMap<Expression, org.eclipse.jdt.core.dom.PrefixExpression.Operator>();

	int filledCount = 0;

	List<NullLiteral> nullLiterals = new ArrayList<NullLiteral>();
	List<NumberLiteral> numberLiterals = new ArrayList<NumberLiteral>();

	private Operator operator;

	private List<Param> paramBaseList = null;
	private final List<Param> params = new ArrayList<Param>();

	List<PostfixExpression> postfixExpressions = new ArrayList<PostfixExpression>();
	List<PrefixExpression> prefixExpressions = new ArrayList<PrefixExpression>();

	List<SimpleName> simpleNames = new ArrayList<SimpleName>();

	private final List<List<TestCase>> testCasesToMerge = new ArrayList<List<TestCase>>();

	public ExpressionAnalyzer() {

	}

	public ExpressionAnalyzer(Method tmlMethod) {
	    this.paramBaseList = tmlMethod.getParam();
	}

	/**
	 * @param expression
	 */
	public void analyze(Expression expression) {
	    // param == null, null == param
	    if (expression.getNodeType() == ASTNode.NULL_LITERAL) {
		setNullLiteral((NullLiteral) expression);
	    } else if (expression.getNodeType() == ASTNode.SIMPLE_NAME) {
		setSimpleName((SimpleName) expression);
	    } else if (expression.getNodeType() == ASTNode.NUMBER_LITERAL) {
		setNumberLiteral((NumberLiteral) expression);
	    } else if (expression.getNodeType() == ASTNode.PREFIX_EXPRESSION) {
		setPrefixExpression((PrefixExpression) expression);
	    } else if (expression.getNodeType() == ASTNode.POSTFIX_EXPRESSION) {
		setPostfixExpression((PostfixExpression) expression);
	    } else if (expression.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION
		    || expression.getNodeType() == ASTNode.INFIX_EXPRESSION
		    || expression.getNodeType() == ASTNode.METHOD_INVOCATION) {
		// addTestCasesToMerge(processIfExpressions(expression,
		// tmlMethod));
	    } else {
		System.out.println("Expression could not be analyzed: "
			+ expression.getNodeType() + ". Expression: "
			+ expression.toString());
	    }
	}

	private Expression getExpressionFromList(
		List<? extends Expression> expressions, int i) {
	    if (expressions.size() > i) {
		return expressions.get(i);
	    }
	    return null;
	}

	public Expression getNullLiteral() {
	    return getExpressionFromList(nullLiterals, 0);
	}

	public Expression getNullLiteralSecond() {
	    return getExpressionFromList(nullLiterals, 1);
	}

	public NumberLiteral getNumberLiteral() {
	    return (NumberLiteral) getExpressionFromList(numberLiterals, 0);
	}

	public Expression getNumberLiteralSecond() {
	    return getExpressionFromList(numberLiterals, 1);
	}

	/**
	 * Liefert das Attribut operator
	 * 
	 * @return Wert von operator
	 */
	public Operator getOperator() {
	    return operator;
	}

	/**
	 * Liefert das Attribut param
	 * 
	 * @return Wert von param
	 */
	public Param getParam() {
	    return getParam(0);
	}

	public Param getParam(int i) {
	    if (params.size() > i) {
		return params.get(i);
	    }
	    return null;
	}

	public Param getParamSecond() {
	    return getParam(1);
	}

	public PostfixExpression getPostfixExpression() {
	    return (PostfixExpression) getExpressionFromList(
		    postfixExpressions, 0);
	}

	public List<PostfixExpression> getPostfixExpressions() {
	    return postfixExpressions;
	}

	public PostfixExpression getPostfixExpressionSecond() {
	    return (PostfixExpression) getExpressionFromList(
		    postfixExpressions, 1);
	}

	public PrefixExpression getPrefixExpression() {
	    return (PrefixExpression) getExpressionFromList(prefixExpressions,
		    0);
	}

	public List<PrefixExpression> getPrefixExpressions() {
	    return prefixExpressions;
	}

	public PrefixExpression getPrefixExpressionSecond() {
	    return (PrefixExpression) getExpressionFromList(prefixExpressions,
		    1);
	}

	public Expression getSimpleName() {
	    return getExpressionFromList(simpleNames, 0);
	}

	public Expression getSimpleNameSecond() {
	    return getExpressionFromList(simpleNames, 1);
	}

	/**
	 * Liefert das Attribut testCasesToMerge
	 * 
	 * @return Wert von testCasesToMerge
	 */
	public List<List<TestCase>> getTestCasesToMerge() {
	    return testCasesToMerge;
	}

	public void setNullLiteral(NullLiteral nullLiteral) {
	    this.expressionOrder.put(nullLiteral, filledCount++);
	    this.nullLiterals.add(nullLiteral);
	}

	public void setNumberLiteral(NumberLiteral numberLiteral) {
	    this.expressionOrder.put(numberLiteral, filledCount++);
	    this.numberLiterals.add(numberLiteral);
	}

	/**
	 * @param operator
	 */
	public void setOperator(Operator operator) {
	    this.operator = operator;
	}

	/**
	 * @param string
	 */
	private void setParam(String name) {
	    for (Param tmpParam : paramBaseList) {
		if (tmpParam.getName().equals(name)) {
		    this.params.add(tmpParam);
		    return;
		}
	    }
	}

	/**
	 * @param expression
	 */
	public void setPostfixExpression(PostfixExpression expression) {
	    this.postfixExpressions.add(expression);
	    Expression operand = expression.getOperand();
	    expressionPostfixes.put(operand, expression.getOperator());
	    analyze(operand);
	}

	/**
	 * @param expression
	 */
	public void setPrefixExpression(PrefixExpression expression) {
	    this.prefixExpressions.add(expression);
	    Expression operand = expression.getOperand();
	    expressionPrefixes.put(operand, expression.getOperator());
	    analyze(operand);
	}

	public void setSimpleName(SimpleName simpleName) {
	    this.expressionOrder.put(simpleName, filledCount++);
	    this.simpleNames.add(simpleName);

	    if (simpleName != null) {
		setParam(simpleName.toString());
	    }

	}

    }

    private static final ArrayList<TestCase> EMPTY_LIST_TC = new ArrayList<TestCase>();

    private final ObjectFactory of = new ObjectFactory();

    private TestCase createTestCase() {
	TestCase tc = of.createTestCase();
	tc.setName("test");
	tc.setTestBase("");
	return tc;
    }

    public void generateTestCases(GeneratorModel utmModel)
	    throws JavaModelException {
	for (IMethod method : utmModel.getMethodsToCreate()) {

	    Method tmlMethod = utmModel.getMethodMap().get(method);

	    // add default test-case
	    TestCase testCase = of.createTestCase();
	    testCase.setTestBase("");
	    testCase.setName("default test");

	    testCase.getAssertion().add(createDefaultAssertion(tmlMethod));

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
	    assignment.setAssignment(JDTUtils.createInitValue(param.getType(), param.getName()));
	    ret.add(assignment);
	}
	return ret;
    }

    private Assertion createDefaultAssertion(Method tmlMethod) {
	Assertion defaultAssertion = new Assertion();
	defaultAssertion.setBase("{result}");
	defaultAssertion.setBaseType(tmlMethod.getResult().getType());
	defaultAssertion.setType(AssertionType.EQUALS_J5);
	defaultAssertion.setValue("\"EXPECTED\""); // TODO based upon the type
	return defaultAssertion;
    }

    /**
     * @param numberLiteral
     */
    protected Number getNumber(NumberLiteral numberLiteral) {
	String sNumber = numberLiteral.getToken();
	Number number = 0;

	try {
	    number = Integer.parseInt(sNumber);

	} catch (NumberFormatException e) {
	    number = 0;
	}

	return number;
    }

    protected String getNumber(PrefixExpression prefixExpression) {
	String number = null;

	Expression operand = prefixExpression.getOperand();
	if (operand.getNodeType() == ASTNode.NUMBER_LITERAL) {
	    org.eclipse.jdt.core.dom.PrefixExpression.Operator operator = prefixExpression
		    .getOperator();

	    if (org.eclipse.jdt.core.dom.PrefixExpression.Operator.MINUS
		    .equals(operator)) {
		number = "-" + operand.toString();
	    } else if (org.eclipse.jdt.core.dom.PrefixExpression.Operator.PLUS
		    .equals(operator)
		    || org.eclipse.jdt.core.dom.PrefixExpression.Operator.DECREMENT
			    .equals(operator)
		    || org.eclipse.jdt.core.dom.PrefixExpression.Operator.INCREMENT
			    .equals(operator)) {
		number = operand.toString();
	    } else {
		number = "0";
	    }

	}

	return number;
    }

    /**
     * @param operator
     * @return
     */
    private boolean isChainOperator(Operator operator) {

	if (operator == Operator.AND || operator == Operator.CONDITIONAL_AND
		|| operator == Operator.OR
		|| operator == Operator.CONDITIONAL_OR
		|| operator == Operator.XOR) {
	    return true;
	}

	return false;
    }

    private boolean isParamVariable(List<Param> params, String variableName) {
	for (Param param : params) {
	    if (param.getName().equals(variableName)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * @param tcLeftOperand
     * @param tcRightOperand
     * @return
     */
    private List<TestCase> mergeTestCases(List<TestCase> tcList1,
	    List<TestCase> tcList2) {
	List<TestCase> mergedTcList = new ArrayList<TestCase>();

	if (tcList2.size() == 0) {
	    return tcList1;
	} else if (tcList1.size() == 0) {
	    return tcList2;
	}

	// merge tc-list2 to tc-list1
	for (TestCase tc1 : tcList1) {
	    for (TestCase tc2 : tcList2) {
		// avoid duplicates
		// param assignments
		for (ParamAssignment pa2 : tc2.getParamAssignments()) {
		    boolean duplicateFound = false;
		    for (ParamAssignment pa1 : tc1.getParamAssignments()) {
			if (pa1.getParamName().equals(pa2.getParamName())) {
			    duplicateFound = true;
			    break;
			}
		    }

		    if (!duplicateFound) {
			tc1.getParamAssignments().add(pa2);
		    }
		}

	    }
	}

	// merge tc-list1 to tc-list2
	for (TestCase tc2 : tcList2) {
	    for (TestCase tc1 : tcList1) {
		// avoid duplicates
		// param assignments
		for (ParamAssignment pa1 : tc1.getParamAssignments()) {
		    boolean duplicateFound = false;
		    for (ParamAssignment pa2 : tc2.getParamAssignments()) {
			if (pa2.getParamName().equals(pa1.getParamName())) {
			    duplicateFound = true;
			    break;
			}
		    }

		    if (!duplicateFound) {
			tc2.getParamAssignments().add(pa1);
		    }
		}
	    }
	}

	mergedTcList.addAll(tcList1);
	mergedTcList.addAll(tcList2);

	return mergedTcList;
    }

}
