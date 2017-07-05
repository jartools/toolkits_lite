package com.bowlong.tool;

import com.bowlong.lang.StrEx;
import com.bowlong.reflect.TypeCSharpEx;
import com.bowlong.reflect.TypeEx;
import com.bowlong.util.ListSingleMap;
import com.bowlong.util.SingleMap;
import com.bowlong.util.StrBuilder;

public class BeanCreate {

	public static final String createJavaFromListSingleMap(String objectName,
			ListSingleMap maps) {

		StrBuilder sb = StrEx.builder();

		sb.pn("import java.io.*;");
		sb.pn("import java.util.*;");
		sb.pn("");

		sb.pn("@SuppressWarnings({ \"unchecked\", \"rawtypes\", \"serial\" })");
		sb.pn("public class ${1} implements Serializable {", objectName);
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();
			String text = entry.getText();
			String type = TypeEx.getBasicType(obj.getClass().getName());
			String val = TypeEx.getDefaultValue(type, obj);
			sb.pn("    public ${1} ${2} = ${3}; ${4}", type, name, val,
					StrEx.comment(text));
		}

		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();

			String nameU1 = StrEx.upperN1(name);
			String type = TypeEx.getBasicType(obj.getClass().getName());
			sb.pn("");
			sb.pn("    // ${1}", name);
			sb.pn("    public ${1} get${2}() { return ${3}; }", type, nameU1,
					name);
			sb.pn("    public ${1} set${3}(${2} ${4}) { this.${4} = ${4}; return this;}",
					objectName, type, nameU1, name);
		}

		sb.pn("");
		sb.pn("    public String toString() {");
		sb.pn("        return toMap().toString();");
		sb.pn("    }");

		sb.pn("");
		sb.pn("    public Map toMap() {");
		sb.pn("        Map result = new HashMap();");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			sb.pn("        result.put($[1], this.${2});", name, name);
		}
		sb.pn("        return result;");
		sb.pn("    }");
		sb.pn("");

		sb.pn("    public ${1} fromMap(Map map) {", objectName);
		sb.pn("        if(map == null) return this;");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();
			String type = obj.getClass().getName();
			String value = TypeCSharpEx.typeValue(type);
			sb.pn("        { // ${1}", name);
			sb.pn("            Object obj = map.get($[1]);", name);
			sb.pn("            this.${1} = obj == null ? ${2} : (${3}) obj;",
					name, value, type);
			sb.pn("        }");
		}
		sb.pn("        return this;");
		sb.pn("    }");

		sb.pn("");
		sb.pn("    public static ${1} parse(Map map) {", objectName);
		sb.pn("        if(map == null) return null;");
		sb.pn("        ${1} r2 = new ${1}();", objectName);
		sb.pn("        r2.fromMap(map);");
		sb.pn("        return r2;");
		sb.pn("    }");
		
		sb.pn("");
		sb.pn("    public Map toHashCodeMap() {");
		sb.pn("        Map result = new HashMap();");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			int nameCode = name.hashCode();
			sb.pn("        result.put(${1}, this.${2});", nameCode, name);
		}
		sb.pn("        return result;");
		sb.pn("    }");
		sb.pn("");

		sb.pn("    public ${1} fromHashCodeMap(Map map) {", objectName);
		sb.pn("        if(map == null) return this;");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();
			String type = obj.getClass().getName();
			String value = TypeCSharpEx.typeValue(type);
			int nameCode = name.hashCode();
			sb.pn("        { // ${1}", name);
			sb.pn("            Object obj = map.get(${1});", nameCode);
			sb.pn("            this.${1} = obj == null ? ${2} : (${3}) obj;",
					name, value, type);
			sb.pn("        }");
		}
		sb.pn("        return this;");
		sb.pn("    }");
		
		sb.pn("");
		sb.pn("    public static ${1} parseHashCodeMap(Map map) {", objectName);
		sb.pn("        if(map == null) return null;");
		sb.pn("        ${1} r2 = new ${1}();", objectName);
		sb.pn("        r2.fromHashCodeMap(map);");
		sb.pn("        return r2;");
		sb.pn("    }");

		sb.pn("}");
		return sb.toString();
	}

	public static final String createCSharpFromListSingleMap(String objectName,
			ListSingleMap maps) {

		StrBuilder sb = StrEx.builder();

		sb.pn("using System;");
		sb.pn("using System.Collections;");
		sb.pn("");
		sb.pn("using Toolkit;");
		sb.pn("");
		sb.pn("public class ${1} {", objectName);
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();
			String text = entry.getText();
			String type = TypeCSharpEx.getBasicType(obj.getClass().getName());
			String val = TypeCSharpEx.getDefaultValue(type, obj);
			sb.pn("    protected ${1} _${2} = ${3}; ${4}", type, name, val,
					StrEx.comment(text));
		}

		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();
			String text = entry.getText();
			String nameU1 = StrEx.upperN1(name);
			String type = TypeCSharpEx.getBasicType(obj.getClass().getName());
			sb.pn("");
			sb.pn("    // ${1} ${2}", name, text);
			sb.pn("    public ${1} ${2} {", type, nameU1);
			if (type.equals("int")) {
				sb.pn("        get { return EnDe.de(_${1}); }", name);
				sb.pn("        set { _${1} = EnDe.en(value); }", name);
			} else {
				sb.pn("        get { return _${1}; }", name);
				sb.pn("        set { _${1} = value; }", name);
			}
			sb.pn("    }");
		}

		sb.pn("");
		sb.pn("    public String toString() {");
		sb.pn("        return toMap().ToString();");
		sb.pn("    }");

		sb.pn("");
		sb.pn("    public Hashtable toMap() {");
		sb.pn("        Hashtable result = new Hashtable();");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			String nameU1 = StrEx.upperN1(name);
			sb.pn("        result[$[1]] = this.${2};", name, nameU1);
		}
		sb.pn("        return result;");
		sb.pn("    }");
		
		sb.pn("");
		sb.pn("    public ${1} fromMap(Hashtable map) {", objectName);
		sb.pn("        if(map == null) return this;");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();
			String type = obj.getClass().getName();
			String cs_type = TypeCSharpEx.getObjectType(type);
			String value = TypeCSharpEx.typeValue(type);

			String nameU1 = StrEx.upperN1(name);
			sb.pn("        { // ${1}", name);
			sb.pn("            Object obj = map[$[1]];", name);
			sb.pn("            this.${1} = obj == null ? ${2} : (${3}) obj;",
					nameU1, value, cs_type);
			sb.pn("        }");
		}
		sb.pn("        return this;");
		sb.pn("    }");

		sb.pn("");
		sb.pn("    public static ${1} parse(Hashtable map) {", objectName);
		sb.pn("        if(map == null) return null;");
		sb.pn("        ${1} r2 = new ${1}();", objectName);
		sb.pn("        r2.fromMap(map);");
		sb.pn("        return r2;");
		sb.pn("    }");
		
		sb.pn("");
		sb.pn("    public Hashtable toHashCodeMap() {");
		sb.pn("        Hashtable result = new Hashtable();");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			String nameU1 = StrEx.upperN1(name);
			int nameCode = name.hashCode();
			sb.pn("        result[${1}] = this.${2};", nameCode, nameU1);
		}
		sb.pn("        return result;");
		sb.pn("    }");
		
		sb.pn("");
		sb.pn("    public ${1} fromHashCodeMap(Hashtable map) {", objectName);
		sb.pn("        if(map == null) return this;");
		for (SingleMap<Object, Object> entry : maps) {
			String name = (String) entry.getKey();
			Object obj = entry.getValue();
			String type = obj.getClass().getName();
			String cs_type = TypeCSharpEx.getObjectType(type);
			String value = TypeCSharpEx.typeValue(type);
			int nameCode = name.hashCode();
			String nameU1 = StrEx.upperN1(name);
			sb.pn("        { // ${1}", name);
			sb.pn("            Object obj = map[${1}];", nameCode);
			sb.pn("            this.${1} = obj == null ? ${2} : (${3}) obj;",
					nameU1, value, cs_type);
			sb.pn("        }");
		}
		sb.pn("        return this;");
		sb.pn("    }");

		sb.pn("");
		sb.pn("    public static ${1} parseHashCodeMap(Hashtable map) {", objectName);
		sb.pn("        if(map == null) return null;");
		sb.pn("        ${1} r2 = new ${1}();", objectName);
		sb.pn("        r2.fromHashCodeMap(map);");
		sb.pn("        return r2;");
		sb.pn("    }");
		
		sb.pn("}");
		return sb.toString();
	}

	public static void main(String[] args) {
		ListSingleMap maps = new ListSingleMap();
		maps.add("userid", 1, "用户id");
		maps.add("username", "", "用户名");
		maps.add("pwd", "1", "密码");
		maps.add("age", 0.0, "年龄 ");
		maps.add("sex", false, "性别");
		String s = createCSharpFromListSingleMap("Userinfo", maps);
		System.out.println(s);
	}

}
