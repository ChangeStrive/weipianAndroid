package com.platform.sys.context;


import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

public class AppContextImpl implements AppContext,ApplicationContextAware,ServletContextAware {
	
	private static AppContext instance;
	
	private ServletContext context;
	private ApplicationContext  springContext;
	
	private  AppContextImpl(){
	}
	
	public void init(){	
		instance=this;
	}
	
	public ServletContext getServletContext(){
		return context;
	}
	
	public ApplicationContext getSpringContext(){
		return springContext;
	}
	
	public String getContextPath(){
		return context.getContextPath();
	}
	
	public String getRealPath(String path){
		return context.getRealPath(path);
	}
	
	public <T> T getBean(Class<T> clazz){
		return springContext.getBean(clazz);
	}
	
	public Object getBean(String name){
		return springContext.getBean(name);
	}
	
	public <T> T getBean(String name,Class<T> clazz){
		return springContext.getBean(name,clazz);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {	
		springContext=applicationContext;
	}

	public void setServletContext(ServletContext servletContext) {
		context=servletContext;
	}
	
	public static AppContext getInstance(){
		return instance;
	}
}
