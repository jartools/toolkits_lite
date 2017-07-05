package com.bowlong.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.concurrent.bio2.B2Codec;
import com.bowlong.lang.ByteEx;
import com.bowlong.lang.StrEx;
import com.bowlong.text.EncodingEx;

/**
 * 共享缓存
 * 
 * @author Canyon
 * @version createtime：2015年12月20日
 */
@SuppressWarnings("rawtypes")
public class MapperShareBuffer {
	RandomAccessFile rndAcf;
	FileChannel channel;
	MappedByteBuffer buffer;
	public int TRY_NUM = 99; // 重试99次
	public int TRY_WAIT = 10; // 每次重试间隔10ms

	// 过滤掉非法不可见字符 如果不过滤 XML解析就报异常
	public boolean isFilter = true;
	// 缓冲区大小为3M , 每次读出3M的内容
	public int BUFFER_SIZE = 0x300000;

	public MapperShareBuffer(File file, long size) throws Exception {
		rndAcf = new RandomAccessFile(file, "rws");
		channel = rndAcf.getChannel();
		buffer = channel.map(MapMode.READ_WRITE, 0, size);
	}

	public MapperShareBuffer(File file) throws Exception {
		rndAcf = new RandomAccessFile(file, "rws");
		channel = rndAcf.getChannel();
		buffer = channel.map(MapMode.READ_WRITE, 0, channel.size());
	}

	public final void close() throws Exception {
		if (buffer != null) {
			buffer.clear();
			buffer = null;
		}

		if (channel != null) {
			channel.close();
			channel = null;
		}

		if (rndAcf != null) {
			rndAcf.close();
			rndAcf = null;
		}
	}

	// /////////////////////////////////////////////////////////
	private final FileLock getLock() throws InterruptedException, IOException {
		FileLock result = null;
		for (int i = 0; i < TRY_NUM; i++) {
			result = channel.tryLock();
			if (result != null) {
				break;
			}
			Thread.sleep(TRY_WAIT);
		}
		return result;
	}

	// /////////////////////////////////////////////////////////
	public final int position(int pos) throws Exception {
		this.buffer.position(pos);
		return pos;
	}

	// /////////////////////////////////////////////////////////
	public final MappedByteBuffer buffer() {
		return this.buffer;
	}

	// /////////////////////////////////////////////////////////
	public final Map readMap(int pos) throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			position(pos);
			return B2Codec.toMap(buffer);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	public final Map readMap(MappedByteBuffer buffer) throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			return B2Codec.toMap(buffer);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	// /////////////////////////////////////////////////////////
	public final void putMap(int pos, Map m) throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			position(pos);
			B2Codec.toBytes(buffer, m);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	public final void putMap(Map m) throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			B2Codec.toBytes(buffer, m);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	// /////////////////////////////////////////////////////////
	public final List readList(int pos) throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			position(pos);
			return B2Codec.toList(buffer);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	public final List readList() throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			return B2Codec.toList(buffer);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	// /////////////////////////////////////////////////////////
	public final void putList(int pos, List l) throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			position(pos);
			B2Codec.toBytes(buffer, l);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	public final void putList(List l) throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");

		try {
			B2Codec.toBytes(buffer, l);
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	// ////////////////////////// InputStream ///////////////////////////
	public final InputStream readInps() throws Exception {
		FileLock lock = getLock();
		if (lock == null)
			throw new Exception("tryLock TIMEOUT.");
		try (ByteOutStream out = ByteOutStream.create()) {
			byte[] dst = new byte[BUFFER_SIZE];
			int size = buffer.capacity();
			for (int offset = 0; offset < size; offset += BUFFER_SIZE) {
				int diff = size - offset;
				if (diff >= BUFFER_SIZE) {
					for (int i = 0; i < BUFFER_SIZE; i++) {
						dst[i] = buffer.get(offset + i);
					}
				} else {
					for (int i = 0; i < diff; i++) {
						dst[i] = buffer.get(offset + i);
					}
				}

				int model = size % BUFFER_SIZE;
				int length = (model == 0) ? BUFFER_SIZE : model;
				byte[] bts = ByteEx.subBytes(dst, 0, length);
				out.write(bts);
			}

			out.flush();
			out.close();
			InputStream inps = ByteInStream.create(out.toByteArray());
			return inps;
		} catch (Exception e) {
			throw e;
		} finally {
			lock.release();
		}
	}

	// ////////////////////////// String ///////////////////////////
	public final String readStr(String charset) throws Exception {
		try (InputStream inps = readInps()) {
			InputStream inps2 = B2InputStream.dropBomByInps(inps);
			byte[] bts = B2InputStream.readStream(inps2);
			inps2.close();
			boolean isSup = EncodingEx.isSupported(charset);
			if (isSup) {
				return new String(bts, charset);
			}
			return new String(bts);
		}
	}

	public final String readStr4Xml(String charset) throws Exception {
		return StrEx.fitlerNonValidXMLChars(readStr(charset));
	}

	// /////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		List l = new Vector();
		l.add(1);
		l.add(2);
		l.add(3);
		l.add("4");
		l.add(false);
		l.add("呵呵");
		l.add(11.2233);
		// for (int i = 0; i < 10000; i++) {
		// l.add(i);
		// }
		File f = new File("m.bin");
		MapperShareBuffer msb = new MapperShareBuffer(f, 100 * 1024 * 1024);
		long _1 = System.currentTimeMillis();
		msb.putList(l);
		long _2 = System.currentTimeMillis();

		List l2 = msb.readList(0);
		long _3 = System.currentTimeMillis();
		System.out.println(l2);
		System.out.println("2-1:" + (_2 - _1) + " 3-2:" + (_3 - _2));
	}

}
