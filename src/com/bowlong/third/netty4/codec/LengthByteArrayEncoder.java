package com.bowlong.third.netty4.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.bowlong.bio2.B2Type;
import com.bowlong.lang.NumEx;

@Sharable
public class LengthByteArrayEncoder extends MessageToMessageEncoder<byte[]> {
	@Override
	protected void encode(ChannelHandlerContext ctx, byte[] msg,
			List<Object> out) throws Exception {
		byte[] b1 = toBytes(msg.length);
		// out.add(Unpooled.wrappedBuffer(b1)); // length
		// out.add(Unpooled.wrappedBuffer(msg)); // data

		ByteBuf buf = Unpooled.wrappedBuffer(b1, msg); // length , data
		out.add(buf);
	}

	static final byte[] N1 = { B2Type.INT_N1 };
	static final byte[] I0 = { B2Type.INT_0 };
	static final byte[] I1 = { B2Type.INT_1 };
	static final byte[] I2 = { B2Type.INT_2 };
	static final byte[] I3 = { B2Type.INT_3 };
	static final byte[] I4 = { B2Type.INT_4 };
	static final byte[] I5 = { B2Type.INT_5 };
	static final byte[] I6 = { B2Type.INT_6 };
	static final byte[] I7 = { B2Type.INT_7 };
	static final byte[] I8 = { B2Type.INT_8 };
	static final byte[] I9 = { B2Type.INT_9 };
	static final byte[] I10 = { B2Type.INT_10 };
	static final byte[] I11 = { B2Type.INT_11 };
	static final byte[] I12 = { B2Type.INT_12 };
	static final byte[] I13 = { B2Type.INT_13 };
	static final byte[] I14 = { B2Type.INT_14 };
	static final byte[] I15 = { B2Type.INT_15 };
	static final byte[] I16 = { B2Type.INT_16 };
	static final byte[] I17 = { B2Type.INT_17 };
	static final byte[] I18 = { B2Type.INT_18 };
	static final byte[] I19 = { B2Type.INT_19 };
	static final byte[] I20 = { B2Type.INT_20 };
	static final byte[] I21 = { B2Type.INT_21 };
	static final byte[] I22 = { B2Type.INT_22 };
	static final byte[] I23 = { B2Type.INT_23 };
	static final byte[] I24 = { B2Type.INT_24 };
	static final byte[] I25 = { B2Type.INT_25 };
	static final byte[] I26 = { B2Type.INT_26 };
	static final byte[] I27 = { B2Type.INT_27 };
	static final byte[] I28 = { B2Type.INT_28 };
	static final byte[] I29 = { B2Type.INT_29 };
	static final byte[] I30 = { B2Type.INT_30 };
	static final byte[] I31 = { B2Type.INT_31 };
	static final byte[] I32 = { B2Type.INT_32 };

	static final byte[] toBytes(int v) {
		switch (v) {
		case -1:
			return N1;
		case 0:
			return I0;
		case 1:
			return I1;
		case 2:
			return I2;
		case 3:
			return I3;
		case 4:
			return I4;
		case 5:
			return I5;
		case 6:
			return I6;
		case 7:
			return I7;
		case 8:
			return I8;
		case 9:
			return I9;
		case 10:
			return I10;
		case 11:
			return I11;
		case 12:
			return I12;
		case 13:
			return I13;
		case 14:
			return I14;
		case 15:
			return I15;
		case 16:
			return I16;
		case 17:
			return I17;
		case 18:
			return I18;
		case 19:
			return I19;
		case 20:
			return I20;
		case 21:
			return I21;
		case 22:
			return I22;
		case 23:
			return I23;
		case 24:
			return I24;
		case 25:
			return I25;
		case 26:
			return I26;
		case 27:
			return I27;
		case 28:
			return I28;
		case 29:
			return I29;
		case 30:
			return I30;
		case 31:
			return I31;
		case 32:
			return I32;
		default:
			if (v >= NumEx.BYTE_MIN_VALUE && v <= NumEx.BYTE_MAX_VALUE) {
				// byte[] r2 = new byte[2];
				// r2[0] = B2Type.INT_8B;
				byte[] r2 = { B2Type.INT_8B, 1 };
				r2[1] = (byte) (v & 0xff);
				return r2;
			} else if (v >= NumEx.SHORT_MIN_VALUE && v <= NumEx.SHORT_MAX_VALUE) {
				// byte[] r2 = new byte[3];
				// r2[0] = B2Type.INT_16B;
				byte[] r2 = { B2Type.INT_16B, 1, 2 };
				r2[1] = (byte) ((v >> 8) & 0xff);
				r2[2] = (byte) ((v >> 0) & 0xff);
				return r2;
			} else {
				// byte[] r2 = new byte[5];
				// r2[0] = B2Type.INT_32B;
				byte[] r2 = { B2Type.INT_32B, 1, 2, 3, 4 };
				r2[1] = (byte) ((v >> 24) & 0xff);
				r2[2] = (byte) ((v >> 16) & 0xff);
				r2[3] = (byte) ((v >> 8) & 0xff);
				r2[4] = (byte) ((v >> 0) & 0xff);
				return r2;
			}
		}
	}
}