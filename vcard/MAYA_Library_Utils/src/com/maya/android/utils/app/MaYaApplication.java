package com.maya.android.utils.app;

import android.app.Application;
import android.content.Context;
/**
 * 全局Application
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-1
 *
 */
public class MaYaApplication extends Application {
	private static MaYaApplication sInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		sInstance = this;
	}
	/**
	 * 取得全局Context
	 * @return Context 上下文
	 */
	public static final Context getInstance(){
		return sInstance;
	}
	
}
