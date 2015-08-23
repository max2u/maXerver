package com.max.impl;

import com.max.core.annotation.Controller;
import com.max.core.annotation.RequestMapping;
import com.max.core.http.Context;

@Controller
@RequestMapping("")
public class OneController {

	@RequestMapping("/1")
	public String processHaha1232353(Context context) {
		context.addModelAttributes("title", "One");
		context.addModelAttributes("v1", "A");
		context.addModelAttributes("v2", "B");
		context.addModelAttributes("v3", "C");
		return "one";
	}
}
