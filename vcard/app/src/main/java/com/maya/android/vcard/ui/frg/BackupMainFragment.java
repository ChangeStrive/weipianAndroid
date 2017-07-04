package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.BackupLogDao;
import com.maya.android.vcard.dao.ContactBookDao;
import com.maya.android.vcard.dao.ContactVCardDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.BackupLogEntity;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.entity.json.BackupJsonEntity;
import com.maya.android.vcard.entity.json.ContactBookBackupJsonEntity;
import com.maya.android.vcard.entity.json.ContactVCardBackupJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.act.RegisterActivity;
import com.maya.android.vcard.ui.adapter.BackupElvAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 备份/恢复:备份
 */
public class BackupMainFragment extends BaseFragment {
    /** 当前展开的分组项 **/
    private int curGroupPosition;
    /** 全选、取消全选 **/
    private boolean isCancelOrSelectAll = true;
    /**成功备份名片数**/
    private int mVcardTotal;
    /**成功备份联系人数**/
    private int mBookTotal;
    /** 当前用户资料 **/
    private UserInfoResultEntity userInfo;
    /** 设置信息 **/
    private SettingEntity mSetting;
    private URLResultEntity mUrlEntity;
    private Button mBtnSubmit;
    private ExpandableListView mElvBackUp;
    private TextView mTxvEmpty;
    private BackupElvAdapter backupElvAdapter;
    private CustomDialogFragment mDialogFragmentBindMobile, mDialogFragmentBackup;
    /** 子项集合 **/
    private  SparseArray<ArrayList<ContactGroupEntity>> mChildChbArr;


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    //登录验证
                    if(!CurrentUser.getInstance().isLogin()){
                        ActivityHelper.showToast(R.string.login_please);
                        return;
                    }
                    //网络验证
                    if(!NetworkHelper.isNetworkAvailable(getActivity())){
                        ActivityHelper.showToast(R.string.no_network);
                        return;
                    }
                    if(ResourceHelper.isNull(userInfo)){
                        userInfo = CurrentUser.getInstance().getUserInfoEntity();
                    }
                    if(userInfo.getBindWay() == Constants.BindWay.MOBILE || userInfo.getBindWay() == Constants.BindWay.ALL){
                        uploadBackup();
                    }else{
                        showDialogFragmentBindMobile();
                    }
                    break;
                case R.id.txv_act_top_right:
                    if(isCancelOrSelectAll){
                        mTitleAction.setActivityTopRightTxv(R.string.frg_text_cancel, mOnClickListener);
                        //全选
                        backupElvAdapter.setGroupAllChecked();
                    }else{
                        mTitleAction.setActivityTopRightTxv(R.string.select_all, mOnClickListener);
                        //取消
                        backupElvAdapter.setGroupAllCancel();
                    }
                    setCancelOrSelectAll();
                    break;
            }
        }
    };

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        ActivityHelper.closeProgressDialog();
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        ActivityHelper.closeProgressDialog();
        if(tag == Constants.CommandRequestTag.CMD_REQUEST_CLOUD_BACKUP){
             showDialogFragmentBackup();
        }
    }



    /**
     * 分组点击事件
     */
    ExpandableListView.OnGroupClickListener mOnGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if(groupPosition != 2){
                if(curGroupPosition >= 0 && mElvBackUp.isGroupExpanded(curGroupPosition)){
                    mElvBackUp.collapseGroup(curGroupPosition);
                }
                if(curGroupPosition != groupPosition){
                    mElvBackUp.expandGroup(groupPosition, true);
                    curGroupPosition = groupPosition;
                }else{
                    curGroupPosition = -1;
                }
            }
            return true;
        }
    };

    /**
     * 子列表选项单击事件
     */
    private ExpandableListView.OnChildClickListener mOnChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            backupElvAdapter.changeChildChecked(groupPosition, childPosition);
            return false;
        }
    };

    private BackupElvAdapter.BackupElvListener mBackupElvListener = new BackupElvAdapter.BackupElvListener() {
        @Override
        public void toChecked(boolean isSelected) {
            if(isSelected){
                ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_399c2f, true);
            }else{
                ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_787878, false);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_backup_main, container, false);
        this.mElvBackUp = (ExpandableListView) view.findViewById(R.id.elv_backup);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_backup_empty);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mElvBackUp.setEmptyView(this.mTxvEmpty);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        //去掉前面的箭头图标
        this.mElvBackUp.setGroupIndicator(null);
        this.mElvBackUp.setOnChildClickListener(this.mOnChildClickListener);
        this.mElvBackUp.setOnGroupClickListener(this.mOnGroupClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        this.mTitleAction.setActivityTitle(R.string.backup_cloud, false);
        this.mTitleAction.setActivityTopRightTxv(R.string.select_all, this.mOnClickListener);
        this.mTitleAction.getActivityTopRightTxv().setVisibility(View.VISIBLE);
        this.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_787878, false);
        this.backupElvAdapter = new BackupElvAdapter(getActivity(), BackupElvAdapter.BACKUP);
        this.mElvBackUp.setAdapter(this.backupElvAdapter);
        this.backupElvAdapter.setBackupElvListener(this.mBackupElvListener);
        this.backupElvAdapter.addGroupItems(getArrayList());
        this.backupElvAdapter.addChildItems(getChildSparseArray());

    }

    /**
     * 设置选择状态改变
     */
    private void setCancelOrSelectAll(){
        this.isCancelOrSelectAll = !this.isCancelOrSelectAll;
    }

    /**
     * 备份对话框
     */
    private void showDialogFragmentBackup(){
        if(ResourceHelper.isNull(this.mDialogFragmentBackup)){
            this.mDialogFragmentBackup = DialogFragmentHelper.showCustomDialogFragment(R.string.backup_details_ok, getString(R.string.backup_details_backup_result, mVcardTotal, mBookTotal) ,R.string.frg_text_ok);
        }
        this.mDialogFragmentBackup.setMessage(getString(R.string.backup_details_backup_result, mVcardTotal, mBookTotal));
        this.mDialogFragmentBackup.show(getFragmentManager(), "mDialogFragmentBackup");
    }

    /**
     * 未验证手机号码对话框
     */
    private void showDialogFragmentBindMobile(){
        if(ResourceHelper.isNull(this.mDialogFragmentBindMobile)){
            CustomDialogFragment.DialogFragmentInterface dlgOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(CustomDialogFragment.BUTTON_POSITIVE == which){
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, RegisterMobileFragment.class.getName());
                        bundle.putInt(RegisterMobileFragment.KEY_REGISTER_OR_BING_MOBILE, RegisterMobileFragment.CODE_BING_MOBILE);
                        intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                        mActivitySwitchTo.switchTo(RegisterActivity.class, intent);
                    }
                }
            };
            this.mDialogFragmentBindMobile = DialogFragmentHelper.showCustomDialogFragment(R.string.mobile_bind_hint,getString(R.string.confirm_mobile_active_warn,R.string.backup_cloud),R.string.frg_text_cancel,R.string.frg_text_ok,dlgOnClick);
        }
        this.mDialogFragmentBindMobile.show(getFragmentManager(), "mDialogFragmentBindMobile");
    }

    /**
     *获取分组数据
     */
    private ArrayList<ContactGroupEntity> getArrayList(){
        return BackupLogDao.getInstance().getDefaultList(BackupLogEntity.FLAG_BACKUP);
    }

    /**
     *获取分组下子集数据数据
     * @return
     */
    private SparseArray<ArrayList<ContactGroupEntity>> getChildSparseArray(){
        if(ResourceHelper.isNull(this.mChildChbArr)){
            this.mChildChbArr = new SparseArray<ArrayList<ContactGroupEntity>>();
        }
        // 我的名片子项
        if(ResourceHelper.isNull(this.mChildChbArr.get(0, null))){
            this.mChildChbArr.put(0, BackupLogDao.getInstance().getCardChildList(BackupLogEntity.FLAG_BACKUP, true));
        }
        if(ResourceHelper.isNull(this.mChildChbArr.get(1, null))){
            // 通讯录
            this.mChildChbArr.put(1, BackupLogDao.getInstance().getLocalChildList(BackupLogEntity.FLAG_BACKUP, true));
        }
        if(ResourceHelper.isNull(this.mChildChbArr.get(2, null))){
            // 私密 空间子项
            this.mChildChbArr.put(2, BackupLogDao.getInstance().getPrivateChildList(BackupLogEntity.FLAG_BACKUP));
        }
        return this.mChildChbArr;
    }

    /**
     * 备份数据
     */
    private void uploadBackup(){
        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
        ArrayList<ContactGroupBackupEntity> mVcardGroupList = this.backupElvAdapter.getSelectedGroupBackupList(0);
        ArrayList<ContactGroupBackupEntity> mBookGroupList = this.backupElvAdapter.getSelectedGroupBackupList(1);
        // 名片夹联系人
        ArrayList<ContactVCardBackupJsonEntity> vcardContactList = null;
        // 通讯录联系人
        ArrayList<ContactBookBackupJsonEntity> bookContactList = null;
        if(ResourceHelper.isNotNull(mVcardGroupList)){
            vcardContactList = ContactVCardDao.getInstance().getListForBackup(mVcardGroupList);
        }
        if(ResourceHelper.isNotNull(mBookGroupList)){
            bookContactList = ContactBookDao.getInstance().getListForBackup(mBookGroupList);
        }
        BackupJsonEntity mBackupEntity = new BackupJsonEntity();
        if(ResourceHelper.isNull(this.mSetting)){
            this.mSetting = CurrentUser.getInstance().getSetting();
        }
        int backupType = this.mSetting.getBackupsType();
        mBackupEntity.setBackupType(backupType);
        mBackupEntity.setBookGroupList(mBookGroupList);
        if(Helper.isNotNull(bookContactList)){
            this.mBookTotal = bookContactList.size();
            mBackupEntity.setBookPersonList(bookContactList);
        }else{
            this.mBookTotal = 0;
        }
        mBackupEntity.setCardGroupList(mVcardGroupList);
        if(Helper.isNotNull(vcardContactList)){
            mBackupEntity.setCardPersonList(vcardContactList);
            this.mVcardTotal = vcardContactList.size();
        }else{
            this.mVcardTotal = 0;
        }
        String json = JsonHelper.toJson(mBackupEntity, BackupJsonEntity.class);
        if(ResourceHelper.isNull(this.mUrlEntity)){
            this.mUrlEntity = CurrentUser.getInstance().getURLEntity();
        }
        String mToken = CurrentUser.getInstance().getToken();
        String backupUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getCloudContactsBackup());


        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CLOUD_BACKUP, backupUrl, mToken, json,this);
    }

}
