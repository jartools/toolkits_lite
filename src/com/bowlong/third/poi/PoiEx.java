package com.bowlong.third.poi;

import java.io.File;
import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * word,excel文件操作工具POI <br/>
 * 2003,2007
 * 
 * @author Canyon
 * @version createtime：2015年8月26日下午1:22:35
 */
public class PoiEx {
	// //////////////////// excel //////////////////////
	static public final String TYPE_UNKNOW = "unknow";
	static public final String TYPE_BOOLEAN = "boolean";
	static public final String TYPE_DOUBLE = "double";
	static public final String TYPE_INT = "int";
	static public final String TYPE_LONG = "long";
	static public final String TYPE_STRING = "string";
	static public final String TYPE_JSON = "json";

	static public int LINE_NAME = 0;// 中文名字
	static public int LINE_TYPE = 1;// 字段类型
	static public int LINE_CNAME = 2;// 字段英文名称
	static public int LINE_MEMO = 3;// 备忘录
	static public int LINE_DATA_MIN = 4;// 数据开始行数
	static public int LINE_DATA_MAX = 50000;// 最大行数
	static public int ROW_MAX = 255;// 最大列数

	// ==== 2003 excel
	static public final POIFSFileSystem openFS(final InputStream inStream)
			throws Exception {
		return new POIFSFileSystem(inStream);
	}

	// ==== 2007 excel
	static public final OPCPackage openPackage(final File file)
			throws Exception {
		return OPCPackage.open(file);
	}

	static public final OPCPackage openPackage(final InputStream inStream)
			throws Exception {
		return OPCPackage.open(inStream);
	}

	// /////////////////////////////////////////////////

	// //////////////////// word //////////////////////
	// /////////////////////////////////////////////////
}
