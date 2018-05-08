package com.bowlong.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.tool.TkitBase;

/*** 时间格式转换 **/
public class DateFmtEx {
	// 字母 日期或时间元素 表示 示例
	// ------------------------------------------------------------------
	// G Era 标志符 Text AD
	// y 年 Year 1996; 96
	// M 年中的月份 Month July; Jul; 07
	// w 年中的周数 Number 27
	// W 月份中的周数 Number 2
	// D 年中的天数 Number 189
	// d 月份中的天数 Number 10
	// F 月份中的星期 Number 2
	// E 星期中的天数 Text Tuesday; Tue
	// a Am/pm 标记 Text PM
	// H 一天中的小时数（0-23） Number 0
	// k 一天中的小时数（1-24） Number 24
	// K am/pm 中的小时数（0-11） Number 0
	// h am/pm 中的小时数（1-12） Number 12
	// m 小时中的分钟数 Number 30
	// s 分钟中的秒数 Number 55
	// S 毫秒数 Number 978
	// z 时区 General time zone Pacific Standard Time;
	// PST; GMT-08:00
	// Z 时区 RFC 822 time zone -0800

	static public final String fmt_yyyy_MM_dd_HH_mm_ss_sss = "yyyy-MM-dd HH:mm:ss.SSS";
	static public final String fmt_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	static public final String fmt_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	static public final String fmt_yyyyMMdd_HHmmss = "yyyyMMdd_HHmmss";
	static public final String fmt_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	static public final String fmt_yyyyMMddHHmm = "yyyyMMddHHmm";
	static public final String fmt_yyyyMMdd_HHmm = "yyyyMMdd_HHmm";
	static public final String fmt_yyyy_MM_dd = "yyyy-MM-dd";
	static public final String fmt_yyyyMMdd = "yyyyMMdd";
	static public final String fmt_yyyy_MM = "yyyy-MM";
	static public final String fmt_yyyyMM = "yyyyMM";
	static public final String fmt_yyyy = "yyyy";
	static public final String fmt_MM_dd_HH_mm_ss = "MM-dd HH:mm:ss";
	static public final String fmt_MM_dd_HH_mm = "MM-dd HH:mm";
	static public final String fmt_MM_dd = "MM-dd";
	static public final String fmt_MM = "MM";
	static public final String fmt_dd = "dd";
	static public final String fmt_HH_mm_ss = "HH:mm:ss";
	static public final String fmt_HH_mm = "HH:mm";
	static public final String fmt_HH = "HH";
	static public final String fmt_mm = "mm";
	static public final String fmt_ss = "ss";
	static public final String fmt_SSS = "SSS";

	/*** 年中的星期(此日期是此年份第几个星期) **/
	static public final String fmt_wInYear = "w";
	/*** 月中的星期(此日期是此月第几个星期) **/
	static public final String fmt_WInMonth = "W";
	/*** 年中的天数(此日期是此年份第几天) **/
	static public final String fmt_DInYear = "D";
	/*** 月中的天数(此日期是此月份第几天) **/
	static public final String fmt_dInMonth = "d";
	/*** 月份中的星期(没搞懂) **/
	static public final String fmt_FInMonth = "F";

	static public final long TIME_MILLISECOND = 1;

	static public final long TIME_SECOND = 1000 * TIME_MILLISECOND;

	static public final long TIME_MINUTE = 60 * TIME_SECOND;

	static public final long TIME_HOUR = 60 * TIME_MINUTE;

	static public final long TIME_DAY = 24 * TIME_HOUR;

	static public final long TIME_WEEK = 7 * TIME_DAY;

	static public final long TIME_YEAR = 365 * TIME_DAY;

	/*** 1900年的时间 **/
	static public final long TIME_1900 = TIME_YEAR * 1900;

	static public final long now() {
		return System.currentTimeMillis();
	}

	static public final Date nowDate() {
		return new Date();
	}

	static public final Calendar nowCalendar() {
		return Calendar.getInstance();// 默认时区(TimeZone)，默认语音环境(Locale)
	}

	static public final String formatStr(Date v, String fmt) {
		return new SimpleDateFormat(fmt).format(v);
	}

	static public final String formatStr(Calendar v, String fmt) {
		return formatStr(v.getTime(), fmt);
	}

	static public final String formatStr(long ms, String fmt) {
		return formatStr(new Date(ms), fmt);
	}

	static public final String format(Date v, String fmt) {
		return formatStr(v, fmt);
	}

	static public final String format(Calendar v, String fmt) {
		return formatStr(v, fmt);
	}

	static public final String format(long ms, String fmt) {
		return formatStr(ms, fmt);
	}

	static public final Date parse2Date(Calendar cal) {
		return cal.getTime();
	}

	static public final Date parse2Date(long ms) {
		return new Date(ms);
	}

	static public final Date parse2Date(String v, String fmt) {
		Date dat = null;
		try {
			dat = new SimpleDateFormat(fmt).parse(v);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dat;
	}

	static public final Calendar parse2Cal(Date v) {
		Calendar c = Calendar.getInstance();
		c.setTime(v);
		return c;
	}

	static public final Calendar parse2Cal(long ms) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ms);
		return c;
	}

	static public final Calendar parse2Cal(String v, String fmt) {
		try {
			return parse2Cal(parse2Date(v, fmt));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*** 当前系统时间字符串(yyyy-MM-dd HH:mm:ss) **/
	static public final String nowStr_YMDHms() {
		return nowStr(fmt_yyyy_MM_dd_HH_mm_ss);
	}

	/*** 当前系统时间字符串(yyyy-MM-dd) **/
	static public final String nowStr_YMD() {
		return nowStr(fmt_yyyy_MM_dd);
	}

	/*** 当前系统时间字符串(HH:mm:ss) **/
	static public final String nowStr_Hms() {
		return nowStr(fmt_HH_mm_ss);
	}

	/*** 当前系统时间字符串(yyyyMMddHHmmss) **/
	static public final String nowStrYMDHms() {
		return nowStr(fmt_yyyyMMddHHmmss);
	}
	
	/*** 当前系统时间字符串(yyyyMMdd_HHmmss) **/
	static public final String nowStrYMD_Hms() {
		return nowStr(fmt_yyyyMMdd_HHmmss);
	}
	
	/*** 当前系统时间字符串(yyyyMMdd_HHmm) **/
	static public final String nowStrYMD_Hm() {
		return nowStr(fmt_yyyyMMdd_HHmm);
	}

	/*** 当前系统时间字符串(yyyyMMdd) **/
	static public final String nowStrYMD() {
		return nowStr(fmt_yyyyMMdd);
	}

	/*** 当前系统时间字符串(yyyyMM) **/
	static public final String nowStrYM() {
		return nowStr(fmt_yyyyMM);
	}

	static public final String nowStr(String fmt) {
		return format(nowDate(), fmt);
	}

	/*** 转换为字符串格式(yyyy-MM-dd HH:mm:ss) **/
	static public final String format_YMDHms(long ms) {
		return format_YMDHms(parse2Date(ms));
	}

	/*** 转换为字符串格式(yyyy-MM-dd HH:mm:ss) **/
	static public final String format_YMDHms(Calendar v) {
		return format_YMDHms(parse2Date(v));
	}

	/*** 转换为字符串格式(yyyy-MM-dd HH:mm:ss) **/
	static public final String format_YMDHms(Date v) {
		return formatStr(v, fmt_yyyy_MM_dd_HH_mm_ss);
	}

	/*** [0]=天，[1]=时，[2]=分，[3]=秒，[4]=毫秒 **/
	static public final int[] toDiffArray(final long diff) {
		long tmpMs = diff;

		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;
		int day = 0, hour = 0, minute = 0, second = 0, milliSecond = 0;

		if (tmpMs > dd) {
			day = (int) (tmpMs / dd);
			tmpMs %= dd;
		}

		if (tmpMs > hh) {
			hour = (int) (tmpMs / hh);
			tmpMs %= hh;
		}

		if (tmpMs > mi) {
			minute = (int) (tmpMs / mi);
			tmpMs %= mi;
		}

		if (tmpMs > ss) {
			second = (int) (tmpMs / ss);
			tmpMs %= ss;
		}

		milliSecond = (int) tmpMs;

		return new int[] { day, hour, minute, second, milliSecond };
	}

	// 时间格式化为:HH:mm:ss;
	static public final String toStrEn(long ms) {
		String strHour = "";
		String strMinute = "";
		String strSecond = "";
		StringBuffer buff = StringBufPool.borrowObject();
		String result = "";
		try {
			int[] arr = toDiffArray(ms);
			int hour = arr[0] * 24 + arr[1];
			if (hour > 0) {
				strHour = hour < 10 ? "0" + hour : "" + hour;
				buff.append(strHour).append(":");
			}
			int minute = arr[2];
			if (minute > 0) {
				strMinute = minute < 10 ? "0" + minute : "" + minute;
				buff.append(strMinute).append(":");
			}
			int second = arr[3];
			if (second >= 0) {
				strSecond = second < 10 ? "0" + second : "" + second;
				buff.append(strSecond);
			}
			result = buff.toString();
		} catch (Exception e) {

		} finally {
			StringBufPool.returnObject(buff);
		}
		return result;
	}

	// 时间格式化为:HH时mm分ss秒;
	static public final String toStrCn(long ms) {
		String strHour = "";
		String strMinute = "";
		String strSecond = "";
		StringBuffer buff = StringBufPool.borrowObject();
		String result = "";
		try {
			int[] arr = toDiffArray(ms);
			int hour = arr[0] * 24 + arr[1];
			if (hour > 0) {
				strHour = hour < 10 ? "0" + hour : "" + hour;
				buff.append(strHour).append("时");
			}
			int minute = arr[2];
			if (minute > 0) {
				strMinute = minute < 10 ? "0" + minute : "" + minute;
				buff.append(strMinute).append("分");
			}
			int second = arr[3];
			if (second >= 0) {
				strSecond = second < 10 ? "0" + second : "" + second;
				buff.append(strSecond).append("秒");
			}
			result = buff.toString();
		} catch (Exception e) {
		} finally {
			StringBufPool.returnObject(buff);
		}
		return result;
	}

	// 两个时间的时间差
	static public final long sub(Date d1, Date d2) {
		long l1 = d1.getTime();
		long l2 = d2.getTime();
		return l1 - l2;
	}

	static public final boolean isAfter(Date d1) {
		if (d1 == null)
			return false;
		return isAfter(d1, nowDate());
	}

	static public final boolean isAfter(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return d1.after(d2);
	}

	static public final boolean isAfter(Date d1, String d2, String pattern) {
		if (d1 == null || StrEx.isEmpty(d2))
			return false;
		Date dd2 = parse2Date(d2, pattern);
		return isAfter(d1, dd2);
	}

	static public final boolean isBefore(Date d1) {
		if (d1 == null)
			return false;
		return isBefore(d1, nowDate());
	}

	static public final boolean isBefore(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return d1.before(d2);
	}

	static public final boolean isBefore(Date d1, String d2, String pattern) {
		if (d1 == null || StrEx.isEmpty(d2))
			return false;
		Date dd2 = parse2Date(d2, pattern);
		return isBefore(d1, dd2);
	}

	/*** d1在d2时间之后,或相等 **/
	static public final boolean isNotBefore(Date d1, String d2, String pattern) {
		if (d1 == null || StrEx.isEmpty(d2))
			return false;
		Date dd2 = parse2Date(d2, pattern);
		return TkitBase.compareTo(d1, dd2) >= 0;
	}

	/*** d1在d2时间之后,或相等 **/
	static public final boolean isNotBefore(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return TkitBase.compareTo(d1, d2) >= 0;
	}

	/*** d1在d2时间之前,或相等 **/
	static public final boolean isNotAfter(Date d1, String d2, String pattern) {
		if (d1 == null || StrEx.isEmpty(d2))
			return false;
		Date dd2 = parse2Date(d2, pattern);
		return TkitBase.compareTo(d1, dd2) <= 0;
	}

	/*** d1在d2时间之前,或相等 **/
	static public final boolean isNotAfter(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;
		return TkitBase.compareTo(d1, d2) <= 0;
	}

	/*** [subtractor:减数],[minuend:被减数] **/
	static public final boolean isTimeout(long subtractor, long minuend,
			long timeout) {
		if (timeout <= 0)
			return false;
		long t = subtractor - minuend;
		return (t > timeout);
	}

	static public final boolean isTimeout(long lastTm, long timeout) {
		if (timeout <= 0)
			return false;
		long l2 = System.currentTimeMillis();
		return isTimeout(l2, lastTm, timeout);
	}

	static public final boolean isTimeout(Date lastDat, long timeout) {
		if (timeout <= 0)
			return false;
		long LASTTIME = lastDat.getTime();
		return isTimeout(LASTTIME, timeout);
	}

	/*** 比较对象 [parttern为空时，直接比较两个对象] **/
	static public final int compareTo(Date d1, Date d2, String parttern) {
		try {
			if (StrEx.isEmptyTrim(parttern))
				return d1.compareTo(d2);
			String str1 = format(d1, parttern);
			String str2 = format(d2, parttern);
			return str1.compareTo(str2);
		} catch (Exception e) {
		}
		return 0;
	}

	/*** 判断是否一样 parttern 默认格式yyyyMMdd **/
	static public final boolean isSame(Date d1, Date d2, String parttern) {
		if (StrEx.isEmptyTrim(parttern))
			parttern = "yyyyMMdd";
		int v = compareTo(d1, d2, parttern);
		return v == 0;
	}

	/*** 判断字符串是不是时间字符串，格式是 parttern 默认格式yyyyMMdd **/
	static public final boolean isTime(String timeStr, String parttern) {
		if (StrEx.isEmptyTrim(timeStr)) {
			return false;
		}
		if (StrEx.isEmptyTrim(parttern))
			parttern = fmt_yyyyMMdd;
		Date v = parse2Date(timeStr, parttern);
		return (v != null);
	}
}
