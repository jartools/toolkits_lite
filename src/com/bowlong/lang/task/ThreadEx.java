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
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ThreadEx {

	static final public MyThreadFactory newThreadFactory(String name, boolean daemon) {
		return new MyThreadFactory(name, daemon);
	}

	// 创建一个无限线程池
	static final public ExecutorService newCachedThreadPool() {
		return newCachedThreadPool("cachedPool");
	}

	static final public ExecutorService newCachedThreadPool(String name) {
		return newCachedThreadPool(name, false);
	}

	static final public ExecutorService newCachedThreadPool(String name, boolean daemon) {
		MyThreadFactory my = newThreadFactory(name, daemon);
		return Executors.newCachedThreadPool(my);
	}

	// 创建一个固定大小线程
	static final public ExecutorService newFixedThreadPool(int nThreads) {
		return newFixedThreadPool("fixedPool", nThreads);
	}

	static final public ExecutorService newFixedThreadPool(String name, int nThreads) {
		return newFixedThreadPool(name, nThreads, false);
	}

	static final public ExecutorService newFixedThreadPool(String name, int nThreads, boolean daemon) {
		MyThreadFactory my = newThreadFactory(name, daemon);
		return Executors.newFixedThreadPool(nThreads, my);
	}

	// 单线程
	static final public ExecutorService newSingleThreadExecutor() {
		return newSingleThreadExecutor("singlePool");
	}

	static final public ExecutorService newSingleThreadExecutor(String name) {
		return newSingleThreadExecutor(name, false);
	}

	static final public ExecutorService newSingleThreadExecutor(String name, boolean daemon) {
		MyThreadFactory my = newThreadFactory(name, daemon);
		return Executors.newSingleThreadExecutor(my);
	}

	public static ExecutorService newThreadExecutor() {
		return newThreadExecutor(0, Integer.MAX_VALUE);
	}

	public static ExecutorService newThreadExecutor(int min, int max) {
		ArrayBlockingQueue<Runnable> abq = new ArrayBlockingQueue<Runnable>(max);
		CallerRunsPolicy crp = new ThreadPoolExecutor.CallerRunsPolicy();
		return new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS, abq, crp);
	}

	public static ExecutorService newThreadExecutor(int min, int max, ThreadFactory threadFactory) {
		ArrayBlockingQueue<Runnable> abq = new ArrayBlockingQueue<Runnable>(max);
		CallerRunsPolicy crp = new ThreadPoolExecutor.CallerRunsPolicy();
		return new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS, abq, threadFactory, crp);
	}

	// 用线程池执行任务 ： (Executor 是 ExecutorService 的父类)
	static final public void execute(Executor executor, Runnable r) {
		executor.execute(r);
	}

	static final public Future<?> submit(ExecutorService executor, Runnable r) {
		return executor.submit(r);
	}

	static final public <T> Future<T> submit(ExecutorService executor, Callable<T> c) {
		return executor.submit(c);
	}

	static final public Thread newThreadExec(Runnable r) {
		final Thread t = new Thread(r);
		t.start();
		return t;
	}

	static final public ForkJoinPool newForkJoinPool(int parallelism) {
		return new ForkJoinPool(parallelism);
	}

	static final public ForkJoinTask<?> forkSubmit(ForkJoinPool fjp, Runnable r) {
		return fjp.submit(r);
	}

	static final public ForkJoinTask<?> forkSubmit(ForkJoinPool fjp, Callable c) {
		return fjp.submit(c);
	}

	static final public ForkJoinTask<?> forkSubmit(ForkJoinPool fjp, RecursiveAction c) {
		return fjp.submit(c);
	}

	// /////////////////////////////////////////////////////////////////
	static final public ScheduledExecutorService newScheduledPool(int nThreads) {
		return newScheduledPool("scheduledPool", nThreads);
	}

	public static ScheduledExecutorService newScheduledPool(String name, int nThreads) {
		return newScheduledPool(name, nThreads, false);
	}

	public static ScheduledExecutorService newScheduledPool(String name, int nThreads, boolean daemon) {
		MyThreadFactory my = newThreadFactory(name, daemon);
		return Executors.newScheduledThreadPool(nThreads, my);
	}

	static final public ScheduledExecutorService newSingleThreadScheduledExecutor(String name) {
		MyThreadFactory my = newThreadFactory(name, false);
		return Executors.newSingleThreadScheduledExecutor(my);
	}

	static final public FutureTask<Exception> execute(ExecutorService es, FutureTask<Exception> task) {
		es.submit(task);
		return task;
	}

	static final public FutureTask<Exception> execute(ExecutorService es, Callable<Exception> task) {
		FutureTask<Exception> ft = new FutureTask<Exception>(task);
		return execute(es, ft);
	}

	// /////////////////////////////////////////////////////////////////
	static final public void sleep(long t) {
		try {
			Thread.sleep(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 用线程池执行
	static ExecutorService _executor = null;

	static ExecutorService _getExec() {
		if (_executor == null)
			_executor = newCachedThreadPool();
		return _executor;
	}

	static ExecutorService _singleExecutor = null;

	static ExecutorService _getSingleExec() {
		if (_singleExecutor == null)
			_singleExecutor = newSingleThreadExecutor();
		return _singleExecutor;
	}

	static final public void shutdown() {
		ExecutorService _es = null;
		if (_executor != null) {
			_es = _getExec();
			_es.shutdown();
		}

		if (_singleExecutor != null) {
			_es = _getSingleExec();
			_es.shutdown();
		}
	}

	static final public void execute(Runnable r) {
		ExecutorService _es = _getExec();
		execute(_es, r);
	}

	static final public Future<?> submit(Runnable r) {
		ExecutorService _es = _getExec();
		return submit(_es, r);
	}

	static final public <T> Future<T> submit(Callable<T> c) {
		ExecutorService _es = _getExec();
		return submit(_es, c);
	}

	static final public void executeSingle(Runnable r) {
		ExecutorService _es = _getSingleExec();
		execute(_es, r);
	}

	static final public Future<?> submitSingle(Runnable r) {
		ExecutorService _es = _getSingleExec();
		return submit(_es, r);
	}

	static final public <T> Future<T> submitSingle(Callable<T> c) {
		ExecutorService _es = _getSingleExec();
		return submit(_es, c);
	}
}
