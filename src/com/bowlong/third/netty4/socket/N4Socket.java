package com.bowlong.third.netty4.socket;

import java.net.InetSocketAddress;
import java.util.Map;

import com.bowlong.basic.ExToolkit;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;

public class N4Socket extends ExToolkit {
	static final public ChannelFuture serverBind(String host, int port, ServerBootstrap bstrap) throws Exception {
		InetSocketAddress address = getAddress(host, port);
		ChannelFuture chn = bstrap.bind(address);
		// 同步
		// chn.sync();
		return chn;
	}

	static final public ChannelFuture clientConnect(String host, int port, Bootstrap bstrap) throws Exception {
		InetSocketAddress address = getAddress(host, port);
		ChannelFuture chn = bstrap.connect(address);
		return chn;
	}

	static final public InetSocketAddress getAddress(final String host, final int port) {
		InetSocketAddress address = null;
		if (isEmpty(host)) {
			address = new InetSocketAddress(port);
		} else {
			address = new InetSocketAddress(host, port);
		}
		return address;
	}

	@SuppressWarnings("rawtypes")
	static final public void shutDown(Map map) {
		if (map == null) {
			return;
		}
		ChannelFuture chnFu = (ChannelFuture) map.get("chnFuture");
		if (chnFu != null) {
			chnFu.channel().close();
			chnFu.cancel(true);
		}
		EventLoopGroup group = null;
		if (map.containsKey("parentGroup")) {
			group = (EventLoopGroup) map.get("parentGroup");
			group.shutdownGracefully();
			EventLoopGroup childGroup = (EventLoopGroup) map.get("childGroup");
			childGroup.shutdownGracefully();
		} else if (map.containsKey("group")) {
			group = (EventLoopGroup) map.get("group");
			group.shutdownGracefully();
		}
		map.clear();
	}
}