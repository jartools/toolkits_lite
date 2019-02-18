package com.bowlong.sql.mysql;

import java.sql.Connection;

import javax.sql.DataSource;

import com.bowlong.sql.AtomicInt;

@SuppressWarnings("all")
public class JdbcTemplate extends com.bowlong.sql.jdbc.DataSet {

	public JdbcTemplate(Connection conn, String TABLENAME) {
		super(conn, TABLENAME);
	}

	public JdbcTemplate(DataSource ds, String TABLENAME) {
		super(ds, TABLENAME);
	}

	public JdbcTemplate(DataSource ds_r, DataSource ds_w, String TABLENAME) {
		super(ds_r, ds_w, TABLENAME);
	}

	static final AtomicInt asyncNum = new AtomicInt();

	public static final int incrementAndGet() {
		// synchronized (asyncNum) {
		return asyncNum.incrementAndGet();
		// }
	}

	public static final int decrementAndGet() {
		// synchronized (asyncNum) {
		return asyncNum.decrementAndGet();
		// }
	}

	public static final int asyncNum() {
		// synchronized (asyncNum) {
		return asyncNum.get();
		// }
	}

	public static void main(String[] args) throws Exception {
	}
}
