package com.bowlong.sql.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

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

	public void execute(final String sql, final BeanSupport x) throws SQLException {
		execute(sql, x.toBasicMap());
	}

	public int insert(final String sql, final BeanSupport x) throws SQLException {
		return insert2(sql, x.toBasicMap());
	}

	public int[] batchInsert4LBean(final String sql, final List<BeanSupport> list) throws SQLException {
		List<Map> listMap = newList();
		for (BeanSupport x : list) {
			listMap.add(x.toBasicMap());
		}
		return batchInsert(sql, listMap);
	}

	public CachedRowSet query(final String sql, final BeanSupport x) throws SQLException {
		return query(sql, x.toBasicMap());
	}

	public final <T> T query(final String sql, final BeanSupport x, final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return query(sql, x, rsh);
	}

	public <T> T query(final String sql, final BeanSupport x, final ResultSetHandler rsh) throws SQLException {
		return query(sql, x.toBasicMap(), rsh);
	}

	public final <T> T queryForObject(final String sql, final BeanSupport x, final Class c) throws Exception {
		return query(sql, x, getRsh(c));
	}

	public final <T> T queryForObject(final String sql, final BeanSupport x, final ResultSetHandler rsh)
			throws SQLException {
		return query(sql, x.toBasicMap(), rsh);
	}

	public Map queryForMap(final String sql, final BeanSupport x) throws SQLException {
		return queryForMap(sql, x.toBasicMap());
	}

	public List<Map> queryForList(final String sql, final BeanSupport x) throws SQLException {
		return queryForList(sql, x.toBasicMap());
	}

	public final <T> List<T> queryForList(final String sql, final BeanSupport x, final Class c) throws Exception {
		return queryForList(sql, x, getRsh(c));
	}

	public <T> List<T> queryForList(final String sql, final BeanSupport x, final ResultSetHandler rsh)
			throws SQLException {
		return queryForList(sql, x.toBasicMap(), rsh);
	}

	public long queryForLong(final String sql, final BeanSupport x) throws SQLException {
		return queryForLong(sql, x.toBasicMap());
	}

	public int queryForInt(final String sql, final BeanSupport x) throws SQLException {
		return queryForInt(sql, x.toBasicMap());
	}

	public double queryForDouble(final String sql, final BeanSupport x) throws SQLException {
		return queryForDouble(sql, x.toBasicMap());
	}

	public final RowSet queryForRowSet(final String sql, final BeanSupport x) throws SQLException {
		return query(sql, x.toBasicMap());
	}

	public int update(final String sql, final BeanSupport x) throws SQLException {
		return update(sql, x.toBasicMap());
	}

	public int[] batchUpdate4LBean(final String sql, final List<BeanSupport> list) throws SQLException {
		List<Map> listMap = newList();
		for (BeanSupport x : list) {
			listMap.add(x.toBasicMap());
		}
		return batchUpdate(sql, listMap);
	}

	public void call(final String sql, final BeanSupport x) throws SQLException {
		call(sql, x.toBasicMap());
	}

	public List<Map> queryByCall(final String sql, final BeanSupport x) throws SQLException {
		return queryByCall(sql, x.toBasicMap());
	}
}
