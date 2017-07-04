package com.maya.android.vcard.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.data.CurrentUser;
import com.maya.android.vcard.data.ReqAndRespCenter;
import com.maya.android.vcard.entity.json.LoginJsonEntity;
import com.maya.android.vcard.entity.json.LoginThirdJsonEntity;
import com.maya.android.vcard.entity.result.LoginResultEntity;
import com.maya.android.vcard.entity.result.SinaWeiboUserResultEntity;
import com.maya.android.vcard.entity.result.TencentQQLoginResultEntity;
import com.maya.android.vcard.entity.result.TencentQQUserResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.entity.result.VCardListResultEntity;
import com.maya.android.vcard.ui.act.RegisterActivity;
import com.maya.android.vcard.ui.act.VCardMainActivity;
import com.maya.android.vcard.ui.base.BaseActivity;
import com.maya.android.vcard.ui.frg.LoginFragment;
import com.maya.android.vcard.ui.frg.RegisterSuccessChangePasswordFragment;
import com.maya.android.vcard.ui.frg.RegisterSuccessInputBaseInfoFragment;
import com.maya.android.vcard.ui.impl.UserLoginImpl;
import com.maya.android.vcard.util.ProjectHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Activity:登录
 * <p>由于微信那坑爹的要求，登录搬到这个Activity</p>
 */
public class WXEntryActivity extends BaseActivity implements UserLoginImpl {

    private static final String TAG = WXEntryActivity.class.getSimpleName();
    private static final String WX_STATE_LOGIN = "vcard_login";
    private static final int CMD_GET_ACCESS_TOKEN = 20001;
    private static final int CMD_GET_USER_INFO = 20002;
    private static final String URL_WX_GET_ACCESS_TOKEN ="https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=%1$s&secret=%2$s&code=%3$s&grant_type=authorization_code";
    private static final String URL_WX_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?" +
            "access_token=%1$s&openid=%2$s";
    /** 是否展示返回按钮 **/
    private boolean isShowBackPressed;
    private boolean isRememberPassword = true;
    /** 新浪微博API **/
    private Weibo mWeibo = null;
    /** 腾讯登陆API **/
    private Tencent mTencent;
    /** QQ登录事件监听 **/
    private QQUiListener mQQListener;
    /** 微信API **/
    private IWXAPI mIWXAPI;
    private CurrentUser mCurrentUser = CurrentUser.getInstance();
    private URLResultEntity mURLEntity = mCurrentUser.getURLEntity();
    /** 微博授权监听 **/
    private WeiboAuthListener mWeiboAuthListener;
    /** 微信消息拦截 **/
    private IWXAPIEventHandler mIWXAPIHandler = new IWXAPIEventHandler() {
        @Override
        public void onReq(BaseReq baseReq) {

        }

        @Override
        public void onResp(BaseResp baseResp) {
            switch(baseResp.errCode){
                case BaseResp.ErrCode.ERR_OK:
                    if(WX_STATE_LOGIN.equals(baseResp.transaction)){
                        SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                        if(Helper.equalString(resp.state, WX_STATE_LOGIN, true)){
                            String code = resp.token;
                            String url = String.format(URL_WX_GET_ACCESS_TOKEN,
                                    Constants.ThirdPartySet.WX_APP_ID,
                                    Constants.ThirdPartySet.WX_APP_SECRET,
                                    code);
                            ReqAndRespCenter.getInstance().getForResult(CMD_GET_ACCESS_TOKEN, url, null, null, WXEntryActivity.this);
                        }
                    }else{
                        ActivityHelper.showToast(R.string.share_success);
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:

                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:

                    break;
            }
        }
    };
    //region Override 方法
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.mIWXAPI.handleIntent(intent, this.mIWXAPIHandler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initUI();
        this.initData();
    }

    @Override
    protected boolean isMainActivity() {
        if(this.isShowBackPressed){
            return false;
        }else{
            return getSupportFragmentManager().getBackStackEntryCount() <= 0;
        }
    }

    @Override
    protected boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        switch (tag){
            case Constants.CommandRequestTag.CMD_LOGIN_NORMAL:
            case Constants.CommandRequestTag.CMD_LOGIN_THIRD:
                if(!super.onCommandCallback2(tag, commandResult, objects)){
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
                    //TODO 离线消息进行处理

                    // 提示登录成功
                    ActivityHelper.showToast(R.string.login_success);

                    boolean isNeedResetPassword = resultEntity.isShouldUpdPwd();
                    if(isNeedResetPassword){
                        //需要修改密码
                        ActivityHelper.closeProgressDialog();
                        this.mCurrentUser.setNeedResetPassword(isNeedResetPassword);
                        Intent intent = new Intent();
                        intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, RegisterSuccessChangePasswordFragment.class.getName());
                        ActivityHelper.switchTo(this, RegisterActivity.class, true);
                    }else{
                        //不需要修改密码
                        //继续请求当前用户资料
                        ActivityHelper.closeProgressDialog();
                        ActivityHelper.showProgressDialog(WXEntryActivity.this, R.string.doing_get_user_info_please_waiting);
                        String url = ProjectHelper.fillRequestURL(this.mURLEntity.getMyInfo());
                        ReqAndRespCenter.getInstance()
                                .postForResult(Constants.CommandRequestTag.CMD_MY_INFO, url, this.mCurrentUser.getToken(), new JSONObject(), this);
                    }
                    return true;
                }
                return false;
            case Constants.CommandRequestTag.CMD_MY_INFO:
                if(!super.onCommandCallback2(tag, commandResult, objects)){
                    this.mCurrentUser.saveUserInfoEntity(commandResult.toString(), null);
                    UserInfoResultEntity userInfoEntity = this.mCurrentUser.getUserInfoEntity();
                    if(Helper.isNotNull(userInfoEntity)){
                        String displayName = userInfoEntity.getDisplayName();
                        if("".equals(displayName)){
                            //资料不完整
                            ActivityHelper.closeProgressDialog();
                            this.mCurrentUser.setNeedCompleteInfo(true);
                            Intent intent = new Intent();
                            intent.putExtra(Constants.IntentSet.KEY_FRG_NAME, RegisterSuccessInputBaseInfoFragment.class.getName());
                            ActivityHelper.switchTo(this, RegisterActivity.class, true);
                        }else{
                            //资料完整
                            //保存简易登录信息
                            this.mCurrentUser.addLoginSimpleInfo(userInfoEntity, this.isRememberPassword);
                            //获取设置信息
                            ReqAndRespCenter.getInstance().requestSetting();
                            //保存简易登陆信息
                            this.mCurrentUser.addLoginSimpleInfo(userInfoEntity, this.isRememberPassword);
                            //继续请求当前用户名片
                            String url = ProjectHelper.fillRequestURL(this.mURLEntity.getMyVcard());
                            ReqAndRespCenter.getInstance()
                                    .postForResult(Constants.CommandRequestTag.CMD_MY_CARD, url, new JSONObject(), this);
                        }
                    }
                    return true;
                }
                return false;
            case Constants.CommandRequestTag.CMD_MY_CARD:
                if(!super.onCommandCallback2(tag, commandResult, objects)){
                    VCardListResultEntity resultEntity = JsonHelper.fromJson(commandResult, VCardListResultEntity.class);
                    if(Helper.isNotNull(resultEntity)){
                        this.mCurrentUser.saveVCardEntityList(resultEntity.getCardEntityList());
                    }
                    ActivityHelper.switchTo(this, VCardMainActivity.class, true);
                    return true;
                }
                return false;
            case CMD_GET_ACCESS_TOKEN:
                if(!super.onCommandCallback2(tag, commandResult, objects)){
                    //返回结果{"openid":"oomT_jupimCyoRtlqDkHnGpm8YEA","expires_in":7200,"scope":"snsapi_userinfo",
                    //"refresh_token":"OezXcEiiBSKSxW0eoylIeDzmdSIW4WBEftYYobl-bkMWjhx3Xy9PkLEmtb7WpmTm8MXUh0OAmvPC0sNWP3i79IwKQ5py0mrFS3_9x_mdEd17FKjerlhYX24vrEnoETUgE-jKPDNm7zJ7AaEtDJemmQ",
                    //"access_token":"OezXcEiiBSKSxW0eoylIeDzmdSIW4WBEftYYobl-bkMWjhx3Xy9PkLEmtb7WpmTmlh1G-kRInqbamruFUi72ORCdUJD8sZcWxqq1BYjVtnZCDT4dcB8BMYxivcahIVAt9TXBinrV6Mor4b9366YNZQ",
                    //"unionid":"ostG1julJOw0NdfknpxKcoU4QLbQ"}

//				LogHelper.d(TAG, "Get access_token :commandResult:" + commandResult);
                    String accessToken = commandResult.optString("access_token");
                    String openId = commandResult.optString("openid");
                    String url = String.format(URL_WX_GET_USER_INFO, accessToken, openId);
                    ReqAndRespCenter.getInstance().getForResult(CMD_GET_USER_INFO, url, null, null, this);
                    return true;
                }else{
                    ActivityHelper.showToast(R.string.auth_failed);
                }
                return false;
            case CMD_GET_USER_INFO:
                if(!super.onCommandCallback2(tag, commandResult, objects)){
                    //请求返回的结果：{"sex":2,"nickname":"微片商务01","unionid":"ostG1julJOw0NdfknpxKcoU4QLbQ",
                    //"privilege":[],"province":"Fujian","openid":"oomT_jupimCyoRtlqDkHnGpm8YEA",
                    //"language":"zh_CN",
                    //"headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/QuzYx4ag1jpX0yr89dWwj6l4yyibVFfCaHlibjx7SRjW2mK1ueMeTiaeDicSo5CwpSYm6ykicUGH2mTZSKdldeLOicfDStPKdFB9BQ\/0",
                    //"country":"CN","city":"Xiamen"}
                    String username = commandResult.optString("openid");
                    String headImg = commandResult.optString("headimgurl");
                    String nickName = commandResult.optString("nickname");
                    String province = commandResult.optString("province");
                    String city = commandResult.optString("city");
                    LoginThirdJsonEntity loginThirdJsonEntity = new LoginThirdJsonEntity(username, "",
                            CurrentUser.getInstance().getClientId(), Constants.LoginWay.LOGIN_WAY_BY_WEIXIN, headImg, nickName, province, city);
                    loginByThird(loginThirdJsonEntity);
                    return true;
//				LogHelper.d(TAG, "Get userInfo :commandResult:" + commandResult);
                }else{
                    ActivityHelper.showToast(R.string.auth_failed);
                }
                return false;
        }
        return false;
    }

    @Override
    protected void onResponseFail(int tag, int responseCode, String msgInfo) {
        super.onResponseFail(tag, responseCode, msgInfo);
        switch (tag){
            case Constants.CommandRequestTag.CMD_LOGIN_NORMAL:
                ActivityHelper.closeProgressDialog();
                ActivityHelper.showToast(msgInfo);
                break;
        }
    }

    @Override
    public void switchTo(Class<?> cls, Intent intent) {
        if(Helper.isNull(intent)){
            intent = new Intent();
        }
        ActivityHelper.switchTo(this, cls, intent);
    }

    @Override
    public void loginByCommon(LoginJsonEntity entity, boolean isRememberPassword) {
        this.isRememberPassword = isRememberPassword;
        if(Helper.isNotNull(entity)){
            if(NetworkHelper.isNetworkAvailable(this)) {
                // 有网络进行登录
                ActivityHelper.showProgressDialog(this, R.string.doing_login_please_waiting);
                String url = this.mURLEntity.getLoginNormal();
                String json = JsonHelper.toJson(entity, LoginJsonEntity.class);
                ReqAndRespCenter.getInstance()
                        .postForResult(Constants.CommandRequestTag.CMD_LOGIN_NORMAL, ProjectHelper.fillRequestURL(url), null, json, this);
            }else{
                // 无网络进行提示
                ActivityHelper.showToast(R.string.no_network_please_open_network);
            }
        }
    }

    @Override
    public void loginByWeixin() {
        this.loginWeixin();
    }

    @Override
    public void loginBySina() {
        this.loginSina();
    }

    @Override
    public void loginByQQ() {
        this.loginQQ();
    }
    //endregion Override 方法

    private void initUI(){
        super.initTop();
        Intent intent = getIntent();
        String frgName = intent.getStringExtra(Constants.IntentSet.KEY_FRG_NAME);
        Bundle bundle = intent.getBundleExtra(Constants.IntentSet.KEY_FRG_BUNDLE);
        if(Helper.isNotNull(bundle)) {
            this.isShowBackPressed = bundle.getBoolean(Constants.IntentSet.KEY_LOGIN_FRG_IS_SHOW_BACK, false);
        }
        if(Helper.isNull(frgName) || "".equals(frgName)){
            frgName = LoginFragment.class.getName();
        }
        this.changeFragment(frgName, bundle, false);
    }

    private void initData(){
        this.mIWXAPI = WXAPIFactory.createWXAPI(this, Constants.ThirdPartySet.WX_APP_ID);
        this.mIWXAPI.handleIntent(getIntent(), this.mIWXAPIHandler);
    }

    private void loginQQ(){
        if(Helper.isNull(this.mTencent)){
            this.mTencent = Tencent.createInstance(Constants.ThirdPartySet.KEY_QQ_APP_SSO, this);
        }
        if(Helper.isNull(this.mQQListener)){
            this.mQQListener = new QQUiListener(QQUiListener.TYPE_LOGIN);
        }
        if (!mTencent.isSessionValid()) {
            mTencent.login(WXEntryActivity.this, "all", this.mQQListener);
        } else {
            mTencent.logout(WXEntryActivity.this);
        }
    }

    private void loginSina(){
        if(Helper.isNull(this.mWeibo)){
            this.mWeibo = Weibo.getInstance(Constants.ThirdPartySet.KEY_SINA_WEIBO, Constants.ThirdPartySet.URL_CALLBACK_SINA);
        }
        if(Helper.isNull(this.mWeiboAuthListener)){
            this.mWeiboAuthListener = new WeiboAuthListener() {

                @Override
                public void onWeiboException(WeiboException arg0) {
                    ActivityHelper.showToast(R.string.auth_failed);
                }

                @Override
                public void onError(WeiboDialogError arg0) {
                    ActivityHelper.showToast(R.string.auth_failed);
                }

                @Override
                public void onComplete(Bundle arg0) {
                    doComplete(arg0);
                }

                @Override
                public void onCancel() {
                    ActivityHelper.showToast(R.string.auth_cancel);
                }

                private void doComplete(Bundle values) {
                    String token = values.getString("access_token");
                    // String token = "2.00ygYG9DhGPsKE8a5c11d250JojucC";
                    String expiresIn = values.getString("expires_in");
                    Oauth2AccessToken mOAuth2Token = new Oauth2AccessToken(token, expiresIn);
                    // 用户ID
                    final String userId = values.getString("uid");

                    if (mOAuth2Token.isSessionValid()) {
                        ActivityHelper.showProgressDialog(WXEntryActivity.this, R.string.doing_login_please_waiting);
                        UsersAPI usersAPI = new UsersAPI(mOAuth2Token);
                        usersAPI.show(Long.parseLong(userId), new RequestListener() {

                            @Override
                            public void onIOException(IOException arg0) {
                                arg0.printStackTrace();
                            }

                            @Override
                            public void onError(WeiboException arg0) {
                                arg0.printStackTrace();
                            }

                            @Override
                            public void onComplete(String userInfo) {
                                if (Helper.isNotEmpty(userInfo)) {
                                    SinaWeiboUserResultEntity resultEntity = JsonHelper.fromJson(userInfo, SinaWeiboUserResultEntity.class);
                                    if (Helper.isNotNull(resultEntity)) {
//                                        CurrentUser.getInstance().setLoginWay(Constant.LoginWay.LOGIN_WAY_BY_WEIBO_SINA);
                                        // mUName = sw.getUserId();
                                        // CurrentUser.getInstance().setCurrentUserName(mUName);
                                        LoginThirdJsonEntity loginThirdJsonEntity = new LoginThirdJsonEntity(String.valueOf(resultEntity.getId()), "",
                                                CurrentUser.getInstance().getClientId(), Constants.LoginWay.LOGIN_WAY_BY_WEIBO_SINA, resultEntity.getProfileImageUrl(),
                                                resultEntity.getName(),resultEntity.getProvince(), resultEntity.getCity());
                                        loginByThird(loginThirdJsonEntity);
                                    }
                                }
                            }
                        });
                    }
                }
            };
        }
        this.mWeibo.authorize(WXEntryActivity.this, this.mWeiboAuthListener);
//        this.mWeibo
    }

    private void loginWeixin(){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = WX_STATE_LOGIN;
        req.transaction = WX_STATE_LOGIN;
        this.mIWXAPI.sendReq(req);
    }

    private void loginByThird(LoginThirdJsonEntity jsonEntity){
        if(Helper.isNotNull(jsonEntity)){
            if(NetworkHelper.isNetworkAvailable(this)) {
                // 有网络进行登录

                String url = this.mURLEntity.getLoginThird();
                String json = JsonHelper.toJson(jsonEntity, LoginThirdJsonEntity.class);
                ReqAndRespCenter.getInstance()
                        .postForResult(Constants.CommandRequestTag.CMD_LOGIN_THIRD, ProjectHelper.fillRequestURL(url), null, json, this);
            }else{
                // 无网络进行提示
                ActivityHelper.showToast(R.string.no_network_please_open_network);
            }
        }
    }

    //region 子类
    /**
     * 腾讯qq 登录、获取用户信息 回调接口
     */
    public class QQUiListener implements IUiListener {
        /** 回调类型常量 **/
        public static final int TYPE_LOGIN = 1;
        public static final int TYPE_LOGOUT = 2;
        public static final int TYPE_GET_USER_INFO = 3;
        private int mType;
        /** 腾讯qq 授权登录结果 **/
        private TencentQQLoginResultEntity mQqLoginResultEntity;

        public QQUiListener(int type) {
            this.mType = type;
        }

        public void setType(int type){
            this.mType = type;
        }

        @Override
        public void onCancel() {
            ActivityHelper.showToast(R.string.auth_cancel);
        }

        @Override
        public void onError(UiError arg0) {
            ActivityHelper.showToast(R.string.auth_failed);
        }

        @Override
        public void onComplete(Object arg0) {
            switch (mType) {
                case TYPE_LOGIN:
                    if (Helper.isNotNull(arg0)) {
                        mQqLoginResultEntity = JsonHelper.fromJson((JSONObject) arg0, TencentQQLoginResultEntity.class);
                        UserInfo qqUser = new UserInfo(WXEntryActivity.this, mTencent.getQQToken());
                        mQQListener.setType(QQUiListener.TYPE_GET_USER_INFO);
                        qqUser.getUserInfo(mQQListener);
                        ActivityHelper.showProgressDialog(WXEntryActivity.this, R.string.doing_login_please_waiting);
//                        // TODO 将qq 授权信息保存到本地 自动登录
//                        PreferencesHelper.getInstance().putString(Constant.Preferences.KEY_LOGIN_TENCENT_QQ, JsonHelper.toJson(mQqLoginResultEntity));
                    }
                    break;

                case TYPE_GET_USER_INFO:
                    ActivityHelper.showProgressDialog(WXEntryActivity.this, R.string.doing_login_please_waiting);
                    // 获取用户信息登录
                    TencentQQUserResultEntity qqUserResult = JsonHelper.fromJson((JSONObject) arg0, TencentQQUserResultEntity.class);

//                    CurrentUser.getInstance().setLoginWay(Constant.LoginWay.LOGIN_WAY_BY_QQ);
                    LoginThirdJsonEntity loginThirdJsonEntity = new LoginThirdJsonEntity(mQqLoginResultEntity.getOpenId(), "", CurrentUser.getInstance().getClientId(),
                            Constants.LoginWay.LOGIN_WAY_BY_QQ, qqUserResult.getFigureurlQq2(), qqUserResult.getNickName(), "", "");
                    loginByThird(loginThirdJsonEntity);

                    break;
            }

        }

    }
    //endregion 子类
}
