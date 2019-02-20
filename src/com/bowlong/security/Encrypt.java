package com.bowlong.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.bowlong.io.ByteInStream;
import com.bowlong.io.ByteOutStream;
import com.bowlong.lang.ByteEx;
import com.bowlong.lang.NumEx;
import com.bowlong.lang.RndEx;
import com.bowlong.objpool.ByteInPool;
import com.bowlong.objpool.ByteOutPool;

public class Encrypt {
	private static final int KEY_LENGTH = 4;
	/**
	 * XOR
	 * 
	 * @param op1
	 *            byte[]
	 * @param op2
	 *            byte[]
	 * @param order
	 *            boolean true ASC | false DESC
	 */
	private static void XOR(byte[] op1, byte[] op2, boolean order) {
		int len = op1.length;
		int n = order ? 0 : op2.length - 1;
		for (int i = 0; i < len; i++) {
			op1[i] = (byte) (op1[i] ^ op2[n]);
			if (order) {
				n = n < op2.length - 1 ? n + 1 : 0;
			} else {
				n = n > 0 ? n - 1 : op2.length - 1;
			}
		}
	}

	/**
	 * RandomXOR
	 * 
	 * @param op1
	 *            byte[] data
	 * @return byte[] key
	 */
	private static byte[] RandomXOR(byte[] op1, boolean asc) {
		byte[] key = RndEx.nextBytes(KEY_LENGTH);
		XOR(op1, key, asc);
		return key;
	}

	public static byte[] decode(byte[] v) throws Exception {
		try (ByteInStream bais = ByteInPool.borrowObject(v);) {
			byte[] key = readKey(bais);
			byte[] value = readValue(bais);
			XOR(value, key, true);
			return value;
		}
	}

	public static byte[] encode(byte[] v) throws Exception {
		try (ByteOutStream baos = ByteOutPool.borrowObject();) {
			byte[] key = RandomXOR(v, true);
			writeKey(baos, key);
			writeValue(baos, v);
			baos.flush();
			return baos.toByteArray();
		}
	}

	public static String encode(String v) throws Exception {
		byte[] encd = encode(v.getBytes());
		return ByteEx.byteToString(encd);
	}

	public static String decode(String v) throws Exception {
		byte[] decd = ByteEx.strToByte(v);
		byte[] ub = decode(decd);
		return new String(ub);
	}

	public static String encodeInt(int i) throws Exception {
		String v = Integer.toString(i);
		return encode(v);
	}

	public static int decodeInt(String i) throws Exception {
		String s = decode(i);
		return Integer.parseInt(s);
	}

	private static byte[] readKey(InputStream is) throws IOException {
		byte[] b = null;
		b = new byte[KEY_LENGTH];
		is.read(b);
		return b;
	}

	private static byte[] readValue(InputStream is) throws IOException {
		byte[] b = null;
		int len = NumEx.readShort(is);
		b = new byte[len];
		is.read(b);
		return b;
	}

	// v length = KEY_LENGTH
	private static void writeKey(OutputStream os, byte[] v) throws IOException {
		os.write(v);
	}

	private static void writeValue(OutputStream os, byte[] v)
			throws IOException {
		NumEx.writeShort(os, (short) v.length);
		os.write(v);
	}

	public static void main(String[] args) throws Exception {
		String a = "a";
		String b = encode(a);
		System.out.println(b);

		String c = decode(b);
		System.out.println(c);

		int num = 123456789;
		String numStr = encodeInt(num);
		System.out.println(numStr);

		int num2 = decodeInt(numStr);
		System.out.println(num2);

		String lon = encode("" + System.currentTimeMillis());
		System.out.println(lon);
		String v = decode(lon);
		System.out.println(v);
	}
}
