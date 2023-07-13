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
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.head.converter.SerializeConverterEditor;

/**
 * 转换器 application/serialize
 *
 * @author Start
 */
public class SerializeHttpMessageConverter extends BaseHttpMessageConverter<Object> {

    public SerializeHttpMessageConverter() {
        super(new MediaType("application", "serialize", UTF8));
    }
    
    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz instanceof java.io.Serializable;
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
    	SerializeConverterEditor editor=new SerializeConverterEditor(rawType);
    	editor.setValue(content);
    	return editor.converter();
	}

    @Override
	protected void writeInternal(Object t, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
    	SerializeConverterEditor editor=new SerializeConverterEditor(t.getClass());
    	editor.restore(t);
        String content = String.valueOf(editor.getValue());
        OutputStream out = outputMessage.getBody();
        try {
            out.write(content.getBytes(Config.getEncoding()));
            out.flush();
        }catch(Exception e) {
        	throw new NotException(e);
        }
    }

}
