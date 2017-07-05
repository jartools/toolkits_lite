package com.bowlong.third.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

public class IdleHandler extends ReadTimeoutHandler {
	public IdleHandler(Timer timer, int timeoutSeconds) {
		super(timeoutSeconds, TimeUnit.SECONDS);
	}

	@Override
	protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
	}

}
