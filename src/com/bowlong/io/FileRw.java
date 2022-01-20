package com.bowlong.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.bowlong.bio2.B2InputStream;
import com.bowlong.text.EncodingEx;
import com.bowlong.util.ListEx;

public class FileRw extends FileBigEx {

	// Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	private static final long serialVersionUID = 1L;

	static final public byte[] readBytes(String path) {
		File f = new File(path);
		return readBytes(f);
	}

	static final public byte[] readBytes(File f) {
		if (isEmpty(f)) {
			return null;
		}
		byte[] r = null;
		try (FileInputStream in = new FileInputStream(f);) {
			r = readFully(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}

	static final public File getFile(String path) throws Exception {
		return createFile(path);
	}

	static final public File getDire(String path) {
		File f = new File(path);
		createFolder(f);
		return f;
	}

	static final public String readStr(String path) {
		return readStr(path, "UTF-8");
	}

	static final public String readStr(String path, String charsetName) {
		File f = new File(path);
		return readStr(f, charsetName);
	}

	static final public String readStr(File f, String charsetName) {
		String r = "";
		byte[] data = readBytes(f);
		if (data == null)
			return r;
		Charset charset = reCharset2(charsetName);
		r = new String(data, charset);
		data = null;
		return r;
	}

	// ============= 读取文件的格式
	static final public String getCharsetStr(InputStream inps) {
		return EncodingEx.getCharsetStr(inps);
	}

	static final public String getCharsetStr(String path) {
		return EncodingEx.getCharsetStr(path);
	}

	static final public Charset getCharset(String path) {
		String _str = getCharsetStr(path);
		return EncodingEx.getCharset(_str);
	}

	static final public String getCodeStr(InputStream inps) {
		return EncodingEx.getCodeStr(inps);
	}

	static final public String getCodeStr(String path) {
		return EncodingEx.getCodeStr(path);
	}

	static final public Charset getCodeCharset(String path) {
		String _str = getCodeStr(path);
		return EncodingEx.getCodeCharset(_str);
	}

	/*** 取得文件夹下面所有文件的路径 [path4Folder文件夹路径] **/
	static final public List<String> getFilePath(String path4Folder, List<String> result) {
		if (result == null)
			result = new ArrayList<String>();

		File file = new File(path4Folder);
		File[] array = file.listFiles();
		int len = array.length;
		File en;
		String enPath;
		for (int i = 0; i < len; i++) {
			en = array[i];
			enPath = en.getPath();
			if (en.isFile()) {
				result.add(enPath);
			} else if (en.isDirectory()) {
				getFilePath(enPath, result);
			}
		}
		return result;
	}

	/*** 设置成ANSI格式(系统运行环境格式) **/
	static final public void resetByBuffReader(String path) {
		List<String> list = getFilePath(path, null);
		if (ListEx.isEmpty(list))
			return;
		int len = list.size();
		for (int i = 0; i < len; i++) {
			String tmp = list.get(i);
			String endname = suffix(tmp);
			if (endname.equalsIgnoreCase("java") || endname.equalsIgnoreCase("lua") || endname.equalsIgnoreCase("cs")
					|| endname.equalsIgnoreCase("txt") || endname.equalsIgnoreCase("text")
					|| endname.equalsIgnoreCase("html") || endname.equalsIgnoreCase("js")
					|| endname.equalsIgnoreCase("css") || endname.equalsIgnoreCase("xml")
					|| endname.equalsIgnoreCase("json") || endname.equalsIgnoreCase("lrc")) {
				try (InputStream inps = openFileInps(tmp);
						BufferedReader buffRead = new BufferedReader(
								new InputStreamReader(inps, getCharsetStr(tmp)));) {
					String v = B2InputStream.readStrByBuffReader(buffRead);
					writeFully(tmp, v, false);
				} catch (Exception e) {
				}
			}
		}
	}

	static final public void resetByReader(String path) {
		List<String> list = getFilePath(path, null);
		if (ListEx.isEmpty(list))
			return;
		int len = list.size();
		for (int i = 0; i < len; i++) {
			String tmp = list.get(i);
			String endname = suffix(tmp);
			if (endname.equalsIgnoreCase("java") || endname.equalsIgnoreCase("lua") || endname.equalsIgnoreCase("cs")
					|| endname.equalsIgnoreCase("txt") || endname.equalsIgnoreCase("text")
					|| endname.equalsIgnoreCase("html") || endname.equalsIgnoreCase("js")
					|| endname.equalsIgnoreCase("css") || endname.equalsIgnoreCase("xml")
					|| endname.equalsIgnoreCase("json") || endname.equalsIgnoreCase("lrc")) {
				try (InputStream inps = openFileInps(tmp);
						InputStreamReader inpReader = new InputStreamReader(inps, getCharsetStr(tmp));) {
					String v = B2InputStream.readStrByReader(inpReader);
					writeFully(tmp, v, false);
				} catch (Exception e) {
				}
			}
		}
	}

	static final public void resetByReaderWithOutBom(String path) {
		List<String> list = getFilePath(path, null);
		if (ListEx.isEmpty(list))
			return;
		int len = list.size();
		for (int i = 0; i < len; i++) {
			String tmp = list.get(i);
			String endname = suffix(tmp);
			if (endname.equalsIgnoreCase("java") || endname.equalsIgnoreCase("lua") || endname.equalsIgnoreCase("cs")
					|| endname.equalsIgnoreCase("txt") || endname.equalsIgnoreCase("text")
					|| endname.equalsIgnoreCase("html") || endname.equalsIgnoreCase("js")
					|| endname.equalsIgnoreCase("css") || endname.equalsIgnoreCase("xml")
					|| endname.equalsIgnoreCase("json") || endname.equalsIgnoreCase("lrc")) {
				try (InputStream inps = openInpsWithOutBom(tmp);
						InputStreamReader inpReader = new InputStreamReader(inps, getCharsetStr(tmp));) {
					String v = B2InputStream.readStrByReader(inpReader);
					writeFully(tmp, v, false);
				} catch (Exception e) {
				}
			}
		}
	}

	static final public void writeText(String fp, String cont) throws Exception {
		File f = createFile(fp);
		writeText(f, cont, EncodingEx.UTF8);
	}

	static final public void writeAppend(String fp, String cont) throws Exception {
		File f = createFile(fp);
		writeFully(f, cont, true);
	}
}
