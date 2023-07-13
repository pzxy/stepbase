package com.base.core.context.netty.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * @author start
 *
 */
public class StringDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
			throws Exception {
		byte[] b = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(b);
		list.add(new String(b));
	}

}
