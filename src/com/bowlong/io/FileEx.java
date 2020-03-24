package com.bowlong.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.lang.InputStreamEx;
import com.bowlong.objpool.StringBufPool;

public class FileEx extends InputStreamEx implements Serializable {
	private static final long serialVersionUID = 1L;

	static final public boolean isEmpty(File f) {
		return f == null || !f.exists();
	}

	static final public boolean isExists(File f) {
		return f != null && f.exists();
	}

	static final public boolean isNoExists(File f) {
		return f != null && !f.exists();
	}

	static final public boolean isExists(String path) {
		return isExists(openFile(path));
	}

	static final public File openFile(String file) {
		return new File(file);
	}

	/**
	 * 判断是否是目录
	 * 
	 * @param path
	 * @return
	 */
	static final public boolean isDir(String path) {
		File file = openFile(path);
		return file.isDirectory();
	}

	/**
	 * 判断是否是文件
	 * 
	 * @param path
	 * @return
	 */
	static final public boolean isFile(String path) {
		File file = openFile(path);
		return file.isFile();
	}

	static final public InputStream openInputStreamByUrl(String url) throws Exception {
		URL u = new URL(url);
		return openInputStreamByUrl(u);
	}

	static final public InputStream openInputStreamByUrl(URL url) throws Exception {
		return url.openStream();
	}

	static final public int getLength(String file) {
		File _f = openFile(file);
		return (int) _f.length();
	}

	/**
	 * 创建目录 createFolder
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	static final public boolean createFolder(String path) {
		return createFolder(openFile(path));
	}

	// createDire
	static final public boolean createFolder(File f) {
		if (isNoExists(f))
			return f.mkdirs();
		return false;
	}

	static final public void createFile(File f) throws Exception {
		try {
			if (isNoExists(f)) {
				createFolder(f.getParentFile());
				f.createNewFile();
			}
		} finally {
		}
	}

	static final public File createFile(String path) throws Exception {
		File f = openFile(path);
		createFile(f);
		return f;
	}

	/**
	 * 新建文件
	 * 
	 * @param file
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param content
	 *            String 文件内容
	 * @return boolean
	 */
	static final public void newFile(String filePath, String content) throws Exception {
		File _f = openFile(filePath);
		if (!_f.exists()) {
			_f.createNewFile();
		}
		FileWriter _fwr = new FileWriter(_f);
		PrintWriter _pwr = new PrintWriter(_fwr);
		_pwr.println(content);
		_pwr.flush();
		_fwr.flush();
		_pwr.close();
		_fwr.close();
	}

	/**
	 * 删除文件
	 */
	static final public void delFile(File del) {
		try {
			del.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static final public void delFile(String filePath) {
		delFile(openFile(filePath));
	}

	/**
	 * 删除文件夹
	 */
	static final public void delFolder(String folderPath) {
		delAllFile(folderPath); // 删除完里面所有内容
		delFile(folderPath);
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	static final public void delAllFile(String path) {
		File file = openFile(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = openFile(path + tempList[i]);
			} else {
				temp = openFile(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			} else if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]); // 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]); // 再删除空文件夹
			}
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 * @throws Exception
	 */
	static final public void copyFile(String oldPath, String newPath) throws Exception {
		int byteread = 0;
		File oldfile = openFile(oldPath);
		if (oldfile.exists()) { // 文件存在时
			try (InputStream inStream = new FileInputStream(oldPath); FileOutputStream fs = new FileOutputStream(newPath);) {
				byte[] buffer = new byte[1024];
				// int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 * @throws Exception
	 */
	static final public void copyFolder(String oldPath, String newPath) throws Exception {
		(openFile(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
		File a = openFile(oldPath);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (oldPath.endsWith(File.separator)) {
				temp = openFile(oldPath + file[i]);
			} else {
				temp = openFile(oldPath + File.separator + file[i]);
			}

			if (temp.isFile()) {
				try (FileInputStream input = new FileInputStream(temp); FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());) {

					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					input.close();
					output.close();
				}
			}
			if (temp.isDirectory()) { // 如果是子文件夹
				copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
			}
		}
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 * @throws Exception
	 */
	static final public void moveFile(String oldPath, String newPath) throws Exception {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 * @throws Exception
	 */
	static final public void moveFolder(String oldPath, String newPath) throws Exception {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	static final public boolean rename(String srcPath, String name) {
		File file = openFile(srcPath);
		File newFile = openFile(file.getParent() + File.separator + name);
		return file.renameTo(newFile);
	}

	// ///////////////////////////////////////////////////
	static final public void close(OutputStream output) {
		if (output == null)
			return;
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////////////
	static final public byte[] readFully(InputStream input, byte result[], int off, int len) throws Exception {
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

	/**
	 * read
	 * 
	 * @param file
	 * @param skip
	 * @param len
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	static final public byte[] read(String file, long skip, int len) throws Exception {
		try (InputStream input = new FileInputStream(file);) {
			byte[] data = new byte[len];
			input.skip(skip);
			int readlen = input.read(data);
			if (len == readlen) {
				return data;
			} else {
				byte[] readData = new byte[readlen];
				System.arraycopy(data, 0, readData, 0, readlen);
				return readData;
			}
		}
	}

	static final public byte[] readFully(File f) {
		if (isEmpty(f))
			return null;

		try (FileInputStream fis = new FileInputStream(f);) {
			int len = (int) f.length();
			byte[] b = new byte[len];
			readFully(fis, b);
			fis.close();
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static final public byte[] readFully(URL url) throws Exception {
		try (InputStream in = url.openStream()) {
			byte[] result = readFully(in);
			in.close();
			return result;
		}
	}

	static final public byte[] readFully(InputStream input, byte result[]) throws Exception {
		int off = 0;
		int len = result.length;
		readFully(input, result, off, len);
		return result;
	}

	static final public byte[] readFully(String file) throws Exception {
		File f = openFile(file);
		byte[] data = new byte[(int) f.length()];
		try (FileInputStream fis = new FileInputStream(f);) {
			readFully(fis, data);
			f = null;
			return data;
		}
	}

	static final public Properties readProperties(File f) {
		if (isEmpty(f))
			return null;
		try (FileInputStream fis = new FileInputStream(f);) {
			Properties p = new Properties();
			p.load(fis);
			fis.close();
			return p;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static final public String readText(String file) throws Exception {
		return readText(openFile(file));
	}

	static final public String readText(File file) throws Exception {
		String line = "";
		StringBuffer sb = StringBufPool.borrowObject();
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n");
			}
			br.close();
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	static final public String readText(InputStream in, Charset charset) throws Exception {
		if (in == null || charset == null)
			return "";
		byte[] buf = readFully(in);
		return new String(buf, charset);
	}

	static public String readText(InputStream in, String charsetName) throws Exception {
		if (in == null || charsetName == null)
			return "";
		Charset charset = Charset.forName(charsetName);
		return readText(in, charset);
	}

	static final public List<String> readLines(String file) throws Exception {
		return readLines(openFile(file));
	}

	static final public List<String> readLines(File file) throws Exception {
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			List<String> ret = new Vector<String>();
			int times = 100000;
			while (true) {
				if (times-- <= 0)
					break;

				String line = br.readLine();
				if (line == null)
					break;
				ret.add(line);
			}
			br.close();
			return ret;
		}
	}

	// /////////////////////////////////////////////////
	static final public void writeFully(File f, byte[] b, boolean append) throws Exception {
		try (FileOutputStream output = new FileOutputStream(f, append);) {
			output.write(b);
			output.flush();
			output.close();
		}
	}

	static final public void writeFully(String file, byte[] b, boolean append) throws Exception {
		writeFully(openFile(file), b, append);
	}

	static final public void writeFully(File f, String str, boolean append) throws Exception {
		writeFully(f, str.getBytes("UTF-8"), append);
	}

	static final public void writeFully(String file, String str, boolean append) throws Exception {
		writeFully(file, str.getBytes("UTF-8"), append);
	}

	static final public void write(String file, byte[] data) throws Exception {
		writeFully(file, data, false);
	}

	static final public void write(String file, String str) throws Exception {
		writeFully(file, str, false);
	}

	static final public void write(File f, byte[] b) throws Exception {
		writeFully(f, b, false);
	}

	static public void write(String file, InputStream inStream) throws Exception {
		byte[] all = readFully(inStream);
		if (all == null)
			return;
		write(file, all);
	}

	static public void write(File file, InputStream inStream) throws Exception {
		byte[] all = readFully(inStream);
		if (all == null)
			return;
		write(file, all);
	}

	static public void writeText(File fn, String str, Charset charset) throws Exception {
		try (FileOutputStream fos = new FileOutputStream(fn); OutputStreamWriter osw = new OutputStreamWriter(fos, charset); BufferedWriter bw = new BufferedWriter(osw);) {
			bw.write(str);
			bw.flush();
			osw.flush();
			fos.flush();
			bw.close();
			osw.close();
			fos.close();
		} finally {
		}
	}

	static public File writeFile(String inFile, String outFile) {
		return writeFile(openFile(inFile), openFile(outFile));
	}

	static public File writeFile(File inFile, File outFile) {
		// 10M缓存
		int mb = 10 * 1024 * 1024;
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
				BufferedReader reader = new BufferedReader(new InputStreamReader(bis, "utf-8"), mb);
				BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));) {
			while (reader.ready()) {
				String line = reader.readLine();
				line = line.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
				writer.append(line).append(" ");
			}
			reader.close();
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return outFile;
	}

	static final public FileInputStream openFileInps(String fn) throws Exception {
		return new FileInputStream(fn);
	}

	static final public InputStream openInps(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	static final public InputStream openInps(String file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	static public InputStream openInpsWithOutBom(String path) throws Exception {
		return B2InputStream.dropBomByInps(new FileInputStream(path));
	}

	static final public BufferedInputStream openBufferedInputStream(InputStream is) {
		return new BufferedInputStream(is);
	}

	static final public BufferedInputStream openBufferedInputStream(String file) throws FileNotFoundException {
		InputStream is = openInps(file);
		return openBufferedInputStream(is);
	}

	static final public DataInputStream openDataInputStream(InputStream is) throws FileNotFoundException {
		BufferedInputStream bis = openBufferedInputStream(is);
		return new DataInputStream(bis);
	}

	static final public DataInputStream openDataInputStream(String file) throws FileNotFoundException {
		InputStream is = openInps(file);
		return openDataInputStream(is);
	}

	static final public OutputStream openOutputStream(String file, boolean append) throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}

	static final public BufferedOutputStream openBufferedOutputStream(OutputStream os) {
		return new BufferedOutputStream(os);
	}

	static final public OutputStream openOutputStream(String file) throws FileNotFoundException {
		return openOutputStream(file, false);
	}

	static final public DataOutputStream openDataOutputStream(OutputStream os) {
		BufferedOutputStream bos = openBufferedOutputStream(os);
		return new DataOutputStream(bos);
	}

	static final public DataOutputStream openDataOutputStream(String file, boolean append) throws FileNotFoundException {
		OutputStream os = openOutputStream(file, append);
		return openDataOutputStream(os);
	}

	static final public DataOutputStream openDataOutputStream(String file) throws FileNotFoundException {
		boolean append = false;
		return openDataOutputStream(file, append);
	}

	static final public DataInputStream openDataInputStream(File file) throws FileNotFoundException {
		InputStream is = openInps(file);
		return openDataInputStream(is);
	}

	static final public FileWriter openFileWriter(File file) throws Exception {
		return new FileWriter(file);
	}

	static final public FileReader openFileReader(File file) throws FileNotFoundException {
		return new FileReader(file);
	}

	static final public BufferedReader openBufferedReader(Reader reader) {
		return new BufferedReader(reader);
	}

	static final public FileReader openFileReader(String file) throws FileNotFoundException {
		File f = openFile(file);
		return openFileReader(f);
	}

	static final public BufferedReader openBufferedReader(File file) throws FileNotFoundException {
		FileReader fr = openFileReader(file);
		return openBufferedReader(fr);
	}

	static final public BufferedReader openBufferedReader(String file) throws FileNotFoundException {
		FileReader fr = openFileReader(file);
		return openBufferedReader(fr);
	}

	static final public InputStreamReader openInputStreamReader(InputStream is) {
		return new InputStreamReader(is);
	}

	static final public FileWriter openFileWriter(String file) throws Exception {
		File f = openFile(file);
		return openFileWriter(f);
	}

	static final public BufferedWriter openBufferedWriter(Writer writer) {
		return new BufferedWriter(writer);
	}

	static final public BufferedWriter openBufferedWriter(String file) throws Exception {
		FileWriter fw = openFileWriter(file);
		return openBufferedWriter(fw);
	}

	static final public String getPath(String filename) {
		File f = openFile(filename);
		return f.getParentFile().getPath();
	}

	static final public String getName(String filename) {
		File f = openFile(filename);
		return f.getName();
	}

	static final public String getExtension(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfExtension(filename);
		if (index == -1) {
			return "";
		} else {
			return filename.substring(index + 1);
		}
	}

	static final public char EXTENSION_SEPARATOR = '.';
	private static char UNIX_SEPARATOR = '/';
	private static char WINDOWS_SEPARATOR = '\\';

	static final public int indexOfExtension(String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
		int lastSeparator = indexOfLastSeparator(filename);
		return (lastSeparator > extensionPos ? -1 : extensionPos);
	}

	static final public int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	static final public InputStream getResourceStream(Class<?> c, String fname) {
		return c.getResourceAsStream(fname);
	}

	static final public URL getResourceURL(Class<?> c, String fname) {
		return c.getResource(fname);
	}

	static final public byte[] getResource(Class<?> c, String fname) throws Exception {
		InputStream in = getResourceStream(c, fname);
		return readFully(in);
	}

	static final public void main(String[] args) {
		try {
			String s = "D:/Temp/GlassfishSvc.txt";
			String p = FileEx.getPath(s);
			System.out.println(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
