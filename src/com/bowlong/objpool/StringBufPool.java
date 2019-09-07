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

	static final public StringBuffer borrowObject() {
		return borrowObject(StringBuffer.class);
	}

	static final public StringBufPool getPool() {
		return (StringBufPool) getPool(StringBuffer.class);
	}
}
