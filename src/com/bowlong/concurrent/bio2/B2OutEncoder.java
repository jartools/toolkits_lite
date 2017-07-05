package com.bowlong.concurrent.bio2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.bowlong.bio2.B2Type;
import com.bowlong.lang.NumEx;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class B2OutEncoder {

	public static final void write(ByteBuffer buff, byte b){
		buff.put(b);
	}
	
	public static final void writeNull(ByteBuffer os) throws IOException {
		write(os, B2Type.NULL);
	}

	public static final void writeBoolean(ByteBuffer os, boolean v)
			throws IOException {
		if (v)
			write(os, B2Type.BOOLEAN_TRUE);
		else
			write(os, B2Type.BOOLEAN_FALSE);
	}

	public static final void writeByte(ByteBuffer os, byte v) throws IOException {
		if (v == 0)
			write(os, B2Type.BYTE_0);
		else {
			write(os, B2Type.BYTE);
			write(os, v);
		}
	}

	public static final void writeShort(ByteBuffer os, int v) throws IOException {
		if (v == 0)
			write(os, B2Type.SHORT_0);
		else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			write(os, B2Type.SHORT_8B);
			write(os, (byte)v);
		} else {
			write(os, B2Type.SHORT_16B);
			write(os, (byte) ((v >> 8) & 0xff));
			write(os, (byte) ((v >> 0) & 0xff));
		}
	}

	public static final void writeInt(ByteBuffer os, int v) throws IOException {
		switch (v) {
		case -1:
			write(os, B2Type.INT_N1);
			break;
		case 0:
			write(os, B2Type.INT_0);
			break;
		case 1:
			write(os, B2Type.INT_1);
			break;
		case 2:
			write(os, B2Type.INT_2);
			break;
		case 3:
			write(os, B2Type.INT_3);
			break;
		case 4:
			write(os, B2Type.INT_4);
			break;
		case 5:
			write(os, B2Type.INT_5);
			break;
		case 6:
			write(os, B2Type.INT_6);
			break;
		case 7:
			write(os, B2Type.INT_7);
			break;
		case 8:
			write(os, B2Type.INT_8);
			break;
		case 9:
			write(os, B2Type.INT_9);
			break;
		case 10:
			write(os, B2Type.INT_10);
			break;
		case 11:
			write(os, B2Type.INT_11);
			break;
		case 12:
			write(os, B2Type.INT_12);
			break;
		case 13:
			write(os, B2Type.INT_13);
			break;
		case 14:
			write(os, B2Type.INT_14);
			break;
		case 15:
			write(os, B2Type.INT_15);
			break;
		case 16:
			write(os, B2Type.INT_16);
			break;
		case 17:
			write(os, B2Type.INT_17);
			break;
		case 18:
			write(os, B2Type.INT_18);
			break;
		case 19:
			write(os, B2Type.INT_19);
			break;
		case 20:
			write(os, B2Type.INT_20);
			break;
		case 21:
			write(os, B2Type.INT_21);
			break;
		case 22:
			write(os, B2Type.INT_22);
			break;
		case 23:
			write(os, B2Type.INT_23);
			break;
		case 24:
			write(os, B2Type.INT_24);
			break;
		case 25:
			write(os, B2Type.INT_25);
			break;
		case 26:
			write(os, B2Type.INT_26);
			break;
		case 27:
			write(os, B2Type.INT_27);
			break;
		case 28:
			write(os, B2Type.INT_28);
			break;
		case 29:
			write(os, B2Type.INT_29);
			break;
		case 30:
			write(os, B2Type.INT_30);
			break;
		case 31:
			write(os, B2Type.INT_31);
			break;
		case 32:
			write(os, B2Type.INT_32);
			break;
		default:
			if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
				write(os, B2Type.INT_8B);
				write(os, (byte)v);
			} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
				write(os, B2Type.INT_16B);
				write(os, (byte) ((v >> 8) & 0xff));
				write(os, (byte) ((v >> 0) & 0xff));
			} else {
				write(os, B2Type.INT_32B);
				write(os, (byte) ((v >> 24) & 0xff));
				write(os, (byte) ((v >> 16) & 0xff));
				write(os, (byte) ((v >> 8) & 0xff));
				write(os, (byte) ((v >> 0) & 0xff));
			}
			break;
		}
	}

	public static final void writeIntArray(ByteBuffer os, int[] v) throws IOException {
		int len = v.length;
		switch (len) {
		case 0:
			write(os, B2Type.INT_ARRAY_0);
			break;
		case 1:
			write(os, B2Type.INT_ARRAY_1);
			break;
		case 2:
			write(os, B2Type.INT_ARRAY_2);
			break;
		case 3:
			write(os, B2Type.INT_ARRAY_3);
			break;
		case 4:
			write(os, B2Type.INT_ARRAY_4);
			break;
		case 5:
			write(os, B2Type.INT_ARRAY_5);
			break;
		case 6:
			write(os, B2Type.INT_ARRAY_6);
			break;
		case 7:
			write(os, B2Type.INT_ARRAY_7);
			break;
		case 8:
			write(os, B2Type.INT_ARRAY_8);
			break;
		case 9:
			write(os, B2Type.INT_ARRAY_9);
			break;
		case 10:
			write(os, B2Type.INT_ARRAY_10);
			break;
		case 11:
			write(os, B2Type.INT_ARRAY_11);
			break;
		case 12:
			write(os, B2Type.INT_ARRAY_12);
			break;
		case 13:
			write(os, B2Type.INT_ARRAY_13);
			break;
		case 14:
			write(os, B2Type.INT_ARRAY_14);
			break;
		case 15:
			write(os, B2Type.INT_ARRAY_15);
			break;
		case 16:
			write(os, B2Type.INT_ARRAY_16);
			break;
		default:
			write(os, B2Type.INT_ARRAY);
			writeInt(os, len);
			break;
		}
		for (int i = 0; i < len; i++) {
			writeInt(os, v[i]);
		}
	}
	
	public static final void writeInt2DArray(ByteBuffer os, int[][] v) throws IOException {
		int len = v.length;
		if(len <= 0){
			write(os, B2Type.INT_2D_ARRAY_0);
			return ;
		}
		write(os, B2Type.INT_2D_ARRAY);
		writeInt(os, len);
		for (int i = 0; i < len; i++) {
			writeIntArray(os, v[i]);
		}
	}
	
	public static final void writeLong(ByteBuffer os, long v) throws IOException {
		if (v == 0) {
			write(os, B2Type.LONG_0);
		} else if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
			write(os, B2Type.LONG_8B);
			write(os, (byte) v);
		} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
			write(os, B2Type.LONG_16B);
			write(os, (byte) ((v >> 8) & 0xff));
			write(os, (byte) ((v >> 0) & 0xff));
		} else if (v >= NumEx.INT_MIN_VALUE && v <= NumEx.INT_MAX_VALUE) {
			write(os, B2Type.LONG_32B);
			write(os, (byte) ((v >> 24) & 0xff));
			write(os, (byte) ((v >> 16) & 0xff));
			write(os, (byte) ((v >> 8) & 0xff));
			write(os, (byte) ((v >> 0) & 0xff));
		} else {
			write(os, B2Type.LONG_64B);
			write(os, (byte) ((v >> 56) & 0xff));
			write(os, (byte) ((v >> 48) & 0xff));
			write(os, (byte) ((v >> 40) & 0xff));
			write(os, (byte) ((v >> 32) & 0xff));
			write(os, (byte) ((v >> 24) & 0xff));
			write(os, (byte) ((v >> 16) & 0xff));
			write(os, (byte) ((v >> 8) & 0xff));
			write(os, (byte) ((v >> 0) & 0xff));
		}
	}

	public static final void writeDouble(ByteBuffer os, double var)
			throws IOException {
		long v = Double.doubleToLongBits(var);
		if (v == 0) {
			write(os, B2Type.DOUBLE_0);
//		} else if (v >= NumEx.Byte_MIN_VALUE && v <= NumEx.Byte_MAX_VALUE) {
//			write(os, B2Type.DOUBLE_8B);
//			write(os, (int) v);
//		} else if (v >= NumEx.Short_MIN_VALUE && v <= NumEx.Short_MAX_VALUE) {
//			write(os, B2Type.DOUBLE_16B);
//			write(os, (byte) ((v >> 8) & 0xff));
//			write(os, (byte) ((v >> 0) & 0xff));
//		} else if (v >= NumEx.Integer_MIN_VALUE && v <= NumEx.Integer_MAX_VALUE) {
//			write(os, B2Type.DOUBLE_32B);
//			write(os, (byte) ((v >> 24) & 0xff));
//			write(os, (byte) ((v >> 16) & 0xff));
//			write(os, (byte) ((v >> 8) & 0xff));
//			write(os, (byte) ((v >> 0) & 0xff));
		} else {
			write(os, B2Type.DOUBLE_64B);
			write(os, (byte) ((v >> 56) & 0xff));
			write(os, (byte) ((v >> 48) & 0xff));
			write(os, (byte) ((v >> 40) & 0xff));
			write(os, (byte) ((v >> 32) & 0xff));
			write(os, (byte) ((v >> 24) & 0xff));
			write(os, (byte) ((v >> 16) & 0xff));
			write(os, (byte) ((v >> 8) & 0xff));
			write(os, (byte) ((v >> 0) & 0xff));
		}
	}

	public static final void writeString(ByteBuffer os, String v)
			throws IOException {
		if (v == null) {
			writeNull(os);
		} else {
			byte[] b = v.getBytes(B2Type.UTF8);
			int len = b.length;
			switch (len) {
			case 0:
				write(os, B2Type.STR_0);
				break;
			case 1:
				write(os, B2Type.STR_1);
				printString(os, b);
				break;
			case 2:
				write(os, B2Type.STR_2);
				printString(os, b);
				break;
			case 3:
				write(os, B2Type.STR_3);
				printString(os, b);
				break;
			case 4:
				write(os, B2Type.STR_4);
				printString(os, b);
				break;
			case 5:
				write(os, B2Type.STR_5);
				printString(os, b);
				break;
			case 6:
				write(os, B2Type.STR_6);
				printString(os, b);
				break;
			case 7:
				write(os, B2Type.STR_7);
				printString(os, b);
				break;
			case 8:
				write(os, B2Type.STR_8);
				printString(os, b);
				break;
			case 9:
				write(os, B2Type.STR_9);
				printString(os, b);
				break;
			case 10:
				write(os, B2Type.STR_10);
				printString(os, b);
				break;
			case 11:
				write(os, B2Type.STR_11);
				printString(os, b);
				break;
			case 12:
				write(os, B2Type.STR_12);
				printString(os, b);
				break;
			case 13:
				write(os, B2Type.STR_13);
				printString(os, b);
				break;
			case 14:
				write(os, B2Type.STR_14);
				printString(os, b);
				break;
			case 15:
				write(os, B2Type.STR_15);
				printString(os, b);
				break;
			case 16:
				write(os, B2Type.STR_16);
				printString(os, b);
				break;
			case 17:
				write(os, B2Type.STR_17);
				printString(os, b);
				break;
			case 18:
				write(os, B2Type.STR_18);
				printString(os, b);
				break;
			case 19:
				write(os, B2Type.STR_19);
				printString(os, b);
				break;
			case 20:
				write(os, B2Type.STR_20);
				printString(os, b);
				break;
			case 21:
				write(os, B2Type.STR_21);
				printString(os, b);
				break;
			case 22:
				write(os, B2Type.STR_22);
				printString(os, b);
				break;
			case 23:
				write(os, B2Type.STR_23);
				printString(os, b);
				break;
			case 24:
				write(os, B2Type.STR_24);
				printString(os, b);
				break;
			case 25:
				write(os, B2Type.STR_25);
				printString(os, b);
				break;
			case 26:
				write(os, B2Type.STR_26);
				printString(os, b);
				break;
			default:
				write(os, B2Type.STR);
				writeInt(os, len);
				printString(os, b);
				break;
			}
		}
	}

	public static final void writeBytes(ByteBuffer os, byte[] v) throws IOException {
		if (v == null) {
			writeNull(os);
		} else {
			int len = v.length;
			if (len == 0) {
				write(os, B2Type.BYTES_0);
			} else {
				write(os, B2Type.BYTES);
				writeInt(os, len);
				os.put(v);
			}
		}
	}

	public static final void writeVector(ByteBuffer os, List v) throws Exception {
		if (v == null) {
			writeNull(os);
		} else {
			int len = v.size();
			switch (len) {
			case 0:
				write(os, B2Type.VECTOR_0);
				break;
			case 1:
				write(os, B2Type.VECTOR_1);
				break;
			case 2:
				write(os, B2Type.VECTOR_2);
				break;
			case 3:
				write(os, B2Type.VECTOR_3);
				break;
			case 4:
				write(os, B2Type.VECTOR_4);
				break;
			case 5:
				write(os, B2Type.VECTOR_5);
				break;
			case 6:
				write(os, B2Type.VECTOR_6);
				break;
			case 7:
				write(os, B2Type.VECTOR_7);
				break;
			case 8:
				write(os, B2Type.VECTOR_8);
				break;
			case 9:
				write(os, B2Type.VECTOR_9);
				break;
			case 10:
				write(os, B2Type.VECTOR_10);
				break;
			case 11:
				write(os, B2Type.VECTOR_11);
				break;
			case 12:
				write(os, B2Type.VECTOR_12);
				break;
			case 13:
				write(os, B2Type.VECTOR_13);
				break;
			case 14:
				write(os, B2Type.VECTOR_14);
				break;
			case 15:
				write(os, B2Type.VECTOR_15);
				break;
			case 16:
				write(os, B2Type.VECTOR_16);
				break;
			case 17:
				write(os, B2Type.VECTOR_17);
				break;
			case 18:
				write(os, B2Type.VECTOR_18);
				break;
			case 19:
				write(os, B2Type.VECTOR_19);
				break;
			case 20:
				write(os, B2Type.VECTOR_20);
				break;
			case 21:
				write(os, B2Type.VECTOR_21);
				break;
			case 22:
				write(os, B2Type.VECTOR_22);
				break;
			case 23:
				write(os, B2Type.VECTOR_23);
				break;
			case 24:
				write(os, B2Type.VECTOR_24);
				break;
			default:
				write(os, B2Type.VECTOR);
				writeInt(os, len);
				break;
			}

			for (int i = 0; i < len; i++) {
				//Object object = v.elementAt(i);
				Object object = v.get(i);
				writeObject(os, object);
			}
		}
	}

	public static final void writeMap(ByteBuffer os, Map v) throws Exception {
		if (v == null) {
			writeNull(os);
		} else {
			int len = v.size();
			switch (len) {
			case 0:
				write(os, B2Type.HASHTABLE_0);
				break;
			case 1: {
				write(os, B2Type.HASHTABLE_1);
				break;
			}
			case 2: {
				write(os, B2Type.HASHTABLE_2);
				break;
			}
			case 3: {
				write(os, B2Type.HASHTABLE_3);
				break;
			}
			case 4: {
				write(os, B2Type.HASHTABLE_4);
				break;
			}
			case 5: {
				write(os, B2Type.HASHTABLE_5);
				break;
			}
			case 6: {
				write(os, B2Type.HASHTABLE_6);
				break;
			}
			case 7: {
				write(os, B2Type.HASHTABLE_7);
				break;
			}
			case 8: {
				write(os, B2Type.HASHTABLE_8);
				break;
			}
			case 9: {
				write(os, B2Type.HASHTABLE_9);
				break;
			}
			case 10: {
				write(os, B2Type.HASHTABLE_10);
				break;
			}
			case 11: {
				write(os, B2Type.HASHTABLE_11);
				break;
			}
			case 12: {
				write(os, B2Type.HASHTABLE_12);
				break;
			}
			case 13: {
				write(os, B2Type.HASHTABLE_13);
				break;
			}
			case 14: {
				write(os, B2Type.HASHTABLE_14);
				break;
			}
			case 15: {
				write(os, B2Type.HASHTABLE_15);
				break;
			}
			default:
				write(os, B2Type.HASHTABLE);
				writeInt(os, len);
				break;
			}
			
			Set<Entry> entrys = v.entrySet();
			for (Entry e : entrys) {
				Object key = e.getKey();
				Object var = e.getValue();
				writeObject(os, key);
				writeObject(os, var);
			}
			
//			Enumeration keys = v.keys();
//			Iterator keys = v.keySet().iterator();
//			while (keys.hasNext()) {
//				Object key = keys.next();
//				Object var = v.get(key);
//				writeObject(os, key);
//				writeObject(os, var);
//			}
		}
	}

	public static final void writeObject(ByteBuffer os, Object object)
			throws Exception {
		if (object == null) {
			writeNull(os);
		} else if (object instanceof Map) {
			Map v = (Map) object;
			writeMap(os, v);
		} else if (object instanceof Integer) {
			int v = ((Integer) object).intValue();
			writeInt(os, v);
		} else if (object instanceof String) {
			String v = (String) object;
			writeString(os, v);
		} else if (object instanceof Boolean) {
			boolean v = ((Boolean) object).booleanValue();
			writeBoolean(os, v);
		} else if (object instanceof Byte) {
			byte v = ((Byte) object).byteValue();
			writeByte(os, v);
		} else if (object instanceof byte[]) {
			byte[] v = (byte[]) object;
			writeBytes(os, v);
		} else if (object instanceof List) {
			List v = (List) object;
			writeVector(os, v);
		} else if (object instanceof Short) {
			int v = ((Short) object).shortValue();
			writeShort(os, v);
		} else if (object instanceof Long) {
			long v = ((Long) object).longValue();
			writeLong(os, v);
		} else if (object instanceof Double) {
			double v = ((Double) object).doubleValue();
			writeDouble(os, v);
		} else if (object instanceof int[]) {
			int[] v = (int[]) object;
			writeIntArray(os, v);
		} else if(object instanceof int[][]){
			int[][] v = (int[][]) object;
			writeInt2DArray(os, v);
		} else {
			throw new IOException("unsupported object:" + object);
		}
	}

	// ////////////////////////////////
	protected static final void printString(ByteBuffer os, byte[] v)
			throws IOException {
		os.put(v);
	}

//	protected static final void printString(ByteBuffer os, String v)
//			throws IOException {
//		printString(os, v, 0, v.length());
//	}
//
//	protected static final void printString(ByteBuffer os, String v, int offset,
//			int length) throws IOException {
//		for (int i = 0; i < length; i++) {
//			char ch = v.charAt(i + offset);
//
//			if (ch < 0x80)
//				write(os, (byte)ch);
//			else if (ch < 0x800) {
//				write(os, (byte) (0xc0 + ((ch >> 6) & 0x1f)));
//				write(os, (byte) (0x80 + (ch & 0x3f)));
//			} else {
//				write(os, (byte) (0xe0 + ((ch >> 12) & 0xf)));
//				write(os, (byte) (0x80 + ((ch >> 6) & 0x3f)));
//				write(os, (byte) (0x80 + (ch & 0x3f)));
//			}
//		}
//	}
}