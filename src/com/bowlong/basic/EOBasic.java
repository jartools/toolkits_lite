package com.bowlong.basic;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ex扩展类的起源<br/>
 * Origin -> Base(Basic) -> Toolkit(Tools,Helper) -> Manager(Ex)
 * @author Canyon
 * @time 2019-02-14 19:32
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EOBasic extends EOConfig {
	
	static final public <T> T toT(Object obj) {
		return (T) obj;
	}

	static final public byte[] toByteArray(String s, String charset) throws Exception {
		return s.getBytes(charset);
	}

	static final public int hashCode(String s) {
		char[] value = s.toCharArray();
		return hashCode(value);
	}

	static final public int hashCode(char[] value) {
		int hash = 0;
		int count = value.length;
		int offset = 0;

		int i = hash;
		if (i == 0 && count > 0) {
			int j = offset;
			char ac[] = value;
			int k = count;
			for (int l = 0; l < k; l++)
				i = 31 * i + ac[j++];

			hash = i;
		}
		return i;
	}

	static final public int compareTo(Object v1, Object v2) {
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
		} else if (v1 instanceof BigInteger && v2 instanceof BigInteger) {
			BigInteger i1 = (BigInteger) v1;
			BigInteger i2 = (BigInteger) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof BigDecimal && v2 instanceof BigDecimal) {
			BigDecimal i1 = (BigDecimal) v1;
			BigDecimal i2 = (BigDecimal) v2;
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
		} else if (v1 instanceof Date && v2 instanceof Date) {
			Date i1 = (Date) v1;
			Date i2 = (Date) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Timestamp && v2 instanceof Timestamp) {
			Timestamp i1 = (Timestamp) v1;
			Timestamp i2 = (Timestamp) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Time && v2 instanceof Time) {
			Time i1 = (Time) v1;
			Time i2 = (Time) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof String && v2 instanceof String) {
			String i1 = (String) v1;
			String i2 = (String) v2;
			return i1.compareTo(i2);
		} else if (v1 instanceof Enum && v2 instanceof Enum) {
			Enum i1 = (Enum) v1;
			Enum i2 = (Enum) v2;
			return i1.compareTo(i2);
		}
		return 0;
	}

	static final public String trim(String o) {
		if (o == null)
			return "";
		return o.trim();
	}

	static final public boolean isEmpty(String val) {
		return val == null || val.length() <= 0;
	}

	static final public boolean isEmptyTrim(String val) {
		if (val != null)
			val = val.trim();
		return isEmpty(val);
	}

	static final public boolean isNull(Object o) {
		return o == null;
	}

	static final public boolean isNull(String o) {
		return o == null;
	}

	static final public boolean isEmpty(byte[] o) {
		return o == null || o.length <= 0;
	}

	static final public boolean notNull(Object o) {
		return o != null;
	}

	static final public boolean notEmpty(byte[] o) {
		return o != null && o.length > 0;
	}

	static final public boolean notEmpty(String o) {
		return o != null && !o.isEmpty();
	}

	static final public boolean isEmpty(String... stres) {
		return (stres == null || stres.length <= 0);
	}

	static public final boolean isEmpty(byte[]... bts) {
		return (bts == null || bts.length <= 0);
	}

	static final public boolean isEmpty(Object... objs) {
		return (objs == null || objs.length <= 0);
	}

	static final public boolean isEmpty(Map o) {
		return o == null || o.isEmpty();
	}

	static final public boolean notEmpty(Map o) {
		return o != null && !o.isEmpty();
	}

	static final public <K, V> Map<K, V> newMap() {
		return new ConcurrentHashMap<K, V>();// 线程安全的,它是HashTable的替代，比HashTable的扩展性更好。
	}

	static final public <K, V> Map<K, V> newMap2() {
		return Collections.synchronizedMap(new HashMap<K, V>());
	}

	static final public <K, V> Map<K, V> newMap3() {
		return new Hashtable<K, V>();// 线程安全的 等价于 newMap2
	}

	static final public <K, V> Map<K, V> newMapT() {
		return new HashMap<K, V>();
	}

	static final public void clear(Map map) {
		if (map == null)
			return;
		map.clear();
	}

	/*** 清空并创建对象 **/
	static final public <K, V> Map<K, V> clearOrNew(Map map) {
		if (map == null) {
			return newMap();
		}
		map.clear();
		return map;
	}

	static final public boolean isEmpty(List o) {
		return o == null || o.isEmpty();
	}

	static final public boolean notEmpty(List o) {
		return o != null && !o.isEmpty();
	}

	static public final <T> List<T> newList() {
		return new CopyOnWriteArrayList<T>(); // 线程性能 : get > add
	}

	static public final <T> List<T> newList2() {
		return Collections.synchronizedList(new ArrayList<T>()); // add > get
	}

	public static final <T> List<T> newListT() {
		return new ArrayList<T>();
	}

	static public final void clear(List ls) {
		if (ls == null)
			return;
		ls.clear();
	}

	/*** 清空并创建对象 **/
	static public final List clearOrNew(List ls) {
		if (ls == null) {
			return newList();
		}
		ls.clear();
		return ls;
	}

	static final public <E> Queue<E> newQueue() {
		return new ConcurrentLinkedQueue<E>();
	}

	static final public <E> Queue<E> newQueueT() {
		return new LinkedList<E>();
	}

	static final public boolean isEmpty(Queue s) {
		return (s == null || s.isEmpty());
	}
	
	static final public <E> Set<E> newSet() {
		return Collections.synchronizedSet(new HashSet<E>());
	}
	
	static final public boolean isEmpty(Set s) {
		return (s == null || s.isEmpty());
	}
}
