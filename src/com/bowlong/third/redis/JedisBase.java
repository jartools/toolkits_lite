package com.bowlong.third.redis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import com.bowlong.Toolkit;
import com.bowlong.bio2.B2Helper;
import com.bowlong.util.ListEx;
import com.bowlong.util.NewList;
import com.bowlong.util.NewMap;

/**
 * jedis -- 基本操作
 * 
 * @author Canyon
 */
@SuppressWarnings("rawtypes")
public class JedisBase extends JedisOrigin {

	private static final long serialVersionUID = 1L;

	// ///////////////////// 检查Redis是否联通 /////////////////////
	static public final String ping(final JedisPool pool) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return ping(jedis);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String ping(final Jedis jedis) {
		return jedis.ping();
	}

	static public final String ping() throws Exception {
		final JedisPool pool = getJedisPool();
		return ping(pool);
	}

	// ///////////////////// 清空所有DB中的所有数据 /////////////////////
	static public final String flushAll(final JedisPool pool) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return flushAll(jedis);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String flushAll(final Jedis jedis) {
		return jedis.flushAll();
	}

	static public final String flushAll() throws Exception {
		final JedisPool pool = getJedisPool();
		return flushAll(pool);
	}

	// ///////////////////// 清空某个DB中的所有数据 /////////////////////
	static public final String flushDB(final JedisPool pool, final int dbIndex)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return flushDB(jedis, dbIndex);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String flushDB(final Jedis jedis, final int dbIndex) {
		Jedis je = selectDb(jedis, dbIndex);
		return je.flushDB();
	}

	static public final String flushDB() throws Exception {
		final JedisPool pool = getJedisPool();
		return flushDB(pool, -1);
	}

	// ///////////////////// DECRBY key decrement 原子减指定的整数 /////////////////////
	static public final Long decrBy(final JedisPool pool, final String key,
			final int val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return decrBy(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long decrBy(final Jedis jedis, final String key,
			final int val) {
		return jedis.decrBy(key, val);
	}

	static public final Long decrBy(final String key, final int val)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return decrBy(pool, key, val);
	}

	// ///////////////////// incrBy key decrement 原子加指定的整数 /////////////////////
	static public final Long incrBy(final JedisPool pool, final String key,
			final int val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return incrBy(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long incrBy(final Jedis jedis, final String key,
			final int val) {
		// hincrBy
		return jedis.incrBy(key, val);
	}

	static public final Long incrBy(final String key, final int val)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return incrBy(pool, key, val);
	}

	// ///////////////////// MULTI 标记一个事务块开始 /////////////////////
	static public final Transaction multi(final JedisPool pool) {
		final Jedis jedis = getJedis(pool);
		return multi(jedis);
	}

	static public final Transaction multi(final Jedis jedis) {
		return jedis.multi();
	}

	static public final Transaction multi() {
		final JedisPool pool = getJedisPool();
		return multi(pool);
	}

	/*** 丢弃所有 MULTI 之后发的命令 **/
	static public final String discard(final Transaction t) {
		return t.discard();
	}

	/*** 执行所有 MULTI 之后发的命令 **/
	static public final List<Object> exec(final Transaction t) {
		return t.exec();
	}

	// ///////////////////// 是否存在 /////////////////////
	static public final boolean exists(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return exists(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final boolean exists(final Jedis jedis, final String key) {
		return jedis.exists(key);
	}

	static public final boolean exists(final Jedis jedis, final byte[] key) {
		return jedis.exists(key);
	}

	static public final boolean exists(final String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return exists(pool, key);
	}

	// ///////////////////// 删除数据 /////////////////////
	static public final Long remove(final JedisPool pool, final String... keys)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return remove(jedis, keys);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long remove(final Jedis jedis, final String... keys) {
		return jedis.del(keys);
	}

	static public final Long remove(final Jedis jedis, final byte[]... keys) {
		return jedis.del(keys);
	}

	static public final Long remove(final String... keys) throws Exception {
		final JedisPool pool = getJedisPool();
		return remove(pool, keys);
	}

	static public final Long remove(final JedisPool pool,
			final List<String> list) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return remove(jedis, list);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long remove(final Jedis jedis, final List<String> list) {
		if (ListEx.isEmpty(list))
			return -1L;
		String[] keys = {};
		keys = list.toArray(keys);
		return jedis.del(keys);
	}

	// ///////////////////// 取得有效时间 /////////////////////
	static public final Long ttl(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return ttl(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long ttl(final Jedis jedis, final byte[] key) {
		return jedis.ttl(key);
	}

	static public final Long ttl(final Jedis jedis, final String key) {
		return jedis.ttl(key);
	}

	static public final Long ttl(final String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return ttl(pool, key);
	}

	// ///////////////////// 设置有效时间 /////////////////////
	static public final Long expire(final JedisPool pool, final String key,
			final int seconds) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return expire(jedis, key, seconds);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long expire(final Jedis jedis, final String key,
			final int seconds) {
		if (seconds <= 0) {
			return -1L;
		}
		return jedis.expire(key, seconds);
	}

	static public final Long expire(final Jedis jedis, final byte[] key,
			final int seconds) {
		if (seconds <= 0) {
			return -1L;
		}
		return jedis.expire(key, seconds);
	}

	static public final Long expire(final String key, final int seconds)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return expire(pool, key, seconds);
	}

	static public final Long expire(final String key, final Date date)
			throws Exception {
		long delay = date.getTime() - System.currentTimeMillis();
		int seconds = (int) (delay <= 0 ? 1 : delay / 1000);

		final JedisPool pool = getJedisPool();
		return expire(pool, key, seconds);
	}

	// ///////////////////// 取消有效时间 /////////////////////
	static public final Long persist(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return persist(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long persist(final Jedis jedis, final String key) {
		return jedis.persist(key);
	}

	static public final Long persist(final Jedis jedis, final byte[] key) {
		return jedis.persist(key);
	}

	static public final Long persist(final String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return persist(pool, key);
	}

	// ///////////////////// 查看当前db的所有keys数量 /////////////////////
	static public final Long dbSize(final JedisPool pool, int dbIndex)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return dbSize(jedis, dbIndex);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long dbSize(final Jedis jedis, final int dbIndex) {
		Jedis je = selectDb(jedis, dbIndex);
		return je.dbSize();
	}

	static public final Long dbSize(final int dbIndex) throws Exception {
		final JedisPool pool = getJedisPool();
		return dbSize(pool, dbIndex);
	}

	// ///////////////////// 重命名 /////////////////////
	static public final Long renamenx(final JedisPool pool, String oldKey,
			String nwKey) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return renamenx(jedis, oldKey, nwKey);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long renamenx(final Jedis jedis, String oldKey,
			String nwKey) {
		return jedis.renamenx(oldKey, nwKey);
	}

	static public final Long renamenx(final Jedis jedis, byte[] oldKey,
			byte[] nwKey) {
		return jedis.renamenx(oldKey, nwKey);
	}

	static public final Long renamenx(String oldKey, String nwKey)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return renamenx(pool, oldKey, nwKey);
	}

	// ///////////////////// 取得正则表达式的keys /////////////////////
	static public final List<String> getList4KeyPattern(final JedisPool pool,
			String pattern) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getList4KeyPattern(jedis, pattern);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<String> getList4KeyPattern(final Jedis jedis,
			String pattern) {
		Set<String> vSet = jedis.keys(pattern);
		return ListEx.toList(vSet);
	}

	static public final List<byte[]> getList4KeyPattern(final Jedis jedis,
			byte[] pattern) {
		Set<byte[]> vSet = jedis.keys(pattern);
		return ListEx.toList(vSet);
	}

	static public final List<String> getList4KeyPattern(String pattern)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return getList4KeyPattern(pool, pattern);
	}

	// ///////////////////// 设值 /////////////////////
	static public final String set(final JedisPool pool, final String key,
			String val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return set(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String set(final Jedis jedis, final String key,
			String val) {
		return jedis.set(key, val);
	}

	static public final String set(final Jedis jedis, final byte[] key,
			byte[] val) {
		return jedis.set(key, val);
	}

	static public final String set(final String key, String val)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return set(pool, key, val);
	}

	// ///////////////////// 取值 /////////////////////
	static public final String get(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return get(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String get(final Jedis jedis, final String key) {
		return jedis.get(key);
	}

	static public final byte[] get(final Jedis jedis, final byte[] key) {
		return jedis.get(key);
	}

	static public final String get(final String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return get(pool, key);
	}

	// ///////////////////// 设值 Object /////////////////////
	static public final String putObject(final JedisPool pool,
			final String key, Object obj) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return putObject(jedis, key, obj);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String putObject(final Jedis jedis, final String key,
			Object obj) throws Exception {
		final byte[] val = Toolkit.serialization(obj);
		return jedis.set(key2(key), val);
	}

	static public final String putObject(final String key, Object obj)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return putObject(pool, key, obj);
	}

	// ///////////////////// 设值 B2Helper - Map /////////////////////
	static public final String putMap(final JedisPool pool, final String key,
			Map<?, ?> map) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return putMap(jedis, key, map);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String putMap(final Jedis jedis, final String key,
			Map<?, ?> map) throws Exception {
		final byte[] val = B2Helper.toBytes(map);
		return jedis.set(key2(key), val);
	}

	static public final String putMap(final String key, Map<?, ?> map)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return putMap(pool, key, map);
	}

	// ///////////////////// 设值 B2Helper - List /////////////////////
	static public final String putList(final JedisPool pool, final String key,
			List<?> list) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return putList(jedis, key, list);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String putList(final Jedis jedis, final String key,
			List<?> list) throws Exception {
		final byte[] val = B2Helper.toBytes(list);
		return jedis.set(key2(key), val);
	}

	static public final String putList(final String key, List<?> list)
			throws Exception {
		final JedisPool pool = getJedisPool();
		return putList(pool, key, list);
	}

	// ///////////////////// 取值 Object /////////////////////
	static public final Object getObject(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getObject(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Object getObject(final Jedis jedis, final String key)
			throws Exception {
		byte[] b = jedis.get(key2(key));
		return Toolkit.deserialization(b);
	}

	static public final Object getObject(final String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return getObject(pool, key);
	}

	// ///////////////////// 取值 B2Helper - Map /////////////////////
	static public final NewMap getMap(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getMap(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final NewMap getMap(final Jedis jedis, final String key)
			throws Exception {
		byte[] b = jedis.get(key2(key));
		return B2Helper.toMap(b);
	}

	static public final NewMap getMap(final String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return getMap(pool, key);
	}

	// ///////////////////// 取值 B2Helper - List /////////////////////
	static public final NewList getList(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getList(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final NewList getList(final Jedis jedis, final String key)
			throws Exception {
		byte[] b = jedis.get(key2(key));
		return B2Helper.toList(b);
	}

	static public final NewList getList(final String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return getList(pool, key);
	}

	// /////////////////////
}
