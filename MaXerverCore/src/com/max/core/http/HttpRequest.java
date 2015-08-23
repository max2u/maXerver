package com.max.core.http;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

	public enum RequestMethod {
		GET, POST
	};

	private RequestMethod method;
	private String url;
	private Map<String, String> requestParams = new HashMap<>();

	private Map<String, String> headers = new HashMap<>();

	public HttpRequest(Socket socket, String request) {
		String[] headerItems = request.split("\\r\\n");
		for (String header : headerItems) {
			String[] items = header.split(":", 2);
			if (items.length == 1) {
				if (items[0].toUpperCase().startsWith("GET")) {
					setMethod(RequestMethod.GET);
					fillUrl(items[0].substring("GET ".length(), items[0].length() - " HTTP/1.1".length()));
				} else if (items[0].toUpperCase().startsWith("POST")) {
					setMethod(RequestMethod.POST);
					fillUrl(items[0].substring("POST ".length(), items[0].length() - " HTTP/1.1".length()));
				}
			} else if (items.length == 2) {
				System.out.println(items[0] + " -> " + items[1]);
				headers.put(items[0], items[1]);
			}
		}
	}

	private void fillUrl(String url) {
		url = url.replace("%20", " ");
		String[] items = url.split("\\?");

		this.setUrl(items[0]);

		this.requestParams.clear();
		if (items.length == 1) {
			return;
		}

		// TODO replace the special chars first
		items = items[1].split("&");
		for (String item : items) {
			String[] param = item.split("=");
			requestParams.put(param[0], param[1]);
		}
	}

	public RequestMethod getMethod() {
		return method;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestParameter(String key) {
		return requestParams.get(key);
	}
}
