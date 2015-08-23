package com.max.core.template;

import java.util.Map.Entry;

import com.max.core.http.Context;

public class TemplateRegistry {

	public static String process(String fileContent, Context context) {
		// FIXME this needs some template engine parsing
		for (Entry<String, Object> modelAttribute : context.getModelAttributes().entrySet()) {
			fileContent = fileContent.replace("#{" + modelAttribute.getKey() + "}", modelAttribute.getValue().toString());
		}
		return fileContent;
	}

}
