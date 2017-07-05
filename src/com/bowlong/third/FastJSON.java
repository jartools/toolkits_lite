package com.bowlong.third;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bowlong.json.EnDeJsonI;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class FastJSON extends JSON {

	public static final String toJSONString(Map map) {
		return JSON.toJSONString(map);
	}

	public static final List parseList(String text) {
		return (List) JSON.parse(text);
	}

	public static final Map parseMap(String text) {
		return (Map) JSON.parse(text);
	}

	public static final String format(String json) {
		return prettyFormat(json);
	}

	public static final String prettyFormat(String json) {
		Object obj = JSON.parse(json);
		return prettyFormat(obj);
	}

	public static final String format(Object object) {
		return prettyFormat(object);
	}

	public static final String prettyFormat(Object object) {
		boolean prettyFormat = true;
		return JSON.toJSONString(object, prettyFormat);
	}

	public static final EnDeJsonI parseForMapNoExcept(String str,
			Class<? extends EnDeJsonI> clazz) {
		try {
			return parseForMap(str, clazz);
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final EnDeJsonI parseForMap(String str,
			Class<? extends EnDeJsonI> clazz) throws IOException,
			InstantiationException, IllegalAccessException {
		Map map = parseMap(str);
		EnDeJsonI result = (EnDeJsonI) clazz.newInstance();
		return result.parse(map);
	}

	public static final List<EnDeJsonI> parseListNoExcept(String str,
			Class<? extends EnDeJsonI> clazz) {
		try {
			return parseList(str, clazz);
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	public static final List<EnDeJsonI> parseList(String str,
			Class<? extends EnDeJsonI> clazz) throws IOException,
			InstantiationException, IllegalAccessException {
		List<EnDeJsonI> r2 = new ArrayList<>();
		List<Map> list = parseList(str);
		for (Map m : list) {
			EnDeJsonI e = (EnDeJsonI) clazz.newInstance();
			e.parse(m);
			r2.add(e);
		}
		return r2;
	}

	public static final String toJSONStringNoExcept(EnDeJsonI obj) {
		try {
			return toJSONString(obj);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static final String toJSONString(EnDeJsonI obj) throws IOException {
		return toJSONString(obj.toMap());
	}

	public static final String toJSONStringNoExcept(
			List<? extends EnDeJsonI> objs) {
		try {
			return toJSONString(objs);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static final String toJSONString(List<? extends EnDeJsonI> objs)
			throws IOException {
		List<Map> maps = new ArrayList<>();
		for (EnDeJsonI fm : objs) {
			maps.add(fm.toMap());
		}
		return toJSONString(maps);
	}

	public static void main(String[] args) throws IOException {
		List l = new ArrayList();
		{
			Map m = new HashMap();
			m.put("1", "001");
			m.put("name", "张三");
			m.put("sex", "male");
			l.add(m);
		}
		{
			Map m = new HashMap();
			m.put("1", "001");
			m.put("name", "张三");
			m.put("sex", "male");
			l.add(m);
		}

		String s = toJSONString(l);
		System.out.println(prettyFormat(s));
	}
}
