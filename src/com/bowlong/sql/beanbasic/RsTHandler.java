package com.bowlong.sql.beanbasic;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings({ "unchecked" })
public interface RsTHandler<T extends RsHandler> extends RsHandler {
	public T handle(ResultSet rs) throws SQLException;
}
