package com.bowlong.third.jsptag;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/***
 * jsp界面上面取得Map中的val值
 * 
 * @author Canyon 2017-04-16 23:30
 */
@SuppressWarnings("rawtypes")
public class MapTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	Object key;
	Object val;
	Map data;

	public void setKey(Object key) {
		this.key = key;
	}

	public void setDefVal(Object defVal) {
		this.val = defVal;
	}

	public void setData(Map data) {
		this.data = data;
	}

	@Override
	public int doStartTag() throws JspException {
		try {

			String v = "";
			if (data != null && data.containsKey(key)) {
				v = data.get(key).toString();
			} else {
				if (val != null)
					v = val.toString();
			}
			pageContext.getOut().write(v);
		} catch (Exception e) {
		}
		return super.doStartTag();
	}

}
