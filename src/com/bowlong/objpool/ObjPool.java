package com.bowlong.objpool;

@SuppressWarnings({"unchecked" })
public class ObjPool {
	static final public <T> T borrowObject(Class<T> c) {
		try {
			BasicPool<T> pool = BasicPool.getPool(c);
			if (pool == null) {
				pool = new BasicPool<T>(c);
				BasicPool.initPool(pool);
			}
			return (T) pool.borrow();
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
			BasicPool<T> pool = BasicPool.getPool(c);
			if (pool == null) {
				pool = new BasicPool<T>(c);
				BasicPool.initPool(pool);
			}
			pool.returnObj(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
