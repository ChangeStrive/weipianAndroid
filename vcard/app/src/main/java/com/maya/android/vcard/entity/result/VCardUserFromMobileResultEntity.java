package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * 匹配通讯录手机号的 微片注册用户实体类
 * @author zheng_cz
 * @since 2014年4月21日 上午10:47:15
 */
public class VCardUserFromMobileResultEntity {
	/** 姓名 **/
	@SerializedName("displayName")
	private String displayName;
	/** 头像 */
	@SerializedName("headImg")
	private String headImg;
	/** 电话号码 */
	@SerializedName("mobileTel")
	private String mobile;
	/** 帐户ID  **/
	@SerializedName("accountId")
	private long accountId;
	/** VIP等级  **/
	@SerializedName("auth")
	private int auth;
	/** 微片号 **/
	@SerializedName("vcardNumber")
	private String VcardNo;
	/** 绑定方式 **/
	@SerializedName("bindWay")
	private int bindWay;
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	public String getVcardNo() {
		return VcardNo;
	}
	public void setVcardNo(String vcardNo) {
		VcardNo = vcardNo;
	}
	public int getBindWay() {
		return bindWay;
	}
	public void setBindWay(int bindWay) {
		this.bindWay = bindWay;
	}
}
