package com.maya.android.jsonwork;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
/**
 * 数据异步请求命令基类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-5
 *
 * @param <T>
 */
public class AbstractDataCommand<T> extends AsyncTask<Void, Void, T> {
	@SuppressWarnings("unused")
	private int mVerificationTwo = 800253060;
	//#region 变量
	/**
	 * 命令标识,用于唯一标识命令
	 */
	private int mTag;
	/**
	 * 请求url地址
	 */
	private String mUrl;
	/**
	 * 参数列表
	 */
	private List<NameValuePair> mParamsList = null;
	/**
	 * 命令返回调用
	 */
	private CommandCallback<T> mCallback;
	/**
	 * 头部参数 Termainal-Agent
	 */
	private String mTerminalAgent = "Terminal-Agent";
	/**
	 * 附加数据
	 */
	private Object[] mExtraObjects;
	//#endregion变量
	
	//#region Getter&Setter集
	public int getTag() {
		return mTag;
	}

	public void setTag(int tag) {
		this.mTag = tag;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public List<NameValuePair> getParamsList() {
		return mParamsList;
	}

	public void setParamsList(List<NameValuePair> paramsList) {
		this.mParamsList = paramsList;
	}

	public CommandCallback<T> getCallback() {
		return mCallback;
	}

	public void setCallback(CommandCallback<T> callback) {
		this.mCallback = callback;
	}

	public String getTerminalAgent() {
		return mTerminalAgent;
	}

	public void setTerminalAgent(String mTerminalAgent) {
		this.mTerminalAgent = mTerminalAgent;
	}

	public Object[] getExtraObjects() {
		return mExtraObjects;
	}

	public void setExtraObjects(Object[] extraObjects) {
		this.mExtraObjects = extraObjects;
	}
	//#endregion Getter&Setter集
	
	//#region 构造方法
	public AbstractDataCommand(){
		
	}
	/**
	 * @param tag 命令标示
	 * @param url 请求地址
	 * @param paramsList 参数list
	 * @param callback 命令回调
	 * @param objects 附加Object
	 */
	public AbstractDataCommand(int tag, String url, String terminalAgent, List<NameValuePair> paramsList, CommandCallback<T> callback, Object... objects){
		this.mTag = tag;
		this.mUrl = url;
		this.mTerminalAgent = terminalAgent;
		this.mParamsList = paramsList;
		this.mCallback = callback;
		this.mExtraObjects = objects;
	}
	//#endregion 构造方法
	
	//#region Override方法
	@Override
	protected T doInBackground(Void... params) {
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(T result) {
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCancelled(T result) {
		super.onCancelled(result);
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
	//#endregion Override方法

	//#region interface
	/**
	 * 命令返回调用
	 * @author ZuoZiJi-Y.J
	 * @version v1.0
	 * @since 2013-7-5
	 *
	 * @param <T> 返回数据类型
	 */
	public interface CommandCallback<T>{
		/**
		 * 命令返回时调用
		 * @param tag 命令标识
		 * @param commandResult 返回结果
		 * @param objects 返回的其它数据
		 * @return 是否已被处理(已处理则返回true,之后的处理都不会被执行)
		 */
		boolean onCommandCallback(int tag, T commandResult, Object... objects);
	}
	//#endregion interface

}
