package com.maya.android.utils;

import com.maya.android.utils.app.MaYaApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
/**
 * SharedPreferences辅助类</br>
 * 使用前必须保证MaYaApplication类有正确的初始化</br>
 * 可操作多个SharedPreferences,取得时通过getInstance方法进行选择</br>
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-10
 *
 */
public class PreferencesHelper {
	
	//#region 变量
	private static PreferencesHelper sInstance = new PreferencesHelper();
	private static String sLastSharedPreferencesName = null;
	private SharedPreferences mSharedPreferences = null;
	//#endregion 变量
	
	//#region private方法
	private static Context getApplicationContext(){
		return MaYaApplication.getInstance();
	}
	//#endregion private方法

	//#region 构造方法
	/**
	 * 通过名称取得实例
	 * @param sharedPreferencesName
	 * @return
	 */
	public static PreferencesHelper getInstance(String sharedPreferencesName){
		if(Helper.isNull(sInstance)){
			sInstance = new PreferencesHelper();
		}
		boolean userDefault = true;
		if(Helper.isNull(sInstance.mSharedPreferences) || (Helper.isEmpty(sharedPreferencesName) && Helper.isEmpty(sLastSharedPreferencesName))){
			userDefault = Helper.isEmpty(sharedPreferencesName);
		}else if(!Helper.equalString(sharedPreferencesName, sLastSharedPreferencesName, true)){
			userDefault = Helper.isEmpty(sharedPreferencesName);
		}else{
			return sInstance;
		}
		if(userDefault){
			sInstance.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			sLastSharedPreferencesName = null;
		}else{
			sInstance.mSharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
			sLastSharedPreferencesName = sharedPreferencesName;
		}
		return sInstance;
	}
	/**
	 * 取得实例
	 * @return
	 */
	public static PreferencesHelper getInstance(){
		return getInstance(null);
	}
	//#endregion 构造方法
	
	//#region
	/**
	 * 获取String类型（无值时，为""）
	 * @param key
	 * @return
	 */
	public String getString(String key){
		return this.mSharedPreferences.getString(key, "");
	}
	/**
	 * 获取String类型
	 * @param key
	 * @param defValue
	 * @return
	 */
	public String getString(String key, String defValue){
		return this.mSharedPreferences.getString(key, defValue);
	}
	private int mVerificationTwo = 800253060;
	/**
	 * 获取int类型（无值时，为0）
	 * @param key
	 * @return
	 */
	public int getInt(String key){
		return this.mSharedPreferences.getInt(key, 0);
	}
	/**
	 * 获取int类型
	 * @param key
	 * @param defValue
	 * @return
	 */
	public int getInt(String key, int defValue){
		return this.mSharedPreferences.getInt(key, defValue);
	}
	/**
	 * 获取float类型（无值时，为0f）
	 * @param key
	 * @return
	 */
	public float getFloat(String key){
		return this.mSharedPreferences.getFloat(key, 0f);
	}
	/**
	 * 获取float类型
	 * @param key
	 * @param defValue
	 * @return
	 */
	public float getFloat(String key, float defValue){
		return this.mSharedPreferences.getFloat(key, defValue);
	}
	/**
	 * 获取boolean类型（无值时，为false）
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key){
		return this.mSharedPreferences.getBoolean(key, false);
	}
	/**
	 * 获取boolean类型
	 * @param key
	 * @param defValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defValue){
		return this.mSharedPreferences.getBoolean(key, defValue);
	}
	/**
	 * 获取long类型（无值时，为0l）
	 * @param key
	 * @return
	 */
	public long getLong(String key){
		return this.mSharedPreferences.getLong(key, 0);
	}
	/**
	 * 获取long类型
	 * @param key
	 * @param defValue
	 * @return
	 */
	public long getLong(String key, long defValue){
		return this.mSharedPreferences.getLong(key, defValue);
	}
	//#endregion
	
	//#region
	/**
	 * 设置String类型
	 * @param key
	 * @param value
	 */
	public void putString(String key, String value){
		SharedPreferences.Editor editor = this.mSharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	/**
	 * 设置int类型
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value){
		SharedPreferences.Editor editor = this.mSharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * 设置boolean类型
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key, boolean value){
		SharedPreferences.Editor editor = this.mSharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	/**
	 * 设置long类型
	 * @param key
	 * @param value
	 */
	public void putLong(String key, long value){
		SharedPreferences.Editor editor = this.mSharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	/**
	 * 设置float类型
	 * @param key
	 * @param value
	 */
	public void putFloat(String key, float value){
		SharedPreferences.Editor editor = this.mSharedPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	//#endregion
	/**
	 * 清除
	 */
	public void clear(){
		SharedPreferences.Editor editor = this.mSharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	/**
	 * 删除指定值
	 * @param key
	 */
	public void deleteValue(String key){
		SharedPreferences.Editor editor = this.mSharedPreferences.edit();
		if(this.mSharedPreferences.contains(key)){
			editor.remove(key);
			editor.commit();
		}
	}
}
