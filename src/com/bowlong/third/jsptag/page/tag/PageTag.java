package com.bowlong.third.jsptag.page.tag;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.bowlong.io.FileRw;
import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.third.jsptag.page.PageEnt;

/**
 * 分页标签
 * 
 * @ClassName: PageTag
 * @author Canyon
 * 
 */
@SuppressWarnings("rawtypes")
public class PageTag extends TagSupport {

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 2771143709830017519L;
	static String pageTxt = "";
	private String action;
	private String pgName = "";

	/*** 在编辑工程中可以用 **/
	public void initTemplete() {
		if (!StrEx.isEmpty(pageTxt))
			return;
		String pkg = this.getClass().getPackage().getName();
		String path = this.getClass().getClassLoader().getResource("")
				.getPath();
		pkg = StrEx.package2Path(pkg);
		path = path + pkg + "/page_cur.txt";
		pageTxt = FileRw.readStr(path);
	}

	/*** 读取文件(通用) **/
	public void initTempleteByURl() {
		if (!StrEx.isEmpty(pageTxt))
			return;
		try {
			URL url = this.getClass().getResource("page_cur.txt");
			InputStream stream = url.openStream();
			pageTxt = FileRw.readText(stream, "UTF-8");
		} catch (IOException e) {
		}
	}

	public void setAction(String action) {
		this.action = action;
		initTempleteByURl();
	}

	private PageEnt pe;

	public void setName(String name) {
		if (StrEx.isEmpty(name)) {
			pgName = "";
		} else if (!name.equals("pageEnt")) {
			pgName = "_" + name;
		} else {
			pgName = "";
		}
		Object result = this.pageContext.findAttribute(name);
		if (result != null && result instanceof PageEnt) {
			this.pe = (PageEnt) result;
		}
	}

	private String wrapid = "";

	public void setWrapid(String wrapid) {
		this.wrapid = wrapid;
	}

	@Override
	public int doEndTag() throws JspException {
		if (this.pe == null) {
			return SKIP_BODY;
		}
		String pg = "";
		int allPage = pe.getTotalPages();
		int curPage = pe.getPage();
		
		if (allPage > 1) {
			StringBuffer buff = StringBufPool.borrowObject();
			try {
				boolean isMore = allPage > 8;
				int pointVector = 1;// 1左边展示点,2中间展示点,3右边展示点
				int begPage = 1;
				int endPage = 1;
				if (isMore) {
					int difPage = allPage - curPage;
					if (difPage > 7) {
						if (curPage == 1) {
							pointVector = 3;
						} else {
							pointVector = 2;
						}
						begPage = curPage;
						endPage = begPage + 7;
					} else {
						endPage = allPage;
						begPage = endPage - 7;
					}
				} else {
					begPage = 1;
					endPage = allPage;
				}

				for (; begPage <= endPage; begPage++) {
					buff.append("<a href=\"javascript:void(0);\"");

					if (begPage == curPage) {
						buff.append(" style=\"margin:0px 2px;padding:1px 12px;border:1px solid #f45819;\"");
					} else {
						buff.append(" style=\"margin:0px 2px;padding:1px 12px;\"");
					}

					buff.append(" onclick=\"click2sendPaging(").append(begPage)
							.append(")\">").append(begPage).append("</a>");
				}

				pg = buff.toString();
				if (isMore) {
					switch (pointVector) {
					case 1:
						pg = "..." + pg;
						break;
					case 2:
						pg = "..." + pg + "...";
						break;
					default:
						pg = pg + "...";
						break;
					}
				}
			} finally {
				StringBufPool.returnObject(buff);
			}
		}

		try {
			String ret = "";
			if (!StrEx.isEmpty(pg))
				ret = StrEx.fmt(pageTxt, pg, action, curPage, allPage, pgName,
						wrapid);
			JspWriter writer = this.pageContext.getOut();
			writer.print(ret);
			writer.flush();
			writer.clearBuffer();
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	public static void main(String[] args) {
		PageTag tag = new PageTag();
		tag.initTemplete();
		// tag.initTempleteByURl();
		System.out.println(pageTxt);
	}
}
