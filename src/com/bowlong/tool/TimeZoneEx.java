package com.bowlong.tool;

import java.util.TimeZone;

import com.bowlong.lang.StrEx;

/**
 * 时区 - 工具
 * 
 * @author Canyon
 * @version createtime：2016年9月11日 下午10:26:37
 */
public class TimeZoneEx {
	// JAVA虚拟机（JVM）的默认TimeZone
	static public final String getJVMTimeZoneID() {
		return System.getProperty("user.timezone");
	}
	
	static public final String setJVMTimeZoneID(String Id) {
		if(StrEx.isEmptyTrim(Id)){
			// 8区(中国)
			Id = "GMT+08";
		}
		return System.setProperty("user.timezone",Id);
	}
	
	static public final TimeZone getJVMTimeZone() {
		return TimeZone.getTimeZone(getJVMTimeZoneID());
	}
	
	static public final TimeZone getDefaultTimeZone() {
		return TimeZone.getDefault();
	}
}
