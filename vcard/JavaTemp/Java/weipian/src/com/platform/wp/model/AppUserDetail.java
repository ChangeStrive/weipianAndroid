package com.platform.wp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 会员信息明细
 * @author Administrator
 *
 */
@Entity
@Table(name = "app_user_detail")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppUserDetail {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 用户id
	 */
	@Column(length = 32)
	private String fdUserId;
	
	/**
	 * 职位
	 */
	@Column(length = 100)
	private String fdJobs;
	
	/**
	 * 公司
	 */
	@Column(length = 400)
	private String fdCompany;
	
	/**
	 * 行业类型
	 */
	@Column(length = 400)
	private String fdCompanyType;
	
	/**
	 * 地址
	 */
	@Column(length = 400)
	private String fdCompanyAddress;
	
	/**
	 * 网址
	 */
	@Column(length =200)
	private String fdCompanyUrl;
	
	/**
	 * 手机
	 */
	@Column(length = 400)
	private String fdMobile;
	
	/**
	 * 联系电话
	 */
	@Column(length = 400)
	private String fdTel;
	
	/**
	 *传真
	 */
	@Column(length = 400)
	private String fdFax;
	
	/**
	 *邮箱
	 */
	@Column(length = 400)
	private String fdEmail;
	
	/**
	 *微信号
	 */
	@Column(length = 400)
	private String fdWxNo;
	
	/**
	 *毕业学校
	 */
	@Column(length = 400)
	private String fdSchool;
	
	/**
	 *学历
	 */
	@Column(length = 400)
	private String fdEducation;
	
	/**
	 * 专业
	 */
	@Column(length = 400)
	private String fdMajor;
	
	/**
	 * 输入企业简介或者其他信息
	 */
	@Column(length = 400)
	private String fdRemark;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdUserId() {
		return fdUserId;
	}

	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}

	public String getFdJobs() {
		return fdJobs;
	}

	public void setFdJobs(String fdJobs) {
		this.fdJobs = fdJobs;
	}

	public String getFdCompany() {
		return fdCompany;
	}

	public void setFdCompany(String fdCompany) {
		this.fdCompany = fdCompany;
	}

	public String getFdCompanyType() {
		return fdCompanyType;
	}

	public void setFdCompanyType(String fdCompanyType) {
		this.fdCompanyType = fdCompanyType;
	}

	public String getFdCompanyAddress() {
		return fdCompanyAddress;
	}

	public void setFdCompanyAddress(String fdCompanyAddress) {
		this.fdCompanyAddress = fdCompanyAddress;
	}

	public String getFdCompanyUrl() {
		return fdCompanyUrl;
	}

	public void setFdCompanyUrl(String fdCompanyUrl) {
		this.fdCompanyUrl = fdCompanyUrl;
	}

	public String getFdMobile() {
		return fdMobile;
	}

	public void setFdMobile(String fdMobile) {
		this.fdMobile = fdMobile;
	}

	public String getFdTel() {
		return fdTel;
	}

	public void setFdTel(String fdTel) {
		this.fdTel = fdTel;
	}

	public String getFdFax() {
		return fdFax;
	}

	public void setFdFax(String fdFax) {
		this.fdFax = fdFax;
	}

	public String getFdEmail() {
		return fdEmail;
	}

	public void setFdEmail(String fdEmail) {
		this.fdEmail = fdEmail;
	}

	public String getFdWxNo() {
		return fdWxNo;
	}

	public void setFdWxNo(String fdWxNo) {
		this.fdWxNo = fdWxNo;
	}

	public String getFdSchool() {
		return fdSchool;
	}

	public void setFdSchool(String fdSchool) {
		this.fdSchool = fdSchool;
	}

	public String getFdEducation() {
		return fdEducation;
	}

	public void setFdEducation(String fdEducation) {
		this.fdEducation = fdEducation;
	}

	public String getFdMajor() {
		return fdMajor;
	}

	public void setFdMajor(String fdMajor) {
		this.fdMajor = fdMajor;
	}

	public String getFdRemark() {
		return fdRemark;
	}

	public void setFdRemark(String fdRemark) {
		this.fdRemark = fdRemark;
	}
}
