package com.bowlong.sql.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.sql.DataSource;
import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.lang.StrEx;
import com.bowlong.lang.task.MyThreadFactory;
import com.bowlong.sql.SqlEx;
import com.bowlong.third.FastJSON;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;
import com.bowlong.util.NewSet;
import com.sun.rowset.CachedRowSetImpl;

@SuppressWarnings("all")
public class JdbcTempletOrigin {
	/*** 记录数据连接对象目录下的所有表名称 **/
	private static final Map<String, NewSet<String>> TABLES = newMap();
	// 数据库连接模式[conn如果不为空，则只有一个连接]
	Connection conn; // 单链接模式
	DataSource ds_r; // 读写分离模式(读数据源)
	DataSource ds_w; // 读写分离模式(写数据源)

	// 检索此 Connection 对象的当前目录名称。
	private String catalog_r;
	// 检索此 Connection 对象的当前目录名称。
	private String catalog_w;

	public static final Log getLog(Class<?> clazz) {
		return LogFactory.getLog(clazz);
	}

	public JdbcTempletOrigin(final Connection conn) {
		this.conn = conn;
	}

	public JdbcTempletOrigin(final DataSource ds) {
		this.ds_r = ds;
		this.ds_w = ds;
	}

	public JdbcTempletOrigin(final DataSource ds_r, final DataSource ds_w) {
		this.ds_r = ds_r;
		this.ds_w = ds_w;
	}

	public DataSource ds_r() {
		return ds_r;
	}

	public DataSource ds_w() {
		return ds_w;
	}

	public String catalog_r() throws SQLException {
		if (!StrEx.isEmpty(catalog_r))
			return catalog_r;

		Connection conn = null;
		try {
			conn = conn_r();
			catalog_r = conn.getCatalog();
			return catalog_r;
		} finally {
			closeNoExcept(conn);
		}
	}

	public String catalog_w() throws SQLException {
		if (!StrEx.isEmpty(catalog_w))
			return catalog_w;

		Connection conn = null;
		try {
			conn = conn_w();
			catalog_w = conn.getCatalog();
			return catalog_w;
		} finally {
			closeNoExcept(conn);
		}
	}

	// /////////////////////////
	public void close() throws SQLException {
		try {
			if (this.conn != null && !this.conn.isClosed()) {
				this.conn.close();
			}
		} finally {
			this.conn = null;
			this.ds_r = null;
			this.ds_w = null;
		}
	}

	protected final void closeNoExcept(final Connection conn) {
		try {
			close(conn);
		} catch (Exception e) {
		}
	}

	protected final void close(final Connection conn) throws SQLException {
		if (this.conn != null) // 如果是单链接模式则不关闭链接
			return;

		conn.close();
	}

	// /////////////////////////
	public final Connection conn_r() throws SQLException {
		if (this.conn != null)
			return this.conn;

		return ds_r.getConnection();
	}

	public final Connection conn_w() throws SQLException {
		if (this.conn != null)
			return this.conn;

		return ds_w.getConnection();
	}

	// /////////////////////////

	public void execute(final String sql) throws SQLException {
		Connection conn = conn_w();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public CachedRowSet query(final String sql) throws SQLException {
		Connection conn = conn_r();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			CachedRowSet crs = new CachedRowSetImpl();
			crs.populate(rs);
			rs.close();
			stmt.close();
			return crs;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	// private <T> T query(String sql, Class c) throws Exception {
	// ResultSetHandler rsh = getRsh(c);
	// return query(sql, rsh);
	// }

	public <T> T query(final String sql, final ResultSetHandler rsh)
			throws SQLException {
		Connection conn = conn_r();
		try {
			T r2 = null;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = (T) rsh.handle(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public final <T> T queryForObject(final String sql,
			final ResultSetHandler rsh) throws SQLException {
		return query(sql, rsh);
	}

	public Map queryForMap(final String sql) throws SQLException {
		Connection conn = conn_r();
		try {
			Map r2 = null;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = toMap(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public List<Map> queryForList(final String sql) throws SQLException {
		Connection conn = conn_r();
		try {
			List<Map> r2 = null;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			r2 = toMaps(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public <T> List<T> queryForKeys(final String sql) throws SQLException {
		Connection conn = conn_r();
		try {
			List<T> r2 = null;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			r2 = toKeys(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public <T> List<T> queryForList(final String sql, final ResultSetHandler rsh)
			throws SQLException {
		Connection conn = conn_r();
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
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
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public long queryForLong(final String sql) throws SQLException {
		Connection conn = conn_r();
		try {
			long r2 = 0;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = rs.getLong(1);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public int queryForInt(final String sql) throws SQLException {
		Connection conn = conn_r();
		try {
			int r2 = 0;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = rs.getInt(1);

			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}
	
	public double queryForDouble(final String sql) throws SQLException {
		Connection conn = conn_r();
		try {
			double r2 = 0;
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				r2 = rs.getDouble(1);

			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public final RowSet queryForRowSet(final String sql) throws SQLException {
		return query(sql);
	}

	public int update(final String sql) throws SQLException {
		Connection conn = conn_w();
		try {
			int r2 = 0;
			PreparedStatement stmt = conn.prepareStatement(sql);
			r2 = stmt.executeUpdate();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public int[] batch4Sqls(final String sqls[]) throws SQLException {
		Connection conn = conn_w();
		try {
			int r2[] = null;
			Statement stmt = conn.createStatement();
			for (String sql : sqls) {
				stmt.addBatch(sql);
			}
			r2 = stmt.executeBatch();

			stmt.close();
			return r2;
		} finally {
			close(conn);
		}
	}

	/***
	 * CallableStatement接口扩展 PreparedStatement，用来调用存储过程 <br/>
	 * 调用已储存过程的语法:{call 过程名[(?, ?, ...)]} <br/>
	 * 不带参数的已储存过程的语法:{call 过程名} 如sql = "call pro_rnkScore();"<br/>
	 * 带参数的已储存过程的语法:{call 过程名[(?, ?, ...)]} <br/>
	 * 例如ps_isa_ywxx_insert有4个参数，第四个是OUT出来INT的参数 <br/>
	 * ps = conn.prepareCall(" call ps_isa_ywxx_insert(?,?,?,?) ; "); <br/>
	 * ps.setString(1, ywbh); <br/>
	 * ps.setString(2, ywmc); <br/>
	 * ps.setString(3, ywbz); <br/>
	 * ps.registerOutParameter(4, Types.INTEGER) ; <br/>
	 * ps.execute() ; <br/>
	 * insertResult = ps.getInt(4); <br/>
	 * 返回结果参数的过程的语法:{? = call 过程名[(?, ?, ...)]}
	 * **/
	public void call(final String sql, final Object... params)
			throws SQLException {
		Connection conn = conn_w();
		try {
			CallableStatement stmt = conn.prepareCall(sql);
			if (params != null && params.length > 0) {
				int lens = params.length;
				for (int i = 0; i < lens; i++) {
					int index = i + 1;
					stmt.setObject(index, params[i]);
				}
			}
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	public List<Map> queryByCall(final String sql) throws SQLException {
		Connection conn = conn_w();
		try {
			List<Map> r2 = null;
			CallableStatement stmt = conn.prepareCall(sql);
			ResultSet rs = stmt.executeQuery();
			r2 = toMaps(rs);
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw rethrow(e, sql);
		} finally {
			close(conn);
		}
	}

	private static final PreparedStatement prepareMap(
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

	public static final String e2s(final Throwable e) {
		return e2s(e, null, new Object[0]);
	}

	public static final String e2s(final Throwable e, final Object obj) {
		return e2s(e, String.valueOf(obj), new Object[0]);
	}

	public static String e2s(final Throwable e, final String fmt,
			final Object... args) {
		return ExceptionEx.e2s(e, fmt, args);
	}

	// ///////////////////////////////////////////////////
	public static final List<Map> toMaps(final ResultSet rs)
			throws SQLException {
		List<Map> result = new Vector();
		while (rs.next()) {
			Map m = toMap(rs);
			result.add(m);
		}
		return result;
	}

	public static final <T> List<T> toKeys(final ResultSet rs)
			throws SQLException {
		List<T> result = new Vector();
		while (rs.next()) {
			Object o = rs.getObject(1);
			result.add((T) o);
		}
		return result;
	}

	public static final Map toMap(final ResultSet rs) throws SQLException {
		Map result = newMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		for (int i = 1; i <= cols; i++)
			result.put(rsmd.getColumnName(i), rs.getObject(i));

		return result;
	}

	public static final int pageCount(final int count, final int pageSize) {
		return ListEx.pageCount(count, pageSize);
	}

	public static final List getPage(final List v, final int page,
			final int pageSize) {
		return ListEx.getPage(v, page, pageSize);
	}

	static public final int getInt(Map map, Object key) {
		return MapEx.getInt(map, key);
	}

	static public final long getLong(Map map, Object key) {
		return MapEx.getLong(map, key);
	}

	static public final <T> List<T> newList() {
		return ListEx.newListT();
	}

	static public final Map newMap() {
		return MapEx.newMap();
	}

	public void truncate(final String TABLENAME2) throws SQLException {
		String sql = "TRUNCATE TABLE `" + TABLENAME2 + "`";
		this.update(sql);
	}

	public void repair(final String TABLENAME2) throws SQLException {
		String sql = "REPAIR TABLE `" + TABLENAME2 + "`";
		this.update(sql);
	}

	public void optimize(final String TABLENAME2) throws SQLException {
		String sql = "OPTIMIZE TABLE `" + TABLENAME2 + "`";
		this.update(sql);
	}

	public void dropTable(final String TABLENAME2) throws SQLException {
		String sql = "DROP TABLE IF EXISTS `" + TABLENAME2 + "`";
		this.update(sql);
	}

	// //////////////////////////////////////////////////////////
	// static ScheduledExecutorService _executor = null;
	//
	// public static void setExecutor(ScheduledExecutorService ses) {
	// _executor = ses;
	// }
	//
	// protected static ScheduledExecutorService executor() {
	// if (_executor == null)
	// _executor = Executors.newScheduledThreadPool(8,
	// new MyThreadFactory("JdbcTemplate", false));
	// return _executor;
	// }

	// //////////////////////////////////////////////////////////
	ScheduledExecutorService _single_executor = null;

	public void setSingleExecutor(ScheduledExecutorService ses) {
		this._single_executor = ses;
	}

	protected synchronized ScheduledExecutorService executor(final String name) {
		if (_single_executor == null)
			_single_executor = Executors
					.newSingleThreadScheduledExecutor(new MyThreadFactory(name,
							false));
		return _single_executor;
	}

	// //////////////////////////////////////////////////////////
	public boolean exist_r(String TABLENAME2) {
		boolean ret = false;
		Connection conn = null;
		try {
			String catalog = catalog_r();
			NewSet<String> tables = TABLES.get(catalog);
			if (tables != null) {
				if (tables.contains(TABLENAME2))
					return true;
			} else {
				tables = new NewSet<String>();
				TABLES.put(catalog, tables);
			}

			conn = conn_r();
			List<Map> maps = SqlEx.getTables(conn);
			for (Map map : maps) {
				String str = MapEx.getString(map, "TABLE_NAME");
				if (tables.contains(str))
					continue;
				tables.Add(str);
				ret = ret || (str.equals(TABLENAME2));
			}
		} catch (Exception e) {
			ret = false;
		} finally {
			closeNoExcept(conn);
		}
		return ret;
	}

	public boolean exist_w(String TABLENAME2) {
		boolean ret = false;
		Connection conn = null;
		try {
			String catalog = catalog_w();
			NewSet<String> tables = TABLES.get(catalog);
			if (tables != null) {
				if (tables.contains(TABLENAME2))
					return true;
			} else {
				tables = new NewSet<String>();
				TABLES.put(catalog, tables);
			}

			conn = conn_w();
			List<Map> maps = SqlEx.getTables(conn);
			for (Map map : maps) {
				String str = MapEx.getString(map, "TABLE_NAME");
				if (tables.contains(str))
					continue;
				tables.Add(str);
				ret = ret || (str.equals(TABLENAME2));
			}
		} catch (Exception e) {
			ret = false;
		} finally {
			closeNoExcept(conn);
		}

		return ret;
	}

	protected SQLException rethrow(SQLException cause, String sql)
			throws SQLException {

		String causeMessage = cause.getMessage();
		if (causeMessage == null) {
			causeMessage = "";
		}
		StringBuffer msg = new StringBuffer(causeMessage);
		msg.append("\r\n");
		msg.append(" Query: ");
		msg.append("\r\n");
		msg.append(sql);
		msg.append("\r\n");

		SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
				cause.getErrorCode());
		e.setNextException(cause);

		return e;
	}

	public static SQLException rethrow(SQLException cause, String sql,
			Object... params) {

		String causeMessage = cause.getMessage();
		if (causeMessage == null) {
			causeMessage = "";
		}
		StringBuffer msg = new StringBuffer(causeMessage);
		msg.append("\r\n");
		msg.append(" Query: ");
		msg.append("\r\n");
		msg.append(sql);
		msg.append("\r\n");
		msg.append(" Parameters: ");
		msg.append("\r\n");
		if (params == null) {
			msg.append("[]");
		} else {
			msg.append(Arrays.deepToString(params));
		}
		msg.append("\r\n");
		SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
				cause.getErrorCode());
		e.setNextException(cause);

		return e;
	}

	protected SQLException rethrow(SQLException cause, String sql, Map params)
			throws SQLException {

		String causeMessage = cause.getMessage();
		if (causeMessage == null) {
			causeMessage = "";
		}
		StringBuffer msg = new StringBuffer(causeMessage);
		msg.append("\r\n");
		msg.append(" Query: ");
		msg.append("\r\n");
		msg.append(sql);
		msg.append("\r\n");
		msg.append(" Parameters: ");
		msg.append("\r\n");
		if (params == null) {
			msg.append("{}");
		} else {
			msg.append(FastJSON.prettyFormat(params));
			// msg.append(JSON.toJSONStringNotExcept(params));
		}
		msg.append("\r\n");
		SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
				cause.getErrorCode());
		e.setNextException(cause);

		return e;
	}

	protected SQLException rethrow(SQLException cause, String sql, List params)
			throws SQLException {

		String causeMessage = cause.getMessage();
		if (causeMessage == null) {
			causeMessage = "";
		}
		StringBuffer msg = new StringBuffer(causeMessage);

		msg.append("\r\n");
		msg.append(" Query: ");
		msg.append("\r\n");
		msg.append(sql);
		msg.append("\r\n");
		msg.append(" Parameters: ");
		msg.append("\r\n");
		if (params == null) {
			msg.append("[]");
		} else {
			msg.append(FastJSON.prettyFormat(params));
			// msg.append(JSON.toJSONStringNotExcept(params));
		}
		msg.append("\r\n");
		SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
				cause.getErrorCode());
		e.setNextException(cause);

		return e;
	}

	public static void main(String[] args) throws Exception {
		// DataSource ds = Dbcp.newMysql("fych").dataSource();
		// JdbcTemplate jt = new JdbcTemplate(ds);
		// String TABLENAME2 = "Copyright";
		// String sql =
		// "INSERT INTO Copyright (name, version) VALUES (:name, :version)";
		// Copyright c = Copyright.newCopyright(0L, "name -- 0", "version");
		// ResultSet rs = jt.insert(sql, c);
		// System.out.println(SqlEx.toMaps(rs));
		// String sql = "SELECT id, name, version FROM " + TABLENAME2
		// + " WHERE id = :id";
		//
		// Copyright x = Copyright.newCopyright(200L, "name -- 0", "version");
		// Copyright c2 = jt.queryForObject(sql, x,
		// new ResultSetHandler<Copyright>() {
		// public Copyright handle(ResultSet rs) throws SQLException {
		// Map e = SqlEx.toMap(rs);
		// return Copyright.mapTo(e);
		// }
		// });
		// System.out.println(c2);
	}
}
