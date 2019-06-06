package com.bowlong.basic;

import com.bowlong.objpool.StringBufPool;
import com.bowlong.util.StrBuilder;

/**
 * 异常转字符串<br/>
 * 
 * @author Canyon
 * @time 2019-02-14 19:32
 */
public class EOException extends EODateFmt {
	static final private Object[] onull = null;

	static final public String e2s(Exception e) {
		return e2s(e, null, onull);
	}

	static final public String e2s(Throwable e) {
		return e2s(e, null, onull);
	}

	static final public String e2s(Throwable e, Object obj) {
		return e2s(e, String.valueOf(obj), onull);
	}

	static final public String e2s(Throwable e, String fmt, Object... args) {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append(e);
			if (fmt != null && !fmt.isEmpty()) {
				if (args == null || args.length <= 0) {
					sb.append(" - ").append(fmt);
				} else {
					String str = StrBuilder.builder().ap(fmt, args).str();
					// String str = String.format(fmt, args);
					sb.append(" - ").append(str);
				}
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
