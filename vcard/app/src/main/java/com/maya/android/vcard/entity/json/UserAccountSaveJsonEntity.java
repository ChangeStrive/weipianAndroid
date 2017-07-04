package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 保存帐户信息
 * @author hzq
 *
 * 2013-8-21 上午11:55:12
 */
public class UserAccountSaveJsonEntity {
	
	/**
	 * 姓氏
	 */
	@SerializedName("surname")
	protected String surname;
	/**
	 * 名字
	 */
	@SerializedName("firstName")
	protected String firstName;
	/**
	 * 个性签名
	 */
	@SerializedName("selfSign")
	protected String selfSign; 
	/**
	 * 血型  0 未知
	 */
	@SerializedName("bloodType")
	protected int bloodType = 0;
	/**
	 * 学历
	 */
	@SerializedName("eduBackground")
	protected String eduBackground;
	/**
	 * 性别,0女，1男，-1是未知(没有未知的)
	 */
	@SerializedName("sex")
	protected int sex = 1;  
	/**
	 * 出生日期
	 */
	@SerializedName("birthday")
	protected String birthday; 
	/**
	 * 头像图片(头像墙，可表示五张头像图片的URL，以“#”分隔)
	 */
	@SerializedName("headImg")
	protected String headImg; 
	/**
	 * 籍贯
	 */
	@SerializedName("nativeplace")
	protected String nativeplace; 
	
	/**
	 * 国家
	 */
	@SerializedName("country")
	protected String country; 
	/**
	 * 省
	 */
	@SerializedName("province")
	protected String province; 
	
	/**
	 * 毕业院校
	 */
	@SerializedName("school")
	protected String school;
	

	/**
	 * 个人简介
	 */
	@SerializedName("intro")
	protected String intro;
		
	/** 工作经历   **/
	@SerializedName("workHistory")
	private String workHistory;
	
	/** 教育信息 **/
	@SerializedName("eduInfo")
	private String eduInfo;
	
	/** 社交信息--腾讯微博号码 **/
	@SerializedName("tencentWeiboNo")
	private String tencentBlogNo;
	
	/** 社交信息--qq号码  **/
	@SerializedName("tencentQQNo")
	private String tencentQQNo;
	
	/** 社交信息--新浪微博号码   **/
	@SerializedName("sinaWeiboNo")
	private String sinaBlogNo;
	
	/** 社交信息--腾讯微博链接地址 **/
	@SerializedName("tencentWeiboUrl")
	private String tencentBlogUrl;
	
	/** 社交信息--腾讯空间链接地址  **/
	@SerializedName("tencentQQUrl")
	private String tencentQQUrl;
	
	/** 社交信息--新浪微博链接地址   **/
	@SerializedName("sinaWeiboUrl")
	private String sinaBlogUrl;
	
	
	
	public String getSurname() {
		return surname;
	}
	
	
	public UserAccountSaveJsonEntity() {
	}

	public UserAccountSaveJsonEntity(String surname, String firstName,
			String selfSign, int bloodType, String eduBackground, int sex,
			String birthday, String headImg, String nativeplace,
			String country, String province,String school,String intro) {
		super();
		this.surname = surname;
		this.firstName = firstName;
		this.selfSign = selfSign;
		this.bloodType = bloodType;
		this.eduBackground = eduBackground;
		this.sex = sex;
		this.birthday = birthday;
		this.headImg = headImg;
		this.nativeplace = nativeplace;
		this.country = country;
		this.province = province;
		this.school = school;
		this.intro = intro;
	}
	
	public UserAccountSaveJsonEntity(String surname, String firstName,
			String selfSign, int bloodType, String eduBackground, int sex,
			String birthday, String headImg, String nativeplace,
			String country, String province,String school,String intro,String eduInfo,String workHistory) {
		this(surname, firstName,
				 selfSign,  bloodType, eduBackground,  sex,
				 birthday,  headImg,  nativeplace,
				 country,  province, school, intro);
		this.eduInfo=eduInfo;
		this.workHistory=workHistory;
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
	public String getSelfSign() {
		return selfSign;
	}
	public void setSelfSign(String selfSign) {
		this.selfSign = selfSign;
	}
	public int getBloodType() {
		return bloodType;
	}
	public void setBloodType(int bloodType) {
		this.bloodType = bloodType;
	}
	public String getEduBackground() {
		return eduBackground;
	}
	public void setEduBackground(String eduBackground) {
		this.eduBackground = eduBackground;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getNativeplace() {
		return nativeplace;
	}
	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	public String getTencentBlogNo() {
		return tencentBlogNo;
	}

	public void setTencentBlogNo(String tencentBlogNo) {
		this.tencentBlogNo = tencentBlogNo;
	}

	public String getTencentQQNo() {
		return tencentQQNo;
	}

	public void setTencentQQNo(String tencentQQNo) {
		this.tencentQQNo = tencentQQNo;
	}

	public String getSinaBlogNo() {
		return sinaBlogNo;
	}

	public void setSinaBlogNo(String sinaBlogNo) {
		this.sinaBlogNo = sinaBlogNo;
	}
	
	public String getTencentBlogUrl() {
		return tencentBlogUrl;
	}

	public void setTencentBlogUrl(String tencentBlogUrl) {
		this.tencentBlogUrl = tencentBlogUrl;
	}

	public String getTencentQQUrl() {
		return tencentQQUrl;
	}

	public void setTencentQQUrl(String tencentQQUrl) {
		this.tencentQQUrl = tencentQQUrl;
	}

	public String getSinaBlogUrl() {
		return sinaBlogUrl;
	}

	public void setSinaBlogUrl(String sinaBlogUrl) {
		this.sinaBlogUrl = sinaBlogUrl;
	}
	public String getWorkHistory() {
		return workHistory;
	}


	public void setWorkHistory(String workHistory) {
		this.workHistory = workHistory;
	}


	public String getEduInfo() {
		return eduInfo;
	}


	public void setEduInfo(String eduInfo) {
		this.eduInfo = eduInfo;
	}
	
}
