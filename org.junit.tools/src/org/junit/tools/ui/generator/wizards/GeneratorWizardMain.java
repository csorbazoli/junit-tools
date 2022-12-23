package org.junit.tools.ui.generator.wizards;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.junit.tools.base.MethodRef;
import org.junit.tools.generator.IGeneratorConstants;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.tml.Method;
import org.junit.tools.generator.model.tml.Param;
import org.junit.tools.generator.model.tml.Result;
import org.junit.tools.generator.model.tml.Settings;
import org.junit.tools.generator.model.tml.Test;
import org.junit.tools.generator.utils.GeneratorUtils;
import org.junit.tools.generator.utils.JDTUtils;
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
	    getPage().getView().getTxtTestProject()
		    .setText(project.getElementName());
	    getPage().getView().getTxtTestProject().setData(project);
	    selectedTestProject = project;
	}

    }

    /**
     * Handle the toggle button for the other
     */
    protected void handleToggleOther() {
	toggleButton(getPage().getView().getBtnLogger());
	toggleButton(getPage().getView().getBtnFailassertion());
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

	if (tmlTest != null) {
	    // initialize settings
	    initPageSettings(page, tmlTest.getSettings());

	}

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
	String baseProjectName = getModel().getJUTElements().getProjects()
		.getBaseProject().getElementName();
	String testProjectName = baseProjectName;

	page.getView().getTxtTestProject().setText(testProjectName);

	page.getView().getMethodPrefix()
		.setText(JUTPreferences.getTestMethodPrefix());
    }

    /**
     * Initializes the page settings.
     * 
     * @param page
     * @param settings
     */
    private void initPageSettings(GeneratorWizardMainPage page,
	    Settings settings) {
	if (settings == null) {
	    return;
	}

	// standard methods
	page.getView().getBtnSetup().setSelection(settings.isSetUp());
	page.getView().getBtnSetupbeforeclass()
		.setSelection(settings.isSetUpBeforeClass());
	page.getView().getBtnTeardown().setSelection(settings.isTearDown());
	page.getView().getBtnTeardownafterclass()
		.setSelection(settings.isTearDownBeforeClass());

	// other
	page.getView().getBtnLogger().setSelection(settings.isLogger());

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
	settings.setSetUp(page.getView().getBtnSetup().getSelection());
	settings.setSetUpBeforeClass(page.getView().getBtnSetupbeforeclass()
		.getSelection());
	settings.setTearDown(page.getView().getBtnTeardown().getSelection());
	settings.setTearDownBeforeClass(page.getView()
		.getBtnTeardownafterclass().getSelection());

	// other
	settings.setLogger(page.getView().getBtnLogger().getSelection());
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

	for (IMethod method : methodSelection.getCheckedMethods()) {
	    tmlMethod = getObjectFactory().createMethod();
	    tmlTest.getMethod().add(tmlMethod);

	    updateModelMethod(method, tmlMethod);

	    // save in method-map
	    methodMap.put(method, tmlMethod);
	}

	HashMap<MethodRef, IMethod> existingMethods = methodSelection
		.getExistingMethods();

	getModel().setExistingMethods(existingMethods);
	getModel().setMethodsToCreate(
		GeneratorUtils.getMethodsToCreate(existingMethods,
			checkedMethods));
	getModel().setMethodsToDelete(
		GeneratorUtils.getMethodsToDelete(existingMethods,
			checkedMethods));
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
	Result result;
	Param param;
	String returnType;

	tmlMethod.setName(method.getElementName());
	tmlMethod.setModifier(JDTUtils.getMethodModifier(method));
	tmlMethod.setStatic(JDTUtils.isStatic(method));
	tmlMethod.setSignature(method.getSignature());

	// parameters
	ILocalVariable[] parameters = method.getParameters();

	for (ILocalVariable parameter : parameters) {
	    param = getObjectFactory().createParam();
	    param.setName(parameter.getElementName());
	    param.setType(Signature.getSignatureSimpleName(parameter
		    .getTypeSignature()));

	    tmlMethod.getParam().add(param);
	}

	// return type
	returnType = method.getReturnType();
	if (returnType != null && !returnType.equals("V")) {
	    result = getObjectFactory().createResult();
	    result.setName("result");
	    result.setType(Signature.getSignatureSimpleName(returnType));
	    tmlMethod.setResult(result);
	}
    }

    public IJavaProject getSelectedProject() {
	return selectedTestProject;
    }

}
