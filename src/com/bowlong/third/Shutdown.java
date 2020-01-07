package com.bowlong.third;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.bowlong.lang.InputStreamEx;

//SHUTDOWN
@SuppressWarnings("all")
public class Shutdown extends Thread {
	public ServerSocket ssocket = null;

	public Shutdown(int port, int sleep) throws Exception {
		if (sleep > 0) {
			try {
				Thread.sleep(sleep);
			} catch (Exception e) {
			}
		}

		InetAddress addr = InetAddress.getByName("127.0.0.1");
		ssocket = new ServerSocket(port, 2, addr);
	}

	public Shutdown(int port) throws Exception {
		this(port, 1000);
	}

	@Override
	public void run() {
		try {
			while (_isShut(ssocket.accept())) {
				break;
			}
			beforeShutDown();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean _isShut(Socket socket) {
		String msg = null;
		try (InputStream inStream = socket.getInputStream()) {
			msg = InputStreamEx.inps2Str(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean _is = false;
		try {
			_is = isCanShut(msg, socket);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _is;
	}

	// 在下线之前
	protected boolean isCanShut(String msg, Socket socket) throws Exception {
		return true;
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