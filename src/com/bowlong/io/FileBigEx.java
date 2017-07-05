package com.bowlong.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 大文件读写处理 <br/>
 * RandomAccessFile[可以处理随机读取，大文件读取]<br/>
 * 
 * @author Canyon
 * @date 2015-12-20 14:13:01
 */
public class FileBigEx extends FileEx {

	private static final long serialVersionUID = 1L;

	// //////////////// RandomAccessFile 处理 ////////////////
	// 只读
	static public final String RAF_READONLY = "r";
	// 读写(文件不存在则创建)
	static public final String RAF_READWRITE = "rw";
	// 打开阅读和写作，如“RW”，并要求每一个更新的文件的内容或元数据写入同步对底层的存储装置。
	static public final String RAF_READONLYSYNC = "rws";
	// 打开阅读和写作，如“RW”，并要求每一个更新文件的内容写入同步对底层的存储装置。
	static public final String RAF_READONLYD = "rwd";

	static public final RandomAccessFile openRandomAccessFile(final File f, final String mode)
			throws FileNotFoundException {
		return new RandomAccessFile(f, mode);
	}

	static public final RandomAccessFile openRandomAccessFile(final String f, final String mode)
			throws FileNotFoundException {
		return new RandomAccessFile(f, mode);
	}

	static public final byte[] readFully(final RandomAccessFile f, final int pos, final int len) throws IOException {
		final byte[] result = new byte[len];
		readFully(f, pos, result);
		return result;
	}

	static public final void readFully(final RandomAccessFile f, final int pos, final byte[] result)
			throws IOException {
		f.seek(pos);
		f.readFully(result);
	}

	static public final String readLine(final RandomAccessFile f, final int pos) throws IOException {
		f.seek(pos);
		return f.readLine();
	}

	static public final InputStream readInps4Mapper(File file) throws Exception {
		MapperShareBuffer mapper = new MapperShareBuffer(file);
		try {
			return mapper.readInps();
		} finally {
			mapper.close();
		}
	}

	static public final InputStream readInps4Mapper(String path) throws Exception {
		File file = openFile(path);
		return readInps4Mapper(file);
	}

	static public final String readText4Mapper(File file, String charset) throws Exception {
		MapperShareBuffer mapper = new MapperShareBuffer(file);
		try {
			return mapper.readStr(charset);
		} finally {
			mapper.close();
		}
	}

	static public final String readText4Mapper(String path, String charset) throws Exception {
		File file = openFile(path);
		return readText4Mapper(file, charset);
	}

	static public final String readXml4Mapper(File file, String charset) throws Exception {
		MapperShareBuffer mapper = new MapperShareBuffer(file);
		try {
			return mapper.readStr4Xml(charset);
		} finally {
			mapper.close();
		}
	}

	static public final String readXml4Mapper(String path, String charset) throws Exception {
		File file = openFile(path);
		return readXml4Mapper(file, charset);
	}
}
