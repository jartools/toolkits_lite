package com.bowlong.concurrent.async;

import java.sql.ResultSet;
import java.util.concurrent.Callable;

public abstract class CallableForResultSet implements Callable<ResultSet> {

	@Override
	public ResultSet call() throws Exception {
		try {
			return exec();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public abstract ResultSet exec() throws Exception;
}
