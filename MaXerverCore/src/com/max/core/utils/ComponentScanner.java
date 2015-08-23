package com.max.core.utils;

import java.util.List;

import com.max.core.controller.ControllerRegistry;

public class ComponentScanner {

	public static void scan(String packageName) throws Exception {
		List<String> classes = ClasspathScanner.getClassNamesFromPackage(packageName);
		for (String className : classes) {
			Class<?> clazz = Class.forName(className);

			ControllerRegistry.register(clazz);

		}
	}
}
