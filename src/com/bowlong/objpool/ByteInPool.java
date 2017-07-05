package com.bowlong.objpool;

import com.bowlong.io.ByteInStream;

public class ByteInPool extends AbstractQueueObjPool<ByteInStream> {
	public static final ByteInPool POOL = new ByteInPool();

	public static final ByteInStream borrowObject(byte[] buf) {
		ByteInStream r2 = POOL.borrow();
		r2.setBytes(buf);
		return r2;
	}

	public ByteInPool() {
	}

	public ByteInPool(int num) {
		for (int i = 0; i < num; i++) {
			returnObj(createObj());
		}
	}

	public static final void returnObject(ByteInStream obj) {
		POOL.returnObj(obj);
	}

	@Override
	public final ByteInStream createObj() {
		ByteInStream is = new ByteInStream(new byte[8]);
		is.pool = this; // close it return to pool
		return is;
	}

	@Override
	public final ByteInStream resetObj(ByteInStream obj) {
		obj.reset();
		return obj;
	}

	@Override
	public final ByteInStream destoryObj(ByteInStream obj) {
		obj.pool = null;
		return obj;
	}

}
