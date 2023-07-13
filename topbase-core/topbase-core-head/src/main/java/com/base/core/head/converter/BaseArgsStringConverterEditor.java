package com.base.core.head.converter;

/**
 * @author start 
 */
public abstract class BaseArgsStringConverterEditor extends AbstractArgsConverterEditor<String> {

	public BaseArgsStringConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public void restore(Object value) {
		if (value != null) {
			setValue(String.valueOf(value));
		}
	}

	@Override
	public String converter() {
		if (getValue() == null) {
			return null;
		}
		return String.valueOf(getValue());
	}

}
