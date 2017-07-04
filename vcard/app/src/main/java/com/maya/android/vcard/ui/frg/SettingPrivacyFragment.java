package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.SlipButton;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 设置：隐私
 */
public class SettingPrivacyFragment extends BaseFragment {
    private SettingEntity mSetting = CurrentUser.getInstance().getSetting();
    private SlipButton mSlbHideMobileNumber, mSlbCloudFindMe, mSlbDeleteVCardPwdIsNeed, mSlbRelevanceMsgFriendCanVisible;//开关按钮
    private TextView mTxvCouldMsgSetting, mTxvFriendTranspondMyVcard;//右边提示信息
    private int mTranspondMyVcardPosition, mCouldMsgSetPosition;
    private CustomDialogFragment mDialogFragmentTranspondMyVcard, mDialogFragmentCouldMsgSet;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.rel_privacy_cloud_msg_setting:
                    //云端信息显示设置
                    showDialogFragmentCouldMsgSe();
                    break;
                case R.id.rel_privacy_friend_transpond_my_vcard:
                    //允许好友转发我的名片
                    showDialogFragmentTranspondMyVcard();
                    break;
                case R.id.txv_privacy_exchange_mode_setting:
                    //交换模式
                    mFragmentInteractionImpl.onFragmentInteraction(SettingPrivacySwapModelFragment.class.getName(), null);
                    break;
            }
        }
    };

    private SlipButton.OnSlipChangedListener mOnSlipChangedListener = new SlipButton.OnSlipChangedListener() {
        @Override
        public void OnChanged(View v, String strName, boolean checkState) {
            switch (v.getId()) {
                case R.id.slb_privacy_hide_molile_number:
                    //隐藏手机号码
                    mSetting.setHideMobileNumber(checkState);
                    break;
                case R.id.slb_privacy_cloud_find_me:
                    //允许云端查找到我
                    mSetting.setAllowCloudFindMe(checkState);
                    break;
                case R.id.slb_privacy_delete_vcard_pwd_isneed:
                    //删除我的名片需要密码
                    mSetting.setDelCardNeedPassword(checkState);
                    break;
                case R.id.slb_privacy_relevance_msg_friend_can_visible:
                    //关联的社交信息对好友可见
                    mSetting.setShowSocialContactToFriend(checkState);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_privacy, container, false);
        this.mSlbHideMobileNumber = (SlipButton) view.findViewById(R.id.slb_privacy_hide_molile_number);
        this.mSlbCloudFindMe = (SlipButton) view.findViewById(R.id.slb_privacy_cloud_find_me);
        this.mSlbDeleteVCardPwdIsNeed = (SlipButton) view.findViewById(R.id.slb_privacy_delete_vcard_pwd_isneed);
        this.mSlbRelevanceMsgFriendCanVisible = (SlipButton) view.findViewById(R.id.slb_privacy_relevance_msg_friend_can_visible);
        RelativeLayout mRelCloudMsgSetting = (RelativeLayout) view.findViewById(R.id.rel_privacy_cloud_msg_setting);
        RelativeLayout mRelFriendTranspondMyVcard = (RelativeLayout) view.findViewById(R.id.rel_privacy_friend_transpond_my_vcard);
        TextView mTxvExchangeModeSetting = (TextView) view.findViewById(R.id.txv_privacy_exchange_mode_setting);
        this.mTxvCouldMsgSetting = (TextView) view.findViewById(R.id.txv_privacy_cloud_msg_setting);
        this.mTxvFriendTranspondMyVcard = (TextView) view.findViewById(R.id.txv_privacy_friend_transpond_my_vcard);
        mRelCloudMsgSetting.setOnClickListener(this.mOnClickListener);
        mRelFriendTranspondMyVcard.setOnClickListener(this.mOnClickListener);
        mTxvExchangeModeSetting.setOnClickListener(this.mOnClickListener);
        this.mSlbHideMobileNumber.setOnChangedListener("", mOnSlipChangedListener);
        this.mSlbCloudFindMe.setOnChangedListener("", mOnSlipChangedListener);
        this.mSlbDeleteVCardPwdIsNeed.setOnChangedListener("", mOnSlipChangedListener);
        this.mSlbRelevanceMsgFriendCanVisible.setOnChangedListener("", mOnSlipChangedListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.frg_setting_main_privacy, true);
        //隐藏手机号码
        this.mSlbHideMobileNumber.setChecked(mSetting.isHideMobileNumber());
        //允许云端查找到我
        this.mSlbCloudFindMe.setChecked(mSetting.isAllowCloudFindMe());
        //删除我的名片需要密码
        this.mSlbDeleteVCardPwdIsNeed.setChecked(mSetting.isDelCardNeedPassword());
        //关联的社交信息对好友可见
        this.mSlbRelevanceMsgFriendCanVisible.setChecked(mSetting.isShowSocialContactToFriend());
        //允许好友转发我的名片
        this.mTranspondMyVcardPosition = mSetting.getAllowFriendTransmitCard();
        this.mTxvFriendTranspondMyVcard.setText(ResourceHelper.getResArrayChild(this.mTranspondMyVcardPosition, R.array.transmit_mycard_set));
        //云端信息显示设置
        this.mCouldMsgSetPosition = mSetting.getCloudFindShowType();
        this.mTxvCouldMsgSetting.setText(ResourceHelper.getResArrayChild(this.mCouldMsgSetPosition, R.array.cloud_find_show_type));
    }

    /**
     * 允许好友转发我的名片弹窗
     */
    private void showDialogFragmentTranspondMyVcard() {
        if (ResourceHelper.isNull(this.mDialogFragmentTranspondMyVcard)) {
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            mLsvAdapter.setPosition(this.mTranspondMyVcardPosition);
            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.transmit_mycard_set));
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mLsvAdapter.setPosition(position);
                    mTranspondMyVcardPosition = position;
                }
            });

            CustomDialogFragment.DialogFragmentInterface mOnclick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if (CustomDialogFragment.BUTTON_POSITIVE == which) {
                        mSetting.setAllowFriendTransmitCard(mTranspondMyVcardPosition);
                        mTxvFriendTranspondMyVcard.setText(ResourceHelper.getResArrayChild(mTranspondMyVcardPosition, R.array.transmit_mycard_set));
                    }
                    mLsvAdapter.setPosition(mSetting.getAllowFriendTransmitCard());
                }
            };
            this.mDialogFragmentTranspondMyVcard = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_privacy_friend_transpond_my_vcard_set, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, mOnclick);
        }
        this.mDialogFragmentTranspondMyVcard.show(getFragmentManager(), "mDialogFragmentTranspondMyVcard");
    }

    /**
     * 云端信息显示设置
     */
    private void showDialogFragmentCouldMsgSe(){
        if(ResourceHelper.isNull(this.mDialogFragmentCouldMsgSet)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            mLsvAdapter.setPosition(this.mCouldMsgSetPosition);
            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.cloud_find_show_type));
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mLsvAdapter.setPosition(position);
                    mCouldMsgSetPosition = position;
                }
            });
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        mSetting.setCloudFindShowType(mCouldMsgSetPosition);
                        mTxvCouldMsgSetting.setText(ResourceHelper.getResArrayChild(mCouldMsgSetPosition, R.array.cloud_find_show_type));
                    }
                    mLsvAdapter.setPosition(mSetting.getCloudFindShowType());
                }
            };
            this.mDialogFragmentCouldMsgSet = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_privacy_cloud_msg_setting, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, mOnClick);

        }
        this.mDialogFragmentCouldMsgSet.show(getFragmentManager(), "mDialogFragmentCouldMsgSet");
    }

}