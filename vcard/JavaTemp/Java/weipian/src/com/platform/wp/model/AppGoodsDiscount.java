package com.platform.wp.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
*
*商品折扣
*/
@Entity
@Table(name = "app_goods_discount")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoodsDiscount {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 商品活动
	 */
	private String fdGoodsActive;
	
	/**
	 * 开始时间
	 */
	@Column(length = 50)
	private String fdStartTime;
	
	/**
	 * 结束时间
	 */
	@Column(length = 50)
	private String fdEndTime;
	
	/**
	 * 顺序
	 */
	private Integer fdSeqNo;
	
	
	/**
	 * 1表示启用  0表示禁用
	 */
	@Column(length = 10)
	private String fdStatus;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	@OneToMany(mappedBy = "appGoodsDiscount", fetch=FetchType.LAZY)
	private Set<AppGoodsDiscountItem> appGoodsDiscountItems=new HashSet(0);

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdGoodsActive() {
		return fdGoodsActive;
	}

	public void setFdGoodsActive(String fdGoodsActive) {
		this.fdGoodsActive = fdGoodsActive;
	}

	public String getFdStartTime() {
		return fdStartTime;
	}

	public void setFdStartTime(String fdStartTime) {
		this.fdStartTime = fdStartTime;
	}

	public String getFdEndTime() {
		return fdEndTime;
	}

	public void setFdEndTime(String fdEndTime) {
		this.fdEndTime = fdEndTime;
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

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public Set<AppGoodsDiscountItem> getAppGoodsDiscountItems() {
		return appGoodsDiscountItems;
	}

	public void setAppGoodsDiscountItems(
			Set<AppGoodsDiscountItem> appGoodsDiscountItems) {
		this.appGoodsDiscountItems = appGoodsDiscountItems;
	}
}
