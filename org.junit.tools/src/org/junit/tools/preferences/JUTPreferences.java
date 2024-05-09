package org.junit.tools.preferences;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Consumer;
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
    private static String testMvcMethodPostfix = null;
    private static String testClassPrefix = null;
    private static String testClassPostfix = null;
    private static String testClassSuperType = null;
    private static String springTestClassPostfix = null;
    private static Boolean gherkinStyleEnabled = null;
    private static Boolean assertjEnabled = null;
    private static Boolean testResurceFullPathEnabled = null;
    private static Boolean replayAllVerifyAllEnabled = null;
    private static Boolean repeatingTestMethodsEnabled = null;
    private static Boolean showSettingsBeforeGenerate = null;
    private static Boolean useMockRunner = null;
    private static String mockFramework = null;
    private static int junitVersion = 0;
    private static String testMethodPosition = null;

    // from annotations-page
    private static String[] testClassAnnotations = null;

    // from filter-page
    private static String[] testMethodFilterName = null;
    private static String[] testMethodFilterModifier = null;
    private static Set<String> injectionTypeFilter = null;

    // from static-bindings-page
    private static String[] staticBindings = null;
    private static Map<String, String> staticBindingsMapBase = null;
    private static Map<String, String> staticBindingsMapTest = null;

    private static Map<String, String> defaultValuesByTypeMap = null;
    private static Map<String, String> defaultValuesGenericByTypeMap = null;
    private static String defaultValueForJavaBeans = null;
    private static String defaultValueFallback = null;
    private static DefaultValueMapper defaultValueMapper = null;

    private static String[] springAnnotations = null;
    private static List<FieldDeclaration> additionalFields = null;
    private static List<ImportDeclaration> additionalImports = null;

    public static boolean getPreferenceBoolean(String name) {
	return getPreferenceStore().getBoolean(name);
    }

    public static boolean getPreferenceBoolean(String name, boolean defVal) {
	getPreferenceStore().setDefault(name, defVal);
	return getPreferenceStore().getBoolean(name);
    }

    public static void setPreferenceBoolean(String name, boolean newVal) {
	getPreferenceStore().setValue(name, newVal);
    }

    public static void setPreference(String name, String newVal) {
	getPreferenceStore().setValue(name, newVal);
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

    public static String getTestMvcMethodPostfix() {
	if (testMvcMethodPostfix == null) {
	    testMvcMethodPostfix = getPreference(TEST_MVC_METHOD_POSTFIX);
	}
	return testMvcMethodPostfix;
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

    public static void setStaticBindings(String[] staticBindings) {
	JUTPreferences.staticBindings = staticBindings;
	initStaticBindingsMaps();
    }

    public static void setRelevantSpringAnnotations(String[] values) {
	JUTPreferences.springAnnotations = values;
    }

    public static void setAdditionalFields(String[] values) {
	JUTPreferences.additionalFields = parseFieldDeclarations(values);
    }

    public static void setAdditionalImports(String[] values) {
	JUTPreferences.additionalImports = parseImports(values);
    }

    public static void setTestMethodFilterName(String[] testMethodFilterName) {
	JUTPreferences.testMethodFilterName = testMethodFilterName;
    }

    public static void setTestMethodFilterModifier(
	    String[] testmethodFilterModifier) {
	JUTPreferences.testMethodFilterModifier = testmethodFilterModifier;
    }

    public static void setInjectionTypeFilter(
	    String[] injectionTypeFilter) {
	JUTPreferences.injectionTypeFilter = new HashSet<>(Arrays.asList(injectionTypeFilter));
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

    public static Set<String> getInjectionTypeFilter() {
	if (injectionTypeFilter == null) {
	    injectionTypeFilter = new HashSet<>(Arrays.asList(convertToArray(getPreference(INJECTION_TYPE_FILTER))));
	}
	return injectionTypeFilter;
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

    public static String[] getRelevantSpringAnnotations() {
	if (springAnnotations == null) {
	    springAnnotations = convertToArray(getPreference(SPRING_ANNOTATIONS));
	}
	return springAnnotations;
    }

    public static List<ImportDeclaration> getAdditionalImports() {
	if (additionalImports == null) {
	    additionalImports = parseImports(convertToArray(getPreference(ADDITIONAL_IMPORTS)));
	}
	return additionalImports;
    }

    private static List<ImportDeclaration> parseImports(String[] items) {
	LinkedList<ImportDeclaration> ret = new LinkedList<>();
	for (String item : items) {
	    ImportDeclaration.fromConfigString(item).ifPresent(ret::add);
	}
	return ret;
    }

    public static List<FieldDeclaration> getAdditionalFields() {
	if (additionalFields == null) {
	    additionalFields = parseFieldDeclarations(convertToArray(getPreference(ADDITIONAL_FIELDS)));
	}
	return additionalFields;
    }

    private static List<FieldDeclaration> parseFieldDeclarations(String[] items) {
	LinkedList<FieldDeclaration> ret = new LinkedList<>();
	for (String item : items) {
	    FieldDeclaration.fromConfigString(item)
		    .ifPresent(ret::add);
	}
	return ret;
    }

    public static DefaultValueMapper getDefaultValueMapper() {
	if (defaultValueMapper == null) {
	    initDefaultValueMapper();
	}
	return defaultValueMapper;
    }

    private static void initDefaultValueMapper() {
	defaultValueMapper = DefaultValueMapper.builder()
		.appendRules(getDefaultValuesByType())
		.appendRules(getDefaultGenericValuesByType())
		.appendRule(getDefaultValueForJavaBeans())
		.appendRule(getDefaultValueFallback())
		.build();
    }

    public static Map<String, String> getDefaultValuesByType() {
	if (defaultValuesByTypeMap == null) {
	    initDefaultValueMapping();
	}

	return defaultValuesByTypeMap;
    }

    public static Map<String, String> getDefaultGenericValuesByType() {
	if (defaultValuesGenericByTypeMap == null) {
	    initDefaultValueGenericMapping();
	}

	return defaultValuesGenericByTypeMap;
    }

    public static void setDefaultValuesByType(Map<String, String> value) {
	defaultValuesByTypeMap = value;
	if (defaultValueMapper != null) {
	    initDefaultValueMapper();
	}
    }

    private static void initDefaultValueMapping() {
	defaultValuesByTypeMap = convertToMap(getPreference(DEFAULT_VALUE_MAPPING));
    }

    public static void setDefaultValuesGenericByType(Map<String, String> value) {
	defaultValuesGenericByTypeMap = value;
	if (defaultValueMapper != null) {
	    initDefaultValueMapper();
	}
    }

    private static void initDefaultValueGenericMapping() {
	defaultValuesGenericByTypeMap = convertToMap(getPreference(DEFAULT_VALUE_GENERIC_MAPPING));
    }

    public static String getDefaultValueForJavaBeans() {
	if (defaultValueForJavaBeans == null) {
	    defaultValueForJavaBeans = getPreference(DEFAULT_VALUE_JAVA_BEANS);
	}
	return defaultValueForJavaBeans;
    }

    public static void setDefaultValueForJavaBeans(String value) {
	defaultValueForJavaBeans = value;
	if (defaultValueMapper != null) {
	    initDefaultValueMapper();
	}
    }

    public static String getDefaultValueFallback() {
	if (defaultValueFallback == null) {
	    defaultValueFallback = getPreference(DEFAULT_VALUE_FALLBACK);
	}
	return defaultValueFallback;
    }

    public static void setDefaultValueFallback(String value) {
	defaultValueFallback = value;
	if (defaultValueMapper != null) {
	    initDefaultValueMapper();
	}
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

    protected static void setTestMvcMethodPostfix(String testmethodPostfixPref) {
	JUTPreferences.testMvcMethodPostfix = testmethodPostfixPref;
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

    protected static void setTestClassSuperType(String newValue) {
	JUTPreferences.testClassSuperType = newValue;
    }

    public static String getTestClassSuperType() {
	if (testClassSuperType == null) {
	    testClassSuperType = getPreference(TEST_CLASS_SUPER_TYPE);
	}
	return testClassSuperType;
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

    public static boolean isGherkinStyleEnabled() {
	if (gherkinStyleEnabled == null) {
	    gherkinStyleEnabled = getPreferenceBoolean(GHERKIN_STYLE_ENABLED, true);
	}
	return gherkinStyleEnabled;
    }

    public static boolean isAssertjEnabled() {
	if (assertjEnabled == null) {
	    assertjEnabled = getPreferenceBoolean(ASSERTJ_ENABLED, true);
	}
	return assertjEnabled;
    }

    public static boolean isTestResourceFullPathEnabled() {
	if (testResurceFullPathEnabled == null) {
	    testResurceFullPathEnabled = getPreferenceBoolean(TEST_RESOURCE_FULL_PATH_ENABLED, false);
	}
	return testResurceFullPathEnabled;
    }

    public static boolean isReplayAllVerifyAllEnabled() {
	if (replayAllVerifyAllEnabled == null) {
	    replayAllVerifyAllEnabled = getPreferenceBoolean(REPLAYALL_VERIFYALL_ENABLED, false);
	}
	return replayAllVerifyAllEnabled;
    }

    public static boolean isRepeatingTestMethodsEnabled() {
	if (repeatingTestMethodsEnabled == null) {
	    repeatingTestMethodsEnabled = getPreferenceBoolean(REPEATING_TEST_METHODS_ENABLED, true);
	}
	return repeatingTestMethodsEnabled;
    }

    public static String getTestMethodPosition() {
	if (testMethodPosition == null) {
	    testMethodPosition = getPreference(TEST_METHOD_POSITION);
	}
	return testMethodPosition;
    }

    public static boolean isShowSettingsBeforeGenerate() {
	if (showSettingsBeforeGenerate == null) {
	    showSettingsBeforeGenerate = getPreferenceBoolean(SHOW_SETTINGS_BEFORE_GENERATE, true);
	}
	return showSettingsBeforeGenerate;
    }

    public static boolean isUseMockRunner() {
	if (useMockRunner == null) {
	    useMockRunner = getPreferenceBoolean(USE_MOCK_RUNNER, true);
	}
	return useMockRunner;
    }

    public static String getMockFramework() {
	if (mockFramework == null) {
	    mockFramework = getPreference(MOCK_FRAMEWORK);
	}
	return mockFramework;
    }

    public static int getJUnitVersion() {
	if (junitVersion == 0) {
	    junitVersion = getPreferenceStore().getInt(JUNIT_VERSION);
	}
	return junitVersion;
    }

    public static void setGherkinStyleEnabled(Boolean enabled) {
	JUTPreferences.gherkinStyleEnabled = enabled;
    }

    public static void setAssertJEnabled(Boolean enabled) {
	JUTPreferences.assertjEnabled = enabled;
    }

    public static void setTestResurceFullPathEnabled(Boolean enabled) {
	JUTPreferences.testResurceFullPathEnabled = enabled;
    }

    public static void setReplayAllVerifyAllEnabled(Boolean enabled) {
	JUTPreferences.replayAllVerifyAllEnabled = enabled;
    }

    public static void setRepeatingTestMethodsEnabled(Boolean enabled) {
	JUTPreferences.repeatingTestMethodsEnabled = enabled;
    }

    public static void setTestMethodPosition(String value) {
	JUTPreferences.testMethodPosition = value;
    }

    public static void setShowSettingsBeforeGenerate(Boolean enabled) {
	JUTPreferences.showSettingsBeforeGenerate = enabled;
    }

    public static void setUseMockRunner(Boolean enabled) {
	JUTPreferences.useMockRunner = enabled;
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
	Map<String, String> ret = new LinkedHashMap<>();
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
	Map<String, Consumer<Boolean>> booleanPropertyHandlers = new HashMap<>();
	booleanPropertyHandlers.put(ASSERTJ_ENABLED, JUTPreferences::setAssertJEnabled);
	booleanPropertyHandlers.put(GHERKIN_STYLE_ENABLED, JUTPreferences::setGherkinStyleEnabled);
	booleanPropertyHandlers.put(REPEATING_TEST_METHODS_ENABLED, JUTPreferences::setRepeatingTestMethodsEnabled);
	booleanPropertyHandlers.put(SHOW_SETTINGS_BEFORE_GENERATE, JUTPreferences::setShowSettingsBeforeGenerate);
	booleanPropertyHandlers.put(REPLAYALL_VERIFYALL_ENABLED, JUTPreferences::setReplayAllVerifyAllEnabled);
	booleanPropertyHandlers.put(TEST_RESOURCE_FULL_PATH_ENABLED, JUTPreferences::setTestResurceFullPathEnabled);
	booleanPropertyHandlers.put(USE_MOCK_RUNNER, JUTPreferences::setUseMockRunner);

	Map<String, Consumer<Integer>> intPropertyHandlers = new HashMap<>();
	// intPropertyHandlers.put(JUNIT_VERSION, JUTPreferences::setJUnitVersion);

	Map<String, Consumer<String>> stringPropertyHandlers = new HashMap<>();
	stringPropertyHandlers.put(DEFAULT_VALUE_JAVA_BEANS, JUTPreferences::setDefaultValueForJavaBeans);
	stringPropertyHandlers.put(DEFAULT_VALUE_FALLBACK, JUTPreferences::setDefaultValueFallback);
	stringPropertyHandlers.put(JUNIT_VERSION, JUTPreferences::setJUnitVersion);
	stringPropertyHandlers.put(MOCK_FRAMEWORK, JUTPreferences::setMockFramework);
	stringPropertyHandlers.put(SPRING_TEST_CLASS_POSTFIX, JUTPreferences::setSpringTestClassPostfix);
	stringPropertyHandlers.put(TEST_SOURCE_FOLDER_NAME, JUTPreferences::setTestSourceFolderName);
	stringPropertyHandlers.put(TEST_METHOD_PREFIX, JUTPreferences::setTestMethodPrefix);
	stringPropertyHandlers.put(TEST_METHOD_POSTFIX, JUTPreferences::setTestMethodPostfix);
	stringPropertyHandlers.put(TEST_MVC_METHOD_POSTFIX, JUTPreferences::setTestMvcMethodPostfix);
	stringPropertyHandlers.put(TEST_CLASS_PREFIX, JUTPreferences::setTestClassPrefix);
	stringPropertyHandlers.put(TEST_CLASS_POSTFIX, JUTPreferences::setTestClassPostfix);
	stringPropertyHandlers.put(TEST_CLASS_SUPER_TYPE, JUTPreferences::setTestClassSuperType);
	stringPropertyHandlers.put(TEST_METHOD_POSITION, JUTPreferences::setTestMethodPosition);

	Map<String, Consumer<String[]>> arrayPropertyHandlers = new HashMap<>();
	arrayPropertyHandlers.put(SPRING_ANNOTATIONS, JUTPreferences::setRelevantSpringAnnotations);
	arrayPropertyHandlers.put(ADDITIONAL_FIELDS, JUTPreferences::setAdditionalFields);
	arrayPropertyHandlers.put(ADDITIONAL_IMPORTS, JUTPreferences::setAdditionalImports);
	arrayPropertyHandlers.put(STATIC_BINDINGS, JUTPreferences::setStaticBindings);
	arrayPropertyHandlers.put(TEST_CLASS_ANNOTATIONS, JUTPreferences::setTestClassAnnotations);
	arrayPropertyHandlers.put(TEST_METHOD_FILTER_MODIFIER, JUTPreferences::setTestMethodFilterModifier);
	arrayPropertyHandlers.put(TEST_METHOD_FILTER_NAME, JUTPreferences::setTestMethodFilterName);
	arrayPropertyHandlers.put(INJECTION_TYPE_FILTER, JUTPreferences::setInjectionTypeFilter);

	Map<String, Consumer<Map<String, String>>> mapPropertyHandlers = new HashMap<>();
	mapPropertyHandlers.put(DEFAULT_VALUE_GENERIC_MAPPING, JUTPreferences::setDefaultValuesGenericByType);
	mapPropertyHandlers.put(DEFAULT_VALUE_MAPPING, JUTPreferences::setDefaultValuesByType);

	getPreferenceStore().addPropertyChangeListener(
		new IPropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent event) {
			if (stringPropertyHandlers.containsKey(event.getProperty())) {
			    stringPropertyHandlers.get(event.getProperty()).accept((String) event.getNewValue());
			} else if (intPropertyHandlers.containsKey(event.getProperty())) {
			    intPropertyHandlers.get(event.getProperty()).accept((Integer) event.getNewValue());
			} else if (booleanPropertyHandlers.containsKey(event.getProperty())) {
			    booleanPropertyHandlers.get(event.getProperty()).accept((Boolean) event.getNewValue());
			} else if (arrayPropertyHandlers.containsKey(event.getProperty())) {
			    arrayPropertyHandlers.get(event.getProperty()).accept(convertToArray((String) event.getNewValue()));
			} else if (mapPropertyHandlers.containsKey(event.getProperty())) {
			    mapPropertyHandlers.get(event.getProperty()).accept(convertToMap((String) event.getNewValue()));
			}
		    }

		});
    }

}
