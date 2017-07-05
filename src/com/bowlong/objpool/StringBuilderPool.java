package com.bowlong.objpool;

public class StringBuilderPool extends AbstractQueueObjPool<StringBuilder> {
	public static final StringBuilderPool POOL = new StringBuilderPool();

	public StringBuilderPool() {
	}

	public StringBuilderPool(int num) {
		for (int i = 0; i < num; i++) {
			returnObj(createObj());
		}
	}

	@Override
	public StringBuilder createObj() {
		return new StringBuilder();
	}

	@Override
	public final StringBuilder resetObj(StringBuilder obj) {
		obj.setLength(0);
		return obj;
	}

	@Override
	public final StringBuilder destoryObj(StringBuilder obj) {
		obj.setLength(0);
		return obj;
	}

	public static final StringBuilder borrowObject() {
		return POOL.borrow();
	}

	public static final void returnObject(StringBuilder obj) {
		POOL.returnObj(obj);
	}

}
