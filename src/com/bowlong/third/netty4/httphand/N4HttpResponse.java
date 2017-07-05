package com.bowlong.third.netty4.httphand;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.bio2.B2Helper;
import com.bowlong.lang.StrEx;

/***
 * N4(netty 4)请求相应类 其父N4HttpResp类处理其他过来的请求， 该类进行响应(返回对应的数据)
 ****/
@SuppressWarnings({ "rawtypes" })
public class N4HttpResponse extends N4HttpResp {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(N4HttpResponse.class);

	/** 需要关闭的地方才关闭 */
	static public void closeChn(Channel chn) {
		closeChn(chn, true);
	}

	/** 需要关闭的地方才关闭 */
	static public void closeChn(Channel chn, boolean isCan) {
		if (!isCan)
			return;
		try {
			chn.disconnect();
			chn.close();
		} catch (Exception e) {
		}
	}

	public static void send(Channel chn, Map params) throws Exception {
		byte[] buff = B2Helper.toBytes(params);
		send(chn, buff);
	}

	public static void send(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}

		byte[] buff = content.getBytes("UTF-8");
		send(chn, buff);
	}
	
	protected static void sendAll(Channel chn, String content, String ContentType) throws Exception {
		if (content == null) {
			content = "";
		}

		byte[] buff = content.getBytes("UTF-8");
		sendAll(chn, buff, ContentType);
	}

	protected static void sendAll(Channel chn, byte[] buff, String ContentType) {
		ByteBuf buf = N4B2ByteBuf.buffer();
		buf.writeBytes(buff);
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

		if (StrEx.isEmptyTrim(ContentType))
			ContentType = "text/html; charset=UTF-8";

		response.headers().set(HttpHeaders.Names.CONTENT_TYPE, ContentType);
		int size = response.content().writableBytes();
		int len = buff.length;
		int realSize = Math.min(len, size);
		System.out.println("size : " + size + ",len : " + len+",realSize : "+realSize);
		// response.headers().set("Content-Length", size);
		response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);  
		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, realSize);  
		ChannelFuture f = chn.writeAndFlush(response);
		f = chn.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		f.addListener(ChannelFutureListener.CLOSE);
	}

	// 直接写内容
	public static void send(Channel chn, byte[] buff) throws Exception {
		String ContentType = "text/html; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}
	
	public static void sendTxt(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}
		byte[] buff = content.getBytes("UTF-8");
		sendTxt(chn, buff);
	}

	public static void sendTxt(Channel chn, byte[] buff) throws Exception {
		String ContentType = "text/plain; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}

	public static void sendJson(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}
		byte[] buff = content.getBytes("UTF-8");
		sendJson(chn, buff);
	}

	// 直接写内容JSON
	public static void sendJson(Channel chn, byte[] buff) throws Exception {
		String ContentType = "application/Json; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}
	
	public static void sendCss(Channel chn, String content) throws Exception {
		if (content == null) {
			content = "";
		}
		byte[] buff = content.getBytes("UTF-8");
		sendCss(chn, buff);
	}

	public static void sendCss(Channel chn, byte[] buff) throws Exception {
		String ContentType = "text/css; charset=UTF-8";
		sendAll(chn, buff, ContentType);
	}

	public static void sendByChunked(Channel chn, String content)
			throws Exception {
		if (content == null) {
			content = "";
		}

		byte[] buff = content.getBytes("UTF-8");
		sendByChunked(chn, buff);
	}

	public static void sendByChunked(Channel chn, Map map) throws Exception {
		byte[] buff = B2Helper.toBytes(map);
		sendByChunked(chn, buff);
	}

	// 直接写内容
	public static void sendByChunked(Channel chn, byte[] buff) throws Exception {
		ByteBuf buf = N4B2ByteBuf.buffer();
		buf.writeBytes(buff);
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

		response.headers().set(HttpHeaders.Names.TRANSFER_ENCODING,
				HttpHeaders.Values.CHUNKED);
		int size = response.content().writableBytes();
		int len = buff.length;
		int realSize = Math.min(len, size);
		System.out.println("size : " + size + ",len : " + len+",realSize : "+realSize);
		// response.headers().set("Content-Length", size);
		response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);  
		response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, realSize);
		ChannelFuture f = chn.writeAndFlush(response);
		f = chn.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		f.addListener(ChannelFutureListener.CLOSE);
	}
}
