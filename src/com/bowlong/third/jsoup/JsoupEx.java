package com.bowlong.third.jsoup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * jsoup 是一款Java 的HTML解析器，可直接解析某个URL地址、HTML文本内容 http://jsoup.org/
 * 
 * @author Canyon
 * @version createtime：2015年8月19日 下午9:42:26
 */
public class JsoupEx {
	static public final Document parse(File fn, String charset)
			throws IOException {
		return Jsoup.parse(fn, charset);
	}

	static public final Document parse(URL url, int timeoutMillis)
			throws IOException {
		return Jsoup.parse(url, timeoutMillis);
	}

	static public final Document parse(String text) {
		return Jsoup.parse(text);
	}

	/*** 取得Cookies **/
	static public final Map<String, String> getCookies(String url,
			Map<String, String> data) throws Exception {
		Response v = Jsoup.connect(url).timeout(30000).data(data).execute();
		return v.cookies();
	}
}
