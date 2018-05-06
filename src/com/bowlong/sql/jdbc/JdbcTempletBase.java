package com.bowlong.sql.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

@SuppressWarnings("all")
public class JdbcTempletBase extends JdbcTempletOrigin {
	// /////////////////////////
	/*** 缓存查询过后的字段 **/
	private static final Map<String, PrepareSQLResult> SQLCHCHE = newMap();
	/*** 处理查询结果 **/
	private static final Map<Class, ResultSetHandler> RSHCHCHE = newMap();

	private static final ResultSetHandler getRsh(Class c) throws Exception {
		ResultSetHandler rsh = RSHCHCHE.get(c);
		if (rsh == null) {
			rsh = (ResultSetHandler) c.newInstance();
			RSHCHCHE.put(c, rsh);
		}

		return rsh;
	}

	public JdbcTempletBase(final Connection conn) {
		super(conn);
	}

	public JdbcTempletBase(final DataSource ds) {
		super(ds);
	}

	public JdbcTempletBase(final DataSource ds_r, final DataSource ds_w) {
		super(ds_r, ds_w);
	}

	public <T> T queryForObject(final String sql, final Class c)
			throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return queryForObject(sql, rsh);
	}

	public <T> List<T> queryForList(final String sql, final Class c)
			throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return queryForList(sql, rsh);
	}

	static public final PrepareSQLResult prepareKeys(final String sql) {
		if (SQLCHCHE.containsKey(sql)) {
			// 从缓存中读取
			return SQLCHCHE.get(sql);
		}

		PrepareSQLResult result = new PrepareSQLResult();

		List<String> keys = new Vector<String>();
		// 没有缓存,则从头获取
		String sql2 = sql;
		int index = 0;
		int times = 10000;
		while (true) {
			if (times-- <= 0)
				break;

			index++;
			int p1 = sql2.indexOf(":");
			if (p1 < 0)
				break;
			p1 = p1 + ":".length();
			int p2 = sql2.indexOf(",", p1);
			int p3 = sql2.indexOf(" ", p1);
			int p4 = sql2.indexOf(")", p1);
			int p5 = sql2.length();
			if (p3 > 0)
				p2 = (p2 >= 0 && p2 < p3) ? p2 : p3;
			if (p4 > 0)
				p2 = (p2 >= 0 && p2 < p4) ? p2 : p4;
			if (p5 > 0)
				p2 = (p2 >= 0 && p2 < p5) ? p2 : p5;

			String key = sql2.substring(p1, p2).trim();
			String okey = String.format(":%s", key);
			sql2 = sql2.replaceFirst(okey, "?");
			keys.add(key);
		}

		result.setSql(sql2);
		result.setKeys(keys);

		// 写入缓存
		SQLCHCHE.put(sql, result);

		return result;
	}

	static public final PreparedStatement prepareMap(
			final PreparedStatement stmt, final List<String> keys, final Map m)
			throws SQLException {
		int index = 0;
		for (String key : keys) {
			index++;
			Object var = m.get(key);
			stmt.setObject(index, var);
		}
		return stmt;
	}

	// /////////////////////////
	public void execute(final String sql, final Map params) throws SQLException {
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

	public Map insert(final String sql, final Map params) throws SQLException {
		Connection conn = conn_w();
		try {
			Map r2 = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
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

	public int insert2(final String sql, final Map params) throws SQLException {
		Connection conn = conn_w();
		try {
			int r2 = 0;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
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

	public int[] batchInsert4LMap(final String sql, final List<Map> list)
			throws SQLException {
		Connection conn = conn_w();
		try {
			int[] r2 = new int[list.size()];
			PrepareSQLResult sr = prepareKeys(sql);
			// PreparedStatement stmt = conn.prepareStatement(sr.sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql,
					PreparedStatement.RETURN_GENERATED_KEYS);

			for (Map map : list) {
				prepareMap(stmt, sr.keys, map);
				stmt.addBatch();
			}
			// r2 = stmt.executeBatch();
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

	public CachedRowSet query(final String sql, final Map params)
			throws SQLException {
		Connection conn = conn_r();
		try {
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			CachedRowSet crs = new CachedRowSetImpl();
			crs.populate(rs);
			rs.close();
			stmt.close();
			return crs;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public final <T> T query(final String sql, final Map params, final Class c)
			throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return query(sql, params, rsh);
	}

	public <T> T query(final String sql, final Map params,
			final ResultSetHandler rsh) throws SQLException {
		Connection conn = conn_r();
		try {
			T r2 = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = (T) rsh.handle(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public final <T> T queryForObject(final String sql, final Map params,
			final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return queryForObject(sql, params, rsh);
	}

	public final <T> T queryForObject(final String sql, final Map params,
			final ResultSetHandler rsh) throws SQLException {
		return query(sql, params, rsh);
	}

	public Map queryForMap(final String sql, final Map params)
			throws SQLException {
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

	public List<Map> queryForList(final String sql, final Map params)
			throws SQLException {
		Connection conn = conn_r();
		try {
			List<Map> r2 = null;
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
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

	public <T> List<T> queryForKeys(final String sql, final Map params)
			throws SQLException {
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

	public final <T> List<T> queryForList(final String sql, final Map params,
			final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return queryForList(sql, params, rsh);
	}

	public <T> List<T> queryForList(final String sql, final Map params,
			final ResultSetHandler rsh) throws SQLException {
		Connection conn = conn_r();
		try {
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql);
			prepareMap(stmt, sr.keys, params);
			ResultSet rs = stmt.executeQuery();
			List<T> result = newList();
			while (rs.next()) {
				T v = (T) rsh.handle(rs);
				result.add(v);
			}
			rs.close();
			stmt.close();
			return result;
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		} finally {
			close(conn);
		}
	}

	public long queryForLong(final String sql, final Map params)
			throws SQLException {
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

	public int queryForInt(final String sql, final Map params)
			throws SQLException {
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

	public double queryForDouble(final String sql, final Map params)
			throws SQLException {
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

	public final RowSet queryForRowSet(final String sql, final Map params)
			throws SQLException {
		return query(sql, params);
	}

	public int update(final String sql, final Map params) throws SQLException {
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

	public int[] batchUpdate4LMap(final String sql, final List<Map> list)
			throws SQLException {
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

	public void call(final String sql, final Map params) throws SQLException {
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

	public List<Map> queryByCall(final String sql, final Map params)
			throws SQLException {
		Connection conn = conn_w();
		try {
			List<Map> r2 = null;
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

	// /////////////////////////
	public void execute(final String sql, final BeanSupport x)
			throws SQLException {
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

	public Map insert(final String sql, final BeanSupport x)
			throws SQLException {
		Connection conn = conn_w();
		Map r2 = null;
		Map params = null;
		try {
			params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
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

	public int insert2(final String sql, final BeanSupport x)
			throws SQLException {
		Connection conn = conn_w();
		Map params = null;
		try {
			int r2 = 0;
			params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
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

	public int[] batchInsert4LBean(final String sql, final List<BeanSupport> list)
			throws SQLException {
		Connection conn = conn_w();
		try {
			int[] r2 = new int[list.size()];
			PrepareSQLResult sr = prepareKeys(sql);
			// PreparedStatement stmt = conn.prepareStatement(sr.sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql,
					PreparedStatement.RETURN_GENERATED_KEYS);

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

	public CachedRowSet query(final String sql, final BeanSupport x)
			throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return query(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final <T> T query(final String sql, final BeanSupport x,
			final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return query(sql, x, rsh);
	}

	public <T> T query(final String sql, final BeanSupport x,
			final ResultSetHandler rsh) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return query(sql, params, rsh);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final <T> T queryForObject(final String sql, final BeanSupport x,
			final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return query(sql, x, rsh);
	}

	public final <T> T queryForObject(final String sql, final BeanSupport x,
			final ResultSetHandler rsh) throws SQLException {
		Map params = x.toBasicMap();
		return query(sql, params, rsh);
	}

	public Map queryForMap(final String sql, final BeanSupport x)
			throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForMap(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public List<Map> queryForList(final String sql, final BeanSupport x)
			throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForList(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final <T> List<T> queryForList(final String sql,
			final BeanSupport x, final Class c) throws Exception {
		ResultSetHandler rsh = getRsh(c);
		return queryForList(sql, x, rsh);
	}

	public <T> List<T> queryForList(final String sql, final BeanSupport x,
			final ResultSetHandler rsh) throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForList(sql, params, rsh);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public long queryForLong(final String sql, final BeanSupport x)
			throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForLong(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public int queryForInt(final String sql, final BeanSupport x)
			throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForInt(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public double queryForDouble(final String sql, final BeanSupport x)
			throws SQLException {
		Map params = null;
		try {
			params = x.toBasicMap();
			return queryForDouble(sql, params);
		} catch (SQLException e) {
			throw rethrow(e, sql, params);
		}
	}

	public final RowSet queryForRowSet(final String sql, final BeanSupport x)
			throws SQLException {
		Map params = x.toBasicMap();
		return query(sql, params);
	}

	public int update(final String sql, final BeanSupport x)
			throws SQLException {
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

	public int[] batchUpdate4LBean(final String sql, final List<BeanSupport> list)
			throws SQLException {
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

	public List<Map> queryBycall(final String sql, final BeanSupport x)
			throws SQLException {
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

	public static void main(String[] args) throws Exception {

	}
}
