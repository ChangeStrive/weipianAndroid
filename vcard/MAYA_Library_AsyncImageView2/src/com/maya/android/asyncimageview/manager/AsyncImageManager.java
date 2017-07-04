package com.maya.android.asyncimageview.manager;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;

import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;
import net.tsz.afinal.bitmap.display.Displayer;
import net.tsz.afinal.bitmap.download.Downloader;

/**
 * <p>异步图片管理类</p> 
 * 用于异步加载图片用的,请尽可能使用静态方法,使用方法主要有两个: </br>
 * 1.addAsyncImage,传递图片url及使用其的AsyncImageView既可,将自动完成图片下载并显示到AsyncImageView中 </br>
 * 2.addAsyncDownload,参数为图片url以及下载结束后的回调方法,回调处理下载好的结果 </br>
 * <br/>
 * 其它还有关于cache的相关方法,在需要时进行清除 </br>
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-6-6
 *
 */
public class AsyncImageManager {
	//#region 常量

	//#endregion 常量

	//#region 静态成员变量
	private static FinalBitmap sInstance = null;
	private static String sFinalBitmapCache = null;
	//#endregion 静态成员变量

	//#region 成员变量

	//#endregion 成员变量

	//#region Getter & Setter集
	private static FinalBitmap getFinalBitmapInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = FinalBitmap.create(ActivityHelper.getGlobalApplicationContext());
		}
		return sInstance;
	}
	//#endregion Getter & Setter集

	//#region 构造方法

	//#endregion 构造方法

	//#region overwrite 方法

	//#endregion overwrite 方法

	//#region public 方法

	//#endregion public 方法
	/**
	 * 设置缓存路径
	 * @param cachePath
	 */
	public static void setCachePath(String cachePath){
		if(Helper.isNotEmpty(cachePath)){
			if(Helper.isNull(sFinalBitmapCache) || !Helper.equalString(sFinalBitmapCache, cachePath, true)){
				sFinalBitmapCache = cachePath;
				getFinalBitmapInstance().configDiskCachePath(sFinalBitmapCache);
			}
		}
	}
	
	public static void addAsyncImage(AsyncImageView imageView, String url){
		getFinalBitmapInstance().display(imageView, url);
	}
	
	public static void addAsyncImage(AsyncImageView imageView, String url, Bitmap loadingBitmap){
		getFinalBitmapInstance().display(imageView, url, loadingBitmap);
	}
	
	public static void addAsyncImage(AsyncImageView imageView, String url, BitmapDisplayConfig config){
		getFinalBitmapInstance().display(imageView, url, config);
	}
	
	public static void addAsyncImage(AsyncImageView imageView, String url, Bitmap loadingBitmap, Bitmap loadfailBitmap){
		getFinalBitmapInstance().display(imageView, url, loadingBitmap, loadfailBitmap);
	}
	
	public static void addAsyncImage(AsyncImageView imageView, String url, int imageWidth, int imageHeight){
		getFinalBitmapInstance().display(imageView, url, imageWidth, imageHeight);
	}
	
	public static void addAsyncImage(AsyncImageView imageView, String url, int imageWidth, int imageHeight,Bitmap loadingBitmap, Bitmap loadfailBitmap){
		getFinalBitmapInstance().display(imageView, url, imageWidth, imageHeight, loadingBitmap, loadfailBitmap);
	}
	
	public static void setBitmapMaxWidthAndBitmapMaxHeight(int bitmapWidth, int bitmapHeight){
		getFinalBitmapInstance().configBitmapMaxWidth(bitmapWidth);
		getFinalBitmapInstance().configBitmapMaxHeight(bitmapHeight);
	}
	
	public static void setDisplayer(Displayer displayer){
		getFinalBitmapInstance().configDisplayer(displayer);
	}
	
	public static void setDownlader(Downloader downlader){
		getFinalBitmapInstance().configDownlader(downlader);
	}
	
	public static void setLoadFailImage(Bitmap bitmap){
		getFinalBitmapInstance().configLoadfailImage(bitmap);
	}
	
	public static void setLoadFailImage(int resId){
		getFinalBitmapInstance().configLoadfailImage(resId);
	}

	public static void setLoadingImage(Bitmap bitmap){
		getFinalBitmapInstance().configLoadingImage(bitmap);
	}
	
	public static void setLoadingImage(int resId){
		getFinalBitmapInstance().configLoadingImage(resId);
	}
	
	public static void clearMemoryCache(){
		getFinalBitmapInstance().clearMemoryCache();
	}
	
	public static void closeCache(){
		getFinalBitmapInstance().closeCache();
	}

	public static void clearAllCache(){
		getFinalBitmapInstance().clearCache();
	}
	
	/**
	 * 取得缓存中的图片(内存/SD卡)
	 * @param url
	 * @return 缓存的图片drawable,无则为null
	 */
	public static Drawable fetchCacheDrawable(String url){
		Drawable result = null;
		if(Helper.isNotEmpty(url)){
			Bitmap bitmap = getFinalBitmapInstance().getBitmapFromDiskCache(url);
			if(Helper.isNotNull(bitmap)){
				result = new BitmapDrawable(ActivityHelper.getGlobalApplicationContext().getResources(), bitmap);
			}
		}
		return result;
	}
	//#region protected 方法

	//#endregion protected 方法

	//#region private 方法

	//#endregion private 方法

	//#region interface

	//#endregion interface
}
