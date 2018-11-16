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
	static final int nmax = 16;
	static ScheduledExecutorService _ses = null;

	// /////////////////////////////////////////////////////////////////
	// 执行 schedule
	static public final ScheduledFuture<?> schedule(final ScheduledExecutorService ses, final Runnable r,
			final long delay, final TimeUnit unit) {
		return ses.schedule(r, delay, unit);
	}

	static public final ScheduledFuture<?> scheduleMS(ScheduledExecutorService ses, Runnable r, long delay) {
		return ses.schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> scheduleSec(ScheduledExecutorService ses, Runnable r, long delay) {
		return ses.schedule(r, delay, TimeUnit.SECONDS);
	}

	static public final ScheduledFuture<?> schedule(Runnable r, long delay, TimeUnit unit) {
		if (_ses == null)
			_ses = ThreadEx.newScheduledPool(nmax);

		return schedule(_ses, r, delay, unit);
	}

	static public final ScheduledFuture<?> scheduleMS(Runnable r, long delay) {
		return schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> scheduleSec(Runnable r, long delay) {
		return schedule(r, delay, TimeUnit.SECONDS);
	}

	static public final ScheduledFuture<?> schedule(ScheduledExecutorService ses, Callable r, long delay,
			TimeUnit unit) {
		return ses.schedule(r, delay, unit);
	}
	
	static public final ScheduledFuture<?> scheduleMS(ScheduledExecutorService ses, Callable r, long delay) {
		return ses.schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> scheduleSec(ScheduledExecutorService ses, Callable r, long delay) {
		return ses.schedule(r, delay, TimeUnit.SECONDS);
	}
	
	static public final ScheduledFuture<?> schedule(Callable r, long delay, TimeUnit unit) {
		if (_ses == null)
			_ses = ThreadEx.newScheduledPool(nmax);

		return schedule(_ses, r, delay, unit);
	}

	static public final ScheduledFuture<?> scheduleMS(Callable r, long delay) {
		return schedule(r, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> scheduleSec(Callable r, long delay) {
		return schedule(r, delay, TimeUnit.SECONDS);
	}

	// 定时执行,再间隔调度重复执行,FixedDelay相对固定的延迟后，执行某项计划。
	static public final ScheduledFuture<?> fixedDelay(ScheduledExecutorService scheduledPool, Runnable r,
			long initialDelay, long delay, TimeUnit unit) {
		return scheduledPool.scheduleWithFixedDelay(r, initialDelay, delay, unit);
	}
	
	static public final ScheduledFuture<?> fixedDelayMS(ScheduledExecutorService scheduledPool, Runnable r,
			long initialDelay, long delay) {
		return fixedDelay(scheduledPool, r, initialDelay, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> fixedDelaySec(ScheduledExecutorService scheduledPool, Runnable r,
			long initialDelay, long delay) {
		return fixedDelay(scheduledPool, r, initialDelay, delay, TimeUnit.SECONDS);
	}

	static public final ScheduledFuture<?> fixedDelay(Runnable r, long initialDelay, long delay, TimeUnit unit) {
		if (_ses == null)
			_ses = ThreadEx.newScheduledPool(nmax);

		return fixedDelay(_ses, r, initialDelay, delay, unit);
	}

	static public final ScheduledFuture<?> fixedDelayMS(Runnable r, long initialDelay, long delay) {
		return fixedDelay(r, initialDelay, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> fixedDelaySec(Runnable r, long initialDelay, long delay) {
		return fixedDelay(r, initialDelay, delay, TimeUnit.SECONDS);
	}

	// 已固定的频率来执行某项计划(任务)
	static public final ScheduledFuture<?> fixedRate(ScheduledExecutorService threadPool, Runnable r, long initialDelay,
			long delay, TimeUnit unit) {
		return threadPool.scheduleAtFixedRate(r, initialDelay, delay, unit);
	}
	
	static public final ScheduledFuture<?> fixedRateMS(ScheduledExecutorService threadPool, Runnable r,
			long initialDelay, long delay) {
		return fixedRate(threadPool, r, initialDelay, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> fixedRateSec(ScheduledExecutorService threadPool, Runnable r,
			long initialDelay, long delay) {
		return fixedRate(threadPool, r, initialDelay, delay, TimeUnit.SECONDS);
	}

	static public final ScheduledFuture<?> fixedRate(Runnable r, long initialDelay, long delay, TimeUnit unit) {
		if (_ses == null)
			_ses = ThreadEx.newScheduledPool(nmax);
		return fixedRate(_ses, r, initialDelay, delay, unit);
	}

	static public final ScheduledFuture<?> fixedRateMS(Runnable r, long initialDelay, long delay) {
		return fixedRate(r, initialDelay, delay, TimeUnit.MILLISECONDS);
	}
	
	static public final ScheduledFuture<?> fixedRateSec(Runnable r, long initialDelay, long delay) {
		return fixedRate(r, initialDelay, delay, TimeUnit.SECONDS);
	}

	static public final long now() {
		return System.currentTimeMillis();
	}

	// /////////////////////////////////////////////////////////////////
	// 定时-执行
	static public final ScheduledFuture<?> timeFixedDelay(ScheduledExecutorService threadPool, Runnable r, Date d,
			long delay) {
		long initialDelay = d.getTime() - now();
		initialDelay = initialDelay <= 0 ? 1 : initialDelay;
		return fixedDelayMS(threadPool, r, initialDelay, delay);
	}

	static public final ScheduledFuture<?> timeFixedDelay(Runnable r, Date d, long delay) {
		if (_ses == null)
			_ses = ThreadEx.newScheduledPool(nmax);
		return timeFixedDelay(_ses, r, d, delay);
	}

	// 确定时分秒，每日执行
	static public final ScheduledFuture<?> timeEveryDay(ScheduledExecutorService threadPool, Runnable r, int hour,
			int minute, int sec) {
		long now = now();
		NewDate dat = new NewDate();
		long h = dat.getHour();
		long m = dat.getMinute();
		long s = dat.getSecound();
		long initialDelay = 0;
		long e1 = h * DateEx.TIME_HOUR + m * DateEx.TIME_MINUTE + s * DateEx.TIME_SECOND;
		long e2 = hour * DateEx.TIME_HOUR + minute * DateEx.TIME_MINUTE + sec * DateEx.TIME_SECOND;
		if (e1 < e2) {
			initialDelay = e2 - e1;
		} else {
			long tomorrow = dat.setHourMinuteSec(0, 0, 0).addDay(+1).getTime();
			initialDelay = tomorrow - now + e2;
		}
		long delay = DateEx.TIME_DAY;
		return fixedRateMS(threadPool, r, initialDelay, delay);
	}

	// 确定时分秒，每日执行
	static public final ScheduledFuture<?> timeEveryDay(Runnable r, int hour, int minute, int sec) {
		if (_ses == null)
			_ses = ThreadEx.newScheduledPool(nmax);
		return timeEveryDay(_ses, r, hour, minute, sec);
	}
}
