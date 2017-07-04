package com.maya.android.vcard.ui.frg;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 登录：找回密码
 */
public class LoginFindPasswordFragment extends BaseFragment {

    public static final int GET_SMS_CODE_TIME = 100;

    private String mMobileNumber, mCheckCode, mPasswordNew, mPasswordNewAgain;
    private GetSMSCodeCount mGetSMSCodeCount;
    private EditText mEdtMobileNumber, mEdtMessCode, mEdtPassWordNew, mEdtPassWordNewRepeted;
    private Button mBtnMessCode;
    private TextView mTxvMobileCodeHint, mTxvPasswordHint;
    private LinearLayout mLilResetPassword;
    private CheckBox mChbShowPassword;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 点击事件
            switch (v.getId()) {
                case R.id.btn_submit:
                    //提交
//                    if(isValidMobile() && isValidMobileCode() && isValidPassword()){
//                        //TODO 提交
//                        mFragmentInteractionImpl.onFragmentInteraction(LoginFindNextPassWordFragment.class.getName(),null);
//                    }
                    if (isValidMobile() && isValidMobileCode()) {
                        //TODO 提交
                        mFragmentInteractionImpl.onFragmentInteraction(LoginFindNextPassWordFragment.class.getName(), null);
                    }
                    break;
                case R.id.btn_get_verification:
                    //获取验证码
                    if (isValidMobile()) {
                        if (Helper.isNull(mGetSMSCodeCount)) {
                            mGetSMSCodeCount = new GetSMSCodeCount(GET_SMS_CODE_TIME * 1000, 1000);
                        }
                        mGetSMSCodeCount.start();
                        setSMSCodeButtonEnabled(false);
                        //请求短信
                        ReqAndRespCenter.getInstance().getValidSMSFromService(mMobileNumber, Constants.GetSMSFlag.SMS_FLAG_RESET_PASSWORD);
                        //显示输入密码框
//                        mTxvPasswordHint.setVisibility(View.VISIBLE);
//                        mLilResetPassword.setVisibility(View.VISIBLE);
//                        mChbShowPassword.setVisibility(View.VISIBLE);
                        mBtnSubmit.setClickable(true);
                        mBtnSubmit.setEnabled(true);
                    }
                    break;
            }
        }
    };

    /**
     * CheckBosx触发监听
     */
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.chb_is_show_password:
                    if (isChecked) {
                        //显示密码
                        mEdtPassWordNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        mEdtPassWordNewRepeted.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //隐藏密码
                        mEdtPassWordNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mEdtPassWordNewRepeted.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    break;
            }
        }
    };
    private Button mBtnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_find_password, container, false);
        this.mEdtMobileNumber = (EditText) view.findViewById(R.id.edt_mobile_number);
        this.mEdtMessCode = (EditText) view.findViewById(R.id.edt_mess_code);
        this.mEdtPassWordNew = (EditText) view.findViewById(R.id.edt_password_new);
        this.mEdtPassWordNewRepeted = (EditText) view.findViewById(R.id.edt_password_new_repetition);
        this.mTxvMobileCodeHint = (TextView) view.findViewById(R.id.txv_fail_info_hint);
        this.mTxvPasswordHint = findView(view, R.id.txv_edt_password_hint);
        this.mLilResetPassword = findView(view, R.id.lil_edt_password);
        this.mChbShowPassword = (CheckBox) view.findViewById(R.id.chb_is_show_password);
        this.mBtnMessCode = (Button) view.findViewById(R.id.btn_get_verification);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        this.mBtnMessCode.setOnClickListener(this.mOnClickListener);
        mBtnSubmit.setOnClickListener(this.mOnClickListener);
        mChbShowPassword.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        addTextChange();
        return view;
    }
    //设置监听
    private void addTextChange() {
        mEdtMessCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isValidMobileCode()&&isValidMobile()){
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_2d659f));
                }else {
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_a1a1aa));
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mTitleAction.setActivityTitle(R.string.find_new_password, true);
        this.mTitleAction.setActivityTopLeftVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Helper.isNotNull(mGetSMSCodeCount)) {
            mGetSMSCodeCount.cancel();
        }
    }

    /**
     * 验证手机号码
     *
     * @return
     */
    private boolean isValidMobile() {
        this.mMobileNumber = this.mEdtMobileNumber.getText().toString();
        if (Helper.isEmpty(mMobileNumber)) {
            ActivityHelper.showToast(R.string.please_fill_in_the_mobile_phone_number);
            return false;
        } else if (!ResourceHelper.isValidMobile(this.mMobileNumber)) {
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
        this.mCheckCode = this.mEdtMessCode.getText().toString();
        if (Helper.isEmpty(this.mCheckCode)) {
            ActivityHelper.showToast(R.string.please_enter_the_verification_code);
            return false;
        }
        return true;
    }

    /**
     * 新密码验证
     *
     * @return
     */
    private boolean isValidPassword() {
        this.mPasswordNew = this.mEdtPassWordNew.getText().toString();
        this.mPasswordNewAgain = this.mEdtPassWordNewRepeted.getText().toString();
        if (Helper.isEmpty(this.mPasswordNew) || Helper.isEmpty(this.mPasswordNewAgain)) {
            ActivityHelper.showToast(R.string.please_fill_in_the_password);
            return false;
        }

        if (!this.mPasswordNew.equals(this.mPasswordNewAgain)) {
            ActivityHelper.showToast(R.string.entered_passwords_differ);
            return false;
        }
        return true;
    }

    /**
     * 定义短信验证码接收倒计时的内部类
     *
     * @Version: 1.0
     * @author: zheng_cz
     * @since: 2013-7-22 下午3:31:35
     */
    private class GetSMSCodeCount extends CountDownTimer {

        public GetSMSCodeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            setSMSCodeButtonEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mTxvMobileCodeHint.setText(Html.fromHtml(getString(R.string.fail_info_code_hint, "<font color='#ab290f'>" + millisUntilFinished / 1000 + "</font>")));
        }
    }

    /**
     * 短信验证触发按钮动态变化及相应功能
     *
     * @param isEnable
     */
    private void setSMSCodeButtonEnabled(boolean isEnable) {
        if (isEnable) {
            this.mBtnMessCode.setEnabled(true);
            this.mBtnMessCode.setTextColor(getResources().getColor(R.color.color_399c2f));
            this.mBtnMessCode.setBackgroundResource(R.drawable.bg_general_btn_green);
        } else {
            this.mBtnMessCode.setEnabled(false);
            this.mBtnMessCode.setTextColor(getResources().getColor(R.color.color_b7babc));
            this.mBtnMessCode.setBackgroundResource(R.drawable.bg_general_btn_gay);
            this.mTxvMobileCodeHint.setText(Html.fromHtml(getString(R.string.fail_info_code_hint, "<font color='#ab290f'>" + GET_SMS_CODE_TIME + "</font>")));
        }
    }
}
