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
 * 留言
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_message")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebMessage {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 用户名
	 */
	
	@Column(length = 100)
	private String fdUserName;
	
	@Column(length = 100)
	private String fdEmail;
	
	@Column(length = 100)
	private String fdTel;
	
	@Column(length = 2000)
	private String fdContent;

	/**
	 * 是否管理员回复
	 */
	@Column(length = 2000)
	private String fdReplay;
	
	
	@Column(length = 50)
	private String fdCreateTime;
	
	
	/**
	 * 0不显示 1显示
	 */
	@Column(length = 10)
	private String fdStatus;
	
	
	
	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdUserName() {
		return fdUserName;
	}

	public void setFdUserName(String fdUserName) {
		this.fdUserName = fdUserName;
	}

	public String getFdEmail() {
		return fdEmail;
	}

	public void setFdEmail(String fdEmail) {
		this.fdEmail = fdEmail;
	}

	public String getFdTel() {
		return fdTel;
	}

	public void setFdTel(String fdTel) {
		this.fdTel = fdTel;
	}

	public String getFdContent() {
		return fdContent;
	}

	public void setFdContent(String fdContent) {
		this.fdContent = fdContent;
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

	public String getFdReplay() {
		return fdReplay;
	}

	public void setFdReplay(String fdReplay) {
		this.fdReplay = fdReplay;
	}

}
