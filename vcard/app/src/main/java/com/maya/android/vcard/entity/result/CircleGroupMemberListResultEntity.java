package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 获取讨论组成员列表
 * @author zheng_cz
 * @since 2014年4月26日 上午10:49:19
 */
public class CircleGroupMemberListResultEntity {
	@SerializedName("memberList")
	private ArrayList<CircleGroupMemberResultEntity> memberList;

	public ArrayList<CircleGroupMemberResultEntity> getMemberList() {
		return memberList;
	}

	public void setMemberList(ArrayList<CircleGroupMemberResultEntity> memberList) {
		this.memberList = memberList;
	}
}
