package com.bowlong.util;

import java.util.Calendar;

/*** 日历对象数据操作 **/
public class CalendarEx extends DateFmtEx {

	static public final int year() {
		return year(nowCalendar());
	}

	static public final int year(Calendar v) {
		return v.get(Calendar.YEAR);
	}

	static public final int month() {
		return month(nowCalendar());
	}

	static public final int month(Calendar v) {
		int m = v.get(Calendar.MONTH);
		return m + 1;
	}

	static public final int day() {
		return day(nowCalendar());
	}

	static public final int day(Calendar v) {
		return v.get(Calendar.DATE);// DAY_OF_MONTH 同意义
	}

	static public final int hour() {
		return hour(nowCalendar());
	}

	static public final int hour(Calendar v) {
		return v.get(Calendar.HOUR_OF_DAY);
	}

	static public final int minute() {
		return minute(nowCalendar());
	}

	static public final int minute(Calendar v) {
		return v.get(Calendar.MINUTE);
	}

	static public final int second() {
		return second(nowCalendar());
	}

	static public final int second(Calendar v) {
		return v.get(Calendar.SECOND);
	}

	static public final int ms() {
		return ms(nowCalendar());
	}

	static public final int ms(Calendar v) {
		return v.get(Calendar.MILLISECOND);
	}

	static public final int week() {
		return week(nowCalendar());
	}

	/*** 星期数(0~6) **/
	static public final int week(Calendar v) {
		int w = v.get(Calendar.DAY_OF_WEEK);
		return w - 1;
	}

	static public final boolean isWeek() {
		return isWeek(nowCalendar());
	}

	static public final boolean isWeek(long nowtime) {
		if (nowtime == 0)
			return false;
		return isWeek(parse2Cal(nowtime));
	}

	/*** 是否为周末(星期六，星期天) **/
	static public final boolean isWeek(Calendar cal) {
		if (cal == null)
			return false;
		int week = week(cal);
		boolean r = week == 0 || week == 6;
		return r;
	}
	
	/*** 年中的星期(此日期是此年份第几个星期) **/
	static public final int weekInYear(Calendar v) {
		return v.get(Calendar.WEEK_OF_YEAR);
	}

	static public final int weekInYear() {
		return weekInYear(nowCalendar());
	}

	/*** 月中的星期(此日期是此月第几个星期) **/
	static public final int weekInMonth(Calendar v) {
		return v.get(Calendar.WEEK_OF_MONTH);
	}

	static public final int weekInMonth() {
		return weekInMonth(nowCalendar());
	}
	
	/*** 年中的天数(此日期是此年份第几天) **/
	static public final int dayInYear(Calendar v) {
		return v.get(Calendar.DAY_OF_YEAR);
	}

	static public final int dayInYear() {
		return dayInYear(nowCalendar());
	}
	
	static public final int dayInMonth(Calendar v) {
		return v.get(Calendar.DAY_OF_MONTH);
	}

	static public final int dayInMonth() {
		return dayInMonth(nowCalendar());
	}

	public static final Calendar addYear(Calendar c, int v) {
		c.add(Calendar.YEAR, v);
		return c;
	}

	public static final Calendar addMonth(Calendar c, int v) {
		c.add(Calendar.MONTH, v);
		return c;
	}

	public static final Calendar addWeek(Calendar c, int v) {
		c.add(Calendar.WEEK_OF_MONTH, v);
		return c;
	}

	public static final Calendar addDay(Calendar c, int v) {
		c.add(Calendar.DAY_OF_MONTH, v);
		return c;
	}

	public static final Calendar addHour(Calendar c, int v) {
		c.add(Calendar.HOUR, v);
		return c;
	}

	public static final Calendar addMinute(Calendar c, int v) {
		c.add(Calendar.MINUTE, v);
		return c;
	}

	public static final Calendar addSecond(Calendar c, int v) {
		c.add(Calendar.SECOND, v);
		return c;
	}

	public static final Calendar addMilliSecond(Calendar c, int v) {
		c.add(Calendar.MILLISECOND, v);
		return c;
	}

	/*** 此时间内当月共有多少天 **/
	static public final int dayNum(int year, int month) {
		Calendar test = nowCalendar();
		test.set(Calendar.YEAR, year);
		test.set(Calendar.MONTH, month);
		int totalDay = test.getActualMaximum(Calendar.DAY_OF_MONTH);
		return totalDay;
	}
	
	/*** 此时间内当年共有多少天 **/
	static public final int dayNum(int year) {
		Calendar test = nowCalendar();
		test.set(Calendar.YEAR, year);
		int totalDay = test.getActualMaximum(Calendar.DAY_OF_YEAR);
		return totalDay;
	}
}
