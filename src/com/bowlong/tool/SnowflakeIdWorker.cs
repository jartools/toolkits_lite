using System.Collections;
using System;

/// <summary>
/// 类名 : Snowflake算法是Twitter的工程师为实现递增而不重复的ID实现的
/// 作者 : Canyon
/// 日期 : 2017-06-07 10:17
/// 功能 : 参照java的修改
/// </summary>
public class SnowflakeIdWorker  {

	// ==============================Fields===========================================
	/** 开始时间截 (2015-01-01) */
	private static long twepoch = 1420041600000L;

	/** 机器id所占的位数 */
	private static long workerIdBits = 5L;

	/** 数据标识id所占的位数 */
	private static long datacenterIdBits = 5L;

	/** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
	private static long maxWorkerId = -1L ^ (-1L << (int)workerIdBits);

	/** 支持的最大数据标识id，结果是31 */
	private static long maxDatacenterId = -1L ^ (-1L << (int)datacenterIdBits);

	/** 序列在id中占的位数 */
	private static long sequenceBits = 12L;

	/** 机器ID向左移12位 */
	private static long workerIdShift = sequenceBits;

	/** 数据标识id向左移17位(12+5) */
	private static long datacenterIdShift = sequenceBits + workerIdBits;

	/** 时间截向左移22位(5+5+12) */
	private static long timestampLeftShift = sequenceBits + workerIdBits
			+ datacenterIdBits;

	/** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
	private static long sequenceMask = -1L ^ (-1L << (int)sequenceBits);

	/** 工作机器ID(0~31) */
	private long workerId;

	/** 数据中心ID(0~31) */
	private long datacenterId;

	/** 毫秒内序列(0~4095) */
	private long sequence = 0L;

	/** 上次生成ID的时间截 */
	private long lastTimestamp = -1L;

	/** 计时开始时间 */
	static DateTime begUtcTime = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc);

	/** 加锁对象 */
	private static object syncRoot = new object();  

	// ==============================Constructors=====================================
	/**
	 * 构造函数
	 * 
	 * @param workerId
	 *            工作ID (0~31)
	 * @param datacenterId
	 *            数据中心ID (0~31)
	 */
	public SnowflakeIdWorker(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new System.Exception(string.Format(
				"worker Id can't be greater than {0} or less than 0",
					maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new System.Exception(string.Format(
				"datacenter Id can't be greater than {0} or less than 0",
					maxDatacenterId));
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	// ==============================Methods==========================================
	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public long nextId() {
		lock (syncRoot) {
			long timestamp = timeGen();

			// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
			if (timestamp < lastTimestamp) {
				throw new  System.Exception(
					string.Format(
						"Clock moved backwards.  Refusing to generate id for {0} milliseconds",
						lastTimestamp - timestamp));
			}

			// 如果是同一时间生成的，则进行毫秒内序列
			if (lastTimestamp == timestamp) {
				sequence = (sequence + 1) & sequenceMask;
				// 毫秒内序列溢出
				if (sequence == 0) {
					// 阻塞到下一个毫秒,获得新的时间戳
					timestamp = tilNextMillis(lastTimestamp);
				}
			}
			// 时间戳改变，毫秒内序列重置
			else {
				sequence = 0L;
			}

			// 上次生成ID的时间截
			lastTimestamp = timestamp;

			// 移位并通过或运算拼到一起组成64位的ID
			return ((timestamp - twepoch) << (int)timestampLeftShift) //
				| (datacenterId << (int)datacenterIdShift) //
				| (workerId << (int)workerIdShift) //
				| sequence;
		}
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * 
	 * @param lastTimestamp
	 *            上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * 
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return (long)(DateTime.UtcNow - begUtcTime).TotalMilliseconds;
	}

	static SnowflakeIdWorker _defInstance = null;
	static public SnowflakeIdWorker defInstance{
		get{
			if (_defInstance == null)
				_defInstance = new SnowflakeIdWorker (0, 0);
			return _defInstance;
		}
	}
}
