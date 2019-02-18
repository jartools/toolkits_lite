package com.bowlong.sql.mysql;

import java.sql.Connection;

import javax.sql.DataSource;

public class DataSet extends JdbcTemplate {
	public DataSet(Connection conn, String TABLENAME) {
		super(conn, TABLENAME);
	}

	public DataSet(DataSource ds, String TABLENAME) {
		super(ds, TABLENAME);
	}

	public DataSet(DataSource ds_r, DataSource ds_w, String TABLENAME) {
		super(ds_r, ds_w, TABLENAME);
	}
}
