package com.bowlong.third.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.net.TcpChannel;

public class TcpInboundHandler extends ChannelInboundHandlerAdapter {
	static Log log = LogFactory.getLog(TcpInboundHandler.class);
	static final ExecutorService pool = Executors.newCachedThreadPool();
	static final AttributeKey<TcpChannel> STATE = AttributeKey.valueOf("attr");

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("ctx:" + ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("ctx:" + ctx);
		close(ctx);
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg)
			throws Exception {
		log.info("ctx:" + ctx + " msg:" + msg);
		try {
			// final byte[] content = (byte[]) msg;
			// dologic content
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		log.info("ctx:" + ctx, cause);
		close(ctx);
	}

	public void close(ChannelHandlerContext ctx) {
		TcpChannel chn = ctx.attr(STATE).get();
		if (chn != null) {
			chn.close();
		}
		ctx.close();
	}
}