package org.junit.tools.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.junit.tools.generator.model.tml.Annotation;
import org.junit.tools.generator.model.tml.Assertion;
import org.junit.tools.generator.model.tml.AssertionType;
import org.junit.tools.generator.model.tml.Attribute;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.ParamAssignment;
import org.junit.tools.generator.model.tml.Result;
import org.junit.tools.generator.model.tml.Settings;
import org.junit.tools.generator.model.tml.Test;
import org.junit.tools.generator.model.tml.TestCase;
import org.junit.tools.generator.utils.GeneratorUtils;
import org.junit.tools.generator.utils.JDTUtils;
import org.junit.tools.preferences.FieldDeclaration;
import org.junit.tools.preferences.IJUTPreferenceConstants;
import org.junit.tools.preferences.ImportDeclaration;
import org.junit.tools.preferences.JUTPreferences;

/**
 * The default test-class java generator. On the base of the TML the test-class
 * will be generated.
 * 
 * @author Robert Streng
 * 
 */
public class TestClassGenerator implements ITestClassGenerator, IGeneratorConstants {

    private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{(\\w+)\\}");

    protected String testmethodPrefix;
    protected String testmethodPostfix;
    protected String testmvcMethodPostfix;
    protected boolean defaultTestbaseMethodCreated = false;
    protected Boolean repeatingTestMethods = null;

    @Override
    public IMethod generate(GeneratorModel model, List<ITestDataFactory> testDataFactories,
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
	tmlTest.setOnlyStaticMethods(JDTUtils.isStaticMethodOrConstructors(model.getMethodsToCreate()));
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
	testClassType = testClass.getType(testClassName);
	boolean newTest = createTestClassFrame(testClass, tmlTest, testClassName, baseClassName);

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create standard-imports
	if (newTest) {
	    createStandardImports(testClass, tmlTest);
	}

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create standard-class-fields
	if (newTest) {
	    createStandardClassFields(testClassType, baseClassName, tmlTest);
	    if (!tmlTest.isOnlyStaticMethods()) {
		createMocksForDependencies(testClassType, baseClass, tmlTest.isSpring());
	    }
	}

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create standard-methods (setup, teardown, ..., only if creation is enabled)
	if (newTest) {
	    createStandardMethods(testClassType, baseClass, tmlTest, springController);
	}

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create test-methods
	IMethod lastTestMethodCreated = createTestMethods(testClassType, model.getMethodMap(), model.getMethodsToCreate(), tmlSettings, baseClass,
		springController && tmlTest.isSpring(), monitor, increment);

	// create static standard-imports
	if (newTest) {
	    createStandardStaticImports(testClass, tmlTest);
	}

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

	return lastTestMethodCreated;
    }

    /**
     * Creates the standard methods.
     * 
     * @param baseClass
     */
    private void createStandardMethods(IType type, ICompilationUnit baseClass, Test tmlTest, boolean springController) throws JavaModelException {
	Settings tmlSettings = tmlTest.getSettings();
	if (tmlSettings == null) {
	    return;
	}

	if (tmlTest.isSpring() && springController) {
	    // need to init mockMvc for rest endpoint testing
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, STANDARD_METHOD_BEFORE, EXCEPTION,
		    JUTPreferences.getPreference(IJUTPreferenceConstants.BEFORE_METHOD_BODY),
		    "mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();", false,
		    isUsingJunit4() ? ANNO_JUNIT_BEFORE : "@BeforeEach");
	} else if (tmlSettings.isSetUp()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, STANDARD_METHOD_BEFORE, EXCEPTION, null,
		    getBeforeMethodBodyWithInitMocks(), false,
		    isUsingJunit4() ? ANNO_JUNIT_BEFORE : "@BeforeEach");
	} else if (!isMockRunnerEnabled() && GeneratorUtils.isUsingAnyMock()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, STANDARD_METHOD_BEFORE, EXCEPTION, null,
		    getInitMocksStatement(), false,
		    isUsingJunit4() ? ANNO_JUNIT_BEFORE : "@BeforeEach");
	}

	if (tmlSettings.isSetUpBeforeClass()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded() + MOD_STATIC_WITH_BLANK, TYPE_VOID, STANDARD_METHOD_BEFORE_ClASS,
		    EXCEPTION, null, JUTPreferences.getPreference(IJUTPreferenceConstants.BEFORE_CLASS_METHOD_BODY), false,
		    isUsingJunit4() ? ANNO_JUNIT_BEFORE_CLASS : "@BeforeAll");
	}

	if (tmlSettings.isTearDown()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, STANDARD_METHOD_AFTER, EXCEPTION, null,
		    JUTPreferences.getPreference(IJUTPreferenceConstants.AFTER_METHOD_BODY), false,
		    isUsingJunit4() ? ANNO_JUNIT_AFTER : "@AfterEach");
	}

	if (tmlSettings.isTearDownAfterClass()) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded() + MOD_STATIC_WITH_BLANK, TYPE_VOID, STANDARD_METHOD_AFTER_CLASS,
		    EXCEPTION, null, JUTPreferences.getPreference(IJUTPreferenceConstants.AFTER_CLASS_METHOD_BODY), false,
		    isUsingJunit4() ? ANNO_JUNIT_AFTER_CLASS : "@AfterAll");
	}

	if (GeneratorUtils.isUsingEasyMock() && JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.REPLAYALL_VERIFYALL_ENABLED)) {
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, "replayAll", EXCEPTION, null,
		    createReplayAllInstruction(baseClass), false); // before annotation not needed
	    JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, "verifyAll", EXCEPTION, null,
		    createVerifyAllInstruction(baseClass), false,
		    isUsingJunit4() ? ANNO_JUNIT_AFTER : "@AfterEach");
	}
    }

    private String getBeforeMethodBodyWithInitMocks() {
	String ret = JUTPreferences.getPreference(IJUTPreferenceConstants.BEFORE_METHOD_BODY);
	if (!isMockRunnerEnabled()) {
	    String initMockStatement = getInitMocksStatement();
	    if (!ret.contains("openMocks") && !ret.contains("injectMocks") && !ret.contains("initMocks")) {
		ret = initMockStatement + RETURN + ret;
	    }
	}
	return ret;
    }

    private String getInitMocksStatement() {
	if (GeneratorUtils.isUsingEasyMock()) {
	    return "EasyMockSupport.injectMocks(this);";
	} else if (GeneratorUtils.isUsingMockito()) {
	    return "MockitoAnnotations.openMocks(this);";
	}
	return "";
    }

    private String createReplayAllInstruction(ICompilationUnit baseClass) throws JavaModelException {
	return findInjectedFields(baseClass).keySet()
		.stream()
		.collect(Collectors.joining(", ", "replay(", ");"));
    }

    private String createVerifyAllInstruction(ICompilationUnit baseClass) throws JavaModelException {
	return findInjectedFields(baseClass).keySet()
		.stream()
		.collect(Collectors.joining(", ", "verify(", ");"));
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
     * @return true if new class was created, false if it already existed
     */
    private boolean createTestClassFrame(ICompilationUnit testCompilationUnit, Test tmlTest, String testClassName, String baseClassName)
	    throws JavaModelException {
	IType type = testCompilationUnit.getType(testClassName);
	boolean ret = !type.exists();

	if (ret) {
	    createTestClassFrame(testCompilationUnit, tmlTest, testClassName, baseClassName, null);
	}

	return ret;
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

	    source = customComment + annotations.toString() + getPublicModifierIfNeeded() + "class " + testclassName
		    + " {" + RETURN + RETURN + "}";
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

	if (!tmlTest.isOnlyStaticMethods()) {
	    if (isSpringTest) {
		annotations.append("@SpringBootTest").append(RETURN);
		annotations.append("@ActiveProfiles(\"test\")").append(RETURN);
		annotations.append("@ContextConfiguration(classes = { ").append(baseClassName).append(".class })").append(RETURN);
	    } else if (isMockRunnerEnabled()) {
		if (GeneratorUtils.isUsingEasyMock()) {
		    if (isUsingJunit4()) {
			annotations.append(GeneratorUtils.createAnnoRunWith("EasyMockRunner"));
		    } else {
			annotations.append(GeneratorUtils.createAnnoExtendWith("EasyMockExtension"));
		    }
		} else if (GeneratorUtils.isUsingMockito()) {
		    if (isUsingJunit4()) {
			annotations.append(GeneratorUtils.createAnnoRunWith("MockitoJUnitRunner"));
		    } else {
			annotations.append(GeneratorUtils.createAnnoExtendWith("MockitoExtension"));
		    }
		}
	    }
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
	    compilationUnit.createImport("org.junit.*", null, null);
	    compilationUnit.createImport("org.junit.runner.RunWith", null, null);
	    if (isMockRunnerEnabled() && GeneratorUtils.isUsingMockito()) {
		compilationUnit.createImport("org.mockito.junit.runner.MockitoJUnitRunner", null, null);
	    }
	} else { // default to JUnit5
	    compilationUnit.createImport("org.junit.jupiter.api.Test", null, null);
	    compilationUnit.createImport("org.junit.jupiter.api.extension.ExtendWith", null, null);
	    if (isMockRunnerEnabled() && GeneratorUtils.isUsingMockito()) {
		compilationUnit.createImport("org.mockito.junit.jupiter.MockitoExtension", null, null);
	    }
	}
	if (GeneratorUtils.isUsingMockito()) {
	    compilationUnit.createImport("org.mockito.*", null, null);
	} else if (GeneratorUtils.isUsingEasyMock()) {
	    compilationUnit.createImport("org.easymock.*", null, null);
	}

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

	// additional imports
	for (ImportDeclaration importDeclaration : JUTPreferences.getAdditionalImports()) {
	    if (!importDeclaration.isStatic()) {
		compilationUnit.createImport(importDeclaration.getPackageName(), null, null);
	    }
	}
    }

    private boolean isUsingJunit4() {
	return JUTPreferences.getJUnitVersion() == 4;
    }

    protected void createStandardStaticImports(ICompilationUnit compilationUnit, Test tmlTest) throws JavaModelException {
	IJavaElement importAbove = null;
	IImportDeclaration[] imports = compilationUnit.getImports();
	if (imports.length > 0) {
	    importAbove = imports[0];
	}
	if (GeneratorUtils.isUsingEasyMock()) {
	    compilationUnit.createImport("org.easymock.EasyMock.replay", importAbove, Flags.AccStatic, null);
	    compilationUnit.createImport("org.easymock.EasyMock.verify", importAbove, Flags.AccStatic, null);
	}
	// use option for assertj Assertions vs Assert.*
	if (isAssertjEnabled()) {
	    compilationUnit.createImport("org.assertj.core.api.Assertions.assertThat", importAbove, Flags.AccStatic, null);
	} else {
	    compilationUnit.createImport("org.junit.Assert.*", importAbove, Flags.AccStatic, null);
	}
	// compilationUnit.createImport("org.assertj.core.api.Assertions.assertThrows",
	// importAbove, Flags.AccStatic, null);
	if (tmlTest.isSpring() && GeneratorUtils.isSpringController(compilationUnit)) {
	    compilationUnit.createImport("org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*", importAbove, Flags.AccStatic, null);
	    compilationUnit.createImport("org.springframework.test.web.servlet.result.MockMvcResultMatchers.status", importAbove, Flags.AccStatic, null);
	}

	// additional imports configured
	for (ImportDeclaration importDeclaration : JUTPreferences.getAdditionalImports()) {
	    if (importDeclaration.isStatic()) {
		compilationUnit.createImport(importDeclaration.getPackageName(), importAbove, Flags.AccStatic, null);
	    }
	}
    }

    /**
     * Create standard class fields.
     * 
     * @param b
     */
    protected void createStandardClassFields(IType type, String testClassName, Test tmlTest) throws JavaModelException {
	String indent = JDTUtils.getIndentation(1);
	if (!tmlTest.isOnlyStaticMethods() && GeneratorUtils.findField(type, UNDER_TEST) == null) {
	    String initializer = "";
	    if (!GeneratorUtils.isUsingMockito()) {
		initializer = " = new " + testClassName + "()";
	    }
	    type.createField(indent + GeneratorUtils.createAnnoForUnderTest(tmlTest.isSpring()) + indent + getPublicModifierIfNeeded() +
		    testClassName + " " + UNDER_TEST + initializer + ";", null, false, null);
	}
	for (FieldDeclaration additionalField : JUTPreferences.getAdditionalFields()) {
	    type.createField(indent + additionalField.toJavaString() + ";", null, false, null);
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
	Set<Entry<String, String>> autowiredFields = findInjectedFields(baseClass).entrySet();
	for (Map.Entry<String, String> fieldNameAndType : autowiredFields) {
	    createMockField(testClassType, fieldNameAndType.getValue(), fieldNameAndType.getKey(), spring);
	}
	if (spring && GeneratorUtils.isSpringController(baseClass)
		&& GeneratorUtils.findField(testClassType, "mockMvc") == null) {
	    testClassType.createField("MockMvc mockMvc;", null, false, null);
	}

    }

    private Map<String, String> findInjectedFields(ICompilationUnit baseClass) throws JavaModelException {
	Map<String, String> ret = null;
	if (GeneratorUtils.hasSpringAnnotation(baseClass)) {
	    ret = GeneratorUtils.findInjectedFields(baseClass);
	}
	if (ret == null || ret.isEmpty()) {
	    ret = GeneratorUtils.findPotentialInjectedFields(baseClass);
	}
	return ret;
    }

    private void createMockField(IType testClassType, String mockClass, String mockName, boolean springTest) throws JavaModelException {
	if (GeneratorUtils.findField(testClassType, mockName) == null) {
	    testClassType.createField(GeneratorUtils.createAnnoForDependency(springTest) +
		    mockClass + " " + mockName + ";", null, false, null);
	}
    }

    private IMethod createTestMethods(IType type, Map<IMethod, Method> methodMap, List<IMethod> methodsToCreate,
	    Settings tmlSettings, ICompilationUnit baseClass, boolean mvcTest, IProgressMonitor monitor, int increment)
	    throws JavaModelException {

	IMethod newTestMethod = null;

	int i = 0;

	String baseClassName = baseClass.findPrimaryType().getElementName();
	String basePath = "";
	if (mvcTest) {
	    basePath = GeneratorUtils.determineRequestPath(baseClass.findPrimaryType());
	}

	for (IMethod methodToCreate : methodsToCreate) {
	    Method tmlMethod = methodMap.get(methodToCreate);
	    String httpMethod = mvcTest ? GeneratorUtils.determineHttpMethod(methodToCreate) : null;
	    if (httpMethod != null) {
		newTestMethod = createMvcTestMethod(tmlSettings, type, tmlMethod, methodToCreate, httpMethod, basePath);
	    } else {
		newTestMethod = createTestMethod(tmlSettings, type, tmlMethod, baseClassName);
	    }

	    if (i++ == increment) {
		i = 0;
		// increment task
		if (incrementTask(monitor)) {
		    return newTestMethod;
		}
	    }
	}

	return newTestMethod;
    }

    private IMethod createMvcTestMethod(Settings settings, IType type, Method tmlMethod, IMethod testedMethod, String httpMethod, String basePath)
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
	String testMethodBody = createMvcTestMethodBody(type, tmlMethod, httpMethod, basePath + GeneratorUtils.determineRequestPath(testedMethod));

	// throws Exception declaration is always needed for MVC testing
	return JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, testMethodName, EXCEPTION, null,
		testMethodBody, isRepeatingTestMethods(), ANNO_JUNIT_TEST);
    }

    private IMethod createTestMethod(Settings settings, IType type, Method tmlMethod, String baseClassName)
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
	String testMethodBody = createTestMethodBody(type, tmlMethod, baseClassName);

	return JDTUtils.createMethod(type, getPublicModifierIfNeeded(), TYPE_VOID, testMethodName, settings.isThrowsDeclaration() ? EXCEPTION : null, null,
		testMethodBody, isRepeatingTestMethods(), ANNO_JUNIT_TEST);
    }

    protected String createMvcTestMethodBody(IType type, Method tmlMethod, String httpMethod, String urlPath)
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
	    createMvcTestCaseBody(sbTestMethodBody, type, resultVariableName, resultType, params,
		    tmlTestcase.getParamAssignments(), httpMethod, urlPath);

	    // assertions
	    tmlTestcase.getAssertion().stream() // don't do TestUtils.objectToJson as the result is already a String
		    .forEach(assertion -> assertion.setBase("{result}"));
	    createAssertionsMethodBody(sbTestMethodBody, resultVariableName, resultType, testBaseVariableName,
		    tmlTestcase);
	}

	return sbTestMethodBody.toString();

    }

    private void createMvcTestCaseBody(StringBuilder sbTestMethodBody, IType type, String resultVariableName, String resultType,
	    List<Param> params, List<ParamAssignment> paramAssignments, String httpMethod, String urlPath) throws JavaModelException {

	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// given").append(RETURN);
	}

	// create param assignments
	createParamAssignments(paramAssignments, sbTestMethodBody);

	if (isReplayAllVerifyAllEnabled() && GeneratorUtils.isUsingEasyMock() && GeneratorUtils.findMethod(type, "replayAll") != null) {
	    sbTestMethodBody.append("replayAll();").append(RETURN);
	}
	// result
	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// when");
	}
	sbTestMethodBody.append(RETURN); // or just an empty line

	if (resultVariableName.length() > 0) {
	    sbTestMethodBody.append(resultType).append(" ").append(resultVariableName).append(" = ");
	}

	String substitutedPath = "\"" + substituePathVariables(urlPath, params) + "\"";
	sbTestMethodBody.append("mockMvc.perform(").append(httpMethod)
		.append("(").append(substitutedPath.replace("+\"\"", "")).append(")");

	// create parameter list
	// method-call
	sbTestMethodBody.append(createMvcParamList(params))
		.append(RETURN)
		.append(".accept(\"application/json\"))").append(RETURN)
		.append(".andExpect(status().isOk())").append(RETURN)
		.append(".andReturn()").append(RETURN)
		.append(".getResponse().getContentAsString();");

    }

    private String substituePathVariables(String urlPath, List<Param> params) {
	StringBuffer ret = new StringBuffer();
	Map<String, Param> paramMap = new HashMap<>();
	params.forEach(param -> paramMap.put(getParameterName(param), param));
	Matcher m = PATH_VARIABLE_PATTERN.matcher(urlPath);
	while (m.find()) {
	    String paramName = m.group(1);
	    Param existingParam = paramMap.get(paramName);
	    if (existingParam != null) {
		m.appendReplacement(ret, "\"+" + existingParam.getName() + "+\"");
		params.remove(existingParam);
	    }
	}
	m.appendTail(ret);
	return ret.toString();
    }

    protected String createTestMethodBody(IType type, Method tmlMethod, String baseClassName) throws JavaModelException {
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

	    createTestCaseBody(sbTestMethodBody, type, tmlMethod.getName(), baseClassName, testBaseVariableName,
		    testbaseMethodName, resultVariableName, resultType, params, tmlTestcase.getParamAssignments(),
		    tmlMethod.isStatic());

	    // assertions
	    createAssertionsMethodBody(sbTestMethodBody, resultVariableName, resultType, testBaseVariableName,
		    tmlTestcase);
	}

	return sbTestMethodBody.toString();
    }

    protected void createTestCaseBody(StringBuilder sbTestMethodBody, IType type, String methodName, String baseClassName,
	    String testBaseVariableName, String testBaseMethodName, String resultVariableName, String resultType,
	    List<Param> params, List<ParamAssignment> paramAssignments, boolean isStatic) throws JavaModelException {

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

	if (isReplayAllVerifyAllEnabled() && GeneratorUtils.isUsingEasyMock() && GeneratorUtils.findMethod(type, "replayAll") != null) {
	    sbTestMethodBody.append("replayAll();").append(RETURN);
	}
	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// when");
	}
	sbTestMethodBody.append(RETURN); // or just an empty line

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

	    Optional<String> headerName = getHeaderNameIfSpecified(param);
	    if (headerName.isPresent()) {
		sbParamList.append(RETURN)
			.append(".header(\"")
			.append(headerName.get())
			.append("\", ")
			.append(param.getName())
			.append(")");
	    } else if (isRequestBody(param)) {
		sbParamList.append(RETURN)
			.append(".content(TestUtils.objectToJson(")
			.append(param.getName())
			.append("))");
	    } else {
		String paramName = getParameterName(param);
		sbParamList.append(RETURN)
			.append(".param(\"")
			.append(paramName)
			.append("\", ")
			.append(param.getName())
			.append(")");
	    }

	}

	return sbParamList.toString();

    }

    private String getParameterName(Param param) {
	Optional<String> ret = Optional.empty();
	Optional<Annotation> annotation = param.getAnnotations().stream()
		.filter(anno -> "RequestParam".equals(anno.getName()) || "PathVariable".equals(anno.getName()))
		.findFirst();
	if (annotation.isPresent()) {
	    ret = getNameAttribute(annotation.get());
	}
	return ret.orElse(param.getName());
    }

    private Optional<String> getNameAttribute(Annotation anno) {
	return anno.getAttributes().stream()
		.filter(attr -> "name".equals(attr.getName()))
		.map(Attribute::getValue)
		.findFirst();
    }

    private boolean isRequestBody(Param param) {
	return param.getAnnotations().stream()
		.anyMatch(anno -> "RequestBody".equals(anno.getName()));
    }

    private Optional<String> getHeaderNameIfSpecified(Param param) {
	Optional<String> ret = Optional.empty();
	Optional<Annotation> annotation = param.getAnnotations().stream()
		.filter(anno -> "RequestHeader".equals(anno.getName()))
		.findFirst();
	if (annotation.isPresent()) {
	    ret = Optional.of(getNameAttribute(annotation.get())
		    .orElse(param.getName()));
	}
	return ret;
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

	sbTestMethodBody.append(RETURN);
	if (isGherkinStyle()) {
	    sbTestMethodBody.append("// then");
	}
	if (tmlTestCase.getAssertion().isEmpty()) {
	    sbTestMethodBody.append(RETURN)
		    .append("// TODO check for expected side effect (i.e. service call, changed parameter or exception thrown)")
		    .append(RETURN)
		    .append(GeneratorUtils.isUsingEasyMock() ? "// verify(mock);" + RETURN
			    : GeneratorUtils.isUsingMockito() ? "// verify(mock).methodcall();" + RETURN : "")
		    .append("// TestUtils.assertTestFileEquals(\"someMethod/ParamType_updated.json\", TestUtils.objectToJson(param));")
		    .append(RETURN)
		    .append("// assertThrows(SomeException.class, () -> underTest.someMethod());");
	    return;
	}
	for (Assertion tmlAssertion : tmlTestCase.getAssertion()) {
	    // base
	    String base;
	    if (tmlAssertion.getBase().contains("{result}")) {
		if ("".equals(resultVariableName)) {
		    continue;
		}

		base = tmlAssertion.getBase().replace("{result}", resultVariableName);
	    } else {
		base = testBaseVariableName + "." + tmlAssertion.getBase() + "()";
	    }

	    // assertion-type
	    AssertionType type = tmlAssertion.getType();

	    // Assertion
	    if (AssertionType.TESTFILEEQUALS.equals(type)) {
		sbTestMethodBody.append(RETURN + type.getMethod() + "(")
			.append(tmlAssertion.getValue())
			.append(", ")
			.append(base);

		sbTestMethodBody.append(");");
	    } else if (JUTPreferences.isAssertjEnabled()) {
		String assertionType = type.getMethod();
		sbTestMethodBody.append(RETURN + "assertThat(" + base + ").");
		if (tmlAssertion.getMessage() != null && tmlAssertion.getMessage().length() > 0) {
		    String message = tmlTestCase.getName() + ": " + tmlAssertion.getMessage();
		    sbTestMethodBody.append("withFailMessage(").append(QUOTES).append(message).append(QUOTES).append(").");
		}
		sbTestMethodBody.append(assertionType).append("(")
			.append(tmlAssertion.getValue());

		sbTestMethodBody.append(");");
	    } else {
		sbTestMethodBody.append(RETURN + type.getLegacyMethod() + "(");
		if (tmlAssertion.getMessage() != null && tmlAssertion.getMessage().length() > 0) {
		    String message = tmlTestCase.getName() + ": " + tmlAssertion.getMessage();
		    sbTestMethodBody.append(QUOTES).append(message).append(QUOTES).append(", ");
		}
		if (tmlAssertion.getValue() != null && !tmlAssertion.getValue().isEmpty()) {
		    sbTestMethodBody.append(tmlAssertion.getValue())
			    .append(", ");
		}
		sbTestMethodBody.append(base);

		sbTestMethodBody.append(");");
	    }
	}

    }

    private boolean isGherkinStyle() {
	return JUTPreferences.isGherkinStyleEnabled();
    }

    private boolean isMockRunnerEnabled() {
	return JUTPreferences.isUseMockRunner();
    }

    private boolean isAssertjEnabled() {
	return JUTPreferences.isAssertjEnabled();
    }

    private boolean isReplayAllVerifyAllEnabled() {
	return JUTPreferences.isReplayAllVerifyAllEnabled();
    }

    private boolean isRepeatingTestMethods() {
	if (repeatingTestMethods == null) {
	    repeatingTestMethods = JUTPreferences.isRepeatingTestMethodsEnabled();
	}

	return repeatingTestMethods;
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
