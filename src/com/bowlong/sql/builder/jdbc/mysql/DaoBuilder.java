package com.bowlong.sql.builder.jdbc.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bowlong.Toolkit;
import com.bowlong.lang.StrEx;
import com.bowlong.pinyin.PinYin;
import com.bowlong.sql.SqlEx;
import com.bowlong.util.MapEx;

public class DaoBuilder extends Toolkit {
	public static void main(String[] args) throws Exception {
		String sql = "SELECT * FROM 用户角色 LIMIT 1";
		String host = "192.168.2.241";
		String db = "fych";
		String bpackage = "fych.db";
		try (Connection conn = SqlEx.newMysqlConnection(host, db);) {
			boolean batch = true;
			ResultSet rs = SqlEx.executeQuery(conn, sql);

			String xml = build(conn, rs, bpackage, batch);
			System.out.println(xml);
		}

	}

	public static String build(Connection conn, ResultSet rs, String pkg,
			boolean batch) throws Exception {
		StringBuffer sb = new StringBuffer();

		ResultSetMetaData rsmd = rs.getMetaData();
		List<Map<String, Object>> cols = SqlEx.getColumns(rs);
		String catalogName = (String) cols.get(0).get("catalogName");
		String tb = (String) cols.get(0).get("tableName");
		String tbEn = PinYin.getShortPinYin(tb);
		String tbUEn = StrEx.upperN1(tbEn);
		Map<String, List<Map<String, Object>>> indexs = SqlEx.getIndexs(conn,
				tb);
		String priKey = BeanBuilder.primaryKey(rsmd, cols);
		String priKeyType = JavaType.getType(rsmd, priKey);
		String pkBType = JavaType.getBasicType(priKeyType);
		String cols1 = BeanBuilder.columns1(rsmd, cols);
		// String cols2 = BeanBuilder.columns2(rsmd, columns);
		String cols3 = BeanBuilder.columns3(rsmd, cols);
		String cols4 = BeanBuilder.columns4(rsmd, cols);
		String cols5 = BeanBuilder.columns5(rsmd, cols);
		String cols6 = BeanBuilder.columns6(rsmd, cols);
		// String cols7 = BeanBuilder.columns7(rsmd, columns);
		String cols8 = BeanBuilder.columns8(rsmd, cols);
		// String cols9 = BeanBuilder.columns9(rsmd, columns);

		String createSql = SqlEx.createMysqlTable(conn, rs, tb);
		String createNoUniqueSql = SqlEx.createMysqlNoUniqueTable(conn, rs, tb);
		sn(sb, "package %s.dao;", pkg);
		sn(sb, "");
		sn(sb, "import org.apache.commons.logging.*;");
		sn(sb, "");
		sn(sb, "import java.util.*;");
		sn(sb, "import java.sql.*;");
		sn(sb, "import java.util.concurrent.*;");
		sn(sb, "import javax.sql.*;");
		sn(sb, "import com.bowlong.util.DateEx;");
		// if(batch){ // 批处理
		// sn(sb, "import java.sql.*;");
		// }
		sn(sb, "import com.bowlong.objpool.*;");
		sn(sb, "import com.bowlong.sql.mysql.*;");
		sn(sb, "import com.bowlong.text.*;");
		sn(sb, "import %s.bean.*;", pkg);
		sn(sb, "");
		sn(sb, "//%s - %s", catalogName, tb);
		sn(sb, "@SuppressWarnings({\"rawtypes\", \"unchecked\"})");
		sn(sb, "public class %sDAO extends JdbcTemplate {", tbUEn);
		sn(sb, "    static Log log = LogFactory.getLog(%sDAO.class);", tbUEn);
		sn(sb, "");
		sn(sb, "    public static final String TABLE = \"%s\";", tb);
		sn(sb, "    public static String TABLENAME = \"%s\";", tb);
		sn(sb, "");
		sn(sb, "    public static String TABLEYY() {");
		sn(sb, "        return TABLE + DateEx.nowStr(DateEx.fmt_yyyy);");
		sn(sb, "    }");
		sn(sb, "");
		sn(sb, "    public static String TABLEMM() {");
		sn(sb, "        return TABLE + DateEx.nowStrYM();");
		sn(sb, "    }");
		sn(sb, "");
		sn(sb, "    public static String TABLEDD() {");
		sn(sb, "        return TABLE + DateEx.nowStrYMD();");
		sn(sb, "    }");
		sn(sb, "");
		sn(sb, "    public static String[] carrays ={%s};", cols1);
		sn(sb, "    public static String coulmns = \"%s\";", cols3);
		sn(sb, "    public static String coulmns2 = \"%s\";", cols4);
		sn(sb, "");

		sn(sb, "    public %sDAO(Connection conn) {", tbUEn);
		sn(sb, "        super(conn);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public %sDAO(DataSource ds) {", tbUEn);
		sn(sb, "        super(ds);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public %sDAO(DataSource ds_r, DataSource ds_w) {", tbUEn);
		sn(sb, "        super(ds_r, ds_w);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public int insert(final %s %s) {", tbUEn, tbEn);
		sn(sb, "        return insert(%s, TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public int insert(final %s %s, final String TABLENAME2) {",
				tbUEn, tbEn);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try {");
		sn(sb, "            %s.reset();", tbEn);
		sn(sb,
				"            sql.append(\"INSERT INTO \").append(TABLENAME2).append(\" (%s) VALUES (%s)\");",
				cols4, cols5);
		sn(sb, "            Map map = super.insert(sql.toString(), %s);", tbEn);
		sn(sb, "            return getInt(map, \"GENERATED_KEY\");");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return 0;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public Future<Integer>  asyncInsert(final %s %s) {", tbUEn,
				tbEn);
		sn(sb, "        return asyncInsert(%s, TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public Future<Integer> asyncInsert(final %s %s, final String TABLENAME2) {",
				tbUEn, tbEn);
		sn(sb, "        try {");
		sn(sb, "            incrementAndGet();");
		sn(sb,
				"            Future<Integer> f = executor(TABLENAME2).submit(new Callable<Integer>() {");
		sn(sb, "                public Integer call() throws Exception {");
		sn(sb, "                   try {");
		sn(sb, "                       return insert(%s, TABLENAME2);", tbEn);
		sn(sb, "                   } catch (Exception e) {");
		sn(sb, "                       log.error(e2s(e));");
		sn(sb, "                       return 0;");
		sn(sb, "                   } finally {");
		sn(sb, "                       decrementAndGet();");
		sn(sb, "                   }");
		sn(sb, "                }");
		sn(sb, "            });");
		sn(sb, "            return f;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return null;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public Future<Integer> asyncInsert2(final %s %s) {", tbUEn,
				tbEn);
		sn(sb, "        return asyncInsert2(%s, TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public Future<Integer> asyncInsert2(final %s %s, final String TABLENAME2) {",
				tbUEn, tbEn);
		sn(sb, "        try {");
		sn(sb, "            incrementAndGet();");
		sn(sb,
				"            Future<Integer> f = executor(TABLENAME2).submit(new Callable<Integer>() {");
		sn(sb, "                public Integer call() throws Exception {");
		sn(sb, "                   try {");
		sn(sb, "                        return insert2(%s, TABLENAME2);", tbEn);
		sn(sb, "                   } catch (Exception e) {");
		sn(sb, "                       log.error(e2s(e));");
		sn(sb, "                       return 0;");
		sn(sb, "                   } finally {");
		sn(sb, "                       decrementAndGet();");
		sn(sb, "                   }");
		sn(sb, "                }");
		sn(sb, "            });");
		sn(sb, "            return f;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return null;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public int insert2(final %s %s) {", tbUEn, tbEn);
		sn(sb, "        return insert2(%s, TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public int insert2(final %s %s, final String TABLENAME2) {",
				tbUEn, tbEn);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb, "            %s.ustr();", tbEn);
		sn(sb,
				"            sql.append(\"INSERT INTO \").append(TABLENAME2).append(\" (%s) VALUES (%s)\");",
				cols3, cols6);
		sn(sb, "            Map map = super.insert(sql.toString(), %s);", tbEn);
		sn(sb, "            return getInt(map, \"GENERATED_KEY\");");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return 0;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		int p = 0;

		if (batch) { // 批处理
			sn(sb, "    public int[] insert(final List<%s> %ss) {", tbUEn, tbEn);
			sn(sb, "        return insert(%ss, TABLENAME);", tbEn);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public int[] insert(final List<%s> %ss, final String TABLENAME2) {",
					tbUEn, tbEn);
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try {");
			sn(sb,
					"            if(%ss == null || %ss.isEmpty()) return new int[0];",
					tbEn, tbEn);
			sn(sb,
					"            sql.append(\"INSERT INTO \").append(TABLENAME2).append(\" (%s) VALUES (%s)\");",
					cols4, cols5);
			sn(sb,
					"            return super.batchInsert(sql.toString(), %ss);",
					tbEn);
			sn(sb, "         } catch (Exception e) {");
			sn(sb, "             log.info(e2s(e));");
			sn(sb, "             return new int[0];");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "         }");
			sn(sb, "    }");
			sn(sb, "");
		}

		sn(sb, "    public int deleteByKey(final %s %s) {", pkBType, priKey);
		sn(sb, "        return deleteByKey(%s, TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public int deleteByKey(final %s %s, final String TABLENAME2) {",
				pkBType, priKey);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"DELETE FROM \").append(TABLENAME2).append(\" WHERE %s=:%s\");",
				priKey, priKey);
		sn(sb, "            Map params = newMap();");
		sn(sb, "            params.put(\"%s\", %s);", priKey, priKey);
		sn(sb, "            return super.update(sql.toString(), params);");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return 0;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");
		sn(sb, "    public Future<Integer> asyncDeleteByKey(final %s %s) {",
				pkBType, priKey);
		sn(sb, "        return asyncDeleteByKey(%s, TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public Future<Integer> asyncDeleteByKey(final %s %s, final String TABLENAME2) {",
				pkBType, priKey);
		sn(sb, "        try{");
		sn(sb, "            incrementAndGet();");
		sn(sb, "");
		sn(sb,
				"            Future<Integer> f = executor(TABLENAME2).submit(new Callable<Integer>() {");
		sn(sb, "                public Integer call() {");
		sn(sb, "                    try {");
		sn(sb, "                        return deleteByKey(%s, TABLENAME2);",
				priKey);
		sn(sb, "                    } catch (Exception e) {");
		sn(sb, "                       log.info(e2s(e));");
		sn(sb, "                       return 0;");
		sn(sb, "                    } finally {");
		sn(sb, "                        decrementAndGet();");
		sn(sb, "                    }");
		sn(sb, "                }");
		sn(sb, "            });");
		sn(sb, "            return f;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return null;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		if (batch) { // 批处理
			sn(sb, "    public int[] deleteByKey(final %s[] %ss) {", pkBType,
					priKey);
			sn(sb, "        return deleteByKey(%ss, TABLENAME);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public int[] deleteByKey(final %s[] keys, final String TABLENAME2) {",
					pkBType);
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb,
					"            if(keys == null || keys.length <= 0) return new int[0];");
			sn(sb,
					"            sql.append(\"DELETE FROM \").append(TABLENAME2).append(\" WHERE %s=:%s\");",
					priKey, priKey);
			sn(sb, "            List list = newList();");
			sn(sb, "            for (%s %s : keys) {", pkBType, priKey);
			sn(sb, "                Map params = newMap();");
			sn(sb, "                params.put(\"%s\", %s);", priKey, priKey);
			sn(sb, "                list.add(params);");
			sn(sb, "            }");
			sn(sb,
					"            return super.batchUpdate(sql.toString(), list);");
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return new int[0];");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");
		}

		if (batch) {
			sn(sb, "    public int deleteInKeys(final List<%s> keys) {",
					priKeyType);
			sn(sb, "        return deleteInKeys(keys, TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");
			sn(sb,
					"    public int deleteInKeys(final List<%s> keys, final String TABLENAME2) {",
					priKeyType);
			sn(sb, "        StringBuffer sb = StringBufPool.borrowObject();");
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb, "            if(keys == null || keys.isEmpty()) return 0;");
			sn(sb, "            int size = keys.size();");
			sn(sb, "            for (int i = 0; i < size; i ++) {");
			sn(sb, "                sb.append(keys.get(i));");
			sn(sb, "                if(i < size - 1)");
			sn(sb, "                    sb.append(\", \");");
			sn(sb, "            }");
			sn(sb, "            String str = sb.toString();");
			sn(sb,
					"            sql.append(\"DELETE FROM \").append(TABLENAME2).append(\" WHERE %s in (\").append(str).append(\" ) \");",
					priKey);
			sn(sb, "            return super.update(sql.toString());");
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return 0;");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sb);");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb, "    public int deleteInBeans(final List<%s> beans) {",
					tbUEn);
			sn(sb, "        return deleteInBeans(beans, TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public int deleteInBeans(final List<%s> beans, final String TABLENAME2) {",
					tbUEn);
			sn(sb, "        StringBuffer sb = StringBufPool.borrowObject();");
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb, "            if(beans == null || beans.isEmpty()) return 0;");
			// sn(sb, "            StringBuffer sb = new StringBuffer();");
			sn(sb, "            int size = beans.size();");
			sn(sb, "            for (int i = 0; i < size; i ++) {");
			sn(sb, "                %s %s = beans.get(i);", tbUEn, tbEn);
			sn(sb, "                %s %s = %s.%s;", pkBType, priKey, tbEn,
					priKey);
			sn(sb, "                sb.append(%s);", priKey);
			sn(sb, "                if(i < size - 1)");
			sn(sb, "                    sb.append(\", \");");
			sn(sb, "            }");
			sn(sb, "            String str = sb.toString();");
			sn(sb,
					"            sql.append(\"DELETE FROM \").append(TABLENAME2).append(\" WHERE %s in (\").append(str).append(\" ) \");",
					priKey);
			sn(sb, "            return super.update(sql.toString());");
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return 0;");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sb);");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");

		}

		sn(sb, "    public List<%s> selectAll() {", tbUEn);
		sn(sb, "        return selectAll(TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public List<%s> selectAll(final String TABLENAME2) {",
				tbUEn);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s\");",
				cols3, priKey);
		sn(sb,
				"            return super.queryForList(sql.toString(), %s.class);",
				tbUEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public List<%s> selectPKs() {", priKeyType);
		sn(sb, "        return selectPKs(TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public List<%s> selectPKs(final String TABLENAME2) {",
				priKeyType);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb, "            List<%s> result = newList();", priKeyType);
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s\");",
				priKey, priKey);
		sn(sb,
				"            List<Map> dbresult = super.queryForList(sql.toString());");
		sn(sb, "            for(Map map : dbresult){");
		if (pkBType.contains("int")) {
			sn(sb, "                result.add( getInt(map, \"%s\") );", priKey);
		} else if (pkBType.contains("long")) {
			sn(sb, "                result.add( getLong(map, \"%s\") );",
					priKey);
		}
		sn(sb, "            }");
		sn(sb, "            return result;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		String ics = BeanBuilder.indexCoulmns(cols, indexs);
		if (ics != null && ics.length() >= 1) {
			sn(sb, "    public List<Map> selectInIndex() {");
			sn(sb, "        return selectInIndex(TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");
			sn(sb,
					"    public List<Map> selectInIndex(final String TABLENAME2) {");
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb,
					"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s\");",
					ics, priKey);
			sn(sb, "            return super.queryForList(sql.toString());");
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return newList();");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");
		}

		if (batch) {
			sn(sb, "    public List<%s> selectIn(final List<%s> keys) {",
					tbUEn, priKeyType);
			sn(sb, "        return selectIn(keys, TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public List<%s> selectIn(final List<%s> keys, final String TABLENAME2) {",
					tbUEn, priKeyType);
			sn(sb, "        StringBuffer sb = StringBufPool.borrowObject();");
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb,
					"            if(keys == null || keys.isEmpty()) return newList();");
			sn(sb, "            int size = keys.size();");
			sn(sb, "            for (int i = 0; i < size; i ++) {");
			sn(sb, "                sb.append(keys.get(i));");
			sn(sb, "                if(i < size - 1)");
			sn(sb, "                    sb.append(\", \");");
			sn(sb, "            }");
			sn(sb, "            String str = sb.toString();");
			sn(sb,
					"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s in (\").append(str).append(\" ) ORDER BY %s\");",
					cols3, priKey, priKey);
			sn(sb,
					"            return super.queryForList(sql.toString(), %s.class);",
					tbUEn);
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return newList();");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sb);");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb, "    public List<%s> selectIn2(final List<%s> keys) {",
					tbUEn, priKeyType);
			sn(sb, "        return selectIn2(keys, TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public List<%s> selectIn2(final List<%s> keys, final String TABLENAME2) {",
					tbUEn, priKeyType);
			sn(sb, "        StringBuffer sb = StringBufPool.borrowObject();");
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb,
					"            if(keys == null || keys.isEmpty()) return newList();");
			sn(sb, "            int size = keys.size();");
			sn(sb, "            for (int i = 0; i < size; i ++) {");
			sn(sb, "                sb.append(keys.get(i));");
			sn(sb, "                if(i < size - 1)");
			sn(sb, "                    sb.append(\", \");");
			sn(sb, "            }");
			sn(sb, "            String str = sb.toString();");
			sn(sb,
					"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s in ( :str ) ORDER BY %s\");",
					cols3, priKey, priKey);
			sn(sb, "            Map params = newMap();");
			sn(sb, "            params.put(\"str\", str);");
			sn(sb,
					"            return super.queryForList(sql.toString(), params, %s.class);",
					tbUEn);
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return newList();");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sb);");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");

		}

		if (batch) {
			sn(sb, "    public List<%s> selectInPKs(final List<%s> keys) {",
					priKeyType, priKeyType);
			sn(sb, "        return selectInPKs(keys, TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public List<%s> selectInPKs(final List<%s> keys, final String TABLENAME2) {",
					priKeyType, priKeyType);
			sn(sb, "        StringBuffer sb = StringBufPool.borrowObject();");
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb,
					"            if(keys == null || keys.isEmpty()) return newList();");
			sn(sb, "            List<%s> result = newList();", priKeyType);
			// sn(sb, "            StringBuffer sb = new StringBuffer();");
			sn(sb, "            int size = keys.size();");
			sn(sb, "            for (int i = 0; i < size; i ++) {");
			sn(sb, "                sb.append(keys.get(i));");
			sn(sb, "                if(i < size - 1)");
			sn(sb, "                    sb.append(\", \");");
			sn(sb, "            }");
			sn(sb, "            String str = sb.toString();");
			sn(sb,
					"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s in (\").append(str).append(\" ) ORDER BY %s\");",
					priKey, priKey, priKey);
			sn(sb,
					"            List<Map> dbresult = super.queryForList(sql.toString());");
			sn(sb, "            for(Map map : dbresult){");
			if (pkBType.contains("int")) {
				sn(sb, "                result.add( getInt(map, \"%s\") );",
						priKey);
			} else if (pkBType.contains("long")) {
				sn(sb, "                result.add( getLong(map, \"%s\") );",
						priKey);
			}
			sn(sb, "            }");
			sn(sb, "            return result;");
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return newList();");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sb);");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");
		}

		sn(sb, "    public List<%s> selectLast(final int num) {", tbUEn);
		sn(sb, "        return selectLast(num, TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectLast(final int num, final String TABLENAME2) {",
				tbUEn);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s DESC LIMIT \").append(num).append(\"\");",
				cols3, priKey);
		sn(sb,
				"            return super.queryForList(sql.toString(), %s.class);",
				tbUEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public List<%s> selectLastPKs(final int num) {", priKeyType);
		sn(sb, "        return selectLastPKs(num, TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectLastPKs(final int num, final String TABLENAME2) {",
				priKeyType);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb, "            List<%s> result = newList();", priKeyType);
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s DESC LIMIT \").append(num).append(\"\");",
				priKey, priKey);
		sn(sb,
				"            List<Map> dbresult = super.queryForList(sql.toString());");
		sn(sb, "            for(Map map : dbresult){");
		if (pkBType.contains("int")) {
			sn(sb, "                result.add( getInt(map, \"%s\") );", priKey);
		} else if (pkBType.contains("long")) {
			sn(sb, "                result.add( getLong(map, \"%s\") );",
					priKey);
		}
		sn(sb, "            }");
		sn(sb, "            return result;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public %s last() {", tbUEn);
		sn(sb, "        return last(TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public %s last(final String TABLENAME2) {", tbUEn);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s DESC LIMIT 1\");",
				cols3, priKey);
		sn(sb,
				"            return super.queryForObject(sql.toString(), %s.class);",
				tbUEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            // log.info(e2s(e));");
		sn(sb, "            return null;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectGtKeyNum(final %s %s, final int _num) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        return selectGtKeyNum(%s, _num, TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectGtKeyNum(final %s %s, final int _num, final String TABLENAME2) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s > :%s ORDER BY %s LIMIT \").append(_num).append(\"\");",
				cols3, priKey, priKey, priKey);
		sn(sb, "            Map params = newMap();");
		sn(sb, "            params.put(\"%s\", %s);", priKey, priKey);
		sn(sb,
				"            return super.queryForList(sql.toString(), params, %s.class);",
				tbUEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public List<%s> selectGtKey(final %s %s) {", tbUEn,
				pkBType, priKey);
		sn(sb, "        return selectGtKey(%s, TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectGtKey(final %s %s, final String TABLENAME2) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s > :%s ORDER BY %s\");",
				cols3, priKey, priKey, priKey);
		sn(sb, "            Map params = newMap();");
		sn(sb, "            params.put(\"%s\", %s);", priKey, priKey);
		sn(sb,
				"            return super.queryForList(sql.toString(), params, %s.class);",
				tbUEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public List<%s> selectGtKeyPKs(final %s %s) {", priKeyType,
				pkBType, priKey);
		sn(sb, "        return selectGtKeyPKs(%s, TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectGtKeyPKs(final %s %s, final String TABLENAME2) {",
				priKeyType, pkBType, priKey);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb, "            List<%s> result = newList();", priKeyType);
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s > :%s ORDER BY %s\");",
				priKey, priKey, priKey, priKey);
		sn(sb, "            Map params = newMap();");
		sn(sb, "            params.put(\"%s\", %s);", priKey, priKey);
		sn(sb,
				"            List<Map> dbresult = super.queryForList(sql.toString(), params);");
		sn(sb, "            for(Map map : dbresult){");
		if (pkBType.contains("int")) {
			sn(sb, "                result.add( getInt(map, \"%s\") );", priKey);
		} else if (pkBType.contains("long")) {
			sn(sb, "                result.add( getLong(map, \"%s\") );",
					priKey);
		}
		sn(sb, "            }");
		sn(sb, "            return result;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public %s selectByKey(final %s %s) {", tbUEn, pkBType,
				priKey);
		sn(sb, "        return selectByKey(%s, TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public %s selectByKey(final %s %s, final String TABLENAME2) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s = :%s\");",
				cols3, priKey, priKey);
		sn(sb, "            Map params = newMap();");
		sn(sb, "            params.put(\"%s\", %s);", priKey, priKey);
		sn(sb,
				"            return super.queryForObject(sql.toString(), params, %s.class);",
				tbUEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            // log.info(e2s(e));");
		sn(sb, "            return null;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public %s maxId() {", pkBType);
		sn(sb, "        return maxId(TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public %s maxId(final String TABLENAME2) {", pkBType);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT MAX(%s) FROM \").append(TABLENAME2);",
				priKey);
		sn(sb, "            return super.queryForInt(sql.toString());");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            // log.info(e2s(e));");
		sn(sb, "            return 0;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		Iterator<String> it = indexs.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			List<Map<String, Object>> idx = indexs.get(key);
			int idx_size = idx.size();
			if (idx_size == 1) { // 单索引
				Map<String, Object> index = idx.get(0);
				// String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
				String COLUMN_NAME = MapEx.get(index, "COLUMN_NAME");
				String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
				String COLUMN_NAME_EN = PinYin.getShortPinYin(COLUMN_NAME);
				String COLUMN_NAME_UEN = StrEx.upperN1(COLUMN_NAME_EN);
				String COLUMN_NAME_TYPE = JavaType.getType(rsmd, COLUMN_NAME);
				// if(INDEX_NAME.equals("PRIMARY"))
				// continue;
				if (NON_UNIQUE.equals("false")) {
					sn(sb, "    public %s selectBy%s(final %s %s) {", tbUEn,
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb, "        return selectBy%s(%s, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public %s selectBy%s(final %s %s, final String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s = :%s\");",
							cols3, COLUMN_NAME, COLUMN_NAME);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", COLUMN_NAME,
							COLUMN_NAME_EN);
					sn(sb,
							"            return super.queryForObject(sql.toString(), params, %s.class);",
							tbUEn);
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            // log.info(e2s(e));");
					sn(sb, "            return null;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

				} else {
					sn(sb, "    public int countBy%s(final %s %s) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb, "        return countBy%s(%s, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int countBy%s(final %s %s, final String TABLENAME2) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT COUNT(*) FROM \").append(TABLENAME2).append(\" WHERE %s = :%s \");",
							COLUMN_NAME, COLUMN_NAME);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", COLUMN_NAME,
							COLUMN_NAME_EN);
					sn(sb,
							"            return super.queryForInt(sql.toString(), params);");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return 0;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public List<%s> selectBy%s(final %s %s) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return selectBy%s(%s, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectBy%s(final %s %s, final String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s = :%s ORDER BY %s \");",
							cols3, COLUMN_NAME, COLUMN_NAME, priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", COLUMN_NAME,
							COLUMN_NAME_EN);
					sn(sb,
							"            return super.queryForList(sql.toString(), params, %s.class);",
							tbUEn);
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public List<%s> selectBy%sPKs(final %s %s) {",
							priKeyType, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return selectBy%sPKs(%s, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectBy%sPKs(final %s %s, final String TABLENAME2) {",
							priKeyType, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb, "            List<%s> result = newList();",
							priKeyType);
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s = :%s ORDER BY %s \");",
							priKey, COLUMN_NAME, COLUMN_NAME, priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", COLUMN_NAME,
							COLUMN_NAME_EN);
					sn(sb,
							"            List<Map> dbresult = super.queryForList(sql.toString(), params);");
					sn(sb, "            for(Map map : dbresult){");
					sn(sb, "                result.add(getInt(map, \"%s\") );",
							priKey);
					sn(sb, "            }");
					sn(sb, "            return result;");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%s(final %s %s, final int begin, final int num) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        return selectPageBy%s(%s, begin, num, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%s(final %s %s, final int begin, final int num, final String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s = :%s ORDER BY %s LIMIT \").append(begin).append(\", \").append(num).append(\"\");",
							cols3, COLUMN_NAME, COLUMN_NAME, priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", COLUMN_NAME,
							COLUMN_NAME_EN);
					sn(sb,
							"            return super.queryForList(sql.toString(), params, %s.class);",
							tbUEn);
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%sPKs(final %s %s, final int begin, final int num) {",
							priKeyType, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        return selectPageBy%sPKs(%s, begin, num, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%sPKs(final %s %s, final int begin, final int num, final String TABLENAME2) {",
							priKeyType, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb, "            List<%s> result = newList();",
							priKeyType);
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s = :%s ORDER BY %s LIMIT \").append(begin).append(\", \").append(num).append(\"\");",
							priKey, COLUMN_NAME, COLUMN_NAME, priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", COLUMN_NAME,
							COLUMN_NAME_EN);
					sn(sb,
							"            List<Map> dbresult = super.queryForList(sql.toString(), params);");
					sn(sb, "            for(Map map : dbresult){");
					if (pkBType.contains("int")) {
						sn(sb,
								"                result.add( getInt(map, \"%s\") );",
								priKey);
					} else if (pkBType.contains("long")) {
						sn(sb,
								"                result.add( getLong(map, \"%s\") );",
								priKey);
					}
					sn(sb, "            }");
					sn(sb, "            return result;");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");
				}

				// 如果类型是字符串则参与LIKE查询
				if (COLUMN_NAME_TYPE.equals("String")) {
					sn(sb, "    public int countLike%s(final %s %s) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb, "        return countLike%s(%s, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int countLike%s(final %s %s, final String TABLENAME2) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT COUNT(*) FROM \").append(TABLENAME2).append(\" WHERE %s LIKE '%%\").append("
									+ COLUMN_NAME_EN + ").append(\"%%' \");",
							COLUMN_NAME);
					sn(sb,
							"            return super.queryForInt(sql.toString());");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return 0;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public List<%s> selectLike%s(final %s %s) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return selectLike%s(%s, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectLike%s(final %s %s, final String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s LIKE '%%\").append("
									+ COLUMN_NAME_EN
									+ ").append(\"%%' ORDER BY %s \");", cols3,
							COLUMN_NAME, priKey);
					sn(sb,
							"            return super.queryForList(sql.toString(), %s.class);",
							tbUEn);
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectLike%sPKs(final %s %s) {",
							priKeyType, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return selectLike%sPKs(%s, TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectLike%sPKs(final %s %s, final String TABLENAME2) {",
							priKeyType, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb, "            List<%s> result = newList();",
							priKeyType);
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s LIKE '%%\").append("
									+ COLUMN_NAME_EN
									+ ").append(\"%%' ORDER BY %s \");",
							priKey, COLUMN_NAME, priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb,
							"            List<Map> dbresult = super.queryForList(sql.toString(), params);");
					sn(sb, "            for(Map map : dbresult){");
					if (pkBType.contains("int")) {
						sn(sb,
								"                result.add( getInt(map, \"%s\") );",
								priKey);
					} else if (pkBType.contains("long")) {
						sn(sb,
								"                result.add( getLong(map, \"%s\") );",
								priKey);
					}
					sn(sb, "            }");
					sn(sb, "            return result;");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

				}
				// /////////////////////////////
			} else { // 多键索引
				Map<String, Object> index = idx.get(0);
				String index1 = BeanBuilder.index1(rsmd, idx);
				String index2 = BeanBuilder.index2(rsmd, idx);
				String index3 = BeanBuilder.index3(rsmd, idx);
				String index4 = BeanBuilder.index4(rsmd, idx);
				String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
				if (NON_UNIQUE.equals("false")) { // 唯一数据
					sn(sb, "    public %s selectBy%s(final %s) {", tbUEn,
							index1, index2);
					sn(sb, "        return selectBy%s(%s, TABLENAME);", index1,
							index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public %s selectBy%s(final %s, final String TABLENAME2) {",
							tbUEn, index1, index2);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s\");",
							cols3, index4);
					sn(sb, "            Map params = newMap();");
					for (Map<String, Object> m : idx) {
						// String INDEX_NAME = MapEx.get(m, "INDEX_NAME");
						String COLUMN_NAME = MapEx.get(m, "COLUMN_NAME");
						String COLUMN_NAME_EN = PinYin
								.getShortPinYin(COLUMN_NAME);
						// String COLUMN_NAME_UEN =
						// StrEx.upperN1(COLUMN_NAME_EN);
						// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
						// COLUMN_NAME);
						sn(sb, "            params.put(\"%s\", %s);",
								COLUMN_NAME, COLUMN_NAME_EN);
					}
					sn(sb,
							"            return super.queryForObject(sql.toString(), params, %s.class);",
							tbUEn);
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            // log.info(e2s(e));");
					sn(sb, "            return null;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");
				} else { // 非唯一数据
					sn(sb, "    public int countBy%s(final %s) {", index1,
							index2);
					sn(sb, "        return  countBy%s(%s, TABLENAME);", index1,
							index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int countBy%s(final %s, final String TABLENAME2) {",
							index1, index2);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT COUNT(*) FROM \").append(TABLENAME2).append(\" WHERE %s \");",
							index4);
					sn(sb, "            Map params = newMap();");
					for (Map<String, Object> m : idx) {
						// String INDEX_NAME = MapEx.get(m, "INDEX_NAME");
						String COLUMN_NAME = MapEx.get(m, "COLUMN_NAME");
						String COLUMN_NAME_EN = PinYin
								.getShortPinYin(COLUMN_NAME);
						// String COLUMN_NAME_UEN =
						// StrEx.upperN1(COLUMN_NAME_EN);
						// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
						// COLUMN_NAME);
						sn(sb, "            params.put(\"%s\", %s);",
								COLUMN_NAME, COLUMN_NAME_EN);
					}
					sn(sb,
							"            return super.queryForInt(sql.toString(), params);");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return 0;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public List<%s> selectBy%s(final %s) {", tbUEn,
							index1, index2);
					sn(sb, "        return selectBy%s(%s, TABLENAME);", index1,
							index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectBy%s(final %s, final String TABLENAME2) {",
							tbUEn, index1, index2);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s ORDER BY %s \");",
							cols3, index4, priKey);
					sn(sb, "            Map params = newMap();");
					for (Map<String, Object> m : idx) {
						// String INDEX_NAME = MapEx.get(m, "INDEX_NAME");
						String COLUMN_NAME = MapEx.get(m, "COLUMN_NAME");
						String COLUMN_NAME_EN = PinYin
								.getShortPinYin(COLUMN_NAME);
						// String COLUMN_NAME_UEN =
						// StrEx.upperN1(COLUMN_NAME_EN);
						// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
						// COLUMN_NAME);
						sn(sb, "            params.put(\"%s\", %s);",
								COLUMN_NAME, COLUMN_NAME_EN);
					}
					sn(sb,
							"            return super.queryForList(sql.toString(), params, %s.class);",
							tbUEn);
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public List<%s> selectBy%sPKs(final %s) {",
							priKeyType, index1, index2);
					sn(sb, "        return selectBy%sPKs(%s, TABLENAME);",
							index1, index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectBy%sPKs(final %s, final String TABLENAME2) {",
							priKeyType, index1, index2);
					sn(sb, "        List<%s> result = newList();", priKeyType);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s ORDER BY %s \");",
							priKey, index4, priKey);
					sn(sb, "            Map params = newMap();");
					for (Map<String, Object> m : idx) {
						// String INDEX_NAME = MapEx.get(m, "INDEX_NAME");
						String COLUMN_NAME = MapEx.get(m, "COLUMN_NAME");
						String COLUMN_NAME_EN = PinYin
								.getShortPinYin(COLUMN_NAME);
						// String COLUMN_NAME_UEN =
						// StrEx.upperN1(COLUMN_NAME_EN);
						// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
						// COLUMN_NAME);
						sn(sb, "            params.put(\"%s\", %s);",
								COLUMN_NAME, COLUMN_NAME_EN);
					}
					sn(sb,
							"            List<Map> dbresult = super.queryForList(sql.toString(), params);");
					sn(sb, "            for(Map map : dbresult){");
					if (pkBType.contains("int")) {
						sn(sb,
								"                result.add( getInt(map, \"%s\") );",
								priKey);
					} else if (pkBType.contains("long")) {
						sn(sb,
								"                result.add( getLong(map, \"%s\") );",
								priKey);
					}
					sn(sb, "            }");
					sn(sb, "            return result;");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%s(final %s, final int begin, final int num) {",
							tbUEn, index1, index2);
					sn(sb,
							"        return selectPageBy%s(%s, begin, num, TABLENAME);",
							index1, index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%s(final %s, final int begin, final int num, final String TABLENAME2) {",
							tbUEn, index1, index2);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s ORDER BY %s LIMIT \").append(begin).append(\", \").append(num).append(\"\");",
							cols3, index4, priKey);
					sn(sb, "            Map params = newMap();");
					for (Map<String, Object> m : idx) {
						// String INDEX_NAME = MapEx.get(m, "INDEX_NAME");
						String COLUMN_NAME = MapEx.get(m, "COLUMN_NAME");
						String COLUMN_NAME_EN = PinYin
								.getShortPinYin(COLUMN_NAME);
						// String COLUMN_NAME_UEN =
						// StrEx.upperN1(COLUMN_NAME_EN);
						// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
						// COLUMN_NAME);
						sn(sb, "            params.put(\"%s\", %s);",
								COLUMN_NAME, COLUMN_NAME_EN);
					}
					sn(sb,
							"            return super.queryForList(sql.toString(), params, %s.class);",
							tbUEn);
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%sPKs(final %s, final int begin, final int num) {",
							priKeyType, index1, index2);
					sn(sb,
							"        return selectPageBy%sPKs(%s, begin, num, TABLENAME);",
							index1, index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public List<%s> selectPageBy%sPKs(final %s, final int begin, final int num, final String TABLENAME2) {",
							priKeyType, index1, index2);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb, "            List<%s> result = newList();",
							priKeyType);
					sn(sb,
							"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" WHERE %s ORDER BY %s LIMIT \").append(begin).append(\", \").append(num).append(\"\");",
							priKey, index4, priKey);
					sn(sb, "            Map params = newMap();");
					for (Map<String, Object> m : idx) {
						// String INDEX_NAME = MapEx.get(m, "INDEX_NAME");
						String COLUMN_NAME = MapEx.get(m, "COLUMN_NAME");
						String COLUMN_NAME_EN = PinYin
								.getShortPinYin(COLUMN_NAME);
						// String COLUMN_NAME_UEN =
						// StrEx.upperN1(COLUMN_NAME_EN);
						// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
						// COLUMN_NAME);
						sn(sb, "            params.put(\"%s\", %s);",
								COLUMN_NAME, COLUMN_NAME_EN);
					}
					sn(sb,
							"            List<Map> dbresult = super.queryForList(sql.toString(), params);");
					sn(sb, "            for(Map map : dbresult){");
					if (pkBType.contains("int")) {
						sn(sb,
								"                result.add( getInt(map, \"%s\") );",
								priKey);
					} else if (pkBType.contains("long")) {
						sn(sb,
								"                result.add( getLong(map, \"%s\") );",
								priKey);
					}
					sn(sb, "            }");
					sn(sb, "            return result;");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return newList();");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");
				}
			}

		}

		sn(sb, "    public int count() {");
		sn(sb, "        return count(TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public int count(final String TABLENAME2) {");
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT COUNT(*) FROM \").append(TABLENAME2).append(\"\");");
		sn(sb, "            return super.queryForInt(sql.toString());");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return 0;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectByPage(final int begin, final int num) {",
				tbUEn);
		sn(sb, "        return selectByPage(begin, num, TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectByPage(final int begin, final int num, final String TABLENAME2) {",
				tbUEn);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s LIMIT \").append(begin).append(\", \").append(num).append(\"\");",
				cols3, priKey);
		sn(sb,
				"            return super.queryForList(sql.toString(), %s.class);",
				tbUEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectByPagePKs(final int begin, final int num) {",
				priKeyType);
		sn(sb, "        return selectByPagePKs(begin, num, TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public List<%s> selectByPagePKs(final int begin, final int num, final String TABLENAME2) {",
				priKeyType);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb, "            List<%s> result = newList();", priKeyType);
		sn(sb,
				"            sql.append(\"SELECT %s FROM \").append(TABLENAME2).append(\" ORDER BY %s LIMIT \").append(begin).append(\", \").append(num).append(\"\");",
				priKey, priKey);
		sn(sb, "            Map params = new Hashtable();");
		sn(sb,
				"            List<Map> dbresult = super.queryForList(sql.toString(), params);");
		sn(sb, "            for(Map map : dbresult){");
		if (pkBType.contains("int")) {
			sn(sb, "                result.add( getInt(map, \"%s\") );", priKey);
		} else if (pkBType.contains("long")) {
			sn(sb, "                result.add( getLong(map, \"%s\") );",
					priKey);
		}
		sn(sb, "            }");
		sn(sb, "            return result;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return newList();");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public int updateByKey(final %s %s) {", tbUEn, tbEn);
		sn(sb, "        return updateByKey(%s, TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public int updateByKey(final %s %s, final String TABLENAME2) {",
				tbUEn, tbEn);
		sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb, "        try{");
		sn(sb, "            String _ustr = %s.ustr();", tbEn);
		sn(sb, "            if( _ustr.length() <= 0 )");
		sn(sb, "                return -1;");
		sn(sb,
				"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET \").append(_ustr).append(\" WHERE %s=:%s\");",
				priKey, priKey);
		sn(sb, "            return super.update(sql.toString(), %s);", tbEn);
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return 0;");
		sn(sb, "        } finally {");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public Future<Integer> asyncUpdate(final %s %s) {", tbUEn,
				tbEn);
		sn(sb, "        return asyncUpdate(%s, TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public Future<Integer> asyncUpdate(final %s %s, final String TABLENAME2) {",
				tbUEn, tbEn);
		sn(sb, "        try {");
		sn(sb, "");
		sn(sb, "            String _ustr = %s.ustr();", tbEn);
		sn(sb, "            if( _ustr.length() <= 0 ) return null;");
		sn(sb, "");
		sn(sb, "            StringBuffer sql = StringBufPool.borrowObject();");
		sn(sb,
				"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET \").append(_ustr).append(\" WHERE %s=:%s\");",
				priKey, priKey);
		sn(sb, "            final String szSql = sql.toString();");
		sn(sb, "            StringBufPool.returnObject(sql);");
		sn(sb, "            incrementAndGet();");
		sn(sb,
				"            Future<Integer> f = executor(TABLENAME2).submit(new Callable<Integer>() {");
		sn(sb, "                public Integer call() {");
		sn(sb, "                    try {");
		sn(sb, "                        return update(szSql, %s);", tbEn);
		sn(sb, "                    } catch (Exception e) {");
		sn(sb, "                        log.error(e2s(e));");
		sn(sb, "                        return 0;");
		sn(sb, "                    } finally {");
		sn(sb, "                        decrementAndGet();");
		sn(sb, "                    }");
		sn(sb, "                }");
		sn(sb, "            });");
		sn(sb, "            return f;");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "            return null;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		for (Map<String, Object> m : cols) {
			String column = MapEx.get(m, "columnName");
			// String columnU = StrEx.upperN1(column);
			String columnEn = PinYin.getShortPinYin(column);
			String columnUEn = StrEx.upperN1(columnEn);
			String javaType = JavaType.getType(rsmd, column);
			String bType = JavaType.getBasicType(javaType);
			if (!column.equals(priKey)) {
				if (bType.contains("short") || bType.contains("int")
						|| bType.contains("long") || bType.contains("float")
						|| bType.contains("double")) {
					sn(sb,
							"    public int update%sByKey(final %s %s, final %s %s){",
							columnUEn, bType, columnEn, pkBType, priKey);
					sn(sb, "        return update%sByKey(%s, %s, TABLENAME);",
							columnUEn, columnEn, priKey);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int update%sByKey(final %s %s, final %s %s, final String TABLENAME2) {",
							columnUEn, bType, columnEn, pkBType, priKey);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET %s=%s+:%s WHERE %s=:%s\");",
							column, column, column, priKey, priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", priKey,
							priKey);
					sn(sb, "            params.put(\"%s\", %s);", column,
							columnEn);
					sn(sb,
							"            return super.update(sql.toString(), params);");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return 0;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int update%sWithMinByKey(final %s %s, final %s %s, final %s _min){",
							columnUEn, pkBType, priKey, bType, columnEn, bType);
					sn(sb,
							"        return update%sWithMinByKey(%s, %s, _min, TABLENAME);",
							columnUEn, priKey, columnEn);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int update%sWithMinByKey(final %s %s, final %s %s, final %s _min, final String TABLENAME2) {",
							columnUEn, pkBType, priKey, bType, columnEn, bType);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET %s = (select case when %s+:%s<=:_min then :_min else %s+:%s end) WHERE %s=:%s\");",
							column, column, column, column, column, priKey,
							priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", priKey,
							priKey);
					sn(sb, "            params.put(\"_min\", _min);");
					sn(sb, "            params.put(\"%s\", %s);", column,
							columnEn);
					sn(sb,
							"            return super.update(sql.toString(), params);");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return 0;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					if (batch) {
						sn(sb,
								"    public int update%sWithMinInKeys(final List<%s> keys, final %s %s, final %s _min){",
								columnUEn, priKeyType, bType, columnEn, bType);
						sn(sb,
								"        return update%sWithMinInKeys(keys, %s, _min, TABLENAME);",
								columnUEn, columnEn);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public int update%sWithMinInKeys(final List<%s> keys, final %s %s, final %s _min, final String TABLENAME2) {",
								columnUEn, priKeyType, bType, columnEn, bType);
						sn(sb,
								"        StringBuffer sb = StringBufPool.borrowObject();");
						sn(sb,
								"        StringBuffer sql = StringBufPool.borrowObject();");
						sn(sb, "        try{");
						sn(sb,
								"            if(keys == null || keys.isEmpty()) return 0;");
						// sn(sb,
						// "            StringBuffer sb = new StringBuffer();");
						sn(sb, "            int size = keys.size();");
						sn(sb, "            for (int i = 0; i < size; i ++) {");
						sn(sb, "                sb.append(keys.get(i));");
						sn(sb, "                if(i < size - 1)");
						sn(sb, "                    sb.append(\", \");");
						sn(sb, "            }");
						sn(sb, "            String str = sb.toString();");
						sn(sb,
								"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET %s = (select case when %s+:%s<=:_min then :_min else %s+:%s end) WHERE %s in (\").append(str).append(\")\");",
								column, column, column, column, column, priKey);
						sn(sb, "            Map params = newMap();");
						sn(sb, "            params.put(\"_min\", _min);");
						sn(sb, "            params.put(\"%s\", %s);", column,
								columnEn);
						sn(sb,
								"            return super.update(sql.toString(), params);");
						sn(sb, "        } catch(Exception e) {");
						sn(sb, "            log.info(e2s(e));");
						sn(sb, "            return 0;");
						sn(sb, "        } finally {");
						sn(sb, "            StringBufPool.returnObject(sb);");
						sn(sb, "            StringBufPool.returnObject(sql);");
						sn(sb, "        }");
						sn(sb, "    }");
						sn(sb, "");
					}

					sn(sb,
							"    public int update%sWithMaxByKey(final %s %s, final %s %s, final %s _max){",
							columnUEn, pkBType, priKey, bType, columnEn, bType);
					sn(sb,
							"        return update%sWithMaxByKey(%s, %s, _max, TABLENAME);",
							columnUEn, priKey, columnEn);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int update%sWithMaxByKey(final %s %s, final %s %s, final %s _max, final String TABLENAME2) {",
							columnUEn, pkBType, priKey, bType, columnEn, bType);
					sn(sb,
							"        StringBuffer sql = StringBufPool.borrowObject();");
					sn(sb, "        try{");
					sn(sb,
							"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET %s = (select case when %s+:%s>=:_max then :_max else %s+:%s end) WHERE %s=:%s\");",
							column, column, column, column, column, priKey,
							priKey);
					sn(sb, "            Map params = newMap();");
					sn(sb, "            params.put(\"%s\", %s);", priKey,
							priKey);
					sn(sb, "            params.put(\"_max\", _max);");
					sn(sb, "            params.put(\"%s\", %s);", column,
							columnEn);
					sn(sb,
							"            return super.update(sql.toString(), params);");
					sn(sb, "        } catch(Exception e) {");
					sn(sb, "            log.info(e2s(e));");
					sn(sb, "            return 0;");
					sn(sb, "        } finally {");
					sn(sb, "            StringBufPool.returnObject(sql);");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					if (batch) {
						sn(sb,
								"    public int update%sWithMaxInKeys(final List<%s> keys, final %s %s, final %s _max){",
								columnUEn, priKeyType, bType, columnEn, bType);
						sn(sb,
								"        return update%sWithMaxInKeys(keys, %s, _max, TABLENAME);",
								columnUEn, columnEn);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public int update%sWithMaxInKeys(final List<%s> keys, final %s %s, final %s _max, final String TABLENAME2) {",
								columnUEn, priKeyType, bType, columnEn, bType);
						sn(sb,
								"        StringBuffer sb = StringBufPool.borrowObject();");
						sn(sb,
								"        StringBuffer sql = StringBufPool.borrowObject();");
						sn(sb, "        try{");
						sn(sb,
								"            if(keys == null || keys.isEmpty()) return 0;");
						// sn(sb,
						// "            StringBuffer sb = new StringBuffer();");
						sn(sb, "            int size = keys.size();");
						sn(sb, "            for (int i = 0; i < size; i ++) {");
						sn(sb, "                sb.append(keys.get(i));");
						sn(sb, "                if(i < size - 1)");
						sn(sb, "                    sb.append(\", \");");
						sn(sb, "            }");
						sn(sb, "            String str = sb.toString();");
						sn(sb,
								"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET %s = (select case when %s+:%s>=:_max then :_max else %s+:%s end) WHERE %s in (\").append(str).append(\")\");",
								column, column, column, column, column, priKey);
						sn(sb, "            Map params = newMap();");
						sn(sb, "            params.put(\"_max\", _max);");
						sn(sb, "            params.put(\"%s\", %s);", column,
								columnEn);
						sn(sb,
								"            return super.update(sql.toString(), params);");
						sn(sb, "        } catch(Exception e) {");
						sn(sb, "            log.info(e2s(e));");
						sn(sb, "            return 0;");
						sn(sb, "        } finally {");
						sn(sb, "            StringBufPool.returnObject(sb);");
						sn(sb, "            StringBufPool.returnObject(sql);");
						sn(sb, "        }");
						sn(sb, "    }");
						sn(sb, "");
					}

					sn(sb,
							"    public int update%sWithMinMaxByKey(final %s %s, final %s %s, final %s _min, final %s _max){",
							columnUEn, pkBType, priKey, bType, columnEn, bType,
							bType);
					sn(sb,
							"        return update%sWithMinMaxByKey(%s, %s, _min, _max, TABLENAME);",
							columnUEn, priKey, columnEn);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public int update%sWithMinMaxByKey(final %s %s, final %s %s, final %s _min, final %s _max, final String TABLENAME2){",
							columnUEn, pkBType, priKey, bType, columnEn, bType,
							bType);
					sn(sb, "        if( %s < 0 ) {", columnEn);
					sn(sb,
							"            return update%sWithMinByKey(%s, %s, _min, TABLENAME2);",
							columnUEn, priKey, columnEn);
					sn(sb, "        } else {");
					sn(sb,
							"            return update%sWithMaxByKey(%s, %s, _max, TABLENAME2);",
							columnUEn, priKey, columnEn);
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");

					if (batch) {
						sn(sb,
								"    public int update%sWithMinMaxInKeys(final List<%s> keys, final %s %s, final %s _min, final %s _max){",
								columnUEn, priKeyType, bType, columnEn, bType,
								bType);
						sn(sb,
								"        return update%sWithMinMaxInKeys(keys, %s, _min, _max, TABLENAME);",
								columnUEn, columnEn);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public int update%sWithMinMaxInKeys(final List<%s> keys, final %s %s, final %s _min, final %s _max, final String TABLENAME2){",
								columnUEn, priKeyType, bType, columnEn, bType,
								bType);
						sn(sb, "        if( %s < 0 ) {", columnEn);
						sn(sb,
								"            return update%sWithMinInKeys(keys, %s, _min, TABLENAME2);",
								columnUEn, columnEn);
						sn(sb, "        } else {");
						sn(sb,
								"            return update%sWithMaxInKeys(keys, %s, _max, TABLENAME2);",
								columnUEn, columnEn);
						sn(sb, "        }");
						sn(sb, "    }");
						sn(sb, "");
					}

				}
			}

		}

		if (batch) { // 批处理
			sn(sb, "    public int[] updateByKey (final List<%s> %ss) {",
					tbUEn, tbEn);
			sn(sb, "        return updateByKey(%ss, TABLENAME);", tbEn);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public int[] updateByKey (final List<%s> %ss, final String TABLENAME2) {",
					tbUEn, tbEn);
			sn(sb, "        StringBuffer sql = StringBufPool.borrowObject();");
			sn(sb, "        try{");
			sn(sb,
					"            if(%ss == null || %ss.isEmpty()) return new int[0];",
					tbEn, tbEn);
			sn(sb,
					"            sql.append(\"UPDATE \").append(TABLENAME2).append(\" SET %s WHERE %s=:%s\");",
					cols8, priKey, priKey);
			sn(sb,
					"            return super.batchUpdate2(sql.toString(), %ss);",
					tbEn);
			sn(sb, "        } catch(Exception e) {");
			sn(sb, "            log.info(e2s(e));");
			sn(sb, "            return new int[0];");
			sn(sb, "        } finally {");
			sn(sb, "            StringBufPool.returnObject(sql);");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");
		}

		String[] ss = createSql.split("\n");
		StringBuffer createSql2 = new StringBuffer();
		p = 0;
		for (String s : ss) {
			if (p > 0)
				s(createSql2, "                \"%s\"", s);
			else
				s(createSql2, "\"%s\"", s);
			p++;
			if (p < ss.length) {
				sn(createSql2, " +");
			}
		}

		String[] ss2 = createNoUniqueSql.split("\n");
		StringBuffer createNoUniqueSql2 = new StringBuffer();
		p = 0;
		for (String s : ss2) {
			if (p > 0)
				s(createNoUniqueSql2, "                \"%s\"", s);
			else
				s(createNoUniqueSql2, "\"%s\"", s);
			p++;
			if (p < ss.length) {
				sn(createNoUniqueSql2, " +");
			}
		}

		sn(sb, "    public void createTable(final String TABLENAME2){");
		sn(sb, "        try{");
		sn(sb, "            String sql = %s;\r\n", createSql2.toString());
		sn(sb, "            Map params = newMap();");
		sn(sb, "            params.put(\"TABLENAME\", TABLENAME2);");
		sn(sb, "            sql  = EasyTemplate.make(sql, params);");
		sn(sb, "            super.update(sql);");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public void createNoUniqueTable(final String TABLENAME2){");
		sn(sb, "        try{");
		sn(sb, "            String sql = %s;\r\n",
				createNoUniqueSql2.toString());
		sn(sb, "            Map params = newMap();");
		sn(sb, "            params.put(\"TABLENAME\", TABLENAME2);");
		sn(sb, "            sql  = EasyTemplate.make(sql, params);");
		sn(sb, "            super.update(sql);");
		sn(sb, "        } catch(Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public void truncate(){");
		sn(sb, "        try {");
		sn(sb, "            super.truncate(TABLENAME);");
		sn(sb, "        } catch (Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public void repair(){");
		sn(sb, "        try {");
		sn(sb, "            super.repair(TABLENAME);");
		sn(sb, "        } catch (Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public void optimize(){");
		sn(sb, "        try {");
		sn(sb, "            super.optimize(TABLENAME);");
		sn(sb, "        } catch (Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public void dropTable(){");
		sn(sb, "        try {");
		sn(sb, "            super.dropTable(TABLENAME);");
		sn(sb, "        } catch (Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		// sn(sb, "    ScheduledExecutorService _single_executor = null;");
		// sn(sb,
		// "    public void setSingleExecutor(ScheduledExecutorService ses) {");
		// sn(sb, "        this._single_executor = ses;");
		// sn(sb, "    }");
		// sn(sb,
		// "    protected synchronized ScheduledExecutorService executor(final String name) {");
		// sn(sb, "        if (_single_executor == null)");
		// sn(sb,
		// "            _single_executor = Executors.newSingleThreadScheduledExecutor(new MyThreadFactory(name, false));");
		// sn(sb, "        return _single_executor;");
		// sn(sb, "    }");

		sn(sb, "}");

		return sb.toString();
	}
}
