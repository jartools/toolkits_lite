package com.bowlong.third;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//SHUTDOWN
@SuppressWarnings("resource")
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
}