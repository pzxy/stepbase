package com.base.core.head.valid;

import com.gitee.magic.core.utils.ReflectUtils;
import com.gitee.magic.core.valid.ValidCheckCustom;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
public class UploadKeyValid implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		try {
			Class<?> prototype=Class.forName("com.base.component.upload.valid.UploadCustomCheck");
			ValidCheckCustom valid=(ValidCheckCustom)ReflectUtils.newInstance(prototype);
			return valid.check(value);
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			return false;
		}
	}
	
}
