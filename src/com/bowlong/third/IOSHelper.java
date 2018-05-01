package com.bowlong.third;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import org.json.*;

import com.bowlong.lang.StrEx;
import com.bowlong.reflect.JsonHelper;
import com.bowlong.util.CalendarEx;
import com.bowlong.util.DateEx;

/**
 * 苹果充值验证帮助脚本
 * 
 * @author Canyon / 龚阳辉 2018-04-28 19:37
 */
public class IOSHelper {
	
	static public boolean m_isDebug = false;
	
	/*** App 专用共享密钥 ***/
	static public String m_iosAppSharedSecret = "";
	
	static final String Valid(String urlPath, String receipt_data) {
		try {
			URL url = new URL(urlPath);
			// receipt_data = {"receipt-data":"xxxx"}
			HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
			// 设置参数
			httpConn.setDoOutput(true); // 需要输出
			httpConn.setDoInput(true); // 需要输入
			httpConn.setUseCaches(false); // 不允许缓存
			httpConn.setRequestMethod("POST"); // 设置POST方式连接
			// 设置请求属性
			httpConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
			httpConn.connect();
			// 建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
			dos.writeBytes(receipt_data);
			dos.flush();
			dos.close();
			int resultCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == resultCode) {
				StringBuffer sb = new StringBuffer();
				String readLine = new String();
				BufferedReader respReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
				while ((readLine = respReader.readLine()) != null) {
					sb.append(readLine);
				}
				respReader.close();
				String vStr = sb.toString();
				
				if(m_isDebug)
					System.out.println(String.format("== ios_verify = [%s]", vStr));
				
				JSONObject resultJson = JsonHelper.toJSON(vStr);
				int nState = resultJson.getInt("status");
				if (nState == 21007) {
					return Valid("https://sandbox.itunes.apple.com/verifyReceipt",receipt_data);
				} else if (nState == 0) {
					return vStr;
				}
				return "-1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-2";
	}
	
	/*** 验证 订单： pwd 是针对 订阅的类型的验证 ***/
	static final public String VlidateTrasncation(String str64, String pwd) {
		try {
			JSONObject json;
			if (str64.startsWith("{") && str64.endsWith("}")) {
				json = JsonHelper.toJSON(str64);
			} else {
				json = new JSONObject();
				json.put("receipt-data", str64);
			}
			if (!StrEx.isEmpty(pwd)){
				json.put("password", pwd);
			}
			
			str64 = json.toString();
			
			if(m_isDebug)
				System.out.println(String.format("== str64 = [%s]", str64));
			
			return Valid("https://buy.itunes.apple.com/verifyReceipt", str64);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-3";
	}
	
	/*** 验证 订单  ***/
	static final public String VlidateTrasncation(String str64) {
		return VlidateTrasncation(str64, m_iosAppSharedSecret);
	}
	
	/*** 针对 订阅的类型的 续订验证 ***/
	static final public String ValidRenewal(String item_id,String str64,String pwd,int day) {
		try {
			if (StrEx.isEmpty(str64)) {
				return "-1";
			}

			String strRes = VlidateTrasncation(str64,pwd);
			if ("-1".equals(strRes) || "-2".equals(strRes)
					|| "-3".equals(strRes)) {
				return "-2";
			}

			JSONObject resultJson = JsonHelper.toJSON(strRes);
			if (!resultJson.has("pending_renewal_info")) {
				return "-3";
			}
			
			JSONArray json_array = resultJson.getJSONArray("pending_renewal_info");
			int size = json_array.length();
			if (size <= 0) {
				return "-4";
			}

			JSONObject item = null;
			JSONObject targetRenew = null;
			for (int i = 0; i < size; i++) {
				item = json_array.getJSONObject(i);
				if (item.getString("auto_renew_product_id").equals(item_id)) {
					targetRenew = item;
					break;
				}
			}

			if (targetRenew == null) {
				return "-5";
			}

			// 当前订阅状态 正常（status为 1）, 已停止续费 （status为 0）
//			String auto_renew_status = targetRenew.getString("auto_renew_status");
//			if (!"1".equals(auto_renew_status)) {
//				return "-6";
//			}

			if (!resultJson.has("latest_receipt_info")) {
				return "-7";
			}
			
			json_array = resultJson.getJSONArray("latest_receipt_info");
			size = json_array.length();
			if (size <= 0) {
				return "-8";
			}

			JSONObject targetItem = null;
			long purchase_date_ms_target = 0;
			long purchase_date_ms_curr = 0;

			for (int i = 0; i < size; i++) {
				item = json_array.getJSONObject(i);
				purchase_date_ms_curr = item.getLong("purchase_date_ms");
				if (item.getString("product_id").equals(item_id)&& purchase_date_ms_target < purchase_date_ms_curr) {
					purchase_date_ms_target = purchase_date_ms_curr;
					targetItem = item;
				}
			}

			if (targetItem == null) {
				return "-9";
			}
			
			// original_transaction_id 与  transaction_id 在 targetItem 里面可能不一致
			// 一致表示首次购买，不一致标识续订成功订单

			long order_time_ms = purchase_date_ms_target;
//			long zone_time = 8 * DateEx.TIME_HOUR;
//			order_time_ms = order_time_ms + zone_time;

			if (day <= 0) {
				Calendar vCalDate = CalendarEx.parse2Cal(order_time_ms);
				day = CalendarEx.dayNumInMonth(vCalDate);
			}

			long day_ms = day * DateEx.TIME_DAY + DateEx.TIME_SECOND * 5;
			long end_ms = order_time_ms + day_ms;
			long now_time_ms = System.currentTimeMillis();
			if (end_ms <= now_time_ms) {
				return "-10";
			}
			return String.valueOf(end_ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-11";
	}
	
	/*** 续订验证   ***/
	static final public String ValidRenewal(String item_id,String str64,int day) {
		return ValidRenewal(item_id, str64, m_iosAppSharedSecret, day);
	}
	
	/*** 续订验证 : 月  ***/
	static final public String ValidRenewal4Month(String item_id,String str64,String pwd) {
		return ValidRenewal(item_id, str64, pwd,0);
	}
	
	/*** 续订验证 : 月  ***/
	static final public String ValidRenewal4Month(String item_id,String str64) {
		return ValidRenewal(item_id, str64,0);
	}
	
	/*** 续订验证 : 周  ***/
	static final public String ValidRenewal4Week(String item_id,String str64,String pwd) {
		return ValidRenewal(item_id, str64, pwd,7);
	}
	
	/*** 续订验证 : 周  ***/
	static final public String ValidRenewal4Week(String item_id,String str64) {
		return ValidRenewal(item_id, str64,7);
	}
}
