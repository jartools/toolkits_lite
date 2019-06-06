package com.bowlong.third.netty4.socket;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class N4BootstrapSocketServer extends N4Socket {
	static Log log = LogFactory.getLog(N4BootstrapSocketServer.class);

	/** 异步的netty */
	static public Map start(String host, int port, boolean nodelay, boolean alive, ChannelInitializer<SocketChannel> childHandler) {
		return begin(host, port, nodelay, alive, childHandler, false);
	}

	/** 同步的netty */
	static public Map startSync(String host, int port, boolean nodelay, boolean alive, ChannelInitializer<SocketChannel> childHandler) {
		return begin(host, port, nodelay, alive, childHandler, true);
	}

	static private Map begin(String host, int port, boolean nodelay, boolean alive, ChannelInitializer<SocketChannel> childHandler, boolean isSync) {

		Map map = new HashMap();
		int sizeParent = Runtime.getRuntime().availableProcessors() * 2;
		int sizeChild = 4;
		EventLoopGroup parentGroup = new NioEventLoopGroup(sizeParent);// 用于接收发来的连接请求
		EventLoopGroup childGroup = new NioEventLoopGroup(sizeChild); // 用于处理parentGroup接收并注册给child的连接中的信息

		ServerBootstrap bstrap = initServer(parentGroup, childGroup, childHandler, nodelay, alive);
		ChannelFuture chnFu = null;
		try {
			chnFu = serverBind(host, port, bstrap);
			// 同步
			// chnFu.channel().closeFuture().sync();
			if (isSync) {
				ChannelFuture f = chnFu.sync();
				f.channel().closeFuture().sync();
			}
		} catch (Exception e) {
			log.error(e2s(e));
		} finally {
			if (isSync) {
				parentGroup.shutdownGracefully();
				childGroup.shutdownGracefully();
			} else {
				map.put("parentGroup", parentGroup);
				map.put("childGroup", childGroup);
			}
		}

		if (chnFu != null) {
			map.put("chnFuture", chnFu);
		}
		return map;
	}

	private static ServerBootstrap initServer(EventLoopGroup parentGroup, EventLoopGroup childGroup, ChannelInitializer<SocketChannel> childHandler, boolean nodelay, boolean alive) {
		ServerBootstrap bstrap = new ServerBootstrap();
		bstrap.group(parentGroup, childGroup);
		bstrap.channel(NioServerSocketChannel.class);
		bstrap.childHandler(childHandler);
		// bstrap.option(ChannelOption.SO_BACKLOG, 128);
		bstrap.childOption(ChannelOption.SO_KEEPALIVE, alive);
		bstrap.childOption(ChannelOption.TCP_NODELAY, nodelay);
		return bstrap;
	}
}