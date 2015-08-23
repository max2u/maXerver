package com.max.core.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpResponse {

	public enum RequestMethod {
		GET, POST
	};

	private String contentType;
	private String serverName;
	private Socket socket;
	boolean headerSent = false;

	/**
	 * @param socket
	 */
	public HttpResponse(Socket socket) {
		this.socket = socket;

		setContentType("text/html");
		try {
			setServerName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			setServerName("localhost");
		}
	}

	public void sendResponse(String response) throws IOException {
		sendResponse(response, 200);
	}

	public void sendResponse(String response, int responseType) throws IOException {
		// send header
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println("HTTP/1.0 " + responseType);
		if (!contentType.isEmpty()) {
			out.println("Content-type: " + contentType);
		}
		if (!serverName.isEmpty()) {
			out.println("Server-name: " + serverName);
		}
		out.println("Content-length: " + response.length());
		out.println("");

		// send the response
		out.println(response);
		out.flush();

	}

	public void sendFile(File file) throws IOException {
		// send header
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println("HTTP/1.0 200");
		contentType = "";
		if (!contentType.isEmpty()) {
			out.println("Content-type: " + contentType);
		}
		if (!serverName.isEmpty()) {
			out.println("Server-name: " + serverName);
		}
		out.println("Content-length: " + file.length());
		out.println("");
		out.flush();

		// send the file content
		byte[] bytearray = new byte[102400];
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);

		int readLength = -1;
		while ((readLength = bis.read(bytearray)) > 0) {
			socket.getOutputStream().write(bytearray, 0, readLength);
		}
		bis.close();
	}

	/**
	 * @return contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return serverName
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @param serverName
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
