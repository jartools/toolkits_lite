package com.bowlong.tool;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.text.EncodingEx;
import com.bowlong.third.xml.province.entity.XmlCities;
import com.bowlong.third.xml.province.entity.XmlCity;
import com.bowlong.third.xml.province.entity.XmlProvinces;
import com.bowlong.util.DateEx;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TkitJsp extends TkitBase {

	/*** 转换时间为字符串 **/
	static final public Map toBasicMap(Map orign) {
		if (MapEx.isEmpty(orign))
			return orign;
		Map result = new HashMap();
		for (Object key : orign.keySet()) {
			Object val = orign.get(key);
			if (val instanceof Date) {
				val = DateEx.format((Date) val, DateEx.fmt_yyyy_MM_dd_HH_mm_ss);
			}
			result.put(key, val);
		}
		return result;
	}

	/*** 添加消息 **/
	static public final Map tipMap(Map map, int result, String msg) {
		if (MapEx.isEmpty(map))
			map = new HashMap();
		map.put("msg", msg);
		map.put("status", result);
		return map;
	}

	/*** 输出文本内容 **/
	static public final void writeAndClose(OutputStream out, String content,
			String chaset) {
		try {
			if (StrEx.isEmptyTrim(chaset)) {
				chaset = EncodingEx.UTF_8;
			} else {
				boolean isU = EncodingEx.isSupported(chaset);
				if (!isU) {
					chaset = EncodingEx.UTF_8;
				}
			}

			out.write(content.getBytes(chaset));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** 输出文本内容 **/
	static public final void writeAndClose(HttpServletResponse response,
			String content, String chaset) {
		try {
			if (StrEx.isEmptyTrim(chaset)) {
				chaset = EncodingEx.UTF_8;
			} else {
				boolean isU = EncodingEx.isSupported(chaset);
				if (!isU) {
					chaset = EncodingEx.UTF_8;
				}
			}

			response.setCharacterEncoding(chaset);
			ServletOutputStream out = response.getOutputStream();
			writeAndClose(out, content, chaset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static public final void writeJsonAndClose(OutputStream out, String strJson,
			String callBackFun) {
		if (!StrEx.isEmptyTrim(callBackFun)) {
			strJson = callBackFun + "(" + strJson + ")";
		}
		writeAndClose(out, strJson, "");
	}
	
	static public final void writeJsonAndClose(HttpServletResponse response,
			String strJson, String callBackFun) {
		try {
			ServletOutputStream out = response.getOutputStream();
			writeJsonAndClose(out, strJson, callBackFun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** json格式写出map对象 [callfun:为空，就默认方式,不为空，就callFun方式] **/
	static public final void writeAndClose(OutputStream out, Map map,
			String callBackFun) {
		try {
			String v = JSON.toJSONString(map);
			writeJsonAndClose(out, v, callBackFun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** json格式写出map对象 **/
	static public final void writeAndClose(HttpServletResponse response,
			Map map, String callBackFun) {
		try {
			ServletOutputStream out = response.getOutputStream();
			writeAndClose(out, map, callBackFun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** json格式写出map对象 **/
	static public final void writeAndClose(OutputStream out, Map map) {
		writeAndClose(out, map, null);
	}

	/*** json格式写出map对象 **/
	static public final void writeAndClose(HttpServletResponse response, Map map) {
		writeAndClose(response, map, null);
	}

	/*** 清空httpsession **/
	static public final void clearHttpSession(HttpSession session) {
		clearHttpSession(session, "");
	}

	/*** 清空httpsession **/
	static public final void clearHttpSession(HttpSession session,
			String... excepts) {
		if (session == null)
			return;
		List<String> list = ListEx.toList(excepts);
		Enumeration names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			Object object = (Object) names.nextElement();
			if (ListEx.have(list, object))
				continue;
			session.removeAttribute(object.toString());
		}
	}

	/*** 添加Cookie的实现 **/
	static public final void addCookie(String name, String password,
			HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		if (!StrEx.isEmptyTrim(name) && !StrEx.isEmptyTrim(password)) {
			// 创建Cookie
			Cookie nameCookie = new Cookie("name", URLEncoder.encode(name,
					"utf-8"));
			Cookie pswCookie = new Cookie("psw", password);

			// 设置Cookie的父路径
			String fpath = request.getContextPath() + "/";
			nameCookie.setPath(fpath);
			pswCookie.setPath(fpath);

			// 获取是否保存Cookie
			String rememberMe = request.getParameter("rememberMe");
			if (rememberMe == null) {// 不保存Cookie
				nameCookie.setMaxAge(0);
				pswCookie.setMaxAge(0);
			} else {
				// 保存Cookie的时间长度，单位为秒
				int day7 = 7 * 24 * 60 * 60;
				nameCookie.setMaxAge(day7);
				pswCookie.setMaxAge(day7);
			}
			// 加入Cookie到响应头
			response.addCookie(nameCookie);
			response.addCookie(pswCookie);
		}
	}

	static public final void getCookie(HttpServletRequest request)
			throws Exception {
		// <%
		String name = "";
		String psw = "";
		String checked = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			// 遍历Cookie
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				// 此处类似与Map有name和value两个字段,name相等才赋值,并处理编码问题
				if ("name".equals(cookie.getName())) {
					name = URLDecoder.decode(cookie.getValue(), "utf-8");
					// 将"记住我"设置为勾选
					checked = "checked";
				}
				if ("psw".equals(cookie.getName())) {
					psw = cookie.getValue();
				}
			}
		}
		System.out.println("name=" + name + ",pwd=" + psw + ",checked="
				+ checked);
		// %>
	}

	/*** 上传图片demo **/
	static public void upload(HttpServletRequest request,
			MultipartHttpServletRequest fileRequest,
			HttpServletResponse response) {
		String pathDir = "";
		/** 得到图片保存目录的真实路径 **/
		String logoRealPathDir = request.getSession().getServletContext()
				.getRealPath(pathDir);
		/** 根据真实路径创建目录 **/
		File logoSaveFile = new File(logoRealPathDir);
		if (!logoSaveFile.exists())
			logoSaveFile.mkdirs();
		/** 页面控件的文件流 **/
		MultipartFile multipartFile = fileRequest.getFile("img2Upload");
		/** 获取文件的后缀 **/
		System.out.println(multipartFile.getOriginalFilename());
		String suffix = multipartFile.getOriginalFilename().substring(
				multipartFile.getOriginalFilename().lastIndexOf("."));

		/** 拼成完整的文件保存路径加文件 **/
		String name = +System.currentTimeMillis() + suffix;
		String fileName = logoRealPathDir + File.separator + name;
		File file = new File(fileName);
		try {
			multipartFile.transferTo(file);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*** 取得省份 **/
	static public final void provinces(String path, ModelMap modelMap) {
		if (path == null)
			path = "";
		XmlProvinces provinces = XmlProvinces.getEn4JarCache(path);
		if (provinces != null && !ListEx.isEmpty(provinces.getList())) {
			modelMap.put("provinces", provinces.getList());
		}
	}

	/*** 根据省ID，取得该省份拥有的市区 **/
	static public final void getCitiesByPid(String path, int pid,
			ModelMap modelMap) {
		if (path == null)
			path = "";
		List<XmlCity> list = XmlCities.getList4Jar(path, pid);
		if (!ListEx.isEmpty(list)) {
			modelMap.put("cities", list);
		}
	}

	static final boolean isNullUnknownIP(String ip) {
		if (StrEx.isEmptyTrim(ip))
			return true;
		return "unknown".equalsIgnoreCase(ip);
	}

	/**
	 * 获取客户端[使用者]真实的IP<br/>
	 * 如果使用了反向代理request.getRemoteAddr()获取的地址就不是客户端的真实IP
	 * 
	 * @return 客户端真实IP地址,如:202.65.16.220
	 */
	static public final String getVisitorIP(HttpServletRequest request) {
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
	static public final String getUrlIP(HttpServletRequest request) {
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
	static public final String getUrlIPProject(HttpServletRequest request) {
		return getUrlIP(request).concat(request.getContextPath());
	}
	
	static String _dirTmWebRoot = null;
	static public final String getDirTmWebRoot() {
		if (null == _dirTmWebRoot) {
			_dirTmWebRoot = getAppRoot().replace("bin", "webapps");
			_dirTmWebRoot = StrEx.repBackSlash(_dirTmWebRoot, "/");
			if(!_dirTmWebRoot.endsWith("/")){
				_dirTmWebRoot = _dirTmWebRoot + "/"; 
			}
		}
		return _dirTmWebRoot;
	}
}
