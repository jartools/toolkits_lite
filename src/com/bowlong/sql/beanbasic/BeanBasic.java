package com.bowlong.sql.beanbasic;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bowlong.lang.NumEx;
import com.bowlong.util.CalendarEx;

/**
 * 添加bean 基础类
 * 
 * @author canyon/龚阳辉
 * @time 2019-02-18 17:24
 */
public abstract class BeanBasic implements ResultSetHandler, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	static final public String insFmt = "INSERT INTO `%s` (%s) VALUES (%s)";
	static final public String selFmt = "SELECT * FROM `%s` WHERE ";
	static final public String upFmt = "UPDATE `%s` SET %s WHERE %s";

	static final public long now() {
		return CalendarEx.now();
	}

	static final public double round(double org, int acc) {
		return NumEx.roundDecimal(org, acc);
	}

	static final public double round2(double org) {
		return round(org, 2);
	}

	protected Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	protected Map<String, Object> toMap(Map<String, Object> map) {
		return map;
	}

	public Map<String, Object> toMap() {
		map.clear();
		return toMap(map);
	}
}
