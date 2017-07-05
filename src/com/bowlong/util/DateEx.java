package com.bowlong.util;

import java.util.Calendar;
import java.util.Date;

import com.bowlong.lang.NumEx;

/*** 日期对象数据操作 **/
public class DateEx extends DateFmtEx {

	static public final int year() {
		return year(nowDate());
	}

	static public final int year(Date v) {
		return NumEx.stringToInt(format(v, fmt_yyyy));
	}

	static public final int month() {
		return month(nowDate());
	}

	static public final int month(Date v) {
		return NumEx.stringToInt(format(v, fmt_MM));
	}

	static public final int day() {
		return day(nowDate());
	}

	static public final int day(Date v) {
		return NumEx.stringToInt(format(v, fmt_dd));
	}

	static public final int hour() {
		return hour(nowDate());
	}

	static public final int hour(Date v) {
		return NumEx.stringToInt(format(v, fmt_HH));
	}

	static public final int minute() {
		return minute(nowDate());
	}

	static public final int minute(Date v) {
		return NumEx.stringToInt(format(v, fmt_mm));
	}

	static public final int second() {
		return second(nowDate());
	}

	static public final int second(Date v) {
		return NumEx.stringToInt(format(v, fmt_ss));
	}

	static public final int ms() {
		return ms(nowDate());
	}

	static public final int ms(Date v) {
		return NumEx.stringToInt(format(v, fmt_SSS));
	}

	static public final int week() {
		return week(nowDate());
	}

	/*** 星期数(0~6) **/
	static public final int week(Date v) {
		return CalendarEx.week(parse2Cal(v));
	}

	static public final boolean isWeek() {
		return isWeek(nowDate());
	}

	static public final boolean isWeek(long nowtime) {
		if (nowtime == 0)
			return false;
		return isWeek(parse2Date(nowtime));
	}

	/*** 是否为周末(星期六，星期天) **/
	static public final boolean isWeek(Date dt) {
		if (dt == null)
			return false;
		int week = week(dt);
		boolean r = week == 0 || week == 6;
		return r;
	}

	static public final int weekInYear(Date v) {
		return NumEx.stringToInt(format(v, fmt_wInYear));
	}

	static public final int weekInYear() {
		return weekInYear(nowDate());
	}

	static public final int weekInMonth(Date v) {
		return NumEx.stringToInt(format(v, fmt_WInMonth));
	}

	static public final int weekInMonth() {
		return weekInMonth(nowDate());
	}

	static public final int dayInYear(Date v) {
		return NumEx.stringToInt(format(v, fmt_DInYear));
	}

	static public final int dayInYear() {
		return dayInYear(nowDate());
	}

	static public final int dayInMonth(Date v) {
		return NumEx.stringToInt(format(v, fmt_dInMonth));
	}

	static public final int dayInMonth() {
		return dayInMonth(nowDate());
	}

	public static final Date addYear(Date date, int v) {
		Calendar c = CalendarEx.addYear(parse2Cal(date), v);
		return c.getTime();
	}

	public static final Date addMonth(Date date, int v) {
		Calendar c = CalendarEx.addMonth(parse2Cal(date), v);
		return c.getTime();
	}

	public static final Date addWeek(Date date, int v) {
		Calendar c = CalendarEx.addWeek(parse2Cal(date), v);
		return c.getTime();
	}

	public static final Date addDay(Date date, int v) {
		Calendar c = CalendarEx.addDay(parse2Cal(date), v);
		return c.getTime();
	}

	public static final Date addHour(Date date, int v) {
		Calendar c = CalendarEx.addHour(parse2Cal(date), v);
		return c.getTime();
	}

	public static final Date addMinute(Date date, int v) {
		Calendar c = CalendarEx.addMinute(parse2Cal(date), v);
		return c.getTime();
	}

	public static final Date addSecond(Date date, int v) {
		Calendar c = CalendarEx.addSecond(parse2Cal(date), v);
		return c.getTime();
	}

	public static final Date addMilliSecond(Date date, int v) {
		Calendar c = CalendarEx.addMilliSecond(parse2Cal(date), v);
		return c.getTime();
	}
}
