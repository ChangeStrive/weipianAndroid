package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.dao.BackupLogDao;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.BackupLogEntity;
import com.maya.android.vcard.entity.ContactGroupBackupEntity;
import com.maya.android.vcard.entity.ContactGroupEntity;
import com.maya.android.vcard.entity.json.RecoveryJsonEntity;
import com.maya.android.vcard.entity.result.RecoverResultEntity;
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

import net.tsz.afinal.core.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 备份/恢复:恢复
 */
public class RecoveryMainFragment extends BaseFragment {
    /** 当前展开的分组项 **/
    private int curGroupPosition = -1;
    /**成功备份名片数**/
    private int mVcardTotal;
    /**成功备份联系人数**/
    private int mBookTotal;
    private Button mBtnSubmit;
    private ExpandableListView mElvRecover;
    private TextView mTxvEmpty;
    private BackupElvAdapter backupElvAdapter;
    /** 全选、取消全选 **/
    private boolean isCancelOrSelectAll = true;
    private CustomDialogFragment mDialogFragmentBindMobile, mDialogFragmentRecover;
    /** 子项集合 **/
    private  SparseArray<ArrayList<ContactGroupEntity>> mChildChbArr;
    /** 当前用户资料 **/
    private UserInfoResultEntity userInfo;
    private URLResultEntity mUrlEntity;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
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
                        uploadRecovery();
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

    /**
     * 分组点击事件
     */
    ExpandableListView.OnGroupClickListener mOnGroupClickListener = new ExpandableListView.OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if(groupPosition != 2){
                if(curGroupPosition >= 0 && mElvRecover.isGroupExpanded(curGroupPosition)){
                    mElvRecover.collapseGroup(curGroupPosition);
                }
                if(curGroupPosition != groupPosition){
                    mElvRecover.expandGroup(groupPosition, true);
                    curGroupPosition = groupPosition;
                }else{
                    curGroupPosition = -1;
                }
            }
            return true;
        }
    };

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            ActivityHelper.closeProgressDialog();
            showDialogFragmentRecover();
        }

    };

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        if(! super.onCommandCallback2(tag, commandResult, objects)){
            if(tag == Constants.CommandRequestTag.CMD_REQUEST_CLOUD_RECOVER) {
                new AsyncTask<JSONObject, String, String>(){

                    @Override
                    protected String doInBackground(JSONObject... params) {
                        RecoverResultEntity recoverEntity = JsonHelper.fromJson(params[0], RecoverResultEntity.class);
                        if(ResourceHelper.isNotNull(recoverEntity)){
                            BackupLogDao.getInstance().recoverContacts(recoverEntity);
                            mHandler.sendMessage(Message.obtain());
                        }
                        return null;
                    }
                }.execute(commandResult);
            }
        }
        return true;
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_main, container, false);
        this.mElvRecover = (ExpandableListView) view.findViewById(R.id.elv_recovery);
        this.mTxvEmpty = (TextView) view.findViewById(R.id.txv_recovery_empty);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mElvRecover.setEmptyView(this.mTxvEmpty);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);

        //去掉前面的箭头图标
        this.mElvRecover.setGroupIndicator(null);
        this.mElvRecover.setOnChildClickListener(this.mOnChildClickListener);
        this.mElvRecover.setOnGroupClickListener(this.mOnGroupClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        this.mTitleAction.setActivityTitle(R.string.backup_recover, false);
        this.mTitleAction.setActivityTopRightTxv(R.string.select_all, this.mOnClickListener);
        this.mTitleAction.getActivityTopRightTxv().setVisibility(View.VISIBLE);
        this.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        ButtonHelper.setButtonEnable(mBtnSubmit, R.color.color_787878, false);
        this.backupElvAdapter = new BackupElvAdapter(getActivity(), BackupElvAdapter.RECOVERY);
        this.mElvRecover.setAdapter(this.backupElvAdapter);
        this.backupElvAdapter.setBackupElvListener(this.mBackupElvListener);
        //测试
        this.backupElvAdapter.addGroupItems(getArrayList());
        this.backupElvAdapter.addChildItems(getSparseArray());
    }

    /**
     * 设置选择状态改变
     */
    private void setCancelOrSelectAll(){
        this.isCancelOrSelectAll = !this.isCancelOrSelectAll;
    }

    /**
     * 恢复对话框
     */
    private void showDialogFragmentRecover(){
        if(ResourceHelper.isNull(this.mDialogFragmentRecover)){
            this.mDialogFragmentRecover = DialogFragmentHelper.showCustomDialogFragment(R.string.recover_details_ok, getString(R.string.recover_details_result, mVcardTotal, mBookTotal), R.string.frg_text_ok);
        }
        this.mDialogFragmentRecover.setMessage(getString(R.string.recover_details_result, this.mVcardTotal,  this.mBookTotal));
        this.mDialogFragmentRecover.show(getFragmentManager(), "mDialogFragmentRecover");
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
     *测试组列表
     */
    private ArrayList<ContactGroupEntity> getArrayList(){
       return BackupLogDao.getInstance().getDefaultList(BackupLogEntity.FLAG_RECOVER);
    }

    /**
     *测试子集列表
     * @return
     */
    private SparseArray<ArrayList<ContactGroupEntity>> getSparseArray(){
        if(ResourceHelper.isNull(this.mChildChbArr)){
            this.mChildChbArr = new SparseArray<ArrayList<ContactGroupEntity>>();
        }
        // 我的名片子项
        if(ResourceHelper.isNull(this.mChildChbArr.get(0, null))){
            this.mChildChbArr.put(0, BackupLogDao.getInstance().getCardChildList(BackupLogEntity.FLAG_RECOVER, true));
        }
        if(ResourceHelper.isNull(this.mChildChbArr.get(1, null))){
            // 通讯录
            this.mChildChbArr.put(1, BackupLogDao.getInstance().getLocalChildList(BackupLogEntity.FLAG_RECOVER, true));
        }
        if(ResourceHelper.isNull(this.mChildChbArr.get(2, null))){
            // 私密 空间子项
            this.mChildChbArr.put(2, BackupLogDao.getInstance().getPrivateChildList(BackupLogEntity.FLAG_RECOVER));
        }
        return this.mChildChbArr;
    }

    /**
     * 恢复数据
     */
    private void uploadRecovery(){
        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
        ArrayList<ContactGroupBackupEntity> mVcardGroupList = this.backupElvAdapter.getSelectedGroupBackupList(0);
        ArrayList<ContactGroupBackupEntity> mBookGroupList = this.backupElvAdapter.getSelectedGroupBackupList(1);

        ArrayList<String> vcardGroupNameList = null;
        if(ResourceHelper.isNotNull(mVcardGroupList)){
            vcardGroupNameList = new ArrayList<String>();
            for(int i = 0, size = mVcardGroupList.size(); i < size; i++){
                String groupName = mVcardGroupList.get(i).getName();
                if(!vcardGroupNameList.contains(groupName)){
                    vcardGroupNameList.add(groupName);
                }
            }
        }
        ArrayList<String> bookGroupNameList = null;
        if(ResourceHelper.isNotNull(mBookGroupList)){
            bookGroupNameList = new ArrayList<String>();
            for (int i = 0, size = mBookGroupList.size(); i < size; i++) {
                String groupName = mBookGroupList.get(i).getName();
                if(!bookGroupNameList.contains(groupName)){
                    bookGroupNameList.add(groupName);
                }
            }
        }
        RecoveryJsonEntity recovery = new RecoveryJsonEntity();
        if(ResourceHelper.isNotNull(vcardGroupNameList)){
            this.mVcardTotal = vcardGroupNameList.size();
            recovery.setCardGroupNameList(vcardGroupNameList);
        }else{
            this.mBookTotal = 0;
        }
        if(ResourceHelper.isNotNull(bookGroupNameList)){
            recovery.setBookGroupNameList(bookGroupNameList);
            this.mBookTotal = bookGroupNameList.size();
        }else{
            this.mBookTotal = 0;
        }
        String json = JsonHelper.toJson(recovery, RecoveryJsonEntity.class);
        if(ResourceHelper.isNull(this.mUrlEntity)){
            this.mUrlEntity = CurrentUser.getInstance().getURLEntity();
        }
        String backupUrl = ProjectHelper.fillRequestURL(this.mUrlEntity.getCloudContactsSync());
        String mToken = CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_CLOUD_RECOVER, backupUrl, mToken, json, this);
    }
}
