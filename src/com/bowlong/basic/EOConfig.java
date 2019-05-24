package com.bowlong.basic;

import com.bowlong.util.Ref;

/**
 * 常量<br/>
 * 
 * @author Canyon
 * @time 2019-02-14 19:32
 */
public class EOConfig {
	static final public String kf_2 = "%s_%s";
	static final public String kf_3 = "%s_%s_%s";
	static final public String kf_4 = "%s_%s_%s_%s";
	static final public String C_SUCCESS = "success";
	static final public String C_WAIT = "wait";
	static final public String C_FAILS = "fails";
	static final public String C_ERROR = "error";
	
	static final public Ref<Boolean> refBl = new Ref<Boolean>(false);
	static final public Ref<Byte> refByte = new Ref<Byte>((byte) 0);
	static final public Ref<Short> refShort = new Ref<Short>((short) 0);
	static final public Ref<Integer> refInt = new Ref<Integer>(0);
	static final public Ref<Long> refLong = new Ref<Long>(0L);
	static final public Ref<Float> refFloat = new Ref<Float>(0F);
	static final public Ref<Double> refDouble = new Ref<Double>(0D);
	static final public Ref<Object> refObj = new Ref<Object>();
}
