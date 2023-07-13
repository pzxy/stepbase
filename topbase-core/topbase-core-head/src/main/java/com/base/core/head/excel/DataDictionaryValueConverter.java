package com.base.core.head.excel;

import com.base.core.head.annotations.ExportExcel.DefaultConverter;
import com.gitee.magic.core.json.JsonObject;

/**
 * 
 * @author start
 *
 */
public class DataDictionaryValueConverter extends DefaultConverter {

	@Override
	public String converter(Object data) {
		if(data instanceof JsonObject) {
			JsonObject json=(JsonObject)data;
			return json.getString("v");
		}
		return super.converter(data);
	}
	
	
}
