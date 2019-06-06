package com.bowlong.third;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//SHUTDOWN
@SuppressWarnings("all")
public class Shutdown extends Thread {
	public ServerSocket ssocket = null;

	public Shutdown(int port, int sleep) throws Exception {
		InetAddress addr = InetAddress.getByName("127.0.0.1");

		try {
			new Socket(addr, port);
			Thread.sleep(sleep);
		} catch (Exception e) {
		}

		ssocket = new ServerSocket(port, 2, addr);
	}

	public Shutdown(int port) throws Exception {
		this(port, 1000);
	}

	@Override
	public void run() {
		try {
			ssocket.accept();
			beforeShutDown();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 在下线之前
	protected void beforeShutDown() throws Exception {
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
}