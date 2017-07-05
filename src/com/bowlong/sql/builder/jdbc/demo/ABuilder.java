package com.bowlong.sql.builder.jdbc.demo;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.bowlong.io.FileEx;
import com.bowlong.io.FileRw;
import com.bowlong.lang.StrEx;
import com.bowlong.pinyin.PinYin;
import com.bowlong.sql.builder.jdbc.mysql.BeanBuilder;
import com.bowlong.sql.builder.jdbc.mysql.DaoBuilder;
import com.bowlong.sql.builder.jdbc.mysql.EntityBuilder;
import com.bowlong.sql.builder.jdbc.mysql.InternalBuilder;
import com.bowlong.sql.builder.jdbc.mysql.RMIBuilder;
import com.bowlong.sql.builder.jdbc.mysql.RemoteBuilder;
import com.bowlong.util.ListEx;

public class ABuilder {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		// 该ds就是设计数据库的的source源
		DataSource ds = new DruidDataSource();// AppContext.ds2();
		String pkg = ABuilder.class.getPackage().getName();
		// pkg = "com.war.db";
		System.out.println(pkg);
		List<String> tbs = getTables(ds);

		List<String> tb = ListEx.toVector(tbs);
		boolean src = FileEx.exists("src");
		String appcontext = "";// AppContext.class.getName();
		String clazzName4AppCont = "AppContext";
		System.out.println(appcontext);
		boolean batch = true;

		for (String tablename : tb) {
			System.out.println("====================");
			System.out.println(tablename);
			BeanBuild(ds.getConnection(), tablename, pkg, src);
			DaoBuild(ds.getConnection(), tablename, pkg, src, batch);
			InternalBuild(ds.getConnection(), tablename, appcontext, pkg, src,
					batch, clazzName4AppCont);
			EntityBuild(ds.getConnection(), tablename, appcontext, pkg, src,
					clazzName4AppCont);
			// RMIBuild(ds.getConnection(), tablename, appcontext, pkg, src,
			// batch);
			// RemoteBuild(ds.getConnection(), tablename, appcontext, pkg, src,
			// batch);
		}
		System.exit(1);
	}

	static public List<String> getTables(DataSource ds) throws Exception {
		String[] tbstrs = new String[] { "aduser" };
		List<String> tbs = ListEx.toList(tbstrs);
		tbs = com.bowlong.sql.SqlEx.getTables(ds);
		return tbs;
	}

	public static void BeanBuild(Connection conn, String tablename, String pkg,
			boolean src) throws Exception {

		String sql = String.format("SELECT * FROM `%s` LIMIT 1", tablename);

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		// BeanBuilder builder = new BeanBuilder();
		// String xml = builder.build(rs, pkg + "bean", true);
		String xml = BeanBuilder.build(conn, rs, pkg);
		System.out.println(xml);
		String filename = file(pkg, src, "bean", tablename, "java");
		File f = new File(filename);
		File parent = f.getParentFile();
		if (!parent.exists() || !parent.isDirectory()) {
			parent.mkdir();
		}
		writeFile(filename, xml);
		conn.close();
	}

	public static void DaoBuild(Connection conn, String tablename, String pkg,
			boolean src, boolean batch) throws Exception {

		String sql = String.format("SELECT * FROM `%s` LIMIT 1", tablename);

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		// DaoBuilder builder = new DaoBuilder();
		// String xml = builder.build(conn, rs, pkg + "dao", pkg + "bean");
		String xml = DaoBuilder.build(conn, rs, pkg, batch);
		System.out.println(xml);
		String filename = file(pkg, src, "dao", tablename + "DAO", "java");
		File f = new File(filename);
		File parent = f.getParentFile();
		if (!parent.exists() || !parent.isDirectory()) {
			parent.mkdir();
		}
		writeFile(filename, xml);
		conn.close();

	}

	public static void InternalBuild(Connection conn, String tablename,
			String appcontext, String pkg, boolean src, boolean batch,
			String clazzName4AppCont) throws Exception {
		boolean sorted = true;

		String sql = String.format("SELECT * FROM `%s` LIMIT 1", tablename);

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		// InternalBuilder builder = new InternalBuilder();
		// String xml = builder.build(conn, rs, pkg + "internal", pkg + "bean",
		// pkg + "dao", pkg + "entity", appcontext, immediately);
		String xml = InternalBuilder.build(conn, rs, pkg, appcontext, batch,
				sorted, clazzName4AppCont);
		System.out.println(xml);
		String filename = file(pkg, src, "internal", tablename + "Internal",
				"java");
		File f = new File(filename);
		File parent = f.getParentFile();
		if (!parent.exists() || !parent.isDirectory()) {
			parent.mkdir();
		}
		writeFile(filename, xml);
		conn.close();
	}

	public static void EntityBuild(Connection conn, String tablename,
			String appContext, String pkg, boolean src, String clazzName4AppCont)
			throws Exception {
		String filename = file(pkg, src, "entity", tablename + "Entity", "java");
		File f = new File(filename);
		if (f.exists())
			return;
		File parent = f.getParentFile();
		if (!parent.exists() || !parent.isDirectory()) {
			parent.mkdir();
		}

		String sql = String.format("SELECT * FROM `%s` LIMIT 1", tablename);

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		// EntityBuilder builder = new EntityBuilder();
		// String xml = builder.build(conn, rs, pkg + "entity", pkg + "bean",
		// pkg
		// + "dao", pkg + "internal", appcontext, immediately);
		String xml = EntityBuilder.build(conn, rs, pkg, appContext,
				clazzName4AppCont);
		System.out.println(xml);
		// String filename = file(pkg, src, "entity", tablename + "Entity",
		// "java");
		// File f = new File(filename);
		// if (!f.exists())
		writeFile(filename, xml);
		conn.close();
	}

	public static void RMIBuild(Connection conn, String tablename,
			String appContext, String pkg, boolean src, boolean batch)
			throws Exception {
		String filename = file(pkg, src, "rmi", tablename + "RMI", "java");
		File f = new File(filename);
		if (f.exists())
			return;
		File parent = f.getParentFile();
		if (!parent.exists() || !parent.isDirectory()) {
			parent.mkdir();
		}

		// File f = new File(filename);
		// if (f.exists())
		// return;
		String sql = String.format("SELECT * FROM `%s` LIMIT 1", tablename);

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		// EntityBuilder builder = new EntityBuilder();
		// String xml = builder.build(conn, rs, pkg + "entity", pkg + "bean",
		// pkg
		// + "dao", pkg + "internal", appcontext, immediately);

		String xml = RMIBuilder.build(conn, rs, pkg, appContext, batch);
		System.out.println(xml);
		// String filename = file(pkg, src, "entity", tablename + "Entity",
		// "java");
		// File f = new File(filename);
		// if (!f.exists())
		writeFile(filename, xml);
		conn.close();
	}

	public static void RemoteBuild(Connection conn, String tablename,
			String appContext, String pkg, boolean src, boolean batch)
			throws Exception {
		String filename = file(pkg, src, "remote", tablename + "Remote", "java");
		File f = new File(filename);
		if (f.exists())
			return;
		File parent = f.getParentFile();
		if (!parent.exists() || !parent.isDirectory()) {
			parent.mkdir();
		}

		// File f = new File(filename);
		// if (f.exists())
		// return;
		String sql = String.format("SELECT * FROM `%s` LIMIT 1", tablename);

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();

		// EntityBuilder builder = new EntityBuilder();
		// String xml = builder.build(conn, rs, pkg + "entity", pkg + "bean",
		// pkg
		// + "dao", pkg + "internal", appcontext, immediately);

		String xml = RemoteBuilder.build(conn, rs, pkg, appContext, batch);
		System.out.println(xml);
		// String filename = file(pkg, src, "entity", tablename + "Entity",
		// "java");
		// File f = new File(filename);
		// if (!f.exists())
		writeFile(filename, xml);
		conn.close();
	}

	public static String file(String pkg, boolean src, String type,
			String tablename, String ext) {
		String path = pkg2Path(pkg);
		if (src)
			path = "src/" + path;
		path = path + "/" + type + "/"
				+ upperFirst(PinYin.getShortPinYin(tablename)) + "." + ext;
		return path;
	}

	public static void writeFile(String f, String s) throws Exception {
		FileRw.write(f, s.getBytes());
	}

	public static String pkg2Path(String pkg) {
		return StrEx.package2Path(pkg);
	}

	public static boolean isDuplicate(List<String> tables) {
		Map<String, String> m = new HashMap<String, String>();
		for (String s : tables) {
			String s2 = PinYin.shortPinYin(s);
			String t = m.get(s2);
			if (t != null) {
				System.out.println(s + " duplicate with:" + t);
				return true;
			}
			m.put(s2, s);
		}
		return false;
	}

	public static final String upperFirst(String s) {
		return StrEx.upperFirst(s);
	}
}
