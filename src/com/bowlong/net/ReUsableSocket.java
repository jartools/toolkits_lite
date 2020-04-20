package com.bowlong.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

public class ReUsableSocket {

	protected Socket socket;

	protected InputStream in;
	protected OutputStream out;

	protected DataInputStream din;
	protected DataOutputStream dout;

	protected com.bowlong.bio2.BIO2InputStream b2in;
	protected com.bowlong.bio2.BIO2OutputStream b2out;

	protected BufferedReader reader;
	protected BufferedWriter writer;

	public boolean isAlive() {
		if (socket == null || in == null || out == null)
			return false;
		if (socket.isClosed() || !socket.isConnected())
			return false;
		if (socket.isClosed())
			return false;
		if (!socket.isConnected())
			return false;
		return true;
	}

	public ReUsableSocket(String host,int port) throws IOException {
		this(Tcp.createSocket(host, port));
	}

	public ReUsableSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.in = socket.getInputStream();
		this.out = socket.getOutputStream();
	}

	public void setTimeout(int timeout) throws SocketException {
		this.socket.setSoTimeout(timeout);
	}

	public void setSoLinger(boolean on, int linger) throws SocketException {
		this.socket.setSoLinger(on, linger);
	}

	public Socket getSocket() {
		return this.socket;
	}

	public boolean isAddr(String addr) {
		if (socket == null)
			return false;
		String str = socket.toString();
		return (str.contains(addr));
	}

	public InputStream in() {
		return this.in;
	}

	public OutputStream out() {
		return this.out;
	}

	public DataInputStream din() {
		if (this.din == null)
			this.din = new DataInputStream(this.in);
		return this.din;
	}

	public DataOutputStream dout() {
		if (this.dout == null)
			this.dout = new DataOutputStream(out);
		return this.dout;
	}

	public com.bowlong.bio2.BIO2InputStream b2in() {
		if (this.b2in == null)
			this.b2in = new com.bowlong.bio2.BIO2InputStream(this.in);
		return this.b2in;
	}

	public com.bowlong.bio2.BIO2OutputStream b2out() {
		if (this.b2out == null)
			this.b2out = new com.bowlong.bio2.BIO2OutputStream(this.out);
		return this.b2out;
	}

	public BufferedReader reader() {
		if (this.reader == null)
			this.reader = new BufferedReader(new InputStreamReader(this.in));
		return this.reader;
	}

	public BufferedWriter writer() {
		if (this.writer == null)
			this.writer = new BufferedWriter(new OutputStreamWriter(this.out));
		return this.writer;
	}

	public void close() {
		Tcp.close(socket, in, out);

		this.socket = null;

		this.in = null;
		this.out = null;

		this.din = null;
		this.dout = null;

		this.b2in = null;
		this.b2out = null;

	}

}
