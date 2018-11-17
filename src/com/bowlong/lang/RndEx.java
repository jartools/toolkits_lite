package com.bowlong.lang;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.bowlong.objpool.StringBufPool;

public final class RndEx {
	// private static final Random rnd = new Random(System.currentTimeMillis());
	private static final Random rnd = new Random(RndEx.randomNum());

	public static final int randomNum() {
		return randomUUID().hashCode();
	}

	public static final String randomUUID() {
		return UUID.randomUUID().toString();
	}

	public static final boolean nextBoolean() {
		return rnd.nextBoolean();
	}

	static public final boolean nextBool(int max, int f) {
		int v = nextInt(max);
		return (v < f);
	}

	public static final byte[] nextBytes(byte[] bts) {
		if (bts == null)
			return null;
		rnd.nextBytes(bts);
		return bts;
	}

	public static final byte[] nextBytes(final int len) {
		return nextBytes(new byte[len]);
	}

	public static final double nextDouble() {
		return rnd.nextDouble();
	}

	public static final double nextGaussian() {
		return rnd.nextGaussian();
	}

	public static final double nextFloat() {
		return rnd.nextFloat();
	}

	public static final int nextInt() {
		return rnd.nextInt();
	}

	public static final int nextInt(final int n) {
		return rnd.nextInt(n);
	}

	public static final int nextInt(final int f, final int t) {
		if (f == t)
			return t;
		int min = f > t ? t : f;
		int max = f > t ? f : t;
		if (max - min == 1)
			return min;

		return min + rnd.nextInt(max - min);
	}

	public static final long nextLong() {
		return rnd.nextLong();
	}

	public static final int nextInt(final int[] args) {
		int i = nextInt(args.length);
		return args[i];
	}

	public static final int nextInt(final List<Integer> args) {
		int i = nextInt(args.size());
		return args.get(i);
	}

	public static final int nextIntArgs(final int... args) {
		int i = nextInt(args.length);
		return args[i];
	}

	public static final String nextString(int num) {
		if (num <= 0)
			return "";
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			for (int i = 0; i < num; i++) {
				int ch = nextInt(33, 127);
				sb.append((char) ch);
			}
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	/*** 从给定的字符里面随机得一个字符 **/
	static public final String nextString(final String org) {
		if (StrEx.isEmpty(org))
			return "";
		int len = org.length();
		int rnd = nextInt(len);
		return org.substring(rnd, rnd + 1);
	}

	/*** 从给定的字符里面随机得字符 **/
	static public final String nextString(final String org, int rndLen) {
		if (rndLen <= 0 || StrEx.isEmpty(org))
			return "";
		StringBuffer buff = StringBufPool.borrowObject();
		try {
			for (int i = 0; i < rndLen; i++) {
				buff.append(nextString(org));
			}
			return buff.toString();
		} catch (Exception e) {
			return "";
		} finally {
			StringBufPool.returnObject(buff);
		}
	}

	/*** 随机指定长度数字字符 **/
	static public final String nextString09(final int rndLen) {
		if (rndLen <= 0)
			return "";
		StringBuffer buff = StringBufPool.borrowObject();
		try {
			for (int i = 0; i < rndLen; i++) {
				buff.append(nextInt(10));
			}
			return buff.toString();
		} catch (Exception e) {
			return "";
		} finally {
			StringBufPool.returnObject(buff);
		}
	}

	public static final double nextDouble(final double f, final double t, final int decimal) {
		if (f == t)
			return t;

		double lmt = 1;
		for (int i = 0; i < decimal; i++) {
			lmt *= 10;
		}

		double min = f > t ? t : f;
		double max = f > t ? f : t;
		if ((max - min) * lmt <= 1)
			return min;

		int mi = (int) (min * lmt);
		int ma = (int) (max * lmt);
		// 经过测试用乘积得的小数会有偏差尤其是保留两位小数0.01以上 始终保持 10的-16次方
		return (mi + rnd.nextInt(ma - mi)) / lmt;
	}

	public static final double nextDouble(final double f, final double t) {
		return nextDouble(f, t, 2);
	}
}
