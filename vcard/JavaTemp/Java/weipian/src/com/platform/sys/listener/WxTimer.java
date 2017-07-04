package com.platform.sys.listener;

import java.util.Timer;
import java.util.TimerTask;

public class WxTimer extends TimerTask {

	
	@Override
	public void run() {
		Timer timer = new Timer();
		timer.schedule(new WeiXinJSTimer(), 1000*1);
	}

}
