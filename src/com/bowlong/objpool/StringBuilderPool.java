package com.bowlong.objpool;

public class StringBuilderPool extends BasicPool<StringBuilder> {

	public StringBuilderPool() {
		super(StringBuilder.class);
	}

	public StringBuilderPool(int num) {
		super(StringBuilder.class, num);
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

	static final public StringBuilder borrowObject() {
		return borrowObject(StringBuilder.class);
	}

	static final public StringBuilderPool getPool() {
		return (StringBuilderPool) getPool(StringBuilder.class);
	}
}
