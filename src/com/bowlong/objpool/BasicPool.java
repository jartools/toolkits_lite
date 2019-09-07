package com.bowlong.objpool;

/**
 * 对象池父类
 * 
 * @author canyon/龚阳辉
 * @time 2019-09-07 14:35
 */
public abstract class BasicPool<T> extends AbstractQueueObjPool<T> {
	protected Class<T> _tClazz = null;

	public BasicPool(Class<T> clazz) {
		this._tClazz = clazz;
	}

	public BasicPool(Class<T> clazz, int initNum) {
		this._tClazz = clazz;
		for (int i = 0; i < initNum; i++) {
			returnObj(createObj());
		}
	}

	static final public <T> T borrowObject(Class<T> clazz) {
		BasicPool<T> _pool = MgrPools.getPool(clazz);
		if (_pool != null)
			return _pool.borrow();
		return null;
	}

	static final public <T> void returnObject(T obj) {
		BasicPool<T> _pool = MgrPools.getPool(obj);
		if (_pool != null)
			_pool.returnObj(obj);
	}

	static final public void initPool(BasicPool<?> pool) {
		MgrPools.initPool(pool);
	}
}
