package com.maya.android.vcard.util;

import android.graphics.Bitmap;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.result.URLResultEntity;

/**
 * 项目Helper类
 * Created by YongJi on 2015/8/18.
 */
public class ProjectHelper {

    //region 请求地址相关
    /** 是否为调试URL **/
    public static boolean isDebugOfUrl = Constants.URL.URL_DEBUG_MODE;
    /**
     * 拼接请求地址
     * @param partUrl URLResultEntity中的部分URL部分
     * @return
     */
    public static String fillRequestURL(String partUrl){
        String result = null;
        if(Helper.isNotEmpty(partUrl)){
            String urlServiceBase = null;
            if(Helper.isNotEmpty(Constants.UmengOnlineKeyAndValue.VALUE_UMENG_URL_CURRENT_SERVICE) && !isDebugOfUrl){
                urlServiceBase = Constants.UmengOnlineKeyAndValue.VALUE_UMENG_URL_CURRENT_SERVICE;
            }else{
                urlServiceBase = Constants.URL.URL_VCARD_SERVICE;
            }
//			Log.i("Test", "Constant.UmengOnlineKeyAdValue.VALUE_UMENG_URL_CURRENT_SERVICE:" + Constant.UmengOnlineKeyAndValue.VALUE_UMENG_URL_CURRENT_SERVICE
//					+ " result:" + urlServiceBase);
            result = urlServiceBase.concat(partUrl);
        }
        return result;
    }
    /**
     * 拼接交换等请求地址
     * @param partUrl
     * @return
     */
    public static String fillSwapRequestURL(String partUrl){
        String result = null;
        if(Helper.isNotEmpty(partUrl)){
            URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
            if(Helper.isNotNull(urlEntity)){
                String swapBaseService = urlEntity.getSwapBaseService();
                if(Helper.isNotEmpty(swapBaseService)){
                    result = swapBaseService + partUrl;
                }else{
                    result = Constants.URL.URL_VCARD_SWAP_BASE_SERVICE + partUrl;
                }
            }
        }
        return result;
    }
    //endregion 请求地址相关

    /**
     * 计算名片格式(名片类型（0为不确定类型、1为90*54、2为90*45、3为90*94 ）)
     * @param bitmap
     * @return
     */
    public static int getVCardForm(Bitmap bitmap){
        int cardForm = Constants.Card.CARD_FORM_OTHER;
        if(Helper.isNotNull(bitmap)){
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            return getVCardForm(width, height);
        }
        return cardForm;
    }
    /**
     * 根据宽高 获取名片类型
     * @param width
     * @param height
     * @return
     */
    public static int getVCardForm(int width, int height){
        int cardForm = Constants.Card.CARD_FORM_OTHER;
        if(height > width){
            int temp = height;
            height = width;
            width = temp;
        }
        float scaleBitmap = width * 1.0f / height;
        if(scaleBitmap <= 1.2f){
            cardForm = Constants.Card.CARD_FORM_9094;
        }else if(1.2f < scaleBitmap && scaleBitmap <= 1.8f){
            cardForm = Constants.Card.CARD_FORM_9054;
        }else if(1.8f < scaleBitmap && scaleBitmap <= 2.1f){
            cardForm = Constants.Card.CARD_FORM_9045;
        }
        return cardForm;
    }

}
