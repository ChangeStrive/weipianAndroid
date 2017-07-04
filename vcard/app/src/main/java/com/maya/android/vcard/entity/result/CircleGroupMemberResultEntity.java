package com.maya.android.vcard.entity.result;

import com.google.gson.annotations.SerializedName;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.entity.json.CircleGroupMemberJsonEntity;

/**
 * 群聊组成员
 * @author zheng_cz
 * @since 2014年3月31日 上午11:29:34
 */
public class CircleGroupMemberResultEntity {

	/** 成员id  */
	@SerializedName("id")
	private long memberId;
	/**  圈子 群组id */
	@SerializedName("messageGroupId")
	private long groupId;
	/** 成员名片id */
	@SerializedName("cardId")
	private long cardId;
	/** 成员帐户id */
	@SerializedName("accountId")
	private long accountId;
	/**
	 * 成员身份 
	 * 2-创建人
	 * 0-普通成员
	 */
	@SerializedName("memberRole")
	private int role;
	/** 头像  */
	@SerializedName("headImg")
	private String headImg;
	
	/** 名字  */
	@SerializedName("firstName")
	private String firstName;
	/**  姓氏  */
	@SerializedName("surname")
	private String surname;
	/** 昵称  */
	@SerializedName("nickName")
	private String nickName;
	/**  成员名称 
	 */
	private String displayName;
	
	
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public long getGroupId() {
		return groupId;
	}
	public void setGroupId(long groupId) {
		this.groupId = groupId;
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
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getDisplayName() {
		if(Helper.isEmpty(displayName)){
			displayName = getFullName();
		}
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	/**
	 * 获取全称
	 * @return
	 */
	public String getFullName(){
		String disName = "";
		if(Helper.isNotEmpty(surname)){
			disName += surname;
		}
		if(Helper.isNotEmpty(firstName)){
			disName += firstName;
		}
		return disName;
	}
	/**
	 * 获取请求提交的成员信息
	 * @return
	 */
	public CircleGroupMemberJsonEntity getCircleGroupMemberJsonEntity(){
		return new CircleGroupMemberJsonEntity(cardId, accountId, groupId, memberId);
		
	}
}
