package org.junit.tools.generator;

import lombok.Data;

@Data
public class TestJUTPreferences {
    private String[] additionalFields;
    private String[] additionalImports;
    private boolean assertJEnabled;
    private boolean gherkinStyleEnabled;
    private int jUnitVersion;
    private String mockFramework;
    private boolean repeatingTestMethodsEnabled;
    private boolean replayAllVerifyAllEnabled;

}
