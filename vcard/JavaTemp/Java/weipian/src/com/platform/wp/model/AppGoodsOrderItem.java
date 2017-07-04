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
 * 订单
 *
 */
@Entity
@Table(name = "app_goods_order_item")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoodsOrderItem {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 商品id
	 */
	@Column(length = 32)
	private String fdGoodsId;
	
	/**
	 * 规格
	 */
	@Column(length = 100)
	private String fdGoodsNo;
	
	
	/**
	 * 商品名称
	 */
	@Column(length = 50)
	private String fdGoodsName;
	
	/**
	 * 图片（主图）
	 */
	@Column(length = 100)
	private String fdPicUrl;
	
	/**
	 * 数量
	 */
	private Integer fdQuantity;

	
	/**
	 * 吊牌价
	 */
	private BigDecimal fdTagPrice;
	
	/**
	 * 销售价
	 */
	private BigDecimal fdPrice;
	
	/**
	 * 合计
	 */
	private BigDecimal fdAmount;
	
	/**
	 * 顺序
	 */
	private Integer fdSeqNo;
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "orderId", nullable = false, insertable = true, updatable = true) })
	private AppGoodsOrder appGoodsOrder;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdGoodsId() {
		return fdGoodsId;
	}

	public void setFdGoodsId(String fdGoodsId) {
		this.fdGoodsId = fdGoodsId;
	}

	public String getFdGoodsNo() {
		return fdGoodsNo;
	}

	public void setFdGoodsNo(String fdGoodsNo) {
		this.fdGoodsNo = fdGoodsNo;
	}

	public String getFdGoodsName() {
		return fdGoodsName;
	}

	public void setFdGoodsName(String fdGoodsName) {
		this.fdGoodsName = fdGoodsName;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}

	public Integer getFdQuantity() {
		return fdQuantity;
	}

	public void setFdQuantity(Integer fdQuantity) {
		this.fdQuantity = fdQuantity;
	}

	public BigDecimal getFdTagPrice() {
		return fdTagPrice;
	}

	public void setFdTagPrice(BigDecimal fdTagPrice) {
		this.fdTagPrice = fdTagPrice;
	}

	public BigDecimal getFdPrice() {
		return fdPrice;
	}

	public void setFdPrice(BigDecimal fdPrice) {
		this.fdPrice = fdPrice;
	}

	public BigDecimal getFdAmount() {
		return fdAmount;
	}

	public void setFdAmount(BigDecimal fdAmount) {
		this.fdAmount = fdAmount;
	}

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}

	public AppGoodsOrder getAppGoodsOrder() {
		return appGoodsOrder;
	}

	public void setAppGoodsOrder(AppGoodsOrder appGoodsOrder) {
		this.appGoodsOrder = appGoodsOrder;
	}
}
