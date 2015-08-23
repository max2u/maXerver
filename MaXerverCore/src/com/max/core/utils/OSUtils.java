package com.max.core.utils;

import com.max.core.utils.fs.FileSystemHandler;
import com.max.core.utils.fs.WindowsFileSystemHandler;

public class OSUtils {

	private OSUtils() {
	}

	public static OS getOS() {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows")) {
			return OS.WINDOWS;
		} else if (os.toLowerCase().contains("android")) {
			return OS.ANDROID;
		} else if (os.toLowerCase().contains("mac")) {
			return OS.MAC_OS;
		}
		return OS.LINUX;
	}

	public static FileSystemHandler getFileSystemHandler() {
		OS os = getOS();
		if (os == OS.WINDOWS) {
			return WindowsFileSystemHandler.getWindowsFileSystemHandlerInstance();
		}
		// TODO implement if needed
		return WindowsFileSystemHandler.getWindowsFileSystemHandlerInstance();
	}

	public static enum OS {
		WINDOWS, ANDROID, MAC_OS, LINUX;
	}
}
