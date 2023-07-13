package com.base.core.context.netty;

import java.net.InetSocketAddress;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * @author mac
 *
 */
public class NettyContext {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void server(int port, List<ChannelHandler> handlers) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(2);

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.option(ChannelOption.SO_REUSEADDR, true);
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ChannelPipeline pipeline = ch.pipeline();
				for (ChannelHandler lh : handlers) {
					pipeline.addLast(lh);
				}
			}
		});
		try {
			ChannelFuture future = bootstrap.bind(port).sync();
			if (future.isSuccess()) {
				logger.info("Server is start success port:{}", port);
			} else {
				logger.info("Server start fail");
			}
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public void client(String ip, int port, List<ChannelHandler> handlers) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) {
				ChannelPipeline pipeline = ch.pipeline();
				for (ChannelHandler lh : handlers) {
					pipeline.addLast(lh);
				}
			}
		});

		try {
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
			if (future.isSuccess()) {
				logger.info("Client is start success port:{}", port);
			} else {
				logger.info("Client is start fail");
			}
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	public void send(byte[] bytes) {
		for (String key : NettyChannelContext.getChannels().keySet()) {
			ChannelHandlerContext ctx = NettyChannelContext.getChannel(key);
			if (ctx == null) {
				continue;
			}
			if (ctx.channel().isActive()) {
				ByteBuf reqStrByteBuf = ctx.alloc().buffer(bytes.length);
				reqStrByteBuf.writeBytes(bytes);
				ctx.writeAndFlush(reqStrByteBuf);
			} else {
				NettyChannelContext.removeChannel(key);
			}
		}
	}

}
