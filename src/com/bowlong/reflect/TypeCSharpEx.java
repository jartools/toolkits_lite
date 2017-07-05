package com.bowlong.reflect;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;

public class TypeCSharpEx {
	public static String getType(Object object) {
		return object.getClass().getName();
	}

	public static String getSimpleType(Object object) {
		return object.getClass().getSimpleName();
	}

	public static String getBasicType(String t) {
		if (t.endsWith("Boolean") || t.equals("boolean"))
			return "bool";
		else if (t.endsWith("Byte") || t.equals("byte"))
			return "byte";
		else if (t.endsWith("Short") || t.equals("short"))
			return "short";
		else if (t.endsWith("Integer") || t.equals("int"))
			return "int";
		else if (t.endsWith("Long") || t.equals("long"))
			return "long";
		else if (t.endsWith("Float") || t.equals("float"))
			return "float";
		else if (t.endsWith("Double") || t.equals("double"))
			return "double";
		else if (t.endsWith("String"))
			return "string";
		return t;
	}

	public static String typeValue(String t) {
		if (t.endsWith("Boolean") || t.equals("boolean"))
			return "false";
		else if (t.endsWith("Byte") || t.equals("byte"))
			return "0";
		else if (t.endsWith("Short") || t.equals("short"))
			return "0";
		else if (t.endsWith("Integer") || t.equals("int"))
			return "0";
		else if (t.endsWith("Long") || t.equals("long"))
			return "0";
		else if (t.endsWith("Float") || t.equals("float"))
			return "0.0";
		else if (t.endsWith("Double") || t.equals("double"))
			return "0.0";
		else if (t.endsWith("BigDecimal"))
			return "0.0";
		else if (t.endsWith("String") || t.equals("string"))
			return "\"\"";
		else if (t.endsWith("Map") || t.endsWith("Hashtable"))
			return "new Hashtable()";
		else if (t.endsWith("List") || t.endsWith("Vector"))
			return "new ArrayList()";
		return t;

	}

	public static String getObjectType(String t) {
		if (t.endsWith("Boolean") || t.equals("boolean"))
			return "bool";
		else if (t.endsWith("Byte") || t.equals("byte"))
			return "byte";
		else if (t.endsWith("Short") || t.equals("short"))
			return "short";
		else if (t.endsWith("Integer") || t.equals("int"))
			return "int";
		else if (t.endsWith("Long") || t.equals("long"))
			return "Int64";
		else if (t.endsWith("Float") || t.equals("float"))
			return "flat";
		else if (t.endsWith("Double") || t.equals("double"))
			return "double";
		else if (t.endsWith("String") || t.equals("double"))
			return "string";
		else if (t.endsWith(".Map"))
			return "Hashtable";
		else if (t.endsWith(".List"))
			return "ArrayList";
		return t;
	}

	public static boolean isNull(Object object) {
		return object == null;
	}

	public static boolean isBoolean(Object object) {
		return object instanceof Boolean;
	}

	public static boolean isByte(Object object) {
		return object instanceof Byte;
	}

	public static boolean isShort(Object object) {
		return object instanceof Short;
	}

	public static boolean isInteger(Object object) {
		return object instanceof Integer;
	}

	public static boolean isLong(Object object) {
		return object instanceof Long;
	}

	public static boolean isFloat(Object object) {
		return object instanceof Float;
	}

	public static boolean isDouble(Object object) {
		return object instanceof Double;
	}

	public static boolean isDate(Object object) {
		return object instanceof Date;
	}

	public static boolean isString(Object object) {
		return object instanceof String;
	}

	public static boolean isByteArray(Object object) {
		return object instanceof byte[] || object instanceof Byte[];
	}

	public static boolean isBooleanArray(Object object) {
		return object instanceof boolean[] || object instanceof Boolean[];
	}

	public static boolean isShortArray(Object object) {
		return object instanceof short[] || object instanceof Short[];
	}

	public static boolean isIntegerArray(Object object) {
		return object instanceof int[] || object instanceof Integer[];
	}

	public static boolean isLongArray(Object object) {
		return object instanceof long[] || object instanceof Long[];
	}

	public static boolean isFloatArray(Object object) {
		return object instanceof float[] || object instanceof Float[];
	}

	public static boolean isDoubleArray(Object object) {
		return object instanceof double[] || object instanceof Double[];
	}

	public static boolean isVector(Object object) {
		return object instanceof Vector;
	}

	public static boolean isLinkedList(Object object) {
		return object instanceof LinkedList;
	}

	public static boolean isArrayList(Object object) {
		return object instanceof ArrayList;
	}

	public static boolean isList(Object object) {
		return object instanceof List;
	}

	public static boolean isHashtable(Object object) {
		return object instanceof Hashtable;
	}

	public static boolean isHashMap(Object object) {
		return object instanceof HashMap;
	}

	public static boolean isWeakHashMap(Object object) {
		return object instanceof WeakHashMap;
	}

	public static boolean isMap(Object object) {
		return object instanceof Map;
	}

	public static String getDefaultValue(String t, Object v) {
		if (t.endsWith("Boolean") || t.equals("boolean"))
			return String.valueOf(v);
		else if (t.endsWith("Byte") || t.equals("byte"))
			return String.valueOf(v);
		else if (t.endsWith("Short") || t.equals("short"))
			return String.valueOf(v);
		else if (t.endsWith("Integer") || t.equals("int"))
			return String.valueOf(v);
		else if (t.endsWith("Long") || t.equals("long"))
			return String.valueOf(v);
		else if (t.endsWith("Float") || t.equals("float"))
			return String.valueOf(v);
		else if (t.endsWith("Double") || t.equals("double"))
			return String.valueOf(v);
		else if (t.endsWith("String") || t.equals("string"))
			return String.format("\"%s\"", v);
		else if (t.endsWith("BigDecimal")) {
			return String.format("%s", v);
		}
		return String.valueOf(v);
	}

}
