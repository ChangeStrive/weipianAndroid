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
*支付记录
*/
@Entity
@Table(name = "app_record")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppRecord {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 流水号
	 */
	@Column(length = 32,unique=true)
	private String fdRecordNo;
	
	/**
	 * 订单号
	 */
	@Column(length = 100)
	private String fdOrderNo;
	
	/**
	 * 实际金额
	 */
	private BigDecimal fdAmount;
	
	/**
	 * 数量
	 */
	private Integer fdQuantity;
	
	/**
	 * 商品金额
	 */
	private BigDecimal fdGoodsAmount;
	
	/**
	 * 优惠卷金额
	 */
	private BigDecimal  fdCoupon;
	
	/**
	 * 用户id
	 */
	@Column(length = 100)
	private String fdUserId;
	
	/**
	 * 用户账号
	 */
	@Column(length = 100)
	private String fdUserCode;
	
	/**
	 * 
	 * 用户名
	 */
	@Column(length = 100)
	private String fdUserName;
	
	/**
	 * 0 表示订单付款  1表示全额退款  2表示退货  3抽成
	 */
	@Column(length = 10)
	private String fdType;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;

	/**
	 * 退货编号
	 */
	@Column(length = 50)
	private String fdReturnGoodsNo;

	/**
	 *  0表示待确认 1表示已确认  -1取消
	 */
	@Column(length = 1)
	private String fdStatus;

	/**
	 * 防止重复提交
	 */
	@Column(length = 100,unique=true)
	private String fdToken;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdRecordNo() {
		return fdRecordNo;
	}

	public void setFdRecordNo(String fdRecordNo) {
		this.fdRecordNo = fdRecordNo;
	}

	public String getFdOrderNo() {
		return fdOrderNo;
	}

	public void setFdOrderNo(String fdOrderNo) {
		this.fdOrderNo = fdOrderNo;
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

	public BigDecimal getFdGoodsAmount() {
		return fdGoodsAmount;
	}

	public void setFdGoodsAmount(BigDecimal fdGoodsAmount) {
		this.fdGoodsAmount = fdGoodsAmount;
	}

	public BigDecimal getFdCoupon() {
		return fdCoupon;
	}

	public void setFdCoupon(BigDecimal fdCoupon) {
		this.fdCoupon = fdCoupon;
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

	public String getFdReturnGoodsNo() {
		return fdReturnGoodsNo;
	}

	public void setFdReturnGoodsNo(String fdReturnGoodsNo) {
		this.fdReturnGoodsNo = fdReturnGoodsNo;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}

	public String getFdToken() {
		return fdToken;
	}

	public void setFdToken(String fdToken) {
		this.fdToken = fdToken;
	}
}
