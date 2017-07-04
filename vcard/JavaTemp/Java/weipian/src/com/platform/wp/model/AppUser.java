package com.platform.wp.model;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 会员
 * @author Administrator
 *
 */
@Entity
@Table(name = "app_user")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppUser {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 手机号
	 */
	@Column(length = 100)
	private String fdCode;
	
	/**
	 * 用户名
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 用户头像
	 */
	@Column(length = 200)
	private String fdPicUrl;
	
	/**
	 * 微信号
	 */
	@Column(length = 32)
	private String fdWeixinid;
	
	/**
	 * 密码
	 */
	@Column(length = 100)
	private String fdPwd;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;

	/**
	 * 状态 1表示男 0表示女
	 */
	@Column(length = 10)
	private String fdSex;
	
	/**
	 * 生日
	 */
	@Column(length = 50)
	private String fdBirthday;
	
	/**
	 * 佣金
	 */
	private BigDecimal fdAmount;
	

	/**
	 * 省份
	 */
	@Column(length = 100)
	private String fdProvince;
	
	/**
	 * 城市
	 */
	@Column(length = 100)
	private String fdCity;
	
	/**
	 * 区域
	 */
	@Column(length = 100)
	private String fdArea;
	
	/**
	 * 一级会员数量
	 */
	private Integer fdFirstCount;
	
	/**
	 * 二级会员数量
	 */
	private Integer fdSecondCount;
	
	/**
	 * 三级会员数量
	 */
	private Integer fdThreeCount;

	/**
	 * 购买数量
	 */
	private Integer fdBuyCount;

	/**
	 * 发货商id
	 */
	@Column(length = 32)
	private String fdFaHuoUserId;
	
	/**
	 * 上级分销商(多级的) 1表示直属上级 2表示上级的上级  3表示上级的上级的上级
	 * 格式如下:1.fdCode;2.fdCode;3.fdCode;
	 */
	@Column(length = 200)
	private String fdUpUser;
	
	/**
	 * 商家类型 0待审核  1普通分销商   2城市合伙人 3区域合伙人
	 */
	@Column(length = 10)
	private String fdShopType;
	
	/**
	 * 状态  0禁用 1表示启用
	 */
	@Column(length = 10)
	private String fdStatus;

	/**
	 * 系统用户id
	 */
	@Column(length = 32)
	private String fdSysUserId;
	
	/**
	 * 用户信息
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fdUserMessage", columnDefinition="TEXT", nullable=true)
	private String fdUserMessage;
	
	/**
	 * 会员信息明细
	 */
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "userDetailId", nullable = true, insertable = true, updatable = true) })
	private AppUserDetail appUserDetail;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdCode() {
		return fdCode;
	}

	public void setFdCode(String fdCode) {
		this.fdCode = fdCode;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}

	public String getFdWeixinid() {
		return fdWeixinid;
	}

	public void setFdWeixinid(String fdWeixinid) {
		this.fdWeixinid = fdWeixinid;
	}

	public String getFdPwd() {
		return fdPwd;
	}

	public void setFdPwd(String fdPwd) {
		this.fdPwd = fdPwd;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public String getFdSex() {
		return fdSex;
	}

	public void setFdSex(String fdSex) {
		this.fdSex = fdSex;
	}

	public String getFdBirthday() {
		return fdBirthday;
	}

	public void setFdBirthday(String fdBirthday) {
		this.fdBirthday = fdBirthday;
	}

	public BigDecimal getFdAmount() {
		return fdAmount;
	}

	public void setFdAmount(BigDecimal fdAmount) {
		this.fdAmount = fdAmount;
	}

	public String getFdProvince() {
		return fdProvince;
	}

	public void setFdProvince(String fdProvince) {
		this.fdProvince = fdProvince;
	}

	public String getFdCity() {
		return fdCity;
	}

	public void setFdCity(String fdCity) {
		this.fdCity = fdCity;
	}

	public String getFdArea() {
		return fdArea;
	}

	public void setFdArea(String fdArea) {
		this.fdArea = fdArea;
	}

	public Integer getFdFirstCount() {
		return fdFirstCount;
	}

	public void setFdFirstCount(Integer fdFirstCount) {
		this.fdFirstCount = fdFirstCount;
	}

	public Integer getFdSecondCount() {
		return fdSecondCount;
	}

	public void setFdSecondCount(Integer fdSecondCount) {
		this.fdSecondCount = fdSecondCount;
	}

	public Integer getFdThreeCount() {
		return fdThreeCount;
	}

	public void setFdThreeCount(Integer fdThreeCount) {
		this.fdThreeCount = fdThreeCount;
	}

	public Integer getFdBuyCount() {
		return fdBuyCount;
	}

	public void setFdBuyCount(Integer fdBuyCount) {
		this.fdBuyCount = fdBuyCount;
	}

	public String getFdFaHuoUserId() {
		return fdFaHuoUserId;
	}

	public void setFdFaHuoUserId(String fdFaHuoUserId) {
		this.fdFaHuoUserId = fdFaHuoUserId;
	}

	public String getFdUpUser() {
		return fdUpUser;
	}

	public void setFdUpUser(String fdUpUser) {
		this.fdUpUser = fdUpUser;
	}

	public String getFdShopType() {
		return fdShopType;
	}

	public void setFdShopType(String fdShopType) {
		this.fdShopType = fdShopType;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}

	public String getFdSysUserId() {
		return fdSysUserId;
	}

	public void setFdSysUserId(String fdSysUserId) {
		this.fdSysUserId = fdSysUserId;
	}

	public String getFdUserMessage() {
		return fdUserMessage;
	}

	public void setFdUserMessage(String fdUserMessage) {
		this.fdUserMessage = fdUserMessage;
	}

	public AppUserDetail getAppUserDetail() {
		return appUserDetail;
	}

	public void setAppUserDetail(AppUserDetail appUserDetail) {
		this.appUserDetail = appUserDetail;
	}
}
