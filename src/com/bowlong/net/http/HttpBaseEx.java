package com.bowlong.net.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bowlong.lang.InputStreamEx;
import com.bowlong.lang.NumFmtEx;
import com.bowlong.util.DateEx;
import com.bowlong.util.MapEx;

/**
 * 
 * @author Canyon
 * @version createtime：2015年8月17日下午7:50:55
 */
@SuppressWarnings({ "rawtypes" })
public class HttpBaseEx extends InputStreamEx {
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
		objLog.error(e2s(ex));
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
			Process pro = exec("ping " + pingUrl + " -l 1000 -n 4");

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
