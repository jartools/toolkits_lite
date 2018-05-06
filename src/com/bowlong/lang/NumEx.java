package com.bowlong.lang;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.bowlong.util.ListEx;

public final class NumEx {
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

	public static final int KB = 1024;
	public static final int MB = 1024 * KB;
	public static final int GB = 1024 * MB;
	public static final int TB = 1024 * GB;
	public static final int PB = 1024 * TB;

	//
	public static final boolean stringToBool(final String s, final boolean v) {
		try {
			if("0".equals(s)){
				return false;
			}
			if("1".equals(s)){
				return true;
			}
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			return v;
		}
	}

	public static final boolean stringToBool(final String s) {
		return stringToBool(s, false);
	}

	public static final byte stringToByte(final String s, final byte v) {
		try {
			return Byte.parseByte(s);
		} catch (Exception e) {
			return v;
		}
	}

	public static final byte stringToByte(final String s) {
		return stringToByte(s, (byte) 0);
	}

	public static final byte[] stringToByte(final String[] v) {
		byte[] r = new byte[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToByte(s);
			n++;
		}
		return r;
	}

	public static final short stringToShort(final String s, final short v) {
		try {
			if (isDouble(s))
				return (short) stringToDouble(s, v);
			return Short.parseShort(s);
		} catch (Exception e) {
			return v;
		}
	}

	public static final short stringToShort(final String s) {
		return stringToShort(s, (short) 0);
	}

	public static final short[] stringToShort(final String[] v) {
		short[] r = new short[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToShort(s);
			n++;
		}
		return r;
	}

	public static final int stringToInt(final String s, final int v) {
		try {
			if (s == null)
				return v;

			if (isDouble(s))
				return (int) stringToDouble(s, v);
			return Integer.parseInt(s);
		} catch (Exception e) {
			return v;
		}
	}

	public static final int stringToInt(final String s) {
		return stringToInt(s, 0);
	}

	public static int[] stringToInt(final String[] v) {
		int[] r = new int[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToInt(s);
			n++;
		}
		return r;
	}

	public static final long stringToLong(final String s, final long v) {
		try {
			if (s == null)
				return v;

			if (isDouble(s))
				return (long) stringToDouble(s, v);

			return Long.parseLong(s);
		} catch (Exception e) {
			return v;
		}
	}

	public static final long stringToLong(final String s) {
		return stringToLong(s, 0);
	}

	public static final long[] stringToLong(final String[] v) {
		long[] r = new long[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToLong(s);
			n++;
		}
		return r;
	}

	public static final float stringToFloat(final String s, final float v) {
		try {
			if (s == null)
				return v;
			String s2 = s.trim();
			return Float.parseFloat(s2);
		} catch (Exception e) {
			return v;
		}
	}

	public static final float stringToFloat(final String s) {
		return stringToFloat(s, (float) 0.0);
	}

	public static final float[] stringToFloat(final String[] v) {
		float[] r = new float[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToFloat(s);
			n++;
		}
		return r;
	}

	public static final double stringToDouble(final String s, final double v) {
		try {
			if (s == null)
				return v;
			String s2 = s.trim();
			return Double.parseDouble(s2);
		} catch (Exception e) {
			return v;
		}
	}

	public static final double stringToDouble(final String s) {
		return stringToDouble(s, 0.0);
	}

	public static final double[] stringToDouble(final String[] v) {
		double[] r = new double[v.length];
		int n = 0;
		for (String s : v) {
			r[n] = stringToDouble(s);
			n++;
		}
		return r;
	}

	private static final int read(final InputStream input) throws IOException {
		int value = input.read();
		if (-1 == value)
			throw new EOFException("Unexpected EOF reached");
		return value;
	}

	public static final void writeBytes(final byte[] data, final int offset, final byte[] value) {
		System.arraycopy(value, 0, data, offset, value.length);
	}

	public static final void writeBytes(final byte[] data, final Offset offset, final byte[] value) {
		try {
			writeBytes(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += value.length;
		}
	}

	public static final byte[] readBytes(final byte[] data, final int offset, final int len) {
		return ByteEx.subBytes(data, offset, len);
	}

	public static final byte[] readBytes(final byte[] data, final Offset offset, final int len) {
		try {
			return readBytes(data, offset.reader, len);
		} catch (Exception e) {
		} finally {
			offset.reader += len;
		}
		return new byte[0];
	}

	public static final void writeUTF8(final byte[] data, final int offset, final String value) {
		byte[] b = value.getBytes(Charset.forName("UTF-8"));
		writeInt(data, offset, b.length);
		writeBytes(data, offset + 4, b);
	}

	public static final void writeUTF8(final byte[] data, final Offset offset, final String value) {
		try {
			byte[] b = value.getBytes(Charset.forName("UTF-8"));
			writeInt(data, offset, b.length);
			writeBytes(data, offset, b);
		} catch (Exception e) {
		} finally {
		}
	}

	public static final String readUTF8(final byte[] data, final int offset) {
		int len = readInt(data, offset);
		byte[] result = new byte[len];
		System.arraycopy(data, offset + 4, result, 0, len);
		return new String(result, Charset.forName("UTF-8"));
	}

	public static final String readUTF8(final byte[] data, final Offset offset) {
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

	public static final void writeBool(final byte[] data, final int offset, final boolean value) {
		data[offset + 0] = (byte) (value ? 0x01 : 0x00);
	}

	public static final void writeBool(final byte[] data, final Offset offset, final boolean value) {
		try {
			writeBool(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 1;
		}
	}

	public static final boolean readBool(final byte[] data, final int offset) {
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

	public static final void write(final byte[] data, final int offset, final boolean value) {
		writeBool(data, offset, value);
	}

	public static final void write(final byte[] data, final Offset offset, final boolean value) {
		writeBool(data, offset, value);
	}

	public static final void writeByte(final byte[] data, final int offset, final int value) {
		data[offset + 0] = (byte) value;
	}

	public static final void writeByte(final byte[] data, final Offset offset, final int value) {
		try {
			writeByte(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 1;
		}
	}

	public static final void writeChar(final byte[] data, final int offset, final int v) throws IOException {
		data[offset + 0] = (byte) ((v >>> 8) & 0xFF);
		data[offset + 1] = (byte) ((v >>> 0) & 0xFF);
	}

	public static final char readChar(final byte[] data, final int offset) throws IOException {
		int ch1 = (data[offset + 0] & 0xff);
		int ch2 = (data[offset + 1] & 0xff);
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	public static final byte readByte(final byte[] data, final int offset) {
		return (byte) ((data[offset + 0] & 0xff));
	}

	public static final byte readByte(final byte[] data, final Offset offset) {
		try {
			return readByte(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 1;
		}
		return 0;
	}

	public static final void write(final byte[] data, final int offset, final byte value) {
		writeByte(data, offset, value);
	}

	public static final void write(final byte[] data, final Offset offset, final byte value) {
		writeByte(data, offset, value);
	}

	public static final void writeShort(final byte[] data, final int offset, final short value) {
		data[offset + 0] = (byte) ((value >> 8) & 0xff);
		data[offset + 1] = (byte) ((value >> 0) & 0xff);
	}

	public static final void writeShort(final byte[] data, final Offset offset, final short value) {
		try {
			writeShort(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 2;
		}
	}

	public static final void write(final byte[] data, final int offset, final short value) {
		writeShort(data, offset, value);
	}

	public static final void write(final byte[] data, final Offset offset, final short value) {
		writeShort(data, offset, value);
	}

	public static final short readShort(final byte[] data, final int offset) {
		return (short) (((data[offset + 0] & 0xff) << 8) + ((data[offset + 1] & 0xff) << 0));
	}

	public static final short readShort(final byte[] data, final Offset offset) {
		try {
			return readShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final int readUnsignedShort(final byte[] data, final int offset) {
		return (((data[offset + 0] & 0xff) << 8) + ((data[offset + 1] & 0xff) << 0));
	}

	public static final int readUnsignedShort(final byte[] data, final Offset offset) {
		try {
			return readUnsignedShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final void writeUnsignedShort(final byte[] data, final int offset, int value) {
		data[offset + 0] = (byte) ((value >> 24) & 0xff);
		data[offset + 1] = (byte) ((value >> 16) & 0xff);
	}

	public static final void writeUnsignedShort(final byte[] data, final Offset offset, final int value) {
		try {
			writeUnsignedShort(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 2;
		}
	}

	public static final void writeInt(final byte[] data, final int offset, final int value) {
		data[offset + 0] = (byte) ((value >> 24) & 0xff);
		data[offset + 1] = (byte) ((value >> 16) & 0xff);
		data[offset + 2] = (byte) ((value >> 8) & 0xff);
		data[offset + 3] = (byte) ((value >> 0) & 0xff);
	}

	public static final void writeInt(final byte[] data, final Offset offset, final int value) {
		try {
			writeInt(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void write(final byte[] data, final int offset, final int value) {
		writeInt(data, offset, value);
	}

	public static final void write(final byte[] data, final Offset offset, final int value) {
		writeInt(data, offset, value);
	}

	public static final int readInt(final byte[] data, final int offset) {
		return (((data[offset + 0] & 0xff) << 24) + ((data[offset + 1] & 0xff) << 16) + ((data[offset + 2] & 0xff) << 8)
				+ ((data[offset + 3] & 0xff) << 0));
	}

	public static final int readInt(final byte[] data, final Offset offset) {
		try {
			return readInt(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final void writeLong(final byte[] data, final int offset, final long value) {
		data[offset + 0] = (byte) ((value >> 56) & 0xff);
		data[offset + 1] = (byte) ((value >> 48) & 0xff);
		data[offset + 2] = (byte) ((value >> 40) & 0xff);
		data[offset + 3] = (byte) ((value >> 32) & 0xff);
		data[offset + 4] = (byte) ((value >> 24) & 0xff);
		data[offset + 5] = (byte) ((value >> 16) & 0xff);
		data[offset + 6] = (byte) ((value >> 8) & 0xff);
		data[offset + 7] = (byte) ((value >> 0) & 0xff);
	}

	public static final void writeLong(final byte[] data, final Offset offset, final long value) {
		try {
			writeLong(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void write(final byte[] data, final int offset, final long value) {
		writeLong(data, offset, value);
	}

	public static final void write(final byte[] data, final Offset offset, final long value) {
		writeLong(data, offset, value);
	}

	public static final long readLong(final byte[] data, final int offset) {
		long high = ((data[offset + 0] & 0xff) << 24) + ((data[offset + 1] & 0xff) << 16)
				+ ((data[offset + 2] & 0xff) << 8) + ((data[offset + 3] & 0xff) << 0);
		long low = ((data[offset + 4] & 0xff) << 24) + ((data[offset + 5] & 0xff) << 16)
				+ ((data[offset + 6] & 0xff) << 8) + ((data[offset + 7] & 0xff) << 0);
		return (high << 32) + (0xffffffffL & low);
	}

	public static final long readLong(final byte[] data, final Offset offset) {
		try {
			return readLong(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeFloat(final byte[] data, final int offset, final float value) {
		writeInt(data, offset, Float.floatToIntBits(value));
	}

	public static final void writeFloat(final byte[] data, final Offset offset, final float value) {
		try {
			writeFloat(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void write(final byte[] data, final int offset, final float value) {
		writeFloat(data, offset, value);
	}

	public static final void write(final byte[] data, final Offset offset, float value) {
		writeFloat(data, offset, value);
	}

	public static final float readFloat(final byte[] data, final int offset) {
		return Float.intBitsToFloat(readInt(data, offset));
	}

	public static final float readFloat(final byte[] data, final Offset offset) {
		try {
			return readFloat(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final void writeDouble(final byte[] data, final int offset, final double value) {
		writeLong(data, offset, Double.doubleToLongBits(value));
	}

	public static final void writeDouble(final byte[] data, final Offset offset, final double value) {
		try {
			writeDouble(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void write(final byte[] data, final int offset, final double value) {
		writeDouble(data, offset, value);
	}

	public static final void write(final byte[] data, final Offset offset, final double value) {
		writeDouble(data, offset, value);
	}

	public static final void writeStr(final byte[] data, int offset, final String value) throws IOException {
		char[] chs = value.toCharArray();
		writeInt(data, offset, chs.length);
		offset += 4;
		for (char c : chs) {
			writeChar(data, offset, c);
			offset += 2;
		}

	}

	public static final String readStr(final byte[] data, int offset) throws IOException {
		int len = readInt(data, offset);
		offset += 4;
		char[] chs = new char[len];
		for (int n = 0; n < len; n++) {
			chs[n] = readChar(data, offset);
			offset += 2;
		}

		return new String(chs);
	}

	public static final double readDouble(final byte[] data, final int offset) {
		return Double.longBitsToDouble(readLong(data, offset));
	}

	public static final double readDouble(final byte[] data, final Offset offset) {
		return Double.longBitsToDouble(readLong(data, offset));
	}

	public static final void writeBytes(final OutputStream output, final byte[] value) throws IOException {
		output.write(value);
	}

	public static final byte[] readFully(final InputStream input, final int len) throws IOException {
		final int off = 0;
		final byte result[] = new byte[len];
		return readFully(input, result, off, len);
	}

	public static final byte[] readFully(final InputStream input, final byte result[]) throws IOException {
		final int off = 0;
		final int len = result.length;
		readFully(input, result, off, len);
		return result;
	}

	public static final byte[] readFully(final InputStream input, final byte result[], final int off, final int len)
			throws IOException {
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

	public static final int readBytes(final InputStream input, final int len) throws IOException {
		byte[] b = new byte[len];
		readFully(input, b);
		return len;
	}

	public static final void writeStr(final OutputStream output, final String value) throws IOException {
		char[] chs = value.toCharArray();
		writeInt(output, chs.length);
		for (char c : chs)
			writeChar(output, c);

	}

	public static final String readStr(final InputStream input) throws IOException {
		int len = readInt(input);
		char[] chs = new char[len];
		for (int n = 0; n < len; n++)
			chs[n] = readChar(input);

		return new String(chs);
	}

	public static final void writeUTF8(final OutputStream output, final String value) throws IOException {
		byte[] b = value.getBytes(Charset.forName("UTF-8"));
		writeInt(output, b.length);
		output.write(b);
	}

	public static final String readUTF8(final InputStream input) throws IOException {
		int len = readInt(input);
		byte[] b = new byte[len];
		input.read(b);
		return new String(b, Charset.forName("UTF-8"));
	}

	public static final void writeBool(final OutputStream output, final boolean value) throws IOException {
		output.write((byte) (value ? 1 : 0));
	}

	public static final boolean readBool(final InputStream input) throws IOException {
		return (read(input) == 1) ? true : false;
	}

	public static final void writeByte(final OutputStream output, final int value) throws IOException {
		output.write((byte) (value) & 0xff);
	}

	public static final int readByte(final InputStream input) throws IOException {
		return read(input);
	}

	public static final void writeChar(final OutputStream output, final int v) throws IOException {
		output.write((v >>> 8) & 0xFF);
		output.write((v >>> 0) & 0xFF);
	}

	public static final char readChar(final InputStream input) throws IOException {
		int ch1 = input.read();
		int ch2 = input.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	public static final void writeShort(final OutputStream output, final short value) throws IOException {
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 0) & 0xff));
	}

	public static final short readShort(final InputStream input) throws IOException {
		return (short) (((read(input) & 0xff) << 8) + ((read(input) & 0xff) << 0));
	}

	public static final void writeInt(final OutputStream output, final int value) throws IOException {
		output.write((byte) ((value >> 24) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 0) & 0xff));
	}

	public static final int readInt(final InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);
		int value3 = read(input);
		int value4 = read(input);

		return ((value1 & 0xff) << 24) + ((value2 & 0xff) << 16) + ((value3 & 0xff) << 8) + ((value4 & 0xff) << 0);
	}

	public static final void writeLong(final OutputStream output, final long value) throws IOException {
		output.write((byte) ((value >> 56) & 0xff));
		output.write((byte) ((value >> 48) & 0xff));
		output.write((byte) ((value >> 40) & 0xff));
		output.write((byte) ((value >> 32) & 0xff));
		output.write((byte) ((value >> 24) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 0) & 0xff));
	}

	public static final long readLong(final InputStream input) throws IOException {
		byte[] bytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int i = 0; i < 8; i++) {
			bytes[i] = (byte) read(input);
		}
		return readLong(bytes, 0);
	}

	public static final void writeFloat(final OutputStream output, final float value) throws IOException {
		writeInt(output, Float.floatToIntBits(value));
	}

	public static final float readFloat(final InputStream input) throws IOException {
		return Float.intBitsToFloat(readInt(input));
	}

	public static final void writeDouble(final OutputStream output, final double value) throws IOException {
		writeLong(output, Double.doubleToLongBits(value));
	}

	public static final double readDouble(final InputStream input) throws IOException {
		return Double.longBitsToDouble(readLong(input));
	}

	public final int readUnsignedByte(final InputStream input) throws IOException {
		int ch = read(input);
		if (ch < 0)
			throw new EOFException();
		return ch & 0xff;
	}

	public static final int readUnsignedShort(final InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);

		return (((value1 & 0xff) << 8) + ((value2 & 0xff) << 0));
	}

	public static final void writeUnsignedShort(final OutputStream os, final int value) throws IOException {
		byte[] data = new byte[2];
		data[0] = (byte) ((value >> 8) & 0xff);
		data[1] = (byte) ((value >> 0) & 0xff);
		os.write(data);
	}

	public static final boolean isByte(final String s) {
		try {
			Byte.parseByte(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final boolean isShort(final String s) {
		try {
			Short.parseShort(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final boolean isInt(final String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final boolean isLong(final String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final boolean isFloat(final String s) {
		try {
			Float.parseFloat(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final boolean isDouble(final String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final String fix6Int(final int v) {
		return fixNInt(v, 6);
	}

	public static final String fixNInt(final int v, final int n) {
		return String.format("%0" + n + "d", v);
	}

	public static final String fix3Int(final int v) {
		return fixNInt(v, 3);
	}

	public static final short swapShort(final short value) {
		return (short) ((((value >> 0) & 0xff) << 8) + (((value >> 8) & 0xff) << 0));
	}

	public static final int swapInteger(final int value) {
		return (((value >> 0) & 0xff) << 24) + (((value >> 8) & 0xff) << 16) + (((value >> 16) & 0xff) << 8)
				+ (((value >> 24) & 0xff) << 0);
	}

	public static final long swapLong(final long value) {
		return (((value >> 0) & 0xff) << 56) + (((value >> 8) & 0xff) << 48) + (((value >> 16) & 0xff) << 40)
				+ (((value >> 24) & 0xff) << 32) + (((value >> 32) & 0xff) << 24) + (((value >> 40) & 0xff) << 16)
				+ (((value >> 48) & 0xff) << 8) + (((value >> 56) & 0xff) << 0);
	}

	public static final float swapFloat(final float value) {
		return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
	}

	public static final double swapDouble(final double value) {
		return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
	}

	public static final void writeSwappedShort(final byte[] data, final int offset, final short value) {
		data[offset + 0] = (byte) ((value >> 0) & 0xff);
		data[offset + 1] = (byte) ((value >> 8) & 0xff);
	}

	public static final void writeSwappedShort(final byte[] data, final Offset offset, final short value) {
		try {
			writeSwappedShort(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 2;
		}
	}

	public static final void writeSwapped(final byte[] data, final int offset, final short value) {
		writeSwappedShort(data, offset, value);
	}

	public static final void writeSwapped(final byte[] data, final Offset offset, final short value) {
		writeSwappedShort(data, offset, value);
	}

	public static final short readSwappedShort(final byte[] data, final int offset) {
		return (short) (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8));
	}

	public static final short readSwappedShort(final byte[] data, final Offset offset) {
		try {
			return readSwappedShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final int readSwappedUnsignedShort(final byte[] data, final int offset) {
		return (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8));
	}

	public static final int readSwappedUnsignedShort(final byte[] data, final Offset offset) {
		try {
			return readSwappedUnsignedShort(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 2;
		}
		return 0;
	}

	public static final void writeSwappedInteger(final byte[] data, final int offset, int value) {
		data[offset + 0] = (byte) ((value >> 0) & 0xff);
		data[offset + 1] = (byte) ((value >> 8) & 0xff);
		data[offset + 2] = (byte) ((value >> 16) & 0xff);
		data[offset + 3] = (byte) ((value >> 24) & 0xff);
	}

	public static final void writeSwappedInteger(final byte[] data, final Offset offset, final int value) {
		try {
			writeSwappedInteger(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void writeSwapped(final byte[] data, final int offset, final int value) {
		writeSwappedInteger(data, offset, value);
	}

	public static final void writeSwapped(final byte[] data, final Offset offset, final int value) {
		writeSwappedInteger(data, offset, value);
	}

	public static final int readSwappedInteger(final byte[] data, final int offset) {
		return (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8) + ((data[offset + 2] & 0xff) << 16)
				+ ((data[offset + 3] & 0xff) << 24));
	}

	public static final int readSwappedInteger(final byte[] data, final Offset offset) {
		try {
			return readSwappedInteger(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final long readSwappedUnsignedInteger(final byte[] data, final int offset) {
		long low = (((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8)
				+ ((data[offset + 2] & 0xff) << 16));
		long high = data[offset + 3] & 0xff;
		return (high << 24) + (0xffffffffL & low);
	}

	public static final long readSwappedUnsignedInteger(final byte[] data, final Offset offset) {
		try {
			return readSwappedUnsignedInteger(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeSwappedLong(final byte[] data, final int offset, final long value) {
		data[offset + 0] = (byte) ((value >> 0) & 0xff);
		data[offset + 1] = (byte) ((value >> 8) & 0xff);
		data[offset + 2] = (byte) ((value >> 16) & 0xff);
		data[offset + 3] = (byte) ((value >> 24) & 0xff);
		data[offset + 4] = (byte) ((value >> 32) & 0xff);
		data[offset + 5] = (byte) ((value >> 40) & 0xff);
		data[offset + 6] = (byte) ((value >> 48) & 0xff);
		data[offset + 7] = (byte) ((value >> 56) & 0xff);
	}

	public static final void writeSwappedLong(final byte[] data, final Offset offset, final long value) {
		try {
			writeSwappedLong(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void writeSwapped(final byte[] data, final int offset, final long value) {
		writeSwappedLong(data, offset, value);
	}

	public static final void writeSwapped(final byte[] data, final Offset offset, final long value) {
		writeSwappedLong(data, offset, value);
	}

	public static final long readSwappedLong(final byte[] data, final int offset) {
		long low = ((data[offset + 0] & 0xff) << 0) + ((data[offset + 1] & 0xff) << 8)
				+ ((data[offset + 2] & 0xff) << 16) + ((data[offset + 3] & 0xff) << 24);
		long high = ((data[offset + 4] & 0xff) << 0) + ((data[offset + 5] & 0xff) << 8)
				+ ((data[offset + 6] & 0xff) << 16) + ((data[offset + 7] & 0xff) << 24);
		return (high << 32) + (0xffffffffL & low);
	}

	public static final long readSwappedLong(final byte[] data, final Offset offset) {
		try {
			return readSwappedLong(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeSwappedFloat(final byte[] data, final int offset, final float value) {
		writeSwappedInteger(data, offset, Float.floatToIntBits(value));
	}

	public static final void writeSwappedFloat(final byte[] data, final Offset offset, final float value) {
		try {
			writeSwappedFloat(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 4;
		}
	}

	public static final void writeSwapped(final byte[] data, final int offset, final float value) {
		writeSwappedFloat(data, offset, value);
	}

	public static final void writeSwapped(final byte[] data, final Offset offset, final float value) {
		writeSwappedFloat(data, offset, value);
	}

	public static final float readSwappedFloat(final byte[] data, final int offset) {
		return Float.intBitsToFloat(readSwappedInteger(data, offset));
	}

	public static final float readSwappedFloat(final byte[] data, final Offset offset) {
		try {
			return readSwappedFloat(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 4;
		}
		return 0;
	}

	public static final void writeSwappedDouble(final byte[] data, final int offset, final double value) {
		writeSwappedLong(data, offset, Double.doubleToLongBits(value));
	}

	public static final void writeSwappedDouble(final byte[] data, final Offset offset, final double value) {
		try {
			writeSwappedDouble(data, offset.writer, value);
		} catch (Exception e) {
		} finally {
			offset.writer += 8;
		}
	}

	public static final void writeSwapped(final byte[] data, final int offset, final double value) {
		writeSwappedDouble(data, offset, value);
	}

	public static final void writeSwapped(final byte[] data, final Offset offset, final double value) {
		writeSwappedDouble(data, offset, value);
	}

	public static final double readSwappedDouble(final byte[] data, final int offset) {
		return Double.longBitsToDouble(readSwappedLong(data, offset));
	}

	public static final double readSwappedDouble(final byte[] data, final Offset offset) {
		try {
			return readSwappedDouble(data, offset.reader);
		} catch (Exception e) {
		} finally {
			offset.reader += 8;
		}
		return 0;
	}

	public static final void writeSwappedShort(final OutputStream output, final short value) throws IOException {
		output.write((byte) ((value >> 0) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
	}

	public static final short readSwappedShort(final InputStream input) throws IOException {
		return (short) (((read(input) & 0xff) << 0) + ((read(input) & 0xff) << 8));
	}

	public static final int readSwappedUnsignedShort(final InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);

		return (((value1 & 0xff) << 0) + ((value2 & 0xff) << 8));
	}

	public static final void writeSwappedInteger(final OutputStream output, final int value) throws IOException {
		output.write((byte) ((value >> 0) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 24) & 0xff));
	}

	public static final int readSwappedInteger(final InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);
		int value3 = read(input);
		int value4 = read(input);

		return ((value1 & 0xff) << 0) + ((value2 & 0xff) << 8) + ((value3 & 0xff) << 16) + ((value4 & 0xff) << 24);
	}

	public static final long readSwappedUnsignedInteger(final InputStream input) throws IOException {
		int value1 = read(input);
		int value2 = read(input);
		int value3 = read(input);
		int value4 = read(input);

		long low = (((value1 & 0xff) << 0) + ((value2 & 0xff) << 8) + ((value3 & 0xff) << 16));

		long high = value4 & 0xff;

		return (high << 24) + (0xffffffffL & low);
	}

	public static final void writeSwappedLong(final OutputStream output, final long value) throws IOException {
		output.write((byte) ((value >> 0) & 0xff));
		output.write((byte) ((value >> 8) & 0xff));
		output.write((byte) ((value >> 16) & 0xff));
		output.write((byte) ((value >> 24) & 0xff));
		output.write((byte) ((value >> 32) & 0xff));
		output.write((byte) ((value >> 40) & 0xff));
		output.write((byte) ((value >> 48) & 0xff));
		output.write((byte) ((value >> 56) & 0xff));
	}

	public static final long readSwappedLong(final InputStream input) throws IOException {
		byte[] bytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
		readFully(input, bytes);
		// for (int i = 0; i < 8; i++) {
		// bytes[i] = (byte) read(input);
		// }
		return readSwappedLong(bytes, 0);
	}

	public static final void writeSwappedFloat(final OutputStream output, final float value) throws IOException {
		writeSwappedInteger(output, Float.floatToIntBits(value));
	}

	public static final float readSwappedFloat(final InputStream input) throws IOException {
		return Float.intBitsToFloat(readSwappedInteger(input));
	}

	public static final void writeSwappedDouble(final OutputStream output, double value) throws IOException {
		writeSwappedLong(output, Double.doubleToLongBits(value));
	}

	public static final double readSwappedDouble(final InputStream input) throws IOException {
		return Double.longBitsToDouble(readSwappedLong(input));
	}
	
	// 计算距离
	public static final int distance(final int x1, final int y1, final int x2, final int y2) {
		double v = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		return (int) v;
	}

	public static final int limitMinMax(final int v, final int min, final int max) {
		final int v2 = v < min ? min : v;
		return v2 > max ? max : v2;
	}

	public static final int limitMax(final int v, final int max) {
		return v > max ? max : v;
	}

	public static final int limitMin(final int v, final int min) {
		return v < min ? min : v;
	}

	// 计算百分率
	public static final int percent(final double v, final double max) {
		if (v <= 0 || max <= 0)
			return 0;
		int r = (int) (v * 100 / max);
		return r > 100 ? 100 : r;
	}

	public static final int max(final int[] list) {
		int result = list[0];
		int size = list.length;
		for (int i = 1; i < size; i++) {
			int v = list[i];
			result = v > result ? v : result;
		}
		return result;
	}
	
	public static final int maxPars(int... arr) {
		int[] v = ListEx.toIntArray(arr);
		return max(v);
	}

	public static final int min(final int[] list) {
		int result = list[0];
		int size = list.length;
		for (int i = 1; i < size; i++) {
			int v = list[i];
			result = v < result ? v : result;
		}
		return result;
	}

	public static final int minPars(int... arr) {
		int[] v = ListEx.toIntArray(arr);
		return min(v);
	}

	public static final String nStr(final int n) {
		if (n < -1000000)
			return ((int) (n / 1000000)) + "百万";
		if (n < -10000)
			return ((int) (n / 10000)) + "万";
		if (n < -1000)
			return ((int) (n / 1000)) + "千";
		if (n < 0)
			return "" + n;
		else if (n < 1000)
			return "" + n;
		else if (n < 10000)
			return ((int) (n / 1000)) + "千";
		else if (n < 1000000)
			return ((int) (n / 10000)) + "万";
		else
			return ((int) (n / 1000000)) + "百万";
	}

	public static final double longBitsToDouble(final long bits) {
		int sign = ((bits >> 63) == 0) ? 1 : -1;
		int exponent = (int) ((bits >> 52) & 0x7ffL);
		long mantissa = (exponent == 0) ? (bits & 0xfffffffffffffL) << 1
				: (bits & 0xfffffffffffffL) | 0x10000000000000L;
		double result = sign * mantissa * Math.pow(2, exponent - 1075);
		return result;
	}

	public static final double POSITIVE_INFINITY = 1.0 / 0.0;
	public static final double NEGATIVE_INFINITY = -1.0 / 0.0;

	static public boolean isNaN(double v) {
		return (v != v);
	}

	public static final long abs(final long a) {
		return (a < 0) ? -a : a;
	}

	public static final double abs(final double a) {
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

	// //////////////////////
	// 空间转换
	public static final int KB(int nb) {
		return nb / KB;
	}

	public static final int MB(int nb) {
		return nb / MB;
	}

	public static final int GB(int nb) {
		return nb / GB;
	}

	public static final int TB(int nb) {
		return nb / TB;
	}

	public static final int PB(int nb) {
		return nb / PB;
	}

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
