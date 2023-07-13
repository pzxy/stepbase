package com.base.core.head.valid;

import java.util.Map;

import com.base.core.head.utils.DataDictionaryUtils;
import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.valid.ValidCheckCustom;

/**
 * @author start 
 */
@DocDescription("<a href=\"/static/doc/data_dictionary.html\" target=\"_blank\">数据字典</a>格式:字段名_值")
public class DataDictionaryValid implements ValidCheckCustom  {

	private String paramName;
	
	@Override
	public void setParamName(String paramName) {
		this.paramName=paramName;
	}

	@Override
	public boolean check(Object value) {
		try {
			String val=String.valueOf(value);
			int index=val.indexOf("_");
			if(index>0&&index!=val.length()-1) {
				String startCode=val.substring(0,index);
				if(this.paramName.equals(startCode)) {
					if(DataDictionaryUtils.getDataDictionaryMap().containsKey(startCode)) {
						Map<String,Object> map=DataDictionaryUtils.getDataDictionaryMap().get(startCode);
						String endVal=val.substring(index+1,val.length());
						if(map.containsKey(endVal)) {
							return true;
						}
					}
				}
			}
			return false;
		}catch(Exception e) {
			return false;
		}
	}
	
}
