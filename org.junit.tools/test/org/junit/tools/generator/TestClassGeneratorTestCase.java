package org.junit.tools.generator;

import org.junit.tools.generator.model.mocks.MockCompilationUnit;
import org.junit.tools.generator.model.mocks.MockJavaProject;
import org.junit.tools.generator.model.mocks.MockMethod;
import org.junit.tools.generator.model.mocks.MockPackageFragment;
import org.junit.tools.generator.model.tml.Method;

import lombok.Data;

@Data
public class TestClassGeneratorTestCase {

    private org.junit.tools.generator.model.tml.Test tmlTest;
    private MockPackageFragment basePackage;
    private MockCompilationUnit testClassCompilationUnit;
    private MockJavaProject javaProject;
    private Method tmlMethod;
    private MockMethod testMethod;
    private TestJUTPreferences preferences;
}
