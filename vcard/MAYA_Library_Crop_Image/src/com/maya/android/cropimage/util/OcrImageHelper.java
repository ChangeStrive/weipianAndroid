package com.maya.android.cropimage.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;

@SuppressLint("SimpleDateFormat")
public class OcrImageHelper {
	private static final String FILE_PATH_OCR_IMAGE = ActivityHelper.getBaseFilePath() + "ocr/";
	private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("yyyyMMDD_HHmmss");
	private static final float OCR_IMAGE_SCALE_NUM = 1.5f;
	private static int sOcrImageWidth = (int) (ActivityHelper.getScreenWidth() * OCR_IMAGE_SCALE_NUM);
	private static int sOcrImageHeight = (int) (ActivityHelper.getScreenHeight() * OCR_IMAGE_SCALE_NUM);
	/**
	 * 保存扫描识别图片
	 * @param imageData
	 * @return
	 */
	public static String saveOcrAndGetImage(byte[] imageData){
		if(Helper.isNotNull(imageData)){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
			if(options.outWidth > sOcrImageWidth || options.outHeight > sOcrImageHeight){
				if((options.outWidth > options.outHeight) && (sOcrImageWidth < sOcrImageHeight)){
					int temp = sOcrImageWidth;
					sOcrImageWidth = sOcrImageHeight;
					sOcrImageHeight = temp;
				}
				float widthScale = options.outWidth * 1.0f/sOcrImageWidth;
				float heightScale = options.outHeight * 1.0f/sOcrImageHeight;
				float wholeScale = widthScale > heightScale ? widthScale : heightScale;
				int sampleSize = (int) wholeScale;
				for(int i = 0; i < 5; i++){
					//2的i次方
					int twoPowI = (int) Math.pow(2, i);
					if(sampleSize < twoPowI){
						sampleSize = twoPowI/2;
						break;
					}else if(sampleSize == twoPowI){
						break;
					}
				}
				options.inSampleSize = sampleSize;
			}
			options.inJustDecodeBounds = false;
			try{
				bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options); 
			}catch(OutOfMemoryError e){
				e.printStackTrace();
				options.inSampleSize *= 2;
				bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
			}
			int bmpWidth = bitmap.getWidth();
			int bmpHeight = bitmap.getHeight();
			//TODO 修改1
//			//图片所在位置（大概）
//			int bmpWidth = (int) (bitmap.getWidth() * 0.9);
//			int bmpHeight = (int) (bitmap.getHeight() * 0.9);
			//按照图片3/4截图
//			if(bmpWidth > bmpHeight){
//				bmpHeight = (int) (bmpWidth * 0.75);
//			}else{
//				bmpWidth = (int) (bmpHeight * 0.75);
//			}
			Matrix matrix = null;
			if(bmpWidth > sOcrImageWidth || ((bmpHeight > bmpWidth) && (bmpHeight > sOcrImageHeight))){
				matrix = new Matrix();
				float scale = 0;
				if(bmpWidth > bmpHeight){
					scale = sOcrImageWidth * 1.0f / bitmap.getWidth();
				}else{
					scale = sOcrImageHeight * 1.0f / bitmap.getHeight();
					matrix.setRotate(270);
				}
				matrix.postScale(scale, scale);
			}
			int x = (int) ((bitmap.getWidth() - bmpWidth) * 0.5);
			int y = (int) ((bitmap.getHeight() - bmpHeight) * 0.5);
			Bitmap result = null;
			if(Helper.isNotNull(matrix)){
				result = Bitmap.createBitmap(bitmap, x, y, bmpWidth, bmpHeight, matrix, false);
			}else{
				result = Bitmap.createBitmap(bitmap, x, y, bmpWidth, bmpHeight);
			}
			if(result != bitmap){
				bitmap.recycle();
				bitmap = null;
			}
			//锐化
//			result = sharpenImageAmeliorate(result);
			//增加对比度
//			result = addContrast2Bitmap(result, 1.5f);
			return saveOcrAndGetImage(result);
		}
		return null;
	}
	/**
	 * 保持识别图片
	 * @param bitmap
	 * @return
	 */
	public static String saveOcrAndGetImage(Bitmap bitmap){
		String path = FILE_PATH_OCR_IMAGE + sSimpleDateFormat.format(new Date()) + ".jpg";
		FileOutputStream fos = null;
		if(Helper.isNull(bitmap)){
			return null;
		}
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 60, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public static Bitmap getValidOcrBitmap(String path){
		Bitmap result = null;
		if(Helper.isNotEmpty(path)){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			result = BitmapFactory.decodeFile(path, options);
			if(options.outWidth > sOcrImageWidth || ((options.outWidth < options.outHeight) && (options.outHeight > sOcrImageHeight))){
				if((options.outWidth > options.outHeight) && (sOcrImageWidth < sOcrImageHeight)){
					int temp = sOcrImageWidth;
					sOcrImageWidth = sOcrImageHeight;
					sOcrImageHeight = temp;
				}
				float widthScale = options.outWidth * 1.0f/sOcrImageWidth;
				float heightScale = options.outHeight * 1.0f/sOcrImageHeight;
				float wholeScale = widthScale > heightScale ? widthScale : heightScale;
				int sampleSize = (int) wholeScale;
				for(int i = 0; i < 5; i++){
					//2的i次方
					int twoPowI = (int) Math.pow(2, i);
					if(sampleSize < twoPowI){
						sampleSize = twoPowI/2;
						break;
					}else if(sampleSize == twoPowI){
						break;
					}
				}
				options.inSampleSize = sampleSize;
			}
			options.inJustDecodeBounds = false;
			result = BitmapFactory.decodeFile(path, options);
			if(Helper.isNull(result)){
				return null;
			}
			int bmpWidth = result.getWidth();
			int bmpHeight = result.getHeight();
			Matrix matrix = null;
			if(bmpWidth > sOcrImageWidth || ((bmpHeight > bmpWidth) && (bmpHeight > sOcrImageHeight))){
				matrix = new Matrix();
				float scale = 0;
				if(bmpWidth > bmpHeight){
					scale = sOcrImageWidth * 1.0f / result.getWidth();
				}else{
					scale = sOcrImageHeight * 1.0f / result.getHeight();
					matrix.setRotate(270);
				}
				matrix.postScale(scale, scale);
			}
			Bitmap result2 = null;
			if(Helper.isNotNull(matrix)){
				result2 = Bitmap.createBitmap(result, 0, 0, bmpWidth, bmpHeight, matrix, false);
			}
			if(Helper.isNotNull(result2) && result != result2){
				result.recycle();
				result = null;
			}
			if(Helper.isNotNull(result2)){
				return result2;
			}
		}
		return result;
	}
	/**
	 * 对比度增强
	 * @param bitmap
	 * @param contrast
	 * @return
	 */
	public static Bitmap addContrast2Bitmap(Bitmap bitmap, float contrast){
		if(Helper.isNotNull(bitmap)){
			Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			ColorMatrix cMatrix = new ColorMatrix();
			cMatrix.set(new float[] { contrast, 0, 0, 0, 0,
									0, contrast, 0, 0, 0,// 改变对比度
									0, 0, contrast, 0, 0,
									0, 0, 0, 1, 0 });
			Paint paint = new Paint();
			paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

			Canvas canvas = new Canvas(result);
			// 在Canvas上绘制一个已经存在的Bitmap
			canvas.drawBitmap(bitmap, 0, 0, paint);
			if(result != bitmap){
				bitmap.recycle();
				bitmap = null;
			}
			return result;
		}
		return bitmap;
	}
	
	/**
	 * 图片锐化（拉普拉斯变换）
	 * @param bmp
	 * @return
	 */
	public static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
		if(Helper.isNotNull(bmp)){
			// 拉普拉斯矩阵
			int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
	
			int width = bmp.getWidth();
			int height = bmp.getHeight();
			Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	
			int pixR = 0;
			int pixG = 0;
			int pixB = 0;
	
			int pixColor = 0;
	
			int newR = 0;
			int newG = 0;
			int newB = 0;
	
			int idx = 0;
			float alpha = 0.3F;
			int[] pixels = new int[width * height];
			bmp.getPixels(pixels, 0, width, 0, 0, width, height);
			for (int i = 1, length = height - 1; i < length; i++) {
				for (int k = 1, len = width - 1; k < len; k++) {
					idx = 0;
					for (int m = -1; m <= 1; m++) {
						for (int n = -1; n <= 1; n++) {
							pixColor = pixels[(i + n) * width + k + m];
							pixR = Color.red(pixColor);
							pixG = Color.green(pixColor);
							pixB = Color.blue(pixColor);
	
							newR = newR + (int) (pixR * laplacian[idx] * alpha);
							newG = newG + (int) (pixG * laplacian[idx] * alpha);
							newB = newB + (int) (pixB * laplacian[idx] * alpha);
							idx++;
						}
					}
	
					newR = Math.min(255, Math.max(0, newR));
					newG = Math.min(255, Math.max(0, newG));
					newB = Math.min(255, Math.max(0, newB));
	
					pixels[i * width + k] = Color.argb(255, newR, newG, newB);
					newR = 0;
					newG = 0;
					newB = 0;
				}
			}
	
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			return bitmap;
		}
		return bmp;
	}
}
