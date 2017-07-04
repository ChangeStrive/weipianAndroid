package com.maya.android.vcard.util;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.data.CurrentUser;

/**
 * 百度定位Helper类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-11-29
 *
 */
public class DBLocationHelper {
	//region 常量/变量
	private static final String TAG = DBLocationHelper.class.getSimpleName();
	/**
	 * 间隔20秒时间
	 */
	public static final int TIME_SCAN_SPAN_20 = 20000;
	/**
	 * 间隔10秒时间
	 */
	public static final int TIME_SCAN_SPAN_10 = 10000;
	/**
	 * 百度定位
	 */
	private static LocationClient sLocationsClient;
	/**
	 * 间隔多少ms重新定位
	 */
	private static int sScanSpanTime = 0;
	private static Context sContext;
	private static boolean isStart = false;
	private static CurrentUser mCurrentUser = CurrentUser.getInstance();
	/**
	 * 定位监听
	 */
	private static BDLocationListener sLocationListener = new BDLocationListener(){

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (Helper.isNotNull(location)) {
				//保存经纬度
				mCurrentUser.setLatitude(location.getLatitude());
				mCurrentUser.setLongitude(location.getLongitude());
				//某些定位完可能要干些毛事
				LogHelper.d(TAG , "取得定位信息：Latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude());
				//TODO 日志打印
//				LogHelper.writeLog2File("取得定位信息：Latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude());
			} else {
				//定位失败不能干些毛事
				LogHelper.e(TAG , "取得定位信息失败");
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			//do notiong
		}
		
	};
	
	//endregion 常量/变量
	
	//region 重要方法
	/**
	 * 初始化百度定位
	 * @param context
	 */
	public static void initLocation(Context context){
		initLocation(context, sLocationListener);
	}
	/**
	 * 初始化百度定位
	 * @param context
	 * @param locationListener 百度定位监听
	 */
	public static void initLocation(Context context, BDLocationListener locationListener){
		initLocation(context, locationListener, TIME_SCAN_SPAN_20);
	}
	/**
	 * 初始化百度定位
	 * @param context
	 * @param locationListener 百度定位监听
	 * @param scanSpanTime 重新定位时间间隔
	 */
	public static void initLocation(Context context, BDLocationListener locationListener, int scanSpanTime){
		if(Helper.isNotNull(context)){
			sContext = context;
			sLocationsClient = new LocationClient(context);
			sLocationsClient.registerLocationListener(locationListener);
			LocationClientOption locationOption = new LocationClientOption();
			//打开gps
			locationOption.setOpenGps(true);
			//设置坐标类型
			locationOption.setCoorType("bd09ll");
			//设置地址信息，仅设置为“all”时有地址信息，默认无地址信息
			locationOption.setAddrType("all");
			//设置间隔多少ms定位
			if(scanSpanTime == 0){
				scanSpanTime = sScanSpanTime = TIME_SCAN_SPAN_20;
				LogHelper.e(TAG, "鄙视你哈！scanSpanTime为0，让百度如何定位？？？你来？？？？？？？就默认20秒了");
			}else{
				sScanSpanTime = scanSpanTime;
			}
			locationOption.setScanSpan(scanSpanTime);
			sLocationsClient.setLocOption(locationOption);
		}else{
			LogHelper.e(TAG, "Context 为Null，拜托，不要乱搞！！！！");
		}
	}
	/**
	 * 重新设置定位时间间隔
	 * @param scanSpanTime {TIME_SCAN_SPAN_10、TIME_SCAN_SPAN_20}
	 */
	public static void setLocationScanSpan(int scanSpanTime){
		if(Helper.isNotNull(sContext)){
			if(scanSpanTime == 0){
				scanSpanTime = TIME_SCAN_SPAN_20;
			}
			if(scanSpanTime != sScanSpanTime){
				stopLocation();
				initLocation(sContext, sLocationListener, scanSpanTime);
				startLocation();
			}
		}else{
			LogHelper.e(TAG, "请先调用initLocation方法！！！！！！！");
		}
	}
	/**
	 * 开始百度定位
	 */
	public static void startLocation(){
		if(NetworkHelper.isNetworkAvailable(ActivityHelper.getGlobalApplicationContext())){
			if(Helper.isNotNull(sLocationsClient) && !isStart){
				sLocationsClient.start();
				isStart = true;
			}else{
				LogHelper.e(TAG, "还没有初始化百度定位喔！");
			}
		}else{
			LogHelper.e(TAG, "网络没有，无法定位");
		}
	}
	/**
	 * 停止百度定位
	 */
	public static void stopLocation(){
		if(Helper.isNotNull(sLocationsClient) && isStart){
			sLocationsClient.stop();
			isStart = false;
		}else{
			LogHelper.e(TAG, "还没有初始化百度定位喔！");
		}
	}
	//endregion 重要方法
}
