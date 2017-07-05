package com.bowlong.json;

import java.util.Map;

@SuppressWarnings("rawtypes")
public interface EnDeJsonI {
	public EnDeJsonI parse(Map map);

	public Map toMap();
}
