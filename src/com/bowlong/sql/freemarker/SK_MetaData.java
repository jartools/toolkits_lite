package com.bowlong.sql.freemarker;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月24日 下午2:15:39
 * @Description ：取得数据库元关系类
 */
public class SK_MetaData {
	/**
	 * 取得数据库名称
	 * 
	 * @param conn
	 * @return
	 */
	public static String getDatabaseName(Connection conn) {
		String databaseName = null;
		try {
			databaseName = conn.getCatalog();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return databaseName;
	}

	/**
	 * 取得数据库的类型
	 * 
	 * @param conn
	 * @return
	 */
	public static String getDatabaseType(Connection conn) {
		String databaseType = "";
		try {
			DatabaseMetaData databaseName = conn.getMetaData();
			databaseType = databaseName.getDatabaseProductName();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return databaseType;
	}

	/**
	 * 取得表
	 * 
	 * @param conn
	 * @return
	 */
	public static List<Map<String, Object>> getTables(Connection conn,
			String tableName) {
		List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>();
		try {
			DatabaseMetaData md = conn.getMetaData();
			ResultSet rs = md.getTables(tableName, "", "",
					new String[] { "TABLE" });
			while (rs.next()) {
				tables.add(rsToMap(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tables;
	}

	/**
	 * 取字段
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static List<Map<String, Object>> getColumns(Connection conn,
			String tableName) {
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
		try {
			DatabaseMetaData md = conn.getMetaData();
			String databaseName = getDatabaseName(conn);
			ResultSet rs = md.getColumns(databaseName, "", tableName, "");
			while (rs.next()) {
				columns.add(rsToMap(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columns;
	}

	/**
	 * 取得主外键关系
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static List<Map<String, Object>> getExportedKeys(Connection conn,
			String tableName) {
		List<Map<String, Object>> exportedKeys = new ArrayList<Map<String, Object>>();
		try {
			DatabaseMetaData md = conn.getMetaData();
			String databaseName = getDatabaseName(conn);
			ResultSet rs = md.getExportedKeys(databaseName, "", tableName);
			while (rs.next()) {
				exportedKeys.add(rsToMap(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exportedKeys;
	}

	/**
	 * 取得主键关系
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static List<Map<String, Object>> getImportedKeys(Connection conn,
			String tableName) {
		List<Map<String, Object>> exportedKeys = new ArrayList<Map<String, Object>>();
		try {
			DatabaseMetaData md = conn.getMetaData();
			String databaseName = getDatabaseName(conn);
			ResultSet rs = md.getImportedKeys(databaseName, "", tableName);
			while (rs.next()) {
				exportedKeys.add(rsToMap(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exportedKeys;
	}

	/**
	 * 取得索引
	 * 
	 * @param conn
	 * @param tableName
	 * @param unique
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getIndexs(Connection conn,
			String tableName) {
		List<Map<String, Object>> indexs = new ArrayList<Map<String, Object>>();
		try {
			DatabaseMetaData dmd = conn.getMetaData();
			String databaseName = getDatabaseName(conn);
			ResultSet rs = dmd.getIndexInfo(databaseName, "", tableName, false,
					true);
			while (rs.next()) {
				indexs.add(rsToMap(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexs;
	}

	private static Map<String, Object> rsToMap(ResultSet rs) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ResultSetMetaData rsMd = rs.getMetaData();
			int count = rsMd.getColumnCount();
			for (int i = 1; i <= count; i++) {
				String keyName = rsMd.getColumnName(i);
				rsMd.getColumnClassName(i);
				Object obj = rs.getObject(i);
				map.put(keyName, obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
}
