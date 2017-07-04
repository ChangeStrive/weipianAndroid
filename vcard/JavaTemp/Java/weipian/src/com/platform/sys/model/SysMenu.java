package com.platform.sys.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 菜单
 * @author Administrator
 *
 */
@Entity
@Table(name = "sys_menu")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class SysMenu {

	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId; 
	
	/**
	 * 菜单编号
	 */
	@Column(length = 100)
	private String fdNo;
	
	/**
	 * 菜单名称
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 父级id
	 */
	@Column(length = 32)
	private String fdPid;
	
	/**
	 * 父级名称
	 */
	@Column(length = 32)
	private String fdPidName;
	
	/**
	 * url
	 */
	@Column(length = 100)
	private String fdUrl;
	
	/**
	 * 分组
	 */
	@Column(length = 100)
	private String fdGroup;
	

	/**
	 * 层级  1表示一级菜单 2表示二级菜单  3表示三级菜单
	 */
	private Integer fdLevel;
	/**
	 * 顺序
	 */
	private Integer fdSeqNo;

	/**
	 * 状态  0表示禁用  1表示启用
	 */
	@Column(length = 10)
	private String fdStatus;
	
	@ManyToMany(mappedBy = "sysMenus", fetch=FetchType.LAZY)
	private Set<SysRole> sysRoles=new HashSet(0);
	
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

	public String getFdPid() {
		return fdPid;
	}

	public void setFdPid(String fdPid) {
		this.fdPid = fdPid;
	}

	public String getFdUrl() {
		return fdUrl;
	}

	public void setFdUrl(String fdUrl) {
		this.fdUrl = fdUrl;
	}

	public String getFdGroup() {
		return fdGroup;
	}

	public void setFdGroup(String fdGroup) {
		this.fdGroup = fdGroup;
	}

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
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

	
	public String getFdPidName() {
		return fdPidName;
	}

	public void setFdPidName(String fdPidName) {
		this.fdPidName = fdPidName;
	}

	public Set<SysRole> getSysRoles() {
		return sysRoles;
	}

	public void setSysRoles(Set<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}

	public String getFdNo() {
		return fdNo;
	}

	public void setFdNo(String fdNo) {
		this.fdNo = fdNo;
	}
}
