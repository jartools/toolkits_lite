package com.bowlong.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理机制 <br/>
 * T t = new T(); <br/>
 * T proxy = (T)new AbstractHandler().getProxy(t);<br/>
 * 
 * @author Canyon
 */
public abstract class AbstractHandler implements InvocationHandler {
	protected Object target;

	public void setTarget(Object target) {
		this.target = target;
	}

	public Object invoke(Object proxy, Method method, Object[] args) {
		doBefore();
		Object result = null;
		try {
			result = method.invoke(target, args);
		} catch (Exception e) {
			doThrow();
		}
		doAfter();
		return result;
	}

	public void doBefore() {
	}

	public void doAfter() {
	}

	public void doThrow() {
	}

	public Object getProxy() {
		Class<? extends Object> tClass = target.getClass();
		return Proxy.newProxyInstance(tClass.getClassLoader(), tClass.getInterfaces(), this);
	}

	public Object getProxy(Object target) {
		setTarget(target);
		return getProxy();
	}
}