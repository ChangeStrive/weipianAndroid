package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建聊天组的实体对象类
 * @author zheng_cz
 * @since 2014年3月31日 下午2:16:53
 */
public class CircleGroupJsonEntity {
	/** 创建人 名片id  */
	@SerializedName("cardId")
	private long cardId;
	/** 群聊成员列表  */
	@SerializedName("memberList")
	private ArrayList<CircleGroupMemberJsonEntity> memberList;
	/** 单位键值id*/
	@SerializedName("enterpriseId")
	private long enterpriseId;
	
	public CircleGroupJsonEntity(long cardId, long enterpriseId ){
		this.cardId = cardId;
		this.enterpriseId = enterpriseId;
	}
	
	public CircleGroupJsonEntity(long cardId, ArrayList<CircleGroupMemberJsonEntity> memberList){
		this.cardId = cardId;
		this.memberList = memberList;
	}
	
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public ArrayList<CircleGroupMemberJsonEntity> getMemberList() {
		return memberList;
	}
	public void setMemberList(ArrayList<CircleGroupMemberJsonEntity> memberList) {
		this.memberList = memberList;
	}
	public long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
}
