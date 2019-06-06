package com.bowlong.basic;

import com.bowlong.util.Ref;

/**
 * 常量<br/>
 * 
 * @author Canyon
 * @time 2019-02-14 19:32
 */
public class EOConfig {
	static final public Ref<Boolean> refBl = new Ref<Boolean>(false);
	static final public Ref<Byte> refByte = new Ref<Byte>((byte) 0);
	static final public Ref<Short> refShort = new Ref<Short>((short) 0);
	static final public Ref<Integer> refInt = new Ref<Integer>(0);
	static final public Ref<Long> refLong = new Ref<Long>(0L);
	static final public Ref<Float> refFloat = new Ref<Float>(0F);
	static final public Ref<Double> refDouble = new Ref<Double>(0D);
	static final public Ref<Object> refObj = new Ref<Object>();
	
	static final public String kf_2 = "%s_%s";
	static final public String kf_3 = "%s_%s_%s";
	static final public String kf_4 = "%s_%s_%s_%s";
	static final public String C_SUCCESS = "success";
	static final public String C_WAIT = "wait";
	static final public String C_FAILS = "fails";
	static final public String C_ERROR = "error";
	static final public String f_d = ".";
	static final public int kb_et = 1; // 存储单位-类型
	static final public int mb_et = 2;
	static final public int gb_et = 3;
	static final public int tb_et = 4;
	static final public String am = "f:%s t:%s u:%s m:%s"; // 当前 app 内存
	static final public String am_kb = "f:%skb t:%skb u:%skb m:%skb";
	static final public String am_mb = "f:%smb t:%smb u:%smb m:%smb";
	static final public String am_gb = "f:%sgb t:%sgb u:%sgb m:%sgb";
	static final public String am_tb = "f:%stb t:%stb u:%stb m:%stb";
	static final public int KB = 1024; // 存储单位-值
	static final public int MB = KB * KB;
	static final public long GB = MB * KB;
	static final public long TB = GB * KB;
	static final public long PB = TB * KB;
}
