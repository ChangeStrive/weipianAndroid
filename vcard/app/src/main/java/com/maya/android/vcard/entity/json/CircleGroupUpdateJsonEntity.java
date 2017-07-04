package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 群组 修改实体类
 * @author zheng_cz
 * @since 2014年4月8日 下午5:05:12
 */
public class CircleGroupUpdateJsonEntity {
	/** 群组id  */
	@SerializedName("messageGroupId")
	private long groupId;
	
	/**  群组名称  */
	@SerializedName("groupName")
	private String groupName;
	
	/**  是否发送推送消息  */
	@SerializedName("hasSendPushMsg")
	private boolean sendPushMsg;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isSendPushMsg() {
		return sendPushMsg;
	}

	public void setSendPushMsg(boolean sendPushMsg) {
		this.sendPushMsg = sendPushMsg;
	}
}
