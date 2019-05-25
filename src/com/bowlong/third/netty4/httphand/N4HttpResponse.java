package com.bowlong.third.netty4.httphand;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;

import com.bowlong.bio2.B2Helper;
import com.bowlong.io.FileRw;
import com.bowlong.lang.StrEx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

/***
 * N4(netty 4)请求相应类 其父N4HttpResp类处理其他过来的请求， 该类进行响应(返回对应的数据)
 ****/
@SuppressWarnings({ "rawtypes" })
public class N4HttpResponse extends N4HttpOrg {

	private static final long serialVersionUID = 1L;

	/** 需要关闭的地方才关闭 */
	static final public void closeChn(Channel chn) {
		closeChn(chn, true);
	}

	/** 需要关闭的地方才关闭 */
	static final public void closeChn(Channel chn, boolean isCan) {
		if (!isCan)
			return;
		try {
			chn.disconnect();
			chn.close();
		} catch (Exception e) {
		}
	}

	static final private int _send(Channel chn, ByteBuf buf, FullHttpResponse response) {
		int size = response.content().readableBytes();
		response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, size);
		ChannelFuture f = chn.writeAndFlush(response);
		f = chn.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		f.addListener(ChannelFutureListener.CLOSE);
		// buf.release(); // 添加buff释放？ 测试了会报错
		// io.netty.util.IllegalReferenceCountException: refCnt: 0, decrement: 1
		return size;
	}

	static final public void send(Channel chn, Map params) throws Exception {
		byte[] buff = B2Helper.toBytes(params);
		send(chn, buff);
	}

	static final public void send(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}

		byte[] buff = content.getBytes("UTF-8");
		send(chn, buff);
	}

	static final protected void sendAll(Channel chn, String content, String ContentType) throws Exception {
		if (content == null) {
			content = "";
		}

		byte[] buff = content.getBytes("UTF-8");
		sendAll(chn, buff, ContentType);
	}

	static final protected void sendAll(Channel chn, byte[] buff, String ContentType) {
		ByteBuf buf = N4B2ByteBuf.buffer(buff);
		sendAll(chn, buf, ContentType);
	}

	static final protected int sendAll(Channel chn, ByteBuf buf, String ContentType) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		if (StrEx.isEmptyTrim(ContentType))
			ContentType = "text/html; charset=UTF-8";
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, ContentType);
		return _send(chn, buf, response);
	}

	// 直接写内容
	static final public void send(Channel chn, byte[] buff) throws Exception {
		String ContentType = "text/html; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}

	static final public void sendTxt(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}
		byte[] buff = content.getBytes("UTF-8");
		sendTxt(chn, buff);
	}

	static final public void sendTxt(Channel chn, byte[] buff) throws Exception {
		String ContentType = "text/plain; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}

	static final public void sendJson(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}
		byte[] buff = content.getBytes("UTF-8");
		sendJson(chn, buff);
	}

	// 直接写内容JSON
	static final public void sendJson(Channel chn, byte[] buff) throws Exception {
		String ContentType = "application/Json; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}

	static final public void sendCss(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}
		byte[] buff = content.getBytes("UTF-8");
		sendCss(chn, buff);
	}

	static final public void sendCss(Channel chn, byte[] buff) throws Exception {
		String ContentType = "text/css; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}

	static final public void sendByChunked(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}

		byte[] buff = content.getBytes("UTF-8");
		sendByChunked(chn, buff);
	}

	static final public void sendByChunked(Channel chn, Map map) throws Exception {
		byte[] buff = B2Helper.toBytes(map);
		sendByChunked(chn, buff);
	}

	static final public int sendByChunked(Channel chn, ByteBuf buf) throws Exception {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
		return _send(chn, buf, response);
	}

	// 直接写内容
	static final public void sendByChunked(Channel chn, byte[] buff) throws Exception {
		// ByteBuf buf = N4B2ByteBuf.buffer();
		// buf.writeBytes(buff);
		ByteBuf buf = N4B2ByteBuf.buffer(buff);
		sendByChunked(chn, buf);
	}

	static final public void sendFile(Channel chn, File file) throws Exception {
		final String fname = file.getName();
		final RandomAccessFile raf = FileRw.openRAFile(file, "r");
		byte[] buff = FileRw.readFully(raf);
		long fileLength = raf.length();
		ByteBuf buf = N4B2ByteBuf.buffer(buff);
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
		response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
		response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, fileLength);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
		response.headers().add(HttpHeaderNames.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fname));
		chn.write(response);

		ChannelFuture f = chn.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), chn.newProgressivePromise());
		f.addListener(new ChannelProgressiveFutureListener() {
			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				raf.close();
			}

			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
			}
		});
		f = chn.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
	}
}
