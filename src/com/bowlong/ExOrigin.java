package com.bowlong;

/**
 * Ex扩展类的起源<br/>
 * 说有扩展类都应该继承该类。 <br/>
 * 
 * @author Canyon
 * @time 2019-02-14 19:32
 */
@SuppressWarnings("unchecked")
public class ExOrigin {
	static public final <T> T toT(Object obj) {
		return (T) obj;
	}
}
