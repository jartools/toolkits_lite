package com.bowlong.basic;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.bowlong.security.Escape;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.Ref;

/**
 * url请求参数封装<br/>
 * 
 * @author Canyon
 * @version 2019-03-04 19:29
 */
public class EOURL extends EODateFmt {
	static final public String reCharset(String charset, Ref<Boolean> refSupport) {
		boolean isSupported = !isEmptyTrim(charset);
		if (isSupported) {
			isSupported = EncodingEx.isSupported(charset);
			if (!isSupported) {
				charset = EncodingEx.UTF_8;
				isSupported = true;
			}
		}
		if (refSupport != null)
			refSupport.val = isSupported;
		return charset;
	}

	static final public String reCharset(String charset) {
		charset = reCharset(charset, refBl);
		if (!refBl.val)
			charset = EncodingEx.UTF_8;
		return charset;
	}

	static final public Charset reCharset2(String charset) {
		charset = reCharset(charset, refBl);
		if (!refBl.val)
			charset = EncodingEx.UTF_8;
		return Charset.forName(charset);
	}

	static final public String escape(String v) throws Exception {
		return Escape.escape(v);
	}

	static final public String unescape(String v) throws Exception {
		return Escape.unescape(v);
	}

	static final public String urlEncode(String v, String charset) throws Exception {
		return URLEncoder.encode(v, charset);
	}

	static final public String urlEncode(String v) throws Exception {
		return urlEncode(v, EncodingEx.UTF_8);
	}

	static final public String urlDecode(String v, String charset) throws Exception {
		v = v.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
		v = v.replaceAll("\\+", "%2B");
		v = v.replaceAll(" ", "%20"); // 空格
		v = v.replaceAll(";", "%3B");
		v = v.replaceAll("/", "%2F"); // 斜线
		v = v.replaceAll("&", "%26");
		v = v.replaceAll("@", "%40");
		return URLDecoder.decode(v, charset);
	}

	static final public String urlDecode(String v) throws Exception {
		return urlDecode(v, EncodingEx.UTF_8);
	}

	/*** GET参数编码 */
	static final public String buildQuery(Map<?, ?> data, String charset, boolean isOrderKey) {
		if (isEmpty(data))
			return "";

		String ret = "";
		charset = reCharset(charset, refBl);
		boolean isSup = refBl.val;

		Object[] keys = data.keySet().toArray();
		if (isOrderKey)
			Arrays.sort(keys);
		StringBuffer buff = new StringBuffer();
		int lens = keys.length;
		try {
			String k, v;
			Object vv;
			for (int i = 0; i < lens; i++) {
				k = keys[i].toString();
				vv = data.get(k);
				if (vv == null)
					continue;
				v = vv.toString();
				if (!isEmpty(v)) {
					if (isSup) {
						k = urlEncode(k, charset);
						v = urlEncode(v, charset);
					}
				}
				buff.append(k).append("=").append(v);
				if (i < lens - 1)
					buff.append("&");
			}
		} catch (Exception e) {
		}

		ret = buff.toString();
		buff.setLength(0);
		return ret;
	}

	static final public String buildQuery(Map<?, ?> data, String charset) {
		return buildQuery(data, charset, false);
	}

	static final public String buildQuery(Map<String, ?> data, boolean isOrderKey) {
		return buildQuery(data, EncodingEx.UTF_8, isOrderKey);
	}

	static final public String buildQuery(Map<String, ?> data) {
		return buildQuery(data, false);
	}

	static final public Map<String, Object> buildDecode(Map<String, Object> map, String k, String v) {
		if (isNull(v) || isNull(map) || isEmpty(k))
			return map;
		try {
			if (v.matches("(.*)?(%u[0-9a-fA-F]{4})+(.*)?")) {
				v = unescape(v);
			}
			if (v.matches("(.*)?(%[0-9a-fA-F]{2})+(.*)?")) {
				v = urlDecode(v);
			}
		} catch (Exception e) {
		}

		if (map.containsKey(k)) {
			v = map.get(k) + "," + v;
		}
		map.put(k, v);
		return map;
	}

	/*** GET参数转换为map对象 */
	static final public Map<String, Object> buildMapByQuery(String query) {
		Map<String, Object> ret = new HashMap<String, Object>();
		if (!isEmptyTrim(query)) {
			int ind = query.indexOf("?");
			if (ind != -1)
				query = query.substring(ind + 1);
			String[] params = query.split("&");
			for (String item : params) {
				if (isEmptyTrim(item))
					continue;
				int index = item.indexOf("=");
				if (index < 0)
					continue;
				String k = item.substring(0, index);
				String v = item.substring(index + 1);
				ret = buildDecode(ret, k, v);
			}
		}
		return ret;
	}

	/*** 取得参数的字节流 **/
	static final public byte[] getBytes(String src, String charset) {
		byte[] ret = new byte[0];
		if (!isEmptyTrim(src)) {
			charset = reCharset(charset, refBl);
			boolean isSup = refBl.val;
			try {
				if (isSup) {
					ret = src.getBytes(charset);
				} else {
					ret = src.getBytes();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	static final public byte[] getBytes(String src) {
		return getBytes(src, EncodingEx.UTF_8);
	}
}
