package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * 登录请求返回结果
 */
public class LoginResultEntity {
	/**
	 * 令牌
	 */
	@SerializedName("accessToken")
	private String accessToken;
	/**
	 * 离线消息
	 */
	@SerializedName("offLineMsg")
	private MessageResultEntity[] offLineMsg;
	
	@SerializedName("shouldUpdPwd")
	private boolean shouldUpdPwd;
	
	@SerializedName("vcardNumber")
	private String vcardNumber;
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public MessageResultEntity[] getOffLineMsg() {
		return offLineMsg;
	}

	public void setOffLineMsg(MessageResultEntity[] offLineMsg) {
		this.offLineMsg = offLineMsg;
	}

	public boolean isShouldUpdPwd() {
		return shouldUpdPwd;
	}

	public void setShouldUpdPwd(boolean shouldUpdPwd) {
		this.shouldUpdPwd = shouldUpdPwd;
	}

	public String getVcardNumber() {
		return vcardNumber;
	}

	public void setVcardNumber(String vcardNumber) {
		this.vcardNumber = vcardNumber;
	}
}
