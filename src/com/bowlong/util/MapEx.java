package com.bowlong.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.collections4.map.StaticBucketMap;

import com.alibaba.fastjson.JSON;
import com.bowlong.lang.NumEx;
import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MapEx {

	public static final Map singletonEmptyMap = new Hashtable();

	public static final Map singletonEmptyMap() {
		return singletonEmptyMap;
	}
	
	public static final Map newMap() {
		return new Hashtable();
	}

	public static final <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static final Hashtable newHashtable() {
		return new Hashtable();
	}

	public static final <K, V> Map<K, V> newSortedMap() {
		return Collections.synchronizedMap(new TreeMap<K, V>());
	}

	public static final <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

	public static final HashedMap newHashedMap() {
		return new HashedMap();
	}

	public static final LinkedMap newLinkedMap() {
		return new LinkedMap();
	}

	public static final TreeBidiMap newTreeBidiMap() {
		return new TreeBidiMap();
	}

	public static final HashBag newHashBag() {
		return new HashBag();
	}

	public static final LRUMap newLRUMap() {
		return new LRUMap();
	}

	public static final MultiKeyMap newMultiKeyMap() {
		return new MultiKeyMap();
	}

	public static final MultiValueMap newMultiValueMap() {
		return new MultiValueMap();
	}

	public static final StaticBucketMap newStaticBucketMap() {
		return new StaticBucketMap();
	}

	public static final boolean isEmpty(Map map) {
		return (map == null || map.isEmpty());
	}

	public static final boolean notEmpty(Map map) {
		return (map != null && !map.isEmpty());
	}

	static public final void clear(Map map) {
		if (map == null)
			return;
		map.clear();
	}

	static public final void clearNull(Map map) {
		if (map == null)
			return;
		map.clear();
		map = null;
	}

	/*** 清空并创建对象 **/
	static public final Map clear4Map(Map map) {
		if (map == null) {
			map = new HashMap();
			return map;
		}
		map.clear();
		return map;
	}

	/*** 清空并创建对象 **/
	static public final <K, V> Map<K, V> clear4MapKV(Map<K, V> map) {
		return (Map<K, V>) clear4Map(map);
	}

	public static final <T> T copyValue(Map from, Map to, Object key) {
		T v = get(from, key);
		if (v == null)
			return null;
		to.put(key, v);
		return v;
	}

	public static final <T> T get(Map map, Object key) {
		return (T) map.get(key);
	}

	public static final boolean getBoolean(Map map, Object key) {
		Object v = map.get(key);
		if (v == null)
			return false;
		if (v instanceof Boolean) {
			return ((Boolean) v).booleanValue();
		} else if (v instanceof Integer) {
			return ((Integer) v) <= 0 ? false : true;
		} else if (v instanceof Long) {
			return ((Long) v) <= 0 ? false : true;
		} else if (v instanceof String) {
			if ("0".equals(v)) {
				return false;
			}
			if ("1".equals(v)) {
				return true;
			}
			return NumEx.stringToBool((String) v);
		}
		return false;
	}

	public static final byte getByte(Map map, Object key) {
		Object v = map.get(key);
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).byteValue();
		} else if (v instanceof String) {
			return NumEx.stringToByte((String) v);
		}
		return 0;
	}

	public static final short getShort(Map map, Object key) {
		Object v = map.get(key);
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).shortValue();
		} else if (v instanceof Short) {
			return ((Short) v).shortValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).shortValue();
		} else if (v instanceof Long) {
			return ((Long) v).shortValue();
		} else if (v instanceof Float) {
			return ((Float) v).shortValue();
		} else if (v instanceof Double) {
			return ((Double) v).shortValue();
		} else if (v instanceof String) {
			return NumEx.stringToShort((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).shortValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).shortValue();
		}
		return 0;
	}

	public static final int getInt(Map map, Object key) {
		Object v = map.get(key);
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).intValue();
		} else if (v instanceof Short) {
			return ((Short) v).intValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).intValue();
		} else if (v instanceof Long) {
			return ((Long) v).intValue();
		} else if (v instanceof Float) {
			return ((Float) v).intValue();
		} else if (v instanceof Double) {
			return ((Double) v).intValue();
		} else if (v instanceof String) {
			return NumEx.stringToInt((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).intValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).intValue();
		}
		return 0;
	}

	public static final long getLong(Map map, Object key) {
		Object v = map.get(key);
		if (v == null)
			return 0;
		if (v instanceof Byte) {
			return ((Byte) v).longValue();
		} else if (v instanceof Short) {
			return ((Short) v).longValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).longValue();
		} else if (v instanceof Long) {
			return ((Long) v).longValue();
		} else if (v instanceof Float) {
			return ((Float) v).longValue();
		} else if (v instanceof Double) {
			return ((Double) v).longValue();
		} else if (v instanceof String) {
			return NumEx.stringToLong((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).longValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).longValue();
		}
		return 0;
	}

	public static final float getFloat(Map map, Object key) {
		Object v = map.get(key);
		if (v == null)
			return (float) 0.0;
		if (v instanceof Byte) {
			return ((Byte) v).floatValue();
		} else if (v instanceof Short) {
			return ((Short) v).floatValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).floatValue();
		} else if (v instanceof Long) {
			return ((Long) v).floatValue();
		} else if (v instanceof Float) {
			return ((Float) v).floatValue();
		} else if (v instanceof Double) {
			return ((Double) v).floatValue();
		} else if (v instanceof String) {
			return NumEx.stringToFloat((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).floatValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).floatValue();
		}
		return 0;
	}

	public static final double getDouble(Map map, Object key) {
		Object v = map.get(key);
		if (v == null)
			return 0.0;
		if (v instanceof Byte) {
			return ((Byte) v).doubleValue();
		} else if (v instanceof Short) {
			return ((Short) v).floatValue();
		} else if (v instanceof Integer) {
			return ((Integer) v).doubleValue();
		} else if (v instanceof Long) {
			return ((Long) v).doubleValue();
		} else if (v instanceof Float) {
			return ((Float) v).doubleValue();
		} else if (v instanceof Double) {
			return ((Double) v).doubleValue();
		} else if (v instanceof String) {
			return NumEx.stringToDouble((String) v);
		} else if (v instanceof BigInteger) {
			return ((BigInteger) v).doubleValue();
		} else if (v instanceof BigDecimal) {
			return ((BigDecimal) v).doubleValue();
		}
		return 0;
	}

	public static final BigInteger getBigInteger(Map map, Object key) {
		return (BigInteger) map.get(key);
	}

	public static final BigDecimal getBigDecimal(Map map, Object key) {
		return (BigDecimal) map.get(key);
	}

	public static final String getStringNoTrim(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return "";
		else if (obj instanceof String)
			return (String) obj;

		return String.valueOf(obj);
	}

	public static final String getString(Map map, Object key) {
		String val = getStringNoTrim(map, key);
		return val.trim();
	}

	public static final Date getDate(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return (Date) obj;
		else if (obj instanceof NewDate)
			return (NewDate) obj;
		else if (obj instanceof java.sql.Date)
			return new Date(((java.sql.Date) obj).getTime());
		else if (obj instanceof java.sql.Timestamp)
			return new Date(((java.sql.Timestamp) obj).getTime());
		else if (obj instanceof Integer)
			return new Date((Integer) obj);
		else if (obj instanceof Long)
			return new Date((Long) obj);
		else if (obj instanceof String)
			return NewDate.parse2((String) obj, DateEx.fmt_yyyy_MM_dd_HH_mm_ss);

		return null;
	}

	public static final NewDate getNewDate(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return null;
		if (obj instanceof NewDate)
			return (NewDate) obj;
		else if (obj instanceof Date)
			return new NewDate((Date) obj);
		else if (obj instanceof java.sql.Date)
			return new NewDate(((java.sql.Date) obj));
		else if (obj instanceof java.sql.Timestamp)
			return new NewDate(((java.sql.Timestamp) obj));
		else if (obj instanceof Long)
			return new NewDate((Long) obj);
		else if (obj instanceof String)
			return NewDate.parse2((String) obj, DateEx.fmt_yyyy_MM_dd_HH_mm_ss);

		return null;
	}

	public static final byte[] getByteArray(Map map, Object key) {
		return (byte[]) map.get(key);
	}

	public static final Map getMap(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return newMap();

		if (obj instanceof NewMap)
			return (NewMap) obj;
		else if (obj instanceof Map)
			return (Map) obj;

		return newMap();
	}

	public static final NewMap getNewMap(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return new NewMap();

		if (obj instanceof NewMap)
			return (NewMap) obj;
		else if (obj instanceof Map)
			return NewMap.create((Map) obj);

		return new NewMap();
	}

	public static final Set getSet(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return new HashSet();

		if (obj instanceof NewSet)
			return (NewSet) map.get(key);
		else if (obj instanceof Set)
			return (Set) map.get(key);

		return new HashSet();
	}

	public static final NewSet getNewSet(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return new NewSet();

		if (obj instanceof NewSet)
			return (NewSet) obj;
		else if (obj instanceof NewSet)
			return NewSet.create((Set) obj);

		return new NewSet();
	}

	public static final List getList(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return new Vector();

		if (obj instanceof NewList)
			return (NewList) obj;
		else if (obj instanceof List)
			return (List) obj;

		return new Vector();
	}

	public static final NewList getNewList(Map map, Object key) {
		Object obj = map.get(key);
		if (obj == null)
			return new NewList();

		if (obj instanceof NewList)
			return (NewList) obj;
		else if (obj instanceof List)
			return NewList.create((List) obj);

		return new NewList();
	}

	public static final Map toMap(List l) {
		Map ret = newMap();
		if (l == null || l.isEmpty())
			return ret;
		int len = l.size();
		for (int i = 0; i < len; i++) {
			Object o = l.get(i);
			if (o == null)
				continue;
			ret.put(i, o);
		}
		return ret;
	}

	public static final Map toHashMap(Map map) {
		Map ret = newHashMap();
		ret.putAll(map);
		return ret;
	}

	public static final Map toHashtable(Map map) {
		Map ret = newHashtable();
		ret.putAll(map);
		return ret;
	}

	public static final Map toConcurrentHashMap(Map map) {
		Map ret = newConcurrentHashMap();
		ret.putAll(map);
		return ret;
	}

	public static final Map toMap(Object[] array) {
		if (array == null) {
			return null;
		}
		final Map map = new HashMap((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			Object object = array[i];
			if (object instanceof Map.Entry) {
				Map.Entry entry = (Map.Entry) object;
				map.put(entry.getKey(), entry.getValue());
			} else if (object instanceof Object[]) {
				Object[] entry = (Object[]) object;
				if (entry.length < 2) {
					throw new IllegalArgumentException("Array element " + i
							+ ", '" + object + "', has a length less than 2");
				}
				map.put(entry[0], entry[1]);
			} else {
				throw new IllegalArgumentException("Array element " + i + ", '"
						+ object
						+ "', is neither of type Map.Entry nor an Array");
			}
		}
		return map;
	}

	public static final Map toHashMap(Object[] array) {
		Map m = toMap(array);
		return toHashMap(m);
	}

	public static final Map toHashtable(Object[] array) {
		Map m = toMap(array);
		return toHashtable(m);
	}

	public static final Map toConcurrentHashMap(Object[] array) {
		Map m = toMap(array);
		return toConcurrentHashMap(m);
	}

	public static final Map toMap(String s) {
		return propertiesToMap(s);
	}

	static public final Map toMapByJson(String json) {
		try {
			if (StrEx.isEmptyTrim(json))
				return newMap();
			json = json.replaceAll("\\\\", "");
			return (Map) JSON.parse(json);
		} catch (Exception e) {
			return newMap();
		}
	}

	static public final Map<String, String> toMapKV(Map map) {
		Map<String, String> ret = newHashMap();
		if (isEmpty(map))
			return ret;
		Object keyObj, valObj;
		String key, val;
		for (Entry<Object, Object> entry : ((Map<Object, Object>) map)
				.entrySet()) {
			keyObj = entry.getKey();
			valObj = entry.getValue();
			if (keyObj == null || valObj == null)
				continue;

			key = keyObj.toString();
			val = valObj.toString();
			ret.put(key, val);
		}
		return ret;
	}

	static public final Map<String, Integer> toMapKVInt(Map map) {
		Map<String, Integer> ret = newHashMap();
		if (isEmpty(map))
			return ret;
		Object keyObj, valObj;
		String key;
		Integer val;

		for (Entry<Object, Object> entry : ((Map<Object, Object>) map)
				.entrySet()) {
			keyObj = entry.getKey();
			valObj = entry.getValue();
			if (keyObj == null || valObj == null)
				continue;

			key = keyObj.toString();
			val = NumEx.stringToInt(valObj.toString());
			ret.put(key, val);
		}
		return ret;
	}

	static public final Map<Integer, String> toMapKIntV(Map map) {
		Map<Integer, String> ret = newHashMap();
		if (isEmpty(map))
			return ret;
		Object keyObj, valObj;
		Integer key;
		String val;

		for (Entry<Object, Object> entry : ((Map<Object, Object>) map)
				.entrySet()) {
			keyObj = entry.getKey();
			valObj = entry.getValue();
			if (keyObj == null || valObj == null)
				continue;

			key = NumEx.stringToInt(keyObj.toString());
			val = valObj.toString();
			ret.put(key, val);
		}
		return ret;
	}

	static public final Map<Integer, Integer> toMapKIntVInt(Map map) {
		Map<Integer, Integer> ret = newHashMap();
		if (isEmpty(map))
			return ret;
		Object keyObj, valObj;
		Integer key;
		Integer val;
		for (Entry<Object, Object> entry : ((Map<Object, Object>) map)
				.entrySet()) {
			keyObj = entry.getKey();
			valObj = entry.getValue();
			if (keyObj == null || valObj == null)
				continue;

			key = NumEx.stringToInt(keyObj.toString());
			val = NumEx.stringToInt(valObj.toString());
			ret.put(key, val);
		}
		return ret;
	}

	public static final Map propertiesToMap(String s) {
		Properties p = new Properties();
		try {
			StringReader sr = new StringReader(s);
			BufferedReader br = new BufferedReader(sr);
			p.load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public static final Properties load(File f) throws Exception {
		try (FileInputStream fis = new FileInputStream(f);) {
			return load(fis);
		}
	}

	public static final Properties load(InputStream is) throws Exception {
		InputStreamReader isr = new InputStreamReader(is);
		return load(isr);
	}

	public static final Properties load(Reader br) throws Exception {
		Properties p = new Properties();
		try {
			p.load(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	public static final void save(File f, Properties p) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(f);) {
			save(fos, p);
		}
	}

	public static final void save(OutputStream os, Properties p)
			throws Exception {
		p.store(os, "");
	}

	public static final String formatString(final Map m) {
		int depth = 1;
		return formatString(m, depth);
	}

	public static final String formatString(final Map m, int depth) {
		int p = 0;
		int size = m.size();
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("{\n");
			Set<Entry> entrys = m.entrySet();
			Object k, v;
			for (Entry e : entrys) {
				k = e.getKey();
				v = e.getValue();
				for (int i = 0; i < depth; i++)
					sb.append("    ");
				if (k instanceof String) {
					sb.append("\"").append(k).append("\"");
				} else {
					sb.append(k);
				}
				sb.append(":");
				if (v instanceof Map) {
					sb.append(formatString((Map) v, depth + 1));
				} else if (v instanceof List) {
					sb.append(ListEx.formatString((List) v, depth + 1));
				} else if (v instanceof String) {
					sb.append("\"").append(v).append("\"");
				} else {
					sb.append(v);
				}
				p++;
				if (p < size) {
					sb.append(",");
				}
				sb.append("\n");
			}
			for (int i = 1; i < depth; i++)
				sb.append("    ");
			sb.append("}");
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	static public final String toStr4Json(Map map) {
		if (isEmpty(map))
			return "{}";
		return JSON.toJSONString(map);
	}

	public static <T> T getKey(Map map) throws Exception {
		if (map == null || map.isEmpty())
			return null;
		Entry<Object, Object> E = ((Map<Object, Object>) map).entrySet()
				.iterator().next();
		return (T) E.getKey();
	}

	public static <T> T getValue(Map map) throws Exception {
		if (map == null || map.isEmpty())
			return null;
		Entry<Object, Object> E = ((Map<Object, Object>) map).entrySet()
				.iterator().next();
		return (T) E.getValue();
	}

	public static Map toMapKv(Object... objs) {
		Map r2 = new HashMap();
		return putKvs4Map(r2, objs);
	}

	static public Map putKvs4Map(final Map r2, final Object... objs) {
		if (objs == null || objs.length <= 0) {
			return r2;
		}

		int count = objs.length;
		Object key, val;
		for (int i = 0; i < count; i++) {
			key = objs[i];
			i++;
			if (i >= count) {
				return r2;
			}
			val = objs[i];
			if (key == null || val == null) {
				continue;
			}

			r2.put(key, val);
		}
		return r2;
	}

	static public NewMap putKvs4NewMap(final NewMap r2, final Object... objs) {
		return (NewMap) putKvs4Map(r2, objs);
	}

	static public Map<String, Object> toMap(Map origin) {
		if (isEmpty(origin))
			return null;
		Object keyObj, valObj;
		String key;
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : ((Map<Object, Object>) origin)
				.entrySet()) {
			keyObj = entry.getKey();
			valObj = entry.getValue();
			if (keyObj == null || valObj == null)
				continue;
			key = keyObj.toString();
			ret.put(key, valObj);
		}
		return ret;
	}
	
	// 清除空的，包含数字小于0的
	static public Map toMapClear(Map map) {
		return toMapClear(map,true);
	}
	
	static public Map toMapClear(Map map,boolean isLessZeroNum) {
		List listKeys = new ArrayList();
		listKeys.addAll(map.keySet());
		int lens = listKeys.size();
		for (int i = 0; i < lens; i++) {
			Object key = listKeys.get(i);
			if(key == null){
				map.remove(key);
				continue;
			}
			
			Object val = map.get(key);
			if (val == null) {
				map.remove(key);
				continue;
			}
			
			String valStr = val.toString().trim();
			if (valStr.isEmpty()) {
				map.remove(key);
				continue;
			}
			
			if(!isLessZeroNum)
				continue;
			
			if (val instanceof Double) {
				double v = (double) val;
				if (v <= 0) {
					map.remove(key);
				}
			} else if (val instanceof Float) {
				Float v = (Float) val;
				if (v <= 0) {
					map.remove(key);
				}
			}else if (val instanceof Long) {
				Long v = (Long) val;
				if (v <= 0) {
					map.remove(key);
				}
			}else if (val instanceof Integer) {
				int v = (int) val;
				if (v <= 0) {
					map.remove(key);
				}
			}
		}
		return map;
	}

	public static void main(String[] args) {
		// Map colorMap = toMap(new Object[][] { { "RED", 0xFF0000 }, { "GREEN",
		// 0x00FF00 }, { "BLUE", 0x0000FF } });
		//
		// System.out.println(colorMap);
		//
		// int[] vars = { 1, 2, 3, 4, 5, 6 };
		//
		// String s = "";
		// s = setInt(s, "1", 111);
		// s = setString(s, "2", "222");
		// s = setString(s, 3, "333");
		// s = setString(s, "4", "444");
		// s = setInt(s, 5, 555);
		// s = setInt(s, 6, vars, ",");
		// System.out.println(s);
		//
		// System.out.println(getInt(s, "1"));
		// System.out.println(getInt(s, "2"));
		// System.out.println(getInt(s, 3));
		// System.out.println(getInt(s, "4"));
		// System.out.println(getInt(s, "5"));
		// int[] vars2 = getInt(s, 6, ",");
		// for (int i : vars2) {
		// System.out.print(i + ",");
		// }
		//
		// System.out.println("-------------------");
		// Iterator it = iterator(s);
		// while (it.hasNext()) {
		// String key = (String) it.next();
		// String var = getString(s, key);
		// System.out.println(key + " = " + var);
		// }

	}
}
