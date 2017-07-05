package com.bowlong.sql.freemarker.decode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月25日 上午10:21:53
 * @Description ：Mysql数据类型解码器
 */
public class SK_MysqlType extends SK_SqlTypeDecode {

	@Override
	public String getTypeByDataType(int code) {
		return null;
	}

	@Override
	public String getTypeByTypeName(String name, int length) {
		String classType = "";
		switch (name) {
		case "BIT":
			if (length > 1) {
				classType = "byte[]";
			} else {
				classType = Boolean.class.getSimpleName();
			}
			break;
		case "TINYINT":
			if (length > 1) {
				classType = Integer.class.getSimpleName();
			} else {
				classType = Boolean.class.getSimpleName();
			}
			break;
		case "FLOAT":
			classType = Float.class.getSimpleName();
			break;
		case "DOUBLE":
			classType = Double.class.getSimpleName();
			break;
		case "DECIMAL":
			classType = BigDecimal.class.getSimpleName();
			break;
		case "SMALLINT":
		case "MEDIUMINT":
		case "INTEGER":
		case "INT":
			classType = Integer.class.getSimpleName();
			break;
		case "BIGINT":
			classType = Long.class.getSimpleName();
			break;
		case "DATE":
		case "DATETIME":
		case "TIMESTAMP":
		case "TIME":
		case "YEAR":
			classType = Date.class.getSimpleName();
			break;
		case "CHAR":
		case "VARCHAR":
		case "TEXT":
		case "LONGTEXT":
		case "MEDIUMTEXT":
			classType = String.class.getSimpleName();
			break;
		case "BINARY":
		case "VARBINARY":
		case "TINYBLOB":
		case "BLOB":
		case "LONGBLOB":
		case "MEDIUMBLOB":
			classType = "byte[]";
			break;
		default:
			classType = "No Class Name";
			break;
		}
		return classType;
	}
}
