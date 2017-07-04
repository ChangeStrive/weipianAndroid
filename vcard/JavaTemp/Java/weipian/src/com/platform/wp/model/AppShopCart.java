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
 * 购物车
 *
 */
@Entity
@Table(name = "app_shop_cart")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppShopCart {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 数量
	 */
	private Integer fdQuantity;
	
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
	 * 购买者id
	 */
	@Column(length = 32)
	private String fdUserId;
	
	/**
	 * 吊牌价
	 */
	private BigDecimal fdTagPrice;
	
	/**
	 * 销售价
	 */
	private BigDecimal fdPrice;

	
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


	public Integer getFdQuantity() {
		return fdQuantity;
	}


	public void setFdQuantity(Integer fdQuantity) {
		this.fdQuantity = fdQuantity;
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


	public String getFdUserId() {
		return fdUserId;
	}


	public void setFdUserId(String fdUserId) {
		this.fdUserId = fdUserId;
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


	public String getFdCreateTime() {
		return fdCreateTime;
	}


	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}
}
