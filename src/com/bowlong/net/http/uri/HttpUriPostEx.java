package com.bowlong.net.http.uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.bowlong.lang.StrEx;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.MapEx;

public class HttpUriPostEx extends HttpUriEx {

	static Log log = LogFactory.getLog(HttpUriPostEx.class);

	static public final HttpResponse postMap(String host,
			Map<String, String> params, String charset) {

		if (StrEx.isEmptyTrim(host)) {
			return null;
		}

		boolean isSupported = !StrEx.isEmptyTrim(charset);
		if (isSupported) {
			isSupported = EncodingEx.isSupported(charset);
			if (!isSupported) {
				charset = EncodingEx.UTF_8;
				isSupported = true;
			}
		}

		try {
			URL url = new URL(host);
			HttpPost post = new HttpPost(url.toURI());
			// long contentLength = 0l;
			if (!MapEx.isEmpty(params)) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Entry<String, String> parame : params.entrySet()) {
					String name = parame.getKey();
					String value = parame.getValue();
					NameValuePair nvParame = new BasicNameValuePair(name, value);
					nvps.add(nvParame);
				}

				UrlEncodedFormEntity urlencode = null;
				if (isSupported) {
					urlencode = new UrlEncodedFormEntity(nvps, charset);
				} else {
					urlencode = new UrlEncodedFormEntity(nvps);
				}
				post.setEntity(urlencode);
				// contentLength = urlencode.getContentLength();
			}
			post.setHeader("Accept", "*/*");
			if (isSupported) {
				post.setHeader("Accept-Charset", charset);
			} else {
				post.setHeader("Accept-Charset", EncodingEx.UTF_8);
			}
			// post.setHeader("Connection", "Keep-Alive");
			post.setHeader("Connection", "close");
			post.setHeader("User-Agent", UseAgent3);
			return execute(post);
		} catch (Exception e) {
			log.error(ExceptionEx.e2s(e));
		}

		return null;
	}

	static public final HttpResponse postStr(String host, String params,
			String charset) {

		if (StrEx.isEmptyTrim(host)) {
			return null;
		}

		boolean isSupported = !StrEx.isEmptyTrim(charset);
		if (isSupported) {
			isSupported = EncodingEx.isSupported(charset);
			if (!isSupported) {
				charset = EncodingEx.UTF_8;
				isSupported = true;
			}
		}

		try {
			URL url = new URL(host);
			HttpPost post = new HttpPost(url.toURI());
			// long contentLength = 0l;
			if (!StrEx.isEmpty(params)) {
				HttpEntity httpEn = null;
				if (isSupported) {
					httpEn = new StringEntity(params, charset);
				} else {
					httpEn = new StringEntity(params);
				}
				post.setEntity(httpEn);
				// contentLength = httpEn.getContentLength();
			}
			post.setHeader("Accept", "*/*");
			if (isSupported) {
				post.setHeader("Accept-Charset", charset);
			} else {
				post.setHeader("Accept-Charset", EncodingEx.UTF_8);
			}
			// post.setHeader("Connection", "Keep-Alive");
			post.setHeader("Connection", "close");
			post.setHeader("User-Agent", UseAgent3);
			return execute(post);
		} catch (Exception e) {
			log.error(ExceptionEx.e2s(e));
		}
		return null;
	}

	static public final HttpResponse postJson4Map(String host,
			Map<String, String> params, String charset) {
		if (StrEx.isEmptyTrim(host)) {
			return null;
		}
		String strJson = buildStrByJSON4Obj(params);
		return postStr(host, strJson, charset);
	}
}
