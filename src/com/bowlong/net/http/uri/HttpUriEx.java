package com.bowlong.net.http.uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.net.http.HttpBaseEx;
import com.bowlong.util.ExceptionEx;

/**
 * web Http 请求
 * 
 * @author Canyon
 * @version createtime：2015年8月17日上午11:57:38
 */
public class HttpUriEx extends HttpBaseEx {

	static Log log = getLog(HttpUriEx.class);

	// 4.3版本的超时
	static final HttpClient httpclient4_3(int tiOutCon, int tiOutSo) {
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		// 设置是否可以重定向
		params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

		// 请求超时
		if (tiOutCon <= 0) {
			tiOutCon = defaultConRequTimeout;
		}
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, tiOutCon);

		// 读取超时
		if (tiOutSo <= 0) {
			tiOutSo = defaultSoTimeout;
		}
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, tiOutSo);
		return client;
	}

	// 4.5版本的超时
	static final CloseableHttpClient httpclient4_5(int tiOutCon, int tiOutSo) {
		// 请求超时
		if (tiOutCon <= 0) {
			tiOutCon = defaultConRequTimeout;
		}

		// 读取超时
		if (tiOutSo <= 0) {
			tiOutSo = defaultSoTimeout;
		}

		RequestConfig cfg = RequestConfig.custom().setConnectTimeout(defaultTimeout)
				.setConnectionRequestTimeout(tiOutCon).setSocketTimeout(tiOutSo).setRedirectsEnabled(true).build();

		// HttpClients.createDefault();
		return HttpClients.custom().setDefaultRequestConfig(cfg).build();
	}

	static public final byte[] execute(HttpUriRequest requ, int tiOutCon, int tiOutSo) {
		CloseableHttpClient client = null;
		CloseableHttpResponse res = null;
		try {
			client = httpclient4_5(tiOutCon, tiOutSo);
			res = client.execute(requ);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				try(InputStream ins = res.getEntity().getContent()){
					return inps2Bytes(ins);
				}
			}
		} catch (Exception e) {
			logError(e, log);
		} finally {
			try {
				if (res != null)
					res.close();
			} catch (Exception e) {
				logError(e, log);
			}
			try {
				if (client != null) {
					// client.getConnectionManager().shutdown();
					client.close();
				}
			} catch (IOException e) {
				logError(e, log);
			}
		}
		return null;
	}

	static public final byte[] execute(HttpUriRequest request) {
		return execute(request, 0, 0);
	}

	static public final InputStream readInStream(HttpResponse response) throws Exception {
		if (response == null)
			return null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity en = response.getEntity();
			return en.getContent();
		}
		return null;
	}

	/*** 因为netty服务器是异步的 **/
	static public final String readStr4Netty(HttpResponse response, String charset) {
		if (response == null)
			return "";
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				charset = reCharset(charset, refBl);
				boolean isSup = refBl.val;

				HttpEntity en = response.getEntity();
				InputStream ins = en.getContent();
				byte[] bts = B2InputStream.readStream(ins);
				if (isSup) {
					return new String(bts, charset);
				} else {
					return new String(bts);
				}
			} catch (Exception e) {
				return ExceptionEx.e2s(e);
			}
		}

		return "";
	}

	static public final String readStr(HttpResponse response, String charset) {
		if (response == null)
			return "";
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {
				charset = reCharset(charset, refBl);
				boolean isSup = refBl.val;

				HttpEntity en = response.getEntity();
				InputStream ins = en.getContent();

				StringBuffer buff = new StringBuffer();
				BufferedReader br = null;
				if (isSup) {
					br = new BufferedReader(new InputStreamReader(ins, charset));
				} else {
					br = new BufferedReader(new InputStreamReader(ins));
				}
				String readLine = null;
				while ((readLine = br.readLine()) != null) {
					buff.append(readLine);
				}
				ins.close();
				br.close();
				return buff.toString();
			} catch (Exception e) {
				return ExceptionEx.e2s(e);
			}
		}

		return "";
	}
}
