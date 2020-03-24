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
import com.bowlong.net.http.HttpBaseEx;
import com.bowlong.third.xml.province.entity.XmlCities;
import com.bowlong.third.xml.province.entity.XmlCity;
import com.bowlong.third.xml.province.entity.XmlProvinces;
import com.bowlong.util.ListEx;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TkitJsp extends TkitOrigin {

	/*** 转换时间为字符串 **/
	static final public Map toBasicMap(Map orign) {
		if (isEmpty(orign))
			return orign;
		Map result = new HashMap();
		Object val;
		for (Object key : orign.keySet()) {
			val = orign.get(key);
			if (val instanceof Date) {
				val = format((Date) val, fmt_yyyy_MM_dd_HH_mm_ss);
			}
			result.put(key, val);
		}
		return result;
	}

	/*** 添加消息 **/
	static final public Map tipMap(Map map, int result, String msg) {
		if (isEmpty(map))
			map = new HashMap();
		map.put("msg", msg);
		map.put("status", result);
		return map;
	}

	/*** 输出文本内容 **/
	static final public void writeAndClose(OutputStream out, String content, String charset) {
		try {
			charset = HttpBaseEx.reCharset(charset);
			out.write(content.getBytes(charset));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** 输出文本内容 **/
	static final public void writeAndClose(HttpServletResponse res, String content, String charset) {
		try {
			charset = HttpBaseEx.reCharset(charset);

			res.setCharacterEncoding(charset);
			ServletOutputStream out = res.getOutputStream();
			writeAndClose(out, content, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static final public void writeJsonAndClose(OutputStream out, String strJson, String callBackFun) {
		if (!isEmptyTrim(callBackFun)) {
			strJson = callBackFun + "(" + strJson + ")";
		}
		writeAndClose(out, strJson, "");
	}

	static final public void writeJsonAndClose(HttpServletResponse response, String strJson, String callBackFun) {
		try {
			ServletOutputStream out = response.getOutputStream();
			writeJsonAndClose(out, strJson, callBackFun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** json格式写出map对象 [callfun:为空，就默认方式,不为空，就callFun方式] **/
	static final public void writeAndClose(OutputStream out, Map map, String callBackFun) {
		try {
			String v = JSON.toJSONString(map);
			writeJsonAndClose(out, v, callBackFun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** json格式写出map对象 **/
	static final public void writeAndClose(HttpServletResponse response, Map map, String callBackFun) {
		try {
			ServletOutputStream out = response.getOutputStream();
			writeAndClose(out, map, callBackFun);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*** json格式写出map对象 **/
	static final public void writeAndClose(OutputStream out, Map map) {
		writeAndClose(out, map, null);
	}

	/*** json格式写出map对象 **/
	static final public void writeAndClose(HttpServletResponse response, Map map) {
		writeAndClose(response, map, null);
	}

	/*** 清空httpsession **/
	static final public void clearHttpSession(HttpSession session) {
		clearHttpSession(session, "");
	}

	/*** 清空httpsession **/
	static final public void clearHttpSession(HttpSession session, String... excepts) {
		if (session == null)
			return;
		List<String> list = ListEx.toList(excepts);
		Enumeration names = session.getAttributeNames();
		Object obj;
		while (names.hasMoreElements()) {
			obj = (Object) names.nextElement();
			if (ListEx.have(list, obj))
				continue;
			session.removeAttribute(obj.toString());
		}
	}

	/*** 添加Cookie的实现 **/
	static final public void addCookie(String name, String password, HttpServletResponse response, HttpServletRequest request) throws Exception {
		if (!isEmptyTrim(name) && !isEmptyTrim(password)) {
			// 创建Cookie
			Cookie nameCookie = new Cookie("name", URLEncoder.encode(name, "utf-8"));
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

	static final public void getCookie(HttpServletRequest request) throws Exception {
		// <%
		String name = "";
		String psw = "";
		String checked = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			// 遍历Cookie
			Cookie cookie;
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
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
		System.out.println("name=" + name + ",pwd=" + psw + ",checked=" + checked);
		// %>
	}

	/*** 上传图片demo **/
	static final public void upload(HttpServletRequest request, MultipartHttpServletRequest fileRequest, HttpServletResponse response) {
		String pathDir = "";
		/** 得到图片保存目录的真实路径 **/
		String logoRealPathDir = request.getSession().getServletContext().getRealPath(pathDir);
		/** 根据真实路径创建目录 **/
		File logoSaveFile = new File(logoRealPathDir);
		if (!logoSaveFile.exists())
			logoSaveFile.mkdirs();
		/** 页面控件的文件流 **/
		MultipartFile multipartFile = fileRequest.getFile("img2Upload");
		/** 获取文件的后缀 **/
		System.out.println(multipartFile.getOriginalFilename());
		String suffix = suffix(multipartFile.getOriginalFilename(), true);

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
	static final public void provinces(String path, ModelMap modelMap) {
		if (path == null)
			path = "";
		XmlProvinces provinces = XmlProvinces.getEn4JarCache(path);
		if (provinces != null && !isEmpty(provinces.getList())) {
			modelMap.put("provinces", provinces.getList());
		}
	}

	/*** 根据省ID，取得该省份拥有的市区 **/
	static final public void getCitiesByPid(String path, int pid, ModelMap modelMap) {
		if (path == null)
			path = "";
		List<XmlCity> list = XmlCities.getList4Jar(path, pid);
		if (!isEmpty(list)) {
			modelMap.put("cities", list);
		}
	}

	/**
	 * 取得ip4对应的城市code-Long值
	 * 
	 * @param visIP
	 *            255.255.255.0
	 * @return
	 */
	static final public String getCountryCode(String visIP) {
		List<Integer> list = ListEx.toListInt(visIP);
		long val = 0L;
		int lens = list.size();
		int counter = 3;
		for (int i = 0; i < lens; i++) {
			val += list.get(i) * (Math.pow(256, counter));
			counter--;
		}
		return toStr(val);
	}

	static final public String getCountryCode(HttpServletRequest request) {
		return getCountryCode(getVisitorIP(request));
	}

	static String _dirTmWebRoot = null;

	static final public String getDirTmWebRoot() {
		if (null == _dirTmWebRoot) {
			_dirTmWebRoot = dirTmWebRoot();
		}
		return _dirTmWebRoot;
	}
}
