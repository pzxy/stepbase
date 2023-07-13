package com.base.core.context.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.core.context.netty.NettyChannelContext;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * @author start
 *
 */
public class BaseInboundHandler extends ChannelInboundHandlerAdapter {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		String uuid = ctx.channel().id().asLongText();
		NettyChannelContext.saveChannel(uuid, ctx);
		if(logger.isInfoEnabled()) {
			logger.info("建立连接: {} 地址: {}" ,uuid, ctx.channel().remoteAddress());
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		String uuid = ctx.channel().id().asLongText();
		NettyChannelContext.removeChannel(uuid);
		if(logger.isInfoEnabled()) {
			logger.info("断开连接: {} 地址: {}" ,uuid, ctx.channel().remoteAddress());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}

}
