package com.base.core.head.converter;

import java.lang.reflect.Array;

import com.gitee.magic.core.json.JsonArray;

/**
 * @author start 
 */
public abstract class BaseArgsStringArrayConverterEditor extends AbstractArgsConverterEditor<JsonArray> {

	public BaseArgsStringArrayConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public void restore(Object value) {
		if(value!=null) {
			JsonArray array=new JsonArray(String.valueOf(value));
//			setSource(array);
			Object arrays =  Array.newInstance(String.class, array.length());
			for (int i = 0; i < array.length(); i++) {
				Array.set(arrays, i, array.getString(i));
			}
			setValue(arrays);
		}
	}

	@Override
	public JsonArray converter() {
		if(getValue()==null) {
			return null;
		}
		return new JsonArray(getValue());
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		if(getValue()!=null) {
			setSource(new JsonArray(getValue()));
		}
	}

}
