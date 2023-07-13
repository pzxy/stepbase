package com.base.common.valid;

import com.base.core.context.utils.SpringUtils;
import com.base.core.framework.sql.service.BaseService;
import com.gitee.magic.core.valid.ValidCheckCustom;
import com.gitee.magic.framework.head.exception.BusinessException;

/**
 * @author start 
 */
public class EntityServiceLoadIdCheck implements ValidCheckCustom  {

	private String paramName;
	
	@Override
	public void setParamName(String paramName) {
		this.paramName=paramName;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean check(Object value) {
		try {
			BaseService<?, Long> service=(BaseService<?,Long>)SpringUtils.getBean(paramName+"Service");
			service.load(Long.parseLong(String.valueOf(value)));
			return true;
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			return false;
		}
	}
	
}
