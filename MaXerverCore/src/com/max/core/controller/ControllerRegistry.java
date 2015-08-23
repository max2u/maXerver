package com.max.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.max.core.annotation.Controller;
import com.max.core.annotation.FileResponse;
import com.max.core.annotation.JSONResponse;
import com.max.core.annotation.PathVariable;
import com.max.core.annotation.RequestMapping;
import com.max.core.annotation.RequestParam;
import com.max.core.controller.helper.RequestPathMatcher;
import com.max.core.http.Context;
import com.max.core.http.HttpRequest;
import com.max.core.http.HttpResponse;
import com.max.core.template.TemplateProvider;
import com.max.core.utils.JsonUtils;
import com.max.core.utils.ObjectMapper;

/**
 * @author sameer
 */
public class ControllerRegistry {

	private static Map<Class<?>, Map<Method, String>> registry = new HashMap<Class<?>, Map<Method, String>>();
	private static Map<Class<?>, Object> instances = new HashMap<Class<?>, Object>();

	/**
	 * @param clazz
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void register(Class<?> clazz) throws IllegalAccessException, InstantiationException {
		if (clazz.isAnnotationPresent(Controller.class)) {
			System.out.println(clazz.getSimpleName() + " is a Controller");
			String requestMapping = "";
			if (clazz.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping requestMappingAnnot = clazz.getAnnotation(RequestMapping.class);
				requestMapping = requestMappingAnnot.value();

				Map<Method, String> requestMappings = new HashMap<Method, String>();
				for (Method method : clazz.getMethods()) {
					if (method.isAnnotationPresent(RequestMapping.class)) {
						RequestMapping methodRequestMappingAnnot = method.getAnnotation(RequestMapping.class);
						requestMappings.put(method, requestMapping + methodRequestMappingAnnot.value());
						// TODO make sure not to register same uri more than
						// once
					}
				}
				registry.put(clazz, requestMappings);
				instances.put(clazz, clazz.newInstance());
			}
		}
	}

	/**
	 * @param exchange
	 * @throws InvocationTargetException,
	 *             IllegalAccessException
	 */
	public static void process(HttpRequest request, HttpResponse response) throws InvocationTargetException, IllegalAccessException {
		// TODO handle request and url parameters
		boolean processed = false;
		for (Entry<Class<?>, Map<Method, String>> entry : registry.entrySet()) {
			for (Entry<Method, String> methodEntry : entry.getValue().entrySet()) {
				String methodUrl = methodEntry.getValue();

				Map<String, String> pathVariables = RequestPathMatcher.match(methodUrl, request.getUrl());
				if (pathVariables == null) {
					continue;
				}
				processed = true;
				Method method = methodEntry.getKey();

				// prepare parameters
				Context context = new Context();

				List<Object> params = new ArrayList<>();
				for (Parameter parameter : method.getParameters()) {
					Object value = null;
					RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
					PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
					if (requestParam != null) {
						String paramKey = requestParam.value();
						if (!paramKey.isEmpty()) {
							String parameterValue = request.getRequestParameter(paramKey);
							value = ObjectMapper.getFromString(parameterValue, parameter.getType());
						}

					} else if (pathVariable != null) {
						String paramKey = pathVariable.value();
						if (!paramKey.isEmpty()) {
							String parameterValue = pathVariables.get(paramKey);
							value = ObjectMapper.getFromString(parameterValue, parameter.getType());
						}
					} else if (HttpRequest.class.isAssignableFrom(parameter.getType())) {
						value = request;
					} else if (HttpResponse.class.isAssignableFrom(parameter.getType())) {
						value = response;
					} else if (Context.class.isAssignableFrom(parameter.getType())) {
						value = context;
					}

					params.add(value);
				}

				if (method.isAnnotationPresent(FileResponse.class)) {

					File file = null;
					if (String.class.isAssignableFrom(method.getReturnType())) {
						String filePath = (String) method.invoke(instances.get(entry.getKey()), params.toArray());
						ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
						URL uri = classLoader.getResource(filePath);
						if (uri == null) {
							file = new File(filePath);
						} else {
							file = new File(uri.getPath());
						}
					} else if (File.class.isAssignableFrom(method.getReturnType())) {
						file = (File) method.invoke(instances.get(entry.getKey()), params.toArray());
					}
					if (file != null) {
						try {
							response.sendFile(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if (method.isAnnotationPresent(JSONResponse.class)) {
					Object responseObj = method.invoke(instances.get(entry.getKey()), params.toArray());
					String json = JsonUtils.toString(responseObj);
					try {
						response.setContentType("application/json");
						response.sendResponse(json);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else // template response
				{

					String page = (String) method.invoke(instances.get(entry.getKey()), params.toArray());
					ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
					URL uri = classLoader.getResource("views/" + page + ".html");
					if (uri == null) {

						System.err.println("File " + page + ".html was not found.");
					} else {
						File file = new File(uri.getPath());
						try {
							FileInputStream fis = new FileInputStream(file);
							byte[] data = new byte[(int) file.length()];
							fis.read(data);
							fis.close();
							String fileContent = new String(data, "UTF-8");
							fileContent = TemplateProvider.parse(fileContent, context);
							response.sendResponse(fileContent);

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		if (!processed) {
			try {
				response.sendResponse("", 404);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
