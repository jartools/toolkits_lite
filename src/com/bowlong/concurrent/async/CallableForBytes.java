package com.bowlong.concurrent.async;

import java.util.concurrent.Callable;

public abstract class CallableForBytes implements Callable<byte[]> {

	@Override
	public byte[] call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public abstract byte[] exec() throws Exception;
}
