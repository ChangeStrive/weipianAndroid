package com.platform.wp.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 订单申请退款信息
 *
 */
@Entity
@Table(name = "app_goods_order_refunder")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoodsOrderRefunder {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 购买者用户id
	 */
	@Column(length = 32)
	private String fdUserId;
	
	/**
	 * 购买者用户账号
	 */
	@Column(length = 50)
	private String fdUserCode;
	
	/**
	 * 购买者用户名称
	 */
	@Column(length = 50)
	private String fdUserName;
	
	/**
	 * 申请退款时间
	 */
	@Column(length = 50)
	private String fdApplyRefundTime;
	
	/**
	 * 申请理由
	 */
	@Column(length = 200)
	private String fdApplyReason;
	
	
	/**
	 * 同意退款时间
	 */
	@Column(length = 50)
	private String fdRefundTime;
	
	/**
	 * 确认退款操作人id
	 */
	@Column(length = 32)
	private String fdRefunderId;
	
	/**
	 * 确认退款操作人名字
	 */
	@Column(length = 100)
	private String fdRefunderName;
	
	/**
	 * 驳回时间
	 */
	@Column(length = 50)
	private String fdBackTime;
	
	/**
	 * 驳回理由
	 */
	@Column(length = 100)
	private String fdBackReason;
	
	/**
	 * 状态 -1表示驳回  0表示申请中  1表示退款中    2表示退款成功
	 */
	@Column(length = 10)
	private String fdStatus;
	
	/**
	 * 退款完成时间
	 */
	@Column(length = 50)
	private String fdFinishTime;
	/**
	 * ping++ 退款编号
	 */
	@Column(length = 32)
	private String fdRefundId;
	
	/**
	 * 防止重复提交
	 */
	@Column(length = 100,unique=true)
	private String fdUniqueCode;
	
	/**
	 * 退款金额
	 */
	private  BigDecimal fdAmount;
	
	/**
	 * 数量
	 */
	private Integer fdQuantity;
	
	/**
	 * 
	 */
	@Column(length = 32)
	private String fdShopId;
	
	@Column(length = 100)
	private String fdShopNo;
	
	@Column(length = 100)
	private String fdShopName;
	
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "orderId", nullable = false, insertable = true, updatable = true) })
	private AppGoodsOrder appGoodsOrder;


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


	public String getFdApplyRefundTime() {
		return fdApplyRefundTime;
	}


	public void setFdApplyRefundTime(String fdApplyRefundTime) {
		this.fdApplyRefundTime = fdApplyRefundTime;
	}


	public String getFdApplyReason() {
		return fdApplyReason;
	}


	public void setFdApplyReason(String fdApplyReason) {
		this.fdApplyReason = fdApplyReason;
	}


	public String getFdRefundTime() {
		return fdRefundTime;
	}


	public void setFdRefundTime(String fdRefundTime) {
		this.fdRefundTime = fdRefundTime;
	}


	public String getFdRefunderId() {
		return fdRefunderId;
	}


	public void setFdRefunderId(String fdRefunderId) {
		this.fdRefunderId = fdRefunderId;
	}


	public String getFdRefunderName() {
		return fdRefunderName;
	}


	public void setFdRefunderName(String fdRefunderName) {
		this.fdRefunderName = fdRefunderName;
	}


	public String getFdBackTime() {
		return fdBackTime;
	}


	public void setFdBackTime(String fdBackTime) {
		this.fdBackTime = fdBackTime;
	}


	public String getFdBackReason() {
		return fdBackReason;
	}


	public void setFdBackReason(String fdBackReason) {
		this.fdBackReason = fdBackReason;
	}


	public String getFdStatus() {
		return fdStatus;
	}


	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}


	public String getFdFinishTime() {
		return fdFinishTime;
	}


	public void setFdFinishTime(String fdFinishTime) {
		this.fdFinishTime = fdFinishTime;
	}


	public String getFdRefundId() {
		return fdRefundId;
	}


	public void setFdRefundId(String fdRefundId) {
		this.fdRefundId = fdRefundId;
	}


	public String getFdUniqueCode() {
		return fdUniqueCode;
	}


	public void setFdUniqueCode(String fdUniqueCode) {
		this.fdUniqueCode = fdUniqueCode;
	}


	public BigDecimal getFdAmount() {
		return fdAmount;
	}


	public void setFdAmount(BigDecimal fdAmount) {
		this.fdAmount = fdAmount;
	}


	public Integer getFdQuantity() {
		return fdQuantity;
	}


	public void setFdQuantity(Integer fdQuantity) {
		this.fdQuantity = fdQuantity;
	}


	public String getFdShopId() {
		return fdShopId;
	}


	public void setFdShopId(String fdShopId) {
		this.fdShopId = fdShopId;
	}


	public String getFdShopNo() {
		return fdShopNo;
	}


	public void setFdShopNo(String fdShopNo) {
		this.fdShopNo = fdShopNo;
	}


	public String getFdShopName() {
		return fdShopName;
	}


	public void setFdShopName(String fdShopName) {
		this.fdShopName = fdShopName;
	}


	public AppGoodsOrder getAppGoodsOrder() {
		return appGoodsOrder;
	}


	public void setAppGoodsOrder(AppGoodsOrder appGoodsOrder) {
		this.appGoodsOrder = appGoodsOrder;
	}
	
}
