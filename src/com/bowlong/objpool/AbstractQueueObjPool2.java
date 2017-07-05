package com.bowlong.objpool;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.bowlong.util.NewMap;

public abstract class AbstractQueueObjPool2<E> {
	protected final Map<Object, ConcurrentLinkedQueue<E>> pools = new NewMap<Object, ConcurrentLinkedQueue<E>>();

	protected abstract E createObj(Object bucket);

	protected abstract E resetObj(E obj);

	protected abstract E destoryObj(E obj);

	protected int MAX = Short.MAX_VALUE;

	protected final AtomicInteger num = new AtomicInteger();

	protected final E borrow(Object bucket) {
		E r2 = null;

		synchronized (pools) {
			ConcurrentLinkedQueue<E> frees = pools.get(bucket);
			if (frees == null)
				frees = new ConcurrentLinkedQueue<>();

			if (num.intValue() > 0) {
				num.decrementAndGet();
				r2 = frees.poll();
			}
		}
		r2 = createObj(bucket);
		return r2;
	}

	protected final void returnObj(Object bucket, E obj) {
		if (obj == null)
			return;

		synchronized (pools) {
			ConcurrentLinkedQueue<E> frees = pools.get(bucket);
			if (frees == null)
				frees = new ConcurrentLinkedQueue<>();

			if (num.intValue() > MAX) {
				destoryObj(obj);
				return;
			}
			resetObj(obj);
			frees.add(obj);
			num.incrementAndGet();
		}
	}

	public final void clear() {
		synchronized (pools) {
			pools.clear();
		}
	}

	public final void clear(Object bucket) {
		synchronized (pools) {
			ConcurrentLinkedQueue<E> frees = pools.get(bucket);
			if (frees == null)
				frees = new ConcurrentLinkedQueue<>();

			frees.clear();
		}
	}

	public final int size(Object bucket) {
		synchronized (pools) {
			ConcurrentLinkedQueue<E> frees = pools.get(bucket);
			if (frees == null)
				frees = new ConcurrentLinkedQueue<>();

			return frees.size();
		}
	}
}
