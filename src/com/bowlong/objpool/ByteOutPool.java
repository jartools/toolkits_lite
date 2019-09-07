package com.bowlong.objpool;

import com.bowlong.io.ByteOutStream;

public class ByteOutPool extends BasicPool<ByteOutStream> {
	
	public ByteOutPool() {
		super(ByteOutStream.class);
	}

	public ByteOutPool(int num) {
		super(ByteOutStream.class,num);
	}
	
	public static final ByteOutStream borrowObject() {
		return borrowObject(ByteOutStream.class);
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
