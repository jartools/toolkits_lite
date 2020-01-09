package com.bowlong.sql;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.bowlong.basic.ExToolkit;
import com.bowlong.util.DateEx;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class InternalSupport extends ExToolkit {

	// NO_CACHE, // 不缓存
	// FULL_CACHE, // 全缓存
	// HOT_CACHE, // 热缓存 (未使用)
	// FULL_MEMORY, // 全内存
	// STATIC_CACHE // 静态缓存(数据不增长)

	public static final long difference(int l1, int l2) {
		return l2 - l1;
	}

	public static final Date time() {
		return new java.util.Date();
	}

	public static final Map newSortedMap() {
		return Collections.synchronizedMap(new TreeMap());
	}

	public static final Set newSortedSet() {
		return Collections.synchronizedSet(new TreeSet());
	}

	// ///////////////////////////////////////////////////
	// 线程并发线程池
	private static ExecutorService _tpool = null;
	protected static ExecutorService _getES() {
		if (_tpool == null)
			_tpool = Executors.newCachedThreadPool();
		return _tpool;
	}

	public static final void execute(Runnable r) {
		_getES().execute(r);
	}

	public static final <T> Future<T> execute(Callable<T> r) {
		return _getES().submit(r);
	}

	public static final Date beginningToday2() {
		try {
			String s = beginningToday3();
			Date d = new SimpleDateFormat("yyyy-MM-dd").parse(s);
			return d;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public static final String beginningToday3() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 今天凌晨(0点0分)
	public static final long beginningToday() {
		try {
			Date d = beginningToday2();
			return d.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static final long beginningTomorrow() {
		try {
			return beginningToday() + DateEx.TIME_DAY;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
