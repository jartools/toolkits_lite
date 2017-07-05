package com.bowlong.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Map;

import com.bowlong.lang.StrEx;

public class Tcp {
	static final Map<String, InetAddress> _HOSTS = new Hashtable<String, InetAddress>();
	static final Map<String, ReUsableSocket> _SOCKETS = new Hashtable<String, ReUsableSocket>();
	static final String localHost = "127.0.0.1";
	static final int backlog = 1000;
	static final int TIME_OUT = 5 * 60 * 1000;

	// //////////////////////////////////
	public synchronized static final InetAddress addr(final String host)
			throws UnknownHostException {
		InetAddress addr = _HOSTS.get(host);
		if (addr == null) {
			addr = InetAddress.getByName(host);
			_HOSTS.put(host, addr);
		}
		return addr;
	}

	public static final Map<String, Boolean> pingNoExcept(final String from,
			final String to, final int tryNum) {
		final Map<String, Boolean> r2 = new Hashtable<>();
		if (!StrEx.isIpv4(from))
			return r2;
		if (!StrEx.isIpv4(to))
			return r2;
		if (tryNum < 1)
			return r2;

		int f[] = StrEx.ipv4(from);
		int t[] = StrEx.ipv4(to);

		int nFrom = f[3];
		int nTo = t[3];

		String pHost = f[0] + "." + f[1] + "." + f[2] + ".";
		for (int i = nFrom; i < nTo; i++) {
			final String host = pHost + i;
			boolean bRet = pingNoExcept(host, tryNum);
			System.out.println(host + " = " + bRet);
			r2.put(host, bRet);
		}
		return r2;
	}

	public static final boolean pingNoExcept(final String host, final int tryNum) {
		for (int i = 0; i < tryNum; i++) {
			if (pingNoExcept(host))
				return true;
		}
		return false;
	}

	public static final boolean pingNoExcept(final String host) {
		try {
			return ping(host);
		} catch (Exception e) {
		}
		return false;
	}

	public static final boolean ping(final String host)
			throws UnknownHostException, IOException {
		final int timeout = 3000;
		return ping(host, timeout);
	}

	public static final boolean ping(final String host, final int timeout)
			throws UnknownHostException, IOException {
		// String host = "192.168.1.181"
		// int timeOut = 3000;
		boolean status = InetAddress.getByName(host).isReachable(timeout);
		return status;
	}

	// //////////////////////////////////
	public static final Socket createSocket(final String host, final int port)
			throws IOException {
		final InetAddress addr = addr(host);
		return new Socket(addr, port);
	}

	// 可复用的Socket对象
	public static final ReUsableSocket getReUsableSocket(final String host,
			final int port) throws IOException {
		final String key = String.format("%s:%d", host, port);
		ReUsableSocket tcp = _SOCKETS.get(key);
		if (!tcp.isAlive()) {
			tcp = new ReUsableSocket(host, port);
			_SOCKETS.put(key, tcp);
		}
		return tcp;
	}

	public static final Socket createLocalSocket(final int port)
			throws IOException {
		return createSocket(localHost, port);
	}

	// //////////////////////////////////
	public static final ServerSocket createServer(final int port)
			throws IOException {
		return createServer(port, backlog);
	}

	public static final ServerSocket createServer(final int port,
			final int backlog) throws IOException {
		return new ServerSocket(port, backlog);
	}

	public static final ServerSocket createServer(final int port,
			final int backlog, final String host) throws IOException {
		final InetAddress bindAddr = addr(host);
		return new ServerSocket(port, backlog, bindAddr);
	}

	// //////////////////////////////////
	public static final ServerSocket createLocalServer(final int port)
			throws IOException {
		return createServer(port, backlog, localHost);
	}

	public static final ServerSocket createLocalServer(final int port,
			final int backlog) throws IOException {
		return createServer(port, backlog, localHost);
	}

	// //////////////////////////////////

	public static final void setTimeout(final Socket socket, final int tm_sec) {
		if (socket == null)
			return;
		try {
			socket.setSoTimeout(tm_sec * 1000);
			socket.setSoLinger(true, tm_sec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static final InputStream sendAndReceive(final String host,
	// final int port, final byte[] b, int tryNum) {
	// CacheTcp tcp = null;
	// try {
	// tcp = getReUsableSocket(host, port);
	// tcp.write(b);
	// return tcp.in;
	// } catch (IOException e) {
	// if (tcp != null)
	// tcp.close();
	// e.printStackTrace();
	//
	// if (tryNum-- > 0) // re try
	// return sendAndReceive(host, port, b, tryNum);
	// }
	// return null;
	// }

	// public static final byte[] readLocalBytes(final int port)
	// throws IOException {
	// byte[] b = null;
	// return readLocalBytes(port, b);
	// }

	// public static final byte[] readLocalBytes(final int port, final byte[] b)
	// throws IOException {
	// String host = "127.0.0.1";
	// return readBytes(host, port, b);
	// }
	//
	// public static final byte[] readBytes(final String host, final int port)
	// throws IOException {
	// byte[] b = null;
	// return readBytes(host, port, b);
	// }

//	public static final byte[] readBytes(final String host, final int port,
//			final byte[] b) throws IOException {
//		final int timeout = 300;
//		return readBytes(host, port, timeout, b);
//	}

	// public static byte[] readBytes(final String host, final int port,
	// final int tm_sec, final byte[] b) throws IOException {
	// final Socket socket = createSocket(host, port);
	// final OutputStream output = socket.getOutputStream();
	// final InputStream input = socket.getInputStream();
	// final byte[] buf = Bytes100KPool.borrowObject();
	// try {
	// setTimeout(socket, tm_sec);
	// if (b != null && b.length > 0) {
	// output.write(b);
	// output.flush();
	// }
	// socket.shutdownOutput();
	// int len = readFully(input, buf);
	// socket.shutdownInput();
	// return Arrays.copyOf(buf, len);
	// } finally {
	// Bytes100KPool.returnObject(buf);
	// output.close();
	// input.close();
	// socket.close();
	// }
	// }

	// //////////////////////////////////
	public static int readFully(final InputStream input, final byte r2[])
			throws IOException {
		final int off = 0;
		final int len = r2.length;
		int n = 0;
		int tm = 0;
		while (n < len) {
			if (tm++ > 1000)
				break;

			int count = input.read(r2, off + n, len - n);
			if (count <= 0)
				break;

			n += count;
		}
		return n;
	}

	// //////////////////////////////////

	public static final void close(final Socket socket,
			final InputStream input, final OutputStream output) {
		close(socket);
		close(input);
		close(output);
	}

	public static final void close(final InputStream input) {
		if (input == null)
			return;
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void close(final OutputStream output) {
		if (output == null)
			return;
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void close(final Socket socket) {
		if (socket == null)
			return;
		try {
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final InetAddress getLocalHost() throws UnknownHostException {
		return InetAddress.getLocalHost();
	}

	public static final String getLoopbackAddress() throws UnknownHostException {
		return InetAddress.getLoopbackAddress().toString();
	}

	public static final String getLocalHostAddress()
			throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress().toString();
	}

	public static final String getLocalHostName() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName().toString();
	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {

		boolean b = pingNoExcept("192.168.2.3", 3);
		System.out.println(b);

		Map<String, Boolean> m1 = pingNoExcept("192.168.2.1", "192.168.2.255",
				1);
		System.out.println(m1);
		// System.out.println(getLoopbackAddress());
		// System.out.println(getLocalHostAddress());
		// System.out.println(getLocalHostName());
	}
}
