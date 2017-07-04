package com.maya.android.vcard.entity.result;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.constant.DatabaseConstant;
import com.maya.android.vcard.data.CurrentUser;

/**
 * 聊天组 信息
 * @author zheng_cz
 * @since 2014年3月31日 上午11:37:11
 */
public class CircleGroupResultEntity {
    /**
	 * 圈子 群组id
	 */
	@SerializedName("groupId")
	private long groupId;
	/**
	 * 群组 圈子名称
	 */
	@SerializedName("groupName")
	private String groupName;
	/**
	 * 创建时间
	 */
	@SerializedName("createTime")
	private String createTime;
	/**
	 * 成员数量
	 */
	@SerializedName("memberCount")
	private int memberCount;
	/**创建者帐户id **/
	@SerializedName("accountId")
	private long accountId;
	/**创建者名片id **/
	@SerializedName("cardId")
	private long cardId;
	/** 创建者姓名 **/
	@SerializedName("creater")
	private String displayName;
	/**
	 * 圈子 群组类型
	 * 10-群组
	 * 11-自定义圈子
	 * 12-企业圈
	 */
	private int type = DatabaseConstant.CircleGroupType.GROUP;
	/** 备注  */
	@SerializedName("remark")
	private String remark;
	/** 圈子头像  */
	private String headImg;

	/** 已请求成员次数  */
	@SerializedName("hadRequestMember")
	private int hadRequestMember = 0;
	
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(int memberCount) {
		this.memberCount = memberCount;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	/**
	 *  获取会话 的标题
	 * @return
	 */
	public String getSessionTitle(){
		String groupName = getGroupName();
		if(Helper.isEmpty(groupName)){
			groupName = ActivityHelper.getGlobalApplicationContext().getString(R.string.group_chat);
		}
		return groupName + "(" + memberCount +"人)";
	}
	/**
	 * 获取聊天组会话的显示名称
	 * @return
	 */
	public String getGroupItemShowName(){
		String showName = null;
		if(this.type == DatabaseConstant.CircleGroupType.CIRCLE_COMPANY){
			showName = this.groupName;
		}else{
			Context context = ActivityHelper.getGlobalApplicationContext();
			if(Helper.isEmpty(this.groupName) || this.groupName.equals(context.getString(R.string.group_chat)) ){
				if(Helper.isNotNull(CurrentUser.getInstance().getUserInfoEntity())){
					if(this.accountId == CurrentUser.getInstance().getUserInfoEntity().getId()){
						showName = context.getString(R.string.you_invite_who_join_group_chat, this.remark);
					}else{
						showName = context.getString(R.string.who_invite_who_join_group_chat, this.displayName, this.remark);
					}
				}
			}else{
				showName = this.groupName;
			}
		}
		return showName;
	}
	public int getHadRequestMember() {
		return hadRequestMember;
	}
	public void setHadRequestMember(int hadRequestMember) {
		this.hadRequestMember = hadRequestMember;
	}
}
