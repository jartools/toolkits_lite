package com.bowlong.third.jsp;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.bowlong.reflect.JsonHelper;
import com.bowlong.tool.TkitJsp;
import com.bowlong.util.CalendarEx;
import com.bowlong.util.MapEx;

/***
 * jsp filter基础过滤文件 <br/>
 * 启动的顺序为listener->Filter->servlet.<br/>
 * 理(Listener)->发(Filter)->师(servlet).
 * 
 * @author Canyon 2017-04-16 23:30
 */
public abstract class BasicFilter implements Filter {
	static protected String[] badSqlStrs = { " and ", "insert ", "select ",
			"delete ", "update ", "count", "drop table ", " or ", "char ",
			"declare", "sitename", "net user", "xp_cmdshell", " like'",
			" like '", "create ", " from ", "grant ", "use ", "group_concat",
			"column_name", "truncate table ", "information_schema.columns",
			"table_schema", "union", " where ", " order by" };

	static protected long ms_bef = CalendarEx.TIME_SECOND * 15;
	static protected long ms_aft = CalendarEx.TIME_SECOND * 15;
	static public boolean isVSql = true; // 是否验证sql注入
	static public boolean isVTime = false; // 是否验证有效时间
	static public String key_time = "time_ms"; // 时间字段
	static public boolean isCFDef = true; // 错误时是否用默认函数返回
	static public boolean isPrint = false;
	static private boolean isInit = false;
	static public String strEncoding = "UTF-8";

	// 效验
	static private boolean sqlValidate(String str) {
		str = str.toLowerCase();// 统一转为小写
		for (int i = 0; i < badSqlStrs.length; i++) {
			// 循环检测，判断在请求参数当中是否包含SQL关键字
			if (str.indexOf(badSqlStrs[i]) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	protected long curr_ms = 0;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		req.setCharacterEncoding(strEncoding);
		preOnFilter(req, res);
		Map<String, Object> pars = TkitJsp.getAllParams(req, true);
		String val = req.getRequestURI();

		_print(val, pars);

		if (onFilter(res, val, pars)) {
			return;
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// 注意MyEclipse的版本不同，方法init的参数有可能不同
		String encoding = arg0.getInitParameter("encoding");
		if (encoding == null) {
			encoding = "UTF-8";
		}
		strEncoding = encoding.trim();

		if (!isInit) {
			isInit = true;
			onInit(arg0);
		}
	}
	
	// 提供参数-头信息设定在 onFilter 之前
	protected void preOnFilter(HttpServletRequest req,HttpServletResponse res){}
	
	protected boolean onFilter(HttpServletResponse res,String uri, Map<String, Object> pars){
		int flagState = 0;
		boolean isFlag = false;
		isFlag = isFilter(uri, pars);
		curr_ms = CalendarEx.now();
		if (isFlag) {
			flagState = 1;
		}else{
			isFlag = isFilterTime(pars, key_time);
			if (isFlag) {
				flagState = 2;
			}
		}
		
		if (!isFlag) {
			isFlag = isFilterSql(pars);
			if (isFlag)
				flagState = 3;
		}
		if (isFlag) {
			if (isCFDef) {
				uri = cfFilterDef(flagState, uri, pars);
			} else {
				uri = cfFilter(flagState, uri, pars);
			}
			TkitJsp.writeAndClose(res, uri, strEncoding);
		}
		return isFlag;
	}

	protected boolean isFilterTime(Map<String, Object> pars, String keyTime) {
		if (isVTime && pars.containsKey(keyTime)) {
			long time_ms = MapEx.getLong(pars, keyTime);
			return !(time_ms > curr_ms - ms_bef && time_ms < curr_ms + ms_aft);
		}
		return isVTime;
	}

	protected boolean isFilterSql(Map<String, Object> pars) {
		boolean isFlag = false;
		Object[] _arrs = pars.values().toArray();
		int lens = _arrs.length;
		for (int i = 0; i < lens; i++) {
			if (isFlag)
				break;

			isFlag = sqlValidate(_arrs[i].toString());
		}
		return isFlag;
	}

	private void _print(String uri, Map<String, Object> pars) {
		if (!isPrint)
			return;
		JSONObject jsonData = JsonHelper.toJSON(pars);
		System.out.println(String.format("%s == [%s] = %s",CalendarEx.nowStr_YMDHms(),uri, jsonData.toString()));
	}

	protected String cfFilterDef(int state, String uri, Map<String, Object> pars) {
		pars.put("uri", uri);
		pars.put("code", "fails");
		pars.put("sv_ms", curr_ms);
		pars.put("ftState", String.valueOf(state));
		JSONObject jsonData = JsonHelper.toJSON(pars);
		return jsonData.toString();
	}

	public abstract void onInit(FilterConfig cfg);
	
	public abstract boolean isFilter(String uri, Map<String, Object> pars);

	// 过滤掉后需要返回的
	public abstract String cfFilter(int state, String uri,Map<String, Object> pars);
}
