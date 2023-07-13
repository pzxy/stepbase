package com.base.core.context.message;

import java.util.Arrays;
import java.util.List;

import com.base.core.context.utils.SpringUtils;
import com.gitee.magic.framework.base.spring.SpringContext;

/**
 * @author start 
 */
public class MessageProxy {

	public static <T>void send(T message) {
		send(Arrays.asList(message));
	}
	
	@SuppressWarnings("unchecked")
	public static <T>void send(List<T> messages) {
		String[] beans = SpringContext.getAppContext().getBeanNamesForType(IMessage.class);
		for (String bean : beans) {
			IMessage<T> iMessage = (IMessage<T>) SpringUtils.getBean(bean);
			for(T data:messages) {
				if(iMessage.supports(data.getClass())) {
					iMessage.send(data);
				}
			}
		}
	}
	
}
