package com.bowlong.basic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Ex扩展类的起源<br/>
 * 说有扩展类都应该继承该类。 <br/>
 * 
 * @author Canyon
 * @time 2019-02-14 19:32
 */
public class ExOrigin extends EOURL {

	public static final boolean toBoolean(Object v) {
		if (v == null)
			return false;
		if (v instanceof Boolean) {
			return ((Boolean) v).booleanValue();
		} else if (v instanceof Integer) {
			return ((Integer) v) <= 0 ? false : true;
		} else if (v instanceof Long) {
			return ((Long) v) <= 0 ? false : true;
		} else if (v instanceof String) {
			return stringToBool((String) v);
		}
		return false;
	}

	public static final byte toByte(Object v) {
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).byteValue();
		} else if (v instanceof String) {
			return stringToByte((String) v);
		}
		return 0;
	}

	public static final short toShort(Object v) {
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).shortValue();
		} else if (v instanceof Short) {
			return ((Short) v).shortValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).shortValue();
		} else if (v instanceof Long) {
			return ((Long) v).shortValue();
		} else if (v instanceof Float) {
			return ((Float) v).shortValue();
		} else if (v instanceof Double) {
			return ((Double) v).shortValue();
		} else if (v instanceof String) {
			return stringToShort((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).shortValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).shortValue();
		}
		return 0;
	}

	public static final int toInt(Object v) {
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).intValue();
		} else if (v instanceof Short) {
			return ((Short) v).intValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).intValue();
		} else if (v instanceof Long) {
			return ((Long) v).intValue();
		} else if (v instanceof Float) {
			return ((Float) v).intValue();
		} else if (v instanceof Double) {
			return ((Double) v).intValue();
		} else if (v instanceof String) {
			return stringToInt((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).intValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).intValue();
		}
		return 0;
	}

	public static final long toLong(Object v) {
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).longValue();
		} else if (v instanceof Short) {
			return ((Short) v).longValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).longValue();
		} else if (v instanceof Long) {
			return ((Long) v).longValue();
		} else if (v instanceof Float) {
			return ((Float) v).longValue();
		} else if (v instanceof Double) {
			return ((Double) v).longValue();
		} else if (v instanceof String) {
			return stringToLong((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).longValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).longValue();
		}
		return 0;
	}

	public static final float toFloat(Object v) {
		if (v == null)
			return (float) 0.0;
		if (v instanceof Byte) {
			return ((Byte) v).floatValue();
		} else if (v instanceof Short) {
			return ((Short) v).floatValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).floatValue();
		} else if (v instanceof Long) {
			return ((Long) v).floatValue();
		} else if (v instanceof Float) {
			return ((Float) v).floatValue();
		} else if (v instanceof Double) {
			return ((Double) v).floatValue();
		} else if (v instanceof String) {
			return stringToFloat((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).floatValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).floatValue();
		}
		return 0;
	}

	public static final double toDouble(Object v) {
		if (v == null)
			return 0.0;
		if (v instanceof Byte) {
			return ((Byte) v).doubleValue();
		} else if (v instanceof Short) {
			return ((Short) v).floatValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).doubleValue();
		} else if (v instanceof Long) {
			return ((Long) v).doubleValue();
		} else if (v instanceof Float) {
			return ((Float) v).doubleValue();
		} else if (v instanceof Double) {
			return ((Double) v).doubleValue();
		} else if (v instanceof String) {
			return stringToDouble((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).doubleValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).doubleValue();
		}
		return 0;
	}

	public static final Date toDate(Object v) {
		if (v == null)
			return new Date();
		else if (v instanceof Date)
			return (Date) v;
		else if (v instanceof java.sql.Date)
			return new Date(((java.sql.Date) v).getTime());
		else if (v instanceof java.sql.Timestamp)
			return new Date(((java.sql.Timestamp) v).getTime());
		else if (v instanceof Long)
			return new Date((Long) v);
		else if (v instanceof String)
			return parse2Date((String) v, fmt_yyyy_MM_dd_HH_mm_ss);
		return new Date();
	}

	// 时间字符串比较
	static final public int compareTo4Dt(Date v1, Object v2, String parttern) {
		if (v1 == null || v2 == null)
			return 0;
		boolean isDate2 = (v2 instanceof Date);
		if (isDate2) {
			return compareTo(v1, v2);
		}

		Date d2 = null;
		long time = stringToLong(v2.toString());
		if (time > TIME_1900) {
			d2 = parse2Date(time);
		} else {
			d2 = parse2Date(v2.toString(), parttern);
		}
		return compareTo(v1, d2);
	}

	/** 保留acc位小数 */
	static final public double roundDecimal(double org, int acc) {
		double pow = 1;
		for (int i = 0; i < acc; i++) {
			pow *= 10;
		}

		double temp = (int) (org * pow + 0.5);
		return temp / pow;
	}

	static final public double round(double org, int acc) {
		return roundDecimal(org, acc);
	}

	static final public double round2(double org) {
		return round(org, 2);
	}
	
	static final public org.apache.commons.logging.Log getLog(Class<?> clazz) {
		return org.apache.commons.logging.LogFactory.getLog(clazz);
	}
}
