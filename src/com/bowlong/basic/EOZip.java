package com.bowlong.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.bowlong.objpool.ObjPool;

/**
 * zip压缩<br/>
 * 
 * @author Canyon
 * @version 2019-05-29 20:15
 */
public class EOZip extends EOURL {
	
	static final public ByteArrayOutputStream newOutStream() {
		return new ByteArrayOutputStream();
	}
	
	static final public InputStream newInStream(byte[] b) {
		return new ByteArrayInputStream(b);
	}

	// ///////////////////////////////////////////////////
	static final public byte[] zip(byte[] b) throws IOException {
		ByteArrayOutputStream baos = ObjPool.borrowObject(ByteArrayOutputStream.class);
		try {
			GZIPOutputStream gos = new GZIPOutputStream(baos);
			gos.write(b);
			gos.finish();
			return baos.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			ObjPool.returnObject(baos);
		}
	}

	static final public byte[] unzip(byte[] b) throws IOException {
		ByteArrayOutputStream baos = ObjPool.borrowObject(ByteArrayOutputStream.class);
		try {
			int times = 1000;
			byte[] buff = new byte[4 * 1024];
			InputStream bais = newInStream(b);
			GZIPInputStream gis = new GZIPInputStream(bais);
			while (true) {
				if (times-- <= 0)
					break;
				int len = gis.read(buff);
				if (len <= 0)
					break;
				baos.write(buff, 0, len);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			ObjPool.returnObject(baos);
		}
	}

	static final public byte[] unzip(byte[] b, int srcLen) throws IOException {
		byte[] buff = new byte[srcLen];
		InputStream bais = newInStream(b);
		GZIPInputStream gis = new GZIPInputStream(bais);
		gis.read(buff);
		return buff;
	}

	// ///////////////////////////////////////////////////
}
