package com.bowlong.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings({})
public class ListSingleMap implements List<SingleMap<Object, Object>> {
	protected List<SingleMap<Object, Object>> list = new ArrayList<>();

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<SingleMap<Object, Object>> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(SingleMap<Object, Object> e) {
		return list.add(e);
	}

	public boolean add(Object key, Object val) {
		SingleMap<Object, Object> e = new SingleMap<Object, Object>(key, val);
		return list.add(e);
	}

	public boolean add(Object key, Object val, String text) {
		SingleMap<Object, Object> e = new SingleMap<Object, Object>(key, val, text);
		return list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends SingleMap<Object, Object>> c) {
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int index,
			Collection<? extends SingleMap<Object, Object>> c) {
		return list.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public SingleMap<Object, Object> get(int index) {
		return list.get(index);
	}

	public Object getKey(int index) {
		SingleMap<Object, Object> m = list.get(index);
		return m.getKey();
	}

	public Object getValue(int index) {
		SingleMap<Object, Object> m = list.get(index);
		return m.getValue();
	}

	public Object getText(int index) {
		SingleMap<Object, Object> m = list.get(index);
		return m.getText();
	}

	@Override
	public SingleMap<Object, Object> set(int index,
			SingleMap<Object, Object> element) {
		return list.set(index, element);
	}

	public SingleMap<Object, Object> set(int index, Object key, Object val) {
		SingleMap<Object, Object> map = list.get(index);
		map.put(key, val);
		return map;
	}

	public SingleMap<Object, Object> set(int index, Object key, Object val, String text) {
		SingleMap<Object, Object> map = list.get(index);
		map.put(key, val, text);
		return map;
	}

	@Override
	public void add(int index, SingleMap<Object, Object> element) {
		list.add(element);
	}

	public void add(int index, Object key, Object val) {
		SingleMap<Object, Object> e = new SingleMap<Object, Object>(key, val);
		list.add(index, e);
	}

	public void add(int index, Object key, Object val, String text) {
		SingleMap<Object, Object> e = new SingleMap<Object, Object>(key, val, text);
		list.add(index, e);
	}

	@Override
	public SingleMap<Object, Object> remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<SingleMap<Object, Object>> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<SingleMap<Object, Object>> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<SingleMap<Object, Object>> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

}
