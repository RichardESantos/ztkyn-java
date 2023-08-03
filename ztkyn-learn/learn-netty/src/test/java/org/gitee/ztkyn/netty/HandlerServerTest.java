package org.gitee.ztkyn.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-08-02 15:05
 * @description HandlerTest
 * @version 1.0.0
 */
public class HandlerServerTest {

	private static final Logger logger = LoggerFactory.getLogger(HandlerServerTest.class);

	public static void main(String[] args) {
		new ServerBootstrap().group(new NioEventLoopGroup())
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<NioSocketChannel>() {
				protected void initChannel(NioSocketChannel ch) {
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) {
							System.out.println(1);
							ctx.fireChannelRead(msg); // 1
						}
					});
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) {
							System.out.println(2);
							ctx.fireChannelRead(msg); // 2
						}
					});
					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) {
							System.out.println(3);
							ctx.channel().write(msg); // 3
						}
					});
					ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
						@Override
						public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
							System.out.println(4);
							ctx.write(msg, promise); // 4
						}
					});
					ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
						@Override
						public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
							System.out.println(5);
							ctx.write(msg, promise); // 5
						}
					});
					ch.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
						@Override
						public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
							System.out.println(6);
							ctx.write(msg, promise); // 6
						}
					});
				}
			})
			.bind(8080);
	}

}
