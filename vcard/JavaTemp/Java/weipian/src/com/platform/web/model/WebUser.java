package com.platform.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 用户
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_user")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebUser {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 登录名
	 */
	@Column(length = 100)
	private String fdLoginName;
	
	/**
	 * 姓名
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 密码
	 */
	@Column(length = 50)
	private String fdPassWord;
	
	/**
	 * 性别 1表示男  0表示女
	 */
	@Column(length = 10)
	private String fdSex;
	
	/**
	 * 联系电话
	 */
	@Column(length = 20)
	private String fdTel;
	
	/**
	 * 邮箱
	 */
	@Column(length = 100)
	private String fdEmail;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 状态 0表示禁用  1表示启用  
	 */
	@Column(length = 10)
	private String fdStatus;

	/**
	 * 最近登陆ip
	 */
	@Column(length = 50)
	private String fdLastLoginIp;
	
	/**
	 * 最近登陆时间
	 */
	@Column(length = 50)
	private String fdLastLoginTime;
	
	/**
	 * 登陆次数
	 */
	@Column(length = 11)
	private Integer fdLoginCount;
	
	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdLoginName() {
		return fdLoginName;
	}

	public void setFdLoginName(String fdLoginName) {
		this.fdLoginName = fdLoginName;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getFdPassWord() {
		return fdPassWord;
	}

	public void setFdPassWord(String fdPassWord) {
		this.fdPassWord = fdPassWord;
	}

	public String getFdSex() {
		return fdSex;
	}

	public void setFdSex(String fdSex) {
		this.fdSex = fdSex;
	}

	public String getFdTel() {
		return fdTel;
	}

	public void setFdTel(String fdTel) {
		this.fdTel = fdTel;
	}

	public String getFdEmail() {
		return fdEmail;
	}

	public void setFdEmail(String fdEmail) {
		this.fdEmail = fdEmail;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}

	public String getFdLastLoginIp() {
		return fdLastLoginIp;
	}

	public void setFdLastLoginIp(String fdLastLoginIp) {
		this.fdLastLoginIp = fdLastLoginIp;
	}

	public String getFdLastLoginTime() {
		return fdLastLoginTime;
	}

	public void setFdLastLoginTime(String fdLastLoginTime) {
		this.fdLastLoginTime = fdLastLoginTime;
	}

	public Integer getFdLoginCount() {
		return fdLoginCount;
	}

	public void setFdLoginCount(Integer fdLoginCount) {
		this.fdLoginCount = fdLoginCount;
	}
}
