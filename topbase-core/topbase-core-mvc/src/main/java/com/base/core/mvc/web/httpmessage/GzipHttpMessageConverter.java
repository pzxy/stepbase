package com.base.core.mvc.web.httpmessage;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Base64;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.base.core.context.mvc.NotException;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.core.utils.GzipComperssedUtils;
import com.gitee.magic.core.utils.reflect.TypeReference;
import com.gitee.magic.core.valid.ValidException;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * 转换器 application/gzip
 *
 * @author Start
 */
public class GzipHttpMessageConverter extends BaseHttpMessageConverter<Object> {

	public GzipHttpMessageConverter() {
        super(new MediaType("application", "gzip", UTF8));
    }
	
	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		Type ltmp;
		Class<?> rawType;
		if(type instanceof ParameterizedType) {
			ltmp=type;
			rawType=(Class<?>) ((ParameterizedType) type).getRawType();
		}else {
			rawType=(Class<?>)type;
			ltmp=new TypeReference<Object>() {}.getType();
		}
		String content=getBodyByBase64(inputMessage.getBody());
    	String encoding=getContentTypeEncoding(inputMessage.getHeaders().getContentType());
		//对GZIP压缩后base64编码的数据进行解压
    	content=new String(GzipComperssedUtils.decompressed(content),encoding);
		AbstractConverterEditor<?> editor = ConverterEditorUtils.getBasicConverter(rawType, ltmp, ltmp);
		try {
			editor.restore(content);
			return editor.getValue();
		}catch(ValidException e) {
			throw e;
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			throw new BusinessException(BaseCode.CODE_1009);
		}
	}

    @Override
	protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
        String content = String.valueOf(ConverterEditorUtils.converterObject(t));
        OutputStream out = outputMessage.getBody();
        try {
            out.write(Base64.getEncoder().encode(GzipComperssedUtils.compressedToBytes(content)));
            out.flush();
        }catch(Exception e) {
        	throw new NotException(e);
        }
    }

}
