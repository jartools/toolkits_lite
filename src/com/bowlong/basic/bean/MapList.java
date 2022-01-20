package com.bowlong.basic.bean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bowlong.basic.ExToolkit;

/**
 * Map 和 List 常用对象<br/>
 * Map -> HashMap<br/>
 * List -> ArrayList
 * 
 * @author Canyon
 * @time 2022-01-19
 */
public class MapList<T> extends ExToolkit {
	private Map<Object, T> map = null;
	private List<T> list = null;
	private boolean isList = false;
	private boolean isSafe = false;

	public List<T> getList() {
		return list;
	}

	public MapList() {
		super();
		this.init();
	}

	public MapList(boolean isList) {
		super();
		this.isList = isList;
		this.init();
	}

	public MapList(boolean isSafe, boolean isList) {
		super();
		this.isSafe = isSafe;
		this.isList = isList;
		this.init();
	}

	protected void init() {
		this.map = this.isSafe ? newMap() : newMapT();
		if (this.isList)
			this.list = this.isSafe ? newList2() : newListT();
	}

	public boolean put(Object key, T obj) {
		if (key == null || obj == null)
			return false;
		T objOld = get(key);
		map.put(key, obj);
		if (list != null) {
			if (objOld != null)
				list.remove(objOld);
			list.add(obj);
		}
		return true;
	}

	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	public boolean containsValue(T obj) {
		return this.map.containsValue(obj);
	}

	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public int size() {
		return this.map.size();
	}

	public void clear() {
		this.map.clear();
		if (this.list != null)
			this.list.clear();
	}

	public T get(Object key) {
		if (key == null)
			return null;
		return map.get(key);
	}

	public T removeByKey(Object key) {
		T obj = get(key);
		if (obj != null) {
			map.remove(key);
			if (list != null)
				list.remove(obj);
		}
		return obj;
	}

	public List<T> getCurrList() {
		List<T> ret = newListT();
		if (this.list != null)
			ret.addAll(this.list);
		else {
			Collection<T> c = null;
			if (!this.map.isEmpty())
				c = this.map.values();
			if (c != null)
				ret.addAll(c);
		}
		return ret;
	}

	public List<T> merge(List<T> src) {
		if (src == null)
			src = newListT();
		List<T> org = this.getCurrList();
		int lens = org.size();
		T it = null;
		for (int i = 0; i < lens; i++) {
			it = org.get(i);
			if (!src.contains(it))
				src.add(it);
		}
		return src;
	}
}
