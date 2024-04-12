package org.junit.tools.preferences;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

@Data
public class FieldDeclaration {

    private static final Pattern CONFIG_PATTERN = Pattern
	    .compile("^(@(?<annotation>\\w+(\\(.*\\))?)\\s+)?"
		    + "((?<modifier>\\w+)\\s+)?"
		    + "(?<type>\\w+)\\s+(?<name>\\w+)"
		    + "(\\s*=\\s*(?<value>.*))?$");
    private final String annotation;
    private final String modifier;
    private final String type;
    private final String name;
    private final String initialValue;

    public static Optional<FieldDeclaration> fromConfigString(String item) {
	Matcher matcher = CONFIG_PATTERN.matcher(item);
	if (matcher.matches()) {
	    return Optional.of(new FieldDeclaration(
		    matcher.group("annotation"),
		    matcher.group("modifier"),
		    matcher.group("type"),
		    matcher.group("name"),
		    matcher.group("value")));
	}
	return Optional.empty();
    }

    public String toConfigString() {
	StringBuilder ret = new StringBuilder();
	if (annotation != null) {
	    ret.append("@").append(annotation).append(" ");
	}
	if (modifier != null) {
	    ret.append(modifier).append(" ");
	}
	if (type != null) {
	    ret.append(type).append(" ");
	}
	if (name != null) {
	    ret.append(name);
	}
	if (initialValue != null) {
	    ret.append(" = ").append(initialValue);
	}
	return ret.toString();
    }

    public String toJavaString() {
	return toConfigString();
    }

}
