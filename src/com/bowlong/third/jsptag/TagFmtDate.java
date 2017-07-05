package com.bowlong.third.jsptag;

import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.bowlong.lang.NumEx;
import com.bowlong.lang.StrEx;
import com.bowlong.util.DateEx;

/**
 * 时间格式话
 * 
 * @author Canyon
 * 
 */
public class TagFmtDate extends TagSupport {
	private static final long serialVersionUID = -3354015192721342312L;
	private Object value;

	public void setValue(Object value) {
		this.value = value;
	}

	private String parttern;

	public void setParttern(String parttern) {
		this.parttern = parttern;
	}

	@Override
	public int doStartTag() throws JspException {
		if (StrEx.isEmpty(parttern))
			parttern = DateEx.fmt_yyyy_MM_dd_HH_mm_ss;
		Date d = null;
		String v = "";
		if (value instanceof Date) {
			d = (Date) value;
		} else if (value instanceof String) {
			long time = NumEx.stringToLong(value.toString());
			if (time > DateEx.TIME_1900) {
				d = DateEx.parse2Date(time);
			}
		} else if (value instanceof Long || value instanceof Integer) {
			
			long time = 0l;
			if (value instanceof Long)
				time = (long) value;
			else
				time = (int) value;
			
			if (time > DateEx.TIME_1900) {
				d = DateEx.parse2Date(time);
			} else {
				int[] arrs = DateEx.toDiffArray(time);
				v = parttern;
				int hh = arrs[1];
				if(v.indexOf("dd") == -1){
					hh = arrs[0] * 24 + hh;
				}else{
					v = v.replaceAll("dd", (arrs[0]) >= 10 ? ""+(arrs[0]) : "0"+(arrs[0]));
				}
				
				v = v.replaceAll("HH", (hh) >= 10 ? ""+(hh) : "0"+(hh));
				v = v.replaceAll("mm", (arrs[2]) >= 10 ? ""+(arrs[2]) : "0"+(arrs[2]));
				v = v.replaceAll("ss", (arrs[3]) >= 10 ? ""+(arrs[3]) : "0"+(arrs[3]));
			}
		}
		
		if (d != null) {
			v = DateEx.format(d, parttern);
		}
		
		try {
			pageContext.getOut().write(v);
		} catch (Exception e) {
		}
		return super.doStartTag();
	}
}
