package org.junit.tools.preferences;

import lombok.Data;

@Data
public class ImportDeclaration {

    private final boolean isStatic;
    private final String packageName;

    public static ImportDeclaration fromConfigString(String item) {
	if (item == null || item.trim().isEmpty()) {
	    return null;
	}
	if (item.startsWith("static")) {
	    return new ImportDeclaration(true, item.substring(6).trim());
	}
	return new ImportDeclaration(false, item.trim());
    }

    public String toConfigString() {
	return (isStatic ? "static " : "") + packageName;
    }

}
