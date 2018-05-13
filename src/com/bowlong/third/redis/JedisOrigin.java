package com.bowlong.third.redis;

import java.io.Serializable;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import com.bowlong.Toolkit;
import com.bowlong.lang.StrEx;
import com.bowlong.sql.jdbc.BeanSupport;
import com.bowlong.util.MapEx;

/**
 * jedis -- redis 的config与pool
 * 
 * @author Canyon
 */
@SuppressWarnings("rawtypes")
public class JedisOrigin implements Serializable {

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

	/**
	 * 标识
	 */
	private static final long serialVersionUID = 1L;

	// ///////////////////// 类型 /////////////////////
	static public final String PUBSUB_CHN_GEN = "GEN"; // General channel
	static public final String PUBSUB_CHN_SET = "SET"; // object update
	static public final String PUBSUB_CHN_LSET = "LSET"; // list update
	static public final String PUBSUB_CHN_HSET = "HSET"; // map update

	static public final String TYPE_UNKNOW = "unknow"; // (不能识别)
	static public final String TYPE_NONE = "none"; // (key不存在)
	static public final String TYPE_STRING = "string"; // (字符串)
	static public final String TYPE_LIST = "list"; // (列表)
	static public final String TYPE_SET = "set";// (集合)
	static public final String TYPE_ZSET = "zset"; // (有序集)
	static public final String TYPE_HASH = "hash"; // (哈希表)

	static public final int N_UNKNOW = -1; // (不能识别)
	static public final int N_NONE = 0; // (key不存在)
	static public final int N_STRING = 1; // (字符串)
	static public final int N_LIST = 2; // (列表)
	static public final int N_SET = 3;// (集合)
	static public final int N_ZSET = 4; // (有序集)
	static public final int N_HASH = 5; // (哈希表)

	static public final int ntype(final String type) {
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

	// 对象类型//none(key不存在),string(字符串),list(列表),set(集合),zset(有序集),hash(哈希表)
	static public final String type(final JedisPool pool, final String key) {
		final Jedis jedis = pool.getResource();
		try {
			return type(jedis, key);
		} catch (Exception e) {
			throw e;
		} finally {
			pool.returnResource(jedis);
		}
	}

	static public final String type(final Jedis jedis, final String key) {
		return jedis.type(key);
	}

	static public final String type(final String key) {
		final JedisPool pool = getJedisPool();
		return type(pool, key);
	}

	static public final boolean isType(final String key, final String type) {
		String t = type(key);
		if (t == null || t.isEmpty())
			return false;
		return t.equals(type);
	}

	static public final boolean isType(final String key, final int type) {
		String t = type(key);
		if (t == null || t.isEmpty())
			return false;
		return t.equals(stype(type));
	}

	static public final String stype(final int type) {
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

	static public final byte[] key2(final byte[] key) {
		return key;
	}

	static public final byte[] key2(final String key) {
		return key.getBytes();
	}

	static public final byte[] val2(final byte[] val) {
		return val;
	}

	static public final byte[] val2(final String val) {
		return val.getBytes();
	}

	static public final byte[] val2(final Object val) {
		if (val instanceof BeanSupport) {
			BeanSupport tt = ((BeanSupport) val);
			try {
				return Toolkit.serialization(tt.toBasicMap());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		try {
			return Toolkit.serialization(val);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ///////////////////// 配置与池对象 /////////////////////
	static public JedisPoolConfig config = null;

	static public final JedisPoolConfig newConfig(final int maxActive,
			final int maxIdle, final int minIdle, final int maxWait,
			final boolean testOnBorrow, final boolean testOnReturn,
			final boolean testWhileIdle,
			final int timeBetweenEvictionRunsMillis,
			final int numTestsPerEvictionRun,
			final int minEvictableIdleTimeMillis) {
		config = new JedisPoolConfig();

		// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示不限制
		config.setMaxTotal(maxActive);

		// 控制一个pool最多有多少个状态为idle的jedis实例
		config.setMaxIdle(maxIdle);

		// 控制一个pool至少有多少个状态为idle的jedis实例
		config.setMinIdle(minIdle);

		// 表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException
		config.setMaxWaitMillis(maxWait);

		// 在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的
		config.setTestOnBorrow(testOnBorrow);

		// 在return给pool时，是否提前进行validate操作
		config.setTestOnReturn(testOnReturn);

		// 如果为true，表示有一个idle object evitor线程
		// 对idle object进行扫描，如果validate失败，此object会被从pool中drop掉
		// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
		config.setTestWhileIdle(testWhileIdle);

		// 表示idle object evitor两次扫描之间要sleep的毫秒数
		config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

		// 表示idle object evitor每次扫描的最多的对象数
		config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);

		// 表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐
		// 这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		return config;
	}

	static public final JedisPoolConfig newConfig(final int maxActive,
			final int maxIdle, final int minIdle, final int maxWait) {
		return newConfig(maxActive, maxIdle, minIdle, maxWait, true, true,
				true, 60 * 1000, 3000, 30 * 1000);
	}

	static public final JedisPoolConfig newPoolConfig(final int maxActive,
			final int maxIdle, final int minIdle, final int maxWait) {
		config = new JedisPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setMinIdle(minIdle);
		config.setMaxTotal(maxActive);
		config.setMaxWaitMillis(maxWait);
		config.setTestWhileIdle(true);
		return config;
	}

	static public final JedisPoolConfig getJedisPoolConfig() {
		if (config != null)
			return config;
		return newConfig(128, 64, 1, 4 * 1000);
	}

	// ///////////////////// 连接池 /////////////////////
	static public JedisPool jedsPool = null;

	static public final JedisPool getJedisPool(JedisPoolConfig config,
			String host, int port, int timeOut, String password, int dbIndex) {

		boolean isPwd = true;
		if (StrEx.isEmpty(password)) {
			password = null;
			isPwd = false;
		}

		boolean isNotDef = dbIndex > 0;
		if (isNotDef) {
			return new JedisPool(config, host, port, timeOut, password, dbIndex);
		}

		if (isPwd) {
			return new JedisPool(config, host, port, timeOut, password);
		}

		boolean isOut = timeOut > 0;
		if (isOut) {
			return new JedisPool(config, host, port, timeOut);
		}

		return new JedisPool(config, host, port);
	}

	static public final JedisPool getJedisPool(JedisPoolConfig config,
			String host, int port) {
		return getJedisPool(config, host, port, 0, null, 0);
	}

	static public final JedisPool getJedisPool(String host, int port,String pwd,int dbIndex) {
		final JedisPoolConfig config = getJedisPoolConfig();
		return getJedisPool(config, host, port, 10000, pwd, dbIndex);
	}
	
	static public final JedisPool getJedisPool(String host, int port) {
		final JedisPoolConfig config = getJedisPoolConfig();
		return getJedisPool(config, host, port);
	}

	static public final JedisPool getJedisPool(String host) {
		JedisPoolConfig config = getJedisPoolConfig();
		return getJedisPool(config, host, 6379);
	}

	static public final JedisPool setJedisPool(JedisPool pool) {
		jedsPool = pool;
		return jedsPool;
	}

	static public final JedisPool getJedisPool() {
		if (jedsPool != null)
			return jedsPool;
		String host = "127.0.0.1";
		jedsPool = getJedisPool(host);
		return jedsPool;
	}

	static public final JedisPool resetJedisPool(Map redisConfig) {
		if (MapEx.isEmpty(redisConfig)) {
			return null;
		}
		// config 配置 参数
		int maxActive = MapEx.getInt(redisConfig, "maxActive");
		int maxIdle = MapEx.getInt(redisConfig, "maxIdle");
		int minIdle = MapEx.getInt(redisConfig, "minIdle");
		int maxWait = MapEx.getInt(redisConfig, "maxWait");
		boolean testOnBorrow = MapEx.getBoolean(redisConfig, "testOnBorrow");
		boolean testOnReturn = MapEx.getBoolean(redisConfig, "testOnReturn");
		boolean testWhileIdle = MapEx.getBoolean(redisConfig, "testWhileIdle");
		int timeBetweenEvictionRunsMillis = MapEx.getInt(redisConfig,
				"timeBetweenEvictionRunsMillis");
		int numTestsPerEvictionRun = MapEx.getInt(redisConfig,
				"numTestsPerEvictionRun");
		int minEvictableIdleTimeMillis = MapEx.getInt(redisConfig,
				"minEvictableIdleTimeMillis");

		config = newConfig(maxActive, maxIdle, minIdle, maxWait, testOnBorrow,
				testOnReturn, testWhileIdle, timeBetweenEvictionRunsMillis,
				numTestsPerEvictionRun, minEvictableIdleTimeMillis);

		// jedis pool 参数
		String host = MapEx.getString(redisConfig, "host");
		int timeOut = MapEx.getInt(redisConfig, "timeOut");
		int port = MapEx.getInt(redisConfig, "port");
		String password = MapEx.getString(redisConfig, "pwd");
		int dbIndex = MapEx.getInt(redisConfig, "dbIndex");

		jedsPool = getJedisPool(config, host, port, timeOut, password, dbIndex);
		return jedsPool;
	}

	/** 关闭数据 **/
	static public final void closeJedisPool(JedisPool pool) {
		if (pool != null)
			pool.destroy();
	}

	// ///////////////////// jedis 对象 /////////////////////

	static public final Jedis newJedis(final String host, final int port,
			final int timeOut) {
		if (timeOut > 0)
			return new Jedis(host, port, timeOut);
		return new Jedis(host, port);
	}

	static public final Jedis newJedis(final String host) {
		return new Jedis(host);
	}

	/** 返回错误的redis **/
	static public final void returnJedisWhenError(JedisPool pool, Jedis resource) {
		if (pool == null || resource == null)
			return;
		try {
			pool.returnBrokenResource(resource);
		} catch (Exception e) {
		}
	}

	static public final void returnJedisWhenError(Jedis resource) {
		returnJedisWhenError(jedsPool, resource);
	}

	/** 返回 redis **/
	static public final void returnJedis(JedisPool pool, Jedis resource) {
		if (resource == null)
			return;
		try {
			if (pool == null) {
				resource.disconnect();
			} else {
				pool.returnResource(resource);
			}
		} catch (Exception e) {
		}
	}

	static public final void returnJedis(Jedis resource) {
		returnJedis(jedsPool, resource);
	}

	static public final Jedis getJedis(JedisPool pool) {
		if (pool == null)
			return null;
		Jedis result = null;
		int exceNum = 0;
		while (result == null) {
			try {
				exceNum++;
				result = pool.getResource();
			} catch (Exception e) {
				returnJedisWhenError(pool, result);
			}
			if (exceNum >= 100) {
				exceNum = 0;
				break;
			}
		}
		return result;
	}

	static public final Jedis getJedis() {
		return getJedis(jedsPool);
	}

	/** 选择数据库 默认是[0~15] **/
	static public final Jedis selectDb(Jedis r, int index) {
		if (r == null || index < 0)
			return r;
		r.select(index);
		return r;
	}

	// ///////////////////// jedis的通道pipeline对象 /////////////////////
	static public final Pipeline getPipeline(JedisPool pool) {
		Jedis jedis = getJedis(pool);
		if (jedis != null) {
			return jedis.pipelined();
		}
		return null;
	}
	// /////////////////////
}
