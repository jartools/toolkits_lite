package com.bowlong.basic;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.io.ByteInStream;
import com.bowlong.io.ByteOutStream;
import com.bowlong.io.FileRw;
import com.bowlong.lang.RndEx;
import com.bowlong.lang.StrEx;
import com.bowlong.lang.task.SchedulerEx;
import com.bowlong.lang.task.ThreadEx;
import com.bowlong.objpool.ByteInPool;
import com.bowlong.objpool.ByteOutPool;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.pinyin.PinYin;
import com.bowlong.util.MapEx;

/**
 * 扩展集合类<br/>
 * 非底层扩展工具和bean实体可继承该对象<br/>
 * 
 * @author Canyon
 * @time 2019-02-14 19:32
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ExToolkit extends ExOrigin {

	static final public void println(Object... args) {
		print(args);
		System.out.println();
	}

	static final public void print(Object... args) {
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			int length = args.length;
			int p = 0;
			for (Object o : args) {
				sb.append(o);
				p++;
				if (p < length)
					sb.append(", ");
			}
			System.out.print(sb);
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	// ///////////////////////////////////////////////////
	static final public Set newSet() {
		return Collections.synchronizedSet(new HashSet());
	}

	static final public boolean intersectedPoints(List<Point> list, Point p) {
		if (list == null || list.isEmpty() || p == null)
			return false;

		Set<String> c = new HashSet<String>();
		for (Point p1 : list) {
			c.add(p1.toString());
		}
		return c.contains(p.toString());
	}

	// ///////////////////////////////////////////////////
	static final public String pn(int n) {
		return n > 0 ? "+" + n : String.valueOf(n);
	}

	// ///////////////////////////////////////////////////
	static final public String s(String s, Object... args) {
		return String.format(s, args);
	}

	static final public void s(StringBuffer sb, String s, Object... args) {
		String s2 = String.format(s, args);
		sb.append(s2);
	}

	static final public String sn(String s, Object... args) {
		return String.format(s + "\r\n", args);
	}

	static final public void sn(StringBuffer sb, String s, Object... args) {
		s(sb, s + "\r\n", args);
	}

	// 计算百分率
	static final public int percent(double v, double max) {
		if (v <= 0 || max <= 0)
			return 0;
		int r = (int) (v * 100 / max);
		return r > 100 ? 100 : r;
	}

	// ///////////////////////////////////////////////////
	static final public ScheduledExecutorService newScheduledPool(String name, int n) {
		return ThreadEx.newScheduledPool(name, n);
	}

	static final public ScheduledFuture<?> scheduleMS(ScheduledExecutorService threadPool, Runnable r, long delayMs) {
		return SchedulerEx.scheduleMS(threadPool, r, delayMs);
	}

	static final public ScheduledFuture<?> scheduleSec(ScheduledExecutorService threadPool, Runnable r, int delaySec) {
		return SchedulerEx.scheduleSec(threadPool, r, delaySec);
	}

	// 固定时间
	static final public ScheduledFuture<?> scheduledFixedDelay(ScheduledExecutorService threadPool, Runnable r, Date d, long delay) {
		return SchedulerEx.timeFixedDelay(threadPool, r, d, delay);
	}

	// 某个频率
	static final public ScheduledFuture<?> scheduledFixedRate(ScheduledExecutorService threadPool, Runnable r, long initialDelay, long delay) {
		return SchedulerEx.fixedRateMS(threadPool, r, initialDelay, delay);
	}

	// 确定时分秒，每日执行
	static final public ScheduledFuture<?> scheduledEveryDay(ScheduledExecutorService threadPool, Runnable r, int hour, int minute, int sec) {
		return SchedulerEx.timeEveryDay(threadPool, r, hour, minute, sec);
	}

	static final public Map getMap(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return null;
		return MapEx.getMap(ps, key);
	}

	static final public List getList(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return null;
		return MapEx.getList(ps, key);
	}

	static final public String getString(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return "";
		return MapEx.getString(ps, key);
	}

	static final public boolean getBool(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return false;
		return MapEx.getBoolean(ps, key);
	}

	static final public int getInt(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return 0;
		return MapEx.getInt(ps, key);
	}

	static final public long getLong(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return 0l;
		return MapEx.getLong(ps, key);
	}

	static final public double getDouble(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return 0;
		return MapEx.getDouble(ps, key);
	}

	static final public Date getDate(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return null;
		return MapEx.getDate(ps, key);
	}

	// //////////////////////////////////////////////
	static final public String upperFirst(String s) {
		return StrEx.upperFirst(s);
	}

	static final public String py(String s) {
		return PinYin.shortPinYin(s);
	}

	static final public byte[] readFully(File f) {
		return FileRw.readFully(f);
	}

	static final public byte[] readStream(InputStream is) throws IOException {
		return B2InputStream.readStream(is);
	}

	static final public String readFully(File f, String charset) throws IOException {
		byte[] b = readFully(f);
		if (b == null)
			return "";
		return new String(b, charset);
	}

	// 序列化
	static final public byte[] serialization(final Object obj) throws Exception {
		if (obj == null)
			return new byte[0];
		try (ByteOutStream out = ByteOutPool.borrowObject(); ObjectOutputStream oos = new ObjectOutputStream(out);) {
			oos.writeObject(obj);
			return out.toByteArray();
		} catch (Exception e) {
			throw e;
		}
	}

	// 反序列化
	static final public Object deserialization(final byte[] b) throws Exception {
		if (b == null || b.length <= 0)
			return null;
		try (ByteInStream in = ByteInPool.borrowObject(b); ObjectInputStream ois = new ObjectInputStream(in);) {
			return ois.readObject();
		} catch (Exception e) {
			throw e;
		}
	}

	static final public String pinyin(String s) {
		return PinYin.pinYin(s);
	}

	static final public String getShortPinYin(String s) {
		return PinYin.getShortPinYin(s);
	}

	static final public <T> T rand(List<T> objs) {
		if (objs == null || objs.isEmpty())
			return null;
		else if (objs.size() == 1)
			return (T) objs.get(0);

		int i = RndEx.nextInt(0, objs.size());
		return (T) objs.get(i);
	}

	static final public void sleep(long ms) {
		ThreadEx.Sleep(ms);
	}

	static final public void sysExit(long ms) {
		if (ms > 0)
			ThreadEx.Sleep(ms);
		System.exit(1);
	}

	// 带1位小数
	static public final String n2s(int i) {
		if (i < 1000)
			return i + "";
		if (i < 1000 * 10)
			return String.format("%.1fK", ((double) i / 1000));
		if (i < 1000 * 1000)
			return String.format("%.1fW", ((double) i / 10000));
		if (i < 1000 * 1000 * 1000)
			return String.format("%.1fM", ((double) i / (1000 * 1000)));
		return String.format("%.1fG", ((double) i / (1000 * 1000 * 1000)));
	}

	// 带小数,支持负数
	static public final String n(int i) {
		boolean abs = false;
		if (i < 0) {
			i = -i;
			abs = true;
		}
		String s = n2s(i);
		String r = abs ? ('-' + s) : s;
		return r;
	}

	static public final int argb(int a, int r, int g, int b) {
		return (r << 24) + (r << 16) + (g << 8) + b;
	}

	static public final int[] argb(long argb) {
		int a = (byte) ((argb >> 24) & 0xff);
		int r = (byte) ((argb >> 16) & 0xff);
		int g = (byte) ((argb >> 8) & 0xff);
		int b = (byte) ((argb >> 0) & 0xff);
		int[] v = { a, r, g, b };
		return v;
	}

	static public final int rgb(int a, int r, int g, int b) {
		return (r << 16) + (g << 8) + b;
	}

	static public final int[] rgb(int rgb) {
		int r = (byte) ((rgb >> 16) & 0xff);
		int g = (byte) ((rgb >> 8) & 0xff);
		int b = (byte) ((rgb >> 0) & 0xff);
		int[] v = { r, g, b };
		return v;
	}
}
