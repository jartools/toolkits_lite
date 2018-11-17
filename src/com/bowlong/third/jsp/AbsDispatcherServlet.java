package com.bowlong.third.jsp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bowlong.tool.TkitJsp;
import com.bowlong.util.ExceptionEx;

/***
 * jsp servlet基础 类 <br/>
 * 理(Listener)->发(Filter)->师(servlet).
 * 
 * @author Canyon 2018-11-12 19:02
 */
public abstract class AbsDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

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
		try {
			outVal = dispatcher(req, resp);
		} catch (Exception e) {
			outVal = ExceptionEx.e2s(e);
		}
		TkitJsp.writeAndClose(resp, outVal, BasicFilter.strEncoding);
	}

	// Handler 处理
	public abstract String dispatcher(HttpServletRequest req, HttpServletResponse resp);
}
