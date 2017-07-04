package com.platform.wp.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 订单
 *
 */
@Entity
@Table(name = "app_goods_order")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoodsOrder {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 订单编号
	 */
	@Column(length = 32,unique=true)
	private String fdOrderNo;
	
	/**
	 * 订单商品标题
	 */
	@Column(length = 100)
	private String fdOrderGoodsName;
	
	
	/**
	 * 购买者用户id
	 */
	@Column(length = 32)
	private String fdUserId;
	
	/**
	 * 购买者用户账号(手机号)
	 */
	@Column(length = 50)
	private String fdUserCode;
	
	/**
	 * 购买者用户名称
	 */
	@Column(length = 50)
	private String fdUserName;
	
	
	/**
	 * 数量
	 */
	private Integer fdQuantity;
	
	/**
	 * 总金额
	 */
	private BigDecimal fdAmount;
	
	/**
	 * 优惠卷金额
	 */
	private BigDecimal  fdCoupon;
	
	
	/**
	 * 商品总金额
	 */
	private  BigDecimal fdGoodsAmount;
	
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
	 * 快递名称
	 */
	@Column(length = 50)
	private String fdExpressName;
	
	/**
	 * 快递编号
	 */
	@Column(length = 50)
	private String fdExpressNo;
	
	/**
	 * 订单明细
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fdOrderDetail", columnDefinition="TEXT", nullable=true)
	private String fdOrderDetail;
	
	/**
	 * 备注
	 */
	@Column(length = 100)
	private String fdRemark;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 付积分时间
	 */
	@Column(length = 50)
	private String fdPayTime;
	
	/**
	 * -2  已退款(付款后取消订单)
	 * -1 取消订单(未付款而取消订单)
	 *  状态  
	 *  0 未付款  
	 *  1 表示已付款 (未发货) 
	 *  2 已发货
	 *  3 待评价
	 *  4 申请退款
	 *  5 退款中
	 *  6 退款申请被驳回
	 *  7 已评价
	 */
	@Column(length = 10)
	private String fdStatus;

	
	/**
	 * 下订单IP
	 */
	@Column(length = 20)
	private String fdIp;
	
	/**
	 * 微信OpenId
	 */
	@Column(length = 50)
	private String fdOpenId;
	
	/**
	 * 防止重复提交
	 */
	@Column(length = 100,unique=true)
	private String fdUniqueCode;
	
	/**
	 * 交易编号
	 */
	@Column(length = 100)
	private String fdTransactionNo;
	
	/**
	 * 交易 id
	 */
	@Column(length = 100)
	private String fdChargeId;
	
	/**
	 * ping++ 退款编号
	 */
	@Column(length = 32)
	private String fdRefundId;
	
	/**
	 * 申请退款时间
	 */
	@Column(length = 50)
	private String fdApplyRefundTime;
	
	/**
	 * 申请理由
	 */
	@Column(length = 200)
	private String fdApplyRefundReason;
	
	/**
	 * 同意退款申请时间
	 */
	@Column(length = 50)
	private String fdAgreeApplyTime;
	
	/**
	 * 收货时间
	 */
	@Column(length = 50)
	private String fdReceiveTime;
	
	/**
	 * 发货时间
	 */
	@Column(length = 50)
	private String fdDeliverTime;
	
	/**
	 * 门店id
	 */
	@Column(length = 32)
	private String fdShopId;

	/**
	 * 门店编号
	 */
	@Column(length = 32)
	private String fdShopNo;
	
	/**
	 * 门店名称
	 */
	@Column(length = 100)
	private String fdShopName;
	
	/**
	 * 门店头像图片
	 */
	@Column(length = 100)
	private String fdShopPicUrl;
	
	
	@OneToMany(mappedBy = "appGoodsOrder", fetch=FetchType.LAZY)
	private Set<AppGoodsOrderItem> appGoodsOrderItems=new HashSet(0);


	public String getFdId() {
		return fdId;
	}


	public void setFdId(String fdId) {
		this.fdId = fdId;
	}


	public String getFdOrderNo() {
		return fdOrderNo;
	}


	public void setFdOrderNo(String fdOrderNo) {
		this.fdOrderNo = fdOrderNo;
	}


	public String getFdOrderGoodsName() {
		return fdOrderGoodsName;
	}


	public void setFdOrderGoodsName(String fdOrderGoodsName) {
		this.fdOrderGoodsName = fdOrderGoodsName;
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


	public Integer getFdQuantity() {
		return fdQuantity;
	}


	public void setFdQuantity(Integer fdQuantity) {
		this.fdQuantity = fdQuantity;
	}


	public BigDecimal getFdAmount() {
		return fdAmount;
	}


	public void setFdAmount(BigDecimal fdAmount) {
		this.fdAmount = fdAmount;
	}


	public BigDecimal getFdCoupon() {
		return fdCoupon;
	}


	public void setFdCoupon(BigDecimal fdCoupon) {
		this.fdCoupon = fdCoupon;
	}


	public BigDecimal getFdGoodsAmount() {
		return fdGoodsAmount;
	}


	public void setFdGoodsAmount(BigDecimal fdGoodsAmount) {
		this.fdGoodsAmount = fdGoodsAmount;
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


	public String getFdExpressName() {
		return fdExpressName;
	}


	public void setFdExpressName(String fdExpressName) {
		this.fdExpressName = fdExpressName;
	}


	public String getFdExpressNo() {
		return fdExpressNo;
	}


	public void setFdExpressNo(String fdExpressNo) {
		this.fdExpressNo = fdExpressNo;
	}


	public String getFdOrderDetail() {
		return fdOrderDetail;
	}


	public void setFdOrderDetail(String fdOrderDetail) {
		this.fdOrderDetail = fdOrderDetail;
	}


	public String getFdRemark() {
		return fdRemark;
	}


	public void setFdRemark(String fdRemark) {
		this.fdRemark = fdRemark;
	}


	public String getFdCreateTime() {
		return fdCreateTime;
	}


	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}


	public String getFdPayTime() {
		return fdPayTime;
	}


	public void setFdPayTime(String fdPayTime) {
		this.fdPayTime = fdPayTime;
	}


	public String getFdStatus() {
		return fdStatus;
	}


	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}


	public String getFdIp() {
		return fdIp;
	}


	public void setFdIp(String fdIp) {
		this.fdIp = fdIp;
	}


	public String getFdOpenId() {
		return fdOpenId;
	}


	public void setFdOpenId(String fdOpenId) {
		this.fdOpenId = fdOpenId;
	}


	public String getFdUniqueCode() {
		return fdUniqueCode;
	}


	public void setFdUniqueCode(String fdUniqueCode) {
		this.fdUniqueCode = fdUniqueCode;
	}


	public String getFdTransactionNo() {
		return fdTransactionNo;
	}


	public void setFdTransactionNo(String fdTransactionNo) {
		this.fdTransactionNo = fdTransactionNo;
	}


	public String getFdChargeId() {
		return fdChargeId;
	}


	public void setFdChargeId(String fdChargeId) {
		this.fdChargeId = fdChargeId;
	}


	public String getFdRefundId() {
		return fdRefundId;
	}


	public void setFdRefundId(String fdRefundId) {
		this.fdRefundId = fdRefundId;
	}


	public String getFdApplyRefundTime() {
		return fdApplyRefundTime;
	}


	public void setFdApplyRefundTime(String fdApplyRefundTime) {
		this.fdApplyRefundTime = fdApplyRefundTime;
	}


	public String getFdApplyRefundReason() {
		return fdApplyRefundReason;
	}


	public void setFdApplyRefundReason(String fdApplyRefundReason) {
		this.fdApplyRefundReason = fdApplyRefundReason;
	}


	public String getFdAgreeApplyTime() {
		return fdAgreeApplyTime;
	}


	public void setFdAgreeApplyTime(String fdAgreeApplyTime) {
		this.fdAgreeApplyTime = fdAgreeApplyTime;
	}


	public String getFdReceiveTime() {
		return fdReceiveTime;
	}


	public void setFdReceiveTime(String fdReceiveTime) {
		this.fdReceiveTime = fdReceiveTime;
	}


	public String getFdDeliverTime() {
		return fdDeliverTime;
	}


	public void setFdDeliverTime(String fdDeliverTime) {
		this.fdDeliverTime = fdDeliverTime;
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


	public String getFdShopPicUrl() {
		return fdShopPicUrl;
	}


	public void setFdShopPicUrl(String fdShopPicUrl) {
		this.fdShopPicUrl = fdShopPicUrl;
	}


	public Set<AppGoodsOrderItem> getAppGoodsOrderItems() {
		return appGoodsOrderItems;
	}


	public void setAppGoodsOrderItems(Set<AppGoodsOrderItem> appGoodsOrderItems) {
		this.appGoodsOrderItems = appGoodsOrderItems;
	}
}
