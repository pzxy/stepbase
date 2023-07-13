package com.base.core.head.converter;

import java.util.Map;

import com.base.core.head.utils.DataDictionaryUtils;
import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.json.JsonObject;

/**
 * @author start 
 */
@DocDescription("[数据字典](/static/doc/data_dictionary.html) 格式:{'k':'编号','v':'值'}")
public class DataDictionaryConverterEditor extends AbstractConverterEditor<String> {

	public DataDictionaryConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public void restore(Object value) {
		if(value!=null) {
			setValue(value);
		}
	}

	@Override
	public String converter() {
		if(getValue()==null) {
			return null;
		}
		return String.valueOf(getValue());
	}

	@Override
	public Object getSource() {
		String val=converter();
		if(val==null) {
			return null;
		}
		int index=val.indexOf("_");
		if(index>0&&index!=val.length()-1) {
			String startCode=val.substring(0,index);
			String endVal=val.substring(index+1,val.length());
			if(DataDictionaryUtils.getDataDictionaryMap().containsKey(startCode)) {
				Map<String,Object> map=DataDictionaryUtils.getDataDictionaryMap().get(startCode);
				if(map.containsKey(endVal)) {
					JsonObject json=new JsonObject();
					json.put("k",val);
					json.put("v",map.get(endVal));
					return json;
				}
			}
		}
		return val;
	}
	
}
