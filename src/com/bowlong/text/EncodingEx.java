package com.bowlong.text;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class EncodingEx extends Encoding {

	static public final String getCharsetStr(InputStream inps) {
		String code = "UTF-8";
		try {
			byte[] head = new byte[3];
			inps.read(head);
			if (head[0] == -2 && head[1] == -1) {
				if (head[2] == 0) {
					code = "UTF-16BE";
				} else {
					code = "UTF-16";
				}
			} else if (head[0] == -1 && head[1] == -2) {
				code = "Unicode";
			} else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
				code = "UTF-8";
			else if (head[0] == -76 && head[1] == -85 && head[2] == -71)
				code = "gb2312";
			else if (head[0] == 49 && head[1] == 50 && head[2] == 51)
				code = "GBK";
		} catch (Exception e) {
		}
		return code;
	}

	static public final String getCharsetStr(String path) {
		String code = "UTF-8";
		try (InputStream inps = new FileInputStream(path);) {
			code = getCharsetStr(inps);
		} catch (Exception e) {
		}
		return code;
	}

	static public final Charset getCharset(String path) {
		return Charset.forName(getCharsetStr(path));
	}

	public static final String getCodeStr(InputStream inps) {
		String code = "UTF-8";
		try (BufferedInputStream bin = new BufferedInputStream(inps);) {
			int p = (bin.read() << 8) + bin.read();
			switch (p) {
			case 12594:
				code = "GBK";
			case 21844:
				code = "UTF-8";
				break;
			case 0xefbb:
				code = "UTF-8";
				break;
			case 0xfffe:
				code = "Unicode";
				break;
			case 0xfeff:
				code = "UTF-16BE";
				break;
			default:
				code = "gb2312";
			}
		} catch (Exception e) {
		}
		return code;
	}

	public static final String getCodeStr(String path) {
		String code = "UTF-8";
		try (InputStream inps = new FileInputStream(path);) {
			code = getCodeStr(inps);
		} catch (Exception e) {
		}
		return code;
	}

	static public final Charset getCodeCharset(String path) {
		return Charset.forName(getCodeStr(path));
	}
}
