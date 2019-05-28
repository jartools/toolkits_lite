package com.bowlong.basic.bean;

import java.util.Comparator;
import java.util.Map;

import com.bowlong.basic.ExOrigin;

/**
 * 添加比较Map对象
 * @author Canyon
 *
 */
@SuppressWarnings("all")
public class ComparatorMap extends ExOrigin implements Comparator<Map> {
	private Object key = null;

	public int compare(Map o1, Map o2) {
		try {
			Object v1 = o1.get(key);
			Object v2 = o2.get(key);
			if (v1 == null || v2 == null)
				return 0;
			return compareTo(v1, v2);
		} catch (Exception e) {
			return 0;
		}
	}

	public ComparatorMap(Object key) {
		super();
		this.key = key;
	}

}
