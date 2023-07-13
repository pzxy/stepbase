package com.base.core.head.valid;

import com.gitee.magic.core.valid.ValidCheckCustom;

/**
 * 只支持时间戳
 * @author start
 *
 */
public class ExpireTimeValid implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		try {
			Long expireTime=Long.parseLong(String.valueOf(value));
			return System.currentTimeMillis()>expireTime;
		}catch(Exception e) {
			return false;
		}
	}
	
}
