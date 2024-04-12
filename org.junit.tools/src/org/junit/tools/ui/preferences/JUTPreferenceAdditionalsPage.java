package org.junit.tools.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.tools.Activator;
import org.junit.tools.messages.Messages;
import org.junit.tools.preferences.IJUTPreferenceConstants;
import org.junit.tools.preferences.JUTPreferences;

/**
 * The page for the additional import and additional field preferences.
 * 
 * @author JUnit-Tools-Team
 * 
 */
public class JUTPreferenceAdditionalsPage extends PreferencePage implements
	IWorkbenchPreferencePage, IJUTPreferenceConstants {
    public JUTPreferenceAdditionalsPage() {
    }

    private List listAdditionalImports;
    private Text newImportDeclaration;
    private GridData data_2;
    private GridData gd_newImportDeclaration;
    private GridData gd_cmpImportDeclaration;

    private List listAdditionalFields;
    private Text newFieldDeclaration;
    private GridData data_3;
    private GridData gd_newFieldDeclaration;
    private GridData gd_cmpFieldDeclaration;

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

	// test-method-filter name
	Group cmpAnnotationsTestClass = new Group(cmpMain, SWT.NONE);
	cmpAnnotationsTestClass
		.setText(Messages.JUTPreferenceAdditionalsPage_imports);
	GridData data;
	gd_cmpImportDeclaration = new GridData(GridData.FILL_HORIZONTAL);
	gd_cmpImportDeclaration.verticalAlignment = SWT.FILL;
	gd_cmpImportDeclaration.grabExcessVerticalSpace = true;
	gd_cmpImportDeclaration.grabExcessHorizontalSpace = true;
	cmpAnnotationsTestClass.setLayoutData(gd_cmpImportDeclaration);
	cmpAnnotationsTestClass.setLayout(new GridLayout());

	listAdditionalImports = new List(cmpAnnotationsTestClass, SWT.BORDER | SWT.V_SCROLL);
	listAdditionalImports.setItems(getAdditionalImports());

	// Create a data that takes up the extra space in the dialog and spans
	// both columns.
	data = new GridData(GridData.FILL_BOTH);
	listAdditionalImports.setLayoutData(data);

	Composite buttonCompositeFilterName = new Composite(cmpAnnotationsTestClass, SWT.NULL);

	GridLayout buttonLayout = new GridLayout();
	buttonLayout.numColumns = 2;
	buttonCompositeFilterName.setLayout(buttonLayout);

	// Create a data that takes up the extra space in the dialog and spans
	// both columns.
	data_2 = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
	data_2.verticalAlignment = SWT.CENTER;
	data_2.grabExcessVerticalSpace = false;
	buttonCompositeFilterName.setLayoutData(data_2);

	Button addButton = new Button(buttonCompositeFilterName, SWT.PUSH | SWT.CENTER);

	addButton.setText(Messages.JUTPreferencePage_addToList);
	addButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		String newEntry = newImportDeclaration.getText();
		for (String item : listAdditionalImports.getItems()) {
		    if (newEntry.equals(item)) {
			return;
		    }
		}

		listAdditionalImports.add(newEntry,
			listAdditionalImports.getItemCount());
	    }
	});

	newImportDeclaration = new Text(buttonCompositeFilterName, SWT.BORDER);
	listAdditionalImports.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		if (listAdditionalImports.getSelection().length > 0) {
		    newImportDeclaration.setText(listAdditionalImports.getSelection()[0]);
		}
	    }
	});
	// Create a data that takes up the extra space in the dialog .
	gd_newImportDeclaration = new GridData(GridData.FILL_HORIZONTAL);
	gd_newImportDeclaration.grabExcessHorizontalSpace = true;
	newImportDeclaration.setLayoutData(gd_newImportDeclaration);

	Button removeButton = new Button(buttonCompositeFilterName, SWT.PUSH
		| SWT.CENTER);

	removeButton.setText(Messages.JUTPreferencePage_removeFromList);
	removeButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		listAdditionalImports.remove(listAdditionalImports
			.getSelectionIndex());
	    }
	});

	data = new GridData();
	data.horizontalSpan = 2;
	removeButton.setLayoutData(data);

	// test-method-filter modifier
	Group cmpAnnotationsMockClass = new Group(cmpMain, SWT.NONE);
	cmpAnnotationsMockClass
		.setText(Messages.JUTPreferenceAdditionalsPage_fields);

	// Create a data that takes up the extra space in the dialog .
	gd_cmpFieldDeclaration = new GridData(GridData.FILL_HORIZONTAL);
	gd_cmpFieldDeclaration.verticalAlignment = SWT.FILL;
	gd_cmpFieldDeclaration.grabExcessVerticalSpace = true;
	gd_cmpFieldDeclaration.grabExcessHorizontalSpace = true;
	cmpAnnotationsMockClass.setLayoutData(gd_cmpFieldDeclaration);

	GridLayout gl_cmpAnnotationsMockClass = new GridLayout();
	cmpAnnotationsMockClass.setLayout(gl_cmpAnnotationsMockClass);

	listAdditionalFields = new List(cmpAnnotationsMockClass, SWT.BORDER | SWT.V_SCROLL);
	listAdditionalFields.setItems(getAdditionalFields());

	// Create a data that takes up the extra space in the dialog and spans
	// both columns.
	data = new GridData(GridData.FILL_BOTH);
	listAdditionalFields.setLayoutData(data);

	Composite buttonComposite = new Composite(cmpAnnotationsMockClass, SWT.NULL);

	buttonLayout = new GridLayout();
	buttonLayout.numColumns = 2;
	buttonComposite.setLayout(buttonLayout);

	// Create a data that takes up the extra space in the dialog and spans
	// both columns.
	data_3 = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
	data_3.verticalAlignment = SWT.CENTER;
	data_3.grabExcessVerticalSpace = false;
	buttonComposite.setLayoutData(data_3);

	addButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);

	addButton.setText(Messages.JUTPreferencePage_addToList);
	addButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		String newEntry = newFieldDeclaration.getText();
		for (String item : listAdditionalFields.getItems()) {
		    if (newEntry.equals(item)) {
			return;
		    }
		}

		listAdditionalFields.add(newEntry, listAdditionalFields.getItemCount());
	    }
	});

	newFieldDeclaration = new Text(buttonComposite, SWT.BORDER);
	listAdditionalFields.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		if (listAdditionalFields.getSelection().length > 0) {
		    newFieldDeclaration.setText(listAdditionalFields.getSelection()[0]);
		}
	    }
	});
	// Create a data that takes up the extra space in the dialog .
	gd_newFieldDeclaration = new GridData(GridData.FILL_HORIZONTAL);
	gd_newFieldDeclaration.grabExcessHorizontalSpace = true;
	newFieldDeclaration.setLayoutData(gd_newFieldDeclaration);

	removeButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);

	removeButton.setText(Messages.JUTPreferencePage_removeFromList);
	removeButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		listAdditionalFields.remove(listAdditionalFields
			.getSelectionIndex());
	    }
	});

	data = new GridData();
	data.horizontalSpan = 2;
	removeButton.setLayoutData(data);

	return cmpMain;
    }

    /**
     * @see IWorkbenchPreferencePage#init(IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
	// Initialize the preference store we wish to use
	setPreferenceStore(Activator.getDefault().getPreferenceStore());
	setDescription(Messages.JUTPreferenceAdditionalsPage_description);
    }

    @Override
    protected void performDefaults() {
	listAdditionalImports.setItems(getDefaultAdditionalImports());
	listAdditionalFields.setItems(getDefaultAdditionalFields());
    }

    @Override
    public boolean performOk() {
	setAdditionalImports(listAdditionalImports.getItems());
	setAdditionalFields(listAdditionalFields.getItems());
	return super.performOk();
    }

    public void setAdditionalImports(String[] values) {
	getPreferenceStore().setValue(ADDITIONAL_IMPORTS, JUTPreferences.convertFromArray(values));
    }

    public void setAdditionalFields(String[] values) {
	getPreferenceStore().setValue(ADDITIONAL_FIELDS, JUTPreferences.convertFromArray(values));
    }

    public String[] getDefaultAdditionalImports() {
	return JUTPreferences.convertToArray(getPreferenceStore().getDefaultString(ADDITIONAL_IMPORTS));
    }

    public String[] getAdditionalImports() {
	return JUTPreferences.convertToArray(getPreferenceStore().getString(ADDITIONAL_IMPORTS));
    }

    public String[] getDefaultAdditionalFields() {
	return JUTPreferences.convertToArray(getPreferenceStore().getDefaultString(ADDITIONAL_FIELDS));
    }

    public String[] getAdditionalFields() {
	return JUTPreferences.convertToArray(getPreferenceStore().getString(ADDITIONAL_FIELDS));
    }

}