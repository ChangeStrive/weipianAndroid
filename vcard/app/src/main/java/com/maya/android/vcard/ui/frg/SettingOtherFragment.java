package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.utils.Helper;
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
 * 设置：其他
 *
 */
public class SettingOtherFragment extends BaseFragment {
    private SettingEntity mSetting = CurrentUser.getInstance().getSetting();
    private int mHomepagePosition, mLanguageTypePosition, mCallerDisplayPosition;//记录当前选中的Item项
    private CustomDialogFragment mDialogFragmentHomepageSetting, mDialogFragmentLanguageType, mDialogFragmentCallerDisplay;
    private TextView mTxvVoiceSelected, mTxvHomepageSetting, mTxvAddressBookCID;//右边提示信息
    private SlipButton mSlbAddressBook;//开关按钮

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_other_homepage_setting:
                    //首页设置
                    showDialogFragmentHomepageSetting();
                    break;
                case R.id.rel_other_voice_selected:
                    //语音选择
                    showDialogFargmentLanguageType();
                    break;
                case R.id.rel_other_address_book_cid:
                    //通讯录来电显示
                    showDialogFragmentCallerDisplay();
                    break;
                case R.id.txv_other_backups_setting:
                    //备份设置
                    mFragmentInteractionImpl.onFragmentInteraction(SettingOtherBackupFragment.class.getName(), null);
                    break;
                case R.id.txv_other_replace_skin:
                    //更换皮肤
                    break;
            }
        }
    };

    private SlipButton.OnSlipChangedListener mOnSlipChangedListener = new SlipButton.OnSlipChangedListener() {
        @Override
        public void OnChanged(View v, String strName, boolean checkState) {
            switch (v.getId()){
                case R.id.slb_other_vcard_add_address_book_to_mobile:
                    //自动将名片信息添加到手机通讯录
                    mSetting.setCardInfo2AddressList(checkState);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,   Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_other, container, false);
        RelativeLayout mRelHomePageSetting = (RelativeLayout) view.findViewById(R.id.rel_other_homepage_setting);
        RelativeLayout mRelVoiceSelected = (RelativeLayout) view.findViewById(R.id.rel_other_voice_selected);
        RelativeLayout mRelAddressBookCID = (RelativeLayout) view.findViewById(R.id.rel_other_address_book_cid);
        TextView mTxvBackupsSetting = (TextView) view.findViewById(R.id.txv_other_backups_setting);
        TextView mTxvReplaceSkin = (TextView) view.findViewById(R.id.txv_other_replace_skin);
        this.mTxvVoiceSelected = (TextView) view.findViewById(R.id.txv_other_voice_selected);
        this.mTxvHomepageSetting = (TextView) view.findViewById(R.id.txv_other_homepage_setting);
        this.mTxvAddressBookCID = (TextView) view.findViewById(R.id.txv_other_address_book_cid);
        this.mSlbAddressBook = (SlipButton) view.findViewById(R.id.slb_other_vcard_add_address_book_to_mobile);
        mRelHomePageSetting.setOnClickListener(this.mOnClickListener);
        mRelVoiceSelected.setOnClickListener(this.mOnClickListener);
        mRelAddressBookCID.setOnClickListener(this.mOnClickListener);
        mTxvBackupsSetting.setOnClickListener(this.mOnClickListener);
        mTxvReplaceSkin.setOnClickListener(this.mOnClickListener);
        this.mSlbAddressBook.setOnChangedListener("", mOnSlipChangedListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.frg_setting_main_other, true);
        //首页设置
        this.mHomepagePosition = mSetting.getHomePage();
        this.mTxvHomepageSetting.setText(ResourceHelper.getResArrayChild(this.mHomepagePosition, R.array.set_homepage));
        //语音选择
        this.mLanguageTypePosition =  mSetting.getLanguageType();
        this.mTxvVoiceSelected.setText(ResourceHelper.getResArrayChild(this.mLanguageTypePosition, R.array.set_language));
        //通讯录来电显示
        this.mCallerDisplayPosition = mSetting.getAddressListShowCard();
        this.mTxvAddressBookCID.setText(ResourceHelper.getResArrayChild(this.mCallerDisplayPosition, R.array.contact_book_caller_display));
        // 自动添加到通讯录
        this.mSlbAddressBook.setChecked(mSetting.isCardInfo2AddressList());
    }

    /**
     * 首页设置对方框
     */
    private void showDialogFragmentHomepageSetting(){
        if(Helper.isNull(this.mDialogFragmentHomepageSetting)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mCustomAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mCustomAdapter);
            mCustomAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.set_homepage));
            mCustomAdapter.setPosition(this.mHomepagePosition);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCustomAdapter.setPosition(position);
                    mHomepagePosition = position;
                }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){

                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //确认
                        mSetting.setHomePage(mHomepagePosition);
                        mTxvHomepageSetting.setText(mCustomAdapter.getItem(mHomepagePosition));
                    }
                    mCustomAdapter.setPosition(mSetting.getHomePage());
                }
            };
            this.mDialogFragmentHomepageSetting = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_other_homepage_setting, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
         }
        this.mDialogFragmentHomepageSetting.show(getFragmentManager(), "mDialogFragmentHomepageSetting");
    }

    /**
     * 语音选择对话框
     */
    private void showDialogFargmentLanguageType(){
        if(Helper.isNull(this.mDialogFragmentLanguageType)) {
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mCustomAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mCustomAdapter);
            mCustomAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.set_language));
            mCustomAdapter.setPosition(this.mLanguageTypePosition);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCustomAdapter.setPosition(position);
                    mLanguageTypePosition = position;
                }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface() {

                @Override
                public void onDialogClick(int which) {
                    if (which == CustomDialogFragment.BUTTON_POSITIVE) {
                        //确认
                        mSetting.setLanguageType(mLanguageTypePosition);
                        mTxvVoiceSelected.setText(mCustomAdapter.getItem(mLanguageTypePosition));
                    }
                    mCustomAdapter.setPosition(mSetting.getLanguageType());
                }
            };
            this.mDialogFragmentLanguageType = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_other_language_type_select, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentLanguageType.show(getFragmentManager(), "mDialogFragmentLanguageType");
    }

    /**
     * 通讯录来电显示对方框
     */
    private void showDialogFragmentCallerDisplay(){
        if(Helper.isNull(this.mDialogFragmentCallerDisplay)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mCustomAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mCustomAdapter);
            mCustomAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.contact_book_caller_display));
            mCustomAdapter.setPosition(this.mCallerDisplayPosition);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCustomAdapter.setPosition(position);
                    mCallerDisplayPosition = position;
                }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){

                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //确认
                        mSetting.setAddressListShowCard(mCallerDisplayPosition);
                        mTxvAddressBookCID.setText(mCustomAdapter.getItem(mCallerDisplayPosition));
                    }
                    mCustomAdapter.setPosition(mSetting.getAddressListShowCard());
                }
            };
            this.mDialogFragmentCallerDisplay = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_other_caller_display_select, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentCallerDisplay.show(getFragmentManager(), "mDialogFragmentCallerDisplay");

    }



}
