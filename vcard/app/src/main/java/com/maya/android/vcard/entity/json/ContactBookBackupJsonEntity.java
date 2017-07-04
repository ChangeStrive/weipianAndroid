package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.maya.android.jsonwork.utils.JsonHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.ContactEntity;
import com.maya.android.vcard.entity.InstantMessengerEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 通讯录联系人备份实体
 * 
 * @author zheng_cz
 * @since 2014年3月25日 上午11:43:23
 */
public class ContactBookBackupJsonEntity {

    /**
     * 联系人名称
     */
    @SerializedName("contactName")
    private String name;
    /**
     * 手机号码
     */
    @SerializedName("mobileTel")
    private String mobile;
    /**
     * 固话
     */
    @SerializedName("phone")
    private String phone;
    /**
     * 主页
     */
    @SerializedName("website")
    private String website;
    /**
     * 单位地址
     */
    @SerializedName("workAddress")
    private String workAddress;
    /**
     * 邮编
     */
    @SerializedName("postcode")
    private String postcode;
    /**
     * 传真
     */
    @SerializedName("fax")
    private String fax;
    /**
     * 单位
     */
    @SerializedName("company")
    private String company;
    /**
     * 职位
     */
    @SerializedName("job")
    private String job;
    /**
     * 邮箱
     */
    @SerializedName("email")
    private String email;

    /**
     * 本地id
     */
    @SerializedName("syncId")
    private long syncId;
    /**
     * 分组id
     */
    @SerializedName("syncGroupId")
    private int syncGroupId;
    /**
     * 分组名称
     */
    @SerializedName("syncGroupName")
    private String syncGroupName;
    /**
     * 即时通讯列表
     */
    @SerializedName("imInfo")
    private String instantMessenger;
    /** 收藏夹标示 */
    @SerializedName("starred")
    private int starred = 0;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getMobile() {
	return mobile;
    }

    public void setMobile(String mobile) {
	this.mobile = mobile;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

    public String getWorkAddress() {
	return workAddress;
    }

    public void setWorkAddress(String workAddress) {
	this.workAddress = workAddress;
    }

    public String getPostcode() {
	return postcode;
    }

    public void setPostcode(String postcode) {
	this.postcode = postcode;
    }

    public String getFax() {
	return fax;
    }

    public void setFax(String fax) {
	this.fax = fax;
    }

    public String getCompany() {
	return company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public String getJob() {
	return job;
    }

    public void setJob(String job) {
	this.job = job;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public long getSyncId() {
	return syncId;
    }

    public void setSyncId(long syncId) {
	this.syncId = syncId;
    }

    public int getSyncGroupId() {
	return syncGroupId;
    }

    public void setSyncGroupId(int syncGroupId) {
	this.syncGroupId = syncGroupId;
    }

    public String getSyncGroupName() {
	return syncGroupName;
    }

    public void setSyncGroupName(String syncGroupName) {
	this.syncGroupName = syncGroupName;
    }

    public String getInstantMessenger() {
	return instantMessenger;
    }

    public void setInstantMessenger(String instantMessenger) {
	this.instantMessenger = instantMessenger;
    }

    public int getStarred() {
	return starred;
    }

    public void setStarred(int starred) {
	this.starred = starred;
    }

    /**
     * 得到 及时通讯列表
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
    public void setInstantMessengerList(ArrayList<InstantMessengerEntity> imArray){
	this.instantMessenger = JsonHelper.toJson(imArray);
    }
    /**
     * 赋值给 contactEntity
     * 
     * @return
     */
    public ContactEntity getContactEntity() {
	ContactEntity contact = new ContactEntity();
	contact.setDisplayName(name);
	contact.setCompanyName(company);
	contact.setAddress(workAddress);
	contact.setMobile(mobile);
	contact.setEmail(email);
	contact.setFax(fax);
	contact.setTelephone(phone);
	contact.setGroupId(syncGroupId);
	contact.setInstantMessenger(instantMessenger);
	contact.setId((int) syncId);
	contact.setGroupName(syncGroupName);
	contact.setGroupId(syncGroupId);
	contact.setContactId(syncId);
	contact.setCompanyHome(website);
	contact.setPostcode(postcode);
	contact.setJob(job);
	contact.setStarred(starred);
	return contact;
    }
}
