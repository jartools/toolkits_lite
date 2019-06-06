package com.bowlong.third.jsp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bowlong.tool.TkitJsp;
import com.bowlong.util.CalendarEx;

/***
 * jsp servlet基础 类 <br/>
 * 理(Listener)->发(Filter)->师(servlet).
 * 
 * @author Canyon 2018-11-12 19:02
 */
public abstract class AbsDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static public boolean isLogOut = false;
	static final public String NoWrite = "noWrite";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		_disp(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		_disp(req, resp);
	}

	protected void _disp(HttpServletRequest req, HttpServletResponse resp) {
		String outVal = "";
		boolean isError = false;
		try {
			outVal = dispatcher(req, resp);
		} catch (Exception e) {
			outVal = TkitJsp.e2s(e);
			isError = true;
		}
		if (isLogOut || isError) {
			System.out.println(String.format("%s == outVal = [%s]", CalendarEx.nowStr_YMDHms(), outVal));
		}
		if (!outVal.equals(NoWrite))
			TkitJsp.writeAndClose(resp, outVal, BasicFilter.strEncoding);
	}

	// Handler 处理
	public abstract String dispatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
