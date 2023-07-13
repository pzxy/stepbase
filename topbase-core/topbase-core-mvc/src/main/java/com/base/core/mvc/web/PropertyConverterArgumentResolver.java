package com.base.core.mvc.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.gitee.magic.context.PropertyConverterEditor;
import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.core.converter.PropertyConverter;

/**
 * @author  start
 */
public class PropertyConverterArgumentResolver implements HandlerMethodArgumentResolver  {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(PropertyConverter.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter mp, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String parameterName=mp.getParameterName();
		
		String requestValue=request.getParameter(parameterName);
		if(requestValue==null) {
			Map<String, String> pathParameters = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			requestValue=pathParameters.get(parameterName);
			if(requestValue==null) {
				requestValue=request.getHeader(parameterName);
			}
		}
		if(requestValue==null) {
			return null;
		}
		return getValue(mp,mp.getParameterType(),requestValue);
//		if(mp.hasParameterAnnotation(RequestParam.class)) {
//			String requestValue=request.getParameter(parameterName);
//			return getValue(mp,mp.getParameterType(),requestValue);
//		}else if(mp.hasParameterAnnotation(PathVariable.class)) {
//			Map<String, String> pathParameters = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//			String requestValue=pathParameters.get(parameterName);
//			return getValue(mp,mp.getParameterType(),requestValue);
//		}else if(mp.hasParameterAnnotation(RequestHeader.class)) {
//			String requestValue=request.getHeader(parameterName);
//			return getValue(mp,mp.getParameterType(),requestValue);
//		}
//		return null;
	}
	
	public Object getValue(MethodParameter mp,Class<?> parameterType,Object requestValue) {
		PropertyConverter converter=mp.getParameterAnnotation(PropertyConverter.class);
		if(converter!=null) {
			AbstractConverterEditor<?> editor=PropertyConverterEditor.newInstance(converter.value(), parameterType);
			editor.restore(requestValue);
			return editor.getValue();
		}else {
			AbstractConverterEditor<?> editor=PropertyConverterEditor.getBasicConverter(parameterType);
			editor.restore(requestValue);
			return editor.getValue();
		}
	}

}
