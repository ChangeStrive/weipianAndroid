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
 * 商品评论
 *
 */
@Entity
@Table(name = "app_goods_reply")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoodsReply {


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
	 * 商品规格
	 */
	@Column(length = 100)
	private String fdGoodsNo;
	
	/**
	 * 商品名称
	 */
	@Column(length = 100)
	private String fdGoodsName;
	
	/**
	 * 商品图片
	 */
	@Column(length = 100)
	private String fdGoodsPicUrl;
	
	/**
	 * 用户id
	 */
	@Column(length = 32)
	private String fdUserId;
	
	/**
	 * 用户账号
	 */
	@Column(length = 32)
	private String fdUserCode;
	
	/**
	 * 用户名
	 */
	@Column(length = 100)
	private String fdUserName;
	
	/**
	 * 用户头像
	 */
	@Column(length = 200)
	private String fdUserPicUrl;
	
	/**
	 * 评论内容
	 */
	@Column(length =400)
	private String fdReplyContent;
	
	/**
	 * 评论时间
	 */
	@Column(length = 50)
	private String fdReplyTime;

	/**
	 * 订单id
	 */
	@Column(length = 32)
	private String fdOrderId;
	
	/**
	 * 订单明细id
	 */
	@Column(length = 32,unique=true)
	private String fdOrderItemId;

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

	public String getFdGoodsPicUrl() {
		return fdGoodsPicUrl;
	}

	public void setFdGoodsPicUrl(String fdGoodsPicUrl) {
		this.fdGoodsPicUrl = fdGoodsPicUrl;
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

	public String getFdUserPicUrl() {
		return fdUserPicUrl;
	}

	public void setFdUserPicUrl(String fdUserPicUrl) {
		this.fdUserPicUrl = fdUserPicUrl;
	}

	public String getFdReplyContent() {
		return fdReplyContent;
	}

	public void setFdReplyContent(String fdReplyContent) {
		this.fdReplyContent = fdReplyContent;
	}

	public String getFdReplyTime() {
		return fdReplyTime;
	}

	public void setFdReplyTime(String fdReplyTime) {
		this.fdReplyTime = fdReplyTime;
	}

	public String getFdOrderId() {
		return fdOrderId;
	}

	public void setFdOrderId(String fdOrderId) {
		this.fdOrderId = fdOrderId;
	}

	public String getFdOrderItemId() {
		return fdOrderItemId;
	}

	public void setFdOrderItemId(String fdOrderItemId) {
		this.fdOrderItemId = fdOrderItemId;
	}
}
