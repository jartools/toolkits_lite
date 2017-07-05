package com.bowlong.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.bowlong.lang.ByteEx;
import com.bowlong.lang.NumEx;
import com.bowlong.objpool.ByteOutPool;

public class ByteOutStream extends ByteArrayOutputStream {

	public static final ByteOutStream create() {
		return new ByteOutStream();
	}

	public ByteOutStream() {
		super();
	}

	public ByteOutStream(int size) {
		super(size);
	}

	public final synchronized byte[] buf() {
		return buf;
	}

	public final synchronized byte[] toByteArray(final int from, final int to) {
		return copyOfRange(buf, from, to);
	}

	public synchronized byte toLengthByteArray()[] {
		return copyOf(buf, count);
	}

	public static byte[] copyOfRange(byte[] original, int from, int to) {
		int newLength = to - from;
		if (newLength < 0)
			throw new IllegalArgumentException(from + " > " + to);
		return copyOf(original, newLength);
	}

	public static byte[] copyOf(byte[] original, int newLength) {
		return ByteEx.subBytes(original, 0, newLength);
	}

	public final synchronized void writeTo(final OutputStream out) throws IOException {
		final int from = 0;
		final int to = size();
		writeTo(out, from, to);
	}

	public final synchronized void writeTo(final OutputStream out, final int from, final int to) throws IOException {
		final int len = to - from;
		out.write(buf, from, len);
	}

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

	public ByteOutPool pool;

	@Override
	public final synchronized void close() throws IOException {
		super.close();

		if (pool != null)
			pool.returnObj(this);
	}
}
