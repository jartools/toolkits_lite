package com.bowlong.third;

import java.io.OutputStream;
import java.net.Socket;

import com.bowlong.basic.ExOrigin;
import com.bowlong.net.Tcp;

//SHUTDOWN - 守护进程
public class RShutdown extends Thread {

	@Override
	final public void run() {
		try {
			exce_run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void exce_run() throws Exception {
		logShutDown("=====RShutdown 守护进程停掉===", null);
		exce_exit();
	}

	final protected void exce_exit() throws Exception {
		beforeShutDown();
		System.exit(1);
	}

	// 记录关闭日志
	protected void logShutDown(String logInfo, Socket socket) throws Exception {
	}

	// 在下线之前
	protected void beforeShutDown() throws Exception {
	}

	// 添加守护进程
	final public void addShutdownHook() {
		ExOrigin.addShutdownHook(this);
	}

	static final public void close(Socket socket) {
		Tcp.close(socket);
	}

	static final public Socket soMsg(String host, int port, String msg) {
		Socket sss = null;
		try {
			sss = new Socket(host, port);
			OutputStream out = sss.getOutputStream();
			out.write(msg.getBytes("UTF-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
		}
		return sss;
	}

	static final public Socket soMsg(int port, String msg) {
		return soMsg("127.0.0.1", port, msg);
	}

	static final public void doMsg(String host, int port, String msg) {
		Socket ss = soMsg(host, port, msg);
		close(ss);
	}

	static final public void doMsg(int port, String msg) {
		doMsg("127.0.0.1", port, msg);
	}
}