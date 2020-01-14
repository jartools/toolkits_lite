package com.bowlong.tool;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bowlong.basic.ExToolkit;
import com.bowlong.objpool.StringBufPool;

@SuppressWarnings({ "unchecked", "rawtypes" })
/**
 * Origin -> Base(Basic) - > Toolkit(Helper)
 * 
 * @author Canyon
 *
 */
public class TkitOrigin extends ExToolkit {
	static final Map<String, Object> allParams(Map<String, Object> map, StringBuffer buff, String key, String[] vals, boolean isNoFitlerEmpty) {
		buff.setLength(0);
		int lens = vals.length;
		for (int i = 0; i < lens; i++) {
			buff.append(vals[i]);
			if (i < lens - 1) {
				buff.append(",");
			}
		}
		String val = buff.toString();
		boolean isNoEmpty = !isEmptyTrim(val);
		if (isNoEmpty || isNoFitlerEmpty) {
			map.put(key, val);
		}
		return map;
	}

	/***
	 * 取得HttpServletRequest全部参数 getParameterNames isNoFitlerEmpty-是否(不过滤空val)
	 **/
	static public final Map<String, Object> getAllParams(HttpServletRequest requ, boolean isNoFitlerEmpty) {
		Map<String, Object> map = newMapT();
		Enumeration paramNames = requ.getParameterNames();
		StringBuffer buff = new StringBuffer();
		String key;
		String[] vals;
		while (paramNames.hasMoreElements()) {
			key = (String) paramNames.nextElement();
			vals = requ.getParameterValues(key);
			map = allParams(map, buff, key, vals, isNoFitlerEmpty);
		}
		key = map.containsKey("visitor_ip") ? "visitor_ip2" : "visitor_ip";
		map.put(key, getVisitorIP(requ));
		return map;
	}

	/*** 取得HttpServletRequest全部参数 getParameterNames **/
	static public final Map<String, Object> getAllParams(HttpServletRequest request) {
		return getAllParams(request, false);
	}

	/*** 取得HttpServletRequest全部参数 getParameterNames **/
	static public final String getStrAllParams(HttpServletRequest request) {
		Map<String, Object> map = getAllParams(request);
		return toJSONStr(map);
	}

	/*** 取得HttpServletRequest全部参数 getParameterMap **/
	static public final Map<String, Object> getAllParamsBy(HttpServletRequest requ, boolean isNoFitlerEmpty) {
		Map<String, Object> map = newMapT();
		Map mapPars = requ.getParameterMap();
		List keyList = keys(mapPars);
		int size = keyList.size();
		StringBuffer buff = new StringBuffer();
		String key;
		String[] vals;
		for (int i = 0; i < size; i++) {
			key = (keyList.get(i)).toString();
			vals = (String[]) mapPars.get(key);
			map = allParams(map, buff, key, vals, isNoFitlerEmpty);
		}
		key = map.containsKey("visitor_ip") ? "visitor_ip2" : "visitor_ip";
		map.put(key, getVisitorIP(requ));
		return map;
	}

	/*** 取得HttpServletRequest全部参数 getParameterMap **/
	static public final Map<String, Object> getAllParamsBy(HttpServletRequest request) {
		return getAllParamsBy(request, false);
	}

	/*** 简单拼接取得数量的Sql语句 **/
	static public final String getSql4Count(String tabName, Map params) {
		Map queMap = new HashMap();
		if (!isEmpty(params)) {
			Object val = null;
			String valStr;
			for (Object key : params.keySet()) {
				val = params.get(key);
				if (val != null) {
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
			if (!isEmpty(params)) {
				Object val = null;
				for (Object key : params.keySet()) {
					val = params.get(key);
					if (val != null) {
						strBuf.append("AND ").append(key.toString()).append(val.toString()).append(" ");
					}
				}
			}
			sql = strBuf.toString();
		} catch (Exception e) {
		} finally {
			StringBufPool.returnObject(strBuf);
		}
		return sql;
	}

	/*** 简单拼接取得列表的Sql语句 **/
	static public final String getSql4List(String tabName, Map params, int begin, int limit) {
		Map queMap = new HashMap();
		if (!isEmpty(params)) {
			Object val = null;
			String valStr;
			for (Object key : params.keySet()) {
				val = params.get(key);
				if (val != null) {
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
		StringBuffer _sb = StringBufPool.borrowObject();
		String sql = "";
		try {
			_sb.append("SELECT * FROM ").append(tabName).append(" WHERE 1 = 1 ");
			if (!isEmpty(params)) {
				Object val = null;
				for (Object key : params.keySet()) {
					val = params.get(key);
					if (val != null) {
						_sb.append("AND ").append(key.toString()).append(val.toString()).append(" ");
					}
				}
			}

			if (begin >= 0 && limit != 0) {
				_sb.append(" LIMIT ").append(begin).append(",").append(limit);
			}
			sql = _sb.toString();
		} finally {
			StringBufPool.returnObject(_sb);
		}
		return sql;
	}

	static final private boolean isNullUnknownIP(String ip) {
		if (isEmptyTrim(ip))
			return true;
		return "unknown".equalsIgnoreCase(ip);
	}

	/**
	 * 获取客户端[使用者]真实的IP<br/>
	 * 如果使用了反向代理request.getRemoteAddr()获取的地址就不是客户端的真实IP
	 * 
	 * @return 客户端真实IP地址,如:202.65.16.220
	 */
	static final public String getVisitorIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (isNullUnknownIP(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (isNullUnknownIP(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (isNullUnknownIP(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取项目的IP地址<br>
	 * 
	 * @return 协议://服务器名称:Web应用的端口号 <br/>
	 *         如：http://127.0.0.1:81
	 */
	static final public String getUrlIP(HttpServletRequest request) {
		StringBuffer buff = StringBufPool.borrowObject();
		try {
			// 取得协议，如：http
			buff.append(request.getScheme());
			buff.append("://");
			// 取得您的服务器名称，如：127.0.0.1
			buff.append(request.getServerName());
			buff.append(":");
			// 取得web应用的端口号，如：tomcat默认8080端口
			buff.append(request.getServerPort());
			return buff.toString();
		} catch (Exception e) {
		} finally {
			StringBufPool.returnObject(buff);
		}
		return "";
	}

	/**
	 * 获取项目的IP+项目名称
	 * 
	 * @return 协议://服务器名称:Web应用的端口号/Context路径 <br/>
	 *         如：http://127.0.0.1:81/项目名称
	 */
	static final public String getUrlIPProject(HttpServletRequest request) {
		return getUrlIP(request).concat(request.getContextPath());
	}

	/**
	 * 获取项目的IP+项目名称+请求
	 * 
	 * @return 协议://服务器名称:Web应用的端口号/uri <br/>
	 *         如：http://127.0.0.1:81/项目名称/函数
	 */
	static final public String getUriAll(HttpServletRequest request) {
		return getUrlIP(request).concat(request.getRequestURI());
	}
}
