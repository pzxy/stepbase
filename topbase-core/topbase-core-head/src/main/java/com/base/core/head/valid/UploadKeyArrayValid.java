package com.base.core.head.valid;

import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.utils.ReflectUtils;
import com.gitee.magic.core.valid.ValidCheckCustom;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
public class UploadKeyArrayValid implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		JsonArray array=new JsonArray(String.valueOf(value));
		try {
			Class<?> prototype=Class.forName("com.base.component.upload.valid.UploadCustomCheck");
			ValidCheckCustom valid=(ValidCheckCustom)ReflectUtils.newInstance(prototype);
			for(int i=0;i<array.length();i++) {
				if(!valid.check(array.getString(i))) {
					return false;
				}
			}
			return true;
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			return false;
		}
	}
	
}
