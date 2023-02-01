package com.example.junittoolsdemo.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.junittoolsdemo.models.DemoObject;

/**
 * Spring service with some dependencies
 */
@Service
public class DemoService {

	@Autowired
	private DemoComponent component;

	public String doSomething(String name) {
		return name + "#" + component.doCalculation(name.length());
	}

	public void updateObject(DemoObject object, String value) {
		if (StringUtils.hasLength(value)) {
			object.setDemoString(value);
		}
	}

	public int countSpecialObjects(List<DemoObject> items) {
		return (int) items.stream()
				.filter(this::isSpecial)
				.count();
	}

	public int countSpecialValues(Map<String, DemoObject> items) {
		return (int) items.values().stream()
				.filter(this::isSpecial)
				.count();
	}

	private boolean isSpecial(DemoObject obj) {
		return obj.getDemoString().contains("special");
	}

	public Optional<DemoObject> deleteObjectByName(String name) {
		return Optional.empty();
	}

	public List<DemoObject> findObjectsByName(String name) {
		return Collections.emptyList();
	}

}
