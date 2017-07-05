package com.bowlong.objpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlPool extends AbstractQueueObjPool<Connection> {

	public String host = "127.0.0.1";
	public int port = 3306;
	public String db = "test";
	public String user = "root";
	public String password = "";
	public boolean reconnect = true;
	public String encoding = "utf-8";

	public MysqlPool() {
		super.MAX = 8;
	}

	public MysqlPool(String db) {
		super.MAX = 8;
		this.db = db;
	}

	public MysqlPool(String host, String db) {
		super.MAX = 8;
		this.host = host;
		this.db = db;
	}

	public MysqlPool(String host, int port, String db) {
		super.MAX = 8;
		this.host = host;
		this.port = port;
		this.db = db;
	}

	public MysqlPool(String db, String user, String password) {
		super.MAX = 8;
		this.db = db;
		this.user = user;
		this.password = password;
	}

	public MysqlPool(String host, int port, String db, String user,
			String password) {
		super.MAX = 8;
		this.host = host;
		this.port = port;
		this.db = db;
		this.user = user;
		this.password = password;
	}

	public MysqlPool(String host, int port, String db, String user,
			String password, String encoding) {
		super.MAX = 8;
		this.host = host;
		this.port = port;
		this.db = db;
		this.user = user;
		this.password = password;
		this.encoding = encoding;
	}

	private static final String s(String s, Object... args) {
		return String.format(s, args);
	}

	private final Connection newMysqlConnection()
			throws ClassNotFoundException, SQLException {
		String driver = ("com.mysql.jdbc.Driver");
		String s = "jdbc:mysql://%s:%d/%s?autoReconnect=%s&characterEncoding=%s";
		String url = s(s, host, port, db, String.valueOf(reconnect), encoding);
		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}

	public final void setMax(int num) {
		this.MAX = num;
	}

	@Override
	protected final Connection createObj() {
		try {
			Connection conn = newMysqlConnection();
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected final Connection resetObj(Connection obj) {
		return obj;
	}

	@Override
	protected final Connection destoryObj(Connection obj) {
		try {
			obj.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public final Connection getConnection() {
		return this.borrow();
	}

	public final void release(Connection conn) {
		this.returnObj(conn);
	}

	// /////////////////////////////////////////////////
	public static final MysqlPool POOL = new MysqlPool();

	public static final void setHost(String host) {
		POOL.host = host;
	}

	public static final void setPort(int port) {
		POOL.port = port;
	}

	public static final void setDb(String db) {
		POOL.db = db;
	}

	public static final void setUser(String user) {
		POOL.user = user;
	}

	public static final void setPassword(String password) {
		POOL.password = password;
	}

	public static final void setReconnect(boolean reconnect) {
		POOL.reconnect = reconnect;
	}

	public static final void setEncoding(String encoding) {
		POOL.encoding = encoding;
	}

	public static final Connection borrowObject() {
		return POOL.borrow();
	}

	public static final void returnObject(Connection conn) {
		POOL.returnObj(conn);
	}

}
