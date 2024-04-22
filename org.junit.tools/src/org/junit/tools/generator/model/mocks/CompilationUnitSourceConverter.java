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
	List<MockImportDeclaration> importDeclarations = compUnit.getImportDeclarations();
	String linebreak = "";
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
	linebreak = "";
	ret.append("// BaseTypes: ").append(compUnit.getBaseTypes()).append(NEWLINE);
	ret.append("// PrimaryElement: ").append(compUnit.getPrimaryElement()).append(NEWLINE);
	ret.append("// ChildElements: ").append(compUnit.getChildElements()).append(NEWLINE);

	return ret.toString();
    }
}
