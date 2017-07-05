package com.bowlong.objpool;

import com.bowlong.util.StrBuilder;

public class StrBufPool extends AbstractQueueObjPool<StrBuilder> {

	public static final StrBufPool POOL = new StrBufPool();

	public StrBufPool() {
	}

	public StrBufPool(int num) {
		for (int i = 0; i < num; i++) {
			returnObj(createObj());
		}
	}

	@Override
	public final StrBuilder createObj() {
		StrBuilder obj = new StrBuilder();
		obj.pool = this;
		return obj;
	}

	@Override
	public final StrBuilder resetObj(StrBuilder obj) {
		obj.setLength(0);
		return obj;
	}

	@Override
	public final StrBuilder destoryObj(StrBuilder obj) {
		obj.setLength(0);
		obj.pool = null;
		return obj;
	}

	public static final StrBuilder borrowObject() {
		return POOL.borrow();
	}

	public static final void returnObject(StrBuilder obj) {
		POOL.returnObj(obj);
	}

}
