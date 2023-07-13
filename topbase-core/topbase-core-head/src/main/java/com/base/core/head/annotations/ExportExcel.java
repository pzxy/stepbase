package com.base.core.head.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * easyexcel 导出注解
 * @author start
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportExcel {
	int index() default -1;
	short height() default 300*2;
	int width() default 2048*4;
	Class<?> converter() default DefaultConverter.class;
	
	public static class DefaultConverter {
		
	    public String converter(Object data) {
	    	return String.valueOf(data);
	    }
	    
	}
	
}
