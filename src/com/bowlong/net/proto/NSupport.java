package com.bowlong.net.proto;

import java.io.OutputStream;

import com.bowlong.bio2.B2OutputStream;
import com.bowlong.io.ByteOutStream;
import com.bowlong.objpool.ByteOutPool;

public class NSupport {
	public static final int MAX_ROW = 20000;

	// //////////////
	public static final ByteOutStream getStream() {
		ByteOutStream baos = ByteOutPool.borrowObject();
		return baos;
	}

	public static final void freeStream(ByteOutStream os) {
		ByteOutPool.returnObject(os);
	}

	// //////////////

	public static final void writeVectorTag(OutputStream out,int len) throws Exception {
		if (out == null)
			return;
		B2OutputStream.writeVectorTag(out, len);
	}

	public static final void writeVectorEntry(OutputStream out,Object object) throws Exception {
		B2OutputStream.writeVectorEntry(out, object);
	}

	// //////////////

	public static final void writeMapTag(OutputStream out,int len) throws Exception {
		if (out == null)
			return;
		B2OutputStream.writeMapTag(out, len);
	}

	public static final void writeMapEntry(OutputStream out,Object key,Object var) throws Exception {
		if (out == null || key == null)
			return;
		B2OutputStream.writeMapEntry(out, key, var);
	}
	// //////////////
}
