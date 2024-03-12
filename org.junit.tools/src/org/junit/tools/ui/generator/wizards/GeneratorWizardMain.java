package org.junit.tools.ui.generator.wizards;

import java.util.HashMap;
import java.util.Vector;
import java.util.stream.Stream;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.junit.tools.base.MethodRef;
import org.junit.tools.generator.IGeneratorConstants;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.JUTElements;
import org.junit.tools.generator.model.JUTElements.JUTConstructorsAndMethods;
import org.junit.tools.generator.model.tml.Annotation;
import org.junit.tools.generator.model.tml.Attribute;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.Result;
import org.junit.tools.generator.model.tml.Settings;
import org.junit.tools.generator.model.tml.Test;
import org.junit.tools.generator.utils.GeneratorUtils;
import org.junit.tools.generator.utils.JDTUtils;
import org.junit.tools.preferences.IJUTPreferenceConstants;
import org.junit.tools.preferences.JUTPreferences;
import org.junit.tools.ui.generator.swt.control.GroupMethodSelectionCtrl;
import org.junit.tools.ui.generator.wizards.pages.GeneratorWizardMainPage;
import org.junit.tools.ui.utils.EclipseUIUtils;

/**
 * The controller for the main page.
 * 
 * @author JUnit-Tools-Team
 * 
 */
public class GeneratorWizardMain extends GeneratorWizardBase implements
	IGeneratorConstants, IMethodeSelectionChangedListener {

    private final ICompilationUnit testBase;

    private final ICompilationUnit testClass;

    private GroupMethodSelectionCtrl methodSelection;

    private Vector<IMethod> checkedMethods = new Vector<IMethod>(0);

    private IJavaProject selectedTestProject;

    /**
     * Constructor
     * 
     * @param model
     * @param generatorWizardMainPage
     */
    public GeneratorWizardMain(GeneratorModel model,
	    GeneratorWizardMainPage generatorWizardMainPage) {
	super(model, generatorWizardMainPage);
	testBase = getModel().getJUTElements().getClassesAndPackages()
		.getBaseClass();
	testClass = getModel().getJUTElements().getClassesAndPackages()
		.getTestClass();

	selectedTestProject = getModel().getJUTElements().getProjects()
		.getTestProject();

    }

    @Override
    public GeneratorWizardMainPage getPage() {
	return (GeneratorWizardMainPage) super.getPage();
    }

    /**
     * Adds the listener to the page elements.
     * 
     * @param page
     */
    private void addListener(GeneratorWizardMainPage page) {

	// toggle buttons
	page.getView().getBtnToggleStandardMethods()
		.addSelectionListener(new SelectionAdapter() {

		    @Override
		    public void widgetSelected(SelectionEvent e) {
			handleToggleStandardMethods();
		    }
		});

	page.getView().getBtnToggleOther()
		.addSelectionListener(new SelectionAdapter() {

		    @Override
		    public void widgetSelected(SelectionEvent e) {
			handleToggleOther();
		    }
		});

	methodSelection.addListener(this);
    }

    protected void handleTestProject() {
	IJavaProject project = EclipseUIUtils
		.getJavaProjectFromDialog(getPage().getShell());

	if (project != null) {
//	    getPage().getView().getTxtTestProject()
//		    .setText(project.getElementName());
//	    getPage().getView().getTxtTestProject().setData(project);
	    selectedTestProject = project;
	}

    }

    /**
     * Handle the toggle button for the other
     */
    protected void handleToggleOther() {
	toggleButton(getPage().getView().getBtnLogger());
	toggleButton(getPage().getView().getBtnTestUtils());
    }

    /**
     * Handle the toggle button for the methods
     */
    protected void handleToggleStandardMethods() {
	toggleButton(getPage().getView().getBtnSetup());
	toggleButton(getPage().getView().getBtnSetupbeforeclass());
	toggleButton(getPage().getView().getBtnTeardown());
	toggleButton(getPage().getView().getBtnTeardownafterclass());
    }

    /**
     * Toggles the button.
     */
    private void toggleButton(Button btn) {
	boolean selection = !btn.getSelection();
	btn.setSelection(selection);
    }

    /**
     * Is called if the method selection changed.
     */
    @Override
    public void methodSelectionChanged(Vector<IMethod> chkdMethods) {
	this.checkedMethods = chkdMethods;

	if (checkedMethods.size() == 0) {
	    getPage().updateStatus("Select at least one method!");
	    return;
	}

	getPage().getView().getCheckboxTreeViewer().expandAll();

	getPage().updateStatus(null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.junit.tools.ui.generator.wizards.IMethodeSelectionChangedListener#selectedMethodChecked(org.eclipse.jdt.core.IMethod)
     */
    @Override
    public void selectedMethodChecked(IMethod selectedMethod) {
	String description = getPage().getDescription();
	if (description == null) {
	    description = "";
	}

	if (!"".equals(description.trim())) {
	    description += RETURN;
	}

	description += "Method " + selectedMethod.getElementName()
		+ " automatically checked!";

	getPage().setDescription(description);
    }

    public Vector<IMethod> getCheckedMethods() {
	return checkedMethods;
    }

    @Override
    public void initPage() {

	GeneratorWizardMainPage page = getPage();
	Test tmlTest = getModel().getTmlTest();
	initDefaults(page);

	// initialize settings
	initPageSettings(page, tmlTest == null ? null : tmlTest.getSettings());

	methodSelection = new GroupMethodSelectionCtrl();

	// add listener (before initializing the method-selection)
	addListener(page);

	try {
	    methodSelection.init(page.getView().getMethodSelectionGroup(),
		    testBase, testClass, getModel());

	    methodSelectionChanged(methodSelection.getCheckedMethods());
	    methodSelection.deactivateFilters();
	} catch (JavaModelException e) {
	    // TODO fehlermeldung
	}
    }

    /**
     * Initializes the defaults.
     * 
     * @param page
     */
    private void initDefaults(GeneratorWizardMainPage page) {
//	String baseProjectName = getModel().getJUTElements().getProjects()
//		.getBaseProject().getElementName();
//	String testProjectName = baseProjectName;

//	page.getView().getTxtTestProject().setText(testProjectName);

	page.getView().getMethodPrefix()
		.setText(JUTPreferences.getTestMethodPrefix());
    }

    /**
     * Initializes the page settings.
     * 
     * @param page
     * @param settings
     */
    private void initPageSettings(GeneratorWizardMainPage page, Settings settings) {
	if (settings == null) {
	    settings = getObjectFactory().createSettings();
	}

	settings.setSetUp(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.BEFORE_METHOD_ENABLED, false));
	settings.setSetUpBeforeClass(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.BEFORE_CLASS_METHOD_ENABLED, false));
	settings.setTearDown(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.AFTER_METHOD_ENABLED, false));
	settings.setTearDownAfterClass(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.AFTER_CLASS_METHOD_ENABLED, false));
	settings.setLogger(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.LOGGER_ENABLED, false));
	settings.setTestUtils(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.TESTUTILS_ENABLED, true));
	settings.setThrowsDeclaration(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.THROWS_DECLARATION_ENABLED, true));

	// standard methods
	page.getView().getBtnSetup().setSelection(settings.isSetUp());
	page.getView().getBtnSetupbeforeclass().setSelection(settings.isSetUpBeforeClass());
	page.getView().getBtnTeardown().setSelection(settings.isTearDown());
	page.getView().getBtnTeardownafterclass().setSelection(settings.isTearDownAfterClass());

	// other
	page.getView().getBtnLogger().setSelection(settings.isLogger());
	page.getView().getBtnTestUtils().setSelection(settings.isTestUtils());
	page.getView().getBtnThrowsDeclaration().setSelection(settings.isThrowsDeclaration());

	page.getView().getBtnShowThisDialog().setSelection(true);

    }

    @Override
    public void updateModel() {
	Test tmlTest = getModel().getTmlTest();
	GeneratorWizardMainPage page = getPage();

	// test
	if (tmlTest == null) {
	    tmlTest = getObjectFactory().createTest();
	    getModel().setTmlTest(tmlTest);
	}

	tmlTest.setTestBase(testBase.getPath().toString());

	if (testClass != null) {
	    tmlTest.setTestClass(testClass.getPath().toString());
	}

	// TML-version
	tmlTest.setVersion(TML_VERSION_ACTUAL);

	updateModelSettings(page, tmlTest);

	// methods
	try {
	    updateModelMethods(tmlTest);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    /**
     * Updates the settings in the model from the page.
     * 
     * @param page
     * @param tmlTest
     */
    private void updateModelSettings(GeneratorWizardMainPage page, Test tmlTest) {
	Settings settings = tmlTest.getSettings();

	if (settings == null) {
	    settings = getObjectFactory().createSettings();
	    tmlTest.setSettings(settings);
	}

	// standard methods
	if (page.getView() == null) {
	    settings.setSetUp(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.BEFORE_METHOD_ENABLED, false));
	    settings.setSetUpBeforeClass(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.BEFORE_CLASS_METHOD_ENABLED, false));
	    settings.setTearDown(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.AFTER_METHOD_ENABLED, false));
	    settings.setTearDownAfterClass(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.AFTER_CLASS_METHOD_ENABLED, false));
	    settings.setLogger(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.LOGGER_ENABLED, false));
	    settings.setTestUtils(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.TESTUTILS_ENABLED, true));
	    settings.setThrowsDeclaration(JUTPreferences.getPreferenceBoolean(IJUTPreferenceConstants.THROWS_DECLARATION_ENABLED, true));
	} else {
	    settings.setSetUp(page.getView().getBtnSetup().getSelection());
	    settings.setSetUpBeforeClass(page.getView().getBtnSetupbeforeclass()
		    .getSelection());
	    settings.setTearDown(page.getView().getBtnTeardown().getSelection());
	    settings.setTearDownAfterClass(page.getView()
		    .getBtnTeardownafterclass().getSelection());
	    // other
	    settings.setLogger(page.getView().getBtnLogger().getSelection());
	    settings.setTestUtils(page.getView().getBtnTestUtils().getSelection());
	    settings.setThrowsDeclaration(page.getView().getBtnThrowsDeclaration().getSelection());

	    // save selections as defaults for next execution
	    JUTPreferences.setPreferenceBoolean(IJUTPreferenceConstants.BEFORE_METHOD_ENABLED, settings.isSetUp());
	    JUTPreferences.setPreferenceBoolean(IJUTPreferenceConstants.BEFORE_CLASS_METHOD_ENABLED, settings.isSetUpBeforeClass());
	    JUTPreferences.setPreferenceBoolean(IJUTPreferenceConstants.AFTER_METHOD_ENABLED, settings.isTearDown());
	    JUTPreferences.setPreferenceBoolean(IJUTPreferenceConstants.AFTER_CLASS_METHOD_ENABLED, settings.isTearDownAfterClass());
	    JUTPreferences.setPreferenceBoolean(IJUTPreferenceConstants.LOGGER_ENABLED, settings.isLogger());
	    JUTPreferences.setPreferenceBoolean(IJUTPreferenceConstants.TESTUTILS_ENABLED, settings.isTestUtils());
	    JUTPreferences.setPreferenceBoolean(IJUTPreferenceConstants.THROWS_DECLARATION_ENABLED, settings.isThrowsDeclaration());

	    JUTPreferences.setShowSettingsBeforeGenerate(page.getView().getBtnShowThisDialog().getSelection());
	}
    }

    /**
     * Updates the methods from the page.
     * 
     * @param tmlTest
     * @throws JavaModelException
     */
    private void updateModelMethods(Test tmlTest) throws JavaModelException {
	Method tmlMethod;

	// delete old methods
	tmlTest.getMethod().clear();

	// add methods
	HashMap<IMethod, Method> methodMap = new HashMap<IMethod, Method>();
	getModel().setMethodMap(methodMap);
	HashMap<MethodRef, IMethod> existingMethods;

	Vector<IMethod> checkedMethods = new Vector<>();
	if (methodSelection == null) {
	    // add selected method or all non-existing methods as if the methodSelection was
	    // initialized
	    JUTElements utmElements = getModel().getJUTElements();
	    ICompilationUnit baseClass = utmElements.getClassesAndPackages().getBaseClass();
	    ICompilationUnit testClass = utmElements.getClassesAndPackages().getTestClass();

	    existingMethods = GeneratorUtils.getExistingTestMethods(baseClass, testClass);

	    // add selected base method
	    if (utmElements.getProjects().isBaseProjectSelected()) {
		JUTConstructorsAndMethods constructorsAndMethods = utmElements.getConstructorsAndMethods();
		IMethod selectedMethod = constructorsAndMethods.getSelectedMethod();
		if (selectedMethod != null) {
		    checkedMethods.add(selectedMethod);
		} else {
		    Vector<IMethod> baseClassMethods = constructorsAndMethods.getBaseClassMethods();
		    for (IMethod method : baseClassMethods) {
			MethodRef methodRef = new MethodRef(method.getElementName(), method.getSignature());
			if (existingMethods.keySet().stream().noneMatch(m -> m.isEquals(methodRef))) {
			    checkedMethods.add(method);
			}
		    }
		}
	    }

	} else {
	    checkedMethods.addAll(methodSelection.getCheckedMethods());
	    existingMethods = new HashMap<>(methodSelection.getExistingMethods());
	}
	for (IMethod method : checkedMethods) {
	    tmlMethod = getObjectFactory().createMethod();
	    tmlTest.getMethod().add(tmlMethod);

	    updateModelMethod(method, tmlMethod);

	    // save in method-map
	    methodMap.put(method, tmlMethod);
	}

	getModel().setMethodsToCreate(GeneratorUtils.getMethodsToCreate(existingMethods, checkedMethods));
    }

    /**
     * Updates the method from the page.
     * 
     * @param method
     * @param tmlMethod
     * @throws JavaModelException
     */
    public void updateModelMethod(IMethod method, Method tmlMethod)
	    throws JavaModelException {

	tmlMethod.setName(method.getElementName());
	tmlMethod.setModifier(JDTUtils.getMethodModifier(method));
	tmlMethod.setStatic(JDTUtils.isStatic(method));
	tmlMethod.setSignature(method.getSignature());

	// method annotations
	convertAnnotations(method.getAnnotations())
		.forEach(tmlMethod.getAnnotations()::add);

	// parameters
	ILocalVariable[] parameters = method.getParameters();

	Param param;
	for (ILocalVariable parameter : parameters) {
	    param = getObjectFactory().createParam();
	    param.setName(parameter.getElementName());
	    param.setType(Signature.getSignatureSimpleName(parameter
		    .getTypeSignature()));
	    param.setPrimitive(JDTUtils.isPrimitive(param.getType()));
	    param.setHasDefaultConstructor(!param.isPrimitive());
	    // TODO how to determine the actual IType of the parameter so that we could
	    // check with JDTUtils.hasDefaultConstructor(paramType) whether it has default
	    // constructor

	    // param annotations
	    convertAnnotations(parameter.getAnnotations())
		    .forEach(param.getAnnotations()::add);

	    tmlMethod.getParam().add(param);
	}

	// return type
	String returnType = method.getReturnType();
	if (returnType != null && !returnType.equals("V")) {
	    Result result = getObjectFactory().createResult();
	    result.setName("actual");
	    result.setType(Signature.getSignatureSimpleName(returnType));
	    tmlMethod.setResult(result);
	}
    }

    private Stream<Annotation> convertAnnotations(IAnnotation[] annotations) {
	if (annotations == null) {
	    return Stream.empty();
	}
	return Stream.of(annotations)
		.map(this::convertAnnotation);
    }

    private Annotation convertAnnotation(IAnnotation anno) {
	Annotation ret = new Annotation();
	ret.setName(anno.getElementName());
	try {
	    convertAnnotationAttributes(anno.getMemberValuePairs())
		    .forEach(ret.getAttributes()::add);
	} catch (JavaModelException e) {
	    throw new RuntimeException(e);
	}
	return ret;
    }

    private Stream<Attribute> convertAnnotationAttributes(IMemberValuePair[] memberValuePairs) {
	if (memberValuePairs == null) {
	    return Stream.empty();
	}
	return Stream.of(memberValuePairs)
		.map(this::convertAnnotationAttribute);
    }

    private Attribute convertAnnotationAttribute(IMemberValuePair valuePair) {
	Attribute ret = new Attribute();
	ret.setName(valuePair.getMemberName());
	ret.setType("String");
	ret.setValue(String.valueOf(valuePair.getValue()));
	return ret;
    }

    public IJavaProject getSelectedProject() {
	return selectedTestProject;
    }

}
