package com.bowlong.sql.builder.jdbc.mysql;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;

public class JavaType {

	public static String getType(ResultSetMetaData rsmd, String columnName) throws SQLException {
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String key = rsmd.getColumnName(i);
			if (!key.equals(columnName))
				continue;

			return getType(rsmd, i);
		}
		return "";
	}

	public static String getType(ResultSetMetaData rsmd, int i) throws SQLException {
		int count = rsmd.getColumnCount();
		if (i > count)
			return "";

		int columnType = rsmd.getColumnType(i);
		switch (columnType) {
		case java.sql.Types.ARRAY:
			return Array.class.getSimpleName();
		case java.sql.Types.BIGINT:
			return Long.class.getSimpleName();
		case java.sql.Types.BINARY:
			return "byte[]";
		case java.sql.Types.BIT:
			return Boolean.class.getSimpleName();
		case java.sql.Types.BLOB:
			return Blob.class.getName();
		case java.sql.Types.BOOLEAN:
			return Boolean.class.getSimpleName();
		case java.sql.Types.CHAR:
			return String.class.getSimpleName();
		case java.sql.Types.CLOB:
			return Clob.class.getName();
		case java.sql.Types.DATE:
			return java.util.Date.class.getName();
		case java.sql.Types.DECIMAL:
			return BigDecimal.class.getName();
		case java.sql.Types.DISTINCT:
			break;
		case java.sql.Types.DOUBLE:
			return Double.class.getSimpleName();
		case java.sql.Types.FLOAT:
			return Float.class.getSimpleName();
		case java.sql.Types.INTEGER:
			return Integer.class.getSimpleName();
		case java.sql.Types.JAVA_OBJECT:
			return Object.class.getSimpleName();
		case java.sql.Types.LONGVARCHAR:
			return String.class.getSimpleName();
		case java.sql.Types.LONGNVARCHAR:
			return String.class.getSimpleName();
		case java.sql.Types.LONGVARBINARY:
			return "byte[]";
		case java.sql.Types.NCHAR:
			return String.class.getName();
		case java.sql.Types.NCLOB:
			return NClob.class.getName();
		case java.sql.Types.NULL:
			break;
		case java.sql.Types.NUMERIC:
			return BigDecimal.class.getName();
		case java.sql.Types.NVARCHAR:
			return String.class.getSimpleName();
		case java.sql.Types.OTHER:
			return Object.class.getSimpleName();
		case java.sql.Types.REAL:
			return Double.class.getSimpleName();
		case java.sql.Types.REF:
			break;
		case java.sql.Types.ROWID:
			return RowId.class.getName();
		case java.sql.Types.SMALLINT:
			return Short.class.getSimpleName();
		case java.sql.Types.SQLXML:
			return SQLXML.class.getName();
		case java.sql.Types.STRUCT:
			break;
		case java.sql.Types.TIME:
			return Time.class.getName();
		case java.sql.Types.TIMESTAMP:
			return java.util.Date.class.getName();
		case java.sql.Types.TINYINT:
			return Byte.class.getSimpleName();
		case java.sql.Types.VARBINARY:
			return "byte[]";
		case java.sql.Types.VARCHAR:
			return String.class.getSimpleName();
		default:
			break;
		}
		return "";
	}

	public static String getMapGet(ResultSetMetaData rsmd, String columnName) throws SQLException {
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String key = rsmd.getColumnName(i);
			if (!key.equals(columnName))
				continue;

			return getMapGet(rsmd, i);
		}
		return "";
	}

	public static String getMapGet(ResultSetMetaData rsmd, int i) throws SQLException {
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

	public static String getBasicType(String type) {
		if ("Boolean".equals(type))
			type = "boolean";
		else if ("Byte".equals(type) || "java.lang.Byte".equals(type))
			type = "byte";
		else if ("Short".equals(type) || "java.lang.Short".equals(type))
			type = "short";
		else if ("Integer".equals(type) || "java.lang.Integer".equals(type))
			type = "int";
		else if ("Long".equals(type) || "java.lang.Long".equals(type))
			type = "long";
		else if ("Float".equals(type) || "java.lang.Float".equals(type))
			type = "float";
		else if ("Double".equals(type) || "java.lang.Double".equals(type))
			type = "double";

		return type;
	}

	static public boolean isNumber(String type) {
		if ("Byte".equalsIgnoreCase(type) || "java.lang.Byte".equals(type))
			return true;
		else if ("Short".equalsIgnoreCase(type) || "java.lang.Short".equals(type))
			return true;
		else if ("Integer".equalsIgnoreCase(type) || "int".equalsIgnoreCase(type) || "java.lang.Integer".equals(type))
			return true;
		else if ("Long".equalsIgnoreCase(type) || "java.lang.Long".equals(type))
			return true;
		else if ("Float".equalsIgnoreCase(type) || "java.lang.Float".equals(type))
			return true;
		else if ("Double".equalsIgnoreCase(type) || "java.lang.Double".equals(type))
			return true;
		return false;
	}

	static public boolean isNum0Bl(String type) {
		boolean isNumber = isNumber(type);
		if (!isNumber) {
			if ("Boolean".equalsIgnoreCase(type)) {
				return true;
			}
		}
		return isNumber;
	}

}
