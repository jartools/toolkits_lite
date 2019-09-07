package com.bowlong.objpool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractQueueObjPool<T> {
	protected final Queue<T> queues = new ConcurrentLinkedQueue<T>();

	protected abstract T createObj();

	protected abstract T resetObj(T obj);

	protected abstract T destoryObj(T obj);

	protected int MAX = Short.MAX_VALUE;

	protected final AtomicInteger num = new AtomicInteger();

	protected final T borrow() {
		synchronized (num) {
			synchronized (queues) {
				if (num.get() > 0) {
					num.decrementAndGet();
					T en = queues.poll();
					return resetObj(en);
				}
			}
		}
		return createObj();
	}

	public final void returnObj(T obj) {
		if (obj == null)
			return;

		synchronized (queues) {
			if (num.get() > MAX) {
				destoryObj(obj);
				return;
			}
			obj = resetObj(obj);
			queues.add(obj);
			num.incrementAndGet();
		}
	}

	public final void clear() {
		synchronized (queues) {
			queues.clear();
		}
	}

	public final int size() {
		return num.intValue();
	}
}
