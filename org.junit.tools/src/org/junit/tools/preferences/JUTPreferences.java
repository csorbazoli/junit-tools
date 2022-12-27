package org.junit.tools.preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.junit.tools.Activator;

/**
 * Preference-class for the junit-tools-processing.
 * 
 * @author Robert Streng
 * 
 */
public class JUTPreferences implements IJUTPreferenceConstants {

    // from main-page

    // USEFUL
    private static String testSourceFolderName = null;
    private static String testMethodPrefix = null;
    private static String testMethodPostfix = null;
    private static String testClassPrefix = null;
    private static String testClassPostfix = null;
    private static String springTestClassPostfix = null;
    private static Boolean gherkinStyleEnabled = true;
    private static Boolean showSettingsBeforeGenerate = false;
    private static String mockFramework = null;
    private static int junitVersion = 5;

    // from annotations-page
    private static String[] testClassAnnotations = null;
    private static String[] mockClassAnnotations = null;

    // from filter-page
    private static String[] testMethodFilterName = null;
    private static String[] testMethodFilterModifier = null;

    // from static-bindings-page
    private static String[] staticBindings = null;
    private static Map<String, String> staticBindingsMapBase = null;
    private static Map<String, String> staticBindingsMapTest = null;

    private static Map<String, String> defaultValuesByTypeMap = null;
    private static String defaultValueForJavaBeans = null;
    private static String defaultValueFallback = null;

    public static boolean getPreferenceBoolean(String name) {
	return getPreferenceStore().getBoolean(name);
    }

    public static boolean getPreferenceBoolean(String name, boolean defVal) {
	getPreferenceStore().setDefault(name, defVal);
	return getPreferenceStore().getBoolean(name);
    }

    public static String getPreference(String name) {
	return getPreferenceStore().getString(name);
    }

    private static IPreferenceStore getPreferenceStore() {
	return Activator.getDefault().getPreferenceStore();
    }

    public static String getTestSourceFolderName() {
	if (testSourceFolderName == null) {
	    testSourceFolderName = getPreference(TEST_SOURCE_FOLDER_NAME);
	}
	return testSourceFolderName;
    }

    public static String getTestMethodPostfix() {
	if (testMethodPostfix == null) {
	    testMethodPostfix = getPreference(TEST_METHOD_POSTFIX);
	}
	return testMethodPostfix;
    }

    protected static void setMockClassAnnotations(String[] mockClassAnnotations) {
	JUTPreferences.mockClassAnnotations = mockClassAnnotations;
    }

    public static void setTestClassAnnotations(String[] testClassAnnotations) {
	JUTPreferences.testClassAnnotations = testClassAnnotations;
    }

    public static String[] getTestClassAnnotations() {
	if (testClassAnnotations == null) {
	    testClassAnnotations = convertToArray(getPreference(TEST_CLASS_ANNOTATIONS));
	}
	return testClassAnnotations;
    }

    public static String[] getMockClassAnnotations() {
	if (mockClassAnnotations == null) {
	    mockClassAnnotations = convertToArray(getPreference(MOCK_CLASS_ANNOTATIONS));
	}
	return mockClassAnnotations;
    }

    protected static void setStaticBindings(String[] staticBindings) {
	JUTPreferences.staticBindings = staticBindings;
	initStaticBindingsMaps();
    }

    protected static void setTestMethodFilterName(String[] testMethodFilterName) {
	JUTPreferences.testMethodFilterName = testMethodFilterName;
    }

    protected static void setTestMethodFilterModifier(
	    String[] testmethodFilterModifier) {
	JUTPreferences.testMethodFilterModifier = testmethodFilterModifier;
    }

    public static String[] getTestMethodFilterName() {
	if (testMethodFilterName == null) {
	    testMethodFilterName = convertToArray(getPreference(TEST_METHOD_FILTER_NAME));
	}
	return testMethodFilterName;
    }

    public static String[] getTestMethodFilterModifier() {
	if (testMethodFilterModifier == null) {
	    testMethodFilterModifier = convertToArray(getPreference(TEST_METHOD_FILTER_MODIFIER));
	}
	return testMethodFilterModifier;
    }

    public static String[] getStaticBindings() {
	if (staticBindings == null) {
	    staticBindings = convertToArray(getPreference(STATIC_BINDINGS));
	}
	return staticBindings;
    }

    private static void initStaticBindingsMaps() {
	if (staticBindings == null) {
	    staticBindings = getStaticBindings();
	}

	staticBindingsMapBase = new HashMap<String, String>();
	staticBindingsMapTest = new HashMap<String, String>();

	for (String staticBinding : staticBindings) {
	    String baseProject, testProject;
	    String[] projects = staticBinding.split(LIST_ENTRY_SEPERATOR);

	    if (projects.length == 2) {
		baseProject = projects[0];
		testProject = projects[1];

		staticBindingsMapBase.put(baseProject, testProject);
		staticBindingsMapTest.put(testProject, baseProject);
	    }
	}
    }

    public static Map<String, String> getDefaultValuesByType() {
	if (defaultValuesByTypeMap == null) {
	    initDefaultValueMapping();
	}

	return defaultValuesByTypeMap;
    }

    public static void setDefaultValuesByType(Map<String, String> value) {
	defaultValuesByTypeMap = value;
    }

    private static void initDefaultValueMapping() {
	defaultValuesByTypeMap = convertToMap(getPreference(DEFAULT_VALUE_MAPPING));
    }

    public static String getDefaultValueForJavaBeans() {
	if (defaultValueForJavaBeans == null) {
	    defaultValueForJavaBeans = getPreference(DEFAULT_VALUE_JAVA_BEANS);
	}
	return defaultValueForJavaBeans;
    }

    public static void setDefaultValueForJavaBeans(String value) {
	defaultValueForJavaBeans = value;
    }

    public static String getDefaultValueFallback() {
	if (defaultValueFallback == null) {
	    defaultValueFallback = getPreference(DEFAULT_VALUE_FALLBACK);
	}
	return defaultValueFallback;
    }

    public static void setDefaultValueFallback(String value) {
	defaultValueFallback = value;
    }

    public static Map<String, String> getStaticBindingsMapBaseProject() {
	if (staticBindingsMapBase == null) {
	    initStaticBindingsMaps();
	}

	return staticBindingsMapBase;
    }

    public static Map<String, String> getStaticBindingsMapTestProject() {
	if (staticBindingsMapTest == null) {
	    initStaticBindingsMaps();
	}

	return staticBindingsMapTest;
    }

    protected static void setTestSourceFolderName(String testSourceFolderName) {
	JUTPreferences.testSourceFolderName = testSourceFolderName;
    }

    protected static void setTestMethodPostfix(String testmethodPostfixPref) {
	JUTPreferences.testMethodPostfix = testmethodPostfixPref;
    }

    public static String getTestMethodPrefix() {
	if (testMethodPrefix == null) {
	    testMethodPrefix = getPreference(TEST_METHOD_PREFIX);
	}
	return testMethodPrefix;
    }

    protected static void setTestMethodPrefix(String testMethodPrefix) {
	JUTPreferences.testMethodPrefix = testMethodPrefix;
    }

    protected static void setTestClassPrefix(String newValue) {
	JUTPreferences.testClassPrefix = newValue;
    }

    public static String getTestClassPrefix() {
	if (testClassPrefix == null) {
	    testClassPrefix = getPreference(TEST_CLASS_PREFIX);
	}
	return testClassPrefix;
    }

    protected static void setTestClassPostfix(String newValue) {
	JUTPreferences.testClassPostfix = newValue;
    }

    public static String getTestClassPostfix() {
	if (testClassPostfix == null) {
	    testClassPostfix = getPreference(TEST_CLASS_POSTFIX);
	}
	return testClassPostfix;
    }

    protected static void setSpringTestClassPostfix(String newValue) {
	JUTPreferences.springTestClassPostfix = newValue;
    }

    public static String getSpringTestClassPostfix() {
	if (springTestClassPostfix == null) {
	    springTestClassPostfix = getPreference(SPRING_TEST_CLASS_POSTFIX);
	}
	return springTestClassPostfix;
    }

    public static Boolean isGherkinStyleEnabled() {
	if (gherkinStyleEnabled == null) {
	    gherkinStyleEnabled = getPreferenceBoolean(GHERKIN_STYLE_ENABLED, true);
	}
	return gherkinStyleEnabled;
    }

    public static Boolean isShowSettingsBeforeGenerate() {
	if (showSettingsBeforeGenerate == null) {
	    showSettingsBeforeGenerate = getPreferenceBoolean(SHOW_SETTINGS_BEFORE_GENERATE, true);
	}
	return showSettingsBeforeGenerate;
    }

    public static String getMockFramework() {
	return mockFramework;
    }

    public static int getJUnitVersion() {
	return junitVersion;
    }

    public static void setGherkinStyleEnabled(Boolean gherkinStyleEnabled) {
	JUTPreferences.gherkinStyleEnabled = gherkinStyleEnabled;
    }

    public static void setShowSettingsBeforeGenerate(Boolean enabled) {
	JUTPreferences.showSettingsBeforeGenerate = enabled;
    }

    public static void setMockFramework(String mockFramework) {
	JUTPreferences.mockFramework = mockFramework;
    }

    public static void setJUnitVersion(int junitVersion) {
	JUTPreferences.junitVersion = junitVersion;
    }

    public static void setJUnitVersion(String junitVersion) {
	JUTPreferences.junitVersion = Integer.parseInt(junitVersion);
    }

    /**
     * Converter for lists
     * 
     * @param value
     * @return Array with list-entries
     */
    public static String[] convertToArray(String value) {
	StringTokenizer tokenizer = new StringTokenizer(value, LIST_DELIMITER);
	int tokenCount = tokenizer.countTokens();
	String[] elements = new String[tokenCount];

	for (int i = 0; i < tokenCount; i++) {
	    elements[i] = tokenizer.nextToken();
	}

	return elements;
    }

    /**
     * Converter for lists
     * 
     * @return value
     */
    public static String convertFromArray(String[] values) {
	StringBuilder buffer = new StringBuilder();
	for (int i = 0; i < values.length; i++) {
	    buffer.append(values[i]);
	    buffer.append(LIST_DELIMITER);
	}

	return buffer.toString();
    }

    public static Map<String, String> convertToMap(String value) {
	Map<String, String> ret = new HashMap<>();
	for (String item : StringUtils.split(value, LIST_DELIMITER)) {
	    int pos = item.indexOf(VALUE_DELIMITER);
	    if (pos > 0) {
		ret.put(item.substring(0, pos).trim(), item.substring(pos + 1).trim());
	    }
	}
	return ret;
    }

    public static String convertFromMap(Map<String, String> map) {
	return map.entrySet().stream()
		.map(entry -> entry.getKey() + VALUE_DELIMITER + entry.getValue())
		.collect(Collectors.joining(LIST_DELIMITER));
    }

    public static void initialize() {
	getPreferenceStore().addPropertyChangeListener(
		new IPropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent event) {
			if (event.getProperty() == TEST_SOURCE_FOLDER_NAME) {
			    setTestSourceFolderName((String) event
				    .getNewValue());
			    return;
			} else if (event.getProperty() == TEST_METHOD_PREFIX) {
			    setTestMethodPrefix((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == TEST_METHOD_POSTFIX) {
			    setTestMethodPostfix((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == TEST_METHOD_FILTER_MODIFIER) {
			    setTestMethodFilterModifier(convertToArray((String) event
				    .getNewValue()));
			    return;
			} else if (event.getProperty() == TEST_CLASS_PREFIX) {
			    setTestClassPrefix((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == TEST_CLASS_POSTFIX) {
			    setTestClassPostfix((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == SPRING_TEST_CLASS_POSTFIX) {
			    setSpringTestClassPostfix((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == JUNIT_VERSION) {
			    setJUnitVersion((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == GHERKIN_STYLE_ENABLED) {
			    setGherkinStyleEnabled((Boolean) event.getNewValue());
			    return;
			} else if (event.getProperty() == SHOW_SETTINGS_BEFORE_GENERATE) {
			    setShowSettingsBeforeGenerate((Boolean) event.getNewValue());
			    return;
			} else if (event.getProperty() == MOCK_FRAMEWORK) {
			    setMockFramework((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == TEST_CLASS_ANNOTATIONS) {
			    setTestClassAnnotations(convertToArray((String) event
				    .getNewValue()));
			    return;
			} else if (event.getProperty() == MOCK_CLASS_ANNOTATIONS) {
			    setMockClassAnnotations(convertToArray((String) event
				    .getNewValue()));
			    return;
			} else if (event.getProperty() == STATIC_BINDINGS) {
			    setStaticBindings(convertToArray((String) event
				    .getNewValue()));
			    return;
			} else if (event.getProperty() == DEFAULT_VALUE_MAPPING) {
			    setDefaultValuesByType(convertToMap((String) event
				    .getNewValue()));
			    return;
			} else if (event.getProperty() == DEFAULT_VALUE_JAVA_BEANS) {
			    setDefaultValueForJavaBeans((String) event.getNewValue());
			    return;
			} else if (event.getProperty() == DEFAULT_VALUE_FALLBACK) {
			    setDefaultValueFallback((String) event.getNewValue());
			    return;
			}
		    }

		});
    }

}
