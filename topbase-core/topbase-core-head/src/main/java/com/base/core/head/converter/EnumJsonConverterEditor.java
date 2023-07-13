package com.base.core.head.converter;

import java.lang.reflect.Field;

import com.gitee.magic.converter.EnumConverterEditor;
import com.gitee.magic.core.json.JsonObject;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author start 
 */
public class EnumJsonConverterEditor extends EnumConverterEditor {

	public EnumJsonConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public String converter() {
		if (getValue() == null) {
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
		for (Field f : getPrototype().getFields()) {
			if (val.equals(f.getName())) {
				Schema property = f.getAnnotation(Schema.class);
				if (property != null) {
					JsonObject json=new JsonObject();
					json.put(val, property.title());
					return json;
				}
			}
		}
		return val;
	}

}
