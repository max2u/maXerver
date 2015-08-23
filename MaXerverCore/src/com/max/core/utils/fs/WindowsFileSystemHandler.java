package com.max.core.utils.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WindowsFileSystemHandler extends FileSystemHandler {

	private static WindowsFileSystemHandler instance = null;

	private WindowsFileSystemHandler() {

	}

	@Override
	public File getFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			return null;
		}
		return file;
	}

	@Override
	public List<File> getFiles(String parentPath) {
		parentPath = parentPath.trim();
		List<File> children = new ArrayList<>();
		if (parentPath.isEmpty()) {
			File[] roots = File.listRoots();
			for (int i = 0; i < roots.length; i++) {
				children.add(roots[i]);
			}
		} else {
			File parentDirectory = new File(parentPath);
			if (parentDirectory.exists() && parentDirectory.isDirectory()) {
				for (File file : parentDirectory.listFiles()) {
					children.add(file);
				}
			}
		}
		return children;
	}

	public static WindowsFileSystemHandler getWindowsFileSystemHandlerInstance() {
		if (instance == null) {
			instance = new WindowsFileSystemHandler();
		}
		return instance;
	}
}
