package com.bowlong;

import java.util.Map;

import redis.clients.jedis.JedisPool;

import com.bowlong.third.redis.JedisTookits;
import com.bowlong.tool.TkitBase;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Toolkit extends TkitBase {

	static public Map<String, Object> RedisMap() {
		return JedisTookits.getOrgRedisCfg();
	}

	static public JedisPool getJedisPool(Map orgRedis) {
		return JedisTookits.getJedisPool(orgRedis);
	}

	static public void closeJedisPool() {
		JedisTookits.closeJedisPool();
	}
}
