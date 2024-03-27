package org.junit.tools.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.tools.Activator;
import org.junit.tools.generator.utils.GeneratorUtils;
import org.junit.tools.messages.Messages;
import org.junit.tools.preferences.IJUTPreferenceConstants;

/**
 * The page for the main preferences.
 * 
 * @author Robert Streng
 * 
 */
public class JUTPreferenceMainPage extends FieldEditorPreferencePage implements
	IWorkbenchPreferencePage, IJUTPreferenceConstants {

    private Label folderExample;
    private Label classExample;
    private Label springClassExample;
    private Label methodExample;
    private Label mvcMethodExample;
    private Label superTypeExample;

    private boolean srcFolderIsEqual = false;
    private boolean classIsEqual = false;

    private StringFieldEditor fieldSrcFolder;
    private StringFieldEditor fieldClassPre;
    private StringFieldEditor fieldClassPost;
    private StringFieldEditor fieldSpringClassPost;
    private StringFieldEditor fieldMethodPre;
    private StringFieldEditor fieldMethodPost;
    private StringFieldEditor fieldMvcMethodPost;
    private StringFieldEditor fieldSuperType; // can be useful for EasyMockSupport

    public JUTPreferenceMainPage() {
	super(FieldEditorPreferencePage.GRID);
    }

    @Override
    public void init(IWorkbench workbench) {
	setPreferenceStore(Activator.getDefault().getPreferenceStore());
	setDescription(Messages.JUTPreferenceMainPage_description_Main_settings);
	setValid(true);
    }

    @Override
    protected void createFieldEditors() {

	// source folder
	fieldSrcFolder = new StringFieldEditor(TEST_SOURCE_FOLDER_NAME,
		Messages.JUTPreferenceMainPage_Test_source_folder_name,
		getFieldEditorParent()) {

	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setExampleValueFolder();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setExampleValueFolder();
	    }
	};

	addField(fieldSrcFolder);

	folderExample = new Label(getFieldEditorParent(), SWT.NONE);
	folderExample.setLayoutData(createGridDataWithIndent());

	// class prefix
	fieldClassPre = new StringFieldEditor(TEST_CLASS_PREFIX,
		Messages.JUTPreferenceMainPage_Test_class_prefix,
		getFieldEditorParent()) {
	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setExampleValueClass();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setExampleValueClass();
	    }
	};
	addField(fieldClassPre);

	// class postfix
	fieldClassPost = new StringFieldEditor(TEST_CLASS_POSTFIX,
		Messages.JUTPreferenceMainPage_Test_class_postfix,
		getFieldEditorParent()) {
	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setExampleValueClass();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setExampleValueClass();
	    }
	};
	addField(fieldClassPost);

	classExample = new Label(getFieldEditorParent(), SWT.NONE);
	classExample.setLayoutData(createGridDataWithIndent());

	// Spring class postfix
	fieldSpringClassPost = new StringFieldEditor(SPRING_TEST_CLASS_POSTFIX,
		Messages.JUTPreferenceMainPage_Spring_Test_class_postfix,
		getFieldEditorParent()) {
	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setExampleValueSpringClass();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setExampleValueSpringClass();
	    }
	};
	addField(fieldSpringClassPost);

	springClassExample = new Label(getFieldEditorParent(), SWT.NONE);
	springClassExample.setLayoutData(createGridDataWithIndent());

	// method prefix
	fieldMethodPre = new StringFieldEditor(TEST_METHOD_PREFIX,
		Messages.JUTPreferenceMainPage_Test_method_prefix,
		getFieldEditorParent()) {
	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setExampleValueMethod();
		setMvcExampleValueMethod();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setExampleValueMethod();
		setMvcExampleValueMethod();
	    }
	};
	addField(fieldMethodPre);

	// method postfix
	fieldMethodPost = new StringFieldEditor(TEST_METHOD_POSTFIX,
		Messages.JUTPreferenceMainPage_Test_Method_postfix,
		getFieldEditorParent()) {
	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setExampleValueMethod();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setExampleValueMethod();
	    }

	};
	addField(fieldMethodPost);

	methodExample = new Label(getFieldEditorParent(), SWT.NONE);
	methodExample.setLayoutData(createGridDataWithIndent());

	// MVC method postfix
	fieldMvcMethodPost = new StringFieldEditor(TEST_MVC_METHOD_POSTFIX,
		Messages.JUTPreferenceMainPage_Test_Mvc_Method_postfix,
		getFieldEditorParent()) {
	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setMvcExampleValueMethod();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setMvcExampleValueMethod();
	    }

	};
	addField(fieldMvcMethodPost);

	mvcMethodExample = new Label(getFieldEditorParent(), SWT.NONE);
	mvcMethodExample.setLayoutData(createGridDataWithIndent());

	// super type
	fieldSuperType = new StringFieldEditor(TEST_CLASS_SUPER_TYPE,
		Messages.JUTPreferenceMainPage_Testclass_supertype,
		getFieldEditorParent()) {
	    @Override
	    protected void valueChanged() {
		super.valueChanged();
		setExampleValueSuperType();
	    }

	    @Override
	    protected void doLoad() {
		super.doLoad();
		setExampleValueSuperType();
	    }

	};
	addField(fieldSuperType);

	superTypeExample = new Label(getFieldEditorParent(), SWT.NONE);
	superTypeExample.setLayoutData(createGridDataWithIndent());

	addBlankLine();

	// mock framework
	addField(new RadioGroupFieldEditor(MOCK_FRAMEWORK, Messages.JUTPreferenceMainPage_Mock_Framework, 1,
		new String[][] {
			{ "Mockito", MOCKFW_MOCKITO },
			{ "EasyMock", MOCKFW_EASYMOCK }
		},
		getFieldEditorParent(),
		true));

	// JUnit version
	addField(new RadioGroupFieldEditor(JUNIT_VERSION, Messages.JUTPreferenceMainPage_JUnit_Version, 1,
		new String[][] {
			{ "JUnit4", "4" },
			{ "JUnit5", "5" }
		},
		getFieldEditorParent(),
		true));

	addField(new BooleanFieldEditor(SHOW_SETTINGS_BEFORE_GENERATE, Messages.JUTPreferenceMainPage_Show_Settings_Before_Generate,
		getFieldEditorParent()));

	addField(new BooleanFieldEditor(GHERKIN_STYLE_ENABLED, Messages.JUTPreferenceMainPage_Gherkin_Style_Enabled,
		getFieldEditorParent()));

	addField(new BooleanFieldEditor(ASSERTJ_ENABLED, Messages.JUTPreferenceMainPage_AssertJ_Enabled,
		getFieldEditorParent()));

	addField(new BooleanFieldEditor(REPLAYALL_VERIFYALL_ENABLED, Messages.JUTPreferenceMainPage_ReplayAllVerifyAll_Enabled,
		getFieldEditorParent()));

	addField(new BooleanFieldEditor(REPEATING_TEST_METHODS_ENABLED, Messages.JUTPreferenceMainPage_Repeating_Test_Methods_Enabled,
		getFieldEditorParent()));

	adjustGridLayout();
    }

    protected void setExampleValueFolder() {

	if ("".equals(fieldSrcFolder.getStringValue())
		|| "src/main/java".equals(fieldSrcFolder.getStringValue())) {
	    srcFolderIsEqual = true;

	    folderExample
		    .setText("Source folder and test source folder is equal");
	} else {
	    srcFolderIsEqual = false;

	    folderExample
		    .setText("The source folder target for the test elements is \""
			    + fieldSrcFolder.getStringValue() + "\"");
	}

	folderExample.getParent().layout();
    }

    private void setExampleValueClass() {

	if ("".equals(fieldClassPre.getStringValue())
		&& "".equals(fieldClassPost.getStringValue())) {
	    classIsEqual = true;

	    if (srcFolderIsEqual) {
		classExample.setText("Class for base and test is equal");
	    } else {
		classExample
			.setText("The base and test name for the class is equal");
	    }
	} else {
	    classIsEqual = false;

	    classExample
		    .setText("If the class under test is \"Calculator\" the test class is \""
			    + fieldClassPre.getStringValue()
			    + "Calculator"
			    + fieldClassPost.getStringValue() + "\"");
	}

	classExample.getParent().layout();

	setExampleValueMethod();
	setMvcExampleValueMethod();
    }

    private void setExampleValueSpringClass() {

	if ("".equals(fieldClassPre.getStringValue())
		&& "".equals(fieldSpringClassPost.getStringValue())) {
	    classIsEqual = true;

	    if (srcFolderIsEqual) {
		springClassExample.setText("Class for base and test is equal");
	    } else {
		springClassExample
			.setText("The base and test name for the class is equal");
	    }
	} else {
	    classIsEqual = false;

	    springClassExample
		    .setText("If the class under test is \"Calculator\" the test class is \""
			    + fieldClassPre.getStringValue()
			    + "Calculator"
			    + fieldSpringClassPost.getStringValue() + "\"");
	}

	springClassExample.getParent().layout();

	setExampleValueMethod();
	setMvcExampleValueMethod();
    }

    protected void setExampleValueMethod() {
	checkValid();

	if ("".equals(fieldMethodPre.getStringValue())
		&& "".equals(fieldMethodPost.getStringValue())) {
	    if (srcFolderIsEqual
		    && classIsEqual) {
		methodExample.setText("Base and test method is equal");
	    } else {
		methodExample
			.setText("Base method name and test method name are equal");
	    }
	} else {
	    String methodName = "";
	    if (fieldMethodPre.getStringValue().equals("")) {
		methodName = "calculate";
	    } else {
		methodName = "Calculate";
	    }

	    methodExample
		    .setText("If the method under test is \"calculate\" the test method is \""
			    + fieldMethodPre.getStringValue()
			    + methodName
			    + GeneratorUtils.firstCharToUpper(fieldMethodPost
				    .getStringValue())
			    + "\"");
	}

	methodExample.getParent().layout();
	getFieldEditorParent().layout();
    }

    protected void setMvcExampleValueMethod() {
	checkValid();

	if ("".equals(fieldMethodPre.getStringValue())
		&& "".equals(fieldMvcMethodPost.getStringValue())) {
	    if (srcFolderIsEqual
		    && classIsEqual) {
		mvcMethodExample.setText("Base and test method is equal");
	    } else {
		mvcMethodExample
			.setText("Base method name and test method name are equal");
	    }
	} else {
	    String methodName = "";
	    if (fieldMethodPre.getStringValue().equals("")) {
		methodName = "calculate";
	    } else {
		methodName = "Calculate";
	    }

	    mvcMethodExample
		    .setText("If the method under test is \"calculate\" the test method is \""
			    + fieldMethodPre.getStringValue()
			    + methodName
			    + GeneratorUtils.firstCharToUpper(fieldMvcMethodPost
				    .getStringValue())
			    + "\"");
	}

	mvcMethodExample.getParent().layout();
	getFieldEditorParent().layout();
    }

    private void checkValid() {
	if (srcFolderIsEqual && classIsEqual) {
	    setErrorMessage("Between the elements under test and the corresponding test elements must be a difference!");
	    setValid(false);
	} else {
	    // reset error message
	    setErrorMessage(null);
	    setValid(true);
	}
    }

    protected void setExampleValueSuperType() {
	checkValid();

	if ("".equals(fieldSuperType.getStringValue())) {
	    superTypeExample.setText("No super type is defined");
	} else {
	    superTypeExample
		    .setText("The default super class for the test class is \""
			    + fieldSuperType.getStringValue() + "\"");
	}

	superTypeExample.getParent().layout();
	getFieldEditorParent().layout();
    }

    private void addBlankLine() {
	Label label = new Label(getFieldEditorParent(), SWT.NONE);
	label.setLayoutData(createGridData());
    }

    @Override
    public Point computeSize() {
	Composite fieldEditorParent2 = getFieldEditorParent();
	GridData gd = (GridData) fieldEditorParent2.getLayoutData();

	if (gd != null) {
	    gd.widthHint = 500;
	}
	return super.computeSize();
    }

    private GridData createGridData() {
	GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
	gridData.horizontalSpan = 2;

	return gridData;
    }

    private GridData createGridDataWithIndent() {
	GridData gridData = createGridData();
	gridData.horizontalIndent = 15;
	return gridData;
    }

}
