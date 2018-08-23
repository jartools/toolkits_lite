package com.bowlong.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * AES PKCS5 加密 解密
 * 
 * @author Canyon
 * @version createtime：2015年8月21日下午3:33:23
 */
public class AESPKCS5Padding {
	public AESPKCS5Padding() {
		this("11111111");
	}

	public AESPKCS5Padding(String secret_key) {
		init(secret_key);
	}

	private String secret_key = "";
	private String charset = "UTF-8";

	Cipher cipher = null;
	SecretKeyFactory keyFactory = null;
	SecretKey secretKey = null;
	IvParameterSpec ivParameterSpec = null;

	public String getSecret_key() {
		return secret_key;
	}

	public void setSecret_key(String secret_key) {
		if (secret_key != null && !secret_key.equals(this.secret_key)) {
			try {
				byte[] buffKey = secret_key.getBytes(charset);
				DESKeySpec desKeySpec = new DESKeySpec(buffKey);
				secretKey = keyFactory.generateSecret(desKeySpec);
				ivParameterSpec = new IvParameterSpec(buffKey);
			} catch (Exception e) {
			}
		}
		this.secret_key = secret_key;
	}

	public void setCharset(String charset) {
		this.charset = charset;
		if (this.charset == null || "".equals(this.charset)) {
			this.charset = "UTF-8";
		}
	}

	public void init(String secret_key) {
		try {
			if (cipher == null) {
				cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
				keyFactory = SecretKeyFactory.getInstance("DES");
			}
			setSecret_key(secret_key);
		} catch (Exception e) {
		}
	}

	/** 解密 **/
	public String decrypt(String message) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			byte[] bytesrc = convertHexString(message);
			byte[] retByte = cipher.doFinal(bytesrc);
			return new String(retByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/** 加密 **/
	public String encrypt(String message) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			return toHexString(cipher.doFinal(message.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}
		return digest;
	}

	public static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}

	static AESPKCS5Padding _self;

	public static AESPKCS5Padding getInstance() {
		if (_self == null)
			_self = new AESPKCS5Padding();
		return _self;
	}

	public static void main(String[] args) throws Exception {
		AESPKCS5Padding obj = AESPKCS5Padding.getInstance();
		String org = "众人所知1234567abcdefg";
		String eV = obj.encrypt(org);
		String dV = obj.decrypt(eV);
		System.out.println(eV);
		System.out.println(dV);
	}
}
