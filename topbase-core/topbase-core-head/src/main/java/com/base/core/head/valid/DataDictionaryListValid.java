package com.base.core.head.valid;

import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.valid.ValidCheckCustom;

/**
 * @author start 
 */
@DocDescription("<a href=\"/static/doc/data_dictionary.html\" target=\"_blank\">数据字典</a>格式:字段名_值")
public class DataDictionaryListValid implements ValidCheckCustom  {

	private String paramName;
	
	@Override
	public void setParamName(String paramName) {
		this.paramName=paramName;
	}

	@Override
	public boolean check(Object value) {
		try {
			JsonArray array=new JsonArray(String.valueOf(value));
			for(int i=0;i<array.length();i++) {
				String val=array.getString(i);
				DataDictionaryValid ddv=new DataDictionaryValid();
				ddv.setParamName(this.paramName);
				if(!ddv.check(val)) {
					return false;
				}
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
}
