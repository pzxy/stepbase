package com.base.core.mvc.web.httpmessage;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.base.core.context.mvc.NotException;
import com.base.core.mvc.web.MethodParameterValid;
import com.gitee.magic.context.ConverterEditorUtils;
import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.framework.base.constant.Config;

/**
 * 转换器 application/json
 *
 * @author Start
 */
public class JsonHttpMessageConverter extends BaseHttpMessageConverter<Object> {

    public JsonHttpMessageConverter() {
        super(new MediaType("application", "json", UTF8));
    }

    @Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		AbstractConverterEditor<?> editor = MethodParameterValid.getRequestBodyEditor(inputMessage.getHeaders().getContentType(),inputMessage.getBody(),type);
		return editor.getValue();
	}

    @Override
	protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
        String content = String.valueOf(ConverterEditorUtils.converterObject(t));
        OutputStream out = outputMessage.getBody();
        try {
            out.write(content.getBytes(Config.getEncoding()));
            out.flush();
        }catch(Exception e) {
        	throw new NotException(e);
        }
    }

}
