package com.bowlong.concurrent.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("rawtypes")
public abstract class CallableForList implements Callable<List> {

	@Override
	public List call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	public abstract List exec() throws Exception;
}
