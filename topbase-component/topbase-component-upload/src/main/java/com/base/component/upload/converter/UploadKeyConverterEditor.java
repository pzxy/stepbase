package com.base.component.upload.converter;

import com.base.component.upload.service.BaseUploadService;
import com.base.core.context.utils.SpringUtils;
import com.base.core.head.converter.BaseArgsStringConverterEditor;

/**
 * @author start 
 */
public class UploadKeyConverterEditor extends BaseArgsStringConverterEditor {

	public UploadKeyConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public Object getSource() {
		BaseUploadService service=SpringUtils.getBean(BaseUploadService.class);
		return service.getUrl(converter(),false);
	}

	@Override
	public void setArgs(String args) {
		
	}
	
}
