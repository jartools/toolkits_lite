package com.bowlong.third.netty4.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.Charset;

import com.bowlong.text.Encoding;
import com.bowlong.third.netty4.codec.LengthByteArrayDecoder;
import com.bowlong.third.netty4.codec.LengthByteArrayEncoder;

/** 数据处理 */
public class N4ChnInitializer extends ChannelInitializer<SocketChannel> {
	public int ntype = 0;
	public boolean isFramer = false;
	public ChannelInboundHandlerAdapter hander;
	private Charset utf8 = Encoding.UTF8;

	// 为了解决TCP粘包／拆包导致的半包读写问题
	private int size = 1024;
	// 以回车换行符("\n")作为消息结束符
	// private ByteBuf delimiter = Unpooled.copiedBuffer("\n".getBytes());
	private ByteBuf[] delimiter = Delimiters.lineDelimiter();
	// 将特殊的分隔符作为消息分隔符，回车换行符是他的一种
	private DelimiterBasedFrameDecoder framer = new DelimiterBasedFrameDecoder(size, delimiter);

	// 将回车换行符作为消息结束符
	// private LineBasedFrameDecoder framer = new LineBasedFrameDecoder(size);

	public N4ChnInitializer(int ntype, boolean isFramer, ChannelInboundHandlerAdapter hander) {
		super();
		this.ntype = ntype;
		this.isFramer = isFramer;
		this.hander = hander;
	}

	public N4ChnInitializer(int ntype, ChannelInboundHandlerAdapter hander) {
		this(ntype, false, hander);
	}

	public N4ChnInitializer(ChannelInboundHandlerAdapter hander) {
		this(0, hander);
	}

	@Override
	protected void initChannel(SocketChannel chn) throws Exception {
		switch (ntype) {
		case 2:
			initType_2(chn);
			break;
		case 3:
			initType_3(chn);
			break;
		case 4:
			initType_4http(chn);
			break;
		default:
			initType_1(chn);
			break;
		}
	}

	void initType_2(SocketChannel chn) throws Exception {
		ChannelPipeline pipeline = chn.pipeline();
		// 解码 和 编码
		pipeline.addLast("decoder", new LengthByteArrayDecoder(40 * 1024, 0, 4, 0, 4));
		pipeline.addLast("encoder", new LengthByteArrayEncoder());

		// hander接受到的是:byte[]
		pipeline.addLast("handler", this.hander);
	}

	void initType_1(SocketChannel chn) throws Exception {
		ChannelPipeline p = chn.pipeline();
		// 以("\n")为结尾分割的 解码器
		if (isFramer)
			p.addLast("framer", framer);
		// 解码 和 编码
		p.addLast("decoder", new StringDecoder(utf8));
		p.addLast("encoder", new StringEncoder(utf8));

		// hander接受到的是:String
		p.addLast("handler", this.hander);
	}

	void initType_3(SocketChannel chn) throws Exception {
		ChannelPipeline p = chn.pipeline();
		if (isFramer)
			p.addLast("framer", framer);
		// 解码 和 编码
		p.addLast("decoder", new Base64Decoder());
		p.addLast("encoder", new Base64Encoder());

		// hander接受到的是:String
		p.addLast("handler", this.hander);
	}

	void initType_4http(SocketChannel chn) throws Exception {
		ChannelPipeline p = chn.pipeline();
		// 解码 和 编码
		p.addLast("decoder", new HttpRequestDecoder());
		p.addLast("encoder", new HttpResponseEncoder());

		// 压缩
		p.addLast("deflater", new HttpContentCompressor());

		// HttpObjectAggregator会把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse。
		int maxContentLength = 1024 * 1024 * 10;
		p.addLast("aggregator", new HttpObjectAggregator(maxContentLength));
		
		//为了处理大文件传输的情形
		p.addLast("http-chunked", new ChunkedWriteHandler());
		
		// hander接受到的是:HttpRequest
		p.addLast("handler", this.hander);
	}
}
