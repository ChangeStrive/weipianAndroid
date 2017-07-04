package com.platform.sys.model;

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
 * 流水号管理
 * @author Administrator
 *
 */
@Entity
@Table(name = "sys_flow_num")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"}) 
public class SysFlowNum {

	/**
	 * 主键
	 */
	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 流水号的数值
	 */
	@Column(length = 100)
	private Integer fdNum;
	
	/**
	 * 流水号 
	 */
	@Column(length = 100)
	private String fdFlowNum;
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns({ @JoinColumn(name = "FLOW_NUM_RULE_ID", nullable = false, insertable = true, updatable = true) })
	private SysFlowNumRule sysFlowNumRule;
	
	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
	}

	public Integer getFdNum() {
		return fdNum;
	}

	public void setFdNum(Integer fdNum) {
		this.fdNum = fdNum;
	}

	public String getFdFlowNum() {
		return fdFlowNum;
	}

	public void setFdFlowNum(String fdFlowNum) {
		this.fdFlowNum = fdFlowNum;
	}

	public SysFlowNumRule getSysFlowNumRule() {
		return sysFlowNumRule;
	}

	public void setSysFlowNumRule(SysFlowNumRule sysFlowNumRule) {
		this.sysFlowNumRule = sysFlowNumRule;
	}
}
