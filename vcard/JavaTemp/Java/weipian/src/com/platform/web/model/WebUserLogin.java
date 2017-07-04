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
 * 用户登陆信息
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_user_login")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebUserLogin {

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
	 * 用户登陆名
	 */
	@Column(length = 32)
	private String fdLoginName;
	
	/**
	 * 用户登陆时间
	 */
	@Column(length = 32)
	private String fdLoginTime;
	
	/**
	 * 用户登陆ip
	 */
	@Column(length = 32)
	private String fdLoginIp;

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

	public String getFdLoginName() {
		return fdLoginName;
	}

	public void setFdLoginName(String fdLoginName) {
		this.fdLoginName = fdLoginName;
	}

	public String getFdLoginTime() {
		return fdLoginTime;
	}

	public void setFdLoginTime(String fdLoginTime) {
		this.fdLoginTime = fdLoginTime;
	}

	public String getFdLoginIp() {
		return fdLoginIp;
	}

	public void setFdLoginIp(String fdLoginIp) {
		this.fdLoginIp = fdLoginIp;
	}
}
