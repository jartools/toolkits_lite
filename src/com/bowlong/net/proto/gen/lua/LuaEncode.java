package com.bowlong.net.proto.gen.lua;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.bio2.B2OutputStream;
import com.bowlong.io.ByteInStream;
import com.bowlong.io.ByteOutStream;

@SuppressWarnings("unchecked")
public class LuaEncode extends B2OutputStream {

	/**
	 * 编码
	 * 
	 * @param obj
	 * @return
	 */
	static public final byte[] encode(Object obj) {
		byte[] result = null;
		try (ByteOutStream os = new ByteOutStream();){
			writeObject(os, obj);
			result = os.toByteArray();
			os.flush();
			os.close();
		} catch (Exception e) {
			result = new byte[0];
		}
		return result;
	}

	/**
	 * 解码
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	static public final <T> T decode(Object obj, Class<T> clazz)
			throws Exception {
		if (obj instanceof byte[]) {
			return decode((byte[]) obj, clazz);
		}
		return clazz.newInstance();
	}

	static public final <T> T decode(byte[] buf, Class<T> clazz)
			throws Exception {
		try (ByteInStream in = new ByteInStream(buf);){
			T result = (T) B2InputStream.readObject(in);
			in.close();
			return result;
		} catch (Exception e) {
		}
		return clazz.newInstance();
	}
}
