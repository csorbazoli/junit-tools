package org.junit.tools.ui.generator.wizards.pages;

import java.util.Vector;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.junit.tools.generator.model.GeneratorModel;
import org.junit.tools.ui.generator.swt.view.GeneratorMainView;
import org.junit.tools.ui.generator.wizards.GeneratorWizardMain;

/**
 * The page for the method-selection and the main settings.
 * 
 * @author JUnit-Tools-Team
 * 
 */
public class GeneratorWizardMainPage extends GeneratorWizardBasePage {

    private Vector<IMethod> methods;
    private GeneratorMainView mainView = null;

    public GeneratorWizardMainPage(String title, String description,
	    String pageName, GeneratorModel model) {
	super(title, description, pageName, model);
    }

    @Override
    public void createControl(Composite parent) {

	mainView = new GeneratorMainView(parent, SWT.NONE);
	setControl(mainView);
    }

    @Override
    protected void createController(GeneratorModel model) {
	this.controller = new GeneratorWizardMain(model, this);
    }

    @Override
    public GeneratorWizardMain getController() {
	return (GeneratorWizardMain) controller;
    }

    public Vector<IMethod> getMethods() {
	return methods;
    }

    public GeneratorMainView getView() {
	return mainView;
    }
}
