package com.bowlong.util;

import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class NewMap<K, V> extends ConcurrentHashMap<K, V> {
	public static NewMap create() {
		return new NewMap();
	}

	public static NewMap newly() {
		return new NewMap();
	}

	public static NewMap create(Object... objs) {
		NewMap r2 = create();
		return MapEx.putKvs4NewMap(r2, objs);
	}

	public static NewMap create(Map map) {
		if (map instanceof NewMap)
			return (NewMap) map;

		NewMap ret = new NewMap();
		ret.putAll(map);
		return ret;
	}

	public static NewMap newly(Map map) {
		return create(map);
	}

	public static NewMap newly(Object key, Object value) {
		return create().putE(key, value);
	}

	// /////////////
	public NewMap() {
	}

	public NewMap(int initialCapacity) {
		super(initialCapacity);
	}

	// /////////////
	@Override
	public V put(K key, V value) {
		return super.put(key, value);
	}
	
	public NewMap<K, V> putE(K key, V value) {
		put(key, value);
		return this;
	}
	
	public NewMap<K, V> add(K key, V value) {
		return putE(key, value);
	}

	public NewMap<K, V> add(Object... objs) {
		return MapEx.putKvs4NewMap(this, objs);
	}

	public NewMap<K, V> putE(Map map) {
		super.putAll(map);
		return this;
	}

	public NewMap<K, V> add(Map map) {
		return putE(map);
	}

	public boolean getBoolean(K key) {
		return MapEx.getBoolean(this, key);
	}

	public byte getByte(K key) {
		return MapEx.getByte(this, key);
	}

	public short getShort(K key) {
		return MapEx.getShort(this, key);
	}

	public int getInt(K key) {
		return MapEx.getInt(this, key);
	}

	public long getLong(K key) {
		return MapEx.getLong(this, key);
	}

	public float getFloat(K key) {
		return MapEx.getFloat(this, key);
	}

	public double getDouble(K key) {
		return MapEx.getDouble(this, key);
	}

	public String getString(K key) {
		return MapEx.getString(this, key);
	}

	public byte[] getBytes(K key) {
		return MapEx.getByteArray(this, key);
	}

	public Date getDate(K key) {
		return MapEx.getDate(this, key);
	}

	public NewDate getNewDate(K key) {
		return MapEx.getNewDate(this, key);
	}

	public Map getMap(K key) {
		return MapEx.getMap(this, key);
	}

	public NewMap getNewMap(K key) {
		return MapEx.getNewMap(this, key);
	}

	public List getList(K key) {
		return MapEx.getList(this, key);
	}

	public NewList getNewList(K key) {
		return MapEx.getNewList(this, key);
	}

	public Set getNewSet(K key) {
		return MapEx.getNewSet(this, key);
	}

	public Set getSet(K key) {
		return MapEx.getSet(this, key);
	}

	public Object getKey() {
		try {
			if (isEmpty())
				return "";
			return MapEx.getKey(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Object getValue() {
		try {
			if (isEmpty())
				return "";
			return MapEx.getValue(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Map<K, V> synchronizedMap() {
		return Collections.synchronizedMap(this);
	}

	public Map toMap() {
		return this;
	}

	public static final Map singletonEmptyMap = new Hashtable();

	public static final Map singletonEmptyMap() {
		return singletonEmptyMap;
	}

}
