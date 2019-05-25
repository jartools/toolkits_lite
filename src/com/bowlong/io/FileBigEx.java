package com.bowlong.io;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.bowlong.text.EncodingEx;

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
	static final public String RAF_READONLY = "r";
	// 读写(文件不存在则创建)
	static final public String RAF_READWRITE = "rw";
	// 打开阅读和写作，如“RW”，并要求每一个更新的文件的内容或元数据写入同步对底层的存储装置。
	static final public String RAF_READONLYSYNC = "rws";
	// 打开阅读和写作，如“RW”，并要求每一个更新文件的内容写入同步对底层的存储装置。
	static final public String RAF_READONLYD = "rwd";

	static final public RandomAccessFile openRAFile(File f, String mode) throws Exception {
		return new RandomAccessFile(f, mode);
	}

	static final public RandomAccessFile openRAFile(String f, String mode) throws Exception {
		return new RandomAccessFile(f, mode);
	}

	static final public byte[] readFully(RandomAccessFile f, int pos, int len) throws Exception {
		final byte[] result = new byte[len];
		readFully(f, pos, result);
		return result;
	}

	static final public void readFully(RandomAccessFile f, int pos, byte[] result) throws Exception {
		f.seek(pos);
		f.readFully(result);
	}

	static final public byte[] readFully(RandomAccessFile f) throws Exception {
		int len = (int) f.length();
		byte[] result = new byte[len];
		readFully(f, 0, result);
		return result;
	}

	static final public String readLine(RandomAccessFile f, int pos) throws Exception {
		f.seek(pos);
		return f.readLine();
	}

	static final public InputStream readInps4Mapper(File file) throws Exception {
		MapperShareBuffer mapper = new MapperShareBuffer(file);
		try {
			return mapper.readInps();
		} finally {
			mapper.close();
		}
	}

	static final public InputStream readInps4Mapper(String path) throws Exception {
		File file = openFile(path);
		return readInps4Mapper(file);
	}

	static final public String readText4Mapper(File file, String charset) throws Exception {
		MapperShareBuffer mapper = new MapperShareBuffer(file);
		try {
			return mapper.readStr(charset);
		} finally {
			mapper.close();
		}
	}

	static final public String readText4Mapper(File file) throws Exception {
		return readText4Mapper(file, EncodingEx.UTF_8);
	}

	static final public String readText4Mapper(String path, String charset) throws Exception {
		File file = openFile(path);
		return readText4Mapper(file, charset);
	}

	static final public String readText4Mapper(String path) throws Exception {
		return readText4Mapper(path, EncodingEx.UTF_8);
	}
}
