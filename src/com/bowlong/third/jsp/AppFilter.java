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

public abstract class AppFilter implements Filter {
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

	// 效验
	private static boolean sqlValidate(String str) {
		str = str.toLowerCase();// 统一转为小写
		for (int i = 0; i < badSqlStrs.length; i++) {
			// 循环检测，判断在请求参数当中是否包含SQL关键字
			if (str.indexOf(badSqlStrs[i]) >= 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		req.setCharacterEncoding("UTF-8");
		HttpServletResponse res = (HttpServletResponse) response;
		Map<String, String> valMap = TkitJsp.getMapAllParams(req);
		String val = req.getRequestURI();

		boolean isFlag = isFilter(val, valMap) || isFilterSql(valMap)
				|| isFilterTime(valMap, key_time);
		
		if (isFlag) {
			valMap.clear();
			valMap.put("code", "fails");
			valMap.put("cmd", val);
			JSONObject jsonData = JsonHelper.toJSON(valMap);
			TkitJsp.writeAndClose(res, jsonData.toString(), "UTF-8");
			return;
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	protected boolean isFilterTime(Map<String, String> map, String keyTime) {
		if (map.containsKey(keyTime)) {
			long time_ms = MapEx.getLong(map, keyTime);
			long curr_ms = CalendarEx.now();
			return !(time_ms > curr_ms - ms_bef && time_ms < curr_ms + ms_aft);
		}
		return false;
	}

	protected boolean isFilterSql(Map<String, String> map) {
		boolean isFlag = false;
		Object[] _arrs = map.values().toArray();
		int lens = _arrs.length;
		for (int i = 0; i < lens; i++) {
			if (isFlag)
				break;

			isFlag = sqlValidate(_arrs[i].toString());
		}
		return isFlag;
	}

	public abstract boolean isFilter(String uri, Map<String, String> map);
}
