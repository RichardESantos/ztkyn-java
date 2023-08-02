package org.gitee.ztkyn.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author richard
 * @date 2023-08-02 15:26
 * @description ClientTest
 * @version 1.0.0
 */
public class ClientTest {

	private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);

	public static void main(String[] args) {
		new Bootstrap().group(new NioEventLoopGroup())
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) {
					ch.pipeline().addLast(new StringEncoder());
				}
			})
			.connect("127.0.0.1", 8080)
			.addListener((ChannelFutureListener) future -> {
				future.channel().writeAndFlush("hello,world");
			});
	}

}
