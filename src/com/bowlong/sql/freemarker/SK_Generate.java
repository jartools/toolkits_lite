package com.bowlong.sql.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sql.DataSource;

import com.bowlong.io.FileRw;
import com.bowlong.lang.StrEx;
import com.bowlong.sql.freemarker.decode.SK_JdbcType;
import com.bowlong.sql.freemarker.decode.SK_MysqlType;
import com.bowlong.sql.freemarker.decode.SK_SqlTypeDecode;
import com.bowlong.sql.freemarker.template.DTP;
import com.bowlong.util.ListEx;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @UserName : SandKing
 * @DataTime : 2013年11月27日 下午5:01:39
 * @Description ：生成器入口
 */
@SuppressWarnings("rawtypes")
public class SK_Generate {
	private static Configuration cfg;
	private static SK_Database db;
	public static final Map<String, String> packageMap = new HashMap<String, String>();

	public static Configuration getConfiguration() {
		if (cfg == null) {
			cfg = new Configuration();
			cfg.setClassForTemplateLoading(DTP.class, "");
			cfg.setObjectWrapper(new DefaultObjectWrapper());
		}
		return cfg;
	}

	public static Configuration getConfiguration(String path) {
		if (cfg == null) {
			try {
				cfg = new Configuration();
				cfg.setDirectoryForTemplateLoading(FileRw.getFile(path));
				cfg.setObjectWrapper(new BeansWrapper());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cfg;
	}

	/**
	 * 根据JDBC的java.sql.Types来解析数据类型
	 * 
	 * @param ds
	 *            数据源
	 * @param config
	 *            源类
	 * @param path
	 *            生成代码路径
	 * @param cfgPath
	 *            模版Freemarker的模版源路径
	 * @param isConfig
	 *            是否配置文件数据[true是，false不是]
	 * @throws Exception
	 */
	public static void run(DataSource ds, Class config, String path,
			String cfgPath, boolean isConfig) throws Exception {
		run(ds, config, path, cfgPath, new SK_JdbcType(), isConfig);
	}

	/**
	 * Mysql数据类型解码器
	 * 
	 * @param ds
	 *            数据源
	 * @param config
	 *            源类
	 * @param path
	 *            生成代码路径
	 * @param cfgPath
	 *            模版Freemarker的模版源路径
	 * @param isConfig
	 *            是否配置文件数据[true是，false不是]
	 * @throws Exception
	 */
	public static void runByMySql(DataSource ds, Class config, String path,
			String cfgPath, boolean isConfig) throws Exception {
		run(ds, config, path, cfgPath, new SK_MysqlType(), isConfig);
	}

	public static void run(DataSource ds, Class config, String path,
			String cfgPath, SK_SqlTypeDecode sqlDecode, boolean isConfig)
			throws Exception {
		if (ds == null)
			return;
		run(ds.getConnection(), config, path, cfgPath, sqlDecode, isConfig);
	}

	public static void run(Connection conn, Class config, String path,
			String cfgPath, boolean isConfig) {
		run(conn, config, path, cfgPath, new SK_JdbcType(), isConfig);
	}

	public static void run(Connection conn, Class config, String path,
			String cfgPath, SK_SqlTypeDecode sqlDecode, boolean isConfig) {

		if (cfgPath == null || "".equals(cfgPath.trim()))
			cfg = getConfiguration();
		else
			cfg = getConfiguration(cfgPath);

		examinePackage(path, isConfig);
		db = new SK_Database(conn, sqlDecode, config);
		loadDtaabase(isConfig, path);
	}

	// ================ 加载部分表的数据
	public static void run(DataSource ds, Class config, String path,
			String cfgPath, boolean isConfig, String... tableName)
			throws Exception {
		run(ds, config, path, cfgPath, new SK_JdbcType(), isConfig, tableName);
	}

	public static void run(DataSource ds, Class config, String path,
			String cfgPath, SK_SqlTypeDecode sqlDecode, boolean isConfig,
			String... tableName) throws Exception {
		if (ds == null)
			return;
		run(ds.getConnection(), config, path, cfgPath, sqlDecode, isConfig,
				tableName);
	}

	public static void run(Connection conn, Class config, String path,
			String cfgPath, boolean isConfig, String... tableName) {
		run(conn, config, path, cfgPath, new SK_JdbcType(), isConfig, tableName);
	}

	public static void run(Connection conn, Class config, String path,
			String cfgPath, SK_SqlTypeDecode sqlDecode, boolean isConfig,
			String... tableName) {

		if (cfgPath == null || "".equals(cfgPath.trim()))
			cfg = getConfiguration();
		else
			cfg = getConfiguration(cfgPath);

		examinePackage(path, isConfig);
		db = new SK_Database(conn, sqlDecode, config);
		loadDtaabase(isConfig, path, tableName);
	}

	/**
	 * 检查包是否存在，不存在则创建
	 * 
	 * @param path
	 */
	private static void examinePackage(String path, boolean isConfig) {
		File file = FileRw.getDire(path);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdir();
		}

		packageMap.put("path", path);

		if (!isConfig) {
			packageMap.put("bean", path + "/bean");
			packageMap.put("dao", path + "/dao");
			packageMap.put("cache", path + "/cache");
			packageMap.put("jedis", path + "/jedis");
			packageMap.put("imp", path + "/imp");
		} else {
			packageMap.put("cfg", path + "/cfg");
		}
		for (Map.Entry<String, String> packageVal : packageMap.entrySet()) {
			file = FileRw.getDire(packageVal.getValue());
			if (!file.exists() || !file.isDirectory()) {
				file.mkdir();
			}
		}
	}

	/**
	 * 生成全部表
	 * 
	 * @param tableName
	 */
	private static void loadDtaabase(boolean isConfig, String path,
			List<SK_ITable> tables) {
		if (ListEx.isEmpty(tables))
			return;

		for (SK_ITable item : tables) {
			String fileName = item.getD_tableName();
			if (!isConfig) {
				writerBean(fileName, cfg, item);
				writerCache(fileName, cfg, item);
				writerJedis(fileName, cfg, item);
				writerDao(fileName, cfg, item);
				writerImp(fileName, cfg, item);
			} else {
				writerCfg(fileName, cfg, item);
			}
		}
		if (isConfig) {
			writerCfgLoad(path, cfg, tables);
		} else {
			writerCfgDBCache(path, cfg, tables);
			writerDbSave(path, cfg, tables);
		}
		completeInfo();
	}

	/**
	 * 生成全部表
	 * 
	 * @param tableName
	 */
	private static void loadDtaabase(boolean isConfig, String path) {
		if (db == null)
			return;
		List<SK_ITable> tables = db.getTables(isConfig);
		loadDtaabase(isConfig, path, tables);
	}

	/**
	 * 生成指定表
	 * 
	 * @param tableName
	 */
	private static void loadDtaabase(boolean isConfig, String path,
			String... tableName) {
		if (db == null)
			return;
		List<SK_ITable> tables = db.getTables(isConfig);
		if (ListEx.isEmpty(tables))
			return;

		List<SK_ITable> tabs = new CopyOnWriteArrayList<SK_ITable>();

		for (SK_ITable item : tables) {
			String fileName = item.getTableName();
			for (String tname : tableName) {
				if (StrEx.isEmpty(tname))
					continue;
				if (tname.equalsIgnoreCase(fileName)) {
					tabs.add(item);
				}
			}
		}

		loadDtaabase(isConfig, path, tabs);
	}

	private static void writerBean(String fileName, Configuration cfg,
			SK_ITable table) {
		String filePath = packageMap.get("bean") + "/" + fileName + ".java";
		String packageName = packageMap.get("bean").replace("src/", "")
				.replace("/", ".");
		table.setPackageName(packageName);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("pojo.ftl");
			fileWriter(template, file, table);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerDao(String fileName, Configuration cfg,
			SK_ITable table) {
		String filePath = packageMap.get("dao") + "/" + fileName + "Dao.java";
		String packageName = packageMap.get("dao").replace("src/", "")
				.replace("/", ".");
		table.setPackageName(packageName);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("dao.ftl");
			fileWriter(template, file, table);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerJedis(String fileName, Configuration cfg,
			SK_ITable table) {
		String filePath = packageMap.get("jedis") + "/" + fileName
				+ "Jedis.java";
		String packageName = packageMap.get("jedis").replace("src/", "")
				.replace("/", ".");
		table.setPackageName(packageName);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("jedis.ftl");
			fileWriter(template, file, table);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerCache(String fileName, Configuration cfg,
			SK_ITable table) {
		String filePath = packageMap.get("cache") + "/" + fileName
				+ "Cache.java";
		String packageName = packageMap.get("cache").replace("src/", "")
				.replace("/", ".");
		table.setPackageName(packageName);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("cache.ftl");
			fileWriter(template, file, table);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerImp(String fileName, Configuration cfg,
			SK_ITable table) {
		String filePath = packageMap.get("imp") + "/" + fileName + "Imp.java";
		String packageName = packageMap.get("imp").replace("src/", "")
				.replace("/", ".");
		table.setPackageName(packageName);
		// 此处特殊，不用重写Imp类
		File file = new File(filePath);
		// 扩展类已存在就不在重写
		if (file.exists()) {
			return;
		}

		file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("imp.ftl");
			fileWriter(template, file, table);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerCfg(String fileName, Configuration cfg,
			SK_ITable table) {
		String filePath = packageMap.get("cfg") + "/" + fileName + "Cfg.java";
		String packageName = packageMap.get("cfg").replace("src/", "")
				.replace("/", ".");
		table.setPackageName(packageName);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("cfg.ftl");
			fileWriter(template, file, table);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerCfgLoad(String path, Configuration cfg,
			List<SK_ITable> tables) {
		String filePath = path + "/ConfigLoad.java";
		String packageName = path.replace("src/", "").replace("/", ".");
		SK_Cfg skCfg = new SK_Cfg("ConfigLoad", packageName, tables);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("cfgLoad.ftl");
			fileWriter(template, file, skCfg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerDbSave(String path, Configuration cfg,
			List<SK_ITable> tables) {
		String filePath = path + "/DbSave.java";
		String packageName = path.replace("src/", "").replace("/", ".");
		SK_Cfg skCfg = new SK_Cfg("DbSave", packageName, tables);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("dbSave.ftl");
			fileWriter(template, file, skCfg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writerCfgDBCache(String path, Configuration cfg,
			List<SK_ITable> tables) {
		String filePath = path + "/CfgDbCache.java";
		String packageName = path.replace("src/", "").replace("/", ".");
		SK_Cfg skCfg = new SK_Cfg("CfgDbCache", packageName, tables);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("cfgDbCache.ftl");
			fileWriter(template, file, skCfg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void fileWriter(Template template, File file, Object table) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			template.process(table, fileWriter);
			fileWriter.flush();
			// 打印输出
			Writer writer = new OutputStreamWriter(System.out);
			template.process(table, writer);
			writer.flush();
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void completeInfo() {
		System.out.println("*********************************************");
		System.out.println("*                                           *");
		System.out.println("**********代码生成完成-请刷新项目！**********");
		System.out.println("*                                           *");
		System.out.println("*********************************************");
	}

	// ========================== 新增添加 redis 缓存的清除
	public static void runJedisCacheClear(DataSource ds, Class config,
			String path, String cfgPath, String... tableName) throws Exception {
		runJedisCacheClear(ds, config, path, cfgPath, new SK_JdbcType(),
				tableName);
	}

	public static void runJedisCacheClear(DataSource ds, Class config,
			String path, String cfgPath, SK_SqlTypeDecode sqlDecode,
			String... tableName) throws Exception {
		if (ds == null)
			return;
		Connection conn = ds.getConnection();
		runJedisCacheClear(conn, config, path, cfgPath, sqlDecode, tableName);
	}

	public static void runJedisCacheClear(Connection conn, Class config,
			String path, String cfgPath, String... tableName) {
		runJedisCacheClear(conn, config, path, cfgPath, new SK_JdbcType(),
				tableName);
	}

	public static void runJedisCacheClear(Connection conn, Class config,
			String path, String cfgPath, SK_SqlTypeDecode sqlDecode,
			String... tableName) {

		if (cfgPath == null || "".equals(cfgPath.trim()))
			cfg = getConfiguration();
		else
			cfg = getConfiguration(cfgPath);

		boolean isConfig = false;

		examinePackage(path, isConfig);
		db = new SK_Database(conn, sqlDecode, config);
		jedisCacheClear(isConfig, path, tableName);
	}

	static private void jedisCacheClear(boolean isConfig, String path,
			String... tableName) {
		if (db == null)
			return;
		List<SK_ITable> tables = db.getTables(isConfig);
		if (ListEx.isEmpty(tables))
			return;

		List<SK_ITable> tabs = new CopyOnWriteArrayList<SK_ITable>();

		for (SK_ITable item : tables) {
			String fileName = item.getTableName();
			for (String tname : tableName) {
				if (StrEx.isEmpty(tname))
					continue;
				if (tname.equalsIgnoreCase(fileName)) {
					tabs.add(item);
				}
			}
		}

		jedisCacheClear(path, tabs);
	}

	private static void jedisCacheClear(String path, List<SK_ITable> tables) {
		if (ListEx.isEmpty(tables))
			return;

		for (SK_ITable item : tables) {
			String fileName = item.getD_tableName();
			writerCacheClear(fileName, cfg, item);
		}
		completeInfo();
	}

	private static void writerCacheClear(String fileName, Configuration cfg,
			SK_ITable table) {
		String filePath = packageMap.get("cache") + "/" + fileName
				+ "Cache.java";
		String packageName = packageMap.get("cache").replace("src/", "")
				.replace("/", ".");
		table.setPackageName(packageName);
		File file = FileRw.getFile(filePath);
		try {
			Template template = cfg.getTemplate("clearCache.ftl");
			fileWriter(template, file, table);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
