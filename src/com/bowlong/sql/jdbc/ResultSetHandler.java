package com.bowlong.sql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 处理查询得到的结果ResultSet
 * @author Canyon
 *
 */
public interface ResultSetHandler {
    public <T> T handle(ResultSet rs) throws SQLException;
}