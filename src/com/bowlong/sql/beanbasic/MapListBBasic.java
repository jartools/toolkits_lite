package com.bowlong.sql.beanbasic;

import java.util.List;

import com.bowlong.basic.bean.MapList;

/**
 * Map 和 List 常用对象<br/>
 * Map -> HashMap<br/>
 * List -> ArrayList
 * 
 * @author Canyon
 * @time 2022-01-19
 */
public class MapListBBasic<T extends BeanBasic> extends MapList<T> {

	public MapListBBasic() {
		super();
	}

	public MapListBBasic(boolean isList) {
		super(isList);
	}

	public boolean add(T obj) {
		long key = obj.getmCKey();
		return this.put(key, obj);
	}

	public boolean put(Object key, T obj) {
		T objOld = this.get(key);
		boolean isState = super.put(key, obj);
		if (isState && objOld != obj)
			Cache.returnObject(objOld);
		return isState;
	}

	public boolean contains(T obj) {
		if (obj == null)
			return false;
		long key = obj.getmCKey();
		return containsKey(key);
	}

	public void addAll(List<T> org) {
		if (isEmpty(org))
			return;
		int lens = org.size();
		long key = 0;
		T it = null;
		for (int i = 0; i < lens; i++) {
			it = org.get(i);
			if (it == null)
				continue;
			key = it.getmCKey();
			if (key <= 0)
				continue;
			put(key, it);
		}
	}

	public void addAll(MapListBBasic<T> org) {
		if (org == null)
			return;
		int lens = org.size();
		long key = 0;
		T it = null;
		for (int i = 0; i < lens; i++) {
			it = org.get(i);
			if (it == null)
				continue;
			key = it.getmCKey();
			if (key <= 0)
				continue;
			put(key, it);
		}
	}

	public MapListBBasic<T> merge(MapListBBasic<T> toDest, boolean isDelCurr) {
		if (toDest == null)
			toDest = new MapListBBasic<T>();
		List<T> org = this.getCurrList();
		int lens = org.size();
		T it = null;
		long key = 0;
		T itSrc = null;
		for (int i = 0; i < lens; i++) {
			it = org.get(i);
			if (it == null)
				continue;
			key = it.getmCKey();
			if (isDelCurr) {
				this.remove(key);
			}
			if (key <= 0)
				continue;
			itSrc = toDest.get(key);
			if (itSrc != null && itSrc.getmLTime() > it.getmLTime()) {
				Cache.returnObject(it);
				continue;
			}
			toDest.put(key, it);
			Cache.returnObject(itSrc);
		}
		return toDest;
	}

	public MapListBBasic<T> mergeD(MapListBBasic<T> toDest) {
		return this.merge(toDest, true);
	}
}
