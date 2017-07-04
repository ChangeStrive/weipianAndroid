package com.platform.sys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * 字典表
 * 
 *
 */
@Entity
@Table(name = "sys_dict")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class SysDict {
	
	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 字典名称
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 字典值
	 */
	@Column(length = 50)
	private String fdValue;
	
	/**
	 * 父id 功能Id
	 */
	@Column(length = 32)
	private String fdFunId;
	
	/**
	 * 功能名称
	 */
	@Column(length = 200)
	private String fdFunName;
	
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

	public String getFdValue() {
		return fdValue;
	}

	public void setFdValue(String fdValue) {
		this.fdValue = fdValue;
	}

	public String getFdFunId() {
		return fdFunId;
	}

	public void setFdFunId(String fdFunId) {
		this.fdFunId = fdFunId;
	}

	public String getFdFunName() {
		return fdFunName;
	}

	public void setFdFunName(String fdFunName) {
		this.fdFunName = fdFunName;
	}

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}
}
