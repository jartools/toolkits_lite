package com.bowlong.third.redis;

import com.bowlong.lang.PStr;

import redis.clients.jedis.JedisPubSub;

public abstract class JedisSubMsg extends JedisPubSub {

	public static final String HELLO = "{\"type\":\"hello\"}";
	public static final String HELLO_STR = "hello";

//	public static final String genMsg(final String msg) {
//		return PStr.begin("{\"type\":\"gen\",\"key\":\"").a(msg).end("\"}");
//	}

	public static final String setMsg(final String key) {
		return PStr.begin("{\"type\":\"set\",\"key\":\"").a(key).end("\"}");
	}

	public static final String delMsg(final String key) {
		return PStr.begin("{\"type\":\"del\",\"key\":\"").a(key).end("\"}");
	}

	public static final String hsetMsg(final String key, final String field) {
		return PStr.begin("{\"type\":\"hset\",\"key\":\"").a(key)
				.a("\",\"field\":\"").a(field).end("\"}");
	}

	public static final String hdelMsg(final String key, final String field) {
		return PStr.begin("{\"type\":\"hdel\",\"key\":\"").a(key)
				.a("\",\"field\":\"").a(field).end("\"}");
	}

	// //////////////////////////////////

	@Override
	public void onMessage(String channel, String message) {
		// switch (message) {
		// case HELLO:
		// System.out.println("channel:" + message);
		// break;
		// default:
		onReceive(channel, message);
		// break;
		// }
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// System.out.println("onPMessage:pattern-" + pattern + ",channel-"
		// + channel + ",message-" + message);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// System.out.println("onSubscribe:channel-" + channel
		// + ",subscribedChannels-" + subscribedChannels);
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		// System.out.println("onUnsubscribe:channel-" + channel
		// + ",subscribedChannels-" + subscribedChannels);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// System.out.println("onPUnsubscribe:pattern-" + pattern
		// + ",subscribedChannels-" + subscribedChannels);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// System.out.println("onPSubscribe:pattern-" + pattern
		// + ",subscribedChannels-" + subscribedChannels);
	}

	// /////////////////////////////

	public abstract void onReceive(String channel, String message);

}
