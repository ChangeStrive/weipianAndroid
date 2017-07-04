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

import com.maya.android.asyncimageview.widget.AsyncImageView;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.SettingActivity;
import com.maya.android.vcard.ui.act.UserMainActivity;
import com.maya.android.vcard.ui.act.WebActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.QrCodeDialogFragment;
import com.maya.android.vcard.ui.widget.UserHeadMaxDialogFragment;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.NFCHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 我的
 */
public class UserMainFragment extends BaseFragment {

    private static final String TAG = UserMainFragment.class.getSimpleName();

    private static final int WHAT_INIT_USER_INFO = 10001;
    private AsyncImageView mImvImgHead;
    private ImageView mImvVip, mImvGrade, mImvSex, mImvVerify, mImvBus, mImvQrCode;
    private TextView mTxvName, mTxvVcardSno, mTxvAge, mTxvConstellation, mTxvProvince, mTxvCity;
    private TextView mTxvVCard, mTxvAttention, mTxvFans;
    private UserInfoResultEntity userInfo;
    private CardEntity mCurVCardEntity;
    private QrCodeDialogFragment mDialogFragmentQrCode;
    private UserHeadMaxDialogFragment mDialogFragmentHeadMax;
    private String headImg;
    private CustomDialogFragment mDialogFragmentNotNfc, mDialogFragmentOpenNfc;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
            switch (v.getId()){
                case R.id.rel_head:
                    //用户资料
//                    Intent intent1 = new Intent(getActivity(),UserMainActivity.class);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserInfoFragment.class.getName());
                    intent.putExtra(UserInfoFragment.KEY_SHOW_USER, UserInfoFragment.CODE_SHOW_CURRENT_USER_INFO);
//                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    intent.setClass(getActivity(), UserMainActivity.class);
                    startActivityForResult(intent, WHAT_INIT_USER_INFO);
                    break;
                case R.id.txv_vcard:
                    //微片
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserVCardFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_attention:
                    //关注
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserAttAndFansFragment.class.getName());
                    bundle.putInt(UserAttAndFansFragment.KEY_ATT_OR_FANS, UserAttAndFansFragment.ATT_OR_FANS_TO_ATTENTION);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_fans:
                    //粉丝
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserAttAndFansFragment.class.getName());
                    bundle.putInt(UserAttAndFansFragment.KEY_ATT_OR_FANS, UserAttAndFansFragment.ATT_OR_FANS_TO_FANS);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_vmarket:
                    //积分超市
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserMarketFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_app_center:
                    //营销工具包
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserAppCenterFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_wealth_center:
                    //我的钱包
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserWealthCenterFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_vip_class:
                    //会员等级
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserLevelFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_nearby_contacts:
                    //附近的人脉
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserNearbyPeopleFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
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
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserNFCWriteFragment.class.getName());
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_recommend_friend_use:
                    //推荐好友使用
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, UserShareFragment.class.getName());
                    bundle.putInt(UserShareFragment.KEY_SHARE_WAY, UserShareFragment.SHARE_WAY_SOFTWARE);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    mActivitySwitchTo.switchTo(UserMainActivity.class, intent);
                    break;
                case R.id.txv_one_tree_green_public:
                    //一棵树绿色公益
                    intent.putExtra(Constants.IntentSet.KEY_INTENT_CODE, Constants.IntentSet.INTENT_CODE_GREEN_PUBLIC);
                    mActivitySwitchTo.switchTo(WebActivity.class, intent);
                    break;
                case R.id.txv_set:
                    //设置
                    mActivitySwitchTo.switchTo(SettingActivity.class, null);
                    break;
                case R.id.imv_qr_code:
                    //二维码
                    showDialogFragmentQrCode();
                    break;
                case R.id.imv_user_head:
                    showDialogFragmentHeadMax(headImg);
                    break;
             }
         }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == Activity.RESULT_OK){
        LogHelper.d(TAG, "RESULT:" + resultCode);
            switch (requestCode){
                case WHAT_INIT_USER_INFO:
                    fillUserInfo(this.userInfo);
                    break;
            }
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        this.mImvImgHead = (AsyncImageView) view.findViewById(R.id.imv_user_head);
        this.mImvVip = (ImageView) view.findViewById(R.id.imv_user_head_vip);
        this.mImvGrade = (ImageView) view.findViewById(R.id.imv_user_head_grade);
        this.mImvSex = (ImageView) view.findViewById(R.id.imv_user_sex);
        this.mImvVerify = (ImageView) view.findViewById(R.id.imv_user_head_verify);
        this.mImvBus = (ImageView) view.findViewById(R.id.imv_user_head_bus);
        this.mTxvName = (TextView) view.findViewById(R.id.txv_user_name);
        this.mTxvVcardSno = (TextView) view.findViewById(R.id.txv_user_vcardsno_enter);
        this.mTxvAge = (TextView) view.findViewById(R.id.txv_user_age);
        this.mTxvConstellation = (TextView) view.findViewById(R.id.txv_user_constellation);
        this.mTxvProvince = (TextView) view.findViewById(R.id.txv_user_province);
        this.mTxvCity = (TextView) view.findViewById(R.id.txv_user_city);
        this.mTxvVCard = (TextView) view.findViewById(R.id.txv_vcard);
        this.mTxvAttention = (TextView) view.findViewById(R.id.txv_attention);
        this.mTxvFans = (TextView) view.findViewById(R.id.txv_fans);
        this.mImvQrCode = (ImageView) view.findViewById(R.id.imv_qr_code);
        RelativeLayout mRelHeadView = (RelativeLayout) view.findViewById(R.id.rel_head);
        TextView mTxvVMarket = (TextView) view.findViewById(R.id.txv_vmarket);
        TextView mTxvAppCenter = (TextView) view.findViewById(R.id.txv_app_center);
        TextView mTxvWealthCenter = (TextView) view.findViewById(R.id.txv_wealth_center);
        TextView mTxvVipCalss = (TextView) view.findViewById(R.id.txv_vip_class);
        TextView mTxvNearbyContacts = (TextView) view.findViewById(R.id.txv_nearby_contacts);
        TextView mTxvNFCVcardEnter = (TextView) view.findViewById(R.id.txv_nfc_vcard_enter);
        TextView mTxvRecommendFriendUse = (TextView) view.findViewById(R.id.txv_recommend_friend_use);
        TextView mTxvOneTree = (TextView) view.findViewById(R.id.txv_one_tree_green_public);
        TextView mTxvSet = (TextView) view.findViewById(R.id.txv_set);
        this.mTxvVCard.setOnClickListener(this.mOnClickListener);
        this.mTxvAttention.setOnClickListener(this.mOnClickListener);
        this.mTxvFans.setOnClickListener(this.mOnClickListener);
        this.mImvQrCode.setOnClickListener(this.mOnClickListener);
        mTxvVMarket.setOnClickListener(this.mOnClickListener);
        mTxvAppCenter.setOnClickListener(this.mOnClickListener);
        mTxvWealthCenter.setOnClickListener(this.mOnClickListener);
        mTxvVipCalss.setOnClickListener(this.mOnClickListener);
        mTxvNearbyContacts.setOnClickListener(this.mOnClickListener);
        mTxvNFCVcardEnter.setOnClickListener(this.mOnClickListener);
        mTxvRecommendFriendUse.setOnClickListener(this.mOnClickListener);
        mTxvOneTree.setOnClickListener(this.mOnClickListener);
        mTxvSet.setOnClickListener(this.mOnClickListener);
        mRelHeadView.setOnClickListener(this.mOnClickListener);
        this.mImvImgHead.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.userInfo = CurrentUser.getInstance().getUserInfoEntity();
        fillUserInfo(this.userInfo);
        fillVCardInfo();
    }


    /**
     * 填充用户信息
     * @param userInfo
     */
    private void fillUserInfo(UserInfoResultEntity userInfo){
        if(ResourceHelper.isNotNull(userInfo)){
            this.mImvImgHead.setDefaultImageResId(R.mipmap.img_upload_head);
            this.headImg = ResourceHelper.getImageUrlOnIndex(userInfo.getHeadImg(), 0);
            ResourceHelper.asyncImageViewFillUrl(this.mImvImgHead, this.headImg);
            this.mTxvName.setText(userInfo.getDisplayName());
            this.mTxvVcardSno.setText(userInfo.getVcardNo());
            this.mTxvProvince.setText(userInfo.getProvince());
            this.mTxvCity.setText(userInfo.getCity());
            setStoreTextSize(mTxvAttention, getString(R.string.attention), userInfo.getAttentionNum());
            setStoreTextSize(mTxvFans, getString(R.string.fans), userInfo.getFansNum());
            //会员等级状况
            int grade = userInfo.getAuth();
            if(grade == Constants.MemberGrade.SENIOR){
                this.mImvVip.setVisibility(View.VISIBLE);
                this.mImvGrade.setVisibility(View.GONE);
            }else if(grade == Constants.MemberGrade.DIAMON){
                this.mImvVip.setVisibility(View.VISIBLE);
                this.mImvGrade.setVisibility(View.VISIBLE);
            }else{
                this.mImvVip.setVisibility(View.GONE);
                this.mImvGrade.setVisibility(View.GONE);
            }
            this.mImvSex.setImageResource(ResourceHelper.getSexImageResource(userInfo.getSex()));
            String birthday = userInfo.getBirthday();
            this.mTxvAge.setText(ResourceHelper.getAgeFromBirthday(birthday));
            this.mTxvConstellation.setText(ResourceHelper.getAstro(birthday));
        }
    }

    /**
     * 填充名片信息
     */
    private void fillVCardInfo(){
        //名片
        ArrayList<CardEntity> cardEntityList = CurrentUser.getInstance().getVCardEntityList();
        if(ResourceHelper.isNotNull(cardEntityList)){
            int cardNum = 0;
            for(CardEntity vcard : cardEntityList){
                cardNum += vcard.getCardCount();
            }
            setStoreTextSize(mTxvVCard, getString(R.string.card), cardNum);
        }else{
            setStoreTextSize(mTxvVCard, getString(R.string.card), 0);
        }
    }

     /**
     * 设置字体不同大小
     * @param textView
     * @param name
     * @param num
     */
    private void setStoreTextSize(TextView textView, String name, int num) {
        ResourceHelper.setStoreTextSize(getActivity(), textView, name, R.dimen.dimen_16sp, String.valueOf(num), R.dimen.dimen_13sp);
    }

    /**
     * 二维码对话框
     */
    private void showDialogFragmentQrCode(){
        if(ResourceHelper.isNull(this.mCurVCardEntity)){
            this.mCurVCardEntity = CurrentUser.getInstance().getCurrentVCardEntity();
        }

        if(ResourceHelper.isNull(this.mCurVCardEntity)){
            ActivityHelper.showToast(R.string.current_card_is_null_please_enter_card_info);
            return;
        }

        String qrCodeUrl = CurrentUser.getInstance().getURLEntity().getImgBaseService() + this.mCurVCardEntity.getQrCardPath();
        if(ResourceHelper.isNull(this.mDialogFragmentQrCode)){
            this.mDialogFragmentQrCode = DialogFragmentHelper.showQrCodeDialogFragment(this.userInfo.getDisplayName(), this.mCurVCardEntity.getJob(), this.mCurVCardEntity.getCompanyName(), this.headImg, qrCodeUrl);
        }else{
            this.mDialogFragmentQrCode.setHeadImg(this.headImg);
            this.mDialogFragmentQrCode.setName(this.userInfo.getDisplayName());
            this.mDialogFragmentQrCode.setJob(this.mCurVCardEntity.getJob());
            this.mDialogFragmentQrCode.setCompany(this.mCurVCardEntity.getCompanyName());
            this.mDialogFragmentQrCode.setQrCodeUrl(qrCodeUrl);
        }
        this.mDialogFragmentQrCode.show(getFragmentManager(), "mDialogFragmentQrCode");
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
}
