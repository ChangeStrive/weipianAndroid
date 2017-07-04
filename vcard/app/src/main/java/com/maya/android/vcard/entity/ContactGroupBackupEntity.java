package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 备份恢复 联系人分组对象
 * @Version: 1.0
 * @author: zheng_cz 
 * @since: 2013-8-26 上午11:18:16 
 */
public class ContactGroupBackupEntity {
	/**
	 * 客户端id
	 */
	@SerializedName("syncGroupId")
	private int groupId;
	/**
	 * 分组名称
	 */
	@SerializedName("name")
	private String name;
	/**
	 * 排序值
	 */
	@SerializedName("orderCode")
	private int orderCode;
	/**
	 * 联系人数
	 */
	@SerializedName("groupContactCount")
	private int contactCount;
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int id) {
		this.groupId = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(int orderCode) {
		this.orderCode = orderCode;
	}
	public int getContactCount() {
		return contactCount;
	}
	public void setContactCount(int contactCount) {
		this.contactCount = contactCount;
	}
}
