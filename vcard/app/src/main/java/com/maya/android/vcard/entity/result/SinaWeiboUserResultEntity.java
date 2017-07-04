package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

/**
 * ResultEntity:新浪微博个人信息
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-3-12
 *
 */
public class SinaWeiboUserResultEntity {
	
	/**新浪微博id**/
	@SerializedName("id")
	private long id;
	/**昵称**/
	@SerializedName("name")
	private String name;
	/**昵称**/
	@SerializedName("screen_name")
	private String screenName;
	/**省份**/
	@SerializedName("province")
	private String province;
	/**城市**/
	@SerializedName("city")
	private String city;
	/**位置**/
	@SerializedName("location")
	private String location;
	/**头像**/
	@SerializedName("profile_image_url")
	private String profileImageUrl;
	/**用户**/
	@SerializedName("userId")
	private String userId;
	/**签名**/
	@SerializedName("signName")
	private String signName;
	/**别名**/
	@SerializedName("platName")
	private String platName;
	@SerializedName("gender")
	private String sex;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getPlatName() {
		return platName;
	}
	public void setPlatName(String platName) {
		this.platName = platName;
	}
	public String getSex() {
		if(sex.equals("f")){
			return "女";
		}else if(sex.equals("m")){
			return "男";
		}
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
}
