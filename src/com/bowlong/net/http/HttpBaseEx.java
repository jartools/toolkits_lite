package com.bowlong.net.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bowlong.lang.InputStreamEx;
import com.bowlong.lang.NumFmtEx;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.DateEx;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.MapEx;

/**
 * 
 * @author Canyon
 * @version createtime：2015年8月17日下午7:50:55
 */
@SuppressWarnings({ "rawtypes" })
public class HttpBaseEx extends InputStreamEx {

	static public String UA_360 = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36";
	static public String UA_Chrome = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16";
	static public String UA_QQ = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)";

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
	static final public Map<String, Object> buildMapByQuery(String query) {
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
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
					v = URLDecoder.decode(v,EncodingEx.UTF_8);
					if (ret.containsKey(k)) {
						v = ret.get(k) + "," + v;
					}
//					v = Escape.unescape(v);
					ret.put(k, v);
				}
			}
		} catch (Exception e) {
		}
		
		return ret;
	}

	static public String getSpeed(String pingUrl, String charUTF) {
		Charset cset = null;
		if (!isEmpty(charUTF)) {
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
