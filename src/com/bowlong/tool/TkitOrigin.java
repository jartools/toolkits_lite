package com.bowlong.tool;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bowlong.basic.ExToolkit;
import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.third.FastJSON;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TkitOrigin extends ExToolkit {
	static final Map<String, Object> allParams(Map<String, Object> map, StringBuffer buff, String key, String[] vals,
			boolean isNoFitlerEmpty) {
		buff.setLength(0);
		int lens = vals.length;
		for (int i = 0; i < lens; i++) {
			buff.append(vals[i]);
			if (i < lens - 1) {
				buff.append(",");
			}
		}
		String val = buff.toString();
		boolean isNoEmpty = !StrEx.isEmptyTrim(val);
		if (isNoEmpty || isNoFitlerEmpty) {
			map.put(key, val);
		}
		return map;
	}

	/***
	 * 取得HttpServletRequest全部参数 getParameterNames isNoFitlerEmpty-是否(不过滤空val)
	 **/
	static public final Map<String, Object> getAllParams(HttpServletRequest requ, boolean isNoFitlerEmpty) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration paramNames = requ.getParameterNames();
		StringBuffer buff = new StringBuffer();
		String key;
		String[] vals;
		while (paramNames.hasMoreElements()) {
			key = (String) paramNames.nextElement();
			vals = requ.getParameterValues(key);
			map = allParams(map, buff, key, vals, isNoFitlerEmpty);
		}
		return map;
	}

	/*** 取得HttpServletRequest全部参数 getParameterNames **/
	static public final Map<String, Object> getAllParams(HttpServletRequest request) {
		return getAllParams(request, false);
	}

	/*** 取得HttpServletRequest全部参数 getParameterNames **/
	static public final String getStrAllParams(HttpServletRequest request) {
		Map<String, Object> map = getAllParams(request);
		return FastJSON.toJSONString(map);
	}

	/*** 取得HttpServletRequest全部参数 getParameterMap **/
	static public final Map<String, Object> getAllParamsBy(HttpServletRequest requ, boolean isNoFitlerEmpty) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map mapPars = requ.getParameterMap();
		List keyList = ListEx.keys(mapPars);
		int size = keyList.size();
		StringBuffer buff = new StringBuffer();

		for (int i = 0; i < size; i++) {
			String key = (keyList.get(i)).toString();
			String[] vals = (String[]) mapPars.get(key);
			map = allParams(map, buff, key, vals, isNoFitlerEmpty);
		}
		return map;
	}

	/*** 取得HttpServletRequest全部参数 getParameterMap **/
	static public final Map<String, Object> getAllParamsBy(HttpServletRequest request) {
		return getAllParamsBy(request, false);
	}

	/*** 简单拼接取得数量的Sql语句 **/
	static public final String getSql4Count(String tabName, Map params) {
		Map queMap = new HashMap();
		if (!MapEx.isEmpty(params)) {
			Object val = null;
			for (Object key : params.keySet()) {
				val = params.get(key);
				if (val != null) {
					String valStr = "";
					if (val instanceof String) {
						valStr = " like '" + val.toString() + "'";
					} else {
						valStr = " = " + val.toString();
					}
					queMap.put(key.toString(), valStr);
				}
			}
		}
		return getSql4CountBy(tabName, queMap);
	}

	/*** map的key是字段名,val是条件自己封装=，！=， >,<等条件 **/
	static public final String getSql4CountBy(String tabName, Map params) {
		StringBuffer strBuf = StringBufPool.borrowObject();
		String sql = "";
		try {
			strBuf.append("SELECT COUNT(*) FROM ").append(tabName).append(" WHERE 1 = 1 ");
			if (!MapEx.isEmpty(params)) {
				Object val = null;
				for (Object key : params.keySet()) {
					val = params.get(key);
					if (val != null) {
						strBuf.append("AND ").append(key.toString()).append(val.toString()).append(" ");
					}
				}
			}

			sql = strBuf.toString();
		} finally {
			StringBufPool.returnObject(strBuf);
		}
		return sql;
	}

	/*** 简单拼接取得列表的Sql语句 **/
	static public final String getSql4List(String tabName, Map params, int begin, int limit) {
		Map queMap = new HashMap();
		if (!MapEx.isEmpty(params)) {
			Object val = null;
			for (Object key : params.keySet()) {
				val = params.get(key);
				if (val != null) {
					String valStr = "";
					if (val instanceof String) {
						valStr = " like '%" + val.toString() + "%'";
					} else {
						valStr = " = " + val.toString();
					}
					queMap.put(key.toString(), valStr);
				}
			}
		}
		return getSql4ListBy(tabName, queMap, begin, limit);
	}

	/*** map的key是字段名,val是条件自己封装=，！=， >,<等条件 **/
	static public final String getSql4ListBy(String tabName, Map params, int begin, int limit) {
		StringBuffer strBuf = StringBufPool.borrowObject();
		String sql = "";
		try {
			strBuf.append("SELECT * FROM ").append(tabName).append(" WHERE 1 = 1 ");
			if (!MapEx.isEmpty(params)) {
				Object val = null;
				for (Object key : params.keySet()) {
					val = params.get(key);
					if (val != null) {
						strBuf.append("AND ").append(key.toString()).append(val.toString()).append(" ");
					}
				}
			}

			if (begin >= 0 && limit != 0) {
				strBuf.append(" LIMIT ").append(begin).append(",").append(limit);
			}
			sql = strBuf.toString();
		} finally {
			StringBufPool.returnObject(strBuf);
		}
		return sql;
	}
}
