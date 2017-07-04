package com.maya.android.vcard.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.LoadingFragment;
import com.maya.android.vcard.ui.frg.RegisterSuccessChangePasswordFragment;
import com.maya.android.vcard.ui.frg.RegisterSuccessInputBaseInfoFragment;
import com.maya.android.vcard.ui.frg.WelcomeFragment;
import com.maya.android.vcard.util.DBLocationHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.wxapi.WXEntryActivity;

/**
 * 过渡页与欢迎页
 */
public class LoadingAndWelcomeActivity extends BaseActivity {

    private static final String TAG = LoadingAndWelcomeActivity.class.getSimpleName();

    private static final int TIME_OUT = 5000;
    private static final int WHAT_SWITCH_TO = 10001;

//    private PreferencesHelper mPreferences = PreferencesHelper.getInstance(Constants.Preferences.KEY_NAME_SOFTWARE);
    private PreferencesHelper mPreferences = PreferencesHelper.getInstance();

    /** 当前版本Code **/
    private int mCurrentVersion = ActivityHelper.getCurrentVersion();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WHAT_SWITCH_TO:
                    switchToClass();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initUI();
        this.initData();
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {
        this.switchToClass();
    }

    private void initUI(){
        setContentView(R.layout.activity_loading_and_welcome);
        this.changeFragment(LoadingFragment.class.getName(), null, false);
    }

    private void initData(){
        super.setShowForceLogoff(false);
        //设置程序正常运行
        this.mPreferences.putBoolean(Constants.Preferences.KEY_IS_FORCE_FINISH, false);
        this.mHandler.sendEmptyMessageDelayed(WHAT_SWITCH_TO, TIME_OUT);
        // 用户有登录时，进行数据初始化
        CurrentUser.getInstance().initCurrentUserData();
        //TODO 友盟相关操作

        // 请求服务器地址列表链接
        ReqAndRespCenter.getInstance().requestAddressList();
        // 初始化百度定位
        DBLocationHelper.initLocation(getApplicationContext());
        // 开始百度定位
        DBLocationHelper.startLocation();

    }

    private void switchToClass(){
        int oldVersion = this.mPreferences.getInt(Constants.Preferences.KEY_SAVE_OLD_VERSION, 0);
        LogHelper.d(TAG, this.mPreferences.toString() + "version:" + oldVersion);
        if(0 == oldVersion || oldVersion < this.mCurrentVersion){
            changeFragment(WelcomeFragment.class.getName(), null, false);
        }else{
            CurrentUser currentUser = CurrentUser.getInstance();
            if(currentUser.isLogin()){
                //登录情况下
                if(currentUser.isNeedResetPassword()){
                    // 需要重置密码
                    Intent intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, RegisterSuccessChangePasswordFragment.class.getName());
                    ActivityHelper.switchTo(this, RegisterActivity.class, intent, true);
                }else if(currentUser.isNeedCompleteInfo()){
                    // 需要完善资料
                    Intent intent = new Intent();
                    intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, RegisterSuccessInputBaseInfoFragment.class.getName());
                    ActivityHelper.switchTo(this, RegisterActivity.class, intent, true);
                }else{
                    //根据设置跳转到我的名片或者名片夹主页
                    int homePage = currentUser.getSetting().getHomePage();
                    if(0 == homePage){
                        // 跳转到名片主页
                        ActivityHelper.switchTo(this, VCardMainActivity.class, true);
                    }else{
                        // 跳转到名片夹
                        ActivityHelper.switchTo(this, MultiMainActivity.class, true);
                    }
                }
            }else{
                // 未登录情况下
                if(ResourceHelper.isExistConfigFile()) {
                    ActivityHelper.switchTo(this, WXEntryActivity.class, true);
                }else{
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.IntentSet.KEY_ADD_VCARD_FRG_IS_SHOW_LOGIN, true);
                    intent.putExtra(Constants.IntentSet.KEY_FRG_BUNDLE, bundle);
                    ActivityHelper.switchTo(this, AddVCardActivity.class, intent, true);
                }
            }
        }
    }


}
