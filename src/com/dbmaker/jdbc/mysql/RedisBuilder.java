package com.dbmaker.jdbc.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

import com.bowlong.basic.ExToolkit;
import com.bowlong.lang.StrEx;
import com.bowlong.pinyin.PinYin;
import com.bowlong.sql.SqlEx;
import com.bowlong.util.StrBuilder;

public class RedisBuilder extends ExToolkit {
	public static String build(Connection conn, ResultSet rs, String pkg, String appContext, boolean batch, boolean sorted) throws Exception {
		// StringBuffer sb = new StringBuffer();
		StrBuilder sb = new StrBuilder();

		ResultSetMetaData rsmd = rs.getMetaData();
		List<Map<String, Object>> columns = SqlEx.getColumns(rs);
		String catalogName = (String) columns.get(0).get("catalogName");
		String tb = (String) columns.get(0).get("tableName");
		String tbEn = PinYin.getShortPinYin(tb);
		String tbUEn = StrEx.upperN1(tbEn);
		// Map<String, List<Map<String, Object>>> indexs = SqlEx.getIndexs(conn,
		// table);
		String priKey = BeanBuilder.primaryKey(rsmd, columns);
		String priUKey = StrEx.upperN1(priKey);
		String priKeyType = JTypeMysql.getType(rsmd, priKey);
		String pkBType = JTypeMysql.getBasicType(priKeyType);
		// String columns1 = BeanBuilder.columns1(rsmd, columns);
		// String columns2 = BeanBuilder.columns2(rsmd, columns);
		// String columns3 = BeanBuilder.columns3(rsmd, columns);
		// String columns4 = BeanBuilder.columns4(rsmd, columns);
		// String columns5 = BeanBuilder.columns5(rsmd, columns);
		// String columns6 = BeanBuilder.columns6(rsmd, columns);
		// String columns7 = BeanBuilder.columns7(rsmd, columns);
		// String columns8 = BeanBuilder.columns8(rsmd, columns);
		// String columns9 = BeanBuilder.columns9(rsmd, columns);

		sb.pn("package ${1}.redis;", pkg);
		sb.pn("");
		sb.pn("import java.util.*;");
		sb.pn("import java.util.concurrent.*;");
		sb.pn("import org.apache.commons.logging.*;");
		sb.pn("import com.bowlong.lang.*;");
		sb.pn("import com.bowlong.util.*;");
		sb.pn("import com.bowlong.sql.mysql.*;");
		sb.pn("import com.bowlong.concurrent.async.*;");
		sb.pn("import com.bowlong.third.redis.*;");
		sb.pn("import ${1}.bean.*;", pkg);
		sb.pn("import ${1}.dao.*;", pkg);
		// sb.pn("import ${1}.entity.*;", pkg);
		sb.pn("import ${1};", appContext);

		sb.pn("import static ${1}.bean.${2}._key;", pkg, tbUEn);

		sb.pn("");
		sb.pn("//${1} - ${2}", catalogName, tb);
		sb.pn("@SuppressWarnings({\"rawtypes\", \"unchecked\"})");
		sb.pn("public abstract class ${1}Redis extends RedisSupport{", tbUEn);
		sb.pn("    static Log log = LogFactory.getLog(${1}Redis.class);", tbUEn);
		sb.pn("");
		sb.pn("    ////////////////////////////////");
		sb.pn("    static ${1}DAO DAO = null;", tbUEn);
		sb.pn("    public static ${1}DAO DAO(){", tbUEn);
		sb.pn("        if (DAO == null) DAO = new ${1}DAO(AppContext.ds());", tbUEn);
		sb.pn("        return DAO;");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static JedisX CACHE() {");
		sb.pn("        return AppContext.getJedisX();");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    static ExecutorService ES = null;");
		sb.pn("    public static ExecutorService ES() {");
		sb.pn("        if (ES == null) ES = newSingleThreadExecutor();");
		sb.pn("        return ES;");
		sb.pn("    }");
		sb.pn("");

		sb.pn("    ////////////////////////////////");
		sb.pn("    public static final String[] _keys(List<Integer> ids) {");
		sb.pn("        int Count = ids.size();");
		sb.pn("        String[] r2 = new String[Count];");
		sb.pn("        for (int i = 0; i < Count; i++)");
		sb.pn("            r2[i] = _key(ids.get(i));");
		sb.pn("        return r2;");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    ////////////////////////////////");
		sb.pn("    public static ${1} insert(final ${1} ${2}) throws Exception {", tbUEn, tbEn);
		sb.pn("        // insert to db");
		sb.pn("        final int ${1} = DAO().insert(${2});", priKey, tbEn);
		sb.pn("        if (${1} <= 0)", priKey);
		sb.pn("            return null;");
		sb.pn("        ${1}.set${2}(${3});", tbEn, priUKey, priKey);
		sb.pn("        // set to cache");
		sb.pn("        setc(${1});", tbEn);
		sb.pn("        return ${1};", tbEn);
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static FutureTask<${1}> insert2(final ${1} ${2}) throws Exception {", tbUEn, tbEn);
		sb.pn("        FutureTask f = async(ES(), new CallableForObject() {");
		sb.pn("            public Object exec() throws Exception {");
		sb.pn("                return insert(${1});", tbEn);
		sb.pn("            }");
		sb.pn("        });");
		sb.pn("        return f;");
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    public static ${1} get(${2} ${3}) throws Exception {", tbUEn, pkBType, priKey);
		sb.pn("        if (${1} <= 0) return null;", priKey);
		sb.pn("        ${1} r2 = getc(${2});", tbUEn, priKey);
		sb.pn("        if (r2 != null)");
		sb.pn("            return r2;");
		sb.pn("        ${1} ${2} = DAO().selectByKey(${3});", tbUEn, tbEn, priKey);
		sb.pn("        return ${1};", tbEn);
		sb.pn("    }");
		sb.pn("");

		sb.pn("    public static FutureTask<${1}> get2(final ${2} ${3}) {", tbUEn, pkBType, priKey);
		sb.pn("        FutureTask f = async(new CallableForObject() {");
		sb.pn("            public Object exec() throws Exception {");
		sb.pn("                return get(${1});", priKey);
		sb.pn("            }");
		sb.pn("        });");
		sb.pn("        return f;");
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    public static List<${1}> mget(final List<Integer> ids) throws Exception {", tbUEn);
		sb.pn("        List<${1}> r2 = mgetc(ids);", tbUEn);
		sb.pn("        if (!ListEx.isEmpty(ids)) {");
		sb.pn("            List<${1}> vals = DAO().selectIn(ids);", tbUEn);
		sb.pn("            if(ListEx.isEmpty(vals))");
		sb.pn("                return r2;");
		sb.pn("            msetc(vals);");
		sb.pn("            r2.addAll(vals);");
		sb.pn("        }");
		sb.pn("        return r2;");
		sb.pn("    }");
		sb.pn("");

		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    public static FutureTask<List<${1}>> mget2(final List<Integer> ids) throws Exception {", tbUEn);
		sb.pn("        FutureTask f = async(new CallableForList() {");
		sb.pn("            public List exec() throws Exception {");
		sb.pn("                return mget(ids);");
		sb.pn("            }");
		sb.pn("        });");
		sb.pn("        return f;");
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    static ${1} set(final String key, final ${1} ${2}) throws Exception {", tbUEn, tbEn);
		sb.pn("        if (StrEx.isEmpty(key) || ${1} == null)", tbEn);
		sb.pn("            return null;");
		sb.pn("        setc(${1});", tbEn);
		sb.pn("        DAO().updateByKey(${1});", tbEn);
		sb.pn("        return ${1};", tbEn);
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void set(final ${1} ${2}) throws Exception {", tbUEn, tbEn);
		sb.pn("        if (${1} == null)", tbEn);
		sb.pn("            return;");
		sb.pn("        String key = ${1}._key();", tbEn);
		sb.pn("        set(key, ${1});", tbEn);
		sb.pn("    }");
		sb.pn("");
		sb.pn("    static ${1} set2(final String key, final ${1} ${2}) throws Exception {", tbUEn, tbEn);
		sb.pn("        if (StrEx.isEmpty(key) || ${1} == null)", tbEn);
		sb.pn("            return null;");
		sb.pn("        async(ES(), new CallableExcept() {");
		sb.pn("            public void exec() throws Exception {");
		sb.pn("                set(key, ${1});", tbEn);
		sb.pn("            }");
		sb.pn("        });");
		sb.pn("        return ${1};", tbEn);
		sb.pn("    }");

		sb.pn("    public static void set2(final ${1} ${2}) throws Exception {", tbUEn, tbEn);
		sb.pn("        if (${1} == null)", tbEn);
		sb.pn("            return;");
		sb.pn("        String key = ${1}._key();", tbEn);
		sb.pn("        set2(key, ${1});", tbEn);
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    public static void mset(final List<${1}> vals) throws Exception {", tbUEn);
		sb.pn("        if (ListEx.isEmpty(vals))");
		sb.pn("            return;");
		sb.pn("        // set to cache");
		sb.pn("        msetc(vals);");
		sb.pn("        // set to db");
		sb.pn("        DAO().updateByKey(vals);");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void mset2(final List<${1}> vals) {", tbUEn);
		sb.pn("        async(ES(), new CallableExcept() {");
		sb.pn("            public void exec() throws Exception {");
		sb.pn("                mset(vals);");
		sb.pn("            }");
		sb.pn("        });");
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    public static void del(final ${1} ${2}) {", pkBType, priKey);
		sb.pn("        if (${1} <= 0) return;", priKey);
		sb.pn("        // delete from cache");
		sb.pn("        delc(${1});", priKey);
		sb.pn("        // delete from db");
		sb.pn("        DAO().deleteByKey(${1});", priKey);
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void del2(final ${1} ${2}) {", pkBType, priKey);
		sb.pn("        if (${1} <= 0) return;", priKey);
		sb.pn("        async(ES(), new CallableExcept() {");
		sb.pn("            public void exec() throws Exception {");
		sb.pn("                del(${1});", priKey);
		sb.pn("            };");
		sb.pn("        });");
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    public static void mdel(final List<Integer> ids) {");
		sb.pn("        if (ListEx.isEmpty(ids)) return;");
		sb.pn("        // delete from cache");
		sb.pn("        mdelc(ids);");
		sb.pn("        // delete from db");
		sb.pn("        DAO().deleteInKeys(ids);");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void mdel2(final List<Integer> ids) {");
		sb.pn("        if (ListEx.isEmpty(ids)) return;");
		sb.pn("        async(ES(), new CallableExcept() {");
		sb.pn("            public void exec() throws Exception {");
		sb.pn("                mdel(ids);");
		sb.pn("            };");
		sb.pn("        });");
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("    public static void setc(final ${1} val, final int seconds) throws Exception {", tbUEn);
		sb.pn("        if (val == null) return;");
		sb.pn("        String key = val._key();");
		sb.pn("        String text = val.toJson();");
		sb.pn("        if (seconds > 0)");
		sb.pn("            CACHE().setex(key, seconds, text);");
		sb.pn("        else");
		sb.pn("            CACHE().set(key, text);");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void setc(final ${1} val) throws Exception {", tbUEn);
		sb.pn("        if (val == null) return;");
		sb.pn("        final int seconds = 0;");
		sb.pn("        setc(val, seconds);");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static ${1} getc(final ${2} id) throws Exception {", tbUEn, pkBType);
		sb.pn("        if (id <= 0) return null;");
		sb.pn("        String key = _key(id);");
		sb.pn("        String text = CACHE().get(key);");
		sb.pn("        if(text == null) return null;");
		sb.pn("        return ${1}.createFor(text);", tbUEn);
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static ${1} getc(final ${2} id, final int seconds) throws Exception {", tbUEn, pkBType);
		sb.pn("        ${1} r2 = getc(id);", tbUEn);
		sb.pn("        if (r2 != null && seconds > 0) expire(id, seconds);");
		sb.pn("        return r2;");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static List<${1}> mgetc(final List<Integer> ids) throws Exception {", tbUEn);
		sb.pn("        List<${1}> r2 = newList();", tbUEn);
		sb.pn("        if (ListEx.isEmpty(ids))");
		sb.pn("            return r2;");
		sb.pn("        String[] keys = _keys(ids);");
		sb.pn("        List<String> texts = CACHE().mget(keys);");
		sb.pn("        for (String text : texts) {");
		sb.pn("            if(text == null) continue;");
		sb.pn("            ${1} val = ${1}.createFor(text);", tbUEn);
		sb.pn("            ids.remove(val.id);");
		sb.pn("            r2.add(val);");
		sb.pn("        }");
		sb.pn("        return r2;");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void msetc(final List<${1}> vals) throws Exception {", tbUEn);
		sb.pn("        if (ListEx.isEmpty(vals))");
		sb.pn("            return;");
		sb.pn("        int Count = vals.size();");
		sb.pn("        String[] kvs = new String[Count * 2];");
		sb.pn("        int ptr = 0;");
		sb.pn("        for (${1} val : vals) {", tbUEn);
		sb.pn("            String key = val._key();");
		sb.pn("            String text = val.toJson();");
		sb.pn("            kvs[ptr++] = key;");
		sb.pn("            kvs[ptr++] = text;");
		sb.pn("        }");
		sb.pn("        CACHE().mset(kvs);");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void delc(final ${1} ${2}) {", pkBType, priKey);
		sb.pn("        if (${1} <= 0) return;", priKey);
		sb.pn("        String key = _key(${1});", priKey);
		sb.pn("        CACHE().del(key);");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    public static void mdelc(final List<Integer> ids) {");
		sb.pn("        if (ListEx.isEmpty(ids)) return;");
		sb.pn("        String[] keys = _keys(ids);");
		sb.pn("        CACHE().del(keys);");
		sb.pn("    }");
		sb.pn("");
		sb.pn("    // //////////////////////////////");
		sb.pn("    public static final void expire(int id, int seconds) {");
		sb.pn("        String key = _key(id);");
		sb.pn("        CACHE().expire(key, seconds);");
		sb.pn("    }");
		// sb.pn(" public static final void expire2(final int id, final int seconds)
		// {");
		// sb.pn(" async(new CallableExcept() {");
		// sb.pn(" public void exec() throws Exception {");
		// sb.pn(" expire(id, seconds);");
		// sb.pn(" }");
		// sb.pn(" });");
		// sb.pn(" }");
		sb.pn("    public static final void expire(int id, Date dat2) {");
		sb.pn("        NewDate dat = new NewDate();");
		sb.pn("        int seconds = (int) (dat.difference(dat2) / 1000);");
		sb.pn("        expire(id, seconds);");
		sb.pn("    }");
		// sb.pn(" public static final void expire2(final int id, final Date dat2) {");
		// sb.pn(" async(new CallableExcept() {");
		// sb.pn(" public void exec() throws Exception {");
		// sb.pn(" expire(id, dat2);");
		// sb.pn(" }");
		// sb.pn(" });");
		// sb.pn(" }");
		sb.pn("    public static boolean exists(final ${1} id) {", pkBType);
		sb.pn("        if(id <= 0) return false;", tbUEn);
		sb.pn("        String key = _key(id);");
		sb.pn("        return CACHE().exists(key);");
		sb.pn("    }");
		sb.pn("    ////////////////////////////////");
		sb.pn("");
		sb.pn("}");

		String str = sb.toString();
		sb.close();
		return str;
	}
}
