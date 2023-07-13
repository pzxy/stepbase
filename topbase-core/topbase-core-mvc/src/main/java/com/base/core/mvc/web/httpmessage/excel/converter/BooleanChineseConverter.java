package com.base.core.mvc.web.httpmessage.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.gitee.magic.core.utils.StringUtils;

/**
 * 
 * @author start
 *
 */
public class BooleanChineseConverter implements Converter<Boolean> {

	@Override
    public Boolean convertToJavaData(ReadConverterContext<?> context) throws Exception {
        String data = context.getReadCellData().getStringValue();
        if(StringUtils.isEmpty(data)) {
        	return null;
        }
        return "是".equals(data);
    }
	
}
