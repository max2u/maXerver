package com.max.core.http;

import java.io.IOException;

import com.max.core.controller.ControllerRegistry;

/**
 * @author sameer
 *
 */
public class RequestDispatcher {

	/**
	 * @param exchange
	 */
	public static void dispatch(HttpRequest request, HttpResponse response) {
		try {
			ControllerRegistry.process(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendResponse("<h1>Internal Server Error :</h1> <br/>" + getStackTrace(e), 500);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static String getStackTrace(Exception e) {
		String result = e.getMessage() + "<br/>";
		for (StackTraceElement trace : e.getStackTrace()) {
			result += trace.toString() + "<br/>";
		}
		return result;
	}

}
