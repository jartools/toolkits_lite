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

public class FileReadBytesDemo {

	static final public byte[] readBytes(File f) {
		if (!f.exists()) {
			f = null;
			return null;
		}
		byte[] r = null;
		try (FileInputStream read = new FileInputStream(f);
				BufferedInputStream inStream = new BufferedInputStream(read);
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return r;
	}
}
