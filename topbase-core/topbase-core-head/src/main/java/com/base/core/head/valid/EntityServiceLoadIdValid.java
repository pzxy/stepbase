package com.base.core.head.valid;

import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.utils.ReflectUtils;
import com.gitee.magic.core.valid.ValidCheckCustom;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
@DocDescription("load方法验证Id")
public class EntityServiceLoadIdValid implements ValidCheckCustom  {

	private String paramName;
	
	@Override
	public void setParamName(String paramName) {
		this.paramName=paramName;
	}
	
	@Override
	public boolean check(Object value) {
		try {
			Class<?> prototype=Class.forName("com.base.common.valid.EntityServiceLoadIdCheck");
			ValidCheckCustom valid=(ValidCheckCustom)ReflectUtils.newInstance(prototype);
			valid.setParamName(paramName.substring(0,paramName.length()-2));
			return valid.check(value);
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			return false;
		}
	}
	
}
