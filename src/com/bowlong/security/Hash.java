package com.bowlong.security;

import com.bowlong.lang.ByteEx;

public class Hash {
	public static final int code(boolean v) {
		return code(String.valueOf(v));
	}

	public static final int code(byte v) {
		return code(String.valueOf(v));
	}

	public static final int code(short v) {
		return code(String.valueOf(v));
	}

	public static final int code(int v) {
		return code(String.valueOf(v));
	}

	public static final int code(long v) {
		return code(String.valueOf(v));
	}

	public static final int code(float v) {
		return code(String.valueOf(v));
	}

	public static final int code(double v) {
		return code(String.valueOf(v));
	}

	public static final int code(byte[] v) {
		return code(ByteEx.bytesToString(v));
	}

	public static final int code(String str) {
		int code = str.hashCode();
		return Integer.MAX_VALUE - code;
	}
}
