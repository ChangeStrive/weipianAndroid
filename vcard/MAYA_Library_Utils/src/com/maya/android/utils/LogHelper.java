package com.maya.android.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;

import android.util.Log;

/**
 * 日志帮助类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-8-2
 *
 */
public class LogHelper {
	/**
	 * 是否打印日志
	 */
	private static boolean isLogOut = false;
	/**
	 * 设置是否打印日志
	 * @param enable
	 */
	public static void setLogEnabled(boolean enabled){
		isLogOut = enabled;
	}
	
	public static void e(String tag, String msg){
		if(isLogOut){
			Log.e(tag, msg);
		}
	}
	
	public static void d(String tag, String msg){
		if(isLogOut){
			Log.d(tag, msg);
		}
	}
	
	public static void i(String tag, String msg){
		if(isLogOut){
			Log.i(tag, msg);
		}
	}
	
	public static void v(String tag, String msg){
		if(isLogOut){
			Log.v(tag, msg);
		}
	}
	
	public static void w(String tag, String msg){
		if(isLogOut){
			Log.w(tag, msg);
		}
	}
	
	private static BufferedWriter sLogBufferedWriter = null;
	private static SimpleDateFormat sDateFormat = null;
	
	/**
	 * 日志写到文件中
	 * @param content
	 */
	public static void writeLog2File(String content){
		String logPath = ActivityHelper.getExternalStoragePath() + "Log/";
		File logPathFile = new File(logPath);
		if(!logPathFile.exists()){
			logPathFile.mkdirs();
		}
		String logFileName = "log.txt";
		File logFile = new File(logPath + logFileName);
		if(!logFile.exists()){
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(Helper.isNotEmpty(content)){
			Date date = new Date();
			if(Helper.isNull(sDateFormat)){
				sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			}
			content = "\n" + sDateFormat.format(date) + " 内容："  + content;
			try {
				if(Helper.isNull(sLogBufferedWriter)){
					sLogBufferedWriter = new BufferedWriter(new OutputStreamWriter(   
					          new FileOutputStream(logFile, true)));
				}
				try {
					sLogBufferedWriter.write(content);
					sLogBufferedWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} 
			
		}
	}
	/**
	 * 关闭日志打印
	 */
	public static void closeWriteLog2File(){
		if(Helper.isNotNull(sLogBufferedWriter)){
			try {
				sLogBufferedWriter.close();
				sLogBufferedWriter = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
