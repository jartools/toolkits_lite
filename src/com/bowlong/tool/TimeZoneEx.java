package com.bowlong.tool;

import java.util.Calendar;
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
		if (StrEx.isEmptyTrim(Id)) {
			// 8区(中国)
			Id = "GMT+08";
		}
		return System.setProperty("user.timezone", Id);
	}

	static public final TimeZone getTimeZone(String tzid) {
		return TimeZone.getTimeZone(tzid);
	}

	static public final TimeZone getJVMTimeZone() {
		return getTimeZone(getJVMTimeZoneID());
	}

	static public final TimeZone getDefaultTimeZone() {
		return TimeZone.getDefault();
	}

	/** 设置默认时区 */
	static public final void setDefaultTimeZone(String tzid) {
		TimeZone.setDefault(getTimeZone(tzid));
	}

	/** 设置默认时区 - G8区 */
	static public final void setDTZGMT8() {
		setDefaultTimeZone("ETC/GMT-8");
	}

	/** 设置默认时区 - G8区 上海 */
	static public final void setDTZGMT8Shai() {
		setDefaultTimeZone("Asia/Shanghai");
	}

	/** 服务器时区值-小时 */
	static public final int getTZHour() {
		Calendar cal = Calendar.getInstance();
		int offset = cal.get(Calendar.ZONE_OFFSET);
		cal.add(Calendar.MILLISECOND, -offset);
		Long timeStampUTC = cal.getTimeInMillis();
		Long timeStamp = System.currentTimeMillis();
		Long timeZone = (timeStamp - timeStampUTC) / (1000 * 3600);
		return timeZone.intValue();
	}
}
