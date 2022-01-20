package com.bowlong.sql.beanbasic;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 缓存对象父节点 数据缓存对象，可以操作增删改
 * 
 * @author Canyon
 *
 * @param <T>
 */
@SuppressWarnings({ "rawtypes" })
public class CacheNSync<T extends BeanBasic> extends Cache<T> {
	protected CacheNSync() {
		super();
	}

	public void cacheNew(T item) {
		if (item.getmMKey() > 0)
			return;
		T nClone = item.toSave();
		if (nClone == null)
			return;
		nClone.setmDBType(DB_TYPE_N);
		dbIn.add(nClone);
		cache(item);
	}

	public void cacheUpdate(T item) {
		if (item.getmMKey() <= 0) {
			this.cacheNew(item);
			return;
		}

		T nClone = item.toSave();
		if (nClone == null)
			return;
		nClone.setmDBType(DB_TYPE_U);
		dbUp.add(nClone);
	}

	public void cacheDelete(T item) {
		T nClone = item.toSave();
		if (nClone == null)
			return;
		nClone.setmDBType(DB_TYPE_D);
		dbDel.add(nClone);
	}

	public void saveDatabase() {
		MapListBBasic<T> tmpSrc = new MapListBBasic<T>();
		dbDel.mergeD(tmpSrc);
		dbIn.mergeD(tmpSrc);
		dbUp.mergeD(tmpSrc);
		List<T> tmpList = tmpSrc.getCurrList();
		int lens = tmpList.size();
		T it = null;
		List<Map> tempDe = newListT();
		List<Map> tempUp = newListT();
		List<Map> tempIn = newListT();
		Map<String, Object> tempMap = null;
		List<T> tmpInT = newListT();
		for (int i = 0; i < lens; i++) {
			it = tmpList.get(i);
			tempMap = it.toMap();
			switch (it.getmDBType()) {
			case DB_TYPE_D:
				rmCache(it);
				tempDe.add(tempMap);
				break;
			case DB_TYPE_U:
				tempUp.add(tempMap);
				break;
			case DB_TYPE_N:
				tempIn.add(tempMap);
				tmpInT.add(it);
				break;
			default:
				break;
			}
		}

		__delete(tempDe);
		__insert(tempIn, tmpInT);
		__update(tempUp);

		for (int i = 0; i < lens; i++) {
			it = tmpList.get(i);
			returnObject(it);
		}

		tempDe.clear();
		tempIn.clear();
		tmpInT.clear();
		tempUp.clear();
		tmpList.clear();
	}

	private void __delete(List<Map> temp) {
		if (!isEmpty(temp) && !isEmpty(sqlDel)) {
			try {
				dset().batchUpdate(sqlDel, temp);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
	}

	private void __insert(List<Map> temp, List<T> tmpList) {
		if (isEmpty(sqlIn) && !isEmpty(tabelName) && !isEmpty(coulmns_new)) {
			sqlIn = String.format(BeanBasic.insFmt, tabelName, coulmns_new, val_new);
		}
		if (!isEmpty(temp) && !isEmpty(sqlIn)) {
			try {
				long[] ids = dset().batchInsertGK(sqlIn, temp);
				callInsert(tmpList, ids);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
	}

	private void __update(List<Map> temp) {
		if (!isEmpty(temp) && !isEmpty(sqlUp)) {
			try {
				dset().batchUpdate(sqlUp, temp);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
	}
}
