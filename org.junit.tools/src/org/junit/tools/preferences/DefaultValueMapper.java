package org.junit.tools.preferences;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;

public class DefaultValueMapper {

    private final List<ValueMapping> valueMappings = new LinkedList<>();

    private DefaultValueMapper() {
	// private constructor
    }

    public static DefaultValueMapperBuilder builder() {
	return new DefaultValueMapperBuilder();
    }

    public static class DefaultValueMapperBuilder {

	private DefaultValueMapper mapper = new DefaultValueMapper();

	private DefaultValueMapperBuilder() {
	    // private constructor
	}

	public DefaultValueMapperBuilder appendRules(Map<String, String> typeToValueMappings) {
	    if (typeToValueMappings != null) {
		typeToValueMappings.entrySet()
			.forEach(entry -> this.appendRule(entry.getKey(), entry.getValue()));
	    }
	    return this;
	}

	public DefaultValueMapperBuilder appendRule(String type, String defaultValue) {
	    if (StringUtils.isNotBlank(defaultValue)) {
		mapper.valueMappings.add(new ValueMapping(type, defaultValue, mapper));
	    }
	    return this;
	}

	public DefaultValueMapperBuilder appendRule(String defaultValue) {
	    if (StringUtils.isNotBlank(defaultValue)) {
		mapper.valueMappings.add(new ValueMapping(".*", defaultValue, mapper));
	    }
	    return this;
	}

	public DefaultValueMapper build() {
	    return mapper;
	}
    }

    public String getDefaultValueForParameter(String type, String name) {
	return valueMappings.stream()
		.filter(mapping -> mapping.isMatchingType(type))
		.map(mapping -> mapping.getValueForNamedVariable(type, name))
		.filter(Objects::nonNull)
		.findFirst()
		.orElse("null");
    }
}
