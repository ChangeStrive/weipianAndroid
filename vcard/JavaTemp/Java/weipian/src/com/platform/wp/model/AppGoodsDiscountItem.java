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
 * 
 * 商品折扣
 */
@Entity
@Table(name = "app_goods_discount_item")
@JsonAutoDetect
@JsonIgnoreProperties(value = { "hibernateLazyInitializer" })
public class AppGoodsDiscountItem {

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;

	
	/**
	 * 折扣
	 */
	private BigDecimal fdDiscount;

	/**
	 * 原价
	 */
	private BigDecimal fdPrice;

	/**
	 * 折后价
	 */
	private BigDecimal fdDiscountPrice;

	/**
	 * 顺序
	 */
	private Integer fdSeqNo;

	/**
	 * 1表示启用 0表示禁用
	 */
	@Column(length = 10)
	private String fdStatus;

	@Column(length = 32)
	private String fdGoodsId;
	
	@Column(length = 100)
	private String fdGoodsNo;
	
	/**
	 * 商品图片
	 */
	@Column(length = 100)
	private String fdImageUrl;
	

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "discountId", nullable = false, insertable = true, updatable = true) })
	private AppGoodsDiscount appGoodsDiscount;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "goods", nullable = false, insertable = true, updatable = true) })
	private AppGoods appGoods;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public BigDecimal getFdDiscount() {
		return fdDiscount;
	}

	public void setFdDiscount(BigDecimal fdDiscount) {
		this.fdDiscount = fdDiscount;
	}

	public BigDecimal getFdPrice() {
		return fdPrice;
	}

	public void setFdPrice(BigDecimal fdPrice) {
		this.fdPrice = fdPrice;
	}

	public BigDecimal getFdDiscountPrice() {
		return fdDiscountPrice;
	}

	public void setFdDiscountPrice(BigDecimal fdDiscountPrice) {
		this.fdDiscountPrice = fdDiscountPrice;
	}

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
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

	public String getFdImageUrl() {
		return fdImageUrl;
	}

	public void setFdImageUrl(String fdImageUrl) {
		this.fdImageUrl = fdImageUrl;
	}

	public AppGoodsDiscount getAppGoodsDiscount() {
		return appGoodsDiscount;
	}

	public void setAppGoodsDiscount(AppGoodsDiscount appGoodsDiscount) {
		this.appGoodsDiscount = appGoodsDiscount;
	}

	public AppGoods getAppGoods() {
		return appGoods;
	}

	public void setAppGoods(AppGoods appGoods) {
		this.appGoods = appGoods;
	}
	
}
