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

	public static final void freeStream(final ByteOutStream os) {
		ByteOutPool.returnObject(os);
	}

	// //////////////

	public static final void writeVectorTag(final OutputStream out,
			final int len) throws Exception {
		if (out == null)
			return;
		B2OutputStream.writeVectorTag(out, len);
	}

	public static final void writeVectorEntry(final OutputStream out,
			final Object object) throws Exception {
		B2OutputStream.writeVectorEntry(out, object);
	}

	// //////////////

	public static final void writeMapTag(final OutputStream out, final int len)
			throws Exception {
		if (out == null)
			return;
		B2OutputStream.writeMapTag(out, len);
	}

	public static final void writeMapEntry(final OutputStream out,
			final Object key, final Object var) throws Exception {
		if (out == null || key == null)
			return;
		B2OutputStream.writeMapEntry(out, key, var);
	}
	// //////////////
}
