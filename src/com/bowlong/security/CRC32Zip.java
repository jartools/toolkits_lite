package com.bowlong.security;

import java.util.zip.CRC32;

import com.bowlong.basic.ExToolkit;

/**
 * CRC32 Zip算法
 * 
 * @author Canyon
 * @version 2019-03-28 18:50
 */
public class CRC32Zip extends ExToolkit {
	static final CRC32 _obj = new CRC32();

	static final public long crc32(byte[] buff) {
		_obj.reset();
		_obj.update(buff);
		return _obj.getValue();
	}

	static final public String crc32Hex(byte[] buff) {
		return Long.toHexString(crc32(buff));
	}

	static final public long crc32(String val) {
		if (isEmpty(val))
			return 0L;
		return crc32(val.getBytes());
	}

	static final public String crc32Hex(String val) {
		if (isEmpty(val))
			return "";
		return crc32Hex(val.getBytes());
	}
}
