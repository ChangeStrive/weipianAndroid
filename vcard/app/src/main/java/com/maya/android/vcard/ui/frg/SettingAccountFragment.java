package com.maya.android.vcard.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.RegisterActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.SlipButton;
import com.maya.android.vcard.util.ResourceHelper;

/**
 *设置：账号设置
 */
public class SettingAccountFragment extends BaseFragment{
    private SettingEntity mSetting = CurrentUser.getInstance().getSetting();
    private SlipButton mSlbContact;//开关设置
    private TextView mTxvMobileIsbinded, mTxvEmailIsbinded, mTxvProtectIsLock ,mTxvAccountIsLogin;//显示当前状态
    private AsyncImageView mImvHead;//头像
    private TextView mTxvName;//名字
    private TextView mTxvSVno;//微片号
    private TextView mTxvPhone, mTxvEmail;
    private RelativeLayout mRelMobile, mRelEmiail;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            switch (v.getId()){
                case R.id.rel_frg_account_manage_vcard_secretary:
                    //账号管理
                    mFragmentInteractionImpl.onFragmentInteraction(SettingAccountManageFragment.class.getName(), null);
                    break;
                case R.id.rel_frg_account_manage_mobile:
                    //手机验证
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, RegisterMobileFragment.class.getName());
                    bundle.putInt(RegisterMobileFragment.KEY_REGISTER_OR_BING_MOBILE, RegisterMobileFragment.CODE_BING_MOBILE);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(RegisterActivity.class, intent);
                    break;
                case R.id.rel_frg_account_manage_email:
                    //邮箱验证
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, RegisterEmailFragment.class.getName());
                    bundle.putInt(RegisterEmailFragment.KEY_REGISTER_OR_BING_EMAIL, RegisterEmailFragment.CODE_BING_EMAIL);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(RegisterActivity.class, intent);
                    break;
                case R.id.rel_frg_account_setting_protect:
                    //账号保护
                    break;
                case R.id.txv_frg_account_manage_alert_password:
                    //密码修改
                    mFragmentInteractionImpl.onFragmentInteraction(SettingAccountChangePasswordFragment.class.getName(), null);
                    break;

             }
        }
    };

    private SlipButton.OnSlipChangedListener mOnSlipChangedListener = new SlipButton.OnSlipChangedListener() {
        @Override
        public void OnChanged(View v, String strName, boolean checkState) {
            switch (v.getId()){
                case R.id.sbtn_frg_account_auto_associate_match_contact:
                    //自动关联匹配通讯录
                    mSetting.setAccountLinkAddressBook(checkState);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mTitleAction.setActivityTitle(R.string.frg_setting_main_account, true);
        View view = inflater.inflate(R.layout.fragment_setting_account, container, false);
        RelativeLayout mRelSecretary = (RelativeLayout) view.findViewById(R.id.rel_frg_account_manage_vcard_secretary);
        this.mRelMobile = (RelativeLayout) view.findViewById(R.id.rel_frg_account_manage_mobile);
        this.mRelEmiail = (RelativeLayout) view.findViewById(R.id.rel_frg_account_manage_email);
        RelativeLayout mRelAccountProtect= (RelativeLayout) view.findViewById(R.id.rel_frg_account_setting_protect);
        TextView mTxvAlterPwd = (TextView) view.findViewById(R.id.txv_frg_account_manage_alert_password);
        this.mSlbContact = (SlipButton) view.findViewById(R.id.sbtn_frg_account_auto_associate_match_contact);
        this.mTxvMobileIsbinded = (TextView) view.findViewById(R.id.txv_frg_account_manage_mobile_isbinded);
        this.mTxvEmailIsbinded = (TextView) view.findViewById(R.id.txv_frg_account_manage_email_isbinded);
        this.mTxvProtectIsLock = (TextView) view.findViewById(R.id.txv_frg_account_setting_protect_lock);
        this.mTxvAccountIsLogin = (TextView) view.findViewById(R.id.txv_frg_account_islogin);
        this.mImvHead = (AsyncImageView) view.findViewById(R.id.imv_frg_account_setting_head);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_frg_account_setting_vcard_username);
        this.mTxvSVno = (TextView) view.findViewById(R.id.txv_frg_account_vsno);
        this.mTxvPhone = (TextView) view.findViewById(R.id.txv_account_phone);
        this.mTxvEmail = (TextView) view.findViewById(R.id.txv_account_email);
        mRelSecretary.setOnClickListener(this.mOnClickListener);
        mRelMobile.setOnClickListener(this.mOnClickListener);
        mRelEmiail.setOnClickListener(this.mOnClickListener);
        mRelAccountProtect.setOnClickListener(this.mOnClickListener);
        mTxvAlterPwd.setOnClickListener(this.mOnClickListener);
        this.mSlbContact.setOnChangedListener("", this.mOnSlipChangedListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //自动关联匹配通讯录
        this.mSlbContact.setChecked(this.mSetting.isAccountLinkAddressBook());
        initData();
    }

    private void initData(){
        int isVerify = getResources().getColor(R.color.color_198e02);
        int noVerify = getResources().getColor(R.color.color_fe6030);
        if(!CurrentUser.getInstance().isLogin()){
            this.mTxvAccountIsLogin.setText(R.string.no_login);
            this.mTxvAccountIsLogin.setTextColor(noVerify);
            return;
        }
//        this.mTxvAccountIsLogin.setTextColor(isVerify);
        //用户资料
        UserInfoResultEntity userInfo =  CurrentUser.getInstance().getUserInfoEntity();
        if(ResourceHelper.isNotNull(userInfo)) {
            String displayName = userInfo.getDisplayName();
            if (ResourceHelper.isEmpty(displayName)) {
                displayName = userInfo.getNickName();
                if (ResourceHelper.isEmpty(displayName)) {
                    displayName = userInfo.getUsername();
                }
            }
            this.mTxvName.setText(displayName);
            String vCardNo = userInfo.getVcardNo();
            this.mTxvSVno.setText(vCardNo);
            if (ResourceHelper.isNotEmpty(vCardNo) && vCardNo.equals(Constants.DefaultUser.DEFAULT_USER_VCARD_NO)) {
                this.mImvHead.setImageResource(R.mipmap.img_user_head_mytip);
            } else {
                this.mImvHead.setDefaultImageResId(R.mipmap.img_upload_head);
                ResourceHelper.asyncImageViewFillUrl(this.mImvHead, ResourceHelper.getImageUrlOnIndex(userInfo.getHeadImg(), 0));
            }

            int bindWay = userInfo.getBindWay();
            if(Constants.BindWay.ALL == bindWay){
                this.mTxvPhone.setText("("+userInfo.getMobileTel()+")");
                this.mTxvMobileIsbinded.setTextColor(isVerify);
                this.mTxvEmailIsbinded.setTextColor(isVerify);
                this.mRelEmiail.setEnabled(false);
                this.mRelMobile.setEnabled(false);
            }else{
                if(Constants.BindWay.MOBILE == bindWay){
                    this.mTxvPhone.setText("("+userInfo.getMobileTel()+")");
                    this.mTxvMobileIsbinded.setText(R.string.change_bind);
                    this.mTxvMobileIsbinded.setTextColor(isVerify);
                    this.mRelMobile.setEnabled(false);
                }else{
                    this.mTxvMobileIsbinded.setText(R.string.frg_setting_account_common_unbind);
                    this.mTxvMobileIsbinded.setTextColor(noVerify);
                }
                if(Constants.BindWay.EMAIL == bindWay){
                    this.mTxvEmail.setText(userInfo.getEmail());
                    this.mTxvEmailIsbinded.setText(R.string.have_bind);
                    this.mTxvEmailIsbinded.setTextColor(isVerify);
                    this.mRelEmiail.setEnabled(false);
                }else{
                    this.mTxvEmailIsbinded.setText(R.string.frg_setting_account_common_unbind);
                    this.mTxvEmailIsbinded.setTextColor(noVerify);
                }
            }


        }
    }
}
