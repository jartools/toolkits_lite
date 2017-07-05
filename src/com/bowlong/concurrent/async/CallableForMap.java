package com.bowlong.concurrent.async;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@SuppressWarnings("rawtypes")
public abstract class CallableForMap implements Callable<Map> {

	@Override
	public Map call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap();
		}
	}

	public abstract Map exec() throws Exception;
}
