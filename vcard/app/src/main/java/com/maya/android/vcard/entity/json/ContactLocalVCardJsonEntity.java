package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 保存本地新增的名片
 * @author zheng_cz
 * @since 2014年4月29日 下午6:07:46
 */
public class ContactLocalVCardJsonEntity {
	/**
	 * 姓氏
	 */
	@SerializedName("surname")
	protected String surname;
	/**
	 * 名称
	 */
	@SerializedName("firstName")
	protected String firstName;
	/**英文名称*/
	@SerializedName("enName")
	private String enName;
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
	 * 手机号
	 */
	@SerializedName("mobileTelphone")
	private String mobileTelphone;
	/**
	 * 固话
	 */
	@SerializedName("lineTelphone")
	private String lineTelphone;
	/**
	 * 传真
	 */
	@SerializedName("fax")
	private String fax;
	/**
	 * 工作地址
	 */
	@SerializedName("workAddress")
	private String workAddress;
	/**
	 * 邮编
	 */
	@SerializedName("postcode")
	private String postcode;
	/**
	 * 电子邮箱
	 */
	@SerializedName("email")
	private String email;
	/** 行业 **/
	@SerializedName("business")
	private int business;
	/** 备注 **/
	@SerializedName("description")
	private String remark;
	/** 公司简介 **/
	@SerializedName("companyAbout")
	private String companyAbout; 
	/** 省份 **/
	@SerializedName("province")
	private String province; 
	/** 国籍 **/
	@SerializedName("country")
	private String country;
	/** 公司主页 **/
	@SerializedName("companyHome")
	private String companyHome; 
	/** 即时通讯json **/
	@SerializedName("imInfo")
	private String instantMessenger;
	
	/** 其他中文公司名称列表  **/
	@SerializedName("companyNameList")
	protected String otherCompanyNameList;
	
	/** 英文单位  **/
	@SerializedName("enCompanyName")
	private String companyEnName;

	/**企业logo*/
	@SerializedName("companyLogo")
	private String companyLogo;
	/**分组id*/
	@SerializedName("groupId")
	private long groupId;
	/**分组名称*/
	@SerializedName("groupName")
	private String groupName;
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
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
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
	public String getMobileTelphone() {
		return mobileTelphone;
	}
	public void setMobileTelphone(String mobileTelphone) {
		this.mobileTelphone = mobileTelphone;
	}
	public String getLineTelphone() {
		return lineTelphone;
	}
	public void setLineTelphone(String lineTelphone) {
		this.lineTelphone = lineTelphone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getBusiness() {
		return business;
	}
	public void setBusiness(int business) {
		this.business = business;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCompanyAbout() {
		return companyAbout;
	}
	public void setCompanyAbout(String companyAbout) {
		this.companyAbout = companyAbout;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCompanyHome() {
		return companyHome;
	}
	public void setCompanyHome(String companyHome) {
		this.companyHome = companyHome;
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
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
