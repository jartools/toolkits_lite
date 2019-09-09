package com.bowlong.objpool;

import java.util.Map;

import com.bowlong.basic.ExToolkit;
import com.bowlong.tool.SnowflakeldPool;

/**
 * 对象池的管理类 管理 Factory( = pool，创建，借出，回收对象等逻辑)
 * 
 * @author canyon/龚阳辉
 * @time 2019-09-07 14:35
 */
@SuppressWarnings("unchecked")
public class MgrPools extends ExToolkit {
	static final Map<Class<?>, BasicPool<?>> map = newMap();

	static final boolean isEmpty(BasicPool<?> val) {
		return val == null || val._tClazz == null;
	}

	static final public void initPool(BasicPool<?> val) {
		if (!isEmpty(val))
			map.put(val._tClazz, val);
	}

	static final public BasicPool<?> rmPool(Class<?> clazz) {
		if (clazz != null)
			return map.remove(clazz);
		return null;
	}

	static final public void reInitPool(BasicPool<?> val) {
		if (!isEmpty(val)) {
			rmPool(val._tClazz);
			initPool(val);
		}
	}

	static final public BasicPool<?> getPool0(Class<?> clazz) {
		if (clazz == null)
			return null;
		return map.get(clazz);
	}

	static final public BasicPool<?> getPool0(Object obj) {
		if (obj == null)
			return null;
		return getPool0(obj.getClass());
	}

	static final public <T> BasicPool<T> getPool(Class<T> clazz) {
		return (BasicPool<T>) getPool0(clazz);
	}

	static final public <T> BasicPool<T> getPool(T obj) {
		return (BasicPool<T>) getPool0(obj);
	}

	static final public <T> T get(Class<T> clazz) {
		BasicPool<?> _pool = getPool(clazz);
		if (_pool != null)
			return (T) _pool.borrow();
		return null;
	}

	static final public <T> T borrowObject(Class<T> clazz) {
		return get(clazz);
	}

	static final public <T> void returnObject(T obj) {
		if (obj == null)
			return;
		BasicPool<T> _pool = getPool(obj);
		if (_pool == null)
			return;
		_pool.returnObj(obj);
	}

	static {
		initPool(new ByteInPool());
		initPool(new ByteOutPool());
		initPool(new StrBufPool());
		initPool(new StringBufPool());
		initPool(new StringBuilderPool());
		initPool(new SnowflakeldPool());
	}
}
