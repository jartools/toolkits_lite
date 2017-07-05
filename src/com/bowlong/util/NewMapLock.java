package com.bowlong.util;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class NewMapLock<K, V> extends NewMap<K, V> {
	public static NewMapLock create() {
		return new NewMapLock();
	}

	public static NewMapLock newly() {
		return new NewMapLock();
	}

	public static NewMapLock create(Object... objs) {
		NewMapLock r2 = create();
		return (NewMapLock) MapEx.putKvs4NewMap(r2, objs);
	}

	public static NewMapLock create(Map map) {
		if (map instanceof NewMapLock)
			return (NewMapLock) map;

		NewMapLock ret = new NewMapLock();
		ret.putAll(map);
		return ret;
	}

	public static NewMapLock newly(Map map) {
		return create(map);
	}

	public static NewMapLock newly(Object key, Object value) {
		return create().putE(key, value);
	}

	// /////////////
	// 非公平锁
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	public ReadLock rl = lock.readLock();
	public WriteLock wl = lock.writeLock();

	public NewMapLock() {
	}

	public NewMapLock(int initialCapacity) {
		super(initialCapacity);
	}

	// /////////////

	public NewMapLock<K, V> putE(K key, V value) {
		try {
			wl.lockInterruptibly();
			super.putE(key, value);
		} catch (Exception e) {
		} finally {
			wl.unlock();
		}
		return this;
	}

	public NewMapLock<K, V> add(Object... objs) {
		NewMapLock<K, V> result = null;
		try {
			wl.lockInterruptibly();
			result = (NewMapLock<K, V>) super.add(objs);
		} catch (Exception e) {
		} finally {
			wl.unlock();
		}
		return result;
	}

	public NewMapLock<K, V> putE(Map map) {
		try {
			wl.lockInterruptibly();
			super.putE(map);
		} catch (Exception e) {
		} finally {
			wl.unlock();
		}
		return this;
	}

	@Override
	public void clear() {
		try {
			wl.lockInterruptibly();
			super.clear();
		} catch (Exception e) {
		} finally {
			wl.unlock();
		}
	}

	@Override
	public V remove(Object key) {
		V rem = null;
		try {
			wl.lockInterruptibly();
			rem = super.remove(key);
		} catch (Exception e) {
		} finally {
			wl.unlock();
		}
		return rem;
	}

	@Override
	public V get(Object key) {
		V result = null;
		try {
			rl.lockInterruptibly();
			result = super.get(key);
		} catch (Exception e) {
		} finally {
			rl.unlock();
		}
		return result;
	}

	public static void main(String[] args) {
		NewMapLock<Integer, Integer> map = new NewMapLock<Integer, Integer>();
		map.add(1, 2);
		System.out.println(map);
	}
}
