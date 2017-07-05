package com.bowlong.sql.jdbc;

import java.util.List;

import com.bowlong.objpool.StringBufPool;
/**
 * 记录执行查询语句后所得的字段值
 * @author Canyon
 *
 */
public class PrepareSQLResult {
	public String sql;
	public List<String> keys; // 字段

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public String toString() {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("[").append(sql).append(", ").append(keys).append("]");
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}
}
