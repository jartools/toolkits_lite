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

	public final synchronized byte[] toByteArray(int from,int to) {
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

	public final synchronized void writeTo(OutputStream out) throws IOException {
		final int from = 0;
		final int to = size();
		writeTo(out, from, to);
	}

	public final synchronized void writeTo(OutputStream out,int from,int to) throws IOException {
		final int len = to - from;
		out.write(buf, from, len);
	}

	public final synchronized void setBoolean(int pos,boolean v) {
		NumEx.writeBool(buf, pos, v);
	}

	public final synchronized void setByte(int pos,byte v) {
		NumEx.writeByte(buf, pos, v);
	}

	public final synchronized void setShort(int pos,short v) {
		NumEx.writeShort(buf, pos, v);
	}

	public final synchronized void setInt(int pos, int v) {
		NumEx.writeInt(buf, pos, v);
	}

	public final synchronized void setLong(int pos,long v) {
		NumEx.writeLong(buf, pos, v);
	}

	public final synchronized void setFloat(int pos,float v) {
		NumEx.writeFloat(buf, pos, v);
	}

	public final synchronized void setDouble(int pos,double v) {
		NumEx.writeDouble(buf, pos, v);
	}

	public final synchronized void setBytes(int pos,byte[] v) {
		NumEx.writeBytes(buf, pos, v);
	}

	//
	public final synchronized boolean getBoolean(int pos) {
		return NumEx.readBool(buf, pos);
	}

	public final synchronized byte getByte(int pos) {
		return NumEx.readByte(buf, pos);
	}

	public final synchronized short getShort(int pos) {
		return NumEx.readShort(buf, pos);
	}

	public final synchronized int getInt(int pos) {
		return NumEx.readInt(buf, pos);
	}

	public final synchronized long getLong(int pos) {
		return NumEx.readLong(buf, pos);
	}

	public final synchronized float getFloat(int pos) {
		return NumEx.readFloat(buf, pos);
	}

	public final synchronized double getDouble(int pos) {
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
