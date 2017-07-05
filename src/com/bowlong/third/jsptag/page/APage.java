package com.bowlong.third.jsptag.page;

import java.util.List;
import java.util.Map;

public abstract class APage<T> implements IPage<T> {

	private static final long serialVersionUID = 1L;
	
	/** 分页实体对象 */
	protected PageEnt<T> pageBean;

	/** 取得分页实体对象 */
	public PageEnt<T> getPageBean() {
		return pageBean;
	}

	/** 设置分页实体对象 */
	public void setPageBean(PageEnt<T> pageBean) {
		this.pageBean = pageBean;
	}

	public APage() {
		super();
		this.pageBean = new PageEnt<T>();
	}

	public APage(PageEnt<T> pageBean) {
		super();
		this.pageBean = pageBean;
		if (this.pageBean == null)
			this.pageBean = new PageEnt<T>();
	}

	@Override
	public PageEnt<T> getPage(Map<String, Object> params, int page, int pageSize) {
		int totalRecords = countAll(params);
		List<T> pagelist = getList(params, page, pageSize);
		this.pageBean.reset(page, pageSize, totalRecords, pagelist);
		return this.pageBean;
	}
}
