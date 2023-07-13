package com.base.core.context.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.gitee.magic.framework.base.constant.Message;

/**
 * @author start 
 */
public class LocalizationMessage {
	
	public final static Map<String,ResourceBundle> BUNDLEMAP=new HashMap<>();

	public static void setLanaguageBundle(String language,ResourceBundle bundle) {
		BUNDLEMAP.put(language, bundle);
	}
	
	public static Message getMessage(Message message,String language) {
		ResourceBundle bundle=BUNDLEMAP.get(language);
		if(bundle!=null) {
			String key=String.valueOf(message.getCode());
			if(bundle.containsKey(key)) {
				String value=bundle.getString(key);
				return new Message(message.getCode(),value);
			}
		}
		return message;
	}
	
	public static void main(String[] args) throws Exception {
		Map<Integer, String> content = new TreeMap<Integer, String>();
		Class<?> prototype=Class.forName("com.topnetwork.common.CodeResVal");
		Field[] fields = prototype.getFields();
        for (Field field : fields) {
            String value = String.valueOf(field.get(null));
            Message message = Message.parse(value);
            content.put(message.getCode(), message.getMessage());
        }
        for(Integer key:content.keySet()) {
            System.out.println(key+"="+content.get(key));
        }
	}
	
}
