package com.platform.sys.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 角色
 * @author Administrator
 *
 */
@Entity
@Table(name = "sys_role")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class SysRole {

	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 角色名称
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 描述
	 */
	@Column(length = 200)
	private String fdDesc;
	
	/**
	 * 状态 0表示禁用  1表示启用
	 */
	@Column(length =10)
	private String fdStatus;
	
	@ManyToMany( fetch=FetchType.LAZY)
	@JoinTable(name = "sys_role_user", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "userId") })
	private Set<SysUser> sysUsers=new HashSet(0);

	@ManyToMany( fetch=FetchType.LAZY)
	@JoinTable(name = "sys_role_menu", joinColumns = { @JoinColumn(name = "roleId") }, inverseJoinColumns = { @JoinColumn(name = "menuId") })
	private Set<SysMenu> sysMenus=new HashSet(0);
	
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

	public String getFdDesc() {
		return fdDesc;
	}

	public void setFdDesc(String fdDesc) {
		this.fdDesc = fdDesc;
	}
	
	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}

	public Set<SysUser> getSysUsers() {
		return sysUsers;
	}

	public void setSysUsers(Set<SysUser> sysUsers) {
		this.sysUsers = sysUsers;
	}

	public Set<SysMenu> getSysMenus() {
		return sysMenus;
	}

	public void setSysMenus(Set<SysMenu> sysMenus) {
		this.sysMenus = sysMenus;
	}
}
