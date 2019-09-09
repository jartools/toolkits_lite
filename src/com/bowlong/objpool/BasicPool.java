package com.bowlong.objpool;

/**
 * 对象池父类
 * 
 * @author canyon/龚阳辉
 * @time 2019-09-07 14:35
 */
public class BasicPool<T> extends AbstractQueueObjPool<T> {
	protected Class<T> _tClazz = null;

	public BasicPool(Class<T> clazz) {
		this._tClazz = clazz;
	}

	public BasicPool(Class<T> clazz, int initNum) {
		this._tClazz = clazz;
		onInit(initNum);
	}

	@Override
	protected T createObj() {
		if (this._tClazz != null) {
			try {
				return this._tClazz.newInstance();
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	@Override
	protected T resetObj(T obj) {
		return obj;
	}

	@Override
	protected T destoryObj(T obj) {
		return obj;
	}

	public final void onInit(int initNum) {
		for (int i = 0; i < initNum; i++) {
			returnObj(createObj());
		}
	}

	static final public <T> BasicPool<T> getPool(Class<T> clazz) {
		return MgrPools.getPool(clazz);
	}

	static final public <T> T borrowObject(Class<T> clazz) {
		BasicPool<T> _pool = getPool(clazz);
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
