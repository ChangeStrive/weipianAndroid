package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * Json Entity:注册参数
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-26
 *
 */
public class RegisterJsonEntity {
	/** 注册方式：手机 **/
	public static final int REGISTER_METHOD_MOBILE = 1;
	/** 注册方式：邮箱 **/
	public static final int REGISTER_METHOD_EMAIL = 2;
	/** 注册方式：云端  **/
	public static final int REGISTER_METHOD_YUN = 3;
	
	/** 用户名，必须为邮箱/手机 **/
	@SerializedName("username")
	private String username;
	/** 密码 **/
	@SerializedName("password")
	private String password;
	/** 注册方式 1 手机 2邮箱 **/
	@SerializedName("registerMethod")
	private int registerMethod;
	/** 手机注册需要验证验证码 **/
	@SerializedName("smsVerifyCode")
	private String smsVerifyCode;
	/** 参数签名，防止注册被刷。加密方式待定。 **/
	@SerializedName("signName")
	private String signName = "";
	
	public RegisterJsonEntity(String username, String password, int registerMethod, String smsVerifyCode){
		this.username = username;
		this.password = password;
		this.registerMethod = registerMethod;
		this.smsVerifyCode = smsVerifyCode;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getRegisterMethod() {
		return registerMethod;
	}
	public void setRegisterMethod(int registerMethod) {
		this.registerMethod = registerMethod;
	}
	public String getSmsVerifyCode() {
		return smsVerifyCode;
	}
	public void setSmsVerifyCode(String smsVerifyCode) {
		this.smsVerifyCode = smsVerifyCode;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}

}
