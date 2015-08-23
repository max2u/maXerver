package com.max.core.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class TagEvaluator {
	private static final String INCLUDE_TAG = "max:include";
	private static final String FILENAME_TAG = "file";

	public static String process(String content) {
		while (content.contains("<" + INCLUDE_TAG)) {
			int index = content.indexOf("<" + INCLUDE_TAG);
			int fileNameBeginIndex = content.indexOf(FILENAME_TAG, index) + FILENAME_TAG.length() + 2;
			int fileNameEndIndex = content.indexOf(content.substring(fileNameBeginIndex - 1, fileNameBeginIndex), fileNameBeginIndex);

			String fileName = content.substring(fileNameBeginIndex, fileNameEndIndex);
			String fileContent = readContent(fileName);
			content = content.replace(content.subSequence(index, content.indexOf("/>", index) + 2), fileContent);
		}
		return content;
	}

	private static String readContent(String fileSimpleName) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL uri = classLoader.getResource("views/" + fileSimpleName + ".html");
		if (uri == null) {
			System.err.println("File " + fileSimpleName + ".html was not found.");
			return "";
		}
		File file = new File(uri.getPath());
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			return new String(data, "UTF-8");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
