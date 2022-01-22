package com.bowlong.third.redis;

import java.util.ArrayList;
import java.util.List;

import com.bowlong.lang.StrEx;
import com.bowlong.util.ListEx;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.SortingParams;
import redis.clients.jedis.params.SortingParams;

/**
 * jedis -- 主要操作 <br/>
 * T-List -- Key - Val(list 元素); <br/>
 * T-Hash -- Key - Filed - Val 值
 * 
 * @author Canyon
 */
public class JedisNormal extends JedisBase {

	private static final long serialVersionUID = 1L;

	// ///////////////////// Type:Redis's_List的操作 /////////////////////
	// 常用操作
	// ///////////////////// ===================== /////////////////////

	// ///////////////////// 取得长度 /////////////////////
	static public final Long llen(JedisPool pool,String key) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return llen(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long llen(Jedis jedis,String key) {
		return jedis.llen(key);
	}

	static public final Long llen(Jedis jedis,byte[] key) {
		return jedis.llen(key);
	}

	static public final Long llen(String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return llen(pool, key);
	}

	// ////////// 取得区间在[start,end]的值,包含start与end //////////
	static public final List<String> lrange(JedisPool pool,String key,long start,long end) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return lrange(jedis, key, start, end);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<String> lrange(Jedis jedis,String key,long start,long end) {
		return jedis.lrange(key, start, end);
	}

	static public final List<byte[]> lrange(Jedis jedis,byte[] key,long start,long end) {
		return jedis.lrange(key, start, end);
	}

	static public final List<String> lrange(String key,long start,long end) throws Exception {
		final JedisPool pool = getJedisPool();
		return lrange(pool, key, start, end);
	}

	// ////////// ltrim[保留(retain) start(包含),end(包含)之间的值] 成功_返回:ok //////////
	static public final String ltrim(JedisPool pool,String key,long start,long end) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return ltrim(jedis, key, start, end);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String ltrim(Jedis jedis,String key,long start,long end) {
		return jedis.ltrim(key, start, end);
	}

	static public final String ltrim(Jedis jedis,byte[] key,long start,long end) {
		return jedis.ltrim(key, start, end);
	}

	static public final String ltrim(String key,long start,long end) throws Exception {
		final JedisPool pool = getJedisPool();
		return ltrim(pool, key, start, end);
	}

	// ////////// 取得 Redis_List对象的所有值 //////////
	static public final List<String> getAllList4TList(JedisPool pool,String key) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getAllList4TList(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<String> getAllList4TList(Jedis jedis,String key) {
		final long end = llen(jedis, key);
		return jedis.lrange(key, 0, end);
	}

	static public final List<byte[]> getAllList4TList(Jedis jedis,byte[] key) {
		final long end = llen(jedis, key);
		return jedis.lrange(key, 0, end);
	}

	static public final List<String> getAllList4TList2(Jedis jedis,String key) {
		return jedis.lrange(key, 0, -1);
	}

	static public final List<byte[]> getAllList4TList2(Jedis jedis,byte[] key) {
		return jedis.lrange(key, 0, -1);
	}

	static public final List<String> getAllList4TList(String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return getAllList4TList(pool, key);
	}

	// ////////// list中删除count个值为val,返回：被移除元素的数量 //////////
	/**
	 * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 <br/>
	 * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。<br/>
	 * count = 0 : 移除表中所有与 value 相等的值。<br/>
	 */
	static public final Long lrem(JedisPool pool,String key,long count,String val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return lrem(jedis, key, count, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long lrem(Jedis jedis,String key,long count,String val) {
		return jedis.lrem(key, count, val);
	}

	static public final Long lrem(Jedis jedis,byte[] key,long count,byte[] val) {
		return jedis.lrem(key, count, val);
	}

	static public final Long lrem(String key,long count,String val) throws Exception {
		final JedisPool pool = getJedisPool();
		return lrem(pool, key, count, val);
	}

	static public final Long remvoeAllVal4TList(JedisPool pool,String key,String val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return remvoeAllVal4TList(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long remvoeAllVal4TList(Jedis jedis,String key,String val) {
		return jedis.lrem(key, 0, val);
	}

	static public final Long remvoeAllVal4TList(Jedis jedis,byte[] key,byte[] val) {
		return jedis.lrem(key, 0, val);
	}

	static public final Long remvoeAllVal4TList(String key,String val) throws Exception {
		final JedisPool pool = getJedisPool();
		return remvoeAllVal4TList(pool, key, val);
	}

	// ////////// 取得下标为index一个值 不成功_返回:nil //////////
	static public final String lindex(JedisPool pool,String key,long index) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return lindex(jedis, key, index);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String lindex(Jedis jedis,String key,long index) {
		return jedis.lindex(key, index);
	}

	static public final byte[] lindex(Jedis jedis,byte[] key,long index) {
		return jedis.lindex(key, index);
	}

	static public final String lindex(String key,long index) throws Exception {
		final JedisPool pool = getJedisPool();
		return lindex(pool, key, index);
	}

	// ////////// 设置下标为index一个值 ,成功_返回:ok //////////
	static public final String lset(JedisPool pool,String key,long index,String val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return lset(jedis, key, index, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String lset(Jedis jedis,String key,long index,String val) {
		return jedis.lset(key, index, val);
	}

	static public final String lset(Jedis jedis,byte[] key,long index,byte[] val) {
		return jedis.lset(key, index, val);
	}

	static public final String lset(String key,long index,String val) throws Exception {
		final JedisPool pool = getJedisPool();
		return lset(pool, key, index, val);
	}

	// ////////// rpush 将元素插入尾部 先进先出[返回：表的长度] //////////
	static public final Long rpush(JedisPool pool,String key,String... val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return rpush(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long rpush(Jedis jedis,String key,String... val) {
		return jedis.rpush(key, val);
	}

	static public final Long rpush(Jedis jedis,byte[] key,byte[]... val) {
		return jedis.rpush(key, val);
	}

	static public final Long rpush(String key,String... val) throws Exception {
		final JedisPool pool = getJedisPool();
		return rpush(pool, key, val);
	}

	static public final Long rpush(JedisPool pool,String key,List<String> vals) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return rpush(jedis, key, vals);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long rpush(Jedis jedis,String key,List<String> vals) {
		if (jedis == null || ListEx.isEmpty(vals))
			return llen(jedis, key);
		String[] val = {};
		val = vals.toArray(val);
		return jedis.rpush(key, val);
	}

	// ////////// lpush 将元素插入头部 先进后出[返回：表的长度] //////////
	static public final Long lpush(JedisPool pool,String key,String... val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return lpush(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long lpush(Jedis jedis,String key,String... val) {
		return jedis.lpush(key, val);
	}

	static public final Long lpush(JedisPool pool,byte[] key,byte[]... val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return lpush(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long lpush(Jedis jedis,byte[] key,byte[]... val) {
		return jedis.lpush(key, val);
	}

	static public final Long lpush(String key,String... val) throws Exception {
		final JedisPool pool = getJedisPool();
		return lpush(pool, key, val);
	}

	static public final Long lpush(JedisPool pool,String key,List<String> vals) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return lpush(jedis, key, vals);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long lpush(Jedis jedis,String key,List<String> vals) {
		if (jedis == null || ListEx.isEmpty(vals))
			return llen(jedis, key);
		String[] val = {};
		val = vals.toArray(val);
		return jedis.lpush(key, val);
	}

	static public final Long lpush(Jedis jedis,byte[] key,List<byte[]> vals) {
		if (jedis == null || ListEx.isEmpty(vals))
			return llen(jedis, key);
		byte[][] val = {};
		val = vals.toArray(val);
		return jedis.lpush(key, val);
	}

	// ////////// 取得 SortingParams 筛选排序值 //////////
	static public final List<String> getListSort4TList(JedisPool pool,String key,SortingParams sop) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getListSort4TList(jedis, key, sop);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<String> getListSort4TList(Jedis jedis,String key,SortingParams sop) {
		return jedis.sort(key, sop);
	}

	static public final List<String> getListSort4TList(String key,SortingParams sop) throws Exception {
		final JedisPool pool = getJedisPool();
		return getListSort4TList(pool, key, sop);
	}

	// ////////// 取得 这个值在list里面的所有Index位置 //////////
	static public final List<Long> getIndexs4TList(JedisPool pool,String key,String val) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return getIndexs4TList(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final List<Long> getIndexs4TList(Jedis jedis,String key,String val) {
		List<Long> ret = new ArrayList<Long>();
		List<String> all = getAllList4TList(jedis, key);
		if (ListEx.isEmpty(all))
			return ret;
		int lens = all.size();
		for (int i = 0; i < lens; i++) {
			String v = all.get(i);
			if (StrEx.isSame(val, v)) {
				ret.add((long) i);
			}
		}
		return ret;
	}

	static public final List<Long> getIndexs4TList(String key,String val) throws Exception {
		final JedisPool pool = getJedisPool();
		return getIndexs4TList(pool, key, val);
	}

	// ///////////////////// Type:Redis's_HashMap的操作 /////////////////////
	// 常用操作
	// ///////////////////// ===================== /////////////////////

	// ///////////////////// 取得长度 /////////////////////
	static public final Long hlen(JedisPool pool,String key) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return hlen(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long hlen(Jedis jedis,String key) {
		return jedis.hlen(key);
	}

	static public final Long hlen(Jedis jedis,byte[] key) {
		return jedis.hlen(key);
	}

	static public final Long hlen(String key) throws Exception {
		final JedisPool pool = getJedisPool();
		return hlen(pool, key);
	}

	// ///////////////////// 是否存在 /////////////////////
	static public final boolean hexists(JedisPool pool,String key,String filed) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return hexists(jedis, key, filed);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final boolean hexists(Jedis jedis,String key,String filed) {
		return jedis.hexists(key, filed);
	}

	static public final boolean hexists(Jedis jedis,byte[] key,byte[] filed) {
		return jedis.hexists(key, filed);
	}

	static public final boolean hexists(String key,String filed) throws Exception {
		final JedisPool pool = getJedisPool();
		return hexists(pool, key, filed);
	}

	// ///////////////////// 取得val根据k_f /////////////////////
	static public final String hget(JedisPool pool,String key,String filed) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return hget(jedis, key, filed);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final String hget(Jedis jedis,String key,String filed) {
		return jedis.hget(key, filed);
	}

	static public final byte[] hget(Jedis jedis,byte[] key,byte[] filed) {
		return jedis.hget(key, filed);
	}

	static public final String hget(String key,String filed) throws Exception {
		final JedisPool pool = getJedisPool();
		return hget(pool, key, filed);
	}

	// ///////////////////// 删除k_fields /////////////////////
	static public final Long hdel(JedisPool pool,String key,String... filed) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return hdel(jedis, key, filed);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long hdel(Jedis jedis,String key,String... filed) {
		return jedis.hdel(key, filed);
	}

	static public final Long hdel(Jedis jedis,byte[] key,byte[]... filed) {
		return jedis.hdel(key, filed);
	}

	static public final Long hdel(String key,String... filed) throws Exception {
		final JedisPool pool = getJedisPool();
		return hdel(pool, key, filed);
	}

	static public final Long hdel(JedisPool pool,String key,List<String> filed) throws Exception {
		final Jedis jedis = getJedis(pool);
		try {
			return hdel(jedis, key, filed);
		} catch (Exception e) {
			throw e;
		} finally {
			returnJedis(pool, jedis);
		}
	}

	static public final Long hdel(Jedis jedis,String key,List<String> filed) {
		String[] fileds = {};
		fileds = filed.toArray(fileds);
		return hdel(jedis, key, fileds);
	}
	// /////////////////////

	// /////////////////////
}
