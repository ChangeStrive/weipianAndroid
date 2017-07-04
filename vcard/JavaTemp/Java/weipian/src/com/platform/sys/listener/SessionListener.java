package com.platform.sys.listener;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;


public class SessionListener implements HttpSessionListener,HttpSessionAttributeListener  {

	/** */
	/**
	 * 功能描述： 编码时间：2008-3-25 上午09:41:11 参数及返回值：
	 * 
	 * @param arg0
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se) {
		// 时间: 2008-3-25 上午09:41:11
		System.out.println(" Session创建： " + se.getSession().getId());
	}

	/** */
	/**
	 * 功能描述：session销毁监控，销毁时注销用户 编码时间：
	 * 
	 * @param arg0
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se) {

	}

	/** */
	/**
	 * 
	 * 功能描述：监控用户登录。有登录的，记录其状态。 编码时间：2008-4-3 上午10:58:36 参数及返回值：
	 * 
	 * @param event
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeAdded(HttpSessionBindingEvent event) {
		// 时间: 2008-3-25 上午09:55:42
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		// 时间: 2008-3-25 上午09:55:42
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		// 时间: 2008-3-25 上午09:55:42
	}

}
