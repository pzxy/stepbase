package com.base.component.upload.valid;

import com.base.component.upload.service.BaseUploadService;
import com.base.core.context.utils.SpringUtils;
import com.gitee.magic.core.valid.ValidCheckCustom;
import com.gitee.magic.framework.head.constants.BaseCode;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
public class UploadCustomCheck implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		try {
			BaseUploadService service=SpringUtils.getBean(BaseUploadService.class);
			if(!service.doesObjectExist(String.valueOf(value))) {
				throw new BusinessException(BaseCode.CODE_1020);
			}
			return true;
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			return false;
		}
	}
	
}
