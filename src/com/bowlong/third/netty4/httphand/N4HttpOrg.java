package com.bowlong.third.netty4.httphand;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.lang.StrEx;
import com.bowlong.net.http.HttpBaseEx;
import com.bowlong.text.Encoding;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.MapEx;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

/***
 * N4(netty 4)请求相应父类 处理过来的请求，解析请求参数，取得请求传送的数据 此处的res 是 response 的简称. 此处的req 是
 * request 的简称. ByPost 表示post 请求传参 ByGet 表示 get 请求传参
 ****/
public class N4HttpOrg extends HttpBaseEx implements Serializable {

	private static final long serialVersionUID = 1L;

	static public boolean isLog = false;
	static Log log = LogFactory.getLog(N4HttpOrg.class);

	// 根据uri取得get传递上来的所有参数集合的map对象
	static public Map<String, Object> getMapByGet(HttpRequest request) {
		return buildMapByQuery(request.uri()); // QueryStringDecoder
	}
	// ==================== post 请求传参

	static HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

	private static HttpPostRequestDecoder getDecoderByPost(HttpRequest request) throws Exception {
		return new HttpPostRequestDecoder(factory, request);
	}

	static private Map<String, Object> _addData(InterfaceHttpData data, Map<String, Object> map) throws Exception {
		if (data == null || data.getHttpDataType() != HttpDataType.Attribute)
			return map;
		Attribute attr = (Attribute) data;
		String k = attr.getName();
		String v = attr.getValue();
		map = buildDecode(map, k, v);
		return map;
	}

	static private Map<String, Object> getMapByPostDecoder(HttpPostRequestDecoder msg, Map<String, Object> map, boolean isBody) {
		if (msg == null)
			return map;
		try {
			InterfaceHttpData data = null;
			if (isBody) {
				List<InterfaceHttpData> datas = msg.getBodyHttpDatas();
				int lens = datas.size();
				for (int i = 0; i < lens; i++) {
					data = datas.get(i);
					map = _addData(data, map);
				}
			} else {
				// Chunk
				while (msg.hasNext()) {
					data = msg.next();
					map = _addData(data, map);
				}
			}
		} catch (Exception e) {
			if (isLog)
				log.error(ExceptionEx.e2s(e));
		} finally {
			msg.destroy();
		}
		return map;
	}

	static HttpPostRequestDecoder getPostDecoder(Object msg) {
		HttpPostRequestDecoder post = null;
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			if (request.method().equals(HttpMethod.POST)) {
				try {
					post = getDecoderByPost(request);
				} catch (Exception e) {
					if (isLog)
						log.error(ExceptionEx.e2s(e));
				}
			}
		}
		return post;
	}

	/*** 取得 传送过来的参数map对象(处理了get,post) ***/
	static public Map<String, Object> getMapKVByMsg(Object msg) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			if (request.method().equals(HttpMethod.GET)) {
				Map<String, Object> m = getMapByGet(request);
				if (m != null && !m.isEmpty()) {
					map = m;
				}
			}
		}

		HttpPostRequestDecoder post = getPostDecoder(msg);
		if (post != null) {
			if (msg instanceof HttpContent) {
				HttpContent chunk = (HttpContent) msg;
				try {
					post.offer(chunk);
					map = getMapByPostDecoder(post, map, false);
				} finally {
					chunk.release();
				}
			} else {
				map = getMapByPostDecoder(post, map, true);
			}
		}

		return map;
	}

	/*** 取得 传送过来的字节流对象 ***/
	static public byte[] getBytesContByMsg(Object msg) {
		if (msg instanceof HttpContent) {
			HttpContent chunk = (HttpContent) msg;
			try {
				ByteBuf content = chunk.content();
				byte[] buff = N4B2ByteBuf.readBuff(content);
				if (buff != null && buff.length > 0) {
					return buff;
				}
			} finally {
				chunk.release();
			}
		}
		return new byte[0];
	}

	/*** 取得 传送过来的字符串内容对象 ***/
	static public String getStrContByMsg(Object msg, String charType) {
		byte[] buff = getBytesContByMsg(msg);
		String result = "";
		if (buff != null && buff.length > 0) {
			try {
				if (StrEx.isEmptyTrim(charType))
					charType = Encoding.UTF_8;
				result = new String(buff, charType);
			} catch (UnsupportedEncodingException e) {
				if (isLog)
					log.error(ExceptionEx.e2s(e));
			}
		}
		return result;
	}

	/*** 取得 更新文件对象 ***/
	static public FileUpload getFileByMsg(Object msg) {
		HttpPostRequestDecoder post = getPostDecoder(msg);

		if (post != null) {
			if (msg instanceof HttpContent) {
				post.offer((HttpContent) msg);
			}
			while (post.hasNext()) {
				InterfaceHttpData data = post.next();
				if (data.getHttpDataType() == HttpDataType.FileUpload) {
					FileUpload up = (FileUpload) data;
					if (up.isCompleted())
						return up;
				}
			}
		}
		return null;
	}

	static public Map<String, Object> getMapKVByMsgKeys(Object msg, String... keys) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (keys == null || keys.length <= 0)
			return map;
		Map<String, Object> dataMap = getMapKVByMsg(msg);
		if (dataMap == null || dataMap.isEmpty())
			return map;
		for (String key : keys) {
			String val = MapEx.getString(dataMap, key);
			map.put(key, val);
		}
		return map;
	}

	public static String getQueryByMsg(Object msg) {
		try {
			Map<String, Object> dataMap = getMapKVByMsg(msg);
			return HttpBaseEx.buildQuery(dataMap, "");
		} catch (Exception e) {
			if (isLog)
				log.error(ExceptionEx.e2s(e));
		}
		return "";
	}
}
