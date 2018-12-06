package com.bowlong.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.io.ByteInStream;
import com.bowlong.io.ByteOutStream;
import com.bowlong.objpool.ByteOutPool;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.ExceptionEx;
import com.bowlong.util.Ref;

/**
 * 
 * @author Canyon
 * @version createtime：2015年8月17日下午7:50:55
 */
@SuppressWarnings({ "rawtypes" })
public class InputStreamEx {

	static final protected Ref<Boolean> refObj = new Ref<Boolean>(false);

	static final public String reCharset(String charset) {
		charset = reCharset(charset, refObj);
		if (!refObj.val)
			charset = EncodingEx.UTF_8;
		return charset;
	}

	static final public String reCharset(String charset, Ref<Boolean> refSupport) {
		boolean isSupported = !StrEx.isEmptyTrim(charset);
		if (isSupported) {
			isSupported = EncodingEx.isSupported(charset);
			if (!isSupported) {
				charset = EncodingEx.UTF_8;
				isSupported = true;
			}
		}
		if (refSupport != null)
			refSupport.val = isSupported;
		return charset;
	}

	static final public List<String> inps2LineStr(InputStream ins, String charset) {
		if (ins == null)
			return null;
		try {
			List<String> reList = new ArrayList<String>();
			charset = reCharset(charset, refObj);
			boolean isSup = refObj.val;
			BufferedReader br = null;
			if (isSup) {
				br = new BufferedReader(new InputStreamReader(ins, charset));
			} else {
				br = new BufferedReader(new InputStreamReader(ins));
			}
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

	static final public String inps2Str(InputStream ins, String charset) {
		List<String> list = inps2LineStr(ins, charset);
		if (list == null)
			return "";
		int lens = list.size();
		if (lens <= 0)
			return "";
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < lens; i++) {
			buff.append(list.get(i));
		}
		String ret = buff.toString();
		buff.setLength(0);
		return ret;
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
			return readStream(ins);
		} catch (Exception e) {
		} finally {
			if (isCloseIns) {
				try {
					ins.close();
				} catch (Exception e) {
				}
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
			charset = reCharset(charset, refObj);
			boolean isSup = refObj.val;
			byte[] bts = inps2Bytes(ins);
			if (isSup) {
				return new String(bts, charset);
			} else {
				return new String(bts);
			}
		} catch (Exception e) {
			return ExceptionEx.e2s(e);
		}
	}

	static final public byte[] readStream(InputStream is, boolean isLmt) throws IOException {
		if (is == null)
			return null;
		try (ByteOutStream out = ByteOutPool.borrowObject();) {
			byte[] buf = new byte[1024];
			int times = 1024 * 50;
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
