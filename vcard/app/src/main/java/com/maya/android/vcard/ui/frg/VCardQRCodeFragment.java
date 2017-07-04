package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.ScanQrCodeActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.UserHeadMaxDialogFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 二维码名片
 */
public class VCardQRCodeFragment extends BaseFragment {
    private static final String TAG = VCardQRCodeFragment.class.getSimpleName();
    private AsyncImageView mImvHead, mImvQrcodeScan;
    private ImageView mImvVip, mImvGrade;
    private TextView mTxvName, mTxvPosition, mTxvCompany;
    private Button mBtnSubmit;
    private ImageView mImvQrcodeLoading;
    private CardEntity mCurrentVCardEntity;
    private UserInfoResultEntity mUserInfo;
    private UserHeadMaxDialogFragment mDialogFragmentHeadMax;
    /** 当前用户头像 **/
    private String mCurHeadPath;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    // 扫描二维码
                    Intent intent = new Intent();
                    intent.putExtra(ScanQrCodeActivity.IS_NEED_SWITCH_TO_SHOW_QR_CODE_FRG, false);
                    ActivityHelper.switchTo(getActivity(), ScanQrCodeActivity.class, intent);
                    break;
                case R.id.imv_head:
                    //显示大图
                    showDialogFragmentHeadMax(mCurHeadPath);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vcard_qrcode, container, false);
        this.mImvHead = (AsyncImageView) view.findViewById(R.id.imv_head);
        this.mImvQrcodeScan = (AsyncImageView) view.findViewById(R.id.imv_qrcode_scan);
        this.mImvQrcodeLoading = (ImageView) view.findViewById(R.id.imv_qrcode_loading);
        this.mImvVip = (ImageView) view.findViewById(R.id.imv_qrcode_vip);
        this.mImvGrade = (ImageView) view.findViewById(R.id.imv_qrcode_grade);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_name);
        this.mTxvPosition = (TextView) view.findViewById(R.id.txv_position);
        this.mTxvCompany = (TextView) view.findViewById(R.id.txv_company);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mImvHead.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTitle(R.string.qr_code_card, true);
        initData();
    }

    private void initData(){
        this.mUserInfo = CurrentUser.getInstance().getUserInfoEntity();
        if(ResourceHelper.isNotNull(this.mUserInfo)){
            this.mTxvName.setText(this.mUserInfo.getDisplayName());
            this.mImvHead.setImageLoadListener(new AsyncImageView.ImageLoadListener() {

                @Override
                public void imageLoading() {

                }

                @Override
                public void imageLoadFinish() {

                }

                @Override
                public void imageLoadFail() {
                    mImvHead.setImageResource(R.mipmap.img_default_upload_head);
                }
            });
            this.mCurHeadPath = ResourceHelper.getImageUrlOnIndex(this.mUserInfo.getHeadImg(), 0);
            LogHelper.d(TAG, "头像URL：this.mUserInfo.getHeadImg():" + this.mUserInfo.getHeadImg());
            ResourceHelper.asyncImageViewFillUrl(this.mImvHead, this.mCurHeadPath);
            int mUserAuth = this.mUserInfo.getAuth();
            if(mUserAuth == Constants.MemberGrade.SENIOR){
                this.mImvVip.setVisibility(View.VISIBLE);
                this.mImvGrade.setVisibility(View.GONE);
            } else if(mUserAuth == Constants.MemberGrade.DIAMON){
                this.mImvGrade.setVisibility(View.VISIBLE);
                this.mImvVip.setVisibility(View.GONE);
                this.mImvGrade.setImageResource(R.mipmap.img_diamon_sign);
            } else {
                this.mImvVip.setVisibility(View.GONE);
                this.mImvGrade.setVisibility(View.GONE);
            }
        }
        // 取得当前名片
        this.mCurrentVCardEntity = CurrentUser.getInstance().getCurrentVCardEntity();
        if(ResourceHelper.isNotNull(this.mCurrentVCardEntity)){
            this.mTxvPosition.setText(this.mCurrentVCardEntity.getJob());
            this.mTxvCompany.setText(this.mCurrentVCardEntity.getCompanyName());
            AsyncImageView.ImageLoadListener imageLoadListener = new AsyncImageView.ImageLoadListener(){

                AnimationDrawable mDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.qrcode_anim_loading);

                @Override
                public void imageLoading() {
                    mImvQrcodeLoading.setImageDrawable(mDrawable);
                    mDrawable.start();
                    mImvQrcodeScan.setVisibility(View.GONE);
                }

                @Override
                public void imageLoadFinish() {
                    mDrawable.stop();
                    mImvQrcodeLoading.setVisibility(View.GONE);
                    mImvQrcodeScan.setVisibility(View.VISIBLE);
                }

                @Override
                public void imageLoadFail() {
                    mDrawable.stop();
                    mImvQrcodeLoading.setVisibility(View.GONE);
                    mImvQrcodeScan.setImageResource(R.mipmap.img_qrcode_load_fail);
                    mImvQrcodeScan.setVisibility(View.VISIBLE);
                }

            };
            this.mImvQrcodeScan.setImageLoadListener(imageLoadListener);
            this.mImvQrcodeScan.autoFillFromUrl(CurrentUser.getInstance().getURLEntity().getImgBaseService() + mCurrentVCardEntity.getQrCardPath());
        }
     }

    /**
     * 显示大图
     */
    private void showDialogFragmentHeadMax(String headImg){
        if(ResourceHelper.isNull(this.mDialogFragmentHeadMax)){
            this.mDialogFragmentHeadMax = DialogFragmentHelper.showUserHeadMaxDialogFragment(headImg);
            this.mDialogFragmentHeadMax.setTargetFragment(this, Constants.TakePicture.REQUEST_CODE_CHOOSE_HEAD);
        }
        this.mDialogFragmentHeadMax.setHeadImg(headImg);
        this.mDialogFragmentHeadMax.show(getFragmentManager(),"mDialogFragmentHeadMax");
    }
}
