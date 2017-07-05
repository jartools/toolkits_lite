package com.bowlong.sql.freemarker.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

/**
 * @UserName : SandKing
 * @DataTime : 2013年12月18日 下午11:07:44
 * @Description ：Please describe this document
 */
public class SK_Query extends QueryRunner {

	public long insert(Connection conn, String sql) throws SQLException {
		return this.insert(conn, false, sql, (Object[]) null);
	}

	public long insert(Connection conn, String sql, Object param)
			throws SQLException {
		return this.insert(conn, false, sql, new Object[] { param });
	}

	public long insert(Connection conn, String sql, Object... params)
			throws SQLException {
		return insert(conn, false, sql, params);
	}

	public long insert(String sql) throws SQLException {
		Connection conn = this.prepareConnection();

		return this.insert(conn, true, sql, (Object[]) null);
	}

	public long insert(String sql, Object param) throws SQLException {
		Connection conn = this.prepareConnection();

		return this.insert(conn, true, sql, new Object[] { param });
	}

	public long insert(String sql, Object... params) throws SQLException {
		Connection conn = this.prepareConnection();

        return this.insert(conn, true, sql, params);
	}

	private long insert(Connection conn, boolean closeConn, String sql,
			Object... params) throws SQLException {
		if (conn == null) {
			throw new SQLException("Null connection");
		}

		if (sql == null) {
			if (closeConn) {
				close(conn);
			}
			throw new SQLException("Null SQL statement");
		}

		PreparedStatement stmt = null;
		ResultSet rs = null;
		long key = -1;
		try {
			stmt = conn.prepareStatement(sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			this.fillStatement(stmt, params);
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			key = rs.next() ? rs.getLong(1) : -1;
		} catch (SQLException e) {
			this.rethrow(e, sql, params);

		} finally {
			close(rs);
			close(stmt);
			if (closeConn) {
				close(conn);
			}
		}
		return key;
	}
}
