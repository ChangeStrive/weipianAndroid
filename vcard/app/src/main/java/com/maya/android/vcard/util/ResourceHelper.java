package com.maya.android.vcard.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.ContactBookDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.BusinessEnum;
import com.maya.android.vcard.entity.CustomLsvIconEntity;
import com.maya.android.vcard.entity.EmailSendEntity;
import com.maya.android.vcard.entity.SMSSendEntity;
import com.maya.android.vcard.entity.TerminalAgentEntity;

import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resource Helper类
 * Created by YongJi on 2015/8/18.
 */
public class ResourceHelper {

    //变量
    private static Context sContext = ActivityHelper.getGlobalApplicationContext();
    //变量

    //region public方法
    /**
     * 根据资源id 获取dip/sp 并转化为px
     * @param resouceId
     * @return int
     */
    public static int getDp2PxFromResouce(int resouceId) {
        return sContext.getResources().getDimensionPixelOffset(resouceId);
    }
    /**
     * 获取RawFile并转化成String
     * @return
     */
    public static String getStringFromRawFile(int resourceId){
        String result = null;
        InputStreamReader inputRawStream = new InputStreamReader(
                sContext.getResources().openRawResource(resourceId));
        BufferedReader reader = new BufferedReader(inputRawStream);
        if(Helper.isNotNull(reader)){
            String line;
            StringBuffer strBuffer = new StringBuffer();
            try {
                while(Helper.isNotNull((line = reader.readLine()))){
                    strBuffer.append(line.trim());
                }
                result = strBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(Helper.isNotNull(reader)){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputRawStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    //region SD卡 ConfigFile
    /** ConfigFile内容 **/
    private static String sConfigInfo = null;
    /** 是否有进行Config判断 **/
    private static boolean sIsDoneJudgeConfig = false;
    /** 是否存在Config文件 **/
    private static boolean sIsExistConfigFile = false;
    /**
     * 是否存在Config文件
     * @return
     */
    public static boolean isExistConfigFile(){
        if(!sIsDoneJudgeConfig){
            getConfigInfoFromSDCard();
        }
        return sIsExistConfigFile;
    }
    /**
     * 获取Config内容
     * @return
     */
    public static String getConfigInfoFromSDCard(){
        if(!sIsDoneJudgeConfig){
            String configFilePath = Constants.PATH.PATH_VCARD_CONFIG;
            File configFile = new File(configFilePath);
            if(configFile.exists()){
                sIsExistConfigFile = true;
                BufferedReader reader = null;
                StringBuffer stringBuffer = new StringBuffer();
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8")); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码
                    String str = null;
                    while (Helper.isNotNull(str = reader.readLine())) {
                        stringBuffer.append(str);
                    }
                    sConfigInfo = stringBuffer.toString();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(Helper.isNotNull(reader)){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                sIsExistConfigFile = false;
            }
            sIsDoneJudgeConfig = true;
        }
        return sConfigInfo;
    }
    /**
     * 写入Config内容
     * @param vcardNo
     */
    public static void putConfigInfo2SDCard(String vcardNo){
        if(Helper.isNotEmpty(vcardNo)){
            String configFilePath = Constants.PATH.PATH_VCARD_CONFIG;
            File configFile = new File(configFilePath);
            if(!configFile.exists()){
                String vcardFilePath = Constants.PATH.PATH_BASE_VCARD;
                File vcardFile = new File(vcardFilePath);
                if(!vcardFile.exists()){
                    vcardFile.mkdirs();
                }
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
                writer.write(vcardNo);
                writer.flush(); // 全部写入缓存中的内容
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (Helper.isNotNull(writer)) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //endregion SD卡 ConfigFile

    //region 验证相关
    /**
     * 验证:是否为有效手机号码
     * 允许 86-15005946558, 8615005946558,15005946558
     * ^([\\(\\+]*\\d{2}[\\+\\s\\)-]*)?\\d{11}$
     */
    public static boolean isValidMobile(String strMobile) {
        if(Helper.isEmpty(strMobile)){
            return false;
        }
        Pattern p = Pattern.compile("^([\\(\\+]*\\d{2}[\\+\\s\\)-]*)?((13[0-9])|(15[^4,\\D])|(18[0-9])|(147))\\d{8}$");
        Matcher m = p.matcher(strMobile);
        return m.matches();
    }

    /**
     * 验证:多组号码是否都为有效手机号码(默认解析规则为"#")
     * @param strMobile 电话号码字符串
     * @return
     */
    public static boolean isValidArrayMobile(String strMobile){
        return isValidArrayMobile(strMobile, null);
    }

    /**
     * 验证:多组号码是否都为有效手机号码
     * @param strMobile 电话号码字符串
     * @param regularExpression 截取规则
     * @return
     */
    public static boolean isValidArrayMobile(String strMobile, String regularExpression){
       if(ResourceHelper.isEmpty(strMobile)){
          return false;
        }
        String regular = Constants.Other.CONTENT_ARRAY_SPLIT;
        if(ResourceHelper.isNotEmpty(regularExpression)){
            regular = regularExpression;
        }
        String[] mobileArr = strMobile.trim().split(regular);
        if(ResourceHelper.isNull(mobileArr)){
            return false;
        }
        boolean result = true;
        Pattern p = Pattern.compile("^([\\(\\+]*\\d{2}[\\+\\s\\)-]*)?((13[0-9])|(15[^4,\\D])|(18[0-9])|(147))\\d{8}$");
        for(int i = 0, mobileArrLength = mobileArr.length; i < mobileArrLength; i++){
            Matcher m = p.matcher(mobileArr[i]);
            boolean isValidMobile = m.matches();
            if(!isValidMobile){
                result = isValidMobile;
                break;
            }
        }
        return result;
    }

    /**
     * 验证：是否为有效邮箱
     *
     * @param strEmail
     * @return
     */
    public static boolean isValidEmail(String strEmail) {
        if(Helper.isEmpty(strEmail)){
            return false;
        }
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     *  验证：多组 是否为有效邮箱(默认解析规则为"#")
     * @param strEmail 邮箱字符串
     * @return
     */
    public static boolean isValidArrayEmail(String strEmail){
        return isValidArrayEmail(strEmail, null);
    }

    /**
     * 验证：多组 是否为有效邮箱
     * @param strEmail 邮箱字符串
     * @param regularExpression 截取规则
     * @return
     */
    public static boolean isValidArrayEmail(String strEmail, String regularExpression){
        if(ResourceHelper.isEmpty(strEmail)){
            return false;
        }
        String regular = Constants.Other.CONTENT_ARRAY_SPLIT;
        if(ResourceHelper.isNotEmpty(regularExpression)){
            regular = regularExpression;
        }
        String[] emailArr = strEmail.trim().split(regular);
        if(ResourceHelper.isNull(emailArr)){
            return false;
        }
        boolean result = true;
        Pattern p = Pattern.compile("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$");
        for(int i = 0, emailArrLength = emailArr.length; i < emailArrLength; i++){
            Matcher m = p.matcher(emailArr[i]);
            boolean isValidEmail = m.matches();
            if(!isValidEmail){
                result = isValidEmail;
                break;
            }
        }
        return result;
    }

    /**
     * 验证：是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isValidNumberType(String str) {
        if(Helper.isEmpty(str)){
            return false;
        }
        Pattern p = Pattern.compile("^[0-9]*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 从资源文件获取列表
     * @return
     */
    public static ArrayList<String> getListFromResArray(Context context, int resId){
        ArrayList<String> result = new ArrayList<String>();
        String[] dataArray = context.getResources().getStringArray(resId);
        for(int i = 0, len = dataArray.length; i<len; i++){
            result.add(dataArray[i]);
        }
        return result;
    }

    /**
     *  获取arrays
     * @param position
     * @return
     */
    public static CharSequence getResArrayChild(int position, int resId) {
        return sContext.getResources().getStringArray(resId)[position];
    }
    //endregion 验证相关

    /**
     * 获取Dimen资源文件里的数字
     * @param context
     * @param resouceId
     * @return
     */
    public static float getDimenNumFromResouce(Context context, int resouceId){
        String resultStr = context.getString(resouceId);
        resultStr = resultStr.replace("dip", "").replace("dp", "").replace("sp", "");
        float result = Float.parseFloat(resultStr);
        return result;
    }

    /**
     * 获取String资源内容
     * @param strId
     * @return
     */
    public static String getString(int strId){
        return sContext.getResources().getString(strId);
    }

    /**
     * 获取资源中的图片
     * @param resId
     * @return
     */
    public static Drawable getDrawable(int resId){
        return sContext.getResources().getDrawable(resId);
    }

    /**
     * 获取性别对应的图标资源id
     * @param code
     * @return int
     */
    public static int getSexImageResource(int code){
        if(code == 1){
            return R.mipmap.img_sex_male;
        }
        return R.mipmap.img_sex_female;
    }

    /**
     * 根据出生日期获取年龄
     * @param birthday
     * @return
     */
    public static String getAgeFromBirthday(String birthday){
        String retAge = "";
        if(Helper.isNotEmpty(birthday)){
            try {
                Calendar calendarBirthday = Calendar.getInstance();
                Date dateBirthday = sDateFormat_YYMMDD.parse(birthday);
                calendarBirthday.setTime(dateBirthday);
                Calendar calendarNow = Calendar.getInstance();
                calendarNow.setTime(new Date());
                if(calendarBirthday.after(calendarNow)){

                    return null;
                }
                int age=0;
                int diffDay = 0;
                int mDifYear = calendarNow.get(Calendar.YEAR) - calendarBirthday.get(Calendar.YEAR);//年份数
                int mNowDay = calendarNow.get(Calendar.DAY_OF_YEAR);// 年初~今天 的天数
                int mBirthday = calendarBirthday.get(Calendar.DAY_OF_YEAR);//出生那年  年初~出生当天 的天数
                if(mDifYear > 0){
                    if(mDifYear == 1){
                        diffDay = mNowDay + mBirthday;
                    }else{
                        diffDay = 365 * mDifYear + mNowDay + (365 - mBirthday);
                    }
                }else{
                    diffDay = mNowDay - mBirthday;
                }

                if(diffDay > 365){
                    age = diffDay / 365;
                    //年
                    retAge = age + "岁";
                }else if(diffDay > 30){
                    age = diffDay / 30;
                    //月
                    retAge = age + "月";
                }else{
                    //天
                    if(diffDay == 0){
                        diffDay++;//如果是今天出生的 默认为1天
                    }
                    retAge = diffDay + "天";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return retAge;
    }

    /**
     * 根据出生日期 获取 星座
     * @param birthday
     * @return
     */
    public static String getAstro(String birthday){
        String astro=null;
        try {
            if(birthday!=null && !birthday.equals("")){
                Calendar cd=Calendar.getInstance();
                SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(birthday);
                cd.setTime(date);
                astro= getAstro(cd.get(Calendar.MONTH) + 1, cd.get(Calendar.DAY_OF_MONTH));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return astro;
    }

    /**
     * 根据月日获取星座信息
     * @param month 月
     * @param day 日
     * @return
     */
    public static String getAstro(int month, int day) {
        String[] astro = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
                "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
        int[] arr = new int[] { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };// 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month-1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return astro[index];
    }

    /**
     * 获取行业对应名
     * @param context
     * @param code
     * @return
     */
    //TODO 必须改
    public static String getBusinessNameByCode(Context context,int code){
        return BusinessEnum.getBusinessNameByCode(code);
    }

    /**
     * 获取行业对应的图标
     * @param context
     * @param code
     * @return Bitmap
     */
    //TODO 必须改
    public static Bitmap getBusinessIconByCode(Context context, int code) {
        String iconName = BusinessEnum.getBusinessIconNameByCode(code);
        Bitmap bit = null;
        if (ResourceHelper.isNotEmpty(iconName)) {
            bit = getImgBitmap(context, iconName);
        }
        return bit;
    }

    /**
     * 获取行业图片Id及对应名称
     * @return
     */
    public static ArrayList<CustomLsvIconEntity> getArrayListBusiness(){
        ArrayList<CustomLsvIconEntity> customIcons = new ArrayList<CustomLsvIconEntity>();
        int businessCount = BusinessEnum.length;
        CustomLsvIconEntity customIcon = new CustomLsvIconEntity();
        for(int i = 1; i < businessCount; i++){
            try {
                customIcon = (CustomLsvIconEntity) customIcon.clone();
                customIcon.setIconId(getImgResourceId(sContext, BusinessEnum.getBusinessIconNameByCode(i)));
                customIcon.setBusinessName(BusinessEnum.getBusinessNameByCode(i));
                customIcons.add(customIcon);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return customIcons;
    }

    /**
     * 根据图片名称 获取资源下的图片
     * @param context
     * @param imgName
     * @return
     */
    //TODO 必须改
    public static Bitmap getImgBitmap(Context context, String imgName) {

        int resID = getImgResourceId(context, imgName);

        Bitmap bitM = BitmapFactory.decodeResource(context.getResources(), resID);
        return bitM;

    }
    /**
     * 根据资源的图片名称获取资源id
     *
     * @param context
     * @param imgName
     * @return
     */
    //TODO 必须改
    public static int getImgResourceId(Context context, String imgName) {
        int resID = 0;
        try {
            imgName = imgName.replace(".png", "").replace(".jpg", "");
            resID = context.getResources().getIdentifier(imgName, "mipmap", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resID;
    }

    /**
     * ImageView：判断是填充本地图片或者网络图片
     * @param imageView
     * @param url (可为 非完整 网路路径 )
     */
    public static void asyncImageViewFillUrl(AsyncImageView imageView, String url){
        if(ResourceHelper.isNotEmpty(url)){
            String fullPath = getHttpImageFullUrl(url);
            asyncImageViewFillPath(imageView, fullPath);
        }
    }
    /**
     * ImageView：判断是填充本地图片或者网络图片
     * @param imageView
     * @param url
     */
    public static void asyncImageViewFillPath(AsyncImageView imageView, String url){
        if(!Helper.isEmpty(url)){
            imageView.autoFillFromUrl(url);
        }
    }
    /**
     * 获取网络图片地址
     * @param url
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String getHttpImageFullUrl(String url){
        if(ResourceHelper.isEmpty(url)){
            return url;
        }else{
            String urlLowerCase = url.toLowerCase();
            if(urlLowerCase.startsWith(Constants.ImagePathSign.VCARD_IMAGE_PATH_SIGN_UPLOAD)){
                return CurrentUser.getInstance().getURLEntity().getImgBaseService() + url;
            }else if(urlLowerCase.startsWith(Constants.ImagePathSign.VCARD_IMAGE_PATH_SIGN_HTTP)){
                return url;
            }else{
                return url;
            }
        }
    }

    /**
     * 把arrlist 转为字符拼接的 字符串
     * @param itemList
     * @param concatSign
     * @return
     */
    public static String getStringFromList(ArrayList<String> itemList, String concatSign){
        if(Helper.isNull(itemList)){
            return null;
        }
        StringBuilder concats = new StringBuilder();
        for (int i = 0, size = itemList.size(); i < size; i++) {
            concats.append(itemList.get(i)).append(concatSign);
        }
        if(Helper.isNotEmpty(concats)){
            return concats.substring(0, concats.length()-1);
        }
        return null;
    }

    /**
     * 把传入的字符串按分隔符拆开返回列表
     * @param input
     * @param splitStr
     * @return
     */
    public static ArrayList<String> getListFromString(String input, String splitStr){
        if(Helper.isEmpty(input) || Helper.isEmpty(splitStr)){
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        String[] array = input.split(splitStr);
        for(int i = 0; i < array.length; i++){
            if(Helper.isNotEmpty(array[i]))
                list.add(array[i]);
        }
        return list;
    }

    /**
     * 创建空文件
     * @param fileName
     * @return
     */
    public static String creatEmptyFile(String fileName){
        String result = null;
        if(Helper.isEmpty(fileName)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDD_HHmmss");
            fileName = sdf.format(new java.util.Date())+".jpg";
        }
        result = CurrentUser.getCurrentCachePath() + fileName;
        try {
            File file = new File(result);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取消息的文件存放路径
     * @param folder 子文件夹名称
     * @param fileName 文件名
     * @return
     */
    public static String getMessageLocalFilePath(String folder, String fileName){
        String filePath = null;
        try {
            String path = String.format(Constants.MessageFiles.MESSAGE_PATH, CurrentUser.getInstance().getVCardNo(), folder);
            filePath = path + fileName;
            boolean success = (new File(path)).mkdirs();
            File file = new File(filePath);
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * 保存图片到指定路径
     * @param bitmap
     * @param fileDir 文件完整路径
     * @return
     */
    public static String saveBitmap2SDCard(Bitmap bitmap, String fileDir, String fileName){
        String result = null;

        if(Helper.isNotNull(bitmap)){
            if(Helper.isEmpty(fileDir)){
                fileDir = CurrentUser.getCurrentCachePath();
            }
            if(Helper.isEmpty(fileName)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMDD_HHmmss");
                fileName = sdf.format(new java.util.Date())+".jpg";
            }
            result = fileDir + fileName;
            FileOutputStream fos = null;
            try {
                File file = new File(result);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取裁切图像的 intent 对象
     * @param uri
     * @param cutWidth
     * @return
     */
    public static Intent getCropImageIntent(Uri uri, int cutWidth) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", cutWidth);
        intent.putExtra("outputY", cutWidth);
        intent.putExtra("return-data", true);
        return intent;
    }

    /**
     * 获取本地图库图片路径
     * @param data
     * @return
     */
    public static String getChoosePhotoPath(Intent data, Context context){
        Uri uri = data.getData();
        String path = null;
        if(uri.toString().startsWith("content")){

            String [] proj={MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query( uri,
                    proj,       // Which columns to return
                    null,       // WHERE clause; which rows to return (all rows)
                    null,       // WHERE clause selection arguments (none)
                    null);      // Order-by clause (ascending by name)
            if(Helper.isNotNull(cursor)){
                if(cursor.moveToFirst()){
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(column_index);
                }
                cursor.close();
                cursor = null;
            }
        }else{
            path = String.valueOf(uri).substring(7);
        }
        return path;
    }

    /**
     * 获取 用 “#” 隔开的 多个 图片路径中的一个(获取字符串中第一条字符串)
     * @param urls
     * @return 指定项的路径
     */
    public static String getImageUrlOnIndex(String urls, int index){
        if(ResourceHelper.isEmpty(urls)){
            return urls;
        }
        String curUrl = urls;
        String[] urlArr = urls.split(Constants.Other.CONTENT_ARRAY_SPLIT);

        if(ResourceHelper.isNotNull(urlArr) && urlArr.length > 0){
            if(index < urlArr.length){
                curUrl = urlArr[index];
            }
        }
        return curUrl;
    }

    /**
     * 设置字体不同大小
     * @param mContext
     * @param textView
     * @param topText
     * @param sizeId
     * @param bottomText
     * @param size2Id
     */
    public static void setStoreTextSize(Context mContext,TextView textView, String topText, int sizeId, String bottomText, int size2Id) {
//        String newText = topText + "\n" + bottomText;
        String newText = bottomText+"\n"+topText;
//        int end = topText.length();
        int end = bottomText.length();
//        int secStart = newText.length() - bottomText.length();
        int secStart = newText.length() - topText.length();
        int nameDp = (int) ResourceHelper.getDimenNumFromResouce(mContext, sizeId);
        int numDp = (int) ResourceHelper.getDimenNumFromResouce(mContext, size2Id);
        Spannable spanText = new SpannableString(newText);
        spanText.setSpan(new AbsoluteSizeSpan(nameDp, true), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置0-2的字符颜色
        spanText.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_7e)), secStart, newText.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new AbsoluteSizeSpan(numDp, true), secStart, newText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spanText);
    }

    /**
     *String 判空(不空)
     * <p>简单判定是否为空，内容如下：</p>
     * <p>obj == null || "".equals(obj)</p>
     * @param obj
     * @return
     */
    public static boolean isNotEmpty(String obj){
        return !isEmpty(obj);
    }

    /**
     *String 判空(空)
     * <p>简单判定是否为空，内容如下：</p>
     * <p>obj == null || "".equals(obj)</p>
     * @param obj
     * @return
     */
    public static boolean isEmpty(String obj){
        return obj == null || "".equals(obj);
    }

    /**
     *判空(不空)
     * @param object
     * @return
     */
    public static boolean isNotNull(Object object){
        return null != object;
    }

    /**
     * 判空（空）
     * @param object
     * @return
     */
    public static boolean isNull(Object object){
        return null == object;
    }

    //region 日期时间相关
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
    private static Date String2Date(String datetime){

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
                weekStr = "周日";
                break;

            case 2:
                weekStr = "周一 ";
                break;
            case 3:
                weekStr = "周二";
                break;
            case 4:
                weekStr = "周三";
                break;
            case 5:
                weekStr = "周四";
                break;
            case 6:
                weekStr = "周五";
                break;
            case 7:
                weekStr = "周六";
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
        if(Helper.isEmpty(datetime)){
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
        if(Helper.isEmpty(time)){
            return "";
        }
//		SimpleDateFormat   formatter   =   new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 Date   curDate   =   new   Date(System.currentTimeMillis());
        String   theDayTime   =   getNowTime();
        String befor = time.substring(0, 10);
        String beforTime = befor.replaceAll("-", "");
        String now = theDayTime.substring(0, 10);
        String nowTime = now.replaceAll("-", "");
        String dayTime = befor;
        if(isValidNumberType(beforTime) && isValidNumberType(nowTime)){
            if(Integer.valueOf(nowTime) - Integer.valueOf(beforTime) == 0){
                dayTime = ActivityHelper.getGlobalApplicationContext().getString(R.string.common_today);
            }else if((Integer.valueOf(nowTime) - Integer.valueOf(beforTime) == 1) || ((Integer.valueOf(nowTime) - Integer.valueOf(beforTime) >= 70) && (Integer.valueOf(nowTime) - Integer.valueOf(beforTime) <= 73))){
                dayTime = ActivityHelper.getGlobalApplicationContext().getString(R.string.common_yesterday);
            }
        }
        String resultTime = dayTime + " " + getTimeShortStr(time);
//		String resultTime = compareNow(time) + " " + getTimeShortStr(time);
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

        if(Helper.isEmpty(dateTime)){
            return "";
        }
        Date strtodate = String2Date(dateTime);
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        if(Helper.isNotNull(strtodate)){
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

        Date strToDate = String2Date(strDate);
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
        if (Helper.isNotNull(strInput)) {
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
    //endregion 日期时间相关

    //endregion public 方法
    /**
     * 将整型转换为布尔
     * @param val 0-false 非0=true
     * @return
     */
    public static boolean int2Boolean(int val) {
        return val != 0;
    }
    /**
     * 隐藏输入法
     * @param activity
     */
    public static void hideInputMethod(Activity activity){
        if(Helper.isNotNull(activity)){
            // 隐藏输入法
            InputMethodManager imm = (InputMethodManager) ActivityHelper.getGlobalApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 取得名片交换声音
     * @param representativeId 声音代表Id （详见：{@link Constants.SwapSound}）
     * @return
     */
    public static int getSwapCardResourceId(int representativeId){
        int result = 0;
        switch(representativeId){
            case Constants.SwapSound.SWAP_CARD_RESOURCE_ID_BEIJING_OPERA:
                result = R.raw.voice_swap_card_anim;
                break;
            case Constants.SwapSound.SWAP_CARD_RESOURCE_ID_SUNSET:
                result = R.raw.sunset_journey;
                break;
            case Constants.SwapSound.SWAP_CARD_RESOURCE_ID_SOUND_OFF:
                result = 0;
                break;
        }
        return result;
    }

    /**
     * 获取本机的提示音名字及其uri
     * @return 提示音名-对应的uri　键值对
     */
    public static HashMap<String,Uri> getRingtoneList(){
        int type = RingtoneManager.TYPE_NOTIFICATION;
        HashMap<String,Uri> msgSoundHashMap = new HashMap<String,Uri>();
        RingtoneManager manager = new RingtoneManager(ActivityHelper.getGlobalApplicationContext());
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        if(Helper.isNotNull(cursor) && cursor.moveToFirst()){
            do{
                String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
                Uri musicUri = Uri.withAppendedPath(Uri.parse(cursor.getString(RingtoneManager.URI_COLUMN_INDEX)),
                        cursor.getString(RingtoneManager.ID_COLUMN_INDEX));
                msgSoundHashMap.put(title, musicUri);
            }while(cursor.moveToNext());
        }
        return msgSoundHashMap;
    }



    /**
     * 设置控件是否可点击
     * @param txv
     * @param isEnable
     * @param colorId 字体颜色
     */
    public static void setTextViewEnable(TextView txv, boolean isEnable, int colorId){
        if(Helper.isNotNull(txv)){
            txv.setTextColor(sContext.getResources().getColor(colorId));
            txv.setEnabled(isEnable);
        }
    }

    /**
     * 拨打电话
     */
    public static void callPhone(Activity activity, String phone){

        if (phone.length() < 3){
            return ;
        }
        Uri uri = Uri.parse("tel:"+phone);
        Intent intent = new Intent(Intent.ACTION_CALL,uri);
        activity.startActivityForResult(intent, Constants.RequestCode.REQUEST_CODE_CALL);
    }

    /**
     * 发送邮件
     * @param emailInfo
     * @param mActivity
     */
    public static void sendEmail(EmailSendEntity emailInfo, Activity mActivity) {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.putExtra(Intent.EXTRA_EMAIL, emailInfo.getArrReceiver());
        if (Helper.isNotNull(emailInfo.getArrCc())) {
            mailIntent.putExtra(Intent.EXTRA_CC, emailInfo.getArrCc());
        }
        if (Helper.isNotNull(emailInfo.getArrBcc())) {
            mailIntent.putExtra(Intent.EXTRA_BCC, emailInfo.getArrBcc());
        }
        if (Helper.isNotEmpty(emailInfo.getSubject())) {
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, emailInfo.getSubject());
        }
        if (Helper.isNotEmpty(emailInfo.getBody())) {
            mailIntent.putExtra(Intent.EXTRA_TEXT, emailInfo.getBody());
        }
        if (Helper.isNotEmpty(emailInfo.getAttachPath())) {
            mailIntent.putExtra(Intent.EXTRA_STREAM,
                    Uri.parse(emailInfo.getAttachPath()));
        }
//		mailIntent.setType("application/octet-stream");
        mailIntent.setType("text/plain");
        mActivity.startActivity(Intent.createChooser(mailIntent, "请选择邮件发送软件"));
    }

    /**
     * 直接调用短信系统发送信息
     * @param phone
     * @param message
     * @param isInSMSLog
     */
    public static void sendSMS(String phone, String message, boolean isInSMSLog){
        if(ResourceHelper.isEmpty(phone)){
            ActivityHelper.showToast(R.string.sms_send_phone_empty);
            return;
        }
        if(ResourceHelper.isEmpty(message)){
            ActivityHelper.showToast(R.string.sms_send_msg_empty);
            return ;
        }
        Context curActivity = ActivityHelper.getGlobalApplicationContext();
        SmsManager smsMgr = SmsManager.getDefault();

        Intent sent = new Intent(Constants.ActionIntent.ACTION_INTENT_SMS_SEND);
        PendingIntent sentIntent = PendingIntent.getBroadcast(curActivity, 0, sent, 0);

        Intent delivery = new Intent(Constants.ActionIntent.ACTION_INTENT_SMS_DELIVERED);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(curActivity, 0, delivery , 0);

        try{
            // TODO 短信字符限制处理
            if(message.length() > 70){
                // 发送不含链接地址的消息
                int lenUrl = message.indexOf(Constants.Other.SMS_DOWNLOAD_SPLIT);
                if(lenUrl < 0){
                    lenUrl = message.length();
                }
                List<String> divideContent = smsMgr.divideMessage(message.substring(0,lenUrl));
                for (String text : divideContent) {
                    smsMgr.sendTextMessage(phone, null, text, sentIntent, deliveryIntent);
                }
                if(lenUrl < message.length()){
                    // 发送链接地址
                    String urlMsg = message.substring(lenUrl);
                    smsMgr.sendTextMessage(phone, null, urlMsg, sentIntent, deliveryIntent);
                }
            }else{
                smsMgr.sendTextMessage(phone, null, message, sentIntent, deliveryIntent);
            }
            if(isInSMSLog){
                SMSSendEntity smsEntity = new SMSSendEntity();
                smsEntity.setAddress(phone);
                smsEntity.setBody(message);
                smsEntity.setRead(0);
                smsEntity.setType(2);
                ContactBookDao.getInstance().insertSMS(smsEntity);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 取得头参Entity
     * @return
     */
    public static TerminalAgentEntity getTerminalAgentEntity(){
        TelephonyManager tm = (TelephonyManager) ActivityHelper.getGlobalApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String clientVerType = Helper.isNull(Build.TYPE) ? "null" : Build.DISPLAY;
        String clientVerNum = String.valueOf(ActivityHelper.getCurrentVersion());
        String clientChannelType = "null";
        String osType = tm.getDeviceSoftwareVersion();
        String menuVerNum = "null";
        String imei = Helper.isNull(tm.getDeviceId()) ? "null" : tm.getDeviceId();
        String imsi = tm.getSubscriberId();
        String mobile = Helper.isNull(tm.getLine1Number()) ? "null" : tm.getLine1Number();
        //手机品牌
        String operatorName = ResourceHelper.getProviderInfo(tm);
        String operatorNum = tm.getSubscriberId();
        String macAddress = ActivityHelper.getMacAddress();
        String clientId = CurrentUser.getInstance().getClientId();
        TerminalAgentEntity mTerminalAgentEntity = new TerminalAgentEntity(clientVerType, clientVerNum, clientChannelType, osType, menuVerNum, imsi, imei, mobile, operatorName, operatorNum, macAddress, clientId);
        return mTerminalAgentEntity;
    }

    /**
     * 获取手机服务提供商(中国移动,中国联通,中国电信) "460"表示国家,00,02表示移动,01表示联通,03表示电信
     *
     * @return
     */
    public static String getProviderInfo(TelephonyManager tm) {
        //判断sim卡状态
        int simState = tm.getSimState();
        String providerInfo = "";
        if(simState == TelephonyManager.SIM_STATE_READY){
            tm.getNetworkOperatorName();
        }
        String imsi = tm.getSubscriberId();
        if(Helper.isNull(imsi)){
            return providerInfo;
        }
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            providerInfo = "中国移动";
        } else if (imsi.startsWith("46001")) {
            providerInfo = "中国联通";
        } else if (imsi.startsWith("46003")) {
            providerInfo = "中国电信";
        }
        String result = "";
        try {
            result = new String(providerInfo.getBytes(), HTTP.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 复制
     * @param content
     */
    @SuppressWarnings("deprecation")
    public static void copyText(String content){
        ClipboardManager clip = (ClipboardManager) ActivityHelper.getGlobalApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(content);
    }

    /**
     * 图片转为byte[]
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bitmap2ByteArray(final Bitmap bmp, final boolean needRecycle) {
        if(Helper.isNull(bmp)){
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
