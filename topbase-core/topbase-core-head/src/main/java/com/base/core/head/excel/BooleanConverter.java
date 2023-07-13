package com.base.core.head.excel;

import com.base.core.head.annotations.ExportExcel.DefaultConverter;

/**
 * 
 * @author start
 *
 */
public class BooleanConverter extends DefaultConverter {

	@Override
	public String converter(Object data) {
		return ((Boolean)data)?"是":"否";
	}
	
}
