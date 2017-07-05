package com.bowlong.sql.freemarker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.io.FileUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.alibaba.fastjson.JSON;
import com.bowlong.bio2.B2InputStream;
import com.bowlong.bio2.B2OutputStream;
import com.bowlong.lang.PStr;
import com.bowlong.sql.AtomicInt;
import com.bowlong.sql.freemarker.jdbc.SK_Query;
import com.bowlong.third.redis.JedisTookits;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;
import com.bowlong.util.NewListLock;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月24日 下午6:28:21
 * @Description ：数据库表对象
 */
public class SK_Table extends SK_ITable {
	public SK_Table(String tableName, String d_tableName, String x_tableName,
			String packageName, String all_basicType_x_columnName,
			String all_columnName, String all_columnNameSign,
			String all_objAndGetD_columnName, String all_columnName_Sign,
			String primary_columnName, String primaryD_columnName,
			String primaryX_columnName, String primaryClassType,
			String primaryBasicType, List<String> all_objAndGetD_columnNames,
			List<SK_Column> columns, List<SK_BindKey> bindKeys,
			List<SK_Index> indexKeys, SK_Database db, String config,
			int columnSize, boolean isCfg) {
		super(tableName, d_tableName, x_tableName, packageName,
				all_basicType_x_columnName, all_columnName, all_columnNameSign,
				all_objAndGetD_columnName, all_columnName_Sign,
				primary_columnName, primaryD_columnName, primaryX_columnName,
				primaryClassType, primaryBasicType, all_objAndGetD_columnNames,
				columns, bindKeys, indexKeys, db, config, columnSize, isCfg);
	}

	@Override
	public Set<String> getBeanImports() {
		if (ListEx.isEmpty(beanImports)) {
			if (beanImports == null)
				beanImports = new HashSet<String>();

			List<SK_BindKey> bindKeys = getBindKeys();
			for (SK_BindKey sk_BindKey : bindKeys) {
				if (sk_BindKey.isPk()) {
					String packageName = SK_Generate.packageMap.get("cache")
							.replace("src/", "").replace("/", ".");
					packageName += ("." + sk_BindKey.getD_pkTableName() + "Cache");
					beanImports.add(packageName);

				} else {
					String packageName = SK_Generate.packageMap.get("cache")
							.replace("src/", "").replace("/", ".");
					packageName += ("." + sk_BindKey.getD_fkTableName() + "Cache");
					beanImports.add(packageName);
					if (!sk_BindKey.isUnique()) {
						// beanImports.add(List.class.getName());
					}
				}
			}
			String packageName = SK_Generate.packageMap.get("cache")
					.replace("src/", "").replace("/", ".");
			packageName += ("." + getD_tableName() + "Cache");
			beanImports.add(packageName);
			// list ,ArrayList, map,Hashmap,Date 均为java.util.*
			beanImports.add("java.util.*");
			// beanImports.add(List.class.getName());
			// beanImports.add(ArrayList.class.getName());
			// beanImports.add(Map.class.getName());
			// beanImports.add(HashMap.class.getName());
			// beanImports.add(Date.class.getName());

			// beanImports.add(ByteArrayOutputStream.class.getName());
			// beanImports.add(ByteArrayInputStream.class.getName());
			// beanImports.add(B2InputStream.class.getName());
			// beanImports.add(B2OutputStream.class.getName());

			beanImports.add(MapEx.class.getName());
			beanImports.add(JSON.class.getName());
			// 用于加锁ReentrantReadWriteLock
			beanImports
					.add("java.util.concurrent.locks.ReentrantReadWriteLock");
			beanImports
					.add("java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock");
			beanImports
					.add("java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock");
		}
		return beanImports;
	}

	public Set<String> getCacheImports() {
		if (ListEx.isEmpty(cacheImports)) {
			if (cacheImports == null)
				cacheImports = new HashSet<String>();

			String packageName = SK_Generate.packageMap.get("bean")
					.replace("src/", "").replace("/", ".");
			packageName += ("." + getD_tableName());
			cacheImports.add(packageName);

			packageName = SK_Generate.packageMap.get("jedis")
					.replace("src/", "").replace("/", ".");
			packageName += ("." + getD_tableName() + "Jedis");
			cacheImports.add(packageName);

			packageName = SK_Generate.packageMap.get("dao").replace("src/", "")
					.replace("/", ".");
			packageName += ("." + getD_tableName() + "Dao");
			cacheImports.add(packageName);

			packageName = SK_Generate.packageMap.get("path")
					.replace("src/", "").replace("/", ".");
			packageName += ".CfgDbCache";
			cacheImports.add(packageName);

			List<SK_Index> indexKeys = getIndexKeys();
			for (SK_Index sk_Index : indexKeys) {
				if (!sk_Index.isUnique()) {
					// cacheImports.add(Set.class.getName());
					// cacheImports.add(HashSet.class.getName());
					cacheImports.add(ConcurrentHashMap.class.getName());
				} else {
					if (!"PRIMARY".equals(sk_Index.getIndexName())) {
						cacheImports.add(ConcurrentHashMap.class.getName());
					}
				}
			}

			cacheImports.add("java.util.*");
			// cacheImports.add(List.class.getName());
			// cacheImports.add(ArrayList.class.getName());
			// cacheImports.add(Map.class.getName());
			// cacheImports.add(Date.class.getName());

			cacheImports.add(ListEx.class.getName());
			cacheImports.add(MapEx.class.getName());
			cacheImports.add(PStr.class.getName());
			cacheImports.add(AtomicInt.class.getName());
			// cacheImports.add(CopyOnWriteArrayList.class.getName());

			// 用于加锁ReentrantReadWriteLock
			cacheImports.add(ConcurrentHashMap.class.getName());
			cacheImports.add(NewListLock.class.getName());
		}
		return cacheImports;
	}

	@Override
	public Set<String> getJedisImports() {
		if (ListEx.isEmpty(jedisImports)) {
			if (jedisImports == null)
				jedisImports = new HashSet<String>();

			String packageName = SK_Generate.packageMap.get("bean")
					.replace("src/", "").replace("/", ".");
			packageName += ("." + getD_tableName());
			jedisImports.add(packageName);

			// 加载 dao
			// packageName = SK_Generate.packageMap.get("dao").replace("src/",
			// "")
			// .replace("/", ".");
			// packageName += ("." + getD_tableName() + "Dao");

			jedisImports.add(packageName);
			List<SK_Index> indexKeys = getIndexKeys();
			for (SK_Index sk_Index : indexKeys) {
				if (!sk_Index.isUnique()) {
					// jedisImports.add(Set.class.getName());
				}
			}

			jedisImports.add("java.util.*");
			// jedisImports.add(ArrayList.class.getName());
			// jedisImports.add(List.class.getName());
			// jedisImports.add(Date.class.getName());

			jedisImports.add(ListEx.class.getName());
			jedisImports.add(Pipeline.class.getName());
			jedisImports.add(Jedis.class.getName());
			jedisImports.add(JedisTookits.class.getName());
			jedisImports.add(PStr.class.getName());
		}
		return jedisImports;
	}

	public Set<String> getDaoImports() {
		if (ListEx.isEmpty(daoImports)) {
			if (daoImports == null)
				daoImports = new HashSet<String>();

			String packageName = SK_Generate.packageMap.get("bean")
					.replace("src/", "").replace("/", ".");
			packageName += ("." + getD_tableName());
			daoImports.add(packageName);
			// 添加List包名称
			List<SK_Index> indexKeys = getIndexKeys();
			for (SK_Index sk_Index : indexKeys) {
				if (!sk_Index.isUnique()) {
					// daoImports.add(List.class.getName());
				}
			}
			List<SK_Column> columns = getColumns();
			boolean autoincrement = false;
			for (SK_Column sk_Column : columns) {
				if (sk_Column.getX_columnName()
						.equals(getPrimaryX_columnName())
						&& sk_Column.isAutoincrement()) {
					autoincrement = true;
				}
			}
			if (autoincrement) {
				daoImports.add(SK_Query.class.getName());
			}

			daoImports.add("java.util.*");
			// daoImports.add(Map.class.getName());
			// daoImports.add(List.class.getName());
			// daoImports.add(Date.class.getName());

			daoImports.add(MapListHandler.class.getName());
			daoImports.add(QueryRunner.class.getName());
			daoImports.add(MapHandler.class.getName());
			daoImports.add(DataSource.class.getName());
			daoImports.add(Connection.class.getName());
			daoImports.add(PStr.class.getName());
			daoImports.add(getDb().getConfig().getName());
			daoImports.add(DbUtils.class.getName());
		}
		return daoImports;
	}

	public Set<String> getImpImports() {
		if (ListEx.isEmpty(impImports)) {
			if (impImports == null)
				impImports = new HashSet<String>();

			String packageName = SK_Generate.packageMap.get("cache")
					.replace("src/", "").replace("/", ".");
			packageName += ("." + getD_tableName() + "Cache");
			this.impImports.add(packageName);
		}
		return this.impImports;
	}

	public Set<String> getCfgImports() {
		if (ListEx.isEmpty(cfgImports)) {
			if (cfgImports == null)
				cfgImports = new HashSet<String>();

			cfgImports.add("java.util.*");
			// cfgImports.add(Date.class.getName());
			// cfgImports.add(ArrayList.class.getName());
			// cfgImports.add(List.class.getName());
			// cfgImports.add(Map.class.getName());

			cfgImports.add(DataSource.class.getName());
			cfgImports.add(Connection.class.getName());
			cfgImports.add(getDb().getConfig().getName());
			cfgImports.add(QueryRunner.class.getName());
			cfgImports.add(MapListHandler.class.getName());
			cfgImports.add(DbUtils.class.getName());
			cfgImports.add(ByteArrayOutputStream.class.getName());
			cfgImports.add(ByteArrayInputStream.class.getName());

			cfgImports.add(B2OutputStream.class.getName());
			cfgImports.add(B2InputStream.class.getName());

			cfgImports.add(FileUtils.class.getName());
			cfgImports.add(File.class.getName());
			cfgImports.add(Exception.class.getName());
			cfgImports.add(MapEx.class.getName());
			cfgImports.add(PStr.class.getName());
			cfgImports.add(ListEx.class.getName());

			List<SK_Index> indexKeys = getIndexKeys();
			for (SK_Index sk_Index : indexKeys) {
				if (!sk_Index.isUnique()) {
					// cfgImports.add(Set.class.getName());
					// cfgImports.add(HashSet.class.getName());
				} else {
					if (!"PRIMARY".equals(sk_Index.getIndexName())) {
						cfgImports.add(ConcurrentHashMap.class.getName());
					}
				}
			}
		}
		return this.cfgImports;
	}
}
