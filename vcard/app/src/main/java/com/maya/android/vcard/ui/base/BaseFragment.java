package com.maya.android.vcard.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;

import com.maya.android.jsonwork.AbstractDataCommand;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.ui.impl.ActivitySwitchToImpl;
import com.maya.android.vcard.ui.impl.ActivityTitleActionImpl;
import com.maya.android.vcard.ui.impl.OnFragmentInteractionImpl;

import org.json.JSONObject;

//import android.app.Fragment;

//import android.support.v4.app.Fragment;

/**
 * BaseFragment:
 * <p>说明：
 * <p>1、有三个变量类提供使用分别如下：
 * <ol>
 *     <li>{@link #mFragmentInteractionImpl}用于frg间的切换</li>
 *     <li>{@link #mTitleAction}用于有涉及到Activity中Title的操作</li>
 *     <li>{@link #mActivitySwitchTo}用于frg内需要跳转到其他Activity的操作</li>
 * </ol>
 * */
public class BaseFragment extends Fragment implements AbstractDataCommand.CommandCallback<JSONObject> {

    public static final int WHAT_ON_BACK_PRESSED = 11000;
    private static final String RESULT_RESPONSE_CODE = "responseCode";
    private static final String RESULT_RESPONSE_DATA = "data";
    private static final String RESULT_RESPONSE_MSGINFO = "msgInfo";
    private static final String DATA_NULL = "null";
    private static final String ARRAY_NULL = "[]";

    protected OnFragmentInteractionImpl mFragmentInteractionImpl;
    protected ActivityTitleActionImpl mTitleAction;
    protected ActivitySwitchToImpl mActivitySwitchTo;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WHAT_ON_BACK_PRESSED:
                    onBackPressed();
                    break;
            }
        }
    };

    //region Override 方法
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFragmentInteractionImpl = (OnFragmentInteractionImpl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionImpl");
        }
        try {
            mTitleAction = (ActivityTitleActionImpl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityTitleActionImpl");
        }
        try {
            mActivitySwitchTo = (ActivitySwitchToImpl) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement mActivitySwitchTo");
        }
        ((BaseActivity)activity).setBackPressedHandler(this.mHandler);
        this.mFragmentInteractionImpl.onActivitySetBackPressed(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteractionImpl = null;
        mTitleAction = null;
        mActivitySwitchTo = null;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
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

    //region Protected 方法
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
        if(Constants.ResponseErrorCode.FORCED_OFFLINE == responseCode){
            CurrentUser currentUser = CurrentUser.getInstance();
            currentUser.setForcedLogoff(true);
            currentUser.setForcedLogoffMessage(msgInfo);
        }
    }

    protected void onBackPressed(){}

    protected <T> T findView(View view, int resId){
        return (T)view.findViewById(resId);
    }
    //endregion Protected 方法

    //region private 方法
    /**
     * 预处理返回结果
     * @param commandResult
     * @return
     */
    private boolean preProccessResult(JSONObject commandResult){
        return Helper.isNull(commandResult) || "".equals(commandResult.toString());
    }
    //endregion private 方法
}
