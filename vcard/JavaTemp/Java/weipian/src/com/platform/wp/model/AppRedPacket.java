package com.platform.wp.model;

import java.math.BigDecimal;

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
*红包
*/
@Entity
@Table(name = "app_red_packet")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppRedPacket {

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
	@Column(length = 32,unique=true)
	private String fdUserId;
	
	/**
	 * 用户账号
	 */
	@Column(length = 32)
	private String fdUserCode;
	
	/**
	 * 用户名
	 */
	@Column(length = 100)
	private String fdUserName;
	
	/**
	 * 用户头像
	 */
	@Column(length = 200)
	private String fdUserPicUrl;
	
	/**
	 * 订单id
	 */
	@Column(length = 32)
	private Integer fdOrderId;
	
	/**
	 * 红包金额
	 */
	private BigDecimal fdAmount;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 状态 0表示未使用 1表示已使用
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

	public String getFdUserPicUrl() {
		return fdUserPicUrl;
	}

	public void setFdUserPicUrl(String fdUserPicUrl) {
		this.fdUserPicUrl = fdUserPicUrl;
	}

	public Integer getFdOrderId() {
		return fdOrderId;
	}

	public void setFdOrderId(Integer fdOrderId) {
		this.fdOrderId = fdOrderId;
	}

	public BigDecimal getFdAmount() {
		return fdAmount;
	}

	public void setFdAmount(BigDecimal fdAmount) {
		this.fdAmount = fdAmount;
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
