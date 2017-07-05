package com.bowlong.lang;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class NumFmtEx {

	public static enum DecimalDot {
		DOT_MInt, DOT_M2, DOT_0, DOT_1, DOT_2, DOT_3, DOT_4, DOT_5, DOT_6, DOT_7, DOT_8
	}

	/** money 格式化整数显示 */
	static final DecimalFormat _dfmt_MInt = new DecimalFormat("###,###");
	/** money 格式化保留两位小数显示，0 = 0.00 */
	static final DecimalFormat _dfmt_M2 = new DecimalFormat("###,##0.00");

	static final DecimalFormat _decimalFormat0 = new DecimalFormat(".");
	static final DecimalFormat _decimalFormat1 = new DecimalFormat(".0");
	static final DecimalFormat _decimalFormat2 = new DecimalFormat(".00");
	static final DecimalFormat _decimalFormat3 = new DecimalFormat(".000");
	static final DecimalFormat _decimalFormat4 = new DecimalFormat(".0000");
	static final DecimalFormat _decimalFormat5 = new DecimalFormat(".00000");
	static final DecimalFormat _decimalFormat6 = new DecimalFormat(".000000");
	static final DecimalFormat _decimalFormat7 = new DecimalFormat(".0000000");
	static final DecimalFormat _decimalFormat8 = new DecimalFormat(".00000000");

	static public final String formatDouble(final double s) {
		return formatDouble(s, DecimalDot.DOT_2);
	}

	static public final String formatDouble(final double s, DecimalDot dot) {
		String fmtVal = fmtDouble(s, dot);
		if (fmtVal.indexOf(".") == 0) {
			return PStr.str("0", fmtVal);
		}
		return fmtVal;
	}

	static public final String fmtDouble(final double s, DecimalDot dot) {
		switch (dot) {
		case DOT_MInt:
			return _dfmt_MInt.format(s);
		case DOT_M2:
			return _dfmt_M2.format(s);
		case DOT_0:
			return _decimalFormat0.format(s);
		case DOT_1:
			return _decimalFormat1.format(s);
		case DOT_2:
			return _decimalFormat2.format(s);
		case DOT_3:
			return _decimalFormat3.format(s);
		case DOT_4:
			return _decimalFormat4.format(s);
		case DOT_5:
			return _decimalFormat5.format(s);
		case DOT_6:
			return _decimalFormat6.format(s);
		case DOT_7:
			return _decimalFormat7.format(s);
		case DOT_8:
			return _decimalFormat8.format(s);
		default:
			return _decimalFormat2.format(s);
		}
	}

	static public final String formartBigNum(final BigDecimal s) {
		return formartBigNum(s, DecimalDot.DOT_2);
	}

	static public final String formartBigNum(final BigDecimal s, DecimalDot dot) {
		String fmtVal = fmtBigNum(s, dot);
		if (fmtVal.indexOf(".") == 0) {
			return PStr.str("0", fmtVal);
		}
		return fmtVal;
	}

	static public final String fmtBigNum(final BigDecimal s, DecimalDot dot) {
		switch (dot) {
		case DOT_MInt:
			return _dfmt_MInt.format(s);
		case DOT_M2:
			return _dfmt_M2.format(s);
		case DOT_0:
			return _decimalFormat0.format(s);
		case DOT_1:
			return _decimalFormat1.format(s);
		case DOT_2:
			return _decimalFormat2.format(s);
		case DOT_3:
			return _decimalFormat3.format(s);
		case DOT_4:
			return _decimalFormat4.format(s);
		case DOT_5:
			return _decimalFormat5.format(s);
		case DOT_6:
			return _decimalFormat6.format(s);
		case DOT_7:
			return _decimalFormat7.format(s);
		case DOT_8:
			return _decimalFormat8.format(s);
		default:
			return _decimalFormat2.format(s);
		}
	}
}
