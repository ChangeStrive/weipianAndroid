package com.platform.web.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 招聘
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_job")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebJob {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	private String fdId;
	
	/**
	 * 标题
	 */
	@Column(length = 100)
	private String fdTitle;
	
	/**
	 * 学历要求
	 */
	@Column(length = 100)
	private String fdEducation;
	
	/**
	 * 性别要求
	 */
	@Column(length = 100)
	private String fdSex;
	
	/**
	 * 年龄要求
	 */
	@Column(length = 100)
	private String fdAge;
	
	/**
	 * 职位性质  兼职 全职 临时
	 */
	@Column(length = 100)
	private String fdJobType;
	
	/**
	 * 工作地点
	 */
	@Column(length = 200)
	private String fdAddress;
	
	/**
	 * 招聘人数
	 */
	private Integer fdNum;
	
	/**
	 * 开始时间
	 */
	@Column(length = 50)
	private String fdStartTime;
	
	/**
	 * 结束时间
	 */
	@Column(length = 50)
	private String fdEndTime;
	
	/**
	 * 招聘类型
	 */
	@Column(length = 32)
	private String fdTypeId;
	
	/**
	 * 招聘类型
	 */
	@Column(length = 100)
	private String fdTypeName;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 职位职责
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fdJobDuty", columnDefinition="TEXT", nullable=true)
	private String fdJobDuty;
	
	/**
	 * 任职资格
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fdJobReq", columnDefinition="TEXT", nullable=true)
	private String fdJobReq;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdTitle() {
		return fdTitle;
	}

	public void setFdTitle(String fdTitle) {
		this.fdTitle = fdTitle;
	}

	public String getFdEducation() {
		return fdEducation;
	}

	public void setFdEducation(String fdEducation) {
		this.fdEducation = fdEducation;
	}

	public String getFdSex() {
		return fdSex;
	}

	public void setFdSex(String fdSex) {
		this.fdSex = fdSex;
	}

	public String getFdAge() {
		return fdAge;
	}

	public void setFdAge(String fdAge) {
		this.fdAge = fdAge;
	}

	public String getFdJobType() {
		return fdJobType;
	}

	public void setFdJobType(String fdJobType) {
		this.fdJobType = fdJobType;
	}

	public String getFdAddress() {
		return fdAddress;
	}

	public void setFdAddress(String fdAddress) {
		this.fdAddress = fdAddress;
	}

	public Integer getFdNum() {
		return fdNum;
	}

	public void setFdNum(Integer fdNum) {
		this.fdNum = fdNum;
	}

	public String getFdStartTime() {
		return fdStartTime;
	}

	public void setFdStartTime(String fdStartTime) {
		this.fdStartTime = fdStartTime;
	}

	public String getFdEndTime() {
		return fdEndTime;
	}

	public void setFdEndTime(String fdEndTime) {
		this.fdEndTime = fdEndTime;
	}

	public String getFdTypeId() {
		return fdTypeId;
	}

	public void setFdTypeId(String fdTypeId) {
		this.fdTypeId = fdTypeId;
	}

	public String getFdTypeName() {
		return fdTypeName;
	}

	public void setFdTypeName(String fdTypeName) {
		this.fdTypeName = fdTypeName;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public String getFdJobDuty() {
		return fdJobDuty;
	}

	public void setFdJobDuty(String fdJobDuty) {
		this.fdJobDuty = fdJobDuty;
	}

	public String getFdJobReq() {
		return fdJobReq;
	}

	public void setFdJobReq(String fdJobReq) {
		this.fdJobReq = fdJobReq;
	}
}
