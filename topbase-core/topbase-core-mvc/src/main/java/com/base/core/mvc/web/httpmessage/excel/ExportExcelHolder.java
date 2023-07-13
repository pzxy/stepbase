package com.base.core.mvc.web.httpmessage.excel;

import com.base.core.head.annotations.ExportExcel;

/**
 * 
 * @author start
 *
 */
public class ExportExcelHolder{
	
	/**
	 * 标题
	 */
	private String headName;

	/**
	 * 字段名
	 */
	private String fieldName;
	
	private ExportExcel annCls;
	
	public String getHeadName() {
		return headName;
	}
	
	public void setHeadName(String headName) {
		this.headName = headName;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public ExportExcel getAnnCls() {
		return annCls;
	}

	public void setAnnCls(ExportExcel annCls) {
		this.annCls = annCls;
	}
	
	

}
