package com.max.core.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileInfo {

	private static Set<String> video = new HashSet<String>(Arrays.asList(new String[] {
			"mp4", "avi", "flv", "mkv", "wmv"
	}));
	private static Set<String> music = new HashSet<String>(Arrays.asList(new String[] {
			"mp3", "wav", "wma", "ogg"
	}));
	private static Set<String> image = new HashSet<String>(Arrays.asList(new String[] {
			"png", "bmp", "jpg", "jpeg", "gif", "tif", "tiff"
	}));
	private static Set<String> code = new HashSet<String>(Arrays.asList(new String[] {
			"java", "cpp", "hpp", "c", "h", "python", "php", "html", "js", "css"
	}));

	public static FileType getType(String ext) {
		if (ext == null || ext.trim().isEmpty()) {
			return null;
		}
		ext = ext.trim().toLowerCase();
		if (video.contains(ext)) {
			return FileType.VIDEO;
		}
		if (music.contains(ext)) {
			return FileType.MUSIC;
		}
		if (image.contains(ext)) {
			return FileType.IMAGE;
		}
		if (code.contains(ext)) {
			return FileType.CODE;
		}

		if (ext.contains("doc")) {
			return FileType.DOCUMENT;
		}
		if (ext.contains("xls")) {
			return FileType.SPREADSHEET;
		}
		if (ext.contains("pdf")) {
			return FileType.PDF;
		}
		if (ext.contains("doc")) {
			return FileType.DOCUMENT;
		}

		return FileType.FILE;
	}

	public static enum FileType {
		FILE, MUSIC, VIDEO, IMAGE, DOCUMENT, SPREADSHEET, PDF, CODE, DIRECTORY;
	}

}
