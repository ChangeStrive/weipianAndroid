package com.maya.android.vcard.entity.json;

import com.google.gson.annotations.SerializedName;

/**
 * 聊天组成员添加
 * @author zheng_cz
 * @since 2014年3月31日 下午2:00:36
 */
public class CircleGroupMemberJsonEntity implements Cloneable{
	/** 名片id **/
	@SerializedName("cardId")
	private long cardId;
	/** 帐户id **/
	@SerializedName("accountId")
	private long accountId;
	/** 讨论组id **/
	@SerializedName("messageGroupId")
	private long groupId;
	/** 成员id **/
	@SerializedName("id")
	private long memberId;
	
	public CircleGroupMemberJsonEntity(){
		
	}
	public CircleGroupMemberJsonEntity(long cardId, long accountId){
		this.cardId = cardId;
		this.accountId = accountId;
	}
	public CircleGroupMemberJsonEntity(long cardId, long accountId, long groupId, long memberId){
		this.cardId = cardId;
		this.accountId = accountId;
		this.groupId = groupId;
		this.memberId = memberId;
	}
	
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
