package com.bowlong.sql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.bowlong.objpool.StringBufPool;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DataSet extends JdbcTempletBase {

	private String TABLENAME;

	public DataSet(Connection conn, String TABLENAME) {
		super(conn);
		this.TABLENAME = TABLENAME;
	}

	public DataSet(DataSource ds, String TABLENAME) {
		super(ds);
		this.TABLENAME = TABLENAME;
	}

	public DataSet(DataSource ds_r, DataSource ds_w, String TABLENAME) {
		super(ds_r, ds_w);
		this.TABLENAME = TABLENAME;
	}

	public final int count(String c) throws SQLException {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("SELECT COUNT(*) FROM ").append(TABLENAME);
			if (c != null && !c.isEmpty()) {
				sb.append(" WHERE ").append(c);
			}
			String sql = sb.toString();
			return super.queryForInt(sql);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final int count() throws SQLException {
		return count("");
	}

	public final int pageCount(final String c, final int size)
			throws SQLException {
		int count = count(c);
		return super.pageCount(count, size);
	}

	public final int pageCount(final int size) throws SQLException {
		return pageCount("", size);
	}

	public final List<Map> queryAll() throws SQLException {
		return queryForList("");
	}

	public final <T> List<T> queryAll(final Class c2) throws Exception {
		return queryForList("", c2);
	}

	public final <T> T queryForObject(final String c, final Class c2)
			throws Exception {
		List<T> dataset = queryForList(c, c2);
		if (dataset == null || dataset.isEmpty())
			return null;
		return dataset.get(0);
	}

	public final Map queryForMap(final String c) throws SQLException {
		List<Map> dataset = queryForList(c);
		if (dataset == null || dataset.isEmpty())
			return null;
		return dataset.get(0);
	}

	public final List<Map> queryForList(final String c) throws SQLException {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("SELECT * FROM ").append(TABLENAME);
			if (c != null && !c.isEmpty()) {
				sb.append(" WHERE ").append(c);
			}
			String sql = sb.toString();
			return super.queryForList(sql);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final <T> List<T> queryForList(final String c, final Class c2)
			throws Exception {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("SELECT * FROM ").append(TABLENAME);
			if (c != null && !c.isEmpty()) {
				sb.append(" WHERE ").append(c);
			}
			String sql = sb.toString();
			return super.queryForList(sql, c2);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final List<Map> queryForList(final String c, final int begin,
			final int num) throws SQLException {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("SELECT * FROM ").append(TABLENAME);
			if (c != null && !c.isEmpty()) {
				sb.append(" WHERE ").append(c).append(" LIMIT ").append(begin)
						.append(", ").append(num);
			}
			String sql = sb.toString();
			return super.queryForList(sql);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final <T> List<T> queryForList(final String c, final Class c2,
			final int begin, final int num) throws Exception {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("SELECT * FROM ").append(TABLENAME);
			if (c != null && !c.isEmpty()) {
				sb.append(" WHERE ").append(c).append(" LIMIT ").append(begin)
						.append(", ").append(num);
			}
			String sql = sb.toString();
			return super.queryForList(sql, c2);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final List<Map> queryForList(final int begin, final int num)
			throws SQLException {
		return queryForList("", begin, num);
	}

	public final <T> List<T> queryForList(final Class c2, final int begin,
			final int num) throws Exception {
		return queryForList("", c2, begin, num);
	}

	public final int insert(BeanSupport x) throws SQLException {
		return insert(x.toBasicMap());
	}

	public final int insert(final Map<String, Object> m) throws SQLException {
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
			Map r = super.insert(sql, m);
			if (r == null)
				return 0;
			return getInt(r, "GENERATED_KEY");
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final int update(final BeanSupport x, final String c)
			throws SQLException {
		return update(x.toBasicMap(), c);
	}

	public final int update(final Map<String, Object> m, final String c)
			throws SQLException {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			List<String> keys = newList();
			keys.addAll(m.keySet());
			int num = keys.size();
			sb.append("UPDATE ").append(TABLENAME).append(" SET ");
			for (int i = 0; i < num; i++) {
				String key = keys.get(i);
				sb.append(key).append("=:").append(key);
				if (i < num - 1) {
					sb.append(", ");
				}
			}
			if (c != null && !c.isEmpty()) {
				sb.append(" WHERE ").append(c);
			}
			String sql = sb.toString();
			return super.update(sql, m);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final int delete(final Map<String, Object> m, final String c)
			throws SQLException {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("DELETE FROM ");
			sb.append(TABLENAME);
			if (c != null && !c.isEmpty()) {
				sb.append(" WHERE ");
				sb.append(c);
			}
			String sql = sb.toString();
			return super.update(sql, m);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public final int delete(final BeanSupport x, final String c)
			throws SQLException {
		return delete(x.toBasicMap(), c);
	}

	public final int delete(final String c) throws SQLException {
		return delete(newMap(), c);
	}

	public final void truncate() throws SQLException {
		super.truncate(TABLENAME);
	}

	public final void repair() throws SQLException {
		super.repair(TABLENAME);
	}

	public final void optimize() throws SQLException {
		super.optimize(TABLENAME);
	}

	// public class QuerySQL {
	// public String getSql(String TABLENAME, String c, int begin, int num){
	// StringBuffer sb = new StringBuffer();
	// sb.append("SELECT * FROM ").append(TABLENAME);
	// if (c != null && !c.isEmpty()) {
	// sb.append(" WHERE ").append(c);
	// }
	// if(begin > 0 && num > 0){
	// sb.append(" LIMIT ").append(begin).append(", ").append(num);
	// }else if(num > 0){
	// sb.append(" LIMIT ").append(num);
	// }
	// String sql = sb.toString();
	// return sql;
	// }
	// }

}
