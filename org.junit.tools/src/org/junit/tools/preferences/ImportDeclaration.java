package org.junit.tools.preferences;

import java.util.Optional;

import lombok.Data;

@Data
public class ImportDeclaration {

    private final boolean isStatic;
    private final String packageName;

    public static Optional<ImportDeclaration> fromConfigString(String item) {
	if (item == null || item.trim().isEmpty()) {
	    return Optional.empty();
	}
	if (item.startsWith("static")) {
	    return Optional.of(new ImportDeclaration(true, item.substring(6).trim()));
	}
	return Optional.of(new ImportDeclaration(false, item.trim()));
    }

    public String toConfigString() {
	return (isStatic ? "static " : "") + packageName;
    }

}
