package com.base.component.upload.converter;

import com.base.component.upload.service.BaseUploadService;
import com.base.core.context.utils.SpringUtils;
import com.base.core.head.converter.BaseArgsStringConverterEditor;

/**
 * @author start 
 */
public class UploadKeyVideoSnapshotConverterEditor extends BaseArgsStringConverterEditor {

	private String args;

	public UploadKeyVideoSnapshotConverterEditor(Class<?> prototype) {
		super(prototype);
	}

	@Override
	public Object getSource() {
		String[] size=this.args.split("|");
		Integer width=Integer.parseInt(size[0]);
		Integer height=Integer.parseInt(size[1]);
		BaseUploadService service=SpringUtils.getBean(BaseUploadService.class);
		return service.getVideoSnapshotUrl(converter(),width,height);
	}
	
	@Override
	public void setArgs(String args) {
		this.args=args;
	}
	
}
