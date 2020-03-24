package com.bowlong.third.netty4.socket;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class N4BootstrapSocketClient extends N4Socket {
	static Log log = LogFactory.getLog(N4BootstrapSocketClient.class);

	/** 异步的netty */
	static public Map start(String host, int port, boolean nodelay, boolean alive, ChannelInitializer<SocketChannel> handler) {
		return begin(host, port, nodelay, alive, handler, false);
	}

	/** 同步的netty */
	static public Map startSync(String host, int port, boolean nodelay, boolean alive, ChannelInitializer<SocketChannel> handler) {
		return begin(host, port, nodelay, alive, handler, true);
	}

	static private Map begin(String host, int port, boolean nodelay, boolean alive, ChannelInitializer<SocketChannel> handler, boolean isSync) {
		EventLoopGroup group = new NioEventLoopGroup();// 用于接收发来的连接请求
		Bootstrap bstrap = initServer(group, handler, nodelay, alive);
		Map map = new HashMap();
		map.put("host", host);
		map.put("port", port);
		map.put("isSync", isSync);
		map.put("bstrap", bstrap);
		map.put("group", group);
		map = reconnect(map);
		return map;
	}

	private static Bootstrap initServer(EventLoopGroup group, ChannelInitializer<SocketChannel> handler, boolean nodelay, boolean alive) {
		Bootstrap bstrap = new Bootstrap();

		bstrap.group(group);
		bstrap.channel(NioSocketChannel.class);
		bstrap.handler(handler);
		// bstrap.option(ChannelOption.SO_BACKLOG, 128);
		bstrap.option(ChannelOption.SO_KEEPALIVE, alive);
		bstrap.option(ChannelOption.TCP_NODELAY, nodelay);
		return bstrap;
	}

	static public Map reconnect(Map map) {
		ChannelFuture chnFu = (ChannelFuture) map.get("chnFuture");
		boolean isCanCon = chnFu == null;
		if (!isCanCon) {
			Channel chn = chnFu.channel();
			isCanCon = (chn == null) || !chn.isOpen();
		}
		if (isCanCon) {
			String host = getString(map, "host");
			int port = getInt(map, "port");
			boolean isSync = getBool(map, "isSync");
			Bootstrap bstrap = (Bootstrap) map.get("bstrap");
			try {
				chnFu = clientConnect(host, port, bstrap);
				if (isSync) {
					ChannelFuture f = chnFu.sync();
					f.channel().closeFuture().sync();
				} else {
					map.put("chnFuture", chnFu);
				}
			} catch (Exception e) {
				log.error(e2s(e));
			} finally {
				if (isSync) {
					EventLoopGroup group = (EventLoopGroup) map.remove("group");
					group.shutdownGracefully();
				}
			}
		}
		return map;
	}
}