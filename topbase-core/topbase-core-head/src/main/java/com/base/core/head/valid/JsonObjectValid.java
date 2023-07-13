package com.base.core.head.valid;

import com.gitee.magic.core.converter.DocDescription;
import com.gitee.magic.core.json.JsonObject;
import com.gitee.magic.core.valid.ValidCheckCustom;

/**
 * @author start 
 */
@DocDescription("Json对象")
public class JsonObjectValid implements ValidCheckCustom  {

	@Override
	public boolean check(Object value) {
		try {
			new JsonObject(String.valueOf(value));
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
}
