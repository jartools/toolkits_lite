package com.bowlong.third.netty4.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.Toolkit;
import com.bowlong.lang.StrEx;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class N4BootstrapSocketServer {
	static Log log = LogFactory.getLog(N4BootstrapSocketServer.class);

	/** 异步的netty */
	static public Map start(String host, int port, boolean nodelay,
			boolean alive, ChannelInitializer<SocketChannel> childHandler) {
		return begin(host, port, nodelay, alive, childHandler, false);
	}

	/** 同步的netty */
	static public Map startSync(String host, int port, boolean nodelay,
			boolean alive, ChannelInitializer<SocketChannel> childHandler) {
		return begin(host, port, nodelay, alive, childHandler, true);
	}

	static private Map begin(String host, int port, boolean nodelay,
			boolean alive, ChannelInitializer<SocketChannel> childHandler,
			boolean isSync) {

		Map map = new HashMap();
		int sizeParent = Runtime.getRuntime().availableProcessors() * 2;
		int sizeChild = 4;
		EventLoopGroup parentGroup = new NioEventLoopGroup(sizeParent);// 用于接收发来的连接请求
		EventLoopGroup childGroup = new NioEventLoopGroup(sizeChild); // 用于处理parentGroup接收并注册给child的连接中的信息

		ServerBootstrap bstrap = initServer(parentGroup, childGroup,
				childHandler, nodelay, alive);
		ChannelFuture chnFu = null;
		try {
			chnFu = bindServerSocketAddress(host, port, bstrap);
			// 同步
			// chnFu.channel().closeFuture().sync();
			if (isSync) {
				chnFu.sync();
			}
		} catch (Exception e) {
			log.error(Toolkit.e2s(e));
		} finally {
			// parentGroup.shutdownGracefully();
			// childGroup.shutdownGracefully();
			map.put("parentGroup", parentGroup);
			map.put("childGroup", childGroup);
		}
		
		if (chnFu != null) {
			map.put("chnFuture", chnFu);
		}
		return map;
	}

	private static ServerBootstrap initServer(EventLoopGroup parentGroup,
			EventLoopGroup childGroup,
			ChannelInitializer<SocketChannel> childHandler, boolean nodelay,
			boolean alive) {
		ServerBootstrap bstrap = new ServerBootstrap();
		bstrap.group(parentGroup, childGroup);
		bstrap.channel(NioServerSocketChannel.class);
		bstrap.childHandler(childHandler);
		// bstrap.option(ChannelOption.SO_BACKLOG, 128);
		bstrap.childOption(ChannelOption.SO_KEEPALIVE, alive);
		bstrap.childOption(ChannelOption.TCP_NODELAY, nodelay);
		return bstrap;
	}

	private static ChannelFuture bindServerSocketAddress(final String host,
			final int port, ServerBootstrap bstrap) throws Exception {
		InetSocketAddress address = getAddress(host, port);
		ChannelFuture chn = bstrap.bind(address);
		// 同步
		// chn.sync();
		return chn;
	}
	
	static public InetSocketAddress getAddress(final String host, final int port) {
		InetSocketAddress address = null;
		if (StrEx.isEmpty(host)) {
			address = new InetSocketAddress(port);
		} else {
			address = new InetSocketAddress(host, port);
		}
		return address;
	}
}