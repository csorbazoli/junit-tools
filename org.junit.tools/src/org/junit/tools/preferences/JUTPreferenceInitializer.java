package org.junit.tools.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.tools.Activator;
import org.junit.tools.base.ExtensionPointHandler;

/**
 * Initializer for the junit-tools-preference-values.
 * 
 * @author Robert Streng
 */
public class JUTPreferenceInitializer extends AbstractPreferenceInitializer
	implements IJUTPreferenceConstants {

    public static final String DEFAULT_METHOD_FILTER_NAME = "get*;set*;";
    public static final String DEFAULT_METHOD_FILTER_MODIFIER = "private;protected;package;";

    @Override
    public void initializeDefaultPreferences() {
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	// useful settings
	store.setDefault(TEST_CLASS_ANNOTATIONS, "");
	store.setDefault(STATIC_BINDINGS, "");

	store.setDefault(TEST_CLASS_PREFIX, "");
	store.setDefault(TEST_CLASS_POSTFIX, "Test");

	store.setDefault(TEST_METHOD_PREFIX, "test");
	store.setDefault(TEST_METHOD_POSTFIX, "");

	store.setDefault(TEST_METHOD_FILTER_NAME, "");
	store.setDefault(TEST_METHOD_FILTER_MODIFIER, DEFAULT_METHOD_FILTER_MODIFIER);

	// this are not needed
	store.setDefault(TEST_PROJECT_POSTFIX, "");
	store.setDefault(TML_CONTAINER, "test_files");
	store.setDefault(WRITE_TML, false);
	store.setDefault(TEST_CLASS_SUPER_TYPE, "");
	store.setDefault(TEST_PACKAGE_POSTFIX, "");
	store.setDefault(MOCK_PROJECT, "org.junit.tools.mock"); // this is not needed
	store.setDefault(MOCK_SAVE_IN_TESTPROJECT, false);
	store.setDefault(MOCK_CLASS_ANNOTATIONS, "");

	// Important settings
	store.setDefault(TEST_SOURCE_FOLDER_NAME, "src/test/java");
	store.setDefault(MOCK_FRAMEWORK, "mockito"); // should we support EasyMock as well?
	store.setDefault(GHERKIN_STYLE_ENABLED, true);
	// settings to be implemented/used
	store.setDefault(JUNIT_VERSION, 5);
	store.setDefault(SHOW_SETTINGS_BEFORE_GENERATE, false);

	// initialize JUT-preferences
	JUTPreferences.initialize();

	// set custom preferences
	ExtensionPointHandler extensionHandler = Activator.getDefault()
		.getExtensionHandler();
	for (AbstractPreferenceInitializer initializer : extensionHandler
		.getPreferenceInitializer()) {
	    initializer.initializeDefaultPreferences();
	}

    }

}
