package com.bowlong.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

@SuppressWarnings({ "unchecked" })
public class NewListLock<E> extends NewList<E> {
	private static final long serialVersionUID = 1L;

	public NewListLock() {
		super();
	}

	public NewListLock(int paramInt) {
		super(paramInt);
	}

	public NewListLock(int from, int to) {
		subList(from, to);
	}

	// 非公平锁
	// lock :拿不到lock就不罢休，不然线程就一直block(阻塞)。
	// lockInterruptibly :会优先响应线程中断，处理响应的方式是抛出InterruptedException
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	public ReadLock rl = lock.readLock();
	public WriteLock wl = lock.writeLock();

	@Override
	public NewListLock<E> addE(E e) {
		WriteLock writeLock = this.wl;
		try {
			writeLock.lockInterruptibly();
			add(e);
		} catch (Exception ex) {
		} finally {
			writeLock.unlock();
		}
		return this;
	}

	public NewListLock<E> addList(List<E> list) {
		WriteLock writeLock = this.wl;
		try {
			writeLock.lockInterruptibly();
			addAll(list);
		} catch (Exception ex) {
		} finally {
			writeLock.unlock();
		}
		return this;
	}

	public NewListLock<E> addList(E... arr) {
		WriteLock writeLock = this.wl;
		try {
			writeLock.lockInterruptibly();
			if (arr == null)
				return this;
			int len = arr.length;
			for (int i = 0; i < len; i++) {
				add(arr[i]);
			}
		} catch (Exception ex) {
		} finally {
			writeLock.unlock();
		}
		return this;
	}

	@Override
	public E get(int index) {
		ReadLock readLock = this.rl;
		E result = null;
		try {
			readLock.lockInterruptibly();
			result = super.get(index);
		} catch (Exception ex) {
		} finally {
			readLock.unlock();
		}
		return result;
	}

	@Override
	public E remove(int paramInt) {
		WriteLock writeLock = this.wl;
		E result = null;
		try {
			writeLock.lockInterruptibly();
			result = super.remove(paramInt);
		} catch (Exception ex) {
		} finally {
			writeLock.unlock();
		}
		return result;
	}

	@Override
	public boolean remove(Object paramObject) {
		WriteLock writeLock = this.wl;
		boolean result = false;
		try {
			writeLock.lockInterruptibly();
			result = super.remove(paramObject);
		} catch (Exception ex) {
		} finally {
			writeLock.unlock();
		}
		return result;
	}

	@Override
	public void clear() {
		WriteLock writeLock = this.wl;
		try {
			writeLock.lockInterruptibly();
			super.clear();
		} catch (Exception ex) {
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public boolean removeAll(Collection<?> paramCollection) {
		WriteLock writeLock = this.wl;
		boolean result = false;
		try {
			writeLock.lockInterruptibly();
			result = super.removeAll(paramCollection);
		} catch (Exception ex) {
		} finally {
			writeLock.unlock();
		}
		return result;
	}
}
