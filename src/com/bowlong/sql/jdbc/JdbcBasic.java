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

import com.bowlong.sql.beanbasic.RsHandler;
import com.sun.rowset.CachedRowSetImpl;

@SuppressWarnings("all")
public class JdbcBasic extends JdbcOrigin {
	// /////////////////////////
	/*** 处理查询结果 **/
	private static final Map<Class, RsHandler> RSHCHCHE = newMap();

	static final RsHandler getRsh(Class c) throws Exception {
		RsHandler rsh = RSHCHCHE.get(c);
		if (rsh == null) {
			rsh = (RsHandler) c.newInstance();
			RSHCHCHE.put(c, rsh);
		}

		return rsh;
	}

	public JdbcBasic(final Connection conn) {
		super(conn);
	}

	public JdbcBasic(final DataSource ds) {
		super(ds);
	}

	public JdbcBasic(final DataSource ds_r, final DataSource ds_w) {
		super(ds_r, ds_w);
	}

	public <T> T queryForObject(String sql, Class c) throws Exception {
		RsHandler rsh = getRsh(c);
		return queryForObject(sql, rsh);
	}

	public <T> List<T> queryForList(String sql, Class c) throws Exception {
		RsHandler rsh = getRsh(c);
		return queryForList(sql, rsh);
	}

	// /////////////////////////
	public void execute(String sql, Map params) throws SQLException {
		Connection conn = conn_w();
		try {
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

	public Map insert(String sql, Map params) throws SQLException {
		Connection conn = conn_w();
		try {
			Map<String, Object> r2 = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql, PreparedStatement.RETURN_GENERATED_KEYS);
			prepareMap(stmt, sr.keys, params);
			int r = stmt.executeUpdate();
			if (r < 0)
				throw new SQLException(" r = 0");

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next())
				r2 = toMap(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public int insert2(String sql, Map params) throws SQLException {
		Map<String, Object> map = insert(sql, params);
		if (map != null && !map.isEmpty()) {
			for (Object val : map.values()) {
				return (int) val;
			}
		}
		return 0;
	}

	public long[] batchInsertGK(String sql, List<Map> list) throws SQLException {
		Connection conn = conn_w();
		try {
			long[] r2 = new long[list.size()];
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql, PreparedStatement.RETURN_GENERATED_KEYS);
			for (Map map : list) {
				prepareMap(stmt, sr.keys, map);
				stmt.addBatch();
			}
			stmt.executeBatch();
			ResultSet rs = stmt.getGeneratedKeys();
			int i = 0;
			while (rs.next()) {
				r2[i++] = rs.getLong(1);
			}
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, list);
		} finally {
			close(conn);
		}
	}

	public int[] batchInsert(String sql, List<Map> list) throws SQLException {
		try {
			long[] r2 = batchInsertGK(sql, list);
			int lens = r2.length;
			int[] r1 = new int[lens];
			for (int i = 0; i < lens; i++) {
				r1[i] = (int) r2[i];
			}
			return r1;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	public final <T> T query(String sql, Map params, Class c) throws Exception {
		RsHandler rsh = getRsh(c);
		return query(sql, params, rsh);
	}

	public final <T> T query(String sql, Class c) throws Exception {
		RsHandler rsh = getRsh(c);
		return query(sql, rsh);
	}

	public final <T> T queryForObject(String sql, Map params, Class c) throws Exception {
		RsHandler rsh = getRsh(c);
		return queryForObject(sql, params, rsh);
	}

	public final <T> T queryForObject(String sql, Map params, RsHandler rsh) throws SQLException {
		return query(sql, params, rsh);
	}

	public Map queryForMap(String sql, Map params) throws SQLException {
		Connection conn = conn_r();
		try {
			Map r2 = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = toMap(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public List<Map<String, Object>> queryForList(String sql, Map params) throws SQLException {
		Connection conn = conn_r();
		try {
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			List<Map<String, Object>> r2 = toMaps(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public <T> List<T> queryForKeys(String sql, Map params) throws SQLException {
		Connection conn = conn_r();
		try {
			List<T> r2 = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			r2 = toKeys(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public final <T> List<T> queryForList(String sql, Map params, Class c) throws Exception {
		RsHandler rsh = getRsh(c);
		return queryForList(sql, params, rsh);
	}

	public long queryForLong(String sql, Map params) throws SQLException {
		Connection conn = conn_r();
		try {
			long r2 = 0;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = rs.getLong(1);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public int queryForInt(String sql, Map params) throws SQLException {
		Connection conn = conn_r();
		try {
			int r2 = 0;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
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

	public double queryForDouble(String sql, Map params) throws SQLException {
		Connection conn = conn_r();
		try {
			double r2 = 0;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = rs.getDouble(1);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public final RowSet queryForRowSet(String sql, Map params) throws SQLException {
		return query(sql, params);
	}

	public int update(String sql, Map params) throws SQLException {
		Connection conn = conn_w();
		try {
			int r2 = 0;
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

	public int[] batchUpdate(String sql, List<Map> list) throws SQLException {
		Connection conn = conn_w();
		try {
			int r2[] = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			for (Map map : list) {
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

	public void call(String sql, Map params) throws SQLException {
		Connection conn = conn_w();
		try {
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

	public List<Map<String, Object>> queryByCall(String sql, Map params) throws SQLException {
		Connection conn = conn_w();
		try {
			PrepareSQLResult sr = prepareKeys(sql);
			CallableStatement stmt = conn.prepareCall(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			List<Map<String, Object>> r2 = toMaps(rs);
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
