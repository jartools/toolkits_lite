package com.bowlong;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.bowlong.lang.NumEx;
import com.bowlong.util.DateEx;

/**
 * Ex扩展类的起源<br/>
 * 说有扩展类都应该继承该类。 <br/>
 * 
 * @author Canyon
 * @time 2019-02-14 19:32
 */
@SuppressWarnings("unchecked")
public class ExOrigin {
	static public final <T> T toT(Object obj) {
		return (T) obj;
	}

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
			return NumEx.stringToBool((String) v);
		}
		return false;
	}

	public static final byte toByte(Object v) {
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).byteValue();
		} else if (v instanceof String) {
			return NumEx.stringToByte((String) v);
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
			return NumEx.stringToShort((String) v);
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
			return NumEx.stringToInt((String) v);
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
			return NumEx.stringToLong((String) v);
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
			return NumEx.stringToFloat((String) v);
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
			return NumEx.stringToDouble((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).doubleValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).doubleValue();
		}
		return 0;
	}

	public static final String toString(Object v) {
		if (v == null)
			return "";
		if (v instanceof String)
			return (String) v;

		return String.valueOf(v);
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
			return DateEx.parse2Date((String) v, DateEx.fmt_yyyy_MM_dd_HH_mm_ss);
		return new Date();
	}
}
