package com.bowlong.net.proto.gen.lua.ver3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bowlong.lang.PStr;
import com.bowlong.lang.StrEx;
import com.bowlong.net.proto.gen.B2Class;
import com.bowlong.net.proto.gen.B2Field;
import com.bowlong.net.proto.gen.B2G;
import com.bowlong.text.Encoding;
import com.bowlong.util.NewList;
import com.bowlong.util.NewMap;
import com.bowlong.util.StrBuilder;

@SuppressWarnings({ "unused" })
public class Bio2GLuaBts {
	/**
	 * @param args
	 * @return
	 */
	public static String b2g(Class<?> c, boolean src) {
		B2Class B2C = c.getAnnotation(B2Class.class);
		String sname = c.getSimpleName();
		String namespace = "";
		if (B2C != null) {
			namespace = B2C.namespace();
		}

		// String p = "gen_b2g";
		String p = (src ? "src/" : "") + "gen_b2g";
		if (namespace != null && !namespace.isEmpty()) {
			p = p + "/" + namespace;
		}
		File path = new File(p);
		if (!path.exists())
			path.mkdirs();

		Class<?>[] classes = c.getDeclaredClasses();
		StrBuilder sb = new StrBuilder();

		sb.pn("do");
		sb.pn("");
		// sb.pn("local long2Bio= NumEx.Long2Bio;");
		// sb.pn("local bio2Long= NumEx.bio2Long;");
		sb.pn("  local int2Bio = NumEx.int2Bio;");
		sb.pn("  local bio2Int = NumEx.bio2Int;");
		sb.pn("  local getByIntKey = MapEx.getByIntKey;");
		sb.pn("");
		sb.pn("  " + sname + " = {");
		sb.pn("");
		// =========== 生成实体对象

		for (Class<?> class1 : classes) {
			if (B2G.isData(class1)) {
				String f = class1.getSimpleName();
				// 是否是常量类
				boolean isConstant = B2G.isConstant(class1);
				if (isConstant) {
					g2beanConstant(class1, namespace, sb);
				} else {
					g2bean(class1, namespace, sb, sname);
				}
			}
		}

		// =========== end

		// 请求
		List<Class<?>> listServer = new ArrayList<Class<?>>();
		for (Class<?> class1 : classes) {
			if (B2G.isServer(class1)) {
				listServer.add(class1);
			}
		}

		sb.pn("   callNet = { ");
		sb.pn("    __sessionid = 0;");
		sb.pn("");

		for (Class<?> class1 : listServer) {
			if (B2G.isServer(class1)) {
				lua_request(class1, namespace, sb, sname);
			}
		}
		sb.pn("   };");
		sb.pn("");

		// 回调
		sb.pn("   --[[");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    // 请求回掉分发解析");
		sb.pn("    // //////////////////////////////////////////////");
		sb.pn("    --]]");
		sb.pn("");
		sb.pn("    onCallNet = { ");
		sb.pn("");

		for (Class<?> class1 : listServer) {
			if (B2G.isServer(class1)) {
				lua_call(class1, namespace, sb, sname);
			}
		}
		sb.pn("    };");
		sb.pn("");
		sb.pn("  };");
		sb.pn("");

		// swich
		for (Class<?> class1 : listServer) {
			if (B2G.isServer(class1)) {
				lua_call_swich(class1, namespace, sb, sname);
			}
		}

		sb.pn("end;");
		sb.pn("");

		// sb.pn("module(\"" + sname + "\",package.seeall)");

		writeFile(p + "/" + sname + ".lua", sb.toString());

		System.out.println(sb);
		return sb.toString();
	}

	// 普通类
	public static void g2bean(Class<?> c, String namespace, StrBuilder sb,
			String fathName) {
		Field[] fs = c.getDeclaredFields();
		String cname = c.getSimpleName();
		int hcname = cname.hashCode();
		sb.pn("    ${1} = {", cname);

		// ///////
		for (Field field : fs) {
			B2Field a = field.getAnnotation(B2Field.class);
			String s = field.getName();
			String t = B2G.getType(field);
			if (s.contains("$"))
				continue;

			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				boolean isBtype = B2G.isBType(gtype);
				if (gtype != null && !gtype.isEmpty() && !isBtype) {
					sb.pn("      list_${1} = function(list) ", s);
					sb.pn("        if(list == nil) then");
					sb.pn("          return {};");
					sb.pn("        end;");
					sb.pn("        local len = list.Count");
					sb.pn("        local r = {};");
					sb.pn("        for i=0,len-1 do");
					sb.pn("          local _e = list[i];");
					sb.pn("          local e = ${1}.${2}.parse(_e);", fathName,
							gtype);
					sb.pn("          if(e ~= nil) then");
					sb.pn("             table.insert(r, e);");
					sb.pn("          end");
					sb.pn("        end");
					sb.pn("        return r;");
					sb.pn("      end;");
					sb.pn("");
				}
			} else if (field.getType().equals(Map.class)) {
				String[] types = getMapType(field);
				String gtype0 =types[0];
				String gtype =types[1]; 
				boolean isBtype = B2G.isBType(gtype);
//				if (gtype != null && !gtype.isEmpty() && !isBtype) {
					sb.pn("      map_${1} = function(map) ", s);
					sb.pn("        if(map == nil) then");
					sb.pn("          return {};");
					sb.pn("        end;");
					sb.pn("        local len = map.Keys.Count");
					sb.pn("        local r = {};");
					sb.pn("        local iter = map:GetEnumerator();");
					sb.pn("        while iter:MoveNext() do");
					if (gtype0 != null && !gtype0.isEmpty() && !B2G.isBType(gtype0)) {
						sb.pn("          printe('not serport type')");
					}
					sb.pn("          local k = iter.Current.Key;");
					sb.pn("          local _e = iter.Current.Value;");
					sb.pn("          local e = ${1}.${2}.parse(_e);", fathName,
							gtype);
					sb.pn("          if(e ~= nil) then");
					sb.pn("             r[k] = e;");
					sb.pn("          end");
					sb.pn("        end");
					sb.pn("        return r;");
					sb.pn("      end;");
					sb.pn("");
//				}
			}
		}
		// ///////
		sb.pn("      parse = function(dataMap)");
		sb.pn("        if(dataMap == nil) then return nil; end;");
		sb.pn("");
		// C#的Hashtable
		// sb.pn("        local r = Hashtable();");
		// lua的lua_table
		sb.pn("        local r = {};");
		for (Field field : fs) {
			String t = B2G.getCsType(field);
			String gm = B2G.getCsMapType(t);
			String s = field.getName();
			String remark = B2G.getRemark(field);
			if (!StrEx.isEmpty(remark)) {
				remark = remark.replaceAll("//", "--");
			} else {
				remark = "--";
			}
			remark = remark + " [" + t + "]";

			int hs = s.hashCode();
			if (s.contains("$"))
				continue;

			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				boolean isBtype = B2G.isBType(gtype);
				if (gtype != null && !gtype.isEmpty() && !isBtype) {
					// sb.pn("        r:set_Item(\"${1}\",${2}.${3}.list_${1}(getByIntKey(dataMap,${4})));--${4}",s,
					// fathName, cname, hs, t);
					sb.pn("        r.${1} = ${2}.${3}.list_${1}(getByIntKey(dataMap,${4}));--${4}",
							s, fathName, cname, hs, t);
				} else {
					// sb.pn("        r:set_Item(\"${1}\",getByIntKey(dataMap,${2}));${3}",s,
					// hs, remark);
					sb.pn("        r.${1} = getByIntKey(dataMap,${2});${3}", s,
							hs, remark);
				}
			} else if (field.getType().equals(Map.class)) {
				String[] types = getMapType(field);
				String gtype0 =types[0];
				String gtype =types[1]; 
				boolean isBtype = B2G.isBType(gtype);
				if (gtype != null && !gtype.isEmpty() && !isBtype) {
					// sb.pn("        r:set_Item(\"${1}\",${2}.${3}.list_${1}(getByIntKey(dataMap,${4})));--${4}",s,
					// fathName, cname, hs, t);
					sb.pn("        r.${1} = ${2}.${3}.map_${1}(getByIntKey(dataMap,${4}));--${4}",
							s, fathName, cname, hs, t);
				} else {
					// sb.pn("        r:set_Item(\"${1}\",getByIntKey(dataMap,${2}));${3}",s,
					// hs, remark);
					sb.pn("        r.${1} = getByIntKey(dataMap,${2});${3}", s,
							hs, remark);
				}
			} else {
				if (gm.equals("getObject")) {
					// sb.pn("        r:set_Item(\"${1}\",${2}.${3}.parse(getByIntKey(dataMap,${4})));${5}",s,
					// fathName, t, hs, remark);
					sb.pn("        r.${1} = ${2}.${3}.parse(getByIntKey(dataMap,${4}));${5}",
							s, fathName, t, hs, remark);
				} else {
					// sb.pn("        r:set_Item(\"${1}\",getByIntKey(dataMap,${2}));${3}",s,
					// hs, remark);
					sb.pn("        r.${1} = getByIntKey(dataMap,${2});${3}", s,
							hs, remark);
				}
			}
		}
		sb.pn("        return r;");
		sb.pn("      end;");
		sb.pn("");
		sb.pn("    };");
		sb.pn("");
	}

	// 常量类
	public static void g2beanConstant(Class<?> c, String namespace,
			StrBuilder sb) {
		Field[] fs = c.getDeclaredFields();
		String cname = c.getSimpleName();
		sb.pn("    ${1} = {", cname);
		for (Field field : fs) {
			String t = B2G.getCsType(field);
			String s = field.getName();

			if (s.contains("$"))
				continue;

			String remark = B2G.getRemark(field);
			if (!StrEx.isEmpty(remark)) {
				remark = remark.replaceAll("//", "--");
			} else {
				remark = "--";
			}

			String def = B2G.getDef(field);
			if (field.getType().equals(List.class)) {
				String gtype = B2G.getListType(field);
				if (gtype != null && !gtype.isEmpty()) {
					continue;
				}
			} else {
				if ("string".equals(t))
					sb.pn("      ${1} = \"${3}\"; ${2}", s, remark, def);
				else
					sb.pn("      ${1} = ${3}; ${2}", s, remark, def);
			}
		}
		sb.pn("    };");
		sb.pn("");
	}

	// 生成客户端接口--请求
	public static void lua_request(Class<?> c, String namespace, StrBuilder sb,
			String fathName) {
		String sname = c.getSimpleName();
		Method[] methods = c.getMethods();
		String cname = c.getSimpleName();
		for (Method m : methods) {
			if (!B2G.isServer(m))
				continue;

			String remark = B2G.getRemark(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int h_mname = mname.hashCode();
			NewList<NewMap<String, String>> params = B2G.getParameters(m);
			StrBuilder sb1 = new StrBuilder();
			for (NewMap<String, String> m1 : params) {
				String myvar = (String) m1.getValue();
				boolean isOut = B2G.isOut(m, myvar);
				if (isOut) {

				} else {
					sb1.ap("${1}, ", myvar);
				}
			}
			if (sb1.length() > 2) {
				sb1.removeRight(2);
			}

			// 需要实现的逻辑函数
			sb.pn("      -- ${1}", remark);
			sb.pn("      ${1} = function(${2})", mname, sb1);
			sb.pn("        local _map = Hashtable();");
			sb.pn("        _map[B2Int(-100)] = ${1}.callNet.__sessionid;-- __sessionid",
					fathName);
			sb.pn("        _map[B2Int(${1})] = B2Int(${2});-- cmd:${3}",
					B2G.METHOD, h_mname, mname);
			for (NewMap<String, String> m1 : params) {
				String key = (String) m1.getKey();
				String val = (String) m1.getValue();
				String p = B2G.getMapType(key);
				int h_val = val.hashCode();
				boolean isOut = B2G.isOut(m, val);
				if (isOut) {

				} else {
					if (p.equals("getList")) {
						String oType = B2G.getOType(m, val);
						String mType = B2G.getMapType(oType);
						if (mType.equals("getObject")) {

						} else {
							sb.pn("        _map[B2Int(${1})] = ${2};", h_val,
									val);
						}
					} else if (p.equals("getObject")) {
						sb.pn("        _map[B2Int(${1})] = ${2}.toMap();",
								h_val, val);
					} else if ("getInt".equals(p) || "getByte".equals(p)
							|| "getShort".equals(p)) {
						sb.pn("        _map[B2Int(${1})] = B2Int(${2});",
								h_val, val);
					} else {
						sb.pn("        _map[B2Int(${1})] = ${2};", h_val, val);
					}
				}
			}
			sb.pn("        return _map;");
			sb.pn("      end;");
			sb.pn("");
		}
		sb.pn("");
	}

	// 生成客户端接口--回调
	public static void lua_call(Class<?> c, String namespace, StrBuilder sb,
			String fathName) {
		String sname = c.getSimpleName();
		Method[] methods = c.getMethods();
		String cname = c.getSimpleName();

		String callName = fathName + ".onCallNet";

		sb.pn("      --[[");
		sb.pn("      // //////////////////////////////////////////////");
		sb.pn("      // 逻辑分发");
		sb.pn("      // //////////////////////////////////////////////");
		sb.pn("      --]]");
		sb.pn("");

		sb.pn("      disp = function(map)");
		sb.pn("        local cmd = getByIntKey(map,${1});", B2G.METHOD);
		sb.pn("        ${1}.disp_each(cmd, map);", callName);
		sb.pn("      end;");
		sb.pn("");

		sb.pn("      disp_each = function(cmd,map)");
		sb.pn("        local funMap = ${1}_switch[cmd];", fathName);
		sb.pn("        if(funMap ~= nil) then");
		sb.pn("          local exFun = funMap[\"fun\"];");
		sb.pn("          local exMethod = funMap[\"name\"];");
		sb.pn("          exFun(exMethod, map);");
		sb.pn("        end;");
		sb.pn("");
		sb.pn("      end;");
		sb.pn("");

		sb.pn("");
		sb.pn("      --[[");
		sb.pn("      // //////////////////////////////////////////////");
		sb.pn("      // 参数解析");
		sb.pn("      // //////////////////////////////////////////////");
		sb.pn("      --]]");
		sb.pn("");

		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int hmname = mname.hashCode();
			NewList<NewMap<String, String>> params = B2G.getParameters(m);

			// 解析参数函数
			// if (B2G.isServer(m)) {
			if (!srtype.equals("void")) {
				sb.pn("      -- ${1}", remark);
				sb.pn("      __onCallback_${1} = function(cmd,map)", mname);
				String mx = B2G.getCsMapType(srtype);
				sb.pn("        local retVal = getByIntKey(map,${1});",
						B2G.RETURN_STAT);
				sb.pn("        local rst = ${1}.ReturnStatus.parse(retVal);",
						fathName);
				StrBuilder msb = new StrBuilder();
				for (NewMap<String, String> m1 : params) {
					String key = (String) m1.getKey();
					String val = (String) m1.getValue();
					int hval = val.hashCode();
					String p = B2G.getMapType(key);
					boolean isOut = B2G.isOut(m, val);
					if (isOut) {
						if (p.equals("getObject")) {
							sb.pn("        local ${2} = ${4}.${1}.parse(getByIntKey(map,${3}));",
									key, val, hval, fathName);
							msb.ap("${1}, ", val);
						}
					}
				}
				sb.pn("");
				sb.pn("        cllNetDis.dispatch(cmd,${1}rst);", msb);
				sb.pn("      end;");
			}
			// }
			sb.pn("");
		}
	}

	// 生成客户端接口--回调
	public static void lua_call_swich(Class<?> c, String namespace,
			StrBuilder sb, String fathName) {
		String sname = c.getSimpleName();
		Method[] methods = c.getMethods();
		String cname = c.getSimpleName();

		String callName = fathName + ".onCallNet";

		sb.pn("  --[[");
		sb.pn("  // //////////////////////////////////////////////");
		sb.pn("  // 请求回掉分发解析所对应的对象");
		sb.pn("  // //////////////////////////////////////////////");
		sb.pn("  --]]");
		sb.pn("");

		sb.pn("  " + fathName + "_switch = {");

		for (Method m : methods) {
			String remark = B2G.getRemark(m);
			remark = remark.replaceAll("//", "--");
			String srtype = B2G.getReturnType(m);
			String mname = B2G.getNethodName(m);
			int hmname = mname.hashCode();
			if (!srtype.equals("void")) {
				sb.pn("    [${1}] = {name = \"${2}\"; fun = ${4}.__onCallback_${2}}; --${3}",
						hmname, mname, remark, callName);
			}

			// if (B2G.isServer(m)) {
			// if (!srtype.equals("void")) {
			// sb.pn("    [\"${1}\"] = {[\"name\"] = \"${2}\"; fun = ${4}.__onCallback_${2}}; --${3}",
			// hmname, mname, remark, callName);
			// }
			// } else {
			// sb.pn("    [\"${1}\"] = {[\"name\"] = \"${2}\"; fun = ${4}.__onCall_${2}}; --${3}",
			// hmname, mname, remark, callName);
			// }
		}
		sb.pn("  };");
		sb.pn("");
	}

	public static String upper1(String s) {
		if (s == null || s.isEmpty())
			return s;
		int len = s.length();
		return s.substring(0, 1).toUpperCase() + s.substring(1, len);
	}

	public static void writeFile(String f, String str) {
		try (FileOutputStream out = new FileOutputStream(new File(f));
				OutputStreamWriter osw = new OutputStreamWriter(out,
						Encoding.UTF8);) {
			osw.write(str, 0, str.length());
			osw.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String b2g_globle(Class<?> c, boolean src) {
		B2Class B2C = c.getAnnotation(B2Class.class);
		String sname = c.getSimpleName();
		String namespace = "";
		if (B2C != null) {
			namespace = B2C.namespace();
		}

		String p = (src ? "src/" : "") + "gen_b2g";
		if (namespace != null && !namespace.isEmpty()) {
			p = p + "/" + namespace;
		}
		File path = new File(p);
		if (!path.exists())
			path.mkdirs();

		Class<?>[] classes = c.getDeclaredClasses();
		StrBuilder sb = new StrBuilder();

		sb.pn("do");
		sb.pn("");
		sb.pn("  " + sname + " = {");
		sb.pn("");
		// =========== 生成实体对象
		for (Class<?> class1 : classes) {
			if (B2G.isData(class1)) {
				String f = class1.getSimpleName();
				// 是否是常量类
				boolean isConstant = B2G.isConstant(class1);
				if (isConstant) {
					g2beanConstant(class1, namespace, sb);
				}
			}
		}
		sb.pn("  };");
		sb.pn("");
		sb.pn("end;");
		sb.pn("");

		// sb.pn("module(\"" + sname + "\",package.seeall);");

		writeFile(p + "/" + sname + ".lua", sb.toString());

		System.out.println(sb);
		return sb.toString();
	}

	public static final String[] getMapType(Field f) {
		ParameterizedType pt = (ParameterizedType) f.getGenericType();

		String keyStr = "";
		String valStr = "";

		if (pt.getActualTypeArguments().length <= 1)
			return new String[] { keyStr, valStr };
		Type keyType = pt.getActualTypeArguments()[0];
		Type valType = pt.getActualTypeArguments()[1];
		String str = keyType.toString();
		if ((str.equals("boolean")) || (str.endsWith(".Boolean")))
			keyStr = "Boolean";
		else if ((str.equals("byte")) || (str.endsWith(".Byte")))
			keyStr = "Byte";
		else if ((str.equals("short")) || (str.endsWith(".Short")))
			keyStr = "Short";
		else if ((str.equals("int")) || (str.endsWith(".Integer")))
			keyStr = "Integer";
		else if ((str.equals("long")) || (str.endsWith(".Long")))
			keyStr = "Long";
		else if ((str.equals("float")) || (str.endsWith(".Float")))
			keyStr = "Float";
		else if ((str.equals("double")) || (str.endsWith(".Double")))
			keyStr = "Double";
		else if ((str.equals("String")) || (str.endsWith(".String"))) {
			keyStr = "String";
		} else {
			int pos = str.indexOf("$");
			keyStr = str.substring(pos + 1, str.length());
		}

		// ========================
		str = valType.toString();
		if ((str.equals("boolean")) || (str.endsWith(".Boolean")))
			valStr = "Boolean";
		else if ((str.equals("byte")) || (str.endsWith(".Byte")))
			valStr = "Byte";
		else if ((str.equals("short")) || (str.endsWith(".Short")))
			valStr = "Short";
		else if ((str.equals("int")) || (str.endsWith(".Integer")))
			valStr = "Integer";
		else if ((str.equals("long")) || (str.endsWith(".Long")))
			valStr = "Long";
		else if ((str.equals("float")) || (str.endsWith(".Float")))
			valStr = "Float";
		else if ((str.equals("double")) || (str.endsWith(".Double")))
			valStr = "Double";
		else if ((str.equals("String")) || (str.endsWith(".String"))) {
			valStr = "String";
		} else {
			int pos = str.indexOf("$");
			valStr = str.substring(pos + 1, str.length());
		}

		return new String[] { keyStr, valStr };
	}
}
