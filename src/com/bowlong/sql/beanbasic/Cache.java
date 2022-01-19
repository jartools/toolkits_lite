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
 * @author Canyon
 *
 * @param <T>
 */
@SuppressWarnings({ "rawtypes" })
public class Cache<T extends BeanBasic> extends ExToolkit {

	static final protected int NLOG_NONE = 0; // 无log
	static final protected int NLOG_LOAD = 1; // 所有 log
	static final protected int NLOG_LOAD_L = 2; // log load_list
	static final protected int NLOG_LOAD_M = 3; // log load_map

	static final public int DB_TYPE_N = 1; // dbType = new
	static final public int DB_TYPE_U = 2; // dbType = update
	static final public int DB_TYPE_D = 3; // dbType = delete

	static Log log = getLog(Cache.class);

	// 为了防止多线程异常问题，需要在Cache的初始化的时候，将pool池初始好
	static final public <T extends BeanBasic> void initPool(Class<T> clazz) {
		BBasisPool.initPool(new BBasisPool<T>(clazz));
	}

	static final public <T extends BeanBasic> BBasisPool<T> getPool(Class<T> clazz) {
		return (BBasisPool<T>) BBasisPool.getPool(clazz);
	}

	static final public <T extends BeanBasic> BBasisPool<T> getOrAddPool(Class<T> clazz) {
		BBasisPool<T> pool = getPool(clazz);
		if (pool == null) {
			pool = new BBasisPool<T>(clazz);
			BBasisPool.initPool(pool);
		}
		return pool;
	}

	static final public <T extends BeanBasic> T borrowObject(Class<T> clazz) {
		return BBasisPool.borrowObject(clazz);
	}

	static final public <T extends BeanBasic> void returnObject(T obj) {
		if (obj != null)
			BBasisPool.returnObject(obj);
	}

	protected Cache() {
		// 为了防止多线程异常问题，需要在Cache的初始化的时候，将pool池初始好
		_init();
	}

	public boolean isPtCache = false;
	public String tabelName = "";
	protected String coulmns_new = "";
	protected String val_new = "";
	protected String coulmns_all = "";
	protected String val_all = "";
	protected DataSet _dset = null; // mysql的DataSet
	private String sqlIn = "";
	protected String sqlUp = "";
	protected String sqlDel = "";
	final protected MapListBBasic<T> listIn = new MapListBBasic<>();
	final protected MapListBBasic<T> listUp = new MapListBBasic<>();
	final protected MapListBBasic<T> listDel = new MapListBBasic<>();

	// 分页查询效率慢,优化方式 ORDER BY
	// (建议where的时候用某个自增标识来做起点。比如id必须大于那个最后一条记录的id,并用 ORDER BY id 贼快)
	protected String str_c = ""; // 取得总页数的条件 - class
	protected String fmt_c = " 1 = 1 LIMIT %s,%s"; // 分页查询条件 - class
	protected String str_c_map = ""; // 取得总页数的条件 - map
	protected String fmt_s_map = ""; // 分页查询sql - map
	protected int nCurrLog = NLOG_NONE; // 打印 类型
	protected int nLimit = 10000; // 限定加载数量
	protected int nLmtMap = 10000; // 限定加载数量 - map
	protected int msSleep = 2; // 限定延迟处理
	// ExecutorService pool = Executors.newCachedThreadPool();

	protected void _init() {
	}

	protected DataSource ds() {
		return null;
	}

	protected DataSet dset() {
		DataSource _ds = ds();
		if (_dset == null && _ds != null) {
			_dset = new DataSet(_ds, tabelName);
		}
		return _dset;
	}

	public void loadAll() throws Exception {
		Class clzz = getTClazz();
		if (clzz != null && clzz != this.getClass())
			_loadList(clzz, true);
	}

	protected Class getTClazz() {
		return null;
	}

	protected String getCondition(int begin, int nlmt) {
		return String.format(fmt_c, begin, nlmt);
	}

	private List<T> _loadLst(Class clazz, boolean isCache) throws Exception {
		List<T> ret = null;
		if (!isCache)
			ret = newListT();
		int count = dset().count(str_c);
		int pageCount = ListEx.pageCount(count, nLimit);
		int begin = 0;
		List<T> list = null;
		int nlmt = nLimit;
		String c = "";
		boolean _isLog = (nCurrLog == NLOG_LOAD || nCurrLog == NLOG_LOAD_L);
		for (int i = 0; i < pageCount; i++) {
			nlmt = Math.min(nLimit, count);
			c = getCondition(begin, nlmt);
			list = dset().queryForList4C(c, clazz);
			if (isEmpty(list))
				break;
			begin += nlmt;
			count -= nlmt;
			if (isCache)
				cache(list);
			else
				ret.addAll(list);

			Thread.sleep(msSleep);
			if (_isLog) {
				log.info(String.format("== %s = [%s]", this.tabelName, c));
			}
		}
		return ret;
	}

	protected List<T> _loadList(Class clazz, boolean isCache) {
		List<T> ret = null;
		try {
			ret = _loadLst(clazz, isCache);
		} catch (Exception e) {
			log.error(e2s(e));
		}
		if (!isCache && ret == null)
			ret = newListT();
		return ret;
	}

	protected void cache(List<T> list) {
		if (list == null)
			return;
		int lens = list.size();
		T _it = null;
		for (int i = 0; i < lens; i++) {
			_it = list.get(i);
			if (_it != null)
				cache(_it);
		}
	}

	protected void cache(T item) {
	}

	protected void rmCache(List<T> list) {
		if (list == null)
			return;
		int lens = list.size();
		T _it = null;
		for (int i = 0; i < lens; i++) {
			_it = list.get(i);
			if (_it != null)
				rmCache(_it);
		}
	}

	protected void rmCache(T item) {
	}

	protected void cacheDB(T item, int dbType) {
		if (item == null)
			return;
		long key = item.getmCKey();
		switch (dbType) {
		case DB_TYPE_D:
			listIn.remove(key);
			listUp.remove(key);
			this._cacheDelete(item);
			break;
		default:
			this._cacheUpdate(item);
			break;
		}
	}

	public void cacheDelete(T item) {
		this.cacheDB(item, DB_TYPE_D);
	}

	public void cacheUpdate(T item) {
		this.cacheDB(item, DB_TYPE_U);
	}

	public void cacheNew(T item) {
		this.cacheDB(item, DB_TYPE_N);
	}

	private void _cacheNew(T item) {
		if (item.getmMKey() > 0)
			return;
		T nClone = item.toSave();
		if (nClone == null)
			return;
		nClone.setmDBType(DB_TYPE_N);
		listIn.add(nClone);
		cache(item);
	}

	private void _cacheUpdate(T item) {
		if (item.getmMKey() <= 0) {
			this._cacheNew(item);
			return;
		}

		T nClone = item.toSave();
		if (nClone == null)
			return;
		nClone.setmDBType(DB_TYPE_U);
		listUp.add(nClone);
	}

	private void _cacheDelete(T item) {
		T nClone = item.toSave();
		if (nClone == null)
			return;
		nClone.setmDBType(DB_TYPE_D);
		listDel.add(nClone);
	}

	public void saveDatabase() {
		MapListBBasic<T> tmpSrc = new MapListBBasic<T>();
		listDel.mergeD(tmpSrc);
		listIn.mergeD(tmpSrc);
		listUp.mergeD(tmpSrc);
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

		_delete(tempDe);
		_insert(tempIn, tmpInT);
		_update(tempUp);

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

	void _delete(List<Map> temp) {
		if (!isEmpty(temp) && !isEmpty(sqlDel)) {
			try {
				dset().batchUpdate(sqlDel, temp);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
	}

	void _insert(List<Map> temp, List<T> tmpList) {
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

	void _update(List<Map> temp) {
		if (!isEmpty(temp) && !isEmpty(sqlUp)) {
			try {
				dset().batchUpdate(sqlUp, temp);
			} catch (SQLException e) {
				log.error(e2s(e));
			}
		}
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

	protected String getSql4Map(int begin, int nlmt) {
		return String.format(fmt_s_map, begin, nlmt);
	}

	private List<Map<String, Object>> _load4Map() throws Exception {
		List<Map<String, Object>> ret = newListT();
		if (isEmpty(fmt_s_map))
			return ret;

		int count = dset().count(str_c_map);
		int pageCount = ListEx.pageCount(count, nLmtMap);
		int begin = 0;
		List<Map<String, Object>> list = null;
		int nlmt = nLmtMap;
		String _s = "";
		boolean _isLog = (nCurrLog == NLOG_LOAD || nCurrLog == NLOG_LOAD_M);
		for (int i = 0; i < pageCount; i++) {
			nlmt = Math.min(nLmtMap, count);
			_s = getSql4Map(begin, nlmt);
			list = dset().queryForList(_s);
			if (isEmpty(list))
				break;
			begin += nlmt;
			count -= nlmt;
			ret.addAll(list);
			Thread.sleep(msSleep);
			if (_isLog) {
				log.info(String.format("==m= %s = [%s]", this.tabelName, _s));
			}
		}
		return ret;
	}

	protected List<Map<String, Object>> _loadList4Map() {
		try {
			return _load4Map();
		} catch (Exception e) {
			log.error(e2s(e));
		}
		return newListT();
	}

	protected T _loadOne(String c, Class clazz, boolean isCache) {
		T ret = null;
		try {
			ret = dset().queryForObject4C(c, clazz);
			if (isCache && ret != null)
				cache(ret);
		} catch (Exception e) {
			log.error(e2s(e));
		}
		return ret;
	}

	public void excBatchSql(List<String> sqls) {
		if (isEmpty(sqls))
			return;
		excBatchSql(ListEx.toStrArray(sqls));
	}

	public void excBatchSql(String... sqls) {
		if (isEmpty(sqls))
			return;
		try {
			dset().batch4Sqls(sqls);
		} catch (SQLException e) {
			log.error(e2s(e));
		}
	}

	public void excSql(String sql) {
		if (isEmpty(sql))
			return;
		try {
			dset().execute(sql);
		} catch (SQLException e) {
			log.error(e2s(e));
		}
	}
}
