package com.maya.android.vcard.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;

import java.io.IOException;

/**
 * 录音/播放  Helper类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-2-10
 *
 */
public class AudioRecordHelper {
	//#region 常量
	private static final String TAG = AudioRecordHelper.class.getSimpleName();
	//#endregion 常量

	//#region 静态成员变量
	private static AudioRecordHelper sInstance;
	// 录制时间限制
	private static final int mMaxDurationInMs = 20000;
	// 文件大小限制
	private static final long mMaxFileSize = 500000;
	// 帧频率
	private static final int mRate = 20;
	//#endregion 静态成员变量

	//#region 成员变量
	private Context mContext;
	private MediaPlayer mPlayer;
	private int mResourceId = 0;
	/** 录音录像 **/
	private MediaRecorder mRecorder;
	private MediaPlayListener mMediaPlayListener;
	
	//#endregion 成员变量

	//#region Getter & Setter集
	public Context getContext() {
		return mContext;
	}
	public void setContext(Context context) {
		this.mContext = context;
	}
	//#endregion Getter & Setter集

	//#region 构造方法
	private AudioRecordHelper(){
		this.mContext = ActivityHelper.getGlobalApplicationContext();
	}
	
	private static AudioRecordHelper getInstance(){
		if(Helper.isNull(sInstance)){
			sInstance = new AudioRecordHelper();
		}
		return sInstance;
	}
	//#endregion 构造方法

	//#region public 方法
	/**
	 * 设置资源
	 * @param resId
	 */
	public static void setRescource2MediaPlay(int resId){
		getInstance().setRescource2MediaPlay2Instance(resId);
	}
	/**
	 * 播放声音</br>
	 * (在此之前必须要先设置要播放的声音资源：详见：{@link #setRescource2MediaPlay(int)})
	 */
	public static void play(){
		getInstance().play2Instance();
	}
	/**
	 * 播放声音
	 * @param uri
	 */
	public static void startMediaPlay(Uri uri){
	    getInstance().startMediaPlay2Instance(uri);
	}
	/**
	 * 播放声音
	 * @param filePath
	 */
	public static void startMediaPlay(String filePath){
		getInstance().startMediaPlay2Instance(filePath);
	}
	/**
	 * 暂停播放音乐
	 */
	public static void stopMediaPlay(){
	    getInstance().stopMediaPlay2Instance();
	}
	/**
	 * 释放 meida 资源
	 * @param isSetNull 是否把对象置空
	 */
	public static void releaseMediaPlay(boolean isSetNull){
	    getInstance().releaseMediaPlay2Instance(isSetNull);
	}
	/**
	 * 设置播放监听
	 * @param playListener
	 */
	public static void setMediaPlayListener(MediaPlayListener playListener){
		getInstance().setMediaPlayListener2Instance(playListener);
	}
	/**
	 * 停止录制
	 */
	public static void stopMediaRecord(){
		getInstance().stopMediaRecord2Instance();
	}
	/**
	 * 释放录制设备
	 */
	public static void releaseMediaRecorder(){
		getInstance().releaseMediaRecord2Instance();
	}
	/**
	 * 录音
	 * @param saveFilePath
	 */
	public static void startMediaRecordAudio(String saveFilePath){
		getInstance().startMediaRecordAudio2Instance(saveFilePath);
	}
	/**
	 * 录像
	 * @param mCamera
	 * @param mCameraFilePath
	 * @param mSfvWidth
	 * @param mSfvHeight
	 */
	public static void startMediaRecordVideo(Camera mCamera, String mCameraFilePath, int mSfvWidth, int mSfvHeight){
		getInstance().startMediaRecordVideo2Instance(mCamera, mCameraFilePath, mSfvWidth, mSfvHeight);
	}
	//#endregion public 方法

	//#region protected 方法

	//#endregion protected 方法

	//#region private 方法
	private void initMedia(){
		this.releaseMediaPlay2Instance(true);
//		if(Helper.isNull(this.mPlayer)){
			this.mPlayer = new MediaPlayer();
			this.mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					if(Helper.isNotNull(mMediaPlayListener)){
						mMediaPlayListener.onCompletion();
					}	
				}
			});
//
//		}else{
//			if(this.mPlayer.isPlaying()){
//				this.mPlayer.stop();
//			}
//			//将播放位置置为开始位置，不然不能播放
//			this.mPlayer.seekTo(0);
//		}
		
	}
	/**
	 * 设置播放结束监听
	 * @param playListener
	 */
	private void setMediaPlayListener2Instance(MediaPlayListener playListener){
		this.mMediaPlayListener = playListener;
	}
	
	@SuppressLint("NewApi")
	private void setRescource2MediaPlay2Instance(int resId){
		if(this.mResourceId != resId){
			this.mResourceId = resId;
			this.initMedia();
			try {
				AssetFileDescriptor afd = this.mContext.getResources().openRawResourceFd(resId);
				this.mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				this.mPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void play2Instance(){
		if(Helper.isNotNull(mPlayer)){
			this.mPlayer.start();
		}
	}
	
	/**
	 * 播放系统音效
	 * @param uri
	 * @author zxf 2014.01.02
	 */
	private void startMediaPlay2Instance(Uri uri){			
		
		try {	
			this.initMedia();
			mPlayer.reset();          
			mPlayer.setDataSource(mContext,uri);			
			mPlayer.prepare();
			mPlayer.start();
			
		} catch(IllegalStateException ex){
			ex.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 播放语音文件
	 * @param filePath
	 */
	private void startMediaPlay2Instance(String filePath){			
		
		try {
			this.initMedia();
			mPlayer.reset();
			mPlayer.setDataSource(filePath);			
			mPlayer.prepare();
			mPlayer.start();
			
		} catch(IllegalStateException ex){
			ex.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 停止播放 
	 */
	private void stopMediaPlay2Instance(){	
		try {
			if(Helper.isNotNull(mPlayer) && mPlayer.isPlaying()){
				mPlayer.stop();
			}
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 释放 资源
	 * @param isSetNull 是否把media对象置空
	 */
	private void releaseMediaPlay2Instance(boolean isSetNull){
		if(Helper.isNotNull(mPlayer)){
			mPlayer.release();
			if(isSetNull){
				mPlayer = null;
			}
		}
	}
	/**
	 * 开始录音
	 * @param filePath
	 */
	@SuppressLint("InlinedApi")
	private void startMediaRecordAudio2Instance(String filePath){
		if(Helper.isEmpty(filePath)){
			return;
		}

		try {
//			stopMediaRecord2Instance();
//			if(Helper.isNull(mRecorder)){
			releaseMediaRecord2Instance();
				mRecorder = new MediaRecorder();
//			}
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置输出格式
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			// 设置语音编码格式
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			mRecorder.setOutputFile(filePath);
		
			mRecorder.prepare();
			mRecorder.start();
		} catch (IllegalStateException e) {
			Log.e(TAG, "录音异常");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, "录音异常");
			e.printStackTrace();
		}catch (Exception e) {
			Log.e(TAG, "录音异常");
			e.printStackTrace();
		}
	
	}
	/**
	 * 开始摄像
	 * @param mCamera
	 * @param mCameraFilePath 文件存储路径
	 * @param mSfvWidth 摄像区域宽
	 * @param mSfvHeight 摄像区域高
	 */
	public void startMediaRecordVideo2Instance(Camera mCamera, String mCameraFilePath, int mSfvWidth, int mSfvHeight){
		try {
			stopMediaRecord2Instance();
			mCamera.unlock();
			if(Helper.isNull(mRecorder)){
				mRecorder = new MediaRecorder();
			}
			mRecorder.setCamera(mCamera);
			// 视频源
			mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
			// 录音源为麦克风
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); 
			// 输出格式为3gp
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			// 最长时间
			mRecorder.setMaxDuration(mMaxDurationInMs);
			mRecorder.setOutputFile(mCameraFilePath);// 保存路径
			// 视频尺寸
			mRecorder.setVideoSize(mSfvWidth, mSfvHeight);
			// 视频帧频率
			mRecorder.setVideoFrameRate(mRate);
			// 视频编码
			mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
			// 音频编码
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 文件最大值
			mRecorder.setMaxFileSize(mMaxFileSize);
			mRecorder.prepare();
			mRecorder.start();

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 停止录音/摄像
	 */
	private void stopMediaRecord2Instance(){
	
		if (Helper.isNotNull(mRecorder)) {
			mRecorder.stop();
			mRecorder.reset();
			mRecorder.release();
		}
		
	}
	/**
	 * 释放录音摄像硬件设备
	 */
	private void releaseMediaRecord2Instance(){
		stopMediaRecord2Instance();
		mRecorder = null;
	}
	//#endregion private 方法

	//#region interface
	/**
	 * 播放器  事件监听
	 * @author zheng_cz
	 * @since 2013-10-11 下午5:17:52
	 */
	public interface MediaPlayListener{
		/**
		 * 播放结束
		 */
		void onCompletion();
	}
	//#endregion interface
	
}
