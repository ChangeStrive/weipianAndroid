package com.maya.android.vcard.app;

import com.igexin.sdk.PushManager;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.utils.app.MaYaApplication;
import com.maya.android.vcard.constant.Constants;

/**
 * Created by YongJi on 2015/8/7.
 */
public class VCardApplication extends MaYaApplication implements Thread.UncaughtExceptionHandler {

    private static final String TAG = VCardApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        //TODO 打包时，需关掉 是否激活日志打印
        LogHelper.setLogEnabled(true);
        //初始化个信推送
        PushManager.getInstance().initialize(getApplicationContext());
        //TODO 设置异常捕获
        //Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //
        LogHelper.i(TAG, "uncaughtException");
        ex.printStackTrace();

        //保存当前用户信息
        //CurrentUser.getInstance().saveSetting();
        //设置强制退出
        PreferencesHelper.getInstance().putBoolean(Constants.Preferences.KEY_IS_FORCE_FINISH, true);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogHelper.i(TAG, "onLowMemory");
    }
}
