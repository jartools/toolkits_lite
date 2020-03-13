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
import com.bowlong.text.EncodingEx;
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
	static final public int defaultConRequTimeout = 59000;
	/** 回应超时时间 */
	static final public int defaultSoTimeout = 59000;

	static private Map<String, String> mdefHead = newMapT(), mcustHead = newMapT();

	static final protected Map<String, String> getDefHead() {
		if (mdefHead.isEmpty()) {
			// 设置接受所有类型
			mdefHead.put("Accept", "*/*");
			mdefHead.put("Accept-Charset", EncodingEx.UTF_8);
			// 维持长连接 Keep-Alive close
			// mdefHead.setHeader("Connection", "Keep-Alive");
			mdefHead.put("Connection", "close");
			// 编码格式
			mdefHead.put("Charset", EncodingEx.UTF_8);
			mdefHead.put("User-Agent", Browser.ch360);
			// post.setHeader("Expect", "100-continue"); // 1.1 规则? 有问题
		}
		return mdefHead;
	}

	static final public void clearCustHead() {
		mcustHead.clear();
	}

	static final public void setCustHead(String... kvals) {
		clearCustHead();
		if (!isEmpty(kvals) && kvals.length >= 2) {
			int lens = kvals.length;
			for (int i = 0; (i + 1) < lens; i += 2) {
				mcustHead.put(kvals[i], kvals[i + 1]);
			}
		}
	}

	static final protected Map<String, String> getMapHead() {
		Map<String, String> _ret = newMapT();
		copy(getDefHead(), _ret, true);
		copy(mcustHead, _ret, true);
		return _ret;
	}

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
			if (isEmpty(data))
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
