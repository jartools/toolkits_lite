package com.bowlong.third.redis;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.ZParams;
import redis.clients.util.Slowlog;

import com.bowlong.util.MapEx;

public class JedisX {
	final JedisPool pool_w;

	public JedisX(final JedisPool pool_w) {
		this.pool_w = pool_w;
	}

	// APPEND key value
	// 追加一个值到key上
	public Long append(final int key, final String value) {
		return append(ts(key), value);
	}

	public Long append(final int key, final int value) {
		return append(ts(key), ts(value));
	}

	public Long append(final String key, final int value) {
		return append(key, ts(value));
	}

	public Long append(final String key, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.append(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long append(final byte[] key, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.append(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// AUTH password
	// 验证服务器
	public String auth(final String password) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.auth(password);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// BGREWRITEAOF
	// 异步重写追加文件
	public String bgrewriteaof() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bgrewriteaof();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// BGSAVE
	// 异步保存数据集到磁盘上
	public String bgsave() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bgsave();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// BITCOUNT key [start] [end]
	// 统计字符串指定起始位置的字节数
	public Long bitcount(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bitcount(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}

		return 0L;
	}

	public Long bitcount(final int key) {
		return bitcount(ts(key));
	}

	public Long bitcount(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bitcount(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long bitcount(final byte[] key, long start, long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bitcount(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long bitcount(final int key, long start, long end) {
		return bitcount(ts(key), start, end);
	}

	public Long bitcount(final String key, long start, long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bitcount(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// BITOP operation destkey key [key ...]
	// Perform bitwise operations between strings
	public Long bitop(BitOP op, final byte[] destKey, byte[]... srcKeys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bitop(op, destKey, srcKeys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long bitop(BitOP op, final String destKey, String... srcKeys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.bitop(op, destKey, srcKeys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// BLPOP key [key ...] timeout
	// 删除，并获得该列表中的第一元素，或阻塞，直到有一个可用
	public List<byte[]> bitop(byte[] arg) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.blpop(arg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<byte[]> bitop(byte[]... args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.blpop(args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> bitop(int arg) {
		return bitop(ts(arg));
	}

	public List<String> bitop(String arg) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.blpop(arg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public List<String> bitop(String... args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.blpop(args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public List<byte[]> bitop(final int timeout, final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.blpop(timeout, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> bitop(final int timeout, final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.blpop(timeout, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	// BRPOP key [key ...] timeout
	// 删除，并获得该列表中的最后一个元素，或阻塞，直到有一个可用
	public List<byte[]> brpop(final byte[] arg) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpop(arg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<byte[]> brpop(final byte[]... args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpop(args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> brpop(final int arg) {
		return brpop(ts(arg));
	}

	public List<String> brpop(final String arg) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpop(arg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public List<String> brpop(final String... args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpop(args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public List<byte[]> brpop(final int timeout, final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpop(timeout, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> brpop(final int timeout, final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpop(timeout, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	// BRPOPLPUSH source destination timeout
	// 弹出一个列表的值，将它推到另一个列表，并返回它;或阻塞，直到有一个可用
	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpoplpush(source, destination, timeout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String brpoplpush(String source, String destination, int timeout) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.brpoplpush(source, destination, timeout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// CLIENT KILL ip:port
	// 关闭客户端连接
	public String clientKill(final byte[] client) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.clientKill(client);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String clientKill(final String client) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.clientKill(client);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// CLIENT LIST
	// 获得客户端连接列表
	public String clientList() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.clientList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// CLIENT GETNAME
	// 获得当前连接名称
	public String clientGetname() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.clientGetname();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// CLIENT SETNAME connection-name
	// 设置当前连接的名字
	public String clientSetname(final byte[] name) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.clientSetname(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String clientSetname(final String name) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.clientSetname(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// CONFIG GET parameter
	// 获取配置参数的值
	public List<byte[]> configGet(final byte[] pattern) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.configGet(pattern);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	// CONFIG SET parameter value
	// 获取配置参数的值
	public String configSet(final String parameter, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.configSet(parameter, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public byte[] configSet(final byte[] parameter, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.configSet(parameter, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new byte[0];
	}

	// public void closeStore() {
	// configSet("appendonly", "no");
	// configSet("save", "");
	// }

	// CONFIG RESETSTAT
	// 复位再分配使用info命令报告的统计
	public String configResetStat() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.configResetStat();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// DBSIZE
	// 返回当前数据库里面的keys数量
	public Long dbSize() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.dbSize();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// DEBUG OBJECT key
	// 获取一个key的debug信息
	// DEBUG SEGFAULT
	// 使服务器崩溃
	public String debug(final DebugParams params) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.debug(params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// DECR key
	// 整数原子减1
	public Long decr(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.decr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long decr(final int key) {
		return decr(ts(key));
	}

	public Long decr(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.decr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// DECRBY key decrement
	// 原子减指定的整数
	public Long decrBy(final byte[] key, final int intValue) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.decrBy(key, intValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long decrBy(final int key, final int intValue) {
		return decrBy(ts(key), intValue);
	}

	public Long decrBy(final String key, final int intValue) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.decrBy(key, intValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// DEL key [key ...]
	// 删除一个key
	public Long del(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long del(final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.del(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long del(final int key) {
		return del(ts(key));
	}

	public Long del(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long del(final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.del(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// DISCARD
	// 丢弃所有 MULTI 之后发的命令
	public String discard(final Transaction t) {
		// Jedis jedis = pool.getResource();
		try {
			return t.discard();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool.returnResource(jedis);
		}
		return "";
	}

	// DUMP key
	// 导出key的值
	public byte[] dump(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.dump(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public byte[] dump(final int key) {
		return dump(ts(key));
	}

	public byte[] dump(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.dump(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	// ECHO message
	// 回显输入的字符串
	public byte[] echo(final byte[] str) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.echo(str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String echo(final int nval) {
		return echo(ts(nval));
	}

	public String echo(final String str) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.echo(str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// EVAL script numkeys key [key ...] arg [arg ...]
	// 在服务器端执行 LUA 脚本
	public Object eval(final byte[] script) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.eval(script);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object eval(final String script) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.eval(script);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object eval(final byte[] script, byte[] keyCount, byte[]... params) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.eval(script, keyCount, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object eval(final byte[] script, int keyCount, byte[]... params) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.eval(script, keyCount, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.eval(script, keys, args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object eval(final String script, int keyCount, String... params) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.eval(script, keyCount, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object eval(final String script, List<String> keys, List<String> args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.eval(script, keys, args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	// EVALSHA sha1 numkeys key [key ...] arg [arg ...]
	// 在服务器端执行 LUA 脚本
	public Object evalsha(byte[] sha1) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.evalsha(sha1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object evalsha(String sha1) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.evalsha(sha1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.evalsha(sha1, keyCount, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.evalsha(sha1, keys, args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object evalsha(String sha1, int keyCount, String... params) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.evalsha(sha1, keyCount, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public Object evalsha(String sha1, List<String> keys, List<String> args) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.evalsha(sha1, keys, args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	// EXEC
	// 执行所有 MULTI 之后发的命令
	public List<Object> exec(final Transaction t) {
		// Jedis jedis = pool.getResource();
		try {
			return t.exec();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool.returnResource(jedis);
		}
		return null;
	}

	// EXISTS key
	// 查询一个key是否存在
	public Boolean exists(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public Boolean exists(final int key) {
		return exists(ts(key));
	}

	public Boolean exists(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	// EXPIRE key seconds
	// 设置一个key的过期的秒数
	public Long expire(final byte[] key, int seconds) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long expire(final String key, int seconds) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// EXPIREAT key timestamp
	// 设置一个UNIX时间戳的过期时间
	public Long expireAt(final byte[] key, final long unixTime) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.expireAt(key, unixTime);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long expireAt(final int key, final long unixTime) {
		return expireAt(ts(key), unixTime);
	}

	public Long expireAt(final String key, final long unixTime) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.expireAt(key, unixTime);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// FLUSHALL
	// 清空所有数据库
	public String flushAll() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.flushAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// FLUSHDB
	// 清空当前的数据库
	public String flushDB() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.flushDB();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// GET key
	// 获取key的值
	public byte[] get(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String get(final int key) {
		return get(ts(key));
	}

	public String get(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// GETBIT key offset
	// 返回位的值存储在关键的字符串值的偏移量。
	public Boolean getbit(final byte[] key, final long offset) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.getbit(key, offset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public Boolean getbit(final int key, final long offset) {
		return getbit(ts(key), offset);
	}

	public Boolean getbit(final String key, final long offset) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.getbit(key, offset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	// GETRANGE key start end
	// 获取存储在key上的值的一个子字符串
	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String getrange(int key, long startOffset, long endOffset) {
		return getrange(ts(key), startOffset, endOffset);
	}

	public String getrange(String key, long startOffset, long endOffset) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// GETSET key value
	// 设置一个key的value，并获取设置前的值
	public byte[] getSet(final byte[] key, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.getSet(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String getSet(final int key, final String value) {
		return getSet(ts(key), value);
	}

	public String getSet(final int key, final int value) {
		return getSet(ts(key), ts(value));
	}

	public String getSet(final String key, final int value) {
		return getSet(key, ts(value));
	}

	public String getSet(final String key, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.getSet(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// HDEL key field [field ...]
	// 删除一个或多个哈希域
	public Long hdel(final byte[] key, final byte[]... fields) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hdel(key, fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long hdel(final int key, final String... fields) {
		return hdel(ts(key), fields);
	}

	public Long hdel(final String key, final String... fields) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hdel(key, fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// HEXISTS key field
	// 判断给定域是否存在于哈希集中
	public Boolean hexists(final byte[] key, final byte[] field) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public Boolean hexists(final int key, final String field) {
		return hexists(ts(key), field);
	}

	public Boolean hexists(final String key, final String field) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hexists(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	// HGET key field
	// 读取哈希域的的值
	public byte[] hget(final byte[] key, final byte[] field) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String hget(final int key, final String field) {
		return hget(ts(key), field);
	}

	public String hget(final String key, final String field) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hget(key, field);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// HGETALL key
	// 从哈希集中读取全部的域和值
	public Map<byte[], byte[]> hgetAll(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashMap<byte[], byte[]>();
	}

	public Map<String, String> hgetAll(final int key) {
		return hgetAll(ts(key));
	}

	public Map<String, String> hgetAll(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashMap<String, String>();
	}

	// HINCRBY key field increment
	// 将哈希集中指定域的值增加给定的数字
	public Long hincrBy(final byte[] key, final byte[] field, final long value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long hincrBy(final int key, final String field, final long value) {
		return hincrBy(ts(key), field, value);
	}

	public Long hincrBy(final String key, final String field, final long value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// HINCRBYFLOAT key field increment
	// 将哈希集中指定域的值增加给定的浮点数
	public Double hincrByFloat(final byte[] key, final byte[] field,
			double increment) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hincrByFloat(key, field, increment);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	public Double hincrByFloat(final int key, final String field,
			double increment) {
		return hincrByFloat(ts(key), field, increment);
	}

	public Double hincrByFloat(final String key, final String field,
			double increment) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hincrByFloat(key, field, increment);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	// HKEYS key
	// 获取hash的所有字段
	public Set<String> hincrByFloat(final int key) {
		return hincrByFloat(ts(key));
	}

	public Set<String> hincrByFloat(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hkeys(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// HLEN key
	// 获取hash里所有字段的数量
	public Long hlen(final int key) {
		return hlen(ts(key));
	}

	public Long hlen(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long hlen(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// HMGET key field [field ...]
	// 获取hash里面指定字段的值
	public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public List<String> hmget(final int key, final String... fields) {
		return hmget(ts(key), fields);
	}

	public List<String> hmget(final String key, final String... fields) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hmget(key, fields);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	// HMSET key field value [field value ...]
	// 设置hash字段值
	public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hmset(key, hash);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String hmset(final int key, final Map<String, String> hash) {
		return hmset(ts(key), hash);
	}

	public String hmset(final String key, final Map<String, String> hash) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hmset(key, hash);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// HSET key field value
	// 设置hash里面一个字段的值
	public Long hset(final byte[] key, final byte[] field, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long hset(final int key, final String field, final String value) {
		return hset(ts(key), field, value);
	}

	public Long hset(final int key, final String field, final int value) {
		return hset(ts(key), field, ts(value));
	}

	public Long hset(final String key, final String field, final int value) {
		return hset(key, field, ts(value));
	}

	public Long hset(final String key, final String field, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hset(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// HSETNX key field value
	// 设置hash的一个字段，只有当这个字段不存在时有效
	public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hsetnx(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long hsetnx(final int key, final String field, final String value) {
		return hsetnx(ts(key), field, value);
	}

	public Long hsetnx(final int key, final String field, final int value) {
		return hsetnx(ts(key), field, ts(value));
	}

	public Long hsetnx(final String key, final String field, final int value) {
		return hsetnx(key, field, ts(value));
	}

	public Long hsetnx(final String key, final String field, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hsetnx(key, field, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// HVALS key
	// 获得hash的所有值
	public List<byte[]> hvals(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> hvals(final int key) {
		return hvals(ts(key));
	}

	public List<String> hvals(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.hvals(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	// INCR key
	// 执行原子加1操作
	public Long incr(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long incr(final int key) {
		return incr(ts(key));
	}

	public Long incr(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// INCRBY key increment
	// 执行原子增加一个整数
	public Long incrBy(final byte[] key, int num) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.incrBy(key, num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long incrBy(final int key, int num) {
		return incrBy(ts(key), num);
	}

	public Long incrBy(final String key, int num) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.incrBy(key, num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// INCRBYFLOAT key increment
	// 执行原子增加一个浮点数
	public Double incrBy(final byte[] key, float increment) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.incrByFloat(key, increment);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	public Double incrBy(final int key, float increment) {
		return incrBy(ts(key), increment);
	}

	public Double incrBy(final String key, float increment) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.incrByFloat(key, increment);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	// INFO [section]
	// 获得服务器的详细信息
	public String info() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.info();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String info(String section) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.info(section);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String info_Server() {
		// # Server
		// redis_version:2.6.12
		// redis_git_sha1:00000000
		// redis_git_dirty:0
		// redis_mode:standalone
		// os:Windows
		// arch_bits:32
		// multiplexing_api:winsock_IOCP
		// gcc_version:0.0.0
		// process_id:2284
		// run_id:e02e92bedae22bf7caf2650cdf1b8bccb889cc35
		// tcp_port:6379
		// uptime_in_seconds:709738
		// uptime_in_days:0
		// hz:8
		// lru_clock:0
		return info("Server");
	}

	public int tcp_port() {
		String str = info_Server();
		return MapEx.getInt(toMap(str), "tcp_port");
	}

	public String info_Clients() {
		// # Clients
		// connected_clients:12
		// client_longest_output_list:0
		// client_biggest_input_buf:0
		// blocked_clients:0
		return info("Clients");
	}

	public int connected_clients() {
		String str = info_Memory();
		return MapEx.getInt(toMap(str), "connected_clients");
	}

	public String info_Memory() {
		// # Memory
		// used_memory:811452
		// used_memory_human:792.43K
		// used_memory_rss:811452
		// used_memory_peak:4119800
		// used_memory_peak_human:3.93M
		// used_memory_lua:23552
		// mem_fragmentation_ratio:1.00
		// mem_allocator:libc
		return info("Memory");
	}

	public int used_memory() {
		String str = info_Memory();
		return MapEx.getInt(toMap(str), "used_memory");
	}

	public int used_memory_peak() {
		String str = info_Memory();
		return MapEx.getInt(toMap(str), "used_memory_peak");
	}

	public String info_Persistence() {
		// # Persistence
		// loading:0
		// rdb_changes_since_last_save:364
		// rdb_bgsave_in_progress:0
		// rdb_last_save_time:1388730918
		// rdb_last_bgsave_status:ok
		// rdb_last_bgsave_time_sec:0
		// rdb_current_bgsave_time_sec:-1
		// aof_enabled:1
		// aof_rewrite_in_progress:0
		// aof_rewrite_scheduled:0
		// aof_last_rewrite_time_sec:1388732037
		// aof_current_rewrite_time_sec:-1
		// aof_last_bgrewrite_status:ok
		// aof_current_size:54280
		// aof_base_size:22839
		// aof_pending_rewrite:0
		// aof_buffer_length:0
		// aof_rewrite_buffer_length:0
		// aof_pending_bio_fsync:0
		// aof_delayed_fsync:0
		return info("Persistence");
	}

	public String info_Stats() {
		// # Stats
		// total_connections_received:7
		// total_commands_processed:324
		// instantaneous_ops_per_sec:0
		// rejected_connections:0
		// expired_keys:0
		// evicted_keys:0
		// keyspace_hits:121
		// keyspace_misses:5
		// pubsub_channels:1
		// pubsub_patterns:1
		// latest_fork_usec:0
		return info("Stats");
	}

	public String info_Replication() {
		// # Replication
		// role:master
		// connected_slaves:0
		return info("Replication");
	}

	public String info_CPU() {
		// # CPU
		// used_cpu_sys:7.73
		// used_cpu_user:6.36
		// used_cpu_sys_children:0.00
		// used_cpu_user_children:0.00
		return info("CPU");
	}

	public double used_cpu_sys() {
		String str = info_CPU();
		return MapEx.getDouble(toMap(str), "used_cpu_sys");
	}

	public double used_cpu_user() {
		String str = info_CPU();
		return MapEx.getDouble(toMap(str), "used_cpu_user");
	}

	public String info_Keyspace() {
		// # Keyspace
		// db0:keys=20,expires=0
		return info("Keyspace");
	}

	// KEYS pattern
	// 查找所有匹配给定的模式的键
	public Set<byte[]> keys(final byte[] pattern) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.keys(pattern);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> keys(final String pattern) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.keys(pattern);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// LASTSAVE
	// 获得最后一次同步磁盘的时间
	public Long lastsave() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lastsave();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// LINDEX key index
	// 获取一个元素，通过其索引列表
	public byte[] lindex(final byte[] key, final long index) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lindex(key, index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String lindex(final int key, final long index) {
		return lindex(ts(key), index);
	}

	public String lindex(final String key, final long index) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lindex(key, index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// LINSERT key BEFORE|AFTER pivot value
	// 在列表中的另一个元素之前或之后插入一个元素
	public Long linsert(final byte[] key, final LIST_POSITION where,
			final byte[] pivot, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long linsert(final String key, final LIST_POSITION where,
			final String pivot, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// LLEN key
	// 获得队列(List)的长度
	public Long llen(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long llen(final int key) {
		return llen(ts(key));
	}

	public Long llen(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.llen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// LPOP key
	// 从队列的左边出队一个元素
	public byte[] lpop(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String lpop(final int key) {
		return lpop(ts(key));
	}

	public String lpop(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// LPUSH key value [value ...]
	// 从队列的左边入队一个或多个元素
	public Long lpush(final byte[] key, byte[]... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lpush(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long lpush(final int key, String... strs) {
		return lpush(ts(key), strs);
	}

	public Long lpush(final String key, int... vals) {
		int Count = vals.length;
		String[] strs = new String[Count];
		for (int i = 0; i < Count; i++) {
			strs[i] = Integer.toString(vals[i]);
		}
		return lpush(key, strs);
	}

	public Long lpush(final String key, String... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lpush(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// LPUSHX key value
	// 当队列存在时，从队到左边入队一个元素
	public Long lpushx(final byte[] key, byte[]... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lpushx(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long lpushx(final int key, String... strs) {
		return lpushx(ts(key), strs);
	}

	public Long lpushx(final String key, String... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lpushx(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// LRANGE key start stop
	// 从列表中获取指定返回的元素
	public List<byte[]> lrange(final byte[] key, final long start,
			final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> lrange(final int key, final long start, final long end) {
		return lrange(ts(key), start, end);
	}

	public List<String> lrange(final String key, final long start,
			final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	// LREM key count value
	// 从列表中删除元素
	public Long lrem(final byte[] key, final long count, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lrem(key, count, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long lrem(final int key, final long count, final String value) {
		return lrem(ts(key), count, value);
	}

	public Long lrem(final int key, final long count, final int value) {
		return lrem(ts(key), count, ts(value));
	}

	public Long lrem(final String key, final long count, final int value) {
		return lrem(key, count, ts(value));
	}

	public Long lrem(final String key, final long count, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lrem(key, count, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// LSET key index value
	// 设置队列里面一个元素的值
	public String lset(final byte[] key, final long count, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lset(key, count, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String lset(final int key, final long count, final String value) {
		return lset(ts(key), count, value);
	}

	public String lset(final int key, final long count, final int value) {
		return lset(ts(key), count, ts(value));
	}

	public String lset(final String key, final long count, final int value) {
		return lset(key, count, ts(value));
	}

	public String lset(final String key, final long count, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.lset(key, count, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// LTRIM key start stop
	// 修剪到指定范围内的清单
	public String ltrim(final byte[] key, final long start, final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.ltrim(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String ltrim(final int key, final long start, final long end) {
		return ltrim(ts(key), start, end);
	}

	public String ltrim(final String key, final long start, final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.ltrim(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// MGET key [key ...]
	// 获得所有key的值
	public List<byte[]> mget(final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.mget(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> mget(final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.mget(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	// MIGRATE host port key destination-db timeout
	// 原子性的将key从redis的一个实例移到另一个实例
	public String migrate(final byte[] host, final int port, final byte[] key,
			final int destinationDb, final int timeout) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.migrate(host, port, key, destinationDb, timeout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String migrate(final String host, final int port, final String key,
			final int destinationDb, final int timeout) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.migrate(host, port, key, destinationDb, timeout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// MONITOR
	// 实时监控服务器
	public void monitor(final JedisMonitor jedisMonitor) {
		final Jedis jedis = pool_w.getResource();
		try {
			jedis.monitor(jedisMonitor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
	}

	// MOVE key db
	// 移动一个key到另一个数据库
	public Long move(final byte[] key, final int dbIndex) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.move(key, dbIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long move(final int key, final int dbIndex) {
		return move(ts(key), dbIndex);
	}

	public Long move(final String key, final int dbIndex) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.move(key, dbIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// MSET key value [key value ...]
	// 设置多个key value
	public String mset(final byte[]... keysvalues) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.mset(keysvalues);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String mset(final String... keysvalues) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.mset(keysvalues);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// MSETNX key value [key value ...]
	// 设置多个key value,仅当key存在时
	public Long msetnx(final byte[]... keysvalues) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.msetnx(keysvalues);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long msetnx(final String... keysvalues) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.msetnx(keysvalues);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// MULTI
	// 标记一个事务块开始
	// public Transaction multi() {
	// final Jedis jedis = pool_w.getResource();
	// try {
	// return jedis.multi();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// pool_w.returnResource(jedis);
	// }
	// return null;
	// }

	// public List<Object> multi(final TransactionBlock jedisTransaction) {
	// final Jedis jedis = pool_w.getResource();
	// try {
	// return jedis.multi(jedisTransaction);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// pool_w.returnResource(jedis);
	// }
	// return new ArrayList<Object>();
	// }

	// OBJECT subcommand [arguments [arguments ...]]
	// 检查内部的再分配对象
	// PERSIST key
	// 移除key的过期时间
	public Long persist(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.persist(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long persist(final int key) {
		return persist(ts(key));
	}

	public Long persist(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.persist(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// PEXPIRE key milliseconds
	// 设置一个key的过期的毫秒数
	public Long pexpire(final byte[] key, final int milliseconds) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.pexpire(key, milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long pexpire(final int key, final int milliseconds) {
		return pexpire(ts(key), milliseconds);
	}

	public Long pexpire(final String key, final int milliseconds) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.pexpire(key, milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// PEXPIREAT key milliseconds-timestamp
	// 设置一个带毫秒的UNIX时间戳的过期时间
	public Long pexpireAt(final byte[] key, final long millisecondsTimestamp) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.pexpireAt(key, millisecondsTimestamp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long pexpireAt(final int key, final long millisecondsTimestamp) {
		return pexpireAt(ts(key), millisecondsTimestamp);
	}

	public Long pexpireAt(final String key, final long millisecondsTimestamp) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.pexpireAt(key, millisecondsTimestamp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// PING
	// Ping 服务器
	public String ping() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.ping();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// PSETEX key milliseconds value
	// Set the value and expiration in milliseconds of a key
	public String psetex(final byte[] key, final int milliseconds,
			final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.psetex(key, milliseconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String psetex(final int key, final int milliseconds,
			final String value) {
		return psetex(ts(key), milliseconds, value);
	}

	public String psetex(final int key, final int milliseconds, final int value) {
		return psetex(ts(key), milliseconds, ts(value));
	}

	public String psetex(final String key, final int milliseconds,
			final int value) {
		return psetex(key, milliseconds, ts(value));
	}

	public String psetex(final String key, final int milliseconds,
			final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.psetex(key, milliseconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// PSUBSCRIBE pattern [pattern ...]
	// 听出版匹配给定模式的渠道的消息
	public void psubscribe(final BinaryJedisPubSub jedisPubSub,
			final byte[]... patterns) {
		final Jedis jedis = pool_w.getResource();
		try {
			jedis.psubscribe(jedisPubSub, patterns);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
	}

	public void psubscribe(final JedisPubSub jedisPubSub,
			final String... patterns) {
		final Jedis jedis = pool_w.getResource();
		try {
			jedis.psubscribe(jedisPubSub, patterns);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
	}

	// PTTL key
	// 获取key的有效毫秒数
	public Long pttl(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.pttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long pttl(final int key) {
		return pttl(ts(key));
	}

	public Long pttl(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.pttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// PUBLISH channel message
	// 发布一条消息到频道
	public Long publish(final byte[] channel, final byte[] message) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.publish(channel, message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long publish(final String channel, final String message) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.publish(channel, message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// PUNSUBSCRIBE [pattern [pattern ...]]
	// 停止发布到匹配给定模式的渠道的消息听
	// QUIT
	// 关闭连接，退出
	public String quit() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.quit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// RANDOMKEY
	// 返回一个随机的key
	public String randomKey() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.randomKey();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// RENAME key newkey
	// 将一个key重命名
	public String rename(final byte[] oldkey, final byte[] newkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rename(oldkey, newkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String rename(final int oldkey, final String newkey) {
		return rename(ts(oldkey), newkey);
	}

	public String rename(final int oldkey, final int newkey) {
		return rename(ts(oldkey), ts(newkey));
	}

	public String rename(final String oldkey, final int newkey) {
		return rename(oldkey, ts(newkey));
	}

	public String rename(final String oldkey, final String newkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rename(oldkey, newkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// RENAMENX key newkey
	// 重命名一个key,新的key必须是不存在的key
	public Long renamenx(final byte[] oldkey, final byte[] newkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.renamenx(oldkey, newkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long renamenx(final int oldkey, final String newkey) {
		return renamenx(ts(oldkey), newkey);
	}

	public Long renamenx(final int oldkey, final int newkey) {
		return renamenx(ts(oldkey), ts(newkey));
	}

	public Long renamenx(final String oldkey, final int newkey) {
		return renamenx(oldkey, ts(newkey));
	}

	public Long renamenx(final String oldkey, final String newkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.renamenx(oldkey, newkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// RESTORE key ttl serialized-value
	// Create a key using the provided serialized value, previously obtained
	public String restore(final byte[] key, final int ttl,
			final byte[] serializedValue) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.restore(key, ttl, serializedValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String restore(final int key, final int ttl,
			final byte[] serializedValue) {
		return restore(ts(key), ttl, serializedValue);
	}

	public String restore(final String key, final int ttl,
			final byte[] serializedValue) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.restore(key, ttl, serializedValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// using DUMP.
	// RPOP key
	// 从队列的右边出队一个元素
	public byte[] rpop(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String rpop(final int key) {
		return rpop(ts(key));
	}

	public String rpop(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// RPOPLPUSH source destination
	// 删除列表中的最后一个元素，将其追加到另一个列表
	public byte[] rpoplpush(final byte[] srckey, final byte[] dstkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpoplpush(srckey, dstkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String rpoplpush(final int srckey, final String dstkey) {
		return rpoplpush(ts(srckey), dstkey);
	}

	public String rpoplpush(final int srckey, final int dstkey) {
		return rpoplpush(ts(srckey), ts(dstkey));
	}

	public String rpoplpush(final String srckey, final int dstkey) {
		return rpoplpush(srckey, ts(dstkey));
	}

	public String rpoplpush(final String srckey, final String dstkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpoplpush(srckey, dstkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// RPUSH key value [value ...]
	// 从队列的右边入队一个元素
	public Long rpush(final byte[] key, final byte[]... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpush(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long rpush(final int key, final String... strs) {
		return rpush(ts(key), strs);
	}

	public Long rpush(final String key, final String... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpush(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// RPUSHX key value
	// 从队列的右边入队一个元素，仅队列存在时有效
	public Long rpushx(final byte[] key, final byte[]... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpushx(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long rpushx(final int key, final String... strs) {
		return rpushx(ts(key), strs);
	}

	public Long rpushx(final String key, final String... strs) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.rpushx(key, strs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SADD key member [member ...]
	// 添加一个或者多个元素到集合(set)里
	public Long sadd(final byte[] key, final byte[]... members) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sadd(key, members);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long sadd(final int key, final String... members) {
		return sadd(ts(key), members);
	}

	public Long sadd(final String key, final String... members) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sadd(key, members);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SAVE
	// 同步数据到磁盘上
	public String save() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.save();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SCARD key
	// 获取集合里面的元素数量
	public Long scard(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long scard(final int key) {
		return scard(ts(key));
	}

	public Long scard(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SCRIPT EXISTS script [script ...]
	// Check existence of scripts in the script cache.
	public List<Long> scriptExists(final byte[] sha1) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scriptExists(sha1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<Long>();
	}

	public Boolean scriptExists(final String sha1) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scriptExists(sha1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public List<Boolean> scriptExists(final String... shas1) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scriptExists(shas1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<Boolean>();
	}

	// SCRIPT FLUSH
	// 删除服务器缓存中所有Lua脚本。
	public String scriptFlush() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scriptFlush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SCRIPT KILL
	// 杀死当前正在运行的 Lua 脚本。
	public String scriptKill() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scriptKill();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SCRIPT LOAD script
	// 从服务器缓存中装载一个Lua脚本。
	public byte[] scriptLoad(byte[] script) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scriptLoad(script);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String scriptLoad(String script) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.scriptLoad(script);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SDIFF key [key ...]
	// 获得队列不存在的元素
	public Set<byte[]> sdiff(final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sdiff(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> sdiff(final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sdiff(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// SDIFFSTORE destination key [key ...]
	// 获得队列不存在的元素，并存储在一个关键的结果集
	public Long sdiffstore(final byte[] dstkey, final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sdiffstore(dstkey, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long sdiffstore(final String dstkey, final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sdiffstore(dstkey, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SELECT index
	// 选择数据库
	public String select(final int index) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.select(index);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SET key value
	// 设置一个key的value值
	public String set(final byte[] key, byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String set(final int key, String value) {
		return set(ts(key), value);
	}

	public String set(final int key, int value) {
		return set(ts(key), ts(value));
	}

	public String set(final String key, int value) {
		return set(key, ts(value));
	}

	public String set(final String key, String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String set(final byte[] key, byte[] value, byte[] nxxx) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value, nxxx);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String set(final String key, String value, String nxxx) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value, nxxx);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String set(final byte[] key, byte[] value, byte[] nxxx, byte[] expx,
			int time) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value, nxxx, expx, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String set(final int key, String value, String nxxx, String expx,
			int time) {
		return set(ts(key), value, nxxx, expx, time);
	}

	public String set(final int key, int value, String nxxx, String expx,
			int time) {
		return set(ts(key), ts(value), nxxx, expx, time);
	}

	public String set(final String key, int value, String nxxx, String expx,
			int time) {
		return set(key, ts(value), nxxx, expx, time);
	}

	public String set(final String key, String value, String nxxx, String expx,
			int time) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value, nxxx, expx, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String set(final byte[] key, byte[] value, byte[] nxxx, byte[] expx,
			long time) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value, nxxx, expx, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String set(final int key, String value, String nxxx, String expx,
			long time) {
		return set(ts(key), value, nxxx, expx, time);
	}

	public String set(final int key, int value, String nxxx, String expx,
			long time) {
		return set(ts(key), ts(value), nxxx, expx, time);
	}

	public String set(final String key, int value, String nxxx, String expx,
			long time) {
		return set(key, ts(value), nxxx, expx, time);
	}

	public String set(final String key, String value, String nxxx, String expx,
			long time) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.set(key, value, nxxx, expx, time);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SETBIT key offset value
	// Sets or clears the bit at offset in the string value stored at key
	public Boolean setbit(byte[] key, long offset, byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setbit(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public Boolean setbit(int key, long offset, String value) {
		return setbit(ts(key), offset, value);
	}

	public Boolean setbit(int key, long offset, int value) {
		return setbit(ts(key), offset, ts(value));
	}

	public Boolean setbit(String key, long offset, int value) {
		return setbit(key, offset, ts(value));
	}

	public Boolean setbit(String key, long offset, String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setbit(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public Boolean setbit(byte[] key, long offset, boolean value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setbit(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public Boolean setbit(int key, long offset, boolean value) {
		return setbit(ts(key), offset, value);
	}

	public Boolean setbit(String key, long offset, boolean value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setbit(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	// SETEX key seconds value
	// 设置key-value并设置过期时间（单位：秒）
	public String setex(final byte[] key, final int seconds, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String setex(final int key, final int seconds, final String value) {
		return setex(ts(key), seconds, value);
	}

	public String setex(final int key, final int seconds, final int value) {
		return setex(ts(key), seconds, ts(value));
	}

	public String setex(final String key, final int seconds, final int value) {
		return setex(key, seconds, ts(value));
	}

	public String setex(final String key, final int seconds, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SETNX key value
	// 设置的一个关键的价值，只有当该键不存在
	public Long setnx(final byte[] key, final byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setnx(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long setnx(final int key, final String value) {
		return setnx(ts(key), value);
	}

	public Long setnx(final int key, final int value) {
		return setnx(ts(key), ts(value));
	}

	public Long setnx(final String key, final int value) {
		return setnx(key, ts(value));
	}

	public Long setnx(final String key, final String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setnx(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SETRANGE key offset value
	// Overwrite part of a string at key starting at the specified offset
	public Long setrange(byte[] key, long offset, byte[] value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setrange(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long setrange(int key, long offset, String value) {
		return setrange(ts(key), offset, value);
	}

	public Long setrange(int key, long offset, int value) {
		return setrange(ts(key), offset, ts(value));
	}

	public Long setrange(String key, long offset, int value) {
		return setrange(key, offset, ts(value));
	}

	public Long setrange(String key, long offset, String value) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.setrange(key, offset, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SHUTDOWN [NOSAVE] [SAVE]
	// 关闭服务
	public String shutdown() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SINTER key [key ...]
	// 获得两个集合的交集
	public Set<byte[]> sinter(final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sinter(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> sinter(final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sinter(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// SINTERSTORE destination key [key ...]
	// 获得两个集合的交集，并存储在一个关键的结果集
	public Long sinterstore(final byte[] dstkey, final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sinterstore(dstkey, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long sinterstore(final int dstkey, final String... keys) {
		return sinterstore(ts(dstkey), keys);
	}

	public Long sinterstore(final String dstkey, final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sinterstore(dstkey, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SISMEMBER key member
	// 确定一个给定的值是一个集合的成员
	public Boolean sismember(final byte[] key, final byte[] member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sismember(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	public Boolean sismember(final int key, final String member) {
		return sismember(ts(key), member);
	}

	public Boolean sismember(final String key, final String member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sismember(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return false;
	}

	// SLAVEOF host port
	// 指定当前服务器的主服务器
	public String slaveof(final String host, final int port) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.slaveof(host, port);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SLOWLOG subcommand [argument]
	// 管理再分配的慢查询日志
	public List<Slowlog> slowlogGet() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.slowlogGet();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<Slowlog>();
	}

	public List<Slowlog> slowlogGet(long entries) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.slowlogGet(entries);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<Slowlog>();
	}

	public List<byte[]> slowlogGetBinary() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.slowlogGetBinary();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<byte[]> slowlogGetBinary(long entries) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.slowlogGetBinary(entries);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	// SMEMBERS key
	// 获取集合里面的所有key
	public Set<byte[]> smembers(byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.smembers(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> smembers(int key) {
		return smembers(ts(key));
	}

	public Set<String> smembers(String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.smembers(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// SMOVE source destination member
	// 移动集合里面的一个key到另一个集合
	public Long smove(final byte[] srckey, final byte[] dstkey,
			final byte[] member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.smove(srckey, dstkey, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long smove(final int srckey, final String dstkey, final String member) {
		return smove(ts(srckey), dstkey, member);
	}

	public Long smove(final int srckey, final int dstkey, final String member) {
		return smove(ts(srckey), ts(dstkey), member);
	}

	public Long smove(final String srckey, final int dstkey, final String member) {
		return smove(srckey, ts(dstkey), member);
	}

	public Long smove(final String srckey, final String dstkey,
			final String member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.smove(srckey, dstkey, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SORT key [BY pattern] [LIMIT offset count] [GET pattern [GET pattern
	// ...]] [ASC|DESC] [ALPHA] [STORE destination]
	// 对队列、集合、有序集合排序
	public List<byte[]> sort(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> sort(final int key) {
		return sort(ts(key));
	}

	public List<String> sort(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public Long sort(final byte[] key, final byte[] dstkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key, dstkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long sort(final int key, final String dstkey) {
		return sort(ts(key), dstkey);
	}

	public Long sort(final int key, final int dstkey) {
		return sort(ts(key), ts(dstkey));
	}

	public Long sort(final String key, final int dstkey) {
		return sort(key, ts(dstkey));
	}

	public Long sort(final String key, final String dstkey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key, dstkey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public List<byte[]> sort(final byte[] key,
			final SortingParams sortingParameters) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key, sortingParameters);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	public List<String> sort(final int key,
			final SortingParams sortingParameters) {
		return sort(ts(key), sortingParameters);
	}

	public List<String> sort(final String key,
			final SortingParams sortingParameters) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key, sortingParameters);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public Long sort(final byte[] key, final SortingParams sortingParameters,
			final byte[] dstKey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key, sortingParameters, dstKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long sort(final int key, final SortingParams sortingParameters,
			final String dstKey) {
		return sort(ts(key), sortingParameters, dstKey);
	}

	public Long sort(final int key, final SortingParams sortingParameters,
			final int dstKey) {
		return sort(ts(key), sortingParameters, ts(dstKey));
	}

	public Long sort(final String key, final SortingParams sortingParameters,
			final String dstKey) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sort(key, sortingParameters, dstKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SPOP key
	// 删除并获取一个集合里面的元素
	public byte[] spop(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.spop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public String spop(final int key) {
		return spop(ts(key));
	}

	public String spop(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.spop(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// SRANDMEMBER key [count]
	// 从集合里面随机获取一个key
	public String srandmember(final int key) {
		return srandmember(ts(key));
	}

	public String srandmember(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.srandmember(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public byte[] srandmember(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.srandmember(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	public List<String> srandmember(final int key, int count) {
		return srandmember(ts(key), count);
	}

	public List<String> srandmember(final String key, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.srandmember(key, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	public List<byte[]> srandmember(final byte[] key, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.srandmember(key, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<byte[]>();
	}

	// SREM key member [member ...]
	// 从集合里删除一个或多个key
	public Long srem(final byte[] key, final byte[]... member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.srem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long srem(final int key, final String... member) {
		return srem(ts(key), member);
	}

	public Long srem(final String key, final String... member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.srem(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// STRLEN key
	// 获取指定key值的长度
	public Long strlen(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.strlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long strlen(final int key) {
		return strlen(ts(key));
	}

	public Long strlen(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.strlen(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SUBSCRIBE channel [channel ...]
	// 聆听发布途径的消息
	public void subscribe(final JedisPubSub jedisPubSub,
			final String... channels) {
		final Jedis jedis = pool_w.getResource();
		try {
			jedis.subscribe(jedisPubSub, channels);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
	}

	public void subscribe(final BinaryJedisPubSub jedisPubSub,
			final byte[]... channels) {
		final Jedis jedis = pool_w.getResource();
		try {
			jedis.subscribe(jedisPubSub, channels);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
	}

	// SUNION key [key ...]
	// 添加多个set元素
	public Set<byte[]> sunion(final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sunion(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> sunion(final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sunion(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// SUNIONSTORE destination key [key ...]
	// 合并set元素，并将结果存入新的set里面
	public Long sunionstore(final byte[] dstkey, final byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sunionstore(dstkey, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long sunionstore(final int dstkey, final String... keys) {
		return sunionstore(ts(dstkey), keys);
	}

	public Long sunionstore(final String dstkey, final String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.sunionstore(dstkey, keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// SYNC
	// 用于复制的内部命令
	public void sync() {
		final Jedis jedis = pool_w.getResource();
		try {
			jedis.sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
	}

	public void sync(final Pipeline pipe) {
		// final Jedis jedis = pool_w.getResource();
		try {
			pipe.sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool_w.returnResource(jedis);
		}
	}

	public List<Object> syncAndReturnAll(final Pipeline pipe) {
		// final Jedis jedis = pool_w.getResource();
		try {
			return pipe.syncAndReturnAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool_w.returnResource(jedis);
		}
		return new ArrayList<Object>();
	}

	// TIME
	// 返回当前服务器时间
	public List<String> time() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.time();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new ArrayList<String>();
	}

	// TTL key
	// 获取key的有效时间（单位：秒）
	public Long ttl(byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long ttl(int key) {
		return ttl(ts(key));
	}

	public Long ttl(String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.ttl(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// TYPE key
	// 获取key的存储类型
	public String type(byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.type(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String type(int key) {
		return type(ts(key));
	}

	public String type(String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.type(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// UNSUBSCRIBE [channel [channel ...]]
	// 停止发布途径的消息听
	public void unsubscribe(final JedisPubSub pubsub) {
		// Jedis jedis = pool.getResource();
		try {
			pubsub.unsubscribe();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool.returnResource(jedis);
		}
	}

	public void unsubscribe(final JedisPubSub pubsub, String... channels) {
		// Jedis jedis = pool.getResource();
		try {
			pubsub.unsubscribe(channels);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool.returnResource(jedis);
		}
	}

	// UNWATCH
	// 取消事务
	public String unwatch() {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.unwatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// WATCH key [key ...]
	// 锁定key直到执行了 MULTI/EXEC 命令
	public Response<String> watch(final Transaction t, byte[]... keys) {
		// final Jedis jedis = pool_w.getResource();
		try {
			return t.watch(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool_w.returnResource(jedis);
		}
		return null;
	}

	public Response<String> watch(final Transaction t, String... keys) {
		// final Jedis jedis = pool_w.getResource();
		try {
			return t.watch(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// pool_w.returnResource(jedis);
		}
		return null;
	}

	public String watch(byte[]... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.watch(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	public String watch(String... keys) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.watch(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return "";
	}

	// ZADD key score member [score member ...]
	// 添加到有序set的一个或多个成员，或更新的分数，如果它已经存在
	public Long zadd(final byte[] key, final double score, final byte[] member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zadd(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zadd(final int key, final double score, final String member) {
		return zadd(ts(key), score, member);
	}

	public Long zadd(final String key, final double score, final String member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zadd(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zadd(key, scoreMembers);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zadd(final int key, final Map<String, Double> scoreMembers) {
		return zadd(ts(key), scoreMembers);
	}

	public Long zadd(final String key, final Map<String, Double> scoreMembers) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zadd(key, scoreMembers);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZCARD key
	// 获取一个排序的集合中的成员数量
	public Long zcard(final byte[] key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zcard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zcard(final int key) {
		return zcard(ts(key));
	}

	public Long zcard(final String key) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zcard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZCOUNT key min max
	// 给定值范围内的成员数与分数排序
	public Long zcount(final byte[] key, final byte[] min, final byte[] max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zcount(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zcount(final byte[] key, final double min, final double max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zcount(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zcount(final int key, final String min, final String max) {
		return zcount(ts(key), min, max);
	}

	public Long zcount(final int key, final int min, final int max) {
		return zcount(ts(key), ts(min), ts(max));
	}

	public Long zcount(final String key, final int min, final int max) {
		return zcount(key, ts(min), ts(max));
	}

	public Long zcount(final String key, final String min, final String max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zcount(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zcount(final int key, final double min, final double max) {
		return zcount(ts(key), min, max);
	}

	public Long zcount(final String key, final double min, final double max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zcount(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZINCRBY key increment member
	// 增量的一名成员在排序设置的评分
	public Double zincrby(final byte[] key, final double score,
			final byte[] member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zincrby(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	public Double zincrby(final int key, final double score, final String member) {
		return zincrby(ts(key), score, member);
	}

	public Double zincrby(final String key, final double score,
			final String member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zincrby(key, score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	// ZINTERSTORE destination numkeys key [key ...] [WEIGHTS weight [weight
	// ...]] [AGGREGATE SUM|MIN|MAX]
	// 相交多个排序集，导致排序的设置存储在一个新的关键
	public Long zinterstore(final int dstkey, final String... sets) {
		return zinterstore(ts(dstkey), sets);
	}

	public Long zinterstore(final String dstkey, final String... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zinterstore(dstkey, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zinterstore(final byte[] dstkey, final byte[]... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zinterstore(dstkey, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zinterstore(final int dstkey, ZParams params,
			final String... sets) {
		return zinterstore(ts(dstkey), params, sets);
	}

	public Long zinterstore(final String dstkey, ZParams params,
			final String... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zinterstore(dstkey, params, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zinterstore(final byte[] dstkey, ZParams params,
			final byte[]... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zinterstore(dstkey, params, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZRANGE key start stop [WITHSCORES]
	// 返回的成员在排序设置的范围，由指数
	public Set<byte[]> zrange(final byte[] key, final long start, final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrange(final int key, final long start, final long end) {
		return zrange(ts(key), start, end);
	}

	public Set<String> zrange(final String key, final long start, final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]
	// 返回的成员在排序设置的范围，由得分
	public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min,
			final byte[] max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<byte[]> zrangeByScore(final byte[] key, final double min,
			final double max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrangeByScore(final int key, final String min,
			final String max) {
		return zrangeByScore(ts(key), min, max);
	}

	public Set<String> zrangeByScore(final String key, final String min,
			final String max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	public Set<String> zrangeByScore(final int key, final double min,
			final double max) {
		return zrangeByScore(ts(key), min, max);
	}

	public Set<String> zrangeByScore(final String key, final double min,
			final double max) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min,
			final byte[] max, int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<byte[]> zrangeByScore(final byte[] key, final double min,
			final double max, int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrangeByScore(final int key, final String min,
			final String max, int offset, int count) {
		return zrangeByScore(ts(key), min, max, offset, count);
	}

	public Set<String> zrangeByScore(final String key, final String min,
			final String max, int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	public Set<String> zrangeByScore(int key, double min, double max,
			int offset, int count) {
		return zrangeByScore(ts(key), min, max, offset, count);
	}

	public Set<String> zrangeByScore(String key, double min, double max,
			int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrangeByScore(key, min, max, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// ZRANK key member
	// 确定在排序集合成员的索引
	public Long zrank(final byte[] key, final byte[] member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zrank(final int key, final String member) {
		return zrank(ts(key), member);
	}

	public Long zrank(final String key, final String member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZREM key member [member ...]
	// 从排序的集合中删除一个或多个成员
	public Long zrank(final byte[] key, final byte[]... members) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrem(key, members);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zrank(final int key, final String... members) {
		return zrank(ts(key), members);
	}

	public Long zrank(final String key, final String... members) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrem(key, members);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZREMRANGEBYRANK key start stop
	// 在排序设置的所有成员在给定的索引中删除
	public Long zremrangeByRank(final byte[] key, final long start,
			final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zremrangeByRank(final int key, final long start, final long end) {
		return zremrangeByRank(ts(key), start, end);
	}

	public Long zremrangeByRank(final String key, final long start,
			final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZREMRANGEBYSCORE key min max
	// 删除一个排序的设置在给定的分数所有成员
	public Long zremrangeByScore(final byte[] key, final byte[] start,
			final byte[] end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zremrangeByScore(final int key, final String start,
			final String end) {
		return zremrangeByScore(ts(key), start, end);
	}

	public Long zremrangeByScore(final String key, final String start,
			final String end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zremrangeByScore(final byte[] key, final double start,
			final double end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zremrangeByScore(final int key, final double start,
			final double end) {
		return zremrangeByScore(ts(key), start, end);
	}

	public Long zremrangeByScore(final String key, final double start,
			final double end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZREVRANGE key start stop [WITHSCORES]
	// 在排序的设置返回的成员范围，通过索引，下令从分数高到低
	public Set<byte[]> zrevrange(final byte[] key, final long start,
			final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrevrange(final int key, final long start, final long end) {
		return zrevrange(ts(key), start, end);
	}

	public Set<String> zrevrange(final String key, final long start,
			final long end) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
	// 返回的成员在排序设置的范围，由得分，下令从分数高到低
	public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max,
			final byte[] min) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrevrangeByScore(final int key, final String max,
			final String min) {
		return zrevrangeByScore(ts(key), max, min);
	}

	public Set<String> zrevrangeByScore(final String key, final String max,
			final String min) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	public Set<byte[]> zrevrangeByScore(final byte[] key, final double max,
			final double min) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrevrangeByScore(final int key, final double max,
			final double min) {
		return zrevrangeByScore(ts(key), max, min);
	}

	public Set<String> zrevrangeByScore(final String key, final double max,
			final double min) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max,
			final byte[] min, int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrevrangeByScore(final int key, final String max,
			final String min, int offset, int count) {
		return zrevrangeByScore(ts(key), max, min, offset, count);
	}

	public Set<String> zrevrangeByScore(final String key, final String max,
			final String min, int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	public Set<byte[]> zrevrangeByScore(final byte[] key, final double max,
			final double min, int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<byte[]>();
	}

	public Set<String> zrevrangeByScore(final int key, final double max,
			final double min, int offset, int count) {
		return zrevrangeByScore(ts(key), max, min, offset, count);
	}

	public Set<String> zrevrangeByScore(final String key, final double max,
			final double min, int offset, int count) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrangeByScore(key, max, min, offset, count);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return new HashSet<String>();
	}

	// ZREVRANK key member
	// 确定指数在排序集的成员，下令从分数高到低
	public Long zrevrank(final byte[] key, final byte[] member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zrevrank(final int key, final String member) {
		return zrevrank(ts(key), member);
	}

	public Long zrevrank(final String key, final String member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zrevrank(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// ZSCORE key member
	// 获取成员在排序设置相关的比分
	public Double zscore(final byte[] key, final byte[] member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zscore(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	public Double zscore(final int key, final String member) {
		return zscore(ts(key), member);
	}

	public Double zscore(final String key, final String member) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zscore(key, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0.0;
	}

	// ZUNIONSTORE destination numkeys key [key ...] [WEIGHTS weight [weight
	// ...]] [AGGREGATE SUM|MIN|MAX]
	// 添加多个排序集和导致排序的设置存储在一个新的关键
	public Long zunionstore(final byte[] dstkey, final byte[]... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zunionstore(dstkey, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zunionstore(final int dstkey, final String... sets) {
		return zunionstore(ts(dstkey), sets);
	}

	public Long zunionstore(final String dstkey, final String... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zunionstore(dstkey, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zunionstore(final byte[] dstkey, ZParams params,
			final byte[]... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zunionstore(dstkey, params, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	public Long zunionstore(final int dstkey, ZParams params,
			final String... sets) {
		return zunionstore(ts(dstkey), params, sets);
	}

	public Long zunionstore(final String dstkey, ZParams params,
			final String... sets) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.zunionstore(dstkey, params, sets);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return 0L;
	}

	// /////////////////////////////////////////////
	// com.bowlong.sql.mysql.BeanSupport BIO2
	// public String set(final String key,
	// final com.bowlong.sql.mysql.BeanSupport val) {
	// try {
	// final byte[] key2 = key.getBytes();
	// final byte[] value2 = val.toByteArray();
	// return set(key2, value2);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return "";
	// }
	//
	// public String set(final String key,
	// final com.bowlong.sql.mysql.BeanSupport val, int seconds) {
	// try {
	// byte[] key2 = key.getBytes();
	// byte[] value2 = val.toByteArray();
	// return setex(key2, seconds, value2);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return "";
	// }

	// /////////////////////////////////////////////
	// 管道
	// public Pipeline pipelined() {
	// final Jedis jedis = pool_w.getResource();
	// try {
	// return jedis.pipelined();
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// pool_w.returnResource(jedis);
	// }
	// return null;
	// }

	public List<Object> pipelined(final PipelineBlock jedisPipeline) {
		final Jedis jedis = pool_w.getResource();
		try {
			return jedis.pipelined(jedisPipeline);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool_w.returnResource(jedis);
		}
		return null;
	}

	// /////////////////////////////////////////////
	public Jedis borrowJedis() {
		return pool_w.getResource();
	}

	public void returnJedis(Jedis jedis) {
		pool_w.returnResource(jedis);
	}

	@SuppressWarnings("rawtypes")
	private static final Map toMap(String str) {
		try {
			Properties map = new Properties();
			map.load(new StringReader(str));
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap();
	}

	public static final String ts(int i) {
		return Integer.toString(i);
	}
}
