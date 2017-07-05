package com.bowlong.third.poi.excel.hss;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bowlong.io.FileEx;

public class HWorkbook extends HSS {
	final FileInputStream pkg;
	final HSSFWorkbook wb;
	final HSSFSheet[] sheets;
	final Map<String, HSSFSheet> sheetMaps;
	final int sheetNum;
	final HSheet[] hSheet;
	protected final boolean iScached;

	public HWorkbook(final String fn) throws Exception {
		this(fn, false);
	}

	public HWorkbook(final String fn, boolean iScached)
			throws Exception {
		this.pkg = FileEx.openFileInps(fn);
		this.wb = openWorkbook(pkg);
		this.iScached = iScached;
		this.sheets = sheets(wb);
		this.sheetMaps = getSheetMaps(wb);
		this.sheetNum = sheetNum(wb);
		this.hSheet = new HSheet[sheetNum];
		for (int i = 0; i < sheetNum; i++) {
			hSheet[i] = new HSheet(this, sheets[i]);
		}
	}

	public void close() {
		try {
			this.pkg.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileInputStream getPkg() {
		return pkg;
	}

	public HSSFWorkbook getWb() {
		return wb;
	}

	public HSSFSheet[] getSheets() {
		return sheets;
	}

	public HSSFSheet getSheet(int index) {
		return sheets[index];
	}

	public Map<String, HSSFSheet> getSheetMaps() {
		return sheetMaps;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public HSheet[] getHSheets() {
		return hSheet;
	}

	public HSheet getHSheet(int index) {
		return hSheet[index];
	}

}
