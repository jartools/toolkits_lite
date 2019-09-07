package com.bowlong.objpool;

import com.bowlong.io.ByteInStream;

public class ByteInPool extends BasicPool<ByteInStream> {

	public ByteInPool() {
		super(ByteInStream.class);
	}

	public ByteInPool(int num) {
		super(ByteInStream.class, num);
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

	static final public ByteInStream borrowObject() {
		return borrowObject(ByteInStream.class);
	}

	static final public ByteInStream borrowObject(byte[] buf) {
		ByteInStream r2 = borrowObject();
		r2.setBytes(buf);
		return r2;
	}

	static final public ByteInPool getPool() {
		return (ByteInPool) getPool(ByteInStream.class);
	}
}
