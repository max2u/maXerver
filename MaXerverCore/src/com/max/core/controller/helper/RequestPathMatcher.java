package com.max.core.controller.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestPathMatcher {
	private final static Pattern PATH_VARIABLE_PATTERN = Pattern.compile("(\\$\\{\\w*\\})");

	public static Map<String, String> match(String controllerMethodUrl, String requestUrl) {
		try {
			Matcher mvariableMatcher = RequestPathMatcher.PATH_VARIABLE_PATTERN.matcher(controllerMethodUrl);

			List<String> variables = new ArrayList<String>();

			String urlRegex = controllerMethodUrl;
			while (mvariableMatcher.find()) {
				urlRegex = urlRegex.replace(mvariableMatcher.group(), "(.*)");
				variables.add(mvariableMatcher.group().substring(2, mvariableMatcher.group().length() - 1));
			}
			urlRegex = "^" + urlRegex.replace("/", "\\/") + "$";
			Pattern urlPattern = Pattern.compile(urlRegex);

			Matcher urlMatcher = urlPattern.matcher(requestUrl);
			Map<String, String> pathVariables = new HashMap<>();

			if (!urlMatcher.find()) {
				return null;
			}

			for (int i = 0; i < variables.size(); ++i) {
				pathVariables.put(variables.get(i), urlMatcher.group(i + 1));
			}

			return pathVariables;
		} catch (Exception ex) {
		}
		return null;
	}

	public static void main(String[] args) {
		Map<String, String> result = RequestPathMatcher.match("/hello/${terst12}/${something_weird}/hehe",
				"/hello/123123123123/sameer_is_great/hehe");
		if (result != null)
			System.out.println(result.toString());
		else
			System.out.println("not match");
		Map<String, String> result1 = RequestPathMatcher.match("/hello/${terst12}/${something_weird}/hehe",
				"/hello/123123123123/sameer_is_great/hehew");
		if (result1 != null)
			System.out.println(result1.toString());
		else
			System.out.println("not match");

		Map<String, String> result2 = RequestPathMatcher.match("/hello/123123123123/sameer_is_great/hehe",
				"/hello/123123123123/sameer_is_great/hehe");
		if (result2 != null)
			System.out.println(result2.toString());
		else
			System.out.println("not match");

		Map<String, String> result3 = RequestPathMatcher.match("/hello/123123123123/sameer_is_great/hehe",
				"/hello/123123123123/sameer_is_great/hehew");
		if (result3 != null)
			System.out.println(result3.toString());
		else
			System.out.println("not match");
	}

}
