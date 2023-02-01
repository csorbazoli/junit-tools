package org.junit.tools.preferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.tools.generator.utils.GeneratorUtils;

public class ValueMapping {

    private static final Pattern GENERIC_TYPE_PATTERN = Pattern.compile("^(.*)<([^<]+)>(.*)$");
    private static final Pattern REFERENCE_PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
    private final Pattern typePattern;
    private final String expression;
    private final DefaultValueMapper mapper;

    public ValueMapping(String typeReference, String mapping, DefaultValueMapper mapper) {
	this.typePattern = initTypePattern(typeReference);
	this.expression = mapping;
	this.mapper = mapper;
    }

    private Pattern initTypePattern(String typeReference) {
	String typeRefPattern = typeReference;
	Matcher m = GENERIC_TYPE_PATTERN.matcher(typeRefPattern);
	while (m.matches()) {
	    typeRefPattern = m.group(1) + replaceTypeReferenceWithWildcard(m.group(2)) + m.group(3);
	    m = GENERIC_TYPE_PATTERN.matcher(typeRefPattern);
	}
	typeRefPattern = typeRefPattern
		.replace("&{", "<")
		.replace("#{", "(?<")
		.replace("}#", ">.*)")
		.replace("}&", ">");
	return Pattern.compile(typeRefPattern);
    }

    private String replaceTypeReferenceWithWildcard(String typeRefList) {
	StringBuilder ret = new StringBuilder("&{");
	String sep = "";
	for (String typeRef : typeRefList.split(",")) {
	    ret.append(sep)
		    .append("#{" + typeRef.trim() + "}#");
	    sep = ",\\s*";
	}
	ret.append("}&");
	return ret.toString();
    }

    public boolean isMatchingType(String type) {
	return typePattern.matcher(type.replaceFirst("\\[\\]$", "")).matches();
    }

    public String getValueForNamedVariable(String type, String varName) {
	boolean array = type.endsWith("[]");
	String elementType = array ? type.substring(0, type.length() - 2) : type;
	Matcher matcher = typePattern.matcher(elementType);
	if (!matcher.matches()) {
	    return null;
	}
	String ret = expression;
	if (expression.contains("${")) {
	    ret = replaceReferences(type, varName, matcher);
	}
	if (array) {
	    ret = "new " + type + "{" + ret + "}";
	}
	return ret;
    }

    private String replaceReferences(String type, String varName, Matcher matcher) {
	StringBuffer ret = new StringBuffer();
	Matcher m = REFERENCE_PATTERN.matcher(expression);
	while (m.find()) {
	    String reference = m.group(1);
	    switch (reference) {
	    case "Name":
		m.appendReplacement(ret, GeneratorUtils.firstCharToUpper(varName));
		break;
	    case "name":
		m.appendReplacement(ret, varName);
		break;
	    case "Class":
		m.appendReplacement(ret, type);
		break;
	    case "class":
		m.appendReplacement(ret, GeneratorUtils.firstCharToLower(type));
		break;
	    default:
		try {
		    String innerType = matcher.group(reference);
		    m.appendReplacement(ret, mapper.getDefaultValueForParameter(innerType, varName));
		} catch (Exception e) {
		    m.appendReplacement(ret, reference);
		}
	    }
	}
	m.appendTail(ret);
	return ret.toString();
    }

}
