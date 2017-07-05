package com.bowlong.third.netty4.httphand;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.bowlong.objpool.AbstractQueueObjPool;

public class N4B2ByteBuf extends AbstractQueueObjPool<ByteBuf> {

	private int intNum;
	private int maxNum;

	public int getIntNum() {
		return intNum;
	}

	public void setIntNum(int intNum) {
		this.intNum = intNum;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

	public N4B2ByteBuf() {

	}

	public N4B2ByteBuf(int init, int max) {
		init = init < 0 ? 0 : init;
		max = max < init ? init : max;
		this.intNum = init;
		this.maxNum = max;
		for (int i = 0; i < init; i++) {
			returnObj(createObj());
		}
	}

	@Override
	protected ByteBuf createObj() {
		if (maxNum > 0) {
			if (size() > maxNum)
				return null;
		}
		return buffer();
	}

	@Override
	protected ByteBuf resetObj(ByteBuf obj) {
		if (obj != null)
			obj.clear();
		return obj;
	}

	@Override
	protected ByteBuf destoryObj(ByteBuf obj) {
		if (obj != null) {
			obj.clear();
			obj.release();
		}
		return null;
	}

	// =================== 静态对象

	static protected final N4B2ByteBuf pool = new N4B2ByteBuf(1,
			Short.MAX_VALUE);

	static public final ByteBuf borrowBuf() {
		return pool.borrow();
	}

	static public final void returnBuf(ByteBuf buf) {
		pool.returnObj(buf);
	}

	static public final byte[] readBuff(ByteBuf dataBuf) {
		if (dataBuf == null || !dataBuf.isReadable()
				|| dataBuf.readableBytes() == 0) {
			return new byte[0];
		}
		byte[] buff = new byte[dataBuf.readableBytes()];
		dataBuf.readBytes(buff);
		return buff;
	}
	
	static public final ByteBuf buffer(){
		return Unpooled.buffer();
	}
}
