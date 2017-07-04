package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.EmailBindJsonEntity;
import com.maya.android.vcard.entity.json.RegisterJsonEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.act.WebActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 注册：邮箱
 */
public class RegisterEmailFragment extends BaseFragment {
    /** key **/
    public static final String KEY_REGISTER_OR_BING_EMAIL = "key_register_or_bing_mobile";
    /** 绑定手机 **/
    public static final int CODE_BING_EMAIL = 30000003;
    /** 注册手机 **/
    public static final int CODE_REGISTER_EMAIL = 30000004;
    /** 当前指定显示页面 **/
    private int curIntentCode;
    private String mEmail, mPassword, mPasswordAgain;
//    private TextView mTxvEnterEmailHint;
    private TextView mTxvMobileRegister;
    private EditText mEdtEnterEmail, mEdtPassword, mEdtPasswordAgain;
    private View mViewDividerPassword, mViewDivierPasswordAgain;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.txv_protocol:
                    //协议
                    Intent intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_INTENT_CODE, Constants.IntentSet.INTENT_CODE_STATEMENT);
                    mActivitySwitchTo.switchTo(WebActivity.class, intent);
                    break;
                case R.id.txv_mobile_register:
                    //跳转到手机注册
                    mFragmentInteractionImpl.onActivityBackPressed();
                    break;
                case R.id.btn_send_the_activation_email:
                    //邮箱激活
                    if(isValidEmail()){
                        //网络验证
                        if(!NetworkHelper.isNetworkAvailable(getActivity())){
                            ActivityHelper.showToast(R.string.no_network);
                            return;
                        }
                        ActivityHelper.showProgressDialog(getActivity(), R.string.deal_with_data);
                        if(curIntentCode == CODE_REGISTER_EMAIL){
                            if(isValidPassword()){
                                //邮箱注册
                                requestRegisterEmail();
                            }
                        }else{
                            //绑定邮箱
                            requestBindEmail();
                        }
                     }
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
        switch (tag){
            case Constants.CommandRequestTag.CMD_REQUEST_REGISTER_EMAIL:
                //邮箱注册
                ActivityHelper.showToast(msgInfo);
                //TODO 切换到登录页
                break;
            case Constants.CommandRequestTag.CMD_REQUEST_BIND_EMAIL:
                //邮箱绑定
                super.mFragmentInteractionImpl.onActivityBackPressed();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_email, container, false);
//        this.mTxvEnterEmailHint = (TextView) view.findViewById(R.id.txv_email_hint);
        this.mTxvMobileRegister = (TextView) view.findViewById(R.id.txv_mobile_register);
        TextView mTxvProtocol = (TextView) view.findViewById(R.id.txv_protocol);
        this.mEdtEnterEmail = (EditText) view.findViewById(R.id.edt_email);
        this.mEdtPassword = (EditText) view.findViewById(R.id.edt_password);
        this.mEdtPasswordAgain = (EditText) view.findViewById(R.id.edt_password_again);
        Button mBtnSubmit = (Button) view.findViewById(R.id.btn_send_the_activation_email);
        this.mTxvMobileRegister.setOnClickListener(this.mOnClickListener);
        mTxvProtocol.setOnClickListener(this.mOnClickListener);
        mBtnSubmit.setOnClickListener(this.mOnClickListener);
        this.mViewDividerPassword = (View) view.findViewById(R.id.view_password_divider);
        this.mViewDivierPasswordAgain = (View) view.findViewById(R.id.view_password_again_divider);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.curIntentCode = CODE_REGISTER_EMAIL;
        Bundle bundle = getArguments();
        if(ResourceHelper.isNotNull(bundle)){
            this.curIntentCode = bundle.getInt(KEY_REGISTER_OR_BING_EMAIL, CODE_REGISTER_EMAIL);
        }
        super.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        super.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        super.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        switch (this.curIntentCode){
            case CODE_REGISTER_EMAIL:
                //邮箱注册
                super.mTitleAction.setActivityTitle(R.string.e_mail_register, false);
                break;
            case CODE_BING_EMAIL:
                //邮箱验证
                super.mTitleAction.setActivityTitle(R.string.bind_email, false);
                this.mTxvMobileRegister.setVisibility(View.GONE);
                this.mViewDividerPassword.setVisibility(View.GONE);
                this.mViewDivierPasswordAgain.setVisibility(View.GONE);
                this.mEdtPassword.setVisibility(View.GONE);
                this.mEdtPasswordAgain.setVisibility(View.GONE);
                break;
        }
        this.mEdtEnterEmail.addTextChangedListener(this.mEdtEnterEmailWatcher);
        this.mEdtPassword.addTextChangedListener(this.mEdtPasswordWatcher);
        this.mEdtPasswordAgain.addTextChangedListener(this.mEdtPasswordAgainWatcher);
    }

    /**
     * 邮箱验证
     * @return
     */
    private boolean isValidEmail(){
        this.mEmail = this.mEdtEnterEmail.getText().toString();
        if(Helper.isEmpty(this.mEmail)){
            ActivityHelper.showToast(R.string.please_enter_email_address);
            return false;
        }
        if(!ResourceHelper.isValidEmail(this.mEmail)){
            ActivityHelper.showToast(R.string.please_enter_correct_email_address);
            return false;
        }
         return true;
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
     * 邮箱
     */
    private TextWatcher mEdtEnterEmailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(ResourceHelper.isValidEmail(s.toString())){
                mEdtEnterEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.img_general_agree, 0);
            }else{
                mEdtEnterEmail.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
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

    /**
     * 请求邮箱注册
     */
    private void requestRegisterEmail(){
        RegisterJsonEntity jsonEntity = new RegisterJsonEntity(this.mEmail, this.mPassword, RegisterJsonEntity.REGISTER_METHOD_EMAIL, "");
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        String json = JsonHelper.toJson(jsonEntity, RegisterJsonEntity.class);
        String requestUrl = ProjectHelper.fillRequestURL(urlEntity.getVcardRegister());
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_REGISTER_EMAIL, requestUrl , null, json, this);
    }

    /**
     * 邮箱绑定
     */
    private void requestBindEmail(){
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        EmailBindJsonEntity emailBind = new EmailBindJsonEntity();
        emailBind.setEmail(this.mEmail);
        String json = JsonHelper.toJson(emailBind, EmailBindJsonEntity.class);
        String requestUrl = ProjectHelper.fillRequestURL(urlEntity.getBindEmail());
        String token =  CurrentUser.getInstance().getToken();
        ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REQUEST_BIND_EMAIL, requestUrl, token, json, this);
    }

}
