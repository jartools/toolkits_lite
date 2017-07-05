package com.bowlong.tool;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.io.ByteInStream;
import com.bowlong.io.ByteOutStream;
import com.bowlong.io.FileRw;
import com.bowlong.lang.NumEx;
import com.bowlong.lang.RndEx;
import com.bowlong.lang.StrEx;
import com.bowlong.lang.task.SchedulerEx;
import com.bowlong.lang.task.ThreadEx;
import com.bowlong.objpool.ByteInPool;
import com.bowlong.objpool.ByteOutPool;
import com.bowlong.objpool.ObjPool;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.pinyin.PinYin;
import com.bowlong.util.DateEx;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.MapEx;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TkitBase extends TkitOrigin {

	static public final Log getLog(Class<?> clazz) {
		return LogFactory.getLog(clazz);
	}

	// static ScheduledExecutorService SES = newScheduledThreadPool("TipToAll",
	// 2);

	static public final void println(Object... args) {
		print(args);
		System.out.println();
	}

	static public final void print(Object... args) {
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
	static public final Map newMap() {
		return Collections.synchronizedMap(new HashMap());
	}

	static public final Set newSet() {
		return Collections.synchronizedSet(new HashSet());
	}

	static public final List newList() {
		return Collections.synchronizedList(new ArrayList());
	}

	static public final boolean intersectedPoints(List<Point> list, Point p) {
		if (list == null || list.isEmpty() || p == null)
			return false;

		Set<String> c = new HashSet<String>();
		for (Point p1 : list) {
			c.add(p1.toString());
		}
		return c.contains(p.toString());
	}

	// ///////////////////////////////////////////////////
	static public final ByteArrayOutputStream newStream() {
		return new ByteArrayOutputStream();
	}

	static public final InputStream newStream(byte[] b) {
		return new ByteArrayInputStream(b);
	}

	// ///////////////////////////////////////////////////
	static public final boolean nextBool() {
		return RndEx.nextBoolean();
	}

	static public final boolean nextBool(int max, int f) {
		int v = nextInt(max);
		return (v < f);
	}

	static public final <T> T rand(List objs) {
		if (objs == null || objs.isEmpty())
			return null;
		else if (objs.size() == 1)
			return (T) objs.get(0);

		int i = RndEx.nextInt(0, objs.size());
		return (T) objs.get(i);
	}

	static public final int nextInt(int max) {
		if (max <= 0)
			return 0;
		return RndEx.nextInt(max);
	}

	static public final int nextInt(int f, int t) {
		if (t <= f)
			return f;
		return RndEx.nextInt(t - f) + f;
	}

	static public final String pn(int n) {
		return n > 0 ? "+" + n : String.valueOf(n);
	}

	static public final List<Map> sort(List m1, final String key) {
		Collections.sort(m1, new Comparator<Map>() {
			public int compare(Map o1, Map o2) {
				Object v1 = o1.get(key);
				Object v2 = o2.get(key);
				if (v1 == null || v2 == null)
					return 0;
				return compareTo(v1, v2);
			}
		});
		return m1;
	}

	static public final int compareTo(Object v1, Object v2) {

		if (v1 == null || v2 == null)
			return 0;

		if (v1 instanceof Byte && v2 instanceof Byte) {
			Boolean i1 = (Boolean) v1;
			Boolean i2 = (Boolean) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Byte && v2 instanceof Byte) {
			Byte i1 = (Byte) v1;
			Byte i2 = (Byte) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Short && v2 instanceof Short) {
			Short i1 = (Short) v1;
			Short i2 = (Short) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Integer && v2 instanceof Integer) {
			Integer i1 = (Integer) v1;
			Integer i2 = (Integer) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Long && v2 instanceof Long) {
			Long i1 = (Long) v1;
			Long i2 = (Long) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof java.math.BigInteger
				&& v2 instanceof java.math.BigInteger) {
			java.math.BigInteger i1 = (java.math.BigInteger) v1;
			java.math.BigInteger i2 = (java.math.BigInteger) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof java.math.BigDecimal
				&& v2 instanceof java.math.BigDecimal) {
			java.math.BigDecimal i1 = (java.math.BigDecimal) v1;
			java.math.BigDecimal i2 = (java.math.BigDecimal) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Float && v2 instanceof Float) {
			Float i1 = (Float) v1;
			Float i2 = (Float) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Double && v2 instanceof Double) {
			Double i1 = (Double) v1;
			Double i2 = (Double) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Date && v2 instanceof Date) {
			Date i1 = (Date) v1;
			Date i2 = (Date) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof java.sql.Date && v2 instanceof java.sql.Date) {
			java.sql.Date i1 = (java.sql.Date) v1;
			java.sql.Date i2 = (java.sql.Date) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof java.sql.Timestamp
				&& v2 instanceof java.sql.Timestamp) {
			java.sql.Timestamp i1 = (java.sql.Timestamp) v1;
			java.sql.Timestamp i2 = (java.sql.Timestamp) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof java.sql.Time && v2 instanceof java.sql.Time) {
			java.sql.Time i1 = (java.sql.Time) v1;
			java.sql.Time i2 = (java.sql.Time) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof String && v2 instanceof String) {
			String i1 = (String) v1;
			String i2 = (String) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof java.lang.Enum && v2 instanceof java.lang.Enum) {
			java.lang.Enum i1 = (java.lang.Enum) v1;
			java.lang.Enum i2 = (java.lang.Enum) v2;
			return i1.compareTo(i2);
		}
		return 0;
	}

	static public final int compareTo4More(Object v1, Object v2, String parttern) {
		if (v1 == null || v2 == null)
			return 0;
		boolean isDate1 = (v1 instanceof Date);
		boolean isDate2 = (v2 instanceof Date);
		if ((isDate1 && isDate2) || (!isDate1 && !isDate2)) {
			return compareTo(v1, v2);
		}

		if (isDate1) {
			Date d1 = (Date) v1;
			Date d2 = null;
			if (v2 instanceof String) {
				long time = NumEx.stringToLong(v2.toString());
				if (time > DateEx.TIME_1900) {
					d2 = DateEx.parse2Date(time);
				} else {
					d2 = DateEx.parse2Date(v2.toString(), parttern);
				}
			} else if (v2 instanceof Long || v2 instanceof Integer) {
				long time = 0l;
				if (v2 instanceof Long)
					time = (long) v2;
				else
					time = (int) v2;

				if (time > DateEx.TIME_1900) {
					d2 = DateEx.parse2Date(time);
				} else {
					return 0;
				}
			}
			return compareTo(d1, d2);
		}

		return compareTo4More(v2, v1, parttern);
	}

	// ///////////////////////////////////////////////////
	static public final byte[] zip(byte[] b) throws IOException {
		ByteArrayOutputStream baos = ObjPool
				.borrowObject(ByteArrayOutputStream.class);
		try {
			GZIPOutputStream gos = new GZIPOutputStream(baos);
			gos.write(b);
			gos.finish();
			return baos.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			ObjPool.returnObject(baos);
		}
	}

	static public final byte[] unzip(byte[] b) throws IOException {
		ByteArrayOutputStream baos = ObjPool
				.borrowObject(ByteArrayOutputStream.class);
		try {
			int times = 1000;
			byte[] buff = new byte[4 * 1024];
			InputStream bais = newStream(b);
			GZIPInputStream gis = new GZIPInputStream(bais);
			while (true) {
				if (times-- <= 0)
					break;
				int len = gis.read(buff);
				if (len <= 0)
					break;
				baos.write(buff, 0, len);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			ObjPool.returnObject(baos);
		}
	}

	static public final byte[] unzip(byte[] b, int srcLen) throws IOException {
		byte[] buff = new byte[srcLen];
		InputStream bais = newStream(b);
		GZIPInputStream gis = new GZIPInputStream(bais);
		gis.read(buff);
		return buff;
	}

	// ///////////////////////////////////////////////////
	static public final long now() {
		return System.currentTimeMillis();
	}

	static public final String tFmt(long ms) {
		return DateEx.format_YMDHms(ms);
	}

	static public final String tFmt(Date d) {
		return DateEx.format_YMDHms(d);
	}

	static public final String s(String s, Object... args) {
		return String.format(s, args);
	}

	static public final void s(StringBuffer sb, String s, Object... args) {
		String s2 = String.format(s, args);
		sb.append(s2);
	}

	static public final String sn(String s, Object... args) {
		return String.format(s + "\r\n", args);
	}

	static public final void sn(StringBuffer sb, String s, Object... args) {
		s(sb, s + "\r\n", args);
	}

	static public final String fmt(String s, Object... args) {
		return String.format(s, args);
	}

	static public final String format(String s, Object... args) {
		return String.format(s, args);
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

	// 自动识别是否带小数
	static public final String n2sn(long i) {
		if (i < 1000)
			return i + "";
		if (i < 1000 * 10)
			return String.format("%.1fK", ((double) i / 1000));
		if (i < 1000 * 1000)
			return String.format("%.0fW", ((double) i / 10000));
		if (i < 1000 * 1000 * 1000)
			return String.format("%.0fM", ((double) i / (1000 * 1000)));
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

	// ///////////////////////////////////////////////////
	// 计算两点间距离
	public final static int distance(Point a, Point b) {
		int x1 = a.x;
		int y1 = a.y;
		int x2 = b.x;
		int y2 = b.y;
		return distance(x1, y1, x2, y2);
	}

	// 计算直角距离
	public final static int distance90Angle(Point a, Point b) {
		int x1 = a.x;
		int y1 = a.y;
		int x2 = b.x;
		int y2 = b.y;
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

	public final static int distance90Angle(int x1, int y1, int x2, int y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

	public final static int distance(int x1, int y1, int x2, int y2) {
		double v = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		return (int) v;
	}

	static public final double angle(int x1, int y1, int x2, int y2) {
		int x = Math.abs(x1 - x2);
		int y = Math.abs(y1 - y2);
		double z = Math.sqrt(x * x + y * y);
		return (Math.asin(y / z) / Math.PI * 180);// 最终角度
	}

	// ///////////////////////////////////////////////////
	// 计算两点间距离(直线，直角)
	public final static int distanceXy(int x1, int y1, int x2, int y2) {
		double v = Math.abs(x1 - x2) + Math.abs(y1 - y2);
		return (int) v;
	}

	// 周围4个点，按远近关系排序
	public final static List<Point> point4(final Point f, final Point t) {
		List<Point> p4 = new ArrayList<Point>();
		p4.add(new Point(t.x - 1, t.y));
		p4.add(new Point(t.x + 1, t.y));
		p4.add(new Point(t.x, t.y - 1));
		p4.add(new Point(t.x, t.y + 1));
		// 按距离排序
		Collections.sort(p4, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				Integer d1 = distance90Angle(f, o1);
				Integer d2 = distance90Angle(f, o2);
				return d1.compareTo(d2);
			}
		});
		return p4;
	}

	// 近点
	public final static Point near(List<Point> p4) {
		return p4.get(1);
	}

	// 远点
	public final static Point far(List<Point> p4) {
		return p4.get(p4.size() - 1);
	}

	// 到达目标的最远点
	public final static Point p2(final Point f, final Point t, final int d) {
		int x2 = f.x;
		int y2 = f.y;

		int dist = distance90Angle(f, t);
		if (dist < d)
			return new Point(t.x, t.y);

		int d2 = d;
		int mx = Math.abs(t.x - f.x);
		int my = Math.abs(t.y - f.y);

		if (mx > my) { // 先走x边
			if (d2 > 0) {
				if (f.x > t.x)
					x2 = d2 > mx ? f.x - mx : f.x - d2;
				else
					x2 = d2 > mx ? f.x + mx : f.x + d2;
				d2 -= mx;
			}

			if (d2 > 0) {
				if (f.y > t.x)
					y2 = d2 > my ? f.y - my : f.y - d2;
				else
					y2 = d2 > my ? f.y + my : f.y + d2;
				d2 -= my;
			}
		} else { // 先走y边
			if (d2 > 0) {
				if (f.y > t.y) {
					y2 = d2 > my ? f.y - my : f.y - d2;
				} else {
					y2 = d2 > my ? f.y + my : f.y + d2;
				}
				d2 -= d2 > my ? my : d2;
			}

			if (d2 > 0) {
				if (f.x > t.x)
					x2 = d2 > mx ? f.x - mx : f.x - d2;
				else
					x2 = d2 > mx ? f.x + mx : f.x + d2;
				d2 -= d2 > mx ? mx : d2;
			}
		}

		Point p = new Point(x2, y2);
		return p;
	}

	public final static Point move2d(final Point f, final Point t, final int d,
			final int range, List<Point> occupys) {
		if (f == null || t == null)
			return null;
		if (occupys == null)
			occupys = new ArrayList<Point>();

		List<Point> p4 = point4(f, t);

		// 排除不能到达的点
		Iterator<Point> it = p4.iterator();
		while (it.hasNext()) {
			int dist = distance90Angle(f, it.next());
			if (dist > d)
				it.remove();
		}

		// 如果没有点了则找一个能够到达的最近的点
		if (p4.isEmpty()) {
			p4.add(p2(f, t, d));
		}

		if (occupys.isEmpty())
			return p4.get(0);

		// 找出没有船的点
		for (Point p : occupys) {
			if (intersectedPoints(occupys, p)) {
				continue;
			}
			return p;
		}

		// 没有找到，继续找新点
		return move2d(f, t, d - 1, range, occupys);
	}

	// 直线上的所有点
	public final static List<Point> points2(int x1, int y1, int x2, int y2) {
		List<Point> result = newList();
		Point a = new Point(x1, y1);
		Point b = new Point(x2, y2);
		if (x1 > x2) {
			Point x = a;
			a = b;
			b = x;
		}
		Set<String> e = newSet();
		// 循环x坐标
		for (int i = a.x + 1; i < b.x; i++) {
			// 计算斜率
			double k = ((double) (a.y - b.y)) / (a.x - b.x);
			// 根据斜率，计算y坐标
			double y = k * (i - a.x) + a.y;
			// 简单判断一下y是不是整数
			// double d = y - (int) y;
			int ey = (int) y;
			String key = i + "," + ey;
			if (e.contains(key))
				continue;
			e.add(key);
			result.add(new Point(i, ey));
			// if (0.001 > d && d > -0.001) {
			// // Console.Write("点的坐标：{0},{1}", i, y);
			// result.add(new Point(i, (int) y));
			// }
		}
		return result;
	}

	// 计算百分率
	static public final int percent(double v, double max) {
		if (v <= 0 || max <= 0)
			return 0;
		int r = (int) (v * 100 / max);
		return r > 100 ? 100 : r;
	}

	// ///////////////////////////////////////////////////
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

	// ///////////////////////////////////////////////////
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

	// ///////////////////////////////////////////////////

	static public final String trim(String o) {
		if (o == null)
			return "";
		return o.trim();
	}

	static public final boolean isNull(Object o) {
		return o == null;
	}

	static public final boolean isNull(String o) {
		if (o == null)
			return true;
		o = o.trim();
		return o.isEmpty();
	}

	static public final boolean isEmpty(byte[] o) {
		return o == null || o.length <= 0;
	}

	static public final boolean isEmpty(List o) {
		return o == null || o.isEmpty();
	}

	static public final boolean isEmpty(Map o) {
		return o == null || o.isEmpty();
	}

	static public final boolean isEmpty(String o) {
		return o == null || o.isEmpty();
	}

	static public final boolean notNull(Object o) {
		return o != null;
	}

	static public final boolean notEmpty(byte[] o) {
		return o != null && o.length > 0;
	}

	static public final boolean notEmpty(List o) {
		return o != null && !o.isEmpty();
	}

	static public final boolean notEmpty(Map o) {
		return o != null && !o.isEmpty();
	}

	static public final boolean notEmpty(String o) {
		return o != null && !o.isEmpty();
	}

	static public final String e2s(Throwable e) {
		return e2s(e, null, new Object[0]);
	}

	static public final String e2s(Throwable e, Object obj) {
		return e2s(e, String.valueOf(obj), new Object[0]);
	}

	static public final String e2s(Throwable e, String fmt, Object... args) {
		return ExceptionEx.e2s(e, fmt, args);
	}

	// ///////////////////////////////////////////////////
	static public final ScheduledExecutorService newScheduledThreadPool(
			String name, int n) {
		return ThreadEx.newScheduledPool(name, n);
	}

	static public final ScheduledFuture<?> scheduled(
			ScheduledExecutorService threadPool, Runnable r, Date d) {
		return SchedulerEx.timeScheduled(threadPool, r, d);
	}

	static public final ScheduledFuture<?> scheduledFixedDelay(
			ScheduledExecutorService threadPool, Runnable r, Date d, long delay) {
		return SchedulerEx.timeScheduledFixedDelay(threadPool, r, d, delay);
	}

	// 确定时分秒，每日执行
	static public final ScheduledFuture<?> scheduledEveryDay(
			ScheduledExecutorService threadPool, Runnable r, int hour,
			int minute, int sec) {
		return SchedulerEx.timeScheduledEveryDay(threadPool, r, hour, minute,
				sec);
	}

	// 定时执行
	static public final ScheduledFuture<?> scheduled8FixedRate(
			ScheduledExecutorService threadPool, Runnable r, long initialDelay,
			long delay) {
		return SchedulerEx.scheduledFixedRateMS(threadPool, r, initialDelay,
				delay);
	}

	static public final Map getMap(Map map, Object key) {
		return MapEx.getMap(map, key);
	}

	static public final List getList(Map ps, Object key) {
		if (MapEx.isEmpty(ps))
			return null;
		return MapEx.getList(ps, key);
	}

	static public final String getString(Map ps, String key) {
		if (MapEx.isEmpty(ps))
			return "";
		return MapEx.getString(ps, key);
	}

	static public final boolean getBool(Map ps, String key) {
		if (MapEx.isEmpty(ps))
			return false;
		return MapEx.getBoolean(ps, key);
	}

	static public final int getInt(Map ps, String key) {
		if (MapEx.isEmpty(ps))
			return 0;
		return MapEx.getInt(ps, key);
	}

	static public final double getDouble(Map ps, String key) {
		if (MapEx.isEmpty(ps))
			return 0;
		return MapEx.getDouble(ps, key);
	}

	// //////////////////////////////////////////////
	static public final String pinyin(String s) {
		return PinYin.pinYin(s);
	}

	static public final String getShortPinYin(String s) {
		return PinYin.getShortPinYin(s);
	}

	static public final String upperFirst(String s) {
		return StrEx.upperFirst(s);
	}

	static public final String py(String s) {
		return PinYin.shortPinYin(s);
	}

	static public final byte[] readFully(File f) {
		return FileRw.readFully(f);
	}

	static public final byte[] readStream(InputStream is) throws IOException {
		return B2InputStream.readStream(is);
	}

	static public final String readFully(File f, String charset)
			throws IOException {
		byte[] b = readFully(f);
		if (b == null)
			return "";
		return new String(b, charset);
	}

	// 序列化
	static public final byte[] serialization(final Object obj) throws Exception {
		if (obj == null)
			return new byte[0];
		try (ByteOutStream out = ByteOutPool.borrowObject();
				ObjectOutputStream oos = new ObjectOutputStream(out);) {
			oos.writeObject(obj);
			return out.toByteArray();
		} catch (Exception e) {
			throw e;
		}
	}

	// 反序列化
	static public final Object deserialization(final byte[] b) throws Exception {
		if (b == null || b.length <= 0)
			return null;
		try (ByteInStream in = ByteInPool.borrowObject(b);
				ObjectInputStream ois = new ObjectInputStream(in);) {
			return ois.readObject();
		} catch (Exception e) {
			throw e;
		}
	}

	static public final String getAppRoot() {
		return System.getProperty("user.dir");
	}

	static public final String getAppPath() {
		return TkitBase.class.getClassLoader().getResource("").getPath();
	}

	static public final String nStr(long n) {
		boolean isNegative = n < 0;
		n = Math.abs(n);
		String vRet = "";
		if (n >= 1000000) {
			vRet = (n / 1000000) + "M";
		} else if (n >= 10000) {
			vRet = (n / 10000) + "W";
		} else if (n >= 1000) {
			vRet = (n / 10000) + "K";
		} else {
			vRet = n + "";
		}
		return isNegative ? ("-" + vRet) : vRet;
	}
}
