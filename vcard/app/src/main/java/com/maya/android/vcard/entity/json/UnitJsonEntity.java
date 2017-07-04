package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 企业信息编辑实体类
 * @author zheng_cz
 * @since 2014年4月10日 上午9:53:31
 */
public class UnitJsonEntity {
	@SerializedName("enterpriseId")
	protected Long enterpriseId;
	/** 地址 */
	@SerializedName("address")
	protected String address;
	/** 分类标签  (没修改传-1)*/
	@SerializedName("classLabel")
	protected Integer classLabel;
	/** 企业简介  */
	@SerializedName("enterpriseAbout")
	protected String enterpriseAbout;
	/** 公告 */
	@SerializedName("announcement")	
	protected String announcement;
	/** 单位网址 **/
	@SerializedName("enterpriseWeb")
	protected String enterpriseWeb;
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getClassLabel() {
		return classLabel;
	}
	public void setClassLabel(Integer classLabel) {
		this.classLabel = classLabel;
	}
	public String getEnterpriseAbout() {
		return enterpriseAbout;
	}
	public void setEnterpriseAbout(String enterpriseAbout) {
		this.enterpriseAbout = enterpriseAbout;
	}
	public String getAnnouncement() {
		return announcement;
	}
	public void setAnnouncement(String announcement) {
		this.announcement = announcement;
	}
	public String getEnterpriseWeb() {
		return enterpriseWeb;
	}
	public void setEnterpriseWeb(String web) {
		this.enterpriseWeb = web;
	}
}
