package com.bowlong.json;

import java.util.List;
import java.util.Map;

import com.bowlong.basic.ExOrigin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("rawtypes")
public class JsonGsonHelper extends ExOrigin {
	static final public Gson getGson(boolean isFmt) {
		if(isFmt)
			return new GsonBuilder().setPrettyPrinting().create();
		return new Gson();
	}
	
	static final public String toJSONStr4Gson(JsonObject json,boolean isFmt) {
		if (json == null)
			return "{}";
		if(isFmt)
			return getGson(true).toJson(json);
		return json.toString();
	}
	
	static final public String toJSONStr4Gson(JsonObject json) {
		return toJSONStr4Gson(json,false);
	}
	
	static final public String toJSONStr4Gson(JsonArray json,boolean isFmt) {
		if (json == null) 
			return "[]";
		if(isFmt)
			return getGson(true).toJson(json);
		return json.toString();
	}
	
	static final public String toJSONStr4Gson(JsonArray json) {
		return toJSONStr4Gson(json,false);
	}

	static final public String toJSONStr4Gson(Object javabean,boolean isFmt) {
		Gson gson = getGson(isFmt);
		return gson.toJson(javabean);
	}
	
	static final public String toJSONStr4Gson(Object javabean) {
		return toJSONStr4Gson(javabean,false);
	}

	static final public String toJSONStr4Gson(Map<?, ?> map,boolean isFmt) {
		Gson gson = getGson(isFmt);
		return gson.toJson(map);
	}
	
	static final public String toJSONStr4Gson(Map<?, ?> map) {
		return toJSONStr4Gson(map,false);
	}

	static final public String toJSONStr4Gson(List<?> list,boolean isFmt) {
		Gson gson = getGson(isFmt);
		return gson.toJson(list);
	}
	
	static final public String toJSONStr4Gson(List<?> list) {
		return toJSONStr4Gson(list,false);
	}

	static final public Map toMap4Gson(String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, Map.class);
	}
	
	static final public List toList4Gson(String jsonStr) {
		Gson gson = new Gson();
		TypeToken<?> type = TypeToken.getArray(Map.class);
		return gson.fromJson(jsonStr, type.getType());
	}
	
	// M = Map,L = list
	static final public Object toMLObject4Gson(String jsonStr) {
		Gson gson = new Gson();
		TypeToken<?> type = null;
		if(jsonStr.startsWith("{"))
			type = TypeToken.get(Map.class);
		else
			type = TypeToken.getArray(Map.class);
		return gson.fromJson(jsonStr, type.getType());
	}
	
	static final public <T> List<T> toList4Gson(Class<T> clazz, String jsonStr) {
		Gson gson = new Gson();
		TypeToken<?> type = TypeToken.getArray(clazz);
		return gson.fromJson(jsonStr, type.getType());
	}
	
	static final public Object toJObject4Gson(String jsonStr) {
		JsonElement jEl = JsonParser.parseString(jsonStr);
		if(jEl.isJsonArray())
			return jEl.getAsJsonArray();
		return jEl.getAsJsonObject();
	}

	static final public JsonObject toJSON4Gson(String jsonStr) {
		JsonElement jEl = JsonParser.parseString(jsonStr);
		return jEl.getAsJsonObject();
	}

	static final public JsonArray toJSONArr4Gson(String jsonStr) {
		JsonElement jEl = JsonParser.parseString(jsonStr);
		return jEl.getAsJsonArray();
	}

	static final public JsonObject toJSON4Gson(Map<?, ?> map) {
		String jsonStr = toJSONStr4Gson(map);
		return toJSON4Gson(jsonStr);
	}

	static final public JsonArray toJSONArr4Gson(List<?> list) {
		String jsonStr = toJSONStr4Gson(list);
		return toJSONArr4Gson(jsonStr);
	}

	static final public <T> T toJavaBean4Gson(Class<T> clazz, String jsonStr) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, clazz);
	}
	
	static final public JsonObject get(JsonObject jroot,String key) {
		if (jroot == null || !jroot.has(key))
			return null;
		return jroot.getAsJsonObject(key);
	}
	
	static final public String getStr(JsonObject jroot, String key) {
		JsonObject json = get(jroot, key);
		if (json == null)
			return "";
		return json.getAsString();
	}
	
	static final public int getInt(JsonObject jroot, String key) {
		JsonObject json = get(jroot, key);
		if (json == null)
			return 0;
		return json.getAsInt();
	}
}