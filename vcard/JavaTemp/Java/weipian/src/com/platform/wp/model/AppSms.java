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
 * 短信管理
 * @author Administrator
 *
 */
@Entity
@Table(name = "app_sms")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppSms {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	
	/**
	 * 手机号
	 */
	@Column(length = 11)
	private String fdMobile;
	
	/**
	 * 内容
	 */
	@Column(length = 100)
	private String fdContent;
	
	/**
	 * 类型（发送类型（0.注册，1.修改密码 ）
	 */
	@Column(length = 10)
	private String fdType;
	
	/**
	 * 验证码
	 */
	@Column(length = 10)
	private String fdValidCode;
	
	
	/**
	 * 发送时间
	 */
	@Column(length = 50)
	private String fdSendTime;
	
	/**
	 * 状态(短信接口返回状态)
	 */
	@Column(length = 10)
	private String fdStatus;

	/**
	 * 0表示未使用  1表示已使用
	 */
	@Column(length = 10)
	private String fdIsUse;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdMobile() {
		return fdMobile;
	}

	public void setFdMobile(String fdMobile) {
		this.fdMobile = fdMobile;
	}

	public String getFdContent() {
		return fdContent;
	}

	public void setFdContent(String fdContent) {
		this.fdContent = fdContent;
	}

	public String getFdType() {
		return fdType;
	}

	public void setFdType(String fdType) {
		this.fdType = fdType;
	}

	public String getFdValidCode() {
		return fdValidCode;
	}

	public void setFdValidCode(String fdValidCode) {
		this.fdValidCode = fdValidCode;
	}

	public String getFdSendTime() {
		return fdSendTime;
	}

	public void setFdSendTime(String fdSendTime) {
		this.fdSendTime = fdSendTime;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}

	public String getFdIsUse() {
		return fdIsUse;
	}

	public void setFdIsUse(String fdIsUse) {
		this.fdIsUse = fdIsUse;
	}
}
