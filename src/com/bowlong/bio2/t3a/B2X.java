package com.bowlong.bio2.t3a;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.bio2.B2OutputStream;
import com.bowlong.bio2.B2Type;
import com.bowlong.io.ByteOutStream;
import com.bowlong.lang.ByteEx;
import com.bowlong.lang.NumEx;
import com.bowlong.objpool.ByteOutPool;
import com.bowlong.util.NewList;
import com.bowlong.util.Ref;
/***
 * com.bowlong.third.assist生成用
 * @author Canyon
 * @version createtime：2015年10月27日上午10:44:14
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class B2X {

	// null
	public static final byte TYPE_NULL = 0;
	// bool
	public static final byte TYPE_BOOLEAN_TRUE = 1;
	public static final byte TYPE_BOOLEAN_FALSE = 2;
	// byte
	public static final byte TYPE_BYTE_0 = 3;
	public static final byte TYPE_BYTE = 4;
	// short
	public static final byte TYPE_SHORT_0 = 5;
	public static final byte TYPE_SHORT_8B = 6;
	public static final byte TYPE_SHORT_16B = 7;
	// int b32 b24 b16 b8
	public static final byte TYPE_INT_0 = 8;
	public static final byte TYPE_INT_8B = 9;
	public static final byte TYPE_INT_16B = 10;
	public static final byte TYPE_INT_32B = 11;
	public static final byte TYPE_INT_N1 = 12;
	public static final byte TYPE_INT_1 = 13;
	public static final byte TYPE_INT_2 = 14;
	public static final byte TYPE_INT_3 = 15;
	public static final byte TYPE_INT_4 = 16;
	public static final byte TYPE_INT_5 = 17;
	public static final byte TYPE_INT_6 = 18;
	public static final byte TYPE_INT_7 = 19;
	public static final byte TYPE_INT_8 = 20;
	public static final byte TYPE_INT_9 = 21;
	public static final byte TYPE_INT_10 = 22;
	public static final byte TYPE_INT_11 = 23;
	public static final byte TYPE_INT_12 = 24;
	public static final byte TYPE_INT_13 = 25;
	public static final byte TYPE_INT_14 = 26;
	public static final byte TYPE_INT_15 = 27;
	public static final byte TYPE_INT_16 = 28;
	public static final byte TYPE_INT_17 = 29;
	public static final byte TYPE_INT_18 = 30;
	public static final byte TYPE_INT_19 = 31;
	public static final byte TYPE_INT_20 = 32;
	public static final byte TYPE_INT_21 = 33;
	public static final byte TYPE_INT_22 = 34;
	public static final byte TYPE_INT_23 = 35;
	public static final byte TYPE_INT_24 = 36;
	public static final byte TYPE_INT_25 = 37;
	public static final byte TYPE_INT_26 = 38;
	public static final byte TYPE_INT_27 = 39;
	public static final byte TYPE_INT_28 = 40;
	public static final byte TYPE_INT_29 = 41;
	public static final byte TYPE_INT_30 = 42;
	public static final byte TYPE_INT_31 = 43;
	public static final byte TYPE_INT_32 = 44;
	// long b64 b56 b48 b40 b32 b24 b16 b8
	public static final byte TYPE_LONG_0 = 45;
	public static final byte TYPE_LONG_8B = 46;
	public static final byte TYPE_LONG_16B = 47;
	public static final byte TYPE_LONG_32B = 48;
	public static final byte TYPE_LONG_64B = 49;
	// double b64 b56 b48 b40 b32 b24 b16 b8
	public static final byte TYPE_DOUBLE_0 = 50;
	// public static final byte DOUBLE_8B = 51;
	// public static final byte DOUBLE_16B = 52;
	// public static final byte DOUBLE_32B = 53;
	public static final byte TYPE_DOUBLE_64B = 54;
	// STR [bytes]
	public static final byte TYPE_STR_0 = 55;
	public static final byte TYPE_STR = 56;
	public static final byte TYPE_STR_1 = 57;
	public static final byte TYPE_STR_2 = 58;
	public static final byte TYPE_STR_3 = 59;
	public static final byte TYPE_STR_4 = 60;
	public static final byte TYPE_STR_5 = 61;
	public static final byte TYPE_STR_6 = 62;
	public static final byte TYPE_STR_7 = 63;
	public static final byte TYPE_STR_8 = 64;
	public static final byte TYPE_STR_9 = 65;
	public static final byte TYPE_STR_10 = 66;
	public static final byte TYPE_STR_11 = 67;
	public static final byte TYPE_STR_12 = 68;
	public static final byte TYPE_STR_13 = 69;
	public static final byte TYPE_STR_14 = 70;
	public static final byte TYPE_STR_15 = 71;
	public static final byte TYPE_STR_16 = 72;
	public static final byte TYPE_STR_17 = 73;
	public static final byte TYPE_STR_18 = 74;
	public static final byte TYPE_STR_19 = 75;
	public static final byte TYPE_STR_20 = 76;
	public static final byte TYPE_STR_21 = 77;
	public static final byte TYPE_STR_22 = 78;
	public static final byte TYPE_STR_23 = 79;
	public static final byte TYPE_STR_24 = 80;
	public static final byte TYPE_STR_25 = 81;
	public static final byte TYPE_STR_26 = 82;
	// Bytes [int len, byte[]]
	public static final byte TYPE_BYTES_0 = 83;
	public static final byte TYPE_BYTES = 84;
	// VECTOR [int len, v...]
	public static final byte TYPE_VECTOR_0 = 85;
	public static final byte TYPE_VECTOR = 86;
	public static final byte TYPE_VECTOR_1 = 87;
	public static final byte TYPE_VECTOR_2 = 88;
	public static final byte TYPE_VECTOR_3 = 89;
	public static final byte TYPE_VECTOR_4 = 90;
	public static final byte TYPE_VECTOR_5 = 91;
	public static final byte TYPE_VECTOR_6 = 92;
	public static final byte TYPE_VECTOR_7 = 93;
	public static final byte TYPE_VECTOR_8 = 94;
	public static final byte TYPE_VECTOR_9 = 95;
	public static final byte TYPE_VECTOR_10 = 96;
	public static final byte TYPE_VECTOR_11 = 97;
	public static final byte TYPE_VECTOR_12 = 98;
	public static final byte TYPE_VECTOR_13 = 99;
	public static final byte TYPE_VECTOR_14 = 100;
	public static final byte TYPE_VECTOR_15 = 101;
	public static final byte TYPE_VECTOR_16 = 102;
	public static final byte TYPE_VECTOR_17 = 103;
	public static final byte TYPE_VECTOR_18 = 104;
	public static final byte TYPE_VECTOR_19 = 105;
	public static final byte TYPE_VECTOR_20 = 106;
	public static final byte TYPE_VECTOR_21 = 107;
	public static final byte TYPE_VECTOR_22 = 108;
	public static final byte TYPE_VECTOR_23 = 109;
	public static final byte TYPE_VECTOR_24 = 110;
	// HASHTABLE [int len, k,v...]
	public static final byte TYPE_HASHTABLE_0 = 111;
	public static final byte TYPE_HASHTABLE = 112;
	public static final byte TYPE_HASHTABLE_1 = 113;
	public static final byte TYPE_HASHTABLE_2 = 114;
	public static final byte TYPE_HASHTABLE_3 = 115;
	public static final byte TYPE_HASHTABLE_4 = 116;
	public static final byte TYPE_HASHTABLE_5 = 117;
	public static final byte TYPE_HASHTABLE_6 = 118;
	public static final byte TYPE_HASHTABLE_7 = 119;
	public static final byte TYPE_HASHTABLE_8 = 120;
	public static final byte TYPE_HASHTABLE_9 = 121;
	public static final byte TYPE_HASHTABLE_10 = 122;
	public static final byte TYPE_HASHTABLE_11 = 123;
	public static final byte TYPE_HASHTABLE_12 = 124;
	public static final byte TYPE_HASHTABLE_13 = 125;
	public static final byte TYPE_HASHTABLE_14 = 126;
	public static final byte TYPE_HASHTABLE_15 = 127;
	// java.util.Date
	public static final byte TYPE_JAVA_DATE = -31;
	// 新增类型 - b2int
	public static final byte Type_INT_B2 = -33;

	public static final Charset UTF8 = Charset.forName("UTF-8");
	// ///////////////////////////
	public static final Boolean boolVal = Boolean.TRUE;
	public static final Byte byteVal = NumEx.BYTE_MIN_VALUE;
	public static final Short shortVal = NumEx.SHORT_MIN_VALUE;
	public static final Integer intVal = NumEx.INT_MIN_VALUE;
	public static final Long longVal = NumEx.LONG_MIN_VALUE;
	public static final Double doubleVal = NumEx.DOUBLE_MIN_VALUE;
	public static final String stringVal = new String("");
	public static final byte[] bytesVal = new byte[0];
	public static final Date dateVal = new Date();

	// ///////////////////////////

	public static final boolean toBool(byte[] b) throws IOException {
		return toBool(b, 0);
	}

	public static final boolean toBool(byte[] b, int offset) throws IOException {
		byte tag = b[offset];
		if (tag == B2Type.BOOLEAN_TRUE)
			return true;
		else if (tag == B2Type.BOOLEAN_FALSE)
			return false;

		throw new IOException(tag + " - tag not bool.");
	}

	public static final byte toByte(byte[] b) throws IOException {
		return toByte(b, 0);
	}

	public static final byte toByte(byte[] b, int offset) throws IOException {
		byte tag = b[offset];
		if (tag == B2Type.BYTE_0)
			return 0;
		else if (tag == B2Type.BYTE)
			return b[offset + 1];

		throw new IOException(tag + " - tag not byte.");
	}

	public static final short toShort(byte[] b) throws IOException {
		return toShort(b, 0);
	}

	public static final short toShort(byte[] b, int offset) throws IOException {
		byte tag = b[offset];
		if (tag == B2Type.SHORT_0)
			return 0;
		else if (tag == B2Type.SHORT_8B)
			return (short) b[offset + 1];
		else if (tag == B2Type.SHORT_16B) {
			return (short) (((b[offset + 1] & 0xff) << 8) + ((b[offset + 2] & 0xff) << 0));
		}

		throw new IOException(tag + " - tag not short.");
	}

	public static final int toInt(byte[] b) throws IOException {
		return toInt(b, 0);
	}

	public static final int toInt(byte[] b, int offset) throws IOException {
		byte tag = b[offset];
		if (tag == B2Type.INT_N1) {
			return -1;
		} else if (tag == B2Type.INT_0) {
			return 0;
		} else if (tag == B2Type.INT_1) {
			return 1;
		} else if (tag == B2Type.INT_2) {
			return 2;
		} else if (tag == B2Type.INT_3) {
			return 3;
		} else if (tag == B2Type.INT_4) {
			return 4;
		} else if (tag == B2Type.INT_5) {
			return 5;
		} else if (tag == B2Type.INT_6) {
			return 6;
		} else if (tag == B2Type.INT_7) {
			return 7;
		} else if (tag == B2Type.INT_8) {
			return 8;
		} else if (tag == B2Type.INT_9) {
			return 9;
		} else if (tag == B2Type.INT_10) {
			return 10;
		} else if (tag == B2Type.INT_11) {
			return 11;
		} else if (tag == B2Type.INT_12) {
			return 12;
		} else if (tag == B2Type.INT_13) {
			return 13;
		} else if (tag == B2Type.INT_14) {
			return 14;
		} else if (tag == B2Type.INT_15) {
			return 15;
		} else if (tag == B2Type.INT_16) {
			return 16;
		} else if (tag == B2Type.INT_17) {
			return 17;
		} else if (tag == B2Type.INT_18) {
			return 18;
		} else if (tag == B2Type.INT_19) {
			return 19;
		} else if (tag == B2Type.INT_20) {
			return 20;
		} else if (tag == B2Type.INT_21) {
			return 21;
		} else if (tag == B2Type.INT_22) {
			return 22;
		} else if (tag == B2Type.INT_23) {
			return 23;
		} else if (tag == B2Type.INT_24) {
			return 24;
		} else if (tag == B2Type.INT_25) {
			return 25;
		} else if (tag == B2Type.INT_26) {
			return 26;
		} else if (tag == B2Type.INT_27) {
			return 27;
		} else if (tag == B2Type.INT_28) {
			return 28;
		} else if (tag == B2Type.INT_29) {
			return 29;
		} else if (tag == B2Type.INT_30) {
			return 30;
		} else if (tag == B2Type.INT_31) {
			return 31;
		} else if (tag == B2Type.INT_32) {
			return 32;
		} else if (tag == B2Type.INT_8B) {
			byte v = (byte) b[offset + 1];
			return new Integer(v);
		} else if (tag == B2Type.INT_16B) {
			short v = (short) (((b[offset + 1] & 0xff) << 8) + ((b[offset + 2] & 0xff) << 0));
			return new Integer(v);
		} else if (tag == B2Type.INT_32B) {
			int v1 = b[offset + 1];
			int v2 = b[offset + 2];
			int v3 = b[offset + 3];
			int v4 = b[offset + 4];
			int v = ((v1 & 0xff) << 24) + ((v2 & 0xff) << 16)
					+ ((v3 & 0xff) << 8) + ((v4 & 0xff) << 0);
			return new Integer(v);
		}
		throw new IOException(tag + " - tag not int.");
	}

	public static final long toLong(byte[] b) throws IOException {
		return toLong(b, 0);
	}

	public static final long toLong(byte[] b, int offset) throws IOException {
		byte tag = b[offset];
		if (tag == B2Type.LONG_0) {
			return 0;
		} else if (tag == B2Type.LONG_8B) {
			int v = b[offset + 1];
			return v;
		} else if (tag == B2Type.LONG_16B) {
			int v = (((b[offset + 1] & 0xff) << 8) + ((b[offset + 2] & 0xff) << 0));
			return v;
		} else if (tag == B2Type.LONG_32B) {
			int v1 = b[offset + 1];
			int v2 = b[offset + 2];
			int v3 = b[offset + 3];
			int v4 = b[offset + 4];
			int v = ((v1 & 0xff) << 24) + ((v2 & 0xff) << 16)
					+ ((v3 & 0xff) << 8) + ((v4 & 0xff) << 0);
			return v;
		} else if (tag == B2Type.LONG_64B) {
			long high = ((b[offset + 1] & 0xff) << 24)
					+ ((b[offset + 2] & 0xff) << 16)
					+ ((b[offset + 3] & 0xff) << 8)
					+ ((b[offset + 4] & 0xff) << 0);
			long low = ((b[offset + 5] & 0xff) << 24)
					+ ((b[offset + 6] & 0xff) << 16)
					+ ((b[offset + 7] & 0xff) << 8)
					+ ((b[offset + 8] & 0xff) << 0);
			long v = (high << 32) + (0xffffffffL & low);
			return v;
		}

		throw new IOException(tag + " - tag not long.");
	}

	public static final double toDouble(byte[] b) throws IOException {
		return toDouble(b, 0);
	}

	public static final double toDouble(byte[] b, int offset)
			throws IOException {
		byte tag = b[offset];
		if (tag == B2Type.DOUBLE_0) {
			return 0;
		} else if (tag == B2Type.DOUBLE_64B) {
			long high = ((b[offset + 1] & 0xff) << 24)
					+ ((b[offset + 2] & 0xff) << 16)
					+ ((b[offset + 3] & 0xff) << 8)
					+ ((b[offset + 4] & 0xff) << 0);
			long low = ((b[offset + 5] & 0xff) << 24)
					+ ((b[offset + 6] & 0xff) << 16)
					+ ((b[offset + 7] & 0xff) << 8)
					+ ((b[offset + 8] & 0xff) << 0);
			long v = (high << 32) + (0xffffffffL & low);
			double ret = Double.longBitsToDouble(v);
			return new Double(ret);
		}

		throw new IOException(tag + " - tag not double.");
	}

	public static final byte[] toBytes(boolean v) {
		if (v) {
			byte[] r2 = { B2Type.BOOLEAN_TRUE };
			return r2;
		}

		byte[] r2 = { B2Type.BOOLEAN_FALSE };
		return r2;
	}

	public static final byte[] toBytes(byte v) {
		if (v == 0) {
			byte[] r2 = { B2Type.BYTE_0 };
			return r2;
		}

		// byte[] r2 = new byte[2];
		// r2[0] = B2Type.BYTE;
		byte[] r2 = { B2Type.BYTE, 1 };
		r2[1] = v;
		return r2;
	}

	public static final byte[] toBytes(short v) {
		if (v == 0) {
			byte[] r2 = { B2Type.SHORT_0 };
			return r2;
		} else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			// byte[] r2 = new byte[2];
			// r2[0] = B2Type.SHORT_8B;
			byte[] r2 = { B2Type.SHORT_8B, 1 };
			r2[1] = (byte) (v & 0xff);
			return r2;
		} else {
			// byte[] r2 = new byte[2];
			// r2[0] = B2Type.SHORT_16B;
			byte[] r2 = { B2Type.SHORT_16B, 1, 2 };
			r2[1] = ((byte) ((v >> 8) & 0xff));
			r2[2] = ((byte) ((v >> 0) & 0xff));
			return r2;
		}
	}

	public static final byte[] toBytes(int v) {
		switch (v) {
		case -1: {
			byte[] r2 = { B2Type.INT_N1 };
			return r2;
		}
		case 0: {
			byte[] r2 = { B2Type.INT_0 };
			return r2;
		}
		case 1: {
			byte[] r2 = { B2Type.INT_1 };
			return r2;
		}
		case 2: {
			byte[] r2 = { B2Type.INT_2 };
			return r2;
		}
		case 3: {
			byte[] r2 = { B2Type.INT_3 };
			return r2;
		}
		case 4: {
			byte[] r2 = { B2Type.INT_4 };
			return r2;
		}
		case 5: {
			byte[] r2 = { B2Type.INT_5 };
			return r2;
		}
		case 6: {
			byte[] r2 = { B2Type.INT_6 };
			return r2;
		}
		case 7: {
			byte[] r2 = { B2Type.INT_7 };
			return r2;
		}
		case 8: {
			byte[] r2 = { B2Type.INT_8 };
			return r2;
		}
		case 9: {
			byte[] r2 = { B2Type.INT_9 };
			return r2;
		}
		case 10: {
			byte[] r2 = { B2Type.INT_10 };
			return r2;
		}
		case 11: {
			byte[] r2 = { B2Type.INT_11 };
			return r2;
		}
		case 12: {
			byte[] r2 = { B2Type.INT_12 };
			return r2;
		}
		case 13: {
			byte[] r2 = { B2Type.INT_13 };
			return r2;
		}
		case 14: {
			byte[] r2 = { B2Type.INT_14 };
			return r2;
		}
		case 15: {
			byte[] r2 = { B2Type.INT_15 };
			return r2;
		}
		case 16: {
			byte[] r2 = { B2Type.INT_16 };
			return r2;
		}
		case 17: {
			byte[] r2 = { B2Type.INT_17 };
			return r2;
		}
		case 18: {
			byte[] r2 = { B2Type.INT_18 };
			return r2;
		}
		case 19: {
			byte[] r2 = { B2Type.INT_19 };
			return r2;
		}
		case 20: {
			byte[] r2 = { B2Type.INT_20 };
			return r2;
		}
		case 21: {
			byte[] r2 = { B2Type.INT_21 };
			return r2;
		}
		case 22: {
			byte[] r2 = { B2Type.INT_22 };
			return r2;
		}
		case 23: {
			byte[] r2 = { B2Type.INT_23 };
			return r2;
		}
		case 24: {
			byte[] r2 = { B2Type.INT_24 };
			return r2;
		}
		case 25: {
			byte[] r2 = { B2Type.INT_25 };
			return r2;
		}
		case 26: {
			byte[] r2 = { B2Type.INT_26 };
			return r2;
		}
		case 27: {
			byte[] r2 = { B2Type.INT_27 };
			return r2;
		}
		case 28: {
			byte[] r2 = { B2Type.INT_28 };
			return r2;
		}
		case 29: {
			byte[] r2 = { B2Type.INT_29 };
			return r2;
		}
		case 30: {
			byte[] r2 = { B2Type.INT_30 };
			return r2;
		}
		case 31: {
			byte[] r2 = { B2Type.INT_31 };
			return r2;
		}
		case 32: {
			byte[] r2 = { B2Type.INT_32 };
			return r2;
		}
		default:
			if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
				// byte[] r2 = new byte[2];
				// r2[0] = B2Type.INT_8B;
				byte[] r2 = { B2Type.INT_8B, 1 };
				r2[1] = (byte) (v & 0xff);
				return r2;
			} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
				// byte[] r2 = new byte[3];
				// r2[0] = B2Type.INT_16B;
				byte[] r2 = { B2Type.INT_16B, 1, 2 };
				r2[1] = (byte) ((v >> 8) & 0xff);
				r2[2] = (byte) ((v >> 0) & 0xff);
				return r2;
			} else {
				// byte[] r2 = new byte[5];
				// r2[0] = B2Type.INT_32B;
				byte[] r2 = { B2Type.INT_32B, 1, 2, 3, 4 };
				r2[1] = (byte) ((v >> 24) & 0xff);
				r2[2] = (byte) ((v >> 16) & 0xff);
				r2[3] = (byte) ((v >> 8) & 0xff);
				r2[4] = (byte) ((v >> 0) & 0xff);
				return r2;
			}
		}
	}

	public static final byte[] toBytes(long v) {
		if (v == 0) {
			byte[] r2 = { B2Type.LONG_0 };
			return r2;
		} else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			// byte[] r2 = new byte[2];
			// r2[0] = B2Type.LONG_8B;
			byte[] r2 = { B2Type.LONG_8B, 1 };
			r2[1] = (byte) (v & 0xff);
			return r2;
		} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
			// byte[] r2 = new byte[3];
			// r2[0] = B2Type.LONG_16B;
			byte[] r2 = { B2Type.LONG_16B, 1, 2 };
			r2[1] = (byte) ((v >> 8) & 0xff);
			r2[2] = (byte) ((v >> 0) & 0xff);
			return r2;
		} else if (v >= NumEx.INT_MIN_VALUE && v <= NumEx.INT_MAX_VALUE) {
			// byte[] r2 = new byte[5];
			// r2[0] = B2Type.LONG_32B;
			byte[] r2 = { B2Type.LONG_32B, 1, 2, 3, 4 };
			r2[1] = (byte) ((v >> 24) & 0xff);
			r2[2] = (byte) ((v >> 16) & 0xff);
			r2[3] = (byte) ((v >> 8) & 0xff);
			r2[4] = (byte) ((v >> 0) & 0xff);
			return r2;
		} else {
			// byte[] r2 = new byte[9];
			// r2[0] = B2Type.LONG_64B;
			byte[] r2 = { B2Type.LONG_64B, 1, 2, 3, 4, 5, 6, 7, 8 };
			r2[1] = (byte) ((v >> 56) & 0xff);
			r2[2] = (byte) ((v >> 48) & 0xff);
			r2[3] = (byte) ((v >> 40) & 0xff);
			r2[4] = (byte) ((v >> 32) & 0xff);
			r2[5] = (byte) ((v >> 24) & 0xff);
			r2[6] = (byte) ((v >> 16) & 0xff);
			r2[7] = (byte) ((v >> 8) & 0xff);
			r2[8] = (byte) ((v >> 0) & 0xff);
			return r2;
		}
	}

	public static final byte[] toBytes(double val) {
		long v = Double.doubleToLongBits(val);
		if (v == 0 || val == 0) {
			byte[] r2 = { B2Type.DOUBLE_0 };
			return r2;
		} else {
			// byte[] r2 = new byte[9];
			// r2[0] = B2Type.DOUBLE_64B;
			byte[] r2 = { B2Type.DOUBLE_64B, 1, 2, 3, 4, 5, 6, 7, 8 };
			r2[1] = (byte) ((v >> 56) & 0xff);
			r2[2] = (byte) ((v >> 48) & 0xff);
			r2[3] = (byte) ((v >> 40) & 0xff);
			r2[4] = (byte) ((v >> 32) & 0xff);
			r2[5] = (byte) ((v >> 24) & 0xff);
			r2[6] = (byte) ((v >> 16) & 0xff);
			r2[7] = (byte) ((v >> 8) & 0xff);
			r2[8] = (byte) ((v >> 0) & 0xff);
			return r2;
		}
	}

	// ////////////////////////////

	public static final byte peek(final InputStream input) throws IOException {
		try {
			input.mark(1);
			return (byte) input.read();
		} finally {
			input.reset();
		}
	}

	public static final byte read(final InputStream input) throws IOException {
		return (byte) input.read();
	}

	public static final int read(final InputStream input, final byte[] result)
			throws IOException {
		return input.read(result);
	}

	public static final byte[] readFully(final InputStream input, final int len)
			throws IOException {
		final int off = 0;
		final byte result[] = new byte[len];
		return readFully(input, result, off, len);
	}

	public static final byte[] readFully(final InputStream input,
			final byte result[]) throws IOException {
		final int off = 0;
		final int len = result.length;
		return readFully(input, result, off, len);
	}

	public static final byte[] readFully(final InputStream input,
			final byte result[], final int off, final int len)
			throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = input.read(result, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
		return result;
	}

	public static final void write(final OutputStream output, final byte b)
			throws IOException {
		output.write(b);
	}

	public static final void write(final OutputStream output, final int b)
			throws IOException {
		output.write(b);
	}

	public static final void write(final OutputStream output, final byte[] b)
			throws IOException {
		write(output, b, 0, b.length);
	}

	public static final void write(final OutputStream output, final byte[] b,
			int offset, int len) throws IOException {
		output.write(b, offset, len);
	}

	public static final void writeNull(final OutputStream output)
			throws IOException {
		write(output, TYPE_NULL);
	}

	public static final boolean readBool(final InputStream input)
			throws IOException {
		int v = read(input);
		if (v == TYPE_BOOLEAN_TRUE)
			return true;
		else if (v == TYPE_BOOLEAN_FALSE)
			return false;

		throw new IOException("unknow type: " + v);
	}

	public static final void writeBool(final OutputStream output,
			final boolean v) throws IOException {
		if (v)
			write(output, TYPE_BOOLEAN_TRUE);
		else
			write(output, TYPE_BOOLEAN_FALSE);
	}

	// ////////////////////////////
	public static final byte readByte(final InputStream input)
			throws IOException {
		int v = read(input);
		if (v == TYPE_BYTE_0)
			return 0;
		else if (v == TYPE_BYTE)
			return (byte) read(input);

		throw new IOException("unknow type: " + v);
	}

	public static final void writeByte(final OutputStream output, final int v)
			throws IOException {
		if (v == 0)
			write(output, TYPE_BYTE_0);
		else {
			write(output, TYPE_BYTE);
			write(output, v);
		}
	}

	// ////////////////////////////
	public static final short readShort(final InputStream input)
			throws IOException {
		int v = read(input);
		if (v == TYPE_SHORT_0)
			return 0;
		else if (v == TYPE_SHORT_8B)
			return (short) read(input);
		else if (v == TYPE_SHORT_16B)
			return (short) (((read(input) & 0xff) << 8) + ((read(input) & 0xff) << 0));

		throw new IOException("unknow type: " + v);
	}

	public static final void writeShort(final OutputStream output, int v)
			throws IOException {
		if (v == 0)
			write(output, TYPE_SHORT_0);
		else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			write(output, TYPE_SHORT_8B);
			write(output, v);
		} else {
			write(output, TYPE_SHORT_16B);
			write(output, ((v >> 8) & 0xff));
			write(output, ((v >> 0) & 0xff));
		}
	}

	// ////////////////////////////
	public static final int readInt(final InputStream input) throws IOException {
		int v = read(input);
		switch (v) {
		case TYPE_INT_N1: {
			return -1;
		}
		case TYPE_INT_0: {
			return 0;
		}
		case TYPE_INT_1: {
			return 1;
		}
		case TYPE_INT_2: {
			return 2;
		}
		case TYPE_INT_3: {
			return 3;
		}
		case TYPE_INT_4: {
			return 4;
		}
		case TYPE_INT_5: {
			return 5;
		}
		case TYPE_INT_6: {
			return 6;
		}
		case TYPE_INT_7: {
			return 7;
		}
		case TYPE_INT_8: {
			return 8;
		}
		case TYPE_INT_9: {
			return 9;
		}
		case TYPE_INT_10: {
			return 10;
		}
		case TYPE_INT_11: {
			return 11;
		}
		case TYPE_INT_12: {
			return 12;
		}
		case TYPE_INT_13: {
			return 13;
		}
		case TYPE_INT_14: {
			return 14;
		}
		case TYPE_INT_15: {
			return 15;
		}
		case TYPE_INT_16: {
			return 16;
		}
		case TYPE_INT_17: {
			return 17;
		}
		case TYPE_INT_18: {
			return 18;
		}
		case TYPE_INT_19: {
			return 19;
		}
		case TYPE_INT_20: {
			return 20;
		}
		case TYPE_INT_21: {
			return 21;
		}
		case TYPE_INT_22: {
			return 22;
		}
		case TYPE_INT_23: {
			return 23;
		}
		case TYPE_INT_24: {
			return 24;
		}
		case TYPE_INT_25: {
			return 25;
		}
		case TYPE_INT_26: {
			return 26;
		}
		case TYPE_INT_27: {
			return 27;
		}
		case TYPE_INT_28: {
			return 28;
		}
		case TYPE_INT_29: {
			return 29;
		}
		case TYPE_INT_30: {
			return 30;
		}
		case TYPE_INT_31: {
			return 31;
		}
		case TYPE_INT_32: {
			return 32;
		}
		case TYPE_INT_8B: {
			return read(input);
		}
		case TYPE_INT_16B: {
			return (short) (((read(input) & 0xff) << 8) + ((read(input) & 0xff) << 0));
		}
		case TYPE_INT_32B: {
			return ((read(input) & 0xff) << 24) + ((read(input) & 0xff) << 16)
					+ ((read(input) & 0xff) << 8) + ((read(input) & 0xff) << 0);
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeInt(final OutputStream output, final int v)
			throws IOException {
		switch (v) {
		case -1:
			write(output, TYPE_INT_N1);
			break;
		case 0:
			write(output, TYPE_INT_0);
			break;
		case 1:
			write(output, TYPE_INT_1);
			break;
		case 2:
			write(output, TYPE_INT_2);
			break;
		case 3:
			write(output, TYPE_INT_3);
			break;
		case 4:
			write(output, TYPE_INT_4);
			break;
		case 5:
			write(output, TYPE_INT_5);
			break;
		case 6:
			write(output, TYPE_INT_6);
			break;
		case 7:
			write(output, TYPE_INT_7);
			break;
		case 8:
			write(output, TYPE_INT_8);
			break;
		case 9:
			write(output, TYPE_INT_9);
			break;
		case 10:
			write(output, TYPE_INT_10);
			break;
		case 11:
			write(output, TYPE_INT_11);
			break;
		case 12:
			write(output, TYPE_INT_12);
			break;
		case 13:
			write(output, TYPE_INT_13);
			break;
		case 14:
			write(output, TYPE_INT_14);
			break;
		case 15:
			write(output, TYPE_INT_15);
			break;
		case 16:
			write(output, TYPE_INT_16);
			break;
		case 17:
			write(output, TYPE_INT_17);
			break;
		case 18:
			write(output, TYPE_INT_18);
			break;
		case 19:
			write(output, TYPE_INT_19);
			break;
		case 20:
			write(output, TYPE_INT_20);
			break;
		case 21:
			write(output, TYPE_INT_21);
			break;
		case 22:
			write(output, TYPE_INT_22);
			break;
		case 23:
			write(output, TYPE_INT_23);
			break;
		case 24:
			write(output, TYPE_INT_24);
			break;
		case 25:
			write(output, TYPE_INT_25);
			break;
		case 26:
			write(output, TYPE_INT_26);
			break;
		case 27:
			write(output, TYPE_INT_27);
			break;
		case 28:
			write(output, TYPE_INT_28);
			break;
		case 29:
			write(output, TYPE_INT_29);
			break;
		case 30:
			write(output, TYPE_INT_30);
			break;
		case 31:
			write(output, TYPE_INT_31);
			break;
		case 32:
			write(output, TYPE_INT_32);
			break;
		default:
			if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
				write(output, TYPE_INT_8B);
				write(output, v);
			} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
				write(output, TYPE_INT_16B);
				write(output, ((v >> 8) & 0xff));
				write(output, ((v >> 0) & 0xff));
			} else {
				write(output, TYPE_INT_32B);
				write(output, ((v >> 24) & 0xff));
				write(output, ((v >> 16) & 0xff));
				write(output, ((v >> 8) & 0xff));
				write(output, ((v >> 0) & 0xff));
			}
			break;
		}
	}

	// ////////////////////////////
	public static final long readLong(final InputStream input)
			throws IOException {
		int v = read(input);
		switch (v) {
		case TYPE_LONG_0: {
		}
			return 0;
		case TYPE_LONG_8B: {
			return read(input);
		}
		case TYPE_LONG_16B: {
			return (((read(input) & 0xff) << 8) + ((read(input) & 0xff) << 0));
		}
		case TYPE_LONG_32B: {
			return ((read(input) & 0xff) << 24) + ((read(input) & 0xff) << 16)
					+ ((read(input) & 0xff) << 8) + ((read(input) & 0xff) << 0);
		}
		case TYPE_LONG_64B: {
			final byte[] b = new byte[8];
			readFully(input, b);
			// for (int i = 0; i < 8; i++) {
			// b[i] = (byte) read(input);
			// }
			long high = ((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16)
					+ ((b[2] & 0xff) << 8) + ((b[3] & 0xff) << 0);
			long low = ((b[4] & 0xff) << 24) + ((b[5] & 0xff) << 16)
					+ ((b[6] & 0xff) << 8) + ((b[7] & 0xff) << 0);
			return (high << 32) + (0xffffffffL & low);
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeLong(final OutputStream output, final long v)
			throws IOException {
		if (v == 0) {
			write(output, TYPE_LONG_0);
		} else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			write(output, TYPE_LONG_8B);
			write(output, (int) v);
		} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
			write(output, TYPE_LONG_16B);
			write(output, (byte) ((v >> 8) & 0xff));
			write(output, (byte) ((v >> 0) & 0xff));
		} else if (v >= NumEx.INT_MIN_VALUE && v <= NumEx.INT_MAX_VALUE) {
			write(output, TYPE_LONG_32B);
			write(output, (byte) ((v >> 24) & 0xff));
			write(output, (byte) ((v >> 16) & 0xff));
			write(output, (byte) ((v >> 8) & 0xff));
			write(output, (byte) ((v >> 0) & 0xff));
		} else {
			write(output, TYPE_LONG_64B);
			write(output, (byte) ((v >> 56) & 0xff));
			write(output, (byte) ((v >> 48) & 0xff));
			write(output, (byte) ((v >> 40) & 0xff));
			write(output, (byte) ((v >> 32) & 0xff));
			write(output, (byte) ((v >> 24) & 0xff));
			write(output, (byte) ((v >> 16) & 0xff));
			write(output, (byte) ((v >> 8) & 0xff));
			write(output, (byte) ((v >> 0) & 0xff));
		}
	}

	// ////////////////////////////
	public static final double readDouble(final InputStream input)
			throws IOException {
		int v = read(input);
		switch (v) {
		case TYPE_DOUBLE_0: {
			return 0.0;
		}
		case TYPE_DOUBLE_64B: {
			final byte[] b = new byte[8];
			readFully(input, b);
			// for (int i = 0; i < 8; i++) {
			// b[i] = (byte) read(input);
			// }
			long high = ((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16)
					+ ((b[2] & 0xff) << 8) + ((b[3] & 0xff) << 0);
			long low = ((b[4] & 0xff) << 24) + ((b[5] & 0xff) << 16)
					+ ((b[6] & 0xff) << 8) + ((b[7] & 0xff) << 0);
			long x = (high << 32) + (0xffffffffL & low);
			return Double.longBitsToDouble(x);
		}
		}

		throw new IOException("unknow type: " + v);
	}

	public static final void writeDouble(final OutputStream output,
			final double var) throws IOException {
		long v = Double.doubleToLongBits(var);
		if (v == 0) {
			write(output, TYPE_DOUBLE_0);
			// } else if (v >= NumEx.Byte_MIN_VALUE && v <=
			// NumEx.Byte_MAX_VALUE) {
			// write(buff, wp,DOUBLE_8B);
			// write(buff, wp,(int) v);
			// } else if (v >= NumEx.Short_MIN_VALUE && v <=
			// NumEx.Short_MAX_VALUE) {
			// write(buff, wp,DOUBLE_16B);
			// write(buff, wp,(byte) ((v >> 8) & 0xff));
			// write(buff, wp,(byte) ((v >> 0) & 0xff));
			// } else if (v >= NumEx.Integer_MIN_VALUE && v <=
			// NumEx.Integer_MAX_VALUE) {
			// write(buff, wp,DOUBLE_32B);
			// write(buff, wp,(byte) ((v >> 24) & 0xff));
			// write(buff, wp,(byte) ((v >> 16) & 0xff));
			// write(buff, wp,(byte) ((v >> 8) & 0xff));
			// write(buff, wp,(byte) ((v >> 0) & 0xff));
		} else {
			write(output, TYPE_DOUBLE_64B);
			write(output, (byte) ((v >> 56) & 0xff));
			write(output, (byte) ((v >> 48) & 0xff));
			write(output, (byte) ((v >> 40) & 0xff));
			write(output, (byte) ((v >> 32) & 0xff));
			write(output, (byte) ((v >> 24) & 0xff));
			write(output, (byte) ((v >> 16) & 0xff));
			write(output, (byte) ((v >> 8) & 0xff));
			write(output, (byte) ((v >> 0) & 0xff));
		}
	}

	public static final String readString(final InputStream input)
			throws IOException {
		int v = read(input);
		switch (v) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_STR_0: {
			return "";
		}
		case TYPE_STR_1: {
			return readStringImpl(input, 1);
		}
		case TYPE_STR_2: {
			return readStringImpl(input, 2);
		}
		case TYPE_STR_3: {
			return readStringImpl(input, 3);
		}
		case TYPE_STR_4: {
			return readStringImpl(input, 4);
		}
		case TYPE_STR_5: {
			return readStringImpl(input, 5);
		}
		case TYPE_STR_6: {
			return readStringImpl(input, 6);
		}
		case TYPE_STR_7: {
			return readStringImpl(input, 7);
		}
		case TYPE_STR_8: {
			return readStringImpl(input, 8);
		}
		case TYPE_STR_9: {
			return readStringImpl(input, 9);
		}
		case TYPE_STR_10: {
			return readStringImpl(input, 10);
		}
		case TYPE_STR_11: {
			return readStringImpl(input, 11);
		}
		case TYPE_STR_12: {
			return readStringImpl(input, 12);
		}
		case TYPE_STR_13: {
			return readStringImpl(input, 13);
		}
		case TYPE_STR_14: {
			return readStringImpl(input, 14);
		}
		case TYPE_STR_15: {
			return readStringImpl(input, 15);
		}
		case TYPE_STR_16: {
			return readStringImpl(input, 16);
		}
		case TYPE_STR_17: {
			return readStringImpl(input, 17);
		}
		case TYPE_STR_18: {
			return readStringImpl(input, 18);
		}
		case TYPE_STR_19: {
			return readStringImpl(input, 19);
		}
		case TYPE_STR_20: {
			return readStringImpl(input, 20);
		}
		case TYPE_STR_21: {
			return readStringImpl(input, 21);
		}
		case TYPE_STR_22: {
			return readStringImpl(input, 22);
		}
		case TYPE_STR_23: {
			return readStringImpl(input, 23);
		}
		case TYPE_STR_24: {
			return readStringImpl(input, 24);
		}
		case TYPE_STR_25: {
			return readStringImpl(input, 25);
		}
		case TYPE_STR_26: {
			return readStringImpl(input, 26);
		}
		case TYPE_STR: {
			int len = readInt(input);
			return readStringImpl(input, len);
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeString(final OutputStream output,
			final String v) throws IOException {
		if (v == null) {
			writeNull(output);
		} else {
			byte[] b = v.getBytes(UTF8);
			int len = b.length;
			switch (len) {
			case 0:
				write(output, TYPE_STR_0);
				break;
			case 1:
				write(output, TYPE_STR_1);
				printString(output, b);
				break;
			case 2:
				write(output, TYPE_STR_2);
				printString(output, b);
				break;
			case 3:
				write(output, TYPE_STR_3);
				printString(output, b);
				break;
			case 4:
				write(output, TYPE_STR_4);
				printString(output, b);
				break;
			case 5:
				write(output, TYPE_STR_5);
				printString(output, b);
				break;
			case 6:
				write(output, TYPE_STR_6);
				printString(output, b);
				break;
			case 7:
				write(output, TYPE_STR_7);
				printString(output, b);
				break;
			case 8:
				write(output, TYPE_STR_8);
				printString(output, b);
				break;
			case 9:
				write(output, TYPE_STR_9);
				printString(output, b);
				break;
			case 10:
				write(output, TYPE_STR_10);
				printString(output, b);
				break;
			case 11:
				write(output, TYPE_STR_11);
				printString(output, b);
				break;
			case 12:
				write(output, TYPE_STR_12);
				printString(output, b);
				break;
			case 13:
				write(output, TYPE_STR_13);
				printString(output, b);
				break;
			case 14:
				write(output, TYPE_STR_14);
				printString(output, b);
				break;
			case 15:
				write(output, TYPE_STR_15);
				printString(output, b);
				break;
			case 16:
				write(output, TYPE_STR_16);
				printString(output, b);
				break;
			case 17:
				write(output, TYPE_STR_17);
				printString(output, b);
				break;
			case 18:
				write(output, TYPE_STR_18);
				printString(output, b);
				break;
			case 19:
				write(output, TYPE_STR_19);
				printString(output, b);
				break;
			case 20:
				write(output, TYPE_STR_20);
				printString(output, b);
				break;
			case 21:
				write(output, TYPE_STR_21);
				printString(output, b);
				break;
			case 22:
				write(output, TYPE_STR_22);
				printString(output, b);
				break;
			case 23:
				write(output, TYPE_STR_23);
				printString(output, b);
				break;
			case 24:
				write(output, TYPE_STR_24);
				printString(output, b);
				break;
			case 25:
				write(output, TYPE_STR_25);
				printString(output, b);
				break;
			case 26:
				write(output, TYPE_STR_26);
				printString(output, b);
				break;
			default:
				write(output, TYPE_STR);
				writeInt(output, len);
				printString(output, b);
				break;
			}
		}
	}

	public static final Date readDate(final InputStream input)
			throws IOException {
		int v = read(input);
		if (v == TYPE_NULL)
			return null;
		else if (v == TYPE_JAVA_DATE) {
			final byte[] b = new byte[8];
			readFully(input, b);
			// for (int i = 0; i < 8; i++) {
			// b[i] = (byte) read(input);
			// }
			long high = ((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16)
					+ ((b[2] & 0xff) << 8) + ((b[3] & 0xff) << 0);
			long low = ((b[4] & 0xff) << 24) + ((b[5] & 0xff) << 16)
					+ ((b[6] & 0xff) << 8) + ((b[7] & 0xff) << 0);
			long x = (high << 32) + (0xffffffffL & low);
			return new java.util.Date(x);
		}

		throw new IOException("unknow type: " + v);
	}

	public static final void writeDate(final OutputStream output,
			final java.sql.Date dat) throws IOException {
		writeDate(output, new java.util.Date(dat.getTime()));
	}

	public static final void writeDate(final OutputStream output,
			final java.sql.Timestamp dat) throws IOException {
		writeDate(output, new java.util.Date(dat.getTime()));
	}

	public static final void writeDate(final OutputStream output,
			final java.sql.Time dat) throws IOException {
		writeDate(output, new java.util.Date(dat.getTime()));
	}

	public static final void writeDate(final OutputStream output,
			final java.util.Date dat) throws IOException {
		if (dat == null) {
			writeNull(output);
		} else {
			long v = dat.getTime();
			write(output, TYPE_JAVA_DATE);
			write(output, (byte) ((v >> 56) & 0xff));
			write(output, (byte) ((v >> 48) & 0xff));
			write(output, (byte) ((v >> 40) & 0xff));
			write(output, (byte) ((v >> 32) & 0xff));
			write(output, (byte) ((v >> 24) & 0xff));
			write(output, (byte) ((v >> 16) & 0xff));
			write(output, (byte) ((v >> 8) & 0xff));
			write(output, (byte) ((v >> 0) & 0xff));
		}
	}

	public static final byte[] readBytes(final InputStream input)
			throws IOException {
		int v = read(input);
		switch (v) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_BYTES_0: {
			return new byte[0];
		}
		case TYPE_BYTES: {
			int len = readInt(input);
			byte[] b = new byte[len];
			readFully(input, b);
			return b;
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeBytes(final OutputStream output,
			final byte[] v) throws IOException {
		if (v == null) {
			writeNull(output);
		} else {
			int len = v.length;
			if (len == 0) {
				write(output, TYPE_BYTES_0);
			} else {
				write(output, TYPE_BYTES);
				writeInt(output, len);
				write(output, v);
			}
		}
	}

	public static final List readList(final InputStream input, final Object o1)
			throws Exception {
		int v = read(input);
		switch (v) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_VECTOR_0: {
			return new NewList();
		}
		case TYPE_VECTOR_1: {
			return readList(input, 1, o1);
		}
		case TYPE_VECTOR_2: {
			return readList(input, 2, o1);
		}
		case TYPE_VECTOR_3: {
			return readList(input, 3, o1);
		}
		case TYPE_VECTOR_4: {
			return readList(input, 4, o1);
		}
		case TYPE_VECTOR_5: {
			return readList(input, 5, o1);
		}
		case TYPE_VECTOR_6: {
			return readList(input, 6, o1);
		}
		case TYPE_VECTOR_7: {
			return readList(input, 7, o1);
		}
		case TYPE_VECTOR_8: {
			return readList(input, 8, o1);
		}
		case TYPE_VECTOR_9: {
			return readList(input, 9, o1);
		}
		case TYPE_VECTOR_10: {
			return readList(input, 10, o1);
		}
		case TYPE_VECTOR_11: {
			return readList(input, 11, o1);
		}
		case TYPE_VECTOR_12: {
			return readList(input, 12, o1);
		}
		case TYPE_VECTOR_13: {
			return readList(input, 13, o1);
		}
		case TYPE_VECTOR_14: {
			return readList(input, 14, o1);
		}
		case TYPE_VECTOR_15: {
			return readList(input, 15, o1);
		}
		case TYPE_VECTOR_16: {
			return readList(input, 16, o1);
		}
		case TYPE_VECTOR_17: {
			return readList(input, 17, o1);
		}
		case TYPE_VECTOR_18: {
			return readList(input, 18, o1);
		}
		case TYPE_VECTOR_19: {
			return readList(input, 19, o1);
		}
		case TYPE_VECTOR_20: {
			return readList(input, 20, o1);
		}
		case TYPE_VECTOR_21: {
			return readList(input, 21, o1);
		}
		case TYPE_VECTOR_22: {
			return readList(input, 22, o1);
		}
		case TYPE_VECTOR_23: {
			return readList(input, 23, o1);
		}
		case TYPE_VECTOR_24: {
			return readList(input, 24, o1);
		}
		case TYPE_VECTOR: {
			int len = readInt(input);
			return readList(input, len, o1);
		}
		}

		throw new IOException("unknow type: " + v);
	}

	private static final List readList(final InputStream input, final int len,
			final Object o1) throws Exception {
		List ret = new ArrayList(len);
		for (int i = 0; i < len; i++) {
			// obj.readObject(input);
			Object obj = readObj(input, o1);
			ret.add(obj);
		}
		return ret;
	}

	public static final void writeList(final OutputStream output, final List v)
			throws Exception {
		if (v == null) {
			writeNull(output);
		} else {
			int len = v.size();
			switch (len) {
			case 0:
				write(output, TYPE_VECTOR_0);
				break;
			case 1:
				write(output, TYPE_VECTOR_1);
				break;
			case 2:
				write(output, TYPE_VECTOR_2);
				break;
			case 3:
				write(output, TYPE_VECTOR_3);
				break;
			case 4:
				write(output, TYPE_VECTOR_4);
				break;
			case 5:
				write(output, TYPE_VECTOR_5);
				break;
			case 6:
				write(output, TYPE_VECTOR_6);
				break;
			case 7:
				write(output, TYPE_VECTOR_7);
				break;
			case 8:
				write(output, TYPE_VECTOR_8);
				break;
			case 9:
				write(output, TYPE_VECTOR_9);
				break;
			case 10:
				write(output, TYPE_VECTOR_10);
				break;
			case 11:
				write(output, TYPE_VECTOR_11);
				break;
			case 12:
				write(output, TYPE_VECTOR_12);
				break;
			case 13:
				write(output, TYPE_VECTOR_13);
				break;
			case 14:
				write(output, TYPE_VECTOR_14);
				break;
			case 15:
				write(output, TYPE_VECTOR_15);
				break;
			case 16:
				write(output, TYPE_VECTOR_16);
				break;
			case 17:
				write(output, TYPE_VECTOR_17);
				break;
			case 18:
				write(output, TYPE_VECTOR_18);
				break;
			case 19:
				write(output, TYPE_VECTOR_19);
				break;
			case 20:
				write(output, TYPE_VECTOR_20);
				break;
			case 21:
				write(output, TYPE_VECTOR_21);
				break;
			case 22:
				write(output, TYPE_VECTOR_22);
				break;
			case 23:
				write(output, TYPE_VECTOR_23);
				break;
			case 24:
				write(output, TYPE_VECTOR_24);
				break;
			default:
				write(output, TYPE_VECTOR);
				writeInt(output, len);
				break;
			}
			for (int i = 0; i < len; i++) {
				Object obj = v.get(i);
				writeObj(output, obj);
			}
		}
	}

	public static final Map readMap(final InputStream input,
			final Object kType, final Object vType) throws Exception {
		byte tag = read(input);
		switch (tag) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_HASHTABLE_0: {
			return new HashMap();
		}
		case TYPE_HASHTABLE_1: {
			return readMap(input, 1, kType, vType);
		}
		case TYPE_HASHTABLE_2: {
			return readMap(input, 2, kType, vType);
		}
		case TYPE_HASHTABLE_3: {
			return readMap(input, 3, kType, vType);
		}
		case TYPE_HASHTABLE_4: {
			return readMap(input, 4, kType, vType);
		}
		case TYPE_HASHTABLE_5: {
			return readMap(input, 5, kType, vType);
		}
		case TYPE_HASHTABLE_6: {
			return readMap(input, 6, kType, vType);
		}
		case TYPE_HASHTABLE_7: {
			return readMap(input, 7, kType, vType);
		}
		case TYPE_HASHTABLE_8: {
			return readMap(input, 8, kType, vType);
		}
		case TYPE_HASHTABLE_9: {
			return readMap(input, 9, kType, vType);
		}
		case TYPE_HASHTABLE_10: {
			return readMap(input, 10, kType, vType);
		}
		case TYPE_HASHTABLE_11: {
			return readMap(input, 11, kType, vType);
		}
		case TYPE_HASHTABLE_12: {
			return readMap(input, 12, kType, vType);
		}
		case TYPE_HASHTABLE_13: {
			return readMap(input, 13, kType, vType);
		}
		case TYPE_HASHTABLE_14: {
			return readMap(input, 14, kType, vType);
		}
		case TYPE_HASHTABLE_15: {
			return readMap(input, 15, kType, vType);
		}
		case TYPE_HASHTABLE: {
			int len = readInt(input);
			return readMap(input, len, kType, vType);
		}
		}
		throw new IOException("unknow type: " + tag);
	}

	private static final Map readMap(final InputStream input, final int len,
			final Object kType, final Object vType) throws Exception {
		Map ret = new HashMap();
		for (int i = 0; i < len; i++) {
			Object key = readObj(input, kType);
			Object var = readObj(input, vType);
			ret.put(key, var);
		}
		return ret;
	}

	public static final void writeMapTag(OutputStream os, int len)
			throws Exception {
		switch (len) {
		case 0:
			os.write(TYPE_HASHTABLE_0);
			break;
		case 1: {
			os.write(TYPE_HASHTABLE_1);
			break;
		}
		case 2: {
			os.write(TYPE_HASHTABLE_2);
			break;
		}
		case 3: {
			os.write(TYPE_HASHTABLE_3);
			break;
		}
		case 4: {
			os.write(TYPE_HASHTABLE_4);
			break;
		}
		case 5: {
			os.write(TYPE_HASHTABLE_5);
			break;
		}
		case 6: {
			os.write(TYPE_HASHTABLE_6);
			break;
		}
		case 7: {
			os.write(TYPE_HASHTABLE_7);
			break;
		}
		case 8: {
			os.write(TYPE_HASHTABLE_8);
			break;
		}
		case 9: {
			os.write(TYPE_HASHTABLE_9);
			break;
		}
		case 10: {
			os.write(TYPE_HASHTABLE_10);
			break;
		}
		case 11: {
			os.write(TYPE_HASHTABLE_11);
			break;
		}
		case 12: {
			os.write(TYPE_HASHTABLE_12);
			break;
		}
		case 13: {
			os.write(TYPE_HASHTABLE_13);
			break;
		}
		case 14: {
			os.write(TYPE_HASHTABLE_14);
			break;
		}
		case 15: {
			os.write(TYPE_HASHTABLE_15);
			break;
		}
		default:
			os.write(TYPE_HASHTABLE);
			writeInt(os, len);
			break;
		}
	}

	public static final void writeMapEntry(OutputStream os, Object key,
			Object var) throws Exception {
		writeObj(os, key);
		writeObj(os, var);
	}

	public static final void writeMap(final OutputStream output, final Map v)
			throws Exception {
		if (v == null) {
			writeNull(output);
		} else {
			int len = v.size();
			switch (len) {
			case 0:
				write(output, TYPE_HASHTABLE_0);
				break;
			case 1: {
				write(output, TYPE_HASHTABLE_1);
				break;
			}
			case 2: {
				write(output, TYPE_HASHTABLE_2);
				break;
			}
			case 3: {
				write(output, TYPE_HASHTABLE_3);
				break;
			}
			case 4: {
				write(output, TYPE_HASHTABLE_4);
				break;
			}
			case 5: {
				write(output, TYPE_HASHTABLE_5);
				break;
			}
			case 6: {
				write(output, TYPE_HASHTABLE_6);
				break;
			}
			case 7: {
				write(output, TYPE_HASHTABLE_7);
				break;
			}
			case 8: {
				write(output, TYPE_HASHTABLE_8);
				break;
			}
			case 9: {
				write(output, TYPE_HASHTABLE_9);
				break;
			}
			case 10: {
				write(output, TYPE_HASHTABLE_10);
				break;
			}
			case 11: {
				write(output, TYPE_HASHTABLE_11);
				break;
			}
			case 12: {
				write(output, TYPE_HASHTABLE_12);
				break;
			}
			case 13: {
				write(output, TYPE_HASHTABLE_13);
				break;
			}
			case 14: {
				write(output, TYPE_HASHTABLE_14);
				break;
			}
			case 15: {
				write(output, TYPE_HASHTABLE_15);
				break;
			}
			default:
				write(output, TYPE_HASHTABLE);
				writeInt(output, len);
				break;
			}

			Set<Entry> entrys = v.entrySet();
			for (Entry e : entrys) {
				Object key = e.getKey();
				Object var = e.getValue();
				writeObj(output, key);
				writeObj(output, var);
			}
		}
	}

	protected static final String readStringImpl(final InputStream input,
			final int length) throws IOException {
		if (length <= 0)
			return "";

		byte[] b = new byte[length];
		readFully(input, b);

		return new String(b, UTF8);
	}

	protected static final void printString(final OutputStream output,
			final byte[] v) throws IOException {
		write(output, v);
	}

	// ///////////////////////////
	private static final Object readObj(final InputStream input, final Object o1)
			throws Exception {
		if (o1 instanceof Integer) {
			return readInt(input);
		} else if (o1 instanceof String) {
			return readString(input);
		} else if (o1 instanceof Boolean) {
			return readBool(input);
		} else if (o1 instanceof Byte) {
			return readByte(input);
		} else if (o1 instanceof byte[]) {
			return readBytes(input);
		} else if (o1 instanceof Short) {
			return readShort(input);
		} else if (o1 instanceof Long) {
			return readLong(input);
		} else if (o1 instanceof java.util.Date) {
			return readDate(input);
		} else if (o1 instanceof Double) {
			return readDouble(input);
		} else if (o1 instanceof List) {
			return readList(input, o1);
		} else if (o1 instanceof B2X) {
			B2X obj = (B2X) o1.getClass().newInstance();
			obj.readObject(input);
			return obj;
		} else if (o1 instanceof int[]) {
			return B2InputStream.readObject(input);
		} else if (o1 instanceof int[][]) {
			return B2InputStream.readObject(input);
		} else {
			throw new IOException("unknow tag error:" + o1);
		}
	}

	private static final void writeObj(final OutputStream output,
			final Object object) throws Exception {
		if (object == null) {
			writeNull(output);
		} else if (object instanceof Integer) {
			int v = ((Integer) object).intValue();
			writeInt(output, v);
		} else if (object instanceof String) {
			String v = (String) object;
			writeString(output, v);
		} else if (object instanceof Boolean) {
			boolean v = ((Boolean) object).booleanValue();
			writeBool(output, v);
		} else if (object instanceof Byte) {
			int v = ((Byte) object).byteValue();
			writeByte(output, v);
		} else if (object instanceof byte[]) {
			byte[] v = (byte[]) object;
			writeBytes(output, v);
		} else if (object instanceof List) {
			List v = (List) object;
			writeList(output, v);
		} else if (object instanceof Map) {
			Map v = (Map) object;
			writeMap(output, v);
		} else if (object instanceof Short) {
			int v = ((Short) object).shortValue();
			writeShort(output, v);
		} else if (object instanceof Long) {
			long v = ((Long) object).longValue();
			writeLong(output, v);
		} else if (object instanceof Double) {
			double v = ((Double) object).doubleValue();
			writeDouble(output, v);
		} else if (object instanceof java.util.Date) {
			java.util.Date v = (java.util.Date) object;
			writeDate(output, v);
		} else if (object instanceof java.sql.Date) {
			java.sql.Date v = (java.sql.Date) object;
			writeDate(output, new java.util.Date(v.getTime()));
		} else if (object instanceof java.sql.Timestamp) {
			java.sql.Timestamp v = (java.sql.Timestamp) object;
			writeDate(output, new java.util.Date(v.getTime()));
		} else if (object instanceof java.sql.Time) {
			java.sql.Time v = (java.sql.Time) object;
			writeDate(output, new java.util.Date(v.getTime()));
		} else if (object instanceof B2X) {
			B2X v = (B2X) object;
			v.writeObject(output);
		} else if (object instanceof int[]) {
			int[] v = (int[]) object;
			B2OutputStream.writeIntArray(output, v);
		} else if (object instanceof int[][]) {
			int[][] v = (int[][]) object;
			B2OutputStream.writeInt2DArray(output, v);
		} else {
			throw new IOException("unsupported object:" + object);
		}
	}

	// ///////////////////////////

	public void writeObject(final OutputStream output) throws Exception {
	}

	public void readObject(final InputStream input) throws Exception {
	}

	// ///////////////////////////

	// ////////////////////////////
	// ///////////////////////
	public static final byte read(final byte[] buff, final Ref<Integer> rp)
			throws IOException {
		return buff[rp.val++];
	}

	public static final int read(final byte[] buff, final Ref<Integer> rp,
			final byte[] result) throws IOException {
		for (int n = 0; n < result.length; n++) {
			result[n] = read(buff, rp);
		}
		return result.length;
	}

	public static final void readFully(final byte[] buff,
			final Ref<Integer> rp, final int len) throws IOException {
		final int off = 0;
		final byte result[] = new byte[len];
		readFully(buff, rp, result, off, len);
	}

	public static final void readFully(final byte[] buff,
			final Ref<Integer> rp, final byte result[]) throws IOException {
		final int off = 0;
		final int len = result.length;
		readFully(buff, rp, result, off, len);
	}

	public static final void readFully(final byte[] buff,
			final Ref<Integer> rp, final byte result[], final int off,
			final int len) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		for (int n = 0; n < len; n++) {
			result[off + n] = read(buff, rp);
		}
	}

	public static final void write(final byte[] buff, final Ref<Integer> wp,
			final byte b) throws IOException {
		buff[wp.val++] = b;
	}

	public static final void write(final byte[] buff, final Ref<Integer> wp,
			final byte[] b) throws IOException {
		for (byte t : b) {
			write(buff, wp, t);
		}
	}

	public static final void write(final byte[] buff, final Ref<Integer> wp,
			final int b) throws IOException {
		write(buff, wp, (byte) b);
	}

	public static final void writeNull(final byte[] buff, final Ref<Integer> wp)
			throws IOException {
		write(buff, wp, TYPE_NULL);
	}

	// ////////////////////////////
	public static final boolean readBool(final byte[] buff,
			final Ref<Integer> rp) throws IOException {
		int v = read(buff, rp);
		if (v == TYPE_BOOLEAN_TRUE)
			return true;
		else if (v == TYPE_BOOLEAN_FALSE)
			return false;

		throw new IOException("unknow type: " + v);
	}

	public static final void writeBool(final byte[] buff,
			final Ref<Integer> wp, final boolean v) throws IOException {
		if (v)
			write(buff, wp, TYPE_BOOLEAN_TRUE);
		else
			write(buff, wp, TYPE_BOOLEAN_FALSE);
	}

	// ////////////////////////////
	public static final byte readByte(final byte[] buff, final Ref<Integer> rp)
			throws IOException {
		int v = read(buff, rp);
		if (v == TYPE_BYTE_0)
			return 0;
		else if (v == TYPE_BYTE)
			return read(buff, rp);

		throw new IOException("unknow type: " + v);
	}

	public static final void writeByte(final byte[] buff,
			final Ref<Integer> wp, final int v) throws IOException {
		if (v == 0)
			write(buff, wp, TYPE_BYTE_0);
		else {
			write(buff, wp, TYPE_BYTE);
			write(buff, wp, v);
		}
	}

	// ////////////////////////////
	public static final short readShort(final byte[] buff, final Ref<Integer> rp)
			throws IOException {
		int v = read(buff, rp);
		if (v == TYPE_SHORT_0)
			return 0;
		else if (v == TYPE_SHORT_8B)
			return (short) read(buff, rp);
		else if (v == TYPE_SHORT_16B)
			return (short) (((read(buff, rp) & 0xff) << 8) + ((read(buff, rp) & 0xff) << 0));

		throw new IOException("unknow type: " + v);
	}

	public static final void writeShort(final byte[] buff,
			final Ref<Integer> wp, int v) throws IOException {
		if (v == 0)
			write(buff, wp, TYPE_SHORT_0);
		else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			write(buff, wp, TYPE_SHORT_8B);
			write(buff, wp, v);
		} else {
			write(buff, wp, TYPE_SHORT_16B);
			write(buff, wp, ((v >> 8) & 0xff));
			write(buff, wp, ((v >> 0) & 0xff));
		}
	}

	// ////////////////////////////
	public static final int readInt(final byte[] buff, final Ref<Integer> rp)
			throws IOException {
		int v = read(buff, rp);
		switch (v) {
		case TYPE_INT_N1: {
			return -1;
		}
		case TYPE_INT_0: {
			return 0;
		}
		case TYPE_INT_1: {
			return 1;
		}
		case TYPE_INT_2: {
			return 2;
		}
		case TYPE_INT_3: {
			return 3;
		}
		case TYPE_INT_4: {
			return 4;
		}
		case TYPE_INT_5: {
			return 5;
		}
		case TYPE_INT_6: {
			return 6;
		}
		case TYPE_INT_7: {
			return 7;
		}
		case TYPE_INT_8: {
			return 8;
		}
		case TYPE_INT_9: {
			return 9;
		}
		case TYPE_INT_10: {
			return 10;
		}
		case TYPE_INT_11: {
			return 11;
		}
		case TYPE_INT_12: {
			return 12;
		}
		case TYPE_INT_13: {
			return 13;
		}
		case TYPE_INT_14: {
			return 14;
		}
		case TYPE_INT_15: {
			return 15;
		}
		case TYPE_INT_16: {
			return 16;
		}
		case TYPE_INT_17: {
			return 17;
		}
		case TYPE_INT_18: {
			return 18;
		}
		case TYPE_INT_19: {
			return 19;
		}
		case TYPE_INT_20: {
			return 20;
		}
		case TYPE_INT_21: {
			return 21;
		}
		case TYPE_INT_22: {
			return 22;
		}
		case TYPE_INT_23: {
			return 23;
		}
		case TYPE_INT_24: {
			return 24;
		}
		case TYPE_INT_25: {
			return 25;
		}
		case TYPE_INT_26: {
			return 26;
		}
		case TYPE_INT_27: {
			return 27;
		}
		case TYPE_INT_28: {
			return 28;
		}
		case TYPE_INT_29: {
			return 29;
		}
		case TYPE_INT_30: {
			return 30;
		}
		case TYPE_INT_31: {
			return 31;
		}
		case TYPE_INT_32: {
			return 32;
		}
		case TYPE_INT_8B: {
			return read(buff, rp);
		}
		case TYPE_INT_16B: {
			return (short) (((read(buff, rp) & 0xff) << 8) + ((read(buff, rp) & 0xff) << 0));
		}
		case TYPE_INT_32B: {
			return ((read(buff, rp) & 0xff) << 24)
					+ ((read(buff, rp) & 0xff) << 16)
					+ ((read(buff, rp) & 0xff) << 8)
					+ ((read(buff, rp) & 0xff) << 0);
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeInt(final byte[] buff, final Ref<Integer> wp,
			final int v) throws IOException {
		switch (v) {
		case -1:
			write(buff, wp, TYPE_INT_N1);
			break;
		case 0:
			write(buff, wp, TYPE_INT_0);
			break;
		case 1:
			write(buff, wp, TYPE_INT_1);
			break;
		case 2:
			write(buff, wp, TYPE_INT_2);
			break;
		case 3:
			write(buff, wp, TYPE_INT_3);
			break;
		case 4:
			write(buff, wp, TYPE_INT_4);
			break;
		case 5:
			write(buff, wp, TYPE_INT_5);
			break;
		case 6:
			write(buff, wp, TYPE_INT_6);
			break;
		case 7:
			write(buff, wp, TYPE_INT_7);
			break;
		case 8:
			write(buff, wp, TYPE_INT_8);
			break;
		case 9:
			write(buff, wp, TYPE_INT_9);
			break;
		case 10:
			write(buff, wp, TYPE_INT_10);
			break;
		case 11:
			write(buff, wp, TYPE_INT_11);
			break;
		case 12:
			write(buff, wp, TYPE_INT_12);
			break;
		case 13:
			write(buff, wp, TYPE_INT_13);
			break;
		case 14:
			write(buff, wp, TYPE_INT_14);
			break;
		case 15:
			write(buff, wp, TYPE_INT_15);
			break;
		case 16:
			write(buff, wp, TYPE_INT_16);
			break;
		case 17:
			write(buff, wp, TYPE_INT_17);
			break;
		case 18:
			write(buff, wp, TYPE_INT_18);
			break;
		case 19:
			write(buff, wp, TYPE_INT_19);
			break;
		case 20:
			write(buff, wp, TYPE_INT_20);
			break;
		case 21:
			write(buff, wp, TYPE_INT_21);
			break;
		case 22:
			write(buff, wp, TYPE_INT_22);
			break;
		case 23:
			write(buff, wp, TYPE_INT_23);
			break;
		case 24:
			write(buff, wp, TYPE_INT_24);
			break;
		case 25:
			write(buff, wp, TYPE_INT_25);
			break;
		case 26:
			write(buff, wp, TYPE_INT_26);
			break;
		case 27:
			write(buff, wp, TYPE_INT_27);
			break;
		case 28:
			write(buff, wp, TYPE_INT_28);
			break;
		case 29:
			write(buff, wp, TYPE_INT_29);
			break;
		case 30:
			write(buff, wp, TYPE_INT_30);
			break;
		case 31:
			write(buff, wp, TYPE_INT_31);
			break;
		case 32:
			write(buff, wp, TYPE_INT_32);
			break;
		default:
			if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
				write(buff, wp, TYPE_INT_8B);
				write(buff, wp, v);
			} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
				write(buff, wp, TYPE_INT_16B);
				write(buff, wp, ((v >> 8) & 0xff));
				write(buff, wp, ((v >> 0) & 0xff));
			} else {
				write(buff, wp, TYPE_INT_32B);
				write(buff, wp, ((v >> 24) & 0xff));
				write(buff, wp, ((v >> 16) & 0xff));
				write(buff, wp, ((v >> 8) & 0xff));
				write(buff, wp, ((v >> 0) & 0xff));
			}
			break;
		}
	}

	// ////////////////////////////
	public static final long readLong(final byte[] buff, final Ref<Integer> rp)
			throws IOException {
		int v = read(buff, rp);
		switch (v) {
		case TYPE_LONG_0: {
		}
			return 0;
		case TYPE_LONG_8B: {
			return read(buff, rp);
		}
		case TYPE_LONG_16B: {
			return (((read(buff, rp) & 0xff) << 8) + ((read(buff, rp) & 0xff) << 0));
		}
		case TYPE_LONG_32B: {
			return ((read(buff, rp) & 0xff) << 24)
					+ ((read(buff, rp) & 0xff) << 16)
					+ ((read(buff, rp) & 0xff) << 8)
					+ ((read(buff, rp) & 0xff) << 0);
		}
		case TYPE_LONG_64B: {
			final byte[] b = new byte[8];
			readFully(buff, rp, b);
			// for (int i = 0; i < 8; i++) {
			// b[i] = read(buff, rp);
			// }
			long high = ((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16)
					+ ((b[2] & 0xff) << 8) + ((b[3] & 0xff) << 0);
			long low = ((b[4] & 0xff) << 24) + ((b[5] & 0xff) << 16)
					+ ((b[6] & 0xff) << 8) + ((b[7] & 0xff) << 0);
			return (high << 32) + (0xffffffffL & low);
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeLong(final byte[] buff,
			final Ref<Integer> wp, final long v) throws IOException {
		if (v == 0) {
			write(buff, wp, TYPE_LONG_0);
		} else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			write(buff, wp, TYPE_LONG_8B);
			write(buff, wp, (int) v);
		} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
			write(buff, wp, TYPE_LONG_16B);
			write(buff, wp, (byte) ((v >> 8) & 0xff));
			write(buff, wp, (byte) ((v >> 0) & 0xff));
		} else if (v >= NumEx.INT_MIN_VALUE && v <= NumEx.INT_MAX_VALUE) {
			write(buff, wp, TYPE_LONG_32B);
			write(buff, wp, (byte) ((v >> 24) & 0xff));
			write(buff, wp, (byte) ((v >> 16) & 0xff));
			write(buff, wp, (byte) ((v >> 8) & 0xff));
			write(buff, wp, (byte) ((v >> 0) & 0xff));
		} else {
			write(buff, wp, TYPE_LONG_64B);
			write(buff, wp, (byte) ((v >> 56) & 0xff));
			write(buff, wp, (byte) ((v >> 48) & 0xff));
			write(buff, wp, (byte) ((v >> 40) & 0xff));
			write(buff, wp, (byte) ((v >> 32) & 0xff));
			write(buff, wp, (byte) ((v >> 24) & 0xff));
			write(buff, wp, (byte) ((v >> 16) & 0xff));
			write(buff, wp, (byte) ((v >> 8) & 0xff));
			write(buff, wp, (byte) ((v >> 0) & 0xff));
		}
	}

	// ////////////////////////////
	public static final double readDouble(final byte[] buff,
			final Ref<Integer> rp) throws IOException {
		int v = read(buff, rp);
		switch (v) {
		case TYPE_DOUBLE_0: {
			return 0.0;
		}
		case TYPE_DOUBLE_64B: {
			final byte[] b = new byte[8];
			readFully(buff, rp, b);
			// for (int i = 0; i < 8; i++) {
			// b[i] = read(buff, rp);
			// }
			long high = ((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16)
					+ ((b[2] & 0xff) << 8) + ((b[3] & 0xff) << 0);
			long low = ((b[4] & 0xff) << 24) + ((b[5] & 0xff) << 16)
					+ ((b[6] & 0xff) << 8) + ((b[7] & 0xff) << 0);
			long x = (high << 32) + (0xffffffffL & low);
			return Double.longBitsToDouble(x);
		}
		}

		throw new IOException("unknow type: " + v);
	}

	public static final void writeDouble(final byte[] buff,
			final Ref<Integer> wp, final double var) throws IOException {
		long v = Double.doubleToLongBits(var);
		if (v == 0) {
			write(buff, wp, TYPE_DOUBLE_0);
			// } else if (v >= NumEx.Byte_MIN_VALUE && v <=
			// NumEx.Byte_MAX_VALUE) {
			// write(buff, wp,DOUBLE_8B);
			// write(buff, wp,(int) v);
			// } else if (v >= NumEx.Short_MIN_VALUE && v <=
			// NumEx.Short_MAX_VALUE) {
			// write(buff, wp,DOUBLE_16B);
			// write(buff, wp,(byte) ((v >> 8) & 0xff));
			// write(buff, wp,(byte) ((v >> 0) & 0xff));
			// } else if (v >= NumEx.Integer_MIN_VALUE && v <=
			// NumEx.Integer_NumEx.MAX_VALUE) {
			// write(buff, wp,DOUBLE_32B);
			// write(buff, wp,(byte) ((v >> 24) & 0xff));
			// write(buff, wp,(byte) ((v >> 16) & 0xff));
			// write(buff, wp,(byte) ((v >> 8) & 0xff));
			// write(buff, wp,(byte) ((v >> 0) & 0xff));
		} else {
			write(buff, wp, TYPE_DOUBLE_64B);
			write(buff, wp, (byte) ((v >> 56) & 0xff));
			write(buff, wp, (byte) ((v >> 48) & 0xff));
			write(buff, wp, (byte) ((v >> 40) & 0xff));
			write(buff, wp, (byte) ((v >> 32) & 0xff));
			write(buff, wp, (byte) ((v >> 24) & 0xff));
			write(buff, wp, (byte) ((v >> 16) & 0xff));
			write(buff, wp, (byte) ((v >> 8) & 0xff));
			write(buff, wp, (byte) ((v >> 0) & 0xff));
		}
	}

	public static final String readString(final byte[] buff,
			final Ref<Integer> rp) throws IOException {
		int v = read(buff, rp);
		switch (v) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_STR_0: {
			return "";
		}
		case TYPE_STR_1: {
			return readStringImpl(buff, rp, 1);
		}
		case TYPE_STR_2: {
			return readStringImpl(buff, rp, 2);
		}
		case TYPE_STR_3: {
			return readStringImpl(buff, rp, 3);
		}
		case TYPE_STR_4: {
			return readStringImpl(buff, rp, 4);
		}
		case TYPE_STR_5: {
			return readStringImpl(buff, rp, 5);
		}
		case TYPE_STR_6: {
			return readStringImpl(buff, rp, 6);
		}
		case TYPE_STR_7: {
			return readStringImpl(buff, rp, 7);
		}
		case TYPE_STR_8: {
			return readStringImpl(buff, rp, 8);
		}
		case TYPE_STR_9: {
			return readStringImpl(buff, rp, 9);
		}
		case TYPE_STR_10: {
			return readStringImpl(buff, rp, 10);
		}
		case TYPE_STR_11: {
			return readStringImpl(buff, rp, 11);
		}
		case TYPE_STR_12: {
			return readStringImpl(buff, rp, 12);
		}
		case TYPE_STR_13: {
			return readStringImpl(buff, rp, 13);
		}
		case TYPE_STR_14: {
			return readStringImpl(buff, rp, 14);
		}
		case TYPE_STR_15: {
			return readStringImpl(buff, rp, 15);
		}
		case TYPE_STR_16: {
			return readStringImpl(buff, rp, 16);
		}
		case TYPE_STR_17: {
			return readStringImpl(buff, rp, 17);
		}
		case TYPE_STR_18: {
			return readStringImpl(buff, rp, 18);
		}
		case TYPE_STR_19: {
			return readStringImpl(buff, rp, 19);
		}
		case TYPE_STR_20: {
			return readStringImpl(buff, rp, 20);
		}
		case TYPE_STR_21: {
			return readStringImpl(buff, rp, 21);
		}
		case TYPE_STR_22: {
			return readStringImpl(buff, rp, 22);
		}
		case TYPE_STR_23: {
			return readStringImpl(buff, rp, 23);
		}
		case TYPE_STR_24: {
			return readStringImpl(buff, rp, 24);
		}
		case TYPE_STR_25: {
			return readStringImpl(buff, rp, 25);
		}
		case TYPE_STR_26: {
			return readStringImpl(buff, rp, 26);
		}
		case TYPE_STR: {
			int len = readInt(buff, rp);
			return readStringImpl(buff, rp, len);
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeString(final byte[] buff,
			final Ref<Integer> wp, final String v) throws IOException {
		if (v == null) {
			writeNull(buff, wp);
		} else {
			byte[] b = v.getBytes(UTF8);
			int len = b.length;
			switch (len) {
			case 0:
				write(buff, wp, TYPE_STR_0);
				break;
			case 1:
				write(buff, wp, TYPE_STR_1);
				printString(buff, wp, b);
				break;
			case 2:
				write(buff, wp, TYPE_STR_2);
				printString(buff, wp, b);
				break;
			case 3:
				write(buff, wp, TYPE_STR_3);
				printString(buff, wp, b);
				break;
			case 4:
				write(buff, wp, TYPE_STR_4);
				printString(buff, wp, b);
				break;
			case 5:
				write(buff, wp, TYPE_STR_5);
				printString(buff, wp, b);
				break;
			case 6:
				write(buff, wp, TYPE_STR_6);
				printString(buff, wp, b);
				break;
			case 7:
				write(buff, wp, TYPE_STR_7);
				printString(buff, wp, b);
				break;
			case 8:
				write(buff, wp, TYPE_STR_8);
				printString(buff, wp, b);
				break;
			case 9:
				write(buff, wp, TYPE_STR_9);
				printString(buff, wp, b);
				break;
			case 10:
				write(buff, wp, TYPE_STR_10);
				printString(buff, wp, b);
				break;
			case 11:
				write(buff, wp, TYPE_STR_11);
				printString(buff, wp, b);
				break;
			case 12:
				write(buff, wp, TYPE_STR_12);
				printString(buff, wp, b);
				break;
			case 13:
				write(buff, wp, TYPE_STR_13);
				printString(buff, wp, b);
				break;
			case 14:
				write(buff, wp, TYPE_STR_14);
				printString(buff, wp, b);
				break;
			case 15:
				write(buff, wp, TYPE_STR_15);
				printString(buff, wp, b);
				break;
			case 16:
				write(buff, wp, TYPE_STR_16);
				printString(buff, wp, b);
				break;
			case 17:
				write(buff, wp, TYPE_STR_17);
				printString(buff, wp, b);
				break;
			case 18:
				write(buff, wp, TYPE_STR_18);
				printString(buff, wp, b);
				break;
			case 19:
				write(buff, wp, TYPE_STR_19);
				printString(buff, wp, b);
				break;
			case 20:
				write(buff, wp, TYPE_STR_20);
				printString(buff, wp, b);
				break;
			case 21:
				write(buff, wp, TYPE_STR_21);
				printString(buff, wp, b);
				break;
			case 22:
				write(buff, wp, TYPE_STR_22);
				printString(buff, wp, b);
				break;
			case 23:
				write(buff, wp, TYPE_STR_23);
				printString(buff, wp, b);
				break;
			case 24:
				write(buff, wp, TYPE_STR_24);
				printString(buff, wp, b);
				break;
			case 25:
				write(buff, wp, TYPE_STR_25);
				printString(buff, wp, b);
				break;
			case 26:
				write(buff, wp, TYPE_STR_26);
				printString(buff, wp, b);
				break;
			default:
				write(buff, wp, TYPE_STR);
				writeInt(buff, wp, len);
				printString(buff, wp, b);
				break;
			}
		}
	}

	public static final Date readDate(final byte[] buff, final Ref<Integer> rp)
			throws IOException {
		int v = read(buff, rp);
		if (v == TYPE_NULL)
			return null;
		else if (v == TYPE_JAVA_DATE) {
			final byte[] b = new byte[8];
			readFully(buff, rp, b);
			// for (int i = 0; i < 8; i++) {
			// b[i] = read(buff, rp);
			// }
			long high = ((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16)
					+ ((b[2] & 0xff) << 8) + ((b[3] & 0xff) << 0);
			long low = ((b[4] & 0xff) << 24) + ((b[5] & 0xff) << 16)
					+ ((b[6] & 0xff) << 8) + ((b[7] & 0xff) << 0);
			long x = (high << 32) + (0xffffffffL & low);
			return new java.util.Date(x);
		}

		throw new IOException("unknow type: " + v);
	}

	public static final void writeDate(final byte[] buff,
			final Ref<Integer> wp, final java.sql.Date dat) throws IOException {
		writeDate(buff, wp, new java.util.Date(dat.getTime()));
	}

	public static final void writeDate(final byte[] buff,
			final Ref<Integer> wp, final java.sql.Timestamp dat)
			throws IOException {
		writeDate(buff, wp, new java.util.Date(dat.getTime()));
	}

	public static final void writeDate(final byte[] buff,
			final Ref<Integer> wp, final java.sql.Time dat) throws IOException {
		writeDate(buff, wp, new java.util.Date(dat.getTime()));
	}

	public static final void writeDate(final byte[] buff,
			final Ref<Integer> wp, final java.util.Date dat) throws IOException {
		if (dat == null) {
			writeNull(buff, wp);
		} else {
			long v = dat.getTime();
			write(buff, wp, TYPE_JAVA_DATE);
			write(buff, wp, (byte) ((v >> 56) & 0xff));
			write(buff, wp, (byte) ((v >> 48) & 0xff));
			write(buff, wp, (byte) ((v >> 40) & 0xff));
			write(buff, wp, (byte) ((v >> 32) & 0xff));
			write(buff, wp, (byte) ((v >> 24) & 0xff));
			write(buff, wp, (byte) ((v >> 16) & 0xff));
			write(buff, wp, (byte) ((v >> 8) & 0xff));
			write(buff, wp, (byte) ((v >> 0) & 0xff));
		}
	}

	public static final byte[] readBytes(final byte[] buff,
			final Ref<Integer> rp) throws IOException {
		int v = read(buff, rp);
		switch (v) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_BYTES_0: {
			return new byte[0];
		}
		case TYPE_BYTES: {
			int len = readInt(buff, rp);
			byte[] b = new byte[len];
			int len2 = read(buff, rp, b);
			if (len != len2)
				throw new IOException("bytes not enough");
			return b;
		}
		}
		throw new IOException("unknow type: " + v);
	}

	public static final void writeBytes(final byte[] buff,
			final Ref<Integer> wp, final byte[] v) throws IOException {
		if (v == null) {
			writeNull(buff, wp);
		} else {
			int len = v.length;
			if (len == 0) {
				write(buff, wp, TYPE_BYTES_0);
			} else {
				write(buff, wp, TYPE_BYTES);
				writeInt(buff, wp, len);
				write(buff, wp, v);
			}
		}
	}

	public static final List readList(final byte[] buff, final Ref<Integer> rp,
			final Object o1) throws Exception {
		int v = read(buff, rp);
		switch (v) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_VECTOR_0: {
			return new NewList();
		}
		case TYPE_VECTOR_1: {
			return readList(buff, rp, 1, o1);
		}
		case TYPE_VECTOR_2: {
			return readList(buff, rp, 2, o1);
		}
		case TYPE_VECTOR_3: {
			return readList(buff, rp, 3, o1);
		}
		case TYPE_VECTOR_4: {
			return readList(buff, rp, 4, o1);
		}
		case TYPE_VECTOR_5: {
			return readList(buff, rp, 5, o1);
		}
		case TYPE_VECTOR_6: {
			return readList(buff, rp, 6, o1);
		}
		case TYPE_VECTOR_7: {
			return readList(buff, rp, 7, o1);
		}
		case TYPE_VECTOR_8: {
			return readList(buff, rp, 8, o1);
		}
		case TYPE_VECTOR_9: {
			return readList(buff, rp, 9, o1);
		}
		case TYPE_VECTOR_10: {
			return readList(buff, rp, 10, o1);
		}
		case TYPE_VECTOR_11: {
			return readList(buff, rp, 11, o1);
		}
		case TYPE_VECTOR_12: {
			return readList(buff, rp, 12, o1);
		}
		case TYPE_VECTOR_13: {
			return readList(buff, rp, 13, o1);
		}
		case TYPE_VECTOR_14: {
			return readList(buff, rp, 14, o1);
		}
		case TYPE_VECTOR_15: {
			return readList(buff, rp, 15, o1);
		}
		case TYPE_VECTOR_16: {
			return readList(buff, rp, 16, o1);
		}
		case TYPE_VECTOR_17: {
			return readList(buff, rp, 17, o1);
		}
		case TYPE_VECTOR_18: {
			return readList(buff, rp, 18, o1);
		}
		case TYPE_VECTOR_19: {
			return readList(buff, rp, 19, o1);
		}
		case TYPE_VECTOR_20: {
			return readList(buff, rp, 20, o1);
		}
		case TYPE_VECTOR_21: {
			return readList(buff, rp, 21, o1);
		}
		case TYPE_VECTOR_22: {
			return readList(buff, rp, 22, o1);
		}
		case TYPE_VECTOR_23: {
			return readList(buff, rp, 23, o1);
		}
		case TYPE_VECTOR_24: {
			return readList(buff, rp, 24, o1);
		}
		case TYPE_VECTOR: {
			int len = readInt(buff, rp);
			return readList(buff, rp, len, o1);
		}
		}

		throw new IOException("unknow type: " + v);
	}

	private static final List readList(final byte[] buff,
			final Ref<Integer> rp, final int len, final Object o1)
			throws Exception {
		List ret = new ArrayList(len);
		for (int i = 0; i < len; i++) {
			// obj.readObject(input);
			Object obj = readObj(buff, rp, o1);
			ret.add(obj);
		}
		return ret;
	}

	public static final void writeList(final byte[] buff,
			final Ref<Integer> wp, final List v) throws Exception {
		if (v == null) {
			writeNull(buff, wp);
		} else {
			int len = v.size();
			switch (len) {
			case 0:
				write(buff, wp, TYPE_VECTOR_0);
				break;
			case 1:
				write(buff, wp, TYPE_VECTOR_1);
				break;
			case 2:
				write(buff, wp, TYPE_VECTOR_2);
				break;
			case 3:
				write(buff, wp, TYPE_VECTOR_3);
				break;
			case 4:
				write(buff, wp, TYPE_VECTOR_4);
				break;
			case 5:
				write(buff, wp, TYPE_VECTOR_5);
				break;
			case 6:
				write(buff, wp, TYPE_VECTOR_6);
				break;
			case 7:
				write(buff, wp, TYPE_VECTOR_7);
				break;
			case 8:
				write(buff, wp, TYPE_VECTOR_8);
				break;
			case 9:
				write(buff, wp, TYPE_VECTOR_9);
				break;
			case 10:
				write(buff, wp, TYPE_VECTOR_10);
				break;
			case 11:
				write(buff, wp, TYPE_VECTOR_11);
				break;
			case 12:
				write(buff, wp, TYPE_VECTOR_12);
				break;
			case 13:
				write(buff, wp, TYPE_VECTOR_13);
				break;
			case 14:
				write(buff, wp, TYPE_VECTOR_14);
				break;
			case 15:
				write(buff, wp, TYPE_VECTOR_15);
				break;
			case 16:
				write(buff, wp, TYPE_VECTOR_16);
				break;
			case 17:
				write(buff, wp, TYPE_VECTOR_17);
				break;
			case 18:
				write(buff, wp, TYPE_VECTOR_18);
				break;
			case 19:
				write(buff, wp, TYPE_VECTOR_19);
				break;
			case 20:
				write(buff, wp, TYPE_VECTOR_20);
				break;
			case 21:
				write(buff, wp, TYPE_VECTOR_21);
				break;
			case 22:
				write(buff, wp, TYPE_VECTOR_22);
				break;
			case 23:
				write(buff, wp, TYPE_VECTOR_23);
				break;
			case 24:
				write(buff, wp, TYPE_VECTOR_24);
				break;
			default:
				write(buff, wp, TYPE_VECTOR);
				writeInt(buff, wp, len);
				break;
			}
			for (int i = 0; i < len; i++) {
				Object obj = v.get(i);
				writeObj(buff, wp, obj);
			}
		}
	}

	public static final Map readMap(final byte[] buff, final Ref<Integer> rp,
			final Object kType, final Object vType) throws Exception {
		byte tag = read(buff, rp);
		switch (tag) {
		case TYPE_NULL: {
			return null;
		}
		case TYPE_HASHTABLE_0: {
			return new HashMap();
		}
		case TYPE_HASHTABLE_1: {
			return readMap(buff, rp, 1, kType, vType);
		}
		case TYPE_HASHTABLE_2: {
			return readMap(buff, rp, 2, kType, vType);
		}
		case TYPE_HASHTABLE_3: {
			return readMap(buff, rp, 3, kType, vType);
		}
		case TYPE_HASHTABLE_4: {
			return readMap(buff, rp, 4, kType, vType);
		}
		case TYPE_HASHTABLE_5: {
			return readMap(buff, rp, 5, kType, vType);
		}
		case TYPE_HASHTABLE_6: {
			return readMap(buff, rp, 6, kType, vType);
		}
		case TYPE_HASHTABLE_7: {
			return readMap(buff, rp, 7, kType, vType);
		}
		case TYPE_HASHTABLE_8: {
			return readMap(buff, rp, 8, kType, vType);
		}
		case TYPE_HASHTABLE_9: {
			return readMap(buff, rp, 9, kType, vType);
		}
		case TYPE_HASHTABLE_10: {
			return readMap(buff, rp, 10, kType, vType);
		}
		case TYPE_HASHTABLE_11: {
			return readMap(buff, rp, 11, kType, vType);
		}
		case TYPE_HASHTABLE_12: {
			return readMap(buff, rp, 12, kType, vType);
		}
		case TYPE_HASHTABLE_13: {
			return readMap(buff, rp, 13, kType, vType);
		}
		case TYPE_HASHTABLE_14: {
			return readMap(buff, rp, 14, kType, vType);
		}
		case TYPE_HASHTABLE_15: {
			return readMap(buff, rp, 15, kType, vType);
		}
		case TYPE_HASHTABLE: {
			int len = readInt(buff, rp);
			return readMap(buff, rp, len, kType, vType);
		}
		}
		throw new IOException("unknow type: " + tag);
	}

	private static final Map readMap(final byte[] buff, final Ref<Integer> rp,
			final int len, final Object kType, final Object vType)
			throws Exception {
		Map ret = new HashMap();
		for (int i = 0; i < len; i++) {
			Object key = readObj(buff, rp, kType);
			Object var = readObj(buff, rp, vType);
			ret.put(key, var);
		}
		return ret;
	}

	public static final void writeMap(final byte[] buff, final Ref<Integer> wp,
			final Map v) throws Exception {
		if (v == null) {
			writeNull(buff, wp);
		} else {
			int len = v.size();
			switch (len) {
			case 0:
				write(buff, wp, TYPE_HASHTABLE_0);
				break;
			case 1: {
				write(buff, wp, TYPE_HASHTABLE_1);
				break;
			}
			case 2: {
				write(buff, wp, TYPE_HASHTABLE_2);
				break;
			}
			case 3: {
				write(buff, wp, TYPE_HASHTABLE_3);
				break;
			}
			case 4: {
				write(buff, wp, TYPE_HASHTABLE_4);
				break;
			}
			case 5: {
				write(buff, wp, TYPE_HASHTABLE_5);
				break;
			}
			case 6: {
				write(buff, wp, TYPE_HASHTABLE_6);
				break;
			}
			case 7: {
				write(buff, wp, TYPE_HASHTABLE_7);
				break;
			}
			case 8: {
				write(buff, wp, TYPE_HASHTABLE_8);
				break;
			}
			case 9: {
				write(buff, wp, TYPE_HASHTABLE_9);
				break;
			}
			case 10: {
				write(buff, wp, TYPE_HASHTABLE_10);
				break;
			}
			case 11: {
				write(buff, wp, TYPE_HASHTABLE_11);
				break;
			}
			case 12: {
				write(buff, wp, TYPE_HASHTABLE_12);
				break;
			}
			case 13: {
				write(buff, wp, TYPE_HASHTABLE_13);
				break;
			}
			case 14: {
				write(buff, wp, TYPE_HASHTABLE_14);
				break;
			}
			case 15: {
				write(buff, wp, TYPE_HASHTABLE_15);
				break;
			}
			default:
				write(buff, wp, TYPE_HASHTABLE);
				writeInt(buff, wp, len);
				break;
			}

			Set<Entry> entrys = v.entrySet();
			for (Entry e : entrys) {
				Object key = e.getKey();
				Object var = e.getValue();
				writeObj(buff, wp, key);
				writeObj(buff, wp, var);
			}
		}
	}

	protected static final String readStringImpl(final byte[] buff,
			final Ref<Integer> rp, final int length) throws IOException {
		if (length <= 0)
			return "";

		byte[] b = new byte[length];
		int len2 = read(buff, rp, b);
		if (length != len2) {
			throw new IOException("bytes not enough");
		}

		return new String(b, UTF8);
	}

	protected static final void printString(final byte[] buff,
			final Ref<Integer> wp, final byte[] v) throws IOException {
		write(buff, wp, v);
	}

	// ///////////////////////////
	private static final Object readObj(final byte[] buff,
			final Ref<Integer> rp, final Object o1) throws Exception {
		if (o1 instanceof Integer) {
			return readInt(buff, rp);
		} else if (o1 instanceof String) {
			return readString(buff, rp);
		} else if (o1 instanceof Boolean) {
			return readBool(buff, rp);
		} else if (o1 instanceof Byte) {
			return readByte(buff, rp);
		} else if (o1 instanceof byte[]) {
			return readBytes(buff, rp);
		} else if (o1 instanceof Short) {
			return readShort(buff, rp);
		} else if (o1 instanceof Long) {
			return readLong(buff, rp);
		} else if (o1 instanceof java.util.Date) {
			return readDate(buff, rp);
		} else if (o1 instanceof Double) {
			return readDouble(buff, rp);
		} else if (o1 instanceof List) {
			return readList(buff, rp, o1);
		} else if (o1 instanceof B2X) {
			B2X obj = (B2X) o1.getClass().newInstance();
			obj.readObject(buff, rp);
			return obj;
		} else {
			throw new IOException("unknow tag error:" + o1);
		}
	}

	private static final void writeObj(final byte[] buff,
			final Ref<Integer> wp, final Object object) throws Exception {
		if (object == null) {
			writeNull(buff, wp);
		} else if (object instanceof Integer) {
			int v = ((Integer) object).intValue();
			writeInt(buff, wp, v);
		} else if (object instanceof String) {
			String v = (String) object;
			writeString(buff, wp, v);
		} else if (object instanceof Boolean) {
			boolean v = ((Boolean) object).booleanValue();
			writeBool(buff, wp, v);
		} else if (object instanceof Byte) {
			int v = ((Byte) object).byteValue();
			writeByte(buff, wp, v);
		} else if (object instanceof byte[]) {
			byte[] v = (byte[]) object;
			writeBytes(buff, wp, v);
		} else if (object instanceof List) {
			List v = (List) object;
			writeList(buff, wp, v);
		} else if (object instanceof Map) {
			Map v = (Map) object;
			writeMap(buff, wp, v);
		} else if (object instanceof Short) {
			int v = ((Short) object).shortValue();
			writeShort(buff, wp, v);
		} else if (object instanceof Long) {
			long v = ((Long) object).longValue();
			writeLong(buff, wp, v);
		} else if (object instanceof Double) {
			double v = ((Double) object).doubleValue();
			writeDouble(buff, wp, v);
		} else if (object instanceof java.util.Date) {
			java.util.Date v = (java.util.Date) object;
			writeDate(buff, wp, v);
		} else if (object instanceof java.sql.Date) {
			java.sql.Date v = (java.sql.Date) object;
			writeDate(buff, wp, new java.util.Date(v.getTime()));
		} else if (object instanceof java.sql.Timestamp) {
			java.sql.Timestamp v = (java.sql.Timestamp) object;
			writeDate(buff, wp, new java.util.Date(v.getTime()));
		} else if (object instanceof java.sql.Time) {
			java.sql.Time v = (java.sql.Time) object;
			writeDate(buff, wp, new java.util.Date(v.getTime()));
		} else if (object instanceof B2X) {
			B2X v = (B2X) object;
			v.writeObject(buff, wp);
		} else {
			throw new IOException("unsupported object:" + object);
		}
	}

	// ///////////////////////////

	public void writeObject(final byte[] buff, final Ref<Integer> wp)
			throws Exception {
	}

	public void readObject(final byte[] buff, final Ref<Integer> rp)
			throws Exception {
	}

	public final int writeObject(final byte[] buff) throws Exception {
		final Ref<Integer> wp = new Ref<Integer>(0);
		writeObject(buff, wp);
		return wp.val;
	}

	public final int writeObject(final byte[] buff, final int pos)
			throws Exception {
		final Ref<Integer> wp = new Ref<Integer>(pos);
		writeObject(buff, wp);
		return wp.val;
	}

	public final void writeTo(final OutputStream output) throws Exception {
		writeObject(output);
	}

	// ///////////////////////////

	public final int readObject(final byte[] buff) throws Exception {
		final Ref<Integer> rp = new Ref<Integer>(0);
		readObject(buff, rp);
		return rp.val;
	}

	// ////////////////////////////

	public final byte[] toByteArray() throws Exception {
		try (ByteOutStream baos = ByteOutPool.borrowObject();) {
			writeObject(baos);
			baos.flush();
			return baos.toByteArray();
		}
	}

	// ////////////////////////////
	public static void main(String[] args) throws Exception {
		final Ref<Integer> rp = new Ref<Integer>(0);
		final Ref<Integer> wp = new Ref<Integer>(0);
		byte[] buf = new byte[64];

		NewList v = new NewList();
		v.addE(10).addE(11).addE(12).addE(13);

		write(buf, wp, 1);
		write(buf, wp, 2);
		write(buf, wp, 3);
		write(buf, wp, 4);
		write(buf, wp, 5);
		writeInt(buf, wp, 1234567);
		writeString(buf, wp, "你啊aaaab");
		writeList(buf, wp, v);
		writeInt(buf, wp, -1);
		write(buf, wp, -5);
		writeDouble(buf, wp, -12.123);
		System.out.println(wp.val);
		System.out.println(ByteEx.bytesToString(buf));

		int b1 = read(buf, rp);
		int b2 = read(buf, rp);
		int b3 = read(buf, rp);
		int b4 = read(buf, rp);
		int b5 = read(buf, rp);
		int i7 = readInt(buf, rp);
		String s8 = readString(buf, rp);
		List v9 = readList(buf, rp, intVal);
		int i10 = readInt(buf, rp);
		int i11 = read(buf, rp);
		double d12 = readDouble(buf, rp);
		System.out.println(b1);
		System.out.println(b2);
		System.out.println(b3);
		System.out.println(b4);
		System.out.println(b5);
		System.out.println(i7);
		System.out.println(s8);
		System.out.println(v9);
		System.out.println(i10);
		System.out.println(i11);
		System.out.println(d12);
	}
}
