package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.ChangePasswordJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ProjectHelper;

/**
 * 设置：账号设置：修改密码
 */
public class SettingAccountChangePasswordFragment extends BaseFragment {
    private String mPasswordOld, mPasswordNew, mPasswordNewAgain;
    private EditText mEdtOldPwd, mEdtNewPwd, mEdtNewPwdAgain;
    private Button mBtnSubmitOk;

    private View.OnClickListener mOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //确定
                case R.id.btn_setting_account_change_pwd_ok:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmitOk);
                    if(idValidPssswordOld() && isValidPasswordNew()){
                        // 确定
                        requestChangePassword();
                    }
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.chb_setting_account_change_pwd_is_show:
                    if (isChecked) {
                        // 是 显示密码
                        mEdtNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mEdtNewPwdAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        // 否 隐藏密码
                        mEdtNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mEdtNewPwdAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting_account_change_password, container, false);
        this.mEdtOldPwd = (EditText) view.findViewById(R.id.edt_change_pwd_current_input);
        this.mEdtNewPwd = (EditText) view.findViewById(R.id.edt_change_pwd_input_new);
        this.mEdtNewPwdAgain = (EditText) view.findViewById(R.id.edt_setting_change_pwd_input_again);
        CheckBox mChbIsShowPwd = (CheckBox) view.findViewById(R.id.chb_setting_account_change_pwd_is_show);
        this.mBtnSubmitOk = (Button) view.findViewById(R.id.btn_setting_account_change_pwd_ok);
        this.mBtnSubmitOk.setOnClickListener(this.mOnClickListener);
        mChbIsShowPwd.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.frg_setting_account_alert_pwd, false);
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        switch(tag){
            case Constants.CommandRequestTag.CMD_CHANGE_PASSWORD:
                ActivityHelper.showToast(R.string.change_password_success);
                mFragmentInteractionImpl.onActivityBackPressed();
                break;
        }
    }
    /**
     * 旧密码验证
     * @return
     */
    private boolean idValidPssswordOld(){
        this.mPasswordOld = this.mEdtOldPwd.getText().toString();
        if(Helper.isEmpty(this.mPasswordOld)){
            ActivityHelper.showToast(R.string.please_fill_in_the_password_old);
            return false;
        }
        if(this.mPasswordOld.length() < 6){
            ActivityHelper.showToast(R.string.please_enter_the_password_6_12_old);
            return false;
        }
        return true;
    }
    /**
     * 新密码验证
     * @return
     */
    private boolean isValidPasswordNew(){
        this.mPasswordNew = this.mEdtNewPwd.getText().toString();
        this.mPasswordNewAgain = this.mEdtNewPwdAgain.getText().toString();
        if(Helper.isEmpty(this.mPasswordNew) || Helper.isEmpty(this.mPasswordNewAgain)){
            ActivityHelper.showToast(R.string.please_fill_in_the_password_new);
            return false;
        }

        if(this.mPasswordNew.length() < 6 || this.mPasswordNewAgain.length()< 6){
            ActivityHelper.showToast(R.string.please_enter_the_password_6_12_new);
            return false;
        }

       if(!this.mPasswordNew.equals(this.mPasswordNewAgain)){
            ActivityHelper.showToast(R.string.entered_passwords_differ);
            return false;
        }
        return true;
    }
    /**
     * 请求修改密码
     */
    private void requestChangePassword(){
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        if(Helper.isNotNull(urlEntity)){
            ChangePasswordJsonEntity jsonEntity = new ChangePasswordJsonEntity(this.mPasswordOld, this.mPasswordNew, 1);
            String url = ProjectHelper.fillRequestURL(urlEntity.getPawsswordUpdate());
            String json = JsonHelper.toJson(jsonEntity, ChangePasswordJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_CHANGE_PASSWORD, url, json, this);
        }
    }
}
