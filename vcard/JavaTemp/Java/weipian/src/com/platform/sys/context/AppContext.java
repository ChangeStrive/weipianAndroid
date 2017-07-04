package com.platform.sys.context;



import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

public interface AppContext{
		
	public ServletContext getServletContext();
	
	public ApplicationContext getSpringContext();
	
	public String getContextPath();
	
	public String getRealPath(String path);
	
	public <T> T getBean(Class<T> clazz);
	
	public Object getBean(String name);
	
	public <T> T getBean(String name,Class<T> clazz);
}
