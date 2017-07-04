package com.maya.android.ocr.manager;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.WindowManager;

import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;

import java.util.regex.Pattern;
/**
 * 拍照相机参数管理类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-11-26
 *
 */
public class CameraConfigManager {
	
	private static final String TAG = CameraConfigManager.class.getSimpleName();
	private static final Pattern COMMA_PATTERN = Pattern.compile(",");
	private static final int PICTURE_SIZE = 2000;
	
	private static CameraConfigManager sInstance;
	private Context mContext;
	private Point mScreenResolution;
	private Point mCameraResolution;
	private Point mInitResolurion = null;
	
	public static CameraConfigManager getInstance(Context context){
		if(Helper.isNull(sInstance)){
			sInstance = new CameraConfigManager(context);
		}
		return sInstance;
	}
	
	private CameraConfigManager(Context context){
		this.mContext = context;
	}
	@SuppressWarnings("unused")
	private int mVerificationTwo = 800253060;
	/**
	 * 初始化相机参数
	 * @param camera
	 * @param screenResolution
	 */
	public void initFromCameraParameters(Camera camera, Point screenResolution){
		if(Helper.isNotNull(this.mInitResolurion) && Helper.isNotNull(screenResolution) 
				&& this.mInitResolurion.x == screenResolution.x && this.mInitResolurion.y == screenResolution.y){
			return;
		}
		if(Helper.isNotNull(camera)){
			if(Helper.isNull(screenResolution)){
				WindowManager manager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
			    Display display = manager.getDefaultDisplay();
				screenResolution = new Point(display.getHeight(), display.getWidth());
			}
			this.mInitResolurion = screenResolution;
			Camera.Parameters parameters = camera.getParameters();
			this.mScreenResolution = screenResolution;
			LogHelper.d(TAG, "Screen resolution: " + this.mScreenResolution);
			Point taskPhotoResolution = null;
			if(screenResolution.y * 2 < PICTURE_SIZE){
				taskPhotoResolution = new Point(screenResolution.x * 2, screenResolution.y * 2);
			}else if((int)(screenResolution.y * 1.5) < PICTURE_SIZE){
				taskPhotoResolution = new Point((int)(screenResolution.x * 1.5), (int)(screenResolution.y * 1.5));
			}else{
				taskPhotoResolution = screenResolution;
			}
//			taskPhotoResolution = screenResolution;
			this.mCameraResolution = getCameraResolution(parameters, taskPhotoResolution);
			LogHelper.d(TAG, "Camera resolution: " + this.mCameraResolution);
		}
	}
	
	/**
	 * 设置相机
	 * @param camera
	 */
	public void setDesiredCameraParameters(Camera camera) {
		if(Helper.isNotNull(camera) && Helper.isNotNull(this.mCameraResolution)){
		    Camera.Parameters parameters = camera.getParameters();
			LogHelper.d(TAG, "Setting preview size: " + this.mCameraResolution);
		    parameters.setPreviewSize(this.mCameraResolution.x, this.mCameraResolution.y);
//		    parameters.setPictureSize(this.mCameraResolution.y, this.mCameraResolution.x);
		    parameters.setPictureSize(this.mCameraResolution.x, this.mCameraResolution.y);
	//	    setFlash(parameters);
	//	    setZoom(parameters);
		    //setSharpness(parameters);
		    try {
		    	camera.setParameters(parameters);
			} catch (Exception e) {
				LogHelper.e(TAG, "setParameters failed");
			}
		}else{
			LogHelper.e(TAG, "未进行初始化或camera为NULL！");
		}
	  }
	
	private static Point getCameraResolution(Camera.Parameters parameters, Point screenResolution) {

	    String previewSizeValueString = parameters.get("preview-size-values");
	    // saw this on Xperia
	    if (previewSizeValueString == null) {
	      previewSizeValueString = parameters.get("preview-size-value");
	    }

	    Point cameraResolution = null;

	    if (previewSizeValueString != null) {
			LogHelper.d(TAG, "preview-size-values parameter: " + previewSizeValueString);
	        cameraResolution = findBestPreviewSizeValue(previewSizeValueString, screenResolution);
	    }

	    if (cameraResolution == null) {
	      // Ensure that the camera resolution is a multiple of 8, as the screen may not be.
	      cameraResolution = new Point(
	          (screenResolution.x >> 3) << 3,
	          (screenResolution.y >> 3) << 3);
	    }

	    return cameraResolution;
	  }
	
	private static Point findBestPreviewSizeValue(CharSequence previewSizeValueString, Point screenResolution) {
	    int bestX = 0;
	    int bestY = 0;
	    int diff = Integer.MAX_VALUE;
	    double minDiff = Double.MAX_VALUE;
	    double screenRatio = (double) screenResolution.x / screenResolution.y;
	    for (String previewSize : COMMA_PATTERN.split(previewSizeValueString)) {
	
	      previewSize = previewSize.trim();
	      int dimPosition = previewSize.indexOf('x');
	      if (dimPosition < 0) {
			  LogHelper.w(TAG, "Bad preview-size: " + previewSize);
	          continue;
	      }
	
	      int newX;
	      int newY;
	      try {
	    	  newX = Integer.parseInt(previewSize.substring(0, dimPosition));
	          newY = Integer.parseInt(previewSize.substring(dimPosition + 1));
	      } catch (NumberFormatException nfe) {
			  LogHelper.w(TAG, "Bad preview-size: " + previewSize);
	          continue;
	      }
	
	      int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
	      double ratio = (double) newX / newY;
		  
		  if (Math.abs(ratio - screenRatio) > 0.1){
			  continue;
		  }
		  if (Math.abs(ratio - screenRatio) < minDiff) {
		        bestX = newX;
		        bestY = newY;
		        minDiff = Math.abs(ratio - screenRatio) ; 
			  
			  // 宽高最接近 实际屏幕宽高
			  if( minDiff == 0 &&  newDiff < diff){
			        bestX = newX;
			        bestY = newY;
			        diff = newDiff;
			  }
		  }
		  
//	      if (newDiff == 0) {
//	        bestX = newX;
//	        bestY = newY;
//	        
//	        break;
//	      } else if (newDiff < diff) {
//	        bestX = newX;
//	        bestY = newY;
//	        diff = newDiff;
//	      }
	    }
	    
	    if (bestX > 0 && bestY > 0) {
	      return new Point(bestX, bestY);
	    }
	    return null;
	  }
}
