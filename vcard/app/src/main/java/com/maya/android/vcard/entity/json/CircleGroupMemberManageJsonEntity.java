package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 添加 、 删除 讨论组成员 实体类
 * @author zheng_cz
 * @since 2014年4月8日 下午5:36:39
 */
public class CircleGroupMemberManageJsonEntity {
	/** 群聊组id **/
	@SerializedName("messageGroupId")
	private long groupId;
	
	/** 成员列表  **/
	@SerializedName("memberList")
	private ArrayList<CircleGroupMemberJsonEntity> memberList;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public ArrayList<CircleGroupMemberJsonEntity> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<CircleGroupMemberJsonEntity> memberList) {
		this.memberList = memberList;
	}
}
