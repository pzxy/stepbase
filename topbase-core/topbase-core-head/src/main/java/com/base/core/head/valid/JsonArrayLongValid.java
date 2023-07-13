package com.base.core.head.valid;

import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.utils.CheckUtils;
import com.gitee.magic.core.valid.ValidCheckCustom;

/**
 * @author start 
 */
@DocDescription("Long,Json数组对象")
public class JsonArrayLongValid implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		try {
			JsonArray array=new JsonArray(String.valueOf(value));
			return CheckUtils.isJsonArrayLong(array);
		}catch(Exception e) {
			return false;
		}
	}
	
}
