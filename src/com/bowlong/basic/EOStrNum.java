package com.bowlong.basic;

import com.bowlong.util.Ref;

/**
 * 字符串-数字之间的转换,判断等逻辑<br/>
 * 
 * @author Canyon
 * @version createtime：2015年8月21日下午3:33:23
 */
public class EOStrNum extends EOBasic {
	static final public Ref<Boolean> refBl = new Ref<Boolean>(false);
	static final public Ref<Byte> refByte = new Ref<Byte>((byte) 0);
	static final public Ref<Short> refShort = new Ref<Short>((short) 0);
	static final public Ref<Integer> refInt = new Ref<Integer>(0);
	static final public Ref<Long> refLong = new Ref<Long>(0L);
	static final public Ref<Float> refFloat = new Ref<Float>(0F);
	static final public Ref<Double> refDouble = new Ref<Double>(0D);
	static final public Ref<Object> refObj = new Ref<Object>();

	static final public boolean isByte(final String s, final Ref<Byte> ref) {
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

	static final public boolean isByte(final String s) {
		return isByte(s, null);
	}

	static final public boolean isShort(final String s, final Ref<Short> ref) {
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

	static final public boolean isShort(final String s) {
		return isShort(s, null);
	}

	static final public boolean isInt(final String s, final Ref<Integer> ref) {
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

	static final public boolean isInt(final String s) {
		return isInt(s, null);
	}

	static final public boolean isLong(final String s, final Ref<Long> ref) {
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

	static final public boolean isLong(final String s) {
		return isLong(s, null);
	}

	static final public boolean isFloat(final String s, final Ref<Float> ref) {
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

	static final public boolean isFloat(final String s) {
		return isFloat(s, null);
	}

	static final public boolean isDouble(final String s, final Ref<Double> ref) {
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

	static final public boolean isDouble(final String s) {
		return isDouble(s, null);
	}

	//
	static final public boolean stringToBool(final String s, final boolean v) {
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

	static final public boolean stringToBool(final String s) {
		return stringToBool(s, false);
	}

	static final public byte stringToByte(final String s, final byte v) {
		refByte.val = v;
		isByte(s, refByte);
		return refByte.val;
	}

	static final public byte stringToByte(final String s) {
		return stringToByte(s, (byte) 0);
	}

	static final public byte[] stringToByte(final String[] v) {
		byte[] r = new byte[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToByte(s);
			n++;
		}
		return r;
	}

	static final public short stringToShort(final String s, final short v) {
		refShort.val = v;
		isShort(s, refShort);
		return refShort.val;
	}

	static final public short stringToShort(final String s) {
		return stringToShort(s, (short) 0);
	}

	static final public short[] stringToShort(final String[] v) {
		short[] r = new short[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToShort(s);
			n++;
		}
		return r;
	}

	static final public int stringToInt(final String s, final int v) {
		refInt.val = v;
		isInt(s, refInt);
		return refInt.val;
	}

	static final public int stringToInt(final String s) {
		return stringToInt(s, 0);
	}

	static final public int[] stringToInt(final String[] v) {
		int[] r = new int[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToInt(s);
			n++;
		}
		return r;
	}

	static final public long stringToLong(final String s, final long v) {
		refLong.val = v;
		isLong(s, refLong);
		return refLong.val;
	}

	static final public long stringToLong(final String s) {
		return stringToLong(s, 0);
	}

	static final public long[] stringToLong(final String[] v) {
		long[] r = new long[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToLong(s);
			n++;
		}
		return r;
	}

	static final public float stringToFloat(final String s, final float v) {
		refFloat.val = v;
		isFloat(s, refFloat);
		return refFloat.val;
	}

	static final public float stringToFloat(final String s) {
		return stringToFloat(s, (float) 0.0);
	}

	static final public float[] stringToFloat(final String[] v) {
		float[] r = new float[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToFloat(s);
			n++;
		}
		return r;
	}

	static final public double stringToDouble(final String s, final double v) {
		refDouble.val = v;
		isDouble(s, refDouble);
		return refDouble.val;
	}

	static final public double stringToDouble(final String s) {
		return stringToDouble(s, 0.0);
	}

	static final public double[] stringToDouble(final String[] v) {
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

	static final public String appRoot() {
		return System.getProperty("user.dir");
	}

	static final public String appPath() {
		return EOStrNum.class.getClassLoader().getResource("").getPath();
	}

	static final public String dirTmWebRoot() {
		String root = appRoot();
		String dir = root.replace("bin", "webapps");
		dir = repBackSlash(dir, "/");
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		return dir;
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
}
