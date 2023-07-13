package com.base.component.upload.converter;

import java.lang.reflect.Array;

import com.base.component.upload.service.BaseUploadService;
import com.base.core.context.utils.SpringUtils;
import com.base.core.head.converter.BaseArgsStringArrayConverterEditor;
import com.gitee.magic.core.json.JsonArray;

/**
 * @author start 
 */
public class UploadKeyArrayConverterEditor extends BaseArgsStringArrayConverterEditor {

	public UploadKeyArrayConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public Object getSource() {
		BaseUploadService service=SpringUtils.getBean(BaseUploadService.class);
		JsonArray array=converter();
		Object arrays =  Array.newInstance(String.class, array.length());
		for(int i=0;i<array.length();i++) {
			Array.set(arrays, i, service.getUrl(array.getString(i),false));
		}
		return arrays;
	}

	@Override
	public void setArgs(String args) {
		
	}
	
}
