package com.bowlong.concurrent.async;

import java.util.concurrent.Callable;

public abstract class CallableForBoolean implements Callable<Boolean> {

	@Override
	public Boolean call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public abstract boolean exec() throws Exception;
}
