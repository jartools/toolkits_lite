package com.bowlong.sql.beanbasic;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bowlong.basic.ExToolkit;
import com.bowlong.sql.SqlEx;

/**
 * 添加bean 基础类
 * 
 * @author canyon/龚阳辉
 * @time 2019-02-18 17:24
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BeanBasic extends ExToolkit implements RsTHandler<BeanBasic>, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	static final public String insFmt = "INSERT INTO `%s` (%s) VALUES (%s)";
	static final public String selFmt = "SELECT * FROM `%s` WHERE ";
	static final public String upFmt = "UPDATE `%s` SET %s WHERE %s";
	static final public String delFmt = "DELETE FROM `%s` WHERE %s";

	private Map<String, Object> toMap(ResultSet rs) throws SQLException {
		return SqlEx.toMap(rs);
	}

	@Override
	public BeanBasic handle(ResultSet rs) throws SQLException {
		return toEntity(rs);
	}

	public <T extends BeanBasic> T toEntity(ResultSet rs) throws SQLException {
		return _newEntity(toMap(rs));
	}

	/** 必须实现:new对象并调用toEntity函数 */
	protected <T extends BeanBasic> T _newEntity(Map map) {
		return null; // 例如 return new BeanBasic().toEntity(map);
	}

	/** map -> entity */
	public <T extends BeanBasic> T toEntity(Map map) {
		return null;
	}

	/** entity -> map,子类想外部访问，可以将修饰语改成public */
	protected Map<String, Object> toMap(Map<String, Object> map) {
		return map;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = newMapT();
		return toMap(map);
	}

	protected Map<String, Object> toMap4Client(Map<String, Object> map) {
		return toMap(map);
	}

	public Map<String, Object> toMap4Client() {
		Map<String, Object> map = newMapT();
		return toMap4Client(map);
	}

	protected Map<String, Object> toMap4Html(Map<String, Object> map) {
		return toMap(map);
	}

	public Map<String, Object> toMap4Html() {
		Map<String, Object> map = newMapT();
		return toMap4Html(map);
	}

	protected Map<String, Object> toMap4Json(Map<String, Object> map) {
		return toMap(map);
	}

	public Map<String, Object> toMap4Json() {
		Map<String, Object> map = newMapT();
		return toMap4Json(map);
	}

	/** 清除部分 */
	public void clear() {
	}

	/** 清除全部 */
	public void clearAll() {
		clear();
	}
}
