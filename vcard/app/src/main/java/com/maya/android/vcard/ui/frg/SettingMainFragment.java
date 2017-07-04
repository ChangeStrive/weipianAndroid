package com.maya.android.vcard.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.ui.act.UserMainActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.NFCHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.wxapi.WXEntryActivity;

/**
 * 设置：主界面
 */
public class SettingMainFragment extends BaseFragment {

    private ImageView mImvRecommendSign, mImvAboutVCardSign;//显示消息提醒
    private CustomDialogFragment mDialogFragmentNotNfc, mDialogFragmentOpenNfc;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_frg_setting_seting_account:
                    //账户设置
                    mFragmentInteractionImpl.onFragmentInteraction(SettingAccountFragment.class.getName(), null);
                    break;
                case R.id.txv_frg_setting_msg_warn_set:
                    //消息提醒设置
                    mFragmentInteractionImpl.onFragmentInteraction(SettingNoticeFragment.class.getName(), null);
                    break;
                case R.id.txv_frg_setting_privacy:
                    //隐私设置
                    mFragmentInteractionImpl.onFragmentInteraction(SettingPrivacyFragment.class.getName(), null);
                    break;
                case R.id.txv_frg_setting_other:
                    //其他设置
                    mFragmentInteractionImpl.onFragmentInteraction(SettingOtherFragment.class.getName(), null);
                     break;
                case R.id.rel_frg_setting_top_recommend:
                    //热门推荐
                    break;
                case R.id.txv_frg_setting_share:
                    //分享给朋友
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserShareFragment.class.getName());
                    bundle.putInt(UserShareFragment.KEY_SHARE_WAY, UserShareFragment.SHARE_WAY_SOFTWARE);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_frg_web_common_feedback:
                    //意见反馈
                    mFragmentInteractionImpl.onFragmentInteraction(SettingFeedbackFragment.class.getName(), null);
                    break;
                case R.id.rel_frg_setting_about_vcard:
                    //关于微片
                    mFragmentInteractionImpl.onFragmentInteraction(SettingAboutFragment.class.getName(), null);
                    break;
                case R.id.rel_frg_setting_submit:
                    //注销登录
                    CurrentUser.getInstance().logout();
                    mActivitySwitchTo.switchTo(WXEntryActivity.class, null);
                    break;
                case R.id.txv_nfc_vcard_enter:
                    //NFC名片写入U
                    if(!NFCHelper.hasNFC()){
                        showDialogFragmentNotNfc();
                        return;
                    }
                    if(!NFCHelper.isOpenNFC()){
                        showDialogFragmentOpenNfc();
                        return;
                    }

                    if(Helper.isNull(CurrentUser.getInstance().getCurrentVCardEntity())){
                        ActivityHelper.showToast(R.string.nfc_no_data);
                        return;
                    }
                    Intent intent2 = new Intent();
                    intent2.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserNFCWriteFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent2);
                    break;

            }
        }
    };
    /**
     * 没有NFC功能
     */
    private void showDialogFragmentNotNfc(){
        if(ResourceHelper.isNull(this.mDialogFragmentNotNfc)){
            this.mDialogFragmentNotNfc = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, R.string.cur_mobile_nfc_hint_isnull, R.string.frg_text_ok);
        }
        this.mDialogFragmentNotNfc.show(getFragmentManager(), "mDialogFragmentNotNfc");
    }
    /**
     * 没有开启NFC功能
     */
    private void showDialogFragmentOpenNfc(){
        if(ResourceHelper.isNull(this.mDialogFragmentOpenNfc)){
            CustomDialogFragment.DialogFragmentInterface dialogOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(intent);
                }
            };
            this.mDialogFragmentOpenNfc = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, R.string.cur_mobile_nfc_hint_not_open, R.string.frg_text_ok, dialogOnClick);
        }
        this.mDialogFragmentOpenNfc.show(getFragmentManager(), "mDialogFragmentNotNfc");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        this.mTitleAction.setActivityTitle(R.string.setting, true);
        View view = inflater.inflate(R.layout.fragment_setting_main, container, false);
        TextView mTxvAccount = (TextView)view.findViewById(R.id.txv_frg_setting_seting_account);
        TextView mTxvMsgwarn = (TextView)view.findViewById(R.id.txv_frg_setting_msg_warn_set);
        TextView mTxvPrivacy = (TextView)view.findViewById(R.id.txv_frg_setting_privacy);
        TextView mTxvOther = (TextView)view.findViewById(R.id.txv_frg_setting_other);
        RelativeLayout mRelRecommend = (RelativeLayout)view.findViewById(R.id.rel_frg_setting_top_recommend);
        TextView mTxvShare = (TextView)view.findViewById(R.id.txv_frg_setting_share);
        TextView mTxvFeedback = (TextView)view.findViewById(R.id.txv_frg_web_common_feedback);
        RelativeLayout mRelAbloutVCard = (RelativeLayout)view.findViewById(R.id.rel_frg_setting_about_vcard);
        RelativeLayout mRelSubmit = (RelativeLayout)view.findViewById(R.id.rel_frg_setting_submit);
        TextView mTxvNFCVcardEnter = (TextView) view.findViewById(R.id.txv_nfc_vcard_enter);//智能硬件设置

        this.mImvRecommendSign = (ImageView)view.findViewById(R.id.imv_frg_setting_recomend_sign);
        this.mImvAboutVCardSign = (ImageView)view.findViewById(R.id.imv_frg_setting_about_vcard_sign);
        mTxvPrivacy.setOnClickListener(this.mOnClickListener);
        mTxvAccount.setOnClickListener(this.mOnClickListener);
        mTxvMsgwarn.setOnClickListener(this.mOnClickListener);
        mTxvOther.setOnClickListener(this.mOnClickListener);
        mRelRecommend.setOnClickListener(this.mOnClickListener);
        mTxvShare.setOnClickListener(this.mOnClickListener);
        mTxvFeedback.setOnClickListener(this.mOnClickListener);
        mRelAbloutVCard.setOnClickListener(this.mOnClickListener);
        mRelSubmit.setOnClickListener(this.mOnClickListener);
        mTxvNFCVcardEnter.setOnClickListener(this.mOnClickListener);
        return view;
    }

}
