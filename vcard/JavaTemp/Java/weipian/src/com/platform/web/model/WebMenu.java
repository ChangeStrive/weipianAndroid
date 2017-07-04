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
 * 导航菜单
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_menu")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebMenu {

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
	 * url
	 */
	@Column(length = 100)
	private String fdUrl;
	
	/**
	 * 父级菜单id
	 */
	@Column(length = 100)
	private String fdPid;
	
	/**
	 * 父级菜单名称
	 */
	@Column(length = 100)
	private String fdPidName;
	
	/**
	 * 顺序
	 */
	@Column(length = 11)
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

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}
}
