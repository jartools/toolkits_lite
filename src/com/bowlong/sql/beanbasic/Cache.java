package com.bowlong.sql.beanbasic;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;

import com.bowlong.basic.ExToolkit;
import com.bowlong.sql.mysql.DataSet;
import com.bowlong.util.ListEx;

/**
 * 缓存对象父节点 数据缓存对象，可以操作增删改
 * 
 * @author Administrator
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class Cache<T extends BeanBasic> extends ExToolkit {
	static Log log = getLog(Cache.class);

	protected Cache() {
		_init();
	}

	public boolean isPtCache = false;
	public String TabelName = "";
	protected String coulmns_new = "";
	protected String val_new = "";
	protected String coulmns_all = "";
	protected String val_all = "";
	protected DataSet _dset = null; // mysql的DataSet
	private String sqlIn = "";
	protected String sqlUp = "";
	protected String sqlDel = "";
	final private List<T> listIn = newList2();
	final private List<T> listUp = newList2();
	final private List<T> listDel = newList2();

	// 分页查询效率慢,优化方式 ORDER BY
	// (建议where的时候用某个自增标识来做起点。比如id必须大于那个最后一条记录的id,并用 ORDER BY id 贼快)
	protected String fmt_c = " 1 = 1 LIMIT %s,%s"; // 分页查询条件
	protected int nLimit = 10000; // 限定加载数量
	protected int msSleep = 2; // 限定延迟处理

	protected void _init() {
	}

	protected DataSource ds() {
		return null;
	}

	protected DataSet dset() {
		DataSource _ds = ds();
		if (_dset == null && _ds != null) {
			_dset = new DataSet(_ds, TabelName);
		}
		return _dset;
	}

	public void loadAll() throws Exception {
		Class clzz = getTClazz();
		if (clzz != null && clzz != this.getClass())
			_loadAll(clzz, true);
	}

	protected Class getTClazz() {
		return null;
	}

	protected String getCondition(int begin, int nlmt) {
		return String.format(fmt_c, begin, nlmt);
	}

	protected List<T> _loadAll(Class clazz, boolean isCache) throws Exception {
		List<T> ret = null;
		if (!isCache)
			ret = newListT();
		int count = dset().count();
		int pageCount = ListEx.pageCount(count, nLimit);
		int begin = 0;
		List<T> list = null;
		int nlmt = nLimit;
		String c = "";
		for (int i = 0; i < pageCount; i++) {
			nlmt = Math.min(nLimit, count);
			c = getCondition(begin, nlmt);
			list = dset().queryForList(c, clazz);
			if (isEmpty(list))
				break;
			begin += nlmt;
			count -= nlmt;
			if (isCache)
				cache(list);
			else
				ret.addAll(list);

			Thread.sleep(msSleep);
			log.info(String.format("== %s = [%s]", this.TabelName, c));
		}
		return ret;
	}

	protected void cache(List<T> list) {
		if (list == null)
			return;
		int lens = list.size();
		for (int i = 0; i < lens; i++) {
			cache(list.get(i));
		}
	}

	protected void cache(T item) {
	}

	protected void rmCache(List<T> list) {
		if (list == null)
			return;
		int lens = list.size();
		for (int i = 0; i < lens; i++) {
			rmCache(list.get(i));
		}
	}

	protected void rmCache(T item) {
	}

	public void cacheNew(T item) {
		synchronized (listIn) {
			if (listDel.contains(item))
				return;
			listIn.add(item);
		}
		cache(item);
	}

	public void cacheUpdate(T item) {
		synchronized (listUp) {
			if (listDel.contains(item))
				return;
			if (listIn.contains(item))
				return;
			listUp.add(item);
		}
	}

	public void cacheDelete(T item) {
		synchronized (listDel) {
			listIn.remove(item);
			listUp.remove(item);
			if (listDel.contains(item))
				return;
			listDel.add(item);
		}
	}

	public void saveDatabase() {
		List<Map> temp = newListT();
		List<T> tmpList = newListT();
		_delete(temp, tmpList);
		_insert(temp, tmpList);
		_update(temp, tmpList);
	}

	void _delete(List<Map> temp, List<T> tmpList) {
		synchronized (listDel) {
			if (!listDel.isEmpty()) {
				T _tt = null;
				int lens = listDel.size();
				for (int i = 0; i < lens; i++) {
					_tt = listDel.get(i);
					rmCache(_tt);
					listIn.remove(_tt);
					listUp.remove(_tt);
					temp.add(_tt.toMap());
				}
				listDel.clear();
			}
		}
		if (!isEmpty(temp) && !isEmpty(sqlDel)) {
			try {
				dset().batchUpdate(sqlDel, temp);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
		tmpList.clear();
		temp.clear();
	}

	void _insert(List<Map> temp, List<T> tmpList) {
		if (isEmpty(sqlIn)) {
			sqlIn = String.format(BeanBasic.insFmt, TabelName, coulmns_new, val_new);
		}
		synchronized (listIn) {
			if (!listIn.isEmpty()) {
				int lens = listIn.size();
				for (int i = 0; i < lens; i++) {
					temp.add(listIn.get(i).toMap());
				}
				tmpList.addAll(listIn);
				listIn.clear();
			}
		}
		if (!isEmpty(temp)) {
			try {
				long[] ids = dset().batchInsertGK(sqlIn, temp);
				callInsert(tmpList, ids);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
		tmpList.clear();
		temp.clear();
	}

	void _update(List<Map> temp, List<T> tmpList) {
		synchronized (listUp) {
			if (!listUp.isEmpty()) {
				int lens = listUp.size();
				for (int i = 0; i < lens; i++) {
					temp.add(listUp.get(i).toMap());
				}
				listUp.clear();
			}
		}
		if (!isEmpty(temp) && !isEmpty(sqlUp)) {
			try {
				dset().batchUpdate(sqlUp, temp);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
		tmpList.clear();
		temp.clear();
	}

	private void callInsert(List<T> list, long[] ids) {
		if (list == null || ids == null)
			return;
		int lens = ids.length;
		T _item = null;
		long _id = 0;
		for (int i = 0; i < lens; i++) {
			_item = list.get(i);
			_id = ids[i];
			callInsertLong(_item, _id);
			callInsert(_item, (int) _id);
		}
	}

	protected void callInsert(T item, int id) {
	}

	protected void callInsertLong(T item, long id) {
	}

	protected boolean isInOrUp(T item) {
		return listIn.contains(item) || listUp.contains(item);
	}
}
