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
 * 设置：其他：备份
 */
public class SettingOtherBackupFragment extends BaseFragment {
    private SettingEntity mSetting = CurrentUser.getInstance().getSetting();
    private int mTimeDistancePosition, mBackupTypePosition;
    private CustomDialogFragment mDialogFragmentTimeDistance, mDialogFragmentBackupType;
    private SlipButton mSlbAutoCloudBackup, mSlbWIFIAutoBackup;//开关按钮
    private TextView mTxvAutoBackupTime, mTxvBackupWay;//右边提示信息

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_backup_auto_backup_time:
                    //自动备份时间间隔
                    showDialogFragmentTimeDistance();
                    break;
                case R.id.rel_backup_backup_way:
                    //备份方式
                    showDialogFragmentBackupType();
                    break;
                case R.id.txv_backup_log_backup:
                    //备份日志
                    mFragmentInteractionImpl.onFragmentInteraction(SettingOtherBackupLogFragment.class.getName(), null);
                    break;
            }
        }
    }  ;

    private SlipButton.OnSlipChangedListener mOnSlipChangedListener = new SlipButton.OnSlipChangedListener() {
        @Override
        public void OnChanged(View v, String strName, boolean checkState) {
            switch (v.getId()){
                case R.id.slb_backup_auto_cloud_backup:
                    //自动云端备份
                    mSetting.setOpenBackups(checkState);
                    break;
                case R.id.slb_backup_wifi_auto_backup:
                    //仅在WIFI网络时同步备份
                    mSetting.setBackupsInWIFI(checkState);
                    break;

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_other_backup, container, false);
        this.mSlbAutoCloudBackup = (SlipButton) view.findViewById(R.id.slb_backup_auto_cloud_backup);
        this.mSlbWIFIAutoBackup = (SlipButton) view.findViewById(R.id.slb_backup_wifi_auto_backup);
        RelativeLayout mRelAutoBackupTime = (RelativeLayout) view.findViewById(R.id.rel_backup_auto_backup_time);
        RelativeLayout mRelBackupWay = (RelativeLayout) view.findViewById(R.id.rel_backup_backup_way);
        this.mTxvAutoBackupTime = (TextView) view.findViewById(R.id.txv_backup_auto_backup_time);
        this.mTxvBackupWay = (TextView) view.findViewById(R.id.txv_backup_backup_way);
        TextView mTxvLogBackup = (TextView) view.findViewById(R.id.txv_backup_log_backup);
        mRelAutoBackupTime.setOnClickListener(this.mOnClickListener);
        mRelBackupWay.setOnClickListener(this.mOnClickListener);
        mTxvLogBackup.setOnClickListener(this.mOnClickListener);
        this.mSlbAutoCloudBackup.setOnChangedListener("", this.mOnSlipChangedListener);
        this.mSlbWIFIAutoBackup.setOnChangedListener("", this.mOnSlipChangedListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.frg_setting_other_backups_setting, true);
        //自动云端备份
        this.mSlbAutoCloudBackup.setChecked(this.mSetting.isOpenBackups());
        //仅在WIFI网络时同步备份
        this.mSlbWIFIAutoBackup.setChecked(this.mSetting.isBackupsInWIFI());
        //自动备份时间间隔
        this.mTimeDistancePosition = this.mSetting.getBackupsTime();
        this.mTxvAutoBackupTime.setText(ResourceHelper.getResArrayChild(this.mTimeDistancePosition, R.array.backup_set_distance));
        //备份方式
        this.mBackupTypePosition = this.mSetting.getBackupsType();
        this.mTxvBackupWay.setText(ResourceHelper.getResArrayChild(this.mBackupTypePosition, R.array.backup_set_pattern));
    }

    /**
     * 自动备份时间间隔
     */
    private void showDialogFragmentTimeDistance(){
        if(ResourceHelper.isNull(this.mDialogFragmentTimeDistance)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mCustomAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mCustomAdapter);
            mCustomAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.backup_set_distance));
            mCustomAdapter.setPosition(this.mTimeDistancePosition);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCustomAdapter.setPosition(position);
                    mTimeDistancePosition = position;
                }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){

                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //确认
                        mSetting.setBackupsTime(mTimeDistancePosition);
                        mTxvAutoBackupTime.setText(mCustomAdapter.getItem(mTimeDistancePosition));
                    }
                    mCustomAdapter.setPosition(mSetting.getBackupsTime());
                }
            };
            this.mDialogFragmentTimeDistance = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_other_language_type_select, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentTimeDistance.show(getFragmentManager(), "mDialogFragmentTimeDistance");
    }

    /**
     * 备份方式对方框
     */
    private void showDialogFragmentBackupType(){
        if(ResourceHelper.isNull(this.mDialogFragmentBackupType)){
            ListView mLsvItem = DialogFragmentHelper.getCustomContentView(getActivity());
            final CustomLsvAdapter mCustomAdapter = new CustomLsvAdapter(getActivity());
            mLsvItem.setAdapter(mCustomAdapter);
            mCustomAdapter.addItems(ResourceHelper.getListFromResArray(getActivity(), R.array.backup_set_pattern));
            mCustomAdapter.setPosition(this.mBackupTypePosition);
            mLsvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mCustomAdapter.setPosition(position);
                    mBackupTypePosition = position;
                }
            });
            CustomDialogFragment.DialogFragmentInterface dialogOnclick = new CustomDialogFragment.DialogFragmentInterface(){

                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //确认
                        mSetting.setBackupsType(mBackupTypePosition);
                        mTxvBackupWay.setText(mCustomAdapter.getItem(mBackupTypePosition));
                    }
                    mCustomAdapter.setPosition(mSetting.getBackupsType());
                }
            };
            this.mDialogFragmentBackupType = DialogFragmentHelper.showCustomDialogFragment(R.string.frg_setting_other_language_type_select, mLsvItem, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnclick);
        }
        this.mDialogFragmentBackupType.show(getFragmentManager(), "mDialogFragmentBackupType");
    }


}
