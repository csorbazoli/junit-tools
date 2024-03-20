package org.junit.tools.ui.preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.tools.Activator;
import org.junit.tools.messages.Messages;
import org.junit.tools.preferences.IJUTPreferenceConstants;

/**
 * The page for the standard method body preferences (i.e. before, after
 * method).
 * 
 * @author csorbazoli
 * 
 */
public class JUTPreferenceStandardMethodsPage extends PreferencePage implements
	IWorkbenchPreferencePage, IJUTPreferenceConstants {

    private final Logger logger = Logger.getLogger(JUTPreferenceStandardMethodsPage.class.getName());

    private final Map<String, BooleanFieldEditor> checkBoxMap = new HashMap<>();
    private final Map<String, StringFieldEditor> bodyEditorMap = new HashMap<>();

    public JUTPreferenceStandardMethodsPage() {
	// default constructor
    }

    /**
     * @see PreferencePage#createContents(Composite)
     * @param parent
     */
    @Override
    protected Control createContents(Composite parent) {
	Composite cmpMain = new Composite(parent, SWT.NONE);

	GridData cmpMainLayoutData = new GridData(GridData.FILL_HORIZONTAL);
	cmpMainLayoutData.grabExcessHorizontalSpace = true;
	cmpMain.setLayoutData(cmpMainLayoutData);
	cmpMain.setLayout(new GridLayout());

	createCheckBoxWithTextEditor(cmpMain,
		BEFORE_CLASS_METHOD_ENABLED, Messages.JUTPreferenceStandardMethodsPage_before_class_enabled,
		BEFORE_CLASS_METHOD_BODY, Messages.JUTPreferenceStandardMethodsPage_before_class_body);
	createCheckBoxWithTextEditor(cmpMain,
		BEFORE_METHOD_ENABLED, Messages.JUTPreferenceStandardMethodsPage_before_test_enabled,
		BEFORE_METHOD_BODY, Messages.JUTPreferenceStandardMethodsPage_before_test_body);
	createCheckBoxWithTextEditor(cmpMain,
		AFTER_METHOD_ENABLED, Messages.JUTPreferenceStandardMethodsPage_after_test_enabled,
		AFTER_METHOD_BODY, Messages.JUTPreferenceStandardMethodsPage_after_test_body);
	createCheckBoxWithTextEditor(cmpMain,
		AFTER_CLASS_METHOD_ENABLED, Messages.JUTPreferenceStandardMethodsPage_after_class_enabled,
		AFTER_CLASS_METHOD_BODY, Messages.JUTPreferenceStandardMethodsPage_after_class_body);

	return cmpMain;
    }

    private void createCheckBoxWithTextEditor(Composite cmpMain, String checkBoxKey, String checkBoxLabel,
	    String textAreaKey, String textAreaLabel) {
	// default values for simple types
	Group cmpCheckboxwithTextEditor = new Group(cmpMain, SWT.NONE);
	cmpCheckboxwithTextEditor.setText("");
	GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
	data.verticalAlignment = SWT.FILL;
	data.grabExcessVerticalSpace = true;
	data.grabExcessHorizontalSpace = true;
	cmpCheckboxwithTextEditor.setLayoutData(data);
	GridLayout gridLayout = new GridLayout(5, false);
	cmpCheckboxwithTextEditor.setLayout(gridLayout);

	BooleanFieldEditor toggleButton = new BooleanFieldEditor(checkBoxKey, checkBoxLabel, cmpCheckboxwithTextEditor);
	data = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
	data.verticalAlignment = SWT.FILL;
	data.grabExcessVerticalSpace = true;
	data.grabExcessHorizontalSpace = true;
	checkBoxMap.put(checkBoxKey, toggleButton);
	StringFieldEditor bodyEditor = new StringFieldEditor(textAreaKey, "", -1, 3, StringFieldEditor.VALIDATE_ON_FOCUS_LOST,
		cmpCheckboxwithTextEditor);
	bodyEditor.setStringValue(getPreferenceStore().getString(textAreaKey));
	Object layoutData = bodyEditor.getTextControl(cmpCheckboxwithTextEditor).getLayoutData();
	if (layoutData instanceof GridData) {
	    GridData gd = (GridData) layoutData;
	    gd.grabExcessVerticalSpace = true;
	    gd.verticalAlignment = SWT.FILL;
	}
	bodyEditorMap.put(textAreaKey, bodyEditor);

	toggleButton.setPropertyChangeListener(new IPropertyChangeListener() {

	    @Override
	    public void propertyChange(PropertyChangeEvent event) {
		bodyEditor.setEnabled(toggleButton.getBooleanValue(), cmpCheckboxwithTextEditor);
	    }
	});
	toggleButton.load();
	boolean enabled = getPreferenceStore().getBoolean(checkBoxKey);
	((Button) toggleButton.getDescriptionControl(cmpCheckboxwithTextEditor)).setSelection(enabled);
	bodyEditor.setEnabled(enabled, cmpCheckboxwithTextEditor);

    }

    /**
     * @see IWorkbenchPreferencePage#init(IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
	// Initialize the preference store we wish to use
	setPreferenceStore(Activator.getDefault().getPreferenceStore());
	setDescription(Messages.JUTPreferenceStandardMethodsPage_description);
    }

    @Override
    protected void performDefaults() {
	// this might not be needed at all as the editors are bound to their
	// corresponding preference values
	checkBoxMap.entrySet()
		.forEach(entry -> setDefaultValueFor(entry.getKey(), entry.getValue()));
	bodyEditorMap.entrySet()
		.forEach(entry -> setDefaultValueFor(entry.getKey(), entry.getValue()));
    }

    private void setDefaultValueFor(String key, BooleanFieldEditor checkBox) {
	checkBox.setEnabled(getPreferenceStore().getBoolean(key), getShell());
    }

    private void setDefaultValueFor(String key, StringFieldEditor bodyEditor) {
	bodyEditor.setStringValue(getPreferenceStore().getString(key));
    }

    @Override
    public boolean performOk() {
	checkBoxMap.entrySet()
		.forEach(entry -> getPreferenceStore().setValue(entry.getKey(), entry.getValue().getBooleanValue()));
	bodyEditorMap.entrySet()
		.forEach(entry -> getPreferenceStore().setValue(entry.getKey(), entry.getValue().getStringValue()));
	return super.performOk();
    }

}