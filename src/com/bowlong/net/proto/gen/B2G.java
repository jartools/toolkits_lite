package com.bowlong.net.proto.gen;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.bowlong.util.NewList;
import com.bowlong.util.NewMap;

public class B2G {
	public static final int METHOD = 0;
	public static final int BEAN = -1;
	public static final int RETURN_STAT = 1;

	public static final String SERVER = "SERVER";
	public static final String CLIENT = "CLIENT";
	public static final String DATA = "DATA";

	public static final String SHEET_NONE = "";
	public static final String SHEET = "Sheet";
	public static final String SHEET_ROW = "SheetRow";

	public static final boolean isServer(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return false;
		boolean ret = (b2c.type().equals(SERVER));
		return ret;
	}

	public static final boolean isClient(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return false;
		boolean ret = (b2c.type().equals(CLIENT));
		return ret;
	}

	public static final boolean isData(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return false;
		boolean ret = (b2c.type().equals(DATA));
		return ret;
	}

	public static final boolean isConstant(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return false;
		return b2c.constant();
	}

	public static final boolean isSheet(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return false;
		boolean ret = (b2c.sheetType().equals(SHEET));
		return ret;
	}

	public static final boolean isSheetRow(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return false;
		boolean ret = (b2c.sheetType().equals(SHEET_ROW));
		return ret;
	}

	public static final boolean isXls(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return false;
		return b2c.isXls();
	}

	public static final String getSheetName(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return "";
		String ret = (b2c.sheetName());
		return ret;
	}

	public static final String getSheetCName(Class<?> c) {
		B2Class b2c = c.getAnnotation(B2Class.class);
		if (b2c == null)
			return "";
		String ret = (b2c.sheetCName());
		return ret;
	}

	public static final boolean isServer(Method m) {
		B2Method b2f = m.getAnnotation(B2Method.class);
		if (b2f == null)
			return false;
		boolean ret = (b2f.type().equals(SERVER));
		return ret;
	}

	public static final boolean isClient(Method m) {
		B2Method b2f = m.getAnnotation(B2Method.class);
		if (b2f == null)
			return false;
		boolean ret = (b2f.type().equals(CLIENT));
		return ret;
	}

	// ///////////////////////////////////////////////////////////////
	public static String getRemark(Method m) {
		B2Method b2m = m.getAnnotation(B2Method.class);
		return b2m.remark();
	}

	public static String getOType(Method m, String val) {
		B2Method b2m = m.getAnnotation(B2Method.class);
		String[] params = b2m.params();
		int i = 0;
		for (; i < params.length; i++) {
			if (params[i].equals(val)) {
				break;
			}
		}
		Annotation[][] annotations = m.getParameterAnnotations();
		B2Param b2p = (B2Param) annotations[i][0];
		return b2p.oType();
	}

	public static boolean isOut(Method m, String val) {
		B2Method b2m = m.getAnnotation(B2Method.class);
		String[] params = b2m.params();
		int i = 0;
		for (; i < params.length; i++) {
			if (params[i].equals(val)) {
				break;
			}
		}
		Annotation[][] annotations = m.getParameterAnnotations();
		if (annotations[i].length < 1)
			return false;
		B2Param b2p = (B2Param) annotations[i][0];
		return b2p.isOut();
	}

	public static final String getFieldRemark(Field f) {
		B2Field b2f = f.getAnnotation(B2Field.class);
		if (b2f == null)
			return null;
		return b2f.remark();
	}

	public static final String getFieldType(Field f) {
		if (f == null)
			return "";
		String r = f.getClass().getSimpleName();
		if (r.equals("void")) {
			return "";
		} else if (r.equals("boolean") || r.equals("Boolean")) {
			return "return true;";
		} else if (r.equals("byte") || r.equals("Byte")) {
			return "return 0;";
		} else if (r.equals("short") || r.equals("Short")) {
			return "return 0;";
		} else if (r.equals("int") || r.equals("Integer")) {
			return "return 0;";
		} else if (r.equals("long") || r.equals("Long")) {
			return "return 0L;";
		} else if (r.equals("float") || r.equals("Float")) {
			return "return 0.0;";
		} else if (r.equals("double") || r.equals("Double")) {
			return "return 0.0;";
		} else if (r.equals("String")) {
			return "return \"\";";
		} else if (r.equals("Map") || r.equals("Hashtable")) {
			return "return new NewMap();";
		} else if (r.equals("List") || r.equals("Vector")) {
			return "return new NewList();";
		} else if (r.equals("byte[]") || r.equals("[B")) {
			return "return new byte[0];";
		}
		return r;
	}

	public static final String getReturnType(Method m) {
		if (m == null)
			return "";
		Class<?> rtype = m.getReturnType();
		String srtype = rtype.getSimpleName();
		return srtype;
	}

	public static final String getReturnVal(Method m) {
		if (m == null)
			return "";
		Class<?> rtype = m.getReturnType();
		String r = rtype.getSimpleName();
		if (r.equals("void")) {
			return "";
		} else if (r.equals("boolean") || r.equals("Boolean")) {
			return "return true;";
		} else if (r.equals("byte") || r.equals("Byte")) {
			return "return 0;";
		} else if (r.equals("short") || r.equals("Short")) {
			return "return 0;";
		} else if (r.equals("int") || r.equals("Integer")) {
			return "return 0;";
		} else if (r.equals("long") || r.equals("Long")) {
			return "return 0L;";
		} else if (r.equals("float") || r.equals("Float")) {
			return "return 0.0;";
		} else if (r.equals("double") || r.equals("Double")) {
			return "return 0.0;";
		} else if (r.equals("String")) {
			return "return \"\";";
		} else if (r.equals("Map") || r.equals("Hashtable")) {
			return "return new NewMap();";
		} else if (r.equals("List") || r.equals("Vector")) {
			return "return new NewList();";
		} else if (r.equals("byte[]") || r.equals("[B")) {
			return "return new byte[0];";
		}
		return r;
	}

	public static final String getMapType(String r) {
		if (r.equals("void")) {
			return "";
		} else if (r.equals("boolean") || r.equals("Boolean")) {
			return "getBoolean";
		} else if (r.equals("byte") || r.equals("Byte")) {
			return "getByte";
		} else if (r.equals("short") || r.equals("Short")) {
			return "getShort";
		} else if (r.equals("int") || r.equals("Integer")) {
			return "getInt";
		} else if (r.equals("long") || r.equals("Long")) {
			return "getLong";
		} else if (r.equals("float") || r.equals("Float")) {
			return "getFloat";
		} else if (r.equals("double") || r.equals("Double")) {
			return "getDouble";
		} else if (r.equals("String")) {
			return "getString";
		} else if (r.equals("Map") || r.equals("Hashtable")
				|| r.equals("HashMap") || r.equals("NewMap")) {
			return "getMap";
		} else if (r.equals("List") || r.equals("Vector")
				|| r.equals("ArrayList") || r.equals("NewList")) {
			return "getList";
		} else if (r.contains("byte[]") || r.contains("[B")) {
			return "getBytes";
		}
		return "getObject";
	}

	public static final String getCsMapType(String r) {
		if (r.equals("void")) {
			return "";
		} else if (r.equals("bool") || r.equals("Boolean")) {
			return "getBool";
		} else if (r.equals("byte") || r.equals("Byte")) {
			return "getByte";
		} else if (r.equals("short") || r.equals("Short")) {
			return "getShort";
		} else if (r.equals("int") || r.equals("Integer")) {
			return "getInt";
		} else if (r.equals("long") || r.equals("Long")) {
			return "getLong";
		} else if (r.equals("float") || r.equals("Float")) {
			return "getFloat";
		} else if (r.equals("double") || r.equals("Double")) {
			return "getDouble";
		} else if (r.equals("string") || r.equals("String")) {
			return "getString";
		} else if (r.equals("Map") || r.equals("Hashtable")) {
			return "getMap";
		} else if (r.equals("List") || r.equals("Vector")) {
			return "getList";
		} else if (r.equals("byte[]") || r.equals("[B")) {
			return "getBytes";
		}
		return "getObject";
	}

	public static final String getNethodName(Method m) {
		if (m == null)
			return "";
		String ret = m.getName();
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static final NewList<NewMap<String, String>> getParameters(Method m) {
		if (m == null)
			return new NewList<NewMap<String, String>>();
		Class<?>[] types = m.getParameterTypes();
		B2Method b2m = m.getAnnotation(B2Method.class);
		String[] params = b2m.params();
		NewList<NewMap<String, String>> ret = new NewList<NewMap<String, String>>();
		for (int i = 0; i < types.length; i++) {
			String type = types[i].getSimpleName();
			String value = params[i];
			ret.add(NewMap.create().putE(type, value));
		}

		return ret;
	}

	public static final String getRemark(Field f) {
		B2Field a = f.getAnnotation(B2Field.class);
		if (a == null)
			return "";
		String remark = a.remark();
		if (!remark.isEmpty())
			remark = "// " + remark;
		return remark;
	}

	public static final String getDef(Field f) {
		B2Field a = f.getAnnotation(B2Field.class);
		if (a == null)
			return "";
		return a.def();
	}
	
	public static final String getColumn(Field f) {
		B2Field a = f.getAnnotation(B2Field.class);
		if (a == null)
			return "";
		return a.column();
	}

	public static final String getType(Field f) {
		if (f == null)
			return "";
		Class<?> rtype = f.getType();
		String r = rtype.getSimpleName();
		if (r.equals("void")) {
			return "";
		} else if (r.equals("boolean") || r.equals("Boolean")) {
			return "boolean";
		} else if (r.equals("byte") || r.equals("Byte")) {
			return "byte";
		} else if (r.equals("short") || r.equals("Short")) {
			return "short";
		} else if (r.equals("int") || r.equals("Integer")) {
			return "int";
		} else if (r.equals("long") || r.equals("Long")) {
			return "long";
		} else if (r.equals("float") || r.equals("Float")) {
			return "float";
		} else if (r.equals("double") || r.equals("Double")) {
			return "double";
		} else if (r.equals("String")) {
			return "String";
		} else if (r.equals("Map") || r.equals("Hashtable")) {
			return "Map";
		} else if (r.equals("List") || r.equals("Vector")) {
			return "List";
		}
		return r;
	}

	public static final String getJType(Field f) {
		if (f == null)
			return "";
		Class<?> rtype = f.getType();
		String r = rtype.getSimpleName();
		if (r.equals("void")) {
			return "";
		} else if (r.contains("boolean") || r.contains("Boolean")) {
			return "Boolean";
		} else if (r.contains("byte") || r.contains("Byte")) {
			return "Byte";
		} else if (r.contains("short") || r.contains("Short")) {
			return "Short";
		} else if (r.contains("int") || r.contains("Integer")) {
			return "Integer";
		} else if (r.contains("long") || r.contains("Long")) {
			return "Long";
		} else if (r.contains("float") || r.contains("Float")) {
			return "Float";
		} else if (r.contains("double") || r.contains("Double")) {
			return "Double";
		} else if (r.contains("String")) {
			return "String";
		} else if (r.contains("Map") || r.contains("Hashtable")) {
			return "Map";
		} else if (r.contains("List") || r.contains("Vector")) {
			return "List";
		}
		return r;
	}

	public static final String getCsType(Field f) {
		if (f == null)
			return "";
		Class<?> rtype = f.getType();
		String r = rtype.getSimpleName();
		if (r.equals("void")) {
			return "";
		} else if (r.equals("boolean") || r.equals("Boolean")) {
			return "bool";
		} else if (r.equals("byte") || r.equals("Byte")) {
			return "byte";
		} else if (r.equals("short") || r.equals("Short")) {
			return "short";
		} else if (r.equals("int") || r.equals("Integer")) {
			return "int";
		} else if (r.equals("long") || r.equals("Long")) {
			return "long";
		} else if (r.equals("float") || r.equals("Float")) {
			return "float";
		} else if (r.equals("double") || r.equals("Double")) {
			return "double";
		} else if (r.equals("String")) {
			return "string";
		} else if (r.equals("Map") || r.equals("Hashtable")) {
			return "Hashtable";
		} else if (r.equals("List") || r.equals("Vector")) {
			return "ArrayList";
		}
		return r;
	}

	public static final String getListType(Field f) {
		ParameterizedType pt = (ParameterizedType) f.getGenericType();
		if (pt.getActualTypeArguments().length <= 0)
			return "";
		Type type = pt.getActualTypeArguments()[0];
		String str = type.toString();
		if (str.equals("boolean") || str.endsWith(".Boolean"))
			return "Boolean";
		else if (str.equals("byte") || str.endsWith(".Byte"))
			return "Byte";
		else if (str.equals("short") || str.endsWith(".Short"))
			return "Short";
		else if (str.equals("int") || str.endsWith(".Integer"))
			return "Integer";
		else if (str.equals("long") || str.endsWith(".Long"))
			return "Long";
		else if (str.equals("float") || str.endsWith(".Float"))
			return "Float";
		else if (str.equals("double") || str.endsWith(".Double"))
			return "Double";
		else if (str.equals("String") || str.endsWith(".String"))
			return "String";

		int pos = str.indexOf("$");
		return str.substring(pos + 1, str.length());
	}

	public static final boolean isBType(String str) {
		if (str.contains("void"))
			return true;
		else if (str.equals("boolean") || str.equals("Boolean")) {
			return true;
		} else if (str.equals("byte") || str.equals("Byte")) {
			return true;
		} else if (str.equals("short") || str.equals("Short")) {
			return true;
		} else if (str.contains("int") || str.equals("Integer")) {
			return true;
		} else if (str.equals("long") || str.equals("Long")) {
			return true;
		} else if (str.equals("float") || str.equals("Float")) {
			return true;
		} else if (str.equals("double") || str.equals("Double")) {
			return true;
		} else if (str.equals("String")) {
			return true;
		} else if (str.equals("[B") || str.equals("byte[]")) {
			return true;
		}
		return false;
	}
}
