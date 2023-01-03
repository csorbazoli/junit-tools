package com.example.junittoolsdemo.services;

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

	public Optional<DemoObject> deleteObjectByName(String name) {
		return Optional.empty();
	}

}
