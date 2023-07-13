package com.base.core.head.converter;

import java.lang.reflect.Field;

import com.gitee.magic.converter.EnumConverterEditor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author start 
 */
public class EnumNameConverterEditor extends EnumConverterEditor {

	public EnumNameConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public Object getSource() {
		return converter();
	}

	@Override
	public String converter() {
		if (getValue() == null) {
			return null;
		}
		String name = String.valueOf(getValue());
		for (Field f : getPrototype().getFields()) {
			if (name.equals(f.getName())) {
				Schema property = f.getAnnotation(Schema.class);
				if (property != null) {
					return property.title();
				}
			}
		}
		return name;
	}

}
