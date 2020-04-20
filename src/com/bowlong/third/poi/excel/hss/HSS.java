package com.bowlong.third.poi.excel.hss;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.bowlong.json.MyJson;
import com.bowlong.lang.NumEx;
import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.third.poi.PoiEx;
import com.bowlong.util.MapEx;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class HSS extends PoiEx {

	public static final HSSFWorkbook openWorkbook(FileInputStream stream) throws Exception {
		POIFSFileSystem fs = openFS(stream);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		return wb;
	}

	public static final int sheetNum(HSSFWorkbook wb) {
		return wb.getNumberOfSheets();
	}

	public static final HSSFSheet getSheet(HSSFWorkbook wb,String name) {
		HSSFSheet sheet = wb.getSheet(name);
		return sheet;
	}

	public static final Map<String, HSSFSheet> getSheetMaps(HSSFWorkbook wb) {
		Map<String, HSSFSheet> ret = new HashMap<String, HSSFSheet>();
		HSSFSheet[] sheets = sheets(wb);
		for (HSSFSheet sheet : sheets) {
			ret.put(sheet.getSheetName(), sheet);
		}
		return ret;
	}

	public static final HSSFSheet[] sheets(HSSFWorkbook wb) {
		int sheetNum = sheetNum(wb);
		HSSFSheet[] r2 = new HSSFSheet[sheetNum];
		for (int i = 0; i < sheetNum; i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			if (sheet == null)
				break;
			r2[i] = sheet;
		}
		return r2;
	}

	// //////////////////////

	public static final int estimateSize(HSSFSheet sheet) {
		int r2 = 8;
		List<Map<String, String>> headers = readHeaders(sheet);
		for (Map<String, String> map : headers) {
			String sType = getValue(map);
			sType = sType.toLowerCase();
			if (sType.equals("int")) {
				r2 += 4;
			} else if (sType.equals("boolean")) {
				r2 += 1;
			} else if (sType.equals("double")) {
				r2 += 8;
			} else if (sType.equals("int")) {
				r2 += 4;
			} else if (sType.equals("long")) {
				r2 += 8;
			} else if (sType.equals("string")) {
				r2 += 64;
			} else {
				r2 += 8;
			}
		}
		return r2;
	}

	public static final List<Map<String, String>> readHeaders(HSSFSheet sheet) {
		List<Map<String, String>> r2 = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 1024; i++) {
			String sName = getString(sheet, LINE_NAME, i);
			String sType = getString(sheet, LINE_TYPE, i);
			if (StrEx.isEmpty(sName) || StrEx.isEmpty(sType))
				break;
			Map<String, String> e = new HashMap<String, String>();
			e.put(sName, sType);
			r2.add(e);
		}
		return r2;
	}

	public static final int nameCol(HSSFSheet sheet,String name) {
		for (int i = 0; i < 255; i++) {
			String str = getName(sheet, i);
			if (str != null && str.equals(name))
				return i;
		}
		return -1;
	}

	public static final String getName(HSSFSheet sheet,int col) {
		String sName = getString(sheet, LINE_NAME, col);
		return sName;
	}

	public static final List<String> getNames(HSSFSheet sheet) {
		List<String> r2 = new ArrayList<>();
		for (int i = 0; i < 255; i++) {
			String str = getName(sheet, i);
			if (str == null)
				return r2;
			r2.add(str);
		}
		return r2;
	}

	public static final String getType(HSSFSheet sheet, int col) {
		String sName = getString(sheet, LINE_TYPE, col);
		return sName.toLowerCase();
	}

	public static final String getType(HSSFSheet sheet,String name) {
		int col = nameCol(sheet, name);
		if (col < 0)
			return TYPE_UNKNOW;
		return getType(sheet, col);
	}

	public static final String getCName(HSSFSheet sheet, int col) {
		String sName = getString(sheet, LINE_CNAME, col);
		return sName;
	}

	public static final String getCName(HSSFSheet sheet,String name) {
		int col = nameCol(sheet, name);
		if (col < 0)
			return "";
		return getCName(sheet, col);
	}

	public static final String getMemo(HSSFSheet sheet, int col) {
		String sName = getString(sheet, LINE_MEMO, col);
		return sName;
	}

	public static final String getMemo(HSSFSheet sheet,String name) {
		int col = nameCol(sheet, name);
		if (col < 0)
			return "";
		return getMemo(sheet, col);
	}

	public static final List<String> readIndexs(HSSFSheet sheet) {
		List<String> r2 = new ArrayList<String>();
		for (int i = 0; i < 1024; i++) {
			String sName = getString(sheet, 0, i);
			String sComment = getComment(sheet, 1, i);
			if (sComment == null || sComment.isEmpty())
				continue;
			r2.add(sName);
		}
		return r2;
	}

	public static final String[][] readAll2D(HSSFSheet sheet) throws Exception {
		List<String[]> datas = new ArrayList<String[]>();
		int col = 0;
		int row = 1;
		for (row = 0; row < LINE_DATA_MAX; row++) {
			String[] rd = readRow(sheet, row);
			if (rd == null || rd.length <= 0)
				break;
			if (col <= 0)
				col = rd.length;
			datas.add(rd);
		}
		String[][] r2 = new String[row][col];
		int i = 0;
		for (String[] ss : datas) {
			r2[i] = ss;
			i++;
		}
		return r2;
	}

	public static final String[] readRow(HSSFSheet sheet,int row) throws Exception {
		List<String> list = new Vector<String>();
		for (int i = 0; i < 1024; i++) {
			String str = getString(sheet, row, i);
			if (StrEx.isEmpty(str))
				break;
			list.add(str);
		}
		return toArray(list);
	}

	public static final String[] toArray(List<String> list) {
		String[] r2 = new String[list.size()];
		int i = 0;
		for (String s : list) {
			r2[i] = s;
			i++;
		}
		return r2;
	}

	public static final List<List<Map<String, Object>>> readData(HSSFSheet sheet,List<Map<String, String>> headers) throws Exception {
		List<List<Map<String, Object>>> r2 = new ArrayList<List<Map<String, Object>>>();
		for (int row = LINE_DATA_MIN; row < LINE_DATA_MAX; row++) {
			String dockId = getString(sheet, row, 0);
			if (StrEx.isEmpty(dockId))
				break;

			final List<Map<String, Object>> rowData = readRow(sheet, headers, row);
			r2.add(rowData);
		}

		return r2;
	}

	public static final List<Map<String, Object>> readRow(HSSFSheet sheet,List<Map<String, String>> headers,int row) throws Exception {
		int column = 0;

		List<Map<String, Object>> row2 = new ArrayList<Map<String, Object>>();
		for (Map<String, String> t : headers) {
			String name = MapEx.getKey(t);
			String type = MapEx.getValue(t);
			Object obj = getObject(sheet, type, row, column);
			Map<String, Object> e = new HashMap<String, Object>();
			e.put(name, obj);
			row2.add(e);
			column++;
		}
		return row2;
	}

	public static final String upperN(String s,int p) {
		int len = s.length();
		if (len <= 0)
			return "";
		StringBuffer sb = StringBufPool.borrowObject();
		try {
			sb.append(s);
			sb.replace(p, p + 1, s.substring(p, p + 1).toUpperCase());
			return sb.toString();
		} finally {
			StringBufPool.returnObject(sb);
		}
	}

	public static final Object getObject(HSSFSheet sheet,String type,int row,int column) {
		String type2 = type.toLowerCase();
		if (type2.equals(TYPE_BOOLEAN)) {
			return getBool(sheet, row, column);
		} else if (type2.equals(TYPE_STRING)) {
			return getString(sheet, row, column);
		} else if (type2.equals(TYPE_INT)) {
			return getInt(sheet, row, column);
		} else if (type2.equals(TYPE_LONG)) {
			return getLong(sheet, row, column);
		} else if (type2.equals(TYPE_DOUBLE)) {
			return getDouble(sheet, row, column);
		} else if (type2.equals(TYPE_JSON)) {
			return getJSON(sheet, row, column);
		}
		return getString(sheet, row, column);
	}

	public static final String getString(HSSFSheet sheet,int row,int column) {
		try {
			HSSFRow c = sheet.getRow(row);
			if (c == null)
				return "";
			HSSFCell v = c.getCell(column);
			if (v == null)
				return "";
			int cellType = v.getCellType();
			if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
				return "" + v.getNumericCellValue();
			} else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
				return "";
			} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
				return v.getStringCellValue().trim();
			} else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
				return "" + v.getBooleanCellValue();
			} else if (cellType == HSSFCell.CELL_TYPE_FORMULA) {
				String result = "";
				try {
					result = "" + v.getStringCellValue().trim();
				} catch (Exception e) {
					result = "" + v.getNumericCellValue();
				}
				return result;
			}
			return v.getStringCellValue().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static final String getComment(HSSFSheet sheet,int row,int column) {
		try {
			HSSFRow c = sheet.getRow(row);
			if (c == null)
				return "";
			HSSFCell v = c.getCell(column);
			if (v == null)
				return "";
			HSSFComment comment = v.getCellComment();
			if (comment == null)
				return "";
			HSSFRichTextString rich = comment.getString();
			if (rich == null)
				return "";
			return rich.getString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int getInt(HSSFSheet sheet,int row,int column) {
		try {
			double v = getDouble(sheet, row, column);
			return (int) v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getLong(HSSFSheet sheet,int row,int column) {
		try {
			double v = getDouble(sheet, row, column);
			return (long) v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static double getDouble(HSSFSheet sheet,int row,int column) {
		try {
			String v = getString(sheet, row, column);
			return NumEx.stringToDouble(v, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean getBoolean(HSSFSheet sheet,int row,int column) {
		return getBool(sheet, row, column);
	}

	public static boolean getBool(HSSFSheet sheet,int row,int column) {
		try {
			String v = getString(sheet, row, column);
			return v.toLowerCase().equals("true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static final Object getJSON(HSSFSheet sheet,int row,int column) {
		try {
			String str = getString(sheet, row, column);
			return MyJson.parse(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final <T> T getKey(Map map) {
		if (map == null || map.isEmpty())
			return null;
		Entry<Object, Object> E = ((Map<Object, Object>) map).entrySet().iterator().next();
		return (T) E.getKey();
	}

	public static final <T> T getValue(Map map) {
		if (map == null || map.isEmpty())
			return null;
		Entry<Object, Object> E = ((Map<Object, Object>) map).entrySet().iterator().next();
		return (T) E.getValue();
	}

	public static void main(String[] args) throws Exception {
		String f = "Clash of Clans 3.25 Revision.xlsx";
		FileInputStream stream = new FileInputStream(f);
		HSSFWorkbook wb = openWorkbook(stream);
		HSSFSheet[] sheets = sheets(wb);
		String[][] d = readAll2D(sheets[0]);
		System.out.println(toString(d));
		stream.close();
		System.exit(1);
	}

	public static String toString(String[][] str) {
		StringBuffer sb = StringBufPool.borrowObject();
		for (String[] ss : str) {
			for (String s : ss) {
				sb.append(s).append(", ");
			}
			StrEx.removeRight(sb, 2);
			sb.append("\r\n");
		}
		String r2 = sb.toString();
		StringBufPool.returnObject(sb);
		return r2;
	}

}
