package com.base.core.mvc.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import com.base.core.head.constants.CodeResVal;
import com.base.core.mvc.business.CommonBusiness;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.CheckUtils;
import com.gitee.magic.core.utils.StringUtils;
import com.gitee.magic.core.utils.codec.Base64;
import com.gitee.magic.core.utils.reflect.TypeReference;
import com.gitee.magic.core.valid.ValidContext;
import com.gitee.magic.core.valid.ValidException;
import com.gitee.magic.core.valid.annotation.BooleanValid;
import com.gitee.magic.core.valid.annotation.Custom;
import com.gitee.magic.core.valid.annotation.Enum;
import com.gitee.magic.core.valid.annotation.Equals;
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
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * @author  start
 */
public class MethodParameterValid {
	
	@SuppressWarnings("unchecked")
	public static void valid(HttpServletRequest request,HandlerMethod handlerMethod) {
		for(MethodParameter mp:handlerMethod.getMethodParameters()) {
			if(mp.hasParameterAnnotation(RequestBody.class)) {
    			String contentType=request.getContentType();
    			if(StringUtils.isEmpty(contentType)) {
    				throw new BusinessException(CodeResVal.CODE_1018);
    			}
    		}else if(mp.hasParameterAnnotation(RequestPart.class)) {
    			if(!CommonBusiness.isFileContentType(request.getContentType())) {
    				throw new BusinessException(CodeResVal.CODE_1019,MediaType.MULTIPART_FORM_DATA_VALUE);
    			}
    		}
			if(!isValid(mp)) {
				continue;
			}
    		String parameterName=mp.getParameter().getName();
    		if(mp.hasParameterAnnotation(RequestHeader.class)) {
    			String requestValue=request.getHeader(parameterName);
    			MethodParameterValid.valid(mp,parameterName,requestValue);
    		}else if(mp.hasParameterAnnotation(PathVariable.class)) {
    			Map<String, String> pathParameters = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    			String requestValue=pathParameters.get(parameterName);
    			MethodParameterValid.valid(mp,parameterName,requestValue);
    		}else if(mp.hasParameterAnnotation(RequestParam.class)) {
    			String requestValue=request.getParameter(parameterName);
    			MethodParameterValid.valid(mp,parameterName,requestValue);
    		}else if(mp.hasParameterAnnotation(CookieValue.class)) {
    			//TODO:暂不支持CookieValue
    		}else if(mp.hasParameterAnnotation(RequestBody.class)) {
    			MediaType mediaType=MediaType.valueOf(request.getContentType());
    			InputStream inputStream=null;
    			try {
    				inputStream=request.getInputStream();
				} catch (IOException e) {
					throw new ApplicationException(e);
				}
    			AbstractConverterEditor<?> editor = getRequestBodyEditor(mediaType,inputStream,mp.getGenericParameterType());
				MethodParameterValid.valid(mp,parameterName,editor.getSource());
    		}
    	}
	}
	
	public static Boolean isValid(MethodParameter mp) {
		return mp.hasParameterAnnotation(NotNull.class)
				||mp.hasParameterAnnotation(NotEmpty.class)
				||mp.hasParameterAnnotation(Equals.class)
				||mp.hasParameterAnnotation(NotEquals.class)
				||mp.hasParameterAnnotation(Length.class)
				||mp.hasParameterAnnotation(ShortValid.class)
				||mp.hasParameterAnnotation(IntegerValid.class)
				||mp.hasParameterAnnotation(LongValid.class)
				||mp.hasParameterAnnotation(FloatValid.class)
				||mp.hasParameterAnnotation(DoubleValid.class)
				||mp.hasParameterAnnotation(Enum.class)
				||mp.hasParameterAnnotation(BooleanValid.class)
				||mp.hasParameterAnnotation(TimeFormat.class)
				||mp.hasParameterAnnotation(Format.class)
				||mp.hasParameterAnnotation(Regex.class)
				||mp.hasParameterAnnotation(Custom.class);
	}

	public static void valid(MethodParameter mp,String parameterName,Object requestValue) {
		ValidContext.verify(mp.getParameterAnnotation(NotNull.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(NotEmpty.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(Equals.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(NotEquals.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(Length.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(ShortValid.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(IntegerValid.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(LongValid.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(FloatValid.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(DoubleValid.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(Enum.class),mp.getParameterType(),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(BooleanValid.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(TimeFormat.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(Format.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(Regex.class),requestValue,parameterName);
		ValidContext.verify(mp.getParameterAnnotation(Custom.class),requestValue,parameterName);
	}
	
	public static AbstractConverterEditor<?> getRequestBodyEditor(MediaType mediaType,InputStream inputStream,Type type) {
		Type ltmp;
		Class<?> rawType;
		if(type instanceof ParameterizedType) {
			ltmp=type;
			rawType=(Class<?>) ((ParameterizedType) type).getRawType();
		}else {
			rawType=(Class<?>)type;
			ltmp=new TypeReference<Object>() {}.getType();
		}
		String content=new String(IoUtils.inputStreamConvertBytes(inputStream, -1));
    	if(CheckUtils.isBase64(content)) {
    		String encoding=mediaType.getCharset()==null?Config.getEncoding():mediaType.getCharset().name();
    		try {
				content=new String(Base64.decode(content),encoding);
			} catch (UnsupportedEncodingException e) {
				throw new BusinessException(BaseCode.CODE_1009);
			}
    	}
		AbstractConverterEditor<?> editor = ConverterEditorUtils.getBasicConverter(rawType, ltmp, ltmp);
		try {
			editor.restore(content);
		}catch(ValidException e) {
			throw e;
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			throw new BusinessException(BaseCode.CODE_1009);
		}
		return editor;
	}
	
}
