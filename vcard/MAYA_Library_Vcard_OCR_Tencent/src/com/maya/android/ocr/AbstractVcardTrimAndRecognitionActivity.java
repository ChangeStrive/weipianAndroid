package com.maya.android.ocr;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.maya.android.cropimage.activity.AbstractCropImageActivity;
import com.maya.android.ocr.entity.OcrRecogResultEntitiy;
import com.maya.android.utils.Helper;
import com.tencent.qqvision.util.CardResChar;
import com.tencent.qqvision.util.NativeLib;
/**
 * 名片识别及切边
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-8-8
 *
 */
public abstract class AbstractVcardTrimAndRecognitionActivity extends AbstractCropImageActivity {
	//#region 常量
	private static final String TAG = AbstractVcardTrimAndRecognitionActivity.class.getSimpleName();
	private static final int WHAT_IMAGE_OCR_SUCCESS = 2001;
	private static final int WHAT_IMAGE_OCR_FAIL = 2002;
	private static final int WHAT_START_OCR = 3000;
	private static final String STRING_CONNECT_CHAR = "#";
	//#endregion 常量

	//#region 静态成员变量
	private boolean isTwoSideMode = false;
	private String mPhotoPath;
	private String mPhotoBackPath;
	//#endregion 静态成员变量

	//#region 成员变量
	private boolean isInitNativeLib = false;
	private CardResChar[] mRecogResult;
	@SuppressLint("HandlerLeak")
	private Handler mOcrHandler = new Handler(Looper.getMainLooper()){
		private int mOcrCount = 0;
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mOcrCount++;
			boolean isVcardRecognition = false;
			if(mOcrCount == 1){
				isVcardRecognition = true;
			}
			switch(msg.what){
			case WHAT_IMAGE_OCR_SUCCESS:
				OcrRecogResultEntitiy entity = (OcrRecogResultEntitiy)msg.obj;
				vcardRecognitionSuccess(entity);
				break;
			case WHAT_IMAGE_OCR_FAIL:
				vcardRecognitionFail();
				break;
			}
			//always one done 
			if(isTwoSideMode&& isVcardRecognition){
				//扫描开始扫描背面
				startRecognitionBack();
			}
		}
		
	};
	private Handler mOcrBitmapHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case WHAT_START_OCR:
				setOcrImage((Bitmap)msg.obj);
				break;
			}
			
		}
		
	};
	//#endregion 成员变量

	//#region Getter & Setter集
	/**
	 * 设置正面图片路径
	 * @param photoPath
	 */
	protected void setPhotoPath(String photoPath){
		mPhotoPath = photoPath;
	}
	/**
	 * 取得正面图片路径
	 * @return
	 */
	protected String getPhotoPath(){
		return mPhotoPath;
	}
	/**
	 * 设置背面图片路径
	 * @param photoPath
	 */
	protected void setPhotoBackPath(String photoPath){
		this.mPhotoBackPath = photoPath;
	}
	/**
	 * 取得背面图片路径
	 * @return
	 */
	protected String getPhotoBackPath(){
		return mPhotoBackPath;
	}
	/**
	 * 设置是否是双面模式
	 * @param isTwoSideMode
	 */
	protected void setTwoSideMode(boolean isTwoSideMode){
		this.isTwoSideMode = isTwoSideMode;
	}
	/**
	 * 获取是否为双面模式
	 * @return
	 */
	protected boolean getTwoSideMode(){
		return isTwoSideMode;
	}
	//#endregion Getter & Setter集

	//#region overwrite 方法
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.initNativeLib();
	}
	//#endregion overwrite 方法

	//#region protected 方法
	protected void init(String photoPath){
		this.initNativeLib();
		if(Helper.isNotEmpty(photoPath)){
			super.init(photoPath);
		}
	}
	/**
	 * 开始识别
	 * @param bitmap
	 */
	protected void startRecognition(Bitmap bitmap){
		Message msg = new Message();
		msg.what = WHAT_START_OCR;
		msg.obj = bitmap;
		this.mOcrBitmapHandler.sendMessage(msg);
	}
	//#endregion protected 方法

	//#region private 方法
	private void initNativeLib(){
		if(!this.isInitNativeLib){
			if(NativeLib.OcrInit() != 0){
//				ActivityHelper.showToast("OCRLib init error!");
				this.isInitNativeLib = false;
			}else{
				this.isInitNativeLib = true;
			}
			this.mRecogResult = new CardResChar[1000];
			for(int i = 0; i < 1000; i++){
				this.mRecogResult[i] = new CardResChar();
			}
		}
	}
	
	private void setOcrImage(Bitmap bitmap){
		this.initNativeLib();
	    if(Helper.isNotNull(bitmap)){
	    	int bmpWidth = bitmap.getWidth();
	    	int bmpHeight = bitmap.getHeight();
	    	byte[] imageByte = new byte[bmpWidth * bmpHeight];
	    	int[] imageWidth = new int[bmpWidth];
	    	for(int i = 0, height = bmpHeight; i < height; i ++){
	    		bitmap.getPixels(imageWidth, 0, bmpWidth, 0, i, bmpWidth, 1);
				for(int j = 0, width = bmpWidth; j < width; j++){
					int z = i * width + j;
					int red = Color.red(imageWidth[j]) * 38;
					int green = Color.green(imageWidth[j]) * 75;
					int redAndGreen = red + green;
					int blue = Color.blue(imageWidth[j]) * 15;
					byte pixel = (byte)(redAndGreen + blue >> 7);
					imageByte[z] = pixel;
				}
			}
	    	NativeLib.OcrSetImage(imageByte, bmpWidth, bmpHeight);
	    		new Thread(new RecognizeRunnable()).start();
	    	Log.d(TAG, "OcrImage width:" + bmpWidth + ", height:" + bmpHeight);
	    }
	}
	private void recognize(){
		boolean isRecognizeSuccess = false;
		if(NativeLib.CardRecogRegion(NativeLib.TH_LANGUAGE_SCHINESE, this.mRecogResult) != 0){
			this.mOcrHandler.sendEmptyMessage(WHAT_IMAGE_OCR_FAIL);
			Log.d(TAG, "fail!");
			return;
		}
		Log.d(TAG, "ocrImage recognize finish!");
		int resSize = NativeLib.resSize;
		OcrRecogResultEntitiy entity = new OcrRecogResultEntitiy();
		for(int i = 0, len = resSize; i < len; i ++){
//			char bottom = this.mRecogResult[0].bottom;
//			char top = this.mRecogResult[0].top;
//			char left = this.mRecogResult[0].left;
//			char right = this.mRecogResult[0].right;
			CardResChar cardResChar = this.mRecogResult[i];
			char[] string = cardResChar.String;
			char wClass = cardResChar.wClass;
			int w = findWClass(wClass);
			if(!isRecognizeSuccess && -1 != w){
				isRecognizeSuccess = true;
			}
			getRecogResultEntity(entity, w, String.valueOf(string));
		}
		if(isRecognizeSuccess){
			Log.d(TAG, "ocrImage recognize success!");
		}else{
			Log.d(TAG, "ocrImage recognize fail");
		}
		Message msg = new Message();
		if(isRecognizeSuccess){
			msg.what = WHAT_IMAGE_OCR_SUCCESS;
			msg.obj = entity;
		}else{
			msg.what = WHAT_IMAGE_OCR_FAIL;
		}
		this.mOcrHandler.sendMessage(msg);
	}
	private int findWClass(int w){
		int result = -1;
		switch(w){
		case 1:
			result = 0;
			break;
		case 2:
			result = 1;
			break;
		case 4:
			result = 2;
			break;
		case 8:
			result = 3;
			break;
		case 16:
			result = 4;
			break;
		case 32:
			result = 5;
			break;
		case 64:
			result = 6;
			break;
		case 128:
			result = 7;
			break;
		case 256:
			result = 8;
			break;
		case 512:
			result = 9;
			break;
		case 1024:
			result = 10;
			break;
		case 2048:
			result = 11;
			break;
		case 4096:
			result = 12;
			break;
		case 8192:
			result = 13;
			break;
		case 16384:
			result = 14;
			break;
		}
		return result;
	}
	private OcrRecogResultEntitiy getRecogResultEntity(OcrRecogResultEntitiy entity, int wClass, String content){
		if(Helper.isNotNull(entity)){
			if(Helper.isNull(content)){
				content = "";
			}
			content = content.trim();
			Log.d(TAG, "Recog result content : " + content);
			switch(wClass){
			case 0:
				if(Helper.isNull(entity.getUrl())){
					entity.setUrl(content);
				}else{
					entity.setUrl(entity.getUrl() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 1:
				if(Helper.isNull(entity.getMail())){
					entity.setMail(content);
				}else{
					entity.setMail(entity.getMail() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 2:
				if(Helper.isNull(entity.getPhone())){
					entity.setPhone(content);
				}else{
					entity.setPhone(entity.getPhone() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 3:
				if(Helper.isNull(entity.getTelephone())){
					entity.setTelephone(content);
				}else{
					entity.setTelephone(entity.getTelephone() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 4:
				if(Helper.isNull(entity.getFax())){
					entity.setFax(content);
				}else{
					entity.setFax(entity.getFax() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 5:
				if(Helper.isNull(entity.getCall())){
					entity.setCall(content);
				}else{
					entity.setCall(entity.getCall() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 6:
				if(Helper.isNull(entity.getPostcode())){
					entity.setPostcode(content);
				}else{
					entity.setPostcode(entity.getPostcode() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 7:
				if(Helper.isNull(entity.getAddress())){
					entity.setAddress(content);
				}else{
					entity.setAddress(entity.getAddress() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 8:
//				entity.setAddress(content);
				if(Helper.isNull(entity.getCompany())){
					entity.setCompany(content);
				}else{
					entity.setCompany(entity.getCompany() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 9:
				if(Helper.isNull(entity.getDepartment())){
					entity.setDepartment(content);
				}else{
					entity.setDepartment(entity.getDepartment() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 10:
				if(Helper.isNull(entity.getPosition())){
					entity.setPosition(content);
				}else{
					entity.setPosition(entity.getPosition() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 11:
				if(Helper.isNull(entity.getEnglishName())){
				entity.setEnglishName(content);
				}else{
					entity.setEnglishName(entity.getEnglishName() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 12:
				if(Helper.isNull(entity.getChineseName())){
					entity.setChineseName(content);
				}else{
					entity.setChineseName(entity.getChineseName() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 13:
				if(Helper.isNull(entity.getQQ())){
					entity.setQQ(content);
				}else{
					entity.setQQ(entity.getQQ() + STRING_CONNECT_CHAR + content);
				}
				break;
			case 14:
				if(Helper.isNull(entity.getMSN())){
					entity.setMSN(content);
				}else{
					entity.setMSN(entity.getMSN() + STRING_CONNECT_CHAR + content);
				}
				break;
				default:
					break;
			}
			return entity;
		}
		return null;
	}
	//#endregion private 方法
	
	//#region abstract 方法
//	/**
//	 * 展示处理流程信息
//	 * @param processInfo
//	 */
//	protected abstract void showImageProcessInfo(String processInfo);
//	/**
//	 * 展示处理bitmap
//	 * @param bitmap
//	 */
//	protected abstract void showImageTrimBitmap(Bitmap bitmap);
	/**
	 * 扫描名片识别成功</br>
	 * (要做好存储扫描结果)</br>
	 * @param ocrItems
	 */
	protected abstract void vcardRecognitionSuccess(OcrRecogResultEntitiy entity);
	/**
	 * 扫描名片识别失败
	 */
	protected abstract void vcardRecognitionFail();
	/**
	 * 开始扫描背面
	 */
	protected abstract void startRecognitionBack();
//	/**
//	 * 开始扫描动画
//	 */
//	protected abstract void showStartRecognitionAction();
//	/**
//	 * 扫描名片背面识别成功</br>
//	 * (要做好存储扫描结果)</br>
//	 * @param ocrItems
//	 */
//	protected abstract void vcardBackRecognitionSuccess(OcrRecogResultEntitiy entity);
//	/**
//	 * 扫描名片背面识别失败
//	 */
//	protected abstract void vcardBackRecognitionFail();
//	/**
//	 * 所有名片扫描识别完成</br>
//	 * (无论单张模式还是双面模式，都会调用)</br>
//	 */
//	protected abstract void allVcardRecognitionFinish();
//	/**
//	 * 拍摄完将要跳转到的页面</br>
//	 * 在跳转前要做好参数传递工作</br>
//	 */
//	protected abstract void switchToClass();
	//#endregion abstract 方法
	
	//#region 子类
	private class RecognizeRunnable implements Runnable{

		@Override
		public void run() {
			recognize();
		}
		
	}
	//#endregion 子类
}
