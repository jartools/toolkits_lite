package com.bowlong.io;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

import com.bowlong.lang.NumEx;
import com.bowlong.objpool.ByteInPool;

public class ByteInStream extends ByteArrayInputStream {

	public static final ByteInStream create(final byte[] buf) {
		return new ByteInStream(buf);
	}

	public ByteInStream(final byte[] buf) {
		super(buf);
	}

	public final synchronized byte[] buf() {
		return buf;
	}

	public final synchronized void setBytes(final byte[] buf) {
		super.buf = buf;
		super.pos = pos;
		super.mark = 0;
		super.count = buf.length;
	}

	public synchronized final byte[] readFully(final byte result[],
			final int off, final int len) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = read(result, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
		return result;
	}

	public synchronized final byte[] readFully(final int len)
			throws IOException {
		final int off = 0;
		final byte result[] = new byte[len];
		return readFully(result, off, len);
	}

	public synchronized final byte[] readFully(final byte result[])
			throws IOException {
		final int off = 0;
		final int len = result.length;
		return readFully(result, off, len);
	}

	//
	public final synchronized void setBoolean(final int pos, final boolean v) {
		NumEx.writeBool(buf, pos, v);
	}

	public final synchronized void setByte(final int pos, final byte v) {
		NumEx.writeByte(buf, pos, v);
	}

	public final synchronized void setShort(final int pos, final short v) {
		NumEx.writeShort(buf, pos, v);
	}

	public final synchronized void setInt(final int pos, int v) {
		NumEx.writeInt(buf, pos, v);
	}

	public final synchronized void setLong(final int pos, final long v) {
		NumEx.writeLong(buf, pos, v);
	}

	public final synchronized void setFloat(final int pos, final float v) {
		NumEx.writeFloat(buf, pos, v);
	}

	public final synchronized void setDouble(final int pos, final double v) {
		NumEx.writeDouble(buf, pos, v);
	}

	public final synchronized void setBytes(final int pos, final byte[] v) {
		NumEx.writeBytes(buf, pos, v);
	}

	//
	public final synchronized boolean getBoolean(final int pos) {
		return NumEx.readBool(buf, pos);
	}

	public final synchronized byte getByte(final int pos) {
		return NumEx.readByte(buf, pos);
	}

	public final synchronized short getShort(final int pos) {
		return NumEx.readShort(buf, pos);
	}

	public final synchronized int getInt(final int pos) {
		return NumEx.readInt(buf, pos);
	}

	public final synchronized long getLong(final int pos) {
		return NumEx.readLong(buf, pos);
	}

	public final synchronized float getFloat(final int pos) {
		return NumEx.readFloat(buf, pos);
	}

	public final synchronized double getDouble(final int pos) {
		return NumEx.readDouble(buf, pos);
	}

	//
	public final synchronized boolean readBoolean() throws IOException {
		return NumEx.readBool(this);
	}

	public final synchronized int readByte() throws IOException {
		return NumEx.readByte(this);
	}

	public final synchronized short readShort() throws IOException {
		return NumEx.readShort(this);
	}

	public final synchronized int readInt() throws IOException {
		return NumEx.readInt(this);
	}

	public final synchronized long readLong() throws IOException {
		return NumEx.readLong(this);
	}

	public final synchronized float readFloat() throws IOException {
		return NumEx.readFloat(this);
	}

	public final synchronized double readDouble() throws IOException {
		return NumEx.readDouble(this);
	}

	public ByteInPool pool = null;

	@Override
	public synchronized void close() throws IOException {
		super.close();

		if (pool != null)
			pool.returnObj(this);
	}

}
