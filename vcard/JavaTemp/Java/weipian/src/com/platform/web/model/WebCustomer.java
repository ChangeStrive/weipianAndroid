package com.platform.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 客服
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_customer")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebCustomer {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 客服名称
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 客服账号
	 */
	private String fdNo;
	/**
	 * 客服类型
	 */
	@Column(length = 100)
	private String fdTypeId;
	
	/**
	 * 客服类型
	 */
	@Column(length = 100)
	private String fdTypeName;
	
	/**
	 * 顺序
	 */
	@Column(length = 11)
	private Integer fdSeqNo;

	
	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public String getFdName() {
		return fdName;
	}

	public void setFdName(String fdName) {
		this.fdName = fdName;
	}

	public String getFdNo() {
		return fdNo;
	}

	public void setFdNo(String fdNo) {
		this.fdNo = fdNo;
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

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}
}
