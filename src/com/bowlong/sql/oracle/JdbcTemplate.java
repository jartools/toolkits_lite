package com.bowlong.sql.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.bowlong.sql.jdbc.BeanSupport;
import com.bowlong.sql.jdbc.JdbcTempletBase;
import com.bowlong.sql.jdbc.PrepareSQLResult;

@SuppressWarnings("all")
public class JdbcTemplate extends JdbcTempletBase {

	public JdbcTemplate(Connection conn) {
		super(conn);
	}
	
	public JdbcTemplate(final DataSource ds) {
		super(ds);
	}

	public JdbcTemplate(final DataSource ds_r, final DataSource ds_w) {
		super(ds_r, ds_w);
	}

	public Map insert(String sql, String sqlId, Map params) throws SQLException {
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
			if (rs.next()) {
				RowId rowid = rs.getRowId(1);
				PreparedStatement stmt2 = conn.prepareStatement(sqlId);
				stmt2.setRowId(1, rowid);
				ResultSet rs2 = stmt2.executeQuery();
				if (rs2.next())
					r2 = toMap(rs2);
			}
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw e;
		} finally {
			close(conn);
		}
	}

	public Map insert(String sql, String sqlId, BeanSupport x)
			throws SQLException {
		Connection conn = conn_w();
		try {
			Map r2 = null;
			Map params = x.toBasicMap();
			PrepareSQLResult sr = prepareKeys(sql);
			PreparedStatement stmt = conn.prepareStatement(sr.sql,
					PreparedStatement.RETURN_GENERATED_KEYS);
			prepareMap(stmt, sr.keys, params);
			int r = stmt.executeUpdate();
			if (r < 0)
				throw new SQLException(" r = 0");

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				RowId rowid = rs.getRowId(1);
				PreparedStatement stmt2 = conn.prepareStatement(sqlId);
				stmt2.setRowId(1, rowid);
				ResultSet rs2 = stmt2.executeQuery();
				if (rs2.next())
					r2 = toMap(rs2);
			}
			rs.close();
			stmt.close();
			return r2;
		} catch (SQLException e) {
			throw e;
		} finally {
			close(conn);
		}
	}
	
	public static void main(String[] args) throws Exception {
	}
}
