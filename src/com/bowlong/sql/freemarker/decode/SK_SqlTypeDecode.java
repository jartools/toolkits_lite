package com.bowlong.sql.freemarker.decode;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月25日 上午10:20:12
 * @Description ：SQL类型解码器
 */
public abstract class SK_SqlTypeDecode {
	/**
	 * 根据DataType解码
	 * 
	 * @param code
	 * @return
	 */
	public abstract String getTypeByDataType(int code);

	/**
	 * 根据类型名称解码
	 * 
	 * @param name
	 * @return
	 */
	public abstract String getTypeByTypeName(String name, int length);

	/**
	 * 转基本类型
	 * 
	 * @param type
	 * @return
	 */
	public static String getBasicType(String type) {
		if (type.equals("Boolean"))
			type = "boolean";
		if (type.equals("Byte") || type.equals("java.lang.Byte"))
			type = "byte";
		else if (type.equals("Short") || type.equals("java.lang.Short"))
			type = "short";
		else if (type.equals("Integer") || type.equals("java.lang.Integer"))
			type = "int";
		else if (type.equals("Long") || type.equals("java.lang.Long"))
			type = "long";
		else if (type.equals("Float") || type.equals("java.lang.Float"))
			type = "float";
		else if (type.equals("Double") || type.equals("java.lang.Double"))
			type = "double";

		return type;
	}
}
