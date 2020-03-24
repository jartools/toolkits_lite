package com.bowlong.third.netty4.httphand;

import com.bowlong.text.Encoding;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class N4B2ByteBuf {
	static public final byte[] readBuff(ByteBuf dataBuf) {
		if (dataBuf == null || !dataBuf.isReadable() || dataBuf.readableBytes() == 0) {
			return new byte[0];
		}
		byte[] buff = new byte[dataBuf.readableBytes()];
		dataBuf.readBytes(buff);
		return buff;
	}

	static public final ByteBuf buffer() {
		return Unpooled.buffer();
	}

	static public final ByteBuf bufferComposite() {
		return Unpooled.compositeBuffer();
	}

	static public final ByteBuf buffer(byte[] buf) {
		return Unpooled.copiedBuffer(buf);
	}

	static public final ByteBuf buffer(byte[]... bufs) {
		return Unpooled.copiedBuffer(bufs);
	}

	static public final ByteBuf buffer(CharSequence charSequence) {
		return Unpooled.copiedBuffer(charSequence, Encoding.UTF8);
	}

	static public final ByteBuf bufferWrapped(byte[]... bufs) {
		return Unpooled.wrappedBuffer(bufs);
	}
}
