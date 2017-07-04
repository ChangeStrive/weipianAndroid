package com.maya.android.asyncimageview.widget;

import java.io.IOException;
import java.io.InputStream;

import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.maya.android.asyncimageview.manager.AsyncImageManager;
import com.maya.android.asyncimageview.utils.BitmapHelper;
import com.maya.android.utils.Helper;

public class AsyncImageView extends ImageView {
	//#region 常量
	private static final int DEFAULT_IMAGE_RES_ID = 0;
	//#endregion 常量

	//#region 静态成员变量

	//#endregion 静态成员变量

	//#region 成员变量
	private int mRoundCorner = -1;
	private ImageLoadListener mListener = null;
	/** 是否为竖向 **/
	private boolean isPortrait;
	/** 是否为横向 **/
	private boolean isLandscape;
	/** 是否为灰色 **/
	private boolean isGray;
	/** 默认显示图片的res id **/
	private int mDefaultImageResId = DEFAULT_IMAGE_RES_ID;
	//#endregion 成员变量

	//#region Getter & Setter集
	public void setImageLoadListener(ImageLoadListener listener){
		this.mListener = listener;
	}

	public ImageLoadListener getImageLoadListener(){
		return this.mListener;
	}
	/**
	 * 设置圆角度,-1为不设置
	 * @param roundCorner
	 */
	public void setRoundCorner(int roundCorner){
		this.mRoundCorner = roundCorner;
	}
	
	public int getRoundCorner(){
		return this.mRoundCorner;
	}
	//#endregion Getter & Setter集

	//#region 构造方法
	/**
	 * 构造方法
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 构造方法
	 * @param context
	 * @param attrs
	 */
	public AsyncImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 构造方法
	 * @param context
	 */
	public AsyncImageView(Context context) {
		super(context);
	}
	//#endregion 构造方法

	//#region overwrite 方法
	@Override
	public void setImageDrawable(Drawable drawable) {
		if(this.isGray && Helper.isNotNull(drawable)){
			drawable.mutate();
			ColorMatrix cm = new ColorMatrix();
			cm.setSaturation(0);
			ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
			drawable.setColorFilter(cf);
		}
		super.setImageDrawable(drawable);
	}
	
	//#endregion overwrite 方法

	//#region public 方法
	public void autoFillFromUrl(String url){
		AsyncImageManager.addAsyncImage(this, url);
	}
	
	public void autoFillFromUrl(String url, Bitmap loadingBitmap){
		AsyncImageManager.addAsyncImage(this, url, loadingBitmap);
	}
	
	public void autoFillFromUrl(String url, BitmapDisplayConfig config){
		AsyncImageManager.addAsyncImage(this, url, config);
	}
	
	public void autoFillFromUrl(String url, Bitmap loadingBitmap, Bitmap loadfailBitmap){
		AsyncImageManager.addAsyncImage(this, url, loadingBitmap, loadfailBitmap);
	}
	
	public void autoFillFromUrl(String url, int imageWidth, int imageHeight){
		AsyncImageManager.addAsyncImage(this, url, imageWidth, imageHeight);
	}
	
	public void autoFillFromUrl(String url, int imageWidth, int imageHeight, Bitmap loadingBitmap, Bitmap loadfailBitmap){
		AsyncImageManager.addAsyncImage(this, url, imageWidth, imageHeight, loadingBitmap, loadfailBitmap);
	}
	
	public void setBitmapMaxWidthAndBitmapMaxHeight(int bitmapWidth, int bitmapHeight){
		AsyncImageManager.setBitmapMaxWidthAndBitmapMaxHeight(bitmapWidth, bitmapHeight);
	}
	/**
	 * 设置默认显示图片的res id
	 * @param 默认显示图片的res id
	 */
	public void setDefaultImageResId(int defaultImageResId){
		if (defaultImageResId != 0){
			if (this.mDefaultImageResId != defaultImageResId){
				this.mDefaultImageResId = defaultImageResId;
//				Drawable drawable = null;
//				if(this.mRotate != 0){
//					drawable = getResources().getDrawable(mDefaultImageResId);
//					drawable = rotateImage(this.mRotate, (BitmapDrawable)drawable);
//				}
//				if(Helper.isNotNull(drawable)){
//					this.setImageDrawable(drawable);
//				}else{
//					this.setImageResource(this.mDefaultImageResId);
//				}
			}
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		try {
			InputStream is = getResources().openRawResource(mDefaultImageResId);
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
			this.setImageBitmap(bitmap);
			AsyncImageManager.setLoadingImage(bitmap); 
			is.close();
		} catch (IOException e) {
			Log.i("AsyncIMageView", "流异常");
		} catch (OutOfMemoryError e) {
			Log.i("AsyncIMageView", "内存溢出");
		} catch (Exception e) {
			Log.i("AsyncIMageView", "AsyncIMageView出现了异常");
		}
		
//		this.setImageResource(mDefaultImageResId);
//		AsyncImageManager.setLoadingImage(defaultImageResId);
	}
	/**
	 * 设置默认显示图片
	 * @param bitmap
	 */
	public void setDefaultImage(Bitmap bitmap){
		AsyncImageManager.setLoadingImage(bitmap);
	}
	
	public void setLoadingImage(Bitmap bitmap){
		AsyncImageManager.setLoadingImage(bitmap);
	}
	
	public void setLoadingImage(int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = getResources().openRawResource(resId);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		AsyncImageManager.setLoadingImage(bitmap);
	}
	
	public void setLoadFailImage(Bitmap bitmap){
		AsyncImageManager.setLoadFailImage(bitmap);
	}
	
	public void setLoadFailImage(int resId){
		AsyncImageManager.setLoadFailImage(resId);
	}
	/**
	 * 设置图片竖向展示
	 */
	public void setPortrait(boolean isPortrait){
		this.isLandscape = false;
		this.isPortrait = isPortrait;
	}
	/**
	 * 图片是否竖向展示
	 * @return
	 */
	public boolean isPortrait(){
		return this.isPortrait;
	}
	/**
	 * 设置图片横向展示
	 * @param isLandscape
	 */
	public void setLandscape(boolean isLandscape){
		this.isPortrait = false;
		this.isLandscape = isLandscape;
	}
	/**
	 * 图片是否横向展示
	 * @return
	 */
	public boolean isLandscape(){
		return this.isLandscape;
	}
	/**
	 * 设置图片灰色
	 * @param gray 是否设置为灰色,true则变灰,false则还原
	 */
	public void setGray(boolean gray){
		this.isGray = gray;
	}
	//#endregion public 方法

	//#region protected 方法

	//#endregion protected 方法

	//#region private 方法

	//#endregion private 方法

	//#region interface
	/**
	 * 图片加载监听
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2014-3-7
	 *
	 */
	public interface ImageLoadListener{
		/**
		 * 图片正在加载
		 */
		public void imageLoading();
		/**
		 * 图片加载结束
		 */
		public void imageLoadFinish();
		/**
		 * 图片加载失败
		 */
		public void imageLoadFail();
	}
	//#endregion interface
}
