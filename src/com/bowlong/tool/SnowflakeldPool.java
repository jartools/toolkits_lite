package com.bowlong.tool;

import com.bowlong.lang.task.ThreadEx;
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
	private int maxWid = 0;
	private int maxDid = 0;
	private int maxPobj = 0;
	private int sumPobj = 0;

	public SnowflakeldPool() {
		super(SnowflakeIdWorker.class);
		_init();
	}

	public SnowflakeldPool(int num) {
		super(SnowflakeIdWorker.class, num);
		_init();
	}

	void _init() {
		maxWid = (int) (SnowflakeIdWorker.maxWorkerId + 1);
		maxDid = (int) (SnowflakeIdWorker.maxDatacenterId + 1);
		maxPobj = maxWid * maxDid;
	}

	@Override
	synchronized public SnowflakeIdWorker createObj() {
		this.workId++;
		if (this.workId >= maxWid) {
			this.workId %= maxWid;
			this.centerId++;
			this.centerId %= maxDid;
		}
		SnowflakeIdWorker ret = null;
		if (sumPobj < maxPobj) {
			this.sumPobj++;
			ret = new SnowflakeIdWorker(this.workId, this.centerId);
		} else {
			do {
				ret = get();
				ThreadEx.sleep(20);
			} while (ret == null);
		}
		return ret;
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

	static final public String nextIdStr() {
		return String.valueOf(nextId());
	}
}
