package com.maya.android.vcard.ui.widget;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 自定义DilogFragment弹框
 * Created by Administrator on 2015/8/12.
 */
public class CustomDialogFragment extends DialogFragment {
    /** 点击右边边按钮 **/
    public static final int BUTTON_POSITIVE = 1001;
    /** 点击左边按钮 **/
    public static final int BUTTON_NEGATIVE = 1002;
    /** 标题 **/
    private String mTitle;
    /** 对话框提示信息 **/
    private String mMessage;
    /** 左边按钮名称 **/
    private String mNegative;
    /** 右边按钮名称 **/
    private String mPositive;
    /** 对话框图标 **/
    private Drawable mIcon;
    /** 对话框宽 小于0 默认为系统给定宽 **/
    private int width = -1;
    /** 对话框高 小于0 默认为系统给定高 **/
    private int height = -1;
    /** 添加view与屏幕的高度比 **/
    private double size;
    /** 点击弹框外外围屏幕，弹框默认消失 **/
    private boolean isCanceled = true;
    /** 新内容View左右间距默认为15dp **/
    private boolean isPadding = true;
    /** 对方话框按钮默认显示 **/
    private boolean isSubmit = true;
    /** 默认添加view关闭高度设置 **/
    private boolean isContentViewHeight = false;
    /** 是否手动控制弹窗消失 **/
    private boolean isManualDismiss = false;
    private TextView mTxvTitle,mTxvMessage;//标题和正文控件
    private Button mBtnNegative, mBtnPositive;//按钮控件
    private ImageView mImvIcon;//对话框图标控件
    private LinearLayout mLilContentView;//只设置在此容器中科替换其中的ChildView
    private View mViewVerticalDivider;//两个按钮之间的分割线
    private View mContentView;//新的view用于替换原来的view
    private LinearLayout mLilSubmit;//对话框下方的按钮
    private View mViewSubmitDivider;//对方框和正文内容之间的分割线
    private DialogFragmentInterface mListener;
    private DialogInterface.OnKeyListener mOnKeyListener;
    /**
     * 生成新的CustomDialogFrgment
     * @return
     */
    public static CustomDialogFragment newInstance(){
        CustomDialogFragment dlgFragment = new CustomDialogFragment();
        return dlgFragment;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_dialog_negative:
                    //左边按钮
                    if(ResourceHelper.isNotNull(mListener)){
                        mListener.onDialogClick(BUTTON_NEGATIVE);
                    }
                    break;
                case R.id.btn_dialog_positive:
                    //右边按钮
                    if (Helper.isNotNull(mListener)) {
                        mListener.onDialogClick(BUTTON_POSITIVE);
                    }
                    break;
            }
            if(!isManualDismiss){
                getDialog().dismiss();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //去掉原始标题栏
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            View rootView = inflater.inflate(R.layout.dialog_frgment_custom, container, false);
            this.mTxvTitle = (TextView) rootView.findViewById(R.id.txv_dialog_title);
            this.mImvIcon = (ImageView) rootView.findViewById(R.id.imv_dialog_icon);
            this.mLilContentView = (LinearLayout) rootView.findViewById(R.id.lil_dialog_content);
            this.mBtnNegative = (Button) rootView.findViewById(R.id.btn_dialog_negative);
            this.mBtnPositive = (Button) rootView.findViewById(R.id.btn_dialog_positive);
            this.mViewVerticalDivider = rootView.findViewById(R.id.dlg_custom_line);
            this.mLilSubmit = (LinearLayout) rootView.findViewById(R.id.lil_dialog_submit);
            this.mViewSubmitDivider = rootView.findViewById(R.id.view_dialog_line);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initData(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ResourceHelper.isNotNull(getDialog()) ) {
            //设置对方框背景透明
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置点击屏幕，对话框消失
            getDialog().setCanceledOnTouchOutside(this.isCanceled);
            //设置按键监听
            if(Helper.isNotNull(this.mOnKeyListener)) {
                getDialog().setOnKeyListener(this.mOnKeyListener);
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(ResourceHelper.isNotNull(getDialog())){
            Window dialogWindow = getDialog().getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            if (this.width > 0){
                lp.width = this.width;
            }
            if (this.height > 0){
                lp.height = this.height;
             }
            dialogWindow.setAttributes(lp);
        }
     }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.mLilContentView.removeAllViews();
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public CustomDialogFragment setTitle(String title){
        this.mTitle = title;
        return this;
    }

    /**
     * 设置标题
     * @param titleId
     * @return
     */
    public CustomDialogFragment setTitle(int titleId){
        this.mTitle = ActivityHelper.getGlobalApplicationContext().getString(titleId);
        return this;
    }

    /**
     * 设置提示信息
     * @param message
     * @return
     */
    public CustomDialogFragment setMessage(String message){
        this.mMessage = message;
        return this;
    }

    /**
     * 设置提示信息
     * @param messageId
     * @return
     */
    public CustomDialogFragment setMessage(int messageId){
        this.mMessage = ActivityHelper.getGlobalApplicationContext().getString(messageId);
        return this;
    }

    /**
     * 设置左按钮
     * @param test
     * @return
     */
    public CustomDialogFragment setNegativeButton(String test){
        this.mNegative = test;
        return this;
    }
    /**
     * 设置左按钮
     * @param testId
     * @return
     */
    public CustomDialogFragment setNegativeButton(int testId){
        if(testId != 0){
            this.mNegative  = ActivityHelper.getGlobalApplicationContext().getString(testId);
        }
        return this;
    }

    /**
     * 设置右边按钮
     * @param test
     * @return
     */
    public CustomDialogFragment setPositiveButton(String test){
        this.mPositive  = test;
        return this;
    }

    /**
     * 设置右边按钮
     * @param testId
     * @return
     */
    public CustomDialogFragment setPositiveButton(int testId){
        if(0 != testId ) {
            this.mPositive = ActivityHelper.getGlobalApplicationContext().getString(testId);
        }
        return this;
    }

    /**
     * 设置标题图标
     * @param resourceId 标题图片Id
     * @return
     */
    @SuppressLint("NewApi")
    public CustomDialogFragment setIcon(int resourceId){
        this.mIcon = ActivityHelper.getGlobalApplicationContext().getDrawable(resourceId);
        return this;
    }

    /**
     * 设置标题图标 默认左右间距为15dp
     * @param icon 标题图片
     * @return
     */
    public CustomDialogFragment setIcon(Drawable icon){
        this.mIcon = icon;
        return this;
    }

    public CustomDialogFragment setContentView(View contentview){
        this.mContentView = contentview;
        return this;
    }

    /**
     * 设置弹框中展示内容需要的View
     * @param contentview
     * @param isPadding 是否默认左右间距为15dp
     * @return
     */
    public CustomDialogFragment setContentView(View contentview, boolean isPadding){
        this.mContentView = contentview;
        this.isPadding = isPadding;
        return this;
    }

    /**
     * 设置点击弹框外屏幕，弹框消失
     * @param isCanceled
     * @return
     */
    public CustomDialogFragment setCanceledOutSize(boolean isCanceled){
        this.isCanceled = isCanceled;
        return this;
    }

    /**
     * 是否手动 控件弹窗关闭
     * @param isManualDismiss
     * @return
     */
    public CustomDialogFragment setManualDismiss(boolean isManualDismiss){
        this.isManualDismiss = isManualDismiss;
        return this;
    }

    /**
     * 设置按键监听
     * @param onKeyListener
     * @return
     */
    public CustomDialogFragment setOnKeyListener(DialogInterface.OnKeyListener onKeyListener){
        if(Helper.isNotNull(onKeyListener)){
            this.mOnKeyListener = onKeyListener;
        }
        return this;
    }

    /**
     * 是否显示按钮
     * @param showSubmit
     * @return
     */
    public CustomDialogFragment setSubmitisShow(boolean showSubmit){
        this.isSubmit = showSubmit;
        return this;
    }

    /**
     * 对话框 宽高
     * @param width 宽
     * @param height 高
     * @return
     */
    public CustomDialogFragment setLayoutParam(int width, int height){
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     *  设置弹框内容的View的高度
     * @param isContentViewHeight 是否按高度比设置
     * @param size 屏幕高度的比例
     * @return
     */
    public CustomDialogFragment setContentViewHeight(boolean isContentViewHeight, double size){
        this.isContentViewHeight = isContentViewHeight;
        this.size = size;
        return this;
    }

   /**
     * 接口监听（如果需要监听按钮点击事件，则必须调用此接口）
     * @param mlistener
     * @return
     */
    public CustomDialogFragment setDialogFragmentInterfaceListener(DialogFragmentInterface mlistener){
        this.mListener = mlistener;
        return this;
    }

     /**
     * 回调监听接口
     */
    public interface DialogFragmentInterface{
         void onDialogClick(int which);
    }

    /**
     * 修改取消按钮的内容(本方法只能在初始化后执行调用)
     * @param content
     */
    public void setButtonsNegative(String content){
        if(ResourceHelper.isNotNull(this.mBtnNegative)){
            this.mBtnNegative.setText(content);
        }
    }

    /**
     * 修改确定按钮的内容(本方法只能在初始化后执行调用)
     * @param content
     */
    public void setIsAddedPositiveButton(String content){
        if(ResourceHelper.isNotNull(this.mBtnPositive)){
            this.mBtnPositive.setText(content);
        }
    }

    /**
     * 初始化数据加载
     */
    private void initData(View view){
        if(ResourceHelper.isNotNull(this.mTitle)){//标题
          this.mTxvTitle.setText(this.mTitle);
         }
        if(isSubmit){//true,显示按钮及分割线，false 隐藏按钮及分割线
            this.mLilSubmit.setVisibility(View.VISIBLE);
            this.mViewSubmitDivider.setVisibility(View.VISIBLE);
            if(ResourceHelper.isNotNull(this.mNegative)){//左边按钮
                this.mBtnNegative.setText(this.mNegative);
                this.mBtnNegative.setVisibility(View.VISIBLE);
                this.mViewVerticalDivider.setVisibility(View.VISIBLE);
                this.mBtnNegative.setOnClickListener(this.mOnClickListener);
            }else{
                this.mBtnNegative.setVisibility(View.GONE);
                this.mViewVerticalDivider.setVisibility(View.GONE);
            }
            if(Helper.isNotNull(this.mPositive)){//右边按钮
                this.mBtnPositive.setText(this.mPositive);
                this.mBtnPositive.setVisibility(View.VISIBLE);
                this.mBtnPositive.setOnClickListener(this.mOnClickListener);
            }
        }else {
            this.mLilSubmit.setVisibility(View.GONE);
            this.mViewSubmitDivider.setVisibility(View.GONE);
        }

        if(ResourceHelper.isNotNull(this.mIcon)){//Icon 图标
            this.mImvIcon.setImageDrawable(this.mIcon);
        }
        if(ResourceHelper.isNotNull(this.mContentView)){//需要展示的view
            this.mLilContentView.removeAllViews();
            this.mLilContentView.addView(this.mContentView);
            if(this.isPadding){
                //默认新添加View的左右间距为15dp
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mContentView.getLayoutParams();
                params.leftMargin = (int)getResources().getDimension(R.dimen.dimen_15dp);
                params.rightMargin = (int)getResources().getDimension(R.dimen.dimen_15dp);
                if(this.isContentViewHeight){
                    params.height = (int)(ActivityHelper.getScreenHeight() * this.size);

                }
                this.mContentView.setLayoutParams(params);
            }else  if(this.isContentViewHeight){
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.mContentView.getLayoutParams();
                params.height = (int)(ActivityHelper.getScreenHeight() * this.size);
                this.mContentView.setLayoutParams(params);
            }

        }else if(ResourceHelper.isNotNull(this.mMessage)){//需要展示的文本信息
                 this.mTxvMessage  = (TextView) view.findViewById(R.id.txv_dialog_message);
                 this.mTxvMessage.setText(this.mMessage);
        }
    }


}
