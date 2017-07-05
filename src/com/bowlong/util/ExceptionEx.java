package com.bowlong.util;

import com.bowlong.objpool.StringBufPool;

public class ExceptionEx {
	static public String e2s(Throwable e) {
		return e2s(e, null, new Object[0]);
	}

	static public String e2s(Throwable e, Object obj) {
		return e2s(e, String.valueOf(obj), new Object[0]);
	}

	static public String e2s(Throwable e, String fmt, Object... args) {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append(e);
			if (fmt != null && !fmt.isEmpty() && args.length <= 0)
				sb.append(" - ").append(fmt);
			if (fmt != null && !fmt.isEmpty() && args.length > 0) {
				String str = StrBuilder.builder().ap(fmt, args).str();
				sb.append(" - ").append(str);
			}
			sb.append("\r\n");
			for (StackTraceElement ste : e.getStackTrace()) {
				sb.append("at ");
				sb.append(ste);
				sb.append("\r\n");
			}
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}
}
