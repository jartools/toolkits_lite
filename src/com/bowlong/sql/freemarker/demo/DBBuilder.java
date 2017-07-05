package com.bowlong.sql.freemarker.demo;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.bowlong.sql.freemarker.SK_Generate;
import com.bowlong.sql.freemarker.decode.SK_MysqlType;

@SuppressWarnings("rawtypes")
public class DBBuilder {
	public static void main(String[] args) throws Exception {
		buildEntityByDesign();
		// buildEntityByCfg();
		System.exit(1);
	}

	static void buildEntityByDesign() throws Exception {
		String path = "src/com/gb/db";// 实体保存的位置
		String cfgPath = ""; // "src/com/monster/metadata/template/"
		DataSource ds = new DruidDataSource(); // AppContext.dsDesign();
		Class clazz = DBBuilder.class;// AppContext.class;// 取得连接的getConnection
		SK_Generate.runByMySql(ds, clazz, path, cfgPath, false);
		SK_Generate.runJedisCacheClear(ds, clazz, path, cfgPath,
				new SK_MysqlType(), "player");
	}

	static void buildEntityByCfg() throws Exception {
		DataSource dsCfg = new DruidDataSource(); // AppContext.dsDesign();
		Class clazz = DBBuilder.class;// AppContext.class;// 取得连接的getConnection
		SK_Generate.runByMySql(dsCfg, clazz, "src/com/gb/db", "", true);
	}
}
