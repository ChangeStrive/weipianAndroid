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
 * 用户对账单
 * @author Administrator
 *
 */
@Entity
@Table(name = "app_user_account")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppUserAccount {

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
	 * 积分数量
	 */
	private Integer fdIntegral;
		
	/**
	 * 类型（0.充值、1.购买、2.返利、3.赠送）
	 */
	@Column(length = 10)
	private String fdType;

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


	public String getFdUserId() {
		return fdUserId;
	}


	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
	}


	public Integer getFdIntegral() {
		return fdIntegral;
	}


	public void setFdIntegral(Integer fdIntegral) {
		this.fdIntegral = fdIntegral;
	}


	public String getFdType() {
		return fdType;
	}


	public void setFdType(String fdType) {
		this.fdType = fdType;
	}


	public String getFdCreateTime() {
		return fdCreateTime;
	}


	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	


}
