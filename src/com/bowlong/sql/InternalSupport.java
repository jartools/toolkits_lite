package com.bowlong.sql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.Toolkit;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.util.DateEx;
import com.bowlong.util.ListEx;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class InternalSupport {

	// NO_CACHE, // 不缓存
	// FULL_CACHE, // 全缓存
	// HOT_CACHE, // 热缓存 (未使用)
	// FULL_MEMORY, // 全内存
	// STATIC_CACHE // 静态缓存(数据不增长)

	public static final Log getLog(Class<?> clazz) {
		return LogFactory.getLog(clazz);
	}

	public static final long difference(int l1, int l2) {
		return l2 - l1;
	}

	public static final Date time() {
		return new java.util.Date();
	}

	public static final List newList() {
		return Collections.synchronizedList(new ArrayList());
	}

	public static final Map newMap() {
		return new ConcurrentHashMap();
	}

	public static final Map newSortedMap() {
		return Collections.synchronizedMap(new TreeMap());
	}

	public static final Set newSet() {
		return new CopyOnWriteArraySet();
	}

	public static final Set newSortedSet() {
		return Collections.synchronizedSet(new TreeSet());
	}

	// ///////////////////////////////////////////////////////////////////////
	public static final int pageCount(int count, int pageSize) {
		return ListEx.pageCount(count, pageSize);
	}

	public static final List getPage(List v, int page, int pageSize) {
		return ListEx.getPage(v, page, pageSize);
	}

	// ///////////////////////////////////////////////////
	// 线程并发线程池
	private static ExecutorService _threadPool = null;

	public static final void execute(Runnable r) {
		if (_threadPool == null)
			_threadPool = Executors.newCachedThreadPool();

		_threadPool.execute(r);
	}

	public static final <T> Future<T> execute(Callable<T> r) {
		if (_threadPool == null)
			_threadPool = Executors.newCachedThreadPool();

		return _threadPool.submit(r);
	}

	// 错误堆栈的内容
	public static final String e2s(Exception e) {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append(e);
			sb.append("\r\n");
			for (StackTraceElement ste : e.getStackTrace()) {
				sb.append("at ");
				sb.append(ste);
				sb.append("\r\n");
			}
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public static final long now() {
		return System.currentTimeMillis();
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

	public static final int compareTo(Object v1, Object v2) {
		return Toolkit.compareTo(v1, v2);
	}
}
