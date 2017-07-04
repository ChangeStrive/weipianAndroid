package com.maya.android.vcard.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.QrCodeDialogFragment;
import com.maya.android.vcard.ui.widget.UserHeadMaxDialogFragment;

import java.util.ArrayList;

/**
 * dialogFragment弹窗帮助类
 * Created by Administrator on 2015/8/13.
 */
public class DialogFragmentHelper {



    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, String positive){
        return showCustomDialogFragment(title, message, null, positive, getCustomDialogFragmentWidth(), -1, null);
    }

    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, String positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, null, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, String negative, String positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, negative, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

     /**
     * 默认宽左右间隔30dp
     * @param title 标题
     * @param message 内容
     * @param negative 左边按钮名称
     * @param positive
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, String negative, String positive, int widht, int height, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance();
        dialogFragment.setTitle(title);
        dialogFragment.setMessage(message);
        dialogFragment.setNegativeButton(negative);
        dialogFragment.setPositiveButton(positive);
        dialogFragment.setDialogFragmentInterfaceListener(dialogOnClick);
        dialogFragment.setLayoutParam(widht, height);
        return dialogFragment;
    }

    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, int positive){
        return showCustomDialogFragment(title, message, 0, positive, getCustomDialogFragmentWidth(), -1, null);
    }

    /**
     * 默认宽高30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, 0, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, int negative, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, negative, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     *
     * @param title 标题
     * @param message 内容
     * @param negative 左边按钮名称
     * @param positive
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, String message, int negative, int positive, int width, int height, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance();
        dialogFragment.setTitle(title);
        dialogFragment.setMessage(message);
        dialogFragment.setNegativeButton(negative);
        dialogFragment.setPositiveButton(positive);
        dialogFragment.setDialogFragmentInterfaceListener(dialogOnClick);
        dialogFragment.setLayoutParam(width, height);
        return dialogFragment;
    }



    /**
     * 默认宽左右间隔30dp
     * @param title
     * @param message
     * @param positive
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, String message, int positive){
        return showCustomDialogFragment(title, message, 0, positive, getCustomDialogFragmentWidth(), -1, null);
    }
    /**
     * 默认宽左右间隔30dp
     * @param title
     * @param message
     * @param positive
     * @param dialogOnClick
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, String message, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, 0, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     * 默认宽左右间隔30dp
     * @param title
     * @param message
     * @param negative
     * @param positive
     * @param dialogOnClick
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, String message, int negative, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, negative, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     *
     * @param title 标题
     * @param message 内容
     * @param negative 左边按钮名称
     * @param positive 右边按钮名片
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, String message, int negative, int positive, int width, int height, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance();
        dialogFragment.setTitle(title);
        dialogFragment.setMessage(message);
        dialogFragment.setNegativeButton(negative);
        dialogFragment.setPositiveButton(positive);
        dialogFragment.setDialogFragmentInterfaceListener(dialogOnClick);
        dialogFragment.setLayoutParam(width, height);
        return dialogFragment;
    }

    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, int message, int positive){
        return showCustomDialogFragment(title, message, 0, positive, getCustomDialogFragmentWidth(), -1, null);
    }

    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, int message, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, 0, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     * 默认宽左右间隔30dp
     * @param title  标题
     * @param message 内容
     * @param positive 单个按钮名称
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, int message, int Negetive, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, message, Negetive, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     *
     * @param title 标题
     * @param message 内容
     * @param negative 左边按钮名称
     * @param positive 右边按钮名片
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, int message, int negative, int positive, int width, int height, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance();
        dialogFragment.setTitle(title);
        dialogFragment.setMessage(message);
        dialogFragment.setNegativeButton(negative);
        dialogFragment.setPositiveButton(positive);
        dialogFragment.setDialogFragmentInterfaceListener(dialogOnClick);
        dialogFragment.setLayoutParam(width, height);
        return dialogFragment;
    }


    /**
     * 默认宽左右间隔30dp
     * @param title 标题
     * @param mContentView 要替换的View
     * @param positive 右边按钮名片
     * @param dialogOnClick 接口监听
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, View mContentView, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        return showCustomDialogFragment(title, mContentView, 0, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }

    /**
     * 默认宽左右间隔30dp
     * @param title 标题
     * @param mContentView 要替换的View
     * @param negative 左边按钮名称
     * @param positive 右边按钮名片
     * @param dialogOnClick 接口监听
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, View mContentView, int negative, int positive, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
         return showCustomDialogFragment(title, mContentView, negative, positive, getCustomDialogFragmentWidth(), -1, dialogOnClick);
    }
    /**
     *
     * @param title 标题
     * @param mContentView 要替换的View
     * @param negative 左边按钮名称
     * @param positive 右边按钮名片
     * @param dialogOnClick 接口监听
     * @param width 弹窗宽度
     * @param height 弹窗高度
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, View mContentView, int negative, int positive, int width, int height, CustomDialogFragment.DialogFragmentInterface dialogOnClick){
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance();
        dialogFragment.setTitle(title);
        dialogFragment.setContentView(mContentView);
        dialogFragment.setNegativeButton(negative);
        dialogFragment.setPositiveButton(positive);
        dialogFragment.setDialogFragmentInterfaceListener(dialogOnClick);
        dialogFragment.setLayoutParam(width, height);
        return dialogFragment;
    }

    /**
     * 默认没有底部按钮(默认宽高)
     * @param title 标题
     * @param mContentView 要替换的View
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, View mContentView){
        return showCustomDialogFragment(title, mContentView, getCustomDialogFragmentWidth(), -1);
    }

    /**
     * 默认没有底部按钮(默认宽高)
     * @param title  标题
     * @param mContentView 要替换的View
     * @param setHeight 是否启用最大高度
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, View mContentView, boolean setHeight){
        if(setHeight){
            return showCustomDialogFragment(title, mContentView, getCustomDialogFragmentWidth(), getCustomDialogFragmentHeight());
        }
        return showCustomDialogFragment(title, mContentView, getCustomDialogFragmentWidth(), -1);
    }

    /**
     *
     * @param title  title 标题
     * @param mContentView 要替换的View
     * @param width 对话框 宽
     * @param height 对话框 高
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(String title, View mContentView, int width, int height){
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance();
        dialogFragment.setTitle(title);
        dialogFragment.setContentView(mContentView);
        dialogFragment.setSubmitisShow(false);
        dialogFragment.setLayoutParam(width, height);
        return dialogFragment;
    }

    /**
     * 默认没有底部按钮(默认宽高)
     * @param title 标题
     * @param mContentView 要替换的View
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, View mContentView){
        return showCustomDialogFragment(title, mContentView, getCustomDialogFragmentWidth(), -1);
    }

    /**
     * 默认没有底部按钮(默认宽高)
     * @param title  标题
     * @param mContentView 要替换的View
     * @param setHeight 是否启用最大高度
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, View mContentView, boolean setHeight){
        if(setHeight){
            return showCustomDialogFragment(title, mContentView, getCustomDialogFragmentWidth(), getCustomDialogFragmentHeight());
        }
        return showCustomDialogFragment(title, mContentView, getCustomDialogFragmentWidth(), -1);
    }

    /**
     *
     * @param title  title 标题
     * @param mContentView 要替换的View
     * @param width 对话框 宽
     * @param height 对话框 高
     * @return
     */
    public static CustomDialogFragment showCustomDialogFragment(int title, View mContentView, int width, int height){
        CustomDialogFragment dialogFragment = CustomDialogFragment.newInstance();
        dialogFragment.setTitle(title);
        dialogFragment.setContentView(mContentView);
        dialogFragment.setSubmitisShow(false);
        dialogFragment.setLayoutParam(width, height);
        return dialogFragment;
    }


    /**
     * 获取已定义ListView
     * @param mContext
     * @return
     */
    public static ListView getCustomContentView(Context mContext){
        ListView mLsvItem =(ListView)  LayoutInflater.from(mContext).inflate(R.layout.dialog_frgment_custom_lsv, null);
        mLsvItem.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return mLsvItem;
    }

    /**
     * 显示二维码对话框
     * @param userName 姓名
     * @param userJob 职业
     * @param userCompany 公司
     * @param headImg 头像
     * @param qrCodeUrl 生成二维码链接
     * @return
     */
    public static QrCodeDialogFragment showQrCodeDialogFragment(String userName, String userJob, String userCompany, String headImg, String qrCodeUrl){
        return showQrCodeDialogFragment(userName, userJob, userCompany, headImg, qrCodeUrl, getCustomDialogFragmentWidth(), 0, true);
    }

    /**
     * 显示二维码对话框
     * @param userName 姓名
     * @param userJob 职业
     * @param userCompany 公司
     * @param headImg 头像
     * @param qrCodeUrl 生成二维码链接
     * @param width 对话框 宽
     * @param height 对话框 高
     * @return
     */
    public static QrCodeDialogFragment showQrCodeDialogFragment(String userName, String userJob, String userCompany, String headImg, String qrCodeUrl, int width, int height, boolean isCancel){
        QrCodeDialogFragment dialog =  QrCodeDialogFragment.newInstance();
        dialog.setName(userName).setJob(userJob).setCompany(userCompany).setHeadImg(headImg).setQrCodeUrl(qrCodeUrl).setLayoutParam(width, height).setCanceledOutSize(isCancel);
        return dialog;
    }

    /**
     * 显示图像大图
     * @param headImg
     * @return
     */
    public static UserHeadMaxDialogFragment showUserHeadMaxDialogFragment(String headImg){
        return showUserHeadMaxDialogFragment(headImg, -1);
    }

    /**
     * 显示图像大图
     * @param headImg
     * @param positiveId
     * @return
     */
    public static UserHeadMaxDialogFragment showUserHeadMaxDialogFragment(String headImg, int positiveId){
        return showUserHeadMaxDialogFragment(headImg, -1, positiveId);
    }

    /**
     * 显示图像大图
     * @param headImg
     * @param negativeId
     * @param positiveId
     * @return
     */
    public static UserHeadMaxDialogFragment showUserHeadMaxDialogFragment(String headImg, int negativeId, int positiveId){
        return showUserHeadMaxDialogFragment(headImg, negativeId, positiveId, ActivityHelper.getScreenWidth() * 2/3 , -1);
    }

    /**
     * 显示图像大图
     * @param headImg
     * @param negativeId
     * @param positiveId
     * @param width
     * @param height
     * @return
     */
    public static UserHeadMaxDialogFragment showUserHeadMaxDialogFragment(String headImg, int negativeId, int positiveId, int width, int height){
        UserHeadMaxDialogFragment dialog = UserHeadMaxDialogFragment.newInstance();
        dialog.setHeadImg(headImg).setNeagtive(negativeId).setPositive(positiveId).setWidthAndHeight(width, height);
         return dialog;
    }

    /**
     * 默认对话框宽度
     * @return
     */
    public static int getCustomDialogFragmentWidth(){
        return (ActivityHelper.getScreenWidth() - (int)ActivityHelper.getGlobalApplicationContext().getResources().getDimension(R.dimen.dimen_60dp));
    }

    /**
     *默认最大对话框高度
     * @return
     */
    public static int getCustomDialogFragmentHeight(){
        return (ActivityHelper.getScreenHeight() - (int)ActivityHelper.getGlobalApplicationContext().getResources().getDimension(R.dimen.dimen_60dp));
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


}
