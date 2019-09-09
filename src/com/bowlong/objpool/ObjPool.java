package com.bowlong.objpool;

/***
 * 所有Class对象的Pool
 * @author Canyon/龚阳辉
 * 2019-09-09 10:19
 */
@SuppressWarnings({ "unchecked" })
public class ObjPool {

	static final public <T> BasicPool<T> getPool(Class<T> c) {
		BasicPool<T> pool = BasicPool.getPool(c);
		if (pool == null) {
			pool = new BasicPool<T>(c);
			BasicPool.initPool(pool);
		}
		return pool;
	}

	static final public <T> T borrowObject(Class<T> c) {
		try {
			return (T) getPool(c).borrow();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final <T> void returnObject(T obj) {
		if (obj == null)
			return;
		try {
			Class<T> c = (Class<T>) obj.getClass();
			getPool(c).returnObj(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
