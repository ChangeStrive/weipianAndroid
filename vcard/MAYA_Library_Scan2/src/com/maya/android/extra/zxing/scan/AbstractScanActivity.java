package com.maya.android.extra.zxing.scan;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.maya.android.extra.zxing.CaptureActivityHandler;
import com.maya.android.extra.zxing.InactivityTimer;
import com.maya.android.extra.zxing.ViewfinderView;
import com.maya.android.extra.zxing.scan.camera.CameraManager;

import java.io.IOException;
import java.util.Vector;
/**
 * 
 * 扫描抽象类</br>
 * 使用扫描功能需要继续自本类</br>
 * 主要使用的方法在底部,具体如下:</br>
 * 1.init方法,若使用自带的布局,可不用重载本方法,并可实现doAddNewViews方法动态加入控件,</br>
 *   若要重新布局则重载本方法,必须设定viewfinderView和txtResult</br>
 * 2.实现doBeforeScan方法完成扫描时的界面设定,如显示或隐藏控件</br>
 * 3.实现doAfterScan方法完成对结果的处理</br>
 * 4.调用restartScan方法可以再次开始新的一轮的扫描</br>
 * 5.通过setAllowVibrate和setAllowPlayBeep可控制是否震动或播放音乐</br>
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-15
 *
 */
public abstract class AbstractScanActivity extends Activity implements Callback {

	private static final int WHAT_INIT_CAMERA = 10001;
	private static final int WHAT_INIT_SURFACEVIEW = 10002;
	private static final float BEEP_VOLUME = 0.10f;

	protected CaptureActivityHandler mHandler;
	protected ViewfinderView mViewfinderView;
	private boolean isSurface;
	protected Vector<BarcodeFormat> mDecodeFormats;
	protected String mCharacterSet;
	protected TextView mTxvResult;
	private InactivityTimer mInactivityTimer;
	private MediaPlayer mMediaPlayer;
	private boolean mPlayBeep;
	private boolean mVibrate;
	
	private boolean mAllowPlayBeep = true;
	private boolean mAllowVibrate = true;
	private boolean mShowScanImage = true;
	private boolean mShowScanTextResult = true;
	
	private Handler mInitHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
				case WHAT_INIT_CAMERA:
					// CameraManager
					CameraManager.init(getApplication());
					break;
				case WHAT_INIT_SURFACEVIEW:
					SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
					SurfaceHolder surfaceHolder = surfaceView.getHolder();
					if (isSurface) {
						initCamera(surfaceHolder);
					} else {
						surfaceHolder.addCallback(AbstractScanActivity.this);
						surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
					}
					break;
			}
		}
	};
	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.init();
		// CameraManager
		CameraManager.init(getApplication());
//		mInitHandler.sendEmptyMessageDelayed(WHAT_INIT_CAMERA, 800);
		isSurface = false;
		mInactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (isSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
//		this.mInitHandler.sendEmptyMessageDelayed(WHAT_INIT_SURFACEVIEW, 1200);
		mDecodeFormats = null;
		mCharacterSet = null;

		mPlayBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			mPlayBeep = false;
		}
		initBeepSound();
		mVibrate = true;
		
		this.doBeforeScan();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mHandler != null) {
			mHandler.quitSynchronously();
			mHandler = null;
		}
		CameraManager.get().closeDriver();
	}
	
	@Override
	protected void onDestroy() {
		mInactivityTimer.shutdown();
		super.onDestroy();
	}
	@SuppressWarnings("unused")
	private String mVerification = "135270700791922253";
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (mHandler == null) {
			mHandler = new CaptureActivityHandler(this, mDecodeFormats,
					mCharacterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!isSurface) {
			isSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isSurface = false;

	}

	public void setViewfinderView(ViewfinderView viewfinderView) {
		this.mViewfinderView = viewfinderView;
	}

	public ViewfinderView getViewfinderView() {
		return mViewfinderView;
	}
	
	public void setTxtResult(TextView txtResult){
		this.mTxvResult = txtResult;
	}
	
	public TextView getTxtResult(){
		return this.mTxvResult;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public void drawViewfinder() {
		mViewfinderView.drawViewfinder();
	}

	/**
	 * 成功后处理
	 * 
	 * @param obj
	 * @param barcode
	 */
	public void handleDecode(Result obj, Bitmap barcode) {
		mInactivityTimer.onActivity();
//		mViewfinderView.drawResultBitmap(barcode);
		try{
			Matrix matrix = new Matrix();
			matrix.reset();
			matrix.setRotate(90);
			Bitmap bmp = Bitmap.createBitmap(barcode, 0, 0, barcode.getWidth(), barcode.getHeight(), matrix, false);
			if(barcode != bmp){
				barcode.recycle();
				barcode = bmp;
			}
		}catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		if(this.mShowScanImage){
			mViewfinderView.drawResultBitmap(barcode);
		}
		playBeepSoundAndVibrate();
		if(this.mShowScanTextResult){
			mTxvResult.setText(obj.getBarcodeFormat().toString() + ":"
					+ obj.getText());
		}
		this.doAfterScan(obj, barcode);
	}

	private void initBeepSound() {
		if (mPlayBeep && mMediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.fwex_beep);
			try {
				mMediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mMediaPlayer.prepare();
			} catch (IOException e) {
				mMediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (mAllowPlayBeep && mPlayBeep && mMediaPlayer != null) {
			mMediaPlayer.start();
		}
		if (mAllowVibrate && mVibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	//#region 使用时主要使用方法
	/**
	 * 初始化(重载用)
	 */
	protected void init() {
		setContentView(R.layout.fwex_scan_layout_scan);
		mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mTxvResult = (TextView) findViewById(R.id.txv_result);
		View addView = this.doAddNewViews();
		if(null != addView){
			FrameLayout frlScan = (FrameLayout) findViewById(R.id.frl_scan_main);
			frlScan.addView(addView);
//			new AsyncTask<Void, Void, Void>(){
//				protected Void doInBackground(Void... params) {
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					return null;
//				}
//
//				protected void onPostExecute(Void result) {
//					FrameLayout frlScan = (FrameLayout) findViewById(R.id.frl_scan_main);
//					frlScan.addView(doAddNewViews());
//				};
//
//			}.execute();
		}
	}
	/**
	 * 重新开始扫描
	 */
	protected void restartScan() {
		isSurface = false;
		drawViewfinder();
		mInactivityTimer = new InactivityTimer(this);
		mTxvResult.setText("");
		this.doBeforeScan();
		mHandler.sendEmptyMessage(R.id.fwex_scan_restart_preview);
	}
	
	/**
	 * @return the allowPlayBeep
	 */
	public boolean isAllowPlayBeep() {
		return mAllowPlayBeep;
	}

	/**
	 * @param allowPlayBeep the allowPlayBeep to set
	 */
	public void setAllowPlayBeep(boolean allowPlayBeep) {
		this.mAllowPlayBeep = allowPlayBeep;
	}

	/**
	 * @return the allowVibrate
	 */
	public boolean isAllowVibrate() {
		return mAllowVibrate;
	}

	/**
	 * @param allowVibrate the allowVibrate to set
	 */
	public void setAllowVibrate(boolean allowVibrate) {
		this.mAllowVibrate = allowVibrate;
	}
	/**
	 * 设置二维码展示区域</br>
	 * 一定要在super.onCreate之前调用，否则没有效果</br>
	 * @param top 距顶部位置的距离
	 * @param width 展示框的宽度
	 * @param height 展示框的高度
	 */
	protected void setDisplayFrame(int top, int width, int height){
		CameraManager.setDisplayFrame(top, width, height);
	}
	/**
	 * 设置二维码展示区域是否有红线（默认为有）</br>
	 * 一定要在super.onCreate之前调用，否则没有效果</br>
	 * @param showRedLineInMiddle
	 */
	protected void setShowRedLineInMiddle(boolean showRedLineInMiddle){
		ViewfinderView.setShowRedLineInMiddle(showRedLineInMiddle);
	}

	/**
	 * 设置闪光灯
	 * @param cameraFlashMode {Camera.Parameters.FLASH_MODE_AUTO, Camera.Parameters.FLASH_MODE_OFF, Camera.Parameters.FLASH_MODE_TORCH}
	 */
	public void setCameraFlashMode(String cameraFlashMode){
		 CameraManager.get().setCameraFlashMode(cameraFlashMode);
	}
	/**
	 * 是否支持闪光灯
	 * @return boolean
	 * @author zheng_cz 2013-08-15
	 */
	public boolean isSupportCameraFlash(){
		return CameraManager.get().isSupportCameraFlash();
	}
//	/**
//	 * 设置相机是否为横屏（默认为横屏）
//	 * @return
//	 */
//	public void setCameraOrientation(boolean isLandscape){
//		CameraManager.setCameraOrientation(isLandscape);
//	}
	/**
	 * 设置是否展示扫描结果图片到的二维码取景框（默认为展示）
	 * @param mShowScanImage
	 */
	protected void setShowScanImage(boolean mShowScanImage) {
		this.mShowScanImage = mShowScanImage;
	}
	/**
	 * 设置是否展示扫描结果到界面（默认为展示）
	 * @param mShowScanTextResult
	 */
	protected void setShowScanTextResult(boolean mShowScanTextResult) {
		this.mShowScanTextResult = mShowScanTextResult;
	}

	/**
	 * 进行扫描完后的处理
	 * @param obj
	 * @param barcode
	 */
	abstract protected void doAfterScan(Result obj, Bitmap barcode);
	/**
	 * 进行扫描前处理
	 */
	abstract protected void doBeforeScan();
	/**
	 * 添加新的View(使用纯代码进行)
	 */
	abstract protected View doAddNewViews();
	//#endregion 使用时主要使用方法
}