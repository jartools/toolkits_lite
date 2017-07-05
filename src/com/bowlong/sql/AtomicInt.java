package com.bowlong.sql;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicInt {

	/*
	 * 【以get开头】的函数 是先取值，再改变 ;【以get结尾】的函数 是先改变，再取值
	 */

	private AtomicInteger count = null;

	public AtomicInt() {
		count = new AtomicInteger();
	}

	public AtomicInt(int curval) {
		count = new AtomicInteger(curval);
	}

	// method 方法

	// 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
	public boolean compareAndSet(int expect, int update) {
		return count.compareAndSet(expect, update);
	}

	// 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。
	public boolean weakCompareAndSet(int expect, int update) {
		return count.weakCompareAndSet(expect, update);
	}

	// 以 double 形式返回指定的数值。
	public double doubleValue() {
		return count.doubleValue();
	}

	// 以 float 形式返回指定的数值。
	public float floatValue() {
		return count.floatValue();
	}

	// 以 int 形式返回指定的数值。
	public int intValue() {
		return count.intValue();
	}

	// 获取当前值
	public int get() {
		return count.get();
	}

	// 设置当前值
	public void set(int newValue) {
		count.set(newValue);
	}

	// 以原子方式将给定值与当前值相加。
	public int getAndAdd(int delta) {
		return count.getAndAdd(delta);
	}

	// 以原子方式将给定值与当前值相加
	public int addAndGet(int delta) {
		return count.addAndGet(delta);
	}

	// 以原子方式将当前值减 1。
	public int getAndDecrement() {
		return count.getAndDecrement();
	}

	// 以原子方式将当前值减 1。
	public int decrementAndGet() {
		return count.decrementAndGet();
	}

	// 以原子方式将当前值加 1。
	public int getAndIncrement() {
		return count.getAndIncrement();
	}

	// 以原子方式将当前值加 1。
	public int incrementAndGet() {
		return count.incrementAndGet();
	}

	// 以原子方式设置为给定值，并返回旧值。
	public int getAndSet(int newValue) {
		return count.getAndSet(newValue);
	}
}
