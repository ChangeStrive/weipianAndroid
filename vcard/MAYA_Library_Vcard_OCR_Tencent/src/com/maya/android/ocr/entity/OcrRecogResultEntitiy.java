package com.maya.android.ocr.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 名片识别结果  Entity
 * @author ZuoZiJi-Y.J
 * @version v1.0
 * @since 2013-8-29
 *
 */
public class OcrRecogResultEntitiy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4627008788658340929L;
	/**
	 * 网址
	 */
	@SerializedName("url")
	private String url;
	/**
	 * 邮箱
	 */
	@SerializedName("mail")
	private String mail;
	/**
	 * 电话
	 */
	@SerializedName("phone")
	private String phone;
	/**
	 * 手机
	 */
	@SerializedName("telephone")
	private String telephone;
	/**
	 * 传真
	 */
	@SerializedName("fax")
	private String fax;
	/**
	 * 传呼
	 */
	@SerializedName("call")
	private String call;
	/**
	 * 邮编
	 */
	@SerializedName("postcode")
	private String postcode;
	/**
	 * 地址
	 */
	@SerializedName("address")
	private String address;
	/**
	 * 公司
	 */
	@SerializedName("company")
	private String company;
	/**
	 * 部门
	 */
	@SerializedName("department")
	private String department;
	/**
	 * 职位
	 */
	@SerializedName("position")
	private String position;
	/**
	 * 英文名
	 */
	@SerializedName("englishName")
	private String englishName;
	/**
	 * 中文名
	 */
	@SerializedName("chineseName")
	private String chineseName;
	/**
	 * QQ
	 */
	@SerializedName("QQ")
	private String QQ;
	/**
	 * MSN
	 */
	@SerializedName("MSN")
	private String MSN;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getCall() {
		return call;
	}
	public void setCall(String call) {
		this.call = call;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getMSN() {
		return MSN;
	}
	public void setMSN(String mSN) {
		MSN = mSN;
	}
	
	
}
