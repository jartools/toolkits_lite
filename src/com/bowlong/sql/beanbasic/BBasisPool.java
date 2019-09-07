package com.bowlong.sql.beanbasic;

import com.bowlong.objpool.BasicPool;

/**
 * 添加继承了 BeanBasic的对象池
 * 
 * @author canyon/龚阳辉
 * @time 2019-09-07 13:35
 */
public class BBasisPool<T extends BeanBasic> extends BasicPool<T> {

	public BBasisPool(Class<T> clazz) {
		super(clazz);
	}

	public BBasisPool(Class<T> clazz, int num) {
		super(clazz, num);
	}

	@Override
	protected T createObj() {
		if (_tClazz != null) {
			try {
				return _tClazz.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected T resetObj(T obj) {
		if (obj != null && _tClazz != null && _tClazz.isInstance(obj)) {
			obj.clear();
		}
		return obj;
	}

	@Override
	protected T destoryObj(T obj) {
		if (obj != null)
			obj.clearAll();
		return obj;
	}
}
