package com.bowlong.io;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;

import com.bowlong.lang.NumEx;
import com.bowlong.objpool.ByteInPool;

public class ByteInStream extends ByteArrayInputStream {

	public static final ByteInStream create(byte[] buf) {
		return new ByteInStream(buf);
	}

	public ByteInStream(byte[] buf) {
		super(buf);
	}

	public final synchronized byte[] buf() {
		return buf;
	}

	public final synchronized void setBytes(byte[] buf) {
		super.buf = buf;
		super.pos = pos;
		super.mark = 0;
		super.count = buf.length;
	}

	public synchronized final byte[] readFully(byte result[],int off,int len) throws IOException {
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

	public synchronized final byte[] readFully(int len) throws IOException {
		final int off = 0;
		final byte result[] = new byte[len];
		return readFully(result, off, len);
	}

	public synchronized final byte[] readFully(byte result[]) throws IOException {
		final int off = 0;
		final int len = result.length;
		return readFully(result, off, len);
	}

	//
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
