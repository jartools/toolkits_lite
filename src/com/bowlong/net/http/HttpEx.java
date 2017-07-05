package com.bowlong.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.net.http.urlcon.HttpUrlConEx;
/**
 * @author Canyon
 * @version createtime：2015年8月17日 下午10:31:15
 */
public class HttpEx extends HttpUrlConEx {
	public static final byte[] readUrl(String url) throws Exception {
		InputStream is = openUrl(url);
		try {
			return readStream(is);
		} finally {
			is.close();
		}
	}

	public static final byte[] readUrl(String url, byte[] post)
			throws Exception {
		HttpURLConnection huc = openUrl(url, post);
		InputStream is = huc.getInputStream();
		try {
			return readStream(is);
		} finally {
			is.close();
			huc.disconnect();
		}
	}

	public static final byte[] readUrl(URL url) throws Exception {
		InputStream is = openUrl(url);
		try {
			return readStream(is);
		} finally {
			is.close();
		}
	}

	public static final InputStream openUrl(String url) throws Exception {
		return openUrl(new URL(url));
	}

	public static HttpURLConnection openUrl(String url, byte[] post)
			throws Exception {
		URL u = new URL(url);
		HttpURLConnection huc = (HttpURLConnection) u.openConnection();
		huc.setDoOutput(true);
		huc.setRequestMethod("POST");
		huc.setRequestProperty("Content-type", "text/html; charset=utf-8");
		huc.setRequestProperty("Connection", "close");
		huc.setRequestProperty("Content-Length", String.valueOf(post.length));
		huc.getOutputStream().write(post);
		huc.getOutputStream().flush();
		huc.getOutputStream().close();
		return huc;
	}

	public static final InputStream openUrl(URL url) throws IOException {
		return url.openStream();
	}

	private static byte[] readStream(InputStream is) throws IOException {
		return B2InputStream.readStream(is);
	}

	// private static final ByteOutStream newStream(int size) {
	// return new ByteOutStream(size);
	// }

}
