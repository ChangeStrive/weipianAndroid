package com.maya.android.vcard.data;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.google.gson.reflect.TypeToken;
import com.maya.android.asyncimageview.manager.AsyncImageManager;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.PreferencesHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.AddVCardEntity;
import com.maya.android.vcard.entity.CardEntity;
import com.maya.android.vcard.entity.LoginSimpleInfoEntity;
import com.maya.android.vcard.entity.LoginSimpleInfoListEntity;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.ui.widget.CustomDialogFragment;
import com.maya.android.vcard.util.DialogFragmentHelper;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.wxapi.WXEntryActivity;

import java.util.ArrayList;

/**
 * 当前用户
 * Created by YongJi on 2015/8/13.
 */
public class CurrentUser {

    private static final String TAG = CurrentUser.class.getSimpleName();

    //region 单例 start
    private static CurrentUser sInstance = null;
    public static CurrentUser getInstance() {
        if (Helper.isNull(sInstance)) {
            sInstance = new CurrentUser();
        }
        return sInstance;
    }
    private CurrentUser(){}
    //endregion 单例 end

    //region 变量
    /** Format：路径 **/
    private static final String PATH_FORMAT = Constants.PATH.PATH_FORMAT;
    /** NAME：缓存名称 **/
    private static final String NAME_CACHE = "cache";
    /** NAME：数据名称  **/
    private static final String NAME_DATA = "data";

//    private static PreferencesHelper sPreference = PreferencesHelper.getInstance(Constants.Preferences.KEY_NAME_USER_INFO);
    private static PreferencesHelper sPreference = PreferencesHelper.getInstance();

    private SettingEntity settingEntity = null;
    /** 网络各个请求地址  **/
    private URLResultEntity mURLEntity;
    /** 个信推送ID **/
    private String mClientId = null;
    /** TOKEN **/
    private String mToken = null;
    /** 经度 */
    private double mLongitude;
    /** 纬度 */
    private double mLatitude;
    /** 微片号 **/
    private String mVCardNo = null;
    /** 用户资料Entity **/
    private UserInfoResultEntity mUserInfoEntity = null;

    private ArrayList<CardEntity> mVCardList = null;

    private ArrayList<CardEntity> mUploadVCardList = null;

    private CustomDialogFragment mDialogFragmentOffilne;
    //endregion 变量

    //region public方法

    //region URLEntity 相关
    /**
     * 获取URLEntity
     * @return
     */
    public URLResultEntity getURLEntity() {
        if(Helper.isNull(this.mURLEntity)){
            //获取失败，读取前一次缓存
            String strURLEntity = sPreference.getString(Constants.Preferences.KEY_SAVE_URL_ADDRESS_LIST, "");
            if(ResourceHelper.isNotEmpty(strURLEntity)){
                this.mURLEntity = JsonHelper.fromJson(strURLEntity, URLResultEntity.class);
            }
            if(Helper.isNull(this.mURLEntity)) {
                //反馈URLEntity为空
                this.feedbackLoseURLEntity();
            }
        }
        return mURLEntity;
    }
    /**
     * 保存URLEntity
     * @param urlStrData
     * @param urlEntity  可以为NULL，为NULL时，从urlStrData生成
     */
    public void saveURLEntity(String urlStrData, URLResultEntity urlEntity){
        if(Helper.isNotNull(urlEntity)){
            this.mURLEntity = urlEntity;
        }
        if(Helper.isNotNull(urlStrData) && !"".equals(urlStrData)){
            sPreference.putString(Constants.Preferences.KEY_SAVE_URL_ADDRESS_LIST, urlStrData);
            if(Helper.isNull(this.mURLEntity)){
                URLResultEntity entity = JsonHelper.fromJson(urlStrData, URLResultEntity.class);
                this.mURLEntity = entity;
            }
        }

    }
   /**
     * 反馈丢失URLEntity</br>
     * 在{@link #getURLEntity()}为Null时调用
     */
    public void feedbackLoseURLEntity(){
        if(Helper.isNull(this.mURLEntity)){
            String urlStrData = ResourceHelper.getStringFromRawFile(R.raw.url_string_data);
            if(urlStrData != null && !"".equals(urlStrData)){
                this.saveURLEntity(urlStrData, null);
            }
            //TODO 进行必要反馈

            //重新进行请求
            ReqAndRespCenter.getInstance().requestAddressList();

        }
   }    //endregion URLEntity 相关

    //region 设置 相关
    public SettingEntity getSetting(){
        if(Helper.isNull(this.settingEntity)){
            String settingStrData = sPreference.getString(Constants.Preferences.KEY_SETTING_DATA, "");
            if(!"".equals(settingStrData)){
                this.settingEntity = JsonHelper.fromJson(settingStrData, SettingEntity.class);
            }else{
                this.settingEntity = new SettingEntity();
            }
        }
        return this.settingEntity;
    }

    public void setSetting(String stringStr){
        if(Helper.isNotNull(stringStr) && !"".equals(stringStr)){
            this.settingEntity = JsonHelper.fromJson(stringStr, SettingEntity.class);
        }else{
            LogHelper.e(TAG, "Setting Data is NULL!");
        }
    }

    public void saveSetting(){
        if(Helper.isNotNull(this.settingEntity)){
            String settingStrData = JsonHelper.toJson(this.settingEntity, SettingEntity.class);
            sPreference.putString(Constants.Preferences.KEY_SETTING_DATA, settingStrData);
        }
    }
    //endregion 设置 相关

    //region 名片上传相关
    private AddVCardEntity mAddVCardEntity = null;
    public AddVCardEntity getAddVCardEntity(){
        return this.mAddVCardEntity;
    }
    public void setAddVCardEntity(AddVCardEntity entity){
        this.mAddVCardEntity = entity;
    }
    //endregion 名片上传相关

    //region get&set
    /**
     * 是否登录
     * @return
     */
    public boolean isLogin(){
        return sPreference.getBoolean(Constants.Preferences.KEY_IS_LOGIN, false);
    }
    /**
     * 设置登录状态
     * @param isLogin
     */
    public void setLogin(boolean isLogin){
        sPreference.putBoolean(Constants.Preferences.KEY_IS_LOGIN, isLogin);
    }
    /**
     * 当前用户是否需要重置密码
     * @return
     */
    public boolean isNeedResetPassword() {
        return sPreference.getBoolean(Constants.Preferences.KEY_IS_NEED_RESET_PASSWORD, false);
    }
    /**
     * 设置当前用户是否需要重置密码
     * @param isNeedResetPassword
     */
    public void setNeedResetPassword(boolean isNeedResetPassword) {
        sPreference.putBoolean(Constants.Preferences.KEY_IS_NEED_RESET_PASSWORD, isNeedResetPassword);
    }
    /**
     * 当前用户是否需要完善个人资料
     * @return
     */
    public boolean isNeedCompleteInfo() {
        return sPreference.getBoolean(Constants.Preferences.KEY_IS_NEED_COMPLETE_INFO, false);
    }
    /**
     * 设置当前用户是否需要完善个人资料
     * @param isNeedCompleteInfo
     */
    public void setNeedCompleteInfo(boolean isNeedCompleteInfo) {
        sPreference.putBoolean(Constants.Preferences.KEY_IS_NEED_COMPLETE_INFO, isNeedCompleteInfo);
    }
    /**
     * 设置是否被下线
     * @param isForcedLogoff
     */
    public void setForcedLogoff(boolean isForcedLogoff) {
        sPreference.putBoolean(Constants.Preferences.KEY_IS_FORCED_LOGOFF, isForcedLogoff);
    }
    /**
     * 是否被迫下线
     * @return
     */
    public boolean isForcedLogoff(){
        return sPreference.getBoolean(Constants.Preferences.KEY_IS_FORCED_LOGOFF, false);
    }
    /**
     * 设置被迫下线信息
     * @param forcedLogoffMessage
     */
    public void setForcedLogoffMessage(String forcedLogoffMessage){
        if(Helper.isNotNull(forcedLogoffMessage) && !"".equals(forcedLogoffMessage)){
            sPreference.putString(Constants.Preferences.KEY_IS_FORCED_LOGOFF_MESSAGE, forcedLogoffMessage);
        }
    }
    /**
     * 取得被迫下线信息
     * @return
     */
    public String getForcedLogoffMessage(){
        return sPreference.getString(Constants.Preferences.KEY_IS_FORCED_LOGOFF_MESSAGE, "");
    }
    /**
     * 展示被迫下线提示框
     * @param activity
     */
    public void showForceLogoffDialog(final FragmentActivity activity){
        String forceLogoffMessage = this.getForcedLogoffMessage();
//        String forceLogoffMessage = "哈哈哈哈";
        // 展示被迫下线提示框
        if(ResourceHelper.isNull(this.mDialogFragmentOffilne)){
            CustomDialogFragment.DialogFragmentInterface mOnClick = new CustomDialogFragment.DialogFragmentInterface() {
                @Override
                public void onDialogClick(int which) {
                    setLogin(false);
                    setForcedLogoff(false);
                    ActivityHelper.switchTo(activity, WXEntryActivity.class, true);
                }
            };
            this.mDialogFragmentOffilne = DialogFragmentHelper.showCustomDialogFragment(R.string.be_forced_offline_hint, forceLogoffMessage, R.string.frg_text_ok, mOnClick);
            //设置返回监听
            this.mDialogFragmentOffilne.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
            //设置外围不可点击
            this.mDialogFragmentOffilne.setCanceledOutSize(false);

        }
        this.mDialogFragmentOffilne.setMessage(forceLogoffMessage);
        if(!this.mDialogFragmentOffilne.isAdded()) {
            this.mDialogFragmentOffilne.show(activity.getSupportFragmentManager(), "mDialogFragmentOffilne");
        }
    }

    /**
     * 用户名是否可编辑
     * @return
     */
    public boolean getUserNameEdt(){
        if(isLogin()){
            UserInfoResultEntity userInfo = getUserInfoEntity();
            if (ResourceHelper.isNotNull(userInfo)){
                if(userInfo.getUpdNameTime() > 0 || ResourceHelper.isEmpty(userInfo.getDisplayName())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 取得个信推送ID
     * @return
     */
    public String getClientId() {
        if(Helper.isNull(this.mClientId)){
            this.mClientId = sPreference.getString(Constants.Preferences.KEY_GE_XIN_CLIENT_ID, "");
        }
        return mClientId;
    }
    /**
     * 设置个信推送ID
     * @param clientId
     */
    public void setClientId(String clientId) {
        this.mClientId = clientId;
        sPreference.putString(Constants.Preferences.KEY_GE_XIN_CLIENT_ID, clientId);
    }
    /**
     * 取得TOKEN
     * @return
     */
    public String getToken() {
        if(Helper.isNull(this.mToken)){
            this.mToken = sPreference.getString(Constants.Preferences.KEY_CURRENT_USER_TOKEN, "");
        }
        return mToken;
    }
    /**
     * 设置TOKEN
     * @param token
     */
    public void setToken(String token) {
        this.mToken = token;
        sPreference.putString(Constants.Preferences.KEY_CURRENT_USER_TOKEN, token);
    }
    /**
     * 设置纬度
     * @param latitude
     */
    public void setLatitude(double latitude){
        if(latitude > 1){
            this.mLatitude = latitude;
            sPreference.putString(Constants.Preferences.KEY_LATITUDE, String.valueOf(latitude));
        }
    }
    /**
     * 获取纬度
     * @return
     */
    public double getLatitude(){
        if(this.mLatitude < 1){
            String str = sPreference.getString(Constants.Preferences.KEY_LATITUDE, "");
            if(!"".equals(str)){
                this.mLatitude = Double.valueOf(str);
            }
        }
        return this.mLatitude;
    }
    /**
     * 设置经度
     * @param longitude
     */
    public void setLongitude(double longitude){
        if(longitude > 1){
            this.mLongitude = longitude;
            sPreference.putString(Constants.Preferences.KEY_LONGITUDE, String.valueOf(longitude));
        }
    }
    /**
     * 获取经度
     * @return
     */
    public double getLongitude(){
        if(this.mLongitude < 1){
            String str = sPreference.getString(Constants.Preferences.KEY_LONGITUDE, "");
            if(!"".equals(str)){
                this.mLongitude = Double.valueOf(str);
            }
        }
        return this.mLongitude;
    }
    /**
     *设置当前用户微片号
     * @param vcardNo
     */
    public void setVCardNo(String vcardNo){
        if(Helper.isNotNull(vcardNo) && !"".equals(vcardNo)) {
            this.mVCardNo = vcardNo;
            sPreference.putString(Constants.Preferences.KEY_CURRENT_USER_VCARD_NO, vcardNo);
            // 需要进行数据库操作及数据请求
            initCurrentUserData();
            //更改缓存
            AsyncImageManager.setCachePath(getCurrentCachePath());
        }
    }
    /**
     * 获取当前用户微片号
     * @return
     */
    public String getVCardNo(){
        if(isLogin() && Helper.isNull(this.mVCardNo)){
            this.mVCardNo = sPreference.getString(Constants.Preferences.KEY_CURRENT_USER_VCARD_NO);
        }
        return this.mVCardNo;
    }

    /**
     * 保存用户资料，并生成UserInfoEntity
     * @param userInfoStr
     * @param entity
     */
    public void saveUserInfoEntity(String userInfoStr, UserInfoResultEntity entity){
        if(Helper.isNotNull(entity)){
            this.mUserInfoEntity = entity;
        }
        if(Helper.isNotNull(userInfoStr) && !"".equals(userInfoStr)){
            sPreference.putString(Constants.Preferences.KEY_CURRENT_USER_INFO, userInfoStr);
//            if(Helper.isNull(this.mUserInfoEntity)){
                this.mUserInfoEntity = JsonHelper.fromJson(userInfoStr, UserInfoResultEntity.class);
//            }
        }
    }
    /**
     * 获取当前用户资料
     * @return
     */
    public UserInfoResultEntity getUserInfoEntity(){
        if(isLogin() && Helper.isNull(this.mUserInfoEntity)){
            String userInfoStr = sPreference.getString(Constants.Preferences.KEY_CURRENT_USER_INFO, "");
            if(Helper.isNotNull(userInfoStr) && !"".equals(userInfoStr)) {
                this.mUserInfoEntity = JsonHelper.fromJson(userInfoStr, UserInfoResultEntity.class);
            }
        }
        return this.mUserInfoEntity;
    }
    /**
     * 保持当前用户名片列表
     * @param cardEntityList
     */
    public void saveVCardEntityList(ArrayList<CardEntity> cardEntityList){
        if(Helper.isNotNull(cardEntityList)){
            this.mVCardList = cardEntityList;
            sPreference.putString(Constants.Preferences.KEY_CURRENT_USER_VCARD_LIST, JsonHelper.toJson(cardEntityList, new TypeToken<ArrayList<CardEntity>>(){}.getType()));
        }
    }
    /**
     * 获取当前用户名片列表
     * @return
     */
    public ArrayList<CardEntity> getVCardEntityList(){
        if(Helper.isNull(this.mVCardList)){
            String vcardListStr = sPreference.getString(Constants.Preferences.KEY_CURRENT_USER_VCARD_LIST, "");
            if(!"".equals(vcardListStr)){
                this.mVCardList = JsonHelper.fromJson(vcardListStr, new TypeToken<ArrayList<CardEntity>>(){}.getType());
            }
            if(Helper.isNull(this.mVCardList)){
                this.mVCardList = new ArrayList<CardEntity>();
            }
        }
        return this.mVCardList;
    }

    /**
     * 更改名片数量
     */
    public void updateVCardEntityListCount(long cardId, int cardCount, boolean isSub){
        ArrayList<CardEntity> vCardList = getVCardEntityList();
        if(ResourceHelper.isNotNull(vCardList)){
            for (int i = 0, size = vCardList.size(); i < size; i++) {
                CardEntity curCard = vCardList.get(i);
                if(ResourceHelper.isNotNull(curCard)){
                    if (curCard.getId() == cardId) {
                        if (isSub) {
                            int nowCardCount = curCard.getCardCount() - cardCount;
                            nowCardCount = nowCardCount > 0 ? nowCardCount : 0;
                            curCard.setCardCount(nowCardCount);
                        } else {
                            curCard.setCardCount(cardCount);
                        }
                    }
                }
            }
            saveVCardEntityList(vCardList);
            Intent toMyCardIntent = new Intent(Constants.ActionIntent.ACTION_INTENT_MY_CARD);
            ActivityHelper.getGlobalApplicationContext().sendBroadcast(toMyCardIntent);
        }
    }

    /**
     * 设置当前用户名片位置
     * @param position
     */
    public void setCurrentVCardPosition(int position){
        sPreference.putInt(Constants.Preferences.KEY_CURRENT_VCARD_POSITION, position);
    }
    /**
     *获取当前用户名片位置
     * @return
     */
    public int getCurrentVCardPosition(){
        return sPreference.getInt(Constants.Preferences.KEY_CURRENT_VCARD_POSITION, 0);
    }
    /**
     *获取当前用户名片
     * @return
     */
    public CardEntity getCurrentVCardEntity(){
        CardEntity result = null;
        ArrayList<CardEntity> cardEntityList = this.getVCardEntityList();
        int cardEntityListSize = cardEntityList.size();
        if(cardEntityListSize != 0){
            if(cardEntityListSize <= this.getCurrentVCardPosition()){
                this.setCurrentVCardPosition(cardEntityListSize - 1);
            }
            result = cardEntityList.get(this.getCurrentVCardPosition());
        }
        return result;
    }
    //region 简易登陆列表
    /**
     * 获取简易登陆列表
     * @return
     */
    public LoginSimpleInfoListEntity getLoginSimpleInfoList(){
        LoginSimpleInfoListEntity result = null;
        String strLoginSimpleInfoList = sPreference.getString(Constants.Preferences.KEY_SAVE_LOGIN_SIMPLE_INFO_LIST, "");
        if(!"".equals(strLoginSimpleInfoList)){
            result = JsonHelper.fromJson(strLoginSimpleInfoList, LoginSimpleInfoListEntity.class);
        }
        return result;
    }

    /**
     * 添加简易登陆信息
     * @param userinfoEntity
     * @param isRememberPassword
     */
    public void addLoginSimpleInfo(UserInfoResultEntity userinfoEntity, boolean isRememberPassword){
        LoginSimpleInfoListEntity loginSimpleListEntity =  getLoginSimpleInfoList();
        if(Helper.isNull(loginSimpleListEntity)){
            loginSimpleListEntity = new LoginSimpleInfoListEntity();
        }
        ArrayList<LoginSimpleInfoEntity> loginSimpleList = loginSimpleListEntity.getLoginSimpleInfoEntityList();
        if(Helper.isNull(loginSimpleList)){
            loginSimpleList = new ArrayList<LoginSimpleInfoEntity>();
            loginSimpleListEntity.setLoginSimpleInfoEntityList(loginSimpleList);
        }
        LoginSimpleInfoEntity loginSimpleInfoEntity = new LoginSimpleInfoEntity(userinfoEntity);
        loginSimpleInfoEntity.setIsRememberPassword(isRememberPassword);
        int length = loginSimpleList.size();
        if(length == 0){
            loginSimpleList.add(loginSimpleInfoEntity);
        }else{
            String vcardNo = loginSimpleInfoEntity.getVcardNo();
            for(int i = 0; i < length; i++){
                LoginSimpleInfoEntity entity = loginSimpleList.get(i);
                if(vcardNo.equals(entity.getVcardNo())){
                    loginSimpleList.remove(i);
                    break;
                }
            }
            loginSimpleList.add(0, loginSimpleInfoEntity);
        }
        sPreference.putString(Constants.Preferences.KEY_SAVE_LOGIN_SIMPLE_INFO_LIST, JsonHelper.toJson(loginSimpleListEntity));
    }

    /**
     * 删除简易登陆信息
     * @param vcardNo 微片号
     * @return
     */
    public boolean removeLoginSimpleInfo(String vcardNo){
        LoginSimpleInfoListEntity loginSimpleListEntity =  getLoginSimpleInfoList();
        if(Helper.isNull(loginSimpleListEntity)) {
            return false;
        }
        ArrayList<LoginSimpleInfoEntity> loginSimpleList = loginSimpleListEntity.getLoginSimpleInfoEntityList();
        if(Helper.isNull(loginSimpleList) || loginSimpleList.size() == 0){
            return false;
        }
        for(int i = 0, len = loginSimpleList.size(); i < len; i++){
            LoginSimpleInfoEntity entity = loginSimpleList.get(i);
            if(vcardNo.equals(entity.getVcardNo())){
                loginSimpleList.remove(i);
                return true;
            }
        }
        return  false;
    }
    //endregion 简易登陆列表

    //endregion get&set
    /**
     * 退出，用于保存数据，区别于logout（注销）
     */
    public void quit(){
        //保存设置
        this.saveSetting();
        // 保存当前用户名片
        this.saveVCardEntityList(this.getVCardEntityList());
        //TODO 保存当前用户未上传名片
        // 保存当前用户资料
        this.saveUserInfoEntity(JsonHelper.toJson(this.getUserInfoEntity(), UserInfoResultEntity.class), null);
        //TODO 保存
    }
    /**
     * 注销
     */
    public void logout(){
        //清除XML所有个人信息
//        sPreference.clear();
//        sInstance = null;
        //改变登录状态
        this.setLogin(false);
        //关闭数据库
        VCardSQLiteDatabase.getInstance().closeSQLiteDatabase();
    }

    /**
     * 初始化用户数据
     */
    public void initCurrentUserData(){
        if(this.isLogin()){
            //初始化数据库
            VCardSQLiteDatabase.getInstance().initSQLiteDatabase(getCurrentDataPath());
        }else{
            LogHelper.d(TAG, "当前用户未登录！");
        }
    }

    //endregion public方法

    //region static 方法
    /**
     * 取得当前缓存路径
     * @return
     */
    public static String getCurrentCachePath(){
        return String.format(PATH_FORMAT, getInstance().getVCardNo(), NAME_CACHE);
    }
    /**
     * 取得当前数据路径
     * @return
     */
    public static String getCurrentDataPath(){
        return String.format(PATH_FORMAT, getInstance().getVCardNo(), NAME_DATA);
    }
    //endregion static 方法


}
