package com.bowlong.sql.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		Connection conn = conn_w();
		Map params = null;
		try {
			params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public int insert(final String sql, final BeanSupport x) throws SQLException {
		Connection conn = conn_w();
		Map params = null;
		try {
			int r2 = 0;
			params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql, PreparedStatement.RETURN_GENERATED_KEYS);
			prepareMap(stmt, sr.keys, params);
			int r = stmt.executeUpdate();
			if (r < 0)
				throw new SQLException(" r = 0");

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next())
				r2 = rs.getInt(1);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public int[] batchInsert4LBean(final String sql, final List<BeanSupport> list) throws SQLException {
		Connection conn = conn_w();
		try {
			int[] r2 = new int[list.size()];
			PrepareSQLResult sr = prepareKeys(sql);
			// PreparedStatement stmt = conn.prepareStatement(sr.sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql, PreparedStatement.RETURN_GENERATED_KEYS);

			for (BeanSupport x : list) {
				Map params = x.toBasicMap();
				prepareMap(stmt, sr.keys, params);
				stmt.addBatch();
			}
			stmt.executeBatch();
			ResultSet rs = stmt.getGeneratedKeys();
			int i = 0;
			while (rs.next()) {
				r2[i++] = rs.getInt(1);
			}
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, list);
		} finally {
			close(conn);
		}
	}

	public CachedRowSet query(final String sql, final BeanSupport x) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return query(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final <T> T query(final String sql, final BeanSupport x, final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return query(sql, x, rsh);
	}

	public <T> T query(final String sql, final BeanSupport x, final ResultSetHandler rsh) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return query(sql, params, rsh);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final <T> T queryForObject(final String sql, final BeanSupport x, final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return query(sql, x, rsh);
	}

	public final <T> T queryForObject(final String sql, final BeanSupport x, final ResultSetHandler rsh)
			throws SQLException {
		Map params = x.toBasicMap();
		return query(sql, params, rsh);
	}

	public Map queryForMap(final String sql, final BeanSupport x) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForMap(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public List<Map> queryForList(final String sql, final BeanSupport x) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForList(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final <T> List<T> queryForList(final String sql, final BeanSupport x, final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return queryForList(sql, x, rsh);
	}

	public <T> List<T> queryForList(final String sql, final BeanSupport x, final ResultSetHandler rsh)
			throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForList(sql, params, rsh);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public long queryForLong(final String sql, final BeanSupport x) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForLong(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public int queryForInt(final String sql, final BeanSupport x) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForInt(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public double queryForDouble(final String sql, final BeanSupport x) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForDouble(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final RowSet queryForRowSet(final String sql, final BeanSupport x) throws SQLException {
		Map params = x.toBasicMap();
		return query(sql, params);
	}

	public int update(final String sql, final BeanSupport x) throws SQLException {
		Connection conn = conn_w();
		Map params = null;
		try {
			int r2 = 0;
			params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			r2 = stmt.executeUpdate();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public int[] batchUpdate4LBean(final String sql, final List<BeanSupport> list) throws SQLException {
		Connection conn = conn_w();
		try {
			int r2[] = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			for (BeanSupport x : list) {
				Map map = x.toBasicMap();
				prepareMap(stmt, sr.keys, map);
				stmt.addBatch();
			}
			r2 = stmt.executeBatch();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, list);
		} finally {
			close(conn);
		}
	}

	public void call(final String sql, final BeanSupport x) throws SQLException {
		Connection conn = conn_w();
		Map params = null;
		try {
			params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			CallableStatement stmt = conn.prepareCall(sr.sql);
			prepareMap(stmt, sr.keys, params);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public List<Map> queryBycall(final String sql, final BeanSupport x) throws SQLException {
		Connection conn = conn_w();
		Map params = null;
		try {
			List<Map> r2 = null;
			params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			CallableStatement stmt = conn.prepareCall(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			r2 = toMaps(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}
}
