package com.bowlong.third.redis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import com.bowlong.lang.StrEx;
import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;

@SuppressWarnings("rawtypes")
public class JedisTookits {
	static JedisPool _pool = null;
	// redis 的配置map对象
	static private Map cfgMap = null;
	// redis访问密码
	static protected String pwd = null;

	// redis 数据库DB的index
	static protected int dbIndex = -1;

	static final public JedisPool getPool(Map rdsMap) {
		if (rdsMap != null && _pool == null) {
			int maxActive = MapEx.getInt(rdsMap, "maxActive");
			int maxIdle = MapEx.getInt(rdsMap, "maxIdle");
			int minIdle = MapEx.getInt(rdsMap, "minIdle");
			int maxWait = MapEx.getInt(rdsMap, "maxWait");
			String host = MapEx.getString(rdsMap, "host");
			int timeOut = MapEx.getInt(rdsMap, "timeOut");
			int port = MapEx.getInt(rdsMap, "port");
			String password = MapEx.getString(rdsMap, "pwd");
			int indexDB = MapEx.getInt(rdsMap, "dbIndex");

			JedisPoolConfig config = JedisEx.newJedisPoolConfig(maxActive, maxIdle, minIdle, maxWait);

			// testOnBorrow：在borrow一个jedis实例时，是否提前进行alidate操作；
			// 如果为true，则得到的jedis实例均是可用的；
			boolean testOnBorrow = MapEx.getBoolean(rdsMap, "testOnBorrow");

			// testOnReturn：在return给pool时，是否提前进行validate操作；
			boolean testOnReturn = MapEx.getBoolean(rdsMap, "testOnReturn");

			// testWhileIdle：如果为true，表示有一个idle object evitor线程对idle
			// object进行扫描，如果validate失败，此object会被从pool中drop掉；
			// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
			boolean testWhileIdle = MapEx.getBoolean(rdsMap, "testWhileIdle");

			// timeBetweenEvictionRunsMillis：表示idle object
			// evitor两次扫描之间要sleep的毫秒数；
			int timeBetweenEvictionRunsMillis = MapEx.getInt(rdsMap, "timeBetweenEvictionRunsMillis");

			// numTestsPerEvictionRun：表示idle object evitor每次扫描的最多的对象数；
			int numTestsPerEvictionRun = MapEx.getInt(rdsMap, "numTestsPerEvictionRun");

			// minEvictableIdleTimeMillis：表示一个对象至少停留在idle状态的最短时间，然后才能被idle
			// object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
			int minEvictableIdleTimeMillis = MapEx.getInt(rdsMap, "minEvictableIdleTimeMillis");

			config.setTestOnBorrow(testOnBorrow);
			config.setTestOnReturn(testOnReturn);
			config.setTestWhileIdle(testWhileIdle);
			Duration d = Duration.ofMillis(timeBetweenEvictionRunsMillis);
			config.setTimeBetweenEvictionRuns(d);
			config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
			d = Duration.ofMillis(minEvictableIdleTimeMillis);
			config.setMinEvictableIdleTime(d);

			pwd = StrEx.isEmpty(pwd) ? password : pwd;
			boolean isNullPwd = StrEx.isEmpty(pwd);

			dbIndex = dbIndex < 0 ? indexDB : dbIndex;
			boolean isZeroDBIndex = dbIndex <= 0;

			if (isZeroDBIndex && isNullPwd) {
				_pool = new JedisPool(config, host, port, timeOut);
			} else {
				_pool = new JedisPool(config, host, port, timeOut, pwd, dbIndex);
			}

			cfgMap = rdsMap;
		}
		return _pool;
	}

	static final public JedisPool getPool(int selectDb, String passwd, Map redisConfig) {
		pwd = passwd;
		dbIndex = selectDb;
		return getPool(redisConfig);
	}

	static final public JedisPool getPool() {
		return getPool(null);
	}

	/** 关闭数据 **/
	static final public void closePool() {
		if (_pool != null)
			_pool.destroy();
		_pool = null;
	}

	static final public Jedis getJedis() {
		JedisPool pool = getPool();
		if (pool == null)
			return null;
		Jedis result = null;
		int exceNum = 0;
		while (result == null) {
			try {
				exceNum++;
				result = pool.getResource();
			} catch (Exception e) {
				returnWhenError(pool, result);
			}
			if (exceNum >= 100) {
				exceNum = 0;
				break;
			}
		}
		return result;
	}

	static final public void returnWhenError(Jedis resource) {
		returnWhenError(null, resource);
	}

	static final public void returnWhenError(JedisPool pool, Jedis resource) {
		try {
			if (pool == null)
				pool = getPool();
			pool.returnBrokenResource(resource);
		} catch (Exception e) {
		}

	}

	static final public void returnJedis(Jedis resource) {
		returnJedis(null, resource);
	}

	static final public void returnJedis(JedisPool pool, Jedis resource) {
		try {
			if (pool == null)
				pool = getPool();
			if (resource != null) {
				if (pool == null) {
					resource.disconnect();
				} else {
					pool.returnResource(resource);
				}
			}
		} catch (Exception e) {
		}
	}

	/** 删除数据 参数是正则表达式模式的patterns的key函数 **/
	static final public void delParameters(String host, int port, String... patterns) {
		int maxActive = 2;
		int maxIdle = 2;
		int minIdle = 1;
		int maxWait = 3000;
		int timeOut = 10000;
		JedisPoolConfig config = JedisEx.newJedisPoolConfig(maxActive, maxIdle, minIdle, maxWait);
		JedisPool pool = new JedisPool(config, host, port, timeOut);
		Jedis r = null;
		try {
			r = pool.getResource();
			delByPatternsJds(r, patterns);
		} catch (Exception e) {
		} finally {
			try {
				pool.returnResource(r);
				pool.close();
				pool.destroy();
			} catch (Exception e2) {
			}
		}
	}

	static final public void delByPatternsJds(Jedis r, String... patterns) throws Exception {
		if (r == null || ListEx.isEmpty(patterns))
			return;
		List<String> list = new ArrayList<String>();

		for (String pattern : patterns) {
			Set<String> set = r.keys(pattern);
			if (!ListEx.isEmpty(set)) {
				list.addAll(set);
			}
		}

		if (ListEx.isEmpty(list))
			return;

		delByKeysJds(r, list);
	}

	/** 删除数据 参数是正则表达式模式的patterns的key函数 **/
	static final public void delKeys(String host, int port, String... keys) {
		int maxActive = 2;
		int maxIdle = 2;
		int minIdle = 1;
		int maxWait = 3000;
		int timeOut = 10000;
		JedisPoolConfig config = JedisEx.newJedisPoolConfig(maxActive, maxIdle, minIdle, maxWait);
		JedisPool pool = new JedisPool(config, host, port, timeOut);
		Jedis r = null;
		try {
			r = pool.getResource();
			delByKeysJds(r, keys);
		} catch (Exception e) {
		} finally {
			try {
				pool.returnResource(r);
				pool.close();
				pool.destroy();
			} catch (Exception e2) {
			}
		}
	}

	static final public void delByKeysJds(Jedis r, List<String> keylist) throws Exception {
		if (r == null || ListEx.isEmpty(keylist))
			return;
		String[] keys = {};
		keys = keylist.toArray(keys);
		delByKeysJds(r, keys);
	}

	static final public void delByKeysJds(Jedis r, String... keys) throws Exception {
		if (r == null || ListEx.isEmpty(keys))
			return;
		Pipeline pipeline = r.pipelined();
		delByKeysPip(pipeline, keys);
	}

	static final public void delByKeysPip(Pipeline pipeline, String... keys) throws Exception {
		if (pipeline == null || ListEx.isEmpty(keys))
			return;
		pipeline.del(keys);
		pipeline.syncAndReturnAll();
	}

	/***** 删除所有的key,fiel例如(Map k ,v为filed) *********/
	static final public void delByMapJds(Jedis r, Map<String, String> delMap) throws Exception {
		if (r == null || MapEx.isEmpty(delMap))
			return;
		delByMapPip(r.pipelined(), delMap);
	}

	static final public void delByMapPip(Pipeline pipeline, Map<String, String> delMap) throws Exception {
		if (pipeline == null || MapEx.isEmpty(delMap))
			return;

		for (Entry<String, String> e : delMap.entrySet()) {
			String key = e.getKey();
			String field = e.getValue();
			pipeline.hdel(key, field);
		}
		pipeline.syncAndReturnAll();
	}

	/*** 删除redis在第index的DB中的所有 key **/
	static final public void clearDBRedis(Jedis r, int index) {
		if (r == null)
			return;
		r.select(index);
		r.flushDB();
	}

	/*** 删除redis本库DB中的所有 key **/
	static final public void clearDBRedis(Jedis r) {
		if (r == null)
			return;
		r.flushDB();
	}

	static final public void clearDBRedis() {
		Jedis r = getJedis();
		try {
			clearDBRedis(r);
		} catch (Exception e) {
			returnWhenError(r);
		} finally {
			returnJedis(r);
		}
	}

	/*** 删除redis所有库中的所有 key **/
	static final public void clearAllRedis(Jedis r) {
		if (r == null)
			return;
		r.flushAll();
	}

	static final public void clearAllRedis() {
		Jedis r = getJedis();
		try {
			clearAllRedis(r);
		} catch (Exception e) {
			returnWhenError(r);
		} finally {
			returnJedis(r);
		}
	}

	/*** 查看当前db的所有keys数量 **/
	static final public long getDBSize() {
		Jedis r = getJedis();
		long result = 0;
		try {
			result = r.dbSize();
		} catch (Exception e) {
			returnWhenError(r);
		} finally {
			returnJedis(r);
		}
		return result;
	}

	// 取得 当前redis的配置文件
	static final public Map getCfgMap() {
		return cfgMap;
	}
}
