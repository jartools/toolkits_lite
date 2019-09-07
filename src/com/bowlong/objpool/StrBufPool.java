package com.bowlong.objpool;

import com.bowlong.util.StrBuilder;

public class StrBufPool extends BasicPool<StrBuilder> {

	public StrBufPool() {
		super(StrBuilder.class);
	}

	public StrBufPool(int num) {
		super(StrBuilder.class, num);
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

	static final public StrBuilder borrowObject() {
		return borrowObject(StrBuilder.class);
	}

	static final public StrBufPool getPool() {
		return (StrBufPool) getPool(StrBuilder.class);
	}
}
