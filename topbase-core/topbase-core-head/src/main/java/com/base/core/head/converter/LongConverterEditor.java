package com.base.core.head.converter;

/**
 * @author start 
 */
public class LongConverterEditor extends com.gitee.magic.converter.LongConverterEditor  {

	public LongConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public Object getSource() {
		return String.valueOf(converter());
	}
	
}
