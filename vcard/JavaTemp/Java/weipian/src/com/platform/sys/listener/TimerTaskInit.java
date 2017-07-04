package com.platform.sys.listener;


import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class TimerTaskInit implements ServletContextListener {
	
	
	private static ScheduledThreadPoolExecutor stpe = null;
	
	public void contextDestroyed(ServletContextEvent event) {
	
	}

	public void contextInitialized(ServletContextEvent event) {
		try {
			goTimer(event);
		} catch (Exception ex) {
			System.out.println("失败:" + ex.getMessage());
		}
	}
	
	private void goTimer(ServletContextEvent event) {
		
		//构造一个ScheduledThreadPoolExecutor对象，并且设置它的容量为10个
		stpe = new ScheduledThreadPoolExecutor(10);
		event.getServletContext().log("********************** 定时器已启动  **********************");
		
		/*ChatPraiseReportService chatReportSrv = AppContextImpl.getInstance().getBean(ChatPraiseReportService.class);
		chatReportSrv.createTemporary(TimerTaskInit.TEMPORARY_NAME_TODAY);
		chatReportSrv.createTemporary(TimerTaskInit.TEMPORARY_NAME_QUARTER);*/
		stpe.scheduleWithFixedDelay(new WxTimer(), 10,45*60, TimeUnit.SECONDS);
		
	}
	
	
	
}
