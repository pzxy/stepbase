package com.base.core.head.valid;

import java.util.Date;

import com.gitee.magic.core.valid.ValidCheckCustom;
import com.gitee.magic.framework.head.exception.BusinessException;
import com.gitee.magic.framework.head.utils.TimeUtils;

/**
 * @author start 
 */
public class GeCurrentDateTimeValid implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		try {
			Date validDate=TimeUtils.format(String.valueOf(value), TimeUtils.YYYYMMDDHHMMSS);
			return validDate.compareTo(new Date())>0;
		}catch(BusinessException e) {
			throw e;
		}catch(Exception e) {
			return false;
		}
	}
	
}
