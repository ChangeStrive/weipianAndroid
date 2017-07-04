package com.platform.wp.model;

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
 * 商品相册
 *
 */
@Entity
@Table(name = "app_goods_pic")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoodsPic {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	
	/**
	 * 图片
	 */
	@Column(length = 100)
	private String fdPicUrl;
	
	
	private Integer fdSeqNo;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "goodsId", nullable = false, insertable = true, updatable = true) })
	private AppGoods appGoods;


	public String getFdId() {
		return fdId;
	}


	public void setFdId(String fdId) {
		this.fdId = fdId;
	}


	public Integer getFdSeqNo() {
		return fdSeqNo;
	}


	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}


	public String getFdCreateTime() {
		return fdCreateTime;
	}


	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

	public String getFdPicUrl() {
		return fdPicUrl;
	}


	public void setFdPicUrl(String fdPicUrl) {
		this.fdPicUrl = fdPicUrl;
	}


	public AppGoods getAppGoods() {
		return appGoods;
	}


	public void setAppGoods(AppGoods appGoods) {
		this.appGoods = appGoods;
	}

	
	
}
