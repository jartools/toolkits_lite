package com.bowlong.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bowlong.basic.ExOrigin;
import com.bowlong.bio2.B2InputStream;
import com.bowlong.io.ByteInStream;
import com.bowlong.io.ByteOutStream;
import com.bowlong.objpool.ByteOutPool;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.Ref;

/**
 * 
 * @author Canyon
 * @version createtime：2015年8月17日下午7:50:55
 */
@SuppressWarnings({ "rawtypes" })
public class InputStreamEx extends ExOrigin {
	static final public List<String> inps2LineStr(InputStream ins, String charset) {
		if (ins == null)
			return null;
		try {
			List<String> reList = new ArrayList<String>();
			charset = reCharset(charset);
			BufferedReader br = new BufferedReader(new InputStreamReader(ins, charset));
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				reList.add(readLine);
			}
			ins.close();
			br.close();
			return reList;
		} catch (Exception e) {
			return null;
		}
	}

	static final public List<String> inps2LineStr(InputStream ins) {
		return inps2LineStr(ins, EncodingEx.UTF_8);
	}

	static final public String inps2Str(InputStream ins, String charset) {
		List<String> list = inps2LineStr(ins, charset);
		if (list == null)
			return "";
		int lens = list.size();
		if (lens <= 0)
			return "";
		StringBuffer buff = StringBufPool.borrowObject();
		for (int i = 0; i < lens; i++) {
			buff.append(list.get(i));
		}
		String ret = buff.toString();
		StringBufPool.returnObject(buff);
		return ret;
	}

	static final public String inps2Str(InputStream ins) {
		return inps2Str(ins, EncodingEx.UTF_8);
	}

	static final public Object inps2Obj(InputStream ins) {
		if (ins == null)
			return null;
		try {
			return B2InputStream.readObject(ins);
		} catch (Exception e) {
		}
		return null;
	}

	static final public Map inps2Map(InputStream ins) {
		if (ins == null)
			return new HashMap();
		try {
			return B2InputStream.readMap(ins);
		} catch (Exception e) {
		}
		return new HashMap();
	}

	static final public byte[] inps2Bytes(InputStream ins, boolean isCloseIns) {
		if (ins == null)
			return new byte[0];
		try {
			return readFully(ins);
		} catch (Exception e) {
		} finally {
			if (isCloseIns) {
				close(ins);
			}
		}
		return new byte[0];
	}

	static final public byte[] inps2Bytes(InputStream ins) {
		return inps2Bytes(ins, true);
	}

	static final public Object inps2Obj4Stream(InputStream ins) throws Exception {
		byte[] bts = inps2Bytes(ins);
		try (ByteInStream byteStream = ByteInStream.create(bts)) {
			return B2InputStream.readObject(byteStream);
		}
	}

	static final public Map inps2Map4Stream(InputStream ins) throws Exception {
		byte[] bts = inps2Bytes(ins);
		try (ByteInStream byteStream = ByteInStream.create(bts)) {
			return B2InputStream.readMap(byteStream);
		}
	}

	static final public String inps2Str4Stream(InputStream ins, String charset) {
		if (ins == null)
			return "";
		try {
			Ref<Boolean> refBl = new Ref<Boolean>(false);
			charset = reCharset(charset, refBl);
			boolean isSup = refBl.val;
			byte[] bts = inps2Bytes(ins);
			if (isSup) {
				return new String(bts, charset);
			} else {
				return new String(bts);
			}
		} catch (Exception e) {
			return e2s(e);
		}
	}

	static final public String inps2Str4Stream(InputStream ins) {
		return inps2Str4Stream(ins, EncodingEx.UTF_8);
	}

	static final public byte[] readStream(InputStream is, boolean isLmt) throws IOException {
		if (is == null)
			return null;
		try (ByteOutStream out = ByteOutPool.borrowObject();) {
			byte[] buf = new byte[1024];
			int times = 1024 * 1024 * 100;
			while (true) {
				if (isLmt) {
					if (times-- <= 0)
						break;
				}
				try {
					int len = is.read(buf);
					if (len <= 0)
						break;
					out.write(buf, 0, len);
				} catch (Exception e) {
					break;
				}
			}
			byte[] b = out.toByteArray();
			return b;
		}
	}

	static final public byte[] readStream(InputStream is) throws IOException {
		return readStream(is, true);
	}

	static final public byte[] readFully(final InputStream is) throws IOException {
		return readStream(is, false);
	}

	static final public void close(final InputStream input) {
		if (input == null)
			return;
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
