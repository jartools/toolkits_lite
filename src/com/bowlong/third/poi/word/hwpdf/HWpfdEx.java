package com.bowlong.third.poi.word.hwpdf;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Bookmarks;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * word.doc 文本读取
 * 
 * @author Canyon
 * @version createtime：2015年8月26日下午2:01:56
 */
public class HWpfdEx {

	static public final HWPFDocument openHwpdf(final InputStream inStream)
			throws Exception {
		return new HWPFDocument(inStream);
	}

	static public final HWPFDocument openHwpdf(
			final POIFSFileSystem poifsFileSystem) throws Exception {
		return new HWPFDocument(poifsFileSystem);
	}

	// 书签信息
	static public final Bookmarks getBookMarks(HWPFDocument doc) {
		return doc.getBookmarks();
	}

	// 文本内容
	static public final String getDocumentText(HWPFDocument doc) {
		return doc.getDocumentText();
	}

	// 范围:表格,列表
	static public final Range getRange(HWPFDocument doc) {
		return doc.getRange();
	}

	static public final void write(HWPFDocument doc, OutputStream out) {
		try {
			doc.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
		}
	}
}
