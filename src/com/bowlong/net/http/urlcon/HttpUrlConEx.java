package com.bowlong.net.http.urlcon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.lang.StrEx;
import com.bowlong.net.http.HttpBaseEx;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.ExceptionEx;

/**
 * URLConnection,HttpURLConnection <br/>
 * params以字节流的方式发送内容Object 接受内容需要在Body的getContent里面 <br/>
 * query : 参数k=v&参数k=v
 * 
 * @author Canyon
 * @version createtime：2015年8月17日 下午10:31:15
 */
public class HttpUrlConEx extends HttpBaseEx {

	static Log log = LogFactory.getLog(HttpUrlConEx.class);

	static public final InputStream send(String url, String query,
			byte[] params, boolean isPost, int timeOutCon, int timeOutSo) {
		HttpURLConnection conn = null;
		try {
			int lens4params = 0;
			if (params != null && params.length > 0) {
				lens4params = params.length;
			}
			boolean isOut = lens4params > 0;

			URL reqUrl = null;
			if (StrEx.isEmptyTrim(query)) {
				reqUrl = new URL(url);
			} else {
				reqUrl = new URL(url + "?" + query);
			}
			// 打开和URL之间的连接
			conn = (HttpURLConnection) reqUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			// 编码格式
			conn.setRequestProperty("Charset", EncodingEx.UTF_8);
			// 不允许使用缓存
			conn.setUseCaches(false);
			// Keep-Alive
			conn.setRequestProperty("Connection", "close");
			if (isOut) {
				conn.setRequestProperty("Content-Length",
						String.valueOf(lens4params));
			}
			conn.setRequestProperty("user-agent", UseAgent3);
			// 请求超时
			if (timeOutCon <= 0) {
				timeOutCon = defaultConnectionTimeout;
			}
			conn.setConnectTimeout(timeOutCon);
			// 读取超时
			if (timeOutSo <= 0) {
				timeOutSo = defaultSoTimeout;
			}
			conn.setReadTimeout(timeOutSo);

			// 是否设置输入的内容
			conn.setDoInput(true);
			// 是否设置输出的内容
			conn.setDoOutput(isOut);

			if (isPost) {
				conn.setRequestMethod("POST");
			} else {
				conn.setRequestMethod("GET");
			}

			// 建立实际的连接
			conn.connect();

			if (isOut) {
				OutputStream outPut = conn.getOutputStream();
				outPut.write(params);
				outPut.flush();
				outPut.close();
			}

			// 获取所有响应头字段
			// Map<String, List<String>> map = conn.getHeaderFields();
			try {
				return conn.getInputStream();
			} catch (IOException e) {
				log.info(ExceptionEx.e2s(e));
				return conn.getErrorStream();
			}
		} catch (Exception e) {
			log.info(ExceptionEx.e2s(e));
			return null;
		}
	}

	static public final InputStream sendBytes(String url, byte[] params,
			boolean isPost, int timeOutCon, int timeOutSo) {
		return send(url, "", params, isPost, timeOutCon, timeOutSo);
	}

	static public final InputStream sendBytes(String url, byte[] params,
			boolean isPost) {
		return sendBytes(url, params, isPost, 0, 0);
	}

	static public final InputStream sendStr(String url, String params,
			boolean isPost, int timeOutCon, int timeOutSo, String charset) {
		byte[] btParams = getBytes4Str(params, charset);
		return sendBytes(url, btParams, isPost, timeOutCon, timeOutSo);
	}

	static public final InputStream sendPostXml(String url, byte[] bes) {
		if (bes == null)
			return null;
		try {
			URL reqUrl = new URL(url);
			URLConnection con = reqUrl.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStream out = con.getOutputStream();
			out.write(bes);
			out.flush();
			out.close();

			return con.getInputStream();
		} catch (Exception e) {
			log.error(ExceptionEx.e2s(e));
		}
		return null;
	}

	static public final InputStream sendParams(String url,
			Map<String, String> map, String charset, boolean isPost) {
		String query = buildQuery(map, charset);
		if (isPost) {
			return send(url, "", query.getBytes(), isPost, 0, 0);
		} else {
			return send(url, query, null, isPost, 0, 0);
		}
	}

	static public InputStream postParams(String url, Map<String, String> map,
			String charset) {
		return sendParams(url, map, charset, true);
	}

	static public InputStream queryParams(String url, Map<String, String> map,
			String charset) {
		return sendParams(url, map, charset, false);
	}

	static public final InputStream sendParams4Json(String url,
			Map<String, String> map, String charset, boolean isPost) {
		String query = buildStrByJSON4Obj(map);
		if (isPost) {
			return send(url, "", getBytes4Str(query, charset), isPost, 0, 0);
		} else {
			return send(url, query, null, isPost, 0, 0);
		}
	}

	static public InputStream postParams4Json(String url,
			Map<String, String> map, String charset) {
		return sendParams4Json(url, map, charset, true);
	}
}