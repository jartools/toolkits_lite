package com.bowlong.sql.beanbasic;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 处理查询得到的结果ResultSet
 * @author Canyon
 *
 */
public interface RsHandler {
    public <T> T handle(ResultSet rs) throws SQLException;
}