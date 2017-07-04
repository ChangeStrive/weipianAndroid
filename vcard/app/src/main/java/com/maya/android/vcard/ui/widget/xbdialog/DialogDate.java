package com.maya.android.vcard.ui.widget.xbdialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 显示日期
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class DialogDate {
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private WheelMain wheelMain;
	private static DialogDate intance;
	private DialogDate() {

	}

	/**
	 * 初始化调用
	 * 
	 * @return
	 */
	public static DialogDate getIntance() {
		if (intance == null) {
			intance = new DialogDate();
		}
		return intance;

	}
	/** 
	 * @param activity
	 * @param startyear 开始年限
	 * @param endyear 结束年限
	 * @return 
	 */
	public View getDialogDate(Activity activity,int startyear, int endyear){
		//设定开始年份
		WheelMain.START_YEAR = startyear;
		//设定结束年份
		WheelMain.END_YEAR = endyear;
		return getDialogDate(activity);
	}
	/**
	 * 
	 * @param activity
	 * @param startyear 开始年限
	 * @param endyear 结束年限
	 * @param showTime 需要展示的时间格式为“yyyy-MM-dd”
	 * @return
	 */
	public View getDialogDate(Activity activity,int startyear, int endyear, String showTime){
		//设定开始年份
		WheelMain.START_YEAR = startyear;
		//设定结束年份
		WheelMain.END_YEAR = endyear;
		return getDialogDate(activity, showTime);
	}
	
	
	/**
	 * 传入activity
	 * @param activity
	 * @return
	 */
	@SuppressLint("InflateParams")
	public View getDialogDate(Activity activity) {
		View timepickerview = LayoutInflater.from(activity).inflate(R.layout.timepicker, null);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = ActivityHelper.getScreenHeight();
		Calendar calendar = Calendar.getInstance();
		String time = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "";

		if (ResourceHelper.isDate(time, "yyyy-MM-dd")) {
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);
		return timepickerview;
	}
	/**
	 * @param activity
	 * @param showTime 需要展示的时间格式为“yyyy-MM-dd”
	 * @return
	 */
	public View getDialogDate(Activity activity, String showTime) {
		if(Helper.isEmpty(showTime)){
			return this.getDialogDate(activity);
		}
		Date showTimeDate = null;
		try {
			showTimeDate = dateFormat.parse(showTime);
		} catch (ParseException e) {
			e.printStackTrace();
			showTimeDate = null;
			return this.getDialogDate(activity);
		}
		View timepickerview = LayoutInflater.from(activity).inflate(R.layout.timepicker, null);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = ActivityHelper.getScreenHeight();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(showTimeDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);
		return timepickerview;
	}
	/**
	 * 获取选择的时间
	 * 
	 * @return
	 */
	public String getDates() {
		return wheelMain.getTime();

	}
	
	
}
