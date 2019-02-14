package com.bowlong;

import java.util.Map;

import com.bowlong.third.redis.JedisTookits;
import com.bowlong.tool.TkitJsp;

import redis.clients.jedis.JedisPool;

@SuppressWarnings({ "unchecked" })
public class Toolkit extends TkitJsp {

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
