package com.dbmaker.freemarker.sk.decode;

import com.dbmaker.type.JType;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月25日 上午10:20:12
 * @Description ：SQL类型解码器
 */
public abstract class SK_SqlTypeDecode extends JType {
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
}
