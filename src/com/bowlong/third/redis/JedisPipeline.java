package com.bowlong.third.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import com.bowlong.util.ListEx;
import com.bowlong.util.MapEx;

/**
 * jedis -- Pipeline 操作
 * 
 * @author Canyon
 */
@SuppressWarnings("unchecked")
public class JedisPipeline extends JedisNormal {

	// ///////////////////// /////////////////////
	private static final long serialVersionUID = 1L;

	// ///////////////////// 删除 keys /////////////////////
	static public final void remove4Pip(final JedisPool pool,
			final String... keys) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			remove4Pip(jedis, keys);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final void remove4Pip(final Jedis jedis, final String... keys) {
		if (jedis == null)
			return;
		remove4Pip(jedis.pipelined(), keys);
	}

	static public final void remove4Pip(final Pipeline pipeline,
			final String... keys) {
		if (pipeline == null || ListEx.isEmpty(keys))
			return;
		pipeline.del(keys);
		pipeline.syncAndReturnAll();
	}

	static public final void remove4Pip(final Pipeline pipeline,
			final byte[]... keys) {
		if (pipeline == null || ListEx.isEmpty(keys))
			return;
		pipeline.del(keys);
		pipeline.syncAndReturnAll();
	}

	static public final void remove4Pip(final String... keys) throws Exception {
		final JedisPool pool = getJedisPool();
		remove4Pip(pool, keys);
	}

	static public final void remove4Pip(final JedisPool pool,
			final List<String> list) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			remove4Pip(jedis, list);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final void remove4Pip(final Jedis jedis,
			final List<String> list) {
		if (jedis == null || ListEx.isEmpty(list))
			return;
		String[] keys = {};
		keys = list.toArray(keys);
		remove4Pip(jedis.pipelined(), keys);
	}

	static public final void remove4Pip(final List<String> list)
			throws Exception {
		final JedisPool pool = getJedisPool();
		remove4Pip(pool, list);
	}

	// ///////////////////// 删除 满足pattern的所有keys 通配符[*] /////////////////////
	static public final void remove4PipByPatterns(final JedisPool pool,
			final String... patterns) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			remove4PipByPatterns(jedis, patterns);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final void remove4PipByPatterns(final Jedis jedis,
			final String... patterns) {
		if (jedis == null || ListEx.isEmpty(patterns))
			return;
		List<String> list = new ArrayList<String>();
		int lens = patterns.length;
		for (int i = 0; i < lens; i++) {
			Set<String> set = jedis.keys(patterns[i]);
			if (!ListEx.isEmpty(set)) {
				list.addAll(set);
			}
		}
		remove4Pip(jedis, list);
	}

	static public final void remove4PipByPatterns(final String... patterns)
			throws Exception {
		final JedisPool pool = getJedisPool();
		remove4PipByPatterns(pool, patterns);
	}

	static public final void remove4PipByPatterns(final String host,
			final int port, final String pwd, final int dbIndex,
			final String... patterns) throws Exception {
		final JedisPool pool = getJedisPool(host, port, pwd, dbIndex);
		remove4PipByPatterns(pool, patterns);
	}

	static public final void remove4PipByPatterns(final JedisPool pool,
			final List<String> patterns) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			remove4PipByPatterns(jedis, patterns);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final void remove4PipByPatterns(final Jedis jedis,
			final List<String> patterns) {
		if (jedis == null || ListEx.isEmpty(patterns))
			return;
		List<String> list = new ArrayList<String>();
		int lens = patterns.size();
		for (int i = 0; i < lens; i++) {
			Set<String> set = jedis.keys(patterns.get(i));
			if (!ListEx.isEmpty(set)) {
				list.addAll(set);
			}
		}
		remove4Pip(jedis, list);
	}

	static public final void remove4PipByPatterns(final String host,
			final int port, final String pwd, final int dbIndex,
			final List<String> patterns) throws Exception {
		final JedisPool pool = getJedisPool(host, port, pwd, dbIndex);
		remove4PipByPatterns(pool, patterns);
	}

	// ///////////////////// 删除 满足Key_Filed_Value的所有filed /////////////////////
	static public final void remove4TList(final JedisPool pool,
			final String key, final String... vals) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			remove4TList(jedis, key, vals);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final void remove4TList(final Jedis jedis, final String key,
			final String... vals) {
		remove4TList(jedis.pipelined(), key, vals);
	}

	static public final void remove4TList(final Pipeline pipeline,
			final String key, final String... vals) {
		if (pipeline == null || ListEx.isEmpty(vals))
			return;
		int lens = vals.length;
		for (int i = 0; i < lens; i++) {
			pipeline.lrem(key, 0, vals[i]);
		}
		pipeline.syncAndReturnAll();
	}

	static public final void remove4TList(final Pipeline pipeline,
			final byte[] key, final byte[]... vals) {
		if (pipeline == null || ListEx.isEmpty(vals))
			return;
		int lens = vals.length;
		for (int i = 0; i < lens; i++) {
			pipeline.lrem(key, 0, vals[i]);
		}
		pipeline.syncAndReturnAll();
	}

	static public final void remove4TList(final String key,
			final String... vals) throws Exception {
		final JedisPool pool = getJedisPool();
		remove4TList(pool, key, vals);
	}

	static public final void remove4TList(final JedisPool pool,
			final String key, final List<String> vals) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			remove4TList(jedis, key, vals);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final void remove4TList(final Jedis jedis, final String key,
			final List<String> vals) {
		remove4TList(jedis.pipelined(), key, vals);
	}

	static public final void remove4TList(final Pipeline pipeline,
			final String key, final List<String> vals) {
		if (pipeline == null || ListEx.isEmpty(vals))
			return;
		int lens = vals.size();
		for (int i = 0; i < lens; i++) {
			pipeline.lrem(key, 0, vals.get(i));
		}
		pipeline.syncAndReturnAll();
	}

	static public final void remove4TList(final Jedis jedis, final byte[] key,
			final List<byte[]> vals) {
		remove4TList(jedis.pipelined(), key, vals);
	}

	static public final void remove4TList(final Pipeline pipeline,
			final byte[] key, final List<byte[]> vals) {
		if (pipeline == null || ListEx.isEmpty(vals))
			return;
		int lens = vals.size();
		for (int i = 0; i < lens; i++) {
			pipeline.lrem(key, 0, vals.get(i));
		}
		pipeline.syncAndReturnAll();
	}

	static public final void remove4TList(final String key,
			final List<String> vals) throws Exception {
		final JedisPool pool = getJedisPool();
		remove4TList(pool, key, vals);
	}

	// ///////////////////// Type:Redis's_HashMap的操作 /////////////////////

	// ///////////////////// 删除 满足Key_Filed_Value的所有filed /////////////////////
	static public final void remove4THash(final JedisPool pool,
			final Map<String, String> mapKF) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			remove4THash(jedis, mapKF);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final void remove4THash(final Jedis jedis,
			final Map<String, String> mapKF) {
		remove4THash(jedis.pipelined(), mapKF);
	}

	static public final void remove4THash(final Pipeline pipeline,
			final Map<String, String> mapKF) {
		if (pipeline == null || MapEx.isEmpty(mapKF))
			return;
		for (Entry<String, String> e : mapKF.entrySet()) {
			String key = e.getKey();
			String field = e.getValue();
			pipeline.hdel(key, field);
		}
		pipeline.syncAndReturnAll();
	}

	static public final void remove4THash(final Map<String, String> mapKF)
			throws Exception {
		final JedisPool pool = getJedisPool();
		remove4THash(pool, mapKF);
	}

	// ///////////////////// 取得列表对象 /////////////////////
	static public final List<Map<String, String>> getList4THash(
			final JedisPool pool, final String... thash_keys) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getList4THash(jedis, thash_keys);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<Map<String, String>> getList4THash(
			final Jedis jedis, final String... thash_keys) {
		return getList4THash(jedis.pipelined(), thash_keys);
	}

	static public final List<Map<String, String>> getList4THash(
			final Pipeline pipeline, final String... thash_keys) {
		if (pipeline == null || ListEx.isEmpty(thash_keys))
			return null;

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		int lens = thash_keys.length;
		for (int i = 0; i < lens; i++) {
			pipeline.hgetAll(thash_keys[i]);
		}
		List<Object> vBack = pipeline.syncAndReturnAll();
		lens = vBack.size();
		for (int i = 0; i < lens; i++) {
			Map<String, String> map = (Map<String, String>) vBack.get(i);
			if (MapEx.isEmpty(map))
				continue;
			result.add(map);
		}
		return result;
	}

	static public final List<Map<byte[], byte[]>> getList4THash(
			final JedisPool pool, final byte[]... thash_keys) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getList4THash(jedis, thash_keys);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<Map<byte[], byte[]>> getList4THash(
			final Jedis jedis, final byte[]... thash_keys) {
		return getList4THash(jedis.pipelined(), thash_keys);
	}

	static public final List<Map<byte[], byte[]>> getList4THash(
			final Pipeline pipeline, final byte[]... thash_keys) {
		if (pipeline == null || ListEx.isEmpty(thash_keys))
			return null;

		List<Map<byte[], byte[]>> result = new ArrayList<Map<byte[], byte[]>>();

		int lens = thash_keys.length;
		for (int i = 0; i < lens; i++) {
			pipeline.hgetAll(thash_keys[i]);
		}
		List<Object> vBack = pipeline.syncAndReturnAll();
		lens = vBack.size();
		for (int i = 0; i < lens; i++) {
			Map<byte[], byte[]> map = (Map<byte[], byte[]>) vBack.get(i);
			if (MapEx.isEmpty(map))
				continue;
			result.add(map);
		}
		return result;
	}

	static public final List<Map<String, String>> getList4THash(
			final String... thash_keys) throws Exception {
		final JedisPool pool = getJedisPool();
		return getList4THash(pool, thash_keys);
	}

	static public final List<Map<String, String>> getList4THash(
			final JedisPool pool, final List<String> thash_keys)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getList4THash(jedis, thash_keys);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<Map<String, String>> getList4THash(
			final Jedis jedis, final List<String> thash_keys) {
		return getList4THash(jedis.pipelined(), thash_keys);
	}

	static public final List<Map<String, String>> getList4THash(
			final Pipeline pipeline, final List<String> thash_keys) {
		if (pipeline == null || ListEx.isEmpty(thash_keys))
			return null;
		String[] keys = {};
		keys = thash_keys.toArray(keys);
		return getList4THash(pipeline, keys);
	}
	// /////////////////////
}
