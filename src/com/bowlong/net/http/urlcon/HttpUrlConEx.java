package com.bowlong.net.http.urlcon;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.logging.Log;

import com.bowlong.lang.StrEx;
import com.bowlong.net.http.Browser;
import com.bowlong.net.http.HttpBaseEx;
import com.bowlong.text.EncodingEx;

/**
 * URLConnection,HttpURLConnection <br/>
 * params以字节流的方式发送内容Object 接受内容需要在Body的getContent里面 <br/>
 * query : 参数k=v&参数k=v
 * 
 * @author Canyon
 * @version createtime：2015年8月17日 下午10:31:15
 */
public class HttpUrlConEx extends HttpBaseEx {

	static Log log = getLog(HttpUrlConEx.class);

	static public final byte[] send(String url, String query, byte[] params, boolean isPost, int timeOutCon, int timeOutSo) {
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
			// 设置请求数据类型 - 浏览器编码类型
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 编码格式
			conn.setRequestProperty("Charset", EncodingEx.UTF_8);
			// 设置是否使用缓存 - POST不能使用缓存?
			conn.setUseCaches(false);
			// 维持长连接 Keep-Alive close
			conn.setRequestProperty("Connection", "close");
			// 设置接受所有类型
			conn.setRequestProperty("Accept-Charset", EncodingEx.UTF_8);
			conn.setRequestProperty("accept", "*/*");
			if (isOut) {
				conn.setRequestProperty("Content-Length", String.valueOf(lens4params));
			}
			conn.setRequestProperty("user-agent", Browser.ch360);
			// 请求超时
			timeOutCon = (timeOutCon <= 0) ? defaultConRequTimeout : timeOutCon;
			conn.setConnectTimeout(timeOutCon);

			// 读取超时
			timeOutSo = (timeOutSo <= 0) ? defaultSoTimeout : timeOutSo;
			conn.setReadTimeout(timeOutSo);

			// 是否设置输入的内容
			conn.setDoInput(true);
			// 是否设置输出的内容
			conn.setDoOutput(isOut);

			conn.setRequestMethod(isPost ? "POST" : "GET");

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
			int respCode = conn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == respCode) {
				return inps2Bytes(conn.getInputStream());
			} else {
				return inps2Bytes(conn.getErrorStream());
			}
		} catch (Exception e) {
			logError(e, log);
		}
		return null;
	}

	static public final byte[] send(String url, String query, byte[] params, boolean isPost) {
		return send(url, query, params, isPost, 0, 0);
	}

	static public final byte[] sendBytes(String url, byte[] params, boolean isPost, int timeOutCon, int timeOutSo) {
		return send(url, "", params, isPost, timeOutCon, timeOutSo);
	}

	static public final byte[] sendBytes(String url, byte[] params, boolean isPost) {
		return sendBytes(url, params, isPost, 0, 0);
	}

	static public final byte[] sendStr(String url, String params, boolean isPost, int timeOutCon, int timeOutSo, String charset) {
		byte[] btParams = getBytes(params, charset);
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
			logError(e, log);
		}
		return null;
	}

	static public final byte[] sendParams(String url, Map<String, ?> map, String charset, boolean isPost) {
		String query = buildQuery(map, charset);
		if (isPost) {
			return send(url, "", query.getBytes(), isPost);
		} else {
			return send(url, query, null, isPost);
		}
	}

	static final public byte[] postParams(String url, Map<String, ?> map, String charset) {
		return sendParams(url, map, charset, true);
	}

	static final public byte[] postParams(String url, Map<String, ?> map) {
		return postParams(url, map, "utf-8");
	}

	static final public byte[] queryParams(String url, Map<String, ?> map, String charset) {
		return sendParams(url, map, charset, false);
	}

	static final public byte[] queryParams(String url, Map<String, ?> map) {
		return queryParams(url, map, "utf-8");
	}

	static final public byte[] sendParams4Json(String url, Map<String, ?> map, String charset, boolean isPost) {
		String query = buildStrByJSON4Obj(map);
		if (isPost) {
			return send(url, "", getBytes(query, charset), isPost);
		} else {
			return send(url, query, null, isPost);
		}
	}

	static final public byte[] postParams4Json(String url, Map<String, ?> map, String charset) {
		return sendParams4Json(url, map, charset, true);
	}

	static final public byte[] postParams4Json(String url, Map<String, ?> map) {
		return postParams4Json(url, map, "utf-8");
	}
}