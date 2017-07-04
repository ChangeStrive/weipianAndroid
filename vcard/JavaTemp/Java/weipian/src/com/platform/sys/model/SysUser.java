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
 * 用户
 * @author Administrator
 *
 */
@Entity
@Table(name = "sys_user")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class SysUser {

	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId; 
	
	/**
	 * 登陆名
	 */
	@Column(length = 100,unique=true)
	private String fdLoginName;
	
	/**
	 * 是否超级管理员  0否 1是
	 */
	@Column(length = 10)
	private String fdIsAdmin;
	/**
	 * 姓名
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 密码
	 */
	@Column(length = 50)
	private String fdPwd;
	
	/**
	 * 邮箱
	 */
	@Column(length = 50)
	private String fdEmail;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	
	/**
	 * 最近登陆时间
	 */
	@Column(length = 50)
	private String fdLastTime;
	
	/**
	 * 登陆次数
	 */
	private Integer fdLoginNum=0;
	
	/**
	 * IP
	 */
	@Column(length = 50)
	private String fdLoginIp;
	
	/**
	 * 状态  0表示禁用  1表示启用
	 */
	@Column(length = 10)
	private String fdStatus;
	
	/**
	 * app用户id
	 */
	@Column(length = 32)
	private String fdAppUserId;
	
	/**
	 * app用户类型
	 */
	@Column(length = 10)
	private String fdAppUserType;
	
	@ManyToMany(mappedBy = "sysUsers", fetch=FetchType.LAZY)
	private Set<SysRole> sysRoles=new HashSet(0);

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdLoginName() {
		return fdLoginName;
	}

	public void setFdLoginName(String fdLoginName) {
		this.fdLoginName = fdLoginName;
	}

	public String getFdIsAdmin() {
		return fdIsAdmin;
	}

	public void setFdIsAdmin(String fdIsAdmin) {
		this.fdIsAdmin = fdIsAdmin;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getFdPwd() {
		return fdPwd;
	}

	public void setFdPwd(String fdPwd) {
		this.fdPwd = fdPwd;
	}

	public String getFdEmail() {
		return fdEmail;
	}

	public void setFdEmail(String fdEmail) {
		this.fdEmail = fdEmail;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public String getFdLastTime() {
		return fdLastTime;
	}

	public void setFdLastTime(String fdLastTime) {
		this.fdLastTime = fdLastTime;
	}

	public Integer getFdLoginNum() {
		return fdLoginNum;
	}

	public void setFdLoginNum(Integer fdLoginNum) {
		this.fdLoginNum = fdLoginNum;
	}

	public String getFdLoginIp() {
		return fdLoginIp;
	}

	public void setFdLoginIp(String fdLoginIp) {
		this.fdLoginIp = fdLoginIp;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}

	public String getFdAppUserId() {
		return fdAppUserId;
	}

	public void setFdAppUserId(String fdAppUserId) {
		this.fdAppUserId = fdAppUserId;
	}

	public String getFdAppUserType() {
		return fdAppUserType;
	}

	public void setFdAppUserType(String fdAppUserType) {
		this.fdAppUserType = fdAppUserType;
	}

	public Set<SysRole> getSysRoles() {
		return sysRoles;
	}

	public void setSysRoles(Set<SysRole> sysRoles) {
		this.sysRoles = sysRoles;
	}
}
