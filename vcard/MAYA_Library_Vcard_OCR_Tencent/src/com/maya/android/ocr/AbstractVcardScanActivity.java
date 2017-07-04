package com.maya.android.ocr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.maya.android.ocr.manager.CameraConfigManager;
import com.maya.android.ocr.util.OcrImageHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
/**
 * 名片识别扫描抽象类</br>
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-8-6
 *
 */
public abstract class AbstractVcardScanActivity extends Activity {
	//#region 常量
	private static final String TAG = AbstractVcardScanActivity.class.getSimpleName();
	private static final int HANDLER_WHAT_GET_PICTURE = 1001;
	private static final int HANDLER_WHAT_GET_PICTURE_BACK = 1002;
	private static final int HANDLER_WHAT_CAMERA_AUTO_FOCUS = 2001;
	private static final int TIME_CAMERA_AUTO_FOCUS = 4000;
	private static final int POINT_SIZE_WIDTH = (int)(1000*1.0/ActivityHelper.getScreenHeight()* ActivityHelper.getScreenWidth());
	private static final int POINT_SIZE_HEIGHT = 1000;
	//#endregion 常量

	//#region 静态成员变量

	//#endregion 静态成员变量

	//#region 成员变量

	// 截取的 x , y 起点
	protected int mStartX,mStartY;
	// 截取 图片的 宽 高
	protected int mImgWidth,mImgHeight;
	// 图片缩放的 宽 高
	protected int mImgScaleWidth,mImgScaleHeight;
	
	private boolean isLive;
	private boolean isCapturePic;
	private boolean isStoppingRec;
	private boolean isRec;
	private boolean isCameraError;
	private boolean isPreviewing;
	private Camera mCamera;
	private SurfaceHolder mSurfaceHolder;
	private SurfaceView mSurfaceView;
	private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
//			closeCamera();
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
				startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}
	};
	private CameraAutoFocusCallback mCameraAutoFocusCallback;
	private CameraShutterCallback mCameraShutterCallback;
	private CameraRawPictureCallback mCameraRawPictureCallback;
	private CameraJpegPictureCallback mCameraJpegPictureCallback;
	private byte[] mPictureData;
	private byte[] mPictureBackData;
	private boolean isTwoSideMode = false;
	/** 相机是否为横向（默认为横向） **/
	private boolean isCameraLandscapeOrientation = true;
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case HANDLER_WHAT_GET_PICTURE:
				if(Helper.isNotNull(mPictureData)){
					if(!isTwoSideMode){
						stopPreview();
					}
					String picturePath = OcrImageHelper.saveOcrAndGetImage(mPictureData);
//					AbstractVcardTrimAndRecognitionActivity.setPhotoData(mPictureData);
//					AbstractVcardTrimAndRecognitionActivity.setPhotoPath(picturePath);
					mPictureBackData = null;
					getPhotoData(mPictureData, picturePath);
					if(!isTwoSideMode){
						switchToClass();
					}else{
						ActivityHelper.showToast(R.string.hint_abs_vcard_scan_ocr_second);
						mHandler.removeMessages(HANDLER_WHAT_CAMERA_AUTO_FOCUS);
						mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_CAMERA_AUTO_FOCUS, 1000);
						startScanBack();
						startCameraResume();
					}
				}
				break;
			case HANDLER_WHAT_GET_PICTURE_BACK:
				if(Helper.isNotNull(mPictureBackData)){
					stopPreview();
//					AbstractVcardTrimAndRecognitionActivity.setPhotoBackData(mPictureBackData);
					String picturePath = OcrImageHelper.saveOcrAndGetImage(mPictureBackData);
//					AbstractVcardTrimAndRecognitionActivity.setPhotoBackPath(picturePath);
					mPictureBackData = null;
					getPhotoBackData(mPictureBackData, picturePath);
					switchToClass();
				}
				break;
			case HANDLER_WHAT_CAMERA_AUTO_FOCUS:
				mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_CAMERA_AUTO_FOCUS, TIME_CAMERA_AUTO_FOCUS);
				if(Helper.isNotNull(mCamera) && Helper.isNotNull(mCameraAutoFocusCallback)){
					try {
						mCamera.autoFocus(mCameraAutoFocusCallback);
					} catch (Exception e) {
						Log.i(TAG, "自动对焦失败");
					}
				}
				break;
				default:
					break;
			}
		}
		
	};
	//#endregion 成员变量

	//#region Getter & Setter集
	protected Camera getCamera(){
		if(Helper.isNull(this.mCamera)){
			try {
				this.mCamera = Camera.open();
			} catch (Exception e) {
				Log.i(TAG, "相机打开失败");
			}
			Point cameraPoint = null;
			if(this.isCameraLandscapeOrientation()){
				cameraPoint = new Point(ActivityHelper.getScreenHeight(), ActivityHelper.getScreenWidth());
			}else{
				cameraPoint = new Point(ActivityHelper.getScreenWidth(), ActivityHelper.getScreenHeight());
			}
			CameraConfigManager.getInstance(this).initFromCameraParameters(this.mCamera, cameraPoint);
			CameraConfigManager.getInstance(this).setDesiredCameraParameters(this.mCamera);
//			Parameters parameters = this.mCamera.getParameters();
//			Size size = parameters.getPictureSize();
//			if(size.height < Extra.OCR_IMAGE_SIZE_MAX){
//				int scale = 0;
//				for(int i = 2; i < 5; i++){
//					if(size.height * i > Extra.OCR_IMAGE_SIZE_MAX){
//						scale = i;
//						break;
//					}
//				}
//				parameters.setPictureSize(size.width * scale, size.height * scale);
//				this.mCamera.setParameters(parameters);
//			}
		}
		return this.mCamera;
	}
	/**
	 * 设置双面拍摄
	 * @param isTwoSideMode
	 */
	protected void setTwoSideMode(boolean isTwoSideMode){
		this.isTwoSideMode = isTwoSideMode;
//		AbstractVcardTrimAndRecognitionActivity.setTwoSideMode(this.isTwoSideMode);
	}
	/**
	 * 相机是否为横向（默认是横向）
	 * @return
	 */
	public boolean isCameraLandscapeOrientation() {
		return isCameraLandscapeOrientation;
	}
	/**
	 * 设置相机是否为横向</br>
	 * 最好在super.onCreate或getCamera方法前用
	 * @param isCameraLandscapeOrientation
	 */
	public void setCameraLandscapeOrientation(boolean isCameraLandscapeOrientation) {
		this.isCameraLandscapeOrientation = isCameraLandscapeOrientation;
	}
	
	
	//#endregion Getter & Setter集

	//#region overwrite 方法
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		//保持界面常亮
		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		this.init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			this.mCamera.setPreviewDisplay(this.mSurfaceHolder);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_CAMERA_AUTO_FOCUS, 1000);
		this.startCameraResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.isLive = false;
		if(this.isPreviewing){
			this.stopPreview();
		}
//		this.closeCamera();
		this.mHandler.removeMessages(HANDLER_WHAT_CAMERA_AUTO_FOCUS);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.closeCamera();
	}
	//#endregion overwrite 方法

	//#region protected 方法
	@SuppressWarnings("deprecation")
	protected void init(){
		this.mSurfaceView = setSurfaceView();
		if(Helper.isNotNull(this.mSurfaceView)){
			this.mSurfaceHolder = this.mSurfaceView.getHolder();
			this.mSurfaceHolder.addCallback(this.mSurfaceCallback);
			this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}else{
			Log.e(TAG, "setSurfaceView() return null");
		}
		this.mCameraAutoFocusCallback = new CameraAutoFocusCallback();
		this.mCameraShutterCallback = new CameraShutterCallback();
		this.mCameraRawPictureCallback = new CameraRawPictureCallback();
		this.mCameraJpegPictureCallback = new CameraJpegPictureCallback();
	}
	/**
	 * 开始预览
	 */
	protected void startPreview(){
		if(!this.ensureCameraDevice()){
			return;
		}
		if(!this.isPreviewing){
			try {
				this.mCamera.startPreview();
				this.isPreviewing = true;
			} catch (Exception e) {
				Log.i(TAG, "startPreview failed");
			}
		}
	}
	/**
	 * 停止预览
	 */
	protected void stopPreview(){
		if(Helper.isNotNull(this.mCamera)){
			this.mCamera.stopPreview();
			this.isPreviewing = false;
		}
	}
	/**
	 * 关闭相机
	 */
	protected void closeCamera(){
		if(this.isPreviewing){
			this.stopPreview();
		}
		if(Helper.isNotNull(this.mCamera)){
			this.mCamera.release();
			this.isPreviewing = false;
			this.mCamera = null;
		}
	}
	/**
	 * 设置相机预览大小 </br>
	 * (为重写而存在)
	 * @return
	 */
	protected Point setCameraPreviewSize(){
		return null;
	}
	/**
	 * 开始拍照
	 */
	protected void takePhoto(){
		if(Helper.isNotNull(this.mCamera) && !this.isCameraError){
			this.isCapturePic = true;
			this.isPreviewing = false;
			//停止预览
//			this.stopPreview();
			//聚焦拍摄
//			this.mCamera.autoFocus(this.mCameraAutoFocusCallback);
			//拍照
			try {
				this.mCamera.takePicture(this.mCameraShutterCallback, this.mCameraRawPictureCallback, this.mCameraJpegPictureCallback);
			} catch (Exception e) {
			}
			this.isCapturePic = false;
		}
	}
	//#endregion protected 方法

	//#region private 方法
	private boolean ensureCameraDevice() {
		if (Helper.isNull(this.mCamera)) {
			this.mCamera = Camera.open();
//			CameraConfigManager.getInstance(this).initFromCameraParameters(this.mCamera, setCameraPreviewSize());
			CameraConfigManager.getInstance(this).initFromCameraParameters(this.mCamera, new Point(POINT_SIZE_WIDTH, POINT_SIZE_HEIGHT));
			CameraConfigManager.getInstance(this).setDesiredCameraParameters(this.mCamera);
		}
		return Helper.isNotNull(this.mCamera);
	}
	/**
	 * 开始Resume
	 */
	private void startCameraResume(){
		this.isLive = true;
		if(!this.isStoppingRec && !this.isRec){
			this.isCapturePic = false;
			try{
				this.startPreview();
			}catch(Exception e){
				e.printStackTrace();
				this.isCameraError = true;
				Log.e(TAG, TAG + "onResume():启动相机预览出错！");
				ActivityHelper.showToast(R.string.hint_abs_vcard_scan_system_camera_error);
			}
		}
	}
	/**
	 * 设置预览宽高比
	 * @param sizes
	 * @param w
	 * @param h
	 * @return
	 */
	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		  final double ASPECT_TOLERANCE = 0.1;
		  double targetRatio = (double) w / h;
		  if (sizes == null)
		   return null;

		  Size optimalSize = null;
		  double minDiff = Double.MAX_VALUE;

		  int targetHeight = h;

		  // Try to find an size match aspect ratio and size
		  for (Size size : sizes) {
		   double ratio = (double) size.width / size.height;
		   if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
		    continue;
		   if (Math.abs(size.height - targetHeight) < minDiff) {
		    optimalSize = size;
		    minDiff = Math.abs(size.height - targetHeight);
		   }
		  }

		  // Cannot find the one match the aspect ratio, ignore the requirement
		  if (optimalSize == null) {
		   minDiff = Double.MAX_VALUE;
		   for (Size size : sizes) {
		    if (Math.abs(size.height - targetHeight) < minDiff) {
		     optimalSize = size;
		     minDiff = Math.abs(size.height - targetHeight);
		    }
		   }
		  }
		  return optimalSize;
		 }

	/**
	 * 获取取景框内的图片
	 * @param data
	 */
	private byte[] getFrameImgData(byte[] data){
		
		if(null==data){
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 设置为true 则图片不读入内存，及bmp 为空
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length,options);
		// 图片的原始大小
		int bmpW = options.outWidth;
		int bmpH = options.outHeight;
		// 缩放比例
		int scale = 0 ;
		// 如果图片宽度大于高度，则按宽度比例缩放，否则按高度等比例缩放
		Log.d("屏幕 宽高", bmpW + "/" + mImgScaleWidth +":"+ bmpH + "/" + mImgScaleHeight);
		if(bmpW > bmpH){
			scale = bmpW / mImgScaleWidth;
		}else{
			scale = bmpH / mImgScaleHeight;
		}
		if (scale <= 0){
			scale = 1;
		}	
		Log.d("缩放比例", scale +"");
		options.inPreferredConfig = Config.RGB_565;
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		// 按缩放后的比例读入内存
		bmp = BitmapFactory.decodeByteArray(data, 0, data.length,options);
		Log.d("缩小后的图片宽高", bmp.getWidth() +":"+ bmp.getHeight());
		Bitmap bmpScale = Bitmap.createScaledBitmap(bmp, mImgScaleWidth, mImgScaleHeight, true);

		if(null != bmp){
			bmp.recycle();
			bmp = null;
		}
		Bitmap rectBitmap = Bitmap.createBitmap(bmpScale, mStartX, mStartY, mImgWidth, mImgHeight);//截取
		if(null != bmpScale){
			bmpScale.recycle();
			bmpScale = null;
		}
		Log.d("截取图片", "startX:startY" + mStartX + ":" + mStartY + "\n 宽 :高" +  mImgWidth + ":" + mImgHeight);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		rectBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);   
		byte[] newData = baos.toByteArray();
		return newData;
	}
	//#endregion private 方法

	//#region abstract
	@SuppressWarnings("unused")
	private String mVerification = "135270700791922253";
	/**
	 * 开始拍摄第二张
	 */
	protected abstract void startScanBack();
	/**
	 * 设置 SurfaceView
	 * @return
	 */
	protected abstract SurfaceView setSurfaceView();
	/**
	 * 可获得正面照片
	 * @param photoData 原始图片
	 * @param photoPath 处理后的图片路径
	 */
	protected abstract void getPhotoData(byte[] photoData, String photoPath);
	/**
	 * 可获得背面照片
	 * @param photoBackData 原始图片
	 * @param photoPath 处理后的图片路径
	 */
	protected abstract void getPhotoBackData(byte[] photoBackData, String photoPath);
	/**
	 * 拍摄完将要跳转到的页面</br>
	 * 在跳转前要做好getPhotoData()工作</br>
	 */
	protected abstract void switchToClass();
	//#endregion abstract
	
	//#region 子类 
	private class CameraAutoFocusCallback implements Camera.AutoFocusCallback{
		
		private AbstractVcardScanActivity mActivity = AbstractVcardScanActivity.this;
		
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if(this.mActivity.isCapturePic && this.mActivity.isLive){
				if(success){
					this.mActivity.mCamera.takePicture(this.mActivity.mCameraShutterCallback
							, this.mActivity.mCameraRawPictureCallback, this.mActivity.mCameraJpegPictureCallback);
				}else{
					ActivityHelper.showToast(R.string.hint_abs_vcard_scan_take_photo_faile);
					this.mActivity.isCapturePic = false;
//					this.mActivity.mCamera.autoFocus(this.mActivity.mCameraAutoFocusCallback);
				}
			}
		}
		
	}
	
	private class CameraShutterCallback implements Camera.ShutterCallback{

		@Override
		public void onShutter() {
			//do nothing now
		}
		
	}
	
	private class CameraRawPictureCallback implements Camera.PictureCallback{

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//内存图片
			//do nothing now
		}
		
	}
	
	private class CameraJpegPictureCallback implements Camera.PictureCallback{
		private AbstractVcardScanActivity mActivity = AbstractVcardScanActivity.this;
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if(this.mActivity.isLive){
				this.mActivity.isCapturePic = false;
				
				if(Helper.isNotNull(data)){					
					 // 获取截取的图片
//					byte[] newData = getFrameImgData(data);
					
					if(Helper.isNotNull(this.mActivity.mPictureData) && this.mActivity.isTwoSideMode){
						this.mActivity.mPictureBackData = data;
						this.mActivity.mHandler.sendEmptyMessage(HANDLER_WHAT_GET_PICTURE_BACK);
					}else{
						this.mActivity.mPictureData = data;
						this.mActivity.mHandler.sendEmptyMessage(HANDLER_WHAT_GET_PICTURE);
					}
				}else{
					Log.e(TAG, "所获取到的Jpeg data = null");
				}
			}
		}
		
	}
	//#endregion 子类
}
