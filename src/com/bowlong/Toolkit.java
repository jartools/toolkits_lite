package com.bowlong;

import java.util.Map;

import org.apache.commons.logging.Log;

import com.alibaba.druid.pool.DruidDataSource;
import com.bowlong.third.redis.JedisTookits;
import com.bowlong.tool.TkitJsp;

import redis.clients.jedis.JedisPool;

@SuppressWarnings({ "unchecked" })
public class Toolkit extends TkitJsp {
	static final private Log log = getLog(Toolkit.class);

	static final public DruidDataSource dataSource(Map<?, ?> map) {
		String url = getString(map, "url");
		String driverClassName = getString(map, "driverClassName");
		String username = getString(map, "username");
		String password = getString(map, "password");
		int maxActive = getInt(map, "maxActive");
		int initialSize = getInt(map, "initialSize");
		int minIdle = getInt(map, "minIdle");
		int maxWait = getInt(map, "maxWait");
		boolean removeAbandoned = getBool(map, "removeAbandoned");
		int removeAbandonedTimeout = getInt(map, "removeAbandonedTimeout");
		String validationQuery = getString(map, "validationQuery");
		int validationQueryTimeout = getInt(map, "validationQueryTimeout");
		boolean testOnBorrow = getBool(map, "testOnBorrow");
		boolean testOnReturn = getBool(map, "testOnReturn");
		boolean testWhileIdle = getBool(map, "testWhileIdle");
		int timeBetweenEvictionRunsMillis = getInt(map, "timeBetweenEvictionRunsMillis");
		int minEvictableIdleTimeMillis = getInt(map, "minEvictableIdleTimeMillis");
		String filters = getString(map, "filters");
		DruidDataSource ds = null;
		try {
			ds = new DruidDataSource();
			ds.setDriverClassName(driverClassName);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			ds.setMaxActive(maxActive);
			ds.setInitialSize(initialSize);
			ds.setMinIdle(minIdle);
			ds.setMaxWait(maxWait);
			ds.setRemoveAbandoned(removeAbandoned);
			ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);

			ds.setValidationQuery(validationQuery);
			ds.setValidationQueryTimeout(validationQueryTimeout);
			ds.setTestOnBorrow(testOnBorrow);
			ds.setTestOnReturn(testOnReturn);
			ds.setTestWhileIdle(testWhileIdle);
			ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
			ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
			ds.setFilters(filters);
		} catch (Exception e1) {
			log.error(e2s(e1));
		}
		try {
			ds.getConnection().getMetaData();
			// System.out.println(ds.getNumActive() + "-" + ds.getNumIdle());
		} catch (Exception e) {
			log.error(e2s(e));
		}
		return ds;
	}

	static public Map<String, Object> redisMap() {
		return JedisTookits.getCfgMap();
	}

	static public JedisPool getJedisPool() {
		return JedisTookits.getPool();
	}

	static public void closeJedisPool() {
		JedisTookits.closePool();
	}
}
