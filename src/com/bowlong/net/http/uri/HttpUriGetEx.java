package com.bowlong.net.http.uri;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.http.client.methods.HttpGet;

import com.bowlong.lang.StrEx;
import com.bowlong.net.http.Browser;
import com.bowlong.text.EncodingEx;

public class HttpUriGetEx extends HttpUriEx {

	static Log log = getLog(HttpUriGetEx.class);

	/*
	 * This interface represents a collection of HTTP protocol parameters.
	 * HttpParams是一种HTTP协议参数。支持HTTP 1.0和HTTP 1.1 HttpParams httpParams = new
	 * BasicHttpParams();
	 */
	static final public byte[] queryStr(String host, String params, String charset) {
		if (StrEx.isEmptyTrim(host)) {
			return null;
		}

		charset = reCharset(charset);

		try {
			URL url = null;
			if (StrEx.isEmptyTrim(params)) {
				url = new URL(host);
			} else {
				params = URLEncoder.encode(params, charset);
				url = new URL(host + "?" + params);
			}

			HttpGet get = new HttpGet(url.toURI());
			get.setHeader("Accept", "*/*");
			get.setHeader("Accept-Charset", charset);
			// get.setHeader("Connection", "Keep-Alive");
			get.setHeader("Connection", "close");
			get.setHeader("User-Agent", Browser.ch360);
			get.setHeader("Expect", "100-continue"); // 1.1 规则?
			return execute(get);
		} catch (Exception e) {
			logError(e, log);
		}
		return null;
	}

	static final public byte[] queryStr(String host, String params) {
		return queryStr(host, params, EncodingEx.UTF_8);
	}

	static final public byte[] queryMapByStr(String host, Map<String, ?> params, String charset) {
		if (StrEx.isEmptyTrim(host)) {
			return null;
		}
		String strJson = buildQuery(params, charset);
		return queryStr(host, strJson, null);
	}

	static final public byte[] queryMapByStr(String host, Map<String, ?> params) {
		return queryMapByStr(host, params, EncodingEx.UTF_8);
	}
}
