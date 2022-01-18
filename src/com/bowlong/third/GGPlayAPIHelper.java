package com.bowlong.third;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import com.bowlong.json.JsonHelper;
import com.bowlong.util.CalendarEx;
import com.bowlong.util.DateEx;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisher.Purchases.Products;
import com.google.api.services.androidpublisher.AndroidPublisher.Purchases.Subscriptions;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;

/**
 * Google Play 支付校验 API + RSA </br>
 * https://developers.google.com/android-publisher/api-ref/purchases/products
 * </br>
 * 
 * @author Canyon / 龚阳辉 2018-09-19 19:37
 */
public class GGPlayAPIHelper extends GGPlayRSA {

	static private HttpTransport HTTP_TRANSPORT;
	static private JacksonFactory JSON_FACTORY;

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			JSON_FACTORY = JacksonFactory.getDefaultInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GGPlayAPIHelper() {
	}

	public GGPlayAPIHelper(String emailAddress, InputStream inP12Stream) {
		init(emailAddress, inP12Stream);
	}

	private boolean m_isInited = false;
	public String m_emailAddress = "";
	public PrivateKey m_privateKey = null;

	public boolean getIsInited() {
		return m_isInited;
	}

	public GGPlayAPIHelper init(String emailAddress, InputStream inStreamP12) {
		try {
			this.m_emailAddress = emailAddress;
			this.m_privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(), inStreamP12, "notasecret", "privatekey", "notasecret");
			this.m_isInited = true;
		} catch (Exception e) {
			this.m_isInited = false;
			e.printStackTrace();
		}
		return this;
	}

	protected static String authorize() throws Exception {
		try (InputStream inStream = GGPlayAPIHelper.class.getResourceAsStream("/SlashandGirl-pulish-18a0e1718035.json")) {

			List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/androidpublisher");

			GoogleCredential credential = GoogleCredential.fromStream(inStream)// 加载服务帐户认证文件
					.createScoped(SCOPES);
			// 刷新token
			credential.refreshToken();

			// 获取token
			return credential.getAccessToken();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}

	static final public HttpRequestInitializer setHttpTimeout(final HttpRequestInitializer requestInitializer) {
		return new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest httpRequest) throws IOException {
				requestInitializer.initialize(httpRequest);
				httpRequest.setConnectTimeout(3 * 60000); // 3 minutes connect timeout
				httpRequest.setReadTimeout(3 * 60000); // 3 minutes read timeout
			}
		};
	}

	static final public HttpRequestInitializer setHttpTimeout() {
		return new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest httpRequest) throws IOException {
				httpRequest.setConnectTimeout(3 * 60000); // 3 minutes connect timeout
				httpRequest.setReadTimeout(3 * 60000); // 3 minutes read timeout
			}
		};
	}

	private GoogleCredential.Builder getServiceAccountBuilder() {
		return new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT).setJsonFactory(JSON_FACTORY).setServiceAccountId(m_emailAddress).setServiceAccountPrivateKey(m_privateKey);
	}

	protected GoogleCredential.Builder getServiceAccountBuilder(HttpRequestInitializer initializer) {
		GoogleCredential.Builder _builder = getServiceAccountBuilder();
		if (initializer != null)
			_builder = _builder.setRequestInitializer(initializer);
		return _builder;
	}

	protected Credential authorizeWithServiceAccount(HttpRequestInitializer initializer) throws Exception {
		return getServiceAccountBuilder(initializer).setServiceAccountScopes(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER))
				// .setServiceAccountPrivateKeyFromP12File(new File(SRC_RESOURCES_KEY_P12))
				.build();
	}

	protected Credential authorizeWithServiceAccount4All(HttpRequestInitializer initializer) throws Exception {
		return getServiceAccountBuilder(initializer).setServiceAccountScopes(AndroidPublisherScopes.all()).build();
	}

	public int validPay(String packageName, String productId, String purchaseToken) {
		if (!this.m_isInited) {
			return -1;
		}

		try {
			HttpRequestInitializer initializer = authorizeWithServiceAccount(setHttpTimeout());
			AndroidPublisher publisher = new AndroidPublisher.Builder(HTTP_TRANSPORT, JSON_FACTORY, initializer).build();
			Products products = publisher.purchases().products();
			// // 参数详细说明:
			// https://developers.google.com/android-publisher/api-ref/purchases/products/get
			Products.Get product = products.get(packageName, productId, purchaseToken);
			ProductPurchase purchase = product.execute();
			if (purchase.getPurchaseState() == 0) {
				return 0;
			}
			return -2;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -3;
	}

	public int validPay(String pubKey, String strJson) {
		try {
			JSONObject objJson = validRSA2Json(pubKey, strJson);
			if (objJson == null) {
				return -1;
			}
			String packageName = objJson.getString("packageName");
			String productId = objJson.getString("productId");
			String purchaseToken = objJson.getString("purchaseToken");
			return validPay(packageName, productId, purchaseToken);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -3;
	}

	public long validRenewing(String packageName, String productId, String purchaseToken) {
		if (!this.m_isInited) {
			return -1;
		}
		try {
			HttpRequestInitializer initializer = authorizeWithServiceAccount(setHttpTimeout());

			AndroidPublisher publisher = new AndroidPublisher.Builder(HTTP_TRANSPORT, JSON_FACTORY, initializer).build();

			Subscriptions subscriptions = publisher.purchases().subscriptions();
			Subscriptions.Get productGet = subscriptions.get(packageName, productId, purchaseToken);
			SubscriptionPurchase purchase = productGet.execute();

			System.out.println(productGet.getSubscriptionId());
			System.out.println(purchase.getAutoRenewing());
			System.out.println(purchase.getPaymentState());
			System.out.println(purchase.getExpiryTimeMillis());
			return purchase.getExpiryTimeMillis();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -11;
	}

	public long validRenewing(String pubKey, String strJson, int day) {
		try {
			JSONObject objJson = validRSA2Json(pubKey, strJson);
			if (objJson == null) {
				return -2;
			}

			if (!objJson.has("autoRenewing")) {
				return -3;
			}

			long purchaseTime = objJson.getLong("purchaseTime");
			if (day <= 0) {
				Calendar vCalDate = CalendarEx.parse2Cal(purchaseTime);
				day = CalendarEx.dayNumInMonth(vCalDate);
			}

			long day_ms = day * DateEx.TIME_DAY + DateEx.TIME_SECOND * 5;
			long end_ms = purchaseTime + day_ms;
			long now_time_ms = System.currentTimeMillis();
			if (end_ms > now_time_ms) {
				return end_ms;
			}

			// 重新取得数据进行验证
			String packageName = objJson.getString("packageName");
			String productId = objJson.getString("productId");
			String purchaseToken = objJson.getString("purchaseToken");

			purchaseTime = validRenewing(packageName, productId, purchaseToken);
			if (purchaseTime <= 0)
				return purchaseTime;

			end_ms = purchaseTime + day_ms;
			if (end_ms > now_time_ms) {
				return end_ms;
			}
			return -10;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return -11;
	}

	static GGPlayAPIHelper _self;

	public static GGPlayAPIHelper getInstance() {
		if (_self == null)
			_self = new GGPlayAPIHelper();
		return _self;
	}

	public static void main(String[] args) throws Exception {
		// test_api_authorize();
		// test_api_renew();
		test_rsa();
	}

	static void test_api_renew() throws Exception {
		try (InputStream inStream = GGPlayAPIHelper.class.getResourceAsStream("/api-project-6624369-bde2de9f1522.p12")) {
			String emailAddress = "slashandgirl@api-project-6624369.iam.gserviceaccount.com";
			String packageName = "com.slashandgirl.android";
			String productId = "com.sungame.slashandgirl.innoadsfreecoin";
			String purchaseToken = "hmmajpnhmoggdlhgcelhgpkf.AO-J1OyIfdvabwuUn_DEIEWPx5UGrRBlwmh9LTTaHcPtsk-dxW_4ZL3q0h_z2rIzCvofd-oy5CUsuHXGO_qE8K7_zZWtjiOZnllCl4pIEsc2xwu9GLBH6lmchaZSfba9CB-U55yY1Tml9e0BIERnQw4FwLVHU5bEyXWjAF7n1Khi4XUGW5XXVOk";
			getInstance().init(emailAddress, inStream).validRenewing(packageName, productId, purchaseToken);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static void test_api_authorize() throws Exception {
		// authorization
		System.out.println(authorize());
	}

	static void test_rsa() throws Exception {
		// 加载公钥
		String ggplay_publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk/sZkgrmGEzcfcpUreUZERFmzQtj+VBVbVK/4kUciIyFHVzIN/eZSs22rKaF5y78DDjCIBGXEaZ+ndLdfZma9LvHuXKcRn6xVxvsPb3c/bd8BdOtvZmKoQ6e3jrsXq7TBCFN+H3zU9GGdr9tXLSp0pxLwO4LAmO8vun7UlqTupWjDV+MTA80dW1CrahbicncahOLTLOxG9ei4z9nlWlBwuNOGgSFpVDqAKtvmgUCa0966adT4WgBwHsoZaHBD+O9bQkVfhxzxH5vbXTvQKW4WDsvVTtd5NJ0cEyLgQ7LyVQ9iL9r3GEpjkU/9Hx0ORZWcdmyXYlc51AhZmiF/rH3AQIDAQAB";

		String pStr5 = "9B108C8B5E0D7AB801995851E1485C6B0D7B98E37BF0A6540E0A51737BE37870F8844F8AD3E9DFACDDAD677182BB5C5D668F58756AE76EC0A2662EBC5C68DB805A4B8A4FEF338007E57849D6919BFF4234E35F69E4C7619CE488DA186BD400D0E42B8E195CBC0C5809D6FD496696F19318B9593A54E98812DD24795B52C8E52A3ECBD37DAF81E5D73FC3B342D6A3CA62AC785FB341BD84373A82710B678B065F21A30A2ECF934E6E90B71CEA2A0179A598EEFCBCC11A0B642679325461F11E350AADFDB6A791A7C9C7109C5B63A077C93610445A05B214B3FDB500D50D4892C57269049D8996E0326F35287C20DCB8EEFF56C9BB8BB231F251E80DED70F2A8D1A33B3DE0FA7A964E96386B6C8D014EDF51FF94F3BA5AA2E41DBB191DD0DD011D09619D71FE0C8D9FCF9F56C4C18B4FAD50FDB91F786C5EB3362453E0B394A127D3BC82AAB28814175672D5F387211C4A36B23DF56ECB5F03C0737EA1D3D73D92C18A5E22631C1C19E4537315B9EF6DFE8A6F7B1AF5DBE997AE7E760EAB2B0D5EEC65118063D98D054736E66B9D29FF5DC167C95DB97619978C3598568191B683EE4EC17EF41598FB8F9FBA3238928E89D3CE9773954AC129C7EFF8417F03D9B6D5EAA4A565AF2FE90E65F5AB8F3A376873B4B7D8E72754C031EAC6A2C27D1F6C24FCAEF451391042024D6150652CC8CE63AF025E3AF4AE1AA24B1D6F57905ADACC05D2ECFF445098CF4DB17CBD5B7188DBDDF6C9D69AA3752CFAB7F9219D5CC532F8C75B6165016F1E70ECC3B8E047EFF5A897AC63773E6E34BF81FAB3D52341A6F823F2761A07D8F28E147D9E3677A3B77BB4F70AE75D6CC567737C9DD42FE09BB0ED4FA77563920B80CC8AE62307B35482D14682FB20F664B4C2F757850714776854633A5FECFBA2AB6F90CB9664CCA592FFBC07972F9054BAEC28DFC565587D61FDABD336272573F863C7B8294A2D7B57A09BDC6A91847E2382E3F4CA4021CCEEA40F0E172672829AD66AB84C9D6A7C435D8DE83B3919B271026CE18D02CBD9E3CA1ED04F6E89A5409B01F14EF5C9081990C78DB4ECE1A855D708E4C0117B800BAA7C4AF91C2DB7BCA39D62D7CFBA61BBD6C78AC7BA15975696C0A51709887875A205DB85EFBAC3A1D668B28747921F42AD7FD126340E8BAF433E8383291A";
		pStr5 = "9B108C8B5E0D7AB801995851E1485C6B0D7B98E37BF0A65424EAE90A7105AF43398738424AD793C13B3A8C7D424C495EDDA580653E9E42E5115E99BF983CC68B8D3D0510E6C90545A741701921694D304F15C1D05A8011A1F037CC224F0B7847D994890EFA17254B2D2992F985595E3EDA494A7197180305ABBB47529ADA9A30849FF1BACCA914127319462CCEA490FC2D9D0508E6715E67CEB964C356D5C9A26762883A1D63E44E7D5DA2C56E14567C58218CDE7097B544F340C07FF8A146A4A6E7743FD96DA756ADD9DA21E76E3C23918A04D3FD5869315DEA8B11794B220A49025BFD06511D352BFF4D140A8B5E12EA29233B2A83D86BF3B923AACB022C9D5BF954846CC16EA77597CD79178F6CE8A6EE07F6F62EBD47E082F4A8D6958CD47E98A06FF194E7CA378477F4F5BE723DB6892C5A91E4DE9F58D479BF4671648E2FAEA6E72649C5CBA75C8A3CB5510FEE63465C4AB1DF223C7C64EF01300E54DD58A2D27A67D419ED12AA355E81B96BF3CC040530A68086F3806D9AD117061D56D665A7D28067C94AD9CB3C227C36DDE9ECF3D32FCCE1A5FE47F87F4E8DD1C2BEB13B1423FE42615766605DF0470B4A749B6488E49F33A42932E69872260D38DEBE5410957BF274C88B284E27B162A70782463E1608CD60B553A938BF22DB174DFC03283A609FC0688DA90DB5B3262A28D06D07E454BCD9F6670262754797D8E5E62C51184B633E4909D5C98C04C7FD7E26158103B274AEDBA4AFD04BE8F4FD3913EA1260C6B6C536F607C22828C6A0F5A2539018ED545FD1F882DC1AD2292B2EC52D01DB4F876848FE11EED66DB1889306F5E5C67602786FEEAC034DC4796DB89EF3815AC16BD930033DDC4D566084FD87EB468A5C934ACFD518C0C0F171F323C30C5D801B404DE689E68043FC3E61C9B461CA4D9E8E7C46FA82A4D4D1D98E9CDFEAB3899C84BB23517ABA6F21B7D82AF763FEA1F135EE3F341FF1ED9730BBC193B13FFAA45A5D1644CB18C39E95034B8C07EC89003CE926C7EA884C42C4D509FB16CB2296EEE5E039A2F8F46613A94D3ED7C4442DB7113FAFF5A9BBBAECE8176C03483274EE8F611DF08350356CE0F53D65F5A4384B883BC7295ECAAAF2447F7C8A13839D079F928946ECF82F3894C244D3EE8BF850B10D21AF9208477B36BF684F7091245CA67911262036CBAA889F";

		String transaction_id = "GPA.3350-9432-0438-59739";
		String product_id = "com.sungame.slashandgirl.innoadsfreecoin";

		String valStr = com.bowlong.security.AESPKCS5Padding.getInstance().decrypt(pStr5);

		System.out.println(validRSA2State(ggplay_publicKey, valStr, transaction_id, product_id));
		JSONObject objJson = validRSA2Json(ggplay_publicKey, valStr);
		// 文档测试数据
		if (objJson != null) {
			String testDataStr = objJson.toString();
			System.out.println(testDataStr);
			System.out.println(objJson.get("purchaseState"));
		}
		JSONObject objSrc = JsonHelper.toJSON(valStr);
		System.out.println(objSrc.getString("json"));
	}
}
