package com.bowlong.lang.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ThreadEx {

	final private static MyThreadFactory getThreadFactory(String name,
			boolean daemon) {
		return new MyThreadFactory(name, daemon);
	}

	// 创建一个无限线程池
	public static final ExecutorService newCachedThreadPool() {
		return newCachedThreadPool("cachedPool");
	}

	public static final ExecutorService newCachedThreadPool(String name) {
		return newCachedThreadPool(name, false);
	}

	public static final ExecutorService newCachedThreadPool(String name,
			boolean daemon) {
		ThreadFactory cachedFactory = getThreadFactory(name, daemon);
		return Executors.newCachedThreadPool(cachedFactory);
	}

	// 创建一个固定大小线程

	public static final ExecutorService newFixedThreadPool(int nThreads) {
		return newFixedThreadPool("fixedPool", nThreads);
	}

	public static final ExecutorService newFixedThreadPool(String name,
			int nThreads) {
		return newFixedThreadPool(name, nThreads, false);
	}

	public static final ExecutorService newFixedThreadPool(String name,
			int nThreads, boolean daemon) {
		ThreadFactory fixedFactory = getThreadFactory(name, daemon);
		return Executors.newFixedThreadPool(nThreads, fixedFactory);
	}

	// 单线程
	public static final ExecutorService newSingleThreadExecutor() {
		return newSingleThreadExecutor("singlePool");
	}

	public static final ExecutorService newSingleThreadExecutor(String name) {
		return newSingleThreadExecutor(name, false);
	}

	public static final ExecutorService newSingleThreadExecutor(String name,
			boolean daemon) {
		ThreadFactory singleFactory = getThreadFactory(name, daemon);
		return Executors.newSingleThreadExecutor(singleFactory);
	}

	public static ExecutorService newThreadExecutor() {
		final int min = 0;
		final int max = Integer.MAX_VALUE;
		return newThreadExecutor(min, max);
	}

	public static ExecutorService newThreadExecutor(final int min, final int max) {
		return new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(max),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static ExecutorService newThreadExecutor(final int min,
			final int max, final ThreadFactory threadFactory) {
		return new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(max), threadFactory,
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	// 用线程池执行任务
	public static final void execute(final Executor executor, final Runnable r) {
		executor.execute(r);
	}

	public static final Future submit(final ExecutorService executor,
			final Callable c) {
		return executor.submit(c);
	}

	public static final Thread newThreadExec(final Runnable r) {
		final Thread t = new Thread(r);
		t.start();
		return t;
	}

	public static final ForkJoinPool newForkJoinPool(int parallelism) {
		return new ForkJoinPool(parallelism);
	}

	public static final ForkJoinTask<?> forkSubmit(final ForkJoinPool fjp,
			final Runnable r) {
		return fjp.submit(r);
	}

	public static final ForkJoinTask<?> forkSubmit(final ForkJoinPool fjp,
			final Callable c) {
		return fjp.submit(c);
	}

	public static final ForkJoinTask<?> forkSubmit(final ForkJoinPool fjp,
			final RecursiveAction c) {
		return fjp.submit(c);
	}

	// /////////////////////////////////////////////////////////////////
	// 用线程池执行
	static Executor _executor = null;

	public static final void execute(final Runnable r) {
		if (_executor == null)
			_executor = newCachedThreadPool();

		execute(_executor, r);
	}

	static Executor _singleExecutor = null;

	public static final void executeSingle(final Runnable r) {
		if (_singleExecutor == null)
			_singleExecutor = newSingleThreadExecutor();

		execute(_singleExecutor, r);
	}

	public static final ScheduledExecutorService newScheduledPool(
			final int nThreads) {
		return newScheduledPool("scheduledPool", nThreads);
	}

	public static ScheduledExecutorService newScheduledPool(final String name,
			final int nThreads) {
		return newScheduledPool(name, nThreads, false);
	}

	public static ScheduledExecutorService newScheduledPool(final String name,
			final int nThreads, boolean daemon) {
		MyThreadFactory scheduledFactory = getThreadFactory(name, daemon);
		return Executors.newScheduledThreadPool(nThreads, scheduledFactory);
	}

	public static final FutureTask<Exception> execute(final ExecutorService es,
			final FutureTask<Exception> task) {
		es.submit(task);
		return task;
	}

	public static final FutureTask<Exception> execute(final ExecutorService es,
			final Callable<Exception> task) {
		FutureTask<Exception> ft = new FutureTask<Exception>(task);
		return execute(es, ft);
	}

	// /////////////////////////////////////////////////////////////////
	public static final void Sleep(final long t) {
		try {
			Thread.sleep(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /////////////////////////////////////////////////////////////////
}
