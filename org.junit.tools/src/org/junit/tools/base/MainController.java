package org.junit.tools.base;

import java.util.Vector;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.tools.Activator;
import org.junit.tools.generator.IGeneratorConstants;
import org.junit.tools.generator.ITestClassGenerator;
import org.junit.tools.generator.TestCasesGenerator;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.generator.model.JUTElements;
import org.junit.tools.generator.model.JUTElements.JUTClassesAndPackages;
import org.junit.tools.generator.model.JUTElements.JUTConstructorsAndMethods;
import org.junit.tools.generator.model.JUTElements.JUTProjects;
import org.junit.tools.generator.model.tml.Test;
import org.junit.tools.generator.utils.GeneratorUtils;
import org.junit.tools.generator.utils.JDTUtils;
import org.junit.tools.messages.Messages;
import org.junit.tools.preferences.JUTPreferences;
import org.junit.tools.ui.generator.wizards.GeneratorWizard;
import org.junit.tools.ui.utils.EclipseUIUtils;

/**
 * Main controller for all junit-tools-commands and actions.
 * 
 * TODO move the logic to the handler classes.
 * 
 * @author JUnit-Tools-Team
 */
public class MainController implements IGeneratorConstants {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ICompilationUnit generatedTestClass = null;

    private boolean error = false;

    private Exception errorException;

    private ExtensionPointHandler extensionHandler = null;

    /**
     * Generates a test-class.
     * 
     * @param activeWorkbenchWindow
     * @param selection
     * @return true, if the test-class is successful generated. False otherwise.
     * @throws JUTException
     * @throws JUTWarning
     * @throws CoreException
     */
    public boolean generateTestclass(IWorkbenchWindow activeWorkbenchWindow,
	    IStructuredSelection selection) throws JUTException, JUTWarning,
	    CoreException {
	JUTElements jutElements = detectJUTElements(selection, null);
	return generateTestclass(activeWorkbenchWindow, jutElements, false);
    }

    public boolean generateSpringTestclass(IWorkbenchWindow activeWorkbenchWindow,
	    IStructuredSelection selection) throws JUTException, JUTWarning,
	    CoreException {
	JUTElements jutElements = detectJUTElements(selection, null);
	return generateTestclass(activeWorkbenchWindow, jutElements, true);
    }

    /**
     * Generates a test-class.
     * 
     * @param activeWorkbenchWindow
     * @param fileEditorInput
     * @return true, if the test-class is successful generated. False otherwise.
     * @throws JUTException
     * @throws JUTWarning
     * @throws CoreException
     */
    public boolean generateTestclass(IWorkbenchWindow activeWorkbenchWindow,
	    IFileEditorInput fileEditorInput) throws JUTException, JUTWarning,
	    CoreException {
	JUTElements jutElements = detectJUTElements(null, fileEditorInput);
	return generateTestclass(activeWorkbenchWindow, jutElements, false);
    }

    public boolean generateSpringTestclass(IWorkbenchWindow activeWorkbenchWindow,
	    IFileEditorInput fileEditorInput) throws JUTException, JUTWarning,
	    CoreException {
	JUTElements jutElements = detectJUTElements(null, fileEditorInput);
	return generateTestclass(activeWorkbenchWindow, jutElements, true);
    }

    protected ExtensionPointHandler getExtensionHandler() {
	if (extensionHandler == null) {
	    extensionHandler = Activator.getDefault().getExtensionHandler();
	}

	return extensionHandler;
    }

    /**
     * Generates a test-class.
     * 
     * @param activeWorkbenchWindow
     * @param jutElements
     * @param springTest            TODO
     * @return true, if the test-class is successful generated. False otherwise.
     * @throws JUTException
     * @throws JUTWarning
     */
    private boolean generateTestclass(
	    final IWorkbenchWindow activeWorkbenchWindow,
	    JUTElements jutElements, boolean springTest) throws JUTException, JUTWarning {
	if (jutElements == null) {
	    throw new JUTWarning(
		    "No elements found! Perhaps baseclass changed.");
	}

	JUTProjects projects = jutElements.getProjects();
	if (projects == null) {
	    throw new JUTWarning("No project found! Perhaps baseclass changed.");
	}

	try {
	    JUTClassesAndPackages classesAndPackages = jutElements
		    .getClassesAndPackages();
	    String testClassName = classesAndPackages.getTestClassName();
	    ICompilationUnit testClass = classesAndPackages.getTestClass();

	    Test tmlTest = null;

	    // create the model
	    final GeneratorModel model = new GeneratorModel(jutElements, tmlTest);

	    // Open wizard (only if needed)
	    if (!runGeneratorWizard(model, activeWorkbenchWindow)) {
		return false;
	    }

	    model.getTmlTest().setSpring(springTest);

	    // generate test-cases (in tml)
	    try {
		TestCasesGenerator tcg = new TestCasesGenerator();
		tcg.generateTestCases(model);
	    } catch (Exception ex) {
		// only log the exception
		logger.warning("Exception occured during the test-cases-generation! "
			+ ex.getMessage());
	    }

	    // save and close opened test-class-file
	    EclipseUIUtils.saveAndCloseEditor(testClassName);

	    // Generate test-elements
	    IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {

		@Override
		public void run(IProgressMonitor monitor) {
		    try {
			ICompilationUnit generatedClass = null;
			for (ITestClassGenerator testClassGenerator : getExtensionHandler()
				.getTestClassGenerators()) {
			    generatedClass = testClassGenerator.generate(model,
				    getExtensionHandler()
					    .getTestDataFactories(),
				    monitor);
			}
			setGeneratedTestClass(generatedClass);

			monitor.done();
		    } catch (Exception e) {
			setError(true, e);
		    }
		}
	    };

	    setError(false);
	    try {
		activeWorkbenchWindow.run(true, true, runnableWithProgress);
	    } catch (Exception ex) {
		throw new JUTException(ex);
	    }

	    if (isError()) {
		throw new JUTException(errorException);
	    }

	    // make source beautiful
	    IWorkbenchPartSite site = activeWorkbenchWindow.getActivePage()
		    .getActivePart().getSite();
	    EclipseUIUtils.organizeImports(site, testClass);
	    EclipseUIUtils.format(site, testClass);

	    // open in editor
	    openInEditor(activeWorkbenchWindow.getShell(),
		    (IFile) getGeneratedTestClass().getResource());

	} catch (JUTException ex) {
	    throw ex;
	} catch (Exception ex) {
	    throw new JUTException(ex);
	}

	return true;
    }

    protected void setError(boolean error, Exception ex) {
	setError(error);
	this.errorException = ex;
    }

    private ICompilationUnit getGeneratedTestClass() {
	return this.generatedTestClass;
    }

    protected void setGeneratedTestClass(ICompilationUnit generatedClass) {
	this.generatedTestClass = generatedClass;
    }

    protected void openInEditor(Shell shell, IFile generatedTestclass) {
	EclipseUIUtils.openInEditor(shell, generatedTestclass);
    }

    /**
     * @throws CoreException
     * @throws JavaModelException Detects all the necessary JUT-Elements: project,
     *                            package and class for the base and test.
     * @return detected JUT-Elements
     * @throws JUTException
     * @throws JUTWarning
     * @throws
     */
    private JUTElements detectJUTElements(IStructuredSelection selection,
	    IFileEditorInput fileEditorInput) throws JUTException, JUTWarning,
	    CoreException {

	JUTElements jutElements = new JUTElements();

	// get active editor if nothing selected
	if ((selection == null || selection.isEmpty())
		&& fileEditorInput == null) {
	    IEditorInput editorInput = EclipseUIUtils.getEditorInput();

	    if (editorInput != null && editorInput instanceof IFileEditorInput) {
		fileEditorInput = (IFileEditorInput) editorInput;
	    } else {
		throw new JUTWarning(Messages.General_warning_nothing_selected);
	    }
	}

	// get JUT-Elements
	IJavaProject project = JDTUtils.getProject(selection, fileEditorInput);

	if (project == null || !project.exists()) {
	    throw new JUTWarning(Messages.General_warning_nothing_selected);
	}

	Vector<IJavaElement> elements = JDTUtils.getCompilationUnits(selection,
		fileEditorInput);

	Vector<ICompilationUnit> cuList;
	cuList = new Vector<ICompilationUnit>();

	for (IJavaElement element : elements) {
	    if (element instanceof ICompilationUnit) {
		cuList.add((ICompilationUnit) element);
	    }
	}

	if (cuList.size() > 0) {
	    // init projects with cu
	    jutElements.initProjects(project, cuList.firstElement());

	    jutElements.initClassesAndPackages(cuList);

	    if (!jutElements.getClassesAndPackages().getBaseClass().exists()) {
		return jutElements;
	    }

	    // base-class constructors and methods
	    jutElements
		    .setConstructorsAndMethods(getConstructorsAndMethods(jutElements
			    .getClassesAndPackages().getBaseClass()));

	    // set selected method
	    IMethod selectedMethod = null;

	    if (selection != null) {
		Object firstElement = selection.getFirstElement();

		if (firstElement != null && firstElement instanceof IMethod) {
		    selectedMethod = (IMethod) firstElement;
		}
	    } else if (fileEditorInput != null) {
		selectedMethod = JDTUtils.getSelectedMethod(fileEditorInput);
	    }

	    jutElements.getConstructorsAndMethods().setSelectedMethod(
		    selectedMethod);

	} else {
	    // init projects without cu
	    jutElements.initProjects(project);
	}

	return jutElements;
    }

    private JUTConstructorsAndMethods getConstructorsAndMethods(
	    ICompilationUnit baseclass) {

	Vector<IMethod> baseclassConstructors = new Vector<IMethod>();
	Vector<IMethod> baseclassMethods = new Vector<IMethod>();

	try {
	    for (IType type : baseclass.getTypes()) {
		for (IMethod method : type.getMethods()) {
		    if (method.isConstructor()) {
			baseclassConstructors.add(method);
		    } else {
			baseclassMethods.add(method);
		    }
		}
	    }
	} catch (JavaModelException e) {
	    throw new RuntimeException(e);
	}

	JUTConstructorsAndMethods constructorsAndMethods = new JUTConstructorsAndMethods();
	constructorsAndMethods.setBaseClassConstructors(baseclassConstructors);
	constructorsAndMethods.setBaseClassMethods(baseclassMethods);

	return constructorsAndMethods;
    }

    /**
     * Runs the generator-wizard.
     * 
     * @param model
     * @param workbenchPart
     * @return boolean true
     */
    private boolean runGeneratorWizard(GeneratorModel model,
	    IWorkbenchWindow workbenchPart) {

	GeneratorWizard wizard = new GeneratorWizard(model);

	if (JUTPreferences.isShowSettingsBeforeGenerate()) {
	    WizardDialog dialog = new WizardDialog(workbenchPart.getShell(), wizard);
	    dialog.create();

	    wizard.initPages();

	    dialog.open();
	} else {
	    wizard.addPages();
	    wizard.performFinish();
	}
	return wizard.isFinished();
    }

    /**
     * Indicates if an error occurred.
     * 
     * @return true if an error occurred
     */
    public boolean isError() {
	return error;
    }

    /**
     * Sets the error-flag.
     * 
     * @param error
     */
    public void setError(boolean error) {
	this.error = error;
    }

    /**
     * switches between test-class and test-subject
     * 
     * @throws CoreException
     */
    public boolean switchClass(IWorkbenchWindow activeWorkbenchWindow,
	    IStructuredSelection selection) throws JUTException, JUTWarning,
	    CoreException {
	JUTElements jutElements = detectJUTElements(selection, null);
	if (jutElements == null) {
	    return false;
	}

	return switchClass(activeWorkbenchWindow, jutElements);
    }

    /**
     * switches between test-class and test-subject
     * 
     * @throws CoreException
     */
    public boolean switchClass(IWorkbenchWindow activeWorkbenchWindow,
	    IFileEditorInput fileEditorInput) throws JUTException, JUTWarning,
	    CoreException {
	JUTElements jutElements = detectJUTElements(null, fileEditorInput);
	if (jutElements == null) {
	    return false;
	}
	return switchClass(activeWorkbenchWindow, jutElements);
    }

    private boolean switchClass(IWorkbenchWindow activeWorkbenchWindow,
	    JUTElements jutElements) throws JUTException, JUTWarning,
	    JavaModelException {
	JUTConstructorsAndMethods constructorsAndMethods = jutElements
		.getConstructorsAndMethods();
	if (constructorsAndMethods == null) {
	    throw new JUTWarning(
		    "No constructors and methods were found! Perhaps the preferences are wrong or some manual changes were done which are not compatible. "
			    + "Elsewise create an issue or contact the JUnit-Tools-Team.");
	}

	IMethod selectedMethod = jutElements.getConstructorsAndMethods()
		.getSelectedMethod();
	JUTProjects projects = jutElements.getProjects();
	JUTClassesAndPackages classesAndPackages = jutElements
		.getClassesAndPackages();
	ICompilationUnit classToOpen;
	Shell shell = activeWorkbenchWindow.getShell();

	MethodRef mr = null;
	if (projects.isBaseProjectSelected()) {
	    classToOpen = classesAndPackages.getTestClass();

	    if (selectedMethod != null) {
		mr = new MethodRef(
			GeneratorUtils.createTestMethodName(selectedMethod
				.getElementName()),
			selectedMethod.getSignature());
	    }
	} else {
	    classToOpen = classesAndPackages.getBaseClass();

	    if (selectedMethod != null) {
		mr = GeneratorUtils.getMethodRef(selectedMethod);
	    }
	}

	if (classToOpen != null && classToOpen.exists()) {
	    EclipseUIUtils.openInEditor(shell,
		    (IFile) classToOpen.getResource());

	    if (selectedMethod != null) {
		EclipseUIUtils.selectMethodInEditor(mr);
	    }

	    return true;
	} else if (projects.isBaseProjectSelected()) {
	    boolean result = MessageDialog
		    .openConfirm(
			    shell,
			    "Generate new test-class?",
			    "No existing test-class found (perhaps the configuration is wrong). Would you like to generate a new test-class?");

	    if (result) {
		generateTestclass(activeWorkbenchWindow, jutElements, false);
	    }
	} else {
	    return false;
	}

	return true;
    }

}
