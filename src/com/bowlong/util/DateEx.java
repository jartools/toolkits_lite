package com.bowlong.util;

import java.util.Calendar;
import java.util.Date;

import com.bowlong.basic.ExOrigin;
import com.bowlong.lang.NumEx;

/*** 日期对象数据操作 **/
public class DateEx extends ExOrigin {

	static public final int year() {
		Date d = nowDate();
		return year(d);
	}

	static public final int year(Date v) {
		String s = format(v, fmt_yyyy);
		return NumEx.stringToInt(s);
	}

	static public final int month() {
		Date d = nowDate();
		return month(d);
	}

	static public final int month(Date v) {
		String s = format(v, fmt_MM);
		return NumEx.stringToInt(s);
	}

	static public final int day() {
		Date d = nowDate();
		return day(d);
	}

	static public final int day(Date v) {
		String s = format(v, fmt_dd);
		return NumEx.stringToInt(s);
	}

	static public final int hour() {
		Date d = nowDate();
		return hour(d);
	}

	static public final int hour(Date v) {
		String s = format(v, fmt_HH);
		return NumEx.stringToInt(s);
	}

	static public final int minute() {
		Date d = nowDate();
		return minute(d);
	}

	static public final int minute(Date v) {
		String s = format(v, fmt_mm);
		return NumEx.stringToInt(s);
	}

	static public final int second() {
		Date d = nowDate();
		return second(d);
	}

	static public final int second(Date v) {
		String s = format(v, fmt_ss);
		return NumEx.stringToInt(s);
	}

	static public final int ms() {
		Date d = nowDate();
		return ms(d);
	}

	static public final int ms(Date v) {
		String s = format(v, fmt_SSS);
		return NumEx.stringToInt(s);
	}

	/*** 是否为周末(星期六，星期天) **/
	static public final boolean isWeekEnd(Date dt) {
		if (dt == null)
			return false;
		int week = week(dt);
		boolean r = week == 0 || week == 6;
		return r;
	}

	static public final boolean isWeekEnd() {
		Date d = nowDate();
		return isWeekEnd(d);
	}

	static public final boolean isWeekEnd(long nowtime) {
		if (nowtime == 0)
			return false;
		Date d = parse2Date(nowtime);
		return isWeekEnd(d);
	}

	static public final int weekInYear(Date v) {
		String s = format(v, fmt_wInYear);
		return NumEx.stringToInt(s);
	}

	static public final int weekInYear() {
		Date d = nowDate();
		return weekInYear(d);
	}

	static public final int weekInMonth(Date v) {
		String s = format(v, fmt_WInMonth);
		return NumEx.stringToInt(s);
	}

	static public final int weekInMonth() {
		Date d = nowDate();
		return weekInMonth(d);
	}

	static public final int dayInYear(Date v) {
		String s = format(v, fmt_DInYear);
		return NumEx.stringToInt(s);
	}

	static public final int dayInYear() {
		Date d = nowDate();
		return dayInYear(d);
	}

	static public final int dayInMonth(Date v) {
		String s = format(v, fmt_dInMonth);
		return NumEx.stringToInt(s);
	}

	static public final int dayInMonth() {
		Date d = nowDate();
		return dayInMonth(d);
	}

	public static final Date addYear(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addYear(c, v);
		return c.getTime();
	}

	public static final Date addMonth(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addMonth(c, v);
		return c.getTime();
	}

	public static final Date addWeek(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addWeek(c, v);
		return c.getTime();
	}

	public static final Date addDay(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addDay(c, v);
		return c.getTime();
	}

	public static final Date addHour(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addHour(c, v);
		return c.getTime();
	}

	public static final Date addMinute(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addMinute(c, v);
		return c.getTime();
	}

	public static final Date addSecond(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addSecond(c, v);
		return c.getTime();
	}

	public static final Date addMilliSecond(Date date, int v) {
		Calendar c = parse2Cal(date);
		c = CalendarEx.addMilliSecond(c, v);
		return c.getTime();
	}
}
