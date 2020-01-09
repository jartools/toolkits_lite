package com.bowlong.sql.mysql;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.bowlong.basic.ExToolkit;
import com.bowlong.concurrent.async.Async;
import com.bowlong.concurrent.async.CallableExcept;
import com.bowlong.concurrent.async.CallableForList;
import com.bowlong.concurrent.async.CallableForObject;

@SuppressWarnings("rawtypes")
public class RedisSupport extends ExToolkit {
	// ///////////////////////
	public static final ExecutorService newSingleThreadExecutor() {
		return Executors.newSingleThreadExecutor();
	}

	// ///////////////////////
	public static final FutureTask<Exception> async(final ExecutorService es, final CallableExcept ce) {
		return Async.ForExcept.exec(es, ce);
	}

	public static final FutureTask async(final ExecutorService es, final CallableForObject ce) {
		return Async.ForObject.exec(es, ce);
	}

	public static final FutureTask<List> async(final ExecutorService es, final CallableForList ce) {
		return Async.ForList.exec(es, ce);
	}

	// ///////////////////////
	public static final FutureTask<Exception> async(final CallableExcept ce) {
		return Async.ForExcept.exec(ce);
	}

	public static final FutureTask async(final CallableForObject ce) {
		return Async.ForObject.exec(ce);
	}

	public static final FutureTask<List> async(final CallableForList ce) {
		return Async.ForList.exec(ce);
	}
	// ///////////////////////

}
