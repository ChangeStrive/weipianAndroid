package com.platform.weixin.model;

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
 * 首次关注欢迎语
 * 
 *
 */
@Entity
@Table(name = "weixin_welcomes")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WeiXinWelcomes {
	
	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 标题
	 */
	@Column(length = 100)
	private String fdTitle;
	
	/**
	 * 正文
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fd_content", columnDefinition="TEXT", nullable=true)
	private String fdContent;
	
	/**
	 * 封面图
	 */
	@Column(length = 100)
	private String fdPicUrl;
	
	/**
	 * 摘要
	 */
	@Column(length = 200)
	private String fdSummary;
	
	/**
	 * 链接
	 */
	@Column(length = 200)
	private String fdUrl;
	
	/**
	 * 上级
	 */
	@Column(length = 100)
	private String fdPid;
	
	/**
	 * 上级名称
	 */
	@Column(length = 100)
	private String fdPidName;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 自动回复类型：0、文本 1、图文
	 */
	@Column(length = 11)
	private String fdType;
	
	/**
	 * 状态：0、禁用 1、启用
	 */
	@Column(length = 11)
	private String fdStatus;

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
	
	public String getFdUrl() {
		return fdUrl;
	}

	public void setFdUrl(String fdUrl) {
		this.fdUrl = fdUrl;
	}

	public String getFdContent() {
		return fdContent;
	}

	public void setFdContent(String fdContent) {
		this.fdContent = fdContent;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}

	public String getFdSummary() {
		return fdSummary;
	}

	public void setFdSummary(String fdSummary) {
		this.fdSummary = fdSummary;
	}

	public String getFdPid() {
		return fdPid;
	}

	public void setFdPid(String fdPid) {
		this.fdPid = fdPid;
	}
	
	public String getFdPidName() {
		return fdPidName;
	}

	public void setFdPidName(String fdPidName) {
		this.fdPidName = fdPidName;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public String getFdType() {
		return fdType;
	}

	public void setFdType(String fdType) {
		this.fdType = fdType;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}
}
