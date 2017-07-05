package com.bowlong.lang.task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//@SuppressWarnings({ "rawtypes", "unchecked" })
public final class ThreadQ implements Runnable {
	// private final Vector<Runnable> queue = new Vector<>();
	private final Queue<Runnable> queue; // = new ConcurrentLinkedQueue<>();

	public ThreadQ(final int nThreads) {
		queue = new ConcurrentLinkedQueue<>();
		for (int i = 0; i < nThreads; i++)
			new Thread(this, "TaskQueue-Thread-" + i).start();
	}

	public void execute(final Runnable task) {
		synchronized (queue) {
			queue.add(task);
			// queue.addElement(task);
			queue.notify();
		}
	}

	// public void addElement(final Runnable task) {
	// synchronized (queue) {
	// queue.add(task);
	// }
	// }
	//
	// public void notifyTasks() {
	// synchronized (queue) {
	// queue.notifyAll();
	// }
	// }

	public void clear() {
		synchronized (queue) {
			queue.clear();
		}
	}

	public boolean cancel(final Runnable task) {
		synchronized (queue) {
			if (queue.contains(task)) {
				return queue.remove(task);
			}
		}
		return false;
	}

	public void run() {
		while (true) {
			try {
				final Runnable task;
				synchronized (queue) {
					while (queue.isEmpty())
						queue.wait();
					// task = (Runnable) queue.firstElement();
					// queue.removeElementAt(0);
					task = queue.poll();
				}
				if (task != null)
					task.run();
			} catch (InterruptedException e) {
				// ignore
			} catch (RuntimeException e) {
				// ignore
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}