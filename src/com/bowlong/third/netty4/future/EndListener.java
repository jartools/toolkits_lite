package com.bowlong.third.netty4.future;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.ReferenceCountUtil;

/** 结束处理 */
public class EndListener implements ChannelFutureListener {
	boolean isDoClose = true;

	public EndListener() {
		super();
	}

	public EndListener(boolean isDoClose) {
		super();
		this.isDoClose = isDoClose;
	}

	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (!isDoClose)
			return;
		Channel chn = future.channel();
		ReferenceCountUtil.release(chn);
		chn.disconnect();
		chn.close();
	}

}
