package com.maya.android.vcard.entity;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

/**
 * VCard和本地通讯录实体类
 */
public class VCardAndBookEntity {
	@SerializedName("ITEM")
	public static final int ITEM = 0;
	@SerializedName("SECTION")
	public static final int SECTION = 1;
	@SerializedName("Flag")
	public String Flag;
	@SerializedName("type")
	public int type;
	@SerializedName("text")
	public String text;
	@SerializedName("displayName")
	public String displayName;
	@SerializedName("headImg")
	public String headImg;
	@SerializedName("mobile")
	public String mobile;
	@SerializedName("accountId")
	public long accountId;
	@SerializedName("auth")
	public int auth;
	@SerializedName("VcardNo")
	public String VcardNo;
	@SerializedName("bindWay")
	public int bindWay;
	@SerializedName("hasPhone")
	public int hasPhone;
	@SerializedName("headData")
	public byte[] headData;
	@SerializedName("contactId")
	public long contactId;
	@SerializedName("nameSpell")
	public String nameSpell;
	@SerializedName("sortKey")
	public String sortKey;
	@SerializedName("headPhoto")
	public Bitmap headPhoto;
	@SerializedName("isVcardUser")
	public boolean isVcardUser;
	@SerializedName("contactRawId")
	public long contactRawId;

	public VCardAndBookEntity(){

	}

	public VCardAndBookEntity(int type, String text) {
	    this.type = type;
	    this.text = text;
	}
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
	public int getHasPhone() {
		return hasPhone;
	}
	public void setHasPhone(int hasPhone) {
		this.hasPhone = hasPhone;
	}
	public byte[] getHeadData() {
		return headData;
	}
	public void setHeadData(byte[] headData) {
		this.headData = headData;
	}
	public long getContactId() {
		return contactId;
	}
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	public String getNameSpell() {
		return nameSpell;
	}
	public void setNameSpell(String nameSpell) {
		this.nameSpell = nameSpell;
	}
	public String getSortKey() {
		return sortKey;
	}
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	public Bitmap getHeadPhoto() {
		return headPhoto;
	}
	public void setHeadPhoto(Bitmap headPhoto) {
		this.headPhoto = headPhoto;
	}
	public boolean isVcardUser() {
		return isVcardUser;
	}
	public void setVcardUser(boolean isVcardUser) {
		this.isVcardUser = isVcardUser;
	}
	public long getContactRawId() {
		return contactRawId;
	}
	public void setContactRawId(long contactRawId) {
		this.contactRawId = contactRawId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}
}
