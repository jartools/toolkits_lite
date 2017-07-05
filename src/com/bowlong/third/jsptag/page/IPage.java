package com.bowlong.third.jsptag.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IPage<T> extends Serializable{
	/** 取得满足参数的总条数 */
	int countAll(Map<String, Object> params);
	/** 取得满足参数的分页列表对象 */
	List<T> getList(Map<String, Object> params,int page,int pageSize);
	/** 取得满足参数的分页实体 */
	PageEnt<T> getPage(Map<String, Object> params,int page,int pageSize);
}
