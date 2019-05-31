package com.bowlong.third.netty4.future;

import java.io.File;
import java.io.RandomAccessFile;

import io.netty.channel.Channel;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.util.ReferenceCountUtil;

/** 文件下载流处理  */
public class FilePrgListener implements ChannelProgressiveFutureListener {
	File file = null;
	RandomAccessFile raf = null;
	boolean isDelFile = false;

	public FilePrgListener(File file, RandomAccessFile raf, boolean isDelFile) {
		super();
		this.file = file;
		this.raf = raf;
		this.isDelFile = isDelFile;
	}

	@Override
	public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
		// System.err.println(String.format("progress / total = [%s/%s]", progress, total));
	}

	@Override
	public void operationComplete(ChannelProgressiveFuture future) throws Exception {
		raf.close();
		if (isDelFile) {
			try {
				file.delete();
			} catch (Exception e) {
			}
		}
		Channel chn = future.channel();
		ReferenceCountUtil.release(chn);
		chn.disconnect();
		chn.close();
	}
}
