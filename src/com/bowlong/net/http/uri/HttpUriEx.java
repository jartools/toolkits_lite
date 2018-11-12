package com.bowlong.net.http.uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.lang.StrEx;
import com.bowlong.net.http.HttpBaseEx;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.ExceptionEx;

/**
 * web Http 请求
 * 
 * @author Canyon
 * @version createtime：2015年8月17日上午11:57:38
 */
public class HttpUriEx extends HttpBaseEx {

	static Log log = LogFactory.getLog(HttpUriEx.class);

	static public final CloseableHttpResponse execute(HttpUriRequest request,
			int timeOutCon, int timeOutSo) {
		CloseableHttpClient client = null;
		try {
			client = HttpClients.createDefault();
			HttpParams params = client.getParams();
			// 设置是否可以重定向
			params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);

			// 请求超时
			if (timeOutCon <= 0) {
				timeOutCon = defaultConnectionTimeout;
			}
			params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
					timeOutCon);

			// 读取超时
			if (timeOutSo <= 0) {
				timeOutSo = defaultSoTimeout;
			}
			params.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeOutSo);
			return client.execute(request);
		} catch (Exception e) {
			log.error(ExceptionEx.e2s(e));
		} finally {
			if (client != null) {
				try {
					// client.getConnectionManager().shutdown();
					client.close();
				} catch (IOException e) {
					log.error(ExceptionEx.e2s(e));
				}
			}
		}
		return null;
	}

	static public final CloseableHttpResponse execute(HttpUriRequest request) {
		return execute(request, 0, 0);
	}

	static public final InputStream readInStream(HttpResponse response)
			throws Exception {
		if (response == null)
			return null;
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity en = response.getEntity();
			return en.getContent();
		}
		return null;
	}

	/*** 因为netty服务器是异步的 **/
	static public final String readStr4Netty(HttpResponse response,
			String charset) {
		if (response == null)
			return "";
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			try {

				boolean isSupported = !StrEx.isEmptyTrim(charset);
				if (isSupported) {
					isSupported = EncodingEx.isSupported(charset);
					if (!isSupported) {
						charset = EncodingEx.UTF_8;
						isSupported = true;
					}
				}

				HttpEntity en = response.getEntity();
				InputStream ins = en.getContent();
				byte[] bts = B2InputStream.readStream(ins);
				if (isSupported) {
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

				boolean isSupported = !StrEx.isEmptyTrim(charset);
				if (isSupported) {
					isSupported = EncodingEx.isSupported(charset);
					if (!isSupported) {
						charset = EncodingEx.UTF_8;
						isSupported = true;
					}
				}

				HttpEntity en = response.getEntity();
				InputStream ins = en.getContent();

				StringBuffer buff = new StringBuffer();
				BufferedReader br = null;
				if (isSupported) {
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
