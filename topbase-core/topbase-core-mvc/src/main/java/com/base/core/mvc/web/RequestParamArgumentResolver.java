package com.base.core.mvc.web;

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
public class RequestParamArgumentResolver implements HandlerMethodArgumentResolver  {

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
			String value=request.getParameter(key);
			paramValues.put(key, value);
		}
		//PATH
		Map<String, Object> map = (Map<String, Object>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		for(String key:map.keySet()) {
			if(!paramValues.containsKey(key)) {
				paramValues.put(key, map.get(key));
			}
		}
		return ConverterEditorUtils.restoreObject(parameter.getParameterType(), new JsonObject(paramValues));
//		return RestConverterEditor.converterObjectMap(parameter.getParameterType(), paramValues);
	}

}
