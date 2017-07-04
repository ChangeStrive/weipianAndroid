package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * JsonEntity：登陆Entity
 */
public class LoginJsonEntity{
	
	/**
	 * 第三方登陆，则为第三方唯一标示,否则为登陆名
	 */
	@SerializedName("username")
	private String username;  
	/**
	 * 第三方登陆时，可以为空
	 */
	@SerializedName("password")
	private String password;  
	/**
	 * 参数签名，防刷
	 */
	@SerializedName("signName")
    private String signName;  
	/**
	 * 个推clientId;
	 */
	@SerializedName("clientId")
	private String clientId;
	
	
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
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	/**
	 * 构造函数：登录Entity
	 * @param username 第三方登陆，则为第三方唯一标示,否则为登陆名
	 * @param password 第三方登陆时，可以为空
	 * @param signName 参数签名，防刷
	 * @param clientId 个推clientId
	 */
	public LoginJsonEntity(String username, String password, String signName, String clientId){
		this.username = username;
		this.password = password;
		this.signName = signName;
		this.clientId = clientId;
	}
	
}
