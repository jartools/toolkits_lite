package com.bowlong.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bowlong.basic.ExOrigin;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.text.EasyTemplate;
import com.bowlong.util.ListEx;

public final class StrEx extends ExOrigin {
	public static final String left(final String s, final int len) {
		return s.substring(0, len);
	}

	public static final String right(final String s, final int len) {
		int length = s.length();
		return s.substring(length - len);
	}

	public static final String sub(final String s, final int begin, final int end) {
		return s.substring(begin, end);
	}

	public static final String sub(final String s, final int begin) {
		return s.substring(begin);
	}

	public static final String sub(final String s, final String begin, final String end) {
		int p1 = s.indexOf(begin);
		p1 = p1 < 0 ? 0 : p1 + begin.length();
		int p2 = s.indexOf(end, p1);
		p2 = p2 < 0 ? s.length() : p2;
		return s.substring(p1, p2);
	}

	/** 格式化字符串使其长度为n，不足长度是在前面补上“空格字符” **/
	public static final String fixNSpace(final String s, final int n) {
		return String.format("%" + n + "s", s);
	}

	/** 格式化数字val，使其长度为n，不足长度是在前面补上“0” **/
	public static final String fixNInt(final int val, final int n) {
		return String.format("%0" + n + "d", val);
	}

	public static final String f(final String s, final Object... args) {
		return String.format(s, args);
	}

	public static final String fmtCrLf(final String fmt, final Object... args) {
		return fmt(fmt, args) + "\r\n";
	}

	/**
	 * 
	 * @param fmt
	 *            a_${1}$[1]_${2}
	 * @param args
	 *            v1,v2
	 * @return a_v1v1_v2
	 */
	public static final String fmt(final String fmt, final Object... args) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			int length = args.length;
			for (int i = 1; i < length + 1; i++) {
				params.put(String.valueOf(i), String.valueOf(args[i - 1]));
			}
			return EasyTemplate.make(fmt, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fmt;
	}

	static final private String changeN(final String s, final int p, boolean isLowerP, boolean isLowerOther) {
		int len = s.length();
		if (len <= 0)
			return "";
		if (p >= len)
			return s;

		StringBuffer sb = StringBufPool.borrowObject();
		try {
			String str = "";
			if (p > 0) {
				str = s.substring(0, p);
				if (isLowerOther)
					str = str.toLowerCase();
				sb.append(str);
			}
			str = s.substring(p, p + 1).toUpperCase();
			if (isLowerP) {
				str = str.toLowerCase();
			}
			sb.append(str);

			str = s.substring(p + 1, len);
			if (isLowerOther)
				str = str.toLowerCase();
			sb.append(str);
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public static final String upperN1(final String s) {
		return changeN(s, 0, false, false);
	}

	public static final String upperN(final String s, final int p) {
		return changeN(s, p, false, false);
	}

	static public final String upperFirst(final String s) {
		return upperN1(s);
	}

	static public final String upperFirstLowerOther(final String s) {
		return changeN(s, 1, false, true);
	}

	public static final String lowerFirst(final String s) {
		return changeN(s, 0, true, false);
	}

	public static final String mapToString(final Map<?, ?> m) {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			Iterator<?> it = m.keySet().iterator();
			while (it.hasNext()) {
				Object k = it.next();
				Object v = m.get(k);
				sb.append(k).append("=").append(v).append("\n");
			}
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public static final String toString(final List<?> l) {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			Iterator<?> it = l.iterator();
			while (it.hasNext()) {
				Object v = it.next();
				String var = String.valueOf(v);
				sb.append(var).append("\n");
			}
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public static final String createString(final byte[] b, final String charset) throws Exception {
		return new String(b, charset);
	}

	public static final List<String> toLines(final String s) throws IOException {
		List<String> ret = new Vector<String>();
		StringReader sr = new StringReader(s);
		BufferedReader br = new BufferedReader(sr);
		while (true) {
			String line = br.readLine();
			if (line == null)
				break;
			ret.add(line);
		}
		return ret;
	}

	// 全角字符
	public static final String toW(final String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	// 半角字符
	public static final String toC(final String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	public static final String removeLeft(final String s, final int n) {
		int len = s.length();
		if (len < n)
			return "";
		return s.substring(n);
	}

	public static final String removeRight(final String s, final int n) {
		int len = s.length();
		if (len < n)
			return "";
		return s.substring(0, len - n - 1);
	}

	public static final StringBuffer removeRight(final StringBuffer s, final int n) {
		int len = s.length();
		if (len < n)
			s.setLength(0);
		else {
			s.setLength(len - n);
		}
		return s;
	}

	public static String[] toArray(List<String> list) {
		if (list == null || list.size() <= 0)
			return new String[0];
		return list.toArray(new String[list.size()]);
	}

	public static final boolean isIpv4(String ip) {
		if (ip == null || ip.length() < 6 || ip.length() > 17)
			return false;

		int p1 = ip.indexOf('.');
		if (p1 < 1)
			return false;
		int p2 = ip.indexOf('.', p1 + 1);
		if (p2 < 1)
			return false;
		int p3 = ip.indexOf('.', p2 + 1);
		if (p3 < 1)
			return false;

		return true;
	}

	public static final boolean isEmailAddr(String mail) {
		if (mail == null || mail.length() < 6 || mail.length() > 255)
			return false;

		int p1 = mail.indexOf('@');
		if (p1 < 1)
			return false;
		int p2 = mail.indexOf('.', p1 + 1);
		if (p2 < 1)
			return false;

		return true;
	}

	static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	public static final boolean isValidEmailAddress(String email) {
		if (isEmailAddr(email))
			return email.matches(EMAIL_REGEX);
		return false;
	}

	public static int[] ipv4(final String ipv4) {
		int[] r2 = new int[4];
		if (ipv4 == null || ipv4.length() < 6 || ipv4.length() > 17)
			return r2;

		// 127.0.0.1
		int p1 = ipv4.indexOf('.');
		if (p1 < 1)
			return r2;
		int p2 = ipv4.indexOf('.', p1 + 1);
		if (p2 < 1)
			return r2;
		int p3 = ipv4.indexOf('.', p2 + 1);
		if (p3 < 1)
			return r2;

		String s1 = StrEx.sub(ipv4, 0, p1);
		String s2 = StrEx.sub(ipv4, p1 + 1, p2);
		String s3 = StrEx.sub(ipv4, p2 + 1, p3);
		String s4 = StrEx.sub(ipv4, p3 + 1, ipv4.length());
		r2[0] = NumEx.stringToInt(s1);
		r2[1] = NumEx.stringToInt(s2);
		r2[2] = NumEx.stringToInt(s3);
		r2[3] = NumEx.stringToInt(s4);
		return r2;
	}

	static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

	static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

	static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

	public static final String EMPTY = "";

	public static final boolean isIPv4Address(final String input) {
		return IPV4_PATTERN.matcher(input).matches();
	}

	public static final boolean isIPv6StdAddress(final String input) {
		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	public static final boolean isIPv6HexCompressedAddress(final String input) {
		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}

	public static final boolean isIPv6Address(final String input) {
		return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
	}

	public static String comment(String text) {
		if (isEmpty(text))
			return "";
		return "// " + text;
	}

	static public final void println(Object... vars) {
		print(vars);
		System.out.println();
	}

	static public final void print(Object... vars) {
		StringBuffer sb = new StringBuffer();
		for (Object o : vars) {
			sb.append(o);
		}
		System.out.print(sb);
	}

	/*** 取得长度 **/
	static public final int getLens(String v) {
		if (isEmptyTrim(v))
			return 0;
		return v.length();
	}

	/*** 省略 **/
	static public final String ellipsis(String txt, int lens) {
		int len = getLens(txt);
		if (len == 0)
			return "";
		if (len < lens)
			return txt;
		txt = txt.substring(0, lens);
		txt += "...";
		return txt;
	}

	/*** 判断str1在str2之后 **/
	static public final boolean isAfter(String str1, String str2) {
		if (isEmptyTrim(str1) || isEmptyTrim(str2))
			return false;
		return str1.compareTo(str2) > 0;
	}

	/*** 判断str1在str2之前 **/
	static public final boolean isBefore(String str1, String str2) {
		if (isEmptyTrim(str1) || isEmptyTrim(str2))
			return false;
		return str1.compareTo(str2) < 0;
	}

	/*** 判断str1与str2相同 **/
	static public final boolean isSame(String str1, String str2) {
		if (isEmptyTrim(str1) || isEmptyTrim(str2))
			return false;
		return str1.compareTo(str2) == 0;
	}

	/*** 判断str1在str2之前或者相同 **/
	static public final boolean isNotAfter(String str1, String str2) {
		if (isEmptyTrim(str1) || isEmptyTrim(str2))
			return false;
		return str1.compareTo(str2) <= 0;
	}

	/*** 判断str1在str2之后或者相同 **/
	static public final boolean isNotBefore(String str1, String str2) {
		if (isEmptyTrim(str1) || isEmptyTrim(str2))
			return false;
		return str1.compareTo(str2) >= 0;
	}

	/*** 过滤掉-特殊字符 **/
	static public final String fitler(String txt) {
		int len = getLens(txt);
		if (len == 0)
			return "";
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(txt);
		return m.replaceAll("").trim();
	}

	/*** 过滤替换-特殊字符 **/
	static public final String fitlerRepacle(String txt) {
		int len = getLens(txt);
		if (len == 0)
			return "";
		PStr pStr = PStr.begin();
		txt = txt.trim();
		int lens = 0;
		for (int pos = 0; pos < lens; pos++) {
			switch (txt.charAt(pos)) {
			case '\"':
				pStr = pStr.a("&quot;");
				break;
			case '\'':
				pStr = pStr.a("&apos;");
				break;
			case '<':
				pStr = pStr.a("&lt;");
				break;
			case '>':
				pStr = pStr.a("&gt;");
				break;
			/*
			 * case '&': pStr = pStr.a("&amp;"); break;
			 */
			case '%':
				pStr = pStr.a("&pc;");
				break;
			case '_':
				pStr = pStr.a("&ul;");
				break;
			case '#':
				pStr = pStr.a("&shap;");
				break;
			case '?':
				pStr = pStr.a("&ques;");
				break;
			default:
				pStr = pStr.a(txt.charAt(pos));
				break;
			}
		}
		return pStr.end();
	}

	/*** 过滤替换-xml的无效字符 **/
	static public final String fitlerNonValidXMLChars(String str) {
		if (isEmpty(str)) {
			return str;
		}
		return str.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
	}

	static public final <T extends Object> String join(String sep, List<T> list) {
		if (ListEx.isEmpty(list))
			return "";
		int lens = list.size();
		StringBuffer buff = StringBufPool.borrowObject();
		for (int i = 0; i < lens; i++) {
			buff.append(list.get(i));
			if (i < lens - 1)
				buff.append(sep);
		}
		String ret = buff.toString();
		StringBufPool.returnObject(buff);
		return ret;
	}

	static public final String join(String sep, Object... objects) {
		return join(sep, ListEx.toListT(objects));
	}
}
