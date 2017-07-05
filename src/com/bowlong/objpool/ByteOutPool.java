package com.bowlong.objpool;

import com.bowlong.io.ByteOutStream;

public class ByteOutPool extends AbstractQueueObjPool<ByteOutStream> {
	public static final ByteOutPool POOL = new ByteOutPool();

	public static final ByteOutStream borrowObject() {
		return POOL.borrow();
	}

	public ByteOutPool() {
	}

	public ByteOutPool(int num) {
		for (int i = 0; i < num; i++) {
			returnObj(createObj());
		}
	}

	public static final void returnObject(ByteOutStream obj) {
		POOL.returnObj(obj);
	}

	@Override
	public final ByteOutStream createObj() {
		ByteOutStream os = new ByteOutStream(2048);
		os.pool = this;
		return os;
	}

	@Override
	public final ByteOutStream resetObj(ByteOutStream obj) {
		obj.reset();
		return obj;
	}

	@Override
	public final ByteOutStream destoryObj(ByteOutStream obj) {
		obj.pool = null;
		return obj;
	}

}
