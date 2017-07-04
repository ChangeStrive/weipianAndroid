package com.maya.android.vcard.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import java.util.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 日期时间帮助类
 * Created by Administrator on 2015/9/2.
 */
public class DateTimeHepler {
    private static Context sContext = ActivityHelper.getGlobalApplicationContext();
    //#region 日期时间相关
    public static String sDateFormat_YYMMDD_HHMM = "yyyy-MM-dd HH:mm";
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sDateFormat_YYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat sDateFormat_YYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat sTimeFormat_HHMM = new SimpleDateFormat("HH:mm");
    /**
     * 把 日期时间字符串 转换为 date
     * @param datetime
     * @return
     */
    private static java.util.Date String2Date(String datetime){

        ParsePosition pos = new ParsePosition(0);
        Date strToDate = sDateFormat_YYMMDD_HHMMSS.parse(datetime, pos);

        if(Helper.isNull(strToDate)){
            strToDate = sDateFormat_YYMMDD.parse(datetime, pos);
        }
        return strToDate;
    }
    /**
     * 获取现在时间 字符串
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getNowTime() {
        String dateString = sDateFormat_YYMMDD_HHMMSS.format(new Date());
        return dateString;
    }
    /**
     * 获取时间 小时:分 HH:mm
     *
     * @return
     */
    public static String getTimeShortStr(String datetime) {
        Date date = String2Date(datetime);
        String dateString = sTimeFormat_HHMM.format(date);
        return dateString;
    }
    /**
     * 返回 周几
     * @param datetime
     * @return
     */
    public static String getWeekShort(String datetime){
        Calendar c = Calendar.getInstance();
        c.setTime(String2Date(datetime));
        String weekStr = "";
        int num = c.get(Calendar.DAY_OF_WEEK);
        switch (num) {
            case 1:
                weekStr = sContext.getString(R.string.sunday);
                break;
            case 2:
                weekStr = sContext.getString(R.string.monday);
                break;
            case 3:
                weekStr = sContext.getString(R.string.tuesday);
                break;
            case 4:
                weekStr = sContext.getString(R.string.wednesday);
                break;
            case 5:
                weekStr = sContext.getString(R.string.thursday);
                break;
            case 6:
                weekStr =sContext.getString(R.string.friday);
                break;
            case 7:
                weekStr = sContext.getString(R.string.saturday);
                break;
        }
        return weekStr;
    }
    /**
     * 传入的日期时间转化为 日期 + 周几显示
     * @param datetime
     * @return yyyy-MM-dd 周几
     */
    public static String getDateWeekForShow(String datetime) {
        if(ResourceHelper.isEmpty(datetime)){
            return "";
        }

        String formatDate = "";
        String [] timeArr = datetime.split(" ");
        if(Helper.isNotEmpty(timeArr) && timeArr.length >= 1){
            String date = timeArr[0];
            String weekDay = getWeekShort(datetime);
            formatDate = date + " " + weekDay;
        }
        return formatDate;
    }

    /**
     * 获取显示的时间
     * @param time
     * @return  今天/昨天/前天/yyyy-MM-dd HH:mm
     */
    public static String getDateTimeForSession(String time){
        if(ResourceHelper.isEmpty(time)){
            return "";
        }
        String   theDayTime   =   getNowTime();
        String befor = time.substring(0, 10);
        String beforTime = befor.replaceAll("-", "");
        String now = theDayTime.substring(0, 10);
        String nowTime = now.replaceAll("-", "");
        String dayTime = befor;
        if(ResourceHelper.isValidNumberType(beforTime) && ResourceHelper.isValidNumberType(nowTime)){
            if(Integer.valueOf(nowTime) - Integer.valueOf(beforTime) == 0){
                dayTime = ActivityHelper.getGlobalApplicationContext().getString(R.string.common_today);
            }else if((Integer.valueOf(nowTime) - Integer.valueOf(beforTime) == 1) || ((Integer.valueOf(nowTime) - Integer.valueOf(beforTime) >= 70) && (Integer.valueOf(nowTime) - Integer.valueOf(beforTime) <= 73))){
                dayTime = ActivityHelper.getGlobalApplicationContext().getString(R.string.common_yesterday);
            }
        }
        String resultTime = dayTime + " " + getTimeShortStr(time);
        return resultTime;
    }
    /**
     * 按格式化显示时间
     * @param dateTime
     * @param format
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateTimeFormat(String dateTime, String format){

        if(ResourceHelper.isEmpty(dateTime)){
            return "";
        }
        java.util.Date strtodate = String2Date(dateTime);
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        if(ResourceHelper.isNotNull(strtodate)){
            return formatter.format(strtodate);
        }
        return dateTime;
    }
    /**
     * 比较时间 显示
     * 		与当前时间做比较
     * @param strDate
     * @return 今天 、昨天 、明天 、YYYY-mm-DD
     */
    private static String compareNow(String strDate){
        String dateShow = null;
        long now = new Date().getTime();

        java.util.Date strToDate = String2Date(strDate);
        long compareTme = strToDate.getTime();
        long dateDiff = now - compareTme;

        long day = 24*3600*1000;
        if(dateDiff <= day && dateDiff > 0){
            dateShow =  ActivityHelper.getGlobalApplicationContext().getString(R.string.common_today);
        }else if(dateDiff > day && dateDiff < 2* day){
            dateShow = ActivityHelper.getGlobalApplicationContext().getString(R.string.common_yesterday);
        }else if(dateDiff > 2 *day && dateDiff <= 3 * day){
            dateShow = ActivityHelper.getGlobalApplicationContext().getString(R.string.common_thirday);
        }else{
            dateShow = sDateFormat_YYMMDD.format(strToDate);
        }
        return dateShow;
    }
    /**
     * 判断是否为合法的日期时间字符串
     * @param strInput
     * @param strInput
     * @return boolean;符合为true,不符合为false
     */
    public static  boolean isDate(String strInput, String rDateFormat){
        if (ResourceHelper.isNotNull(strInput)) {
            SimpleDateFormat formatter = new SimpleDateFormat(rDateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(strInput));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
