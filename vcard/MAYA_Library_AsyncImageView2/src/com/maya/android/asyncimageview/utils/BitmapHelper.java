package com.maya.android.asyncimageview.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.maya.android.utils.Helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 图片工具类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-12-9
 *
 */
public class BitmapHelper {
	
	/**
	 * 缓存图片文件到SD卡(PNG格式)
	 * @param bitmap
	 * @param filename
	 * @return 保存成功与否
	 */
	public static boolean saveBitmap(Bitmap bitmap, String filename) {
		return saveBitmap(bitmap, filename, Bitmap.CompressFormat.PNG);
	}
	
	/**
	 * 缓存图片文件到SD卡
	 * @param bitmap
	 * @param filename
	 * @param expandedName 扩展名，参见Bitmap.CompressFormat
	 * @return 保存成功与否
	 */
	public static boolean saveBitmap(Bitmap bitmap, String filename, Bitmap.CompressFormat format) {
		if (bitmap != null) {
			File file = new File(filename);
			file.getParentFile().mkdirs();
			FileOutputStream fileOut = null;
			try {
				fileOut = new FileOutputStream(filename);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bitmap.compress(format, 80, fileOut);
			try {
				fileOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fileOut.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Byte转BMP
	 * @param bytes
	 * @param opts
	 * @return
	 */
	public static Bitmap bytes2Bitmap(byte[] bytes, BitmapFactory.Options opts) {
		if (bytes != null){
			if (opts != null){
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			}else{
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
		}
		return null;
	}
	
    /**
     * 把图片变成圆角
     * @param bitmap 需要修改的图片
     * @param radian (取值为1~99之间，0和100，别闹，旁边站去)圆角的弧度 -2 表示圆形; -1表示不处理
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int radian) {

//        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap
//                .getHeight(), Config.ARGB_8888);
    	if(radian == -1){
    		return bitmap;
    	}
    	Bitmap result = null;
    	if(Helper.isNotNull(bitmap)){
	    	final int width = bitmap.getWidth();
	    	final int height = bitmap.getHeight();
	    	if(Helper.isNotNull(bitmap.getConfig())){
	    		result = Bitmap.createBitmap(width, height, bitmap.getConfig());
	    	}else{
	    		result = Bitmap.createBitmap(width, height, Config.RGB_565);
	    	}
//	    	result = bitmap;
	        Canvas canvas = new Canvas(result);
	
	        int color = 0xff424242;
	        Paint paint = new Paint();
	        Rect rect = new Rect(0, 0, width, height);
	        RectF rectF = new RectF(rect);
	        float roundPx;
	        // zheng_cz 增加圆形判断 2014-5-20
	        if(radian == -2){
	        	roundPx = width / 2;
	        }else{
	        	roundPx = (((float)radian) / 100) * width;
	        }
	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
    	}
    	return result;
    }
    /**
     * 把图片变成圆角
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @param width 输出的图片宽度
     * @param height 输出的图片的高度
     * @return 圆角图片
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels, int width, int height) {
    	Bitmap result = null;
    	if(Helper.isNotNull(bitmap)){
    		result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	    	Canvas canvas = new Canvas(result);
	    	
	    	int color = 0xff424242;
	    	Paint paint = new Paint();
	    	Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    	Rect rect2 = new Rect(0, 0, width, height);
	    	RectF rectF = new RectF(rect2);
	    	float roundPx = pixels;
	    	
	    	paint.setAntiAlias(true);
	    	canvas.drawARGB(0, 0, 0, 0);
	    	paint.setColor(color);
	    	canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    	
	    	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    	canvas.drawBitmap(bitmap, rect, rect2, paint);
    	}
    	
    	return result;
    }
    /**
	 * 如果bitmap的width>height时，进行旋转90度
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getPortraitBitmap(Bitmap bitmap){
		Bitmap result = bitmap;
		if(Helper.isNotNull(bitmap)){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if(width > height){
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postRotate(90);
				try{
					result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
				}catch(OutOfMemoryError e1){
					e1.printStackTrace();
					matrix.postScale(0.5f, 0.5f);
					try{
						result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
					}catch(OutOfMemoryError e2){
						e2.printStackTrace();
						matrix.postScale(0.5f, 0.5f);
						result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
					}
				}
//				if(result != bitmap){
//					bitmap.recycle();
//					bitmap = null;
//				}
			}
		}
		return result;
	}
	/**
	 * 如果bitmap的height>width时，进行旋转90度
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getLandscapeBitmap(Bitmap bitmap){
		Bitmap result = bitmap;
		if(Helper.isNotNull(bitmap)){
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if(height > width){
				Matrix matrix = new Matrix();
				matrix.reset();
				matrix.postRotate(90);
				try{
					result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
				}catch(OutOfMemoryError e1){
					e1.printStackTrace();
					matrix.postScale(0.5f, 0.5f);
					try{
						result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
					}catch(OutOfMemoryError e2){
						e2.printStackTrace();
						matrix.postScale(0.5f, 0.5f);
						result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
					}
				}
//				if(result != bitmap){
//					bitmap.recycle();
//					bitmap = null;
//				}
			}
		}
		return result;
	}
	
    /**
     * 使圆角功能支持BitampDrawable
     * @param drawable 
     * @param pixels 
     * @return
     */
    public static Drawable toRoundCorner(Drawable drawable, int pixels) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        drawable = new BitmapDrawable(Resources.getSystem(), toRoundCorner(bitmap, pixels));
        return drawable;
    }
    /**
     * 使圆角功能支持BitampDrawable
     * @param bitmapDrawable 
     * @param pixels 
     * @return
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels, int width, int height) {
    	Bitmap bitmap = bitmapDrawable.getBitmap();
    	bitmapDrawable = new BitmapDrawable(Resources.getSystem(), toRoundCorner(bitmap, pixels, width, height));
    	return bitmapDrawable;
    }
    /**
     * 回收bitmap(在调用后若返回的是true,建议将此bitmap设置为null)
     * @param bitmap
     * @return
     */
    public static boolean recycleBitmap(Bitmap bitmap){
    	if (Helper.isNotNull(bitmap)){
    		if (!bitmap.isRecycled()){
    			bitmap.recycle();
    		}
    		//System.gc();
    		return true;
    	}
    	return false;
    }
}
