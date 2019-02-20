package com.bowlong.reflect;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
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
public class JsonHelper {

	/**
	 * 将Javabean转换为Map
	 * 
	 * @param javaBean
	 *            javaBean
	 * @return Map对象
	 */
	static final public Map toMap(Object javaBean) {
		Map result = new HashMap();
		Method[] methods = javaBean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
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
	 * @throws JSONException
	 */
	static final public Map toMap(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		Map result = new HashMap();
		Iterator iterator = jsonObject.keys();
		String key = null;
		String value = null;

		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 将JavaBean转换成JSONObject（通过Map中转）
	 * 
	 * @param bean
	 *            javaBean
	 * @return json对象
	 */
	static final public JSONObject toJSON(Object bean) {
		return toJSON(toMap(bean));
	}

	static final public JSONObject toJSON(Map<?, ?> map) {
		return new JSONObject(map);
	}

	/***
	 * 将json字符串转换org.json的JSONObject对象
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	static final public JSONObject toJSON(String json) throws JSONException {
		return new JSONObject(json);
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
		Method[] methods = javabean.getClass().getDeclaredMethods();
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
	 * @throws JSONException
	 */
	static final public Object toJavaBean(Object javabean, String jsonString) throws Exception {
		JSONObject jsonObject = new JSONObject(jsonString);
		Map map = toMap(jsonObject.toString());
		return toJavaBean(javabean, map);
	}

	static final public <T> T toJavaBean(Class<T> clazz, String jsonString) throws Exception {
		T javabean = clazz.newInstance();
		return (T) toJavaBean(javabean, jsonString);
	}

	static final public String toJSONStr(Object javabean){
		return toJSON(javabean).toString();
	}
	
	static final public String toJSONStr(Map<?, ?> map){
		return toJSON(map).toString();
	}
}