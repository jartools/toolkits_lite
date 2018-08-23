package com.bowlong.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.tools.ToolProvider;

@SuppressWarnings("all")
public class RSAEncrypt {

	public RSAEncrypt() {
	}

	public RSAEncrypt(String publicKey) {
		try {
			loadPublicKey(publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 私钥
	 */
	private RSAPrivateKey rsaPriKey;

	/**
	 * 公钥(openssl)
	 */
	private RSAPublicKey rsaPubKey;

	/**
	 * 字节数据转字符串专用集合
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	KeyFactory keyFactory = null;
	Cipher cipher = null;

	/**
	 * 获取私钥
	 * 
	 * @return 当前的私钥对象
	 */
	public RSAPrivateKey getPrivateKey() {
		return rsaPriKey;
	}

	/**
	 * 获取公钥
	 * 
	 * @return 当前的公钥对象
	 */
	public RSAPublicKey getPublicKey() {
		return rsaPubKey;
	}

	/**
	 * 随机生成密钥对
	 */
	public void genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.rsaPriKey = (RSAPrivateKey) keyPair.getPrivate();
		this.rsaPubKey = (RSAPublicKey) keyPair.getPublic();
	}

	public RSAPublicKey toPublicKey(String publicKey) {
		try {
			if (keyFactory == null)
				keyFactory = KeyFactory.getInstance("RSA");

			byte[] buffer = Base64.decode(publicKey);
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in 公钥输入流
	 * @throws Exception 加载公钥时产生的异常
	 */
	public void loadPublicKey(InputStream in) throws Exception {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			loadPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		}
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	public void loadPublicKey(String publicKey) throws Exception {
		this.rsaPubKey = toPublicKey(publicKey);
	}

	/**
	 * 公钥加密过程
	 * 
	 * @param publicKey     公钥
	 * @param plainTextData 明文数据
	 * @return
	 * @throws Exception 加密过程中的异常信息
	 */
	public byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		try {
			// 使用默认RSA
			if (cipher == null) {
				cipher = Cipher.getInstance("RSA");
				// cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
			}
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	public byte[] encrypt(byte[] buffer) throws Exception {
		return encrypt(getPublicKey(), buffer);
	}

	/**
	 * 公钥解密过程
	 * 
	 * @param publicKey  公钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
		if (publicKey == null) {
			throw new Exception("解密公钥为空, 请设置");
		}
		try {
			if (cipher == null) {
				cipher = Cipher.getInstance("RSA");
				// cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
			}
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	public byte[] decrypt(byte[] cipherData) throws Exception {
		return decrypt(getPublicKey(), cipherData);
	}

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data 输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
			stringBuilder.append(HEX_CHAR[(data[i] & 0xf0) >>> 4]);
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
			if (i < data.length - 1) {
				stringBuilder.append(' ');
			}
		}
		return stringBuilder.toString();
	}

	/************ sign 签名 **********/
	protected Signature sign(String content, PublicKey rsaPubKey, String charset) throws Exception {
		if (charset == null || charset.length() <= 0)
			charset = "utf-8";
		Signature signature = Signature.getInstance("SHA1WithRSA");
		signature.initVerify(rsaPubKey);
		signature.update(content.getBytes(charset));
		return signature;
	}

	protected Signature sign(String content, String publicKey, String charset) throws Exception {
		return sign(content, toPublicKey(publicKey), charset);
	}

	public String signStr(String content, PublicKey rsaPubKey, String charset) {
		try {
			Signature signature = sign(content, rsaPubKey, charset);
			return Base64.encode(signature.sign());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public String signStr(String content, String publicKey, String charset) {
		try {
			Signature signature = sign(content, publicKey, charset);
			return Base64.encode(signature.sign());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	public String signStrUTF8(String content, PublicKey rsaPubKey) {
		return signStr(content, rsaPubKey, "");
	}

	public String signStrUTF8(String content, String publicKey) {
		return signStr(content, publicKey, "");
	}

	public String signStrUTF8(String content) throws Exception {
		return signStr(content, getPublicKey(), "");
	}

	/************ 验证 **********/
	public boolean verify(String content, String sign, PublicKey rsaPubKey, String input_charset) {
		try {
			Signature signature = sign(content, rsaPubKey, input_charset);
			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean verify(String content, String sign, String publicKey, String input_charset) {
		try {
			Signature signature = sign(content, publicKey, input_charset);
			return signature.verify(Base64.decode(sign));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean verify(String content, String sign) {
		return verify(content, sign, getPublicKey(), "");
	}

	static RSAEncrypt _self;

	public static RSAEncrypt getInstance() {
		if (_self == null)
			_self = new RSAEncrypt();
		return _self;
	}

	static void demoDecode() throws Exception {
		RSAEncrypt rsaEncrypt = RSAEncrypt.getInstance();
		// rsaEncrypt.genKeyPair();

		// 加载公钥
		try {
			String DEFAULT_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1BvMF7LGlQb7OEetChUg" + "\r\n"
					+ "grG6+/GpaH7os5WapsMbcRHftljf2A1Wgy3GvcbILJRcINWohuhrBQ2+PIBDyBof" + "\r\n"
					+ "eVU/LEvaT1hQyyJ3OOI1Qa/vXPtXCTUPjfKk5d+0jr7xKa1rES0xJF8s6Bpll6QA" + "\r\n"
					+ "nfuiSEbBq0O5TTFJAmPR0o9+Ity0retQ0W91O4rrCkfS2aSMsKeA5aaz1ixFwDS3" + "\r\n"
					+ "4dpAO0gqhFUvyHITWkS0n7/4MAVqCIoVSfZwIFZ7k2Bf39EouAYbkuYue6rxIlbV" + "\r\n"
					+ "wABAcopMxr4aHbbJRs7Ll62uHyio10jIHXesdz3Ur4GrKOSomay6vAaT4RjggeCv" + "\r\n" + "SwIDAQAB" + "\r\n";
			rsaEncrypt.loadPublicKey(DEFAULT_PUBLIC_KEY);
			System.out.println("加载公钥成功");
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("加载公钥失败");
		}

		// 文档测试数据
		String testDataStr = "d7C8ph77SaqWsSk+T2KpHXKuhplBdZOosP9a7XnQAziC4A0aO8yQG0RdyMz/Ya2G77V0ufOq0QyHdv25dONOwuCGrq+fUMrn+l8D5fdIsGI0mIvbVVum2A3arxuG0toMhqIlxKD88CIs2hyEMit6exRRMnFgHFjcDh1KVajHC7DecfmhRunQctPFX9Z2JxIpLMGYsqb6qKqSaO0sdfamnFpl2ozwSKBTijAECj7Xx354SiLJTqbsERWx1b5dLR/iuZpODSY9IY3RHdEJ60e+ggk1q+n5MHEdL+M9tnbqw7kYsiLYSVvFJ7YTyqSR4qGC/GyGUAJdNiiNjB8MOGsUBQ==";

		try {
			byte[] dcDataStr = Base64.decode(testDataStr);
			byte[] plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), dcDataStr);
			System.out.println("文档测试数据明文长度:" + plainData.length);
			System.out.println(RSAEncrypt.byteArrayToString(plainData));
			System.out.println(new String(plainData));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}