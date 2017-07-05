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
import com.bowlong.util.StrBuilder;

public class InternalBuilder extends Toolkit {
	public static void main(String[] args) throws Exception {
		String sql = "SELECT * FROM `用户的道具` LIMIT 1";
		String host = "127.0.0.1";
		String db = "test";
		String bpackage = "fych.db";
		String appContext = "fych.context.AppContext";
		String clazzName = "AppContext";
		try (Connection conn = SqlEx.newMysqlConnection(host, db);) {
			boolean batch = true;
			boolean sorted = true;

			ResultSet rs = SqlEx.executeQuery(conn, sql);

			String xml = build(conn, rs, bpackage, appContext, batch, sorted,
					clazzName);
			System.out.println(xml);
		}

	}

	public static String build(Connection conn, ResultSet rs, String pkg,
			String appContext, boolean batch, boolean sorted, String clazzName)
			throws Exception {
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
		// 主键是INT
		boolean isInt4Pk = "Integer".equals(priKeyType);

		sn(sb, "package %s.internal;", pkg);
		sn(sb, "");
		sn(sb, "import java.util.*;");
		sn(sb, "import java.util.concurrent.*;");
		sn(sb, "import java.util.concurrent.atomic.*;");
		sn(sb, "");
		sn(sb, "import org.apache.commons.logging.*;");
		// sn(sb, "import java.util.concurrent.*;");
		sn(sb, "");
		sn(sb, "import com.bowlong.sql.*;");
		sn(sb, "import com.bowlong.concurrent.async.*;");
		sn(sb, "import static com.bowlong.sql.CacheModel.*;");
		sn(sb, "");
		sn(sb, "import %s.bean.*;", pkg);
		sn(sb, "import %s.dao.*;", pkg);
		sn(sb, "import %s.entity.*;", pkg);
		sn(sb, "");
		// sn(sb, "import %s;", appContext);
		if (indexs.size() > 1)
			sn(sb, "import static %s.bean.%s.Col;", pkg, tbUEn);

		sn(sb, "");
		sn(sb, "//%s - %s", catalogName, tb);
		sn(sb,
				"@SuppressWarnings({\"rawtypes\", \"unchecked\", \"static-access\"})");
		sn(sb, "public abstract class %sInternal extends InternalSupport{",
				tbUEn);
		sn(sb, "    static Log log = LogFactory.getLog(%sInternal.class);",
				tbUEn);
		// sn(sb, "    // true直接操作数据库, false操作内存缓存");
		// sn(sb, "    // NO_CACHE    // 不缓存");
		// sn(sb, "    // FULL_CACHE  // 全缓存");
		// sn(sb, "    // FULL_MEMORY // 全内存");
		sn(sb, "    public static CacheModel cacheModel = NO_CACHE;");
		// sn(sb, "    // 超时时间(超过时间了则从数据库重新加载数据)");
		// sn(sb, "    public static long LASTTIME = 0;");
		// sn(sb, "    public static long TIMEOUT = %sEntity.TIMEOUT();",
		// tableUEn);
		sn(sb, "");
		// sn(sb, "    // 全内存数据(不写入数据库)");
		// sn(sb, "    public static boolean InMEM = false;");
		sn(sb, "    // public static %s LASTID = 0;", pkBType);
		if (isInt4Pk) {
			sn(sb,
					"    private static AtomicInteger LASTID = new AtomicInteger();");
		} else {
			sn(sb, "    private static AtomicLong LASTID = new AtomicLong();");
		}

		sn(sb, "");
		sn(sb, "    public %sInternal(){}", tbUEn);
		sn(sb, "");
		sn(sb, "    public static %sDAO DAO(){", tbUEn);
		sn(sb, "        return %sEntity.%sDAO();", tbUEn, tbUEn);
		sn(sb, "    }");
		sn(sb, "");
		// sn(sb, "    public static final %sInternal my = new %sInternal();",
		// tableUEn, tableUEn);
		// sn(sb, "");
		// sn(sb,
		// "    public static final Map<%s, ScheduledFuture> futures = newMap();",
		// primaryKeyType);
		sn(sb, "");

		if (isInt4Pk) {
			sn(sb, "    private static int MAX = 0;");
			sn(sb, "    public static void setFixedMAX(int num) {");
			sn(sb, "        MAX = num;");
			sn(sb, "        FIXED = new %s[MAX];", tbUEn);
			sn(sb, "    }");
			sn(sb, "    private static %s[] FIXED = new %s[MAX];", tbUEn, tbUEn);
		}

		sn(sb, "    public static final Map<%s, %s> vars = %s();", priKeyType,
				tbUEn, (sorted ? "newSortedMap" : "newMap"));
		{
			Iterator<String> it = indexs.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<Map<String, Object>> idx = indexs.get(key);
				int idx_size = idx.size();
				if (idx_size == 1) { // 单索引
					Map<String, Object> index = idx.get(0);
					String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
					String COL_NAME = MapEx.get(index, "COLUMN_NAME");
					String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
					String COLUMN_NAME_EN = PinYin.getShortPinYin(COL_NAME);
					String COLUMN_NAME_UEN = StrEx.upperN1(COLUMN_NAME_EN);
					String COLUMN_NAME_TYPE = JavaType.getType(rsmd, COL_NAME);
					if (INDEX_NAME.equals("PRIMARY"))
						continue;
					if (NON_UNIQUE.equals("false")) { // 唯一数据
						sn(sb,
								"    public static final Map<%s, %s> varsBy%s = %s();",
								COLUMN_NAME_TYPE, priKeyType, COLUMN_NAME_UEN,
								(sorted ? "newSortedMap" : "newMap"));
					} else {
						if (COLUMN_NAME_TYPE.equals("java.util.Date"))
							continue;
						sn(sb,
								"    public static final Map<%s, Set<%s>> varsBy%s = %s();",
								COLUMN_NAME_TYPE, priKeyType, COLUMN_NAME_UEN,
								(sorted ? "newSortedMap" : "newMap"));
					}

				} else { // 多索引
					Map<String, Object> index = idx.get(0);
					String index1 = BeanBuilder.index1(rsmd, idx);
					// String index2 = SqlBeanBuilder.index2(rsmd, idx);
					// String index3 = SqlBeanBuilder.index3(rsmd, idx);
					// String index4 = SqlBeanBuilder.index4(rsmd, idx);
					String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
					if (NON_UNIQUE.equals("false")) { // 唯一数据
						sn(sb,
								"    public static final Map<String, %s> varsBy%s = %s();",
								priKeyType, index1, (sorted ? "newSortedMap"
										: "newMap"));
					} else {
						sn(sb,
								"    public static final Map<String, Set<%s>> varsBy%s = %s();",
								priKeyType, index1, (sorted ? "newSortedMap"
										: "newMap"));
					}
				}
			}
		}
		sn(sb, "");
		sn(sb, "    private static final List<%s> fixedCache = newList();",
				tbUEn);
		sn(sb, "");

		sn(sb, "    public static void put(%s %s, boolean force){", tbUEn, tbEn);
		sn(sb, "        if(%s == null || %s.%s <= 0) return ;", tbEn, tbEn,
				priKey);
		sn(sb, "");
		sn(sb, "        %s %s = %s.%s;", pkBType, priKey, tbEn, priKey);
		sn(sb, "        if (cacheModel == STATIC_CACHE) {");

		if (isInt4Pk) {
			sn(sb, "            if (%s > 0 && %s <= FIXED.length) {", priKey,
					priKey);
			sn(sb,
					"                if (FIXED[%s - 1] == null || !FIXED[%s - 1].equals(%s))",
					priKey, priKey, tbEn);
			sn(sb, "                	FIXED[%s - 1] = %s;", priKey, tbEn);
			sn(sb, "                if (!fixedCache.contains(%s))", tbEn, tbEn);
			sn(sb, "                	fixedCache.add(%s);", tbEn);
			sn(sb, "            }");
		} else {
			sn(sb, "            if (%s > 0) {", priKey);
			sn(sb, "                if (!fixedCache.contains(%s))", tbEn, tbEn);
			sn(sb, "                	fixedCache.add(%s);", tbEn);
			sn(sb, "            }");
		}

		sn(sb, "        } else {");
		sn(sb, "            vars.put(%s, %s);", priKey, tbEn);
		sn(sb, "        }");
		sn(sb, "");
		{
			Iterator<String> it = indexs.keySet().iterator();
			int p = 0;
			while (it.hasNext()) {
				String key = it.next();
				List<Map<String, Object>> idx = indexs.get(key);
				int idx_size = idx.size();
				if (idx_size == 1) { // 单索引
					Map<String, Object> index = idx.get(0);
					String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
					String COL_NAME = MapEx.get(index, "COLUMN_NAME");
					String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
					String COL_NAME_EN = PinYin.getShortPinYin(COL_NAME);
					String COL_NAME_UEN = StrEx.upperN1(COL_NAME_EN);
					String COL_NAME_TYPE = JavaType.getType(rsmd, COL_NAME);
					String bType = JavaType.getBasicType(COL_NAME_TYPE);

					if (INDEX_NAME.equals("PRIMARY"))
						continue;
					if (NON_UNIQUE.equals("false")) { // 唯一数据
						sn(sb, "        { // 单-唯一索引 remove old index %s",
								COL_NAME);
						sn(sb, "          Object ov = %s.oldVal(Col.%s);",
								tbEn, COL_NAME);
						sn(sb, "          if(ov != null)");
						sn(sb, "              varsBy%s.remove(ov);",
								COL_NAME_UEN);
						sn(sb,
								"          if(ov != null || force){ // put new index");
						sn(sb, "            %s %s = %s.get%s();", bType,
								COL_NAME_EN, tbEn, COL_NAME_UEN);
						sn(sb, "            varsBy%s.put(%s, %s);",
								COL_NAME_UEN, COL_NAME_EN, priKey);
						sn(sb, "          }");
						sn(sb, "        }");
					} else {
						if (bType.equals("java.util.Date"))
							continue;
						p = p + 1;
						sn(sb, "        { // 单-非唯一索引 remove old index %s",
								COL_NAME);
						sn(sb, "          Object ov = %s.oldVal(Col.%s);",
								tbEn, COL_NAME);
						sn(sb, "          if(ov != null) {");
						sn(sb, "              %s _val = (%s) ov;",
								COL_NAME_TYPE, COL_NAME_TYPE);
						sn(sb, "              Set m%d = varsBy%s.get(_val);",
								p, COL_NAME_UEN);
						sn(sb, "              if(m%d != null) {", p);
						sn(sb, "                  m%d.remove(%s);", p, priKey);
						sn(sb, "                  if(m%d.isEmpty())", p);
						sn(sb, "                      varsBy%s.remove(_val);",
								COL_NAME_UEN, p);
						sn(sb, "              }");
						sn(sb, "          }");
						sn(sb,
								"          if(ov != null || force){ // put new index");
						sn(sb, "            %s %s = %s.get%s();", bType,
								COL_NAME_EN, tbEn, COL_NAME_UEN);
						sn(sb, "            Set m%d = varsBy%s.get(%s);", p,
								COL_NAME_UEN, COL_NAME_EN);
						sn(sb, "            if(m%d == null){", p);
						sn(sb, "                m%d = %s();", p,
								(sorted ? "newSortedSet" : "newSet"));
						sn(sb, "                varsBy%s.put(%s, m%d);",
								COL_NAME_UEN, COL_NAME_EN, p);
						sn(sb, "            }", p);
						sn(sb, "            m%d.add(%s);", p, priKey);
						sn(sb, "          }");
						sn(sb, "        }");
					}
					sn(sb, "");

				} else { // 多索引
					Map<String, Object> index = idx.get(0);
					String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
					String index1 = BeanBuilder.index1(rsmd, idx);
					// String index2 = SqlBeanBuilder.index2(rsmd, idx);
					// String index3 = SqlBeanBuilder.index3(rsmd, idx);
					// String index4 = SqlBeanBuilder.index4(rsmd, idx);
					String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
					if (NON_UNIQUE.equals("false")) { // 唯一数据
						String skey = "com.bowlong.lang.PStr.b().a(";
						String skey1 = "com.bowlong.lang.PStr.b().a(";
						sn(sb, "        { // %s", INDEX_NAME);
						StrBuilder tmp = new StrBuilder();
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							tmp.a("Col.").a(COL_NAME).a(", ");
						}
						if (tmp.len() > 2)
							tmp.removeRight(2);
						sn(sb,
								"          boolean bChanged = %s.inChanged(%s);",
								tbEn, tmp.str());
						sn(sb,
								"          if(bChanged) { // 多-唯一索引 remove old index");
						tmp.close();
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							String COL_NAME_EN = PinYin
									.getShortPinYin(COL_NAME);
							sn(sb,
									"            Object v%s = %s.oldOrNew(Col.%s);",
									COL_NAME_EN, tbEn, COL_NAME);
							skey1 = skey1 + s("v%s", COL_NAME_EN);
							if (idx.indexOf(map) < idx.size() - 1) {
								skey1 = skey1 + ", \"-\", ";
							}
						}
						skey1 = skey1 + ").e()";
						sn(sb, "            String okey = %s;", skey1);
						sn(sb, "            varsBy%s.remove(okey);", index1);
						sn(sb, "          }");
						sn(sb,
								"          if(bChanged || force) { // put new index");
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							String COL_NAME_EN = PinYin
									.getShortPinYin(COL_NAME);
							String COL_NAME_UEN = StrEx.upperN1(COL_NAME_EN);
							String COL_NAME_TYPE = JavaType.getType(rsmd,
									COL_NAME);
							String bType = JavaType.getBasicType(COL_NAME_TYPE);
							sn(sb, "              %s v%s = %s.get%s();", bType,
									COL_NAME_EN, tbEn, COL_NAME_UEN);
							skey = skey + s("v%s", COL_NAME_EN);
							if (idx.indexOf(map) < idx.size() - 1) {
								skey = skey + ", \"-\", ";
							}
						}
						skey = skey + ").e()";
						sn(sb, "              String vkey = %s;", skey);
						sn(sb, "              varsBy%s.put(vkey, %s);", index1,
								priKey);
						sn(sb, "          }");
						sn(sb, "        }");
					} else {
						String skey = "com.bowlong.lang.PStr.b().a(";
						String skey1 = "com.bowlong.lang.PStr.b().a(";
						p = p + 1;
						sn(sb, "        { // %s", INDEX_NAME);
						StrBuilder tmp = new StrBuilder();
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							tmp.a("Col.").a(COL_NAME).a(", ");
						}
						if (tmp.len() > 2)
							tmp.removeRight(2);
						sn(sb,
								"          boolean bChanged = %s.inChanged(%s);",
								tbEn, tmp.str());
						sn(sb,
								"          if(bChanged){ // 多-非唯一索引 remove old index");
						tmp.close();
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							String COL_NAME_EN = PinYin
									.getShortPinYin(COL_NAME);
							sn(sb,
									"              Object v%s = %s.oldOrNew(Col.%s);",
									COL_NAME_EN, tbEn, COL_NAME);
							skey1 = skey1 + s("v%s", COL_NAME_EN);
							if (idx.indexOf(map) < idx.size() - 1) {
								skey1 = skey1 + ", \"-\", ";
							}
						}
						skey1 = skey1 + ").e()";
						sn(sb, "              String okey = %s;", skey1);
						sn(sb, "              Set m%d = varsBy%s.get(okey);",
								p, index1);
						sn(sb, "              if(m%d != null) {", p);
						sn(sb, "                  m%d.remove(%s);", p, priKey);
						sn(sb, "                  if(m%d.isEmpty())", p);
						sn(sb, "                       varsBy%s.remove(okey);",
								index1);
						sn(sb, "              }", p);
						sn(sb, "          }");
						sn(sb,
								"          if(bChanged || force) { // put new index",
								INDEX_NAME);
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							String COL_NAME_EN = PinYin
									.getShortPinYin(COL_NAME);
							String COL_NAME_UEN = StrEx.upperN1(COL_NAME_EN);
							String COL_NAME_TYPE = JavaType.getType(rsmd,
									COL_NAME);
							String bType = JavaType.getBasicType(COL_NAME_TYPE);

							sn(sb, "              %s v%s = %s.get%s();", bType,
									COL_NAME_EN, tbEn, COL_NAME_UEN);
							skey = skey + s("v%s", COL_NAME_EN);
							if (idx.indexOf(map) < idx.size() - 1) {
								skey = skey + ", \"-\", ";
							}
						}
						skey = skey + ").e()";
						sn(sb, "              String vkey = %s;", skey);
						sn(sb, "              Set m%d = varsBy%s.get(vkey);",
								p, index1);
						sn(sb, "              if(m%d == null){", p);
						sn(sb, "                  m%d = %s();", p,
								(sorted ? "newSortedSet" : "newSet"));
						sn(sb, "                  varsBy%s.put(vkey, m%d);",
								index1, p);
						sn(sb, "              }", p);
						sn(sb, "              m%d.add(%s);", p, priKey);
						sn(sb, "          }");
						sn(sb, "        }");
					}
					sn(sb, "");
				}
			}
		}
		sn(sb, "        // LASTID = %s > LASTID ? %s : LASTID;", priKey, priKey);
		sn(sb, "        if (%s > LASTID.get()) LASTID.set(%s);", priKey, priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void clear(){");
		{
			Iterator<String> it = indexs.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<Map<String, Object>> idx = indexs.get(key);
				int idx_size = idx.size();
				if (idx_size == 1) { // 单索引
					Map<String, Object> index = idx.get(0);
					String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
					String COLUMN_NAME = MapEx.get(index, "COLUMN_NAME");
					// String NON_UNIQUE =
					// String.valueOf(index.get("NON_UNIQUE"));
					String COLUMN_NAME_EN = PinYin.getShortPinYin(COLUMN_NAME);
					String COLUMN_NAME_UEN = StrEx.upperN1(COLUMN_NAME_EN);
					String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
							COLUMN_NAME);
					String basicType = JavaType.getBasicType(COLUMN_NAME_TYPE);

					if (INDEX_NAME.equals("PRIMARY"))
						continue;
					if (basicType.equals("java.util.Date"))
						continue;

					sn(sb, "        varsBy%s.clear();", COLUMN_NAME_UEN);

				} else { // 多索引
					// Map<String, Object> index = idx.get(0);
					String index1 = BeanBuilder.index1(rsmd, idx);
					// String index2 = SqlBeanBuilder.index2(rsmd, idx);
					// String index3 = SqlBeanBuilder.index3(rsmd, idx);
					// String index4 = SqlBeanBuilder.index4(rsmd, idx);
					sn(sb, "        varsBy%s.clear();", index1);
				}
			}
		}

		if (isInt4Pk) {
			sn(sb, "        FIXED = new %s[MAX];", tbUEn);
		}
		sn(sb, "        fixedCache.clear();", tbUEn);
		sn(sb, "        vars.clear();");
		sn(sb, "        LASTID.set(0);");
		// sn(sb, "        clearFutures();");
		sn(sb, "    }");
		sn(sb, "");

		// sn(sb, "    public static void clearIndexs(){");
		// {
		// Iterator<String> it = indexs.keySet().iterator();
		// while (it.hasNext()) {
		// String key = it.next();
		// List<Map<String, Object>> idx = indexs.get(key);
		// int idx_size = idx.size();
		// if(idx_size == 1){ // 单索引
		// Map<String, Object> index = idx.get(0);
		// String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
		// String COLUMN_NAME = MapEx.get(index, "COLUMN_NAME");
		// // String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
		// String COLUMN_NAME_EN = PinYin.getShortPinYin(COLUMN_NAME);
		// String COLUMN_NAME_UEN = StrEx.upperN1(COLUMN_NAME_EN);
		// String COLUMN_NAME_TYPE = JavaType.getType(rsmd, COLUMN_NAME);
		// String basicType = JavaType.getBasicType(COLUMN_NAME_TYPE);
		//
		// if(INDEX_NAME.equals("PRIMARY"))
		// continue;
		// if(basicType.equals("java.util.Date"))
		// continue;
		//
		// sn(sb, "        varsBy%s.clear();", COLUMN_NAME_UEN);
		//
		// }else{ // 多索引
		// // Map<String, Object> index = idx.get(0);
		// String index1 = BeanBuilder.index1(rsmd, idx);
		// // String index2 = SqlBeanBuilder.index2(rsmd, idx);
		// // String index3 = SqlBeanBuilder.index3(rsmd, idx);
		// // String index4 = SqlBeanBuilder.index4(rsmd, idx);
		// sn(sb, "        varsBy%s.clear();", index1);
		// }
		// }
		// }
		// sn(sb, "    }");
		// sn(sb, "");

		sn(sb, "    public static int count(){");
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return count(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int count(String TABLENAME2){");
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return count(DAO, TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int count(%sDAO DAO){", tbUEn);
		sn(sb, "        return count(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int count(%sDAO DAO, String TABLENAME2){",
				tbUEn);
		sn(sb, "        if( cacheModel == NO_CACHE ){");
		sn(sb, "            return DAO.count(TABLENAME2);");
		sn(sb, "        } else if (cacheModel == STATIC_CACHE) {");
		sn(sb, "            return fixedCache.size();");
		sn(sb, "        } else {  // FULL_CACHE || FULL_MEMORY");
		sn(sb, "            return vars.size();");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void relocate(String TABLENAME2) {");
		sn(sb, "        DAO().TABLENAME = TABLENAME2;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static void relocate(%sDAO DAO, String TABLENAME2) {",
				tbUEn);
		sn(sb, "        DAO().TABLENAME = TABLENAME2;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static String createTableYy() {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return createTableYy(DAO);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static String createTableYy(%sDAO DAO) {", tbUEn);
		sn(sb, "        String TABLENAME2 = DAO.TABLEYY();");
		sn(sb, "        createTable(DAO, TABLENAME2);");
		sn(sb, "        return TABLENAME2;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static String createTableMm() {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return createTableMm(DAO);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static String createTableMm(%sDAO DAO) {", tbUEn);
		sn(sb, "        String TABLENAME2 = DAO.TABLEMM();");
		sn(sb, "        createTable(DAO, TABLENAME2);");
		sn(sb, "        return TABLENAME2;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static String createTableDd() {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return createTableDd(DAO);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static String createTableDd(%sDAO DAO) {", tbUEn);
		sn(sb, "        String TABLENAME2 = DAO.TABLEDD();");
		sn(sb, "        createTable(DAO, TABLENAME2);");
		sn(sb, "        return TABLENAME2;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void createTable(String TABLENAME2) {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        DAO.createTable(TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void createTable(%sDAO DAO) {", tbUEn);
		sn(sb, "        DAO.createTable(DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static void createTable(%sDAO DAO, String TABLENAME2) {",
				tbUEn);
		sn(sb, "        DAO.createTable(TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static void createNoUniqueTable(String TABLENAME2) {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        DAO.createNoUniqueTable(TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void createNoUniqueTable(%sDAO DAO) {", tbUEn);
		sn(sb, "        DAO.createNoUniqueTable(DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static void createNoUniqueTable(%sDAO DAO, String TABLENAME2) {",
				tbUEn);
		sn(sb, "        DAO.createNoUniqueTable(TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void loadAll() {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        loadAll(DAO);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void loadAll(%sDAO DAO) {", tbUEn);
		sn(sb, "        if( cacheModel == NO_CACHE )");
		sn(sb, "            return;");
		sn(sb, "        clear();");
		if (isInt4Pk) {
			sn(sb, "        if( cacheModel == STATIC_CACHE )");
			sn(sb, "            if (FIXED == null || MAX <= 0)");
			sn(sb, "                FIXED = new %s[maxId(DAO)];", tbUEn);
		}
		sn(sb, "");
		sn(sb, "        List<%s> %ss = DAO.selectAll();", tbUEn, tbEn);
		sn(sb, "        for (%s %s : %ss) {", tbUEn, tbEn, tbEn);
		sn(sb, "            %s.reset();", tbEn);
		sn(sb, "            put(%s, true);", tbEn);
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static Map toMap(%s %s){", tbUEn, tbEn);
		sn(sb, "        return %s.toMap();", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<Map> toMap(List<%s> %ss){", tbUEn, tbEn);
		sn(sb, "        List<Map> ret = new Vector<Map>();");
		sn(sb, "        for (%s %s : %ss){", tbUEn, tbEn, tbEn);
		sn(sb, "            Map e = %s.toMap();", tbEn);
		sn(sb, "            ret.add(e);");
		sn(sb, "        }");
		sn(sb, "        return ret;");
		sn(sb, "    }");
		sn(sb, "");

		// sortBy
		sn(sb,
				"    public static List<%s> sortZh(List<%s> %ss, final String key) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        Collections.sort(%ss, new Comparator<%s>() {", tbEn,
				tbUEn, tbUEn);
		sn(sb, "            public int compare(%s o1, %s o2) {", tbUEn, tbUEn);
		sn(sb, "                return o1.compareZhTo(o2, key);");
		sn(sb, "            }");
		sn(sb, "        });");
		sn(sb, "        return %ss;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> sort(List<%s> %ss, final String key) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        Collections.sort(%ss, new Comparator<%s>() {", tbEn,
				tbUEn, tbUEn);
		sn(sb, "            public int compare(%s o1, %s o2) {", tbUEn, tbUEn);
		sn(sb, "                return o1.compareTo(o2, key);");
		sn(sb, "            }");
		sn(sb, "        });");
		sn(sb, "        return %ss;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> sort(List<%s> %ss){", tbUEn, tbUEn,
				tbEn);
		sn(sb, "        Collections.sort(%ss, new Comparator<%s>(){", tbEn,
				tbUEn);
		sn(sb, "            public int compare(%s o1, %s o2) {", tbUEn, tbUEn);
		sn(sb, "                Object v1 = o1.%s;", priKey);
		sn(sb, "                Object v2 = o2.%s;", priKey);
		sn(sb, "                return compareTo(v1, v2);");
		sn(sb, "            }");
		sn(sb, "        });");
		sn(sb, "        return %ss;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> sortReverse(List<%s> %ss){", tbUEn,
				tbUEn, tbEn);
		sn(sb, "        Collections.sort(%ss, new Comparator<%s>(){", tbEn,
				tbUEn);
		sn(sb, "            public int compare(%s o1, %s o2) {", tbUEn, tbUEn);
		sn(sb, "                Object v1 = o1.%s;", priKey);
		sn(sb, "                Object v2 = o2.%s;", priKey);
		sn(sb, "                return 0 - compareTo(v1, v2);");
		sn(sb, "            }");
		sn(sb, "        });");
		sn(sb, "        return %ss;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		{
			Iterator<String> it = indexs.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				List<Map<String, Object>> idx = indexs.get(key);
				int idx_size = idx.size();
				if (idx_size == 1) { // 单索引
					Map<String, Object> index = idx.get(0);
					String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
					String COLUMN_NAME = MapEx.get(index, "COLUMN_NAME");
					// String NON_UNIQUE =
					// String.valueOf(index.get("NON_UNIQUE"));
					String COLUMN_NAME_EN = PinYin.getShortPinYin(COLUMN_NAME);
					String COLUMN_NAME_UEN = StrEx.upperN1(COLUMN_NAME_EN);
					// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
					// COLUMN_NAME);
					// String basicType =
					// JavaType.getBasicType(COLUMN_NAME_TYPE);

					if (INDEX_NAME.equals("PRIMARY"))
						continue;
					sn(sb, "    public static List<%s> sort%s(List<%s> %ss){",
							tbUEn, COLUMN_NAME_UEN, tbUEn, tbEn);
					sn(sb,
							"        Collections.sort(%ss, new Comparator<%s>(){",
							tbEn, tbUEn);
					sn(sb, "            public int compare(%s o1, %s o2) {",
							tbUEn, tbUEn);
					sn(sb, "                Object v1 = o1.get%s();",
							COLUMN_NAME_UEN);
					sn(sb, "                Object v2 = o2.get%s();",
							COLUMN_NAME_UEN);
					sn(sb, "                return compareTo(v1, v2);");
					sn(sb, "            }");
					sn(sb, "        });");
					sn(sb, "        return %ss;", tbEn);
					sn(sb, "    }");
					sn(sb, "");
					sn(sb,
							"    public static List<%s> sort%sRo(List<%s> %ss){",
							tbUEn, COLUMN_NAME_UEN, tbUEn, tbEn);
					sn(sb,
							"        Collections.sort(%ss, new Comparator<%s>(){",
							tbEn, tbUEn);
					sn(sb, "            public int compare(%s o1, %s o2) {",
							tbUEn, tbUEn);
					sn(sb, "                Object v1 = o1.get%s();",
							COLUMN_NAME_UEN);
					sn(sb, "                Object v2 = o2.get%s();",
							COLUMN_NAME_UEN);
					sn(sb, "                return 0 - compareTo(v1, v2);");
					sn(sb, "            };");
					sn(sb, "        });");
					sn(sb, "        return %ss;", tbEn);
					sn(sb, "    }");
					sn(sb, "");
				} else { // 多索引
				}
			}
		}

		sn(sb, "    public static %s insert(%s %s) {", tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return insert(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s insert(%sDAO DAO, %s %s) {", tbUEn, tbUEn,
				tbUEn, tbEn);
		sn(sb, "        return insert(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s insert(%s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return insert(DAO, %s, TABLENAME2);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static %s insert(%sDAO DAO, %s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbUEn, tbEn);
		if (isInt4Pk) {
			sn(sb,
					"        if (cacheModel == STATIC_CACHE && LASTID.intValue() >= MAX) {");
			sn(sb,
					"            log.error(\"The cacheModel = STATIC_CACHE is Full.\");");
			sn(sb, "            return null;");
			sn(sb, "        }");
			sn(sb, "");
		}
		sn(sb, "        %s n = 0;", pkBType);
		sn(sb, "        if(cacheModel != FULL_MEMORY){");
		sn(sb, "            n = DAO.insert(%s, TABLENAME2);", tbEn);
		sn(sb, "            if(n <= 0) return null;");
		sn(sb, "        }else{");
		sn(sb, "            n = LASTID.incrementAndGet();");
		sn(sb, "            // n = LASTID + 1;");
		sn(sb, "        }");
		sn(sb, "");
		sn(sb, "        %s.%s = n;", tbEn, priKey);
		sn(sb, "        if(cacheModel != NO_CACHE) put(%s, true);", tbEn);
		sn(sb, "");
		sn(sb, "        return %s;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s asyncInsert(%s %s) {", tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return asyncInsert(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");

		sn(sb, "    public static %s asyncInsert(%sDAO DAO, %s %s) {", tbUEn,
				tbUEn, tbUEn, tbEn);
		sn(sb, "        return asyncInsert(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");

		sn(sb, "    public static %s asyncInsert(%s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return asyncInsert(DAO, %s, TABLENAME2);", tbEn);
		sn(sb, "    }");

		sn(sb,
				"    public static %s asyncInsert(%sDAO DAO, %s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbUEn, tbEn);
		if (isInt4Pk) {
			sn(sb,
					"        if (cacheModel == STATIC_CACHE && LASTID.intValue() >= MAX) {");
			sn(sb,
					"            log.error(\"The cacheModel = STATIC_CACHE is Full.\");");
			sn(sb, "            return null;");
			sn(sb, "        }");
		}

		sn(sb, "        %s n = 0;", pkBType);
		sn(sb, "        if(cacheModel != FULL_MEMORY) {");
		sn(sb, "            DAO.asyncInsert(%s, TABLENAME2);", tbEn);
		sn(sb, "        }");
		sn(sb, "        n = LASTID.incrementAndGet();");
		sn(sb, "        %s.%s = n;", tbEn, priKey);
		sn(sb, "        if(cacheModel != NO_CACHE) put(%s, true);", tbEn);
		sn(sb, "        return %s;", tbEn);
		sn(sb, "    }");

		sn(sb, "    public static %s insert2(%s %s) {", tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return insert2(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s insert2(%sDAO DAO, %s %s) {", tbUEn,
				tbUEn, tbUEn, tbEn);
		sn(sb, "        return insert2(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s insert2(%s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return insert2(DAO, %s, TABLENAME2);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static %s insert2(%sDAO DAO, %s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbUEn, tbEn);
		if (isInt4Pk) {
			sn(sb,
					"        if (cacheModel == STATIC_CACHE && LASTID.intValue() >= MAX) {");
			sn(sb,
					"            log.error(\"The cacheModel = STATIC_CACHE is Full.\");");
			sn(sb, "            return null;");
			sn(sb, "        }");
		}
		sn(sb, "        %s n = 0;", pkBType);
		sn(sb, "        if(cacheModel != FULL_MEMORY){");
		sn(sb, "            n = DAO.insert2(%s, TABLENAME2);", tbEn);
		sn(sb, "            if(n <= 0) return null;");
		sn(sb, "        }else{");
		sn(sb, "            n = LASTID.incrementAndGet();");
		sn(sb, "            // n = LASTID + 1;");
		sn(sb, "        }");
		sn(sb, "");
		sn(sb, "        %s.%s = n;", tbEn, priKey);
		sn(sb, "        if(cacheModel != NO_CACHE) put(%s, true);", tbEn);
		sn(sb, "");
		sn(sb, "        return %s;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		//
		sn(sb, "    public static %s asyncInsert2(%s %s) {", tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return asyncInsert2(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");

		sn(sb, "    public static %s asyncInsert2(%sDAO DAO, %s %s) {", tbUEn,
				tbUEn, tbUEn, tbEn);
		sn(sb, "        return asyncInsert2(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");

		sn(sb, "    public static %s asyncInsert2(%s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return asyncInsert2(DAO, %s, TABLENAME2);", tbEn);
		sn(sb, "    }");

		sn(sb,
				"    public static %s asyncInsert2(%sDAO DAO, %s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbUEn, tbEn);
		if (isInt4Pk) {
			sn(sb,
					"        if (cacheModel == STATIC_CACHE && LASTID.intValue() >= MAX) {");
			sn(sb,
					"            log.error(\"The cacheModel = STATIC_CACHE is Full.\");");
			sn(sb, "            return null;");
			sn(sb, "        }");
		}

		sn(sb, "        %s n = LASTID.incrementAndGet();", pkBType);
		sn(sb, "        %s.%s = n;", tbEn, priKey);
		sn(sb, "        if(cacheModel != FULL_MEMORY) {");
		sn(sb, "            DAO.asyncInsert2(%s, TABLENAME2);", tbEn);
		sn(sb, "        }");
		sn(sb, "        if(cacheModel != NO_CACHE) put(%s, true);", tbEn);
		sn(sb, "        return %s;", tbEn);
		sn(sb, "    }");
		//

		if (batch) { // 批处理
			sn(sb, "    public static int[] insert(List<%s> %ss) {", tbUEn,
					tbEn);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return insert(DAO, %ss, DAO.TABLENAME);", tbEn);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb, "    public static int[] insert(%sDAO DAO, List<%s> %ss) {",
					tbUEn, tbUEn, tbEn);
			sn(sb, "        return insert(DAO, %ss, DAO.TABLENAME);", tbEn);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int[] insert(List<%s> %ss, String TABLENAME2) {",
					tbUEn, tbEn, tbUEn, tbEn);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return insert(DAO, %ss, TABLENAME2);", tbEn);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int[] insert(%sDAO DAO, List<%s> %ss, String TABLENAME2) {",
					tbUEn, tbUEn, tbEn);
			if (isInt4Pk) {
				sn(sb,
						"        if (cacheModel == STATIC_CACHE && LASTID.intValue() >= MAX) {");
				sn(sb,
						"            log.error(\"The cacheModel = STATIC_CACHE is Full.\");");
				sn(sb, "            return null;");
				sn(sb, "        }");
			}

			sn(sb, "        if(cacheModel == NO_CACHE){");
			sn(sb, "            int[] r2 = DAO.insert(%ss, TABLENAME2);", tbEn);
			sn(sb, "            int n = 0;");
			sn(sb, "            for(%s %s : %ss){", tbUEn, tbEn, tbEn);
			sn(sb, "                %s.%s = r2[n++];", tbEn, priKey);
			sn(sb, "            }");
			sn(sb, "            return r2;");
			sn(sb, "        }");
			sn(sb, "");
			sn(sb, "        int[] ret = new int[%ss.size()];", tbEn);
			sn(sb, "        int n = 0;");
			sn(sb, "        for(%s %s : %ss){", tbUEn, tbEn, tbEn);
			sn(sb, "            %s = insert(DAO, %s, TABLENAME2);", tbEn, tbEn);
			sn(sb, "            ret[n++] = (%s == null) ? 0 : (int)%s.%s;",
					tbEn, tbEn, priKey);
			sn(sb, "        }");
			sn(sb, "        return ret;");
			sn(sb, "    }");
			sn(sb, "");
		}

		sn(sb, "    public static int delete(%s %s) {", tbUEn, tbEn);
		sn(sb, "        %s %s = %s.%s;", pkBType, priKey, tbEn, priKey);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return delete(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int delete(%s %s) {", pkBType, priKey);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return delete(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int delete(%sDAO DAO, %s %s) {", tbUEn,
				pkBType, priKey);
		sn(sb, "        return delete(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int delete(%s %s, String TABLENAME2) {",
				pkBType, priKey);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return delete(DAO, %s, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static int delete(%sDAO DAO, %s %s, String TABLENAME2) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        int n = 1;");
		sn(sb, "        if(cacheModel != FULL_MEMORY){");
		sn(sb, "            n = DAO.deleteByKey(%s, TABLENAME2);", priKey);
		sn(sb, "            //if(n <= 0) return 0;");
		sn(sb, "        }");
		sn(sb, "        if(cacheModel != NO_CACHE) {");
		sn(sb, "            deleteFromMemory(%s);", priKey);
		sn(sb, "        }");
		sn(sb, "        return n;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void asyncDelete(%s %s) {", tbUEn, tbEn);
		sn(sb, "        %s %s = %s.%s;", pkBType, priKey, tbEn, priKey);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        asyncDelete(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void asyncDelete(%s %s) {", pkBType, priKey);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        asyncDelete(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static void asyncDelete(%sDAO DAO, %s %s) {", tbUEn,
				pkBType, priKey);
		sn(sb, "        asyncDelete(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static void asyncDelete(%s %s, String TABLENAME2) {",
				pkBType, priKey);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        asyncDelete(DAO, %s, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static void asyncDelete(%sDAO DAO, %s %s, String TABLENAME2) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        if(cacheModel != FULL_MEMORY){");
		sn(sb, "            DAO.asyncDeleteByKey(%s, TABLENAME2);", priKey);
		sn(sb, "        }");
		sn(sb, "        if(cacheModel != NO_CACHE) {");
		sn(sb, "            deleteFromMemory(%s);", priKey);
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		if (batch) { // 批处理
			sn(sb, "    public static int[] delete(%s[] %ss) {", pkBType,
					priKey);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return delete(DAO, %ss, DAO.TABLENAME);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb, "    public static int[] delete(%sDAO DAO, %s[] %ss) {",
					tbUEn, pkBType, priKey);
			sn(sb, "        return delete(DAO, %ss, DAO.TABLENAME);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int[] delete(%s[] %ss,String TABLENAME2) {",
					pkBType, priKey);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return delete(DAO, %ss, TABLENAME2);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int[] delete(%sDAO DAO, %s[] %ss,String TABLENAME2) {",
					tbUEn, pkBType, priKey);
			sn(sb, "        if(cacheModel == NO_CACHE){");
			sn(sb, "            return DAO.deleteByKey(%ss, TABLENAME2);",
					priKey);
			sn(sb, "        }");
			sn(sb, "        int[] ret = new int[%ss.length];", priKey);
			sn(sb, "        int n = 0;");
			sn(sb, "        for(%s %s : %ss){", pkBType, priKey, priKey);
			sn(sb, "            ret[n++] = delete(DAO, %s, TABLENAME2);",
					priKey);
			sn(sb, "        }");
			sn(sb, "        return ret;");
			sn(sb, "    }");
			sn(sb, "");
		}

		if (batch) {
			sn(sb, "    public static int deleteIn(List<%s> keys) {",
					priKeyType);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return deleteIn(keys, DAO, DAO.TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int deleteIn(List<%s> keys, %sDAO DAO) {",
					priKeyType, tbUEn);
			sn(sb, "        return deleteIn(keys, DAO, DAO.TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int deleteIn(List<%s> keys, String TABLENAME2) {",
					priKeyType);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return deleteIn(keys, DAO, TABLENAME2);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int deleteIn(final List<%s> keys, final %sDAO DAO, final String TABLENAME2) {",
					priKeyType, tbUEn);
			sn(sb, "        if(keys == null || keys.isEmpty()) return 0;");
			sn(sb, "        int result = DAO.deleteInKeys(keys, TABLENAME2);");
			sn(sb, "        if(cacheModel != NO_CACHE) {");
			sn(sb, "            for(%s %s : keys){", priKeyType, priKey);
			sn(sb, "                deleteFromMemory(%s);", priKey);
			sn(sb, "            }");
			sn(sb, "        }");
			sn(sb, "        return result;");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb, "    public static int deleteWith(List<%s> beans) {", tbUEn);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return deleteWith(beans, DAO, DAO.TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int deleteWith(List<%s> beans, %sDAO DAO) {",
					tbUEn, tbUEn);
			sn(sb, "        return deleteWith(beans, DAO, DAO.TABLENAME);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int deleteWith(List<%s> beans, String TABLENAME2) {",
					tbUEn);
			sn(sb, "        %sDAO DAO = DAO();", tbUEn);
			sn(sb, "        return deleteWith(beans, DAO, TABLENAME2);");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static int deleteWith(final List<%s> beans, final %sDAO DAO, final String TABLENAME2) {",
					tbUEn, tbUEn);
			sn(sb, "        if(beans == null || beans.isEmpty()) return 0;");
			sn(sb, "        int result = DAO.deleteInBeans(beans, TABLENAME2);");
			sn(sb, "        if(cacheModel != NO_CACHE) {");
			sn(sb, "            for(%s %s : beans){", tbUEn, tbEn);
			sn(sb, "                %s %s = %s.%s;", pkBType, priKey, tbEn,
					priKey);
			sn(sb, "                deleteFromMemory(%s);", priKey);
			sn(sb, "            }");
			sn(sb, "        }");
			sn(sb, "        return result;");
			sn(sb, "    }");
			sn(sb, "");

		}

		sn(sb, "    public static List<%s> getAll() {", tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getAll(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getAll(%sDAO DAO) {", tbUEn, tbUEn);
		sn(sb, "        return getAll(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getAll(String TABLENAME2) {", tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getAll(DAO, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getAll(final %sDAO DAO, final String TABLENAME2) {",
				tbUEn, tbUEn);
		sn(sb, "        if(cacheModel == NO_CACHE){");
		sn(sb, "            return DAO.selectAll(TABLENAME2);");
		sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
		sn(sb, "            List<%s> result = getNoSortAll(DAO, TABLENAME2);",
				tbUEn);
		// sn(sb, "            return sort(result);");
		sn(sb, "            return result;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getNoSortAll() {", tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getNoSortAll(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getNoSortAll(%sDAO DAO) {", tbUEn,
				tbUEn);
		sn(sb, "        return getNoSortAll(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getNoSortAll(String TABLENAME2) {",
				tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getNoSortAll(DAO, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getNoSortAll(final %sDAO DAO, final String TABLENAME2) {",
				tbUEn, tbUEn);
		sn(sb, "        if(cacheModel == NO_CACHE){");
		sn(sb, "            return DAO.selectAll(TABLENAME2);");
		sn(sb, "        } else if (cacheModel == STATIC_CACHE) {");
		sn(sb, "            List<%s> result = newList();", tbUEn);
		sn(sb, "            result.addAll(fixedCache);");
		sn(sb, "            return result;");
		sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
		sn(sb, "            List<%s> result = newList();", tbUEn);
		sn(sb, "            result.addAll(vars.values());");
		sn(sb, "            return result;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static Set<%s> memoryKeys(){", priKeyType);
		if (isInt4Pk) {
			sn(sb, "        if (cacheModel == STATIC_CACHE) {");
			sn(sb, "            Set<%s> result = newSet();", priKeyType);
			sn(sb, "            int max = FIXED.length;");
			sn(sb, "            for (int i = 0; i < max; i++) {");
			sn(sb, "                %s %s = FIXED[i];", tbUEn, tbEn);
			sn(sb, "                if (%s != null) result.add((%s)(i + 1));",
					tbEn, pkBType);
			sn(sb, "            }");
			sn(sb, "            return result;");
			sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
			sn(sb, "            return vars.keySet();");
			sn(sb, "        }");
		} else {
			sn(sb, "        return vars.keySet();");
		}

		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static Collection<%s> memoryValues(){", tbUEn);
		sn(sb, "        if (cacheModel == STATIC_CACHE) {");
		sn(sb, "            return fixedCache;");
		sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
		sn(sb, "            return vars.values();");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getAllNotCopy(){", tbUEn);
		sn(sb, "        if (cacheModel == STATIC_CACHE) {");
		sn(sb, "            return fixedCache;");
		sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
		sn(sb, "            return new Vector(vars.values());");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getPks() {", priKeyType);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getPks(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getPks(%sDAO DAO) {", priKeyType,
				tbUEn);
		sn(sb, "        return getPks(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getPks(String TABLENAME2) {",
				priKeyType);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getPks(DAO, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getPks(final %sDAO DAO, final String TABLENAME2) {",
				priKeyType, tbUEn);
		sn(sb, "        if ( cacheModel == NO_CACHE) { ");
		sn(sb, "            return DAO.selectPKs(TABLENAME2);");
		sn(sb, "        } else if (cacheModel == STATIC_CACHE) {");
		sn(sb, "            List<%s> result = newList();", priKeyType);
		sn(sb, "            result.addAll(memoryKeys());");
		sn(sb, "            return result;");
		sn(sb, "        } else {");
		sn(sb, "            List<%s> result = newList();", priKeyType);
		sn(sb, "            result.addAll(vars.keySet());");
		sn(sb, "            return result;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");
		// /

		sn(sb, "    public static List<Map> getInIndex() {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return getInIndex(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<Map> getInIndex(%sDAO DAO) {", tbUEn);
		sn(sb, "        return getInIndex(DAO, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<Map> getInIndex(String TABLENAME2) {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return getInIndex(DAO, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<Map> getInIndex(final %sDAO DAO, final String TABLENAME2) {",
				tbUEn);
		sn(sb, "        return DAO.selectInIndex(TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		if (batch) {
			sn(sb, "    public static List<%s> getIn(List<%s> keys) {", tbUEn,
					priKeyType);
			sn(sb,
					"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
					tbUEn);
			sn(sb, "        return getIn(keys, DAO, DAO.TABLENAME);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static List<%s> getIn(List<%s> keys, %sDAO DAO) {",
					tbUEn, priKeyType, tbUEn);
			sn(sb, "        return getIn(keys, DAO, DAO.TABLENAME);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static List<%s> getIn(List<%s> keys, String TABLENAME2) {",
					tbUEn, priKeyType);
			sn(sb,
					"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
					tbUEn);
			sn(sb, "        return getIn(keys, DAO, TABLENAME2);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static List<%s> getIn(final List<%s> keys, final %sDAO DAO, final String TABLENAME2) {",
					tbUEn, priKeyType, tbUEn);
			sn(sb,
					"        if(keys == null || keys.isEmpty()) return newList();");
			sn(sb, "        if( cacheModel == NO_CACHE ){");
			sn(sb, "            return DAO.selectIn(keys, TABLENAME2);");
			sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
			sn(sb,
					"            List<%s> result = getNoSortIn(keys, DAO, TABLENAME2);",
					tbUEn);
			// sn(sb, "            return sort(result);");
			sn(sb, "            return result;");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");

			sn(sb, "    public static List<%s> getNoSortIn(List<%s> keys) {",
					tbUEn, priKeyType);
			sn(sb,
					"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
					tbUEn);
			sn(sb, "        return getNoSortIn(keys, DAO, DAO.TABLENAME);",
					priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static List<%s> getNoSortIn(List<%s> keys, %sDAO DAO) {",
					tbUEn, priKeyType, tbUEn);
			sn(sb, "        return getNoSortIn(keys, DAO, DAO.TABLENAME);",
					priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static List<%s> getNoSortIn(List<%s> keys, String TABLENAME2) {",
					tbUEn, priKeyType);
			sn(sb,
					"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
					tbUEn);
			sn(sb, "        return getNoSortIn(keys, DAO, TABLENAME2);", priKey);
			sn(sb, "    }");
			sn(sb, "");

			sn(sb,
					"    public static List<%s> getNoSortIn(final List<%s> keys, final %sDAO DAO, final String TABLENAME2) {",
					tbUEn, priKeyType, tbUEn);
			sn(sb,
					"        if(keys == null || keys.isEmpty()) return newList();");
			sn(sb, "        if( cacheModel == NO_CACHE ){");
			sn(sb, "            return DAO.selectIn(keys, TABLENAME2);");
			sn(sb,
					"        } else { // STATIC_CACHE || FULL_CACHE || FULL_MEMORY");
			sn(sb, "            List<%s> result = newList();", tbUEn);
			sn(sb, "            for (%s key : keys) {", pkBType);
			sn(sb, "                %s %s = getByKeyFromMemory(key);", tbUEn,
					tbEn);
			sn(sb, "                if( %s == null ) continue;", tbEn);
			sn(sb, "                result.add(%s);", tbEn);
			sn(sb, "            }");
			sn(sb, "            return result;");
			sn(sb, "        }");
			sn(sb, "    }");
			sn(sb, "");
		}

		sn(sb, "    public static List<%s> getLast(int num) {", tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getLast(DAO, num, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getLast(%sDAO DAO, int num) {",
				tbUEn, tbUEn);
		sn(sb, "        return getLast(DAO, num, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getLast(int num, String TABLENAME2) {",
				tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getLast(DAO, num, TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getLast(final %sDAO DAO, final int num, final String TABLENAME2) {",
				tbUEn, tbUEn);
		sn(sb, "        if( cacheModel == NO_CACHE ){");
		sn(sb, "            return DAO.selectLast(num, TABLENAME2);");
		sn(sb, "        } else { // FULL_CACHE or FULL_MEMORY");
		sn(sb, "            List<%s> result = newList();", tbUEn);
		sn(sb, "            List<%s> mvars = getAll(DAO, TABLENAME2);", tbUEn);
		sn(sb, "            if( mvars.size() > num ){");
		sn(sb,
				"                result = mvars.subList(mvars.size() - num, mvars.size());");
		sn(sb, "            }else{");
		sn(sb, "                result.addAll(mvars);");
		sn(sb, "            }");
		sn(sb, "            return result;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s last() {", tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return last(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s last(%sDAO DAO) {", tbUEn, tbUEn);
		sn(sb, "        return last(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s last(String TABLENAME2) {", tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return last(DAO, TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static %s last(final %sDAO DAO, final String TABLENAME2) {",
				tbUEn, tbUEn);
		sn(sb, "        %s result = null;", tbUEn);
		sn(sb, "        if( cacheModel == NO_CACHE ){");
		sn(sb, "            return DAO.last(TABLENAME2);");
		sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY");
		sn(sb, "            %s id = LASTID.get();", pkBType);
		sn(sb, "            result = getByKey(DAO, id, TABLENAME2);");
		sn(sb, "        }");
		sn(sb, "        return result;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s maxId() {", pkBType);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return maxId(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s maxId(%sDAO DAO) {", pkBType, tbUEn);
		sn(sb, "        return maxId(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s maxId(String TABLENAME2) {", pkBType);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return maxId(DAO, TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static %s maxId(final %sDAO DAO, final String TABLENAME2) {",
				pkBType, tbUEn);
		sn(sb, "        if( cacheModel == NO_CACHE ){");
		sn(sb, "            return DAO.maxId(TABLENAME2);");
		sn(sb, "        }");
		sn(sb, "        // FULL_CACHE || FULL_MEMORY || STATIC_CACHE");
		sn(sb, "        %s id = LASTID.get();", pkBType);
		sn(sb, "        if(id > 0) return id;");
		sn(sb, "        return DAO.maxId(TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getGtKey(%s %s) {", tbUEn, pkBType,
				priKey);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getGtKey(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getGtKey(%sDAO DAO, %s %s) {",
				tbUEn, tbUEn, pkBType, priKey);
		sn(sb, "        return getGtKey(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getGtKey(%s %s, String TABLENAME2) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getGtKey(DAO, %s, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getGtKey(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
				tbUEn, tbUEn, pkBType, priKey);
		sn(sb, "        if( cacheModel == NO_CACHE ){");
		sn(sb, "            return DAO.selectGtKey(%s, TABLENAME2);", priKey);
		sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
		sn(sb, "            List<%s> result = newList();", tbUEn);
		sn(sb, "            List<%s> %ss = getAll();", tbUEn, tbEn);
		sn(sb, "            for (%s %s : %ss) {", tbUEn, tbEn, tbEn);
		sn(sb, "                if(%s.%s <= %s) continue;", tbEn, priKey,
				priKey);
		sn(sb, "                result.add(%s);", tbEn);
		sn(sb, "            }");
		sn(sb, "            return result;");
		sn(sb, "        }");
		// sn(sb, "        sort(result);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s getByKey(%s %s) {", tbUEn, pkBType, priKey);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getByKey(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static Future<%s> asyncGetByKey(final %s %s) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        Future<%s> f = Async.exec(new Callable<%s>() {", tbUEn,
				tbUEn);
		sn(sb, "            public %s call() throws Exception {", tbUEn);
		sn(sb, "                return getByKey(%s);", priKey);
		sn(sb, "            }");
		sn(sb, "        });");
		sn(sb, "        return f;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s getByKey(%sDAO DAO, %s %s) {", tbUEn,
				tbUEn, pkBType, priKey);
		sn(sb, "        return getByKey(DAO, %s, DAO.TABLENAME);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s getByKey(%s %s, String TABLENAME2) {",
				tbUEn, pkBType, priKey);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getByKey(DAO, %s, TABLENAME2);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static %s getByKey(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
				tbUEn, tbUEn, pkBType, priKey);
		sn(sb, "        if(cacheModel == NO_CACHE){");
		sn(sb, "            return DAO.selectByKey(%s, TABLENAME2);", priKey);
		sn(sb, "        }");
		sn(sb, "        return getByKeyFromMemory(%s);", priKey);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s getByKeyFromMemory(final %s %s) {", tbUEn,
				pkBType, priKey);
		if (isInt4Pk) {
			sn(sb, "        if (cacheModel == STATIC_CACHE) {");
			sn(sb,
					"            if (%s < 1 || FIXED == null || FIXED.length < %s) return null;",
					priKey, priKey);
			sn(sb, "            return FIXED[%s - 1];", priKey);
			sn(sb,
					"        } else if (cacheModel == FULL_CACHE || cacheModel == FULL_MEMORY) {");
		} else {
			sn(sb,
					"        if (cacheModel == FULL_CACHE || cacheModel == FULL_MEMORY) {");
		}
		sn(sb, "            return vars.get(%s);", priKey);
		sn(sb, "        }");
		sn(sb, "        return null;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s deleteFromMemory(final %s %s) {", tbUEn,
				pkBType, priKey);
		sn(sb, "        %s %s;", tbUEn, tbEn);
		if (isInt4Pk) {
			sn(sb, "        if (cacheModel == STATIC_CACHE) {");
			sn(sb,
					"            if (%s < 1 || FIXED == null || FIXED.length < %s || FIXED[%s-1]==null) return null;",
					priKey, priKey, priKey);
			sn(sb, "            %s = FIXED[%s - 1];", tbEn, priKey);
			sn(sb, "            FIXED[%s - 1] = null;", priKey);
			sn(sb, "            fixedCache.remove(%s);", tbEn);
			sn(sb, "        } else {");
			sn(sb, "            %s = vars.remove(%s);", tbEn, priKey);
			sn(sb, "        }");
		} else {
			sn(sb, "            %s = vars.remove(%s);", tbEn, priKey);
		}
		sn(sb, "        if(%s == null) return null;", tbEn);
		sn(sb, "");
		{
			Iterator<String> it = indexs.keySet().iterator();
			int p = 0;
			while (it.hasNext()) {
				String key = it.next();
				List<Map<String, Object>> idx = indexs.get(key);
				int idx_size = idx.size();
				if (idx_size == 1) { // 单索引
					Map<String, Object> index = idx.get(0);
					String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
					String COLUMN_NAME = MapEx.get(index, "COLUMN_NAME");
					String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
					String COL_NAME_EN = PinYin.getShortPinYin(COLUMN_NAME);
					String COL_NAME_UEN = StrEx.upperN1(COL_NAME_EN);
					String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
							COLUMN_NAME);
					String bType = JavaType.getBasicType(COLUMN_NAME_TYPE);

					if (INDEX_NAME.equals("PRIMARY"))
						continue;
					if (NON_UNIQUE.equals("false")) { // 唯一数据
						sn(sb, "        %s %s = %s.get%s();", bType,
								COL_NAME_EN, tbEn, COL_NAME_UEN);
						sn(sb, "        varsBy%s.remove(%s);", COL_NAME_UEN,
								COL_NAME_EN);
					} else {
						if (bType.equals("java.util.Date"))
							continue;
						p = p + 1;
						sn(sb, "        %s %s = %s.get%s();", bType,
								COL_NAME_EN, tbEn, COL_NAME_UEN);
						sn(sb, "        Set m%d = varsBy%s.get(%s);", p,
								COL_NAME_UEN, COL_NAME_EN);
						sn(sb, "        if(m%d != null) {", p);
						sn(sb, "            m%d.remove(%s);", p, priKey);
						sn(sb, "            if(m%d.isEmpty())", p);
						sn(sb, "                varsBy%s.remove(%s);",
								COL_NAME_UEN, COL_NAME_EN);
						sn(sb, "        }");
					}
					sn(sb, "");

				} else { // 多索引
					Map<String, Object> index = idx.get(0);
					String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
					String index1 = BeanBuilder.index1(rsmd, idx);
					// String index2 = SqlBeanBuilder.index2(rsmd, idx);
					// String index3 = SqlBeanBuilder.index3(rsmd, idx);
					// String index4 = SqlBeanBuilder.index4(rsmd, idx);
					String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
					if (NON_UNIQUE.equals("false")) { // 唯一数据
						String skey = "com.bowlong.lang.PStr.b().a(";
						sn(sb, "        { // %s", INDEX_NAME);
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							String COL_NAME_EN = PinYin
									.getShortPinYin(COL_NAME);
							String COL_NAME_UEN = StrEx.upperN1(COL_NAME_EN);
							String COL_NAME_TYPE = JavaType.getType(rsmd,
									COL_NAME);
							String bType = JavaType.getBasicType(COL_NAME_TYPE);
							sn(sb, "            %s v%s = %s.get%s();", bType,
									COL_NAME_EN, tbEn, COL_NAME_UEN);
							skey = skey + s("v%s", COL_NAME_EN);
							if (idx.indexOf(map) < idx.size() - 1) {
								skey = skey + ", \"-\", ";
							}
						}
						skey = skey + ").e()";
						sn(sb, "            String vkey = %s;", skey);
						sn(sb, "            varsBy%s.remove(vkey);", index1);
						sn(sb, "        }");
					} else {
						String skey = "com.bowlong.lang.PStr.b().a(";
						p = p + 1;
						sn(sb, "        { // %s", INDEX_NAME);
						for (Map<String, Object> map : idx) {
							String COL_NAME = MapEx.get(map, "COLUMN_NAME");
							String COL_NAME_EN = PinYin
									.getShortPinYin(COL_NAME);
							String COL_NAME_UEN = StrEx.upperN1(COL_NAME_EN);
							String COL_NAME_TYPE = JavaType.getType(rsmd,
									COL_NAME);
							String bType = JavaType.getBasicType(COL_NAME_TYPE);

							sn(sb, "            %s v%s = %s.get%s();", bType,
									COL_NAME_EN, tbEn, COL_NAME_UEN);
							skey = skey + s("v%s", COL_NAME_EN);
							if (idx.indexOf(map) < idx.size() - 1) {
								skey = skey + ", \"-\", ";
							}
						}
						skey = skey + ").e()";
						sn(sb, "            String vkey = %s;", skey);
						sn(sb, "            Set m%d = varsBy%s.get(vkey);", p,
								index1);
						sn(sb, "            if(m%d != null) { ", p);
						sn(sb, "                m%d.remove(%s);", p, priKey);
						sn(sb, "                if(m%d.isEmpty())", p);
						sn(sb, "                    varsBy%s.remove(vkey);",
								index1);
						sn(sb, "            }");
						sn(sb, "        }");
					}
					sn(sb, "");
				}
			}
		}
		sn(sb, "        return %s;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static List<%s> getByPage(int page, int size) {",
				tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getByPage(DAO, page, size, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getByPage(%sDAO DAO, int page, int size) {",
				tbUEn, tbUEn);
		sn(sb, "        return getByPage(DAO, page, size, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getByPage(int page, int size, String TABLENAME2) {",
				tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return getByPage(DAO, page, size, TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static List<%s> getByPage(final %sDAO DAO, final int page, final int size,final String TABLENAME2) {",
				tbUEn, tbUEn);
		sn(sb, "        int begin = page * size;");
		sn(sb, "        int num = size;");
		sn(sb, "        if( cacheModel == NO_CACHE ){");
		sn(sb, "            return DAO.selectByPage(begin, num, TABLENAME2);");
		sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY ");
		sn(sb, "            List<%s> result = newList();", tbUEn);
		sn(sb, "            List<%s> v = getAll(DAO, TABLENAME2);", tbUEn);
		sn(sb, "            result = SqlEx.getPage(v, page, size);");
		sn(sb, "            return result;");
		sn(sb, "        }");
		sn(sb, "    }");
		sn(sb, "");

		// //
		sn(sb, "    public static int pageCount(int size) {", tbUEn);
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return pageCount(DAO, size, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int pageCount(%sDAO DAO, int size) {", tbUEn);
		sn(sb, "        return pageCount(DAO, size, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int pageCount(int size, String TABLENAME2) {");
		sn(sb, "        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
				tbUEn);
		sn(sb, "        return pageCount(DAO, size, TABLENAME2);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static int pageCount(final %sDAO DAO, final int size,final String TABLENAME2) {",
				tbUEn, tbUEn);
		sn(sb, "        int v = 0;");
		sn(sb, "        if( cacheModel == NO_CACHE ){");
		sn(sb, "            v = DAO.count(TABLENAME2);");
		sn(sb, "        }else{");
		sn(sb, "            v = count(DAO, TABLENAME2);");
		sn(sb, "        }");
		sn(sb, "        return SqlEx.pageCount(v, size);");
		sn(sb, "    }");
		sn(sb, "");

		Iterator<String> it = indexs.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			List<Map<String, Object>> idx = indexs.get(key);
			int idx_size = idx.size();
			if (idx_size == 1) { // 单索引
				Map<String, Object> index = idx.get(0);
				String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
				String COLUMN_NAME = MapEx.get(index, "COLUMN_NAME");
				String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
				String COLUMN_NAME_EN = PinYin.getShortPinYin(COLUMN_NAME);
				String COLUMN_NAME_UEN = StrEx.upperN1(COLUMN_NAME_EN);
				String COLUMN_NAME_TYPE = JavaType.getType(rsmd, COLUMN_NAME);
				String COLUMN_NAME_BTYPE = JavaType
						.getBasicType(COLUMN_NAME_TYPE);
				if (INDEX_NAME.equals("PRIMARY"))
					continue;
				if (COLUMN_NAME_TYPE.equals("java.util.Date"))
					continue;
				if (NON_UNIQUE.equals("false")) {
					sn(sb, "    public static %s getBy%s(%s %s) {", tbUEn,
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getBy%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public static %s getBy%s(%sDAO DAO, %s %s) {",
							tbUEn, COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return getBy%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static %s getBy%s(%s %s, String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getBy%s(DAO, %s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static %s getBy%s(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_BTYPE,
							COLUMN_NAME_EN);
					sn(sb, "        if( cacheModel == NO_CACHE ){");
					sn(sb,
							"            return DAO.selectBy%s(%s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY");
					sn(sb, "            %s %s = varsBy%s.get(%s);", priKeyType,
							priKey, COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "            if(%s == null) return null;", priKey);
					sn(sb,
							"            %s result = getByKey(DAO, %s, TABLENAME2);",
							tbUEn, priKey);
					sn(sb, "            if(result == null) return null;");
					if (COLUMN_NAME_BTYPE.equals("String")) {
						sn(sb, "            if(!result.get%s().equals(%s)){",
								COLUMN_NAME_UEN, COLUMN_NAME_EN);
					} else {
						sn(sb, "            if(result.get%s() != %s){",
								COLUMN_NAME_UEN, COLUMN_NAME_EN);
					}

					sn(sb, "                varsBy%s.remove(%s);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "                return null;");
					sn(sb, "            }");
					sn(sb, "            return result;");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");
				} else {
					sn(sb, "    public static int countBy%s(%s %s) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return countBy%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static int countBy%s(%sDAO DAO, %s %s) {",
							COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return countBy%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static int countBy%s(%s %s, String TABLENAME2) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return countBy%s(DAO, %s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static int countBy%s(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
							COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        if(cacheModel == NO_CACHE){");
					sn(sb, "            return DAO.countBy%s(%s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "        }");
					sn(sb,
							"        List<%s> %ss = getBy%s(DAO, %s, TABLENAME2);",
							tbUEn, tbEn, COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "        return %ss.size();", tbEn);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public static List<%s> getBy%s(%s %s) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getBy%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static List<%s> getBy%s(%sDAO DAO, %s %s) {",
							tbUEn, COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return getBy%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static List<%s> getBy%s(%s %s, String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getBy%s(DAO, %s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static List<%s> getBy%s(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        if( cacheModel == NO_CACHE ){");
					sn(sb,
							"            return DAO.selectBy%s(%s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "        } else { //FULL_CACHE || FULL_MEMORY {");
					sn(sb, "            List<%s> result = newList();", tbUEn);
					sn(sb, "            Set<%s> m1 = varsBy%s.get(%s);",
							priKeyType, COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb,
							"            if (m1 == null || m1.isEmpty()) return result;");
					sn(sb, "            List<%s> list = new ArrayList(m1);",
							priKeyType);
					sn(sb, "            for (%s key : list) {;", pkBType);
					sn(sb,
							"                %s e = getByKey(DAO, key, TABLENAME2);",
							tbUEn);
					sn(sb, "                if(e == null){");
					sn(sb, "                    m1.remove(key); ");
					sn(sb, "                    continue;");
					sn(sb, "                }");
					sn(sb, "                %s _%s = e.get%s();",
							COLUMN_NAME_BTYPE, COLUMN_NAME_EN, COLUMN_NAME_UEN);
					if (COLUMN_NAME_BTYPE.equals("int")
							|| COLUMN_NAME_BTYPE.equals("long")
							|| COLUMN_NAME_BTYPE.equals("boolean")) {
						sn(sb, "                if(_%s != %s){ ",
								COLUMN_NAME_EN, COLUMN_NAME_EN);
					} else if (COLUMN_NAME_BTYPE.equals("String")
							|| COLUMN_NAME_BTYPE.equals("java.util.Date")) {
						sn(sb, "                if(!_%s.equals(%s)){ ",
								COLUMN_NAME_EN, COLUMN_NAME_EN);
					}
					sn(sb, "                    m1.remove(key);");
					sn(sb, "                    continue;");
					sn(sb, "                }");
					sn(sb, "                result.add(e);");
					sn(sb, "            }");

					// sn(sb, "            sort(result);");
					sn(sb, "            return result;");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");
				}

				// like
				if (COLUMN_NAME_TYPE.equals("String")) {
					sn(sb, "    public static int countLike%s(%s %s) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb,
							"        return countLike%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static int countLike%s(%sDAO DAO, %s %s) {",
							COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        return countLike%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static int countLike%s(%s %s, String TABLENAME2) {",
							COLUMN_NAME_UEN, COLUMN_NAME_TYPE, COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return countLike%s(DAO, %s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static int countLike%s(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
							COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        if(cacheModel == NO_CACHE){");
					sn(sb,
							"            return DAO.countLike%s(%s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "        }");
					sn(sb,
							"        List<%s> %ss = getLike%s(DAO, %s, TABLENAME2);",
							tbUEn, tbEn, COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "        return %ss.size();", tbEn);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public static List<%s> getLike%s(%s %s) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getLike%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static List<%s> getLike%s(%sDAO DAO, %s %s) {",
							tbUEn, COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        return getLike%s(DAO, %s, DAO.TABLENAME);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static List<%s> getLike%s(%s %s, String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getLike%s(DAO, %s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static List<%s> getLike%s(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
							tbUEn, COLUMN_NAME_UEN, tbUEn, COLUMN_NAME_TYPE,
							COLUMN_NAME_EN);
					sn(sb, "        if(cacheModel == NO_CACHE){");
					sn(sb,
							"            return DAO.selectLike%s(%s, TABLENAME2);",
							COLUMN_NAME_UEN, COLUMN_NAME_EN);
					sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY");
					sn(sb, "            List<%s> result = newList();", tbUEn);
					sn(sb,
							"            List<%s> %ss = getAll(DAO, TABLENAME2);",
							tbUEn, tbEn);
					sn(sb, "            for(%s e : %ss){", tbUEn, tbEn);
					sn(sb, "                %s _var = e.get%s();",
							COLUMN_NAME_TYPE, COLUMN_NAME_UEN);
					sn(sb, "                if(_var.indexOf(%s) >= 0)",
							COLUMN_NAME_EN, priKey);
					sn(sb, "                    result.add(e);");
					sn(sb, "            }");
					// sn(sb, "        sort(result);");
					sn(sb, "            return result;");
					sn(sb, "        }");
					sn(sb, "    }");
					sn(sb, "");
				}
				// //////////////

			} else { // 多键索引
				Map<String, Object> index = idx.get(0);
				// String INDEX_NAME = MapEx.get(index, "INDEX_NAME");
				// String COLUMN_NAME = MapEx.get(index, "COLUMN_NAME");
				String NON_UNIQUE = String.valueOf(index.get("NON_UNIQUE"));
				// String COLUMN_NAME_EN = PinYin.getShortPinYin(COLUMN_NAME);
				// String COLUMN_NAME_UEN = StrEx.upperN1(COLUMN_NAME_EN);
				// String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
				// COLUMN_NAME);
				// String COLUMN_NAME_BTYPE =
				// JavaType.getBasicType(COLUMN_NAME_TYPE);
				String index1 = BeanBuilder.index1(rsmd, idx);
				String index2 = BeanBuilder.index2(rsmd, idx);
				String index3 = BeanBuilder.index3(rsmd, idx);
				// String index4 = BeanBuilder.index4(rsmd, idx);
				String index5 = BeanBuilder.index5(rsmd, idx);
				if (NON_UNIQUE.equals("false")) { // 唯一数据
					sn(sb, "    public static %s getBy%s(%s) {", tbUEn, index1,
							index2);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getBy%s(DAO, %s, DAO.TABLENAME);",
							index1, index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb, "    public static %s getBy%s(%sDAO DAO, %s) {",
							tbUEn, index1, tbUEn, index2);
					sn(sb, "        return getBy%s(DAO, %s, DAO.TABLENAME);",
							index1, index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static %s getBy%s(%s, String TABLENAME2) {",
							tbUEn, index1, index2);
					sn(sb,
							"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
							tbUEn);
					sn(sb, "        return getBy%s(DAO, %s, TABLENAME2);",
							index1, index3);
					sn(sb, "    }");
					sn(sb, "");

					sn(sb,
							"    public static %s getBy%s(final %sDAO DAO, %s,final String TABLENAME2) {",
							tbUEn, index1, tbUEn, index2);
					sn(sb, "        if( cacheModel == NO_CACHE ){");
					sn(sb,
							"            return DAO.selectBy%s(%s, TABLENAME2);",
							index1, index3);
					sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY");
					sn(sb, "            String vkey = %s;", index5);
					sn(sb, "            %s %s = varsBy%s.get(vkey);",
							priKeyType, priKey, index1);
					sn(sb, "            if(%s == null) return null;", priKey);
					sn(sb,
							"            %s result = getByKey(DAO, %s, TABLENAME2);",
							tbUEn, priKey);
					sn(sb, "            if(result == null) return null;");
					{
						for (Map<String, Object> m : idx) {
							// String INDEX_NAME = MapEx.get(m, "INDEX_NAME");
							String COLUMN_NAME = MapEx.get(m, "COLUMN_NAME");
							// String NON_UNIQUE =
							// String.valueOf(index.get("NON_UNIQUE"));
							String COLUMN_NAME_EN = PinYin
									.getShortPinYin(COLUMN_NAME);
							String COLUMN_NAME_UEN = StrEx
									.upperN1(COLUMN_NAME_EN);
							String COLUMN_NAME_TYPE = JavaType.getType(rsmd,
									COLUMN_NAME);
							String COLUMN_NAME_BTYPE = JavaType
									.getBasicType(COLUMN_NAME_TYPE);
							if (COLUMN_NAME_BTYPE.equals("String")) {
								sn(sb,
										"            if(!result.get%s().equals(%s)){",
										COLUMN_NAME_UEN, COLUMN_NAME_EN);
							} else {
								sn(sb, "            if(result.get%s() != %s){",
										COLUMN_NAME_UEN, COLUMN_NAME_EN);
							}
							sn(sb, "                varsBy%s.remove(vkey);",
									index1);
							sn(sb, "                return null;");
							sn(sb, "            }");
						}
					}
					sn(sb, "            return result;");
					sn(sb, "        }");

					sn(sb, "    }");
					sn(sb, "");
				} else { // 非唯一数据
					{
						sn(sb, "    public static int countBy%s(%s) {", index1,
								index2);
						sn(sb,
								"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
								tbUEn);
						sn(sb,
								"        return countBy%s(DAO, %s, DAO.TABLENAME);",
								index1, index3);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public static int countBy%s(%sDAO DAO, %s) {",
								index1, tbUEn, index2);
						sn(sb,
								"        return countBy%s(DAO, %s, DAO.TABLENAME);",
								index1, index3);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public static int countBy%s(%s, String TABLENAME2) {",
								index1, index2);
						sn(sb,
								"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
								tbUEn);
						sn(sb,
								"        return countBy%s(DAO, %s, TABLENAME2);",
								index1, index3);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public static int countBy%s(final %sDAO DAO, %s, final String TABLENAME2) {",
								index1, tbUEn, index2);
						sn(sb, "        if(cacheModel == NO_CACHE){");
						sn(sb,
								"            return DAO.countBy%s(%s, TABLENAME2);",
								index1, index3);
						sn(sb, "        }");
						sn(sb,
								"        List<%s> %ss = getBy%s(%s, TABLENAME2);",
								tbUEn, tbEn, index1, index3);
						sn(sb, "        return %ss.size();", tbEn);
						sn(sb, "    }");
						sn(sb, "");
					}
					{
						sn(sb, "    public static List<%s> getBy%s(%s) {",
								tbUEn, index1, index2);
						sn(sb,
								"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
								tbUEn);
						sn(sb,
								"        return getBy%s(DAO, %s, DAO.TABLENAME);",
								index1, index3);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public static List<%s> getBy%s(%sDAO DAO, %s) {",
								tbUEn, index1, tbUEn, index2);
						sn(sb,
								"        return getBy%s(DAO, %s, DAO.TABLENAME);",
								index1, index3);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public static List<%s> getBy%s(%s, String TABLENAME2) {",
								tbUEn, index1, index2);
						sn(sb,
								"        %sDAO DAO = (cacheModel == NO_CACHE) ? DAO() : null;",
								tbUEn);
						sn(sb, "        return getBy%s(DAO, %s, TABLENAME2);",
								index1, index3);
						sn(sb, "    }");
						sn(sb, "");

						sn(sb,
								"    public static List<%s> getBy%s(final %sDAO DAO, %s, final String TABLENAME2) {",
								tbUEn, index1, tbUEn, index2);
						sn(sb, "        if( cacheModel == NO_CACHE ){");
						sn(sb,
								"            return DAO.selectBy%s(%s, TABLENAME2);",
								index1, index3);
						sn(sb, "        } else { // FULL_CACHE || FULL_MEMORY");
						sn(sb, "            List<%s> result = newList();",
								tbUEn);
						sn(sb, "            String vkey = %s;", index5);
						sn(sb, "            Set<%s> m1 = varsBy%s.get(vkey);",
								priKeyType, index1);
						sn(sb,
								"            if (m1 == null || m1.isEmpty()) return result;");
						sn(sb,
								"            List<%s> list = new ArrayList(m1);",
								priKeyType);
						sn(sb, "            for (%s key : list) {", pkBType);
						sn(sb,
								"                %s e = getByKey(DAO, key, TABLENAME2);",
								tbUEn);
						sn(sb, "                if(e == null){");
						sn(sb, "                    m1.remove(key); ");
						sn(sb, "                    continue;");
						sn(sb, "                }");
						String _skey = "com.bowlong.lang.PStr.b().a(";
						for (Map<String, Object> map : idx) {
							String _COLUMN_NAME = MapEx.get(map, "COLUMN_NAME");
							String _COLUMN_NAME_EN = PinYin
									.getShortPinYin(_COLUMN_NAME);
							String _COLUMN_NAME_UEN = StrEx
									.upperN1(_COLUMN_NAME_EN);
							String _COLUMN_NAME_TYPE = JavaType.getType(rsmd,
									_COLUMN_NAME);
							String _basicType = JavaType
									.getBasicType(_COLUMN_NAME_TYPE);

							sn(sb, "                %s _%s = e.get%s();",
									_basicType, _COLUMN_NAME_EN,
									_COLUMN_NAME_UEN);
							_skey = _skey + s("_%s", _COLUMN_NAME_EN);
							if (idx.indexOf(map) < idx.size() - 1) {
								_skey = _skey + ", \"-\", ";
							}
						}
						_skey = _skey + ").e()";
						sn(sb, "                String _key = %s;", _skey);
						sn(sb, "                if(!_key.equals(vkey)){");
						sn(sb, "                    m1.remove(key);");
						sn(sb, "                    continue;");
						sn(sb, "                }");
						sn(sb, "                result.add(e);");
						sn(sb, "            }");
						// sn(sb, "            sort(result);");
						sn(sb, "            return result;");
						sn(sb, "        }");
						sn(sb, "    }");
						sn(sb, "");
					}

				}
			}

		}

		sn(sb, "    public static %s update(%s %s) {", tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return update(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s update(%sDAO DAO, %s %s) {", tbUEn, tbUEn,
				tbUEn, tbEn);
		sn(sb, "        return update(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s update(%s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return update(DAO, %s, TABLENAME2);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static %s update(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
				tbUEn, tbUEn, tbUEn, tbEn);
		sn(sb, "        if(cacheModel != NO_CACHE){");
		sn(sb, "            put(%s, false);", tbEn);
		sn(sb, "        }");
		sn(sb, "        if(cacheModel != FULL_MEMORY){");
		sn(sb, "            int n = DAO.updateByKey(%s, TABLENAME2);", tbEn);
		sn(sb, "            if(n == -1) ");
		sn(sb, "                return %s;", tbEn);
		sn(sb, "            else if(n <= 0) ");
		sn(sb, "                return null;");
		sn(sb, "        }");
		sn(sb, "        return %s;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		// //
		sn(sb, "    public static %s asyncUpdate(%s %s) {", tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return asyncUpdate(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s asyncUpdate(%sDAO DAO, %s %s) {", tbUEn,
				tbUEn, tbUEn, tbEn);
		sn(sb, "        return asyncUpdate(DAO, %s, DAO.TABLENAME);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static %s asyncUpdate(%s %s, String TABLENAME2) {",
				tbUEn, tbUEn, tbEn);
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return asyncUpdate(DAO, %s, TABLENAME2);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static %s asyncUpdate(final %sDAO DAO, final %s %s,final String TABLENAME2) {",
				tbUEn, tbUEn, tbUEn, tbEn);
		sn(sb, "        if(cacheModel != NO_CACHE){");
		sn(sb, "            put(%s, false);", tbEn);
		sn(sb, "        }");
		sn(sb, "        if(cacheModel != FULL_MEMORY){");
		sn(sb, "            DAO.asyncUpdate(%s, TABLENAME2);", tbEn);
		sn(sb, "        }");
		sn(sb, "        return %s;", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		// //

		sn(sb, "    public static void truncate(){");
		sn(sb, "        clear();");
		sn(sb, "        DAO().truncate();");
		sn(sb, "        DAO().repair();");
		sn(sb, "        DAO().optimize();");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int inMemFlush() {");
		sn(sb, "        %sDAO DAO = DAO();", tbUEn);
		sn(sb, "        return inMemFlush(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int inMemFlush(%sDAO DAO){", tbUEn);
		sn(sb, "        return inMemFlush(DAO, DAO.TABLENAME);");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "    public static int inMemFlush(String TABLENAME2) {", tbUEn);
		sn(sb, "        return inMemFlush(DAO(), TABLENAME2);", tbEn);
		sn(sb, "    }");
		sn(sb, "");

		sn(sb,
				"    public static int inMemFlush(final %sDAO DAO, final String TABLENAME2) {",
				tbUEn);
		sn(sb, "        int result = 0;");
		sn(sb, "        if(cacheModel != FULL_MEMORY)");
		sn(sb, "            return result;");
		sn(sb, "        try {");
		sn(sb, "            DAO.truncate(TABLENAME2);");
		sn(sb, "            DAO.repair(TABLENAME2);");
		sn(sb, "            DAO.optimize(TABLENAME2);");
		sn(sb, "        } catch (Exception e) {");
		sn(sb, "            log.info(e2s(e));");
		sn(sb, "        }");
		sn(sb, "");
		sn(sb, "        List<%s> %ss = getAll();", tbUEn, tbEn);
		sn(sb, "        for (%s %s : %ss) {", tbUEn, tbEn, tbEn);
		sn(sb, "            int n = DAO.insert2(%s, TABLENAME2);", tbEn);
		sn(sb, "            if (n > 0) result++;");
		sn(sb, "        }");
		sn(sb, "        return result;");
		sn(sb, "    }");
		sn(sb, "");

		sn(sb, "}");

		return sb.toString();
	}
}
