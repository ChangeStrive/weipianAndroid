package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.CardEntity;

/**
 * 获取个人信息实体类
 * @author Administrator
 *
 */
public class UserInfoListResultEntity {
	/**
	 * vcardId
	 */
	@SerializedName("accountId")
	private long accountId;
	/**
	 * 登录账号，用户名
	 */
	@SerializedName("userName")
	private String userName;
	
	/**
	 * 学历
	 */
	@SerializedName("eduBackground")
	private String eduBackground;
	/**
	 * 头像
	 */
	@SerializedName("headImg")
	private String headImg;
	/**
	 * 性别
	 */
	@SerializedName("sex")
	private int sex;
	/**
	 * 生日
	 */
	@SerializedName("birthday")
	private String birthday;
	/**
	 * 注册日期
	 */
	@SerializedName("regTime")
	private String regTime;
	/**
	 * 国家
	 */
	@SerializedName("country")
	private String country;
	/**
	 * 省份
	 */
	@SerializedName("province")
	private String province;
	/**
	 * 毕业学校
	 */
	@SerializedName("school")
	private String school;
	/**
	 * 个人简介
	 */
	@SerializedName("intro")
	private String intro;
	/**
	 * 城市
	 */
	@SerializedName("city")
	private String city;
	/**
	 * 姓氏
	 */
	@SerializedName("surname")
	private String surname;
	/**
	 * 名字
	 */
	@SerializedName("firstName")
	private String firstName;
	/***
	 * 微片等级
	 */
	@SerializedName("grade")
	private int grade;
	/**
	 * 微片id
	 */
	@SerializedName("cardId")
	private long cardId;
	/**
	 * 公司名称
	 */
	@SerializedName("companyName")
	private String companyName;
	/**
	 * 职位
	 */
	@SerializedName("job")
	private String job;
	/**
	 * 行业
	 */
	@SerializedName("business")
	private int business;
	/**
	 * 手机
	 */
	@SerializedName("mobileTelphone")
	private String mobileTelphone;
	/**
	 * 社交关系（0-陌生人，1-关注，2-被关注，3-互相关注）
	 */
	@SerializedName("socialRelation")
	private int socialRelation;
	/**
	 * 拉黑状态（0-黑名单，1-白名单）
	 */
	@SerializedName("enableStatus")
	private int enableStatus;
	
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEduBackground() {
		return eduBackground;
	}
	public void setEduBackground(String eduBackground) {
		this.eduBackground = eduBackground;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
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
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSurnname() {
		return surname;
	}
	public void setSurnname(String surnname) {
		this.surname = surnname;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		job = job;
	}
	public int getBusiness() {
		return business;
	}
	public void setBusiness(int business) {
		this.business = business;
	}
	public String getMobileTelphone() {
		return mobileTelphone;
	}
	public void setMobileTelphone(String mobileTelphone) {
		this.mobileTelphone = mobileTelphone;
	}
	public int getSocialRelation() {
		return socialRelation;
	}
	public void setSocialRelation(int socialRelation) {
		this.socialRelation = socialRelation;
	}
	public int getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(int enableStatus) {
		this.enableStatus = enableStatus;
	}
	
	/**
	 * 获取全称
	 * @return String
	 */
	public String getDisplayName(){
		String disName = "";
		if(Helper.isNotEmpty(surname)){
			disName += surname;
		}
		if(Helper.isNotEmpty(firstName)){
			disName += firstName;
		}
		return disName;
	}
	
	/**
	 * 返回用户对象 
	 * @return
	 */
	public UserInfoResultEntity getAccountEntity(){
		UserInfoResultEntity user = new UserInfoResultEntity();
		user.setSurname(surname);
		user.setFirstName(firstName);
		user.setGrade(grade);
		user.setBirthday(birthday);
		user.setCity(city);
		user.setCountry(country);
		user.setCreateTime(regTime);
		user.setEduBackground(eduBackground);
		user.setHeadImg(headImg);
		user.setProvince(province);
		user.setSex(sex);
		user.setUsername(userName);
		user.setId(accountId);
		user.setSchool(school);
		user.setIntro(intro);
		user.setNativeplace(city);
		return user;
	}
	
	/**
	 * 返回名片对象
	 * @return
	 */
	public CardEntity getCardEntity(){
		CardEntity card = new CardEntity();
		card.setId(cardId);
		card.setBusiness(business);
		card.setCompanyName(companyName);
		card.setCountry(country);
		card.setProvince(province);
		card.setJob(job);
		card.setMobileTelphone(mobileTelphone);
		return card;
	}
}
