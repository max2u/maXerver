package com.max.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author sameer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
		ElementType.METHOD, ElementType.TYPE
})
public @interface RequestMapping {

	/**
	 * the request uri that is processed by the element annotated with
	 * {@link RequestMapping}
	 * 
	 * @return
	 */
	// TODO make it an array
	public String value() default "";

}
