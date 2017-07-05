package com.bowlong.third.poi.excel.xss.create;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bowlong.lang.StrEx;
import com.bowlong.third.poi.excel.xss.XSS;
import com.bowlong.util.ListEx;
import com.bowlong.util.StrBuilder;

public class Excel2ProtoGen {

	static public final String originFile = "aaa.xlsx";

	public static void main(String[] args) throws Exception {
		{
			String packageName = "coh.proto";
			String namespace = "cohdb";
			String clazzName = "CohDB";
			String path = "src/coh/proto/";
			proto(originFile, packageName, namespace, clazzName, path);
		}

		System.exit(1);
	}

	public static void proto(String xlsx, String packageName, String namespace,
			String clazzName, String path) throws Exception {
		File fp = new File(xlsx);
		if (!fp.exists()) {
			System.out.println("fp not exists");
			return;
		}

		OPCPackage pkg = OPCPackage.open(fp);
		XSSFWorkbook wb = new XSSFWorkbook(pkg);

		StrBuilder sb = new StrBuilder();
		sb.pn("package ${1};", packageName);
		sb.pn("");
		// sb.pn("import java.io.*;");
		sb.pn("import java.util.*;");
		sb.pn("import com.bowlong.io.*;");
		sb.pn("import com.bowlong.net.proto.gen.*;");
		sb.pn("");
		sb.pn("import static com.bowlong.net.proto.gen.B2G.DATA;");
		sb.pn("import static com.bowlong.net.proto.gen.B2G.SHEET;");
		sb.pn("import static com.bowlong.net.proto.gen.B2G.SHEET_ROW;");
		sb.pn("");
		sb.pn("@B2Class(namespace = $[1])", namespace);
		sb.pn("public class ${1} {", clazzName);

		sb.pn("    public static void main(String[] args) throws Exception {");
		sb.pn("        Class<?> c = ${1}.class;", clazzName);
		sb.pn("        boolean src = FileEx.exists($[1]);", "src");
		sb.pn("        Bio2GJava.b2g(c, src);");
		sb.pn("        Bio2GCSharp.b2g(c, src);");
		sb.pn("    }");

		XSSFSheet[] sheets = XSS.sheets(wb);
		for (XSSFSheet sheet : sheets) {
			doSheet(sheet, sb);
		}
		sb.pn("}");
		pkg.close();

		System.out.println(sb);
		writeFile(path + clazzName + ".java", sb.toString());
	}

	static public void doSheet(XSSFSheet sheet, StrBuilder sb) {
		String scnam = sheet.getSheetName();
		doSheet(sheet, sb, scnam, 0, null);
	}

	public static void doSheet(XSSFSheet sheet, StrBuilder sb, String scname,
			int row, List<String> attrList) {
		String sname = sheet.getSheetName();
		scname = StrN(scname);
		sb.pn("");
		sb.pn("    @B2Class(type = DATA, sheetType = SHEET_ROW, remark = $[1],isXls = false)",
				sname);
		sb.pn("    class ${1} {", scname);

		if (row <= 0)
			row = 0;

		boolean isReadSome = !ListEx.isEmpty(attrList);
		XSSFRow xssrow = sheet.getRow(row);
		int columnnum = xssrow.getLastCellNum();
		columnnum = columnnum >= 10000 ? 10000 : columnnum;

		for (int col = 0; col < columnnum; col++) {
			String comment = XSS.getString(sheet, row + 0, col);
			String cname = XSS.getString(sheet, row + 1, col);
			String ctype = XSS.getString(sheet, row + 2, col);
			comment = isEmpty(comment) ? cname : comment;
			if (isEmpty(comment) || isEmpty(cname) || isEmpty(ctype))
				break;

			// 判断是否可以读该字段
			if (isReadSome) {
				if (!ListEx.have(attrList, cname)) {
					continue;
				}
			}

			final String ctype2 = ctype.trim();
			switch (ctype2) {
			case "string":
				ctype = "String";
				break;
			case "bool":
				ctype = "boolean";
				break;
			default:
				break;
			}

			if (isReadSome)
				sb.pn("        @B2Field(remark = \"${1}\",column=\"${2}\")",
						comment, col);
			else
				sb.pn("        @B2Field(remark = \"${1}\")", comment);
			sb.pn("        public ${1} ${2};", ctype, cname);
			// sb.pn("        public ${1} ${2}; // $[3]", ctype, cname,
			// comment);
		}

		sb.pn("    }");
		sb.pn("");
		sb.pn("    @B2Class(type = DATA, sheetName=$[1],sheetCName=$[3], sheetType = SHEET, remark = $[2],isXls = false)",
				sname, scname + "s", scname);
		sb.pn("    class ${1}s {", scname);
		sb.pn("        public List<${1}> datas;", scname);
		sb.pn("    }");
		sb.pn("");
	}

	public static final String StrN(String s1) {
		String s2 = StrEx.upperFirst(s1);
		int p1 = -1;
		while ((p1 = s2.indexOf('_')) >= 0) {
			s2 = s2.replace("_", "");
			s2 = StrEx.upperN(s2, p1);
		}
		while ((p1 = s2.indexOf(' ')) >= 0) {
			s2 = s2.replace(" ", "");
			s2 = StrEx.upperN(s2, p1);
		}
		if (s2.toLowerCase().endsWith("s")) {
			s2 = StrEx.left(s2, s2.length() - 1);
		}
		return s2;
	}

	public static final boolean isEmpty(String type) {
		return type == null || type.isEmpty();
	}

	public static void writeFile(String f, String str) {
		try (FileOutputStream out = new FileOutputStream(new File(f));
				OutputStreamWriter osw = new OutputStreamWriter(out,
						Charset.forName("UTF8"));) {
			osw.write(str, 0, str.length());
			osw.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
