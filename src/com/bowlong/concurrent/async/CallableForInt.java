package com.bowlong.concurrent.async;

import java.util.concurrent.Callable;

public abstract class CallableForInt implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public abstract int exec() throws Exception;
}
