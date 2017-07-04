package com.platform.wp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 店铺管理
 * @author Administrator
 *
 */
@Entity
@Table(name = "app_user_shop")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppUserShop {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 用户头像
	 */
	@Column(length = 32,unique=true)
	private String fdUserId;
	
	/**
	 * 手机号
	 */
	@Column(length = 100)
	private String fdUserCode;
	
	/**
	 * 用户名
	 */
	@Column(length = 100)
	private String fdUserName;
	
	/**
	 * 店铺名称
	 */
	@Column(length = 100)
	private String fdStoreName;
	
	/**
	 * 店铺类别
	 */
	@Column(length = 100)
	private String fdStoreType;
	
	/**
	 * 店铺图片
	 */
	@Column(length = 300)
	private String fdPicUrl;

	
	/**
	 * 店铺地址
	 */
	@Column(length = 200)
	private String fdStoreAddress;
	
	/**
	 * 品牌名称
	 */
	@Column(length = 200)
	private String fdStoreBrand;

	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 状态 -1审核不通过  0表示待审核 1表示审核通过
	 */
	@Column(length = 10)
	private String fdStatus;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdUserId() {
		return fdUserId;
	}

	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}

	public String getFdUserCode() {
		return fdUserCode;
	}

	public void setFdUserCode(String fdUserCode) {
		this.fdUserCode = fdUserCode;
	}

	public String getFdUserName() {
		return fdUserName;
	}

	public void setFdUserName(String fdUserName) {
		this.fdUserName = fdUserName;
	}

	public String getFdStoreName() {
		return fdStoreName;
	}

	public void setFdStoreName(String fdStoreName) {
		this.fdStoreName = fdStoreName;
	}

	public String getFdStoreType() {
		return fdStoreType;
	}

	public void setFdStoreType(String fdStoreType) {
		this.fdStoreType = fdStoreType;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}

	public String getFdStoreAddress() {
		return fdStoreAddress;
	}

	public void setFdStoreAddress(String fdStoreAddress) {
		this.fdStoreAddress = fdStoreAddress;
	}

	public String getFdStoreBrand() {
		return fdStoreBrand;
	}

	public void setFdStoreBrand(String fdStoreBrand) {
		this.fdStoreBrand = fdStoreBrand;
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
