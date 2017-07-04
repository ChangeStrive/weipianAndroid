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
 * 网站基础配置
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_config")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebConfig {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 网站名称
	 */
	@Column(length = 100)
	private String fdWebName;
	
	/**
	 * 网站logo图片连接
	 */
	@Column(length = 100)
	private String fdLogoPicUrl;
	
	/**
	 * 版权信息
	 */
	@Column(length = 200)
	private String fdCopyright;

	/**
	 * 网站底部信息
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fdFootContent", columnDefinition="TEXT", nullable=true)
	private String fdFootContent;
	
	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdWebName() {
		return fdWebName;
	}

	public void setFdWebName(String fdWebName) {
		this.fdWebName = fdWebName;
	}

	public String getFdLogoPicUrl() {
		return fdLogoPicUrl;
	}

	public void setFdLogoPicUrl(String fdLogoPicUrl) {
		this.fdLogoPicUrl = fdLogoPicUrl;
	}

	public String getFdCopyright() {
		return fdCopyright;
	}

	public void setFdCopyright(String fdCopyright) {
		this.fdCopyright = fdCopyright;
	}

	public String getFdFootContent() {
		return fdFootContent;
	}

	public void setFdFootContent(String fdFootContent) {
		this.fdFootContent = fdFootContent;
	}
}
