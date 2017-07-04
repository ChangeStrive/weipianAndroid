package com.maya.android.vcard.util;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * 友盟统计帮助类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-2-12
 *
 */
public class MobclickAgentHelper {
	
	/**是否打开友盟统计  **/
	private static boolean isOpenMobclick = true;
	
	/**
	 * 设置是否打开友盟统计
	 * @param isOpen
	 */
	public static void setOpenMobclick(boolean isOpen){
		isOpenMobclick = isOpen;
	}
	
	/**
	 * 事件数量统计
	 * @param context 上下文环境
	 * @param eventId 事件id
	 */
	public static void onEvent(Context context, String eventId){
		if(isOpenMobclick){
			MobclickAgent.onEvent(context, eventId);
		}
	}
}
