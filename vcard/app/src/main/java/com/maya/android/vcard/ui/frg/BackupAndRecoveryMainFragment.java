package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.BackupLogDao;
import com.maya.android.vcard.dao.ContactBookDao;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.BackupLogEntity;
import com.maya.android.vcard.ui.act.MessageMainActivity;
import com.maya.android.vcard.ui.act.SettingActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.wxapi.WXEntryActivity;

/**
 * 备份/恢复
 */
public class BackupAndRecoveryMainFragment extends BaseFragment {

    private TextView mTxvVCardSno, mTxvIsLogin, mTxvBackUpTime, mTxvRecoverTime, mTxvVCardNum, mTxvContactPersonNum,
            mTxvMailListNum, mTxvShortMesNum, mTxvPrivate, mTxvMainSetNum;
    private RelativeLayout mRelBackUp, mRelRecover, mRelAdministration, mRelLogin;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_backup_cloud:
                    //云端备份
                    mFragmentInteractionImpl.onFragmentInteraction(BackupMainFragment.class.getName(), null);
                    break;
                case R.id.rel_back_recovery:
                    //云端恢复
                    mFragmentInteractionImpl.onFragmentInteraction(RecoveryMainFragment.class.getName(), null);
                    break;
                case R.id.rel_back_administration:
                    //管理云端数据
                    Intent logIntent = new Intent();
                    logIntent.putExtra(Constants.IntentSet.KEY_FRG_NAME, SettingOtherBackupLogFragment.class.getName());
                    mActivitySwitchTo.switchTo(MessageMainActivity.class, logIntent);
                    break;
                case R.id.rel_login:
                    //登录
                    Intent loginIntent = new Intent();
                    loginIntent.putExtra(Constants.IntentSet.KEY_FRG_NAME, LoginFragment.class.getName());
                    mActivitySwitchTo.switchTo(WXEntryActivity.class, loginIntent);
                    break;
                case R.id.imv_act_top_right:
                    mActivitySwitchTo.switchTo(SettingActivity.class, null);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backup_and_recovery_main, container, false);
        this.mTxvVCardSno = (TextView) view.findViewById(R.id.txv_vcardsno);
        this.mTxvIsLogin = (TextView) view.findViewById(R.id.txv_is_login);
        this.mRelLogin = (RelativeLayout) view.findViewById(R.id.rel_login);
        this.mRelBackUp = (RelativeLayout) view.findViewById(R.id.rel_backup_cloud);
        this.mRelRecover = (RelativeLayout) view.findViewById(R.id.rel_back_recovery);
        this.mRelAdministration = (RelativeLayout) view.findViewById(R.id.rel_back_administration);
        this.mTxvBackUpTime = (TextView) view.findViewById(R.id.txv_back_up_last_time);
        this.mTxvRecoverTime = (TextView) view.findViewById(R.id.txv_backup_recover_last_time);
        this.mTxvVCardNum = (TextView) view.findViewById(R.id.txv_vcard_num);
        this.mTxvContactPersonNum = (TextView) view.findViewById(R.id.txv_contact_person_num);
        this.mTxvMailListNum = (TextView) view.findViewById(R.id.txv_mail_list_num);
        this.mTxvShortMesNum = (TextView) view.findViewById(R.id.txv_short_mes_num);
        this.mTxvPrivate = (TextView) view.findViewById(R.id.txv_private);
        this.mTxvMainSetNum = (TextView) view.findViewById(R.id.txv_main_set_num);
        this.mRelBackUp.setOnClickListener(this.mOnClickListener);
        this.mRelRecover.setOnClickListener(this.mOnClickListener);
        this.mRelAdministration.setOnClickListener(this.mOnClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        this.mTitleAction.setActivityTitle(R.string.pop_backup_restore, false);
        this.mTitleAction.setActivityTopRightImv(R.mipmap.img_back_set, this.mOnClickListener);
        this.mTitleAction.setActivityTopRightImvVisibility(View.VISIBLE);
        this.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        fillBackUpData();
    }

    /**
     * 填充数据
     */
    private void fillBackUpData(){
        this.mTxvVCardSno.setText(CurrentUser.getInstance().getVCardNo());
        if(CurrentUser.getInstance().isLogin()){
            this.mTxvIsLogin.setText(R.string.frg_setting_account_has_login);
            this.mTxvIsLogin.setTextColor(getResources().getColor(R.color.color_198e02));
            this.mRelLogin.setEnabled(false);
        }else{
            this.mTxvIsLogin.setText(R.string.no_login);
            this.mTxvIsLogin.setTextColor(getResources().getColor(R.color.color_fe6030));
            this.mRelLogin.setEnabled(true);
        }
        String backupTime = BackupLogDao.getInstance().getLastTime("", BackupLogEntity.FLAG_BACKUP);
        String recoverTime = BackupLogDao.getInstance().getLastTime("", BackupLogEntity.FLAG_RECOVER);

        int mycardNum = BackupLogDao.getInstance().getLastNum(getString(R.string.my_card), BackupLogEntity.FLAG_BACKUP, BackupLogEntity.TYPE_VCARD);
        int localNum = BackupLogDao.getInstance().getLastNum(getString(R.string.contact), BackupLogEntity.FLAG_BACKUP, BackupLogEntity.TYPE_LOCAL);
        //名片数量
        int cardNum = ContactVCardDao.getInstance().getContactTotal();
        //联系人数量
        int contactPersonNum = ContactBookDao.getInstance().getContactTotal(true);
        //通讯录数量
        int mailListNum = 0;
        //短信数量
        int shortMesNum = 0;
        //主页设置数量
        int mainSetNUm = 0;
        ContactVCardDao.getInstance().getContactTotal();
        this.mTxvBackUpTime.setText(getString(R.string.backup_label_copy_last_time, ResourceHelper.getDateTimeFormat(backupTime, ResourceHelper.sDateFormat_YYMMDD_HHMM)));
        this.mTxvRecoverTime.setText(getString(R.string.backup_label_recover_last_time, ResourceHelper.getDateTimeFormat(recoverTime, ResourceHelper.sDateFormat_YYMMDD_HHMM)));
        this.mTxvVCardNum.setText(getString(R.string.backup_label_copy_my_card, mycardNum + "/" + cardNum));
        this.mTxvContactPersonNum.setText(getString(R.string.backup_label_copy_my_book,localNum + "/" + contactPersonNum));
        this.mTxvMailListNum.setText(getString(R.string.backup_label_copy_my_address_book, "" + mailListNum));
        this.mTxvShortMesNum.setText(getString(R.string.backup_label_copy_my_note, "" + shortMesNum));
        this.mTxvPrivate.setText(getString(R.string.backup_label_copy_private));
        this.mTxvMainSetNum.setText(getString(R.string.backup_label_copy_set, "" + mainSetNUm));

    }
}
