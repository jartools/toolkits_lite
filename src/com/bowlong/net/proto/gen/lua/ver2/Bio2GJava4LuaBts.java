package com.bowlong.net.proto.gen.lua.ver2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

import com.bowlong.lang.PStr;
import com.bowlong.lang.StrEx;
import com.bowlong.net.proto.gen.B2Class;
import com.bowlong.net.proto.gen.B2Field;
import com.bowlong.net.proto.gen.B2G;
import com.bowlong.net.proto.gen.Bio2GJava;
import com.bowlong.net.proto.gen.lua.LuaEncode;
import com.bowlong.util.NewList;
import com.bowlong.util.NewMap;
import com.bowlong.util.StrBuilder;

@SuppressWarnings({ "unused", "resource" })
public class Bio2GJava4LuaBts {

	public static void b2g(Class<?> c, boolean src) throws Exception {
		b2g(c, src, true);
	}

	public static void b2g(Class<?> c, boolean src, boolean isCall)
			throws Exception {
		B2Class B2C = c.getAnnotation(B2Class.class);
		String namespace = "";
		if (B2C != null) {
			namespace = B2C.namespace();
		}
		Class<?>[] classes = c.getDeclaredClasses();
		String p = (src ? "src/" : "") + "gen_b2g";
		// String p = "gen_b2g";

		if (namespace != null && !namespace.isEmpty()) {
			p = p + "/" + namespace;
		}

		for (Class<?> class1 : classes) {
			String sname = class1.getSimpleName();
			if (B2G.isServer(class1)) {
				File path = new File(p);
				if (!path.exists())
					path.mkdirs();

				{
					StrBuilder sb = new StrBuilder();
					g2s_service(class1, namespace, sb);
					writeFile(p + "/" + sname + ".java", sb.toString());
					System.out.println(sb);
				}
				{
					if (isCall) {
						StrBuilder sb2 = new StrBuilder();
						g2s_call(class1, namespace, sb2);
						writeFile(p + "/" + "Call" + sname + ".java",
								sb2.toString());
						System.out.println(sb2);
					}
				}
			} else if (B2G.isData(class1)) {
				String p2 = p + "/bean";
				File path = new File(p2);
				if (!path.exists())
					path.mkdirs();

				String f = class1.getSimpleName();
				StrBuilder sb = new StrBuilder();
				if (B2G.isConstant(class1)) {
					g2beanConstant(class1, namespace, sb);
				} else {
					g2bean(class1, namespace, sb);
				}
				writeFile(p2 + "/" + f + ".java", sb.toString());
				System.out.println(sb);
			}
		}
	}

	public static void g2bean(Class<?> c, String namespace, StrBuilder sb) {
		Field[] fs = c.getDeclaredFields();
		String cname = c.getSimpleName();
		int hcname = cname.hashCode();
		sb.pn("package gen_b2g${1}.bean;", StrEx.isEmpty(namespace) ? "" : "."
				+ namespace);
		sb.pn("");
		sb.pn("import java.io.*;");
		sb.pn("import java.util.*;");
		sb.pn("");
		// sb.pn("import com.bowlong.bio2.*;");
		// sb.pn("import com.bowlong.io.*;");
		sb.pn("import com.bowlong.util.*;");
		boolean isHasNum = false;
		for (Field field : fs) {
			String t = B2G.getType(field);
			String gm = B2G.getMapType(t);
			isHasNum = gm.equals("getInt") || gm.equals("getLong");
			if (isHasNum) {
				break;
			}
		}

		if (isHasNum)
			sb.pn("import com.bowlong.net.proto.gen.lua.LuaEncode;");

		boolean isXls = B2G.isXls(c);
		if (B2G.isSheet(c)) {
			if (isXls) {
				sb.pn("import com.bowlong.io.*;");
				sb.pn("import org.apache.poi.hssf.usermodel.*;");
			} else {
				sb.pn("import com.bowlong.io.*;");
				sb.pn("import org.apache.poi.xssf.usermodel.*;");
			}
		} else if (B2G.isSheetRow(c)) {
			if (isXls) {
				sb.pn("import org.apache.poi.hssf.usermodel.*;");
				sb.pn("import com.bowlong.third.excel.hss.*;");
			} else {
				sb.pn("import org.apache.poi.xssf.usermodel.*;");
				sb.pn("import com.bowlong.third.excel.xss.*;");
			}
		}

		sb.pn("");
		// sb.pn("@SuppressWarnings({ \"rawtypes\", \"unchecked\", \"serial\", \"unused\" })");
		sb.pn("@SuppressWarnings(\"all\")");
		sb.pn("public class ${1} extends com.bowlong.net.proto.NSupport {",
				cname);
		sb.pn("    public static final int _CID = ${1};", hcname);
		sb.pn("");

		StrBuilder sb0 = new StrBuilder();
		int i = 0;
		for (Field field : fs) {
			B2Field a = field.getAnnotation(B2Field.class);
			String t = B2G.getType(field);
			String s = field.getName();
			if (s.contains("$"))
				continue;
			if (i == 0) {
				sb0.ap("${1} ${2}", t, s);
			} else {
				sb0.ap(",${1} ${2}", t, s);
			}
			i++;
			String remark = B2G.getRemark(field);
			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				if (gtype != null && !gtype.isEmpty()) {
					sb.pn("    public ${1}<${4}> ${2} = new NewList();${3}", t,
							s, remark, gtype);
				}
			} else {
				if (t.contains("String")) {
					sb.pn("    public ${1} ${2} = \"\";${3}", t, s, remark);
				} else if (t.contains("Map")) {
					sb.pn("    public ${1} ${2} = new NewMap();${3}", t, s,
							remark);
				} else {
					if (B2G.isBType(t)) {
						sb.pn("    public ${1} ${2};${3}", t, s, remark);
					} else {
						sb.pn("    public ${1} ${2} = new ${1}();${3}", t, s,
								remark);

					}
				}
			}
		}

		// ///////
		sb.pn("");
		sb.pn("    public void set${2}(${3}){", cname, cname, sb0.toString());
		for (Field field : fs) {
			String t = B2G.getType(field);
			String gm = B2G.getMapType(t);
			String s = field.getName();
			if (s.contains("$"))
				continue;
			sb.pn("        this.${1} = ${2};", s, s);
		}
		sb.pn("    }");

		sb.pn("    public static ${1} new${2}(${3}){", cname, cname,
				sb0.toString());
		sb.pn("        ${1} r = new ${1}();", cname);
		for (Field field : fs) {
			String t = B2G.getType(field);
			String gm = B2G.getMapType(t);
			String s = field.getName();
			if (s.contains("$"))
				continue;
			sb.pn("        r.${1} = ${2};", s, s);
		}
		sb.pn("        return r;");
		sb.pn("    }");

		for (Field field : fs) {
			String s = field.getName();
			if (s.contains("$"))
				continue;
			String s1 = StrEx.upperFirst(s);
			String t = B2G.getType(field);
			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				boolean isBtype = B2G.isBType(gtype);
				if (gtype != null && !gtype.isEmpty() && !isBtype) {
					sb.pn("    public List<Map> ${1}_maps() {", s);
					sb.pn("        List<Map> r = new NewList<Map>();");
					sb.pn("        if(${1} == null) return r;", s);
					sb.pn("        for(${1} _e : ${2}) {", gtype, s);
					sb.pn("            Map e = _e.toMap();");
					sb.pn("            if(e == null) continue;");
					sb.pn("            r.add(e);");
					sb.pn("        }");
					sb.pn("        return r;");
					sb.pn("    }");
					sb.pn("");

					sb.pn("    public static List<${2}> maps_${1}(List<NewMap> maps) {",
							s, gtype);
					sb.pn("        List r = new NewList();");
					sb.pn("        for(NewMap _e : maps) {", gtype, s);
					sb.pn("            ${1} e = ${1}.parse(_e);", gtype);
					sb.pn("            if(e == null) continue;");
					sb.pn("            r.add(e);");
					sb.pn("        }");
					sb.pn("        return r;");
					sb.pn("    }");
					sb.pn("");
				}
			}
		}

		sb.pn("");
		sb.pn("    public Map toMap() {");
		sb.pn("        Map r = new HashMap();");
		// sb.pn("        r.put(\"${1}\", _CID);", B2G.BEAN);
		for (Field field : fs) {
			String t = B2G.getType(field);
			String gm = B2G.getMapType(t);
			String s = field.getName();
			int hs = s.hashCode();
			if (s.contains("$"))
				continue;

			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				boolean isBType = B2G.isBType(gtype);
				if (gtype != null && !gtype.isEmpty() && !isBType) {
					sb.pn("        r.put(${1}, ${2}_maps());", hs, s);
				} else {
					sb.pn("        r.put(${1}, ${2});", hs, s);
				}
			} else {
				if (gm.equals("getObject")) {
					sb.pn("        r.put(${1}, ${2}.toMap());", hs, s);
				} else {
					if (gm.equals("getInt") || gm.equals("getLong")) {
						sb.pn("        r.put(${1}, LuaEncode.encode(${2}));",
								hs, s);
					} else {
						sb.pn("        r.put(${1}, ${2});", hs, s);
					}
				}
			}
		}
		sb.pn("        return r;");
		sb.pn("    }");
		sb.pn("");

		sb.pn("");
		sb.pn("    public static ${1} parse(NewMap map2) {", cname);
		sb.pn("        if(map2 == null) return null;");
		sb.pn("");
		sb.pn("        ${1} r = new ${1}();", cname);
		if (isHasNum) {
			sb.pn("        try {");
		}
		for (Field field : fs) {
			String t = B2G.getType(field);
			String gm = B2G.getMapType(t);
			String s = field.getName();
			int hs = s.hashCode();
			if (s.contains("$"))
				continue;

			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				boolean isBType = B2G.isBType(gtype);
				if (gtype != null && !gtype.isEmpty() && !isBType) {
					sb.pn("        r.${1} = maps_${1}( map2.getList(\"${3}\") );",
							s, gm, hs, gtype);
				} else {
					sb.pn("        r.${1} = map2.${2}(\"${3}\");", s, gm, hs);
				}
			} else {
				if (gm.equals("getObject")) {
					sb.pn("        r.${1} = ${2}.parse(map2.getNewMap(\"${3}\"));",
							s, t, hs);
				} else {
					if (gm.equals("getInt")) {
						gm = "get";
						sb.pn("        r.${1} = LuaEncode.decode(map2.${2}(\"${3}\"),Integer.class);",
								s, gm, hs);
					} else if (gm.equals("getLong")) {
						gm = "get";
						sb.pn("        r.${1} = LuaEncode.decode(map2.${2}(\"${3}\"),Long.class);",
								s, gm, hs);
					} else {
						sb.pn("        r.${1} = map2.${2}(\"${3}\");", s, gm,
								hs);
					}
				}
			}
		}
		if (isHasNum) {
			sb.pn("        } catch (Exception e) {");
			sb.pn("        }");
		}
		sb.pn("        return r;");
		sb.pn("    }");
		sb.pn("");

		StrBuilder sbts = new StrBuilder();
		sbts.a("\"").a(cname).a("[");
		for (Field field : fs) {
			String t = B2G.getType(field);
			String gm = B2G.getMapType(t);
			String s = field.getName();
			if (s.contains("$"))
				continue;
			sbts.a("").a(s).a("=\" + ").a(s).a(" + \", ");
		}
		sbts.removeRight(2);
		sbts.a("]\"");
		sb.pn("    public String toString() {");
		sb.pn("        return ${1};", sbts);
		sb.pn("    }");
		sb.pn("");

		sb.pn("    public static ${1} parse(byte[] buf) throws Exception {",
				cname);
		sb.pn("        NewMap map2 = com.bowlong.bio2.B2Helper.toMap(buf);");
		sb.pn("        return parse(map2);");
		sb.pn("    }");
		sb.pn("");

		sb.pn("    public static ${1} parse(InputStream in) throws Exception {",
				cname);
		sb.pn("        NewMap map2 = com.bowlong.bio2.B2InputStream.readMap(in);");
		sb.pn("        return parse(map2);");
		sb.pn("    }");
		sb.pn("");

		sb.pn("    public byte[] toByteArray() throws Exception {");
		sb.pn("        return com.bowlong.bio2.B2Helper.toBytes(toMap());");
		sb.pn("    }");
		sb.pn("");

		g2bean_Sheet(c, sb);

		sb.pn("}");

	}

	public static void g2bean_Sheet(Class<?> c, StrBuilder sb) {
		Bio2GJava.g2bean_Sheet(c, sb);
	}

	public static void g2beanConstant(Class<?> c, String namespace,
			StrBuilder sb) {
		Bio2GJava.g2beanConstant(c, namespace, sb);
	}

	// 生成CMD方法体，代码(目前没用);
	static void make_cmd_4_service(Method[] methods, StrBuilder sb) {
		StrBuilder sb2 = new StrBuilder();
		for (Method m : methods) {
			String rtype = B2G.getReturnType(m);
			if (B2G.isClient(m) && rtype.equals("void"))
				continue;
			String mname = B2G.getNethodName(m);
			int hmname = mname.hashCode();
			sb2.ap("${1},", hmname);
		}
		sb2.removeRight(1);
		String s = sb2.toString();

		sb.pn("");
		sb.pn("    public static final Set<Integer> CMD = NewSet.create(${1});",
				s);

		sb.pn("");
	}

	// 生成服务器接口
	public static void g2s_service(Class<?> c, String namespace, StrBuilder sb) {
		Method[] methods = c.getMethods();
		String cname = c.getSimpleName();
		sb.pn("package gen_b2g${1};", StrEx.isEmpty(namespace) ? "" : "."
				+ namespace);
		// sb.pn("package gen_b2g;");
		sb.pn("");
		sb.pn("import java.io.*;");
		sb.pn("import java.util.*;");
		sb.pn("");
		sb.pn("import com.bowlong.io.*;");
		sb.pn("import com.bowlong.util.*;");
		sb.pn("import com.bowlong.net.*;");
		sb.pn("import com.bowlong.util.ExceptionEx;");
		// sb.pn("import org.apache.commons.logging.*;");
		sb.pn("import com.bowlong.net.proto.gen.lua.LuaEncode;");
		sb.pn("");
		sb.pn("import gen_b2g${1}.bean.*;", StrEx.isEmpty(namespace) ? "" : "."
				+ namespace);
		sb.pn("");
		// sb.pn("@SuppressWarnings({ \"rawtypes\", \"unchecked\", \"unused\" })");
		sb.pn("@SuppressWarnings(\"all\")");
		sb.pn("public abstract class ${1} extends com.bowlong.net.proto.NSupport {",
				cname);
		sb.pn("");
		// sb.pn("    static Log log = LogFactory.getLog(${1}.class);", cname);
		// sb.pn("    public abstract TcpChannel chn(int XID) throws Exception;");
		// sb.pn("");

		g2s_service_to_client(methods, sb);

		// make_cmd_4_service(methods, sb);

		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 逻辑分发");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		sb.pn("    public String disp(TcpChannel chn, NewMap map) throws Exception {");
		sb.pn("        if(chn == null) return \"\";");
		sb.pn("        int cmd = map.getInt(${1});", B2G.METHOD);
		sb.pn("        return disp(chn, cmd, map);");
		sb.pn("    }");
		sb.pn("    public String disp(TcpChannel chn, int cmd, NewMap map) throws Exception {");
		sb.pn("        if(chn == null) return \"\";");
		sb.pn("        switch (cmd) {");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int hmname = mname.hashCode();
			if (B2G.isServer(m)) {
				sb.pn("            case ${1}: { //  ${2}", hmname, remark);
				sb.pn("                __${1}(chn, map);", mname);
				sb.pn("                return \"${1}\";", mname);
				sb.pn("            }");
			} else {
				if (!srtype.equals("void")) {
					sb.pn("            case ${1}: { //  ${2}", hmname, remark);
					sb.pn("                __onCallback_${1}(chn, map);", mname);
					sb.pn("                return \"${1}\";", mname);
					sb.pn("            }");
				}
			}
		}
		sb.pn("        }");
		sb.pn("        throw new Exception(\" cmd: \" + cmd + \":\" + map + \" not found processor.\");");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 解析参数");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int h_mname = mname.hashCode();
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			StrBuilder sb1 = new StrBuilder();

			// 解析参数函数
			sb.pn("    // ${1}", remark);
			if (B2G.isClient(m)) {
				if (!srtype.equals("void")) {
					String mx = B2G.getMapType(srtype);

					sb.pn("    private void __onCallback_${1}(TcpChannel chn, NewMap map2) throws Exception {",
							mname);
					sb.pn("        if(chn == null) return;");
					// sb.pn("        NewMap map2 = NewMap.create(map);");
					sb.pn("        NewMap retVal = map2.getNewMap(1);", srtype,
							mx);
					sb.pn("");
					sb.pn("        ReturnStatus rst = ReturnStatus.parse(retVal);");
					sb.pn("");
					sb.pn("        on${1}(chn, rst);", upper1(mname));
					sb.pn("    }");
				}
			} else {
				sb.pn("    private void __${1}(TcpChannel chn, NewMap map2) throws Exception {",
						mname);
				sb.pn("        if(chn == null) return;");

				StringBuffer sbThrow = new StringBuffer("Object[] othrows = {");

				int nOutNum = 0;
				for (NewMap<String, String> m1 : params) {
					String key = (String) m1.getKey();
					String val = (String) m1.getValue();
					String hval = val.hashCode() + "";
					String p = B2G.getMapType(key);
					boolean isOut = B2G.isOut(m, val);
					if (isOut) {
						sb.pn("        ${1} ${2} = new ${1}();", key, val);
						nOutNum++;
					} else {
						if (p.equals("getObject")) {
							sb.pn("        ${1} ${2} = ${1}.parse(map2.getNewMap(\"${3}\"));",
									key, val, hval);
						} else {

							if (p.equals("getList")) {
								String oType = B2G.getOType(m, val);
								String mType = B2G.getMapType(oType);
								if (mType.equals("getObject")) {
									sb.pn("        ${1}<${2}> ${3} = new NewList<${2}>();",
											key, oType, val);
									sb.pn("        {");
									sb.pn("            List<NewMap> maps = map2.${1}(\"${2}\");",
											p, hval);
									sb.pn("            for(NewMap m1 : maps) {");
									sb.pn("                ${1}.add(${2}.parse(m1));",
											val, oType);
									sb.pn("            }");
									sb.pn("        }");
									key = PStr.str("${1}<${2}>", key, oType);
								} else {
									sb.pn("        ${1} ${2} = map2.${3}(\"${4}\");",
											key, val, p, hval);

								}
							} else {
								if ("getInt".equals(p) || "getLong".equals(p)
										|| "getByte".equals(p)
										|| "getShort".equals(p)
										|| "getFloat".equals(p)
										|| "getDouble".equals(p)) {
									sb.pn("        ${1} ${2} = (${1}) map2.getDouble(${3});",
											key, val, hval);
								} else {
									sb.pn("        ${1} ${2} = map2.${3}(${4});",
											key, val, p, hval);
								}
							}
						}
					}
					sb1.ap(", ${1}", val);

					sbThrow.append("\"\\\"").append(val).append(":\\\"\", ")
							.append(val).append(", ");
				}

				sbThrow.append("};");

				sb.pn("");
				if (srtype.equals("void")) {
					sb.pn("        try {");
					sb.pn("            on${1}(chn${2});", upper1(mname), sb1);
					sb.pn("        } catch ( Exception e ) {");

					// 此处添加了一个打印错误信息
					// sb.pn("            e.printStackTrace();");

					sb.pn("            ${1}", sbThrow);
					sb.pn("            onExcept(chn, $[1], rethrow(e, $[1], othrows), null);",
							mname);
					sb.pn("        }");
				} else {
					sb.pn("        ${1} rst = new ${1}();", srtype);
					sb.pn("        try {");
					sb.pn("            on${1}(chn${2}, rst);", upper1(mname),
							sb1);
					sb.pn("        } catch ( Exception e ) {");

					// 此处添加了一个打印错误信息
					// sb.pn("            e.printStackTrace();");

					sb.pn("            ${1}", sbThrow);
					sb.pn("            onExcept(chn, $[1], rethrow(e, $[1], othrows), rst);",
							mname);
					sb.pn("        }");
					sb.pn("        try ( ByteOutStream result = getStream();) {");
					sb.pn("            writeMapTag(result, ${1});", nOutNum + 2);
					sb.pn("            writeMapEntry(result, ${1}, ${2});",
							B2G.METHOD, h_mname);
					sb.pn("            writeMapEntry(result, ${1}, rst.toMap());",
							B2G.RETURN_STAT);
					for (NewMap<String, String> m1 : params) {
						String key = (String) m1.getKey();
						String val = (String) m1.getValue();
						String hval = val.hashCode() + "";
						String p = B2G.getMapType(key);
						boolean isOut = B2G.isOut(m, val);
						if (isOut) {
							sb.pn("            writeMapEntry(result, ${1}, ${2}.toMap());",
									hval, val);
						}
					}
					sb.pn("            chn.send(result.toByteArray());");
					sb.pn("        } catch (Exception e) {");
					sb.pn("            throw e;");
					sb.pn("        }");
				}
				sb.pn("    }");

				if (B2G.isServer(m)) {
					if (!srtype.equals("void")) {
						StrBuilder msb = new StrBuilder();
						for (NewMap<String, String> m1 : params) {
							String key = (String) m1.getKey();
							String val = (String) m1.getValue();
							String hval = val.hashCode() + "";
							String p = B2G.getMapType(key);
							boolean isOut = B2G.isOut(m, val);
							if (isOut) {
								if (p.equals("getObject")) {
									msb.ap(", ${1} ${2}", key, val);
								}
							}
						}
					}
					sb.pn("");
				}
			}
		}

		sb.pn("");
		sb.pn("    public static Exception rethrow(Exception cause, String method, Object... params) {");
		// sb.pn("        String causeMessage = cause.getMessage();");
		sb.pn("        String causeMessage = ExceptionEx.e2s(cause);");
		sb.pn("        if (causeMessage == null) {");
		sb.pn("            causeMessage = \"\";");
		sb.pn("        }");
		sb.pn("        StringBuffer msg = new StringBuffer(causeMessage);");
		sb.pn("        msg.append(\"\\r\\n\");");
		sb.pn("        msg.append(method);");
		sb.pn("        msg.append(\" Parameters: \");");
		sb.pn("        msg.append(\"\\r\\n\");");
		sb.pn("        if (params == null) {");
		sb.pn("            msg.append(\"[]\");");
		sb.pn("        } else {");
		sb.pn("            msg.append(Arrays.deepToString(params));");
		sb.pn("        }");
		sb.pn("        msg.append(\"\\r\\n\");");
		sb.pn("        Exception e = new Exception(msg.toString(), cause);");
		sb.pn("        return e;");
		sb.pn("    }");
		sb.pn("");
		sb.pn("");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 需要实现的接口");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		sb.pn("    public abstract void onExcept(TcpChannel chn, String method, Exception e, ReturnStatus ret) throws Exception;");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			StrBuilder sb1 = new StrBuilder();
			for (NewMap<String, String> m1 : params) {
				String key = (String) m1.getKey();
				String val = (String) m1.getValue();
				String p = B2G.getMapType(key);
				if (p.equals("getList") && !"".equals(B2G.getOType(m, val))) {
					key = PStr.str("${1}<${2}>", key, B2G.getOType(m, val));
				}
				sb1.ap(", ${1} ${2}", key, val);
			}

			// 需要实现的逻辑函数
			if (B2G.isServer(m)) {
				sb.pn("    // ${1}", remark);
				if (!srtype.equals("void")) {
					sb.pn("    public abstract void on${2}(TcpChannel chn ${3}, ${1} ret) throws Exception;",
							srtype, upper1(mname), sb1);
				} else {
					sb.pn("    public abstract void on${2}(TcpChannel chn ${3}) throws Exception;",
							srtype, upper1(mname), sb1);
				}
			} else {
				if (!srtype.equals("void")) {
					sb.pn("    // ${1}", remark);
					sb.pn("    public void on${1}(TcpChannel chn, ${2} val) throws Exception { };",
							upper1(mname), srtype);
				}
			}
		}

		// pv 请求
		g2s_pv(methods, sb);

		sb.pn("}");
	}

	// 向客户端主动发送
	static private void g2s_service_to_client(Method[] methods, StrBuilder sb) {
		for (Method m : methods) {
			if (!B2G.isClient(m))
				continue;
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int h_mname = mname.hashCode();
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			StrBuilder sb1 = new StrBuilder();
			StrBuilder sb2 = new StrBuilder();
			for (NewMap<String, String> m1 : params) {
				String key = (String) m1.getKey();
				String val = (String) m1.getValue();
				// StrBuilder sb0 = new StrBuilder();
				if (B2G.getMapType(key).equals("getList")) {
					key = PStr.str("${1}<${2}>", key, B2G.getOType(m, val));
					// sb0.ap("${1}<${2}>", key, B2G.getOType(m, val));
					// key = sb0.toString();
				}
				sb1.ap(", ${1} ${2}", key, val);
				sb2.ap(", ${1}", val);
			}

			sb.pn("    // //////////////////////////////////////////////");
			sb.pn("    // 向客户端 主动发送 逻辑调用");
			sb.pn("    // //////////////////////////////////////////////");
			sb.pn("");
			sb.pn("    // ${1}", remark);
			sb.pn("    public void ${1}(TcpChannel chn ${2},ReturnStatus rst) throws Exception {",
					mname, sb1);
			// sb.pn("        ${1}(chn, null ${2});", mname, sb2);
			sb.pn("        if(chn == null) return;");
			sb.pn("        if(rst == null)");
			sb.pn("           rst = new ReturnStatus();");

			sb.pn("        Map _params = new HashMap();");
			sb.pn("        _params.put(${1},${2}); // cmd:${3}", B2G.METHOD,
					h_mname, mname);
			sb.pn("        _params.put(${1},rst.toMap());", B2G.RETURN_STAT);
			int i = 0;
			for (NewMap<String, String> m1 : params) {
				String key = (String) m1.getKey();
				String val = (String) m1.getValue();
				int hval = val.hashCode();
				if (B2G.getMapType(key).equals("getObject")) {
					sb.pn("        _params.put(${1}, ${2}.toMap());", hval, val);
				} else {
					if (B2G.getMapType(key).equals("getList")) {
						sb.pn("		{");
						sb.pn("			// Lsit对象(${1})", val);
						sb.pn("			List _list = new NewList();");
						String oType = B2G.getOType(m, val);
						String mType = B2G.getMapType(oType);
						sb.pn("			for (${1} object : ${2}) {", oType, val);
						if (mType.equals("getObject")) {
							sb.pn("            _list.add(object.toMap());");
						} else {
							sb.pn("            _list.add(object);");
						}
						sb.pn("			}");
						sb.pn("			_params.put(${1}, _list);", hval);
						sb.pn("		}");
					} else {
						sb.pn("        _params.put(${1}, ${2});", hval, val);
					}
				}
				i++;
			}
			sb.pn("        chn.send(_params);");
			sb.pn("    }");
		}
	}

	private static void g2s_pv(Method[] methods, StrBuilder sb) {
		sb.pn("");
		sb.pn("");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // PV  请求  参数 逻辑分发  ");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		sb.pn("    public String pv_params(NewMap map) throws Exception {");
		sb.pn("        int cmd = map.getInt(${1});", B2G.METHOD);
		sb.pn("        return pv_params(cmd, map);");
		sb.pn("    }");
		sb.pn("    public String pv_params(int cmd, NewMap map) throws Exception {");
		sb.pn("        switch (cmd) {");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int hmname = mname.hashCode();
			if (B2G.isServer(m) || B2G.isClient(m)) {
				sb.pn("            case ${1}: { //  ${2}", hmname, remark);
				sb.pn("                return pv_${1}_params(map);", mname);
				sb.pn("            }");
			}
		}
		sb.pn("        }");
		sb.pn("        return (\" cmd: \" + cmd + \":\" + map + \" not found processor.\");");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // PV 请求  解析参数 ");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			String hmname = mname.hashCode() + "";
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			StrBuilder sb1 = new StrBuilder();

			// 解析参数函数
			sb.pn("    // ${1}", remark);
			sb.pn("    private String pv_${1}_params(NewMap map2) throws Exception {",
					mname);
			StrBuilder sbT = new StrBuilder();
			sbT.an("        StringBuffer sb = com.bowlong.objpool.StringBufPool.borrowObject();");
			sbT.an("        try {");
			sbT.pn("            sb.append(\"${1}(\");", mname);
			int nOutNum = 0;
			for (NewMap<String, String> m1 : params) {
				String key = (String) m1.getKey();
				String val = (String) m1.getValue();
				String hval = val.hashCode() + "";
				String p = B2G.getMapType(key);
				boolean isOut = B2G.isOut(m, val);
				if (isOut) {
					// sb.pn("        ${1} ${2} = new ${1}();", key, val);
					nOutNum++;
				} else {
					if (p.equals("getObject")) {
						sb.pn("        ${1} ${2} = ${1}.parse(map2.getNewMap(${3}));",
								key, val, hval);
					} else {

						if (p.equals("getList")) {
							String oType = B2G.getOType(m, val);
							String mType = B2G.getMapType(oType);
							if (mType.equals("getObject")) {
								sb.pn("        ${1}<${2}> ${3} = new NewList<${2}>();",
										key, oType, val);
								sb.pn("        {");
								sb.pn("            List<NewMap> maps = map2.${1}(${2});",
										p, hval);
								sb.pn("            for(NewMap m1 : maps) {");
								sb.pn("                ${1}.add(${2}.parse(m1));",
										val, oType);
								sb.pn("            }");
								sb.pn("        }");
								key = PStr.str("${1}<${2}>", key, oType);
							} else {
								sb.pn("        ${1} ${2} = map2.${3}(${4});",
										key, val, p, hval);

							}
						} else {
							sb.pn("        ${1} ${2} = map2.${3}(${4});", key,
									val, p, hval);
						}
					}
					sb1.ap(", ${1}", val);
					sbT.pn("            sb.append(\"\\\"${1}\\\":\").append(${1}).append(\",\");",
							val);
				}
			}
			sbT.pn("            sb.append(\")\");");
			sbT.pn("            return sb.toString();");
			sbT.an("        } finally {");
			sbT.an("            com.bowlong.objpool.StringBufPool.returnObject(sb);");
			sbT.an("        }");

			sb.pn(sbT.str());
			sb.pn("    }");
		}
	}

	// 生成客户端接口
	public static void g2s_call(Class<?> c, String namespace, StrBuilder sb) {
		String sname = c.getSimpleName();
		Method[] methods = c.getMethods();
		String cname = c.getSimpleName();
		sb.pn("package gen_b2g${1};", StrEx.isEmpty(namespace) ? "" : "."
				+ namespace);
		// sb.pn("package gen_b2g;");
		sb.pn("");
		// sb.pn("import java.io.*;");
		sb.pn("import java.util.*;");
		sb.pn("");
		// sb.pn("import com.bowlong.io.*;");
		sb.pn("import com.bowlong.util.*;");
		sb.pn("import com.bowlong.net.*;");
		sb.pn("");
		sb.pn("import gen_b2g${1}.bean.*;", StrEx.isEmpty(namespace) ? "" : "."
				+ namespace);
		sb.pn("");
		// sb.pn("@SuppressWarnings({ \"rawtypes\", \"unchecked\" })");
		sb.pn("@SuppressWarnings(\"all\")");
		sb.pn("public abstract class Call${1} {", cname);

		sb.pn("");
		sb.pn("    public static int __pid;");
		sb.pn("    public TcpChannel chn;");
		sb.pn("    public Call${1}(TcpChannel chn) {", sname);
		sb.pn("        this.chn = chn;");
		sb.pn("    }");
		sb.pn("");
		for (Method m : methods) {
			if (!B2G.isServer(m))
				continue;

			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			String hhmname = mname.hashCode() + "";
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			StrBuilder sb1 = new StrBuilder();
			for (NewMap<String, String> m1 : params) {
				String mykey = (String) m1.getKey();
				String myvar = (String) m1.getValue();
				String hval = myvar.hashCode() + "";
				boolean isOut = B2G.isOut(m, myvar);
				if (isOut) {

				} else {
					if (mykey.equals("List")
							&& !"".equals(B2G.getOType(m, myvar))) {
						mykey = PStr.str("${1}<${2}>", mykey,
								B2G.getOType(m, myvar));
					}
					sb1.ap("${1} ${2}, ", mykey, myvar);
				}
			}
			if (sb1.length() > 2) {
				sb1.removeRight(2);
			}

			// 需要实现的逻辑函数
			sb.pn("    // ${1}", remark);
			sb.pn("    public void ${1}(${2}) throws Exception {", mname, sb1);
			sb.pn("        Map _map = new HashMap();");
			sb.pn("        _map.put(\"-100\", __pid);  // _pid");
			sb.pn("        _map.put(\"${1}\", ${2});  // cmd:${3}", B2G.METHOD,
					hhmname, mname);
			for (NewMap<String, String> m1 : params) {
				String key = (String) m1.getKey();
				String val = (String) m1.getValue();
				String p = B2G.getMapType(key);
				String hval = val.hashCode() + "";
				boolean isOut = B2G.isOut(m, val);
				if (isOut) {

				} else {
					if (p.equals("getList")) {
						String oType = B2G.getOType(m, val);
						String mType = B2G.getMapType(oType);
						if (mType.equals("getObject")) {
							sb.pn("		{");
							sb.pn("			// Lsit对象(${1})", val);
							sb.pn("		    List<Map> ${1}_list = new NewList<Map>();",
									val);
							sb.pn("        _map.put(\"${1}\", ${2});", hval,
									val);
							sb.pn("        _map.put(\"${1}\", ${2}_list);",
									hval, val);
							sb.pn("			for(${1} obj : ${2}) {", oType, val);
							sb.pn("				${1}_list.add(obj.toMap());", val, oType);
							sb.pn("			}");
							sb.pn("		}");
							val += "_list";
						} else {
						}
					} else if (B2G.getMapType(key).equals("getObject")) {
						sb.pn("        _map.put(\"${1}\", ${2}.toMap());",
								hval, val);
					} else {
						sb.pn("        _map.put(\"${1}\", ${2});", hval, val);
					}
				}
			}
			sb.pn("        chn.send(_map);");
			sb.pn("    }");
			sb.pn("");
		}

		StrBuilder sb2 = new StrBuilder();
		for (Method m : methods) {
			String rtype = B2G.getReturnType(m);
			if (B2G.isServer(m) && rtype.equals("void"))
				continue;
			String mname = B2G.getNethodName(m);
			String hmane = mname.hashCode() + "";
			sb2.ap("${1},", hmane);
		}
		sb2.removeRight(1);
		String s = sb2.toString();

		sb.pn("");
		sb.pn("    public static final Set<Integer> CMD = NewSet.create(${1});",
				s);
		sb.pn("");
		sb.pn("    public static boolean in(NewMap map) throws Exception {");
		sb.pn("        int cmd = map.getInt(\"${1}\");", B2G.METHOD);
		sb.pn("        return CMD.contains(cmd);");
		sb.pn("    }");
		sb.pn("");

		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 逻辑分发");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		sb.pn("    public void disp(NewMap map) throws Exception {");
		sb.pn("        int cmd = map.getInt(\"${1}\");", B2G.METHOD);
		sb.pn("        disp(cmd, map);");
		sb.pn("    }");
		sb.pn("    public void disp(int cmd, NewMap map) throws Exception {");
		sb.pn("        switch (cmd) {");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int hmname = mname.hashCode();
			if (B2G.isServer(m)) {
				if (!srtype.equals("void")) {
					sb.pn("            case ${1}: { //  ${2}", hmname, remark);
					sb.pn("                __onCallback_${1}(map);", mname);
					sb.pn("                return;");
					sb.pn("            }");
				}
			} else {
				sb.pn("            case ${1}: { //  ${2}", hmname, remark);
				sb.pn("                __onCall_${1}(map);", mname);
				sb.pn("                return;");
				sb.pn("            }");
			}
		}
		sb.pn("        }");
		sb.pn("        throw new Exception(\" cmd: \" + cmd + \":\" + map + \" not found processor.\");");
		sb.pn("    }");
		sb.pn("");
		sb.pn("");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 参数解析");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			String hmname = mname.hashCode() + "";
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			// 解析参数函数
			if (B2G.isServer(m)) {
				if (!srtype.equals("void")) {
					sb.pn("    // ${1}", remark);
					sb.pn("    private void __onCallback_${1}(NewMap map2) throws Exception {",
							mname);
					String mx = B2G.getMapType(srtype);
					// sb.pn("        NewMap map2 = NewMap.create(map);");
					sb.pn("        NewMap retVal = map2.getNewMap(\"${1}\");",
							B2G.RETURN_STAT);
					sb.pn("        ReturnStatus rst = ReturnStatus.parse(retVal);");

					StrBuilder msb = new StrBuilder();
					for (NewMap<String, String> m1 : params) {
						String key = (String) m1.getKey();
						String val = (String) m1.getValue();
						String hval = val.hashCode() + "";
						String p = B2G.getMapType(key);
						boolean isOut = B2G.isOut(m, val);
						if (isOut) {
							if (p.equals("getObject")) {
								sb.pn("        ${1} ${2} = ${1}.parse(map2.getNewMap(\"${3}\"));",
										key, val, hval);
								msb.ap("${1}, ", val);
							}
						}
					}

					sb.pn("");
					sb.pn("        on${1}(${2}rst);", upper1(mname), msb);
					sb.pn("    }");
				}
			} else {
				sb.pn("    // ${1}", remark);
				sb.pn("    private void __onCall_${1}(NewMap map2) throws Exception {",
						mname);
				// sb.pn("        NewMap map2 = NewMap.create(map);");
				sb.pn("");
				StrBuilder sb1 = new StrBuilder();
				for (NewMap<String, String> m1 : params) {
					String key = (String) m1.getKey();
					String val = (String) m1.getValue();
					String hval = val.hashCode() + "";
					String p = B2G.getMapType(key);
					boolean isOut = B2G.isOut(m, val);

					if (p.equals("getObject")) {
						sb.pn("        ${1} ${2} = ${1}.parse(map2.getNewMap(\"${3}\"));",
								key, val, hval);
					} else {
						sb.pn("        ${1} ${2} = map2.${3}(\"${4}\");", key,
								val, p, hval);
						if (p.equals("getList")) {
							String oType = B2G.getOType(m, val);
							String mType = B2G.getMapType(oType);
							sb.pn("		List<${1}> ${2}_list = new NewList<${3}>();",
									oType, val, oType);
							sb.pn("		{");
							sb.pn("			// Lsit对象(${1})", val);
							sb.pn("			for(Object obj : ${1}) {", val);
							if (mType.equals("getObject"))
								sb.pn("				${1}_list.add(${2}.parse((NewMap)obj));",
										val, oType);
							else
								sb.pn("				${1}_list.add((${2})obj);", val,
										oType);

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
					sb.pn("        on${1}(${2});", upper1(mname), sb1);
				} else {

					sb.pn("        ReturnStatus rst = on${1}(${2});",
							upper1(mname), sb1, srtype);

					sb.pn("        Map result = new HashMap();");
					sb.pn("        result.put(\"${1}\", ${2});", B2G.METHOD,
							hmname);
					sb.pn("        result.put(\"${1}\", rst.toMap());",
							B2G.RETURN_STAT);
					sb.pn("        chn.send(result);");
				}
				sb.pn("    }");
			}
			sb.pn("");
		}

		g2s_pv(methods, sb);

		sb.pn("");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 需要实现的接口");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("");
		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			// String oType = B2G.getOType(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			NewList<NewMap<String, String>> params = B2G.getParameters(m);
			// 解析参数函数
			sb.pn("    // ${1}", remark);
			if (B2G.isServer(m)) {
				if (!srtype.equals("void")) {
					StrBuilder msb = new StrBuilder();
					for (NewMap<String, String> m1 : params) {
						String key = (String) m1.getKey();
						String val = (String) m1.getValue();
						String hval = val.hashCode() + "";
						String p = B2G.getMapType(key);
						boolean isOut = B2G.isOut(m, val);
						if (isOut) {
							if (p.equals("getObject")) {
								msb.ap("${1} ${2}, ", key, val);
							}
						}
					}

					sb.pn("    public void on${1}(${2}${3} val) throws Exception {};",
							upper1(mname), msb, srtype);
				}
			} else {

				StrBuilder sb1 = new StrBuilder();
				for (NewMap<String, String> m1 : params) {
					String key = (String) m1.getKey();
					String val = (String) m1.getValue();
					if (B2G.getMapType(key).equals("getList")) {
						key = PStr.str("${1}<${2}>", key, B2G.getOType(m, val));
					}
					sb1.ap("${1} ${2}, ", key, val);
				}
				if (sb1.length() > 2) {
					sb1.removeRight(2);
				}

				// 需要实现的逻辑函数
				sb.pn("    public abstract ${1} on${2}(${3}) throws Exception;",
						srtype, upper1(mname), sb1);

			}
			sb.pn("");
		}
		sb.pn("}");
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
