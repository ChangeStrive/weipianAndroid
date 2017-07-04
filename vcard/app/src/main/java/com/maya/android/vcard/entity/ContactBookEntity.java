package com.maya.android.vcard.entity;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.dao.ContactBookDao;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 通讯录联系人实体类
 * 
 * @author zheng_cz
 * @since 2014年3月24日 下午6:02:44
 */
public class ContactBookEntity {
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("hasPhone")
    private int hasPhone;
    @SerializedName("headData")
    private byte[] headData;
    @SerializedName("contactId")
    private long contactId;
    @SerializedName("nameSpell")
    private String nameSpell;
    @SerializedName("sortKey")
    private String sortKey;
    @SerializedName("headPhoto")
    private Bitmap headPhoto;
    /** 手机号码   */
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("isVcardUser")
    private boolean isVcardUser;
    /** 联系人 raw_contact_id **/
    @SerializedName("contactRawId")
    private long contactRawId;
    public ContactBookEntity(){

    }
    /** 构造函数
	 * @param contactId
	 * @param hasPhone
	 * @param displayName
	 * @param sortKey
	 * @param headPhoto
	 * @param nameFirstSpell
	 */
	public ContactBookEntity(long contactId, int hasPhone, String displayName,
			String sortKey, Bitmap headPhoto, String nameFirstSpell) {
		this.contactId = contactId;
		this.hasPhone = hasPhone;
		this.displayName = displayName;
		this.sortKey = sortKey;
		this.headPhoto = headPhoto;
		this.nameSpell = nameFirstSpell;

	}
	/** 构造函数
	 * @param contactId
	 * @param hasPhone
	 * @param displayName
	 * @param sortKey
	 * @param headPhoto
	 */
	public ContactBookEntity(long contactId, int hasPhone, String displayName,
			String sortKey, Bitmap headPhoto) {
		this.contactId = contactId;
		this.hasPhone = hasPhone;
		this.displayName = displayName;
		this.sortKey = sortKey;
		this.headPhoto = headPhoto;

	}
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
    public String getMobile() {
    	if(ResourceHelper.isEmpty(mobile)){
    		mobile = ContactBookDao.getInstance().getMobile(contactId);
    	}
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
