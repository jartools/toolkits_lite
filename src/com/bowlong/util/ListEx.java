package com.bowlong.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
// import java.util.StringTokenizer; StringTokenizer 类是出于兼容性的原因而被保留的遗留类
import java.util.Vector;

import org.apache.commons.collections4.list.FixedSizeList;
import org.apache.commons.collections4.list.TreeList;

import com.alibaba.fastjson.JSON;
import com.bowlong.basic.ExOrigin;
import com.bowlong.basic.bean.ComparatorMap;
import com.bowlong.lang.RndEx;
import com.bowlong.objpool.StringBufPool;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ListEx extends ExOrigin {
	public static final <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static final <T> List<T> newLinkedList() {
		return new LinkedList<T>();
	}

	public static final <T> List<T> newVector() {
		return new Vector<T>();
	}

	public static final <T> TreeList<T> newTreeList() {
		return new TreeList<T>();
	}

	public static final FixedSizeList newFixedSizeList(List<?> list) {
		return FixedSizeList.fixedSizeList(list);
	}

	public static final <T> T get(List list, int index) {
		return (T) list.get(index);
	}

	public static final <T> T getFirst(List list) {
		if (isEmpty(list))
			return null;
		return (T) list.get(0);
	}

	static public final String[] toArrs(String s, String delimiter) {
		if (isEmpty(s))
			return null;
		if (isEmpty(delimiter))
			delimiter = ",";
		return s.split(delimiter);
	}

	/*** 分隔符 **/
	static public final List<String> toList(String s, String delimiter, int ntype) {
		List<String> _ret = newListT();
		String[] arrs = toArrs(s, delimiter);
		if (arrs == null || arrs.length <= 0)
			return _ret;

		boolean isTrim = ntype == 0 || ntype == 1;
		boolean isExceptEmp = ntype == 0 || ntype == 2;
		int lens = arrs.length;
		String str = "";
		for (int i = 0; i < lens; i++) {
			str = arrs[i];
			if (isExceptEmp && isEmpty(str))
				continue;
			if (isTrim)
				str = str.trim();
			_ret.add(str);
		}
		return _ret;
	}

	/*** 分隔符，英文逗号(,) **/
	static public final List<String> toListByComma(String s, int ntype) {
		return toList(s, ",", ntype);
	}

	/*** 分隔符，英文分号逗号(,;,;) **/
	static public final List<List<String>> toListBySemicolonComma(String s, int ntype) {
		return toLists(s, ";", ",", ntype);
	}

	/*** 分隔符，英文分号逗号(,;,;) **/
	static public final List<List<String>> toLists(String s, String delimiter1, String delimiter2, int ntype) {
		List<List<String>> ret = newListT();
		List<String> list = toList(s, delimiter1, 0);
		int lens = list.size();
		List<String> tmp = null;
		for (int i = 0; i < lens; i++) {
			tmp = toList(list.get(i), delimiter2, ntype);
			if (isEmpty(tmp))
				continue;
			ret.add(tmp);
		}
		return ret;
	}

	static public final List<String> toList(String s, String delimiter) {
		return toList(s, delimiter, 0);
	}

	public static final List<Integer> toListInt(String s) {
		List<Integer> ret = newListT();
		List<String> listStr = toListByComma(s, 0);
		if (isEmpty(listStr))
			return ret;
		int len = listStr.size();
		for (int i = 0; i < len; i++) {
			int e = stringToInt(listStr.get(i));
			ret.add(e);
		}
		return ret;
	}

	static public final List<List<Integer>> toListInts(String s) {
		List<List<Integer>> ret = newListT();
		List<String> list = toList(s, ";", 0);
		int lens = list.size();
		List<Integer> tmp = null;
		for (int i = 0; i < lens; i++) {
			tmp = toListInt(list.get(i));
			if (isEmpty(tmp))
				continue;
			ret.add(tmp);
		}
		return ret;
	}

	/*** [12,12,"dfg",\"adsfasd\"] **/
	public static final <T> List<T> toListByJson(String s, Class<T> clazz) {
		List<T> result = null;
		try {
			s = s.replaceAll("\\\\", "");
			result = JSON.parseArray(s, clazz);
		} catch (Exception e) {
			result = newListT();
		}
		return result;
	}

	public static final List toListByLine(String s) {
		List l = new Vector();
		StringReader sr = new StringReader(s);
		BufferedReader br = new BufferedReader(sr);
		try {
			while (true) {
				String v = br.readLine();
				if (v == null)
					break;
				l.add(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	public static final <T> List<T> toList(Set<T> m) {
		List<T> ret = newListT();
		if (m == null)
			return ret;
		ret.addAll(m);
		return ret;
	}

	public static final List<Integer> toList(int... array) {
		List ret = newList();
		return add(ret, array);
	}

	public static final List<Integer> toListArrs(int[] array) {
		List ret = newList();
		return add(ret, array);
	}

	public static final List<String> toList(String... array) {
		List<String> ret = newListT();
		return addT(ret, array);
	}

	public static final int[] toArrs4Int(List<Integer> list) {
		if (list == null || list.isEmpty())
			return new int[0];

		int count = list.size();
		int[] result = new int[count];

		for (int i = 0; i < count; i++) {
			Integer v = list.get(i);
			if (v == null)
				result[i] = 0;
			result[i] = v.intValue();
		}
		return result;
	}

	public static final int[] toArrs4Int(int... args) {
		return args;
	}

	public static final String[] toStrArray(List<String> list) {
		return toArrs(list, new String[0]);
	}

	public static final List toArrayList(List list) {
		List ret = newArrayList();
		if (list == null)
			return ret;
		for (Object e : list) {
			ret.add(e);
		}
		return ret;
	}

	public static final List toLinkedList(List list) {
		List ret = newLinkedList();
		if (list == null)
			return ret;
		for (Object e : list) {
			ret.add(e);
		}
		return ret;
	}

	public static final List toVector(List list) {
		List ret = newVector();
		if (list == null)
			return ret;
		for (Object e : list) {
			ret.add(e);
		}
		return ret;
	}

	public static final List toListByObj(Object... arrs) {
		List list = newList();
		return add(list, arrs);
	}

	public static final <T> List<T> toListT(T... arrs) {
		List<T> list = newListT();
		return addT(list, arrs);
	}

	public static final List toLinkedList(Object... arrs) {
		List list = newLinkedList();
		return add(list, arrs);
	}

	public static final List toVector(Object... arrs) {
		List list = newVector();
		return add(list, arrs);
	}

	/*** 转为数组对象 **/
	static public final <T> T[] toArrs(List<T> list, T[] arrs) {
		if (isEmpty(list))
			return arrs;
		return (T[]) list.toArray(arrs);
	}

	static public final <T> List<T> toList(T[] arrs) {
		List<T> ret = newListT();
		if (arrs == null || arrs.length <= 0)
			return ret;
		int lens = arrs.length;
		for (int i = 0; i < lens; i++) {
			if (arrs[i] != null)
				ret.add(arrs[i]);
		}
		return ret;
	}

	public static final List listIt(Object... var) {
		List result = new Vector();
		return add(result, var);
	}

	public static final List asList(Object... args) {
		return Arrays.asList(args);
	}

	public static final List add(List list, Object... objs) {
		if (objs == null || objs.length <= 0)
			return list;

		for (int i = 0; i < objs.length; i++) {
			list.add(objs[i]);
		}
		return list;
	}

	public static final <T> List<T> addT(List<T> list, T... objs) {
		if (objs == null || objs.length <= 0)
			return list;

		for (int i = 0; i < objs.length; i++) {
			list.add(objs[i]);
		}
		return list;
	}

	static public final boolean have(List list, Object obj) {
		if (isEmpty(list) || obj == null)
			return false;
		return list.contains(obj);
	}

	public static final boolean haveInt(int[] array, int v) {
		for (int i : array) {
			if (v == i)
				return true;
		}
		return false;
	}

	public static final boolean haveInt(List vars, int i) {
		return have(vars, i);
	}

	public static final boolean haveInt(Set<Integer> sets, int[] array) {
		for (int i : array) {
			if (sets.contains(i))
				return true;
		}
		return false;
	}

	public static final boolean haveInt(int[] array, Set<Integer> sets) {
		Iterator<Integer> it = sets.iterator();
		while (it.hasNext()) {
			int i = it.next();
			if (haveInt(array, i))
				return true;
		}
		return false;
	}

	public static final int max(List<Integer> list) {
		return Collections.max(list);
	}

	public static final int min(List<Integer> list) {
		return Collections.min(list);
	}

	// 是否有交集
	public static final boolean isDisjoint(List l1, List l2) {
		return Collections.disjoint(l1, l2);
	}

	/*** 求交集∩ **/
	public static final List intersected(List l1, List l2) {
		List result = newList();
		result.addAll(l1);
		result.retainAll(l2);
		return result;
	}

	/*** 求交集∩,泛型T **/
	public static final <T> List<T> intersectedT(List<T> l1, List<T> l2) {
		List<T> result = newListT();
		result.addAll(l1);
		result.retainAll(l2);
		return result;
	}

	// 拷贝
	public static final List copy(List src) {
		if (isEmpty(src))
			return null;
		List dest = newVector();
		Collections.copy(dest, src);
		return dest;
	}

	// 反转
	public static final List reverse(List src) {
		Collections.reverse(src);
		return src;
	}

	// 旋转
	public static final List rotate(List src, int distance) {
		Collections.rotate(src, distance);
		return src;
	}

	// 对List进行打乱顺序
	public static final List shuffle(List src) {
		Collections.shuffle(src);
		return src;
	}

	// 对List进行打乱顺序
	public static final List shuffleRnd(List src) {
		// Collections.shuffle(src, new Random(System.currentTimeMillis()));
		Collections.shuffle(src, new Random(RndEx.randomNum()));
		return src;
	}

	/*** 将list都随机打乱 **/
	public static final List rndList(final List srcList) {
		List ret = new ArrayList();
		List list = new Vector();
		list.addAll(srcList);

		int num = list.size();
		for (int n = num; n > 0; n--) {
			int p = RndEx.nextInt(n);
			Object e = list.remove(p);
			ret.add(e);
		}
		return ret;
	}

	public static final List subRndList(final List srcList, final int subSize) {
		if (srcList == null)
			return srcList;
		int len = srcList.size();
		List result = rndList(srcList);
		if (len <= subSize)
			return result;
		return result.subList(0, subSize);
	}

	/*** 将泛型list都随机打乱 **/
	public static final <T> List<T> rndListT(final List<T> srcList) {
		return (List<T>) rndList(srcList);
	}

	public static final <T> List<T> subRndListT(final List<T> srcList, final int subSize) {
		return (List<T>) subRndList(srcList, subSize);
	}

	/*** sublist ,Inc:inclusive,Exc:exclusive **/
	static public final List subList(final List src, int fIndInc, int tIndExc) {
		if (src == null)
			return newList();
		if (src.isEmpty())
			return src;

		int lens = src.size();
		tIndExc = tIndExc >= lens ? lens : tIndExc;
		fIndInc = (fIndInc <= 0) ? 0 : (fIndInc >= tIndExc) ? tIndExc : fIndInc;
		if (fIndInc >= lens)
			return newList();
		return src.subList(fIndInc, tIndExc);
	}

	static public final List subList(final List src, int fIndInc) {
		int tIndExc = 0;
		if (src != null) {
			tIndExc = src.size();
		}
		return subList(src, fIndInc, tIndExc);
	}

	/*** sublist ,Inc:inclusive,Exc:exclusive **/
	static public final <T> List<T> subListT(final List<T> src, int fIndInc, int tIndExc) {
		return subList(src, fIndInc, tIndExc);
	}

	static public final <T> List<T> subListT(final List<T> src, int fIndInc) {
		return subList(src, fIndInc);
	}

	public static final List sort(List src) {
		Collections.sort(src);
		return src;
	}

	public static final <T> List<T> sort(List<T> src, Comparator<T> comparator) {
		Collections.sort(src, comparator);
		return src;
	}

	public static final List<Map> sort(List<Map> m1, String key) {
		return sort(m1, new ComparatorMap(key));
	}

	/*** 交换位置 **/
	public static final List swap(List list, int i, int j) {
		Collections.swap(list, i, j);
		return list;
	}

	public static final List<Byte> distinctByte(List<Byte> vars) {
		List<Byte> ret = new Vector<Byte>();
		Map<Byte, Byte> mvars = new Hashtable<Byte, Byte>();
		for (Byte v : vars) {
			mvars.put(v, v);
		}
		ret.addAll(mvars.values());
		return ret;
	}

	public static final List<Short> distinctShort(List<Short> vars) {
		List<Short> ret = new Vector<Short>();
		Map<Short, Short> mvars = new Hashtable<Short, Short>();
		for (Short v : vars) {
			mvars.put(v, v);
		}
		ret.addAll(mvars.values());
		return ret;
	}

	public static final List<Integer> distinctInteger(List<Integer> vars) {
		List<Integer> ret = new Vector<Integer>();
		Map<Integer, Integer> mvars = new Hashtable<Integer, Integer>();
		for (Integer v : vars) {
			mvars.put(v, v);
		}
		ret.addAll(mvars.values());
		return ret;
	}

	public static final List<Long> distinctLong(List<Long> vars) {
		List<Long> ret = new Vector<Long>();
		Map<Long, Long> mvars = new Hashtable<Long, Long>();
		for (Long v : vars) {
			mvars.put(v, v);
		}
		ret.addAll(mvars.values());
		return ret;
	}

	public static final List<String> distinctString(List<String> vars) {
		List<String> ret = new Vector<String>();
		Map<String, String> mvars = new Hashtable<String, String>();
		for (String v : vars) {
			mvars.put(v, v);
		}
		ret.addAll(mvars.values());
		return ret;
	}

	public static final String formatString(List l) {
		int depth = 1;
		return formatString(l, depth);
	}

	public static final String formatString(List l, int depth) {
		int p = 0;
		int size = l.size();
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append("[\n");
			for (Object v : l) {
				for (int i = 0; i < depth; i++)
					sb.append("    ");
				if (v instanceof List) {
					sb.append(ListEx.formatString((List) v, depth + 1));
				} else if (v instanceof Map) {
					sb.append(MapEx.formatString((Map) v, depth + 1));
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
			sb.append("]");
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	// //////////////// 过滤 ////////////////
	/*** 过滤origin中包含filter里面的元素 **/
	static public final <T> List fitlerList(List<T> origin, List<T> filter) {
		if (isEmpty(origin)) {
			return newListT();
		}
		if (isEmpty(filter)) {
			return origin;
		}

		List<T> ret = newListT();

		int lens = origin.size();
		for (int i = 0; i < lens; i++) {
			T t = origin.get(i);
			if (t == null) {
				continue;
			}
			boolean isFilter = have(filter, t);
			if (isFilter) {
				continue;
			}
			ret.add(t);
		}
		return ret;
	}

	/*** 过滤origin中包含filter的元素 **/
	static public final <T> List fitlerList(List<T> origin, T filter) {
		if (isEmpty(origin)) {
			return newListT();
		}

		if (filter == null) {
			return origin;
		}

		List<T> ret = newListT();

		int lens = origin.size();
		for (int i = 0; i < lens; i++) {
			T t = origin.get(i);
			if (t == null) {
				continue;
			}
			if (t == filter) {
				continue;
			}
			ret.add(t);
		}
		return ret;
	}
	// //////////////////////////////////////////////////

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 50; i++) {
			String str = RndEx.nextString(i);
			System.out.println(i + ":" + str);
			list.add(i);
		}
		List<Integer> rndList = rndListT(list);
		List<Integer> subList = subRndListT(list, 400);
		System.out.println("==== list =====");
		System.out.println(list);
		System.out.println(rndList);
		System.out.println(subList);
	}
}
