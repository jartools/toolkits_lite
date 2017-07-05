package com.bowlong.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DoInTransaction {
	public ResultSet exec(Connection conn) throws SQLException;
}
