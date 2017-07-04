package com.maya.android.vcard.ui.frg;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

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

import org.json.JSONObject;

/**
 * 注册成功：修改密码
 */
public class RegisterSuccessChangePasswordFragment extends BaseFragment  {

    private String mPassword, mPasswordAgain;

    private EditText mEdtPassword, mEdtPasswordAgain;
    private Button mBtnSubmit;
    private CurrentUser mCurrentUser = CurrentUser.getInstance();

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_submit:
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    if(isValidPassword()){
                        // 下一步
                        requestChangePassword();
                    }
                    break;
            }
        }
    };
    private TextWatcher mEdtPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().trim().length() >= 6){
                mEdtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.img_general_agree, 0);
            }else{
                mEdtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
    };

    private TextWatcher mEdtPasswordAgainWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String passwordAgain = s.toString().trim();
            if(passwordAgain.length() >= 6 && passwordAgain.equals(mEdtPassword.getText().toString().trim())){
                mEdtPasswordAgain.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.img_general_agree, 0);
            }else{
                mEdtPasswordAgain.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.chb_show_password:
                    if (isChecked) {
                        // 是 显示密码
                        mEdtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mEdtPasswordAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        // 否 隐藏密码
                        mEdtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mEdtPasswordAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_success_change_password, container, false);
        this.mEdtPassword = (EditText) view.findViewById(R.id.edt_enter_password);
        this.mEdtPasswordAgain = (EditText) view.findViewById(R.id.edt_enter_password_again);
        TextView txvVCandNo = findView(view, R.id.txv_vcard_number);
        txvVCandNo.setText(CurrentUser.getInstance().getVCardNo());
        CheckBox mChbShowPassword = (CheckBox) view.findViewById(R.id.chb_show_password);
        this.mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        mChbShowPassword.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.mTitleAction.setActivityTitle(R.string.register_success, true);
        //设置Activity不能回退
        super.mFragmentInteractionImpl.onActivitySetBackPressed(false);
        super.mTitleAction.setActivityTopLeftVisibility(View.GONE);
        this.mEdtPassword.addTextChangedListener(this.mEdtPasswordWatcher);
        this.mEdtPasswordAgain.addTextChangedListener(this.mEdtPasswordAgainWatcher);
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        switch (tag){
//            case Constants.CommandRequestTag.CMD_CHANGE_PASSWORD:
//                if(!super.onCommandCallback2(tag, commandResult, objects)){
//                    this.mCurrentUser.setNeedResetPassword(false);
//                    this.mCurrentUser.setNeedCompleteInfo(true);
//                    mFragmentInteractionImpl.onFragmentInteraction(RegisterSuccessInputBaseInfoFragment.class.getName(), null);
//                    return true;
//                }
//                return false;
        }
        return false;
    }

    @Override
    protected void onResponseSuccess(int tag, String msgInfo) {
        super.onResponseSuccess(tag, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_CHANGE_PASSWORD:
                this.mCurrentUser.setNeedResetPassword(false);
                this.mCurrentUser.setNeedCompleteInfo(true);
                mFragmentInteractionImpl.onFragmentInteraction(RegisterSuccessInputBaseInfoFragment.class.getName(), null);
                break;
        }
    }

    @Override
    protected void onBackPressed() {
        super.onBackPressed();
        ActivityHelper.showToast(R.string.please_reset_password_first);
    }

    /**
     * 新密码验证
     * @return
     */
    private boolean isValidPassword(){
        this.mPassword = this.mEdtPassword.getText().toString();
        this.mPasswordAgain = this.mEdtPasswordAgain.getText().toString();
        if(Helper.isEmpty(this.mPassword) || Helper.isEmpty(this.mPasswordAgain)){
            ActivityHelper.showToast(R.string.please_fill_in_the_password);
            return false;
        }

        if(this.mPassword.length() < 6 || this.mPasswordAgain.length()< 6){
            ActivityHelper.showToast(R.string.please_enter_no_less_than_six_char);
            return false;
        }

        if(!this.mPassword.equals(this.mPasswordAgain)){
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
            ChangePasswordJsonEntity jsonEntity = new ChangePasswordJsonEntity(Constants.DefaultUser.DEFAULT_PASSWORD, this.mPassword, 1);
            String url = ProjectHelper.fillRequestURL(urlEntity.getPawsswordUpdate());
            String json = JsonHelper.toJson(jsonEntity, ChangePasswordJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_CHANGE_PASSWORD, url, json, this);
        }
    }

}
