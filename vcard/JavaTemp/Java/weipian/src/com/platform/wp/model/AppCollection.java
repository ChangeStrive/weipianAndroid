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
*
*商品收藏
*/
@Entity
@Table(name = "app_collection")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppCollection {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 用户id
	 * 
	 */
	@Column(length = 32)
	private String fdUserId;
	
	/**
	 * 商品id
	 */
	@Column(length = 32)
	private String fdGoodsId;
	
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

	public String getFdGoodsId() {
		return fdGoodsId;
	}

	public void setFdGoodsId(String fdGoodsId) {
		this.fdGoodsId = fdGoodsId;
	}

	public String getFdCreateTime() {
		return fdCreateTime;
	}

	public void setFdCreateTime(String fdCreateTime) {
		this.fdCreateTime = fdCreateTime;
	}

}
