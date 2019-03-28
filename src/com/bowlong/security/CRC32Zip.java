package com.bowlong.security;

import java.util.zip.CRC32;

/**
 * CRC32 Zip算法
 * 
 * @author Canyon
 * @version 2019-03-28 18:50
 */
public class CRC32Zip {
	static final CRC32 _obj = new CRC32();
	static final public long crc32(byte[] buff) {
		_obj.reset();
		_obj.update(buff);
		return _obj.getValue();
	}

	static final public String crc32Hex(byte[] buff) {
		long _v = crc32(buff);
		return Long.toHexString(_v);
	}
}
