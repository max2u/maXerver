package com.max.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.max.core.annotation.Controller;
import com.max.core.annotation.FileResponse;
import com.max.core.annotation.JSONResponse;
import com.max.core.annotation.PathVariable;
import com.max.core.annotation.RequestMapping;
import com.max.core.http.Context;
import com.max.core.utils.fs.FileSystemHandler;
import com.max.to.FileTransferObject;

@Controller
@RequestMapping("/files")
public class FileSystemController {

	@RequestMapping("/file/${file_path}")
	@FileResponse
	public File fileRequest(@PathVariable("file_path") String filePath, Context context) {
		File file = FileSystemHandler.getInstance().getFile(filePath);
		return file;
	}

	@RequestMapping("/list")
	public String fileListRequest() {
		return "files";
	}

	@RequestMapping("/list/${file_path}")
	@JSONResponse
	public List<FileTransferObject> fileListRequest(@PathVariable("file_path") String filePath, Context context) {
		List<File> fileList = FileSystemHandler.getInstance().getFiles(filePath);

		List<FileTransferObject> fileTOList = new ArrayList<>();
		for (File file : fileList) {
			fileTOList.add(new FileTransferObject(file));
		}

		return fileTOList;
	}
}
