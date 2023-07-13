package com.base.core.mvc.web.httpmessage.excel.converter;

import java.lang.reflect.Field;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.gitee.magic.core.utils.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 枚举
 * @author start
 *
 */
public class EnumConverter implements Converter<Enum<?>> {

	@Override
    public Enum<?> convertToJavaData(ReadConverterContext<?> context) throws Exception {
        String data = context.getReadCellData().getStringValue();
        if(StringUtils.isEmpty(data)) {
        	return null;
        }
        Class<?> prototype=context.getContentProperty().getField().getType();
        for(Object e:prototype.getEnumConstants()) {
        	for (Field f : prototype.getFields()) {
    			Schema property = f.getAnnotation(Schema.class);
    			if (property != null) {
    				if(f.getName().equals(e.toString())) {
    					return (Enum<?>)e;
    				}
    			}
    			
        	}
		}
    	return null;
    }
	
}
