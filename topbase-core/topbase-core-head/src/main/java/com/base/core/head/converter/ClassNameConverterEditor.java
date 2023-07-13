package com.base.core.head.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.gitee.magic.core.converter.AbstractConverterEditor;
import com.gitee.magic.core.exception.ApplicationException;

/**
 * @author start 
 */
public class ClassNameConverterEditor extends AbstractConverterEditor<Object> {

	private AbstractConverterEditor<?> editor;
	
	public ClassNameConverterEditor(Class<?> prototype,String className) {
		super(prototype);
		Class<?> target;
		try {
			target=Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new ApplicationException(e);
		}
		Constructor<?> ct=null;
		try {
			ct=target.getDeclaredConstructor(new Class[]{Class.class});
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ApplicationException(e);
		}
		try {
			editor=(AbstractConverterEditor<?>)ct.newInstance(prototype);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void restore(Object value) {
		editor.restore(value);
	}

	@Override
	public Object converter() {
		return editor.converter();
	}

	@Override
	public Object getValue() {
		return editor.getValue();
	}

	@Override
	public Object getSource() {
		return editor.getSource();
	}

	@Override
	public Class<?> getPrototype() {
		return editor.getPrototype();
	}

	@Override
	public void setValue(Object value) {
		editor.setValue(value);
	}

	@Override
	public void setSource(Object source) {
		editor.setSource(source);
	}

	@Override
	public void setPrototype(Class<?> prototype) {
		editor.setPrototype(prototype);
	}

}
