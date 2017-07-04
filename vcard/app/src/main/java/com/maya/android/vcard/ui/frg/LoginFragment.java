package com.maya.android.vcard.ui.frg;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.entity.LoginSimpleInfoEntity;
import com.maya.android.vcard.entity.LoginSimpleInfoListEntity;
import com.maya.android.vcard.entity.json.LoginJsonEntity;
import com.maya.android.vcard.ui.act.RegisterActivity;
import com.maya.android.vcard.ui.base.BaseFragment;
import com.maya.android.vcard.ui.impl.UserLoginImpl;
import com.maya.android.vcard.util.ButtonHelper;
import com.maya.android.vcard.util.ResourceHelper;

import java.util.ArrayList;

/**
 * 登录页
 */
public class LoginFragment extends BaseFragment {

    private String mAccount, mPassword;

    /**
     * 是否展示返回按钮
     **/
    private boolean isShowBackPressed = false;
    private UserLoginImpl mUserLogin = null;
    private EditText mEdtAccount, mEdtPassword;
    private CheckBox mChbRememberPassword;
    private Button mBtnSubmit;
    private CurrentUser mCurrentUser = CurrentUser.getInstance();
    private LoginSimpleInfoListEntity mLoginSimpleInfoListEntity;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 登录方式
            switch (v.getId()) {
                case R.id.txv_forget_password_solution:
                    //密码找回
                    mFragmentInteractionImpl.onFragmentInteraction(LoginFindPasswordFragment.class.getName(), null);
                    break;
                case R.id.btn_login_submit:
                    //普通登录
                    ButtonHelper.setButtonEnableDelayed(mBtnSubmit);
                    if (isValidAccount() && isValidPassword()) {
                        LoginJsonEntity loginJsonEntity = new LoginJsonEntity(mAccount, mPassword, "", CurrentUser.getInstance().getClientId());
                        mUserLogin.loginByCommon(loginJsonEntity, mChbRememberPassword.isChecked());
                    }
                    break;
                case R.id.txv_quick_register_account_new:
                    //快速注册
                    mActivitySwitchTo.switchTo(RegisterActivity.class, null);
                    break;
                case R.id.txv_login_way_qq:
                    //QQ登录
                    mUserLogin.loginByQQ();
                    break;
                case R.id.txv_login_way_sina_weibo:
                    //新浪微博登录
                    mUserLogin.loginBySina();
                    break;
                case R.id.txv_login_way_wechat:
                    //微信登录
                    mUserLogin.loginByWeixin();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (Helper.isNotNull(args)) {
            this.isShowBackPressed = args.getBoolean(Constants.IntentSet.KEY_LOGIN_FRG_IS_SHOW_BACK, false);
        }
        this.mLoginSimpleInfoListEntity = this.mCurrentUser.getLoginSimpleInfoList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mUserLogin = (UserLoginImpl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement UserLoginImpl");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        this.mEdtAccount = (EditText) rootView.findViewById(R.id.edt_login_account);
        this.mEdtPassword = (EditText) rootView.findViewById(R.id.edt_login_password);
        this.mChbRememberPassword = (CheckBox) rootView.findViewById(R.id.chb_is_remember_password);
        this.mBtnSubmit = (Button) rootView.findViewById(R.id.btn_login_submit);
        TextView mTxvQuickRegister = (TextView) rootView.findViewById(R.id.txv_quick_register_account_new);
        TextView mTxvLoginWayQQ = (TextView) rootView.findViewById(R.id.txv_login_way_qq);
        TextView mTxvLoginWaySinaWeibo = (TextView) rootView.findViewById(R.id.txv_login_way_sina_weibo);
        TextView mTxvLoginWayWeChat = (TextView) rootView.findViewById(R.id.txv_login_way_wechat);
        TextView mTxvForgetPassword = (TextView) rootView.findViewById(R.id.txv_forget_password_solution);
        this.mBtnSubmit.setOnClickListener(this.mOnClickListener);
        mTxvQuickRegister.setOnClickListener(this.mOnClickListener);
        mTxvLoginWayQQ.setOnClickListener(this.mOnClickListener);
        mTxvLoginWaySinaWeibo.setOnClickListener(this.mOnClickListener);
        mTxvLoginWayWeChat.setOnClickListener(this.mOnClickListener);
        mTxvForgetPassword.setOnClickListener(this.mOnClickListener);
        // 设置下划线
        SpannableString spanReg = new SpannableString(mTxvQuickRegister.getText());
        spanReg.setSpan(new UnderlineSpan(), 0, spanReg.length(), 0);
        mTxvQuickRegister.setText(spanReg);


        addTextChange();


        return rootView;
    }

    //设置文本输入完成的监听事件
    private void addTextChange() {
        mEdtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isValidAccount() && isValidPassword()){
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_2d659f));
                }else {
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_777777));
                }
            }
        });
        mEdtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>6 && isValidAccount()){
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_2d659f));
                }else {
                    mBtnSubmit.setTextColor(getResources().getColor(R.color.color_777777));
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int visibility = this.isShowBackPressed ? View.VISIBLE : View.GONE;
        this.mTitleAction.setActivityTopLeftVisibility(visibility);
        this.mTitleAction.setActivityTitle(R.string.frg_text_login, !this.isShowBackPressed);
        this.mTitleAction.setActivityTopLeftVisibility(View.GONE);
        this.mTitleAction.setActivityTopRightImvVisibility(View.GONE);
        //账户切换自动登录或者普通登录
        boolean isAutoLogin = false;
        boolean isRememberAutoLoginPassword = false;
        boolean isRememberPassword = true;
        Bundle args = getArguments();
        if (Helper.isNotNull(args)) {
            isAutoLogin = args.getBoolean(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN, false);
            this.mAccount = args.getString(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN_VCARD_NO, "");
            String encodePassword = args.getString(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN_PASSWORD, "");
            this.mPassword = Helper.decryptByAES(Helper.parseHexStr2Byte(encodePassword), Constants.AESKey.KEY_USER_PASSWORD);
            isRememberAutoLoginPassword = args.getBoolean(Constants.IntentSet.KEY_LOGIN_FRG_IS_AUTO_LOGIN_REMEMBER_PASSWORD, false);
            if (isRememberAutoLoginPassword) {
                LoginJsonEntity loginJsonEntity = new LoginJsonEntity(mAccount, mPassword, "", CurrentUser.getInstance().getClientId());
                mUserLogin.loginByCommon(loginJsonEntity, this.mChbRememberPassword.isChecked());
            }
        }
        if (!isAutoLogin) {
            if (ResourceHelper.isNotNull(this.mLoginSimpleInfoListEntity)) {
                ArrayList<LoginSimpleInfoEntity> loginInfoList = this.mLoginSimpleInfoListEntity.getLoginSimpleInfoEntityList();
                if (Helper.isNull(loginInfoList) || loginInfoList.size() == 0) {
                    this.mAccount = ResourceHelper.getConfigInfoFromSDCard();
                } else {
                    LoginSimpleInfoEntity entity = loginInfoList.get(0);
                    this.mAccount = entity.getVcardNo();
                    String encodePassword = entity.getPassword();
                    this.mPassword = Helper.decryptByAES(Helper.parseHexStr2Byte(encodePassword), Constants.AESKey.KEY_USER_PASSWORD);
                    isRememberPassword = entity.isRememberPassword();
                }
            }
        }
        this.mEdtAccount.setText(this.mAccount);
        if (isRememberPassword || isRememberAutoLoginPassword) {
            this.mEdtPassword.setText(this.mPassword);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mUserLogin = null;
    }

    /**
     * 账户验证
     *
     * @return
     */
    private boolean isValidAccount() {
        this.mAccount = this.mEdtAccount.getText().toString();
        if (Helper.isEmpty(this.mAccount)) {
            ActivityHelper.showToast(R.string.please_enter_user_name);
            return false;
        }
        if (!ResourceHelper.isValidEmail(this.mAccount) && !ResourceHelper.isValidNumberType(this.mAccount)) {
            ActivityHelper.showToast(R.string.please_enter_correct_user_name);
            return false;
        }
        return true;
    }

    /**
     * 新密码验证(判空)
     *
     * @return
     */
    private boolean isValidPassword() {
        this.mPassword = this.mEdtPassword.getText().toString();
        if (Helper.isEmpty(this.mPassword)) {
            ActivityHelper.showToast(R.string.please_fill_in_the_password);
            return false;
        }
        if (this.mPassword.length() < 6) {
            ActivityHelper.showToast(R.string.please_enter_no_less_than_six_char);
            return false;
        }
        return true;
    }

}
