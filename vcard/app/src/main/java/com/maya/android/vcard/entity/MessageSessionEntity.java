package com.maya.android.vcard.entity;

import com.google.gson.annotations.SerializedName;
import com.maya.android.utils.ActivityHelper;
import com.maya.android.utils.Helper;
import com.maya.android.vcard.R;
import com.maya.android.vcard.dao.CircleDao;
import com.maya.android.vcard.entity.result.CircleGroupResultEntity;
import com.maya.android.vcard.entity.result.MessageResultEntity;
import com.maya.android.vcard.util.ResourceHelper;

/**
 * 消息会话实体
 * @author zheng_cz
 * @since 2014年3月29日 下午2:31:52
 */
public class MessageSessionEntity extends MessageResultEntity implements Cloneable{
	private long id;
	/** 我的名片id **/
	@SerializedName("myCardId")
	private long myCardId;
	/**
	 * 已读/未读状态
	 * 0-未读
	 * 1-已读
	 */
	@SerializedName("read")
	private int read;
	/**
	 * 发送类型
	 * 0-表示已接收
	 * 1-表示已发送
	 */
	@SerializedName("sendType")
	private int sendType;
	
	/**
	 * 是否发送失败
	 * true:表示发送失败
	 * false:表示发送成功
	 */
	@SerializedName("isSendFail")
	private boolean isSendFail;
	/** 文件的本地路径 */
	@SerializedName("filePath")
	private String filePath;
	/** 是否可操作  0-可以 1-不可以 **/
	@SerializedName("enable")
	private int enable;
	/** 是否黑名单 消息 */
	@SerializedName("isBlack")
	private boolean isBlack;
	/** 当前帐户未读消息数量 (显示用) */
	@SerializedName("unreadCount")
	private int unreadCount;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMyCardId() {
		return myCardId;
	}
	public void setMyCardId(long myCardId) {
		this.myCardId = myCardId;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	public boolean isSendFail() {
		return isSendFail;
	}
	public void setSendFail(boolean isSendFail) {
		this.isSendFail = isSendFail;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public boolean isBlack() {
		return isBlack;
	}
	public void setBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}
	public int getUnreadCount() {
		return unreadCount;
	}
	public void setUnreadCount(int unreadCount) {
		this.unreadCount = unreadCount;
	}
	/**
	 * 获取会话组  显示的 名称 
	 * @return
	 */
	public String getListShowName(){
		if(getTagId() > 0){
			CircleGroupResultEntity group = CircleDao.getInstance().getEntity(getTagId());
			if(Helper.isNotNull(group)){
				String showName = group.getGroupItemShowName();
				if(ResourceHelper.isNotEmpty(showName)){
					return showName;
				}else{
					//群聊组名称如果为空 则显示群聊Id
					return ""+ group.getGroupId();
				}
			}
		}else{
			if(ResourceHelper.isEmpty(getFromName())){
				return ActivityHelper.getGlobalApplicationContext().getString(R.string.stranger);
			}
		}
		return getFromName();
	}

}
