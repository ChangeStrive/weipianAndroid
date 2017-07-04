package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.json.ContactLocalVCardJsonEntity;
import com.maya.android.vcard.entity.result.UserInfoResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 名片夹 联系人 实体对象
 * 
 * @author zheng_cz
 * @since 2014年3月24日 下午5:47:03
 */
public class ContactEntity extends ContactListItemEntity {

	/**
     * 用户名
     */
    @SerializedName("username")
    private String accountName;
    /**
     * 英文名
     */
    @SerializedName("enName")
    private String enName;
    /**
     * 个性签名
     */
    @SerializedName("selfSign")
    private String selfSign;
   
    /**
     * 学历
     */
    @SerializedName("eduBackground")
    private String degree;
    /**
     * 性别,0未知，1男，2女
     */
    @SerializedName("sex")
    private int sex = 0;
    /**
     * 出生日期
     */
    @SerializedName("birthday")
    private String birthday;
    /**
     * 注册时间
     */
    @SerializedName("regTime")
    private String registerTime;
    /**
     * 国家
     */
    @SerializedName("country")
    private String country;
    /**
     * 省
     */
    @SerializedName("province")
    private String province;
    /**
     * 市
     */
    @SerializedName("city")
    private String city;
    /**
     * 籍贯
     */
    @SerializedName("nativeplace")
    private String nativeplace;
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
    /**
     * 社会关系（0-陌生人，1-关注，2-被关注，3-互相关注）
     */
    @SerializedName("socialRelation")
    private int socialRelation;
    public int getEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(int enableStatus) {
		this.enableStatus = enableStatus;
	}

	public int getSocialRelation() {
		return socialRelation;
	}

	public void setSocialRelation(int socialRelation) {
		this.socialRelation = socialRelation;
	}
	/**
     * 拉黑状态（0-黑名单，1-白名单）
     */
    @SerializedName("enableStatus")
    private int enableStatus;

    /**
     * **********名片信息*****************
     */

    /**
     * 名片名称
     */
    @SerializedName("cardName")
    private String cardName;
    /**
     * 名片主体正面图片
     */
    @SerializedName("cardImgA")
    private String cardImgA;
    /**
     * 名片主体背面图片
     */
    @SerializedName("cardImgB")
    private String cardImgB;
    /**
     * 认证标准
     */
    @SerializedName("enterpriseApproval")
    private int companyApproval;
    /**
     * 国籍
     */
    @SerializedName("cardCountry")
    private String companyCountry;
    /**
     * 省
     */
    @SerializedName("cardProvince")
    private String companyProvince;
    /**
     * 公司地址(工作地址)
     */
    @SerializedName("workAddress")
    private String address;
    /**
     * 邮编
     */
    @SerializedName("postcode")
    private String postcode;
    /**
     * 手机
     */
    @SerializedName("mobileTelphone")
    private String mobile;
    /**
     * 固定电话
     */
    @SerializedName("lineTelphone")
    private String telephone;
    /**
     * 传真
     */
    @SerializedName("fax")
    private String fax;

    /**
     * 邮箱
     */
    @SerializedName("email")
    private String email;
    /**
     * 公司首页
     */
    @SerializedName("companyHome")
    private String companyHome;
    /**
     * 公司简介
     */
    @SerializedName("companyAbout")
    private String companyAbout;
   
    @SerializedName("cardRemark")
    private String cardRemark;

    /**
     * 收藏夹标识
     */
    @SerializedName("starred")
    private int starred = 0;

    /**
     * 就读学校
     */
    @SerializedName("school")
    private String school;
    /**
     * 联系人备注
     */
    @SerializedName("remark")
    private String remark;

    /**
     * 个人简介
     */
    @SerializedName("intro")
    private String introduction;

    /**
     * 最后联系时间
     */
    @SerializedName("lastContactedTime")
    private String lastContactedTime;
    /**
     * 联系次数
     */
    @SerializedName("timesContacted")
    private int timesContacted;
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

    /** 即时通讯json **/
    @SerializedName("imInfo")
    private String instantMessenger;

    /** 社交信息--腾讯微博链接地址 **/
    @SerializedName("tencentWeiboUrl")
    private String tencentBlogUrl;

    /** 社交信息--腾讯空间链接地址 **/
    @SerializedName("tencentQQUrl")
    private String tencentQQUrl;

    /** 社交信息--新浪微博链接地址 **/
    @SerializedName("sinaWeiboUrl")
    private String sinaBlogUrl;

    /** 云端查找 结果信息显示方式 */
    @SerializedName("cloudFindShowType")
    private int cloudFindShowType;

    /** 其他中文公司名称列表 **/
    @SerializedName("companyNameList")
    private String otherCompanyNameList;

    /** 英文单位 **/
    @SerializedName("enCompanyName")
    private String companyEnName;
    /** 企业logo */
    @SerializedName("companyLogo")
    private String companyLogo;
    /**
     * 手机查找时用户是否发送过请求
     */
    @SerializedName("swapHis")
    private int swapHis;
    private ArrayList<InstantMessengerEntity> instantMessengerList;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getSelfSign() {
        return selfSign;
    }

    public void setSelfSign(String selfSign) {
        this.selfSign = selfSign;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
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

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNativeplace() {
        return nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardImgA() {
        return cardImgA;
    }

    public void setCardImgA(String cardImgA) {
        this.cardImgA = cardImgA;
    }

    public String getCardImgB() {
        return cardImgB;
    }

    public void setCardImgB(String cardImgB) {
        this.cardImgB = cardImgB;
    }

    public int getCompanyApproval() {
        return companyApproval;
    }

    public void setCompanyApproval(int approval) {
        this.companyApproval = approval;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String cardCountry) {
        this.companyCountry = cardCountry;
    }

    public String getCompanyProvince() {
        return companyProvince;
    }

    public void setCompanyProvince(String cardProvince) {
        this.companyProvince = cardProvince;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyHome() {
        return companyHome;
    }

    public void setCompanyHome(String companyHome) {
        this.companyHome = companyHome;
    }

    public String getCompanyAbout() {
        return companyAbout;
    }

    public void setCompanyAbout(String companyAbout) {
        this.companyAbout = companyAbout;
    }

    public String getCardRemark() {
        return cardRemark;
    }

    public void setCardRemark(String cardRemark) {
        this.cardRemark = cardRemark;
    }

    public int getStarred() {
        return starred;
    }

    public void setStarred(int starred) {
        this.starred = starred;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLastContactedTime() {
        return lastContactedTime;
    }

    public void setLastContactedTime(String lastContactedTime) {
        this.lastContactedTime = lastContactedTime;
    }

    public int getTimesContacted() {
        return timesContacted;
    }

    public void setTimesContacted(int timesContacted) {
        this.timesContacted = timesContacted;
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

    public String getInstantMessenger() {
        return instantMessenger;
    }

    public void setInstantMessenger(String instantMessenger) {
        this.instantMessenger = instantMessenger;
	if(ResourceHelper.isNotEmpty(instantMessenger)){
	    Type typeOfIm = new TypeToken<ArrayList<InstantMessengerEntity>>(){}.getType();
	    this.instantMessengerList = JsonHelper.fromJson(instantMessenger, typeOfIm);
	}
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

    public int getCloudFindShowType() {
        return cloudFindShowType;
    }

    public void setCloudFindShowType(int cloudFindShowType) {
        this.cloudFindShowType = cloudFindShowType;
    }

    public String getOtherCompanyNameList() {
        return otherCompanyNameList;
    }

    public void setOtherCompanyNameList(String otherCompanyNameList) {
        this.otherCompanyNameList = otherCompanyNameList;
    }

    public String getCompanyEnName() {
        return companyEnName;
    }

    public void setCompanyEnName(String companyEnName) {
        this.companyEnName = companyEnName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public ArrayList<InstantMessengerEntity> getInstantMessengerList() {
        return instantMessengerList;
    }

    public void setInstantMessengerList(ArrayList<InstantMessengerEntity> instantMessengerList) {
        this.instantMessengerList = instantMessengerList;

		if(ResourceHelper.isNotNull(instantMessengerList)){
			this.instantMessenger = JsonHelper.toJson(instantMessengerList);
		}
    }

	public int getSwapHis() {
		return swapHis;
	}

	public void setSwapHis(int swapHis) {
		this.swapHis = swapHis;
	}
	/**
	 * 获取完整的 姓名 
	 * @return String
	 */
	public String getFullName(){
		String disName = "";
		if(Helper.isNotEmpty(this.surname)){
			disName += this.surname;
		}
		if(Helper.isNotEmpty(this.firstName)){
			disName += this.firstName;
		}
		if(Helper.isEmpty(disName)){
			disName = this.enName;
		}
		setDisplayName(disName);
		return disName;
	}
	/**
	 * 返回用户对象 
	 * @return
	 */
	public UserInfoResultEntity getAccountEntity(){
        UserInfoResultEntity user = new UserInfoResultEntity();
		if(Helper.isNotEmpty(this.displayName)){
			user.setSurname(this.displayName);
		}else{
			user.setSurname(this.surname);
			user.setFirstName(this.firstName);
		}
		user.setGrade(this.accountGrade);
		user.setBirthday(this.birthday);
		user.setCity(this.city);
		user.setCountry(this.country);
		user.setCreateTime(this.registerTime);
		user.setEduBackground(this.degree);
		user.setEnName(this.enName);
		user.setHeadImg(this.headImg);
		user.setNativeplace(this.nativeplace);
		user.setProvince(this.province);
		user.setSelfSign(this.selfSign);
		user.setSex(this.sex);
		user.setVcardNo(this.vcardNo);
		user.setUsername(this.accountName);
		user.setPartnerId(this.partnerId);
		user.setId(this.accountId);
		user.setSchool(this.school);
		user.setIntro(this.introduction);
		user.setTencentBlogUrl(this.tencentBlogUrl);
		user.setSinaBlogUrl(this.sinaBlogUrl);
		user.setTencentQQUrl(this.tencentQQUrl);
		user.setAuth(this.auth);
		user.setBindWay(this.bindway);
		return user;
	}
	/**
	 * 返回名片对象
	 * @return
	 */
	public CardEntity getCardEntity(){
		CardEntity card = new CardEntity();
		card.setId(this.cardId);
		card.setBusiness(this.business);
		card.setCardImgA(this.cardImgA);
		card.setCardImgB(this.cardImgB);
		card.setCardName(this.cardName);
		card.setCompanyAbout(this.companyAbout);
		card.setCompanyHome(this.companyHome);
		card.setCompanyName(this.companyName);
		card.setOtherCompanyNameList(this.otherCompanyNameList);
		card.setEnCompanyName(this.companyEnName);
		card.setCountry(this.companyCountry);
		card.setProvince(this.companyProvince);
		card.setEmail(this.email);
		card.setFax(this.fax);
		card.setJob(this.job);
		card.setLineTelphone(this.telephone);
		card.setMobileTelphone(this.mobile);
		card.setPostcode(this.postcode);
		card.setRemark(this.cardRemark);
		card.setWorkAddress(this.address);
		card.setCardAType(this.cardAType);
		card.setCardAForm(this.cardAForm);
		card.setCardAOrientation(this.cardAOrientation);
		card.setCardBType(this.cardBType);
		card.setCardBForm(this.cardBForm);
		card.setCardBOrientation(this.cardBOrientation);
	    card.setImJson(this.instantMessenger);
	    card.setCompanyLogo(this.companyLogo);
		return card;
	}
	/**
	 * 获取本地新增名片的保存对象
	 * @return
	 */
	public ContactLocalVCardJsonEntity getContactLocalVCardJsonEntity(){
        ContactLocalVCardJsonEntity contactCard = new ContactLocalVCardJsonEntity();
		contactCard.setSurname(this.surname);
		contactCard.setFirstName(this.firstName);
		contactCard.setEnName(this.enName);
		contactCard.setBusiness(this.business);
		contactCard.setCardImgA(this.cardImgA);
		contactCard.setCardImgB(this.cardImgB);
		contactCard.setCompanyAbout(this.companyAbout);
		contactCard.setCompanyHome(this.companyHome);
		contactCard.setCompanyName(this.companyName);
		contactCard.setOtherCompanyNameList(this.otherCompanyNameList);
		contactCard.setCompanyEnName(this.companyEnName);
		contactCard.setCountry(this.companyCountry);
		contactCard.setProvince(this.companyProvince);
		contactCard.setEmail(this.email);
		contactCard.setFax(this.fax);
		contactCard.setJob(this.job);
		contactCard.setLineTelphone(this.telephone);
		contactCard.setMobileTelphone(this.mobile);
		contactCard.setPostcode(this.postcode);
		contactCard.setRemark(this.cardRemark);
		contactCard.setWorkAddress(this.address);
		contactCard.setCardAType(this.cardAType);
		contactCard.setCardAForm(this.cardAForm);
		contactCard.setCardAOrientation(this.cardAOrientation);
		contactCard.setCardBType(this.cardBType);
		contactCard.setCardBForm(this.cardBForm);
		contactCard.setCardBOrientation(this.cardBOrientation);
	    contactCard.setInstantMessenger(this.instantMessenger);
	    contactCard.setGroupId(this.groupId);
	    contactCard.setGroupName(this.groupName);
	    contactCard.setCompanyLogo(this.companyLogo);
		return contactCard;
	}
}
