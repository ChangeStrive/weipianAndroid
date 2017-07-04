package com.maya.android.vcard.ui.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 显示大图
 * Created by Administrator on 2015/10/13.
 */
public class UserHeadMaxDialogFragment extends DialogFragment{
    /** 默认系统宽 **/
    private int width = -1;
    /** 默认系统高 **/
    private int height = -1;
    private String negative, positive;
    private String headImg;
    /** 点击弹框外外围屏幕，弹框默认消失 **/
    private boolean isCanceled = true;
    private DialogInterface.OnKeyListener mOnKeyListener;
    private AsyncImageView mImvHeadMax;
    private Button mBtnNegative, mBtnPositive;
    private View mViewDivider;
    private LinearLayout mLilSubmit;

    public static UserHeadMaxDialogFragment newInstance(){
        UserHeadMaxDialogFragment dialog = new UserHeadMaxDialogFragment();
        return dialog;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_dialog_positive:
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                    break;
                case R.id.btn_dialog_negative:
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                    break;
            }
             getDialog().dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_user_head_max, container, false);
        this.mImvHeadMax = (AsyncImageView) view.findViewById(R.id.imv_picture);
        this.mBtnNegative = (Button) view.findViewById(R.id.btn_dialog_negative);
        this.mBtnPositive = (Button) view.findViewById(R.id.btn_dialog_positive);
        this.mViewDivider = (View) view.findViewById(R.id.dlg_custom_line);
        this.mLilSubmit = (LinearLayout) view.findViewById(R.id.lil_dialog_submit);
        this.mBtnPositive.setOnClickListener(this.mOnClickListener);
        this.mBtnNegative.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ResourceHelper.isNotEmpty(this.negative)){
            this.mBtnNegative.setVisibility(View.VISIBLE);
            this.mViewDivider.setVisibility(View.VISIBLE);
            this.mBtnNegative.setText(this.negative);
        }else{
            this.mBtnNegative.setVisibility(View.GONE);
            this.mViewDivider.setVisibility(View.GONE);
        }
        if(ResourceHelper.isNotEmpty(this.positive)){
            this.mBtnPositive.setVisibility(View.VISIBLE);
            this.mBtnPositive.setText(this.positive);
        }
        if(ResourceHelper.isEmpty(this.negative) && ResourceHelper.isEmpty(this.positive)){
            this.mLilSubmit.setVisibility(View.GONE);
        }else{
            this.mLilSubmit.setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mImvHeadMax.getLayoutParams();
        params.width = ActivityHelper.getScreenWidth() * 2/3;
        params.height = ActivityHelper.getScreenWidth() * 2/3;
        this.mImvHeadMax.setLayoutParams(params);
        this.mImvHeadMax.setDefaultImageResId(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(this.mImvHeadMax, this.headImg);

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

    /**
     * 设置点击屏幕，对话框消失
     * @param isCanceled true消失 false 不消失
     * @return
     */
    public UserHeadMaxDialogFragment setCanceledOnTouchOutside(boolean isCanceled){
        this.isCanceled = isCanceled;
        return this;
    }

    /**
     * 设置弹窗宽高
     * @param width
     * @param height
     * @return
     */
    public UserHeadMaxDialogFragment setWidthAndHeight(int width, int height){
        if(width > 0){
            this.width = width;
        }
        if(height > 0){
            this.height = height;
        }
        return this;
    }

    /**
     * 设置图像地址
     * @param headImg
     * @return
     */
    public UserHeadMaxDialogFragment setHeadImg(String headImg){
        this.headImg = headImg;
        return this;
    }

    /**
     * 左边按钮
     * @param negative
     * @return
     */
    public UserHeadMaxDialogFragment setNeagtive(String negative){
        this.negative = negative;
        return this;
    }

    /**
     * 左边按钮
     * @param negativeId
     * @return
     */
    public UserHeadMaxDialogFragment setNeagtive(int negativeId){
        if(negativeId > 0){
            this.negative = ActivityHelper.getGlobalApplicationContext().getString(negativeId);
        }
        return this;
    }

    /**
     * 右边按钮
     * @param positive
     * @return
     */
    public UserHeadMaxDialogFragment setPositive(String positive){
        this.positive = positive;
        return this;
    }

    /**
     * 右边按钮
     * @param positiveId
     * @return
     */
    public UserHeadMaxDialogFragment setPositive(int positiveId){
        if(positiveId > 0){
            this.positive = ActivityHelper.getGlobalApplicationContext().getString(positiveId);
        }
        return this;
    }


}
