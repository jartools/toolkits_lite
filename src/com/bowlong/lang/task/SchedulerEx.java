package com.bowlong.lang.task;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.bowlong.util.DateEx;
import com.bowlong.util.NewDate;

/**
 * 使用调度器执行<br/>
 * schedule 创建并执行在给定延迟后启用的一次性操作。 <br/>
 * <br/>
 * scheduleWithFixedDelay 创建并执行一个在给定初始延迟后首次启用的定期操作。<br/>
 * 如果此任务的任何一个执行要花费比其周期更长的时间 ，则将推迟后续执行，但不会同时执行。<br/>
 * <br/>
 * scheduleAtFixedRate 创建并执行一个在给定初始延迟后首次启用的定期操作。<br/>
 * 与任务执行时间无关，相隔两次之间的时间是一样的。
 * 
 * @author Canyon
 * @version createtime：2015年8月21日下午3:33:23
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SchedulerEx {

	// scheduleWithFixedDelay,scheduleAtFixedRate
	// 如果任务的任一执行遇到异常，就会取消后续执行。否则，只能通过执行程序的取消或终止方法来终止该任务。

	// /////////////////////////////////////////////////////////////////
	// 使用调度器执行
	static ScheduledExecutorService _scheduledPool = null;

	// /////////////////////////////////////////////////////////////////
	// 执行 schedule
	static public final ScheduledFuture<?> schedule(
			final ScheduledExecutorService ses, final Runnable r,
			final long delay, final TimeUnit unit) {
		return ses.schedule(r, delay, unit);
	}

	static public final ScheduledFuture<?> schedule(
			ScheduledExecutorService ses, Callable r, long delay, TimeUnit unit) {
		return ses.schedule(r, delay, unit);
	}

	static public final ScheduledFuture<?> scheduleMS(
			ScheduledExecutorService ses, Runnable r, long delay) {
		return ses.schedule(r, delay, TimeUnit.MILLISECONDS);
	}

	static public final ScheduledFuture<?> schedule(Runnable r, long delay,
			TimeUnit unit) {
		if (_scheduledPool == null)
			_scheduledPool = ThreadEx.newScheduledPool(32);

		return schedule(_scheduledPool, r, delay, unit);
	}

	static public final ScheduledFuture<?> scheduleMS(Runnable r, long delay) {
		return schedule(r, delay, TimeUnit.MILLISECONDS);
	}

	static public final ScheduledFuture<?> schedule(Callable r, long delay,
			TimeUnit unit) {
		if (_scheduledPool == null)
			_scheduledPool = ThreadEx.newScheduledPool(32);

		return schedule(_scheduledPool, r, delay, unit);
	}

	static public final ScheduledFuture<?> scheduleMS(Callable r, long delay) {
		return schedule(r, delay, TimeUnit.MILLISECONDS);
	}

	// 定时执行,再间隔调度重复执行,FixedDelay相对固定的延迟后，执行某项计划。
	static public final ScheduledFuture<?> scheduledFixedDelay(
			ScheduledExecutorService scheduledPool, Runnable r,
			long initialDelay, long delay, TimeUnit unit) {
		return scheduledPool.scheduleWithFixedDelay(r, initialDelay, delay,
				unit);
	}

	static public final ScheduledFuture<?> scheduledFixedDelay(Runnable r,
			long initialDelay, long delay, TimeUnit unit) {
		if (_scheduledPool == null)
			_scheduledPool = ThreadEx.newScheduledPool(32);

		return scheduledFixedDelay(_scheduledPool, r, initialDelay, delay, unit);
	}

	static public final ScheduledFuture<?> scheduledFixedDelayMS(
			ScheduledExecutorService scheduledPool, Runnable r,
			long initialDelay, long delay) {
		return scheduledFixedDelay(scheduledPool, r, initialDelay, delay,
				TimeUnit.MILLISECONDS);
	}

	static public final ScheduledFuture<?> scheduledFixedDelayMS(Runnable r,
			long initialDelay, long delay) {
		return scheduledFixedDelay(r, initialDelay, delay,
				TimeUnit.MILLISECONDS);
	}

	// 已固定的频率来执行某项计划(任务)
	static public final ScheduledFuture<?> scheduledFixedRate(
			ScheduledExecutorService threadPool, Runnable r, long initialDelay,
			long delay, TimeUnit unit) {
		return threadPool.scheduleAtFixedRate(r, initialDelay, delay, unit);
	}

	static public final ScheduledFuture<?> scheduledFixedRate(Runnable r,
			long initialDelay, long delay, TimeUnit unit) {
		if (_scheduledPool == null)
			_scheduledPool = ThreadEx.newScheduledPool(32);
		return scheduledFixedRate(_scheduledPool, r, initialDelay, delay, unit);
	}

	static public final ScheduledFuture<?> scheduledFixedRateMS(
			ScheduledExecutorService threadPool, Runnable r, long initialDelay,
			long delay) {
		return scheduledFixedRate(threadPool, r, initialDelay, delay,
				TimeUnit.MILLISECONDS);
	}

	static public final ScheduledFuture<?> scheduledFixedRateMS(Runnable r,
			long initialDelay, long delay) {
		return scheduledFixedRate(r, initialDelay, delay, TimeUnit.MILLISECONDS);
	}

	static public final long now() {
		return System.currentTimeMillis();
	}

	// /////////////////////////////////////////////////////////////////
	// 定时-执行
	static public final ScheduledFuture<?> timeScheduled(
			ScheduledExecutorService threadPool, Runnable r, Date d) {
		long delay = d.getTime() - now();
		delay = delay <= 0 ? 1 : delay;
		return scheduleMS(threadPool, r, delay);
	}

	static public final ScheduledFuture<?> timeScheduled(Runnable r, Date d) {
		if (_scheduledPool == null)
			_scheduledPool = ThreadEx.newScheduledPool(32);
		return timeScheduled(_scheduledPool, r, d);
	}

	static public final ScheduledFuture<?> timeScheduledFixedDelay(
			ScheduledExecutorService threadPool, Runnable r, Date d, long delay) {
		long initialDelay = d.getTime() - now();
		initialDelay = initialDelay <= 0 ? 1 : initialDelay;
		return scheduledFixedDelayMS(threadPool, r, initialDelay, delay);
	}

	static public final ScheduledFuture<?> timeScheduledFixedDelay(Runnable r,
			Date d, long delay) {
		if (_scheduledPool == null)
			_scheduledPool = ThreadEx.newScheduledPool(32);
		return timeScheduledFixedDelay(_scheduledPool, r, d, delay);
	}

	// 确定时分秒，每日执行
	static public final ScheduledFuture<?> timeScheduledEveryDay(
			ScheduledExecutorService threadPool, Runnable r, int hour,
			int minute, int sec) {
		long now = now();
		NewDate dat = new NewDate();
		long h = dat.getHour();
		long m = dat.getMinute();
		long s = dat.getSecound();
		long initialDelay = 0;
		long e1 = h * DateEx.TIME_HOUR + m * DateEx.TIME_MINUTE + s
				* DateEx.TIME_SECOND;
		long e2 = hour * DateEx.TIME_HOUR + minute * DateEx.TIME_MINUTE + sec
				* DateEx.TIME_SECOND;
		if (e1 < e2) {
			initialDelay = e2 - e1;
		} else {
			long tomorrow = dat.setHourMinuteSec(0, 0, 0).addDay(+1).getTime();
			initialDelay = tomorrow - now + e2;
		}
		long delay = DateEx.TIME_DAY;
		return scheduledFixedRate(threadPool, r, initialDelay, delay,
				TimeUnit.MILLISECONDS);
	}

	// 确定时分秒，每日执行
	static public final ScheduledFuture<?> timeScheduledEveryDay(Runnable r,
			int hour, int minute, int sec) {
		if (_scheduledPool == null)
			_scheduledPool = ThreadEx.newScheduledPool(32);
		return timeScheduledEveryDay(_scheduledPool, r, hour, minute, sec);
	}
}
