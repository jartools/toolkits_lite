package com.bowlong.third;

import org.json.JSONObject;

import com.bowlong.lang.StrEx;
import com.bowlong.reflect.JsonHelper;
import com.bowlong.security.RSAEncrypt;

/**
 *  googleplay 支付 rsa 验证
 * 
 * @author Canyon / 龚阳辉 2018-09-19 19:37
 */
public class GGPlayRSA {
	static String m_strPublicKey = "-1"; // 许可与应用内购买结算 二进制文件中的 Base64 编码 RSA 公共密钥(公钥)
	static RSAEncrypt _rsaGGPlay = null;

	static final public RSAEncrypt getRSAGGPlay(String pubKey) {
		if (StrEx.isEmpty(pubKey) || m_strPublicKey.equals(pubKey)) {
			return _rsaGGPlay;
		}
		try {
			if (_rsaGGPlay == null) {
				_rsaGGPlay = RSAEncrypt.getInstance();
			}
			m_strPublicKey = pubKey;
			_rsaGGPlay.loadPublicKey(m_strPublicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _rsaGGPlay;
	}

	/**
	 * 验证 成功后,获得字符串json 对应的json对象
	 * 
	 * @param pubKey     = 公钥
	 * @param strJsonSrc = {json="",signature=""}
	 * @return null = false
	 */
	static final public JSONObject validRSA2Json(String pubKey, String strJsonSrc) {
		try {
			JSONObject src = JsonHelper.toJSON(strJsonSrc);
			JSONObject objSrc = src;
			if(src.has("Payload")) {
				objSrc = JsonHelper.toJSON(src.getString("Payload"));
			}
			String jStr = objSrc.getString("json");
			String signature = objSrc.getString("signature");
			if (getRSAGGPlay(pubKey).verify(jStr, signature)) {
				return JsonHelper.toJSON(jStr);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * RSA 验证 充值状态
	 * 
	 * @param pubKey         = 公钥
	 * @param strJsonSrc     = {json="",signature=""}
	 * @param transaction_id = ggplay 的订单号
	 * @param product_id     = 商品唯一标识
	 * @return 0 = success
	 */
	static final public int validRSA2State(String pubKey, String strJsonSrc, String transaction_id,
			String product_id) {
		try {
			JSONObject objJson = validRSA2Json(pubKey, strJsonSrc);
			if (objJson != null) {
				String orderId = objJson.getString("orderId");
				int purchaseState = objJson.getInt("purchaseState");
				String productId = objJson.getString("productId");
				if (purchaseState == 0 && orderId.equals(transaction_id) && productId.equals(product_id)) {
					return 0;
				} else {
					return -2;
				}
			}
			return -1;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -3;
	}
}
