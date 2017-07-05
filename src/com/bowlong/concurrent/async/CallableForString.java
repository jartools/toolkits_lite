package com.bowlong.concurrent.async;

import java.util.concurrent.Callable;

public abstract class CallableForString implements Callable<String> {

	@Override
	public String call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public abstract String exec() throws Exception;
}
