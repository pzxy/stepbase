package com.base.core.mvc.web.httpmessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.gitee.magic.context.PropertyConverterEditor;
import com.gitee.magic.core.exception.ApplicationException;
import com.gitee.magic.core.utils.CheckUtils;
import com.gitee.magic.framework.base.constant.Config;
import com.gitee.magic.framework.base.result.BaseResponse;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.IoUtils;
import com.gitee.magic.framework.head.vo.BaseVO;

/**
 * 转换器
 *
 * @author Start
 */
public abstract class BaseHttpMessageConverter<T> extends AbstractGenericHttpMessageConverter<Object> {

    public final static Charset UTF8 = Charset.forName(Config.getEncoding());
	
    protected BaseHttpMessageConverter(MediaType supportedMediaType) {
    	super(supportedMediaType);
    }
    
    @Override
    protected boolean supports(Class<?> clazz) {
    	String simpleName=clazz.getSimpleName();
        return BaseResponse.class.isAssignableFrom(clazz) 
        		||BaseVO.class.isAssignableFrom(clazz) 
        		||PropertyConverterEditor.isSupportConverter(clazz)
//        		||List.class.isAssignableFrom(clazz)
        		||simpleName.endsWith("Param")
        		||simpleName.endsWith("Result")
        		||simpleName.endsWith("AO")
        		||simpleName.endsWith("VO")
        		||simpleName.endsWith("DTO");
    }
    
    public String getContentTypeEncoding(MediaType type) {
    	return type.getCharset()==null?Config.getEncoding():type.getCharset().name();
    }
    
    public String getBodyByBase64(InputStream is) {
    	String content=new String(IoUtils.inputStreamConvertBytes(is, -1));
    	if(!CheckUtils.isBase64(content)) {
			throw new BusinessException(BaseCode.CODE_1011);
		}
    	return content;
    }
    
    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
    	throw new ApplicationException("impl read method");
    }

}
