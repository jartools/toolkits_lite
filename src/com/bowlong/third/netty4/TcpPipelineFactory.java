package com.bowlong.third.netty4;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import com.bowlong.third.netty4.codec.LengthByteArrayDecoder;
import com.bowlong.third.netty4.codec.LengthByteArrayEncoder;

public class TcpPipelineFactory extends ChannelInitializer<SocketChannel> {
	public static Timer timer = new HashedWheelTimer();

	int nThreads = 8;
	int maxFrameLength = 32 * 1024;
	int lengthFieldOffset = 0;
	int lengthFieldLength = 4;
	int lengthAdjustment = 0;
	int initialBytesToStrip = 4;

	final ReadTimeoutHandler idleHandle;
	final ChannelInboundHandlerAdapter adapter;

	public TcpPipelineFactory(ReadTimeoutHandler idleHandle,
			ChannelInboundHandlerAdapter adapter) {
		this.idleHandle = idleHandle;
		this.adapter = adapter;
	}

	public TcpPipelineFactory(ReadTimeoutHandler idleHandle,
			ChannelInboundHandlerAdapter adapter, int nThreads,
			int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			int lengthAdjustment, int initialBytesToStrip) {
		this.idleHandle = idleHandle;
		this.adapter = adapter;
		this.nThreads = nThreads;
		this.maxFrameLength = maxFrameLength;
		this.lengthFieldOffset = lengthFieldOffset;
		this.lengthFieldLength = lengthFieldLength;
		this.lengthAdjustment = lengthAdjustment;
		this.lengthAdjustment = lengthAdjustment;
	}

	// static IpFilterRuleList rules=new IpFilterRuleList("-n:192.168.2.222");

	// static OrderedMemoryAwareThreadPoolExecutor e = new
	// OrderedMemoryAwareThreadPoolExecutor(
	// 16, 0, 0);
	// static ExecutionHandler executionHandler = new ExecutionHandler(e);

	// public ChannelPipeline getPipeline() throws Exception {
	// // Create a default pipeline implementation.
	// ChannelPipeline pipeline = pipeline();
	//
	// // Uncomment the following line if you want HTTPS
	// // SSLEngine engine =
	// // SecureChatSslContextFactory.getServerContext().createSSLEngine();
	// // engine.setUseClientMode(false);
	// // pipeline.addLast("ssl", new SslHandler(engine));
	// // pipeline.addLast("decoder", new HttpRequestDecoder());
	// // Uncomment the following line if you don't want to handle HttpChunks.
	// // pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
	// // pipeline.addLast("encoder", new HttpResponseEncoder());
	// // Remove the following line if you don't want automatic content
	// // compression.
	// // pipeline.addLast("deflater", new HttpContentCompressor());
	// // pipeline.addLast("handler", new WebHandler());
	// pipeline.addLast("timeout", new IdleHandler(timer, 600));
	// // IpFilterRuleHandler firewall = new IpFilterRuleHandler();
	// // firewall.addAll(rules);
	// // pipeline.addLast("firewall", firewall);
	// // pipeline.addLast("executor", executionHandler);
	// pipeline.addLast("decoder", new B2Decoder());
	// pipeline.addLast("tcp", new LogicTcpHandler());
	// return pipeline;
	// }
	
	EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(
			nThreads);

	@Override
	protected void initChannel(SocketChannel sc) throws Exception {
		ChannelPipeline pipeline = sc.pipeline();
		pipeline.addLast("timeout", idleHandle);
		pipeline.addLast("decoder", new LengthByteArrayDecoder(maxFrameLength,
				lengthFieldOffset, lengthFieldLength, lengthAdjustment,
				initialBytesToStrip));
		pipeline.addLast("encoder", new LengthByteArrayEncoder());
		pipeline.addLast(eventExecutorGroup, "tcp_adapter", adapter);
	}
}
