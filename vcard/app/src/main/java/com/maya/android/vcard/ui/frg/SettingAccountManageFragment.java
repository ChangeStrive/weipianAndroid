package com.maya.android.vcard.ui.frg;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.LoginSimpleInfoEntity;
import com.maya.android.vcard.entity.LoginSimpleInfoListEntity;
import com.maya.android.vcard.ui.adapter.AccountManageAdapter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.wxapi.WXEntryActivity;

import java.util.ArrayList;

/**
 * 设置：账号设置：账号管理
 */
public class SettingAccountManageFragment extends BaseFragment {

    /** 当前选中的项 **/
    private int curPosition;
    /** 账户数量 **/
    public int userCount;
    /** 判断是否是当前用户 **/
    private boolean isCurrentUser = false;
    private ListView mLsvAccountManage;
    private AccountManageAdapter mAccountManageAdapter;
    private CustomDialogFragment mDialogFragmentPrompt, mDialogFragmentDelAccount;

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            userCount = mAccountManageAdapter.getCount();
            if(position < userCount){
                LoginSimpleInfoEntity loginSimple = mAccountManageAdapter.getItem(position);
                if(ResourceHelper.isNotNull(loginSimple) && !loginSimple.getVcardNo().equals(CurrentUser.getInstance().getVCardNo())){
                    String msg = getString(R.string.confirm_switch_user, loginSimple.getDisplayName());
                    showDialogFragmentPrompt(position, msg);
                }
            }else{
                if(CurrentUser.getInstance().isLogin()){
                    String msg = getString(R.string.confirm_add_user, getString(R.string.new_user));
                    showDialogFragmentPrompt(position, msg);
                }else{
                    mActivitySwitchTo.switchTo(WXEntryActivity.class, null);
                    mFragmentInteractionImpl.onActivityFinish();
                }

            }
        }
    };

    private AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if(position < mAccountManageAdapter.getCount()){
                showDialogFragmentDelAccount(position);
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_account_manage, container, false);
        this.mLsvAccountManage = (ListView) view.findViewById(R.id.lsv_account_manage_list);
        this.mLsvAccountManage.setOnItemClickListener(this.mOnItemClickListener);
        this.mLsvAccountManage.setOnItemLongClickListener(this.mOnItemLongClickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.account_manager, false);
        this.mAccountManageAdapter = new AccountManageAdapter(getActivity());
        this.mLsvAccountManage.addFooterView(getFootView());
        this.mLsvAccountManage.setAdapter(this.mAccountManageAdapter);
        this.mAccountManageAdapter.addItems(getLoginSimpleInfoList());
    }

    /**
     * 获取数据
     * @return
     */
    public ArrayList<LoginSimpleInfoEntity> getLoginSimpleInfoList(){
        LoginSimpleInfoListEntity loginSimpleInfoList =  CurrentUser.getInstance().getLoginSimpleInfoList();
        if(ResourceHelper.isNotNull(loginSimpleInfoList)){
            return loginSimpleInfoList.getLoginSimpleInfoEntityList();
        }
        return null;
    }

    /**
     * 获取尾部
     * @return
     */
    private View getFootView(){
        return LayoutInflater.from(getActivity()).inflate(R.layout.item_lsv_account_manage_tail, null);
    }

    /**
     * 温馨提示
     * @param position
     * @param msg
     */
    private void showDialogFragmentPrompt(int position, String msg){
        this.curPosition = position;
        if(ResourceHelper.isNull(this.mDialogFragmentPrompt)){
            CustomDialogFragment.DialogFragmentInterface dialogOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        CurrentUser.getInstance().logout();
                        LoginSimpleInfoEntity loginSimple = null;
                        if(curPosition < userCount){
                            loginSimple = mAccountManageAdapter.getItem(curPosition);
                        }
                        if(ResourceHelper.isNotNull(loginSimple)){
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                            intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, LoginFragment.class.getName());
                            bundle.putBoolean(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN, true);
                            bundle.putString(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN_VCARD_NO, loginSimple.getVcardNo());
                            bundle.putString(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN_PASSWORD, loginSimple.getPassword());
                            bundle.putBoolean(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN_REMEMBER_PASSWORD, loginSimple.isRememberPassword());
                            mActivitySwitchTo.switchTo(WXEntryActivity.class, intent);
                        }else{
                            mActivitySwitchTo.switchTo(WXEntryActivity.class, null);
                        }
                        mFragmentInteractionImpl.onActivityFinish();
                    }
                }
            };
            this.mDialogFragmentPrompt = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, msg, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnClick);
        }
        this.mDialogFragmentPrompt.setMessage(msg);
        this.mDialogFragmentPrompt.show(getFragmentManager(), "mDialogFragmentPrompt");
    }

    /**
     * 删除账户
     * @param position
     */
    private void showDialogFragmentDelAccount(int position){
        this.curPosition = position;
        String msg = "";
        LoginSimpleInfoEntity loginSimple = this.mAccountManageAdapter.getItem(this.curPosition);
        if(ResourceHelper.isNotNull(loginSimple) && loginSimple.getVcardNo().equals(CurrentUser.getInstance().getVCardNo())){
            msg += "\n".concat(getString(R.string.confirm_delete_current_user));
            isCurrentUser = true;
        }else{
            msg = getString( R.string.confirm_delete_my_account, loginSimple.getDisplayName());
            isCurrentUser = false;
        }
        if(ResourceHelper.isNull(this.mDialogFragmentDelAccount)){
            CustomDialogFragment.DialogFragmentInterface dialogOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if(which == CustomDialogFragment.BUTTON_POSITIVE){
                        //TODO 删除登录账户记录
                        LoginSimpleInfoEntity loginSimple = mAccountManageAdapter.getItem(curPosition);
                        if(isCurrentUser){
                            CurrentUser.getInstance().logout();
                            mActivitySwitchTo.switchTo(WXEntryActivity.class, null);
                            mFragmentInteractionImpl.onActivityFinish();
                        }else{
                            mAccountManageAdapter.removeItem(loginSimple);
                        }
                    }
                }
            };
            this.mDialogFragmentDelAccount = DialogFragmentHelper.showCustomDialogFragment(R.string.del_account, msg, R.string.frg_text_cancel, R.string.frg_text_ok, dialogOnClick);
        }
        this.mDialogFragmentDelAccount.setMessage(msg);
        this.mDialogFragmentDelAccount.show(getFragmentManager(), "mDialogFragmentDelAccount");
    }

}
