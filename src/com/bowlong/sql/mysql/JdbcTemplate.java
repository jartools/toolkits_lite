package com.bowlong.sql.mysql;

import java.sql.Connection;

import javax.sql.DataSource;

import com.bowlong.sql.AtomicInt;

@SuppressWarnings("all")
public class JdbcTemplate extends com.bowlong.sql.jdbc.JdbcTemplet {

	public JdbcTemplate(Connection conn) {
		super(conn);
	}

	public JdbcTemplate(final DataSource ds) {
		super(ds);
	}

	public JdbcTemplate(final DataSource ds_r, final DataSource ds_w) {
		super(ds_r, ds_w);
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
