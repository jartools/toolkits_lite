package com.bowlong.third.netty4.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;

import com.bowlong.bio2.B2Type;

public class LengthByteArrayDecoder extends LengthFieldBasedFrameDecoder {

	public LengthByteArrayDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
				lengthAdjustment, initialBytesToStrip, true);
	}

	protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer)
			throws Exception {
		if (!buffer.isReadable())
			return null;

		int readableBytes = buffer.readableBytes();
		if (readableBytes < 5)
			return null;
		buffer.markReaderIndex();
		int needBytes = readInt(buffer);
		if (buffer.readableBytes() < needBytes) {
			buffer.resetReaderIndex();
			return null;
		}

		// copy the ByteBuf content to a byte array
		// byte[] array = new byte[msg.readableBytes()];
		// msg.getBytes(0, array);
		// byte[] content = new byte[needBytes];
		// buffer.readBytes(content);
		// return content;
		return buffer.readBytes(needBytes).array();
	}

	public static final int readInt(ByteBuf is) throws IOException {
		byte tag = (byte) is.readByte();
		switch (tag) {
		case B2Type.INT_N1:
			return -1;
		case B2Type.INT_0:
			return 0;
		case B2Type.INT_1:
			return 1;
		case B2Type.INT_2:
			return 2;
		case B2Type.INT_3:
			return 3;
		case B2Type.INT_4:
			return 4;
		case B2Type.INT_5:
			return 5;
		case B2Type.INT_6:
			return 6;
		case B2Type.INT_7:
			return 7;
		case B2Type.INT_8:
			return 8;
		case B2Type.INT_9:
			return 9;
		case B2Type.INT_10:
			return 10;
		case B2Type.INT_11:
			return 11;
		case B2Type.INT_12:
			return 12;
		case B2Type.INT_13:
			return 13;
		case B2Type.INT_14:
			return 14;
		case B2Type.INT_15:
			return 15;
		case B2Type.INT_16:
			return 16;
		case B2Type.INT_17:
			return 17;
		case B2Type.INT_18:
			return 18;
		case B2Type.INT_19:
			return 19;
		case B2Type.INT_20:
			return 20;
		case B2Type.INT_21:
			return 21;
		case B2Type.INT_22:
			return 22;
		case B2Type.INT_23:
			return 23;
		case B2Type.INT_24:
			return 24;
		case B2Type.INT_25:
			return 25;
		case B2Type.INT_26:
			return 26;
		case B2Type.INT_27:
			return 27;
		case B2Type.INT_28:
			return 28;
		case B2Type.INT_29:
			return 29;
		case B2Type.INT_30:
			return 30;
		case B2Type.INT_31:
			return 31;
		case B2Type.INT_32:
			return 32;
		case B2Type.INT_8B: {
			byte v = (byte) is.readByte();
			return v;
		}
		case B2Type.INT_16B: {
			short v = (short) (((is.readByte() & 0xff) << 8) + ((is.readByte() & 0xff) << 0));
			return v;
		}
		case B2Type.INT_32B: {
			int value1 = is.readByte();
			int value2 = is.readByte();
			int value3 = is.readByte();
			int value4 = is.readByte();

			int v = ((value1 & 0xff) << 24) + ((value2 & 0xff) << 16)
					+ ((value3 & 0xff) << 8) + ((value4 & 0xff) << 0);
			return v;
		}
		default:
			throw new IOException("read int tag error:" + tag);
		}
	}

}
