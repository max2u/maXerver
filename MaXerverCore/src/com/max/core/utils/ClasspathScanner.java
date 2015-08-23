package com.max.core.utils;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClasspathScanner {

	public static List<String> getClassNamesFromPackage(String packageName) throws Exception {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL packageURL;
		ArrayList<String> names = new ArrayList<String>();
		;

		String packagePath = packageName.replace(".", "/");
		packageURL = classLoader.getResource(packagePath);

		if (packageURL.getProtocol().equals("jar")) {
			String jarFileName;
			JarFile jf;
			Enumeration<JarEntry> jarEntries;
			String entryName;

			// build jar file name, then loop through zipped entries
			jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
			jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
			System.out.println(">" + jarFileName);
			jf = new JarFile(jarFileName);
			jarEntries = jf.entries();
			while (jarEntries.hasMoreElements()) {
				entryName = jarEntries.nextElement().getName();
				if (entryName.startsWith(packagePath) && entryName.length() > packagePath.length() + 5) {
					entryName = entryName.substring(packagePath.length(), entryName.lastIndexOf('.'));
					names.add(entryName);
				}
			}
			jf.close();

			// loop through files in classpath
		} else {
			URI uri = new URI(packageURL.toString());
			File folder = new File(uri.getPath());
			// won't work with path which contains blank (%20)
			// File folder = new File(packageURL.getFile());
			File[] contenuti = folder.listFiles();
			String entryName;
			for (File actual : contenuti) {
				entryName = actual.getName();
				if (actual.isDirectory()) {
					names.addAll(getClassNamesFromPackage(packageName + "." + entryName));
				} else {
					if (entryName.endsWith(".class")) {
						entryName = entryName.substring(0, entryName.lastIndexOf('.'));
						names.add(packageName + "." + entryName);
					}
				}
			}
		}
		return names;
	}
}
