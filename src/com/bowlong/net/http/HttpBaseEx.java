package com.bowlong.net.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bowlong.lang.InputStreamEx;
import com.bowlong.lang.NumFmtEx;
import com.bowlong.lang.StrEx;
import com.bowlong.util.DateEx;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.MapEx;

/**
 * 
 * @author Canyon
 * @version createtime：2015年8月17日下午7:50:55
 */
@SuppressWarnings({ "rawtypes" })
public class HttpBaseEx extends InputStreamEx{

	static public String UseAgent1 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)";
	static public String UseAgent2 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";
	static public String UseAgent3 = "Mozilla/4.0";

	/** 超时时间为1分钟 */
	static final public int defaultTimeout = 60000;
	/** 连接超时时间 */
	static final public int defaultConRequTimeout = 50000;
	/** 回应超时时间 */
	static final public int defaultSoTimeout = 50000;

	static final protected void logInfo(Object obj, Log objLog) {
		objLog.info(obj);
	}

	static final protected void logError(Exception ex, Log objLog) {
		objLog.error(ExceptionEx.e2s(ex));
	}

	/*** GET参数编码 */
	static final public String buildQuery(Map<String, ?> data, String charset, boolean isOrderKey) {
		if (MapEx.isEmpty(data))
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
				if (!StrEx.isEmpty(v)) {
					if (isSup) {
						k = URLEncoder.encode(k, charset);
						v = URLEncoder.encode(v, charset);
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

	static final public String buildQuery(Map<String, ?> data, String charset) {
		return buildQuery(data, charset, false);
	}

	static final public String buildQuery(Map<String, ?> data) {
		return buildQuery(data, "", false);
	}

	static final public String buildQuery(Map<String, ?> data, boolean isOrderKey) {
		return buildQuery(data, "", isOrderKey);
	}

	static final public String buildStrByJSON4Obj(Object data) {
		try {
			return JSON.toJSONString(data);
		} catch (Exception e) {
		}
		return "";
	}

	static final public String buildStrByJSON4Map(Map data) {
		try {
			if (MapEx.isEmpty(data))
				return "";

			JSONObject json = new JSONObject();
			Map<String, String> mapKV = MapEx.toMapKV(data);
			for (Entry<String, String> entry : mapKV.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue();
				json.put(key, val);
			}
			return json.toJSONString();
		} catch (Exception e) {
		}
		return "";
	}

	/*** GET参数转换为map对象 */
	static final public Map<String, String> buildMapByQuery(String query) {
		Map<String, String> ret = new HashMap<String, String>();
		if (!StrEx.isEmptyTrim(query)) {
			boolean isFirst = query.indexOf("?") == 0;
			if (isFirst)
				query = query.substring(1);
			String[] params = query.split("&");
			for (String item : params) {
				if (StrEx.isEmptyTrim(item))
					continue;
				int index = item.indexOf("=");
				if (index < 0)
					continue;
				String k = item.substring(0, index);
				String v = item.substring(index + 1);
				if (ret.containsKey(k)) {
					v = ret.get(k) + "," + v;
				}
				ret.put(k, v);
			}
		}
		return ret;
	}

	/*** 取得参数的字节流 **/
	static final public byte[] getBytes4Str(String params, String charset) {
		byte[] btParams = new byte[0];
		if (!StrEx.isEmptyTrim(params)) {
			charset = reCharset(charset, refBl);
			boolean isSup = refBl.val;
			try {
				if (isSup) {
					btParams = params.getBytes(charset);
				} else {
					btParams = params.getBytes();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return btParams;
	}

	static public String getSpeed(String pingUrl, String charUTF) {
		Charset cset = null;
		if (!StrEx.isEmpty(charUTF)) {
			cset = Charset.forName(charUTF);
		}
		return getSpeed(pingUrl, cset);
	}

	static public String getSpeed(String pingUrl, Charset cset) {
		try {
			String line = null;
			Process pro = Runtime.getRuntime().exec("ping " + pingUrl + " -l 1000 -n 4");

			InputStream inStream = pro.getInputStream();
			BufferedReader buf = null;
			if (cset == null) {
				buf = new BufferedReader(new InputStreamReader(inStream));
			} else {
				buf = new BufferedReader(new InputStreamReader(inStream, cset));
			}
			int len = 0;
			String vEn = "Average";
			String vCn = "平均";
			while ((line = buf.readLine()) != null) {
				int position = line.indexOf(vEn);
				if (position == -1) {
					position = line.indexOf(vCn);
					len = vCn.length();
				} else {
					len = vEn.length();
				}
				if (position != -1) {
					System.out.println(line);
					String value = line.substring(position + len, line.lastIndexOf("ms"));
					value = value.replaceAll("=", "");
					value = value.trim();
					double speed = (1000d / Integer.parseInt(value)) / 1024 * DateEx.TIME_SECOND;
					double lineMB = (1024 * 1.25);
					String v = "";
					if (speed > lineMB) {
						speed /= 1024;
						v = NumFmtEx.formatDouble(speed) + "MB/s";
					} else {
						v = NumFmtEx.formatDouble(speed) + "KB/s";
					}
					System.out.println("下载速度:" + v);
					return v;
				}
			}
		} catch (Exception e) {
		}
		return "0KB/s";
	}
}
