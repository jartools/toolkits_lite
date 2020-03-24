package com.bowlong.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("serial")
public class NewDate extends Date {
	Calendar c = Calendar.getInstance();

	public NewDate() {
		super();
	}

	public NewDate(Calendar c) {
		this.c = c;
		setTime(c.getTime().getTime());
	}

	public NewDate(long ms) {
		super(ms);
	}

	public NewDate(Date d) {
		super(d.getTime());
	}

	public NewDate(NewDate d) {
		super(d.getTime());
	}

	public NewDate(java.sql.Date d) {
		super(d.getTime());
	}

	public NewDate(java.sql.Timestamp ts) {
		super(ts.getTime());
	}

	public NewDate(int year, int month, int day) {
		this(year, month, day, 0, 0, 0, 0);
	}

	public NewDate(int year, int month, int day, int hour, int minute, int second) {
		this(year, month, day, hour, minute, second, 0);
	}

	public NewDate(int year, int month, int day, int hour, int minute, int second, int millis) {
		c.set(year, month - 1, day, hour, minute, second);
		setDate(c.getTime());
	}

	public void setCalendar(Calendar c2) {
		c = c2;
	}

	public void setLocale(TimeZone zone, Locale locale) {
		c = Calendar.getInstance(locale);
	}

	public NewDate addYear(int y) {
		c.setTime(this);
		c.add(Calendar.YEAR, y);
		setTime(c.getTime().getTime());
		return this;
	}

	public NewDate addMonth(int m) {
		c.setTime(this);
		c.add(Calendar.MONTH, m);
		setTime(c.getTime().getTime());
		return this;
	}

	public NewDate addWeek(int w) {
		return addMillis(w * DateEx.TIME_WEEK);
	}

	public NewDate addDay(int d) {
		return addMillis(d * DateEx.TIME_DAY);
	}

	public NewDate addHour(int h) {
		return addMillis(h * DateEx.TIME_HOUR);
	}

	public NewDate addMinute(int m) {
		return addMillis(m * DateEx.TIME_MINUTE);
	}

	public NewDate addSecond(int s) {
		return addMillis(s * DateEx.TIME_SECOND);
	}

	public NewDate addMillis(long t) {
		setTime(getTime() + t);
		return this;
	}

	public String fmt_yyyyMMdd() {
		return format(DateEx.fmt_yyyy_MM_dd);
	}

	public String fmt_yyyyMMddHHmmss() {
		return format(DateEx.fmt_yyyy_MM_dd_HH_mm_ss);
	}

	public String format(String fmt) {
		return DateEx.format(this, fmt);
	}

	public static void main(String[] args) {
		NewDate d = new NewDate(2012, 2, 1);
		d.setDay(2);
		d.setMonth2(2);
		d.setHour(5);
		d.setYearMonthDay(2013, 2, 29);
		d.setHourMinuteSec(13, 24, 33);
		d.setHour(23);
		System.out.println(d.format(DateEx.fmt_yyyy_MM_dd_HH_mm_ss));
	}

	public NewDate setYearMonthDay(int year, int month, int day) {
		c.setTime(this);
		c.set(year, month - 1, day);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public NewDate setHourMinuteSec(int hour, int min, int sec) {
		c.setTime(this);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);
		c.set(Calendar.MILLISECOND, 0);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public int getYear() {
		c.setTime(this);
		return c.get(Calendar.YEAR);
	}

	public NewDate setYear2(int year) {
		c.setTime(this);
		c.set(Calendar.YEAR, year);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public int getMonth() {
		c.setTime(this);
		return c.get(Calendar.MONTH);
	}

	public NewDate setMonth2(int month) {
		c.setTime(this);
		c.set(Calendar.MONTH, month - 1);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public int getWeekOfYear() {
		c.setTime(this);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	public int getWeekOfMonth() {
		c.setTime(this);
		return c.get(Calendar.WEEK_OF_MONTH);
	}

	public int getDayOfWeek() {
		c.setTime(this);
		return c.get(Calendar.DAY_OF_WEEK) - 1; // 中国习惯是周一为1。欧洲习惯周日1周一2
	}

	public int getDay() {
		c.setTime(this);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public NewDate setDay(int day) {
		c.setTime(this);
		c.set(Calendar.DAY_OF_MONTH, day);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public int getHour() {
		c.setTime(this);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public NewDate setHour(int hour) {
		c.setTime(this);
		c.set(Calendar.HOUR_OF_DAY, hour);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public int getMinute() {
		c.setTime(this);
		return c.get(Calendar.MINUTE);
	}

	public NewDate setMinute(int minute) {
		c.setTime(this);
		c.set(Calendar.MINUTE, minute);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public int getSecond() {
		c.setTime(this);
		return c.get(Calendar.SECOND);
	}

	public NewDate setSecond(int second) {
		c.setTime(this);
		c.set(Calendar.SECOND, second);
		this.setTime(c.getTimeInMillis());
		return this;
	}

	public void setDate(Date d) {
		this.setTime(d.getTime());
	}

	public void setDate(java.sql.Date d) {
		this.setTime(d.getTime());
	}

	public void setTimestamp(java.sql.Timestamp ts) {
		this.setTime(ts.getTime());
	}

	// 时间差
	public long diffMsIt(Date d) {
		return diffMsIt(d.getTime());
	}

	public long diffMsIt(long ms) {
		return ms - getTime();
	}

	public long diffMsIt() {
		return diffMsIt(DateEx.now());
	}

	public int maxDayOfMonth() {
		c.setTime(this);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public boolean in(Date min, Date max) {
		return this.after(min) && this.before(max);
	}

	public boolean notIn(Date min, Date max) {
		return !in(min, max);
	}

	public String toString() {
		return fmt_yyyyMMddHHmmss();
	}

	// 工作日
	public boolean workDay() {
		int dow = getDayOfWeek();
		return (dow >= 1 && dow <= 5);
	}

	// 周末
	public boolean weekend() {
		return !(workDay());
	}

	// 工作时间(早9晚6)
	public boolean workTime9to18() {
		int h = getHour();
		return (h >= 9 && h < 18);
	}

	// 工作时间外
	public boolean noWorkTime() {
		return !(workTime9to18());
	}

	public boolean isTimeout(long timeout) {
		return DateEx.isTimeout(this, timeout);
	}

	public NewDate create() {
		return new NewDate(this);
	}

	public NewDate zero() {
		return setHourMinuteSec(0, 0, 0);
	}
}
