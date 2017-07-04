package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.vcard.entity.json.UnitJsonEntity;

/**
 * 企业信息实体
 * @author zheng_cz
 * @since 2014年4月10日 上午9:52:07
 */
public class UnitResultEntity extends UnitJsonEntity implements Cloneable{
	/** 企业头像墙  */
	@SerializedName("headImg")
	private String headImg;
	/**  等级  */
	@SerializedName("grade")
	private Integer grade;
	
	/** 企业名称  */
	@SerializedName("enterpriseName")
	private String enterpriseName;
	
	/**  创建时间 */
	@SerializedName("createTime")
	private String createTime;
	/** 创始人  */
	@SerializedName("creater")
	private String creater;
	/** 是否公开  */
	@SerializedName("isOpen")
    private boolean isOpen;
	/**
	 * 是否认证
	 * -1:未认证
	 * 0:未审核
	 * 1:已认证
	 */
	@SerializedName("approvalStatus")
	private int approval;
	
	/** 成员数量  */
	@SerializedName("memberCount")
	private int memberCount;
	/** 聊天组id **/
	@SerializedName("messageGroupId")
	private long messageGroupId;
	/** 已请求成员次数  */
	@SerializedName("hadRequestMember")
	private int hadRequestMember = 0;
	
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public int getApproval() {
		return approval;
	}
	public void setApproval(int approval) {
		this.approval = approval;
	}
	public int getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
	public long getMessageGroupId() {
		return messageGroupId;
	}
	public void setMessageGroupId(long messageGroupId) {
		this.messageGroupId = messageGroupId;
	}
	public int getHadRequestMember() {
		return hadRequestMember;
	}
	public void setHadRequestMember(int hadRequestMember) {
		this.hadRequestMember = hadRequestMember;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
