package com.bowlong.sql;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLXML;

import com.bowlong.basic.ExToolkit;

/**
 * Java Type 类型 - 重新整理
 * 
 * @author Canyon / 龚阳辉
 * @version createtime：2020年3月13日下午3:6:36
 */
public class JType extends ExToolkit {

	static final public String getBasicType(String type) {
		if (type == null)
			return null;

		if (type.endsWith("Boolean"))
			type = "boolean";
		if (type.endsWith("Byte"))
			type = "byte";
		else if (type.endsWith("Short"))
			type = "short";
		else if (type.endsWith("Integer"))
			type = "int";
		else if (type.endsWith("Long"))
			type = "long";
		else if (type.endsWith("Float"))
			type = "float";
		else if (type.endsWith("Double"))
			type = "double";

		return type;
	}

	static final public boolean isNumber(String tp) {
		boolean _isRet = false;
		if (tp == null)
			return _isRet;
		tp = tp.toLowerCase();
		_isRet = tp.endsWith("byte") || tp.endsWith("short") || tp.endsWith("integer") || "int".equals(tp);
		_isRet = _isRet || tp.endsWith("long") || tp.endsWith("float") || tp.endsWith("double");
		return _isRet;
	}

	static final public boolean isNum0Bl(String type) {
		boolean isNumber = isNumber(type);
		if (!isNumber) {
			if ("Boolean".equalsIgnoreCase(type)) {
				return true;
			}
		}
		return isNumber;
	}

	static final public String toJavaType(int columnType) {
		switch (columnType) {
		case java.sql.Types.BIT: // boolean
		case java.sql.Types.BOOLEAN:
			return Boolean.class.getSimpleName();
		case java.sql.Types.TINYINT:
			return Byte.class.getSimpleName();
		case java.sql.Types.SMALLINT:
			return Short.class.getSimpleName();
		case java.sql.Types.INTEGER:
			return Integer.class.getSimpleName();
		case java.sql.Types.BIGINT:
			return Long.class.getSimpleName();
		case java.sql.Types.REAL:
			return Float.class.getSimpleName();
		case java.sql.Types.FLOAT:
		case java.sql.Types.DOUBLE:
			return Double.class.getSimpleName();
		case java.sql.Types.DECIMAL:
		case java.sql.Types.NUMERIC:
			return BigDecimal.class.getName();
		case java.sql.Types.CHAR:
		case java.sql.Types.VARCHAR:
		case java.sql.Types.NVARCHAR:
		case java.sql.Types.LONGVARCHAR:
		case java.sql.Types.LONGNVARCHAR:
			return String.class.getSimpleName();
		case java.sql.Types.DATE:
		case java.sql.Types.TIMESTAMP:
			return java.util.Date.class.getName();
		case java.sql.Types.TIME:
			return java.sql.Time.class.getName();
		case java.sql.Types.BINARY:
		case java.sql.Types.VARBINARY:
		case java.sql.Types.LONGVARBINARY:
			return "byte[]";
		case java.sql.Types.BLOB:
			return java.sql.Blob.class.getName();
		case java.sql.Types.CLOB:
			return java.sql.Clob.class.getName();
		case java.sql.Types.ARRAY:
			return java.sql.Array.class.getName();
		case java.sql.Types.REF:
			return java.sql.Ref.class.getName();
		case java.sql.Types.STRUCT:
			return java.sql.Struct.class.getName();
		default:
			break;
		}
		return "";
	}

	static final public String getType(int columnType) {
		switch (columnType) {
		case java.sql.Types.ARRAY:
			return Array.class.getSimpleName();
		case java.sql.Types.FLOAT:
			return Float.class.getSimpleName();
		case java.sql.Types.JAVA_OBJECT:
			return Object.class.getSimpleName();
		case java.sql.Types.NCHAR:
			return String.class.getName();
		case java.sql.Types.NCLOB:
			return NClob.class.getName();
		case java.sql.Types.OTHER:
			return Object.class.getSimpleName();
		case java.sql.Types.REAL:
			return Double.class.getSimpleName();
		case java.sql.Types.ROWID:
			return RowId.class.getName();
		case java.sql.Types.SQLXML:
			return SQLXML.class.getName();
		case java.sql.Types.REF:
		case java.sql.Types.NULL:
		case java.sql.Types.STRUCT:
		case java.sql.Types.DISTINCT:
			break;
		default:
			return toJavaType(columnType);
		}
		return "";
	}

	static final public String getType4Oracle(int columnType) {
		switch (columnType) {
		// case java.sql.Types.DECIMAL:
		// return Integer.class.getName();
		// case java.sql.Types.TIMESTAMP:
		// return java.sql.Timestamp.class.getName();
		// return oracle.sql.TIMESTAMP.class.getName();
		default:
			return getType(columnType);
		}
	}

}
