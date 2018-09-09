package com.bowlong.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本内容解码
 * 
 * @author Canyon
 * @version createtime：2018-08-09 10:07
 */
public class DecodeEx {

	static private String unescapeUnicode(String str){
		StringBuffer _builder = new StringBuffer();
		Matcher m = Pattern.compile("\\\\u([0-9a-fA-F]{4})").matcher(str);
		while (m.find())
			_builder.append((char) Integer.parseInt(m.group(1), 16));
		return _builder.toString();
	}
	
	static public String unUnicode(String str) {
		String ret = unescapeUnicode(str);
		if("".equals(ret)){
			try {
				ret = new String(str.getBytes(), "utf-8");
			} catch (Exception e) {
			}
		}
		return ret;
	}
}
