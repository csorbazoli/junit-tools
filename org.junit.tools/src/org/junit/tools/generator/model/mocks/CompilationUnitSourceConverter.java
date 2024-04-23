package org.junit.tools.generator.model.mocks;

import java.lang.reflect.Modifier;
import java.util.List;

import org.eclipse.jdt.core.IPackageDeclaration;

public class CompilationUnitSourceConverter {

    private static final String NEWLINE = "\n";
    private static final String SEMICOLON_NEWLINE = ";\n";
    private static final String SEMICOLON_EMPTYLINE = ";\n\n";

    private CompilationUnitSourceConverter() {
	// private constructor
    }

    public static String toJavaSource(MockCompilationUnit compUnit) {
	StringBuilder ret = new StringBuilder();
	IPackageDeclaration[] packageDeclarations = compUnit.getPackageDeclarations();
	if (packageDeclarations.length > 0) {
	    ret.append("package ").append(packageDeclarations[0].getElementName()).append(SEMICOLON_EMPTYLINE);
	}
	appendImports(ret, compUnit);

	MockType pPrimaryType = compUnit.findPrimaryType();
	appendClassDeclaration(ret, pPrimaryType);
	appendFields(ret, pPrimaryType);
	appendTestMethods(ret, pPrimaryType);

	ret.append("}");

	return ret.toString();
    }

    private static void appendTestMethods(StringBuilder ret, MockType testClass) {
	String linebreak = "";
	for (MockMethod method : testClass.getMockMethods()) {
	    ret.append(method.getSource())
		    .append(NEWLINE);
	    linebreak = NEWLINE;
	}
	ret.append(linebreak);
    }

    private static void appendFields(StringBuilder ret, MockType testClass) {
	for (MockField field : testClass.getMockFields()) {
	    ret.append(field.getSource())
		    .append(NEWLINE)
		    .append(NEWLINE);
	}
    }

    private static void appendClassDeclaration(StringBuilder ret, MockType testClass) {
	ret.append(trimTrailingCurlyBracket(testClass.getSource()));
    }

    private static String trimTrailingCurlyBracket(String source) {
	return source.replaceFirst("\\}$", "");
    }

    private static void appendImports(StringBuilder ret, MockCompilationUnit compUnit) {
	String linebreak = "";
	List<MockImportDeclaration> importDeclarations = compUnit.getImportDeclarations();
	for (MockImportDeclaration imp : importDeclarations) {
	    if (Modifier.isStatic(imp.getFlags())) {
		ret.append("import ")
			.append("static ")
			.append(imp.getElementName())
			.append(SEMICOLON_NEWLINE);
		linebreak = NEWLINE;
	    }
	}
	ret.append(linebreak);
	linebreak = "";
	for (MockImportDeclaration imp : importDeclarations) {
	    if (!Modifier.isStatic(imp.getFlags())) {
		ret.append("import ")
			.append(imp.getElementName())
			.append(SEMICOLON_NEWLINE);
		linebreak = NEWLINE;
	    }
	}
	ret.append(linebreak);
    }
}
