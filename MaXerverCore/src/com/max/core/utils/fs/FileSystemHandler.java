package com.max.core.utils.fs;

import java.io.File;
import java.util.List;

import com.max.core.utils.OSUtils;

public abstract class FileSystemHandler {

	private static FileSystemHandler instance = null;

	public static FileSystemHandler getInstance() {
		if (instance == null) {
			instance = OSUtils.getFileSystemHandler();
		}
		return instance;
	}

	public abstract File getFile(String filePath) ;

	public abstract List<File> getFiles(String parentPath) ;
}
