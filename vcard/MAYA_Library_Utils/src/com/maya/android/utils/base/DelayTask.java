package com.maya.android.utils.base;

import com.maya.android.utils.Helper;

import android.os.AsyncTask;
import android.util.Log;

/**
 * 延迟执行任务
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-2
 *
 */
public class DelayTask {
//	private static final String TAG = DelayTask.class.getSimpleName();
	/**
	 * 延时时间
	 */
	private int mDelayMs;
	/**
	 * 延时时间
	 */
	private int mRepeatCount = Integer.MAX_VALUE;
	/**
	 * 延时后执行监听器
	 */
	private OnDelayExecuteListener mListener; 
	private InnerDelayTask mInnerDelayTask;
	/**
	 * 构造方法(无限循环)
	 * @param delayMs 延时时间(毫秒)
	 * @param listener 延时后执行监听
	 */
	public DelayTask(int delayMs, OnDelayExecuteListener listener){
		this.mDelayMs = delayMs;
		this.mListener = listener;
	}
	/**
	 * 构造方法
	 * @param delayMs mListener
	 * @param repeatCount 重复次数
	 * @param listener 延时后执行监听
	 */
	public DelayTask(int delayMs, int repeatCount, OnDelayExecuteListener listener){
		this.mDelayMs = delayMs;
		this.mRepeatCount = repeatCount;
		this.mListener = listener;
	}
	/**
	 * 开始
	 */
	public void start(){
		this.mInnerDelayTask = new InnerDelayTask();
		this.mInnerDelayTask.execute();
	}
	/**
	 * 取消
	 */
	public void cancel(){
		if(Helper.isNotNull(this.mInnerDelayTask)){
			this.mInnerDelayTask.cancel(true);
		}
	}
	@SuppressWarnings("unused")
	private String mVerification = "135270700791922253";
	/**
	 * 内部延迟任务
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2013-7-2
	 *
	 */
	private class InnerDelayTask extends AsyncTask<Void, Void, Void>{
		private static final String INNER_TAG = "InnerDelayTask";
		@Override
		protected Void doInBackground(Void... params) {
			try{
				do{
					Thread.sleep(mDelayMs);
					this.publishProgress(params);
					if(mRepeatCount > 0 && mRepeatCount != Integer.MAX_VALUE){
						mRepeatCount--;
					}
				}while(mRepeatCount > 0 || mRepeatCount == Integer.MAX_VALUE);
			}catch(InterruptedException e){
				Log.d(INNER_TAG, e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mListener.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mListener.onPostExecute();
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			mListener.onProgressUpdate();
		}
		
	}
	/**
	 * 延时后执行监听器接口
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2013-7-3
	 *
	 */
	public interface OnDelayExecuteListener{
		/**
	     * Runs on the UI thread before {@link #doInBackground}.
	     *
	     * @see #onPostExecute
	     * @see #doInBackground
	     */
		public void onPreExecute();
		/**
	     * <p>Runs on the UI thread after {@link #doInBackground}. The
	     * specified result is the value returned by {@link #doInBackground}.</p>
	     * 
	     * <p>This method won't be invoked if the task was cancelled.</p>
	     *
	     * @param result The result of the operation computed by {@link #doInBackground}.
	     *
	     * @see #onPreExecute
	     * @see #doInBackground
	     * @see #onCancelled(Object) 
	     */
		public void onPostExecute();
		/**
	     * Runs on the UI thread after {@link #publishProgress} is invoked.
	     * The specified values are the values passed to {@link #publishProgress}.
	     *
	     * @param values The values indicating progress.
	     *
	     * @see #publishProgress
	     * @see #doInBackground
	     */
		public void onProgressUpdate();
	}
}
