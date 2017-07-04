package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * 腾讯qq 授权登录 返回的结果 json 实体
 */
public class TencentQQLoginResultEntity {
	@SerializedName("openid")
	private String openId;
	@SerializedName("access_token")
	private String accessToken;
	@SerializedName("pay_token")
	private String payToken;
	@SerializedName("expires_in")
	private long expiresIn;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getPayToken() {
		return payToken;
	}
	public void setPayToken(String payToken) {
		this.payToken = payToken;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
}
