package com.bowlong.third.netty4.socket;

import java.nio.charset.Charset;

import com.bowlong.text.Encoding;
import com.bowlong.third.netty4.codec.LengthByteArrayDecoder;
import com.bowlong.third.netty4.codec.LengthByteArrayEncoder;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/** 数据处理 */
public class N4ChnInitializer extends ChannelInitializer<SocketChannel> {
	public int ntype = 0;
	public ChannelInboundHandlerAdapter hander;
	Charset utf8 = Encoding.UTF8;
	DelimiterBasedFrameDecoder dbfd1 = new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter());

	public N4ChnInitializer(int ntype, ChannelInboundHandlerAdapter hander) {
		super();
		this.ntype = ntype;
		this.hander = hander;
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
			initType_4(chn);
			break;
		case 5:
			initType_5http(chn);
			break;
		default:
			initType_1(chn);
			break;
		}
	}

	void initType_1(SocketChannel chn) throws Exception {
		ChannelPipeline pipeline = chn.pipeline();
		// 解码 和 编码
		pipeline.addLast("decoder", new LengthByteArrayDecoder(40 * 1024, 0, 4, 0, 4));
		pipeline.addLast("encoder", new LengthByteArrayEncoder());

		// hander接受到的是:byte[]
		pipeline.addLast("handler", this.hander);
	}

	void initType_2(SocketChannel chn) throws Exception {
		ChannelPipeline p = chn.pipeline();
		// 以("\n")为结尾分割的 解码器
		p.addLast("framer", dbfd1);
		// 解码 和 编码
		p.addLast("decoder", new StringDecoder(utf8));
		p.addLast("encoder", new StringEncoder(utf8));

		// hander接受到的是:String
		p.addLast("handler", this.hander);
	}

	void initType_3(SocketChannel chn) throws Exception {
		ChannelPipeline p = chn.pipeline();
		// 以("\n")为结尾分割的 解码器
		p.addLast("framer", dbfd1);
		// 解码 和 编码
		p.addLast("decoder", new Base64Encoder());
		p.addLast("encoder", new Base64Decoder());

		// hander接受到的是:String
		p.addLast("handler", this.hander);
	}

	void initType_4(SocketChannel chn) throws Exception {
		ChannelPipeline p = chn.pipeline();
		// 解码 和 编码
		LineBasedFrameDecoder coder = new LineBasedFrameDecoder(1024 * 1024 * 5);
		p.addLast("decoder", coder);
		p.addLast("encoder", coder);

		// hander接受到的是:String
		p.addLast("handler", this.hander);
	}

	void initType_5http(SocketChannel chn) throws Exception {
		ChannelPipeline p = chn.pipeline();
		// 解码 和 编码
		p.addLast("decoder", new HttpRequestDecoder());
		p.addLast("encoder", new HttpResponseEncoder());

		// 压缩
		p.addLast("deflater", new HttpContentCompressor());

		// HttpObjectAggregator会把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse。
		int maxContentLength = 1024 * 1024 * 3;
		p.addLast("aggregator", new HttpObjectAggregator(maxContentLength));

		// 自己的逻辑Handler
		p.addLast("handler", this.hander);
	}
}
