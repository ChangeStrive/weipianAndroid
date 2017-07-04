package com.platform.wp.model;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 商品信息
 * @author Administrator
 *
 */
@Entity
@Table(name = "app_goods")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoods {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	
	/**
	 * 商品名称
	 */
	@Column(length = 100)
	private String fdGoodsName;
	
	
	/**
	 * 规格
	 */
	@Column(length = 100)
	private String fdGoodsNo;
	
	/**
	 * 图片（主图）
	 */
	@Column(length = 100)
	private String fdPicUrl;
	
	/**
	 * 商品类别id
	 */
	@Column(length = 100)
	private String fdTypeId;
	
	/**
	 * 商品类目名称
	 */
	@Column(length = 100)
	private String fdTypeName;
	
	/**
	 * 吊牌价
	 */
	private BigDecimal fdTagPrice;
	
	/**
	 * 销售价
	 */
	private BigDecimal fdPrice;
	
	/**
	 * 库存数量
	 */
	private Integer fdQuantity;
	
	/**
	 * 销量
	 */
	private Integer fdSaleQuantity;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 更新时间
	 */
	@Column(length = 50)
	private String fdUpdateTime;
	
	/**
	 * 商品描述
	 */
	@Lob 
	@Basic(fetch = FetchType.LAZY) 
	@Column(name="fdDesc", columnDefinition="TEXT", nullable=true)
	private String fdDesc;
	
	/**
	 * 商品状态 1表示上架 0表示下架
	 */
	@Column(length = 10)
	private String fdStatus;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdGoodsName() {
		return fdGoodsName;
	}

	public void setFdGoodsName(String fdGoodsName) {
		this.fdGoodsName = fdGoodsName;
	}

	public String getFdGoodsNo() {
		return fdGoodsNo;
	}

	public void setFdGoodsNo(String fdGoodsNo) {
		this.fdGoodsNo = fdGoodsNo;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}

	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}

	public String getFdTypeId() {
		return fdTypeId;
	}

	public void setFdTypeId(String fdTypeId) {
		this.fdTypeId = fdTypeId;
	}

	public String getFdTypeName() {
		return fdTypeName;
	}

	public void setFdTypeName(String fdTypeName) {
		this.fdTypeName = fdTypeName;
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

	public Integer getFdQuantity() {
		return fdQuantity;
	}

	public void setFdQuantity(Integer fdQuantity) {
		this.fdQuantity = fdQuantity;
	}

	public Integer getFdSaleQuantity() {
		return fdSaleQuantity;
	}

	public void setFdSaleQuantity(Integer fdSaleQuantity) {
		this.fdSaleQuantity = fdSaleQuantity;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public String getFdUpdateTime() {
		return fdUpdateTime;
	}

	public void setFdUpdateTime(String fdUpdateTime) {
		this.fdUpdateTime = fdUpdateTime;
	}

	public String getFdDesc() {
		return fdDesc;
	}

	public void setFdDesc(String fdDesc) {
		this.fdDesc = fdDesc;
	}

	public String getFdStatus() {
		return fdStatus;
	}

	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
	}
}
