package com.bowlong.third.poi.excel.xss;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XWorkbook extends XSS {
	final OPCPackage pkg;
	final XSSFWorkbook wb;
	final XSSFSheet[] sheets;
	final Map<String, XSSFSheet> sheetMaps;
	final int sheetNum;
	final XSheet[] xSheet;
	protected final boolean iScached;

	public XWorkbook(final File fp) throws Exception {
		this(fp, false);
	}

	public XWorkbook(final File fp, boolean iScached) throws Exception {
		this.pkg = openPackage(fp);
		this.wb = openWorkbook(pkg);
		this.iScached = iScached;
		this.sheets = sheets(wb);
		this.sheetMaps = getSheetMaps(wb);
		this.sheetNum = sheetNum(wb);
		this.xSheet = new XSheet[sheetNum];
		for (int i = 0; i < sheetNum; i++) {
			xSheet[i] = new XSheet(this, sheets[i]);
		}
	}

	public void close() {
		try {
			this.pkg.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public OPCPackage getPkg() {
		return pkg;
	}

	public XSSFWorkbook getWb() {
		return wb;
	}

	public XSSFSheet[] getSheets() {
		return sheets;
	}

	public XSSFSheet getSheet(int index) {
		return sheets[index];
	}

	public Map<String, XSSFSheet> getSheetMaps() {
		return sheetMaps;
	}

	public int getSheetNum() {
		return sheetNum;
	}

	public XSheet[] getXSheets() {
		return xSheet;
	}

	public XSheet getXSheet(int index) {
		return xSheet[index];
	}

}
