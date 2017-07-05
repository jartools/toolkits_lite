package com.bowlong.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import com.bowlong.lang.NumEx;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class NewCpWrList<E> extends CopyOnWriteArrayList<E> {
	public static NewCpWrList create() {
		return new NewCpWrList();
	}

	public static NewCpWrList newly() {
		return new NewCpWrList();
	}

	public static <E> NewCpWrList<E> create(List<E> list) {
		if (list instanceof NewCpWrList)
			return (NewCpWrList) list;

		NewCpWrList ret = new NewCpWrList();
		ret.addAll(list);
		return ret;
	}

	public static <E> NewCpWrList<E> newly(List<E> list) {
		return create(list);
	}

	public static <E> NewCpWrList<E> create(Collection<E> c) {
		NewCpWrList<E> ret = new NewCpWrList<E>();
		ret.addAll(c);
		return ret;
	}

	public static <E> NewCpWrList<E> newly(Collection<E> c) {
		return create(c);
	}

	public static NewCpWrList create(Object... objects) {
		NewCpWrList ret = new NewCpWrList();
		for (Object obj : objects) {
			ret.add(obj);
		}
		return ret;
	}

	public static NewCpWrList newly(Object... objects) {
		return create(objects);
	}

	public static NewCpWrList newly(Object e) {
		return create().addE(e);
	}

	// //////////////
	public NewCpWrList() {
	}

	public NewCpWrList(int fromIndex, int toIndex) {
		subList(fromIndex, toIndex);
	}

	// //////////////

	public NewCpWrList<E> addE(E e) {
		this.add(e);
		return this;
	}

	public NewCpWrList<E> addE(List<E> list) {
		this.addAll(list);
		return this;
	}

	public NewCpWrList<E> addE(Object... objects) {
		for (Object obj : objects) {
			this.add((E) obj);
		}
		return this;
	}

	public boolean getBoolean(int i) {
		Object obj = get(i);
		if (obj == null)
			return false;

		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue();
		else if (obj instanceof Integer)
			return ((Integer) obj).intValue() <= 0 ? false : true;
		else if (obj instanceof String)
			return NumEx.stringToBool((String) obj);

		return false;
	}

	public int getInt(int i) {
		Object obj = get(i);
		if (obj == null)
			return 0;

		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue() ? 1 : 0;
		else if (obj instanceof Integer)
			return ((Integer) obj).intValue();
		else if (obj instanceof String)
			return NumEx.stringToInt((String) obj);
		else if (obj instanceof Long)
			return ((Long) obj).intValue();
		else if (obj instanceof Float)
			return ((Float) obj).intValue();
		else if (obj instanceof Double)
			return ((Double) obj).intValue();
		else if (obj instanceof BigInteger)
			return ((BigInteger) obj).intValue();
		else if (obj instanceof BigDecimal)
			return ((BigDecimal) obj).intValue();

		return 0;
	}

	public long getLong(int i) {
		Object obj = get(i);
		if (obj == null)
			return 0;

		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue() ? 1 : 0;
		else if (obj instanceof Integer)
			return ((Integer) obj).longValue();
		else if (obj instanceof String)
			return NumEx.stringToLong((String) obj);
		else if (obj instanceof Long)
			return ((Long) obj).longValue();
		else if (obj instanceof Float)
			return ((Float) obj).longValue();
		else if (obj instanceof Double)
			return ((Double) obj).longValue();
		else if (obj instanceof BigInteger)
			return ((BigInteger) obj).longValue();
		else if (obj instanceof BigDecimal)
			return ((BigDecimal) obj).longValue();

		return 0;
	}

	public double getDouble(int i) {
		Object obj = get(i);
		if (obj == null)
			return 0.0;

		if (obj instanceof Boolean)
			return ((Boolean) obj).booleanValue() ? 1.0 : 0.0;
		else if (obj instanceof Integer)
			return ((Integer) obj).doubleValue();
		else if (obj instanceof String)
			return NumEx.stringToDouble((String) obj);
		else if (obj instanceof Long)
			return ((Long) obj).doubleValue();
		else if (obj instanceof Float)
			return ((Float) obj).doubleValue();
		else if (obj instanceof Double)
			return ((Double) obj).doubleValue();
		else if (obj instanceof BigInteger)
			return ((BigInteger) obj).doubleValue();
		else if (obj instanceof BigDecimal)
			return ((BigDecimal) obj).doubleValue();

		return 0;
	}

	public String getString(int i) {
		Object obj = get(i);
		if (obj == null)
			return "";
		if (obj instanceof String)
			return (String) obj;

		return String.valueOf(obj);
	}

	public Date getDate(int i) {
		Object obj = get(i);
		if (obj == null)
			return null;
		if (obj instanceof NewDate)
			return (NewDate) obj;
		else if (obj instanceof Date)
			return (Date) obj;
		else if (obj instanceof java.sql.Date)
			return new Date(((java.sql.Date) obj).getTime());
		else if (obj instanceof java.sql.Timestamp)
			return new Date(((java.sql.Timestamp) obj).getTime());
		else if (obj instanceof Long)
			return new Date((Long) obj);
		else if (obj instanceof String)
			return NewDate.parse2((String) obj, DateEx.fmt_yyyy_MM_dd_HH_mm_ss);

		return null;
	}

	public NewDate getNewDate(int i) {
		Object obj = get(i);
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

	public Map getMap(int i) {
		Object obj = get(i);
		if (obj == null)
			return new HashMap();

		if (obj instanceof NewMap)
			return (NewMap) obj;
		else if (obj instanceof Map)
			return (Map) obj;

		return new HashMap();
	}

	public NewMap getNewMap(int i) {
		Object obj = get(i);
		if (obj == null)
			return new NewMap();

		if (obj instanceof NewMap)
			return (NewMap) obj;
		if (obj instanceof Map)
			return NewMap.create((Map) obj);

		return new NewMap();
	}

	public Set getSet(int i) {
		Object obj = get(i);
		if (obj == null)
			return new HashSet();

		if (obj instanceof NewSet)
			return (NewSet) obj;
		else if (obj instanceof Set)
			return (Set) obj;

		return new HashSet();
	}

	public NewSet getNewSet(int i) {
		Object obj = get(i);
		if (obj == null)
			return new NewSet();

		if (obj instanceof NewSet)
			return (NewSet) obj;
		if (obj instanceof Map)
			return NewSet.create((Set) obj);

		return new NewSet();
	}

	public List getList(int i) {
		Object obj = get(i);
		if (obj == null)
			return new ArrayList();

		if (obj instanceof NewCpWrList)
			return (NewCpWrList) obj;
		else if (obj instanceof NewCpWrList)
			return (NewCpWrList) obj;

		return new ArrayList();
	}

	public NewCpWrList getNewList(int i) {
		Object obj = get(i);
		if (obj == null)
			return new NewCpWrList();

		if (obj instanceof NewCpWrList)
			return (NewCpWrList) obj;
		if (obj instanceof List)
			return NewCpWrList.create((List) obj);

		return new NewCpWrList();
	}

	public int pageCount(int pageSize) {
		if (pageSize <= 0)
			return 0;
		int count = size();
		int page = count / pageSize;

		page = count == page * pageSize ? page : page + 1;
		return page;
	}

	public List getPage(int page, int pageSize) {
		if (pageSize <= 0)
			return new NewCpWrList();
		int count = this.size();
		int begin = (page * pageSize);
		int end = (begin + pageSize);
		if (begin > count || begin < 0 || end < 0)
			return new NewCpWrList();
		end = count < end ? count : end;
		if (end <= begin)
			return new NewCpWrList();
		return this.subList(begin, end);
	}

	public List<E> synchronizedList() {
		return Collections.synchronizedList(this);
	}

	public List toList() {
		return this;
	}
}
