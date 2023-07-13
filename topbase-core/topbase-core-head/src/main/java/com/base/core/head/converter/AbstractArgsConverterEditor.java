package com.base.core.head.converter;

import com.gitee.magic.core.converter.AbstractConverterEditor;

/**
 * @author start 
 */
public abstract class AbstractArgsConverterEditor<T> extends AbstractConverterEditor<T> {
	
	public AbstractArgsConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	/**
	 * 设置参数
	 * @param args
	 */
	public abstract void setArgs(String args);
	
}
