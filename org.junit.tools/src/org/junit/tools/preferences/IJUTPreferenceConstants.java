package org.junit.tools.preferences;

/**
 * Constants for the JUnit-Tools-preferences.
 * 
 * @author Robert Streng
 * 
 */
public interface IJUTPreferenceConstants {

    // USEFUL
    public static final String TEST_SOURCE_FOLDER_NAME = "TEST_SOURCE_FOLDER_NAME";
    public static final String TEST_CLASS_PREFIX = "TEST_CLASS_PREFIX";
    public static final String TEST_CLASS_POSTFIX = "TEST_CLASS_POSTFIX";
    public static final String SPRING_TEST_CLASS_POSTFIX = "SPRING_TEST_CLASS_POSTFIX";
    public static final String TEST_METHOD_PREFIX = "TEST_METHOD_PREFIX";
    public static final String TEST_METHOD_POSTFIX = "TEST_METHOD_POSTFIX";
    public static final String TEST_MVC_METHOD_POSTFIX = "TEST_MVC_METHOD_POSTFIX";
    public static final String TEST_CLASS_SUPER_TYPE = "TEST_CLASS_SUPER_TYPE";
    public static final String TEST_METHOD_FILTER_NAME = "TEST_METHOD_FILTER_NAME";
    public static final String TEST_METHOD_FILTER_MODIFIER = "TEST_METHOD_FILTER_MODIFIER";
    public static final String INJECTION_TYPE_FILTER = "INJECTION_TYPE_FILTER";
    public static final String JUNIT_VERSION = "JUNIT_VERSION";
    public static final String SHOW_SETTINGS_BEFORE_GENERATE = "SHOW_SETTINGS_BEFORE_GENERATE";
    public static final String GHERKIN_STYLE_ENABLED = "GHERKIN_STYLE";
    public static final String ASSERTJ_ENABLED = "ASSERTJ_ENABLED";
    public static final String TEST_RESOURCE_FULL_PATH_ENABLED = "TEST_RESOURCE_FULL_PATH_ENABLED";
    /**
     * BEFORE, AFTER, LAST
     */
    public static final String TEST_METHOD_POSITION = "TEST_METHOD_POSITION";
    /**
     * If enabled, then it creates a new test method if it already exists
     */
    public static final String REPEATING_TEST_METHODS_ENABLED = "MULTIPLE_TEST_METHODS";
    public static final String MOCK_FRAMEWORK = "MOCK_FRAMEWORK";
    public static final String TEST_CLASS_ANNOTATIONS = "TEST_CLASS_ANNOTATIONS";
    public static final String STATIC_BINDINGS = "STATIC_BINDINGS";
    public static final String SPRING_ANNOTATIONS = "SPRING_ANNOTATIONS";
    public static final String ADDITIONAL_IMPORTS = "ADDITIONAL_IMPORTS";
    public static final String ADDITIONAL_FIELDS = "ADDITIONAL_FIELDS";

    public static final String DEFAULT_VALUE_MAPPING = "DEFAULT_VALUE_MAPPING";
    public static final String DEFAULT_VALUE_GENERIC_MAPPING = "DEFAULT_VALUE_GENERIC_MAPPING";
    public static final String DEFAULT_VALUE_JAVA_BEANS = "DEFAULT_VALUE_JAVA_BEANS";
    public static final String DEFAULT_VALUE_FALLBACK = "DEFAULT_VALUE_FALLBACK";

    public static final String VALUE_DELIMITER = "=";
    public static final String LIST_DELIMITER = ";";
    public static final String LIST_ENTRY_SEPERATOR = " <> ";

    public static final String MOCKFW_MOCKITO = "mockito";
    public static final String MOCKFW_EASYMOCK = "easymock";
    // could support the usage of nullables (i.e. call createNull() for
    // initialization in case that method exists
    // no need to create fields for injectable dependencies
    // public static final String MOCKFW_NULLABLES = "nullables"
    public static final String MOCKFW_NONE = "none";

    public static final String POSITION_BEFORE = "BEFORE";
    public static final String POSITION_AFTER = "AFTER";
    public static final String POSITION_LAST = "LAST";

    // options/flags
    public static final String BEFORE_METHOD_ENABLED = "SetupSelection";
    public static final String BEFORE_CLASS_METHOD_ENABLED = "SetupbeforeclassSelection";
    public static final String AFTER_METHOD_ENABLED = "TeardownSelection";
    public static final String AFTER_CLASS_METHOD_ENABLED = "TeardownafterclassSelection";
    public static final String LOGGER_ENABLED = "LoggerSelection";
    public static final String TESTUTILS_ENABLED = "TestUtilsSelection";
    public static final String THROWS_DECLARATION_ENABLED = "ThrowsDeclaration";
    public static final String REPLAYALL_VERIFYALL_ENABLED = "ReplayAllVerifyAll";
    public static final String USE_MOCK_RUNNER = "UseMockRunner";

    // other settings
    public static final String BEFORE_METHOD_BODY = "SetupBody";
    public static final String BEFORE_CLASS_METHOD_BODY = "SetupbeforeclassBody";
    public static final String AFTER_METHOD_BODY = "TeardownBody";
    public static final String AFTER_CLASS_METHOD_BODY = "TeardownafterclassBody";

}
