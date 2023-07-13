package com.base.core.head.valid;

import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.json.JsonArray;
import com.gitee.magic.core.valid.ValidCheckCustom;

/**
 * @author start 
 */
@DocDescription("Json数组对象")
public class JsonArrayValid implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		try {
			new JsonArray(String.valueOf(value));
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
}
