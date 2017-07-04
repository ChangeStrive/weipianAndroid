package com.platform.sys.model;

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
 * 流失号规则管理
 * @author suyulong
 *
 */
@Entity
@Table(name = "sys_flow_num_rule")
@JsonAutoDetect
@JsonIgnoreProperties (value={"hibernateLazyInitializer"}) 
public class SysFlowNumRule {

	@Id @GeneratedValue(generator="idGenerator")
	@GenericGenerator(name="idGenerator", strategy = "uuid")
	@Column(length = 32)
	private String fdId;
	
	/**
	 * 类名
	 */
	@Column(length = 100)
	private String fdClassName;
	
	/**
	 * 备注
	 */
	@Column(length = 200)
	private String fdRemark;
	
	/**
	 * 规则
	 */
	@Column(length = 100)
	private String fdRule;
	
	/**
	 * 流失号起始值
	 */
	@Column(length = 100)
	private Integer fdStartValue;

	@OneToMany(mappedBy = "sysFlowNumRule", fetch=FetchType.LAZY)
	private Set<SysFlowNum> sysFlowNums= new HashSet<SysFlowNum>(0);
	
	public String getFdId() {
		return fdId;
	}
	public void setFdId(String fdId) {
		this.fdId = fdId;
	}
	

	public String getFdClassName() {
		return fdClassName;
	}
	
	public void setFdClassName(String fdClassName) {
		this.fdClassName = fdClassName;
	}

	public String getFdRule() {
		return fdRule;
	}

	public void setFdRule(String fdRule) {
		this.fdRule = fdRule;
	}

	
	public String getFdRemark() {
		return fdRemark;
	}

	public void setFdRemark(String fdRemark) {
		this.fdRemark = fdRemark;
	}

	public Integer getFdStartValue() {
		return fdStartValue;
	}

	public void setFdStartValue(Integer fdStartValue) {
		this.fdStartValue = fdStartValue;
	}

	
	public Set<SysFlowNum> getSysFlowNums() {
		return sysFlowNums;
	}

	public void setSysFlowNums(Set<SysFlowNum> sysFlowNums) {
		this.sysFlowNums = sysFlowNums;
	}
	
	
}
