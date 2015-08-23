/************************************************************************
 * Copyright iMENA Loyalty
 */
package com.max.core.utils;

import com.notmine.gson.Gson;

/**
 * @author sameer
 */
public class JsonUtils {

	/**
	 * @param responseObj
	 * @return
	 */
	public static String toString(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}
}