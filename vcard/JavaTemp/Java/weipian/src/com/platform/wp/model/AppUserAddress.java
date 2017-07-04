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
*
*用户收货地址管理
*/
@Entity
@Table(name = "app_user_address")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppUserAddress {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 用户id
	 */
	@Column(length = 32)
	private String fdUserId;
	
	/**
	 * 收货人
	 */
	@Column(length = 50)
	private String fdConsignee;
	
	/**
	 * 联系电话
	 */
	@Column(length = 50)
	private String fdTel;
	
	/**
	 * 省份
	 */
	@Column(length = 50)
	private String fdProvince;
	
	/**
	 * 城市
	 */
	@Column(length = 50)
	private String fdCity;
	
	/**
	 * 区域
	 */
	@Column(length = 50)
	private String fdArea;
	/**
	 * 收货地址
	 */
	@Column(length = 200)
	private String fdAddress;
	
	/**
	 * 邮编
	 */
	@Column(length = 50)
	private String fdZipCode;
	
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 1表示默认地址  0表示不是默认地址
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

	public String getFdConsignee() {
		return fdConsignee;
	}

	public void setFdConsignee(String fdConsignee) {
		this.fdConsignee = fdConsignee;
	}

	public String getFdTel() {
		return fdTel;
	}

	public void setFdTel(String fdTel) {
		this.fdTel = fdTel;
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

	public String getFdAddress() {
		return fdAddress;
	}

	public void setFdAddress(String fdAddress) {
		this.fdAddress = fdAddress;
	}

	public String getFdZipCode() {
		return fdZipCode;
	}

	public void setFdZipCode(String fdZipCode) {
		this.fdZipCode = fdZipCode;
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
