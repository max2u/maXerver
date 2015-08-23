package com.max.core.http;

import java.util.HashMap;
import java.util.Map;

public class Context {

	private Map<String, Object> modelAttributes = new HashMap<String, Object>();

	public Map<String, Object> getModelAttributes() {
		return modelAttributes;
	}

	public void addModelAttributes(String key, Object value) {
		modelAttributes.put(key, value);
	}

}
