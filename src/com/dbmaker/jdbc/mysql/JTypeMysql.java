package com.dbmaker.jdbc.mysql;

import java.sql.ResultSetMetaData;

import com.bowlong.sql.JType;

public class JTypeMysql extends JType {

	public static String getType(ResultSetMetaData rsmd, String columnName) throws Exception {
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String key = rsmd.getColumnName(i);
			if (!key.equals(columnName))
				continue;

			return getType(rsmd, i);
		}
		return "";
	}

	public static String getType(ResultSetMetaData rsmd, int i) throws Exception {
		int count = rsmd.getColumnCount();
		if (i > count)
			return "";

		return getType(rsmd.getColumnType(i));
	}

	public static String getMapGet(ResultSetMetaData rsmd, String columnName) throws Exception {
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String key = rsmd.getColumnName(i);
			if (!key.equals(columnName))
				continue;

			return getMapGet(rsmd, i);
		}
		return "";
	}

	public static String getMapGet(ResultSetMetaData rsmd, int i) throws Exception {
		int count = rsmd.getColumnCount();
		if (i > count)
			return "";

		int columnType = rsmd.getColumnType(i);
		switch (columnType) {
		case java.sql.Types.ARRAY:
			return "";
		case java.sql.Types.BIGINT:
			return "getLong";
		case java.sql.Types.BINARY:
			return "getByteArray";
		case java.sql.Types.BIT:
			return "getBoolean";
		case java.sql.Types.BLOB:
			return "getByteArray";
		case java.sql.Types.BOOLEAN:
			return "getBoolean";
		case java.sql.Types.CHAR:
			return "getString";
		case java.sql.Types.CLOB:
			return "getString";
		case java.sql.Types.DATE:
			return "getDate";
		case java.sql.Types.DECIMAL:
			return "getBigDecimal";
		case java.sql.Types.DISTINCT:
			break;
		case java.sql.Types.DOUBLE:
			return "getDouble";
		case java.sql.Types.FLOAT:
			return "getFloat";
		case java.sql.Types.INTEGER:
			return "getInt";
		case java.sql.Types.JAVA_OBJECT:
			return "";
		case java.sql.Types.LONGVARCHAR:
			return "getString";
		case java.sql.Types.LONGNVARCHAR:
			return "getString";
		case java.sql.Types.LONGVARBINARY:
			return "getByteArray";
		case java.sql.Types.NCHAR:
			return "getString";
		case java.sql.Types.NCLOB:
			return "getString";
		case java.sql.Types.NULL:
			break;
		case java.sql.Types.NUMERIC:
			return "getBigDecimal";
		case java.sql.Types.NVARCHAR:
			return "getString";
		case java.sql.Types.OTHER:
			return "";
		case java.sql.Types.REAL:
			return "getDouble";
		case java.sql.Types.REF:
			break;
		case java.sql.Types.ROWID:
			return "";
		case java.sql.Types.SMALLINT:
			return "getShort";
		case java.sql.Types.SQLXML:
			return "getString";
		case java.sql.Types.STRUCT:
			break;
		case java.sql.Types.TIME:
			return "getDate";
		case java.sql.Types.TIMESTAMP:
			return "getDate";
		case java.sql.Types.TINYINT:
			return "getByte";
		case java.sql.Types.VARBINARY:
			return "getByteArray";
		case java.sql.Types.VARCHAR:
			return "getString";
		default:
			break;
		}
		return "";
	}
}
