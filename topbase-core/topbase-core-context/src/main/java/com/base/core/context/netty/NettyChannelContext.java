package com.base.core.context.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author start
 *
 */
public class NettyChannelContext {
	
	private static ConcurrentHashMap<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();

	public static Map<String, ChannelHandlerContext> getChannels() {
		return map;
	}

	public static void saveChannel(String key, ChannelHandlerContext ctx) {
		map.put(key, ctx);
	}

	public static ChannelHandlerContext getChannel(String key) {
		return map.get(key);
	}

	public static void removeChannel(String key) {
		map.remove(key);
	}
	
}