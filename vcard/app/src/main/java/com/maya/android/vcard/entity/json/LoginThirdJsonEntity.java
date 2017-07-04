package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * Json：第三方登录请求
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-11
 *
 */
public class LoginThirdJsonEntity {
	/**
	 * 用户名，必须为邮箱
	 */
	@SerializedName("username")
	private String username;
	/**
	 * 第三方来源id 1-腾讯 2-新浪 3-QQ 4-微信
	 */
	@SerializedName("partnerId")
	private int partnerId;
	/**
	 * 参数签名，防止注册被刷。加密方式待定
	 */
	@SerializedName("signName")
	private String signName;
	/**
	 * 头像图片url
	 */
	@SerializedName("headImg")
	private String headImg;
	/**
	 * 昵称
	 */
	@SerializedName("nickName")
	private String nickName;
	/**
	 * 省份编码
	 */
	@SerializedName("province")
	private String province;
	/**
	 * 城市编码，使用国家行政区域编码
	 */
	@SerializedName("city")
	private String city;
	/**
	 * 个信消息推送客户端ID
	 */
	@SerializedName("clientId")
	private String clientId;
	/**
	 * 第三方登录
	 * @param username
	 * @param signName
	 * @param clientId
	 * @param partnerId
	 * @param headImg
	 * @param nickName
	 * @param province
	 * @param city
	 */
	public LoginThirdJsonEntity(String username, String signName, String clientId, int partnerId, 
			String headImg, String nickName, String province, String city){
		this.username = username;
		this.signName = signName;
		this.clientId = clientId;
		this.partnerId = partnerId;
		this.headImg = headImg;
		this.nickName = nickName;
		this.province = province;
		this.city = city;
	}
	
	public LoginThirdJsonEntity(){
		
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	
}
