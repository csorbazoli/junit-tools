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
	store.setDefault(SPRING_ANNOTATIONS, "Service;Component;Configuration;Controller;RestController;Repository");

	store.setDefault(TEST_CLASS_PREFIX, "");
	store.setDefault(TEST_CLASS_POSTFIX, "Test");
	store.setDefault(SPRING_TEST_CLASS_POSTFIX, "IntegrationTest");

	store.setDefault(TEST_METHOD_PREFIX, "test");
	store.setDefault(TEST_METHOD_POSTFIX, "");
	store.setDefault(TEST_MVC_METHOD_POSTFIX, "_MVC");
	store.setDefault(TEST_CLASS_SUPER_TYPE, "");

	store.setDefault(TEST_METHOD_FILTER_NAME, "");
	store.setDefault(TEST_METHOD_FILTER_MODIFIER, DEFAULT_METHOD_FILTER_MODIFIER);

	// Important settings
	store.setDefault(TEST_SOURCE_FOLDER_NAME, "src/test/java");
	store.setDefault(MOCK_FRAMEWORK, MOCKFW_MOCKITO); // should we support EasyMock as well?
	store.setDefault(GHERKIN_STYLE_ENABLED, true);
	// settings to be implemented/used
	store.setDefault(JUNIT_VERSION, 5);
	store.setDefault(SHOW_SETTINGS_BEFORE_GENERATE, false);

	// default values
	store.setDefault(DEFAULT_VALUE_MAPPING, "String=\"Test${Name}\";boolean=true;Boolean=true;byte=63;Byte=63;char='c';Chararcter='c';"
		+ "double=12.34;Double=12.34;float=15.79;Float=15.79;int=123;Integer=123;");
	store.setDefault(DEFAULT_VALUE_JAVA_BEANS, "TestValueFactory.fillFields(new ${Class}())");
	store.setDefault(DEFAULT_VALUE_FALLBACK, "Mockito.mock(${Class}.class)");

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
