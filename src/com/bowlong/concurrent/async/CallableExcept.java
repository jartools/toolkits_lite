package com.bowlong.concurrent.async;

import java.util.concurrent.Callable;

public abstract class CallableExcept implements Callable<Exception> {

	@Override
	public Exception call() throws Exception {
		try {
			exec();
		} catch (Exception e) {
			return e;
		}
		return null;
	}

	public abstract void exec() throws Exception;
}
