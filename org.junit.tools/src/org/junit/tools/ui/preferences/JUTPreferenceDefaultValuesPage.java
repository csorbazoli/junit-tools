package org.junit.tools.ui.preferences;

import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.junit.tools.Activator;
import org.junit.tools.messages.Messages;
import org.junit.tools.preferences.DefaultValueMapper;
import org.junit.tools.preferences.DefaultValueMapper.DefaultValueMapperBuilder;
import org.junit.tools.preferences.IJUTPreferenceConstants;
import org.junit.tools.preferences.JUTPreferences;
import org.junit.tools.preferences.ValueMapping;

/**
 * The page for the default value preferences.
 * 
 * <ul>
 * <li>Default values for simple types</li>
 * <li>Default values for generic types</li>
 * <li>Default value for Java beans</li>
 * <li>Default value fallback</li>
 * </ul>
 * 
 * @author csorbazoli
 * 
 */
public class JUTPreferenceDefaultValuesPage extends PreferencePage implements
	IWorkbenchPreferencePage, IJUTPreferenceConstants {

    private final Logger logger = Logger.getLogger(JUTPreferenceDefaultValuesPage.class.getName());

    private List listSimpleTypes;
    private Text newSimpleTypeReference;
    private Text newSimpleValueExpression;

    private List listGenericTypes;
    private Text newGenericTypeReference;
    private Text newGenericDefaultValueExpression;

    private Text defaultValueForJavaBeans;
    private Text defaultValueFallback;

    private DefaultValueMapper modifiedMapper;

    // TODO possibility to re-order items (move up/down)

    // TODO add a test button as well with a popup dialog showing
    // test cases (list of types that are dynamically mapped using the current
    // settings)

    public JUTPreferenceDefaultValuesPage() {
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

	createSimpleTypesSection(cmpMain);
	createGenericTypeSection(cmpMain);
	createOtherTypeSection(cmpMain);

	return cmpMain;
    }

    private void createSimpleTypesSection(Composite cmpMain) {
	// default values for simple types
	Group cmpSimpleTypesSettings = new Group(cmpMain, SWT.NONE);
	cmpSimpleTypesSettings.setText(Messages.JUTPreferenceDefaultValuePage_simple_types);
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.verticalAlignment = SWT.FILL;
	data.grabExcessVerticalSpace = true;
	data.grabExcessHorizontalSpace = true;
	cmpSimpleTypesSettings.setLayoutData(data);
	cmpSimpleTypesSettings.setLayout(new GridLayout());

	createSimpleTypeList(cmpSimpleTypesSettings);

	Composite editorComposite = new Composite(cmpSimpleTypesSettings, SWT.NULL);
	GridLayout editorLayout = new GridLayout();
	editorLayout.numColumns = 2;
	editorComposite.setLayout(editorLayout);

	GridData data_2 = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
	data_2.verticalAlignment = SWT.CENTER;
	data_2.grabExcessVerticalSpace = false;
	editorComposite.setLayoutData(data_2);

	newSimpleTypeReference = new Text(editorComposite, SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.grabExcessHorizontalSpace = true;
	newSimpleTypeReference.setLayoutData(data);

	newSimpleValueExpression = new Text(editorComposite, SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.grabExcessHorizontalSpace = true;
	newSimpleValueExpression.setLayoutData(data);

	Label sampleValueLabel = new Label(editorComposite, SWT.NONE);
	data = new GridData();
	data.grabExcessHorizontalSpace = true;
	data.horizontalAlignment = GridData.BEGINNING;
	data.horizontalSpan = 2;
	sampleValueLabel.setText("Sample: ...");
	sampleValueLabel.setLayoutData(data);
	ModifyListener modifyListener = new ModifyListener() {
	    @Override
	    public void modifyText(ModifyEvent event) {
		sampleValueLabel.setText("Sample: " + determineDefaultValueFor(newSimpleTypeReference.getText(), newSimpleValueExpression.getText(), false));
		sampleValueLabel.pack();
	    }
	};
	newSimpleTypeReference.addModifyListener(modifyListener);
	newSimpleValueExpression.addModifyListener(modifyListener);

	Composite buttonCompositeSimpleType = new Composite(cmpSimpleTypesSettings, SWT.NULL);
	GridLayout buttonLayout = new GridLayout();
	buttonLayout.numColumns = 2;
	buttonCompositeSimpleType.setLayout(buttonLayout);

	GridData data_3 = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
	data_3.verticalAlignment = SWT.CENTER;
	data_3.grabExcessVerticalSpace = false;
	buttonCompositeSimpleType.setLayoutData(data_3);

	Button addButton = new Button(buttonCompositeSimpleType, SWT.PUSH | SWT.CENTER);
	addButton.setText("Add to List"); //$NON-NLS-1$
	addButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		// TODO validate syntax!
		modifiedMapper = null;
		String newEntryPrefix = newSimpleTypeReference.getText() + IJUTPreferenceConstants.VALUE_DELIMITER;
		String newEntry = newEntryPrefix + newSimpleValueExpression.getText();
		int idx = 0;
		for (String item : listSimpleTypes.getItems()) {
		    if (item.startsWith(newEntryPrefix)) {
			listSimpleTypes.setItem(idx, newEntry);
			return;
		    }
		    idx++;
		}

		listSimpleTypes.add(newEntry, listSimpleTypes.getItemCount());
	    }
	});
	data = new GridData();
	data.grabExcessHorizontalSpace = true;
	data.horizontalAlignment = GridData.END;
	addButton.setLayoutData(data);

	Button removeButton = new Button(buttonCompositeSimpleType, SWT.PUSH | SWT.CENTER);
	removeButton.setText("Remove Selection"); //$NON-NLS-1$
	removeButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		listSimpleTypes.remove(listSimpleTypes
			.getSelectionIndex());
	    }
	});
    }

    protected String determineDefaultValueFor(String typeRef, String expression, boolean updateMapping) {
	DefaultValueMapper valueMapper = getModifiedValueMapper(updateMapping);
	ValueMapping valueMapping = new ValueMapping(typeRef, expression, valueMapper);
	return valueMapping.getValueForNamedVariable(typeRef, "param");
    }

    private DefaultValueMapper getModifiedValueMapper(boolean updateMapping) {
	// re/init valuemapper with current settings
	if (modifiedMapper == null || updateMapping) {
	    try {
		DefaultValueMapperBuilder builder = DefaultValueMapper.builder();
		for (String mapping : listSimpleTypes.getItems()) {
		    int pos = mapping.indexOf(IJUTPreferenceConstants.VALUE_DELIMITER);
		    builder.appendRule(mapping.substring(0, pos), mapping.substring(pos + 1));
		}
		for (String mapping : listGenericTypes.getItems()) {
		    int pos = mapping.indexOf(IJUTPreferenceConstants.VALUE_DELIMITER);
		    builder.appendRule(mapping.substring(0, pos), mapping.substring(pos + 1));
		}
		builder.appendRule(defaultValueForJavaBeans.getText());
		builder.appendRule(defaultValueFallback.getText());
		modifiedMapper = builder.build();
	    } catch (Exception e) {
		logger.severe("Failed to initialize default value mapper: " + e.getMessage());
		modifiedMapper = JUTPreferences.getDefaultValueMapper();
	    }
	}
	return modifiedMapper;
    }

    private void createSimpleTypeList(Group cmpSimpleTypesSettings) {
	GridData data;
	listSimpleTypes = new List(cmpSimpleTypesSettings, SWT.BORDER | SWT.V_SCROLL);
	listSimpleTypes.setItems(getSimpleTypeSettingsWithSamples());

	data = new GridData(GridData.FILL_BOTH);
	data.heightHint = 100;
	listSimpleTypes.setLayoutData(data);

	listSimpleTypes.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (listSimpleTypes.getSelectionIndex() >= 0) {
		    String selected = listSimpleTypes.getItem(listSimpleTypes.getSelectionIndex());
		    String[] split = selected.split(IJUTPreferenceConstants.VALUE_DELIMITER);
		    newSimpleTypeReference.setText(split[0].trim());
		    newSimpleValueExpression.setText(split[1].trim());
		}
	    }
	});
    }

    private void createGenericTypeSection(Composite cmpMain) {
	// default values for generic types
	Group cmpGenericTypeSettings = new Group(cmpMain, SWT.NONE);
	cmpGenericTypeSettings.setText(Messages.JUTPreferenceDefaultValuePage_generic_types);

	// Create a data that takes up the extra space in the dialog .
	GridData data_1 = new GridData(GridData.FILL_HORIZONTAL);
	data_1.verticalAlignment = SWT.FILL;
	data_1.grabExcessVerticalSpace = true;
	data_1.grabExcessHorizontalSpace = true;
	cmpGenericTypeSettings.setLayoutData(data_1);

	GridLayout layout = new GridLayout();
	cmpGenericTypeSettings.setLayout(layout);

	listGenericTypes = new List(cmpGenericTypeSettings, SWT.BORDER);
	listGenericTypes.setItems(getGenericTypeSettigns());

	GridData data = new GridData(GridData.FILL_BOTH);
	data.heightHint = 100;
	listGenericTypes.setLayoutData(data);

	listGenericTypes.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		if (listGenericTypes.getSelectionIndex() >= 0) {
		    String selected = listGenericTypes.getItem(listGenericTypes.getSelectionIndex());
		    int split = selected.indexOf(IJUTPreferenceConstants.VALUE_DELIMITER);
		    newGenericTypeReference.setText(selected.substring(0, split));
		    newGenericDefaultValueExpression.setText(selected.substring(split + 1));
		}
	    }
	});

	Composite editorComposite = new Composite(cmpGenericTypeSettings, SWT.NULL);
	GridLayout editorLayout = new GridLayout();
	editorLayout.numColumns = 2;
	editorComposite.setLayout(editorLayout);

	GridData data_2 = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
	data_2.verticalAlignment = SWT.CENTER;
	data_2.grabExcessVerticalSpace = false;
	editorComposite.setLayoutData(data_2);

	newGenericTypeReference = new Text(editorComposite, SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.grabExcessHorizontalSpace = true;
	newGenericTypeReference.setLayoutData(data);

	newGenericDefaultValueExpression = new Text(editorComposite, SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.grabExcessHorizontalSpace = true;
	newGenericDefaultValueExpression.setLayoutData(data);

	Label sampleValueLabel = new Label(editorComposite, SWT.NONE);
	data = new GridData();
	data.grabExcessHorizontalSpace = true;
	data.horizontalAlignment = GridData.BEGINNING;
	data.horizontalSpan = 2;
	sampleValueLabel.setText("Sample: ...");
	sampleValueLabel.setLayoutData(data);
	ModifyListener modifyListener = new ModifyListener() {
	    @Override
	    public void modifyText(ModifyEvent event) {
		sampleValueLabel
			.setText("Sample: " + determineDefaultValueFor(newGenericTypeReference.getText(), newGenericDefaultValueExpression.getText(), false));
		sampleValueLabel.pack();
	    }
	};
	newGenericTypeReference.addModifyListener(modifyListener);
	newGenericDefaultValueExpression.addModifyListener(modifyListener);

	Composite buttonComposite = new Composite(cmpGenericTypeSettings, SWT.NULL);
	GridLayout buttonLayout = new GridLayout();
	buttonLayout.numColumns = 2;
	buttonComposite.setLayout(buttonLayout);

	GridData data_3 = new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING);
	data_3.verticalAlignment = SWT.CENTER;
	data_3.grabExcessVerticalSpace = false;
	buttonComposite.setLayoutData(data_3);
	Button addButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
	addButton.setText("Add to List"); //$NON-NLS-1$
	addButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		// TODO validate syntax!
		modifiedMapper = null;
		String newEntryPrefix = newGenericTypeReference.getText() + IJUTPreferenceConstants.VALUE_DELIMITER;
		String newEntry = newEntryPrefix + newGenericDefaultValueExpression.getText();
		int idx = 0;
		for (String item : listGenericTypes.getItems()) {
		    if (item.startsWith(newEntryPrefix)) {
			listGenericTypes.setItem(idx, newEntry);
			return;
		    }
		    idx++;
		}

		listGenericTypes.add(newEntry, listGenericTypes.getItemCount());
	    }
	});
	data = new GridData();
	data.grabExcessHorizontalSpace = true;
	data.horizontalAlignment = GridData.END;
	addButton.setLayoutData(data);

	Button removeButton = new Button(buttonComposite, SWT.PUSH | SWT.CENTER);
	removeButton.setText("Remove Selection"); //$NON-NLS-1$
	removeButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent event) {
		listGenericTypes.remove(listGenericTypes
			.getSelectionIndex());
	    }
	});

    }

    private void createOtherTypeSection(Composite cmpMain) {
	GridData data;
	Group cmpOtherTypeSettings = new Group(cmpMain, SWT.NONE);
	cmpOtherTypeSettings.setText(Messages.JUTPreferenceDefaultValuePage_simple_types);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.verticalAlignment = SWT.FILL;
	data.grabExcessVerticalSpace = true;
	data.grabExcessHorizontalSpace = true;
	cmpOtherTypeSettings.setLayoutData(data);
	cmpOtherTypeSettings.setLayout(new GridLayout());

	Label lblJavaBeans = new Label(cmpOtherTypeSettings, SWT.NONE);
	lblJavaBeans.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
	lblJavaBeans.setText(Messages.JUTPreferenceDefaultValuePage_javabeans);

	defaultValueForJavaBeans = new Text(cmpOtherTypeSettings, SWT.BORDER);
	defaultValueForJavaBeans.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	defaultValueForJavaBeans.setText(JUTPreferences.getDefaultValueForJavaBeans());
	defaultValueForJavaBeans.addModifyListener(new ModifyListener() {

	    @Override
	    public void modifyText(ModifyEvent event) {
		modifiedMapper = null;
	    }
	});

	Label lblFallback = new Label(cmpOtherTypeSettings, SWT.NONE);
	lblFallback.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
	lblFallback.setText(Messages.JUTPreferenceDefaultValuePage_fallback);

	defaultValueFallback = new Text(cmpOtherTypeSettings, SWT.BORDER);
	defaultValueFallback.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	defaultValueFallback.setText(JUTPreferences.getDefaultValueFallback());
	defaultValueFallback.addModifyListener(new ModifyListener() {

	    @Override
	    public void modifyText(ModifyEvent event) {
		modifiedMapper = null;
	    }
	});
    }

    /**
     * @see IWorkbenchPreferencePage#init(IWorkbench)
     */
    @Override
    public void init(IWorkbench workbench) {
	// Initialize the preference store we wish to use
	setPreferenceStore(Activator.getDefault().getPreferenceStore());
	setDescription(Messages.JUTPreferenceDefaultValuePage_description);
    }

    @Override
    protected void performDefaults() {
	listSimpleTypes.setItems(JUTPreferences.convertToArray(getPreferenceStore().getDefaultString(DEFAULT_VALUE_MAPPING)));
	listGenericTypes.setItems(JUTPreferences.convertToArray(getPreferenceStore().getDefaultString(DEFAULT_VALUE_GENERIC_MAPPING)));
	defaultValueForJavaBeans.setText(getPreferenceStore().getDefaultString(DEFAULT_VALUE_JAVA_BEANS));
	defaultValueFallback.setText(getPreferenceStore().getDefaultString(DEFAULT_VALUE_FALLBACK));
    }

    @Override
    public boolean performOk() {
	// TODO validate syntax!
	saveSimpleTypeSettings(listSimpleTypes.getItems());
	saveGenericTypeSettings(listGenericTypes.getItems());
	JUTPreferences.setPreference(DEFAULT_VALUE_JAVA_BEANS, defaultValueForJavaBeans.getText());
	JUTPreferences.setPreference(DEFAULT_VALUE_FALLBACK, defaultValueFallback.getText());
	return super.performOk();
    }

    public void saveSimpleTypeSettings(String[] values) {
	getPreferenceStore().setValue(DEFAULT_VALUE_MAPPING, JUTPreferences.convertFromArray(values));
    }

    public void saveGenericTypeSettings(String[] values) {
	getPreferenceStore().setValue(DEFAULT_VALUE_GENERIC_MAPPING, JUTPreferences.convertFromArray(values));
    }

    public String[] getSimpleTypeSettings() {
	return JUTPreferences.convertToArray(getPreferenceStore().getString(DEFAULT_VALUE_MAPPING));
    }

    public String[] getSimpleTypeSettingsWithSamples() {
	return JUTPreferences.getDefaultValuesByType().entrySet().stream()
		.map(entry -> entry.getKey() + IJUTPreferenceConstants.VALUE_DELIMITER + entry.getValue())
		.collect(Collectors.toList())
		.toArray(new String[0]);
    }

    public String[] getGenericTypeSettigns() {
	return JUTPreferences.convertToArray(getPreferenceStore().getString(DEFAULT_VALUE_GENERIC_MAPPING));
    }
}