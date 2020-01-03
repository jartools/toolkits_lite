package com.bowlong.third.taobao;

import java.util.Map;

import org.json.JSONObject;

import com.bowlong.basic.ExToolkit;
import com.bowlong.net.http.HttpEx;
import com.bowlong.reflect.JsonHelper;

/**
 * IP对应地址信息<br/>
 * 调用淘宝的IP地址取得Json对象 <br/>
 * 
 * @author Canyon
 * @time 2020-01-01 14:29
 */
public class TaobaoIP extends ExToolkit {

	static final String resetUrl = "http://ip.taobao.com/service/getIpInfo.php?ip=%s";

	// {"code":0,"data":{"country":"日本","country_id":"JP","area":"","area_id":"","region":"","region_id":"","city":"","city_id":"","county":"","county_id":"","isp":"","isp_id":"","ip":"133.88.44.178"}}

	static final Map<String, JSONObject> CACHE = newMap();

	public static JSONObject info(String ip) {
		if (CACHE.containsKey(ip))
			return CACHE.get(ip);
		
		JSONObject _ret = null;
		try {
			String url = String.format(resetUrl, ip);
			byte[] b = HttpEx.readUrl(url);
			String str = toStr(b);
			if (isEmpty(str))
				return null;

			JSONObject _src = JsonHelper.toJSON(str);
			if (_src.optInt("code", -1) != 0)
				return null;

			_ret = _src.optJSONObject("data");
			CACHE.put(ip, _ret);
		} catch (Exception e) {
		}
		return _ret;
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String ip = "133.88.44.178";
		long _n = now();
		System.out.println(info(ip));
		System.out.println("====t1==" + (now() - _n));
		_n = now();
		System.out.println(info(ip));
		System.out.println("====t2==" + (now() - _n));
		_n = now();
		System.out.println(info(ip));
		System.out.println("====t3==" + (now() - _n));
	}

}
