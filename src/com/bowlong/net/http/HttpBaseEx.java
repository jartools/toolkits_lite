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

	/** 连接超时时间，缺省为10秒钟 */
	static public final int defaultConnectionTimeout = 10000;
	/** 回应超时时间, 缺省为30秒钟 */
	static public final int defaultSoTimeout = 30000;

	/*** GET参数编码 */
	static public final String buildQuery(Map<String, String> data,String charset) {
		return buildQuery(data,charset,false);
	}
	
	static public final String buildQuery(Map<String,?> data,String charset,boolean isOrderKey) {
		if (MapEx.isEmpty(data))
			return "";

		String ret = "";
		boolean isSupported = !StrEx.isEmptyTrim(charset);
		if (isSupported) {
			isSupported = EncodingEx.isSupported(charset);
			if (!isSupported) {
				charset = EncodingEx.UTF_8;
				isSupported = true;
			}
		}
		
		Object[] keys = data.keySet().toArray();
		if(isOrderKey)
			Arrays.sort(keys);
		StringBuffer buff = new StringBuffer();
		int lens = keys.length;
		try {
			String k,v;
			Object vv;
			for (int i = 0; i < lens; i++) {
				k = keys[i].toString();
				vv = data.get(k);
				if(vv == null)
					continue;
				v = vv.toString();
				if(!StrEx.isEmpty(v)){
					if (isSupported) {
						k = URLEncoder.encode(k, charset);
						v = URLEncoder.encode(v, charset);
					}
				}
				buff.append(k).append("=").append(v);
				if(i < lens - 1)
					buff.append("&");
			}
		} catch (Exception e) {
		}
		
		ret = buff.toString();
		buff.setLength(0);
		return ret;
	}

	static public final String buildStrByJSON4Obj(Object data) {
		try {
			return JSON.toJSONString(data);
		} catch (Exception e) {
		}
		return "";
	}

	static public final String buildStrByJSON4Map(Map data) {
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
	static public final Map<String, String> buildMapByQuery(String query) {
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

	static public final String inps2Str(InputStream ins, String charset) {
		if (ins == null)
			return "";
		try {

			boolean isSupported = !StrEx.isEmptyTrim(charset);
			if (isSupported) {
				isSupported = EncodingEx.isSupported(charset);
				if (!isSupported) {
					charset = EncodingEx.UTF_8;
					isSupported = true;
				}
			}

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

	static public final Object inps2Obj(InputStream ins) {
		if (ins == null)
			return null;
		try {
			return B2InputStream.readObject(ins);
		} catch (Exception e) {
		}
		return null;
	}

	static public final Map inps2Map(InputStream ins) {
		if (ins == null)
			return new HashMap();
		try {
			return B2InputStream.readMap(ins);
		} catch (Exception e) {
		}
		return new HashMap();
	}

	static public final byte[] inps2Bytes(InputStream ins) {
		if (ins == null)
			return new byte[0];
		try {
			return B2InputStream.readStream(ins);
		} catch (Exception e) {
		}
		return new byte[0];
	}

	static public final Object inps2Obj4Stream(InputStream ins)
			throws Exception {
		byte[] bts = inps2Bytes(ins);
		try (ByteInStream byteStream = ByteInStream.create(bts)) {
			return B2InputStream.readObject(byteStream);
		}
	}

	static public final Map inps2Map4Stream(InputStream ins) throws Exception {
		byte[] bts = inps2Bytes(ins);
		try (ByteInStream byteStream = ByteInStream.create(bts)) {
			return B2InputStream.readMap(byteStream);
		}
	}

	static public final String inps2Str4Stream(InputStream ins, String charset) {
		if (ins == null)
			return "";
		try {

			boolean isSupported = !StrEx.isEmptyTrim(charset);
			if (isSupported) {
				isSupported = EncodingEx.isSupported(charset);
				if (!isSupported) {
					charset = EncodingEx.UTF_8;
					isSupported = true;
				}
			}

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

	/*** 取得参数的字节流 **/
	static public final byte[] getBytes4Str(String params, String charset) {
		byte[] btParams = new byte[0];
		if (!StrEx.isEmptyTrim(params)) {
			boolean isSupported = !StrEx.isEmptyTrim(charset);
			if (isSupported) {
				isSupported = EncodingEx.isSupported(charset);
				if (!isSupported) {
					charset = EncodingEx.UTF_8;
					isSupported = true;
				}
			}
			try {
				if (isSupported) {
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
			Process pro = Runtime.getRuntime().exec(
					"ping " + pingUrl + " -l 1000 -n 4");

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
					String value = line.substring(position + len,
							line.lastIndexOf("ms"));
					value = value.replaceAll("=", "");
					value = value.trim();
					double speed = (1000d / Integer.parseInt(value)) / 1024
							* DateEx.TIME_SECOND;
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
