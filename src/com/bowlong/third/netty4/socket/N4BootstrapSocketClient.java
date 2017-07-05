package com.bowlong.third.netty4.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.Toolkit;
import com.bowlong.lang.StrEx;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class N4BootstrapSocketClient {
	static Log log = LogFactory.getLog(N4BootstrapSocketClient.class);

	/** 异步的netty */
	static public Map start(String host, int port, boolean nodelay,
			boolean alive, ChannelInitializer<SocketChannel> handler) {
		return begin(host, port, nodelay, alive, handler, false);
	}

	/** 同步的netty */
	static public Map startSync(String host, int port, boolean nodelay,
			boolean alive, ChannelInitializer<SocketChannel> handler) {
		return begin(host, port, nodelay, alive, handler, true);
	}

	static private Map begin(String host, int port, boolean nodelay,
			boolean alive, ChannelInitializer<SocketChannel> handler,
			boolean isSync) {
		Map map = new HashMap();
		EventLoopGroup group = new NioEventLoopGroup();// 用于接收发来的连接请求
		Bootstrap bstrap = initServer(group, handler, nodelay, alive);
		ChannelFuture chnFu = null;
		try {
			chnFu = connectServerSocketAddress(host, port, bstrap);
			// 同步
			// chnFu.channel().closeFuture().sync();
			if (isSync) {
				chnFu.sync();
			}
		} catch (Exception e) {
			log.error(Toolkit.e2s(e));
		} finally {
			// group.shutdownGracefully();
			map.put("group", group);
		}
		if (chnFu != null) {
			map.put("chnFuture", chnFu);
		}
		return map;
	}

	private static Bootstrap initServer(EventLoopGroup group,
			ChannelInitializer<SocketChannel> handler, boolean nodelay,
			boolean alive) {
		Bootstrap bstrap = new Bootstrap();

		bstrap.group(group);
		bstrap.channel(NioSocketChannel.class);
		bstrap.handler(handler);
		// bstrap.option(ChannelOption.SO_BACKLOG, 128);
		bstrap.option(ChannelOption.SO_KEEPALIVE, alive);
		bstrap.option(ChannelOption.TCP_NODELAY, nodelay);
		return bstrap;
	}

	private static ChannelFuture connectServerSocketAddress(final String host,
			final int port, Bootstrap bstrap) throws Exception {
		InetSocketAddress address = getAddress(host, port);
		ChannelFuture chn = bstrap.connect(address);
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