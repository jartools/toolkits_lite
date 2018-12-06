package com.bowlong.net.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bowlong.bio2.B2InputStream;
import com.bowlong.io.ByteInStream;
import com.bowlong.lang.NumFmtEx;
import com.bowlong.lang.StrEx;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.DateEx;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.MapEx;
import com.bowlong.util.Ref;

/**
 * 
 * @author Canyon
 * @version createtime：2015年8月17日下午7:50:55
 */
@SuppressWarnings({ "rawtypes" })
public class HttpBaseEx {

	static public String UseAgent1 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)";
	static public String UseAgent2 = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";
	static public String UseAgent3 = "Mozilla/4.0";

	/** 超时时间为1分钟 */
	static final public int defaultTimeout = 60000;
	/** 连接超时时间 */
	static final public int defaultConRequTimeout = 10000;
	/** 回应超时时间 */
	static final public int defaultSoTimeout = 30000;

	static final protected Ref<Boolean> refObj = new Ref<Boolean>(false);

	static final protected void logInfo(Object obj, Log objLog) {
		objLog.info(obj);
	}

	static final protected void logError(Exception ex, Log objLog) {
		objLog.error(ExceptionEx.e2s(ex));
	}

	static final public String reCharset(String charset) {
		charset = reCharset(charset, refObj);
		if (!refObj.val)
			charset = EncodingEx.UTF_8;
		return charset;
	}

	static final public String reCharset(String charset, Ref<Boolean> refSupport) {
		boolean isSupported = !StrEx.isEmptyTrim(charset);
		if (isSupported) {
			isSupported = EncodingEx.isSupported(charset);
			if (!isSupported) {
				charset = EncodingEx.UTF_8;
				isSupported = true;
			}
		}
		if (refSupport != null)
			refSupport.val = isSupported;
		return charset;
	}

	/*** GET参数编码 */
	static final public String buildQuery(Map<String, ?> data, String charset, boolean isOrderKey) {
		if (MapEx.isEmpty(data))
			return "";

		String ret = "";
		charset = reCharset(charset, refObj);
		boolean isSup = refObj.val;

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

	static final public List<String> inps2LineStr(InputStream ins, String charset) {
		if (ins == null)
			return null;
		try {
			List<String> reList = new ArrayList<String>();
			charset = reCharset(charset, refObj);
			boolean isSup = refObj.val;
			BufferedReader br = null;
			if (isSup) {
				br = new BufferedReader(new InputStreamReader(ins, charset));
			} else {
				br = new BufferedReader(new InputStreamReader(ins));
			}
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				reList.add(readLine);
			}
			ins.close();
			br.close();
			return reList;
		} catch (Exception e) {
			return null;
		}
	}

	static final public String inps2Str(InputStream ins, String charset) {
		List<String> list = inps2LineStr(ins, charset);
		if (list == null)
			return "";
		int lens = list.size();
		if (lens <= 0)
			return "";
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < lens; i++) {
			buff.append(list.get(i));
		}
		String ret = buff.toString();
		buff.setLength(0);
		return ret;
	}

	static final public Object inps2Obj(InputStream ins) {
		if (ins == null)
			return null;
		try {
			return B2InputStream.readObject(ins);
		} catch (Exception e) {
		}
		return null;
	}

	static final public Map inps2Map(InputStream ins) {
		if (ins == null)
			return new HashMap();
		try {
			return B2InputStream.readMap(ins);
		} catch (Exception e) {
		}
		return new HashMap();
	}

	static final public byte[] inps2Bytes(InputStream ins, boolean isCloseIns) {
		if (ins == null)
			return new byte[0];
		try {
			return B2InputStream.readStream(ins);
		} catch (Exception e) {
		} finally {
			if (isCloseIns) {
				try {
					ins.close();
				} catch (Exception e) {
				}
			}
		}
		return new byte[0];
	}

	static final public byte[] inps2Bytes(InputStream ins) {
		return inps2Bytes(ins, true);
	}

	static final public Object inps2Obj4Stream(InputStream ins) throws Exception {
		byte[] bts = inps2Bytes(ins);
		try (ByteInStream byteStream = ByteInStream.create(bts)) {
			return B2InputStream.readObject(byteStream);
		}
	}

	static final public Map inps2Map4Stream(InputStream ins) throws Exception {
		byte[] bts = inps2Bytes(ins);
		try (ByteInStream byteStream = ByteInStream.create(bts)) {
			return B2InputStream.readMap(byteStream);
		}
	}

	static final public String inps2Str4Stream(InputStream ins, String charset) {
		if (ins == null)
			return "";
		try {
			charset = reCharset(charset, refObj);
			boolean isSup = refObj.val;
			byte[] bts = inps2Bytes(ins);
			if (isSup) {
				return new String(bts, charset);
			} else {
				return new String(bts);
			}
		} catch (Exception e) {
			return ExceptionEx.e2s(e);
		}
	}

	/*** 取得参数的字节流 **/
	static final public byte[] getBytes4Str(String params, String charset) {
		byte[] btParams = new byte[0];
		if (!StrEx.isEmptyTrim(params)) {
			charset = reCharset(charset, refObj);
			boolean isSup = refObj.val;
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
