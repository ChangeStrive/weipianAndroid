package com.maya.android.vcard.ui.frg;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
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
import com.maya.android.utils.LogHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.LoginJsonEntity;
import com.maya.android.vcard.entity.json.RegisterJsonEntity;
import com.maya.android.vcard.entity.result.LoginResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.ui.act.WebActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;

import org.json.JSONObject;

/**
 * 注册：手机
 */
public class RegisterMobileFragment extends BaseFragment {
    private static final String TAG = RegisterMobileFragment.class.getSimpleName();
    /**
     * key
     **/
    public static final String KEY_REGISTER_OR_BING_MOBILE = "key_register_or_bing_mobile";
    /**
     * 绑定手机
     **/
    public static final int CODE_BING_MOBILE = 30000001;
    /**
     * 注册手机
     **/
    public static final int CODE_REGISTER_MOBILE = 30000002;
    /**
     * 验证码时长
     */
    public static final int GET_SMS_CODE_TIME = 100;
    /**
     * 当前指定显示页面
     **/
    private int curIntentCode;
    private String mMobileNum, mCheckCode;
    private TextView mTxvSelectedArea, mTxvMobileCodeHint;
    //mTxvEmailRegister;
    private EditText mEdtMobileNumber, mEdtMobileCod;
    private Button mBtnMobileCode, mBtnSubmitRegister;
    private CustomDialogFragment mDialogFragmentValidate, mDialogFragmentResult;
    private GetSMSCodeCountDownTimer mGetSMSCodeCountDownTimer;
    private CurrentUser mCurrentUser = CurrentUser.getInstance();

    /**
     * 手机号码输入监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (ResourceHelper.isValidMobile(s.toString().trim())) {
                mEdtMobileNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.img_general_agree, 0);
            } else {
                mEdtMobileNumber.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
    };

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.txv_email_register:
//                    //邮箱注册
//                    mFragmentInteractionImpl.onFragmentInteraction(RegisterEmailFragment.class.getName(), null);
//                    break;
                case R.id.txv_protocol:
                    //阅读协议
                    Intent intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_INTENT_CODE, Constants.IntentSet.INTENT_CODE_STATEMENT);
                    mActivitySwitchTo.switchTo(WebActivity.class, intent);
                    break;
                case R.id.btn_get_verification:
                    //获取验证码
                    if (isValidMobile()) {
                        if (Helper.isNull(mGetSMSCodeCountDownTimer)) {
                            mGetSMSCodeCountDownTimer = new GetSMSCodeCountDownTimer(GET_SMS_CODE_TIME * 1000, 1000);
                        }
                        mGetSMSCodeCountDownTimer.start();
                        setSMSCodeButtonEnabled(false);
                        //请求获取验证码
                        ReqAndRespCenter.getInstance().getValidSMSFromService(mMobileNum, Constants.GetSMSFlag.SMS_FLAG_REGISTER);
                    }
                    break;
                case R.id.btn_complete_the_sign:
                    //完成注册
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmitRegister);
                    if (isValidMobile() && isValidMobileCode() && isPassword()) {
                        //开始注册
                        requestRegister();
                    }
                    break;
                case R.id.txv_area_selected:
                    //选择区域
                    //do nothing now
                    break;

            }
        }
    };
    private EditText edPassword;
    private String pwd;

    private boolean isPassword() {
        pwd = edPassword.getText().toString();
        if (pwd == null || "".equals(pwd)) {
            ActivityHelper.showToast(R.string.please_fill_in_the_password);
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_mobile, container, false);
        this.mTxvSelectedArea = (TextView) view.findViewById(R.id.txv_area_selected);
        //this.mTxvEmailRegister = (TextView) view.findViewById(R.id.txv_email_register);
        TextView mTxvProtocol = (TextView) view.findViewById(R.id.txv_protocol);
        this.mEdtMobileNumber = (EditText) view.findViewById(R.id.edt_mobile_number);
        this.mEdtMobileCod = (EditText) view.findViewById(R.id.edt_mobile_code);
        this.mBtnMobileCode = (Button) view.findViewById(R.id.btn_get_verification);
        this.mBtnSubmitRegister = (Button) view.findViewById(R.id.btn_complete_the_sign);//下一步，完成注册
        this.mTxvMobileCodeHint = (TextView) view.findViewById(R.id.txv_fail_info_hint);
        edPassword = (EditText) view.findViewById(R.id.edt_mobile_password);
        // mTxvEmailRegister.setOnClickListener(this.mOnClickListener);
        mTxvProtocol.setOnClickListener(this.mOnClickListener);
        this.mTxvSelectedArea.setOnClickListener(this.mOnClickListener);
        this.mBtnMobileCode.setOnClickListener(this.mOnClickListener);
        this.mBtnSubmitRegister.setOnClickListener(this.mOnClickListener);
        addTextChange();
        return view;
    }

    //设置监听
    private void addTextChange() {
        mEdtMobileCod.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isPassword() && isValidMobile() &&s.length()==4) {
                    mBtnSubmitRegister.setTextColor(getResources().getColor(R.color.color_2d659f));
                } else {
                    mBtnSubmitRegister.setTextColor(getResources().getColor(R.color.color_a1a1aa));
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
        this.mTitleAction.setActivityTopRightTxvVisibility(View.GONE);
        this.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        this.curIntentCode = CODE_REGISTER_MOBILE;
        Bundle bundle = getArguments();
        if (ResourceHelper.isNotNull(bundle)) {
            this.curIntentCode = bundle.getInt(KEY_REGISTER_OR_BING_MOBILE, CODE_REGISTER_MOBILE);
        }
        switch (this.curIntentCode) {
            case CODE_REGISTER_MOBILE:
                //手机注册
                this.mTitleAction.setActivityTitle(R.string.fast_register, true);
                this.mBtnSubmitRegister.setText(R.string.next);
                //this.mTxvEmailRegister.setVisibility(View.VISIBLE);
                break;
            case CODE_BING_MOBILE:
                //手机绑定
                super.mTitleAction.setActivityTitle(R.string.mobile_bind, true);
                this.mBtnSubmitRegister.setText(R.string.immediate_activation);
                // this.mTxvEmailRegister.setVisibility(View.GONE);
                break;
        }
        this.mEdtMobileNumber.addTextChangedListener(this.mTextWatcher);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Helper.isNotNull(mGetSMSCodeCountDownTimer)) {
            mGetSMSCodeCountDownTimer.cancel();
        }
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        switch (tag) {
            case Constants.CommandRequestTag.CMD_REGISTER_MOBILE:
                //注册
                if (!super.onCommandCallback2(tag, commandResult, objects)) {
                    LogHelper.d(TAG, "注册成功");
                    this.requestLogin();
                    return true;
                }
                return false;
            case Constants.CommandRequestTag.CMD_LOGIN_NORMAL:
                //登录
                if (!super.onCommandCallback2(tag, commandResult, objects)) {
                    LoginResultEntity resultEntity = JsonHelper.fromJson(commandResult, LoginResultEntity.class);
                    // 设置为登录状态
                    this.mCurrentUser.setLogin(true);
                    // 赋值Token
                    this.mCurrentUser.setToken(resultEntity.getAccessToken());
                    //赋值微片号
                    String vcardNo = resultEntity.getVcardNumber();
                    this.mCurrentUser.setVCardNo(vcardNo);
                    //微片号写入到SD卡
                    ResourceHelper.putConfigInfo2SDCard(vcardNo);
                    //设置用户必须修改密码
                    // this.mCurrentUser.setNeedResetPassword(true);
                    ActivityHelper.closeProgressDialog();
//                    this.mFragmentInteractionImpl.onFragmentInteraction(RegisterSuccessChangePasswordFragment.class.getName(), null);
                    this.mCurrentUser.setNeedResetPassword(false);
                    this.mCurrentUser.setNeedCompleteInfo(true);
                    this.mFragmentInteractionImpl.onFragmentInteraction(RegisterSuccessInputBaseInfoFragment.class.getName(), null);
                    return true;
                }
                return false;
        }
        return false;
    }

    /**
     * 验证手机号码
     *
     * @return
     */
    private boolean isValidMobile() {
        this.mMobileNum = this.mEdtMobileNumber.getText().toString();
        if (Helper.isEmpty(mMobileNum)) {
            ActivityHelper.showToast(R.string.please_fill_in_the_mobile_phone_number);
            return false;
        } else if (!ResourceHelper.isValidMobile(this.mMobileNum)) {
            ActivityHelper.showToast(R.string.please_fill_in_the_correct_mobile_phone_number);
            return false;
        }
        return true;
    }

    /**
     * 验证码判空
     *
     * @return
     */
    private boolean isValidMobileCode() {
        this.mCheckCode = this.mEdtMobileCod.getText().toString();
        if (Helper.isNull(this.mCheckCode) || "".equals(this.mCheckCode)) {
            ActivityHelper.showToast(R.string.please_enter_the_verification_code);
            return false;
        }
        return true;
    }

    /**
     * 短信验证触发事件
     *
     * @param isEnable
     */
    private void setSMSCodeButtonEnabled(boolean isEnable) {

        if (isEnable) {
            mBtnMobileCode.setEnabled(true);
            mBtnMobileCode.setTextColor(getResources().getColor(R.color.color_399c2f));
            mBtnMobileCode.setBackgroundResource(R.drawable.bg_general_btn_green);
        } else {
            mBtnMobileCode.setEnabled(false);
            mBtnMobileCode.setTextColor(getResources().getColor(R.color.color_b7babc));
            mBtnMobileCode.setBackgroundResource(R.drawable.bg_general_btn_gay);
            mTxvMobileCodeHint.setText(Html.fromHtml(getString(R.string.fail_info_code_hint, "<font color='#ab290f'>" + GET_SMS_CODE_TIME + "</font>")));
        }
    }

    /**
     * 注册失败 云端验证 结果处理
     *
     * @param phoneNum
     */
    private void showDlgResultCloudValidate(String phoneNum) {
        if (Helper.isNull(this.mDialogFragmentValidate)) {
            CustomDialogFragment.DialogFragmentInterface mDialogFragmentOnclick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if (which == CustomDialogFragment.BUTTON_POSITIVE) {
                        //TODO 云端验证
                    }
                }
            };
            this.mDialogFragmentValidate = DialogFragmentHelper.showCustomDialogFragment(R.string.cloud_rust_authority, R.string.cloud_rust_authority_hint, R.string.frg_text_cancel, R.string.cloud_rust_authority, mDialogFragmentOnclick);
            this.mDialogFragmentValidate.show(getFragmentManager(), "mDialogFragmentValidate");
        } else {
            this.mDialogFragmentValidate.show(getFragmentManager(), "mDialogFragmentValidate");
        }
    }

    /**
     * 邮箱注册 结果处理
     */
    private void showDlgResultEmail() {
        if (Helper.isNull(this.mDialogFragmentResult)) {
            CustomDialogFragment.DialogFragmentInterface mDialogFragmentOnclick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    if (which == CustomDialogFragment.BUTTON_POSITIVE) {
                        //TODO 邮箱注册
                    }
                }
            };
            this.mDialogFragmentResult = DialogFragmentHelper.showCustomDialogFragment(R.string.warm_prompt, R.string.cloud_rust_authority_fail_hint, R.string.frg_text_cancel, R.string.frg_text_ok, mDialogFragmentOnclick);
            this.mDialogFragmentResult.show(getFragmentManager(), "mDialogFragmentResult");
        } else {

        }
        this.mDialogFragmentResult.show(getFragmentManager(), "mDialogFragmentResult");
    }

    private void requestRegister() {
        ActivityHelper.showProgressDialog(getActivity(), R.string.doing_register_please_wait);
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        if (Helper.isNotNull(urlEntity) && isPassword()) {
//            RegisterJsonEntity jsonEntity = new RegisterJsonEntity(
//                    this.mMobileNum, Constants.DefaultUser.DEFAULT_PASSWORD,
//                    RegisterJsonEntity.REGISTER_METHOD_MOBILE, this.mCheckCode);
            RegisterJsonEntity jsonEntity = new RegisterJsonEntity(
                    this.mMobileNum, pwd,
                    RegisterJsonEntity.REGISTER_METHOD_MOBILE, this.mCheckCode);
            String url = ProjectHelper.fillRequestURL(urlEntity.getVcardRegister());
            String json = JsonHelper.toJson(jsonEntity, RegisterJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_REGISTER_MOBILE, url, json, this);
        }
    }

    private void requestLogin() {
        URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
        if (Helper.isNotNull(urlEntity)) {
//            LoginJsonEntity jsonEntity = new LoginJsonEntity(this.mMobileNum,
//                    Constants.DefaultUser.DEFAULT_PASSWORD, "",
//                    CurrentUser.getInstance().getClientId());
            LoginJsonEntity jsonEntity = new LoginJsonEntity(this.mMobileNum,
                    pwd, "", CurrentUser.getInstance().getClientId());
            String url = ProjectHelper.fillRequestURL(urlEntity.getLoginNormal());
            String json = JsonHelper.toJson(jsonEntity, LoginJsonEntity.class);
            ReqAndRespCenter.getInstance().postForResult(Constants.CommandRequestTag.CMD_LOGIN_NORMAL, url, json, this);
        }
    }
    //region 子类

    /**
     * 倒计时
     */
    private class GetSMSCodeCountDownTimer extends CountDownTimer {

        public GetSMSCodeCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            setSMSCodeButtonEnabled(true);
//            TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
//            String phoneNum = tm.getLine1Number();
//            if (Helper.isNotEmpty(phoneNum)) {
//                phoneNum = phoneNum.trim();
//                int len = phoneNum.length();
//                if (len > 11) {
//                    phoneNum = phoneNum.substring(len - 12, len - 1);
//                }
//            } else {
//                phoneNum = mMobileNum;
//            }
//            if (Helper.isNotNull(tm) && Helper.isNotEmpty(phoneNum)) {
//                // 获取到本机号码 后 弹出云端注册提示
//                showDlgResultCloudValidate(phoneNum);
//            } else {
//                // 未获取到本机号码,提示邮箱注册
//                showDlgResultEmail();
//            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTxvMobileCodeHint.setText(Html.fromHtml(getString(R.string.fail_info_code_hint, "<font color='#ab290f'>" + millisUntilFinished / 1000 + "</font>")));
        }
    }
    //endregion 子类
}
