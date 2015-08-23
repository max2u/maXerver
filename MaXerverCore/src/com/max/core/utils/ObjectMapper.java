package com.max.core.utils;

public class ObjectMapper {

	public static Object getFromString(String value, Class<?> type) {
		if ( value == null ){
			return null;
		}
		if (String.class.isAssignableFrom(type)) {
			return value;
		}

		if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
			return Integer.parseInt(value);
		}

		if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
			return Long.parseLong(value);
		}

		if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
			return Double.parseDouble(value);
		}

		System.out.println("Type not handled : " + type);
		return value;
	}
}
