package com.bowlong.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MD5 {

	/*** 自封的MD5加密规则 **/
	static private final String md5(byte[] v) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
			mdAlgorithm.update(v);
			byte[] mdCode = mdAlgorithm.digest();

			int mdCodeLength = mdCode.length;
			char strMd5[] = new char[mdCodeLength * 2];
			int k = 0;
			for (int i = 0; i < mdCodeLength; i++) {
				byte byte0 = mdCode[i];
				strMd5[k++] = hexDigits[byte0 >>> 4 & 0xf];
				strMd5[k++] = hexDigits[byte0 & 0xf];
			}
			mdCode = null;
			return new String(strMd5);
		} catch (Exception e) {
			return "";
		}
	}

	static public final byte[] md5Bytes(byte[] v) {
		return md5(v).getBytes();
	}

	static public final byte[] md5Bytes(String v) {
		return md5Bytes(v.getBytes());
	}

	/*** MD5加密方式 得32位 **/
	static public final String encode(String s) {
		return md5(s.getBytes());
	}

	static public final String encode(Object obj) {
		if (obj == null)
			return "";
		return encode(obj.toString());
	}

	/*** MD5加密方式 取得16位 **/
	static public final String encodeF16(String str) {
		String v32 = encode(str);
		return v32.substring(8, 24);
	}

	static public final String encodeF16(Object obj) {
		if (obj == null)
			return "";
		return encodeF16(obj.toString());
	}

	static public final String encode(long times, boolean isAddUUID) {
		String ss = String.valueOf(times);
		if (isAddUUID) {
			UUID uuid = UUID.randomUUID();
			ss = uuid.toString() + times;
		}
		return encode(ss);
	}

	static public final String encodeF16(long times, boolean isAddUUID) {
		String v32 = encode(times, isAddUUID);
		return v32.substring(8, 24);
	}

	/*** UUID+System.currentTimeMillis 值 MD5加密方式 取得32位 **/
	static public final String encodeUUIDSystime() {
		return encode(System.currentTimeMillis(), true);
	}

	static public final String encodeUUIDSystimeF16() {
		return encodeF16(System.currentTimeMillis(), true);
	}

	/*** 默认的MD5加密方式 **/
	static public final String toMD5(byte[] v) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(v);
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/*** 默认的MD5加密方式 **/
	static public final String toMD5(String plain) {
		if (plain == null)
			return "";
		return toMD5(plain.getBytes());
	}

	static public final String toMD5(Object obj) {
		if (obj == null)
			return "";
		return toMD5(obj.toString());
	}

	static public final String toMD5F16(Object obj) {
		String _md5 = toMD5(obj);
		if ("".equals(_md5))
			return _md5;
		return _md5.substring(8, 24);
	}

	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		String ss = uuid.toString() + System.currentTimeMillis();
		System.out.println(uuid.toString());
		System.out.println(ss);
		System.out.println(MD5.toMD5(ss));
		System.out.println(MD5.encode(ss));
		System.out.println(MD5.encodeF16(ss));
	}
}
