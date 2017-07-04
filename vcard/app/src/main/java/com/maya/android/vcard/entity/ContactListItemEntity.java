package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 联系人 listview 展示的属性类
 * 
 * @Version: 1.0
 * @author: zheng_cz
 * @since: 2013-7-17 下午2:47:23
 */
public class ContactListItemEntity implements Cloneable{

    @SerializedName("id")
    protected int id;
    /**
     * 姓名首字母
     */
    @SerializedName("firstLetter")
    private String firstLetter;

    /**
     * 分组id
     */
    @SerializedName("groupId")
    protected int groupId = 0;
    /**
     * 分组名称
     */
    @SerializedName("groupName")
    protected String groupName;
    /**
     * 显示名字
     */
    protected String displayName;

    /**
     * 第三方伙伴ID
     */
    @SerializedName("patnerId")
    protected int partnerId = 0;
    /**
     * 头像路径
     */
    @SerializedName("headImg")
    protected String headImg;
    /**
     * 认证加V
     */
    @SerializedName("auth")
    protected int auth;
    /**
     * 账号等级
     */
    @SerializedName("grade")
    protected int accountGrade = 0;
    /**
     * 公司名称
     */
    @SerializedName("companyName")
    protected String companyName;
    /**
     * 职位
     */
    @SerializedName("job")
    protected String job;
    /**
     * 行业
     */
    @SerializedName("business")
    protected int business = 0;
    /**
     * 排序值
     */
    protected String orderCode;
    /**
     * 名片ID
     */
    @SerializedName("cardId")
    protected Long cardId = 0L;
    /**
     * 帐户id
     */
    @SerializedName("accountId")
    protected long accountId;

    /**
     * 联系人ID
     */
    @SerializedName("contactPersonId")
    private long contactId = 0;

    /**
     * 交互时间
     */
    @SerializedName("createTime")
    private String createTime;

    /**
     * 绑定方式 1=手机绑定 2=邮箱绑定 3=手机、邮箱都绑定
     */
    @SerializedName("bindWay")
    protected int bindway;
    /**
     * 微片号
     */
    @SerializedName("vcardNo")
    protected String vcardNo;

    public ContactListItemEntity() {

    }

    public ContactListItemEntity(int id, int groupId, String groupName, String displayName, String companyName, String job, String headImg, int auth,
	    int accountGrade, int partnerId, int business, String orderCode, Long cardId, long accountId, String vcardNo) {
	this.id = id;
	this.groupId = groupId;
	this.groupName = groupName;
	this.displayName = displayName;
	this.companyName = companyName;
	this.job = job;
	this.headImg = headImg;
	this.auth = auth;
	this.accountGrade = accountGrade;
	this.partnerId = partnerId;
	this.business = business;
	this.orderCode = orderCode;
	this.cardId = cardId;
	this.accountId = accountId;
	this.vcardNo = vcardNo;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
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

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public int getPartnerId() {
	return partnerId;
    }

    public void setPartnerId(int partnerId) {
	this.partnerId = partnerId;
    }

    public String getHeadImg() {
	return headImg;
    }

    public void setHeadImg(String headImg) {
	this.headImg = headImg;
    }

    public int getAuth() {
	return auth;
    }

    public void setAuth(int auth) {
	this.auth = auth;
    }

    public int getAccountGrade() {
	return accountGrade;
    }

    public void setAccountGrade(int accountGrade) {
	this.accountGrade = accountGrade;
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

    public String getOrderCode() {
	return orderCode;
    }

    public void setOrderCode(String orderCode) {
	this.orderCode = orderCode;
    }

    public Long getCardId() {
	return cardId;
    }

    public void setCardId(Long cardId) {
	this.cardId = cardId;
    }

    public long getAccountId() {
	return accountId;
    }

    public void setAccountId(long accountId) {
	this.accountId = accountId;
    }

    public Long getContactId() {
	return contactId;
    }

    public void setContactId(Long contactId) {
	this.contactId = contactId;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public int getBindway() {
	return bindway;
    }

    public void setBindway(int bindway) {
	this.bindway = bindway;
    }

    public String getVcardNo() {
	return vcardNo;
    }

    public void setVcardNo(String vcardCode) {
	this.vcardNo = vcardCode;
    }

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
