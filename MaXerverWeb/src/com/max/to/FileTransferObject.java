package com.max.to;

import java.io.File;

import com.max.core.utils.FileInfo;
import com.max.core.utils.FileInfo.FileType;

public class FileTransferObject {
	
	private String path= "";
	private String parent= "";
	private FileType type= FileType.FILE;
	private String ext= "";
	private boolean file= true;
	
	public FileTransferObject(File file) {
		this.path=file.getAbsolutePath();
		this.parent= file.getParent();
		this.file= file.isFile();
		if (this.file){
			if (file.getName().lastIndexOf(".")>0){
				this.ext= file.getName().substring(file.getName().lastIndexOf("."));
			}
			if(this.ext != null &&  !this.ext.trim().isEmpty()){
				this.type= FileInfo.getType(this.ext);
			}
		}else{
			this.type= FileType.DIRECTORY;
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

}
