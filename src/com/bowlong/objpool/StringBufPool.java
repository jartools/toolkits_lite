package com.bowlong.objpool;

public class StringBufPool extends BasicPool<StringBuffer> {

	public StringBufPool() {
		super(StringBuffer.class);
	}

	public StringBufPool(int num) {
		super(StringBuffer.class, num);
	}

	@Override
	public final StringBuffer createObj() {
		return new StringBuffer();
	}

	@Override
	public final StringBuffer resetObj(StringBuffer obj) {
		obj.setLength(0);
		return obj;
	}

	@Override
	public final StringBuffer destoryObj(StringBuffer obj) {
		obj.setLength(0);
		return obj;
	}

	public static final StringBuffer borrowObject() {
		return borrowObject(StringBuffer.class);
	}
}
