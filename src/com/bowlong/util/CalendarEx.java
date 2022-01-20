package com.bowlong.util;

import java.util.Calendar;

import com.bowlong.basic.ExOrigin;

/*** 日历对象数据操作 **/
public class CalendarEx extends ExOrigin {

	static public final int year() {
		Calendar c = nowCalendar();
		return year(c);
	}

	static public final int year(Calendar v) {
		return v.get(Calendar.YEAR);
	}

	static public final int month() {
		Calendar c = nowCalendar();
		return month(c);
	}

	static public final int month(Calendar v) {
		int m = v.get(Calendar.MONTH);
		return m + 1;
	}

	static public final int day() {
		Calendar c = nowCalendar();
		return day(c);
	}

	static public final int day(Calendar v) {
		return v.get(Calendar.DATE);// DAY_OF_MONTH 同意义
	}

	static public final int hour() {
		Calendar c = nowCalendar();
		return hour(c);
	}

	static public final int hour(Calendar v) {
		return v.get(Calendar.HOUR_OF_DAY);
	}

	static public final int minute() {
		Calendar c = nowCalendar();
		return minute(c);
	}

	static public final int minute(Calendar v) {
		return v.get(Calendar.MINUTE);
	}

	static public final int second() {
		Calendar c = nowCalendar();
		return second(c);
	}

	static public final int second(Calendar v) {
		return v.get(Calendar.SECOND);
	}

	static public final int ms() {
		Calendar c = nowCalendar();
		return ms(c);
	}

	static public final int ms(Calendar v) {
		return v.get(Calendar.MILLISECOND);
	}

	/*** 是否为周末(星期六，星期天) **/
	static public final boolean isWeekEnd(Calendar cal) {
		if (cal == null)
			return false;
		int week = week(cal);
		boolean r = week == 0 || week == 6;
		return r;
	}

	static public final boolean isWeekEnd() {
		Calendar c = nowCalendar();
		return isWeekEnd(c);
	}

	static public final boolean isWeekEnd(long nowtime) {
		if (nowtime == 0)
			return false;
		Calendar c = parse2Cal(nowtime);
		return isWeekEnd(c);
	}

	/*** 年中的星期(此日期是此年份第几个星期) **/
	static public final int weekInYear(Calendar v) {
		return v.get(Calendar.WEEK_OF_YEAR);
	}

	static public final int weekInYear() {
		Calendar c = nowCalendar();
		return weekInYear(c);
	}

	/*** 月中的星期(此日期是此月第几个星期) **/
	static public final int weekInMonth(Calendar v) {
		return v.get(Calendar.WEEK_OF_MONTH);
	}

	static public final int weekInMonth() {
		Calendar c = nowCalendar();
		return weekInMonth(c);
	}

	/*** 年中的天数(此日期是此年份第几天) **/
	static public final int dayInYear(Calendar v) {
		return v.get(Calendar.DAY_OF_YEAR);
	}

	static public final int dayInYear() {
		Calendar c = nowCalendar();
		return dayInYear(c);
	}

	static public final int dayInMonth(Calendar v) {
		return v.get(Calendar.DAY_OF_MONTH);
	}

	static public final int dayInMonth() {
		Calendar c = nowCalendar();
		return dayInMonth(c);
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
	static public final int dayNumInMonth(Calendar calendar, int year, int month) {
		if (calendar == null) {
			calendar = nowCalendar();
		}

		if (year > 0) {
			calendar.set(Calendar.YEAR, year);
		}

		if (month >= 0 && month <= 11) {
			calendar.set(Calendar.MONTH, month);
		}
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/*** 此时间内当月共有多少天 **/
	static public final int dayNumInMonth(Calendar calendar) {
		return dayNumInMonth(calendar, 0, -1);
	}

	/*** 此时间内当月共有多少天 **/
	static public final int dayNumInMonth(int month) {
		return dayNumInMonth(null, 0, month);
	}

	/*** 当前时间内当月共有多少天 **/
	static public final int dayNumInMonth() {
		return dayNumInMonth(null, 0, -1);
	}

	/*** 此时间内当月共有多少天 **/
	static public final int dayNumInMonth(int year, int month) {
		return dayNumInMonth(null, year, month);
	}

	/*** 此时间内当年共有多少天 **/
	static public final int dayNumInYear(Calendar calendar, int year) {
		if (calendar == null) {
			calendar = nowCalendar();
		}
		if (year > 0) {
			calendar.set(Calendar.YEAR, year);
		}
		return calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/*** 此时间内当年共有多少天 **/
	static public final int dayNumInYear(Calendar calendar) {
		return dayNumInYear(calendar, 0);
	}

	/*** 此时间内当年共有多少天 **/
	static public final int dayNumInYear() {
		return dayNumInYear(null, 0);
	}

	/*** 此时间内当年共有多少天 **/
	static public final int dayNumInYear(int year) {
		return dayNumInYear(null, year);
	}

	/*** calendar 与 nCalendar 之间相差多少天 **/
	static public final int diffDayNum4Month(Calendar calendar, Calendar nCalendar) {
		long t1 = calendar.getTimeInMillis();
		long t2 = nCalendar.getTimeInMillis();
		long diff = t2 - t1;
		if (diff < 0)
			diff = diff * -1;

		int day = (int) (diff / TIME_DAY);
		return day;
	}

	/*** 到添加n个月后 的 calender的day 所增加的天数 **/
	static public final int diffDayNum4AddMonth(Calendar calendar, int add) {
		if (calendar == null) {
			calendar = nowCalendar();
		}
		long t1 = calendar.getTimeInMillis();
		Calendar nCalendar = parse2Cal(t1);
		nCalendar = addMonth(nCalendar, add);
		return diffDayNum4Month(calendar, nCalendar);
	}

	/*** 到下个月的calender的day 所增加的天数 **/
	static public final int diffDayNum4NextMonth(Calendar calendar) {
		return diffDayNum4AddMonth(calendar, 1);
	}

	/*** 到下个月的 今天 所增加的天数 **/
	static public final int diffDayNum4NextMonth() {
		return diffDayNum4AddMonth(null, 1);
	}
}
