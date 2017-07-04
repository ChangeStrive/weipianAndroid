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
*支付
*/
@Entity
@Table(name = "app_charge")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppCharge {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	@Column(length = 32,unique=true)
	private String fdChargeId;
	
	@Column(length = 32)
	private String fdOrderId;
	
	@Column(length = 100)
	private String fdOrderNo;
	
	@Column(length = 100)
	private String fdShopNo;

	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdChargeId() {
		return fdChargeId;
	}

	public void setFdChargeId(String fdChargeId) {
		this.fdChargeId = fdChargeId;
	}

	public String getFdOrderId() {
		return fdOrderId;
	}

	public void setFdOrderId(String fdOrderId) {
		this.fdOrderId = fdOrderId;
	}

	public String getFdOrderNo() {
		return fdOrderNo;
	}

	public void setFdOrderNo(String fdOrderNo) {
		this.fdOrderNo = fdOrderNo;
	}

	public String getFdShopNo() {
		return fdShopNo;
	}

	public void setFdShopNo(String fdShopNo) {
		this.fdShopNo = fdShopNo;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}
}
