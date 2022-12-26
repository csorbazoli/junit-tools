package com.example.junittoolsdemo.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.junittoolsdemo.models.DemoObject;

/**
 * Spring service with some dependencies
 */
@Service
public class DemoService {

	private final DemoComponent component;

	public DemoService(DemoComponent component) {
		this.component = component;
	}

	public String doSomething(String name) {
		return name + "#" + component.doCalculation(name.length());
	}

	public void updateObject(DemoObject object, String value) {
		if (StringUtils.hasLength(value)) {
			object.setDemoString(value);
		}
	}

}
