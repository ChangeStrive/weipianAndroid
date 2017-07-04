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
 * 微信菜单
 * 
 */
@Entity
@Table(name = "weixin_menu")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WeiXinMenu {
	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 名称
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * key
	 */
	@Column(length = 100)
	private String fdKey;
	
	/**
	 * URL
	 */
	@Column(length = 100)
	private String fdUrl;
	
	/**
	 * 类型 0:KEY 1:URL 2扫码带提示 3扫码推事件
	 */
	@Column(length = 11)
	private String fdType;
	
	/**
	 * 排序
	 */
	@Column(length = 11)
	private String fdSeq;
	
	/**
	 * 上级菜单Id
	 */
	@Column(length = 32)
	private String fdPid;
	
	/**
	 * 上级菜单名称
	 */
	@Column(length = 100)
	private String fdPidName;
	
	/**
	 * 层级  1表示一级菜单 2表示二级菜单
	 */
	@Column(length = 100)
	private Integer fdLevel;
	
	/**
	 * 是否授权 0：未授权 1：已授权
	 */
	@Column(length = 11)
	private String fdStatus;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getFdKey() {
		return fdKey;
	}

	public void setFdKey(String fdKey) {
		this.fdKey = fdKey;
	}

	public String getFdUrl() {
		return fdUrl;
	}

	public void setFdUrl(String fdUrl) {
		this.fdUrl = fdUrl;
	}

	public String getFdType() {
		return fdType;
	}

	public void setFdType(String fdType) {
		this.fdType = fdType;
	}

	public String getFdSeq() {
		return fdSeq;
	}

	public void setFdSeq(String fdSeq) {
		this.fdSeq = fdSeq;
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

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}

	public Integer getFdLevel() {
		return fdLevel;
	}

	public void setFdLevel(Integer fdLevel) {
		this.fdLevel = fdLevel;
	}
}
