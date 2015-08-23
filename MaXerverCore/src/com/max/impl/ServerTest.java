package com.max.impl;

import com.max.core.http.HttpServer;
import com.max.core.utils.ComponentScanner;

public class ServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ComponentScanner.scan("com.max.impl");
			HttpServer server = new HttpServer();
			server.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
