package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.util.ResourceHelper;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;

/**
 * Entity:简单登录信息</b>
 * (主要用于：设置：账号设置：账号管理)
 * @author ZuoZiJi-Y.J
 *
 */
public class LoginSimpleInfoEntity {
	/** 用户名 */
	@SerializedName("username")
	private String username;
	/** 昵称 */
	@SerializedName("nickName")
	private String nickName;
	/** 姓氏 */
	@SerializedName("surname")
	protected String surname;
	/** 名称 */
	@SerializedName("firstName")
	protected String firstName;
	/** 微片号 */
	@SerializedName("vcardNo")
	private String vcardNo;
	/** 头像图片(头像墙，可表示五张头像图片的URL，以“#”分隔) */
	@SerializedName("headImg")
	protected String headImg;
	/** 登录密码 **/
	@SerializedName("password")
	private String password;
	/** 是否记住密码 **/
	@SerializedName("isRememberPassword")
	private boolean isRememberPassword;

	public LoginSimpleInfoEntity(UserInfoResultEntity userInfo){
		this.username = userInfo.getUsername();
		this.nickName = userInfo.getNickName();
		this.surname = userInfo.getSurname();
		this.firstName = userInfo.getFirstName();
		this.vcardNo = userInfo.getVcardNo();
		this.headImg = userInfo.getHeadImg();
		this.password = userInfo.getPassword();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getVcardNo() {
		return vcardNo;
	}
	public void setVcardNo(String vcardNo) {
		this.vcardNo = vcardNo;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberPassword() {
		return isRememberPassword;
	}

	public void setIsRememberPassword(boolean isRememberPassword) {
		this.isRememberPassword = isRememberPassword;
	}
	//	public LoginSimpleInfoEntity(String username, String nickName, String surname, String firstName, String vcardNo, String headImg, String password){
//		this.username = username;
//		this.nickName = nickName;
//		this.surname = surname;
//		this.firstName = firstName;
//		this.vcardNo = vcardNo;
//		this.headImg = headImg;
//		this.password = password;
//	}
	
	/**
	 * 获取全称
	 * @return
	 */
	public String getDisplayName(){
		String displayName = "";
		if(ResourceHelper.isNotEmpty(this.surname)){
			displayName += this.surname;
		}
		if(ResourceHelper.isNotEmpty(this.firstName)){
			displayName += this.firstName;
		}
		return displayName;
	}
}
