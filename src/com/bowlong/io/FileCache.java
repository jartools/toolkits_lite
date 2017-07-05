package com.bowlong.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.bowlong.text.Encoding;

public class FileCache implements Closeable {
	File f = null;
	long lastModify;
	byte[] content;
	String text;

	public FileCache(String fn) throws IOException {
		this(new File(fn));
	}

	public FileCache(File file) throws IOException {
		if (file == null)
			throw new IOException("file == null");
		if (!file.exists())
			throw new IOException("file not exists");

		this.f = file;

	}

	private boolean isModify() {
		return content == null || (lastModify != f.lastModified());
	}

	public byte[] getData() throws IOException {
		if (isModify()) {
			this.content = FileEx.readFully(f);
			this.lastModify = f.lastModified();
			this.text = null;
		}

		return this.content;
	}

	public String getText() throws IOException {
		final Charset charset = Encoding.UTF8;
		return getText(charset);
	}

	public String getText(final Charset charset) throws IOException {
		if (text == null) {
			text = new String(getData(), charset);
		}
		return text;

	}

	public boolean exist() {
		if (f == null)
			return false;
		return f.exists();
	}

	@Override
	public void close() throws IOException {
		lastModify = 0;
		f = null;
		content = null;
		text = null;
	}

}
