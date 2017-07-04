package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.util.ResourceHelper;

import java.io.Serializable;

/**
 * Result Entity：交换返回列表实体
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2014-4-14
 *
 */
public class SwapCardResultEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	@SerializedName("companyHome")
	private String companyHome;
	@SerializedName("birthday")
	private String birthday; 
	@SerializedName("constellation")
	private String constellation;
	@SerializedName("sex")
	private int sex;
	@SerializedName("accountId")
	private long accountId;
	@SerializedName("companyAbout")
	private String companyAbout;
	@SerializedName("enterpriseApproval")
	private int enterpriseApproval;
	@SerializedName("surname")
	private String surname;
	@SerializedName("cardImgB")
	private String cardImgB;
	@SerializedName("business")
	private int business;
	
	/** (弃用，改用imArrayJson字段) **/
	@SerializedName("microblog")
	private String microblog;
	@SerializedName("cardImgA")
	private String cardImgA;
	@SerializedName("city")
	private String city;
	@SerializedName("cardName")
	private String cardName;
	@SerializedName("enName")
	private String enName;
	@SerializedName("username")
	private String username;
	@SerializedName("nickName")
	private String nickName;
	@SerializedName("description")
	private String description;
	@SerializedName("province")
	private String province;
	@SerializedName("grade")
	private int grade;
	@SerializedName("cardProvince")
	private String cardProvince;
	@SerializedName("firstName")
	private String firstName;
	
	/** (弃用，改用imArrayJson字段)  **/
	@SerializedName("qq")
	private String qq;
	@SerializedName("cardId")
	private long cardId;
	@SerializedName("fax")
	private String fax;
	@SerializedName("regDate")
	private String regDate;
	@SerializedName("job")
	private String job;
	@SerializedName("lineTelphone")
	private String lineTelphone;
	@SerializedName("postcode")
	private String postcode;
	@SerializedName("companyName")
	private String companyName;
	@SerializedName("workAddress")
	private String workAddress;
	@SerializedName("mobileTelphone")
	private String mobileTelphone;
	@SerializedName("vcardNo")
	private String vcardNo;
	@SerializedName("country")
	private String country;
	@SerializedName("bloodType")
	private int bloodType;
	@SerializedName("headImg")
	private String headImg;
	@SerializedName("email")
	private String email;
	@SerializedName("selfSign")
	private String selfSign;
	@SerializedName("nativeplace")
	private String nativeplace;
	@SerializedName("eduBackground")
	private String eduBackground;
	@SerializedName("cardCountry")
	private String cardCountry;		
	@SerializedName("auth")
	private int auth;	
	@SerializedName("bindWay")
	private int bindway;
	/**
	 * 正面名片类别（0预留、1为扫描、2为模板、3为本地上传）
	 */
	@SerializedName("cardAType")
	private int cardAType;
	/**
	 * 背面名片类别（0预留、1为扫描、2为模板、3为本地上传）
	 */
	@SerializedName("cardBType")
	private int cardBType;
	/**
	 * 正面名片类型（0为不确定类型、1为90*54、2为90*45、3为90*94 ）
	 */
	@SerializedName("cardAForm")
	private int cardAForm;
	/**
	 * 背面名片类型（0为不确定类型、1为90*54、2为90*45、3为90*94 ）
	 */
	@SerializedName("cardBForm")
	private int cardBForm;
	/**
	 * 正面名片方向（0为横向、1为竖向）
	 */
	@SerializedName("cardAOrientation")
	private int cardAOrientation;
	/**
	 * 背面名片方向（0为横向、1为竖向）
	 */
	@SerializedName("cardBOrientation")
	private int cardBOrientation;
	
	/** 社交信息--腾讯微博链接地址 **/
	@SerializedName("tencentWeiboUrl")
	private String tencentBlogUrl;
	
	/** 社交信息--腾讯空间链接地址  **/
	@SerializedName("tencentQQUrl")
	private String tencentQQUrl;
	
	/** 社交信息--新浪微博链接地址   **/
	@SerializedName("sinaWeiboUrl")
	private String sinaBlogUrl;
	
	/** 即时通讯json **/
	@SerializedName("imInfo")
	private String instantMessengerEntitys;
	
	/** 其他中文公司名称列表  **/
	@SerializedName("companyNameList")
	protected String otherCompanyNameList;
	
	/** 英文单位  **/
	@SerializedName("enCompanyName")
	private String enCompanyName;

	public String getDisplayName(){
		String displayName = "";
		if(ResourceHelper.isNotEmpty(surname) ){
			displayName = surname;
		}
		if(ResourceHelper.isNotEmpty(firstName)){
			displayName += firstName;
		}
		return displayName;
	}

	public String getCompanyHome() {
		return companyHome;
	}
	public void setCompanyHome(String companyHome) {
		this.companyHome = companyHome;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getConstellation() {
		return constellation;
	}
	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getCompanyAbout() {
		return companyAbout;
	}
	public void setCompanyAbout(String companyAbout) {
		this.companyAbout = companyAbout;
	}
	public int getEnterpriseApproval() {
		return enterpriseApproval;
	}
	public void setEnterpriseApproval(int enterpriseApproval) {
		this.enterpriseApproval = enterpriseApproval;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getCardImgB() {
		return cardImgB;
	}
	public void setCardImgB(String cardImgB) {
		this.cardImgB = cardImgB;
	}
	public int getBusiness() {
		return business;
	}
	public void setBusiness(int business) {
		this.business = business;
	}
	public String getMicroblog() {
		return microblog;
	}
	public void setMicroblog(String microblog) {
		this.microblog = microblog;
	}
	public String getCardImgA() {
		return cardImgA;
	}
	public void setCardImgA(String cardImgA) {
		this.cardImgA = cardImgA;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getCardProvince() {
		return cardProvince;
	}
	public void setCardProvince(String cardProvince) {
		this.cardProvince = cardProvince;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getLineTelphone() {
		return lineTelphone;
	}
	public void setLineTelphone(String lineTelphone) {
		this.lineTelphone = lineTelphone;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	public String getMobileTelphone() {
		return mobileTelphone;
	}
	public void setMobileTelphone(String mobileTelphone) {
		this.mobileTelphone = mobileTelphone;
	}
	public String getVcardNo() {
		return vcardNo;
	}
	public void setVcardNo(String vcardNo) {
		this.vcardNo = vcardNo;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getBloodType() {
		return bloodType;
	}
	public void setBloodType(int bloodType) {
		this.bloodType = bloodType;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSelfSign() {
		return selfSign;
	}
	public void setSelfSign(String selfSign) {
		this.selfSign = selfSign;
	}
	public String getNativeplace() {
		return nativeplace;
	}
	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}
	public String getEduBackground() {
		return eduBackground;
	}
	public void setEduBackground(String eduBackground) {
		this.eduBackground = eduBackground;
	}
	public String getCardCountry() {
		return cardCountry;
	}
	public void setCardCountry(String cardCountry) {
		this.cardCountry = cardCountry;
	}
	public int getAuth() {
		return auth;
	}
	public void setAuth(int auth) {
		this.auth = auth;
	}
	public int getBindway() {
		return bindway;
	}
	public void setBindway(int bindway) {
		this.bindway = bindway;
	}
	public int getCardAType() {
		return cardAType;
	}
	public void setCardAType(int cardAType) {
		this.cardAType = cardAType;
	}
	public int getCardBType() {
		return cardBType;
	}
	public void setCardBType(int cardBType) {
		this.cardBType = cardBType;
	}
	public int getCardAForm() {
		return cardAForm;
	}
	public void setCardAForm(int cardAForm) {
		this.cardAForm = cardAForm;
	}
	public int getCardBForm() {
		return cardBForm;
	}
	public void setCardBForm(int cardBForm) {
		this.cardBForm = cardBForm;
	}
	public int getCardAOrientation() {
		return cardAOrientation;
	}
	public void setCardAOrientation(int cardAOrientation) {
		this.cardAOrientation = cardAOrientation;
	}
	public int getCardBOrientation() {
		return cardBOrientation;
	}
	public void setCardBOrientation(int cardBOrientation) {
		this.cardBOrientation = cardBOrientation;
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
	public String getInstantMessengerEntitys() {
		return instantMessengerEntitys;
	}
	public void setInstantMessengerEntitys(String instantMessengerEntitys) {
		this.instantMessengerEntitys = instantMessengerEntitys;
	}
	public String getOtherCompanyNameList() {
		return otherCompanyNameList;
	}
	public void setOtherCompanyNameList(String otherCompanyNameList) {
		this.otherCompanyNameList = otherCompanyNameList;
	}
	public String getEnCompanyName() {
		return enCompanyName;
	}
	public void setEnCompanyName(String enCompanyName) {
		this.enCompanyName = enCompanyName;
	}
}
