package com.bowlong.lang;

import com.bowlong.util.Ref;

/**
 * 字符串-数字之间的转换,判断等逻辑<br/>
 * 
 * @author Canyon
 * @version createtime：2015年8月21日下午3:33:23
 */
public class StrNumEx {
	static final public byte[] toByteArray(final String s, final String charset) throws Exception {
		return s.getBytes(charset);
	}

	static final public int hashCode(final String s) {
		char[] value = s.toCharArray();
		return hashCode(value);
	}

	static final public int hashCode(final char[] value) {
		int hash = 0;
		int count = value.length;
		int offset = 0;

		int i = hash;
		if (i == 0 && count > 0) {
			int j = offset;
			char ac[] = value;
			int k = count;
			for (int l = 0; l < k; l++)
				i = 31 * i + ac[j++];

			hash = i;
		}
		return i;
	}

	static final private Ref<Byte> refByte = new Ref<Byte>((byte) 0);
	static final private Ref<Short> refShort = new Ref<Short>((short) 0);
	static final private Ref<Integer> refInt = new Ref<Integer>(0);
	static final private Ref<Long> refLong = new Ref<Long>(0L);
	static final private Ref<Float> refFloat = new Ref<Float>(0F);
	static final private Ref<Double> refDouble = new Ref<Double>(0D);

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

	static final public String toStr(boolean obj) {
		return String.valueOf(obj);
	}

	static final public String toStr(short obj) {
		return String.valueOf(obj);
	}

	static final public String toStr(int obj) {
		return String.valueOf(obj);
	}

	static final public String toStr(long obj) {
		return String.valueOf(obj);
	}

	static final public String toStr(float obj) {
		return String.valueOf(obj);
	}

	static final public String toStr(double obj) {
		return String.valueOf(obj);
	}
}
