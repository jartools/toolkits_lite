package com.dbmaker.freemarker.sk.decode;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月25日 上午10:16:01
 * @Description ：根据JDBC的java.sql.Types来解析数据类型
 */
public class SK_JdbcType extends SK_SqlTypeDecode {

	@Override
	public String getTypeByDataType(int code) {
		String _ret = getType(code);
		if ("".equals(_ret))
			return "No Class Name";
		return _ret;
	}

	@Override
	public String getTypeByTypeName(String name, int length) {
		return null;
	}
}
