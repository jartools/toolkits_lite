package com.bowlong.sql.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.bowlong.objpool.StringBufPool;
import com.bowlong.sql.jdbc.BeanSupport;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataSet extends JdbcTemplate {
	public DataSet(Connection conn, String TABLENAME) {
		super(conn, TABLENAME);
	}

	public DataSet(DataSource ds, String TABLENAME) {
		super(ds, TABLENAME);
	}

	public DataSet(DataSource ds_r, DataSource ds_w, String TABLENAME) {
		super(ds_r, ds_w, TABLENAME);
	}

	/*
	 * 从 begin 条 开始, num 条记录 SELECT * FROM ( SELECT ROWNUM r, a.* FROM 表名 a
	 * WHERE 条件 ORDER BY id ) b WHERE b.r >= begin AND b.r < begin + num ;
	 */
	public List<Map> queryForList(String c, String idKey, int begin, int num) throws SQLException {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			if (c != null && !c.isEmpty()) {
				sb.append("SELECT * FROM (SELECT ROWNUM r, a.* FROM ").append(TABLENAME).append(" a WHERE ").append(c)
						.append(" ORDER BY ").append(idKey).append(") b WHERE b.r >= m AND b.r < m + n ");
			} else {
				sb.append("SELECT * FROM (SELECT ROWNUM r, a.* FROM ").append(TABLENAME).append(" a ")
						.append(" ORDER BY ").append(idKey).append(") b WHERE b.r >= m AND b.r < m + n  ");
			}
			sb.append(" ORDER BY ").append(idKey);
			String sql = sb.toString();
			return super.queryForList(sql);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public <T> List<T> queryForList(String c, String idKey, Class c2, int begin, int num) throws Exception {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			if (c != null && !c.isEmpty()) {
				sb.append("SELECT * FROM (SELECT ROWNUM r, a.* FROM ").append(TABLENAME).append(" a WHERE ").append(c)
						.append(" ORDER BY ").append(idKey).append(") b WHERE b.r >= m AND b.r < m + n ");
			} else {
				sb.append("SELECT * FROM (SELECT ROWNUM r, a.* FROM ").append(TABLENAME).append(" a ")
						.append(" ORDER BY ").append(idKey).append(") b WHERE b.r >= m AND b.r < m + n  ");
			}
			sb.append(" ORDER BY ").append(idKey);
			String sql = sb.toString();
			return super.queryForList(sql, c2);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public List<Map> queryForList2(String idKey, int begin, int num) throws SQLException {
		return queryForList("", idKey, begin, num);
	}

	public <T> List<T> queryForList2(String idKey, Class c2, int begin, int num) throws Exception {
		return queryForList("", idKey, c2, begin, num);
	}

	public int insert(BeanSupport x, String sqlId) throws SQLException {
		return insert(x.toBasicMap(), sqlId);
	}

	public int insert(Map<String, Object> m, String sqlId) throws SQLException {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			List<String> keys = newList();
			keys.addAll(m.keySet());
			int num = keys.size();
			sb.append("INSERT INTO ").append(TABLENAME).append(" (");
			for (int i = 0; i < num; i++) {
				sb.append(keys.get(i));
				if (i < num - 1) {
					sb.append(", ");
				}
			}
			sb.append(") VALUES (");
			for (int i = 0; i < num; i++) {
				sb.append(":").append(keys.get(i));
				if (i < num - 1) {
					sb.append(", ");
				}
			}
			sb.append(")");
			String sql = sb.toString();
			Map r = super.insert(sql, sqlId, m);
			if (r == null)
				return 0;
			return getInt(r, "GENERATED_KEY");
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

}
