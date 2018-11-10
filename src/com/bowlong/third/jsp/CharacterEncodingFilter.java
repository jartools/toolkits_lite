package com.bowlong.third.jsp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {
	String strEncoding = "UTF-8";

	@Override
	public void destroy() {
		strEncoding = "";
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		if (strEncoding != null) {
			// 注意MyEclipse的版本不同，方法doFilter的参数有可能不同
			arg0.setCharacterEncoding(strEncoding);
		}
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// 注意MyEclipse的版本不同，方法init的参数有可能不同
		String encoding = arg0.getInitParameter("encoding");
		if (encoding == null) {
			encoding = "UTF-8";
		}
		this.strEncoding = encoding.trim();
	}
}
