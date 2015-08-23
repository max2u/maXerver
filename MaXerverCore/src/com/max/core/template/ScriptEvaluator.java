package com.max.core.template;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/************************************************************************
 * Copyright iMENA Loyalty
 */

/**
 * @author sameer
 *
 */
public class ScriptEvaluator {
	private final static Pattern STRING_REGEX = Pattern.compile("#\\{([^\\}]*)\\}");
	private final static Pattern SEPARATOR_REGEX = Pattern.compile("([ |+|-|\\/|\\*|\\&\\&|\\|\\|])");

	public final static String LOOP_TAG = "max:foreach";
	public final static String LOOP_ITEMS = "items";
	public final static String LOOP_VAR = "var";

	public final static String IF_TAG = "max:if";
	public final static String IF_TEST = "test";

	/**
	 * @param script
	 * @param context
	 * @throws ScriptException
	 */
	public String evaluate(String script, Map<String, Object> context) throws ScriptException {

		String evaluated = evaluateMethods(script, context);

		evaluated = evaluateScripts(evaluated, context);

		return evaluated;
	}

	/**
	 * @param script
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	private String evaluateMethods(String script, Map<String, Object> context) throws ScriptException {
		String evaluated = evaluateLoops(script, context);
		evaluated = evaluateConditions(evaluated, context);
		return evaluated;
	}

	/**
	 * @param script
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	private String evaluateConditions(String script, Map<String, Object> context) throws ScriptException {
		while (script.contains("<" + IF_TAG)) {

			int index = script.indexOf("<" + IF_TAG);
			int contentBeginIndex = script.indexOf(">", index) + 1;
			int contentEndIndex = script.indexOf("</" + IF_TAG + ">", index);
			String content = script.substring(contentBeginIndex, contentEndIndex);

			int testBeginIndex = script.indexOf(IF_TEST + "=", index) + IF_TEST.length() + 2;
			int testEndIndex = script.indexOf(script.substring(testBeginIndex - 1, testBeginIndex), testBeginIndex);

			String condition = script.substring(testBeginIndex, testEndIndex);

			condition = evaluateScripts(condition, context);
			boolean testResult = toBoolean(condition);
			String result = (testResult ? content : "");
			script = script.replace(script.substring(index, contentEndIndex + ("</" + IF_TAG + ">").length()), result);
		}
		return script;
	}

	/**
	 * @param condition
	 * @return
	 * @throws ScriptException
	 */
	private boolean toBoolean(String condition) throws ScriptException {
		condition = fixHTMLFriendly(condition);
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		return (boolean) se.eval(condition);
	}

	/**
	 * @param condition
	 * @return
	 */
	private String fixHTMLFriendly(String str) {
		// TODO fix this
		str = str.replace("&gt;", ">");
		str = str.replace("&lt;", "<");
		str = str.replace("&amp;", "&");
		return str;
	}

	/**
	 * @param script
	 * @param context
	 * @return
	 */
	private String evaluateLoops(String script, Map<String, Object> context) {
		while (script.contains("<" + LOOP_TAG)) {

			int index = script.indexOf("<" + LOOP_TAG);
			int contentBeginIndex = script.indexOf(">", index) + 1;
			int contentEndIndex = script.indexOf("</" + LOOP_TAG + ">", index);
			String content = script.substring(contentBeginIndex, contentEndIndex);

			int itemsBeginIndex = script.indexOf(LOOP_ITEMS + "=", index) + LOOP_ITEMS.length() + 2;
			int itemsEndIndex = script.indexOf(script.substring(itemsBeginIndex - 1, itemsBeginIndex), itemsBeginIndex);

			int varBeginIndex = script.indexOf(LOOP_VAR + "=", index) + LOOP_VAR.length() + 2;
			int varEndIndex = script.indexOf(script.substring(varBeginIndex - 1, varBeginIndex), varBeginIndex);

			Object collObj = context.get(script.substring(itemsBeginIndex, itemsEndIndex));

			String varName = script.substring(varBeginIndex, varEndIndex);

			String result = "";
			if (collObj != null && Collection.class.isAssignableFrom(collObj.getClass())) {
				Collection<?> coll = (Collection<?>) collObj;
				int idx = 0;
				for (Object item : coll) {
					String newField = "__" + script.substring(itemsBeginIndex, itemsEndIndex) + "__" + ++idx;
					context.put(newField, item);
					result += content.replace(varName, newField);
				}
			} else {
				result = content;
				System.out.println("--------------- unhandled collection type " + (collObj != null ? collObj.getClass() : "null")
						+ " --------------- ");
			}
			script = script.replace(script.substring(index, contentEndIndex + ("</" + LOOP_TAG + ">").length()), result);
		}
		return script;
	}

	/**
	 * @param script
	 * @param context
	 * @return
	 * @throws ScriptException
	 */
	private String evaluateScripts(String script, Map<String, Object> context) throws ScriptException {
		Matcher matcher = STRING_REGEX.matcher(script);
		while (matcher.find()) {
			String evaluatedScript = evaluateScript(matcher.group(1), context);
			script = script.replace("#{" + matcher.group(1) + "}", toString(evaluatedScript));
		}
		return script;
	}

	/**
	 * @param evaluatedScript
	 * @return
	 * @throws ScriptException
	 */
	private String toString(String evaluatedScript) {
		// TODO
		// try{
		// evaluatedScript= fixHTMLFriendly(evaluatedScript);
		// ScriptEngineManager sem = new ScriptEngineManager();
		// ScriptEngine se = sem.getEngineByName("JavaScript");
		// evaluatedScript= se.eval(evaluatedScript).toString();
		// }catch(ScriptException ex){}
		return evaluatedScript;
	}

	/**
	 * @param group
	 * @return
	 */
	private String evaluateScript(String script, Map<String, Object> context) {
		String[] items = SEPARATOR_REGEX.split(script);
		String result = script;
		for (String item : items) {
			for (String contextItem : context.keySet()) {
				if (item.startsWith(contextItem)) {
					result = result.replace(item, evaluateItem(item, context.get(contextItem)));
				}
			}
		}

		return result;
	}

	/**
	 * @param item
	 * @param object
	 * @return
	 */
	private String evaluateItem(String item, Object context) {
		if (item.contains(".")) {
			String[] path = item.split("\\.");
			Object obj = context;
			for (int index = 1; index < path.length; ++index) {
				String fieldName = path[index];

				try {
					String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method method = obj.getClass().getDeclaredMethod(methodName);
					obj = method.invoke(obj);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					try {
						Field field = obj.getClass().getDeclaredField(fieldName);
						obj = field.get(obj);
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
			}
			return obj.toString();
		}
		return context.toString();
	}

}
