package com.bowlong.concurrent.async;

import java.util.concurrent.Callable;

public abstract class CallableForObject implements Callable<Object> {

	@Override
	public Object call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public abstract Object exec() throws Exception;
}
