package com.bowlong.third.jsp;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * Listener是针对对象的操作，如session创建，session.setAttribute等事件发生时做一些事情</br>
 * ServletContextListener,HttpSessionListener,ServletRequestListener
 * 监听对象的创建和销毁</br>
 * 本类是监听Request对象的</br>
 * Servlet,Filter都是针对url之类的
 * 
 * @author Canyon/龚阳辉 2018-12-17 09:10
 */
public class BasicRequestListener implements ServletRequestListener {
	@Override
	public void requestDestroyed(ServletRequestEvent request) {
	}

	@Override
	public void requestInitialized(ServletRequestEvent request) {
	}
}
