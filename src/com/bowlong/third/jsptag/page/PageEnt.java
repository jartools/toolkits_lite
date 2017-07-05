package com.bowlong.third.jsptag.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 普通Java分页支持Bean
 * 
 */
public class PageEnt<T> implements Serializable{
	
	/** 序列化 */
	private static final long serialVersionUID = 1L;
	
	/** 每页记录数 */
	private int pageSize = 10;
	/** 当前页码 */
	private int page = 1;
	/** 总记录数 */
	private int totalRecords = 0;
	/** 总页数 */
	private int totalPages = 1;
	/** 是否有上一页 */
	private boolean havePrePage = false;
	/** 是否有下一页 */
	private boolean haveNextPage = false;
	/** 目标Url */
	private String targetUrl = "";
	/** 具体的分页数据 */
	private List<T> listPages = new ArrayList<T>();

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1)
			pageSize = 1;
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1)
			page = 1;
		this.page = page;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		if (totalRecords < 0)
			totalRecords = 0;

		if (this.pageSize < 1)
			this.pageSize = 1;

		this.totalRecords = totalRecords;
		double allCount = this.totalRecords;
		double discuss = Math.ceil(allCount / this.pageSize);

		// 计算总页数
		this.totalPages = (int) discuss;
		if (this.totalPages == 0) {
			this.page = 1;
		}
		// 计算是否有上一页
		this.havePrePage = this.page > 1;
		// 计算是否有下一页
		this.haveNextPage = this.page < this.totalPages;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public boolean getHavePrePage() {
		return havePrePage;
	}

	public boolean getHaveNextPage() {
		return haveNextPage;
	}

	public List<T> getListPages() {
		return listPages;
	}

	public void setListPages(List<T> listPages) {
		if (listPages == null)
			listPages = new ArrayList<T>();
		this.listPages.clear();
		this.listPages.addAll(listPages);
	}

	// 重新设置值
	public void reset(int page, int pageSize, int totalRecords,
			List<T> listPages) {
		reset(page, pageSize, totalRecords, listPages, "");
	}

	// 重新设置值
	public void reset(int page, int pageSize, int totalRecords,
			List<T> listPages, String targetUrl) {
		setPage(page);
		setPageSize(pageSize);
		setTotalRecords(totalRecords);
		setListPages(listPages);
		setTargetUrl(targetUrl);
	}

	@Override
	public String toString() {
		return "PageEnt [pageSize=" + pageSize + ", page=" + page
				+ ", totalRecords=" + totalRecords + ", totalPages="
				+ totalPages + ", havePrePage=" + havePrePage
				+ ", haveNextPage=" + haveNextPage + ", targetUrl=" + targetUrl
				+ ", listPages.size = " + listPages.size() + "]";
	}

}
