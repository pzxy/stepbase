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
import com.gitee.magic.core.valid.ValidContext;
import com.gitee.magic.core.valid.annotation.BooleanValid;
import com.gitee.magic.core.valid.annotation.Custom;
import com.gitee.magic.core.valid.annotation.Enum;
import com.gitee.magic.core.valid.annotation.Format;
import com.gitee.magic.core.valid.annotation.Length;
import com.gitee.magic.core.valid.annotation.NotEmpty;
import com.gitee.magic.core.valid.annotation.NotEquals;
import com.gitee.magic.core.valid.annotation.NotNull;
import com.gitee.magic.core.valid.annotation.Regex;
import com.gitee.magic.core.valid.annotation.TimeFormat;
import com.gitee.magic.core.valid.annotation.number.DoubleValid;
import com.gitee.magic.core.valid.annotation.number.FloatValid;
import com.gitee.magic.core.valid.annotation.number.IntegerValid;
import com.gitee.magic.core.valid.annotation.number.LongValid;
import com.gitee.magic.core.valid.annotation.number.ShortValid;

/**
 * @author  start
 */
public class RequestParamArgumentValidResolver implements HandlerMethodArgumentResolver  {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(NotNull.class)
				||parameter.hasParameterAnnotation(NotEmpty.class)
				||parameter.hasParameterAnnotation(NotEquals.class)
				||parameter.hasParameterAnnotation(Length.class)
				||parameter.hasParameterAnnotation(ShortValid.class)
				||parameter.hasParameterAnnotation(IntegerValid.class)
				||parameter.hasParameterAnnotation(LongValid.class)
				||parameter.hasParameterAnnotation(FloatValid.class)
				||parameter.hasParameterAnnotation(DoubleValid.class)
				||parameter.hasParameterAnnotation(Enum.class)
				||parameter.hasParameterAnnotation(BooleanValid.class)
				||parameter.hasParameterAnnotation(TimeFormat.class)
				||parameter.hasParameterAnnotation(Format.class)
				||parameter.hasParameterAnnotation(Regex.class)
				||parameter.hasParameterAnnotation(Custom.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String parameterName=parameter.getParameterName();
		String requestValue=request.getParameter(parameter.getParameterName());
		if(requestValue==null) {
			//PATH
			Map<String, String> pathParameters = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
			requestValue=pathParameters.get(parameter.getParameterName());
		}
		ValidContext.verify(parameter.getParameterAnnotation(NotNull.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(NotEmpty.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(NotEquals.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(Length.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(ShortValid.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(IntegerValid.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(LongValid.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(FloatValid.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(DoubleValid.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(Enum.class),parameter.getParameterType(),requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(BooleanValid.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(TimeFormat.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(Format.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(Regex.class), requestValue,parameterName);
		ValidContext.verify(parameter.getParameterAnnotation(Custom.class), requestValue,parameterName);
		PropertyConverter converter=parameter.getParameterAnnotation(PropertyConverter.class);
		if(converter!=null) {
			AbstractConverterEditor<?> editor=PropertyConverterEditor.newInstance(converter.value(), parameter.getParameterType());
			editor.restore(requestValue);
			return editor.getValue();
		}else {
			AbstractConverterEditor<?> editor=PropertyConverterEditor.getBasicConverter(parameter.getParameterType());
			editor.restore(requestValue);
			return editor.getValue();
		}
	}

}
