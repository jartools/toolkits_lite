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
		return Executors.newCachedThreadPool(newThreadFactory(name, daemon));
	}

	// 创建一个固定大小线程
	static final public ExecutorService newFixedThreadPool(int nThreads) {
		return newFixedThreadPool("fixedPool", nThreads);
	}

	static final public ExecutorService newFixedThreadPool(String name, int nThreads) {
		return newFixedThreadPool(name, nThreads, false);
	}

	static final public ExecutorService newFixedThreadPool(String name, int nThreads, boolean daemon) {
		return Executors.newFixedThreadPool(nThreads, newThreadFactory(name, daemon));
	}

	// 单线程
	static final public ExecutorService newSingleThreadExecutor() {
		return newSingleThreadExecutor("singlePool");
	}

	static final public ExecutorService newSingleThreadExecutor(String name) {
		return newSingleThreadExecutor(name, false);
	}

	static final public ExecutorService newSingleThreadExecutor(String name, boolean daemon) {
		return Executors.newSingleThreadExecutor(newThreadFactory(name, daemon));
	}

	public static ExecutorService newThreadExecutor() {
		return newThreadExecutor(0, Integer.MAX_VALUE);
	}

	public static ExecutorService newThreadExecutor(int min, int max) {
		return new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(max), new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static ExecutorService newThreadExecutor(int min, int max, ThreadFactory threadFactory) {
		return new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(max), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
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
		return Executors.newScheduledThreadPool(nThreads, newThreadFactory(name, daemon));
	}

	static final public ScheduledExecutorService newSingleThreadScheduledExecutor(String name) {
		return Executors.newSingleThreadScheduledExecutor(newThreadFactory(name, false));
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

	static final public void execute(Runnable r) {
		execute(_getExec(), r);
	}

	static final public Future<?> submit(Runnable r) {
		return submit(_getExec(), r);
	}

	static final public <T> Future<T> submit(Callable<T> c) {
		return submit(_getExec(), c);
	}

	static final public void executeSingle(Runnable r) {
		execute(_getSingleExec(), r);
	}

	static final public Future<?> submitSingle(Runnable r) {
		return submit(_getSingleExec(), r);
	}

	static final public <T> Future<T> submitSingle(Callable<T> c) {
		return submit(_getSingleExec(), c);
	}
}
