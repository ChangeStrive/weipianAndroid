package com.maya.android.utils;

import java.io.File;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.maya.android.utils.app.MaYaApplication;
import com.maya.android.utils.base.DelayTask;
import com.maya.android.utils.base.DelayTask.OnDelayExecuteListener;

/**
 * Activity辅助类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-1
 *
 */
public class ActivityHelper {
	private static final String TAG = ActivityHelper.class.getSimpleName();
	
	/**
	 * 取得全局Context
	 * @return Context 上下文
	 */
	public static final Context getGlobalApplicationContext(){
		if(Helper.isNull(MaYaApplication.getInstance())){
			Log.e(TAG, "YOU MUST BE IMPLEMENT METHOD OF 'com.maya.android.utils.app.MaYaApplication' IN 'AndroidManifest.xml'" +
					" ON 'application' NODE");
		}
		return MaYaApplication.getInstance();
	}
	
	//#region switchTo 跳转到指定页面
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param bundle 携带的参数
	 * @param finish 是否结束当前activity
	 * @param enterAnim 进入动画
	 * @param exitAnim 退出动画
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, Bundle bundle, boolean finish, int enterAnim, int exitAnim){
		Intent intent = new Intent(activity, toActivity);
		if(Helper.isNotNull(bundle)){
			intent.putExtras(bundle);
		}
		if(finish){
			activity.finish();
		}
		activity.startActivity(intent);
		if(enterAnim != 0 || exitAnim != 0){
			try{
				activity.overridePendingTransition(enterAnim, exitAnim);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param finish 是否结束当前activity
	 * @param enterAnim 进入动画
	 * @param exitAnim 退出动画
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, boolean finish, int enterAnim, int exitAnim){
		switchTo(activity, toActivity, new Bundle(), finish, enterAnim, exitAnim);
	}
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param enterAnim 进入动画
	 * @param exitAnim 退出动画
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, int enterAnim, int exitAnim){
		switchTo(activity, toActivity, false, enterAnim, exitAnim);
	}
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param finish 是否结束当前activity
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, boolean finish){
		switchTo(activity, toActivity, finish, 0, 0);
	}
	/**
	 * 跳转到指定页面(默认不结束当前activity)
	 * @param activity
	 * @param toActivity
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity){
		switchTo(activity, toActivity, false);
	}
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param bundle 携带的参数
	 * @param finish 是否结束当前activity
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, Bundle bundle, boolean finish){
		switchTo(activity, toActivity, bundle, finish, 0, 0);
	}
	/**
	 * 跳转到指定页面(默认不结束当前activity)
	 * @param activity
	 * @param toActivity
	 * @param bundle 携带的参数
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, Bundle bundle){
		switchTo(activity, toActivity, bundle, false);
	}
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param intent 携带的intent(可不设置setClass)
	 * @param finish 是否结束当前activity
	 * @param enterAnim 进入动画
	 * @param exitAnim 退出动画
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, Intent intent, boolean finish, int enterAnim, int exitAnim){
		if(Helper.isNotNull(intent)){
			intent.setClass(activity, toActivity);
			if(finish){
				activity.finish();
			}
			activity.startActivity(intent);
			if(enterAnim != 0 || exitAnim != 0){
				try{
					activity.overridePendingTransition(enterAnim, exitAnim);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param intent 携带的intent(可不设置setClass)
	 * @param finish 是否结束当前activity
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, Intent intent, boolean finish){
		switchTo(activity, toActivity, intent, finish, 0, 0);
	}
	/**
	 * 跳转到指定页面
	 * @param activity
	 * @param toActivity
	 * @param intent 携带的intent(可不设置setClass)
	 */
	public static final void switchTo(Activity activity, Class<?> toActivity, Intent intent){
		switchTo(activity, toActivity, intent, false);
	}
	//#endregion switchTo 跳转到指定页面
	
	//#region Toast集
	private static Toast sToast;
	@SuppressLint("ShowToast")
	private static Toast getToast(){
		if(Helper.isNull(sToast)){
			sToast = Toast.makeText(getGlobalApplicationContext(), "", Toast.LENGTH_SHORT);
		}
		return sToast;
	}
	/**
	 * 显示Toast信息
	 * @param duration 展示时长
	 * @param text 文本信息
	 */
	private static void showToast(int duration, CharSequence text){
		Toast toast = getToast();
		try{
			if(Helper.isNotNull(toast)){
				toast.setDuration(duration);
				toast.setText(text);
				toast.show();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 显示Toast信息
	 * @param duration 展示时长
	 * @param resId 文本id
	 * @param formatArgs
	 */
	private static void showToast(int duration, int resId, Object... formatArgs){
		showToast(duration, getString(resId, formatArgs));
	}
	/**
	 * 展示Toast信息（短）
	 * @param resId 文本id
	 * @param formatArgs
	 */
	public static void showToast(int resId, Object...formatArgs){
		showToast(Toast.LENGTH_SHORT, resId, formatArgs);
	}
	/**
	 * 展示Toast信息（短）
	 * @param text 展示文本
	 */
	public static void showToast(CharSequence text){
		showToast(Toast.LENGTH_SHORT, text);
	}
	/**
	 * 展示Toast信息（长）
	 * @param resId 文本id
	 * @param formatArgs
	 */
	public static void showLongToast(int resId, Object...formatArgs){
		showToast(Toast.LENGTH_LONG, resId, formatArgs);
	}
	/**
	 * 展示Toast信息（长）
	 * @param text 展示文本
	 */
	public static void showLongToast(CharSequence text){
		showToast(Toast.LENGTH_LONG, text);
	}
	//#endregion Toast集
	
	//#region ProgressDialog集
	private static ProgressDialog sProgressDialog;
	private static ProgressDialog getProgressDialog(Activity activity){
//		if(Helper.isNull(sProgressDialog)){
			sProgressDialog = ProgressDialog.show(activity, "", "", false, false);
//		}
		return sProgressDialog;
	}
	/**
	 * 显示ProgressDialog，自定义
	 * @param activity 
	 * @param title
	 * @param message
	 * @param indeterminate
	 * @param cancelable
	 * @param onceMore 出错时是否再执行一次
	 */
	private static void showProgressDialog(Activity activity, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable, boolean onceMore){
		if(Helper.isNotNull(activity) && !activity.isFinishing()){
			closeProgressDialog();
			synchronized (activity) {
				sProgressDialog = getProgressDialog(activity);
				try{
//					sProgressDialog.dismiss();
					sProgressDialog.setTitle(title);
					sProgressDialog.setMessage(message);
					sProgressDialog.setCancelable(cancelable);
					sProgressDialog.setIndeterminate(indeterminate);
					sProgressDialog.show();
				}catch(Exception e){
					e.printStackTrace();
					if(onceMore){
						sProgressDialog = null;
						showProgressDialog(activity, title, message, indeterminate, cancelable, onceMore);
					}
				}
			}
		}
	}
	/**
	 * 显示ProgressDialog
	 * @param activity
	 * @param title 标题
	 * @param message 文本
	 * @param indeterminate 
	 * @param cancelable 可否取消
	 */
	public static void showProgressDialog(Activity activity, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable){
		showProgressDialog(activity, title, message, indeterminate, cancelable, true);
	}
	/**
	 * 显示ProgressDialog
	 * @param activity
	 * @param title 标题
	 * @param message 文本
	 */
	public static void showProgressDialog(Activity activity, CharSequence title, CharSequence message){
		showProgressDialog(activity, title, message, false, true);
	}
	/**
	 * 显示ProgressDialog
	 * @param activity
	 * @param titleResId 标题资源ID
	 * @param messageResId 文本资源ID
	 */
	public static void showProgressDialog(Activity activity, int titleResId, int messageResId){
		showProgressDialog(activity, activity.getString(titleResId), activity.getString(messageResId));
	}
	/**
	 * 显示ProgressDialog, 无标题
	 * @param activity
	 * @param message 文本
	 */
	public static void showProgressDialog(Activity activity, CharSequence message){
		showProgressDialog(activity, "", message);
	}
	/**
	 * 显示ProgressDialog, 无标题
	 * @param activity
	 * @param messageResId 文本资源ID
	 */
	public static void showProgressDialog(Activity activity, int messageResId){
		showProgressDialog(activity, activity.getString(messageResId));
	}
	/**
	 * 关闭进度框
	 */
	public static void closeProgressDialog(){
		try{
			if(Helper.isNotNull(sProgressDialog)){
				sProgressDialog.dismiss();
				sProgressDialog = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//#endregion ProgressDialog集
	
	//#region PopupWindow集
	/**
	 * 创建PopupWindow
	 * @param activity 创建的Activity,非context
	 * @param contentView 内部View
	 * @param width 创建的宽度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @param height 创建的高度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @param touchable 是否可touch
	 * @param outsideTouchable 是否可在区域外touch
	 * @param focusable 是否可聚焦
	 * @return PopupWindow
	 */
	public static PopupWindow createPopupWindow(Activity activity, View contentView, int width, int height, boolean touchable, boolean outsideTouchable, boolean focusable){
		PopupWindow popWin = new PopupWindow(contentView, width, height, focusable);
		popWin.setTouchable(touchable);
		popWin.setOutsideTouchable(outsideTouchable);
		return popWin;
	}
	/**
	 * 创建PopupWindow
	 * @param activity 创建的Activity,非context
	 * @param viewLayoutId 内部View布局id
	 * @param width 创建的宽度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @param height 创建的高度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @param touchable 是否可touch
	 * @param outsideTouchable 是否可在区域外touch
	 * @param focusable 是否可聚焦
	 * @return PopupWindow
	 */
	public static PopupWindow createPopupWindow(Activity activity, int viewLayoutId, int width, int height, boolean touchable, boolean outsideTouchable, boolean focusable){
		View contentView = activity.getLayoutInflater().inflate(viewLayoutId, null);
		return createPopupWindow(activity, contentView, width, height, touchable, outsideTouchable, focusable);
	}
	/**
	 * 创建PopupWindow
	 * @param activity 创建的Activity,非context
	 * @param contentView 内部View
	 * @param width 创建的宽度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @param height 创建的高度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @return PopupWindow
	 */
	public static PopupWindow createPopupWindow(Activity activity, View contentView, int width, int height){
		return createPopupWindow(activity, contentView, width, height, true, true, true);
	}
	/**
	 * 创建PopupWindow
	 * @param activity 创建的Activity,非context
	 * @param viewLayoutId 内部View布局id
	 * @param width 创建的宽度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @param height 创建的高度(可采用LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT之类)
	 * @return PopupWindow
	 */
	public static PopupWindow createPopupWindow(Activity activity, int viewLayoutId, int width, int height){
		View contentView = activity.getLayoutInflater().inflate(viewLayoutId, null);
		return createPopupWindow(activity, contentView, width, height);
	}
	/**
	 * 创建PopupWindow
	 * @param activity 创建的Activity,非context
	 * @param contentView 内部View
	 * @return PopupWindow
	 */
	public static PopupWindow createPopupWindow(Activity activity, View contentView){
		return createPopupWindow(activity, contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}
	/**
	 * 创建PopupWindow
	 * @param activity 创建的Activity,非context
	 * @param viewLayoutId 内部View布局id
	 * @return PopupWindow
	 */
	public static PopupWindow createPopupWindow(Activity activity, int viewLayoutId){
		View contentView = activity.getLayoutInflater().inflate(viewLayoutId, null);
		return createPopupWindow(activity, contentView);
	}
	//#endregion PopupWindow集
	
	//#region Notification集
	/** 状态栏提示的默认ID */
	private static final int NOTIFICATION_DEFAULT_ID = Helper.createIntTag();
	private static NotificationManager sNotificationManager = null;
	/**
	 * 显示状态栏提示
	 * @param context
	 * @param id ID,0则采用默认值
	 * @param icon
	 * @param tickerText 提示时显示的滚动信息,null时则不滚动显示
	 * @param contentTitle 内容标题
	 * @param contentText 内容
	 * @param contentView 点击显示的内容View,无则null
	 * @param contentIntent 点击跳转用PendingIntent
	 * @param cancelLast 是否立即取消上一个提示
	 */
	public static void showNotification(Context context, int id, int icon, CharSequence tickerText, CharSequence contentTitle
			, CharSequence contentText, RemoteViews contentView, PendingIntent contentIntent, boolean cancelLast){
		id = id <= 0 ? NOTIFICATION_DEFAULT_ID : id;
		if(Helper.isNull(sNotificationManager)){
			sNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setSmallIcon(icon);
		builder.setTicker(tickerText);
		builder.setContentTitle(contentTitle);
		builder.setContentText(contentText);
		builder.setContentIntent(contentIntent);
		builder.setWhen(System.currentTimeMillis());
		builder.setAutoCancel(true);
		Notification notification = builder.build();
		if(Helper.isNotNull(contentView)){
			notification.contentView = contentView;
		}		
		sNotificationManager.notify(id, notification);
	}
	/**
	 * 显示默认状态栏提示
	 * @param context
	 * @param icon
	 * @param tickerText 提示时显示的滚动信息,null时则不滚动显示
	 * @param contentTitle 内容标题
	 * @param contentText 内容
	 * @param contentView 点击显示的内容View,无则null
	 * @param contentIntent 点击跳转用PendingIntent
	 */
	public static void showDefaultNotification(Context context, int icon, int tickerText, int contentTitle
			, int contentText, RemoteViews contentView, PendingIntent contentIntent){
		showNotification(context, 0, icon, tickerText, contentTitle, contentText, contentView, contentIntent);
	}
	/**
	 * 显示默认状态栏提示
	 * @param context
	 * @param icon
	 * @param tickerText 提示时显示的滚动信息,null时则不滚动显示
	 * @param contentTitle 内容标题
	 * @param contentText 内容
	 * @param contentView 点击显示的内容View,无则null
	 * @param contentIntent 点击跳转用PendingIntent
	 */
	public static void showDefaultNotification(Context context, int icon, CharSequence tickerText, CharSequence contentTitle
			, CharSequence contentText, RemoteViews contentView, PendingIntent contentIntent){
		showNotification(context, 0, icon, tickerText, contentTitle, contentText, contentView, contentIntent);
	}
	/**
	 * 显示状态栏提示
	 * @param context
	 * @param id ID,0则采用默认值
	 * @param icon
	 * @param tickerText 提示时显示的滚动信息,null时则不滚动显示
	 * @param contentTitle 内容标题
	 * @param contentText 内容
	 * @param contentView 点击显示的内容View,无则null
	 * @param contentIntent 点击跳转用PendingIntent
	 */
	public static void showNotification(Context context, int id, int icon, int tickerText, int contentTitle
			, int contentText, RemoteViews contentView, PendingIntent contentIntent){
		showNotification(context, id, icon
				, context.getText(tickerText), context.getText(contentTitle), context.getText(contentText), contentView, contentIntent);
	}
	/**
	 * 显示状态栏提示
	 * @param context
	 * @param id ID,0则采用默认值
	 * @param icon
	 * @param tickerText 提示时显示的滚动信息,null时则不滚动显示
	 * @param contentTitle 内容标题
	 * @param contentText 内容
	 * @param contentView 点击显示的内容View,无则null
	 * @param contentIntent 点击跳转用PendingIntent
	 */
	public static void showNotification(Context context, int id, int icon, CharSequence tickerText, CharSequence contentTitle
			, CharSequence contentText, RemoteViews contentView, PendingIntent contentIntent){
		showNotification(context, id, icon, tickerText, contentTitle, contentText, contentView, contentIntent, true);
	}
	/**
	 * 显示状态栏提示
	 * @param context
	 * @param id ID,0则采用默认值
	 * @param notification 自定义Notification
	 * @param cancelLast 是否立即取消上一个提示
	 */
	public static void showNotification(Context context, int id, Notification notification, boolean cancelLast){
		int notificationId = id == 0 ? NOTIFICATION_DEFAULT_ID : id;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (cancelLast){
			notificationManager.cancel(id);
		}
		notificationManager.notify(notificationId, notification);
	}
	/**
	 * 取消默认状态栏通知
	 * @param context
	 */
	public static void cancelDefaultNotification(Context context){
		cancelNotification(context, 0);
	}
	/**
	 * 取消状态栏通知
	 * @param context
	 * @param id 通知ID,0则为默认值
	 */
	public static void cancelNotification(Context context, int id){
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id == 0 ? NOTIFICATION_DEFAULT_ID : id);
	}
	//#endregion Notification集
	
	//#region 延迟任务执行
	/**
	 * 延迟重复执行任务
	 * @param delayMS 延迟毫秒数
	 * @param repeatCount 重复次数
	 * @param listener 监听
	 */
	public static DelayTask doDelayTask(int delayMs, int repeatCount, OnDelayExecuteListener listener){
		DelayTask delayTask = new DelayTask(delayMs, repeatCount, listener);
		delayTask.start();
		return delayTask;
	}
	/**
	 * 延迟重复执行任务
	 * @param delayMS 延迟毫秒数
	 * @param listener 监听
	 */
	public static DelayTask doDelayTask(int delayMs, OnDelayExecuteListener listener){
		return doDelayTask(delayMs, 0, listener);
	}
	//#endregion 延迟任务执行
	
	//#region 通过字符串名称取得res下的资源
	/**
	 * 取得资源字符串
	 * @param resId
	 * @return
	 */
	public static String getString(int resId){
		return getGlobalApplicationContext().getString(resId);
	}
	/**
	 * 取得资源字符串
	 * @param resId
	 * @param formatArgs
	 * @return
	 */
	public static String getString(int resId, Object... formatArgs){
		return getGlobalApplicationContext().getString(resId, formatArgs);
	}
	/**
	 * 取得指定名称的图片资源ID
	 * @param imageName 图片名称
	 * @return
	 */
	public static int getImageResId(String imageName){
		return getGlobalApplicationContext().getResources().getIdentifier(imageName, "drawable", getGlobalApplicationContext().getPackageName());
	}
	/**
	 * 取得指定名称的bitmap
	 * @param imageName 图片名称
	 * @return
	 */
	public static Bitmap getBitmapByName(String imageName){
		Bitmap result = null;
		try{
			int resId = getImageResId(imageName);
			if (resId > 0){
				result = BitmapFactory.decodeResource(getGlobalApplicationContext().getResources(), resId);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	/**
	 * 取得指定名称的Drawable
	 * @param imageName 图片名称
	 * @return
	 */
	public static Drawable getDrawableByName(String imageName){
		Drawable result = null;
		try{
			int resId = getImageResId(imageName);
			if (resId > 0){
				result = getGlobalApplicationContext().getResources().getDrawable(resId);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 取得指定名称的字符串资源ID
	 * @param stringName 字符串名称
	 * @return
	 */
	public static int getStringResId(String stringName){
		return getResId(stringName, "string");
	}
	/**
	 * 取得指定名称的布局资源id
	 * @param layoutName
	 * @return
	 */
	public static int getLayoutResId(String layoutName){
		return getResId(layoutName, "layout");
	}
	/**
	 * 取得指定名称的动画资源id
	 * @param animName
	 * @return
	 */
	public static int getAnimResId(String animName){
		return getResId(animName, "anim");
	}
	/**
	 * 取得指定名称的资源id
	 * @param idName
	 * @return
	 */
	public static int getIdResId(String idName){
		return getResId(idName, "id");
	}
	/**
	 * 取得指定名称的样式id
	 * @param styleableString
	 * @return
	 */
	public static int getStyleableResId(String styleableString){
		return getResId(styleableString, "styleable");
	}
	/**
	 * 通过名字和类型取得res id
	 * @param resName
	 * @param type
	 * @return
	 */
	private static int getResId(String resName, String type){
		return getGlobalApplicationContext().getResources()
				.getIdentifier(resName, type, getGlobalApplicationContext().getPackageName());
	}
	/**
	 * 取得指定名称的数组资源
	 * @param arrayName 数组名称
	 * @return
	 */
	public static String[] getStringArray(String arrayName){
		Resources r = getGlobalApplicationContext().getResources();
		return r.getStringArray(r.getIdentifier(arrayName, "array", getGlobalApplicationContext().getPackageName()));
	}
	//#endregion 通过字符串名称取得res下的资源
	
	//#region 取默认存储地址
	
	private static String sExternalStoragePath = null;
	private static String sBaseCachePath = null;
	private static String sBaseFilePath = null;
	/**
	 *  取得SD卡路径(无SD卡则使用RAM)</br>
	 * @return 类似这样的路径 /mnt/sdcard/
	 */
	public static String getExternalStoragePath(){
		if(Helper.isNull(sExternalStoragePath)){
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite()){
				sExternalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath().concat(File.separator);
			}
			if(Helper.isEmpty(sExternalStoragePath)){
				sExternalStoragePath = getGlobalApplicationContext().getCacheDir().getPath().concat(File.separator);
			}
		}
		return sExternalStoragePath;
	}
	/**
	 * 取得基本的缓存路径(无SD卡则使用RAM，在应用程序被删除时，此文件目录也会被删除)</br>
	 * @return String 类似这样的路径 /mnt/sdcard/Android/data/demo.android/cache/ 或者 /data/data/demo.android/cache/
	 */
	public static String getBaseCachePath(){
		if(Helper.isNull(sBaseCachePath)){
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) 
					&& Environment.getExternalStorageDirectory().canWrite()) {
				sBaseCachePath = getGlobalApplicationContext().getExternalCacheDir().getAbsolutePath().concat(File.separator);
			}
			if (Helper.isEmpty(sBaseCachePath)){
				sBaseCachePath = getGlobalApplicationContext().getCacheDir().getPath().concat(File.separator);
			}
		}
		return sBaseCachePath;
	}
	/**
	 * 取得默认类型的基本的文件路径(无SD卡则使用RAM，在应用程序被删除时，此文件目录也会被删除)</br>
	 * @return String 类似这样的路径 /mnt/sdcard/Android/data/demo.android/files/Download/ 或者 /data/data/demo.android/files/
	 */
	public static String getBaseFilePath(){
		if(Helper.isNull(sBaseFilePath)){
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) 
					&& Environment.getExternalStorageDirectory().canWrite()) {
				sBaseFilePath = getGlobalApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath().concat(File.separator);
			}
			if (Helper.isEmpty(sBaseFilePath)){
				sBaseFilePath = getGlobalApplicationContext().getFilesDir().getPath().concat(File.separator);
			}
		}
		return sBaseFilePath;
	}
	//#endregion 取默认存储地址
	
	//#region 便捷方法
	private static int[] sScreenSize = null;
	/**
	 * 取得屏幕尺寸(0:宽度; 1:高度)
	 * @return
	 */
	public static int[] getScreenSize(){
		if(Helper.isNull(sScreenSize)){
			sScreenSize = new int[2];
			DisplayMetrics dm = getGlobalApplicationContext().getResources().getDisplayMetrics();
			sScreenSize[0] = dm.widthPixels;
			sScreenSize[1] = dm.heightPixels;
		}
		return sScreenSize;
	}
	/**
	 * 取得屏幕宽度
	 * @return
	 */
	public static int getScreenWidth(){
		return getScreenSize()[0];
	}
	/**
	 * 取得屏幕高度
	 * @return
	 */
	public static int getScreenHeight(){
		return getScreenSize()[1];
	}
	private static float sDisplayMetricsDensity = -1;
	/**
	 * 计算DPI值对应的PX
	 * @param dpi
	 * @return
	 */
	public static int calDpi2px(int dpi){
		if(dpi == 0){
			return dpi;
		}
		if(sDisplayMetricsDensity < 0){
			sDisplayMetricsDensity = getGlobalApplicationContext().getResources().getDisplayMetrics().density;
		}
		return Math.round(dpi * sDisplayMetricsDensity); 
	}
	
	/**
	 * 取得mac地址
	 * @return
	 */
	public static String getMacAddress(){
		try{
			WifiManager wifi = (WifiManager)getGlobalApplicationContext().getSystemService(Context.WIFI_SERVICE);
			return wifi.getConnectionInfo().getMacAddress();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 判断指定的服务是否已启动
	 * @param context
	 * @param serviceFullName 服务全名(包括包名)
	 * @return
	 */
	public static final boolean isServiceRunning(Context context, String serviceFullName){
		ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = manager.getRunningServices(40);
		if (Helper.isNotEmpty(runningServices)){
			for (RunningServiceInfo runningService : runningServices) {
				if (runningService.service.getClassName().toString().equals(serviceFullName)){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 判断某个intent是否可用
	 * <p> e.g. 判断系统是否可以处理mailto数据：
	 * <p>intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:mayasoft@mayasoft.com.cn"));
	 * @param context 上下文
	 * @param intent intent
	 * @return 是否可用
	 */
	public static final boolean isIntentAvailable(Context context, Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	//#endregion 便捷方法
	
	//#region 获取当前软件信息
	/**
	 * 取得当前软件版本
	 * @return
	 */
	public static int getCurrentVersion(){
		int versionCode = 0;
		try {
			PackageInfo info = getGlobalApplicationContext().getPackageManager()
					.getPackageInfo(getGlobalApplicationContext().getPackageName(), 0);
			versionCode = info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	/**
	 * 取得当前软件版本名称
	 * @return
	 */
	public static String getCurrentVersionName(){
		String versionName = "";
		try {
			PackageInfo info = getGlobalApplicationContext().getPackageManager()
					.getPackageInfo(getGlobalApplicationContext().getPackageName(), 0);
			versionName = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
	//#endregion 获取当前软件信息
}
