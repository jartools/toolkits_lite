package com.bowlong.sql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.bowlong.sql.beanbasic.BeanSupport;
import com.bowlong.sql.beanbasic.RsHandler;

@SuppressWarnings("all")
public class JdbcTemplet extends JdbcBasic {
	public JdbcTemplet(final Connection conn) {
		super(conn);
	}

	public JdbcTemplet(final DataSource ds) {
		super(ds);
	}

	public JdbcTemplet(final DataSource ds_r, final DataSource ds_w) {
		super(ds_r, ds_w);
	}

	public void execute(String sql, BeanSupport x) throws SQLException {
		execute(sql, x.toBasicMap());
	}

	public int insert(String sql, BeanSupport x) throws SQLException {
		return insert2(sql, x.toBasicMap());
	}

	public long[] batchInsert4LBean(String sql, List<BeanSupport> list) throws SQLException {
		List<Map> listMap = newList();
		for (BeanSupport x : list) {
			listMap.add(x.toBasicMap());
		}
		return batchInsertGK(sql, listMap);
	}

	public CachedRowSet query(String sql, BeanSupport x) throws SQLException {
		return query(sql, x.toBasicMap());
	}

	public final <T> T query(String sql, BeanSupport x, Class c) throws Exception {
		RsHandler rsh = getRsh(c);
		return query(sql, x, rsh);
	}

	public <T> T query(String sql, BeanSupport x, RsHandler rsh) throws SQLException {
		return query(sql, x.toBasicMap(), rsh);
	}

	public final <T> T queryForObject(String sql, BeanSupport x, Class c) throws Exception {
		return query(sql, x, getRsh(c));
	}

	public final <T> T queryForObject(String sql, BeanSupport x, RsHandler rsh) throws SQLException {
		return query(sql, x.toBasicMap(), rsh);
	}

	public Map queryForMap(String sql, BeanSupport x) throws SQLException {
		return queryForMap(sql, x.toBasicMap());
	}

	public List<Map<String, Object>> queryForList(String sql, BeanSupport x) throws SQLException {
		return queryForList(sql, x.toBasicMap());
	}

	public final <T> List<T> queryForList(String sql, BeanSupport x, Class c) throws Exception {
		return queryForList(sql, x, getRsh(c));
	}

	public <T> List<T> queryForList(String sql, BeanSupport x, RsHandler rsh) throws SQLException {
		return queryForList(sql, x.toBasicMap(), rsh);
	}

	public long queryForLong(String sql, BeanSupport x) throws SQLException {
		return queryForLong(sql, x.toBasicMap());
	}

	public int queryForInt(String sql, BeanSupport x) throws SQLException {
		return queryForInt(sql, x.toBasicMap());
	}

	public double queryForDouble(String sql, BeanSupport x) throws SQLException {
		return queryForDouble(sql, x.toBasicMap());
	}

	public final RowSet queryForRowSet(String sql, BeanSupport x) throws SQLException {
		return query(sql, x.toBasicMap());
	}

	public int update(String sql, BeanSupport x) throws SQLException {
		return update(sql, x.toBasicMap());
	}

	public int[] batchUpdate4LBean(String sql, List<BeanSupport> list) throws SQLException {
		List<Map> listMap = newList();
		for (BeanSupport x : list) {
			listMap.add(x.toBasicMap());
		}
		return batchUpdate(sql, listMap);
	}

	public void call(String sql, BeanSupport x) throws SQLException {
		call(sql, x.toBasicMap());
	}

	public List<Map<String, Object>> queryByCall(String sql, BeanSupport x) throws SQLException {
		return queryByCall(sql, x.toBasicMap());
	}
}
