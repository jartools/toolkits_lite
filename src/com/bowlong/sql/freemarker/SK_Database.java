package com.bowlong.sql.freemarker;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import opensource.jpinyin.PinyinHelper;

import com.bowlong.lang.StrEx;
import com.bowlong.sql.freemarker.decode.SK_SqlTypeDecode;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月24日 下午6:27:45
 * @Description ：数据库对象
 */
@SuppressWarnings("rawtypes")
public class SK_Database {
	// 数据库名称
	private String databaseName;
	// 数据库类型
	private String databaseType;
	// 数据库的表集合
	private List<SK_ITable> tables;
	private Connection conn;
	private DataSource ds;
	// 类型解码器
	private SK_SqlTypeDecode sqlDecode;
	// 数据源的配置
	private Class config;

	public SK_Database(Connection conn, SK_SqlTypeDecode sqlDecode, Class config) {
		this.conn = conn;
		this.databaseName = SK_MetaData.getDatabaseName(this.conn);
		databaseType = SK_MetaData.getDatabaseType(this.conn);
		this.tables = new ArrayList<SK_ITable>();
		this.sqlDecode = sqlDecode;
		this.config = config;
	}

	public SK_Database(DataSource ds, SK_SqlTypeDecode sqlDecode, Class config) {
		this.ds = ds;
		try {
			this.conn = ds.getConnection();
			this.databaseName = SK_MetaData.getDatabaseName(this.conn);
			databaseType = SK_MetaData.getDatabaseType(this.conn);
			this.tables = new ArrayList<SK_ITable>();
			this.sqlDecode = sqlDecode;
			this.config = config;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	public void setTables(List<SK_ITable> tables) {
		this.tables = tables;
	}

	public List<SK_ITable> getTables(boolean isConfig) {
		if (tables.isEmpty()) {
			List<Map<String, Object>> names = SK_MetaData.getTables(this.conn,
					this.databaseName);
			SK_ITable skTable = null;
			for (Map<String, Object> map : names) {
				// 原名称
				String tableName = map.get("TABLE_NAME").toString();
				List<SK_Column> columns = new ArrayList<SK_Column>();
				List<SK_BindKey> bindKeys = new ArrayList<SK_BindKey>();
				List<SK_Index> indexKeys = new ArrayList<SK_Index>();
				// 拼音名称
				String tableName_ = PinyinHelper.getShortPinyin(tableName);
				// 大写名称
				String d_tableName = StrEx.upperN1(tableName_);
				// 小写名称
				String x_tableName = StrEx.lowerFirst(tableName_);

				List<String> all_objAndGetD_columnNames = new ArrayList<String>();
				skTable = new SK_Table(tableName, d_tableName, x_tableName, "",
						"", "", "", "", "", "", "", "", "", "",
						all_objAndGetD_columnNames, columns, bindKeys,
						indexKeys, this, config.getSimpleName(), 0, isConfig);
				tables.add(skTable);
			}
		}
		return tables;
	}

	public SK_SqlTypeDecode getSqlDecode() {
		return sqlDecode;
	}

	public void setSqlDecode(SK_SqlTypeDecode sqlDecode) {
		this.sqlDecode = sqlDecode;
	}

	public Class getConfig() {
		return config;
	}

	public void setConfig(Class config) {
		this.config = config;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

}
