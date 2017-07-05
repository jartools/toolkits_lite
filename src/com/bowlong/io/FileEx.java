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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.objpool.ByteOutPool;
import com.bowlong.objpool.StringBufPool;

@SuppressWarnings("unused")
public class FileEx implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final InputStream openInputStreamByUrl(final String url) throws MalformedURLException, IOException {
		URL u = new URL(url);
		return openInputStreamByUrl(u);
	}

	public static final InputStream openInputStreamByUrl(final URL url) throws IOException {
		return url.openStream();
	}

	public static final int getLength(final String file) {
		File f = openFile(file);
		return (int) f.length();
	}

	/**
	 * 创建目录 createFolder
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static final boolean createFolder(final String path) {
		File file = new File(path);
		boolean b = file.mkdirs();
		file = null;
		return b;
	}

	/**
	 * 检查文件是否存在 exists
	 */
	public static final boolean exists(final String path) {
		File file = new File(path);
		boolean b = file.exists();
		file = null;
		return b;
	}

	/**
	 * 新建文件
	 * 
	 * @param file
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String 文件内容
	 * @return boolean
	 */
	public static final void newFile(final String filePath, final String fileContent) {

		try {
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);

			myFile.flush();
			resultFile.flush();
			myFile.close();
			resultFile.close();

			myFilePath = null;
			resultFile = null;
			myFile = null;
			strContent = null;
		} catch (Exception e) {
			// Log.println("新建目录操作出错");
			e.printStackTrace();
		}

	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static final void delFile(final String filePath) {
		try {
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();
			myDelFile = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static final void delFolder(final String folderPath) {
		delAllFile(folderPath); // 删除完里面所有内容
		String filePath = folderPath;
		filePath = filePath.toString();
		java.io.File myFilePath = new java.io.File(filePath);
		myFilePath.delete(); // 删除空文件夹
		myFilePath = null;
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public static final void delAllFile(final String path) {
		File file = new File(path);
		if (!file.exists()) {
			file = null;
			return;
		}
		if (!file.isDirectory()) {
			file = null;
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]); // 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]); // 再删除空文件夹
			}
		}
		temp = null;
		tempList = null;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 * @throws IOException
	 */
	public static final void copyFile(final String oldPath, final String newPath) throws IOException {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) { // 文件存在时
			try (InputStream inStream = new FileInputStream(oldPath);
					FileOutputStream fs = new FileOutputStream(newPath);) {

				byte[] buffer = new byte[1024];
				// int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		}

		oldfile = null;
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 * @throws IOException
	 */
	public static final void copyFolder(final String oldPath, final String newPath) throws IOException {

		(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
		File a = new File(oldPath);
		String[] file = a.list();
		File temp = null;
		for (int i = 0; i < file.length; i++) {
			if (oldPath.endsWith(File.separator)) {
				temp = new File(oldPath + file[i]);
			} else {
				temp = new File(oldPath + File.separator + file[i]);
			}

			if (temp.isFile()) {
				try (FileInputStream input = new FileInputStream(temp);
						FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());) {

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

			a = null;
			file = null;
			temp = null;
		}

	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 * @throws IOException
	 */
	public static final void moveFile(final String oldPath, final String newPath) throws IOException {
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
	 * @throws IOException
	 */
	public static final void moveFolder(final String oldPath, final String newPath) throws IOException {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	public static final boolean rename(final String srcPath, final String name) {
		File file = new File(srcPath);
		File newFile = new File(file.getParent() + File.separator + name);
		boolean b = file.renameTo(newFile);
		file = null;
		newFile = null;
		return b;
	}

	/**
	 * 判断是否是目录
	 * 
	 * @param path
	 * @return
	 */
	public static final boolean isDir(final String path) {
		File file = new File(path);
		boolean b = file.isDirectory();
		file = null;
		return b;
	}

	/**
	 * 判断是否是文件
	 * 
	 * @param path
	 * @return
	 */
	public static final boolean isFile(final String path) {
		File file = new File(path);
		boolean b = file.isFile();
		file = null;
		return b;
	}

	// ///////////////////////////////////////////////////
	public static final void close(final InputStream input) {
		if (input == null)
			return;
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void close(final OutputStream output) {
		if (output == null)
			return;
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////////////
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

	public static final byte[] readFully(final InputStream in) throws IOException {
		try (ByteOutStream buf = ByteOutPool.borrowObject();) {
			if (in == null)
				return null;

			try {
				int len = 0;
				byte[] buff = new byte[8 * 1024];
				do {
					len = in.read(buff);
					if (len < 0)
						break;

					buf.write(buff, 0, len);
				} while (len > 0);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return buf.toByteArray();
		}

	}

	/**
	 * read
	 * 
	 * @param file
	 * @param skip
	 * @param len
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static final byte[] read(final String file, final long skip, final int len) throws IOException {
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

	public static final byte[] readFully(final File f) {
		if (f == null)
			return null;

		if (!f.exists())
			return null;

		try (FileInputStream fis = new FileInputStream(f);) {
			int len = (int) f.length();
			byte[] b = new byte[len];
			readFully(fis, b);
			fis.close();
			return b;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final byte[] readFully(final URL url) throws MalformedURLException, IOException {
		try (InputStream in = url.openStream()) {
			byte[] result = readFully(in);
			in.close();
			return result;
		}
	}

	public static final byte[] readFully(final InputStream input, final byte result[]) throws IOException {
		final int off = 0;
		final int len = result.length;
		readFully(input, result, off, len);
		return result;
	}

	public static final byte[] readFully(final String file) throws IOException {
		File f = new File(file);
		byte[] data = new byte[(int) f.length()];
		try (FileInputStream fis = new FileInputStream(f);) {
			readFully(fis, data);
			f = null;
			return data;
		}
	}

	public static final Properties readProperties(final File f) {
		if (f == null || !f.exists())
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

	public static final String readText(final String file) throws IOException {
		return readText(new File(file));
	}

	public static final String readText(final File file) throws IOException {
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

	public static final String readText(final InputStream in, final Charset charset) throws IOException {
		if (in == null || charset == null)
			return "";
		byte[] buf = readFully(in);
		return new String(buf, charset);
	}

	static public final String readText(final InputStream in, final String charsetName) throws IOException {
		if (in == null || charsetName == null)
			return "";
		Charset charset = Charset.forName(charsetName);
		return readText(in, charset);
	}

	public static final List<String> readLines(final String file) throws IOException {
		return readLines(new File(file));
	}

	public static final List<String> readLines(final File file) throws IOException {
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
	public static final void writeFully(final File f, final byte[] b, final boolean append) throws IOException {
		try (FileOutputStream output = new FileOutputStream(f, append);) { // 2表示追加
			output.write(b);
			output.flush();
			output.close();
		}
	}

	public static final void writeFully(final String file, final byte[] b, final boolean append) throws IOException {
		File f = new File(file);
		writeFully(f, b, append);
	}

	public static final void writeAll(final String file, final boolean append, final String str) throws IOException {
		writeFully(file, str.getBytes("UTF-8"), append);
	}

	public static final void write(final String file, final byte[] data) throws IOException {
		writeFully(file, data, false);
	}

	public static final void write(final String file, final String str) throws IOException {
		writeAll(file, false, str);
	}

	public static final void write(final File f, final byte[] b) throws IOException {
		writeFully(f, b, false);
	}

	static public final void write(final String file, final InputStream inStream) throws Exception {
		byte[] all = readFully(inStream);
		if (all == null)
			return;
		write(file, all);
	}

	static public final void write(final File file, final InputStream inStream) throws Exception {
		byte[] all = readFully(inStream);
		if (all == null)
			return;
		write(file, all);
	}

	static public final void writeText(File fn, String str, Charset charset) {
		try (FileOutputStream fos = new FileOutputStream(fn);
				OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
				BufferedWriter bw = new BufferedWriter(osw);) {
			bw.write(str);
			bw.flush();
			osw.flush();
			fos.flush();
			bw.close();
			osw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public final File writeFile(String inFile, String outFile) {
		return writeFile(new File(inFile), new File(outFile));
	}
	
	static public final File writeFile(File inFile, File outFile) {
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
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outFile;
	}

	public static final File openFile(final String file) {
		return new File(file);
	}

	public static final FileInputStream openFileInps(final String fn) throws Exception {
		return new FileInputStream(fn);
	}

	public static final InputStream openInps(final File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	public static final InputStream openInps(final String file) throws FileNotFoundException {
		return new FileInputStream(file);
	}
	
	static public InputStream openInpsWithOutBom(String path)
			throws Exception {
		return B2InputStream.dropBomByInps(new FileInputStream(path));
	}

	public static final BufferedInputStream openBufferedInputStream(final InputStream is) {
		return new BufferedInputStream(is);
	}

	public static final BufferedInputStream openBufferedInputStream(final String file) throws FileNotFoundException {
		InputStream is = openInps(file);
		return openBufferedInputStream(is);
	}

	public static final DataInputStream openDataInputStream(final InputStream is) throws FileNotFoundException {
		BufferedInputStream bis = openBufferedInputStream(is);
		return new DataInputStream(bis);
	}

	public static final DataInputStream openDataInputStream(final String file) throws FileNotFoundException {
		InputStream is = openInps(file);
		return openDataInputStream(is);
	}

	public static final OutputStream openOutputStream(final String file, final boolean append)
			throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}

	public static final BufferedOutputStream openBufferedOutputStream(final OutputStream os) {
		return new BufferedOutputStream(os);
	}

	public static final OutputStream openOutputStream(final String file) throws FileNotFoundException {
		return openOutputStream(file, false);
	}

	public static final DataOutputStream openDataOutputStream(final OutputStream os) {
		BufferedOutputStream bos = openBufferedOutputStream(os);
		return new DataOutputStream(bos);
	}

	public static final DataOutputStream openDataOutputStream(final String file, final boolean append)
			throws FileNotFoundException {
		OutputStream os = openOutputStream(file, append);
		return openDataOutputStream(os);
	}

	public static final DataOutputStream openDataOutputStream(final String file) throws FileNotFoundException {
		boolean append = false;
		return openDataOutputStream(file, append);
	}

	public static final DataInputStream openDataInputStream(final File file) throws FileNotFoundException {
		InputStream is = openInps(file);
		return openDataInputStream(is);
	}

	public static final FileWriter openFileWriter(final File file) throws IOException {
		return new FileWriter(file);
	}

	public static final FileReader openFileReader(final File file) throws FileNotFoundException {
		return new FileReader(file);
	}

	public static final BufferedReader openBufferedReader(final Reader reader) {
		return new BufferedReader(reader);
	}

	public static final FileReader openFileReader(final String file) throws FileNotFoundException {
		File f = openFile(file);
		return openFileReader(f);
	}

	public static final BufferedReader openBufferedReader(final File file) throws FileNotFoundException {
		FileReader fr = openFileReader(file);
		return openBufferedReader(fr);
	}

	public static final BufferedReader openBufferedReader(final String file) throws FileNotFoundException {
		FileReader fr = openFileReader(file);
		return openBufferedReader(fr);
	}

	public static final InputStreamReader openInputStreamReader(final InputStream is) {
		return new InputStreamReader(is);
	}

	public static final FileWriter openFileWriter(final String file) throws IOException {
		File f = openFile(file);
		return openFileWriter(f);
	}

	public static final BufferedWriter openBufferedWriter(final Writer writer) {
		return new BufferedWriter(writer);
	}

	public static final BufferedWriter openBufferedWriter(final String file) throws IOException {
		FileWriter fw = openFileWriter(file);
		return openBufferedWriter(fw);
	}

	public static final String getPath(final String filename) {
		File f = new File(filename);
		return f.getParentFile().getPath();
	}

	public static final String getName(final String filename) {
		File f = new File(filename);
		return f.getName();
	}

	public static final String getExtension(final String filename) {
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

	public static final char EXTENSION_SEPARATOR = '.';
	private static final char UNIX_SEPARATOR = '/';
	private static final char WINDOWS_SEPARATOR = '\\';

	public static final int indexOfExtension(final String filename) {
		if (filename == null) {
			return -1;
		}
		int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
		int lastSeparator = indexOfLastSeparator(filename);
		return (lastSeparator > extensionPos ? -1 : extensionPos);
	}

	public static final int indexOfLastSeparator(final String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	public static final String getUserRoot() {
		return System.getProperty("user.dir");
	}

	public static final InputStream getResourceStream(final Class<?> c, final String fname) {
		return c.getResourceAsStream(fname);
	}

	public static final URL getResourceURL(final Class<?> c, final String fname) {
		return c.getResource(fname);
	}

	public static final byte[] getResource(final Class<?> c, final String fname) throws IOException {
		InputStream in = getResourceStream(c, fname);
		return readFully(in);
	}

	public static void main(String[] args) {
		try {
			String s = "D:/Temp/GlassfishSvc.txt";
			String p = FileEx.getPath(s);
			System.out.println(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isEmpty(File fn) {
		return fn == null || !fn.exists();
	}
}
