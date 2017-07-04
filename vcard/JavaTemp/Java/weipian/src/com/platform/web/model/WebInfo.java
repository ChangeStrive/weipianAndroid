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
 * 咨询
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_info")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebInfo {

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
	 * 简介
	 */
	@Column(length = 100)
	private String fdDesc;
	
	/**
	 * 封面图
	 */
	@Column(length = 100)
	private String fdPicUrl;
	
	/**
	 * 正文
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fdContent", columnDefinition="TEXT", nullable=true)
	private String fdContent;
	
	/**
	 * 咨询类型
	 */
	@Column(length = 32)
	private String fdTypeId;
	
	/**
	 * 咨询类型
	 */
	@Column(length = 100)
	private String fdTypeName;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;

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

	public String getFdDesc() {
		return fdDesc;
	}

	public void setFdDesc(String fdDesc) {
		this.fdDesc = fdDesc;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}

	public String getFdContent() {
		return fdContent;
	}

	public void setFdContent(String fdContent) {
		this.fdContent = fdContent;
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
}
