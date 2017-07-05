package com.bowlong.third.jsptag.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageDemo extends APage<String> {

	private static final long serialVersionUID = 1L;

	@Override
	public int countAll(Map<String, Object> params) {
		// 取得总页数
		return 0;
	}

	@Override
	public List<String> getList(Map<String, Object> params, int page,
			int pageSize) {
		// 取得列表
		return new ArrayList<String>();
	}

	@Override
	public PageEnt<String> getPage(Map<String, Object> params, int page,
			int pageSize) {
		int totalRecords = countAll(params);
		List<String> pagelist = getList(params, page, pageSize);
		pagelist = null;
		this.pageBean.reset(page, pageSize, totalRecords, pagelist);
		return this.pageBean;
	}

	public static void main(String[] args) {
		PageDemo demo = new PageDemo();
		Map<String, Object> params = null;
		int page = 1;
		int pageSize = 10;
		PageEnt<String> v = demo.getPage(params, page, pageSize);
		System.out.println(v.toString());
	}

}
