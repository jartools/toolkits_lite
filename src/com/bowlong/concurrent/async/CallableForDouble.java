package com.bowlong.concurrent.async;

import java.util.concurrent.Callable;

public abstract class CallableForDouble implements Callable<Double> {

	@Override
	public Double call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	public abstract double exec() throws Exception;
}
