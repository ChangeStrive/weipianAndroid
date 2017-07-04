package com.maya.android.vcard.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maya.android.jsonwork.AbstractDataCommand;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.ui.impl.ActivitySwitchToImpl;
import com.maya.android.vcard.ui.impl.ActivityTitleActionImpl;
import com.maya.android.vcard.ui.impl.OnFragmentInteractionImpl;
import com.maya.android.vcard.util.DBLocationHelper;

import org.json.JSONObject;

//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;

/**
 * BaseActivity:
 * <p>说明：
 * <p>1、有继承BaseActivity的Activity，用到frg时，id必须为frg_content
 * <p>2、有继承BaseActivity时，有switchTo(Class)方法，此方法是由frg传过来的跳转，如frg无跳转，此方法可留空
 * <p>3、对子类提供变量：mFrgManager
 * <p>4、对子类提供方法如下：
 * <ol>
 *     <li>{@link #initTop()}</li>
 *     <li>{@link #isMainActivity()}</li>
 *     <li>{@link #doInFinishing()}</li>
 * </ol>
 * * */
public abstract class BaseActivity extends FragmentActivity implements ActivityTitleActionImpl, OnFragmentInteractionImpl, ActivitySwitchToImpl, AbstractDataCommand.CommandCallback<JSONObject> {

    //region 变量
    private static final int TIME_DOUBLE_BACK_TO_EXIT = 2000;
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final String RESULT_RESPONSE_CODE = "responseCode";
    private static final String RESULT_RESPONSE_DATA = "data";
    private static final String RESULT_RESPONSE_MSGINFO = "msgInfo";
    private static final String DATA_NULL = "null";
    private static final String ARRAY_NULL = "[]";

    protected FragmentManager mFrgManager;

    private CurrentUser mCurrentUser = CurrentUser.getInstance();
    private long mLastBackTime = 0;
    private ExitBroadcastReceiver mExitBroadcastReceiver;
    private boolean isBackPressed = true;
    private Handler mBackPressedHandler = null;
    private boolean isShowForceLogoff = true;
//    private int mCurrentFrgCount;
    private RelativeLayout mRelTop;
    private TextView mTxvTopTitleLeft, mTxvTopTitleCenter;
    private ImageView mImvTopLeft, mImvTopRight;
    private TextView mTxvTopRight;
    private View.OnClickListener mOnCkiOnClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imv_act_top_left:
                case R.id.txv_act_top_title_left:
                    onBackPressed();
                    break;
            }
        }
    };
    //endregion 变量

    //region Override 方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.mCurrentUser.isForcedLogoff() && this.isShowForceLogoff){
            //展示被迫退出
            this.mCurrentUser.showForceLogoffDialog(this);
        }
        //是否异常退出
        if(PreferencesHelper.getInstance().getBoolean(Constants.Preferences.KEY_IS_FORCE_FINISH, false)){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销退出广播
        unregisterReceiver(this.mExitBroadcastReceiver);
        ActivityHelper.closeProgressDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(this.isMainActivity()){
                // 点击返回键时所作的操作
                if((System.currentTimeMillis() - mLastBackTime) > TIME_DOUBLE_BACK_TO_EXIT){
                    ActivityHelper.showToast(R.string.double_back_to_exit);
                    // 通过获取系统时间做判断
                    mLastBackTime = System.currentTimeMillis();
                }else{
                    // 清理状态栏
//                    ActivityHelper.cancelDefaultNotification(this);
                    //清楚图片缓存
//                    new AsyncTask<Void, Void, Void>() {
//
//                        @Override
//                        protected Void doInBackground(Void... params) {
//                            //TODO 图片异步修改
////							AsyncImageManager.clearCaches();
////							AsyncImageManager.clearBigCaches();
//
//                            AsyncImageManager.clearMemoryCache();
//                            return null;
//                        }
//                    }.execute();
                    if (this.doInFinishing()) {
                        //发送所有activity关闭广播
                        sendBroadcast(new Intent(Constants.ActionIntent.ACTION_INTENT_EXIT_PROCEDURE));
                        //关闭百度定位
                        DBLocationHelper.stopLocation();

                        //用户退出
                        CurrentUser.getInstance().quit();
                        //关闭Log打印流
//						LogHelper.closeWriteLog2File();
                        // 退出
//						Process.killProcess(Process.myPid());
                    }
//                    int currentVersion = android.os.Build.VERSION.SDK_INT;
//                    if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
//                        Intent startMain = new Intent(Intent.ACTION_MAIN);
//                        startMain.addCategory(Intent.CATEGORY_HOME);
//                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(startMain);
//                        System.exit(0);
//                    } else {// android2.1
//                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//                        am.restartPackage(getPackageName());
//                    }
                }
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(this.isBackPressed) {
            super.onBackPressed();
        }
        if(Helper.isNotNull(this.mBackPressedHandler)){
            this.mBackPressedHandler.sendEmptyMessage(BaseFragment.WHAT_ON_BACK_PRESSED);
        }
    }

    @Override
    public void setActivityTitle(int resId, boolean center) {
        this.setTopTitle(resId, center);
    }

    @Override
    public void setActivityTitle(String title, boolean center) {
        this.setTopTitle(title, center);
    }

    @Override
    public void setActivityTopVisibility(int visibility) {
        this.setTopVisibility(visibility);
    }

    @Override
    public void setActivityTopLeftVisibility(int visibility) {
        this.setTopLeftVisibility(visibility);
    }

    @Override
    public void setActivityTopRightImvVisibility(int visibility) {
        this.setTopRightImvVisibility(visibility);
    }

    @Override
    public void setActivityTopRightTxvVisibility(int visibility) {
        this.setTopRightTxvVisibility(visibility);
    }

    @Override
    public void setActivityTopLeftImv(int srcResId, View.OnClickListener onClickListener) {
        this.setTopLeftImv(srcResId, onClickListener);
    }

    @Override
    public void setActivityTopRightImv(int srcResId, View.OnClickListener onClickListener) {
        this.setTopRightImv(srcResId, onClickListener);
    }

    @Override
    public void setActivityTopRightTxv(int resId, View.OnClickListener onClickListener) {
        this.setTopRightTxv(resId, onClickListener);
    }

    @Override
    public ImageView getActivityTopLeftImv() {
        return this.getTopLeftImv();
    }

    @Override
    public ImageView getActivityTopRightImv() {
        return this.getTopRightImv();
    }

    @Override
    public TextView getActivityTopRightTxv() {
        return this.getTopRightTxv();
    }

    @Override
    public void onFragmentInteraction(String frgName, Bundle frgBundle) {
        this.changeFragment(frgName, frgBundle, true);
    }

    @Override
    public void onActivityBackPressed() {
        this.onBackPressed();
    }

    @Override
    public void onActivityFinish() {
        this.finish();
    }

    @Override
    public void onActivitySetBackPressed(boolean isBackPressed) {
        this.isBackPressed = isBackPressed;
    }

    @Override
    public boolean onCommandCallback(int tag, JSONObject commandResult, Object... objects) {
        if(preProccessResult(commandResult)){
            this.onCommandCallback2(tag, commandResult, objects);
        }else{
            int responseCode = commandResult.optInt(RESULT_RESPONSE_CODE);
            String msgInfo = commandResult.optString(RESULT_RESPONSE_MSGINFO);
            if(0 == responseCode){
                //返回成功
                JSONObject data = commandResult.optJSONObject(RESULT_RESPONSE_DATA);
                if(Helper.isNull(data) || DATA_NULL.equals(data.toString()) || ARRAY_NULL.equals(data.toString())) {
                    this.onResponseSuccess(tag, msgInfo);
                }else{
                    this.onCommandCallback2(tag, data, objects);
                }
            }else{
                //返回错误
                this.onResponseFail(tag, responseCode, msgInfo);
            }
        }
        return false;
    }
    //endregion Override 方法

    //region public 方法
    public void setBackPressedHandler(Handler handler){
        this.mBackPressedHandler = handler;
    }
    //endregion public 方法

    //region protected 方法
    /**&
     *命令返回时调用
     * @param tag - 命令标识&
     * @param commandResult - 返回结果
     * @param objects - 返回的其它数据
     * @return 是否已被处理(已处理则返回true,之后的处理都不会被执行)
     */
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        return preProccessResult(commandResult);
    }
    /**
     *命令返回时调用：成功无结果
     * @param tag - 命令标识
     * @param msgInfo - 提示信息
     */
    protected void onResponseSuccess(int tag, String msgInfo){

    }
    /**
     *命令返回时调用：失败
     * @param tag - 命令标识
     * @param responseCode - 错误代码
     * @param msgInfo - 提示信息
     */
    protected void onResponseFail(int tag, int responseCode, String msgInfo){

    }
    /**
     * 是否是主界面(在程序主界面重载此方法并返回true)
     * @return
     */
    protected boolean isMainActivity() {
        return false;
    }
    /**
     * 是否真正退出
     * @return
     */
    protected boolean doInFinishing(){
        return true;
    }

    /**
     * 初始化Top
     */
    protected void initTop(){
        this.mRelTop = findView(R.id.inc_top);
        this.mImvTopLeft = findView(R.id.imv_act_top_left);
        this.mImvTopRight = findView(R.id.imv_act_top_right);
        this.mTxvTopTitleLeft = findView(R.id.txv_act_top_title_left);
        this.mTxvTopTitleCenter = findView(R.id.txv_act_top_title_center);
        this.mImvTopLeft.setOnClickListener(this.mOnCkiOnClickListener);
        this.mTxvTopTitleLeft.setOnClickListener(this.mOnCkiOnClickListener);
        this.mTxvTopRight = findView(R.id.txv_act_top_right);
    }

    protected <T> T findView(int resId){
        return (T)findViewById(resId);
    }

    /**
     * 设置Top的Title</br>
     * 在使用此方法，确保有调用{@link #initTop}方法
     * @param resId
     * @param center
     */
    protected void setTopTitle(int resId, boolean center){
        if(center){
            if(Helper.isNotNull(this.mTxvTopTitleCenter)){
                this.mTxvTopTitleCenter.setVisibility(View.VISIBLE);
                this.mTxvTopTitleCenter.setText(resId);
                this.mTxvTopTitleLeft.setVisibility(View.GONE);
            }
        }else{
            if(Helper.isNotNull(this.mTxvTopTitleLeft)){
                this.mTxvTopTitleLeft.setVisibility(View.VISIBLE);
                this.mTxvTopTitleLeft.setText(resId);
                this.mTxvTopTitleCenter.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置Top的Title</br>
     * 在使用此方法，确保有调用{@link #initTop}方法
     * @param title
     * @param center
     */
    protected void setTopTitle(String title, boolean center){
        if(center){
            if(Helper.isNotNull(this.mTxvTopTitleCenter)){
                this.mTxvTopTitleCenter.setVisibility(View.VISIBLE);
                this.mTxvTopTitleCenter.setText(title);
                this.mTxvTopTitleLeft.setVisibility(View.GONE);
            }
        }else{
            if(Helper.isNotNull(this.mTxvTopTitleLeft)){
                this.mTxvTopTitleLeft.setVisibility(View.VISIBLE);
                this.mTxvTopTitleLeft.setText(title);
                this.mTxvTopTitleCenter.setVisibility(View.GONE);
            }
        }
    }
    /**
     * 设置头部是否隐藏
     * @param visibility
     */
    protected void setTopVisibility(int visibility){
        if(Helper.isNotNull(this.mRelTop)){
            this.mRelTop.setVisibility(visibility);
        }
    }
    /**
     * 设置Top的返回按钮是否展示</br>
     * 在使用此方法，确保有调用{@link #initTop}方法
     * @param visibility
     */
    protected void setTopLeftVisibility(int visibility){
        if(Helper.isNotNull(this.mImvTopLeft)){
            this.mImvTopLeft.setVisibility(visibility);
        }
    }

    protected void setTopRightImvVisibility(int visibility){
        if(Helper.isNotNull(this.mImvTopRight)){
            this.mImvTopRight.setVisibility(visibility);
        }
    }

    protected void setTopRightTxvVisibility(int visibility){
        if(Helper.isNotNull(this.mTxvTopRight)){
            this.mTxvTopRight.setVisibility(visibility);
        }
    }

    protected void setTopLeftImv(int srcResId, View.OnClickListener onClickListener){
        if(Helper.isNotNull(this.mImvTopLeft)){
            this.mImvTopLeft.setImageResource(srcResId);
            this.mImvTopLeft.setOnClickListener(onClickListener);
        }
    }

    protected void setTopRightImv(int srcResId, View.OnClickListener onClickListener){
        if(Helper.isNotNull(this.mImvTopRight)){
            this.mImvTopRight.setImageResource(srcResId);
            this.mImvTopRight.setOnClickListener(onClickListener);
        }
    }

    protected void setTopRightTxv(int resId, View.OnClickListener onClickListener){
        if(Helper.isNotNull(this.mTxvTopRight)){
            this.mTxvTopRight.setText(resId);
            this.mTxvTopRight.setOnClickListener(onClickListener);
        }
    }

    protected ImageView getTopLeftImv(){
        return this.mImvTopLeft;
    }

    protected ImageView getTopRightImv(){
        return this.mImvTopRight;
    }

    protected TextView getTopRightTxv() {
        return this.mTxvTopRight;
    }
    /**
     * 切换frg
     * @param frgName
     * @param frgBundle
     * @param isAddToBack 是否加到回退栈
     */
    protected void changeFragment(String frgName, Bundle frgBundle, boolean isAddToBack){
        LogHelper.d(TAG, frgName);
        Fragment fragment = this.mFrgManager.findFragmentByTag(frgName);
        if(Helper.isNull(fragment)){
            fragment = Fragment.instantiate(this, frgName, frgBundle);
        }
        if(Helper.isNotNull(fragment)){
            FragmentTransaction frgTransaction = this.mFrgManager.beginTransaction();
            frgTransaction.replace(R.id.frg_content, fragment);
            if(isAddToBack ) {
                frgTransaction.addToBackStack(null);
            }
            frgTransaction.commit();
        }
    }
    /**
     * 设置是否展示被迫下线提示框
     * @param isShowForceLogoff
     */
    protected void setShowForceLogoff(boolean isShowForceLogoff){
        this.isShowForceLogoff = isShowForceLogoff;
    }
    //endregion protected 方法

    //region private 方法
    private void init(){
        this.mFrgManager = getSupportFragmentManager();

        //注册退出广播
        this.mExitBroadcastReceiver = new ExitBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ActionIntent.ACTION_INTENT_EXIT_PROCEDURE);
        registerReceiver(this.mExitBroadcastReceiver, filter);
//        this.mCurrentFrgCount = this.mFrgManager.getBackStackEntryCount();
    }
    /**
     * 预处理返回结果
     * @param commandResult
     * @return
     */
    private boolean preProccessResult(JSONObject commandResult){
        return Helper.isNull(commandResult) || "".equals(commandResult.toString());
    }

    /**
     * 退出广播
     * @author ZuoZiJi-Y.J
     * @version v1.0
     * @since 2014-2-7
     *
     */
    private class ExitBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(Constants.ActionIntent.ACTION_INTENT_EXIT_PROCEDURE.equals(intent.getAction())){
                LogHelper.i(TAG, BaseActivity.this.getLocalClassName() + " is finish!");
                finish();
                // 退出
//				Process.killProcess(Process.myPid());
                //关闭Log打印流
//				LogHelper.closeWriteLog2File();
            }
        }

    }
    //endregion private 方法

}
