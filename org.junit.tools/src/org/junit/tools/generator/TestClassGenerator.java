package org.junit.tools.generator;

import java.util.HashMap;
import java.util.List;

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
// @SuppressWarnings("restriction")
public class TestClassGenerator implements ITestClassGenerator, IGeneratorConstants {

    protected String testmethodPrefix;

    protected String testmethodPostfix;

    protected boolean defaultTestbaseMethodCreated = false;

    private String annoGenerated = null;

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
	IType type;

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

	monitor.beginTask("", 6 + methodSize);

	// create or update test-class-frame
	type = createTestClassFrame(testClass, tmlTest, testClassName);

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// delete generated elements
//	if (testClass.exists()) {
//	    deleteGeneratedElements(testClass, tmlSettings);
//	}

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
	createStandardClassFields(type, testClassName);

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create standard-methods (setup, teardown, ..., only if creation is enabled)
	createStandardMethods(type, tmlSettings);

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// create test-base-methods - this is not needed because we use InjectMocks or
	// Autowired
//	if (writeTML) {
//	    createTestBaseMethods(type, tmlTest, baseClassName);
//	} else {
//	    createTestBaseMethods(type, baseClass, baseClassName,
//		    model.getJUTElements().getConstructorsAndMethods().getBaseClassConstructors(), testDataFactories);
//	}

	// increment task
	if (incrementTask(monitor)) {
	    return null;
	}

	// delete test-methods
//	for (IMethod methodToDelete : model.getMethodsToDelete()) {
//	    methodToDelete.delete(true, null);
//	}

	// create test-methods
	if (createTestMethods(type, model.getMethodMap(), model.getMethodsToCreate(), tmlSettings, baseClassName,
		monitor, increment)) {
	    return null;
	}

	// update test-methods (method-ref)
//	updateExistingMethods(type.getCompilationUnit(), type, model.getExistingMethods());

	// create the test-source-folder and -package
	IPackageFragment testPackage = model.getJUTElements().getClassesAndPackages().getTestPackage();

	if (!testPackage.isDefaultPackage()) {
	    testClass.createPackageDeclaration(testPackage.getElementName(), null);
	}

	// create static standard-imports
	createStandardStaticImports(testClass);

	// save test-class
	testClass.save(null, true);
	testClass.makeConsistent(null);
	if (testClass.hasUnsavedChanges()) {
	    testClass.commitWorkingCopy(true, null);
	}

	return testClass;
    }

//    @SuppressWarnings("unchecked")
//    protected void updateExistingMethods(ICompilationUnit cu, IType cuType, HashMap<MethodRef, IMethod> existingMethods)
//	    throws JavaModelException, IllegalArgumentException, MalformedTreeException, BadLocationException {
//	MethodRef methodRef;
//	Annotation annotation;
//
//	for (Entry<MethodRef, IMethod> entry : existingMethods.entrySet()) {
//	    // update method reference
//	    methodRef = entry.getKey();
//	    if (methodRef.isSignatureChanged()) {
//		// delete old annotation
//		for (IAnnotation iAnnotation : entry.getValue().getAnnotations()) {
//		    if (iAnnotation instanceof Annotation) {
//			annotation = (Annotation) iAnnotation;
//
//			if (ANNO_METHOD_REF_NAME.equals(iAnnotation.getElementName())) {
//			    if (annotation.exists()) {
//				annotation.delete(true, null);
//			    }
//			}
//
//		    }
//		}
//
//		// create new annotation
//		CompilationUnit astRoot = JDTUtils.createASTRoot(cu);
//		MethodDeclaration md = JDTUtils.createMethodDeclaration(astRoot, entry.getValue());
//
//		final ASTRewrite rewriter = ASTRewrite.create(md.getAST());
//
//		NormalAnnotation newNormalAnnotation = rewriter.getAST().newNormalAnnotation();
//
//		newNormalAnnotation.setTypeName(astRoot.getAST().newName("MethodRef"));
//
//		newNormalAnnotation.values()
//			.add(createAnnotationMemberValuePair(astRoot.getAST(), "name", methodRef.getName()));
//		newNormalAnnotation.values().add(
//			createAnnotationMemberValuePair(astRoot.getAST(), "signature", methodRef.getSignatureNew()));
//
//		rewriter.getListRewrite(md, MethodDeclaration.MODIFIERS2_PROPERTY).insertFirst(newNormalAnnotation,
//			null);
//
//		// apply changes
//		TextEdit textEdit = rewriter.rewriteAST();
//		Document document = new Document(cu.getSource());
//		textEdit.apply(document);
//		String newSource = document.get();
//		// update of the compilation unit
//		cu.getBuffer().setContents(newSource);
//	    }
//	}
//
//    }

//    private MemberValuePair createAnnotationMemberValuePair(final AST ast, final String name, final String value) {
//
//	final MemberValuePair mvp = ast.newMemberValuePair();
//	mvp.setName(ast.newSimpleName(name));
//	StringLiteral stringLiteral = ast.newStringLiteral();
//	stringLiteral.setLiteralValue(value);
//	mvp.setValue(stringLiteral);
//	return mvp;
//    }

//    protected void createTestBaseMethods(IType type, ICompilationUnit baseClass, String baseClassName,
//	    Vector<IMethod> constructors, List<ITestDataFactory> testDataFactories) throws JavaModelException {
//
//	String testBaseMethodName = "initUnderTest";
//	if (type.getMethod(testBaseMethodName, null).exists()) {
//	    return;
//	}
//
//	StringBuilder classCreationChain = new StringBuilder();
//	JDTUtils.createClassCreationChain(baseClass.findPrimaryType(), classCreationChain, testDataFactories);
//	String testBaseMethodBody = " return " + classCreationChain.toString() + ";";
//
//	JDTUtils.createMethod(type, MOD_PRIVATE, baseClassName, testBaseMethodName, null, null, testBaseMethodBody,
//		false);
//    }

    /**
     * Creates test base methods.
     * 
     * @param type
     * @param tmlTest
     * @param testBaseName
     * @throws JavaModelException
     */
//    protected void createTestBaseMethods(IType type, Test tmlTest, String testBaseName) throws JavaModelException {
//	Settings tmlSettings = tmlTest.getSettings();
//	TestBases tmlTestbases = tmlTest.getTestBases();
//	if (tmlSettings == null || tmlTestbases == null) {
//	    return;
//	}
//
//	String testBaseMethodBody;
//	String testBaseMethodName;
//
//	for (Constructor tmlConstructor : tmlTestbases.getConstructor()) {
//
//	    for (TestBase tmlTestbase : tmlConstructor.getTestBase()) {
//		testBaseMethodName = createTestBaseMethodName(tmlTestbase.getName());
//
//		testBaseMethodBody = createTestBaseMethodBody(tmlTestbase, testBaseName, testBaseMethodName,
//			tmlConstructor.getParam(), tmlSettings);
//
//		JDTUtils.createMethod(type, MOD_PRIVATE, testBaseName, testBaseMethodName, "Exception", null,
//			testBaseMethodBody, false);
//	    }
//
//	    if (tmlConstructor.getTestBase().size() == 0) {
//		createTestBaseMethodDefault(type, testBaseName, tmlConstructor.getParam());
//	    }
//	}
//    }

    /**
     * Creates the standard methods.
     * 
     * @param type
     * @param tmlSettings
     * @throws JavaModelException
     */
    private void createStandardMethods(IType type, Settings tmlSettings) throws JavaModelException {
	if (tmlSettings == null) {
	    return;
	}

	if (tmlSettings.isSetUp()) {
	    JDTUtils.createMethod(type, MOD_PUBLIC, TYPE_VOID, STANDARD_METHOD_BEFORE, EXCEPTION, null, "", false,
		    ANNO_JUNIT_BEFORE);
	}

	if (tmlSettings.isSetUpBeforeClass()) {
	    JDTUtils.createMethod(type, MOD_PUBLIC + MOD_STATIC_WITH_BLANK, TYPE_VOID, STANDARD_METHOD_BEFORE_ClASS,
		    EXCEPTION, null, "", false, ANNO_JUNIT_BEFORE_CLASS);
	}

	if (tmlSettings.isTearDown()) {
	    JDTUtils.createMethod(type, MOD_PUBLIC, TYPE_VOID, STANDARD_METHOD_AFTER, EXCEPTION, null, "", false,
		    ANNO_JUNIT_AFTER);
	}

	if (tmlSettings.isTearDownBeforeClass()) {
	    JDTUtils.createMethod(type, MOD_PUBLIC + MOD_STATIC_WITH_BLANK, TYPE_VOID, STANDARD_METHOD_AFTER_CLASS,
		    "Exception", null, "", false, ANNO_JUNIT_AFTER_CLASS);
	}
    }

    /**
     * Create a hook after a method call.
     * 
     * @param type
     * @param hookMethodName
     * @param param
     * @throws JavaModelException
     */
//    protected void createHookAfterMethodCall(IType type, String hookMethodName, String param)
//	    throws JavaModelException {
//	JDTUtils.createMethod(type, MOD_PRIVATE, TYPE_VOID, hookMethodName, "Exception", param, "", false);
//    }

    /**
     * Increments the task.
     * 
     * @param monitor
     * @return true if not canceled
     */
    protected boolean incrementTask(IProgressMonitor monitor) {
	return incrementTask(monitor, 1);
    }

    /**
     * Increments the task.
     * 
     * @param monitor
     * @param i
     * @return true if not canceled
     */
    protected boolean incrementTask(IProgressMonitor monitor, int i) {
	if (monitor.isCanceled()) {
	    return true;
	}
	monitor.worked(i);
	return false;
    }

    /**
     * Deletes the generated elements.
     * 
     * @param testClass
     * @param tmlSettings
     * @throws JavaModelException
     */
//    protected void deleteGeneratedElements(ICompilationUnit testClass, Settings tmlSettings) throws JavaModelException {
//	IType[] types = testClass.getTypes();
//	IMethod method;
//	IField field;
//
//	for (IType type : types) {
//	    for (IJavaElement element : type.getChildren()) {
//		if (element instanceof IMethod) {
//		    method = (IMethod) element;
//
//		    if (!deleteStandardMethod(method.getElementName().replace(".java", ""), tmlSettings)) {
//			continue;
//		    }
//		} else if (element instanceof IField) {
//		    field = (IField) element;
//		    if (isGenerated(field.getAnnotations())) {
//			field.delete(true, null);
//		    }
//		}
//	    }
//	}
//    }

    /**
     * @param tmlSettings
     * @return isStandardMethod
     */
//    protected boolean deleteStandardMethod(String methodName, Settings tmlSettings) {
//	if (STANDARD_METHOD_BEFORE.equals(methodName)) {
//	    if (!tmlSettings.isSetUp()) {
//		return true;
//	    } else {
//		return false;
//	    }
//	} else if (STANDARD_METHOD_BEFORE_ClASS.equals(methodName)) {
//	    if (!tmlSettings.isSetUpBeforeClass()) {
//		return true;
//	    } else {
//		return false;
//	    }
//	} else if (STANDARD_METHOD_AFTER.equals(methodName)) {
//	    if (!tmlSettings.isTearDown()) {
//		return true;
//	    } else {
//		return false;
//	    }
//	} else if (STANDARD_METHOD_AFTER_CLASS.equals(methodName)) {
//	    if (!tmlSettings.isTearDownBeforeClass()) {
//		return true;
//	    } else {
//		return false;
//	    }
//	}
//	return true;
//    }

    /**
     * Creates the test class frame.
     * 
     * @param testCompilationUnit
     * @param tmlTest
     * @param testClassName
     * @return the created test class frame
     * @throws JavaModelException
     */
    protected IType createTestClassFrame(ICompilationUnit testCompilationUnit, Test tmlTest, String testClassName)
	    throws JavaModelException {
	IType type = testCompilationUnit.getType(testClassName);

	if (!type.exists()) {
	    return createTestClassFrame(testCompilationUnit, tmlTest, testClassName, null);
	}

	return type;
    }

    /**
     * Returns the annotation to delete.
     * 
     * @param type
     * @param tmlTest
     * @throws JavaModelException
     */
//    protected Vector<Annotation> getAnnotationsToDelete(IType type, Test tmlTest) throws JavaModelException {
//	Vector<Annotation> annotationsToDelete = new Vector<Annotation>();
//	Annotation annotation;
//	boolean recreationNecessary = false;
//
//	for (IAnnotation iAnnotation : type.getAnnotations()) {
//	    if (iAnnotation instanceof Annotation) {
//		annotation = (Annotation) iAnnotation;
//
//		if (ANNO_GENERATED_NAME.equals(iAnnotation.getElementName())) {
//		    annotationsToDelete.add(annotation);
//		    IMemberValuePair[] memberValuePairs = annotation.getMemberValuePairs();
//		    for (IMemberValuePair valuePair : memberValuePairs) {
//			if (!VERSION.equals(valuePair.getValue())) {
//			    recreationNecessary = true;
//			    break;
//			}
//		    }
//		} else if (ANNO_TESTPRIO_NAME.equals(iAnnotation.getElementName())) {
//		    annotationsToDelete.add(annotation);
//		    IMemberValuePair[] memberValuePairs = annotation.getMemberValuePairs();
//
//		    if (memberValuePairs.length == 0) {
//			if (tmlTest.getTestPrio().compareTo(Testprio.DEFAULT) != 0) {
//			    recreationNecessary = true;
//			}
//		    }
//
//		    for (IMemberValuePair valuePair : memberValuePairs) {
//			if (!valuePair.getValue().toString().endsWith(tmlTest.getTestPrio().toString())) {
//			    recreationNecessary = true;
//			    break;
//			}
//		    }
//		}
//	    }
//	}
//
//	if (!recreationNecessary) {
//	    return null;
//	}
//
//	return annotationsToDelete;
//    }

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
    protected IType createTestClassFrame(ICompilationUnit testCompilationUnit, Test tmlTest, String testclassName,
	    String source) throws JavaModelException {
	// create annotations
	String annotations = createTestClassFrameAnnotations(tmlTest);

	// create type
	String superType = "";

	if (source == null) {
	    String customComment = getTestClassComment();

	    superType = tmlTest.getSuperClass();

	    String extendsStmt = "";
	    if (!(superType == null || "".equals(superType))) {
		extendsStmt = " extends " + superType;
	    } else {
		superType = "";
	    }

	    source = customComment + annotations.toString() + MOD_PUBLIC + " class " + testclassName + extendsStmt
		    + "{ " + RETURN + "}";
	} else {
	    source = annotations + source;
	}

	IType type = testCompilationUnit.createType(source, null, true, null);

	String superTypePackage = tmlTest.getSuperClassPackage();
	if (!"".equals(superType) && superTypePackage != null && !"".equals(superTypePackage)) {
	    testCompilationUnit.createImport(superTypePackage + "." + superType, null, null);
	}

	return type;
    }

    protected String getTestClassComment() {
	return "";
    }

    /**
     * Creates the test class annotations.
     * 
     * @param tmlTest
     * 
     * @param tmlTest
     * 
     * @return the created annotations
     */
    protected String createTestClassFrameAnnotations(Test tmlTest) {
	// create annotations
	StringBuilder annotations = new StringBuilder();

	if (tmlTest.getSettings().isLogger()) {
	    annotations.append("@Slf4j").append(RETURN);
	}

	// TODO depending on the method/class we might need different annotations
	// @ExtendWith(MockitoExtension.class) for general usage
	// @ExtendWith(SpringExtension.class) for Spring rest controllers maybe
	// none for class with static methods only
	annotations.append(createAnnoExtendWith("MockitoExtension"));
	// create generator-annotation
	// annotations.append(createAnnoGenerated());

	String[] testClassAnnotations = JUTPreferences.getTestClassAnnotations();
	for (String additionalAnno : testClassAnnotations) {
	    if (!additionalAnno.startsWith("@")) {
		additionalAnno = "@" + additionalAnno;
	    }
	    annotations.append(additionalAnno).append(RETURN);
	}

	return annotations.toString();
    }

    /**
     * Creates the annotation ExtendWith.
     */
    private String createAnnoExtendWith(String extension) {
	return GeneratorUtils.createAnnoExtendWith(extension);
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
	// TODO use different import depending on the JUnit version 4 or 5
	compilationUnit.createImport("org.junit.Test", null, null);

	if (tmlTest.getSettings().isLogger()) {
	    compilationUnit.createImport("lombok.extern.slf4j.Slf4j", null, null);
	}
    }

    private void createStandardStaticImports(ICompilationUnit compilationUnit) throws JavaModelException {
	IJavaElement importAbove = null;
	IImportDeclaration[] imports = compilationUnit.getImports();
	if (imports.length > 0) {
	    importAbove = imports[0];
	    compilationUnit.createImport("org.assertj.core.api.Assertions.assertThat", importAbove, Flags.AccStatic, null);
	    compilationUnit.createImport("org.assertj.core.api.Assertions.assertThrows", importAbove, Flags.AccStatic, null);
	}
    }

    /**
     * Create standard class fields.
     */
    protected void createStandardClassFields(IType type, String testClassName) throws JavaModelException {
	type.createField(GeneratorUtils.createAnnoInjectMocks() + testClassName + " " + UNDER_TEST + ";", null, false, null);
    }

    private boolean createTestMethods(IType type, HashMap<IMethod, Method> methodMap, List<IMethod> methodsToCreate,
	    Settings tmlSettings, String baseClassName, IProgressMonitor monitor, int increment)
	    throws JavaModelException {

	int i = 0;

	for (IMethod methodToCreate : methodsToCreate) {
	    Method tmlMethod = methodMap.get(methodToCreate);
	    createTestMethod(type, tmlMethod, baseClassName);

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

	JDTUtils.createMethod(type, MOD_PUBLIC, TYPE_VOID, testMethodName, "Exception", null, testMethodBody, false,
		// annoMethodRef,
		ANNO_JUNIT_TEST);
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

	String paramNameList;

	// create parameter list
	paramNameList = createParamNameList(params);

	// method-call
	sbTestMethodBody.append(baseName).append(".").append(methodName).append("(").append(paramNameList)
		.append(");");

    }

    protected String createParamNameList(List<Param> params) {
	return createParamNameList(params, false);
    }

    protected String createParamNameList(List<Param> params, boolean useTypeForNull) {

	StringBuilder sbParamList = new StringBuilder();
	String comma = "";

	for (Param param : params) {

	    sbParamList.append(comma);
	    if (useTypeForNull) {
		String initValue = "null";
		if (param.getType() != null) {
		    initValue = JDTUtils.createInitValue(param.getType());
		}

		if (initValue.equals("null")) {
		    sbParamList.append(param.getType() + ".class");
		} else {
		    sbParamList.append(param.getName());
		}

	    } else {
		sbParamList.append(param.getName());
	    }

	    comma = ", ";
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

	String baseType;

	for (Assertion tmlAssertion : tmlTestCase.getAssertion()) {
	    // base
	    String base;
	    if ("{result}".equals(tmlAssertion.getBase())) {
		if ("".equals(resultVariableName)) {
		    continue;
		}

		base = "actual";
		baseType = resultType;
	    } else {
		base = testBaseVariableName + "." + tmlAssertion.getBase() + "()";
		baseType = tmlAssertion.getBaseType();
	    }

	    // assertion-type
	    AssertionType type = tmlAssertion.getType();
	    String assertionType = createAssertionType(type, baseType);

	    if (isGherkinStyle()) {
		sbTestMethodBody.append(RETURN).append("// then");
	    }
	    // Assertion
	    if (type.isJUnit5()) {
		sbTestMethodBody.append(RETURN + "assertThat(" + base + ").");
		if (tmlAssertion.getMessage() != null && tmlAssertion.getMessage().length() > 0) {
		    String message = tmlTestCase.getName() + ": " + tmlAssertion.getMessage();
		    sbTestMethodBody.append("withFailMessage(").append(QUOTES).append(message).append(QUOTES).append(").");
		}
		sbTestMethodBody.append(assertionType)
			.append("(null"); // TODO this should be a test value or TestUtils.readTestFile()
	    } else {
		sbTestMethodBody.append(RETURN + "Assert.").append(assertionType).append("(");
	    }

	    // message
	    String message = "";
	    if (tmlAssertion.getMessage() != null && tmlAssertion.getMessage().length() > 0) {
		if (!type.isJUnit5()) {
		    message = tmlTestCase.getName() + ": " + tmlAssertion.getMessage();
		    sbTestMethodBody.append(QUOTES).append(message).append(QUOTES).append(", ");
		}
	    }

	    // actual
	    if (!type.isJUnit5()) {
		if (type == AssertionType.EQUALS || type == AssertionType.NOT_EQUALS) {
		    // test-value
		    String testValue = tmlAssertion.getValue();
		    testValue = JDTUtils.formatValue(testValue, baseType);
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

    /**
     * @return the test method post fix
     */
    protected String getTestmethodPostfix() {
	if (testmethodPostfix == null) {
	    testmethodPostfix = JUTPreferences.getTestMethodPostfix();
	}

	return testmethodPostfix;
    }

}
