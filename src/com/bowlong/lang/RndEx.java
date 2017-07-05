package com.bowlong.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import com.bowlong.objpool.StringBufPool;

@SuppressWarnings({ "unchecked", "rawtypes" })
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

	public static final byte[] nextBytes(final int len) {
		byte[] bytes = new byte[len];
		rnd.nextBytes(bytes);
		return bytes;
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
		return rnd.nextInt(t - f) + f;
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

	public static final int nextInt2(final int... args) {
		int i = nextInt(args.length);
		return args[i];
	}

	public static final byte[] nextBytes(byte[] b) {
		if (b == null)
			return null;
		rnd.nextBytes(b);
		return b;
	}

	public static final String nextString(int num) {
		if (num <= 0)
			return "";
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			for (int i = 0; i < num; i++) {
				int ch = RndEx.nextInt(33, 127);
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

	/*** 将list都随机打乱 **/
	public static final List rndList(final List srcList) {
		List ret = new ArrayList();
		List list = new Vector();
		list.addAll(srcList);

		int num = list.size();
		for (int n = num; n > 0; n--) {
			int p = nextInt(n);
			Object e = list.remove(p);
			ret.add(e);
		}
		return ret;
	}

	public static final List subRndList(final List srcList, final int subSize) {
		if (srcList == null)
			return srcList;
		int len = srcList.size();
		List result = rndList(srcList);
		if (len <= subSize)
			return result;
		return result.subList(0, subSize);
	}

	/*** 将泛型list都随机打乱 **/
	public static final <T> List<T> rndListT(final List<T> srcList) {
		return (List<T>) rndList(srcList);
	}

	public static final <T> List<T> subRndListT(final List<T> srcList,
			final int subSize) {
		return (List<T>) subRndList(srcList, subSize);
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 50; i++) {
			String str = nextString(i);
			System.out.println(i + ":" + str);
			list.add(i);
		}
		List<Integer> rndList = rndListT(list);
		List<Integer> subList = subRndListT(list, 400);
		System.out.println("==== list =====");
		System.out.println(list);
		System.out.println(rndList);
		System.out.println(subList);
	}

}
