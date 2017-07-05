package com.bowlong.third.netty4.httphand;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.lang.StrEx;
import com.bowlong.net.http.HttpBaseEx;
import com.bowlong.text.Encoding;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;

/***
 * N4(netty 4)请求相应父类 处理过来的请求，解析请求参数，取得请求传送的数据 此处的res 是 response 的简称. 此处的req 是
 * request 的简称. ByPost 表示post 请求传参 ByGet 表示 get 请求传参
 ****/
public class N4HttpResp implements Serializable {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(N4HttpResp.class);

	// =============== get 请求传参
	public static String getParamsVal(String query, String key) {
		Map<String, String> mapPars = HttpBaseEx.buildMapByQuery(query);
		return MapEx.getString(mapPars, key);
	}

	// 根据uri取得get传递上来的所有参数集合的map对象
	static public Map<String, List<String>> getMapKVesByGet(String strUri) {
		Map<String, List<String>> v = null;
		if (strUri != null && !"".equals(strUri.trim())) {
			QueryStringDecoder qdec = new QueryStringDecoder(strUri);
			v = qdec.parameters();
		}
		return v;
	}

	static public Map<String, String> getMapByGet(HttpRequest request) {
		return getMapByGet(request.getUri());
	}

	static public Map<String, String> getMapByGet(String strUri) {
		Map<String, String> result = null;
		Map<String, List<String>> v = getMapKVesByGet(strUri);
		if (v != null && !v.isEmpty()) {
			result = new ConcurrentHashMap<String, String>();

			StringBuffer buff = new StringBuffer();

			for (Entry<String, List<String>> item : v.entrySet()) {

				buff.setLength(0);

				List<String> tmp = item.getValue();
				if (tmp == null || tmp.isEmpty())
					continue;

				int lens = tmp.size();
				for (int i = 0; i < lens; i++) {
					buff.append(tmp.get(i));
					if (i < lens - 1) {
						buff.append(",");
					}
				}
				result.put(item.getKey(), buff.toString());
			}
		}
		return result;
	}

	// 根据parmetes 参数取得对应的value，value默认是key对应的所有值中的第一个值.
	public static Map<String, String> getMapByGetKeys(String strUri,
			String... keyes) {
		Map<String, String> v = new HashMap<String, String>();
		if (ListEx.isEmpty(keyes))
			return v;

		Map<String, String> map = getMapByGet(strUri);
		if (map != null && !map.isEmpty()) {
			for (String key : keyes) {
				boolean isHasKey = map.containsKey(key);
				if (!isHasKey) {
					v.put(key, "");
					continue;
				}
				String val = map.get(key);
				v.put(key, val);
			}
		}
		return v;
	}

	// ==================== post 请求传参

	static HttpDataFactory factory = new DefaultHttpDataFactory(
			DefaultHttpDataFactory.MINSIZE);

	private static HttpPostRequestDecoder getDecoderByPost(HttpRequest request)
			throws Exception {
		return new HttpPostRequestDecoder(factory, request);
	}

	static public Map<String, String> getMapByPostDecoderChunk(
			HttpPostRequestDecoder msg, Map<String, String> map) {
		if (msg == null)
			return map;
		if (map == null)
			map = new HashMap<String, String>();
		try {
			while (msg.hasNext()) {
				InterfaceHttpData data = msg.next();
				if (data == null)
					continue;
				if (data.getHttpDataType() != HttpDataType.Attribute)
					continue;
				Attribute attr = (Attribute) data;
				map.put(attr.getName(), attr.getValue());
			}
		} catch (Exception e) {
			log.error(ExceptionEx.e2s(e));
		} finally {
			msg.destroy();
		}
		return map;
	}

	static public Map<String, String> getMapByPostDecoderBody(
			HttpPostRequestDecoder msg, Map<String, String> map) {
		if (msg == null)
			return map;
		if (map == null)
			map = new HashMap<String, String>();
		try {
			List<InterfaceHttpData> datas = msg.getBodyHttpDatas();
			for (InterfaceHttpData data : datas) {
				if (data.getHttpDataType() != HttpDataType.Attribute)
					continue;
				Attribute attr = (Attribute) data;
				String key = attr.getName();
				String val = attr.getValue();
				map.put(key, val);
			}
		} catch (Exception e) {
			log.error(ExceptionEx.e2s(e));
		} finally {
			msg.destroy();
		}
		return map;
	}

	static public Map<String, String> getMapByPostDecoderBody(Object msgObj) {
		Map<String, String> map = new HashMap<String, String>();
		HttpPostRequestDecoder msg = getPostDecoder(msgObj);
		return getMapByPostDecoderBody(msg, map);
	}

	static HttpPostRequestDecoder getPostDecoder(Object msg) {
		HttpPostRequestDecoder post = null;
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			if (request.getMethod().equals(HttpMethod.POST)) {
				try {
					post = getDecoderByPost(request);
				} catch (Exception e) {
					log.error(ExceptionEx.e2s(e));
				}
			}
		}
		return post;
	}

	/*** 取得 传送过来的参数map对象(处理了get,post) ***/
	static public Map<String, String> getMapKVByMsg(Object msg) {
		Map<String, String> map = new HashMap<String, String>();
		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;
			if (request.getMethod().equals(HttpMethod.GET)) {
				Map<String, String> m = getMapByGet(request);
				if (m != null && !m.isEmpty()) {
					for (Entry<String, String> item : m.entrySet()) {
						map.put(item.getKey(), item.getValue());
					}
				}
			}
		}

		HttpPostRequestDecoder post = getPostDecoder(msg);
		if (post != null) {
			if (msg instanceof HttpContent) {
				HttpContent chunk = (HttpContent) msg;
				post.offer(chunk);
				map = getMapByPostDecoderChunk(post, map);
			} else {
				map = getMapByPostDecoderBody(post, map);
			}
		}

		return map;
	}

	/*** 取得 传送过来的字节流对象 ***/
	static public byte[] getBytesContByMsg(Object msg) {
		// if (msg instanceof HttpRequest) {
		// HttpRequest request = (HttpRequest) msg;
		// if (HttpHeaders.isContentLengthSet(request)) {
		//
		// }
		// }
		if (msg instanceof HttpContent) {
			HttpContent chunk = (HttpContent) msg;
			ByteBuf content = chunk.content();
			byte[] buff = N4B2ByteBuf.readBuff(content);
			if (buff != null && buff.length > 0) {
				return buff;
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

	static public Map<String, String> getMapKVByMsgKeys(Object msg,
			String... keys) {
		Map<String, String> map = new HashMap<String, String>();
		if (keys == null || keys.length <= 0)
			return map;
		Map<String, String> dataMap = getMapKVByMsg(msg);
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
			Map<String, String> dataMap = getMapKVByMsg(msg);
			return HttpBaseEx.buildQuery(dataMap, "");
		} catch (Exception e) {
			log.error(ExceptionEx.e2s(e));
		}
		return "";
	}
}
