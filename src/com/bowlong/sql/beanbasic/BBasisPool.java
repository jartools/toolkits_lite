package com.bowlong.sql.beanbasic;

import com.bowlong.objpool.BasicPool;

/**
 * 添加继承了 BeanBasic的对象池
 * 
 * @author canyon/龚阳辉
 * @time 2019-09-07 13:35 _tClazz.isInstance(obj);
 */
public class BBasisPool<T extends BeanBasic> extends BasicPool<T> {

	public BBasisPool(Class<T> clazz) {
		super(clazz);
	}

	public BBasisPool(Class<T> clazz, int num) {
		super(clazz, num);
	}

	@Override
	protected T resetObj(T obj) {
		if (obj != null) {
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
