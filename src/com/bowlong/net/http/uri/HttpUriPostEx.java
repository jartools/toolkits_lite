package com.bowlong.net.http.uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.bowlong.lang.StrEx;
import com.bowlong.net.http.Browser;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.MapEx;

public class HttpUriPostEx extends HttpUriEx {

	static Log log = getLog(HttpUriPostEx.class);

	static final public byte[] postMap(String host, Map<String, ?> params, String charset) {

		if (StrEx.isEmptyTrim(host)) {
			return null;
		}

		charset = reCharset(charset);
		try {
			URL url = new URL(host);
			HttpPost post = new HttpPost(url.toURI());
			// long contentLength = 0l;
			if (!MapEx.isEmpty(params)) {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				for (Entry<String, ?> ent : params.entrySet()) {
					String name = ent.getKey();
					String value = ent.getValue().toString();
					NameValuePair nvParame = new BasicNameValuePair(name, value);
					nvps.add(nvParame);
				}

				UrlEncodedFormEntity urlencode = new UrlEncodedFormEntity(nvps, charset);
				post.setEntity(urlencode);
				// contentLength = urlencode.getContentLength();
			}
			for (Entry<String, String> entry : getMapHead().entrySet()) {
				post.setHeader(entry.getKey(), entry.getValue());
			}
			post.setHeader("Accept-Charset", charset);
			return execute(post);
		} catch (Exception e) {
			logError(e, log);
		}

		return null;
	}

	static final public byte[] postMap(String host, Map<String, ?> params) {
		return postMap(host, params, EncodingEx.UTF_8);
	}

	static final public byte[] postStr(String host, String params, String charset) {
		if (StrEx.isEmptyTrim(host)) {
			return null;
		}

		charset = reCharset(charset);

		try {
			URL url = new URL(host);
			HttpPost post = new HttpPost(url.toURI());
			if (!StrEx.isEmpty(params)) {
				HttpEntity httpEn = new StringEntity(params, charset);
				post.setEntity(httpEn);
			}
			post.setHeader("Accept", "*/*");
			post.setHeader("Accept-Charset", charset);
			post.setHeader("Connection", "close"); // Keep-Alive close
			post.setHeader("User-Agent", Browser.ch360);
			return execute(post);
		} catch (Exception e) {
			logError(e, log);
		}
		return null;
	}

	static final public byte[] postStr(String host, String params) {
		return postStr(host, params, EncodingEx.UTF_8);
	}

	static final public byte[] postJson4Map(String host, Map<String, ?> params, String charset) {
		if (StrEx.isEmptyTrim(host)) {
			return null;
		}
		String strJson = buildStrByJSON4Obj(params);
		return postStr(host, strJson, charset);
	}

	static final public byte[] postJson4Map(String host, Map<String, ?> params) {
		return postJson4Map(host, params, EncodingEx.UTF_8);
	}
}
