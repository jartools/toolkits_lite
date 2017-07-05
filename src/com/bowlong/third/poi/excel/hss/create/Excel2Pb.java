package com.bowlong.third.poi.excel.hss.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bowlong.io.FileEx;
import com.bowlong.io.FileRw;
import com.bowlong.json.MyJson;
import com.bowlong.objpool.StringBufPool;
import com.bowlong.text.Encoding;

public class Excel2Pb {
	static String url = "pb/";

	static String f = Excel2ProtoGen.originFile;

	public static void main(String[] args) throws Exception {
		{
			proto(f, new ArrayList<String>());
		}

		System.exit(1);
	}

	@SuppressWarnings("unchecked")
	public static void proto(String xls, List<String> fileNames)
			throws Exception {
		FileInputStream stream = new FileInputStream(xls);
		HSSFWorkbook wb = new HSSFWorkbook(stream);

		Map<String, String> md5Map = getMD5Map(wb);

		File verFp = FileRw.getFile(url + "version.json");
		byte[] buff = FileEx.readFully(verFp);
		Map<String, String> map = new HashMap<String, String>();
		if (buff != null) {
			String content = new String(buff, Encoding.UTF8);
			map = MyJson.toMap(content);
		}
		for (Map.Entry<String, String> mb : md5Map.entrySet()) {
			String key = mb.getKey();
			String val1 = mb.getValue();
			String val2 = map.get(key);
			if (val2 == null) {
				fileNames.add(key);
				System.out.println(key + "----- INSERT");
			} else {
				if (!val1.equals(val2)) {
					fileNames.add(key);
					System.out.println(key + "----- UPDATE");
				}
			}
		}
		// 写sersion
		verFp = FileRw.createFile(url + "version.json");
		buff = MyJson.toJSONString(md5Map).getBytes();
		writeFile(verFp, buff);
		stream.close();
	}

	static Map<String, String> getMD5Map(HSSFWorkbook wb) throws Exception {
		Map<String, String> md5Map = new HashMap<String, String>();
		// === 读取文件
		//md5Map.put(DBCFCfgs.sheetName, DBCFCfgs(wb));
		return md5Map;
	}

	// "DBCFMonsters - 怪兽";
	// static String DBCFCfgs(HSSFWorkbook wb) throws Exception {
	// DBCFCfgs ret = DBCFCfgs.parse(wb);
	// byte[] buff = ret.toByteArray();
	// File fp = FileRw.getFile(DBCFCfgs.fn(url));
	// writeFile(fp, buff);
	// System.out.println(fp + " \t " + ret.datas.size());
	// return MD5.MD5Encode(ret.toString());
	// }
	
	public static void writeFile(File f, byte[] buff) {
		try (FileOutputStream fos = new FileOutputStream(f);) {
			fos.write(buff);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String protoType(String s) {
		s = s.toLowerCase();
		if (s.equals("int"))
			return "int32";
		else if (s.equals("boolean"))
			return "bool";

		return s;

	}

	public static String name1(String sheetName) {
		while (true) {
			int p1 = sheetName.indexOf("_");
			if (p1 < 0)
				break;
			sheetName = sheetName.replace("_", "");
			sheetName = upperN(sheetName, p1);
		}

		char ch = sheetName.charAt(sheetName.length() - 1);
		if (ch == 's')
			return upperN(sheetName.substring(0, sheetName.length() - 1), 0);

		return sheetName;
	}

	public static String name2(String sheetName) {
		while (true) {
			int p1 = sheetName.indexOf("_");
			if (p1 < 0)
				break;
			sheetName = sheetName.replace("_", "");
			sheetName = upperN(sheetName, p1);
		}

		char ch = sheetName.charAt(sheetName.length() - 1);
		if (ch == 's')
			return sheetName;

		return sheetName + "s";
	}

	// //////////////////////

	public static final String upperN(String s, int p) {
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

	// public static final String getString(XSSFSheet sheet, int row, int
	// column) {
	// try {
	// XSSFRow c = sheet.getRow(row);
	// if (c == null)
	// return "";
	// XSSFCell v = c.getCell(column);
	// if (v == null)
	// return "";
	// int cellType = v.getCellType();
	// if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
	// return "" + v.getNumericCellValue();
	// } else if (cellType == XSSFCell.CELL_TYPE_STRING) {
	// return v.getStringCellValue().trim();
	// } else if (cellType == XSSFCell.CELL_TYPE_BOOLEAN) {
	// return "" + v.getBooleanCellValue();
	// } else if (cellType == XSSFCell.CELL_TYPE_BLANK) {
	// return "";
	// } else if (cellType == XSSFCell.CELL_TYPE_FORMULA) {
	// return "" + v.getStringCellValue().trim();
	// } else if (cellType == XSSFCell.CELL_TYPE_ERROR) {
	// return "" + v.getStringCellValue();
	// }
	// return v.getStringCellValue().trim();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return "";
	// }
	//
	// public static int getInt(XSSFSheet sheet, int row, int column) {
	// try {
	// String v = getString(sheet, row, column);
	// return NumEx.stringToInt(v);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return 0;
	// }
	//
	// public static double getDouble(XSSFSheet sheet, int row, int column) {
	// try {
	// String v = getString(sheet, row, column);
	// return NumEx.stringToDouble(v);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return 0;
	// }
	//
	// public static boolean getBoolean(XSSFSheet sheet, int row, int column) {
	// return getBool(sheet, row, column);
	// }
	//
	// public static boolean getBool(XSSFSheet sheet, int row, int column) {
	// try {
	// String v = getString(sheet, row, column);
	// return v.toLowerCase().equals("true");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

}
