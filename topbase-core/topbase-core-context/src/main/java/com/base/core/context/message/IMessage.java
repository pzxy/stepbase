package com.base.core.context.message;

/**
 * @author start 
 */
public interface IMessage<T> {
	
	/**
	 * 是否支持该类消息发送
	 * @param clazz
	 * @return
	 */
	boolean supports(Class<?> clazz);

	/**
	 * 发送
	 * @param data
	 */
	void send(T data);
	
}
