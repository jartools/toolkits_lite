package com.bowlong.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SingleMap<K, V> implements Map<K, V> {

	protected K key;
	protected V val;
	
	protected String text;

	public SingleMap() {

	}

	public SingleMap(K k, V val2) {
		this.key = k;
		this.val = val2;
	}

	public SingleMap(K k, V val2, String text) {
		this.key = k;
		this.val = val2;
		this.text = text;
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		return this.key.equals(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.val.equals(value);
	}

	@Override
	public V get(Object key) {
		return val;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return val;
	}

	public String getText() {
		return text;
	}

	@Override
	public V put(K key, V value) {
		this.key = key;
		this.val = value;

		return val;
	}

	public V put(K key, V value, String text) {
		this.key = key;
		this.val = value;
		this.text = text;

		return val;
	}

	@Override
	public V remove(Object key) {
		val = null;
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		if (m == null || MapEx.isEmpty(m))
			return;
		try {
			K key = MapEx.getKey(m);
			V val = MapEx.getValue(m);
			put(key, val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clear() {
		this.key = null;
		this.val = null;
	}

	@Override
	public Set<K> keySet() {
		Set<K> r2 = new HashSet<K>();
		if (key != null)
			r2.add(key);
		return r2;
	}

	@Override
	public Collection<V> values() {
		List<V> r2 = new ArrayList<>();
		if (val != null)
			r2.add(val);
		return r2;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<java.util.Map.Entry<K, V>> r2 = new HashSet<java.util.Map.Entry<K, V>>();
		if (key != null && val != null)
			r2.add(new SingleMapEntry(key, val));
		return null;
	}

	class SingleMapEntry implements java.util.Map.Entry<K, V> {
		protected K key;
		protected V val;

		public SingleMapEntry(K k, V v) {
			this.key = k;
			this.val = v;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return val;
		}

		@Override
		public V setValue(V value) {
			this.val = value;
			return val;
		}
	}


}
