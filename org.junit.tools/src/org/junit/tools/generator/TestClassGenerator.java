package org.junit.tools.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.JUTElements.JUTClassesAndPackages;
import org.junit.tools.generator.model.tml.Assertion;
import org.junit.tools.generator.model.tml.AssertionType;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.ParamAssignment;
import org.junit.tools.generator.model.tml.Result;
import org.junit.tools.generator.model.tml.Settings;
import org.junit.tools.generator.model.tml.Test;
import org.junit.tools.generator.model.tml.TestCase;
import org.junit.tools.generator.utils.GeneratorUtils;
import org.junit.tools.generator.utils.JDTUtils;
import org.junit.tools.preferences.JUTPreferences;

/**
 * The default test-class java generator. On the base of the TML the test-class
 * will be generated.
 * 
 * @author Robert Streng
 * 
 */
public class TestClassGenerator implements ITestClassGenerator, IGeneratorConstants {

    protected String testmethodPrefix;

    protected String testmethodPostfix;

    protected String testmvcMethodPostfix;

    protected boolean defaultTestbaseMethodCreated = false;

    protected Boolean gherkinStyle = null;

    @Override
    public ICompilationUnit generate(GeneratorModel model, List<ITestDataFactory> testDataFactories,
	    IProgressMonitor monitor) throws Exception {
	// boolean writeTML = JUTPreferences.isWriteTML();

	defaultTestbaseMethodCreated = false;

	Test tmlTest = model.getTmlTest();
	Settings tmlSettings = tmlTest.getSettings();

	JUTClassesAndPackages utmClassesAndPackages = model.getJUTElements().getClassesAndPackages();

	utmClassesAndPackages.getTestPackage(true);
	ICompilationUnit testClass = utmClassesAndPackages.getTestClass(true);
	String testClassName = utmClassesAndPackages.getTestClassName();
	// ICompilationUnit baseClass = utmClassesAndPackages.getBaseClass();
	String baseClassName = utmClassesAndPackages.getBaseClassName();
	ICompilationUnit baseClass = utmClassesAndPackages.getBaseClass();
	boolean springController = GeneratorUtils.isSpringController(baseClass);
	IType testClassType;

	// begin task
	int methodSize = tmlTest.getMethod().size();
	int increment;

	if (methodSize >= 300) {
	    increment = 50;
	} else if (methodSize >= 100) {
	    increment = 30;
	} else {
	    increment = 20;
	}

	methodSize = methodSize / increment;

	monitor.beginTask("", 4 + methodSize);

	// create or update test-class-frame
	testClassType = createTestClassFrame(testClass, tmlTest, testClassName, baseClassName);

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create standard-imports
	createStandardImports(testClass, tmlTest);

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create standard-class-fields
	createStandardClassFields(testClassType, baseClassName, tmlTest.isSpring());
	createMocksForDependencies(testClassType, baseClass, tmlTest.isSpring());

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create standard-methods (setup, teardown, ..., only if creation is enabled)
	createStandardMethods(testClassType, tmlTest, springController);

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create test-methods
	createTestMethods(testClassType, model.getMethodMap(), model.getMethodsToCreate(), tmlSettings, baseClass,
		springController && tmlTest.isSpring(), monitor, increment);

	// create static standard-imports
	createStandardStaticImports(testClass, tmlTest);

	// create the test-source-folder and -package
	IPackageFragment testPackage = model.getJUTElements().getClassesAndPackages().getTestPackage();

	if (!testPackage.isDefaultPackage()) {
	    testClass.createPackageDeclaration(testPackage.getElementName(), null);
	}

	// save test-class
	testClass.save(null, true);
	testClass.makeConsistent(null);
	if (testClass.hasUnsavedChanges()) {
	    testClass.commitWorkingCopy(true, null);
	}

	return testClass;
    }

    /**
     * Creates the standard methods.
     */
    private void createStandardMethods(IType type, Test tmlTest, boolean springController) throws JavaModelException {
	Settings tmlSettings = tmlTest.getSettings();
	if (tmlSettings == null) {
	    return;
	}

	if (tmlTest.isSpring() && springController) {
	    // need to init mockMvc for rest endpoint testing
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, STANDARD_METHOD_BEFORE, EXCEPTION, null,
		    "mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();", false,
		    isUsingJunit4() ? ANNO_JUNIT_BEFORE : "@BeforeEach");
	} else if (tmlSettings.isSetUp()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, STANDARD_METHOD_BEFORE, EXCEPTION, null, "", false,
		    isUsingJunit4() ? ANNO_JUNIT_BEFORE : "@BeforeEach");
	}

	if (tmlSettings.isSetUpBeforeClass()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded() + MOD_STATIC_WITH_BLANK, TYPE_VOID, STANDARD_METHOD_BEFORE_ClASS,
		    EXCEPTION, null, "", false,
		    isUsingJunit4() ? ANNO_JUNIT_BEFORE_CLASS : "@BeforeAll");
	}

	if (tmlSettings.isTearDown()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, STANDARD_METHOD_AFTER, EXCEPTION, null, "", false,
		    isUsingJunit4() ? ANNO_JUNIT_AFTER : "@AfterEach");
	}

	if (tmlSettings.isTearDownAfterClass()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded() + MOD_STATIC_WITH_BLANK, TYPE_VOID, STANDARD_METHOD_AFTER_CLASS,
		    "Exception", null, "", false,
		    isUsingJunit4() ? ANNO_JUNIT_AFTER_CLASS : "@AfterAll");
	}
    }

    /**
     * Increments the task.
     */
    private boolean incrementTask(IProgressMonitor monitor) {
	return incrementTask(monitor, 1);
    }

    /**
     * Increments the task.
     */
    private boolean incrementTask(IProgressMonitor monitor, int i) {
	if (monitor.isCanceled()) {
	    return true;
	}
	monitor.worked(i);
	return false;
    }

    /**
     * Creates the test class frame.
     * 
     * @param testCompilationUnit
     * @param tmlTest
     * @param testClassName
     * @return the created test class frame
     * @throws JavaModelException
     */
    private IType createTestClassFrame(ICompilationUnit testCompilationUnit, Test tmlTest, String testClassName, String baseClassName)
	    throws JavaModelException {
	IType type = testCompilationUnit.getType(testClassName);

	if (!type.exists()) {
	    return createTestClassFrame(testCompilationUnit, tmlTest, testClassName, baseClassName, null);
	}

	return type;
    }

    /**
     * Creates a test class frame.
     * 
     * @param testCompilationUnit
     * @param tmlTest
     * @param testclassName
     * @param source
     * @return the created test class frame
     * @throws JavaModelException
     */
    private IType createTestClassFrame(ICompilationUnit testCompilationUnit, Test tmlTest, String testclassName, String baseClassName,
	    String source) throws JavaModelException {
	// create annotations
	String annotations = createTestClassFrameAnnotations(tmlTest, baseClassName);

	// create type
	if (source == null) {
	    String customComment = getTestClassComment();

	    source = customComment + annotations.toString() + getPublicModifierIfNeeded() + " class " + testclassName
		    + "{ " + RETURN + "}";
	} else {
	    source = annotations + source;
	}

	return testCompilationUnit.createType(source, null, true, null);
    }

    private String getPublicModifierIfNeeded() {
	return isUsingJunit4() ? MOD_PUBLIC_WITH_BLANK : "";
    }

    protected String getTestClassComment() {
	return "";
    }

    /**
     * Creates the test class annotations.
     * 
     * @return the created annotations
     */
    protected String createTestClassFrameAnnotations(Test tmlTest, String baseClassName) {
	// create annotations
	StringBuilder annotations = new StringBuilder();
	boolean isSpringTest = tmlTest.isSpring();

	if (tmlTest.getSettings() != null && tmlTest.getSettings().isLogger()) {
	    annotations.append("@Slf4j").append(RETURN);
	}

	if (isSpringTest) {
	    annotations.append("@SpringBootTest").append(RETURN);
	    annotations.append("@ActiveProfiles(\"test\")").append(RETURN);
	    annotations.append("@ContextConfiguration(classes = { ").append(baseClassName).append(".class })").append(RETURN);
	} else if (GeneratorUtils.isUsingEasyMock()) {
	    if (isUsingJunit4()) {
		annotations.append(GeneratorUtils.createAnnoRunWith("EasyMockRunner"));
	    } else {
		annotations.append(GeneratorUtils.createAnnoExtendWith("EasyMockExtension"));
	    }
	} else if (isUsingJunit4()) {
	    annotations.append(GeneratorUtils.createAnnoRunWith("MockitoJUnitRunner"));
	} else {
	    annotations.append(GeneratorUtils.createAnnoExtendWith("MockitoExtension"));
	}

	String[] testClassAnnotations = JUTPreferences.getTestClassAnnotations();
	for (String additionalAnno : testClassAnnotations) {
	    if (!additionalAnno.startsWith("@")) {
		additionalAnno = "@" + additionalAnno;
	    }
	    if (annotations.indexOf(additionalAnno) == -1) {
		annotations.append(additionalAnno).append(RETURN);
	    }
	}

	return annotations.toString();
    }

    /**
     * Creates the standard imports.
     * 
     * @param compilationUnit
     * @param tmlTest
     * @throws JavaModelException
     */
    private void createStandardImports(ICompilationUnit compilationUnit, Test tmlTest) throws JavaModelException {

	compilationUnit.createImport("java.util.*", null, null);
	if (isUsingJunit4()) {
	    compilationUnit.createImport("org.junit.Test", null, null);
	    compilationUnit.createImport("org.junit.runner.RunWith", null, null);
	    if (GeneratorUtils.isUsingEasyMock()) {
		compilationUnit.createImport("org.easynmock.*", null, null);
		// prefer EasyMockRunner instead of Rule
	    } else {
		compilationUnit.createImport("org.mockito.junit.runner.MockitoJUnitRunner", null, null);
	    }
	} else { // default to JUnit5
	    compilationUnit.createImport("org.junit.jupiter.api.Test", null, null);
	    compilationUnit.createImport("org.junit.jupiter.api.extension.ExtendWith", null, null);
	    if (GeneratorUtils.isUsingEasyMock()) {
		compilationUnit.createImport("org.easynmock.*", null, null);
		// compilationUnit.createImport("org.easymock.EasyMockExtension", null, null)
	    } else {
		compilationUnit.createImport("org.mockito.junit.jupiter.MockitoExtension", null, null);
	    }
	}
	if (GeneratorUtils.isUsingMockito()) {
	    compilationUnit.createImport("org.mockito.InjectMocks", null, null);
	    compilationUnit.createImport("org.mockito.Mock", null, null);
	}
	// for EasyMock we need TestSubject

	if (tmlTest.isSpring()) {
	    // SpringRunner/SpringExtension
	    if (isUsingJunit4()) {
		compilationUnit.createImport("org.springframework.test.context.junit4.SpringRunner", null, null);
	    } else {
		compilationUnit.createImport("org.springframework.test.context.junit.jupiter.SpringExtension", null, null);
	    }
	    compilationUnit.createImport("org.springframework.test.context.ActiveProfiles", null, null);
	    compilationUnit.createImport("org.springframework.test.context.ContextConfiguration", null, null);
	    compilationUnit.createImport("org.springframework.beans.factory.annotation.Autowired", null, null);
	    compilationUnit.createImport("org.springframework.boot.test.context.SpringBootTest", null, null);
	    compilationUnit.createImport("org.springframework.boot.test.mock.mockito.MockBean", null, null);
	    if (GeneratorUtils.isSpringController(compilationUnit)) {
		compilationUnit.createImport("org.springframework.http.MediaType", null, null);
		compilationUnit.createImport("org.springframework.test.web.servlet.*", null, null);
		compilationUnit.createImport("org.springframework.test.web.servlet.setup.MockMvcBuilders", null, null);
	    }
	}

	if (tmlTest.getSettings().isLogger()) {
	    compilationUnit.createImport("lombok.extern.slf4j.Slf4j", null, null);
	}
    }

    private boolean isUsingJunit4() {
	return JUTPreferences.getJUnitVersion() == 4;
    }

    private void createStandardStaticImports(ICompilationUnit compilationUnit, Test tmlTest) throws JavaModelException {
	IJavaElement importAbove = null;
	IImportDeclaration[] imports = compilationUnit.getImports();
	if (imports.length > 0) {
	    importAbove = imports[0];
	    compilationUnit.createImport("org.assertj.core.api.Assertions.assertThat", importAbove, Flags.AccStatic, null);
	    // compilationUnit.createImport("org.assertj.core.api.Assertions.assertThrows",
	    // importAbove, Flags.AccStatic, null);
	}
	if (tmlTest.isSpring() && GeneratorUtils.isSpringController(compilationUnit)) {
	    compilationUnit.createImport("org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*", null, Flags.AccStatic, null);
	    compilationUnit.createImport("org.springframework.test.web.servlet.result.MockMvcResultMatchers.status", null, Flags.AccStatic, null);
	}
    }

    /**
     * Create standard class fields.
     * 
     * @param b
     */
    protected void createStandardClassFields(IType type, String testClassName, boolean springTest) throws JavaModelException {
	if (GeneratorUtils.findField(type, UNDER_TEST) == null) {
	    type.createField(GeneratorUtils.createAnnoForUnderTest(springTest) + getPublicModifierIfNeeded() +
		    testClassName + " " + UNDER_TEST + ";", null, false, null);
	}
    }

    /**
     * Create @Mock or @MockBean for each field of baseClass that has @Autowired
     * annotation or injected by constructor.
     * 
     * Also check if the baseClass has a Spring annotation
     * (e.g. @Component, @Service, @Controller or similar)
     */
    protected void createMocksForDependencies(IType testClassType, ICompilationUnit baseClass, boolean spring) throws JavaModelException {
	if (GeneratorUtils.hasSpringAnnotation(baseClass)) {
	    for (Map.Entry<String, String> fieldNameAndType : GeneratorUtils.findInjectedFields(baseClass).entrySet()) {
		createMockField(testClassType, fieldNameAndType.getValue(), fieldNameAndType.getKey(), spring);
	    }
	}
	if (spring && GeneratorUtils.isSpringController(baseClass)
		&& GeneratorUtils.findField(testClassType, "mockMvc") == null) {
	    testClassType.createField("MockMvc mockMvc;", null, false, null);
	}

    }

    private void createMockField(IType testClassType, String mockClass, String mockName, boolean springTest) throws JavaModelException {
	if (GeneratorUtils.findField(testClassType, mockName) == null) {
	    testClassType.createField(GeneratorUtils.createAnnoForDependency(springTest) +
		    mockClass + " " + mockName + ";", null, false, null);
	}
    }

    private boolean createTestMethods(IType type, HashMap<IMethod, Method> methodMap, List<IMethod> methodsToCreate,
	    Settings tmlSettings, ICompilationUnit baseClass, boolean mvcTest, IProgressMonitor monitor, int increment)
	    throws JavaModelException {

	int i = 0;

	String baseClassName = baseClass.getElementName();
	String basePath = "";
	if (mvcTest) {
	    basePath = GeneratorUtils.determineRequestPath(baseClass.findPrimaryType());
	}

	for (IMethod methodToCreate : methodsToCreate) {
	    Method tmlMethod = methodMap.get(methodToCreate);
	    String httpMethod = mvcTest ? GeneratorUtils.determineHttpMethod(methodToCreate) : null;
	    if (httpMethod != null) {
		createMvcTestMethod(type, tmlMethod, baseClassName, httpMethod, basePath + GeneratorUtils.determineRequestPath(methodToCreate));
	    } else {
		createTestMethod(type, tmlMethod, baseClassName);
	    }

	    if (i++ == increment) {
		i = 0;
		// increment task
		if (incrementTask(monitor)) {
		    return true;
		}
	    }
	}

	return false;
    }

    private void createMvcTestMethod(IType type, Method tmlMethod, String baseClassName, String httpMethod, String urlPath)
	    throws JavaModelException {

	// create test-method-name
	String testMethodName;
	String testMethodNamePrefix = getTestmethodPrefix();
	String testMethodNamePostfix = getMvcTestmethodPostfix();
	if (testMethodNamePrefix != null && testMethodNamePrefix.length() > 0) {
	    testMethodName = testMethodNamePrefix + GeneratorUtils.firstCharToUpper(tmlMethod.getName())
		    + testMethodNamePostfix;
	} else {
	    testMethodName = tmlMethod.getName() + testMethodNamePostfix;
	}

	// create test-method-body
	String testMethodBody = createMvcTestMethodBody(type, tmlMethod, testMethodName, baseClassName, httpMethod, urlPath);

	JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, testMethodName, "Exception", null, testMethodBody, false,
		// annoMethodRef,
		ANNO_JUNIT_TEST);
    }

    private void createTestMethod(IType type, Method tmlMethod, String baseClassName)
	    throws JavaModelException {

	// create test-method-name
	String testMethodName;
	String testMethodNamePrefix = getTestmethodPrefix();
	String testMethodNamePostfix = getTestmethodPostfix();
	if (testMethodNamePrefix != null && testMethodNamePrefix.length() > 0) {
	    testMethodName = testMethodNamePrefix + GeneratorUtils.firstCharToUpper(tmlMethod.getName())
		    + testMethodNamePostfix;
	} else {
	    testMethodName = tmlMethod.getName() + testMethodNamePostfix;
	}

	// create test-method-body
	String testMethodBody = createTestMethodBody(type, tmlMethod, testMethodName, baseClassName);

	JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, testMethodName, "Exception", null, testMethodBody, false,
		// annoMethodRef,
		ANNO_JUNIT_TEST);
    }

    protected String createMvcTestMethodBody(IType type, Method tmlMethod, String methodName, String baseClassName, String httpMethod, String urlPath)
	    throws JavaModelException {
	StringBuilder sbTestMethodBody = new StringBuilder();
	List<Param> params = tmlMethod.getParam();
	String testBaseVariableName = UNDER_TEST; // GeneratorUtils.firstCharToLower(baseClassName);

	// create result-variable
	Result result = tmlMethod.getResult();
	String resultVariableName = "";
	String resultType = "String";
	if (result != null) {
	    resultVariableName = result.getName();
	}

	List<TestCase> testCases = tmlMethod.getTestCase();

	for (TestCase tmlTestcase : testCases) {

	    createMvcTestCaseBody(sbTestMethodBody, tmlMethod.getName(), resultVariableName, resultType,
		    params, tmlTestcase.getParamAssignments(), httpMethod, urlPath);

	    // assertions
	    createAssertionsMethodBody(sbTestMethodBody, resultVariableName, resultType, testBaseVariableName,
		    tmlTestcase);
	}

	return sbTestMethodBody.toString();
    }

    private void createMvcTestCaseBody(StringBuilder sbTestMethodBody, String methodName, String resultVariableName,
	    String resultType, List<Param> params, List<ParamAssignment> paramAssignments, String httpMethod, String urlPath) {

	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// given").append(RETURN);
	}

	// create param assignments
	createParamAssignments(paramAssignments, sbTestMethodBody);

	// result
	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// when").append(RETURN);
	}

	if (resultVariableName.length() > 0) {
	    sbTestMethodBody.append(resultType).append(" ").append(resultVariableName).append(" = ");
	}

	sbTestMethodBody.append("mockMvc.perform(").append(httpMethod)
		.append("(\"").append(urlPath).append("\")");

	// create parameter list
	String paramNameList = createMvcParamList(params);

	// method-call
	sbTestMethodBody.append(paramNameList)
		.append(RETURN)
		.append(".accept(\"application/json\"))").append(RETURN)
		.append(".andExpect(status().isOk())").append(RETURN)
		.append(".andReturn()").append(RETURN)
		.append(".getResponse().getContentAsString();");

    }

    protected String createTestMethodBody(IType type, Method tmlMethod, String methodName, String baseClassName) throws JavaModelException {
	StringBuilder sbTestMethodBody = new StringBuilder();
	List<Param> params = tmlMethod.getParam();
	String testbaseMethodName = "";
	String testBaseVariableName = UNDER_TEST; // GeneratorUtils.firstCharToLower(baseClassName);

	// create result-variable
	Result result = tmlMethod.getResult();
	String resultVariableName = "";
	String resultType = "";
	if (result != null) {
	    resultVariableName = result.getName();
	    resultType = result.getType();
	}

	List<TestCase> testCases = tmlMethod.getTestCase();

	for (TestCase tmlTestcase : testCases) {

	    createTestCaseBody(sbTestMethodBody, tmlMethod.getName(), baseClassName, testBaseVariableName,
		    testbaseMethodName, resultVariableName, resultType, params, tmlTestcase.getParamAssignments(),
		    tmlMethod.isStatic());

	    // assertions
	    createAssertionsMethodBody(sbTestMethodBody, resultVariableName, resultType, testBaseVariableName,
		    tmlTestcase);
	}

	return sbTestMethodBody.toString();
    }

    protected void createTestCaseBody(StringBuilder sbTestMethodBody, String methodName, String baseClassName,
	    String testBaseVariableName, String testBaseMethodName, String resultVariableName, String resultType,
	    List<Param> params, List<ParamAssignment> paramAssignments, boolean isStatic) {

	String baseName = testBaseVariableName;

	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// given").append(RETURN);
	}

	// create test-base
	if (isStatic) {
	    baseName = baseClassName;
	} else if (StringUtils.isNotBlank(testBaseMethodName)) {
	    sbTestMethodBody.append(testBaseVariableName).append("=").append(testBaseMethodName).append("();").append(RETURN);
	}

	// create param assignments
	createParamAssignments(paramAssignments, sbTestMethodBody);

	// result
	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// when").append(RETURN);
	}

	if (resultVariableName.length() > 0) {
	    sbTestMethodBody.append(resultType).append(" ").append(resultVariableName).append("=");
	}

	// create parameter list
	String paramNameList = createParamNameList(params);

	// method-call
	sbTestMethodBody.append(baseName).append(".").append(methodName).append("(").append(paramNameList)
		.append(");");

    }

    private String createParamNameList(List<Param> params) {
	return params.stream()
		.map(Param::getName)
		.collect(Collectors.joining(", "));

    }

    private String createMvcParamList(List<Param> params) {
	StringBuilder sbParamList = new StringBuilder();
	for (Param param : params) {

	    if (param.isPrimitive()) {
		sbParamList.append(RETURN)
			.append(".param(\"")
			.append(param.getName())
			.append("\", ")
			.append(param.getName())
			.append(")");
	    } else {
		sbParamList.append(RETURN)
			.append(".content(TestUtils.objectToJson(")
			.append(param.getName())
			.append("))");
	    }

	}

	return sbParamList.toString();

    }

    /**
     * Creates the param assignments
     */
    protected void createParamAssignments(List<ParamAssignment> paramAssignments, StringBuilder methodBody) {
	for (ParamAssignment pa : paramAssignments) {
	    methodBody.append(pa.getParamType()).append(" ")
		    .append(pa.getParamName()).append(" = ")
		    .append(pa.getAssignment()).append(";\n");
	}
    }

    /**
     * Creates the method body for the assertions.
     */
    protected void createAssertionsMethodBody(StringBuilder sbTestMethodBody, String resultVariableName,
	    String resultType, String testBaseVariableName, TestCase tmlTestCase) {

	if (isGherkinStyle()) {
	    sbTestMethodBody.append(RETURN).append("// then");
	}
	if (tmlTestCase.getAssertion().isEmpty()) {
	    sbTestMethodBody.append(RETURN).append("// TODO check for expected side effect (i.e. service call, changed parameter or exception thrown)")
		    .append(RETURN).append("// verify(mock).methodcall();")
		    .append(RETURN)
		    .append("// assertThat(TestUtils.objectToJson(param)).isEqualTo(TestUtils.readTestFile(\"someMethod/ParamType_updated.json\"));")
		    .append(RETURN).append("// assertThrows(SomeException.class, () -> underTest.someMethod());");
	    return;
	}
	for (Assertion tmlAssertion : tmlTestCase.getAssertion()) {
	    // base
	    String base;
	    String baseType;
	    if (tmlAssertion.getBase().contains("{result}")) {
		if ("".equals(resultVariableName)) {
		    continue;
		}

		base = tmlAssertion.getBase().replace("{result}", resultVariableName);
		baseType = resultType;
	    } else {
		base = testBaseVariableName + "." + tmlAssertion.getBase() + "()";
		baseType = tmlAssertion.getBaseType();
	    }

	    // assertion-type
	    AssertionType type = tmlAssertion.getType();
	    String assertionType = createAssertionType(type, baseType);

	    // Assertion
	    if (type.isJUnit5()) {
		sbTestMethodBody.append(RETURN + "assertThat(" + base + ").");
		if (tmlAssertion.getMessage() != null && tmlAssertion.getMessage().length() > 0) {
		    String message = tmlTestCase.getName() + ": " + tmlAssertion.getMessage();
		    sbTestMethodBody.append("withFailMessage(").append(QUOTES).append(message).append(QUOTES).append(").");
		}
		sbTestMethodBody.append(assertionType).append("(")
			.append(tmlAssertion.getValue());
	    } else {
		sbTestMethodBody.append(RETURN + "Assert.").append(assertionType).append("(");

		// message
		String message = "";
		if (StringUtils.isNotBlank(tmlAssertion.getMessage())) {
		    message = tmlTestCase.getName() + ": " + tmlAssertion.getMessage();
		    sbTestMethodBody.append(QUOTES).append(message).append(QUOTES).append(", ");
		}

		// actual
		if (type == AssertionType.EQUALS || type == AssertionType.NOT_EQUALS) {
		    // test-value
		    String testValue = tmlAssertion.getValue();
		    testValue = JDTUtils.formatValue(testValue, baseType, "result");
		    sbTestMethodBody.append(testValue).append(", ");

		    // expected
		    sbTestMethodBody.append(base);

		    // delta
		    if (JDTUtils.isNumber(baseType) && !JDTUtils.isArray(baseType)) {
			sbTestMethodBody.append(", 0");
		    }
		} else {
		    // expected
		    sbTestMethodBody.append(base);
		}
	    }

	    sbTestMethodBody.append(");");
	}

    }

    /**
     * Returns the assertion as String.
     */
    protected String createAssertionType(AssertionType type, String baseType) {
	String assertionType = "assertEquals";

	if (type == AssertionType.EQUALS) {
	    if (JDTUtils.isArray(baseType)) {
		assertionType = "assertArrayEquals";
	    } else {
		assertionType = "assertEquals";
	    }
	} else if (type == AssertionType.NOT_EQUALS) {
	    assertionType = "assertNotEquals";
	} else if (type == AssertionType.IS_NULL) {
	    assertionType = "assertNull";
	} else if (type == AssertionType.NOT_NULL) {
	    assertionType = "assertNotNull";
	} else if (type == AssertionType.IS_TRUE) {
	    assertionType = "assertTrue";
	} else if (type == AssertionType.IS_FALSE) {
	    assertionType = "assertFalse";
	} else if (type == AssertionType.EQUALS_J5) {
	    assertionType = "isEqualTo";
	}
	return assertionType;
    }

    private boolean isGherkinStyle() {
	if (gherkinStyle == null) {
	    gherkinStyle = JUTPreferences.isGherkinStyleEnabled();
	}

	return gherkinStyle;
    }

    protected String getTestmethodPrefix() {
	if (testmethodPrefix == null) {
	    testmethodPrefix = JUTPreferences.getTestMethodPrefix();
	}

	return testmethodPrefix;
    }

    protected String getTestmethodPostfix() {
	if (testmethodPostfix == null) {
	    testmethodPostfix = JUTPreferences.getTestMethodPostfix();
	}

	return testmethodPostfix;
    }

    protected String getMvcTestmethodPostfix() {
	if (testmvcMethodPostfix == null) {
	    testmvcMethodPostfix = JUTPreferences.getTestMvcMethodPostfix();
	}

	return testmvcMethodPostfix;
    }

}
