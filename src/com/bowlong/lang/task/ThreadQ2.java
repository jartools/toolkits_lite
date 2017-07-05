package com.bowlong.lang.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadQ2 {
	final ExecutorService executor;
	final AtomicInteger threadID;

	public ThreadQ2() {
		executor = Executors.newCachedThreadPool();
		threadID = new AtomicInteger(1);
	}

	public ThreadQ2(int min, int max) {
		executor = ThreadEx.newThreadExecutor(min, max);
		threadID = new AtomicInteger(1);
	}

	public ThreadQ2(final String name) {
		executor = Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				String namePrefix = String.format("%s-pool-%d", name,
						threadID.getAndIncrement());
				Thread t = new Thread(r, namePrefix);
				t.setDaemon(false);
				if (t.getPriority() != Thread.NORM_PRIORITY)
					t.setPriority(Thread.NORM_PRIORITY);
				return t;
			}
		});
		threadID = new AtomicInteger(1);
	}

	public Future<?> execute(final Runnable r) {
		return executor.submit(r);
		// executor.execute(r);
	}

	public void shutdownNow() {
		executor.shutdownNow();
	}

	public void cancel(Future<?> f) {
		f.cancel(false);
	}

	private static ThreadQ2 THREAD_Q2;

	public static Future<?> exec(final Runnable r) {
		if (THREAD_Q2 == null)
			THREAD_Q2 = new ThreadQ2();
		return THREAD_Q2.execute(r);
	}

	public static void main(String[] args) {
		ThreadQ2 tq = new ThreadQ2("aa");
		Future<?> f = tq.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("run.1...");
				ThreadEx.Sleep(1000);
				System.out.println("run.2...");
			}
		});
		ThreadEx.Sleep(1000);
		tq.cancel(f);
	}
}
