package com.bowlong.basic;

import java.net.URL;

import com.bowlong.util.Ref;

/**
 * 字符串-数字之间的转换,判断等逻辑<br/>
 * 
 * @author Canyon
 * @version createtime：2015年8月21日下午3:33:23
 */
public class EOStrNum extends EOBasic {
	static final public boolean isByte(String s, Ref<Byte> ref) {
		try {
			if (s == null || "".equals(s))
				return false;
			byte val = Byte.parseByte(s);
			if (ref != null) {
				ref.val = val;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static final public boolean isByte(String s) {
		return isByte(s, null);
	}

	static final public boolean isShort(String s, Ref<Short> ref) {
		try {
			if (s == null || "".equals(s))
				return false;
			short val = Short.parseShort(s);
			if (ref != null) {
				ref.val = val;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static final public boolean isShort(String s) {
		return isShort(s, null);
	}

	static final public boolean isInt(String s, Ref<Integer> ref) {
		try {
			if (s == null || "".equals(s))
				return false;
			int val = Integer.parseInt(s);
			if (ref != null) {
				ref.val = val;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static final public boolean isInt(String s) {
		return isInt(s, null);
	}

	static final public boolean isLong(String s, Ref<Long> ref) {
		try {
			if (s == null || "".equals(s))
				return false;
			long val = Long.parseLong(s);
			if (ref != null) {
				ref.val = val;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static final public boolean isLong(String s) {
		return isLong(s, null);
	}

	static final public boolean isFloat(String s, Ref<Float> ref) {
		try {
			if (s == null || "".equals(s))
				return false;
			float val = Float.parseFloat(s);
			if (ref != null) {
				ref.val = val;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static final public boolean isFloat(String s) {
		return isFloat(s, null);
	}

	static final public boolean isDouble(String s, Ref<Double> ref) {
		try {
			if (s == null || "".equals(s))
				return false;
			double val = Double.parseDouble(s);
			if (ref != null) {
				ref.val = val;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static final public boolean isDouble(String s) {
		return isDouble(s, null);
	}

	//
	static final public boolean stringToBool(String s, boolean v) {
		try {
			if ("0".equals(s)) {
				return false;
			}
			if ("1".equals(s)) {
				return true;
			}
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			return v;
		}
	}

	static final public boolean stringToBool(String s) {
		return stringToBool(s, false);
	}

	static final public byte stringToByte(String s, byte v) {
		Ref<Byte> refByte = new Ref<Byte>((byte) 0);
		refByte.val = v;
		isByte(s, refByte);
		return refByte.val;
	}

	static final public byte stringToByte(String s) {
		return stringToByte(s, (byte) 0);
	}

	static final public byte[] stringToByte(String[] v) {
		byte[] r = new byte[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToByte(s);
			n++;
		}
		return r;
	}

	static final public short stringToShort(String s, short v) {
		Ref<Short> refShort = new Ref<Short>((short) 0);
		refShort.val = v;
		isShort(s, refShort);
		return refShort.val;
	}

	static final public short stringToShort(String s) {
		return stringToShort(s, (short) 0);
	}

	static final public short[] stringToShort(String[] v) {
		short[] r = new short[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToShort(s);
			n++;
		}
		return r;
	}

	static final public int stringToInt(String s, int v) {
		Ref<Integer> refInt = new Ref<Integer>(0);
		refInt.val = v;
		isInt(s, refInt);
		return refInt.val;
	}

	static final public int stringToInt(String s) {
		return stringToInt(s, 0);
	}

	static final public int[] stringToInt(String[] v) {
		int[] r = new int[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToInt(s);
			n++;
		}
		return r;
	}

	static final public long stringToLong(String s, long v) {
		Ref<Long> refLong = new Ref<Long>(0L);
		refLong.val = v;
		isLong(s, refLong);
		return refLong.val;
	}

	static final public long stringToLong(String s) {
		return stringToLong(s, 0);
	}

	static final public long[] stringToLong(String[] v) {
		long[] r = new long[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToLong(s);
			n++;
		}
		return r;
	}

	static final public float stringToFloat(String s, float v) {
		Ref<Float> refFloat = new Ref<Float>(0F);
		refFloat.val = v;
		isFloat(s, refFloat);
		return refFloat.val;
	}

	static final public float stringToFloat(String s) {
		return stringToFloat(s, (float) 0.0);
	}

	static final public float[] stringToFloat(String[] v) {
		float[] r = new float[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToFloat(s);
			n++;
		}
		return r;
	}

	static final public double stringToDouble(String s, double v) {
		Ref<Double> refDouble = new Ref<Double>(0D);
		refDouble.val = v;
		isDouble(s, refDouble);
		return refDouble.val;
	}

	static final public double stringToDouble(String s) {
		return stringToDouble(s, 0.0);
	}

	static final public double[] stringToDouble(String[] v) {
		double[] r = new double[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToDouble(s);
			n++;
		}
		return r;
	}

	static final public String toStr(byte[] b, String chaset) {
		try {
			if (!isEmpty(b))
				return new String(b, chaset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	static final public String toStr(byte[] b) {
		return toStr(b, "utf-8");
	}

	static final public String toStr(Object v) {
		if (v == null)
			return "";
		if (v instanceof String)
			return (String) v;

		return String.valueOf(v);
	}

	static final public String toDir(String dir) {
		if (isEmpty(dir))
			return "";

		dir = repBackSlash(dir, "/");
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		return dir;
	}

	static final public String appRoot() {
		return System.getProperty("user.dir");
	}

	static final public String appPath() {
		ClassLoader _cL = EOStrNum.class.getClassLoader();
		URL _url = _cL.getResource("");
		return _url.getPath();
	}

	static final public String dirTmWebRoot() {
		String root = appRoot();
		String dir = root.replace("bin", "webapps");
		return toDir(dir);
	}

	static final public String locateFromClasspath(String resourceName) {
		String path = null;
		// String tt = "path";
		// attempt to load from the context classpath
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader != null) {
			URL url = loader.getResource(resourceName);
			if (url != null) {
				// tt = "context classpath";
				path = url.getPath();
			}
		}

		// attempt to load from the system classpath
		if (isEmpty(path)) {
			URL url = ClassLoader.getSystemResource(resourceName);
			if (url != null) {
				// tt = "system classpath";
				path = url.getPath();
			}
		}

		if (isEmpty(path))
			path = resourceName;
		// log.info("Loading configuration from "+tt+" (" + path + ")");
		return path;
	}

	static final public String package2Path(String pkg) {
		return pkg.replaceAll("\\.", "/");
	}

	/*** \反斜杠替换成空白 **/
	static final public String repBackSlash(String str) {
		return repBackSlash(str, "");
	}

	/*** \反斜杠替换 **/
	static final public String repBackSlash(String str, String reval) {
		if (reval == null)
			reval = "";
		return str.replaceAll("\\\\", reval);
	}

	/*** 斜杠/替换 **/
	static final public String repSlash(String str, String reval) {
		if (reval == null)
			reval = "";
		return str.replaceAll("/", reval);
	}

	/*** 斜杠/与\反斜杠都替换 **/
	static final public String repSlashAll(String str, String reval) {
		if (reval == null)
			reval = "";
		str = repSlash(str, reval);
		return repBackSlash(str, reval);
	}

	/** Exclude 排除了的 */
	static final public String left(String s, String exclude, boolean isLast) {
		int p1 = -1;
		if (isLast)
			p1 = s.lastIndexOf(exclude);
		else
			p1 = s.indexOf(exclude);
		p1 = p1 < 0 ? s.length() : p1;
		return s.substring(0, p1);
	}

	static final public String left(String s, String exclude) {
		return left(s, exclude, false);
	}

	static final public String right(String s, String exclude, boolean isLast) {
		int p1 = -1;
		if (isLast)
			p1 = s.lastIndexOf(exclude);
		else
			p1 = s.indexOf(exclude);
		p1 = p1 < 0 ? 0 : p1 + exclude.length();
		return s.substring(p1);
	}

	static final public String right(String s, String exclude) {
		return right(s, exclude, true);
	}

	/*** 文件后缀 **/
	static final public String suffix(String fp, boolean include) {
		if (isEmpty(fp))
			return "";
		String r = right(fp, f_d);
		if (include) {
			return f_d + r;
		}
		return r;
	}

	static final public String suffix(String fp) {
		return suffix(fp, false);
	}

	static final private String reFilePath(String fp, String exclude) {
		if (isEmpty(fp))
			return "";
		int index = fp.indexOf(exclude);
		int lens = exclude.length();
		if (index == 0) {
			fp = fp.substring(lens);
		} else if (index > 0) {
			String _s = fp.substring(0, index);
			String _b = left(_s, "/", true);
			_b = left(_b, "/", true);
			String _e = fp.substring(index + lens);
			fp = _b + "/" + _e;
		}
		return fp;
	}

	static final public String realFilePath(String fp) {
		if (isEmpty(fp))
			return "";
		fp = repBackSlash(fp, "/");
		String exclude = "../";
		while (fp.indexOf(exclude) != -1) {
			fp = reFilePath(fp, exclude);
		}

		exclude = "..";
		while (fp.indexOf(exclude) != -1) {
			fp = reFilePath(fp, exclude);
		}
		return fp;
	}

	static final public int count(String str, String target) {
		if (isEmpty(str) || isEmpty(target))
			return 0;
		int _c = 0;
		int i = str.indexOf(target);
		while (i != -1) {
			i = str.indexOf(target, i + 1);
			++_c;
		}
		return _c;
	}

	static final public boolean isInMinMax(double v, double min, double max) {
		return v >= min && v <= max;
	}

	static final public int limitMinMax(int v, int min, int max) {
		v = max(v, min);
		return min(v, max);
	}

	// limitMin 限定最小值 = max
	static final public int max(int v1, int v2) {
		return v1 < v2 ? v2 : v1;
	}

	static final public int max(int[] arrs) {
		int result = arrs[0];
		int size = arrs.length;
		for (int i = 1; i < size; i++) {
			result = max(arrs[i], result);
		}
		return result;
	}

	static final public int max(int v, int... arrs) {
		int size = arrs.length;
		for (int i = 1; i < size; i++) {
			v = max(arrs[i], v);
		}
		return v;
	}

	// limitMax 限定最大值 = min
	static final public int min(int v1, int v2) {
		return v1 < v2 ? v1 : v2;
	}

	static final public int min(int[] arrs) {
		int result = arrs[0];
		int size = arrs.length;
		for (int i = 1; i < size; i++) {
			result = min(arrs[i], result);
		}
		return result;
	}

	static final public int min(int v, int... arrs) {
		int size = arrs.length;
		for (int i = 1; i < size; i++) {
			v = min(arrs[i], v);
		}
		return v;
	}
}
