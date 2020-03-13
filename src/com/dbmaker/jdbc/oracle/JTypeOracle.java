package com.dbmaker.jdbc.oracle;

import java.sql.ResultSetMetaData;

import com.bowlong.sql.JType;

public class JTypeOracle extends JType {

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

		return getType4Oracle(rsmd.getColumnType(i));
	}
}
