package com.bowlong.tool;

import com.bowlong.objpool.BasicPool;

/**
 * 分布式自增ID算法snowflake 的 对象池
 * 
 * @author canyon/龚阳辉
 * @time 2019-09-08 18:15
 */
public class SnowflakeldPool extends BasicPool<SnowflakeIdWorker> {

	private int workId = 0;
	private int centerId = 0;

	public SnowflakeldPool() {
		super(SnowflakeIdWorker.class);
	}

	public SnowflakeldPool(int num) {
		super(SnowflakeIdWorker.class, num);
	}

	@Override
	public SnowflakeIdWorker createObj() {
		this.workId++;
		int _max = (int) (SnowflakeIdWorker.maxWorkerId + 1);
		if (this.workId >= _max) {
			this.workId %= _max;
			_max = (int) (SnowflakeIdWorker.maxDatacenterId + 1);
			this.centerId = (this.centerId++) % _max;
		}
		return new SnowflakeIdWorker(this.workId, this.centerId);
	}

	static final public SnowflakeIdWorker borrowObject() {
		return borrowObject(SnowflakeIdWorker.class);
	}

	static final public SnowflakeldPool getPool() {
		return (SnowflakeldPool) getPool(SnowflakeIdWorker.class);
	}

	/** 外部调用函数 */
	static final public long nextId() {
		SnowflakeIdWorker _w = borrowObject();
		long ret = _w.nextId();
		returnObject(_w);
		return ret;
	}
}
