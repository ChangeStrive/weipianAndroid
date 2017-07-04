package com.maya.android.vcard.entity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.InstantMessengerEntity;

/**
 * 名片联系人备份实体类
 * 
 * @author zheng_cz
 * @since 2014年3月25日 下午5:53:29
 */
public class ContactVCardBackupJsonEntity {
    /**
     * 显示名字
     */
    @SerializedName("displayName")
    private String displayName;
    /**
     * 本机联系人键值id
     */
    @SerializedName("syncId")
    private int keyId;

    /**
     * 分组id
     */
    @SerializedName("groupId")
    private int groupId = 0;
    /**
     * 分组名称
     */
    @SerializedName("groupName")
    private String groupName;
    /**
     * 微片号
     */
    @SerializedName("vcardNo")
    private String vcardCode;
    /**
     * 头像
     */
    @SerializedName("headImg")
    private String headImg;

    /**
     * 联系人ID
     */
    @SerializedName("contactPersonId")
    private long contactId;
    /**
     * 名片ID
     */
    @SerializedName("cardId")
    private Long cardId;

    /**
     * 英文名
     */
    @SerializedName("enName")
    private String enName;

    /**
     * 收藏夹标识
     */
    @SerializedName("starred")
    private int starred = 0;

    /**
     * 联系人备注
     */
    @SerializedName("remark")
    private String remark;
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
    private int business = 0;

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
    /**
     * 即时通讯列表
     */
    @SerializedName("imInfo")
    private String instantMessenger;

    /** 其他中文公司名称列表 **/
    @SerializedName("companyNameList")
    private String otherCompanyNameList;

    /** 英文单位 **/
    @SerializedName("enCompanyName")
    private String companyEnName;
    /** 企业logo */
    @SerializedName("companyLogo")
    private String companyLogo;

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public int getKeyId() {
	return keyId;
    }

    public void setKeyId(int keyId) {
	this.keyId = keyId;
    }

    public int getGroupId() {
	return groupId;
    }

    public void setGroupId(int groupId) {
	this.groupId = groupId;
    }

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public String getVcardCode() {
	return vcardCode;
    }

    public void setVcardCode(String vcardCode) {
	this.vcardCode = vcardCode;
    }

    public String getHeadImg() {
	return headImg;
    }

    public void setHeadImg(String headImg) {
	this.headImg = headImg;
    }

    public long getContactId() {
	return contactId;
    }

    public void setContactId(long contactId) {
	this.contactId = contactId;
    }

    public Long getCardId() {
	return cardId;
    }

    public void setCardId(Long cardId) {
	this.cardId = cardId;
    }

    public String getEnName() {
	return enName;
    }

    public void setEnName(String enName) {
	this.enName = enName;
    }

    public int getStarred() {
	return starred;
    }

    public void setStarred(int starred) {
	this.starred = starred;
    }

    public String getRemark() {
	return remark;
    }

    public void setRemark(String remark) {
	this.remark = remark;
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
	this.job = job;
    }

    public int getBusiness() {
	return business;
    }

    public void setBusiness(int business) {
	this.business = business;
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

    /**
     * 获取及时通讯列表
     * 
     * @return
     */
    public ArrayList<InstantMessengerEntity> getInstantMessengerList() {
	if (Helper.isNotEmpty(instantMessenger)) {
	    Type typeOfIm = new TypeToken<ArrayList<InstantMessengerEntity>>() {
	    }.getType();
	    ArrayList<InstantMessengerEntity> imArray = JsonHelper.fromJson(instantMessenger, typeOfIm);
	    return imArray;
	}
	return null;
    }

    public void setInstantMessengerList(ArrayList<InstantMessengerEntity> imArray) {
	this.instantMessenger = JsonHelper.toJson(imArray);
    }

    /**
     * 转化为 contactEntity 对象
     * 
     * @return String
     */
    public ContactEntity getContactEntity() {
	ContactEntity contact = new ContactEntity();
	contact.setCardId(cardId);
	contact.setContactId(contactId);
	contact.setVcardNo(vcardCode);
	contact.setCardAForm(cardAForm);
	contact.setCardAOrientation(cardAOrientation);
	contact.setCardAType(cardAType);
	contact.setCardBForm(cardBForm);
	contact.setCardBOrientation(cardBOrientation);
	contact.setCardBType(cardBType);
	contact.setCardImgA(cardImgA);
	contact.setCardImgB(cardImgB);
	contact.setCardRemark(cardRemark);
	contact.setRemark(remark);
	contact.setBusiness(business);
	contact.setId(keyId);
	contact.setGroupId(groupId);
	contact.setGroupName(groupName);
	contact.setEnName(enName);
	contact.setCardName(cardName);
	contact.setCompanyName(companyName);
	contact.setCompanyAbout(companyAbout);
	contact.setCompanyHome(companyHome);
	contact.setJob(job);
	contact.setMobile(mobile);
	contact.setTelephone(telephone);
	contact.setFax(fax);
	contact.setAddress(address);
	contact.setPostcode(postcode);
	contact.setInstantMessenger(instantMessenger);
	contact.setDisplayName(displayName);
	contact.setHeadImg(headImg);
	contact.setCompanyEnName(companyEnName);
	contact.setOtherCompanyNameList(otherCompanyNameList);
	contact.setStarred(starred);
	contact.setCompanyLogo(companyLogo);
	return contact;
    }
}
