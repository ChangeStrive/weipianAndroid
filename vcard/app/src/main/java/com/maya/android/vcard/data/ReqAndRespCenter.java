package com.maya.android.vcard.data;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.maya.android.jsonwork.AbstractDataCommand.CommandCallback;
import com.maya.android.jsonwork.JSONCommand;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.NetworkHelper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.Constants;
import com.maya.android.vcard.entity.SettingEntity;
import com.maya.android.vcard.entity.TerminalAgentEntity;
import com.maya.android.vcard.entity.json.SaveSettingJsonEntity;
import com.maya.android.vcard.entity.result.SettingResultEntity;
import com.maya.android.vcard.entity.result.URLResultEntity;
import com.maya.android.vcard.util.ProjectHelper;

import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 请求、返回中心
 * Created by YongJi on 2015/8/25.
 */
public class ReqAndRespCenter implements CommandCallback<JSONObject> {

    private static final String TAG = ReqAndRespCenter.class.getSimpleName();

    //region 单例
    private static ReqAndRespCenter sInstance = null;
    public static ReqAndRespCenter getInstance(){
        if(Helper.isNull(sInstance)){
            sInstance = new ReqAndRespCenter();
        }
        return sInstance;
    }
    private ReqAndRespCenter(){}
    //endregion

    //region 变量
    private static final String RESULT_RESPONSE_CODE = "responseCode";
    private static final String RESULT_RESPONSE_DATA = "data";
    private static final String RESULT_RESPONSE_MSGINFO = "msgInfo";
    private static final String DATA_NULL = "null";
    private static final String ARRAY_NULL = "[]";
    /** Entity：头参 **/
    private TerminalAgentEntity mTerminalAgentEntity;
    /** String：头参 **/
    private String mTerminalAgentString = null;
    private Context mContext = ActivityHelper.getGlobalApplicationContext();
    //endregion

    //region Override方法
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
                LogHelper.d(TAG, "data:" + data);
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
    //endregion

    //region public 方法

    //region 请求命令，回调外部
    /**
     * 发送JSON请求命令,并等待返回处理
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param accessToken token
     * @param isGZip 是否压缩
     * @param isEncrypt 是否加密
     * @param json 请求参数
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects 附加Objects
     * @return
     */
    public final JSONCommand postForResult(int tag, String url, String accessToken, boolean isGZip, boolean isEncrypt
            , JSONObject json, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        return JSONCommand.postForResult(tag, url, accessToken, isGZip, isEncrypt, json, getTerminalAgent(), Helper.isNull(jsonCallback) ? this : jsonCallback, extraObjects);
    }
    /**
     * 发送JSON请求命令,并等待返回处理（默认不压缩）
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param accessToken token
     * @param isEncrypt 是否加密
     * @param json 请求参数
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects 附加Objects
     * @return
     */
    public final JSONCommand postForResult(int tag, String url, String accessToken, boolean isEncrypt
            , JSONObject json, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        return postForResult(tag, url, accessToken, false, isEncrypt, json, jsonCallback, extraObjects);
    }
    /**
     * 发送JSON请求命令,并等待返回处理（默认不压缩、不加密）
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param accessToken token
     * @param json 请求参数
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects 附加Objects
     * @return
     */
    public final JSONCommand postForResult(int tag, String url, String accessToken, JSONObject json, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        return postForResult(tag, url, accessToken, false, json, jsonCallback, extraObjects);
    }
    /**
     * 发送JSON请求命令,并等待返回处理（默认不压缩、不加密）
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param json 请求参数
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects 附加Objects
     * @return
     */
    public final JSONCommand postForResult(int tag, String url, JSONObject json, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        return postForResult(tag, url,  CurrentUser.getInstance().getToken(), false, json, jsonCallback, extraObjects);
    }
    /**
     * 发送JSON请求命令,并等待返回处理（默认不压缩、不加密）
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param accessToken token
     * @param jsonStr 请求参数字符串
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects 附加Objects
     * @return
     */
    public final JSONCommand postForResult(int tag, String url, String accessToken, String jsonStr, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        JSONObject json = null;
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postForResult(tag, url, accessToken, json, jsonCallback, extraObjects);
    }

    /**
     * 发送JSON请求命令,并等待返回处理（默认不压缩、不加密）
     * <p>accessToken为当前用户CurrentUser.getInstance().getToken()</p>
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param jsonStr 请求参数字符串
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects 附加Objects
     * @return
     */
    public final JSONCommand postForResult(int tag, String url, String jsonStr, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        return postForResult(tag, url, CurrentUser.getInstance().getToken(), jsonStr, jsonCallback, extraObjects);
    }
    /**
     * Get请求
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param accessToken token
     * @param json 请求参数
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects
     * @return
     */
    public final JSONCommand getForResult(int tag, String url, String accessToken, JSONObject json, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        return JSONCommand.getForResult(tag, url, accessToken, json, this.getTerminalAgent(), Helper.isNull(jsonCallback) ? this : jsonCallback, extraObjects);
    }
    /**
     * 上传文件
     * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
     * @param url 请求地址
     * @param filePath File路径
     * @param newFileName
     * @param json 请求参数
     * @param jsonCallback 命令回调(可为NULL，为NULL时，返回结果在{@link ReqAndRespCenter}
     * @param extraObjects
     * @return
     */
    public final JSONCommand upload(int tag, String url, String filePath, String newFileName
            , JSONObject json, List<NameValuePair> paramsList, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
        return JSONCommand.upload(tag, url, filePath, newFileName, json, this.getTerminalAgent(), paramsList, Helper.isNull(jsonCallback) ? this : jsonCallback, extraObjects);
    }
    //endregion 请求命令，回调外部 end

    //region get&set
    public String getTerminalAgent(){
        boolean isTerminalAgentChange = false;
        TerminalAgentEntity entity = this.getTerminalAgentEntity();
        if(entity.getLatitude() != CurrentUser.getInstance().getLatitude()
                || entity.getLongitude() != CurrentUser.getInstance().getLongitude()){
            isTerminalAgentChange = true;
            entity.setLatitude(CurrentUser.getInstance().getLatitude());
            entity.setLongitude(CurrentUser.getInstance().getLongitude());
        }
        String newCid = this.getCellLocationId();
        if(!newCid.equals(entity.getBaseStationInfo())){
            isTerminalAgentChange = true;
            entity.setBaseStationInfo(newCid);
        }
        int netType = NetworkHelper.getNetworkType(this.mContext);
        if(netType != entity.getNetType()){
            isTerminalAgentChange = true;
            entity.setNetType(netType);
        }
        String clientId = entity.getClientId();
        String newClientId = CurrentUser.getInstance().getClientId();
        if(Helper.isNotEmpty(newClientId) && !newClientId.equals(clientId)){
            isTerminalAgentChange = true;
            entity.setClientId(newCid);
        }
        if(Helper.isNull(this.mTerminalAgentString) || isTerminalAgentChange){
            try {
                this.mTerminalAgentString = URLEncoder.encode(JsonHelper.toJson(entity), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return this.mTerminalAgentString;
    }

    public TerminalAgentEntity getTerminalAgentEntity(){
        if(Helper.isNull(this.mTerminalAgentEntity)){
            this.initTerminalAgentEntity();
        }
        return this.mTerminalAgentEntity;
    }
    //endregion get&set

    //region 部分接口请求
    /**
     * 请求地址列表
     */
    public void requestAddressList(){
        postForResult(Constants.CommandRequestTag.CMD_LIST, Constants.URL.URL_VCARD_ADDRESS_LIST, null, new JSONObject(), this);
    }
    /**
     * 请求设置
     */
    public void requestSetting(){
        if (NetworkHelper.isNetworkAvailable(this.mContext)) {
            //有网络，进行请求设置
            URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
            String url = ProjectHelper.fillRequestURL(urlEntity.getMySetting());
            postForResult(Constants.CommandRequestTag.CMD_SETTING, url, new JSONObject(), this);
        }
    }
    /**
     * 请求保存设置
     * @param entity
     */
    public void requestSaveSetting(SettingEntity entity){
        if(Helper.isNotNull(entity)) {
            if (NetworkHelper.isNetworkAvailable(this.mContext)) {
                //有网络，进行保存设置
                URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
                String url = ProjectHelper.fillRequestURL(urlEntity.getMySettingSave());
                SaveSettingJsonEntity jsonEntity = new SaveSettingJsonEntity(entity);
                String json = JsonHelper.toJson(jsonEntity, SaveSettingJsonEntity.class);
                this.postForResult(Constants.CommandRequestTag.CMD_SETTING_SAVE, url, json, this);
            }
        }
    }
    /**
     * 取得短信验证码
     * @param mobile
     * @param smsFlag 1-注册验证码，2-手机绑定验证码，3-重设密码验证码 详细见：{@link com.maya.android.vcard.constant.Constants.GetSMSFlag}
     */
    public void getValidSMSFromService(String mobile, int smsFlag){
        getValidSMSFromService(mobile, smsFlag, this);
    }
    /**
     * 取得短信验证码
     * @param mobile
     * @param smsFlag 1-注册验证码，2-手机绑定验证码，3-重设密码验证码 详细见：{@link com.maya.android.vcard.constant.Constants.GetSMSFlag}
     * @param jsonCallback
     */
    public void getValidSMSFromService(String mobile, int smsFlag, CommandCallback<JSONObject> jsonCallback){
        if(NetworkHelper.isNetworkAvailable(this.mContext)){
            //有网络进行登录
            URLResultEntity urlEntity = CurrentUser.getInstance().getURLEntity();
            if(Helper.isNotNull(urlEntity)){
                String authCodeUrl = ProjectHelper.fillRequestURL(urlEntity.getSmsVerifyCode());
                String url = authCodeUrl + "?mobile=" + mobile +"&smsFlag=" + smsFlag;
                getForResult(Constants.CommandRequestTag.CMD_AUTH_CODE, url, null, new JSONObject(), Helper.isNull(jsonCallback) ? this : jsonCallback);
            }
        }else{
            //无网络进行提示
            ActivityHelper.showToast(R.string.no_network_please_open_network);
        }
    }
    //endregion 部分接口请求
    //endregion public 方法

    //region private 方法
    private void initTerminalAgentEntity(){
        TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String clientVerType = Helper.isNull(Build.TYPE) ? DATA_NULL : Build.DISPLAY;
//		String clientVerNum = Helper.isNull(Build.VERSION.RELEASE) ? DATA_NULL : Build.VERSION.RELEASE;
        String clientVerNum = String.valueOf(ActivityHelper.getCurrentVersion());
        String clientChannelType = DATA_NULL;
        String osType = tm.getDeviceSoftwareVersion();
        String menuVerNum = DATA_NULL;
        String imei = Helper.isNull(tm.getDeviceId()) ? DATA_NULL : tm.getDeviceId();
        String imsi = tm.getSubscriberId();
        String mobile = Helper.isNull(tm.getLine1Number()) ? DATA_NULL : tm.getLine1Number();
        //手机品牌
//		String brand = android.os.Build.BRAND;
        String operatorName = getProviderInfo(tm);
        String operatorNum = tm.getSubscriberId();
        String macAddress = ActivityHelper.getMacAddress();
        String clientId = CurrentUser.getInstance().getClientId();

        this.mTerminalAgentEntity = new TerminalAgentEntity(clientVerType, clientVerNum, clientChannelType, osType, menuVerNum, imsi, imei, mobile, operatorName, operatorNum, macAddress, clientId);
    }
    /**
     * 预处理返回结果
     * @param commandResult
     * @return
     */
    private boolean preProccessResult(JSONObject commandResult){
        return Helper.isNull(commandResult) || "".equals(commandResult.toString());
    }
    /**&
     * 命令返回时调用
     * @param tag - 命令标识&
     * @param commandResult - 返回结果
     * @param objects - 返回的其它数据
     * @return 是否已被处理(已处理则返回true,之后的处理都不会被执行)
     */
    private boolean onCommandCallback2(int tag, JSONObject commandResult, Object... objects) {
        switch(tag){
            case Constants.CommandRequestTag.CMD_LIST:
                if(!preProccessResult(commandResult)){
                    CurrentUser.getInstance().saveURLEntity(commandResult.toString(), null);
                    return true;
                }else{
                    this.requestAddressList();
                }
                return false;
            case Constants.CommandRequestTag.CMD_AUTH_CODE:
                //获取短信验证码
                return false;
            case Constants.CommandRequestTag.CMD_SETTING:
                if(!preProccessResult(commandResult)){
                    //TODO 数据异常
                    try {
                        SettingResultEntity resultEntity = JsonHelper.fromJson(commandResult, SettingResultEntity.class);
                        if (Helper.isNotNull(resultEntity)) {
                            CurrentUser.getInstance().setSetting(resultEntity.getSettingStr());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
        }
        return false;
    }
    /**
     *命令返回时调用：成功无结果
     * @param tag - 命令标识
     * @param msgInfo - 提示信息
     */
    private void onResponseSuccess(int tag, String msgInfo){

    }
    /**
     *命令返回时调用：失败
     * @param tag - 命令标识
     * @param responseCode - 错误代码
     * @param msgInfo - 提示信息
     */
    private void onResponseFail(int tag, int responseCode, String msgInfo){

    }
    /**
     * 获取手机服务提供商(中国移动,中国联通,中国电信) "460"表示国家,00,02表示移动,01表示联通,03表示电信
     * @return
     */
    private String getProviderInfo(TelephonyManager tm) {
        //判断sim卡状态
        int simState = tm.getSimState();
        String providerInfo = "";
        if(simState == TelephonyManager.SIM_STATE_READY){
            tm.getNetworkOperatorName();
        }
        String imsi = tm.getSubscriberId();
        if(Helper.isNull(imsi)){
            return providerInfo;
        }
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
//            providerInfo = "中国移动";
            providerInfo = this.mContext.getString(R.string.china_mobile);
        } else if (imsi.startsWith("46001")) {
//            providerInfo = "中国联通";
            providerInfo = this.mContext.getString(R.string.china_unicom);
        } else if (imsi.startsWith("46003")) {
//            providerInfo = "中国电信";
            providerInfo = this.mContext.getString(R.string.china_telecom);
        }
        String result = "";
        try {
            result = new String(providerInfo.getBytes(), HTTP.ISO_8859_1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 获取基站信息
     */
    private String getCellLocationId(){
        int result = 0;
//		try {
//			TelephonyManager tm = (TelephonyManager) ActivityHelper.getGlobalApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
//			if (location != null) {
//				result = location.getCid();
//			}
//		} catch (ClassCastException e) {
//			e.printStackTrace();
//		}
        return String.valueOf(result);
    }
    //endregion private 方法
}
