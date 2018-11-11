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
 * 简单记为：理(Listener)发(Filter)师(servlet).
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
	static protected String key_time = "time_ms";
	static public boolean isValidSql = true;
	static public boolean isPrint = false;
	static private boolean isInit = false;

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

	protected String strEncoding = "UTF-8";

	@Override
	public void destroy() {
		strEncoding = "UTF-8";
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		req.setCharacterEncoding(strEncoding);
		HttpServletResponse res = (HttpServletResponse) response;
		Map<String, String> pars = TkitJsp.getMapAllParams(req, true);
		String val = req.getRequestURI();

		_print(val, pars);

		boolean isFlag = (isFilter(val, pars) && isFilterTime(pars, key_time))
				|| isFilterSql(pars);

		if (isFlag) {
			pars.clear();
			pars.put("code", "fails");
			pars.put("cmd", val);
			JSONObject jsonData = JsonHelper.toJSON(pars);
			TkitJsp.writeAndClose(res, jsonData.toString(), "UTF-8");
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
		this.strEncoding = encoding.trim();

		if (!isInit) {
			isInit = true;
			onInit(arg0);
		}
	}

	protected boolean isFilterTime(Map<String, String> pars, String keyTime) {
		if (pars.containsKey(keyTime)) {
			long time_ms = MapEx.getLong(pars, keyTime);
			long curr_ms = CalendarEx.now();
			return !(time_ms > curr_ms - ms_bef && time_ms < curr_ms + ms_aft);
		}
		return false;
	}

	protected boolean isFilterSql(Map<String, String> pars) {
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

	private void _print(String uri, Map<String, String> pars) {
		if (!isPrint)
			return;
		JSONObject jsonData = JsonHelper.toJSON(pars);
		System.out.println(String.format("%s = %s", uri, jsonData.toString()));
	}

	public abstract void onInit(FilterConfig arg0);

	public abstract boolean isFilter(String uri, Map<String, String> pars);
}
