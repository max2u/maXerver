package com.max.impl;

import com.max.core.annotation.Controller;
import com.max.core.annotation.FileResponse;
import com.max.core.annotation.PathVariable;
import com.max.core.annotation.RequestMapping;
import com.max.core.annotation.RequestParam;
import com.max.core.http.Context;
import com.max.core.http.HttpRequest;

@Controller
@RequestMapping
public class HomeController {

	@RequestMapping("/")
	public String processRequest(@RequestParam("hello") Long hello, HttpRequest request, Context context) {
		context.addModelAttributes("title", "sameer is awesome!");
		return "index";
	}

	@RequestMapping("/resource/${file_path}")
	@FileResponse
	public String getFile(@PathVariable("file_path") String filePath) {
		return "resources/" + filePath;
	}

	@RequestMapping("/favicon.ico")
	@FileResponse
	public String getFavicon() {
		return "resources/favicon.ico";
	}

}
