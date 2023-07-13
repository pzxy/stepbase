package com.base.core.mvc.web.httpmessage;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.base.core.context.mvc.NotException;
import com.gitee.magic.context.PropertyConverterEditor;
import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.framework.base.constant.Config;

/**
 * 转换器 application/text
 *
 * @author Start
 */
public class TextHttpMessageConverter extends BaseHttpMessageConverter<Object> {

    public TextHttpMessageConverter() {
        super(new MediaType("application", "text", UTF8));
    }
    
    @Override
    protected boolean supports(Class<?> clazz) {
        return PropertyConverterEditor.isSupportConverter(clazz);
    }
    
    @Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		Class<?> rawType;
		if(type instanceof ParameterizedType) {
			rawType=(Class<?>) ((ParameterizedType) type).getRawType();
		}else {
			rawType=(Class<?>)type;
		}
    	String content=getBodyByBase64(inputMessage.getBody());
    	AbstractConverterEditor<?> editor=PropertyConverterEditor.getBasicConverter(rawType);
    	editor.restore(content);
    	return editor.getValue();
	}

    @Override
	protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
    	AbstractConverterEditor<?> editor=PropertyConverterEditor.getBasicConverter(t.getClass());
    	editor.setValue(t);
        String content = String.valueOf(editor.getSource());
        OutputStream out = outputMessage.getBody();
        try {
            out.write(content.getBytes(Config.getEncoding()));
            out.flush();
        }catch(Exception e) {
        	throw new NotException(e);
        }
    }

}
