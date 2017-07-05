package com.bowlong.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * 代理类，只能执行除了private修饰的方法以外的其他方法体
 * 
 * @author Canyon
 */
public class Delegate {
	private Class<?> c = null;
	private String method = null;// c中的public修饰的方法名
	private Class<?>[] paramsType = null;// method的参数类型[没有就为null]
	private Object[] params = null;// 调用method时候的参数值
	private Map<Class<?>, Object> mapParams = null;// 参数map对象
	private Object newObj = null;// class c的实例对象

	private void initEntity() {
		try {
			if (this.c != null)
				this.newObj = this.c.newInstance();
		} catch (Exception e) {
		}
	}

	public Delegate(Class<?> c) {
		this.c = c;
		initEntity();
	}

	public Delegate(Class<?> c, String method, Class<?>[] paramsType,
			Object[] params) {
		this.c = c;
		initEntity();
		resetInvoke(method, paramsType, params);
	}

	public Delegate(Class<?> c, String method, Map<Class<?>, Object> mapParams) {
		this.c = c;
		initEntity();
		resetInvoke(method, mapParams);
	}

	/*** 重新初始化 ***/
	public void resetInvoke(Class<?> c, String method, Class<?>[] paramsType,
			Object[] params) {
		if (c == null)
			return;

		boolean isSame = false;
		if (this.c == null) {
			isSame = true;
		} else {
			String curName = this.c.getName();
			String newName = c.getName();
			isSame = curName.equals(newName);
		}

		this.c = c;
		this.method = method;
		this.params = params;
		this.mapParams = null;
		this.paramsType = paramsType;
		if (!isSame) {
			this.newObj = null;
			this.initEntity();
		}
	}

	public void resetInvoke(String method, Class<?>[] paramsType,
			Object[] params) {
		this.resetInvoke(c, method, paramsType, params);
	}

	public void resetInvoke(String method, Map<Class<?>, Object> mapParams) {
		this.method = method;
		this.mapParams = mapParams;
		if (null == this.mapParams || this.mapParams.size() <= 0) {
			this.paramsType = null;
			this.params = null;
		} else {
			Set<Class<?>> keyes = this.mapParams.keySet();

			List<Class<?>> lists = new ArrayList<Class<?>>();
			lists.addAll(keyes);

			int len = lists.size();
			paramsType = new Class<?>[len];
			params = new Object[len];
			for (int i = 0; i < len; i++) {
				Class<?> k = lists.get(i);
				Object v = this.mapParams.get(k);
				paramsType[i] = k;
				params[i] = v;
			}
		}
	}

	/*** 执行方法 **/
	public Object invoke(boolean isStatic) throws Exception {
		if (null == this.method || this.method.length() <= 0)
			return null;

		Method m = null;
		boolean isHasParm = !(paramsType == null || paramsType.length == 0);

		if (isHasParm) {
			m = c.getDeclaredMethod(method, paramsType);
		} else {
			m = c.getDeclaredMethod(method);
		}
		Object excObj = null;
		if (isStatic) {
			excObj = c;
		} else {
			excObj = this.newObj;
		}

		if (isHasParm) {
			return m.invoke(excObj, params);
		} else {
			return m.invoke(excObj);
		}
	}

	public Object invokeExce(Class<?> c, String method, boolean isStatic,
			Class<?>[] paramsType, Object[] params) {
		try {
			this.resetInvoke(c, method, paramsType, params);
			return this.invoke(isStatic);
		} catch (Exception e) {
			return null;
		}
	}

	public Object invokeExce(String method, boolean isStatic,
			Class<?>[] paramsType, Object[] params) {
		return invokeExce(this.c, method, isStatic, paramsType, params);
	}

	static public final Delegate delegate = new Delegate(Delegate.class);

	/*** 静态方法 **/
	static public final Object invoken(Class<?> c, String method,
			boolean isStatic, Class<?>[] paramsType, Object[] params) {
		try {
			return delegate.invokeExce(c, method, isStatic, paramsType, params);
		} catch (Exception e) {
			return null;
		}
	}
}

@SuppressWarnings("unused")
class DelTest {
	public void say() {
		System.out.println("== hello ===");
	}

	public void say(String v) {
		System.out.println("== hello == " + v);
	}

	private void sayPrivate() {
		System.out.println("== sayPrivate ==");
	}

	void sayDef() {
		System.out.println("== sayDef ==");
	}

	protected void sayProtected() {
		System.out.println("== sayProtected ==");
	}

	static public int retVal() {
		return -10;
	}

	static public int retVal(int v) {
		return v;
	}

	static int retValDef() {
		return -11;
	}

	static protected int retValProtected() {
		return -12;
	}

	static private int retValPrivate() {
		return -15;
	}

	public static void main(String[] args) throws Exception {
		Delegate del = new Delegate(DelTest.class);
		del.invokeExce("say", false, null, null);
		del.invokeExce("say", false, new Class<?>[] { String.class },
				new Object[] { "12" });

		// 这个私有方法不能被执行,会抛出异常
		del.invokeExce("sayPrivate", false, null, null);

		del.invokeExce("sayProtected", false, null, null);
		del.invokeExce("sayDef", false, null, null);

		int sp1 = (int) del.invokeExce("retVal", true, null, null);
		System.out.println(sp1);

		sp1 = (int) del.invokeExce("retVal", true,
				new Class<?>[] { int.class }, new Object[] { 19 });
		System.out.println(sp1);

		sp1 = (int) del.invokeExce("retValDef", true, null, null);
		System.out.println(sp1);

		sp1 = (int) del.invokeExce("retValProtected", true, null, null);
		System.out.println(sp1);

		sp1 = (int) del.invokeExce("retValPrivate", true, null, null);
		System.out.println(sp1);
	}
}