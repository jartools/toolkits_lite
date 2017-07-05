package com.bowlong.net;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface TcpChannel {

	// public void writeTo(int pid, int cmd, Map map) throws Exception;

	// public T attachment();
	//
	// public void setAttachment(T attach);
	
	public void close();

	public void send(Map map) throws Exception;

	public void send(byte[] buf) throws Exception;

}
