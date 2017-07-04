package com.platform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>Title: DateUtil</p>
 * <p>Description: 提供日期有关的相关操作</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author echoice
 * @version 1.0
 */
public class DateUtils
{
    /**
     * 根据传入的模式参数返回当天的日期
     * @param pattern 传入的模式
     * @return 按传入的模式返回一个字符串
     */
    public static String getToday(String pattern)
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    /**
     * 根据传入的模式参数返回当天的日期
     * @param pattern 传入的模式
     * @return 按传入的模式返回一个字符串
     */
    public static String getToday()
    {
        return getToday("yyyy-MM-dd");
    }
    
    /**
     * 根据传入的模式参数返回当天的日期
     * @param pattern 传入的模式
     * @return 按传入的模式返回一个字符串
     */
    public static String getNow()
    {
    	return getToday("yyyy-MM-dd HH:mm:ss");
    }   
    
    /**
     * 比较两个日期大小
     * @param date1 日期字符串
     * @param pattern1 日期格式
     * @param date2 日期字符串
     * @param pattern2 日期格式
     * @return boolean 若是date1比date2小则返回true
     * @throws ParseException
     */
    public static boolean compareMinDate(String date1, String pattern1,
                                         String date2, String pattern2) throws ParseException
    {
        Date d1 = convertToCalendar(date1, pattern1).getTime();
        Date d2 = convertToCalendar(date2, pattern2).getTime();
        return d1.before(d2);
    }
    /**
     * 比较两个日期大小
     * @param date1 日期字符串
     * @param date2 日期字符串
     * @param pattern 日期格式
     * @return boolean 若是date1比date2小则返回true
     * @throws ParseException
     */
    public static boolean compareMinDate(String date1,String date2, String pattern) throws ParseException
	{
		Date d1 = convertToCalendar(date1, pattern).getTime();
		Date d2 = convertToCalendar(date2, pattern).getTime();
		return d1.before(d2);
	}
    /**
     * 比较两个日期大小
     * @param date1 Date
     * @param date2 Date
     * @return boolean 若是date1比date2小则返回true
     */
    public static boolean compareMinDate(Date date1, Date date2)
    {
        try
        {
            return DateUtils.compareMinDate(DateUtils.formatDate(date1, "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss",
                                           DateUtils.formatDate(date2, "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss");
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    /**
     * 根据传入的日期字符串以及格式，产生一个Calendar对象
     * @param date 日期字符串
     * @param pattern 日期格式
     * @return Calendar
     * @throws ParseException 当格式与日期字符串不匹配时抛出该异常
     */
    public static Calendar convertToCalendar(String date, String pattern) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date d = sdf.parse(date);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        return calendar;
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param pattern 字符串的格式
     * @param currentDate 被格式化日期
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     */
    public static String formatDate(Calendar currentDate, String pattern)
    {
        if(currentDate == null || pattern == null)
        {
            throw new NullPointerException("The arguments are null !");
        }
        Date date = currentDate.getTime();
        return formatDate(date, pattern);
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param pattern 字符串的格式
     * @param currentDate 被格式化日期
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     */
    public static String formatDate(Date currentDate, String pattern)
    {
        if(currentDate == null || pattern == null)
        {
            throw new NullPointerException("The arguments are null !");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(currentDate);
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param currentDate 被格式化日期字符串 必须为yyyymmdd
     * @param pattern 字符串的格式
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     * @throws java.text.ParseException 若被格式化日期字符串不是yyyymmdd形式时抛出
     */
    public static String formatDate(String currentDate, String pattern) 
    {
        if(currentDate == null || pattern == null)
        {
            throw new NullPointerException("The arguments are null !");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
		try {
			date = sdf.parse(currentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sdf.applyPattern(pattern);
        return sdf.format(date);
    }

    /**
     * 用途：以指定的格式格式化日期字符串
     * @param strDate 被格式化日期字符串 必须为yyyymmdd
     * @param formator 格式字符串
     * @return String 已格式化的日期字符串
     * @throws NullPointerException 如果参数为空
     * @throws java.text.ParseException 若被格式化日期字符串不是yyyymmdd形式时抛出
     */
    public static Calendar strToDate(String strDate)
    {
        if(strDate == null)
        {
            throw new NullPointerException("The arguments are null !");
        }
        Calendar date = Calendar.getInstance();
        date.setTime(java.sql.Date.valueOf(strDate));
        return date;
    }


    /**
     * 开始时间小于结束时间返回true，否则返回false
     * @param beginDate Date
     * @param endDate Date
     * @return boolean
     */
    public boolean compare(Date beginDate, Date endDate)
    {
        try
        {

            return DateUtils.compareMinDate(DateUtils.formatDate(beginDate,
                    "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss",
                                           DateUtils.formatDate(endDate,
                    "yyyy-MM-dd HH:mm:ss"),
                                           "yyyy-MM-dd HH:mm:ss");

        }
        catch(Exception ex)
        {
//            log.error ( "时间格式转换错误" + ex ) ;
            return false;
        }
    }

    /**
     * 将指定格式的时间String转为Date类型
     * @param dateStr String 待转换的时间String
     * @param pattern String 转换的类型
     * @throws ParseException
     * @return Date
     */
    public static Date convertStringToDate(String dateStr,String patternner) throws ParseException
    {
        String pattern =patternner;

        if(dateStr == null)
        {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateStr);
    }

    public static String convertDateToString(Date date) 
    {
        if(date == null)
        {
            return "";
        }
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 获得时间差天数
     * @param time1
     * @param time2
     * @param patternner
     * @return
     * @throws ParseException
     */
    public static int getDiffDay(String  time1,String time2,String patternner) throws ParseException{
    	Date d1 = DateUtils.convertToCalendar(time1,patternner).getTime();
		Date d2 = DateUtils.convertToCalendar(time2,patternner).getTime();
		return (int) ((d1.getTime()-d2.getTime())/(24*60*60*1000));
    }
    /**
     * 字符串转日期
     * @param strDate
     * @param format yyyy-MM-dd
     * @return
     */
    public static Date getDate(String strDate,String format){
 	   Date dt = null;
 	   SimpleDateFormat df = new SimpleDateFormat(format==null?"yyyy-MM-dd":format);
 	   try {
 	    dt = df.parse(strDate);
 	   } catch (ParseException e) {
 	    // TODO Auto-generated catch block
 	    e.printStackTrace();
 	   }
 	   return dt;
 	}
    
    
    public static Long dateDiff(String startTime, String endTime,  String format, String str) { 
        // 按照传入的格式生成一个simpledateformate对象  
		        SimpleDateFormat sd = new SimpleDateFormat(format);  
		        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数  
		        long nh = 1000 * 60 * 60;// 一小时的毫秒数  
		        long nm = 1000 * 60;// 一分钟的毫秒数  
		        long ns = 1000;// 一秒钟的毫秒数  
		        long diff;  
		        long day = 0;  
		        long hour = 0;  
		        long min = 0;  
		        long sec = 0;  
		        // 获得两个时间的毫秒时间差异  
		        try {  
		            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();  
		            if (str.equalsIgnoreCase("h")) {  
		                return diff/nh;  
		            } else if (str.equalsIgnoreCase("m")) {  
		                return diff/nm;  
		            }else if (str.equalsIgnoreCase("nd")) {  
		                return diff/nm;  
		            }else if (str.equalsIgnoreCase("s")) {  
		                return diff/ns;  
		            }
		 
		 
		        } catch (ParseException e) {  
		            e.printStackTrace();  
		        }  
		        if (str.equalsIgnoreCase("h")) {  
		            return hour;  
		        } else {  
		            return min;  
		        }  
		}
    
    public static String getNowDiff(String startTime,String format){
    	String str="";
    	SimpleDateFormat sd = new SimpleDateFormat(format);  
    	long nMonth = 1000 * 24 * 60 * 60*30;// 一天的毫秒数  
    	long nWeek = 1000 * 24 * 60 * 60*7;// 一天的毫秒数  
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数  
        long nh = 1000 * 60 * 60;// 一小时的毫秒数  
        long nm = 1000 * 60;// 一分钟的毫秒数  
        long ns = 1000;// 一秒钟的毫秒数  
        long diff;  
        long month = 0; 
        long week=0;
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        // 获得两个时间的毫秒时间差异  
        try {  
            diff = sd.parse(DateUtils.getNow()).getTime() - sd.parse(startTime).getTime();  
            month=diff/nMonth;  
            week=diff/nWeek;  
            day=diff/nd;  
            hour=diff/nh;  
            min=diff/nm;  
            if(month>0){
            	str=nMonth+"个月前";
            }else if(week>0){
            	str=week+"周前";
            }else if(day>0){
            	str=day+"天前";
            }else if(hour>0){
            	str=hour+"小时前";
            }else if(min>0){
            	str=min+"分钟前";
            }else{
            	str="刚刚";
            }
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
        return str;
    }
    
    public static String getRemainDay(String startTime,String endTime,String format){
    	String str="";
    	SimpleDateFormat sd = new SimpleDateFormat(format);  
    	long nMonth = 1000 * 24 * 60 * 60*30;// 一天的毫秒数  
    	long nWeek = 1000 * 24 * 60 * 60*7;// 一天的毫秒数  
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数  
        long nh = 1000 * 60 * 60;// 一小时的毫秒数  
        long nm = 1000 * 60;// 一分钟的毫秒数  
        long ns = 1000;// 一秒钟的毫秒数  
        long diff;  
        long month = 0; 
        long week=0;
        long day = 0;  
        long hour = 0;  
        long min = 0;  
        long sec = 0;  
        // 获得两个时间的毫秒时间差异  
        try {  
            diff =sd.parse(startTime).getTime()-sd.parse(endTime).getTime();
            day=diff/nd;  
            hour=(diff%nd)/nh;  
            min=(diff%nd%nh)/nm;  
           if(day>0){
            	str+=day+"天";
           }
           if(hour>0){
            	str+=hour+"时";
            }
           if(min>0){
            	str+=min+"分";
            }
        } catch (ParseException e) {  
            e.printStackTrace();  
        }
        return str;
    }
}
