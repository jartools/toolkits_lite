package com.bowlong.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
		byte[] r = null;
		try (FileInputStream read = new FileInputStream(f);
				BufferedInputStream inStream = new BufferedInputStream(read);
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {
			if (!f.exists()) {
				f = null;
				return r;
			}
			byte[] btBuff = new byte[10240];
			int len = 0;
			while ((len = inStream.read(btBuff)) != -1) {
				outStream.write(btBuff, 0, len);
			}
			r = outStream.toByteArray();
			inStream.close();
			outStream.close();
			read.close();
			f = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}

	static final public File getFile(String path) {
		return createFile(path);
	}

	static final public File getDire(String path) {
		File f = null;
		f = new File(path);

		if (!f.exists()) {
			createDire(f);
		}
		return f;
	}

	static final public File createFile(String path) {
		File f = null;
		f = new File(path);

		if (!f.exists()) {
			createFile(f);
		}
		return f;
	}

	static final public void createFile(File f) {
		try {
			if (f == null)
				return;
			boolean isExi = f.exists();
			if (!isExi) {
				File pf = f.getParentFile();
				if (!pf.exists()) {
					createDire(pf);
				}
				f.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static final public void createDire(File f) {
		try {
			if (f == null)
				return;
			boolean isExi = f.exists();
			if (!isExi) {
				f.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static final public String readStr(String path) {
		return readStr(path, "UTF-8");
	}

	static final public String readStr(String path, String charsetName) {
		String r = "";
		byte[] data = readBytes(path);
		if (data == null)
			return r;
		Charset charset = Charset.forName(charsetName);
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
		return EncodingEx.getCharset(getCharsetStr(path));
	}

	static final public String getCodeStr(InputStream inps) {
		return EncodingEx.getCodeStr(inps);
	}

	static final public String getCodeStr(String path) {
		return EncodingEx.getCodeStr(path);
	}

	static final public Charset getCodeCharset(String path) {
		return EncodingEx.getCodeCharset(getCodeStr(path));
	}

	/*** 取得文件夹下面所有文件的路径 [path4Folder文件夹路径] **/
	static final public List<String> getFilePath(String path4Folder, List<String> result) {
		if (result == null)
			result = new ArrayList<String>();

		File file = new File(path4Folder);
		File[] array = file.listFiles();
		int len = array.length;
		for (int i = 0; i < len; i++) {
			File en = array[i];
			String enPath = en.getPath();
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
			String endname = tmp.substring(tmp.lastIndexOf(".") + 1);
			if (endname.equalsIgnoreCase("java")
					|| endname.equalsIgnoreCase("lua")
					|| endname.equalsIgnoreCase("cs")
					|| endname.equalsIgnoreCase("txt")
					|| endname.equalsIgnoreCase("text")
					|| endname.equalsIgnoreCase("html")
					|| endname.equalsIgnoreCase("js")
					|| endname.equalsIgnoreCase("css")
					|| endname.equalsIgnoreCase("xml")
					|| endname.equalsIgnoreCase("json")
					|| endname.equalsIgnoreCase("lrc")) {
				try (InputStream inps = openFileInps(tmp);
						BufferedReader buffRead = new BufferedReader(
								new InputStreamReader(inps, getCharsetStr(tmp)));) {
					String v = B2InputStream.readStrByBuffReader(buffRead);
					writeAll(tmp, false, v);
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
			String endname = tmp.substring(tmp.lastIndexOf(".") + 1);
			if (endname.equalsIgnoreCase("java")
					|| endname.equalsIgnoreCase("lua")
					|| endname.equalsIgnoreCase("cs")
					|| endname.equalsIgnoreCase("txt")
					|| endname.equalsIgnoreCase("text")
					|| endname.equalsIgnoreCase("html")
					|| endname.equalsIgnoreCase("js")
					|| endname.equalsIgnoreCase("css")
					|| endname.equalsIgnoreCase("xml")
					|| endname.equalsIgnoreCase("json")
					|| endname.equalsIgnoreCase("lrc")) {
				try (InputStream inps = openFileInps(tmp);
						InputStreamReader inpReader = new InputStreamReader(
								inps, getCharsetStr(tmp));) {
					String v = B2InputStream.readStrByReader(inpReader);
					writeAll(tmp, false, v);
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
			String endname = tmp.substring(tmp.lastIndexOf(".") + 1);
			if (endname.equalsIgnoreCase("java")
					|| endname.equalsIgnoreCase("lua")
					|| endname.equalsIgnoreCase("cs")
					|| endname.equalsIgnoreCase("txt")
					|| endname.equalsIgnoreCase("text")
					|| endname.equalsIgnoreCase("html")
					|| endname.equalsIgnoreCase("js")
					|| endname.equalsIgnoreCase("css")
					|| endname.equalsIgnoreCase("xml")
					|| endname.equalsIgnoreCase("json")
					|| endname.equalsIgnoreCase("lrc")) {
				try (InputStream inps = openInpsWithOutBom(tmp);
						InputStreamReader inpReader = new InputStreamReader(
								inps, getCharsetStr(tmp));) {
					String v = B2InputStream.readStrByReader(inpReader);
					writeAll(tmp, false, v);
				} catch (Exception e) {
				}
			}
		}
	}
	
	static final public void writeText(String fp,String cont){
		File f = createFile(fp);
		writeText(f, cont, EncodingEx.UTF8);
	}
}
