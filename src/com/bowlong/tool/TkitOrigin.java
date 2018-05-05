package com.bowlong.tool;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.text.EncodingEx;
import com.bowlong.third.FastJSON;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TkitOrigin {
	/*** 取得HttpServletRequest全部参数 getParameterNames **/
	static public final Map<String, String> getMapAllParams(
			HttpServletRequest request) {
		return getMapAllParams(request, "", "");
	}

	/*** 取得HttpServletRequest全部参数 getParameterNames **/
	static public final Map<String, String> getMapAllParams(
			HttpServletRequest request, String charsetFrom, String charsetTo) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration paramNames = request.getParameterNames();
		StringBuffer buff = new StringBuffer();

		boolean isFrom = !StrEx.isEmptyTrim(charsetFrom);
		if (isFrom) {
			isFrom = EncodingEx.isSupported(charsetFrom);
		}

		boolean isTo = !StrEx.isEmptyTrim(charsetTo);
		if (isTo) {
			isTo = EncodingEx.isSupported(charsetTo);
		}

		while (paramNames.hasMoreElements()) {

			buff.setLength(0);

			String key = (String) paramNames.nextElement();
			String[] vals = request.getParameterValues(key);
			int lens = vals.length;
			for (int i = 0; i < lens; i++) {
				buff.append(vals[i]);
				if (i < lens - 1) {
					buff.append(",");
				}
			}

			String val = buff.toString();
			if (!StrEx.isEmptyTrim(val)) {
				try {
					if (isFrom) {
						if (!isTo) {
							charsetTo = EncodingEx.UTF_8;
						}
						val = new String(val.getBytes(charsetFrom), charsetTo);
					} else {
						if (isTo) {
							val = new String(val.getBytes(), charsetTo);
						}
					}
				} catch (Exception e) {
				}

				map.put(key, val);
			}
		}
		return map;
	}

	/*** 取得HttpServletRequest全部参数 getParameterNames **/
	static public final String getStrAllParams(HttpServletRequest request) {
		Map<String, String> map = getMapAllParams(request);
		return FastJSON.toJSONString(map);
	}

	/*** 取得HttpServletRequest全部参数 getParameterMap **/
	static public final Map<String, String> getMapAllParamsBy(
			HttpServletRequest request, String charsetFrom, String charsetTo) {
		Map<String, String> map = new HashMap<String, String>();
		Map mapPars = request.getParameterMap();
		List keyList = ListEx.keyToList(mapPars);
		int size = keyList.size();
		StringBuffer buff = new StringBuffer();

		boolean isFrom = !StrEx.isEmptyTrim(charsetFrom);
		if (isFrom) {
			isFrom = EncodingEx.isSupported(charsetFrom);
		}

		boolean isTo = !StrEx.isEmptyTrim(charsetTo);
		if (isTo) {
			isTo = EncodingEx.isSupported(charsetTo);
		}

		for (int i = 0; i < size; i++) {
			buff.setLength(0);
			String key = (keyList.get(i)).toString();
			String[] vals = (String[]) mapPars.get(key);

			int lens = vals.length;
			for (int j = 0; j < lens; j++) {
				buff.append(vals[j]);
				if (j < lens - 1) {
					buff.append(",");
				}
			}

			String val = buff.toString();
			if (!StrEx.isEmptyTrim(val)) {

				try {
					if (isFrom) {
						if (!isTo) {
							charsetTo = EncodingEx.UTF_8;
						}
						val = new String(val.getBytes(charsetFrom), charsetTo);
					} else {
						if (isTo) {
							val = new String(val.getBytes(), charsetTo);
						}
					}
				} catch (Exception e) {
				}

				map.put(key, val);
			}
		}
		return map;
	}

	/*** 取得HttpServletRequest全部参数 getParameterMap **/
	static public final Map<String, String> getMapAllParamsBy(
			HttpServletRequest request) {
		return getMapAllParamsBy(request, "", "");
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
			strBuf.append("SELECT COUNT(*) FROM ").append(tabName)
					.append(" WHERE 1 = 1 ");
			if (!MapEx.isEmpty(params)) {
				Object val = null;
				for (Object key : params.keySet()) {
					val = params.get(key);
					if (val != null) {
						strBuf.append("AND ").append(key.toString())
								.append(val.toString()).append(" ");
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
	static public final String getSql4List(String tabName, Map params,
			int begin, int limit) {
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
	static public final String getSql4ListBy(String tabName, Map params,
			int begin, int limit) {
		StringBuffer strBuf = StringBufPool.borrowObject();
		String sql = "";
		try {
			strBuf.append("SELECT * FROM ").append(tabName)
					.append(" WHERE 1 = 1 ");
			if (!MapEx.isEmpty(params)) {
				Object val = null;
				for (Object key : params.keySet()) {
					val = params.get(key);
					if (val != null) {
						strBuf.append("AND ").append(key.toString())
								.append(val.toString()).append(" ");
					}
				}
			}

			if (begin >= 0 && limit != 0) {
				strBuf.append(" LIMIT ").append(begin).append(",")
						.append(limit);
			}
			sql = strBuf.toString();
		} finally {
			StringBufPool.returnObject(strBuf);
		}
		return sql;
	}
}
