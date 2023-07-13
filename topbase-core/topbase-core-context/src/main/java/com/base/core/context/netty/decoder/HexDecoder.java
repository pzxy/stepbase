package com.base.core.context.netty.decoder;

import java.util.List;

import com.base.core.context.utils.HexUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * @author start
 *
 */
public class HexDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
			throws Exception {
		byte[] b = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(b);
		list.add(HexUtils.bytesToHex(b));
	}

}
