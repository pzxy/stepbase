package com.base.core.mvc.web.httpmessage.excel.converter;

import java.util.Map;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.base.core.head.utils.DataDictionaryUtils;
import com.gitee.magic.core.utils.StringUtils;

/**
 * 数据字典
 * @author start
 *
 */
public class DictionaryConverter implements Converter<String> {

	@Override
    public String convertToJavaData(ReadConverterContext<?> context) throws Exception {
        String data = context.getReadCellData().getStringValue();
        if(StringUtils.isEmpty(data)) {
        	return null;
        }
        String fieldName=context.getContentProperty().getField().getName();
        Map<String,String> dict=DataDictionaryUtils.getDictoinary(fieldName);
    	for(String k:dict.keySet()) {
    		if(data.equals(dict.get(k))){
    			return k;
    		}
    	}
    	return null;
    }
	
}
