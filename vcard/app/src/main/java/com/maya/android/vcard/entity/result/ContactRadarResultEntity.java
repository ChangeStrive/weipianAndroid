package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
/**
 * 雷达扫描 联系人 实体类
 * @author zheng_cz
 * @since 2014年5月12日 下午4:05:02
 */
public class ContactRadarResultEntity implements Cloneable{

	/** 名片ID **/
	@SerializedName("cardId")
	private long cardId;

	/** 账户ID **/
	@SerializedName("accountId")
	private long accountId;
	/**
	 * 显示名称（默认为帐户昵称，昵称为空则为帐户姓名）
	 */
	@SerializedName("personName")
	private String displayName;
	/** 公司名称 **/
	@SerializedName("companyName")
	private String companyName;
	/** 职位 **/
	@SerializedName("job")
	private String job;
	/** 帐户头像 **/
	@SerializedName("headImg")
	private String headImg;
	/** 性别 **/
	@SerializedName("sex")
	private int sex;
	/** 行业 **/
	@SerializedName("business")
	private int business;
	/** 第三方标识 **/
	@SerializedName("partnerId")
	private long partnerId = 0;
	/** 距离 **/
	@SerializedName("distance")
	private double distance;
	/** 微片号 **/
	@SerializedName("vcardNo")
	private String vcardNo;
	/** 是否已是好友 **/
	@SerializedName("friend")
	private int friend;
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	public int getBusiness() {
		return business;
	}
	public void setBusiness(int business) {
		this.business = business;
	}
	public long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getVcardNo() {
		return vcardNo;
	}
	public void setVcardNo(String vcardNo) {
		this.vcardNo = vcardNo;
	}
	public int getFriend() {
		return friend;
	}
	public void setFriend(int isFriend) {
		this.friend = isFriend;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
