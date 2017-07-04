package com.maya.android.jsonwork;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.GZipHelper;
import com.maya.android.utils.Helper;
import com.maya.android.utils.LogHelper;
import com.maya.android.utils.NetworkHelper;
/**
 * JSONCommand JSON请求帮助类
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-7-8
 *
 */
public class JSONCommand extends AbstractDataCommand<JSONObject> {
	@SuppressWarnings("unused")
	private String mVerification = "135270700791922253";
	//#region 常量
	private static final String TAG = JSONCommand.class.getSimpleName();
	/**
	 * 发送方式:POST
	 */
	public static final int SEND_TYPE_POST 		= 0;
	/**
	 * 发送方式:GET
	 */
	public static final int SEND_TYPE_GET  		= 1;
	/**
	 * 发送方式:上传
	 */
	public static final int SEND_TYPE_UPLOAD  	= 2;
	private static final String KEY_HTTP_REQUEST_PARAMS_ACCESS_TOKEN = "accessToken";
	private static final String KEY_HTTP_REQUEST_PARAMS_IS_ENCRYPT = "encrypt";
	private static final String KEY_HTTP_REQUEST_PARAMS_JSON = "data";
	//#endregion 常量
	
	//#region 变量
	private String sEncryptKey = null;
	private JSONObject mJson;
	private int mSendType = SEND_TYPE_POST;
	private boolean isEncrypt = false;
	private boolean isGZip = false;
	private String mAccessToken;
	/**
	 * 上传的文件
	 */
	private File mFileForUpload;
	/**
	 * 新文件名,用于上传后的换名
	 */
	private String mNewFileName;
	/**
	 * 提交文件时，指定Content-Disposition中的name字段的值
	 */
	private String mFormName;
	//#endregion 变量
	
	
	//#region 构造方法
	public JSONCommand(){
		
	}
	/**
	 * 构造方法(无回调)
	 * @param tag 标示
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isGZip 是否压缩
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent
	 */
	public JSONCommand(String url, String accessToken, boolean isGZip, boolean isEncrypt, JSONObject json, String terminalAgent){
		this(0, url, accessToken, isGZip, isEncrypt, json, terminalAgent, null);
	}
	/**
	 * 构造方法(无回调，默认不压缩)
	 * @param tag 标示
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent
	 */
	public JSONCommand(String url, String accessToken, boolean isEncrypt, JSONObject json, String terminalAgent){
		this(url, accessToken, false, isEncrypt, json, terminalAgent);
	}
	/**
	 * 构造方法(无回调，默认不压缩、不加密)
	 * @param tag 标示
	 * @param url 请求地址
	 * @param accessToken token
	 * @param json 请求参数
	 * @param terminalAgent
	 */
	public JSONCommand(String url, String accessToken, JSONObject json, String terminalAgent){
		this(url, accessToken, false, json, terminalAgent);
	}
	/**
	 * 构造方法
	 * @param tag 标示
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isGZip 是否压缩
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent
	 * @param jsonCallback 回调
	 * @param extraObjects 附加Object
	 */
	public JSONCommand(int tag, String url, String accessToken, boolean isGZip, boolean isEncrypt, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects ){
		super(tag, url, terminalAgent, null, jsonCallback, extraObjects);
		this.mAccessToken = accessToken;
		this.mJson = json;
		this.isEncrypt = isEncrypt;
		this.isGZip = isGZip;
	}
	/**
	 * 构造方法（默认不压缩）
	 * @param tag 标示
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent
	 * @param jsonCallback 回调
	 * @param extraObjects 附加Object
	 */
	public JSONCommand(int tag, String url, String accessToken, boolean isEncrypt, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects ){
		this(tag, url, accessToken, false, isEncrypt, json, terminalAgent, jsonCallback, extraObjects);
	}
	/**
	 * 构造方法（默认不压缩、不加密）
	 * @param tag 标示
	 * @param url 请求地址
	 * @param accessToken token
	 * @param json 请求参数
	 * @param terminalAgent
	 * @param jsonCallback 回调
	 * @param extraObjects 附加Object
	 */
	public JSONCommand(int tag, String url, String accessToken, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
		this(tag, url, accessToken, false, json, terminalAgent, jsonCallback, extraObjects);
	}
	/**
	 * 构造方法(上传用)
	 * @param tag
	 * @param url
	 * @param file 
	 * @param newFileName 服务器收到的文件名
	 * @param json
	 * @param terminalAgent
	 * @param jsonCallback
	 * @param extraObjects
	 */
	public JSONCommand(int tag, String url, File file, String newFileName, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object... extraObjects){
		this(tag, url, file, newFileName, json, terminalAgent, null, jsonCallback, extraObjects);
	}
	/**
	 * 构造方法(上传用)
	 * @param tag
	 * @param url
	 * @param file
	 * @param newFileName 服务器收到的文件名
	 * @param json
	 * @param terminalAgent
	 * @param paramsList
	 * @param jsonCallback
	 * @param extraObjects
	 */
	public JSONCommand(int tag, String url, File file, String newFileName, JSONObject json
			, String terminalAgent, List<NameValuePair> paramsList, CommandCallback<JSONObject> jsonCallback, Object... extraObjects){
		this(tag, url, file, newFileName, "", json, terminalAgent, paramsList, jsonCallback, extraObjects);
	}
	/**
	 * 构造方法(上传用)
	 * @param tag
	 * @param url
	 * @param file
	 * @param newFileName 服务器收到的文件名
	 * @param formName 提交文件时，指定Content-Disposition中的name字段的值
	 * @param json
	 * @param terminalAgent
	 * @param paramsList
	 * @param jsonCallback
	 * @param extraObjects
	 */
	public JSONCommand(int tag, String url, File file, String newFileName
			, String formName, JSONObject json, String terminalAgent, List<NameValuePair> paramsList
			, CommandCallback<JSONObject> jsonCallback, Object... extraObjects){
		super(tag, url, terminalAgent, paramsList, jsonCallback, extraObjects);
		this.mJson = json;	
		this.mNewFileName = newFileName;
		this.mFormName = formName;
		this.mFileForUpload = file;
	}
	//#endregion 构造方法
	
	//#region Getting & Setting 集
	public boolean isEncrypt() {
		return isEncrypt;
	}
	
	public void setEncrypt(boolean isEncrypt) {
		this.isEncrypt = isEncrypt;
	}
	
	public String getAccessToken() {
		return mAccessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.mAccessToken = accessToken;
	}
	
	public JSONObject getJson() {
		return mJson;
	}
	
	public void setJson(JSONObject json) {
		this.mJson = json;
	}
	
	public int getSendType() {
		return mSendType;
	}
	
	public void setSendType(int sendType) {
		this.mSendType = sendType;
	}
	
	public String getEncryptKey() {
		return sEncryptKey;
	}
	
	public void setEncryptKey(String sEncryptKey) {
		this.sEncryptKey = sEncryptKey;
	}
	//#endregion Getting & Setting 集
	
	//#region Override 方法
	@Override
	protected JSONObject doInBackground(Void... params) {
		JSONObject result = null;     
		HttpResponse response = null;
		String responseStr = null;
		switch(this.getSendType()){
		case SEND_TYPE_POST:
			if(Helper.isNotEmpty(this.getUrl())){
				LogHelper.d(TAG, "请求地址：" + this.getUrl());
			}
			if(Helper.isNotEmpty(this.mJson)){
				LogHelper.d(TAG, "请求查参数：" + mJson.toString());
			}else{
				LogHelper.d(TAG, "请求查参数：为Null！");
			}
			response = NetworkHelper.sendHttpPostString(this.getUrl(), this.createRequestParams(mJson)
					, this.getTerminalAgent(), this.isGZip, this.isEncrypt);
			if(Helper.isNotNull(response) && NetworkHelper.isHttpResponseSuccess(response)){
				responseStr = NetworkHelper.getHttpResponseString(response);
				if(Helper.isNotNull(responseStr) && Helper.isNotEmpty(responseStr)){
					result = decodeResponseResult(responseStr);
//					try {
//						JSONObject responseJson = new JSONObject(responseStr);
//						if((this.isEncrypt || this.isGZip) && responseStr.contains(KEY_HTTP_REQUEST_PARAMS_JSON)
//								&& Helper.isNotNull(responseJson)){
//							String dataStr = responseJson.getString(KEY_HTTP_REQUEST_PARAMS_JSON);
//							if(Helper.isNotNull(dataStr)){
//								if(this.isEncrypt && Helper.isNotNull(this.sEncryptKey)){
//									try{
//										dataStr = Helper.decryptByAES(Helper.parseHexStr2Byte(dataStr), this.sEncryptKey);
//									}catch(Exception e){
//										e.printStackTrace();
//										//AES解密失败
//										Log.e(TAG, "request param DE-ENCRYPT fail！！！！！！！");
//									}
//								}
//								if(this.isGZip){
//									try {
//										dataStr = Helper.parseByte2HexStr(GZipHelper.decompress(Helper.parseHexStr2Byte(dataStr)));
//									} catch (Exception e) {
//										e.printStackTrace();
//										//GZIP 解压缩失败
//										Log.e(TAG, "request param DEGZIP fail！！！！！！！");
//									}
//								}
//								if(Helper.isNotNull(dataStr)){
//									responseJson.putOpt(KEY_HTTP_REQUEST_PARAMS_JSON, dataStr);
//								}
//							}
//						}
//						result = responseJson;
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
				}
			}
			break;
		case SEND_TYPE_GET:
			if(Helper.isNotEmpty(this.getUrl())){
				LogHelper.d(TAG, "请求地址：" + this.getUrl());
			}
			response = NetworkHelper.sendHttpGetString(this.getUrl()
					, this.getTerminalAgent(), this.isGZip, this.isEncrypt);
			if(Helper.isNotNull(response) && NetworkHelper.isHttpResponseSuccess(response)){
				responseStr = NetworkHelper.getHttpResponseString(response);
				if(Helper.isNotNull(responseStr) && Helper.isNotEmpty(responseStr)){
					result = decodeResponseResult(responseStr);
//					JSONObject responseJson;
//					try {
//						responseJson = new JSONObject(responseStr);
////						if(Helper.isNotNull(responseJson)){
////							result = responseJson.getJSONObject(KEY_HTTP_REQUEST_PARAMS_JSON);
////						} 
//						result = responseJson;
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
				}
			}
			break;
		case SEND_TYPE_UPLOAD:
			responseStr = NetworkHelper.upload(
					this.getUrl()
					, createRequestParams(this.mJson)
					, this.mFileForUpload
					, this.mNewFileName
					, this.mFormName);
			if (Helper.isNotEmpty(responseStr)){
				try {
					result = new JSONObject(responseStr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		}
		if(Helper.isNotEmpty(result)){
			LogHelper.d(TAG, "请求返回的结果：" + result.toString());
		}
		return result;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		if (Helper.isNull(result)){
			LogHelper.d(TAG, "Http response was wrong");
		}
		if (Helper.isNotNull(this.getCallback())){
			this.getCallback().onCommandCallback(this.getTag(), result, this.getExtraObjects());
		}
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	//#endregion Override 方法
	
	//#region private 方法
	/**
	 * 判断是否有网络
	 * @return
	 */
	private static boolean hasNetwork(){
		if(NetworkHelper.isNetworkAvailable(ActivityHelper.getGlobalApplicationContext())){
			return true;
		}
		LogHelper.i(TAG, "No connect to network!");
		return false;
	}
	/**
	 * 创建请求参数列表
	 * @param json
	 * @return
	 */
	private List<NameValuePair> createRequestParams(JSONObject json){
		List<NameValuePair> result = this.getParamsList();
		if(Helper.isNull(result)){
			result = new ArrayList<NameValuePair>();
		}
		if(Helper.isNotNull(this.mAccessToken)){
			result.add(new BasicNameValuePair(KEY_HTTP_REQUEST_PARAMS_ACCESS_TOKEN, this.mAccessToken));
		}
		result.add(new BasicNameValuePair(KEY_HTTP_REQUEST_PARAMS_IS_ENCRYPT, Boolean.toString(this.isEncrypt)));
		if(Helper.isNotEmpty(json)){
			String jsonStr = json.toString();
			if(this.isGZip){
				try {
					jsonStr = Helper.parseByte2HexStr(GZipHelper.compress(jsonStr.getBytes()));
				} catch (Exception e) {
					e.printStackTrace();
					//压缩失败
					LogHelper.e(TAG, "request param GZIP fail！！！！！！！");
				}
			}
			if(this.isEncrypt && Helper.isNotNull(this.sEncryptKey)){
				try{
					jsonStr = Helper.parseByte2HexStr(Helper.encryptByAES(jsonStr, this.sEncryptKey));
				}catch(Exception e){
					e.printStackTrace();
					//AES 失败
					LogHelper.e(TAG, "request param ENCRYPT fail！！！！！！！");
				}
			}
			try {
				jsonStr = URLEncoder.encode(jsonStr, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			result.add(new BasicNameValuePair(KEY_HTTP_REQUEST_PARAMS_JSON, jsonStr));
		}
		return result;
	}
	/**
	 * 对返回结果进行处理
	 * @param responseStr
	 * @return
	 */
	private JSONObject decodeResponseResult(String responseStr){
		JSONObject result = null;
		try {
			JSONObject responseJson = new JSONObject(responseStr);
			if((this.isEncrypt || this.isGZip) && responseStr.contains(KEY_HTTP_REQUEST_PARAMS_JSON)
					&& Helper.isNotNull(responseJson)){
				String dataStr = responseJson.getString(KEY_HTTP_REQUEST_PARAMS_JSON);
				if(Helper.isNotNull(dataStr)){
					if(this.isEncrypt && Helper.isNotNull(this.sEncryptKey)){
						try{
							dataStr = Helper.decryptByAES(Helper.parseHexStr2Byte(dataStr), this.sEncryptKey);
						}catch(Exception e){
							e.printStackTrace();
							//AES解密失败
							LogHelper.e(TAG, "request param DE-ENCRYPT fail！！！！！！！");
						}
					}
					if(this.isGZip){
						try {
							dataStr = Helper.parseByte2HexStr(GZipHelper.decompress(Helper.parseHexStr2Byte(dataStr)));
						} catch (Exception e) {
							e.printStackTrace();
							//GZIP 解压缩失败
							LogHelper.e(TAG, "request param DEGZIP fail！！！！！！！");
						}
					}
					if(Helper.isNotNull(dataStr)){
						responseJson.putOpt(KEY_HTTP_REQUEST_PARAMS_JSON, dataStr);
					}
				}
			}
			result = responseJson;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	//#endregion private 方法
	
	//#region public 方法
	/**
	 * 发送JSON请求命令,并等待返回处理(默认发送格式)
	 * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isGZip 是否压缩
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @param jsonCallback 命令回调
	 * @param extraObjects 附加Objects
	 * @return
	 */
	public static JSONCommand postForResult(int tag, String url, String accessToken, boolean isGZip, boolean isEncrypt, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
		if(!hasNetwork()){
			return null;
		}
		JSONCommand command = new JSONCommand(tag, url, accessToken, isGZip, isEncrypt, json, terminalAgent, jsonCallback, extraObjects);
		command.execute();
		return command;
	}
	/**
	 * 发送JSON请求命令,并等待返回处理(默认发送格式，不压缩)
	 * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @param jsonCallback 命令回调
	 * @param extraObjects 附加Objects
	 * @return
	 */
	public static JSONCommand postForResult(int tag, String url, String accessToken, boolean isEncrypt, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
		return postForResult(tag, url, accessToken, false, isEncrypt, json, terminalAgent, jsonCallback, extraObjects);
	}
	/**
	 * 发送JSON请求命令,并等待返回处理(默认发送格式，默认不压缩、不加密)
	 * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
	 * @param url 请求地址
	 * @param accessToken token
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @param jsonCallback 命令回调
	 * @param extraObjects 附加Objects
	 * @return
	 */
	public static JSONCommand postForResult(int tag, String url, String accessToken, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
		return postForResult(tag, url, accessToken, false, json, terminalAgent, jsonCallback, extraObjects);
	}
	/**
	 * 发送JSON请求命令, 无需返回(默认发送格式)
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isGZip 是否压缩
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @return
	 */
	public static JSONCommand post(String url, String accessToken, boolean isGZip, boolean isEncrypt, JSONObject json, String terminalAgent){
		if(!hasNetwork()){
			return null;
		}
		JSONCommand command = new JSONCommand(url, accessToken, isGZip, isEncrypt, json, terminalAgent);
		command.execute();
		return command;
	}
	/**
	 * 发送JSON请求命令, 无需返回(默认发送格式,默认不压缩)
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @return
	 */
	public static JSONCommand post(String url, String accessToken, boolean isEncrypt, JSONObject json, String terminalAgent){
		return post(url, accessToken, false, isEncrypt, json, terminalAgent);
	}
	/**
	 * 发送JSON请求命令, 无需返回(默认发送格式， 默认不压缩、不加密)
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isGZip 是否压缩
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @return
	 */
	public static JSONCommand post(String url, String accessToken, JSONObject json, String terminalAgent){
		return post(url, accessToken, false, json, terminalAgent);
	}
	/**
	 * 采用Get方式，发送JSON请求命令,并等待返回处理
	 * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isGZip 是否压缩
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @param jsonCallback 命令回调
	 * @param extraObjects 附加Objects
	 * @return
	 */
	public static JSONCommand getForResult(int tag, String url, String accessToken, boolean isGZip, boolean isEncrypt, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
		if(!hasNetwork()){
			return null;
		}
		JSONCommand command = new JSONCommand(tag, url, accessToken, isGZip, isEncrypt, json, terminalAgent, jsonCallback, extraObjects);
		command.setSendType(SEND_TYPE_GET);
		command.execute();
		return command;
	}
	/**
	 * 采用Get方式，发送JSON请求命令,并等待返回处理（默认不压缩）
	 * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
	 * @param url 请求地址
	 * @param accessToken token
	 * @param isEncrypt 是否加密
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @param jsonCallback 命令回调
	 * @param extraObjects 附加Objects
	 * @return
	 */
	public static JSONCommand getForResult(int tag, String url, String accessToken, boolean isEncrypt, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
		return getForResult(tag, url, accessToken, false, isEncrypt, json, terminalAgent, jsonCallback, extraObjects);
	}
	/**
	 * 采用Get方式，发送JSON请求命令,并等待返回处理（默认不压缩、不加密）
	 * @param tag 命令唯一标识,用于局部范围内识别命令的返回处理
	 * @param url 请求地址
	 * @param accessToken token
	 * @param json 请求参数
	 * @param terminalAgent 请求头部参数
	 * @param jsonCallback 命令回调
	 * @param extraObjects 附加Objects
	 * @return
	 */
	public static JSONCommand getForResult(int tag, String url, String accessToken, JSONObject json
			, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object...extraObjects){
		return getForResult(tag, url, accessToken, false, json, terminalAgent, jsonCallback, extraObjects);
	}
	/**
	 * 发送文件
	 * @param tag
	 * @param url
	 * @param file
	 * @param newFileName 上传到服务器上使用的文件名,不修改可为null
	 * @param formName 提交文件时，指定Content-Disposition中的name字段的值
	 * @param json
	 * @param paramsList
	 * @param terminalAgent
	 * @param jsonCallback
	 * @param extraObjects
	 * @return
	 */
	public static JSONCommand upload(int tag, String url, File file, String newFileName
			, String formName, JSONObject json, String terminalAgent, List<NameValuePair> paramsList
			, CommandCallback<JSONObject> jsonCallback, Object... extraObjects){
		if(!hasNetwork()){
			return null;
		}
		JSONCommand command = new JSONCommand(tag, url, file, newFileName, formName, json, terminalAgent, paramsList, jsonCallback, extraObjects);
		command.setSendType(SEND_TYPE_UPLOAD);
		command.execute();
		return command;
	}
	/**
	 * 发送文件
	 * @param tag
	 * @param url
	 * @param filePath
	 * @param newFileName 上传到服务器上使用的文件名,不修改可为null
	 * @param formName 提交文件时，指定Content-Disposition中的name字段的值
	 * @param json
	 * @param paramsList
	 * @param terminalAgent
	 * @param jsonCallback
	 * @param extraObjects
	 * @return
	 */
	public static JSONCommand upload(int tag, String url, String filePath, String newFileName
			, String formName, JSONObject json, String terminalAgent, List<NameValuePair> paramsList
			, CommandCallback<JSONObject> jsonCallback, Object... extraObjects){
		File file = Helper.isNotEmpty(filePath) ?  new File(filePath) : null;
		return upload(tag, url, file, newFileName, formName, json, terminalAgent, paramsList, jsonCallback, extraObjects);
	}
	/**
	 * 发送文件
	 * @param tag
	 * @param url
	 * @param filePath
	 * @param newFileName 上传到服务器上使用的文件名,不修改可为null
	 * @param json
	 * @param terminalAgent
	 * @param paramsList
	 * @param terminalAgent
	 * @param jsonCallback
	 * @param extraObjects
	 * @return
	 */
	public static JSONCommand upload(int tag, String url, String filePath, String newFileName
			, JSONObject json, String terminalAgent, List<NameValuePair> paramsList
			, CommandCallback<JSONObject> jsonCallback, Object... extraObjects){
		return upload(tag, url, filePath, newFileName, "", json, terminalAgent, paramsList, jsonCallback, extraObjects);
	}
	/**
	 * 发送文件
	 * @param tag
	 * @param url
	 * @param filePath
	 * @param newFileName 上传到服务器上使用的文件名,不修改可为null
	 * @param json
	 * @param terminalAgent
	 * @param jsonCallback
	 * @param extraObjects
	 * @return
	 */
	public static JSONCommand upload(int tag, String url, String filePath, String newFileName
			, JSONObject json, String terminalAgent, CommandCallback<JSONObject> jsonCallback, Object... extraObjects){
		return upload(tag, url, filePath, newFileName, json, terminalAgent, null, jsonCallback, extraObjects);
	}
	//#endregion public 方法
}
