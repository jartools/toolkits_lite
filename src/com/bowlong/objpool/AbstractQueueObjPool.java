package com.bowlong.objpool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractQueueObjPool<E> {
	protected final Queue<E> queues = new ConcurrentLinkedQueue<E>();

	protected abstract E createObj();

	protected abstract E resetObj(E obj);

	protected abstract E destoryObj(E obj);

	protected int MAX = Short.MAX_VALUE;

	protected final AtomicInteger num = new AtomicInteger();

	protected final E borrow() {
		synchronized (num) {
			synchronized (queues) {
				if (num.get() > 0) {
					num.decrementAndGet();
					E en = queues.poll();
					return resetObj(en);
				}
			}
		}
		return createObj();
	}

	public final void returnObj(E obj) {
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
