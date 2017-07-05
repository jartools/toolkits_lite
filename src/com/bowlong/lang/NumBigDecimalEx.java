package com.bowlong.lang;

import java.math.BigDecimal;

/**
 * 科学计算或者是工程计算，在商业计算BigDecimal
 * 
 * @author Canyon
 * @version createtime：2015年8月15日 下午2:47:45
 */
public class NumBigDecimalEx {
	
	static public final int getVal4Int(BigDecimal origin) {
		if (origin == null) {
			return 0;
		}
		return origin.intValue();
	}

	static public final double getVal4Double(BigDecimal origin) {
		if (origin == null) {
			return 0l;
		}
		return origin.doubleValue();
	}

	static public final float getVal4Float(BigDecimal origin) {
		if (origin == null) {
			return 0f;
		}
		return origin.floatValue();
	}

	static public final long getVal4Long(BigDecimal origin) {
		if (origin == null) {
			return 0l;
		}
		return origin.longValue();
	}

	static public final String getVal4Str(BigDecimal origin) {
		if (origin == null) {
			return "0.0";
		}
		return origin.toString();
	}

	/*** 加 **/
	static public final BigDecimal add(BigDecimal b1, BigDecimal b2) {
		return b1.add(b2);
	}

	/*** 减 **/
	static public final BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
		return b1.subtract(b2);
	}

	/*** 乘 **/
	static public final BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
		return b1.multiply(b2);
	}

	/*** 除 **/
	static public final BigDecimal divide(BigDecimal b1, BigDecimal b2) {
		return b1.divide(b2);
	}
}
