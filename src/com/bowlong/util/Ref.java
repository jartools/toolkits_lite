package com.bowlong.util;

public class Ref<T> {
	public Ref() {
	}

	public Ref(T v) {
		val = v;
	}

	public final String toString() {
		if (val == null)
			return nullStr;
		return val.toString();
	}

	static final String nullStr = "val is null";

	public T val;
}
