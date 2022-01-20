package com.bowlong.basic;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * org.json轻量级别的json处理,不需要任何依赖<br/>
 * 1:将JavaBean转换成Map、JSONObject<br/>
 * 2:将Map转换成Javabean<br/>
 * 3:将JSONObject转换成Map、Javabean<br/>
 * 
 * @author Canyon
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class EOJson extends EOException {

	/**
	 * 将Javabean转换为Map
	 * 
	 * @param javaBean
	 *            javaBean
	 * @return Map对象
	 */
	static final public Map toMap(Object javaBean) {
		Map result = new HashMap();
		Class jClass = javaBean.getClass();
		Method[] methods = jClass.getDeclaredMethods();
		for (Method method : methods) {
			try {
				String field = method.getName();
				if (field.startsWith("get") && !"get".equals(field)) {
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 将Json对象转换成Map
	 * 
	 * @param jsonObject
	 *            json对象
	 * @return Map对象
	 * @throws Exception
	 */
	static final public Map toMap(String jsonString) throws Exception {
		JSONObject jroot = new JSONObject(jsonString);
		Map res = new HashMap();
		Iterator iterator = jroot.keys();
		String key = null;
		Object val = null;
		Object val2 = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			val = jroot.opt(key);
			if (val == null)
				continue;
			if (val instanceof JSONObject) {
				val2 = toMap((JSONObject) val);
				if (val2 != null)
					res.put(key, val2);
			} else if (val instanceof JSONArray) {
				val2 = toList((JSONArray) val);
				if (val2 != null)
					res.put(key, val2);
			} else {
				res.put(key, val);
			}
		}
		return res;
	}

	static final public Map toMap(JSONObject json) throws Exception {
		if (json == null)
			return null;
		Map res = new HashMap();
		Iterator iterator = json.keys();
		String key = null;
		Object val = null;
		Object val2 = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			val = json.opt(key);
			if (val == null)
				continue;
			if (val instanceof JSONObject) {
				val2 = toMap((JSONObject) val);
				if (val2 != null)
					res.put(key, val2);
			} else if (val instanceof JSONArray) {
				val2 = toList((JSONArray) val);
				if (val2 != null)
					res.put(key, val2);
			} else {
				res.put(key, val);
			}
		}
		return res;
	}

	static final public List<Object> toList(JSONArray json) throws Exception {
		if (json == null)
			return null;
		List<Object> list = newListT();
		int lens = json.length();
		Object val = null, val2 = null;
		for (int i = 0; i < lens; i++) {
			val = json.opt(i);
			if (val == null)
				continue;
			if (val instanceof JSONObject) {
				val2 = toMap((JSONObject) val);
				if (val2 != null)
					list.add(val2);
			} else if (val instanceof JSONArray) {
				val2 = toList((JSONArray) val);
				if (val2 != null)
					list.add(val2);
			} else {
				list.add(val);
			}
		}
		return list;
	}

	/**
	 * 将JavaBean转换成JSONObject（通过Map中转）
	 * 
	 * @param bean
	 *            javaBean
	 * @return json对象
	 */
	static final public JSONObject toJSON(Object bean) {
		Map map = toMap(bean);
		return toJSON(map);
	}

	static final public JSONObject toJSON(Map<?, ?> map) {
		return new JSONObject(map);
	}

	static final public JSONArray toJSONArr(List<?> list) {
		return new JSONArray(list);
	}

	/***
	 * 将json字符串转换org.json的JSONObject对象
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	static final public JSONObject toJSON(String json) {
		try {
			return new JSONObject(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static final public JSONArray toJSONArr(String json) {
		try {
			return new JSONArray(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将Map转换成Javabean
	 * 
	 * @param javabean
	 *            javaBean
	 * @param data
	 *            Map数据
	 */
	static final public Object toJavaBean(Object javabean, Map data) {
		Class jClass = javabean.getClass();
		Method[] methods = jClass.getDeclaredMethods();
		for (Method method : methods) {

			try {
				if (method.getName().startsWith("set")) {
					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					method.invoke(javabean, new Object[] { data.get(field) });
				}
			} catch (Exception e) {
			}
		}
		return javabean;
	}

	static final public <T> T toJavaBean(Class<T> clazz, Map data) throws Exception {
		T javabean = clazz.newInstance();
		return (T) toJavaBean(javabean, data);
	}

	/**
	 * JSONObject到JavaBean
	 * 
	 * @param bean
	 *            javaBean
	 * @return json对象
	 * @throws ParseException
	 *             json解析异常
	 * @throws Exception
	 */
	static final public Object toJavaBean(Object javabean, String strJson) throws Exception {
		Map map = toMap(strJson);
		return toJavaBean(javabean, map);
	}

	static final public <T> T toJavaBean(Class<T> clazz, String jsonString) throws Exception {
		T javabean = clazz.newInstance();
		return (T) toJavaBean(javabean, jsonString);
	}

	static final public String toJSONStr(JSONObject json, boolean isList) {
		if (json == null) {
			return isList ? "[]" : "{}";
		}
		return json.toString();
	}

	static final public String toJSONStr(JSONObject json) {
		return toJSONStr(json, false);
	}

	static final public String toJSONStr(Object javabean) {
		JSONObject jObj = toJSON(javabean);
		return jObj.toString();
	}

	static final public String toJSONStr(Map<?, ?> map) {
		JSONObject jObj = toJSON(map);
		return jObj.toString();
	}

	static final public String toJSONStr(List<?> list) {
		JSONArray jArr = toJSONArr(list);
		return jArr.toString();
	}

	static final public JSONObject get(JSONObject json, String key) {
		if (json == null || !json.has(key))
			return null;
		return json.getJSONObject(key);
	}

	static final public String getStr(JSONObject json, String key) {
		if (json == null || !json.has(key))
			return "";
		return json.getString(key);
	}

	static final public int getInt(JSONObject json, String key) {
		if (json == null || !json.has(key))
			return 0;
		return json.getInt(key);
	}
}