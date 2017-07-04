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
 * 商品类型
 * @author Administrator
 *
 */
@Entity
@Table(name = "app_goods_type")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class AppGoodsType {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	
	/**
	 * 分类名称
	 */
	@Column(length = 100)
	private String fdTypeName;
	
	/**
	 * 顺序
	 */
	private Integer fdSeqNo;
	
	/**
	 * 创建时间
	 */
	@Column(length = 50)
	private String fdCreateTime;
	
	/**
	 * 父ID
	 */
	@Column(length = 32)
	private String fdPid;
	
	/**
	 * 上级菜单名称
	 */
	@Column(length = 100)
	private String fdPidName;
	
	/**
	 * 层级  1表示一级菜单 2表示二级菜单
	 */
	@Column(length = 10)
	private Integer fdLevel;

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}


	public String getFdTypeName() {
		return fdTypeName;
	}

	public void setFdTypeName(String fdTypeName) {
		this.fdTypeName = fdTypeName;
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

	public String getFdPid() {
		return fdPid;
	}

	public void setFdPid(String fdPid) {
		this.fdPid = fdPid;
	}

	public String getFdPidName() {
		return fdPidName;
	}

	public void setFdPidName(String fdPidName) {
		this.fdPidName = fdPidName;
	}

	public Integer getFdLevel() {
		return fdLevel;
	}

	public void setFdLevel(Integer fdLevel) {
		this.fdLevel = fdLevel;
	}
	
	
	

}
