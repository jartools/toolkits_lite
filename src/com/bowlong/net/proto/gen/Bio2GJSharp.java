package com.bowlong.net.proto.gen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

import com.bowlong.util.NewList;
import com.bowlong.util.NewMap;
import com.bowlong.util.StrBuilder;

/**
 * @UserName : SandKing
 * @DataTime : 2012-10-22 下午11:32:41
 * @Description ：文件描述
 */
@SuppressWarnings({ "unused", "resource" })
public class Bio2GJSharp {
	public static String b2g(Class<?> c, boolean src) {
		B2Class B2C = c.getAnnotation(B2Class.class);
		String pojoName = "Pojo";
		String imp = "Imp";
		String namespace = "";
		if (B2C != null) {
			namespace = B2C.namespace();
		}

		String p = (src ? "src/" : "") + "coh/manage";
		if (namespace != null && !namespace.isEmpty()) {
			p = p + "/" + namespace;
		}
		File path = new File(p);
		if (!path.exists())
			path.mkdirs();
		String fileName = "";
		Class<?>[] classes = c.getDeclaredClasses();
		{
			StrBuilder sb = new StrBuilder();

			sb.pn("var ${1} = {};", c.getSimpleName());
			sb.pn("");
			// POJO
			String s = String.format("%s.%s", c.getSimpleName(), pojoName);
			sb.pn("${1} = {};", s);
			for (Class<?> class1 : classes) {
				String sname = class1.getSimpleName();
				if (B2G.isData(class1)) {
					String f = class1.getSimpleName();
					if (B2G.isConstant(class1)) {
						g2beanConstant(class1, s, sb);
					} else {
						g2bean(class1, s, sb);
					}
				}
			}

			for (Class<?> class1 : classes) {
				String sname = class1.getSimpleName();
				if (B2G.isServer(class1)) {
					g2s_call(class1, c.getSimpleName(), sb, s, imp);
				}
			}

			String sname = c.getSimpleName();
			fileName = sname + ".js";
			writeFile(p + "/" + sname + ".js", sb.toString());

			System.out.println(sb);
		}
		{
			StrBuilder sb = new StrBuilder();
			for (Class<?> class1 : classes) {
				String sname = class1.getSimpleName();
				if (B2G.isServer(class1)) {
					g2s_imp(class1, c.getSimpleName(), sb, imp, fileName);
				}
			}

			String sname = c.getSimpleName() + imp;
			writeFile(p + "/" + sname + ".js", sb.toString());

			System.out.println(sb);
		}
		return "";
	}

	public static void g2beanConstant(Class<?> c, String namespace,
			StrBuilder sb) {
		Field[] fs = c.getDeclaredFields();
		String cname = c.getSimpleName();
		sb.pn("${1}.${2} = {", namespace, cname);
		for (Field field : fs) {
			String t = B2G.getCsType(field);
			String s = field.getName();
			if (s.contains("$"))
				continue;
			String remark = B2G.getRemark(field);
			String def = B2G.getDef(field);
			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				if (gtype != null && !gtype.isEmpty()) {
					continue;
				}
			} else {
				if (t.contains("string")) {
					sb.pn("    ${1} : \"${3}\", ${2};", s, remark, def);

				} else {
					sb.pn("    ${1} : ${3}, ${2};", s, remark, def);
				}
			}
		}
		sb.pn("};");
	}

	public static void g2bean(Class<?> c, String namespace, StrBuilder sb) {
		Field[] fs = c.getDeclaredFields();
		String cname = c.getSimpleName();
		sb.pn("${1}.${2}.parse = function(obj) {", namespace, cname);
		sb.pn("        var r ={};");
		for (Field field : fs) {
			String t = B2G.getCsType(field);
			String gm = B2G.getCsMapType(t);
			String s = field.getName();
			if (s.contains("$"))
				continue;

			sb.pn("        r.${1} = obj.${2};", s, s);
		}
		sb.pn("        return r;");
		sb.pn("");

		sb.pn("};");

	}

	// 生成客户端接口
	public static void g2s_call(Class<?> c, String namespace, StrBuilder sb,
			String pojoName, String imp) {
		String sname = c.getSimpleName();
		Method[] methods = c.getMethods();
		String cname = c.getSimpleName();
		sb.pn("${1}.Call${2}  = {};", namespace, cname);

		for (Method m : methods) {
			if (!B2G.isServer(m))
				continue;

			String remark = B2G.getRemark(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			StrBuilder sb1 = new StrBuilder();
			for (NewMap<String, String> m1 : params) {
				// String mykey = (String) (m1.getKey().equals("boolean") ?
				// "bool"
				// : m1.getKey());
				// mykey = (String) (mykey.equals("List") ? "ArrayList" :
				// mykey);
				sb1.ap("${1}, ", m1.getValue());
			}
			if (sb1.length() > 2) {
				sb1.removeRight(2);
			}

			// 需要实现的逻辑函数
			sb.pn("    // ${1}", remark);
			sb.pn("    ${1}.Call${2}.${3} = function(${4}) {", namespace,
					cname, mname, sb1);
			sb.pn("        var _params = {};");
			sb.pn("        _params.${1} = '${2}';  // cmd:${3}", "cmd", mname,
					mname);
			for (NewMap<String, String> m1 : params) {
				String key = (String) m1.getKey();
				String val = (String) m1.getValue();
				sb.pn("        _params.${1} = ${2};", val, val);
			}
			sb.pn("        $.post(\"test.php\",_params,${1}.Call${2}.disp,\"json\");",namespace, cname);
			sb.pn("    };");
			sb.pn("");
		}

		StrBuilder sb2 = new StrBuilder();
		for (Method m : methods) {
			String mname = B2G.getNethodName(m);
			sb2.ap("${1},", mname.hashCode());
		}
		String s = sb2.toString();
		sb.pn("");

		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 逻辑分发");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		sb.pn("    ${1}.Call${2}.disp = function(data){", namespace, cname);
		sb.pn("        var cmd = obj.cmd;");
		sb.pn("        switch (cmd) {");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			if (B2G.isServer(m)) {
				if (!srtype.equals("void")) {
					sb.pn("            case '${1}': { //  ${2}", mname, remark);
					sb.pn("                ${1}.Call${2}.__onCallback_${3}(obj);",
							namespace, cname, mname);
					sb.pn("                return;");
					sb.pn("            }");
				}
			} else {
				sb.pn("            case ${1}: { //  ${2}", mname.hashCode(),
						remark);
				sb.pn("               ${1}.Call${2}.__onCall_${3}(obj);",
						namespace, cname, mname);
				sb.pn("                return;");
				sb.pn("            }");
			}
		}
		sb.pn("        }");
		sb.pn("    };");

		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 参数解析");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			// 解析参数函数
			if (B2G.isServer(m)) {
				if (!srtype.equals("void")) {
					sb.pn("    // ${1}", remark);
					sb.pn("    ${1}.Call${2}.__onCallback_${3} = function(obj) {",
							namespace, cname, mname);
					String mx = B2G.getCsMapType(srtype);
					sb.pn("        var rst = ${1}.ReturnStatus.parse(obj.get(${2}));",
							pojoName, B2G.RETURN_STAT);
					sb.pn("        Call${1}${2}.on${3}(rst);", cname, imp,
							upper1(mname));
					sb.pn("    };");
				}
			} else {
				sb.pn("    // ${1}", remark);
				sb.pn("    ${1}.Call${2}.__onCall_${3} = function(obj) {",
						namespace, cname, mname);
				// sb.pn("        var rst = ${1}.ReturnStatus.parse(obj);",
				// pojoName);
				sb.pn("");
				StrBuilder sb1 = new StrBuilder();
				for (NewMap<String, String> m1 : params) {
					String key = (String) m1.getKey();
					String val = (String) m1.getValue();
					String p = B2G.getCsMapType(key);
					if (p.equals("getObject")) {
						sb.pn("        var ${1} = ${2}.${3}.parse(obj.get(${4}));",
								val, pojoName, key, val.hashCode());
					} else {
						// if (B2G.getMapType(key).equals("getList")) {
						// key = "var";
						// }
						sb.pn("        var ${1} = obj.get(${2});", val,
								val.hashCode());
						if (B2G.getMapType(key).equals("getList")) {
							sb.pn("		var ${1}_list = new Array();", val);
							sb.pn("		{");
							sb.pn("			// Lsit对象(${1})", val);
							sb.pn("			for (var i = 0 ; i > ${1}.length ; i++) {",
									val);
							sb.pn("				${1}_list[i] = ${2}.${3}.parse(${1}[i])",
									val, pojoName,
									B2G.getOType(m, m1.getValue().toString()));
							sb.pn("			}");
							sb.pn("		}");
							val += "_list";
						}
					}
					sb1.ap("${1}, ", val);
				}
				if (sb1.length() > 2)
					sb1.removeRight(2);
				sb.pn("");
				if (srtype.equals("void")) {
					sb.pn("        Call${1}${2}.on${3}(${4});", cname, imp,
							upper1(mname), sb1);
				} else {
					sb.pn("        var rst = Call${1}${2}.on${3}(${4});",
							cname, imp, upper1(mname), sb1);
					sb.pn("        var result = {};");
					sb.pn("        result[${1}] = ${2};", B2G.METHOD,
							mname.hashCode());
					sb.pn("        result[${1}] = rst;", B2G.RETURN_STAT);
					sb.pn("        websocketSend(result);");
				}
				sb.pn("    }");
			}
			sb.pn("");
		}
	}

	public static void g2s_imp(Class<?> c, String namespace, StrBuilder sb,
			String imp, String fileName) {
		Method[] methods = c.getMethods();
		String cname = c.getSimpleName();
		sb.pn("/// <reference path=\"${1}\"/> ", fileName);
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 需要实现的接口");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		String s = String.format("Call%s%s", cname, imp);
		sb.pn("${1}  = {};", s);
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);

			// 解析参数函数
			sb.pn("    // ${1}", remark);
			if (B2G.isServer(m)) {
				if (!srtype.equals("void")) {
					sb.pn("    ${1}.on${2} = function(obj){", s, upper1(mname));
					sb.pn("");
					sb.pn("    };");
				}
			} else {
				NewList<NewMap<String, String>> params = B2G.getParameters(m);

				StrBuilder sb1 = new StrBuilder();
				for (NewMap<String, String> m1 : params) {
					String key = (String) m1.getKey();
					StrBuilder sb0 = new StrBuilder();
					if (B2G.getMapType(key).equals("getList")) {
						sb0.ap("${1}", "ArrayList",
								B2G.getOType(m, m1.getValue().toString()));
						key = sb0.toString();
					}
					sb1.ap("${1}, ", m1.getValue());
				}
				if (sb1.length() > 2) {
					sb1.removeRight(2);
				}

				// 需要实现的逻辑函数
				sb.pn("    ${1}.on${2} = function(${3}){", s, upper1(mname),
						sb1);
				sb.pn("");
				sb.pn("    }");
			}
			sb.pn("");
		}
	}

	public static void writeFile(String f, String str) {
		try (FileOutputStream out = new FileOutputStream(new File(f));
				OutputStreamWriter osw = new OutputStreamWriter(out,
						Charset.forName("UTF8"));) {
			osw.write(str, 0, str.length());
			osw.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String upper1(String s) {
		if (s == null || s.isEmpty())
			return s;
		int len = s.length();
		return s.substring(0, 1).toUpperCase() + s.substring(1, len);
	}
}
