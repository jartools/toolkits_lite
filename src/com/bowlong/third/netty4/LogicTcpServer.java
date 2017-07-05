package com.bowlong.third.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Date;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.bowlong.Toolkit;

public class LogicTcpServer extends Toolkit {
	static Log log = LogFactory.getLog(LogicTcpServer.class);
	public static Date BEGIN = new Date();
	static String NAME = "sea";
	static double VER = 1.0;

	public static void start(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(
							new TcpPipelineFactory(new IdleHandler(
									TcpPipelineFactory.timer, 600),
									new TcpInboundHandler()));

			b.bind(port);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public static void main(String[] args) {
		
	}
}
