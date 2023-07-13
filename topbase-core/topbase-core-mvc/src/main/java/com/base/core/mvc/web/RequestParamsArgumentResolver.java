package com.base.core.mvc.web;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.base.core.context.annotation.ValidRequestParam;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.json.JsonObject;

/**
 * @author  start
 */
@Deprecated
public class RequestParamsArgumentResolver implements HandlerMethodArgumentResolver  {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ValidRequestParam.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Map<String,Object> paramValues=new HashMap<>(100);
		//Parameter
		Enumeration<String> names=request.getParameterNames();
		while(names.hasMoreElements()) {
			String key=names.nextElement();
			String[] values=request.getParameterValues(key);
			paramValues.put(key, values);
		}
		//PATH
		Map<String, Object> map = (Map<String, Object>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		for(String key:map.keySet()) {
			paramValues.put(key, new String[]{(String) map.get(key)});
		}
		Map<String,Field> fieldMapping=new HashMap<>(100);
		Class<?> curCls=parameter.getParameterType();
		while(!curCls.equals(Object.class)) {
			Field[] fields=curCls.getDeclaredFields();
			for(Field field:fields) {
				fieldMapping.put(field.getName(), field);
			}
			curCls=curCls.getSuperclass();
		}
		for(String name:paramValues.keySet()) {
			if(fieldMapping.containsKey(name)) {
				Field field=fieldMapping.get(name);
				Class<?> prototype=field.getType();
				if(!(prototype.isArray()||
						Collection.class.isAssignableFrom(prototype))) {
					//过滤出不是数组的值
					String[] values=(String[])paramValues.get(name);
					paramValues.put(name, values[0]);
				}
			}
		}
		return ConverterEditorUtils.restoreObject(parameter.getParameterType(), new JsonObject(paramValues));
//		return RestConverterEditor.converterObjectMap(parameter.getParameterType(), paramValues);
	}
	
}
