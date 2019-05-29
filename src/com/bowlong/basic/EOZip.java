package com.bowlong.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.bowlong.objpool.ObjPool;

/**
 * zip压缩<br/>
 * 
 * @author Canyon
 * @version 2019-05-29 20:15
 */
public class EOZip extends EOURL {

	private static final int BUFFER_SIZE = 2 * 1024;

	static final public ByteArrayOutputStream newOutStream() {
		return new ByteArrayOutputStream();
	}

	static final public InputStream newInStream(byte[] b) {
		return new ByteArrayInputStream(b);
	}

	static final public byte[] gzip(byte[] b) throws IOException {
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

	static final public byte[] unGZip(byte[] b) throws IOException {
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

	static final public byte[] unGZip(byte[] b, int srcLen) throws IOException {
		byte[] buff = new byte[srcLen];
		InputStream bais = newInStream(b);
		GZIPInputStream gis = new GZIPInputStream(bais);
		gis.read(buff);
		return buff;
	}

	static void compress(File source, ZipOutputStream zos, String name, boolean keepDir) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (source.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(source);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = source.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (keepDir) {
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (keepDir) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(), keepDir);
					} else {
						compress(file, zos, file.getName(), keepDir);
					}
				}
			}
		}
	}

	public static long toZip(String srcDir, OutputStream out, boolean keepDir) throws Exception {
		long start = System.currentTimeMillis();
		try (ZipOutputStream zos = new ZipOutputStream(out);) {
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, sourceFile.getName(), keepDir);
			long end = System.currentTimeMillis();
			return (end - start);
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		}
	}

	public static long toZip(List<File> srcFiles, OutputStream out) throws Exception {
		long start = System.currentTimeMillis();
		try (ZipOutputStream zos = new ZipOutputStream(out);) {
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			return (end - start);
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		}
	}
}
