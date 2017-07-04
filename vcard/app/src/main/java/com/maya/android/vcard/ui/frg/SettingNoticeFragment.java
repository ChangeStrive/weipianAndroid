package com.maya.android.vcard.ui.frg;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.ui.adapter.CustomLsvAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.ui.widget.SlipButton;
import com.maya.android.vcard.util.AudioRecordHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 设置：消息提醒
 */
public class SettingNoticeFragment extends BaseFragment {

    private SettingEntity mSetting = CurrentUser.getInstance().getSetting();
    private int mSoundEffectPosition, mNewsEffectPosition;//当前选中的Item默认为第一个
    private int mShakeRate = 60;//当前摇晃灵敏度默认为60
    private CustomDialogFragment mDialogFragmentSoundEffect, mDialogFragmentNewsEffect, mDialogFragmentShakeSetting, mDialogFragmentClearAllChatLog;
    private TextView mTxvSoundEffect, mTxvNewsEffect, mTxvSensitivity;//右边提示信息
    private SlipButton mSlbChattingRecords, mSlbReceiveInform;//右边开关按钮
    private ArrayList<String> mNewMsgWarnToneList = null;
    private HashMap<String, Uri> mSystemRingtoneList;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_vcard_exchange_sound_effect:
                    //名片交换音效
                    showDialogFragmentSoundEffect();
                    break;
                case R.id.rel_news_effect:
                    //新消息提示音
                    showDialogFragmentNewsEffect();
                    break;
                case R.id.rel_sensitivity_setting:
                    //摇晃灵敏度设置
                    showDialogFragmentShakeSetting();
                    break;
                case R.id.txv_manage_chat_log:
                    mFragmentInteractionImpl.onFragmentInteraction(SettingManagerChatFragment.class.getName(), null);
                    break;
                case R.id.txv_clear_chat_log:
                    //清空聊天记录
                    showClearAllChatLog();
                    break;

            }
        }
    };

    /**
     * SlipButton监听
     **/
    private SlipButton.OnSlipChangedListener mOnSlipChangedListener = new SlipButton.OnSlipChangedListener() {
        @Override
        public void OnChanged(View v, String strName, boolean checkState) {
            switch (v.getId()){
                case R.id.sbtn_frg_notice_chatting_records:
                    //管理聊天记录
                    mSetting.setAlertNewMessage(checkState);
                    break;
                case R.id.sbtn_frg_notice_receive_inform:
                    //退出登陆后接收离线通知
                    mSetting.setAlertNewMessageOffLine(checkState);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mTitleAction.setActivityTitle(R.string.frg_setting_main_msg_warn_set, true);
        View view = inflater.inflate(R.layout.fragment_setting_notice, container, false);
        RelativeLayout mRelSoundEffect = (RelativeLayout) view.findViewById(R.id.rel_vcard_exchange_sound_effect);
        RelativeLayout mRelNewsEffect = (RelativeLayout) view.findViewById(R.id.rel_news_effect);
        RelativeLayout mRelSensitivity = (RelativeLayout) view.findViewById(R.id.rel_sensitivity_setting);
        TextView mTxvManageChatLog = (TextView) view.findViewById(R.id.txv_manage_chat_log);
        TextView mTxvClearChatLog = (TextView) view.findViewById(R.id.txv_clear_chat_log);
        mRelSoundEffect.setOnClickListener(this.mOnClickListener);
        mRelNewsEffect.setOnClickListener(this.mOnClickListener);
        mRelSensitivity.setOnClickListener(this.mOnClickListener);
        mTxvManageChatLog.setOnClickListener(this.mOnClickListener);
        mTxvClearChatLog.setOnClickListener(this.mOnClickListener);
        this.mTxvSoundEffect = (TextView) view.findViewById(R.id.txv_vcard_exchange_sound_effect);
        this.mTxvNewsEffect = (TextView) view.findViewById(R.id.txv_news_effect);
        this.mTxvSensitivity = (TextView) view.findViewById(R.id.txv_sensitivity_setting);
        this.mSlbChattingRecords = (SlipButton) view.findViewById(R.id.sbtn_frg_notice_chatting_records);
        this.mSlbReceiveInform = (SlipButton) view.findViewById(R.id.sbtn_frg_notice_receive_inform);
        this.mSlbChattingRecords.setOnChangedListener("",this.mOnSlipChangedListener);
        this.mSlbReceiveInform.setOnChangedListener("",this.mOnSlipChangedListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //名片音效
        this.mSoundEffectPosition = this.mSetting.getSwapCardSoundResourceId();
        this.mTxvSoundEffect.setText(ResourceHelper.getResArrayChild(this.mSoundEffectPosition, R.array.swap_card_sound_set));
        //消息提示音
        newsEffect();
        // 摇晃灵敏度
        this.mShakeRate = (int)this.mSetting.getRockRate();
        this.mTxvSensitivity.setText(this.mShakeRate + "%");
        this.mSlbChattingRecords.setChecked(this.mSetting.isAlertNewMessage());
        this.mSlbReceiveInform.setChecked(this.mSetting.isAlertNewMessageOffLine());
    }

    /**
     * 名片音效交换对方框
     */
    private void showDialogFragmentSoundEffect(){
        if(ResourceHelper.isNull(this.mDialogFragmentSoundEffect)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mCustomAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mCustomAdapter);
            mCustomAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.swap_card_sound_set));
            mCustomAdapter.setPosition(this.mSoundEffectPosition);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCustomAdapter.setPosition(position);
                    mSoundEffectPosition = position;
                    int resourceId = ResourceHelper.getSwapCardResourceId(position);
                    if (resourceId != 0) {
                        AudioRecordHelper.setRescource2MediaPlay(resourceId);
                        AudioRecordHelper.play();
                    } else {
                        AudioRecordHelper.stopMediaPlay();
                    }
               }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){

                @Override
                public void onDialogClick(int which) {
                    AudioRecordHelper.stopMediaPlay();
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //确认
                        mTxvSoundEffect.setText(mCustomAdapter.getItem(mSoundEffectPosition));
                        mSetting.setSwapCardSoundResourceId(mSoundEffectPosition);
                    }
                    mCustomAdapter.setPosition(mSetting.getSwapCardSoundResourceId());
                }
            };
            this. mDialogFragmentSoundEffect = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_notice_set_vcard_exchange_sound_effect, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
    }
        this.mDialogFragmentSoundEffect.show(getFragmentManager(), "mDialogFragmentSoundEffect");
    }
    /**
     * 新消息提示音对话框
     */
    private void showDialogFragmentNewsEffect() {
        if (ResourceHelper.isNull(this.mDialogFragmentNewsEffect)) {
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mLsvAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mLsvAdapter);
            mLsvAdapter.addItems(this.mNewMsgWarnToneList);
//            mLsvAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.swap_card_sound_set));
            mLsvAdapter.setPosition(this.mNewsEffectPosition);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mLsvAdapter.setPosition(position);
                    mNewsEffectPosition = position;
                    //播放音效
                    int toneSize = mLsvAdapter.getCount() - 2;
                    if (position < toneSize) {
                        Uri uri = mSystemRingtoneList.get(mLsvAdapter.getItem(position));
                        AudioRecordHelper.startMediaPlay(uri);
                    }
                }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface() {

                @Override
                public void onDialogClick(int which) {
                    if (which == CustomDialogFragment.BUTTON_POSITIVE) {
                        String mNewMsgWarnToneName = mLsvAdapter.getItem(mNewsEffectPosition);
                        mTxvNewsEffect.setText(mNewMsgWarnToneName);
                        mSetting.setNewMessageSoundName(mNewMsgWarnToneName);
                    }
                    mLsvAdapter.setPosition(getNewsEffectPosition());
                }
            };
            this.mDialogFragmentNewsEffect = DialogFragmentHelper.showCustomDialogFragment(R.string.msg_setting_new_msg_sound, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
            this.mDialogFragmentNewsEffect.setContentViewHeight(true, 0.65);
        }
        this.mDialogFragmentNewsEffect.show(getFragmentManager(), "mDialogFragmentNewsEffect");
    }

    /**
     * 获取当前的音效提示音
     */
    private int getNewsEffectPosition(){
        int mNewsEffectPosition = 0;
        String mNewMsgWarnToneName = this.mSetting.getNewMessageSoundName();
        if(ResourceHelper.isEmpty(mNewMsgWarnToneName)){
            mNewsEffectPosition = 0;
        }else{
            for (int i = 0, size = this.mNewMsgWarnToneList.size(); i < size; i++) {
                if(mNewMsgWarnToneName.equals(this.mNewMsgWarnToneList.get(i))){
                    mNewsEffectPosition = i;
                    break;
                }
            }
        }
        return mNewsEffectPosition;
    }

    /**
     * 配置名片提示音效
     */
    private void newsEffect(){
        if(ResourceHelper.isNull(this.mSystemRingtoneList)){
            this.mSystemRingtoneList = ResourceHelper.getRingtoneList();
        }
        if(ResourceHelper.isNotNull(this.mSystemRingtoneList)){
            this.mNewMsgWarnToneList = new ArrayList<String>(this.mSystemRingtoneList.keySet());
        }else{
            this.mNewMsgWarnToneList = new ArrayList<String> ();
        }
        //震动
        this.mNewMsgWarnToneList.add(getString(R.string.shake));
        //静音
        this.mNewMsgWarnToneList.add(getString(R.string.mute));
        //新消息提示音
        this.mNewsEffectPosition = getNewsEffectPosition();
        this.mTxvNewsEffect.setText(this.mNewMsgWarnToneList.get(this.mNewsEffectPosition));
    }
    /**
     * 摇晃灵敏度设置
     */
    private void showDialogFragmentShakeSetting(){
        if(Helper.isNull(this.mDialogFragmentShakeSetting)){
            View mSeekBarView = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_custom_msg_warn_rock_set, null);
            final SeekBar sebRock = (SeekBar) mSeekBarView.findViewById(R.id.seb_dlg_frg_msg_warn_rock_set);
            sebRock.setProgress(this.mShakeRate);
            sebRock.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mShakeRate = progress;
                }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){

                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //确认
                        //若低于最低灵敏度，则设置为默认灵敏度30
                        if(mShakeRate < 30){
                            mShakeRate = 30;
                        }
                        sebRock.setProgress(mShakeRate);
                        mTxvSensitivity.setText(mShakeRate + "%");
                        mSetting.setRockRate(mShakeRate);
                    }
                }
            };
            this.mDialogFragmentShakeSetting = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_notice_sensitivity_setting, mSeekBarView, R.string.frg_text_ok, dialogOnclick);
         }
        this.mDialogFragmentShakeSetting.show(getFragmentManager(), "mDialogFragmentShakeSetting");
    }
    /**
     * 清空聊天记录
     */
    private void showClearAllChatLog(){
        if(Helper.isNull(this.mDialogFragmentClearAllChatLog)){

            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //TODO  清空聊天记录
                    }
                }
            };
            this.mDialogFragmentClearAllChatLog = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_notice_clear_chat_log_all, R.string.frg_setting_notice_clear_chat_friend_log_all_sure, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentClearAllChatLog.show(getFragmentManager(),"mDialogFragmentClearAllChatLog" );
    }
}
