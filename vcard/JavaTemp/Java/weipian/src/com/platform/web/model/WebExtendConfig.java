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
 * 扩展配置
 * @author Administrator
 *
 */
@Entity
@Table(name = "web_extend_config")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"})
public class WebExtendConfig{
	
	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 名称
	 */
	@Column(length = 100)
	private String fdName;
	
	/**
	 * 代码标示符
	 */
	@Column(length = 100)
	private String fdKey;
	
	/**
	 * 值
	 */
	@Column(length = 100)
	private String fdValue;

	/**
	 * 顺序
	 */
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

	public String getFdKey() {
		return fdKey;
	}

	public void setFdKey(String fdKey) {
		this.fdKey = fdKey;
	}

	public String getFdValue() {
		return fdValue;
	}

	public void setFdValue(String fdValue) {
		this.fdValue = fdValue;
	}

	public Integer getFdSeqNo() {
		return fdSeqNo;
	}

	public void setFdSeqNo(Integer fdSeqNo) {
		this.fdSeqNo = fdSeqNo;
	}
}
