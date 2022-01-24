package com.bowlong.third;

import java.io.OutputStream;
import java.net.Socket;

import com.bowlong.basic.ExOrigin;
import com.bowlong.lang.task.ThreadEx;
import com.bowlong.net.Tcp;

//SHUTDOWN - 守护进程
public class RShutdown extends Thread {
	protected boolean isShuting = false;
	private boolean isSHook = false;

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
		doExit();
	}

	final public void doExit() {
		try {
			this.isShuting = true;
			ThreadEx.shutdown();
			clear();
			exce_exit();
		} catch (Exception e) {
			e.printStackTrace();
			this._sysExit(-1);
		}
	}

	final public void doExitByHook() {
		this.isSHook = true;
		this.doExit();
	}

	protected void clear() {
	}

	final private void exce_exit() {
		beforeShutDown();
		this._sysExit(1);
	}

	final private void _sysExit(int statues) {
		if (!this.isSHook)
			System.exit(statues);
	}

	// 记录关闭日志
	protected void logShutDown(String logInfo, Socket socket) {
	}

	// 在下线之前
	protected void beforeShutDown() {
	}

	// 添加守护进程
	final public void addShutdownHook(Thread t) {
		if (t == null)
			return;
		ExOrigin.addShutdownHook(t);
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