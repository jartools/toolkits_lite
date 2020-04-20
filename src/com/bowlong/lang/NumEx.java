package com.bowlong.lang;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import com.bowlong.basic.ExOrigin;

public final class NumEx extends ExOrigin {
	// byte
	public static final byte BYTE_MIN_VALUE = -128;
	public static final byte BYTE_MAX_VALUE = 127;
	// short
	public static final short SHORT_MIN_VALUE = -32768;
	public static final short SHORT_MAX_VALUE = 32767;
	// int
	public static final int INT_MIN_VALUE = 0x80000000;
	public static final int INT_MAX_VALUE = 0x7fffffff;
	// long
	public static final long LONG_MIN_VALUE = 0x8000000000000000L;
	public static final long LONG_MAX_VALUE = 0x7fffffffffffffffL;
	// float
	public static final float FLOAT_MIN_VALUE = 0x0.000002P-126f; // 1.4e-45f
	public static final float FLOAT_MAX_VALUE = 0x1.fffffeP+127f; // 3.4028235e+38f
	// double
	public static final double DOUBLE_MIN_VALUE = 0x0.0000000000001P-1022; // 4.9e-324
	public static final double DOUBLE_MAX_VALUE = 0x1.fffffffffffffP+1023; // 1.7976931348623157e+308

	private static final int read(InputStream input) throws IOException {
		int value = input.read();
		if (-1 == value)
			throw new EOFException("Unexpected EOF reached");
		return value;
	}

	public static final void writeBytes(byte[] data,int offset,byte[] value) {
		System.arraycopy(value, 0, data, offset, value.length);
	}

	public static final void writeBytes(byte[] data,Offset offset,byte[] value) {
		try {
			writeBytes(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += value.length;
		}
	}

	public static final byte[] readBytes(byte[] data,int offset,int len) {
		return ByteEx.subBytes(data, offset, len);
	}

	public static final byte[] readBytes(byte[] data,Offset offset,int len) {
		try {
			return readBytes(data, offset.reader, len);
		} catch (Exception e) {
		} finally {
			offset.reader += len;
		}
		return new byte[0];
	}

	public static final void writeUTF8(byte[] data,int offset,String value) {
		byte[] b = value.getBytes(Charset.forName("UTF-8"));
		writeInt(data, offset, b.length);
		writeBytes(data, offset + 4, b);
	}

	public static final void writeUTF8(byte[] data,Offset offset,String value) {
		try {
			byte[] b = value.getBytes(Charset.forName("UTF-8"));
			writeInt(data, offset, b.length);
			writeBytes(data, offset, b);
		} catch (Exception e) {
		} finally {
		}
	}

	public static final String readUTF8(byte[] data,int offset) {
		int len = readInt(data, offset);
		byte[] result = new byte[len];
		System.arraycopy(data, offset + 4, result, 0, len);
		return new String(result, Charset.forName("UTF-8"));
	}

	public static final String readUTF8(byte[] data,Offset offset) {
		int len = 0;
		try {
			len = readInt(data, offset);
			byte[] result = new byte[len];
			System.arraycopy(data, offset.reader, result, 0, len);
			return new String(result, Charset.forName("UTF-8"));
		} catch (Exception e) {
		} finally {
			offset.reader += len;
		}
		return "";
	}

	public static final void writeBool(byte[] data,int offset,boolean value) {
		data[offset + 0] = (byte) (value ? 0x01 : 0x00);
	}

	public static final void writeBool(byte[] data,Offset offset,boolean value) {
		try {
			writeBool(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 1;
		}
	}

	public static final boolean readBool(byte[] data,int offset) {
		return (boolean) ((data[offset + 0] & 0xff) == 0x01);
	}

	public static final boolean readBool(byte[] data, Offset offset) {
		try {
			return readBool(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 1;
		}
		return false;
	}

	public static final void write(byte[] data,int offset,boolean value) {
		writeBool(data, offset, value);
	}

	public static final void write(byte[] data,Offset offset,boolean value) {
		writeBool(data, offset, value);
	}

	public static final void writeByte(byte[] data,int offset,int value) {
		data[offset + 0] = (byte) value;
	}

	public static final void writeByte(byte[] data,Offset offset,int value) {
		try {
			writeByte(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 1;
		}
	}

	public static final void writeChar(byte[] data,int offset,int v) throws IOException {
		data[offset + 0] = (byte) ((v >>> 8) & 0xFF);
		data[offset + 1] = (byte) ((v >>> 0) & 0xFF);
	}

	public static final char readChar(byte[] data,int offset) throws IOException {
		int ch1 = (data[offset + 0] & 0xff);
		int ch2 = (data[offset + 1] & 0xff);
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	public static final byte readByte(byte[] data,int offset) {
		return (byte) ((data[offset + 0] & 0xff));
	}

	public static final byte readByte(byte[] data,Offset offset) {
		try {
			return readByte(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 1;
		}
		return 0;
	}

	public static final void write(byte[] data,int offset,byte value) {
		writeByte(data, offset, value);
	}

	public static final void write(byte[] data,Offset offset,byte value) {
		writeByte(data, offset, value);
	}

	public static final void writeShort(byte[] data,int offset,short value) {
		data[offset + 0] = (byte) ((value >> 8) & 0xff);
		data[offset + 1] = (byte) ((value >> 0) & 0xff);
	}

	public static final void writeShort(byte[] data,Offset offset,short value) {
		try {
			writeShort(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 2;
		}
	}

	public static final void write(byte[] data,int offset,short value) {
		writeShort(data, offset, value);
	}

	public static final void write(byte[] data,Offset offset,short value) {
		writeShort(data, offset, value);
	}

	public static final short readShort(byte[] data,int offset) {
		return (short) (((data[offset + 0] & 0xff) << 8) + ((data[offset + 1] & 0xff) << 0));
	}

	public static final short readShort(byte[] data,Offset offset) {
		try {
			return readShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final int readUnsignedShort(byte[] data,int offset) {
		return (((data[offset + 0] & 0xff) << 8) + ((data[offset + 1] & 0xff) << 0));
	}

	public static final int readUnsignedShort(byte[] data,Offset offset) {
		try {
			return readUnsignedShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final void writeUnsignedShort(byte[] data,int offset, int value) {
		data[offset + 0] = (byte) ((value >> 24) & 0xff);
		data[offset + 1] = (byte) ((value >> 16) & 0xff);
	}

	public static final void writeUnsignedShort(byte[] data,Offset offset,int value) {
		try {
			writeUnsignedShort(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 2;
		}
	}

	public static final void writeInt(byte[] data,int offset,int value) {
		data[offset + 0] = (byte) ((value >> 24) & 0xff);
		data[offset + 1] = (byte) ((value >> 16) & 0xff);
		data[offset + 2] = (byte) ((value >> 8) & 0xff);
		data[offset + 3] = (byte) ((value >> 0) & 0xff);
	}

	public static final void writeInt(byte[] data,Offset offset,int value) {
		try {
			writeInt(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void write(byte[] data,int offset,int value) {
		writeInt(data, offset, value);
	}

	public static final void write(byte[] data,Offset offset,int value) {
		writeInt(data, offset, value);
	}

	public static final int readInt(byte[] data,int offset) {
		return (((data[offset + 0] & 0xff) << 24) + ((data[offset + 1] & 0xff) << 16) + ((data[offset + 2] & 0xff) << 8) + ((data[offset + 3] & 0xff) << 0));
	}

	public static final int readInt(byte[] data,Offset offset) {
		try {
			return readInt(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final void writeLong(byte[] data,int offset,long value) {
		data[offset + 0] = (byte) ((value >> 56) & 0xff);
		data[offset + 1] = (byte) ((value >> 48) & 0xff);
		data[offset + 2] = (byte) ((value >> 40) & 0xff);
		data[offset + 3] = (byte) ((value >> 32) & 0xff);
		data[offset + 4] = (byte) ((value >> 24) & 0xff);
		data[offset + 5] = (byte) ((value >> 16) & 0xff);
		data[offset + 6] = (byte) ((value >> 8) & 0xff);
		data[offset + 7] = (byte) ((value >> 0) & 0xff);
	}

	public static final void writeLong(byte[] data,Offset offset,long value) {
		try {
			writeLong(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void write(byte[] data,int offset,long value) {
		writeLong(data, offset, value);
	}

	public static final void write(byte[] data,Offset offset,long value) {
		writeLong(data, offset, value);
	}

	public static final long readLong(byte[] data,int offset) {
		long high = ((data[offset + 0] & 0xff) << 24) + ((data[offset + 1] & 0xff) << 16) + ((data[offset + 2] & 0xff) << 8) + ((data[offset + 3] & 0xff) << 0);
		long low = ((data[offset + 4] & 0xff) << 24) + ((data[offset + 5] & 0xff) << 16) + ((data[offset + 6] & 0xff) << 8) + ((data[offset + 7] & 0xff) << 0);
		return (high << 32) + (0xffffffffL & low);
	}

	public static final long readLong(byte[] data,Offset offset) {
		try {
			return readLong(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeFloat(byte[] data,int offset,float value) {
		writeInt(data, offset, Float.floatToIntBits(value));
	}

	public static final void writeFloat(byte[] data,Offset offset,float value) {
		try {
			writeFloat(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void write(byte[] data,int offset,float value) {
		writeFloat(data, offset, value);
	}

	public static final void write(byte[] data,Offset offset, float value) {
		writeFloat(data, offset, value);
	}

	public static final float readFloat(byte[] data,int offset) {
		return Float.intBitsToFloat(readInt(data, offset));
	}

	public static final float readFloat(byte[] data,Offset offset) {
		try {
			return readFloat(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final void writeDouble(byte[] data,int offset,double value) {
		writeLong(data, offset, Double.doubleToLongBits(value));
	}

	public static final void writeDouble(byte[] data,Offset offset,double value) {
		try {
			writeDouble(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void write(byte[] data,int offset,double value) {
		writeDouble(data, offset, value);
	}

	public static final void write(byte[] data,Offset offset,double value) {
		writeDouble(data, offset, value);
	}

	public static final void writeStr(byte[] data, int offset,String value) throws IOException {
		char[] chs = value.toCharArray();
		writeInt(data, offset, chs.length);
		offset += 4;
		for (char c : chs) {
			writeChar(data, offset, c);
			offset += 2;
		}

	}

	public static final String readStr(byte[] data, int offset) throws IOException {
		int len = readInt(data, offset);
		offset += 4;
		char[] chs = new char[len];
		for (int n = 0; n < len; n++) {
			chs[n] = readChar(data, offset);
			offset += 2;
		}

		return new String(chs);
	}

	public static final double readDouble(byte[] data,int offset) {
		return Double.longBitsToDouble(readLong(data, offset));
	}

	public static final double readDouble(byte[] data,Offset offset) {
		return Double.longBitsToDouble(readLong(data, offset));
	}

	public static final void writeBytes(OutputStream output,byte[] value) throws IOException {
		output.write(value);
	}

	public static final byte[] readFully(InputStream input,int len) throws IOException {
		byte result[] = new byte[len];
		return readFully(input, result, 0, len);
	}

	public static final byte[] readFully(InputStream input,byte result[]) throws IOException {
		readFully(input, result, 0, result.length);
		return result;
	}

	public static final byte[] readFully(InputStream input,byte result[],int off,int len) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = input.read(result, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
		return result;
	}

	public static final int readBytes(InputStream input,int len) throws IOException {
		byte[] b = new byte[len];
		readFully(input, b);
		return len;
	}

	public static final void writeStr(OutputStream output,String value) throws IOException {
		char[] chs = value.toCharArray();
		writeInt(output, chs.length);
		for (char c : chs)
			writeChar(output, c);

	}

	public static final String readStr(InputStream input) throws IOException {
		int len = readInt(input);
		char[] chs = new char[len];
		for (int n = 0; n < len; n++)
			chs[n] = readChar(input);

		return new String(chs);
	}

	public static final void writeUTF8(OutputStream output,String value) throws IOException {
		byte[] b = value.getBytes(Charset.forName("UTF-8"));
		writeInt(output, b.length);
		output.write(b);
	}

	public static final String readUTF8(InputStream input) throws IOException {
		int len = readInt(input);
		byte[] b = new byte[len];
		input.read(b);
		return new String(b, Charset.forName("UTF-8"));
	}

	public static final void writeBool(OutputStream output,boolean value) throws IOException {
		output.write((byte) (value ? 1 : 0));
	}

	public static final boolean readBool(InputStream input) throws IOException {
		return (read(input) == 1) ? true : false;
	}

	public static final void writeByte(OutputStream output,int value) throws IOException {
		output.write((byte) (value) & 0xff);
	}

	public static final int readByte(InputStream input) throws IOException {
		return read(input);
	}

	public static final void writeChar(OutputStream output,int v) throws IOException {
		output.write((v >>> 8) & 0xFF);
		output.write((v >>> 0) & 0xFF);
	}

	public static final char readChar(InputStream input) throws IOException {
		int ch1 = input.read();
		int ch2 = input.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	public static final void writeShort(OutputStream output,short value) throws IOException {
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 0) & 0xff));
	}

	public static final short readShort(InputStream input) throws IOException {
		return (short) (((read(input) & 0xff) << 8) + ((read(input) & 0xff) << 0));
	}

	public static final void writeInt(OutputStream output,int value) throws IOException {
		output.write((byte) ((value >> 24) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 0) & 0xff));
	}

	public static final int readInt(InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);
		int value3 = read(input);
		int value4 = read(input);

		return ((value1 & 0xff) << 24) + ((value2 & 0xff) << 16) + ((value3 & 0xff) << 8) + ((value4 & 0xff) << 0);
	}

	public static final void writeLong(OutputStream output,long value) throws IOException {
		output.write((byte) ((value >> 56) & 0xff));
		output.write((byte) ((value >> 48) & 0xff));
		output.write((byte) ((value >> 40) & 0xff));
		output.write((byte) ((value >> 32) & 0xff));
		output.write((byte) ((value >> 24) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 0) & 0xff));
	}

	public static final long readLong(InputStream input) throws IOException {
		byte[] bytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < 8; i++) {
			bytes[i] = (byte) read(input);
		}
		return readLong(bytes, 0);
	}

	public static final void writeFloat(OutputStream output,float value) throws IOException {
		writeInt(output, Float.floatToIntBits(value));
	}

	public static final float readFloat(InputStream input) throws IOException {
		return Float.intBitsToFloat(readInt(input));
	}

	public static final void writeDouble(OutputStream output,double value) throws IOException {
		writeLong(output, Double.doubleToLongBits(value));
	}

	public static final double readDouble(InputStream input) throws IOException {
		return Double.longBitsToDouble(readLong(input));
	}

	public final int readUnsignedByte(InputStream input) throws IOException {
		int ch = read(input);
		if (ch < 0)
			throw new EOFException();
		return ch & 0xff;
	}

	public static final int readUnsignedShort(InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);

		return (((value1 & 0xff) << 8) + ((value2 & 0xff) << 0));
	}

	public static final void writeUnsignedShort(OutputStream os,int value) throws IOException {
		byte[] data = new byte[2];
		data[0] = (byte) ((value >> 8) & 0xff);
		data[1] = (byte) ((value >> 0) & 0xff);
		os.write(data);
	}

	public static final String fixNInt(int v,int n) {
		return StrEx.fixNInt(v, n);
	}

	public static final short swapShort(short value) {
		return (short) ((((value >> 0) & 0xff) << 8) + (((value >> 8) & 0xff) << 0));
	}

	public static final int swapInteger(int value) {
		return (((value >> 0) & 0xff) << 24) + (((value >> 8) & 0xff) << 16) + (((value >> 16) & 0xff) << 8) + (((value >> 24) & 0xff) << 0);
	}

	public static final long swapLong(long value) {
		return (((value >> 0) & 0xff) << 56) + (((value >> 8) & 0xff) << 48) + (((value >> 16) & 0xff) << 40) + (((value >> 24) & 0xff) << 32) + (((value >> 32) & 0xff) << 24)
				+ (((value >> 40) & 0xff) << 16) + (((value >> 48) & 0xff) << 8) + (((value >> 56) & 0xff) << 0);
	}

	public static final float swapFloat(float value) {
		return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
	}

	public static final double swapDouble(double value) {
		return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
	}

	public static final void writeSwappedShort(byte[] data,int offset,short value) {
		data[offset + 0] = (byte) ((value >> 0) & 0xff);
		data[offset + 1] = (byte) ((value >> 8) & 0xff);
	}

	public static final void writeSwappedShort(byte[] data,Offset offset,short value) {
		try {
			writeSwappedShort(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 2;
		}
	}

	public static final void writeSwapped(byte[] data,int offset,short value) {
		writeSwappedShort(data, offset, value);
	}

	public static final void writeSwapped(byte[] data,Offset offset,short value) {
		writeSwappedShort(data, offset, value);
	}

	public static final short readSwappedShort(byte[] data,int offset) {
		return (short) (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8));
	}

	public static final short readSwappedShort(byte[] data,Offset offset) {
		try {
			return readSwappedShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final int readSwappedUnsignedShort(byte[] data,int offset) {
		return (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8));
	}

	public static final int readSwappedUnsignedShort(byte[] data,Offset offset) {
		try {
			return readSwappedUnsignedShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final void writeSwappedInteger(byte[] data,int offset, int value) {
		data[offset + 0] = (byte) ((value >> 0) & 0xff);
		data[offset + 1] = (byte) ((value >> 8) & 0xff);
		data[offset + 2] = (byte) ((value >> 16) & 0xff);
		data[offset + 3] = (byte) ((value >> 24) & 0xff);
	}

	public static final void writeSwappedInteger(byte[] data,Offset offset,int value) {
		try {
			writeSwappedInteger(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void writeSwapped(byte[] data,int offset,int value) {
		writeSwappedInteger(data, offset, value);
	}

	public static final void writeSwapped(byte[] data,Offset offset,int value) {
		writeSwappedInteger(data, offset, value);
	}

	public static final int readSwappedInteger(byte[] data,int offset) {
		return (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8) + ((data[offset + 2] & 0xff) << 16) + ((data[offset + 3] & 0xff) << 24));
	}

	public static final int readSwappedInteger(byte[] data,Offset offset) {
		try {
			return readSwappedInteger(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final long readSwappedUnsignedInteger(byte[] data,int offset) {
		long low = (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8) + ((data[offset + 2] & 0xff) << 16));
		long high = data[offset + 3] & 0xff;
		return (high << 24) + (0xffffffffL & low);
	}

	public static final long readSwappedUnsignedInteger(byte[] data,Offset offset) {
		try {
			return readSwappedUnsignedInteger(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeSwappedLong(byte[] data,int offset,long value) {
		data[offset + 0] = (byte) ((value >> 0) & 0xff);
		data[offset + 1] = (byte) ((value >> 8) & 0xff);
		data[offset + 2] = (byte) ((value >> 16) & 0xff);
		data[offset + 3] = (byte) ((value >> 24) & 0xff);
		data[offset + 4] = (byte) ((value >> 32) & 0xff);
		data[offset + 5] = (byte) ((value >> 40) & 0xff);
		data[offset + 6] = (byte) ((value >> 48) & 0xff);
		data[offset + 7] = (byte) ((value >> 56) & 0xff);
	}

	public static final void writeSwappedLong(byte[] data,Offset offset,long value) {
		try {
			writeSwappedLong(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void writeSwapped(byte[] data,int offset,long value) {
		writeSwappedLong(data, offset, value);
	}

	public static final void writeSwapped(byte[] data,Offset offset,long value) {
		writeSwappedLong(data, offset, value);
	}

	public static final long readSwappedLong(byte[] data,int offset) {
		long low = ((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8) + ((data[offset + 2] & 0xff) << 16) + ((data[offset + 3] & 0xff) << 24);
		long high = ((data[offset + 4] & 0xff) << 0) + ((data[offset + 5] & 0xff) << 8) + ((data[offset + 6] & 0xff) << 16) + ((data[offset + 7] & 0xff) << 24);
		return (high << 32) + (0xffffffffL & low);
	}

	public static final long readSwappedLong(byte[] data,Offset offset) {
		try {
			return readSwappedLong(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeSwappedFloat(byte[] data,int offset,float value) {
		writeSwappedInteger(data, offset, Float.floatToIntBits(value));
	}

	public static final void writeSwappedFloat(byte[] data,Offset offset,float value) {
		try {
			writeSwappedFloat(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void writeSwapped(byte[] data,int offset,float value) {
		writeSwappedFloat(data, offset, value);
	}

	public static final void writeSwapped(byte[] data,Offset offset,float value) {
		writeSwappedFloat(data, offset, value);
	}

	public static final float readSwappedFloat(byte[] data,int offset) {
		return Float.intBitsToFloat(readSwappedInteger(data, offset));
	}

	public static final float readSwappedFloat(byte[] data,Offset offset) {
		try {
			return readSwappedFloat(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final void writeSwappedDouble(byte[] data,int offset,double value) {
		writeSwappedLong(data, offset, Double.doubleToLongBits(value));
	}

	public static final void writeSwappedDouble(byte[] data,Offset offset,double value) {
		try {
			writeSwappedDouble(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void writeSwapped(byte[] data,int offset,double value) {
		writeSwappedDouble(data, offset, value);
	}

	public static final void writeSwapped(byte[] data,Offset offset,double value) {
		writeSwappedDouble(data, offset, value);
	}

	public static final double readSwappedDouble(byte[] data,int offset) {
		return Double.longBitsToDouble(readSwappedLong(data, offset));
	}

	public static final double readSwappedDouble(byte[] data,Offset offset) {
		try {
			return readSwappedDouble(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeSwappedShort(OutputStream output,short value) throws IOException {
		output.write((byte) ((value >> 0) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
	}

	public static final short readSwappedShort(InputStream input) throws IOException {
		return (short) (((read(input) & 0xff) << 0) + ((read(input) & 0xff) << 8));
	}

	public static final int readSwappedUnsignedShort(InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);

		return (((value1 & 0xff) << 0) + ((value2 & 0xff) << 8));
	}

	public static final void writeSwappedInteger(OutputStream output,int value) throws IOException {
		output.write((byte) ((value >> 0) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 24) & 0xff));
	}

	public static final int readSwappedInteger(InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);
		int value3 = read(input);
		int value4 = read(input);

		return ((value1 & 0xff) << 0) + ((value2 & 0xff) << 8) + ((value3 & 0xff) << 16) + ((value4 & 0xff) << 24);
	}

	public static final long readSwappedUnsignedInteger(InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);
		int value3 = read(input);
		int value4 = read(input);

		long low = (((value1 & 0xff) << 0) + ((value2 & 0xff) << 8) + ((value3 & 0xff) << 16));

		long high = value4 & 0xff;

		return (high << 24) + (0xffffffffL & low);
	}

	public static final void writeSwappedLong(OutputStream output,long value) throws IOException {
		output.write((byte) ((value >> 0) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 24) & 0xff));
		output.write((byte) ((value >> 32) & 0xff));
		output.write((byte) ((value >> 40) & 0xff));
		output.write((byte) ((value >> 48) & 0xff));
		output.write((byte) ((value >> 56) & 0xff));
	}

	public static final long readSwappedLong(InputStream input) throws IOException {
		byte[] bytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
		readFully(input, bytes);
		// for (int i = 0; i < 8; i++) {
		// bytes[i] = (byte) read(input);
		// }
		return readSwappedLong(bytes, 0);
	}

	public static final void writeSwappedFloat(OutputStream output,float value) throws IOException {
		writeSwappedInteger(output, Float.floatToIntBits(value));
	}

	public static final float readSwappedFloat(InputStream input) throws IOException {
		return Float.intBitsToFloat(readSwappedInteger(input));
	}

	public static final void writeSwappedDouble(OutputStream output, double value) throws IOException {
		writeSwappedLong(output, Double.doubleToLongBits(value));
	}

	public static final double readSwappedDouble(InputStream input) throws IOException {
		return Double.longBitsToDouble(readSwappedLong(input));
	}

	// 计算距离
	public static final int distance(int x1,int y1,int x2,int y2) {
		double v = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		return (int) v;
	}

	// 计算百分率
	public static final int percent(double v,double max) {
		if (v <= 0 || max <= 0)
			return 0;
		int r = (int) (v * 100 / max);
		return r > 100 ? 100 : r;
	}

	public static final double longBitsToDouble(long bits) {
		int sign = ((bits >> 63) == 0) ? 1 : -1;
		int exponent = (int) ((bits >> 52) & 0x7ffL);
		long mantissa = (exponent == 0) ? (bits & 0xfffffffffffffL) << 1 : (bits & 0xfffffffffffffL) | 0x10000000000000L;
		double result = sign * mantissa * Math.pow(2, exponent - 1075);
		return result;
	}

	public static final double POSITIVE_INFINITY = 1.0 / 0.0;
	public static final double NEGATIVE_INFINITY = -1.0 / 0.0;

	static public boolean isNaN(double v) {
		return (v != v);
	}

	public static final long abs(long a) {
		return (a < 0) ? -a : a;
	}

	public static final double abs(double a) {
		return (a <= 0.0D) ? 0.0D - a : a;
	}

	public static final long doubleToLongBits(double v) {
		long bits = (long) v;
		long total = 0;
		if (v == 0.0) {
			total = 0;
		} else {
			if (isNaN(v)) {
				total = 0x7ff8000000000000L;
			} else if (v == POSITIVE_INFINITY) {
				total = 0x7ff0000000000000L;
			} else if (v == NEGATIVE_INFINITY) {
				total = 0xfff0000000000000L;
			} else {
				if (bits < 0) {
					total = 0x8000000000000000L;
					bits = abs(bits);
					v = abs(v);
				}
				int lastExponent = 0;
				while (bits >> lastExponent > 0) {
					lastExponent++;
				}
				long exp = (lastExponent + 1022) * (long) Math.pow(2, 52) + 1;
				double man = v / (Math.pow(2, lastExponent - 1)) - 1;
				int count = 52;
				PStr _out = PStr.begin();
				// StringBuilder _out = new StringBuilder();
				while (man > 0 && count > 0) {
					man = man * 2;
					if (man > 1) {
						man--;
						_out.a("1");
					} else {
						_out.a("0");
					}
					count--;
				}
				// long manLong = _out.length() == 0 ? 0 : Long.valueOf(
				// _out.toString(), 2);
				long manLong = _out.length() == 0 ? 0 : Long.valueOf(_out.end(), 2);
				total += exp + manLong;
			}
		}
		return total;
	}

	public static final Integer[] toArray(List<Integer> list) {
		if (list == null || list.size() <= 0)
			return new Integer[0];
		return list.toArray(new Integer[list.size()]);
	}

	public static final byte[] toBytes(byte v) {
		byte[] r2 = { 0 };
		r2[0] = v;
		return r2;
	}

	public static final byte[] toBytes(short v) {
		byte[] r2 = { 0, 0 };
		writeShort(r2, 0, v);
		return r2;
	}

	public static final byte[] toBytes(int v) {
		byte[] r2 = { 0, 0, 0, 0 };
		writeInt(r2, 0, v);
		return r2;
	}

	public static final byte[] toBytes(long v) {
		byte[] r2 = { 0, 0, 0, 0, 0, 0, 0, 0 };
		writeLong(r2, 0, v);
		return r2;
	}

	public static final byte[] toBytes(float v) {
		byte[] r2 = { 0, 0, 0, 0 };
		writeFloat(r2, 0, v);
		return r2;
	}

	public static final byte[] toBytes(double v) {
		byte[] r2 = { 0, 0, 0, 0, 0, 0, 0, 0 };
		writeDouble(r2, 0, v);
		return r2;
	}

	public static final byte toByte(byte[] v) {
		return v[0];
	}

	public static final short toShort(byte[] v) {
		return readShort(v, 0);
	}

	public static final int toInt(byte[] v) {
		return readInt(v, 0);
	}

	public static final long toLong(byte[] v) {
		return readLong(v, 0);
	}

	public static final float toFloat(byte[] v) {
		return readFloat(v, 0);
	}

	public static final double toDouble(byte[] v) {
		return readDouble(v, 0);
	}

	static final public int toInt32(byte[] bytes, int index, boolean isHigh2Low) {
		if (isHigh2Low) {
			return (int) ((int) (0xff & bytes[index]) << 56 | (int) (0xff & bytes[index + 1]) << 48 | (int) (0xff & bytes[index + 2]) << 40 | (int) (0xff & bytes[index + 3]) << 32);
		}
		return (int) ((int) (0xff & bytes[index]) << 32 | (int) (0xff & bytes[index + 1]) << 40 | (int) (0xff & bytes[index + 2]) << 48 | (int) (0xff & bytes[index + 3]) << 56);
	}

	static final public int toInt32CShapreBase64(String strCs64) {
		byte[] buf = DatatypeConverter.parseBase64Binary(strCs64);
		return toInt32(buf, 0, false);
	}

	// //////////////////////
	// 空间转换
	public static final int KB(long nb) {
		return (int) (nb / KB);
	}

	public static final int MB(long nb) {
		return (int) (nb / MB);
	}

	public static final int GB(long nb) {
		return (int) (nb / GB);
	}

	public static final int TB(long nb) {
		return (int) (nb / TB);
	}

	public static final int PB(long nb) {
		return (int) (nb / PB);
	}
	// //////////////////////

	public static void main(String[] args) {
		double v1 = -2321311.1232;
		System.out.println(Double.doubleToLongBits(v1));
		System.out.println(Double.longBitsToDouble(Double.doubleToLongBits(v1)));
		System.out.println(doubleToLongBits(v1));
		System.out.println(doubleToLongBits(v1));
		System.out.println(longBitsToDouble(doubleToLongBits(v1)));
	}
}
