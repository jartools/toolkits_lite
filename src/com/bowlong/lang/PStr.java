package com.bowlong.lang;

import java.io.Serializable;
import java.util.Map;

import com.bowlong.objpool.StringBufPool;
import com.bowlong.text.EasyTemplate;

/**
 * 字符串拼接 [不能以单例模式，多线程会出错]
 * 
 * @author Canyon
 * @version createtime：2015年8月19日下午3:40:09
 */
public final class PStr implements Serializable {

	private static final long serialVersionUID = 1L;

	public static PStr getInstance() {
		return new PStr();
	}

	static public final PStr b(Object... objs) {
		PStr ret = getInstance();
		ret.a(objs);
		return ret;
	}

	public static final PStr begin(Object... objs) {
		return b(objs);
	}

	public static final String str(Object... objs) {
		PStr pStr = begin(objs);
		return pStr.end();
	}

	public static final String strFmt(String fmt,Object... args) {
		PStr ret = begin();
		ret.afmt(fmt, args);
		return ret.end();
	}

	// ///////////////////////////////////////////////////
	private StringBuffer sb = null;

	private PStr() {
		sb = StringBufPool.borrowObject();
	}

	public final PStr reset() {
		if (sb == null) {
			sb = StringBufPool.borrowObject();
		}
		return clear();
	}

	public final PStr clear() {
		sb.setLength(0);
		return this;
	}

	public final String end(Object... v) {
		a(v);
		return e();
	}

	public final String e(Object... v) {
		a(v);
		String ret = sb.toString();
		StringBufPool.returnObject(sb);
		sb = null;
		return ret;
	}

	// ///////////////////////////////////////////////////
	public final PStr a(Object... objs) {
		if (objs != null) {
			for (Object obj : objs) {
				if (obj != null) {
					sb.append(String.valueOf(obj));
				}
			}
		}
		return this;
	}

	public final PStr a(String str,Map<String, Object> map) throws Exception {
		String v = EasyTemplate.make(str, map);
		sb.append(v);
		return this;
	}

	public final PStr a(StringBuffer v) {
		sb.append(v);
		return this;
	}

	public final PStr afmt(String needFmtStr,Object... args) {
		try {
			String s2 = StrEx.fmt$(needFmtStr, args);
			return a(s2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public final PStr delLeft(int i) {
		sb.replace(0, i, "");
		return this;
	}

	public final PStr delRight(int i) {
		// int len = sb.length();
		// sb.replace(len - i, len, "");
		StrEx.removeRight(sb, i);
		return this;
	}

	public static void main(String[] args) {
		String s = PStr.begin(123).a("dddd").a(true).delRight(4).end();
		System.out.println(s);
	}

	public int length() {
		return sb.length();
	}
}
