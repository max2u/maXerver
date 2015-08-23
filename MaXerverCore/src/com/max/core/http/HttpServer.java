package com.max.core.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.max.core.GlobalConfiguration;

public class HttpServer {

	private Executor threadPool;

	/**
	 * constructor
	 * 
	 * @param port
	 *            : the port number listening on
	 * @param maxNumberOFThreads
	 *            : maximum number of threads
	 */
	public HttpServer() {
		threadPool = Executors.newFixedThreadPool(GlobalConfiguration.POOL_SIZE);
	}

	/**
	 * starts the HttpServer
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		ServerSocket socket = new ServerSocket(GlobalConfiguration.port);
		System.out.println("Listening on port('" + GlobalConfiguration.port + "')");
		while (GlobalConfiguration.running) {

			final Socket connection = socket.accept();
			threadPool.execute(new Runnable() {
				public void run() {
					HandleRequest(connection);
				}
			});
		}
		socket.close();
	}

	/**
	 * will be called on each request, with
	 * 
	 * @param socket
	 *            : the socket holding the client connection
	 */
	private void HandleRequest(Socket socket) {

		try {
			HttpRequest request = processRequest(socket);

			HttpResponse response = new HttpResponse(socket);
			if (request == null || request.getUrl() == null) {
				socket.close();
				return;
			}
			System.out.println("--- Client request: " + request.getUrl());
			try {
				RequestDispatcher.dispatch(request, response);
			} catch (Exception ex) {
				ex.printStackTrace();

			}
			socket.close();
		} catch (IOException e) {
			System.out.println("Failed respond to client request: " + e.getMessage());
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}

	private HttpRequest processRequest(Socket socket) throws IOException {
		BufferedReader in;
		String request;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		char[] buffer = new char[102400];
		in.read(buffer);
		request = new String(buffer);

		if (request.isEmpty()) {
			return null;
		}

		HttpRequest httpRequest = new HttpRequest(socket, request);
		return httpRequest;
	}
}