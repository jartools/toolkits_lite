package com.bowlong.third.poi.excel.xss;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bowlong.io.FileEx;
import com.bowlong.json.MyJson;
import com.bowlong.lang.NumEx;
import com.bowlong.lang.StrEx;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.third.poi.PoiEx;
import com.bowlong.util.MapEx;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class XSS extends PoiEx {

	public static final XSSFWorkbook openWorkbook(final String fn)
			throws Exception {
		final InputStream inStream = FileEx.openFileInps(fn);
		return openWorkbook(inStream);
	}

	public static final XSSFWorkbook openWorkbook(final InputStream inStream)
			throws Exception {
		OPCPackage pkg = openPackage(inStream);
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		return wb;
	}

	public static final XSSFWorkbook openWorkbook(final OPCPackage pkg)
			throws IOException, InvalidFormatException {
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		return wb;
	}

	public static final int sheetNum(final XSSFWorkbook wb) {
		return wb.getNumberOfSheets();
	}

	public static final XSSFSheet getSheet(final XSSFWorkbook wb,
			final String name) {
		XSSFSheet sheet = wb.getSheet(name);
		return sheet;
	}

	public static final Map<String, XSSFSheet> getSheetMaps(
			final XSSFWorkbook wb) {
		Map<String, XSSFSheet> ret = new HashMap<String, XSSFSheet>();
		XSSFSheet[] sheets = sheets(wb);
		for (XSSFSheet sheet : sheets) {
			ret.put(sheet.getSheetName(), sheet);
		}
		return ret;
	}

	public static final XSSFSheet[] sheets(final XSSFWorkbook wb) {
		int sheetNum = sheetNum(wb);
		XSSFSheet[] r2 = new XSSFSheet[sheetNum];
		for (int i = 0; i < sheetNum; i++) {
			XSSFSheet sheet = wb.getSheetAt(i);
			if (sheet == null)
				break;
			r2[i] = sheet;
		}
		return r2;
	}

	// //////////////////////

	public static final int estimateSize(final XSSFSheet sheet) {
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

	public static final List<Map<String, String>> readHeaders(
			final XSSFSheet sheet) {
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

	public static final int nameCol(final XSSFSheet sheet, final String name) {
		for (int i = 0; i < 255; i++) {
			String str = getName(sheet, i);
			if (str != null && str.equals(name))
				return i;
		}
		return -1;
	}

	public static final String getName(final XSSFSheet sheet, final int col) {
		String sName = getString(sheet, LINE_NAME, col);
		return sName;
	}

	public static final List<String> getNames(final XSSFSheet sheet) {
		List<String> r2 = new ArrayList<>();
		for (int i = 0; i < 255; i++) {
			String str = getName(sheet, i);
			if (str == null)
				return r2;
			r2.add(str);
		}
		return r2;
	}

	public static final String getType(final XSSFSheet sheet, int col) {
		String sName = getString(sheet, LINE_TYPE, col);
		return sName.toLowerCase();
	}

	public static final String getType(final XSSFSheet sheet, final String name) {
		int col = nameCol(sheet, name);
		if (col < 0)
			return TYPE_UNKNOW;
		return getType(sheet, col);
	}

	public static final String getCName(final XSSFSheet sheet, int col) {
		String sName = getString(sheet, LINE_CNAME, col);
		return sName;
	}

	public static final String getCName(final XSSFSheet sheet, final String name) {
		int col = nameCol(sheet, name);
		if (col < 0)
			return "";
		return getCName(sheet, col);
	}

	public static final String getMemo(final XSSFSheet sheet, int col) {
		String sName = getString(sheet, LINE_MEMO, col);
		return sName;
	}

	public static final String getMemo(final XSSFSheet sheet, final String name) {
		int col = nameCol(sheet, name);
		if (col < 0)
			return "";
		return getMemo(sheet, col);
	}

	public static final List<String> readIndexs(final XSSFSheet sheet) {
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

	public static final String[][] readAll2D(final XSSFSheet sheet)
			throws Exception {
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

	public static final String[] readRow(final XSSFSheet sheet, final int row)
			throws Exception {
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

	public static final List<List<Map<String, Object>>> readData(
			final XSSFSheet sheet, final List<Map<String, String>> headers)
			throws Exception {
		List<List<Map<String, Object>>> r2 = new ArrayList<List<Map<String, Object>>>();
		for (int row = LINE_DATA_MIN; row < LINE_DATA_MAX; row++) {
			String dockId = getString(sheet, row, 0);
			if (StrEx.isEmpty(dockId))
				break;

			final List<Map<String, Object>> rowData = readRow(sheet, headers,
					row);
			r2.add(rowData);
		}

		return r2;
	}

	public static final List<Map<String, Object>> readRow(
			final XSSFSheet sheet, final List<Map<String, String>> headers,
			final int row) throws Exception {
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

	public static final String upperN(final String s, final int p) {
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

	public static final Object getObject(final XSSFSheet sheet,
			final String type, final int row, final int column) {
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

	public static final String getString(final XSSFSheet sheet, final int row,
			final int column) {
		try {
			XSSFRow c = sheet.getRow(row);
			if (c == null)
				return "";
			XSSFCell v = c.getCell(column);
			if (v == null)
				return "";
			int cellType = v.getCellType();
			if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
				return "" + v.getNumericCellValue();
			} else if (cellType == XSSFCell.CELL_TYPE_BLANK) {
				return "";
			} else if (cellType == XSSFCell.CELL_TYPE_STRING) {
				return v.getStringCellValue().trim();
			} else if (cellType == XSSFCell.CELL_TYPE_BOOLEAN) {
				return "" + v.getBooleanCellValue();
			} else if (cellType == XSSFCell.CELL_TYPE_FORMULA) {
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

	public static final String getComment(final XSSFSheet sheet, final int row,
			final int column) {
		try {
			XSSFRow c = sheet.getRow(row);
			if (c == null)
				return "";
			XSSFCell v = c.getCell(column);
			if (v == null)
				return "";
			XSSFComment comment = v.getCellComment();
			if (comment == null)
				return "";
			XSSFRichTextString rich = comment.getString();
			if (rich == null)
				return "";
			return rich.getString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static int getInt(final XSSFSheet sheet, final int row,
			final int column) {
		try {
			double v = getDouble(sheet, row, column);
			return (int) v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long getLong(final XSSFSheet sheet, final int row,
			final int column) {
		try {
			double v = getDouble(sheet, row, column);
			return (long) v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static double getDouble(final XSSFSheet sheet, final int row,
			final int column) {
		try {
			String v = getString(sheet, row, column);
			return NumEx.stringToDouble(v, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean getBoolean(final XSSFSheet sheet, final int row,
			final int column) {
		return getBool(sheet, row, column);
	}

	public static boolean getBool(final XSSFSheet sheet, final int row,
			final int column) {
		try {
			String v = getString(sheet, row, column);
			return v.toLowerCase().equals("true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static final Object getJSON(final XSSFSheet sheet, final int row,
			final int column) {
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
		Entry<Object, Object> E = ((Map<Object, Object>) map).entrySet()
				.iterator().next();
		return (T) E.getKey();
	}

	public static final <T> T getValue(Map map) {
		if (map == null || map.isEmpty())
			return null;
		Entry<Object, Object> E = ((Map<Object, Object>) map).entrySet()
				.iterator().next();
		return (T) E.getValue();
	}

	public static final String app = "app.cnf"; // app.xlsx

	static String path = "/Users/zhanghitoshisatoshi/svn/KingOfClans/documents/";

	public static void main(String[] args) throws Exception {
		String f = path + "Clash of Clans 3.25 Revision.xlsx";
		InputStream inStream = FileEx.openInps(f);
		OPCPackage pkg = openPackage(inStream);
		XSSFWorkbook wb = openWorkbook(pkg);
		XSSFSheet[] sheets = sheets(wb);
		// for (XSSFSheet sheet : sheets) {
		// List<Map<String, String>> headers = readHeaders(sheet);
		// System.out.println(headers);
		// List<List<Map<String, Object>>> datas = readData(sheet, headers);
		// for (List<Map<String, Object>> map : datas) {
		// System.out.println(map);
		// }
		// System.out.println();
		// }
		String[][] d = readAll2D(sheets[0]);
		System.out.println(toString(d));

		pkg.close();
		System.exit(1);
	}

	public static String toString(String[][] str) {
		StringBuffer sb = StringBufPool.borrowObject();
		for (String[] ss : str) {
			for (String s : ss) {
				sb.append(s).append(", ");
			}
			StrEx.removeRight(sb, 2);
			// sb.setLength(sb.length() - 2);
			// sb.delete(sb.length() - 2, sb.length());
			sb.append("\r\n");
		}
		String r2 = sb.toString();
		StringBufPool.returnObject(sb);
		return r2;
	}

}
