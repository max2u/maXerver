package com.max.core.template;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import com.max.core.http.Context;

public class TemplateProvider {

	public static String parse(String fileContent, Context context) {
		String content = TagEvaluator.process(fileContent);
		content = evaluateScript(content, context);

		return content;
	}

	/**
	 * @param fileContent
	 * @param context
	 * @return
	 */
	private static String evaluateScript(String fileContent, Context context) {
		ScriptEvaluator evaluator = new ScriptEvaluator();
		Map<String, Object> contextMap = new HashMap<>();
		contextMap.putAll(context.getModelAttributes());
		try {
			return evaluator.evaluate(fileContent, contextMap);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent;
	}

}
