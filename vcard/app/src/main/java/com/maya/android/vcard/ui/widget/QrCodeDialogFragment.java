package com.maya.android.vcard.ui.widget;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 二维码图片弹窗
 * Created by Administrator on 2015/9/22.
 */
public class QrCodeDialogFragment extends DialogFragment {
    /**弹窗宽度**/
    private int width;//默认系统宽高
    /**弹窗高度**/
    private int height;//默认系统宽高
    private AsyncImageView mImvHead, mImvQrScan;
    private ImageView mImvQrCodeLoading;
    private TextView mTxvName, mTxvJob, mTxvCompany;
    private String qrCodeName, qrCodeJob, qrCodeCompany, qrCodeHeadImg, qrCodeUrl;
    /**点击屏幕，弹框是否消失**/
    private boolean isCanceled = true;//点击弹框外外围屏幕，弹框默认消失
    /**返回键监听**/
    private DialogInterface.OnKeyListener mOnKeyListener;

    public static QrCodeDialogFragment newInstance() {
        QrCodeDialogFragment dlgFragment = new QrCodeDialogFragment();
        return dlgFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //去掉原始标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_fragment_qr_code, container, false);
        this.mImvHead = (AsyncImageView) view.findViewById(R.id.imv_head);
        this.mImvQrScan =(AsyncImageView) view.findViewById(R.id.imv_qr_code_scan);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_name);
        this.mTxvJob = (TextView) view.findViewById(R.id.txv_job);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_company);
        this.mImvQrCodeLoading = (ImageView) view.findViewById(R.id.imv_qr_code_loading);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
        ResourceHelper.asyncImageViewFillUrl(this.mImvHead, this.qrCodeHeadImg);
        this.mTxvName.setText(this.qrCodeName);
        this.mTxvJob.setText(this.qrCodeJob);
        this.mTxvCompany.setText(this.qrCodeCompany);
        if(ResourceHelper.isNotNull(this.qrCodeUrl)){
            AsyncImageView.ImageLoadListener imageLoadListener = new AsyncImageView.ImageLoadListener(){

                AnimationDrawable mDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.qrcode_anim_loading);

                @Override
                public void imageLoading() {
                    mImvQrCodeLoading.setImageDrawable(mDrawable);
                    mDrawable.start();
//                    mImvQrScan.setVisibility(View.GONE);
                }

                @Override
                public void imageLoadFinish() {
                    mDrawable.stop();
                    mImvQrCodeLoading.setVisibility(View.GONE);
//                    mImvQrScan.setVisibility(View.VISIBLE);
                }

                @Override
                public void imageLoadFail() {
                    mDrawable.stop();
                    mImvQrCodeLoading.setVisibility(View.GONE);
                    mImvQrScan.setImageResource(R.mipmap.img_qrcode_load_fail);
                    mImvQrScan.setVisibility(View.VISIBLE);
                }

            };
            this.mImvQrScan.setImageLoadListener(imageLoadListener);
            this.mImvQrScan.autoFillFromUrl(this.qrCodeUrl);
        }
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
     * 对话框 宽高
     * @param width 宽
     * @param height 高
     * @return
     */
    public QrCodeDialogFragment setLayoutParam(int width, int height){
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 头像
     * @param headImg
     * @return
     */
    public QrCodeDialogFragment setHeadImg(String headImg){
        this.qrCodeHeadImg = headImg;
        return this;
    }

    /*** @param resId
     * @return 姓名
     */
    public QrCodeDialogFragment setName(int resId){
        if(resId > 0){
            this.qrCodeName = ActivityHelper.getGlobalApplicationContext().getResources().getString(resId);
        }
       return this;
    }

    /*** @param resId
     * @return 姓名
     */
    public QrCodeDialogFragment setName(String userName){
        if(ResourceHelper.isNotEmpty(userName)){
            this.qrCodeName = userName;
        }
        return this;
    }

    /*** @param resId
     * @return 职业
     */
    public QrCodeDialogFragment setJob(int resId){
        if(resId > 0){
            this.qrCodeJob = ActivityHelper.getGlobalApplicationContext().getResources().getString(resId);
        }
        return this;
    }

    /*** @param resId
     * @return 职业
     */
    public QrCodeDialogFragment setJob(String userJob){
        if(ResourceHelper.isNotEmpty(userJob)){
            this.qrCodeJob = userJob;
        }
        return this;
    }

    /*** @param resId
     * @return 公司
     */
    public QrCodeDialogFragment setCompany(int resId){
        if(resId > 0){
            this.qrCodeCompany = ActivityHelper.getGlobalApplicationContext().getResources().getString(resId);
        }
        return this;
    }

    /*** @param resId
     * @return 公司
     */
    public QrCodeDialogFragment setCompany(String userCompany){
        if(ResourceHelper.isNotEmpty(userCompany)){
            this.qrCodeCompany = userCompany;
        }
        return this;
    }

    /**
     * 生成二维码的链接
     * @param url
     * @return
     */
    public QrCodeDialogFragment setQrCodeUrl(String url){
        this.qrCodeUrl = url;
        return this;
    }

    /**
     * 设置点击弹框外屏幕，弹框消失
     * @param isCanceled
     * @return
     */
    public QrCodeDialogFragment setCanceledOutSize(boolean isCanceled){
        this.isCanceled = isCanceled;
        return this;
    }




    /**
     * 设置按键监听
     * @param onKeyListener
     * @return
     */
    public QrCodeDialogFragment setOnKeyListener(DialogInterface.OnKeyListener onKeyListener){
        if(Helper.isNotNull(onKeyListener)){
            this.mOnKeyListener = onKeyListener;
        }
        return this;
    }
}
