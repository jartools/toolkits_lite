package com.bowlong.lang.task;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/***
 * 线程工厂对象 <br/>
 * daemon[false=User Thread(用户线程)、true=Daemon Thread(守护线程)] <br/>
 * 用户线程：就是我们平时创建的普通线程.<br/>
 * 守护线程：主要是用来服务用户线程.<br/>
 * 如果 User Thread已经全部退出运行了，只剩下Daemon Thread存在了，虚拟机也就退出了。<br/>
 * 因为没有了被守护者，Daemon也就没有工作可做了，也就没有继续运行程序的必要了。<br/>
 * 所以：[读写操作或者计算逻辑不要放在守护线程中执行]
 * 
 */
public final class MyThreadFactory implements ThreadFactory {
	// 守护线程注意点:
	// 1 thread.setDaemon(true)必须在thread.start()之前设置
	// 2 在守护线程中产生的新线程也是守护线程的。
	// 3 读写操作或者计算逻辑不要放在守护线程中执行

	static final AtomicInteger nPool = new AtomicInteger(1);
	static final AtomicInteger nThread = new AtomicInteger(1);
	static final String fmt = "%s.pool-%s-thread-";

	final ThreadGroup group;
	final String namePrefix;
	private boolean daemon = true;

	public MyThreadFactory(String nameP, boolean daemon) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = String.format(fmt, nameP, nPool.getAndIncrement());
		this.daemon = daemon;
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + nThread.getAndIncrement(), 0);
		t.setDaemon(daemon);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}
}
