package com.platform.weixin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 关键字自动应答
 * 
 *
 */
@Entity
@Table(name = "weixin_keyword_reply")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WeiXinKeywordReply {
	
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
	 * 关键字
	 */
	@Column(length = 100)
	private String fdKeyword;
	
	/**
	 * 图片
	 */
	@Column(length = 200)
	private String fdPicUrl;
	
	/**
	 * 链接地址
	 */
	@Column(length = 200)
	private String fdUrl;
	
	/**
	 * 描述
	 */
	@Column(length = 200)
	private String fdDesc;
	/**
	 * 类型 0:文本 1:图文
	 */
	@Column(length = 11)
	private String fdType;
	
	/**
	 * 创建时间
	 */
	@Column(length = 20)
	private String fdCreateTime;
	
	/**
	 * 状态
	 */
	@Column(length = 11)
	private String fdStatus;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}
	
	public String getFdKeyword() {
		return fdKeyword;
	}

	public void setFdKeyword(String fdKeyword) {
		this.fdKeyword = fdKeyword;
	}

	public String getFdTitle() {
		return fdTitle;
	}

	public void setFdTitle(String fdTitle) {
		this.fdTitle = fdTitle;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}

	public String getFdUrl() {
		return fdUrl;
	}

	public void setFdUrl(String fdUrl) {
		this.fdUrl = fdUrl;
	}

	public String getFdDesc() {
		return fdDesc;
	}

	public void setFdDesc(String fdDesc) {
		this.fdDesc = fdDesc;
	}

	public String getFdType() {
		return fdType;
	}

	public void setFdType(String fdType) {
		this.fdType = fdType;
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
}
