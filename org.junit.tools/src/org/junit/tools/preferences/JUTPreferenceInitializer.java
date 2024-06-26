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
    public static final String DEFAULT_INJECTION_TYPE_FILTER = "char;Character;byte;Byte;double;Double;int;Integer;long;Long;float;Float;boolean;Boolean;String;"
	    + "Calendar;Date;LocalDate;LocalTime;Timestamp";

    @Override
    public void initializeDefaultPreferences() {
	initDefaultPreferences();

	// set custom preferences
	ExtensionPointHandler extensionHandler = Activator.getDefault()
		.getExtensionHandler();
	for (AbstractPreferenceInitializer initializer : extensionHandler
		.getPreferenceInitializer()) {
	    initializer.initializeDefaultPreferences();
	}

    }

    public static void initDefaultPreferences() {
	IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	// useful settings
	store.setDefault(TEST_CLASS_ANNOTATIONS, "");
	store.setDefault(STATIC_BINDINGS, "");
	store.setDefault(SPRING_ANNOTATIONS, "Service;Component;Configuration;Controller;RestController;Repository");
	store.setDefault(ADDITIONAL_FIELDS, "");
	store.setDefault(ADDITIONAL_IMPORTS, "");

	store.setDefault(TEST_CLASS_PREFIX, "");
	store.setDefault(TEST_CLASS_POSTFIX, "Test");
	store.setDefault(SPRING_TEST_CLASS_POSTFIX, "IntegrationTest");

	store.setDefault(TEST_METHOD_PREFIX, "test");
	store.setDefault(TEST_METHOD_POSTFIX, "");
	store.setDefault(TEST_MVC_METHOD_POSTFIX, "_MVC");
	store.setDefault(TEST_CLASS_SUPER_TYPE, "");

	store.setDefault(TEST_METHOD_FILTER_NAME, "");
	store.setDefault(TEST_METHOD_FILTER_MODIFIER, DEFAULT_METHOD_FILTER_MODIFIER);
	store.setDefault(INJECTION_TYPE_FILTER, DEFAULT_INJECTION_TYPE_FILTER);

	// Important settings
	store.setDefault(TEST_SOURCE_FOLDER_NAME, "src/test/java");
	store.setDefault(MOCK_FRAMEWORK, MOCKFW_MOCKITO); // should we support EasyMock as well?
	store.setDefault(USE_MOCK_RUNNER, true);
	store.setDefault(GHERKIN_STYLE_ENABLED, true);
	store.setDefault(ASSERTJ_ENABLED, true);
	store.setDefault(TEST_RESOURCE_FULL_PATH_ENABLED, false);
	store.setDefault(REPLAYALL_VERIFYALL_ENABLED, false);
	store.setDefault(REPEATING_TEST_METHODS_ENABLED, true);
	store.setDefault(TEST_METHOD_POSITION, POSITION_AFTER);
	// settings to be implemented/used
	store.setDefault(JUNIT_VERSION, 5);
	store.setDefault(SHOW_SETTINGS_BEFORE_GENERATE, false);

	// default values
	store.setDefault(DEFAULT_VALUE_MAPPING, "String=\"Test${Name}\";boolean=true;Boolean=true;byte=63;Byte=63;char='c';Chararcter='c';"
		+ "double=12.34;Double=12.34;float=15.79;Float=15.79;int=123;Integer=123;long=123456L;Long=123456L;");
	store.setDefault(DEFAULT_VALUE_GENERIC_MAPPING, "List<T>=Arrays.asList(${T});"
		+ "Optional<T>=Optional.of(${T});"
		+ "ResponseEntity<T>=ResponseEntity.ok(${T});"
		+ "Set<T>=Collections.singleton(${T});"
		+ "Map<T,U>=Collections.singletonMap(${T}, ${U})");
	store.setDefault(DEFAULT_VALUE_JAVA_BEANS, "TestValueFactory.fillFields(new ${Class}())");
	store.setDefault(DEFAULT_VALUE_FALLBACK, "Mockito.mock(${Class}.class)");

	// initialize JUT-preferences
	JUTPreferences.initialize();
    }

}
