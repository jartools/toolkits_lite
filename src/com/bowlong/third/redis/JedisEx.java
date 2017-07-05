package com.bowlong.third.redis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import com.bowlong.Toolkit;
import com.bowlong.bio2.B2Helper;
import com.bowlong.json.MyJson;
import com.bowlong.lang.task.ThreadEx;
import com.bowlong.sql.jdbc.BeanSupport;
import com.bowlong.util.NewMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JedisEx {

	/*
	 * --------------------------------------------------------------------------
	 * --------------------------------- APPEND key value 追加一个值到key上
	 * 
	 * AUTH password 验证服务器
	 * 
	 * BGREWRITEAOF 异步重写追加文件
	 * 
	 * BGSAVE 异步保存数据集到磁盘上
	 * 
	 * BITCOUNT key [start] [end] 统计字符串指定起始位置的字节数
	 * 
	 * BITOP operation destkey key [key ...] Perform bitwise operations between
	 * strings
	 * 
	 * BLPOP key [key ...] timeout 删除，并获得该列表中的第一元素，或阻塞，直到有一个可用
	 * 
	 * BRPOP key [key ...] timeout 删除，并获得该列表中的最后一个元素，或阻塞，直到有一个可用
	 * 
	 * BRPOPLPUSH source destination timeout 弹出一个列表的值，将它推到另一个列表，并返回它;或阻塞，直到有一个可用
	 * 
	 * CLIENT KILL ip:port 关闭客户端连接
	 * 
	 * CLIENT LIST 获得客户端连接列表
	 * 
	 * CLIENT GETNAME 获得当前连接名称
	 * 
	 * CLIENT SETNAME connection-name 设置当前连接的名字
	 * 
	 * CONFIG GET parameter 获取配置参数的值
	 * 
	 * CONFIG SET parameter value 获取配置参数的值
	 * 
	 * CONFIG RESETSTAT 复位再分配使用info命令报告的统计
	 * 
	 * DBSIZE 返回当前数据库里面的keys数量
	 * 
	 * DEBUG OBJECT key 获取一个key的debug信息
	 * 
	 * DEBUG SEGFAULT 使服务器崩溃
	 * 
	 * DECR key 整数原子减1
	 * 
	 * DECRBY key decrement 原子减指定的整数
	 * 
	 * DEL key [key ...] 删除一个key
	 * 
	 * DISCARD 丢弃所有 MULTI 之后发的命令
	 * 
	 * DUMP key 导出key的值
	 * 
	 * ECHO message 回显输入的字符串
	 * 
	 * EVAL script numkeys key [key ...] arg [arg ...] 在服务器端执行 LUA 脚本
	 * 
	 * EVALSHA sha1 numkeys key [key ...] arg [arg ...] 在服务器端执行 LUA 脚本
	 * 
	 * EXEC 执行所有 MULTI 之后发的命令
	 * 
	 * EXISTS key 查询一个key是否存在
	 * 
	 * EXPIRE key seconds 设置一个key的过期的秒数
	 * 
	 * EXPIREAT key timestamp 设置一个UNIX时间戳的过期时间
	 * 
	 * FLUSHALL 清空所有数据库
	 * 
	 * FLUSHDB 清空当前的数据库
	 * 
	 * GET key 获取key的值
	 * 
	 * GETBIT key offset 返回位的值存储在关键的字符串值的偏移量。
	 * 
	 * GETRANGE key start end 获取存储在key上的值的一个子字符串
	 * 
	 * GETSET key value 设置一个key的value，并获取设置前的值
	 * 
	 * HDEL key field [field ...] 删除一个或多个哈希域
	 * 
	 * HEXISTS key field 判断给定域是否存在于哈希集中
	 * 
	 * HGET key field 读取哈希域的的值
	 * 
	 * HGETALL key 从哈希集中读取全部的域和值
	 * 
	 * HINCRBY key field increment 将哈希集中指定域的值增加给定的数字
	 * 
	 * HINCRBYFLOAT key field increment 将哈希集中指定域的值增加给定的浮点数
	 * 
	 * HKEYS key 获取hash的所有字段
	 * 
	 * HLEN key 获取hash里所有字段的数量
	 * 
	 * HMGET key field [field ...] 获取hash里面指定字段的值
	 * 
	 * HMSET key field value [field value ...] 设置hash字段值
	 * 
	 * HSET key field value 设置hash里面一个字段的值
	 * 
	 * HSETNX key field value 设置hash的一个字段，只有当这个字段不存在时有效
	 * 
	 * HVALS key 获得hash的所有值
	 * 
	 * INCR key 执行原子加1操作
	 * 
	 * INCRBY key increment 执行原子增加一个整数
	 * 
	 * INCRBYFLOAT key increment 执行原子增加一个浮点数
	 * 
	 * INFO [section] 获得服务器的详细信息
	 * 
	 * KEYS pattern 查找所有匹配给定的模式的键
	 * 
	 * LASTSAVE 获得最后一次同步磁盘的时间
	 * 
	 * LINDEX key index 获取一个元素，通过其索引列表
	 * 
	 * LINSERT key BEFORE|AFTER pivot value 在列表中的另一个元素之前或之后插入一个元素
	 * 
	 * LLEN key 获得队列(List)的长度
	 * 
	 * LPOP key 从队列的左边出队一个元素
	 * 
	 * LPUSH key value [value ...] 从队列的左边入队一个或多个元素
	 * 
	 * LPUSHX key value 当队列存在时，从队到左边入队一个元素
	 * 
	 * LRANGE key start stop 从列表中获取指定返回的元素
	 * 
	 * LREM key count value 从列表中删除元素
	 * 
	 * LSET key index value 设置队列里面一个元素的值
	 * 
	 * LTRIM key start stop 修剪到指定范围内的清单
	 * 
	 * MGET key [key ...] 获得所有key的值
	 * 
	 * MIGRATE host port key destination-db timeout 原子性的将key从redis的一个实例移到另一个实例
	 * 
	 * MONITOR 实时监控服务器
	 * 
	 * MOVE key db 移动一个key到另一个数据库
	 * 
	 * MSET key value [key value ...] 设置多个key value
	 * 
	 * MSETNX key value [key value ...] 设置多个key value,仅当key存在时
	 * 
	 * MULTI 标记一个事务块开始
	 * 
	 * OBJECT subcommand [arguments [arguments ...]] 检查内部的再分配对象
	 * 
	 * PERSIST key 移除key的过期时间
	 * 
	 * PEXPIRE key milliseconds 设置一个key的过期的毫秒数
	 * 
	 * PEXPIREAT key milliseconds-timestamp 设置一个带毫秒的UNIX时间戳的过期时间
	 * 
	 * PING Ping 服务器
	 * 
	 * PSETEX key milliseconds value Set the value and expiration in
	 * milliseconds of a key
	 * 
	 * PSUBSCRIBE pattern [pattern ...] 听出版匹配给定模式的渠道的消息
	 * 
	 * PTTL key 获取key的有效毫秒数
	 * 
	 * PUBLISH channel message 发布一条消息到频道
	 * 
	 * PUNSUBSCRIBE [pattern [pattern ...]] 停止发布到匹配给定模式的渠道的消息听
	 * 
	 * QUIT 关闭连接，退出
	 * 
	 * RANDOMKEY 返回一个随机的key
	 * 
	 * RENAME key newkey 将一个key重命名
	 * 
	 * RENAMENX key newkey 重命名一个key,新的key必须是不存在的key
	 * 
	 * RESTORE key ttl serialized-value Create a key using the provided
	 * serialized value, previously obtained using DUMP.
	 * 
	 * RPOP key 从队列的右边出队一个元素
	 * 
	 * RPOPLPUSH source destination 删除列表中的最后一个元素，将其追加到另一个列表
	 * 
	 * RPUSH key value [value ...] 从队列的右边入队一个元素
	 * 
	 * RPUSHX key value 从队列的右边入队一个元素，仅队列存在时有效
	 * 
	 * SADD key member [member ...] 添加一个或者多个元素到集合(set)里
	 * 
	 * SAVE 同步数据到磁盘上
	 * 
	 * SCARD key 获取集合里面的元素数量
	 * 
	 * SCRIPT EXISTS script [script ...] Check existence of scripts in the
	 * script cache.
	 * 
	 * SCRIPT FLUSH 删除服务器缓存中所有Lua脚本。
	 * 
	 * SCRIPT KILL 杀死当前正在运行的 Lua 脚本。
	 * 
	 * SCRIPT LOAD script 从服务器缓存中装载一个Lua脚本。
	 * 
	 * SDIFF key [key ...] 获得队列不存在的元素
	 * 
	 * SDIFFSTORE destination key [key ...] 获得队列不存在的元素，并存储在一个关键的结果集
	 * 
	 * SELECT index 选择数据库
	 * 
	 * SET key value 设置一个key的value值
	 * 
	 * SETBIT key offset value Sets or clears the bit at offset in the string
	 * value stored at key
	 * 
	 * SETEX key seconds value 设置key-value并设置过期时间（单位：秒）
	 * 
	 * SETNX key value 设置的一个关键的价值，只有当该键不存在
	 * 
	 * SETRANGE key offset value Overwrite part of a string at key starting at
	 * the specified offset
	 * 
	 * SHUTDOWN [NOSAVE] [SAVE] 关闭服务
	 * 
	 * SINTER key [key ...] 获得两个集合的交集
	 * 
	 * SINTERSTORE destination key [key ...] 获得两个集合的交集，并存储在一个关键的结果集
	 * 
	 * SISMEMBER key member 确定一个给定的值是一个集合的成员
	 */
	public static void main(String[] args) throws Exception {
		// Jedis jedis = new Jedis("127.0.0.1", 6379);
		// Jedis jedis = new Jedis("127.0.0.1");
		// jedis.set("anythink", "anythink-" + System.currentTimeMillis());
		// System.out.println(jedis.get("anythink"));

		// final JedisPubSub chnSet = new JedisSubMsg() {
		// @Override
		// public void onReceive(String channel, String message) {
		// System.out.println("onReceive1:" + channel + " " + message);
		// System.out.println(JSON.toMap(message));
		// }
		// };

		// final JedisPubSub chnHSet = new JedisSubMsg() {
		// @Override
		// public void onReceive(String channel, String message) {
		// System.out.println("onReceive2:" + channel + " " + message);
		// System.out.println(JSON.toMap(message));
		// }
		// };
		// final JedisPubSub chnHGen = new JedisSubMsg() {
		// @Override
		// public void onReceive(String channel, String message) {
		// System.out.println("onReceive3:" + channel + " " + message);
		// // System.out.println(JSON.toMap(message));
		// }
		// };

		new Thread(new Runnable() {
			@Override
			public void run() {
				subscribe(new JedisSubMsg() {
					@Override
					public void onReceive(String channel, String message) {
						System.out.println("onReceive1:" + channel + " "
								+ message);
						System.out.println(MyJson.toMap(message));
					}
				}, PUBSUB_CHN_SET);
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				subscribe(new JedisSubMsg() {
					@Override
					public void onReceive(String channel, String message) {
						System.out.println("onReceive2:" + channel + " "
								+ message);
						System.out.println(MyJson.toMap(message));
					}
				}, PUBSUB_CHN_HSET);
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				subscribe(new JedisSubMsg() {
					@Override
					public void onReceive(String channel, String message) {
						System.out.println("onReceive3:" + channel + " "
								+ message);
						// System.out.println(JSON.toMap(message));
					}
				});
			}
		}).start();

		ThreadEx.Sleep(1000);

		publish(PUBSUB_CHN_SET, JedisSubMsg.HELLO);
		publish(PUBSUB_CHN_HSET, JedisSubMsg.HELLO);
		publish(JedisSubMsg.HELLO_STR);

		set("a", "bb");
		set("a1", "bb2");
		set("a2", "bb3");
		hset("map", "field1", "value - 值1");
		hset("map", "field2", "value - 值21");
		// hdel("map", "field2");

		Map<byte[], byte[]> map = hgetAll("map");
		Set<Entry<byte[], byte[]>> en = map.entrySet();
		for (Entry<byte[], byte[]> e : en) {
			System.out.println(new String(e.getKey()) + ":"
					+ new String(e.getValue()));
		}

		final Map m1 = NewMap.create().add("1", 1).add("2", 2).toMap();
		putMap("m1", m1);
		System.out.println("map-" + getMap("m1"));
		System.out.println("map-" + getMap("m2"));

		putObject("o1", m1);
		System.out.println("object-" + getObject("o1"));
		System.out.println("object-" + getObject("m2"));

		System.out.println(new String(get("a")));
		System.out.println(new String(hget("map", "field1")));
		ThreadEx.Sleep(10000);

		System.exit(1);
	}

	// /////////////////////
	private static JedisPoolConfig config = null;
	private static JedisPool localPool = null;

	// /////////////////////
	public static final String PUBSUB_CHN_GEN = "GEN"; // General channel
	public static final String PUBSUB_CHN_SET = "SET"; // object update
	public static final String PUBSUB_CHN_LSET = "LSET"; // list update
	public static final String PUBSUB_CHN_HSET = "HSET"; // map update

	public static final String TYPE_UNKNOW = "unknow"; // (不能识别)
	public static final String TYPE_NONE = "none"; // (key不存在)
	public static final String TYPE_STRING = "string"; // (字符串)
	public static final String TYPE_LIST = "list"; // (列表)
	public static final String TYPE_SET = "set";// (集合)
	public static final String TYPE_ZSET = "zset"; // (有序集)
	public static final String TYPE_HASH = "hash"; // (哈希表)

	public static final int N_UNKNOW = -1; // (不能识别)
	public static final int N_NONE = 0; // (key不存在)
	public static final int N_STRING = 1; // (字符串)
	public static final int N_LIST = 2; // (列表)
	public static final int N_SET = 3;// (集合)
	public static final int N_ZSET = 4; // (有序集)
	public static final int N_HASH = 5; // (哈希表)

	public static final int ntype(final String type) {
		switch (type) {
		case TYPE_NONE:
			return N_NONE;
		case TYPE_STRING:
			return N_STRING;
		case TYPE_LIST:
			return N_LIST;
		case TYPE_SET:
			return N_SET;
		case TYPE_ZSET:
			return N_ZSET;
		case TYPE_HASH:
			return N_HASH;
		default:
			return N_UNKNOW;
		}
	}

	public static final boolean isType(final String key, final String type) {
		String t = type(key);
		if (t == null || t.isEmpty())
			return false;
		return t.equals(type);
	}

	public static final boolean isType(final String key, final int type) {
		String t = type(key);
		if (t == null || t.isEmpty())
			return false;
		return t.equals(stype(type));
	}

	public static final String stype(final int type) {
		switch (type) {
		case N_NONE:
			return TYPE_NONE;
		case N_STRING:
			return TYPE_STRING;
		case N_LIST:
			return TYPE_LIST;
		case N_SET:
			return TYPE_SET;
		case N_ZSET:
			return TYPE_ZSET;
		case N_HASH:
			return TYPE_HASH;
		default:
			return TYPE_UNKNOW;
		}
	}

	// /////////////////////
	public static final JedisPoolConfig getJedisPoolConfig() {
		if (config != null)
			return config;

		config = new JedisPoolConfig();
		config.setMaxIdle(64);
		config.setMinIdle(1);
		config.setMaxTotal(128);
		config.setMaxWaitMillis(4 * 1000);
		// config.testOnBorrow = true;
		// config.testOnReturn = true;
		config.setTestWhileIdle(true);
		return config;
	}

	public static final JedisPoolConfig newJedisPoolConfig(final int maxActive,
			final int maxIdle, final int minIdle, final int maxWait) {
		config = new JedisPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		config.setMaxTotal(maxActive);
		config.setMaxWaitMillis(maxWait);
		// config.testOnBorrow = true;
		// config.testOnReturn = true;
		config.setTestWhileIdle(true);
		return config;
	}

	// /////////////////////
	public static final JedisPool getJedisPool(JedisPoolConfig config,
			String host, int port) {
		return new JedisPool(config, host, port);
	}

	public static final JedisPool getJedisPool(String host, int port) {
		JedisPoolConfig config = getJedisPoolConfig();
		return new JedisPool(config, host, port);
	}

	public static final JedisPool getJedisPool(String host) {
		JedisPoolConfig config = getJedisPoolConfig();
		return new JedisPool(config, host);
	}

	public static final JedisPool getJedisLocalPool() {
		if (localPool != null)
			return localPool;
		String host = "127.0.0.1";
		JedisPoolConfig config = getJedisPoolConfig();
		localPool = new JedisPool(config, host);
		return localPool;
	}

	public static final JedisPool setJedisLocalPool(JedisPool pool) {
		localPool = pool;
		return localPool;
	}

	// /////////////////////

	public static final Jedis newJedis(final String host, final int port) {
		return new Jedis(host, port);
	}

	public static final Jedis newJedis(final String host) {
		return new Jedis(host);
	}

	// /////////////////////
	// public static Jedis localJedis = null;
	// public static Jedis localJedis2 = null;
	//
	// public static final Jedis localJedis() {
	// final String host = "127.0.0.1";
	// if (localJedis == null)
	// localJedis = new Jedis(host);
	// return localJedis;
	// }
	//
	// public static final Jedis localJedis2() {
	// final String host = "127.0.0.1";
	// if (localJedis2 == null)
	// localJedis2 = new Jedis(host);
	// return localJedis2;
	// }

	// /////////////////////
	// 检查Redis是否联通
	public static final String ping(final JedisPool pool) {
		final Jedis jedis = pool.getResource();
		try {
			return ping(jedis);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String ping(final Jedis jedis) {
		return jedis.ping();
	}

	public static final String ping() {
		final JedisPool pool = getJedisLocalPool();
		return ping(pool);
	}

	// DECR key 整数原子减1
	public static final Long decr(final JedisPool pool, final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return decr(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Long decr(final Jedis jedis, final String key) {
		return jedis.decr(key);
	}

	public static final Long decr(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return decr(pool, key);
	}

	// DECRBY key decrement 原子减指定的整数
	public static final Long decrBy(final JedisPool pool, final String key,
			final int val) {
		final Jedis jedis = pool.getResource();
		try {
			return decrBy(jedis, key, val);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Long decrBy(final Jedis jedis, final String key,
			final int val) {
		return jedis.decrBy(key, val);
	}

	public static final Long decrBy(final String key, final int val) {
		final JedisPool pool = getJedisLocalPool();
		return decrBy(pool, key, val);
	}

	// MULTI 标记一个事务块开始
	public static final Transaction multi(final JedisPool pool) {
		final Jedis jedis = pool.getResource();
		try {
			return multi(jedis);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Transaction multi(final Jedis jedis) {
		return jedis.multi();
	}

	public static final Transaction multi() {
		final JedisPool pool = getJedisLocalPool();
		return multi(pool);
	}

	// DISCARD 丢弃所有 MULTI 之后发的命令
	public static final String discard(final Transaction t) {
		return t.discard();
	}

	// EXEC 执行所有 MULTI 之后发的命令
	public static final List<Object> exec(final Transaction t) {
		return t.exec();
	}

	// FLUSHALL 清空所有数据库
	public static final String flushAll(final JedisPool pool) {
		final Jedis jedis = pool.getResource();
		try {
			return flushAll(jedis);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String flushAll(final Jedis jedis) {
		return jedis.flushAll();
	}

	public static final String flushAll() {
		final JedisPool pool = getJedisLocalPool();
		return flushAll(pool);
	}

	// FLUSHDB 清空当前的数据库
	public static final String flushDB(final JedisPool pool) {
		final Jedis jedis = pool.getResource();
		try {
			return flushDB(jedis);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String flushDB(final Jedis jedis) {
		return jedis.flushDB();
	}

	public static final String flushDB() {
		final JedisPool pool = getJedisLocalPool();
		return flushDB(pool);
	}

	// /////////////////////
	// 对象类型//none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
	public static final String type(final JedisPool pool, final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return type(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String type(final Jedis jedis, final String key) {
		return jedis.type(key);
	}

	public static final String type(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return type(pool, key);
	}

	// /////////////////////
	// 是否存在
	public static final boolean exists(final JedisPool pool, final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return exists(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final boolean exists(final Jedis jedis, final String key) {
		return jedis.exists(key);
	}

	public static final boolean exists(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return exists(pool, key);
	}

	// /////////////////////
	// 删除数据
	public static final Long remove(final JedisPool pool, final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return remove(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Long remove(final Jedis jedis, final String key) {
		String message = JedisSubMsg.delMsg(key);
		publish(jedis, PUBSUB_CHN_SET, message);
		return jedis.del(key);
	}

	public static final Long remove(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return remove(pool, key);
	}

	// /////////////////////
	public static final Long ttl(final JedisPool pool, final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return ttl(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Long ttl(final Jedis jedis, final String key) {
		return jedis.ttl(key2(key));
	}

	public static final Long ttl(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return ttl(pool, key);
	}

	public static final Long getTimeout(final JedisPool pool, final String key) {
		return ttl(pool, key);
	}

	public static final Long getTimeout(final Jedis jedis, final String key) {
		return ttl(jedis, key);
	}

	public static final Long getTimeout(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return ttl(pool, key);
	}

	// /////////////////////
	// 超时删除
	public static final void expire(final JedisPool pool, final String key,
			final int seconds) {
		final Jedis jedis = pool.getResource();
		try {
			expire(jedis, key, seconds);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final void expire(final Jedis jedis, final String key,
			final int seconds) {
		final byte[] key2 = key2(key);
		if (seconds <= 0) {
			jedis.persist(key2);
			return;
		}

		jedis.expire(key2, seconds);
	}

	public static final void expire(final String key, final int seconds) {
		final JedisPool pool = getJedisLocalPool();
		expire(pool, key, seconds);
	}

	public static final void expire(final String key, final Date date) {
		long delay = date.getTime() - System.currentTimeMillis();
		int seconds = (int) (delay <= 0 ? 1 : delay / 1000);

		final JedisPool pool = getJedisLocalPool();
		expire(pool, key, seconds);
	}

	// /////////////////////
	public static final void setTimeout(final JedisPool pool, final String key,
			final int seconds) {
		expire(pool, key, seconds);
	}

	public static final void setTimeout(final Jedis jedis, final String key,
			final int seconds) {
		expire(jedis, key, seconds);
	}

	public static final void setTimeout(final String key, final int seconds) {
		expire(key, seconds);
	}

	public static final void setTimeout(final String key, final Date date) {
		expire(key, date);
	}

	// /////////////////////
	// public static final String putBean(final JedisPool pool, final String
	// key,
	// BeanSupport bean) throws Exception {
	// final Jedis jedis = pool.getResource();
	// try {
	// return putBean(jedis, key, bean);
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// pool.returnResource(jedis);
	// }
	// }

	// public static final String putBean(final Jedis jedis, final String key,
	// BeanSupport bean) throws Exception {
	// final byte[] val = bean.toByteArray();
	//
	// String message = JedisSubMsg.setMsg(key);
	// publish(jedis, PUBSUB_CHN_SET, message);
	// return jedis.set(key2(key), val);
	// }

	// public static final String putBean(final String key, BeanSupport bean)
	// throws Exception {
	// final JedisPool pool = getJedisLocalPool();
	// return putBean(pool, key, bean);
	// }

	// /////////////////////
	public static final String putObject(final JedisPool pool,
			final String key, Object obj) throws Exception {
		final Jedis jedis = pool.getResource();
		try {
			return putObject(jedis, key, obj);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String putObject(final Jedis jedis, final String key,
			Object obj) throws Exception {
		final byte[] val = Toolkit.serialization(obj);

		String message = JedisSubMsg.setMsg(key);
		publish(jedis, PUBSUB_CHN_SET, message);
		return jedis.set(key2(key), val);
	}

	public static final String putObject(final String key, Object obj)
			throws Exception {
		final JedisPool pool = getJedisLocalPool();
		return putObject(pool, key, obj);
	}

	// /////////////////////
	public static final String putMap(final JedisPool pool, final String key,
			Map<?, ?> map) throws Exception {
		final Jedis jedis = pool.getResource();
		try {
			return putMap(jedis, key, map);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String putMap(final Jedis jedis, final String key,
			Map<?, ?> map) throws Exception {
		final byte[] val = B2Helper.toBytes(map);

		String message = JedisSubMsg.setMsg(key);
		publish(jedis, PUBSUB_CHN_SET, message);
		return jedis.set(key2(key), val);
	}

	public static final String putMap(final String key, Map<?, ?> map)
			throws Exception {
		final JedisPool pool = getJedisLocalPool();
		return putMap(pool, key, map);
	}

	// /////////////////////
	public static final String set(final JedisPool pool, final String key,
			String value) {
		final Jedis jedis = pool.getResource();
		try {
			return set(jedis, key, value);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String set(final Jedis jedis, final String key,
			String value) {
		final byte[] val = value.getBytes();

		String message = JedisSubMsg.setMsg(key);
		publish(jedis, PUBSUB_CHN_SET, message);
		return jedis.set(key2(key), val);
	}

	public static final String set(final String key, String value) {
		final JedisPool pool = getJedisLocalPool();
		return set(pool, key, value);
	}

	public static final String set(final JedisPool pool, final String key,
			byte[] value) {
		final Jedis jedis = pool.getResource();
		try {
			return set(jedis, key, value);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String set(final Jedis jedis, final String key,
			byte[] value) {
		final byte[] val = value;

		String message = JedisSubMsg.setMsg(key);
		publish(jedis, PUBSUB_CHN_SET, message);
		return jedis.set(key2(key), val);
	}

	public static final String set(final String key, byte[] value) {
		final JedisPool pool = getJedisLocalPool();
		return set(pool, key, value);
	}

	// /////////////////////
	// /////////////////////

	public static final Object getObject(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = pool.getResource();
		try {
			return getObject(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Object getObject(final Jedis jedis, final String key)
			throws Exception {
		byte[] b = jedis.get(key2(key));
		return Toolkit.deserialization(b);
	}

	public static final Object getObject(final String key) throws Exception {
		final JedisPool pool = getJedisLocalPool();
		return getObject(pool, key);
	}

	// /////////////////////
	public static final NewMap getMap(final JedisPool pool, final String key)
			throws Exception {
		final Jedis jedis = pool.getResource();
		try {
			return getMap(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final NewMap getMap(final Jedis jedis, final String key)
			throws Exception {
		byte[] b = jedis.get(key2(key));
		return B2Helper.toMap(b);
	}

	public static final NewMap getMap(final String key) throws Exception {
		final JedisPool pool = getJedisLocalPool();
		return getMap(pool, key);
	}

	// /////////////////////
	public static final byte[] get(final JedisPool pool, final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return get(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final byte[] get(final Jedis jedis, final String key) {
		return jedis.get(key2(key));
	}

	public static final byte[] get(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return get(pool, key);
	}

	// /////////////////////
	public static final List<byte[]> mget(final JedisPool pool,
			final byte[]... keys) {
		final Jedis jedis = pool.getResource();
		try {
			return mget(jedis, keys);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final List<byte[]> mget(final Jedis jedis,
			final byte[]... keys) {
		return jedis.mget(keys);
	}

	public static final List<byte[]> mget(final byte[]... keys) {
		final JedisPool pool = getJedisLocalPool();
		return mget(pool, keys);
	}

	// /////////////////////
	// HSET key field value 设置hash里面一个字段的值
	public static final Long hset(final JedisPool pool, final String key,
			final String field, final String value) {
		final byte[] val2 = value.getBytes();
		return hset(pool, key, field, val2);
	}

	public static final Long hset(final JedisPool pool, final String key,
			final String field, final byte[] value) {
		final Jedis jedis = pool.getResource();
		try {
			return hset(jedis, key, field, value);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Long hset(final Jedis jedis, final String key,
			final String field, final String value) {
		final byte[] val2 = value.getBytes();
		return hset(jedis, key, field, val2);
	}

	public static final Long hset(final Jedis jedis, final String key,
			final String field, final byte[] value) {
		final byte[] field2 = field.getBytes();

		String message = JedisSubMsg.hsetMsg(key, field);
		publish(jedis, PUBSUB_CHN_HSET, message);

		return jedis.hset(key2(key), field2, value);
	}

	public static final Long hset(final String key, final String field,
			final String value) {
		final byte[] val2 = value.getBytes();
		return hset(key, field, val2);
	}

	public static final Long hset(final String key, final String field,
			final byte[] value) {
		final JedisPool pool = getJedisLocalPool();
		return hset(pool, key, field, value);
	}

	// /////////////////////
	// HDEL key field [field ...] 删除一个或多个哈希域
	public static final Long hdel(final JedisPool pool, final String key,
			final String field) {
		final Jedis jedis = pool.getResource();
		try {
			return hdel(jedis, key, field);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Long hdel(final Jedis jedis, final String key,
			final String field) {
		final byte[] field2 = field.getBytes();

		String message = JedisSubMsg.hdelMsg(key, field);
		publish(jedis, PUBSUB_CHN_HSET, message);

		return jedis.hdel(key2(key), field2);
	}

	public static final Long hdel(final String key, final String field) {
		final JedisPool pool = getJedisLocalPool();
		return hdel(pool, key, field);
	}

	// /////////////////////
	// HGET key field 读取哈希域的的值
	public static final byte[] hget(final JedisPool pool, final String key,
			final String field) {
		final Jedis jedis = pool.getResource();
		try {
			return hget(jedis, key, field);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final byte[] hget(final Jedis jedis, final String key,
			final String field) {
		final byte[] field2 = field.getBytes();
		return jedis.hget(key2(key), field2);
	}

	public static final byte[] hget(final String key, final String field) {
		final JedisPool pool = getJedisLocalPool();
		return hget(pool, key, field);
	}

	// /////////////////////
	// HGETALL key 从哈希集中读取全部的域和值
	public static final Map<byte[], byte[]> hgetAll(final JedisPool pool,
			final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return hgetAll(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final Map<byte[], byte[]> hgetAll(final Jedis jedis,
			final String key) {
		return jedis.hgetAll(key2(key));
	}

	public static final Map<byte[], byte[]> hgetAll(final String key) {
		final JedisPool pool = getJedisLocalPool();
		return hgetAll(pool, key);
	}

	// /////////////////////
	public static final byte[] getSet(final JedisPool pool, final String key,
			final byte[] value2) {
		final Jedis jedis = pool.getResource();
		try {
			return getSet(jedis, key, value2);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final byte[] getSet(final Jedis jedis, final String key,
			final byte[] value2) {
		String message = JedisSubMsg.setMsg(key);
		publish(jedis, PUBSUB_CHN_SET, message);
		return jedis.getSet(key2(key), value2);
	}

	public static final byte[] getSet(final String key, final byte[] value2) {
		final JedisPool pool = getJedisLocalPool();
		return getSet(pool, key, value2);
	}

	public static final byte[] getSet(final JedisPool pool, final String key,
			final String value) {
		final Jedis jedis = pool.getResource();
		final byte[] value2 = value.getBytes();
		try {
			return getSet(jedis, key, value2);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final byte[] getSet(final Jedis jedis, final String key,
			final String value) {
		final byte[] value2 = value.getBytes();
		String message = JedisSubMsg.setMsg(key);
		publish(jedis, PUBSUB_CHN_SET, message);
		return jedis.getSet(key2(key), value2);
	}

	public static final byte[] getSet(final String key, final String value) {
		final JedisPool pool = getJedisLocalPool();
		final byte[] value2 = value.getBytes();
		return getSet(pool, key, value2);
	}

	// /////////////////////
	public static final void publish(final JedisPool pool,
			final String channel, final String message) {
		final Jedis jedis = pool.getResource();
		try {
			publish(jedis, channel, message);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final void publish(final JedisPool pool, final String str) {
		final Jedis jedis = pool.getResource();
		try {
			publish(jedis, str);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final void publish(final Jedis jedis, final String channel,
			final String message) {
		jedis.publish(channel, message);
	}

	public static final void publish(final Jedis jedis, final String str) {
		final String channel = PUBSUB_CHN_GEN;
		// final String message = JedisSubMsg.genMsg(str);
		jedis.publish(channel, str);
	}

	public static final void publish(final String channel, final String message) {
		final JedisPool pool = getJedisLocalPool();
		publish(pool, channel, message);
	}

	public static final void publish(final String str) {
		final JedisPool pool = getJedisLocalPool();
		publish(pool, str);
	}

	// /////////////////////

	// public static final Long publish(final Jedis jedis, final String channel,
	// final byte[] message) {
	// return jedis.publish(channel.getBytes(), message);
	// }

	// public static final void subscribe(final Jedis jedis,
	// final JedisPubSub jedisPubSub, final String channel) {
	// jedis.psubscribe(jedisPubSub, new String[] { channel });
	// }

	// /////////////////////
	public static final void subscribe(final JedisPool pool,
			final JedisSubMsg jedisPubSub, final String channel) {
		final Jedis jedis = pool.getResource();
		try {
			subscribe(jedis, jedisPubSub, channel);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final void subscribe(final Jedis jedis,
			final JedisSubMsg jedisPubSub, final String channel) {
		jedis.subscribe(jedisPubSub, channel);
	}

	public static final void subscribe(final JedisSubMsg jedisPubSub,
			final String channel) {
		final JedisPool pool = getJedisLocalPool();
		subscribe(pool, jedisPubSub, channel);
	}

	public static final void subscribe(final JedisSubMsg jedisPubSub) {
		final JedisPool pool = getJedisLocalPool();
		final String channel = PUBSUB_CHN_GEN;
		subscribe(pool, jedisPubSub, channel);
	}

	// /////////////////////
	public static final String mset(final JedisPool pool,
			final byte[]... keysvalues) {
		final Jedis jedis = pool.getResource();
		try {
			return mset(jedis, keysvalues);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String mset(final Jedis jedis,
			final byte[]... keysvalues) {
		return jedis.mset(keysvalues);
	}

	public static final String mset(final byte[]... keysvalues) {
		final JedisPool jedis = getJedisLocalPool();
		return mset(jedis, keysvalues);
	}

	public static final String mset(final JedisPool pool,
			final Map<String, String> keysvalues) {
		final Jedis jedis = pool.getResource();
		try {
			return mset(jedis, keysvalues);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	public static final String mset(final Jedis jedis,
			final Map<String, String> keysvalues) {
		Set<Entry<String, String>> entrys = keysvalues.entrySet();
		String[] _keysvalues = new String[keysvalues.size()];
		int p = 0;
		for (Entry<String, String> e : entrys) {
			_keysvalues[p++] = e.getKey();
			_keysvalues[p++] = e.getValue();
		}
		return jedis.mset(_keysvalues);
	}

	public static final String mset(Map<String, String> keysvalues) {
		final JedisPool jedis = getJedisLocalPool();
		return mset(jedis, keysvalues);
	}

	// public static final String mset(final byte[]... keysvalues) {
	// final Jedis jedis = localJedis();
	// return mset(jedis, keysvalues);
	// }

	// public static final byte[] serialization(final Object obj)
	// throws IOException {
	// if (obj == null)
	// return new byte[0];
	// ByteOutStream out = new ByteOutStream();
	// try {
	// ObjectOutputStream oos = new ObjectOutputStream(out);
	// oos.writeObject(obj);
	// oos.close();
	// out.close();
	// return out.toByteArray();
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// OutputArrayPool.returnObject(out);
	// }
	// }
	//
	// public static final Object deserialization(final byte[] b) throws
	// Exception {
	// if (b == null || b.length <= 0)
	// return null;
	// try {
	// ByteInStream in = new ByteInStream(b);
	// ObjectInputStream ois = new ObjectInputStream(in);
	// return ois.readObject();
	// } catch (Exception e) {
	// throw e;
	// } finally {
	// }
	// }

	public static final byte[] key2(final byte[] key) {
		return key;
	}

	public static final byte[] key2(final String key) {
		return key.getBytes();
	}

	public static final byte[] val2(final byte[] val) {
		return val;
	}

	public static final byte[] val2(final String val) {
		return val.getBytes();
	}

	// public static final byte[] val2(final BeanSupport val) {
	// try {
	// return val.toByteArray();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }

	public static final byte[] val2(final Object val) {
		if (val instanceof BeanSupport)
			return val2((BeanSupport) val);
		try {
			return Toolkit.serialization(val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
