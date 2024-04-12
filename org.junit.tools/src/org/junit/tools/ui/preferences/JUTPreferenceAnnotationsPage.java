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
 * The page for the annotations preferences.
 * 
 * @author JUnit-Tools-Team
 * 
 */
public class JUTPreferenceAnnotationsPage extends PreferencePage implements
	IWorkbenchPreferencePage, IJUTPreferenceConstants {
    public JUTPreferenceAnnotationsPage() {
    }

    private List listAnnotationsTestClass;
    private Text newAnnotationTestClass;
    private GridData data_2;
    private GridData gd_newAnnotationTestClass;
    private GridData gd_cmpAnnotationsTestClass;

    private List listAnnotationsSpringClass;
    private Text newAnnotationSpringClass;
    private GridData data_3;
    private GridData gd_newAnnotationSpringClass;
    private GridData gd_cmpAnnotationsSpringClass;

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
		.setText(Messages.JUTPreferenceAnnotationsPage_annotations);
	GridData data;
	gd_cmpAnnotationsTestClass = new GridData(GridData.FILL_HORIZONTAL);
	gd_cmpAnnotationsTestClass.verticalAlignment = SWT.FILL;
	gd_cmpAnnotationsTestClass.grabExcessVerticalSpace = true;
	gd_cmpAnnotationsTestClass.grabExcessHorizontalSpace = true;
	cmpAnnotationsTestClass.setLayoutData(gd_cmpAnnotationsTestClass);
	cmpAnnotationsTestClass.setLayout(new GridLayout());

	listAnnotationsTestClass = new List(cmpAnnotationsTestClass, SWT.BORDER | SWT.V_SCROLL);
	listAnnotationsTestClass.setItems(getAnnotationsTestClass());

	// Create a data that takes up the extra space in the dialog and spans
	// both columns.
	data = new GridData(GridData.FILL_BOTH);
	listAnnotationsTestClass.setLayoutData(data);

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

	addButton.setText(Messages.JUTPreferencePage_addToList); // $NON-NLS-1$
	addButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		String newEntry = newAnnotationTestClass.getText();
		for (String item : listAnnotationsTestClass.getItems()) {
		    if (newEntry.equals(item)) {
			return;
		    }
		}

		listAnnotationsTestClass.add(newEntry,
			listAnnotationsTestClass.getItemCount());
	    }
	});

	newAnnotationTestClass = new Text(buttonCompositeFilterName, SWT.BORDER);

	listAnnotationsTestClass.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		if (listAnnotationsTestClass.getSelection().length > 0) {
		    newAnnotationTestClass.setText(listAnnotationsTestClass.getSelection()[0]);
		}
	    }
	});

	// Create a data that takes up the extra space in the dialog .
	gd_newAnnotationTestClass = new GridData(GridData.FILL_HORIZONTAL);
	gd_newAnnotationTestClass.grabExcessHorizontalSpace = true;
	newAnnotationTestClass.setLayoutData(gd_newAnnotationTestClass);

	Button removeButton = new Button(buttonCompositeFilterName, SWT.PUSH
		| SWT.CENTER);

	removeButton.setText(Messages.JUTPreferencePage_removeFromList);
	removeButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		listAnnotationsTestClass.remove(listAnnotationsTestClass
			.getSelectionIndex());
	    }
	});

	data = new GridData();
	data.horizontalSpan = 2;
	removeButton.setLayoutData(data);

	// test-method-filter modifier
	Group cmpAnnotationsMockClass = new Group(cmpMain, SWT.NONE);
	cmpAnnotationsMockClass
		.setText(Messages.JUTPreferenceAnnotationsPage_springRelatedAnnotations);

	// Create a data that takes up the extra space in the dialog .
	gd_cmpAnnotationsSpringClass = new GridData(GridData.FILL_HORIZONTAL);
	gd_cmpAnnotationsSpringClass.verticalAlignment = SWT.FILL;
	gd_cmpAnnotationsSpringClass.grabExcessVerticalSpace = true;
	gd_cmpAnnotationsSpringClass.grabExcessHorizontalSpace = true;
	cmpAnnotationsMockClass.setLayoutData(gd_cmpAnnotationsSpringClass);

	GridLayout gl_cmpAnnotationsMockClass = new GridLayout();
	cmpAnnotationsMockClass.setLayout(gl_cmpAnnotationsMockClass);

	listAnnotationsSpringClass = new List(cmpAnnotationsMockClass, SWT.BORDER | SWT.V_SCROLL);
	listAnnotationsSpringClass.setItems(getAnnotationsSpringClass());

	// Create a data that takes up the extra space in the dialog and spans
	// both columns.
	data = new GridData(GridData.FILL_BOTH);
	listAnnotationsSpringClass.setLayoutData(data);

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
		String newEntry = newAnnotationSpringClass.getText();
		for (String item : listAnnotationsSpringClass.getItems()) {
		    if (newEntry.equals(item)) {
			return;
		    }
		}

		listAnnotationsSpringClass.add(newEntry, listAnnotationsSpringClass.getItemCount());
	    }
	});

	newAnnotationSpringClass = new Text(buttonComposite, SWT.BORDER);
	listAnnotationsSpringClass.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		if (listAnnotationsSpringClass.getSelection().length > 0) {
		    newAnnotationSpringClass.setText(listAnnotationsSpringClass.getSelection()[0]);
		}
	    }
	});

	// Create a data that takes up the extra space in the dialog .
	gd_newAnnotationSpringClass = new GridData(GridData.FILL_HORIZONTAL);
	gd_newAnnotationSpringClass.grabExcessHorizontalSpace = true;
	newAnnotationSpringClass.setLayoutData(gd_newAnnotationSpringClass);

	removeButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);

	removeButton.setText(Messages.JUTPreferencePage_removeFromList);
	removeButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		listAnnotationsSpringClass.remove(listAnnotationsSpringClass
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
	setDescription(Messages.JUTPreferenceAnnotationsPage_description);
    }

    @Override
    protected void performDefaults() {
	listAnnotationsTestClass.setItems(getDefaultAnnotationsTestClass());
	listAnnotationsSpringClass.setItems(getDefaultAnnotationsSpringClass());
    }

    @Override
    public boolean performOk() {
	setAnnotationsTestClass(listAnnotationsTestClass.getItems());
	setAnnotationsSpringClass(listAnnotationsSpringClass.getItems());
	return super.performOk();
    }

    public void setAnnotationsTestClass(String[] values) {
	getPreferenceStore().setValue(TEST_CLASS_ANNOTATIONS, JUTPreferences.convertFromArray(values));
    }

    public String[] getDefaultAnnotationsTestClass() {
	return JUTPreferences.convertToArray(getPreferenceStore().getDefaultString(TEST_CLASS_ANNOTATIONS));
    }

    public String[] getAnnotationsTestClass() {
	return JUTPreferences.convertToArray(getPreferenceStore().getString(TEST_CLASS_ANNOTATIONS));
    }

    public void setAnnotationsSpringClass(String[] values) {
	getPreferenceStore().setValue(SPRING_ANNOTATIONS, JUTPreferences.convertFromArray(values));
    }

    public String[] getDefaultAnnotationsSpringClass() {
	return JUTPreferences.convertToArray(getPreferenceStore().getDefaultString(SPRING_ANNOTATIONS));
    }

    public String[] getAnnotationsSpringClass() {
	return JUTPreferences.convertToArray(getPreferenceStore().getString(SPRING_ANNOTATIONS));
    }
}