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
 * 广告
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_ad")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebAd {
	
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
	 * 跳转连接
	 */
	@Column(length = 100)
	private String fdUrl;
	
	/**
	 * 图片连接地址
	 */
	@Column(length = 100)
	private String fdPicUrl;
	
	/**
	 * 广告类型
	 */
	@Column(length = 32)
	private String fdTypeId;
	
	/**
	 * 广告类型
	 */
	@Column(length = 100)
	private String fdTypeName;
	
	/**
	 * 顺序
	 */
	private Integer fdSeqNo;

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

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
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

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}
}
